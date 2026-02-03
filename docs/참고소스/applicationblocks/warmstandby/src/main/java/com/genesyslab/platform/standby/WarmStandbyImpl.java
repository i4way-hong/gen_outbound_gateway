/*
 * Copyright (c) 2006 - 2019 Genesys Telecommunications Laboratories, Inc.
 * All rights reserved.
 *
 * For more  information on the Genesys Telecommunications Laboratories, Inc.
 * please see <http://www.genesyslab.com/>.
 */
package com.genesyslab.platform.standby;

import java.security.SecureRandom;
import java.util.EventObject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

import com.genesyslab.platform.commons.log.ILogger;
import com.genesyslab.platform.commons.log.Log;
import com.genesyslab.platform.commons.protocol.ChannelClosedEvent;
import com.genesyslab.platform.commons.protocol.ChannelErrorEvent;
import com.genesyslab.platform.commons.protocol.ChannelListener;
import com.genesyslab.platform.commons.protocol.ChannelOpenedEvent;
import com.genesyslab.platform.commons.protocol.ChannelState;
import com.genesyslab.platform.commons.protocol.ClientChannel;
import com.genesyslab.platform.commons.protocol.ConnectionOperations;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.RegistrationException;
import com.genesyslab.platform.commons.protocol.runtime.ChannelNotificationThreadsControl;
import com.genesyslab.platform.commons.threading.AsyncInvoker;
import com.genesyslab.platform.commons.threading.CompletionHandler;
import com.genesyslab.platform.standby.WSConfig.ChangeListener;
import com.genesyslab.platform.standby.WSConfig.WSConfigOption;
import com.genesyslab.platform.standby.events.WSAllTriedUnsuccessfullyEvent;
import com.genesyslab.platform.standby.events.WSDisconnectedEvent;
import com.genesyslab.platform.standby.events.WSOpenedEvent;
import com.genesyslab.platform.standby.events.WSTriedUnsuccessfullyEvent;
import com.genesyslab.platform.standby.exceptions.WSCanceledException;
import com.genesyslab.platform.standby.exceptions.WSEmptyEndpointPoolException;
import com.genesyslab.platform.standby.exceptions.WSException;
import com.genesyslab.platform.standby.exceptions.WSNoAvailableServersException;


class WarmStandbyImpl implements ChangeListener, ChannelListener {

    private static final ILogger log = Log.getLogger(WarmStandbyImpl.class);

    final WarmStandby warmStandby;
    private final ClientChannel channel;
    private final WSConfig config;
    final Object guard;

    private volatile IWSHandler handler;
    private volatile boolean autoRestoreEnabled;
    volatile int stateCounter;
    
    final ConnectionOperations connectionOperations;

    private SecureRandom rnd = new SecureRandom();

    final AtomicReference<Thread> channelInvokerThreadRef = new AtomicReference<Thread>();

    private final ConcurrentLinkedQueue<Runnable> queue = new ConcurrentLinkedQueue<Runnable>();

    private volatile WSOpenFuture openFuture;

    private Endpoint lastOpenedEndpoint;
    private final Set<Endpoint> checkedEndpoints = new HashSet<Endpoint>();
    int retryNo;

//    private final AtomicInteger channelEventSkipCounter = new AtomicInteger();

    private final String name;
    private final String info;

    private volatile boolean paused;


    WarmStandbyImpl(
            final String name,
            final WarmStandby warmstandby,
            final ClientChannel channel) {

        this.info = "WarmStandby" + (name == null 
                ? (channel != null ? "[" + channel.getClass().getSimpleName() + "]" : "") 
                : "[" + name + "]"); 

        if (channel == null) {
            final IllegalArgumentException ex = new IllegalArgumentException(info + " null channel", null);
            if (log.isError()) {
                log.error(info + " channel is null.", ex);
            }
            throw ex;
        }

        {
            final Endpoint endpoint = channel.getEndpoint();
            if (endpoint != null) {
                if (log.isWarn()) {
                    log.warnFormat(info + "ignores protocol's endpoint.", endpoint);
                }
            }
        }

        this.name = name;
        this.warmStandby = warmstandby;
        this.channel = channel;
        this.guard = channel;
        this.config = new WSConfig(name, this);

        synchronized (guard) {
            final ChannelState state = channel.getState();
            if (state != ChannelState.Closed) {
                final IllegalArgumentException ex = new IllegalArgumentException(info + " channel isn't closed. state=" + state, null);
                if (log.isError()) {
                    log.error(info + " channel isn't closed.", ex);
                }
                throw ex;
            }

            this.connectionOperations = channel.disableConnectionOperations();

            openFuture = WSOpenFuture.createFailed(this);

            channel.addChannelListener(this);
        }

        if (log.isDebug()) {
            log.debug(info + ".created");
        }
    }

    @Override
    public String toString() {
        return info;
    }

    public ClientChannel getChannel() {
        return channel;
    }

    public boolean isOpened() {
        return channel.getState() == ChannelState.Opened;
    }

    public void open() throws InterruptedException, WSException {
        if (channelInvokerThreadRef.get() == Thread.currentThread()) {
            throw new RuntimeException(info + " methods open() and openAsync().get() can't be called inside the handler");
        }
        WSOpenFuture f;
        synchronized (guard) {
            initiateOpening();
            f = openFuture;
        }

        try {
            if (f == null) {
                throw new NullPointerException("openFutrue");
            }
            f.get();
        } catch (final CancellationException ex) {
            throw new WSCanceledException(info + " open operation has been canceled", null);
        } catch (final ExecutionException e) {
            if (e.getCause() instanceof RuntimeException) {
                throw (RuntimeException)e.getCause();
            }
            throw (WSException) e.getCause();
        }
    }

    public <A> void openAsync(final CompletionHandler<ChannelOpenedEvent, A> completionHandler, final A attachment) {
        synchronized (guard) {
            initiateOpening();
            WSOpenFuture f = openFuture;
            f.addCompletionHandler(completionHandler, attachment);
        }
    }

    public Future<ChannelOpenedEvent> openAsync() {
        synchronized (guard) {
            initiateOpening();
            return openFuture;
        }
    }

    public void close() throws InterruptedException {
        WSCloseFuture f;
        synchronized (guard) {
            initiateClosing();
            f = openFuture.getCloseFuture();
        }
        try {
            f.get();
        } catch (final ExecutionException e) {
            if (log.isError()) {
                log.error(info + " unexpected exception.", e.getCause());
            }
        }
    }

    public <A> void closeAsync(final CompletionHandler<Void, A> handler, final A attachment) {
        synchronized (guard) {
            openFuture.getCloseFuture().addCompletionHandler(handler, attachment);
            initiateClosing();
        }
    }

    public Future<Void> closeAsync() {
        synchronized (guard) {
            initiateClosing();
            return openFuture.getCloseFuture();
        }
    }

    private void initiateOpening() {

        paused = false;

        WSOpenFuture fOpen = openFuture;
        if (fOpen == null) {
            throw new NullPointerException("openFuture"); // silent code analyzer
        }

        if (!fOpen.isDone()) {
            return;
        }
        
        WSCloseFuture fClose = fOpen.getCloseFuture();
        if (fClose == null) {
            throw new NullPointerException("closeFuture"); // silent code analyzer
        }
        if (fClose.isDone() || fClose.isInProgress()) {
            retryNo = 0;
            lastOpenedEndpoint = null;
            checkedEndpoints.clear();
            createNewOpenContext();
        }
        else if (fOpen.isDone()) {
            return;
        }

        doOpening();
    }

    private void createNewOpenContext() {
        stateCounter++;
        openFuture = WSOpenFuture.create(this);
        if (log.isDebug()) {
            log.debug(getEventLogInfo("openFutureCreated", ""));
        }
    }

    private void initiateClosing() {

        paused = false;

        if (autoRestoreEnabled) {
            autoRestoreEnabled = false;
        }

        openFuture.failed(null);

        WSCloseFuture closeFuture = openFuture.getCloseFuture();
        if (closeFuture.isDone() || closeFuture.isInProgress()) {
            return;
        }
        if (log.isDebug()) {
            log.debug(getEventLogInfo("initiateClosing", ""));
        }
        
        stateCounter++;
        
        if (channel.getState() != ChannelState.Closed) {
            try {
                closeFuture.setFuture(connectionOperations.closeAsync());
            }
            finally{
            }
        }
        else {
            closeFuture.success(null);
        }
    }

    private void enableAutoRestore() {
        synchronized (guard) {
            if (!autoRestoreEnabled) {
                autoRestoreEnabled = true;
                if (log.isDebug()) {
                    log.debug(info + ".started");
                }
            }
        }
    }

    public void autoRestore(final boolean open) {
        enableAutoRestore();
        if (open) {
            openAsync();
        }
    }

    public WSConfig getConfig() {
        return config;
    }

    public void setConfig(final WSConfig config) {
        if (log.isDebug()) {
            log.debug(info + ".setConfiguration " + config );
        }
        this.config.apply(config);
    }

    public void setHandler(final IWSHandler handler) throws IllegalArgumentException {
        this.handler = handler;
    }

    
    String getEventLogInfo(final String eventName, final String eventInfo) {
        final StringBuilder sb = new StringBuilder();
        sb.append(info);
        sb.append('.');
        sb.append(eventName);
        sb.append(' ');
        if (eventInfo != null) {
            sb.append(eventInfo);
            sb.append(' ');
        }
        return sb.toString();
    }


    private WSException createEmptyPoolException() {
        return new WSEmptyEndpointPoolException(info + " endpoint pool is empty", null);
    }

    private WSNoAvailableServersException createNoAvailableServersException() {
        return new WSNoAvailableServersException(info + " No available servers", null);
    }


    private void scheduleTask(final Runnable task) {
        if (task == null) {
            throw new IllegalArgumentException("null task");
        }
        if (log.isDebug()) {
            log.debug(getEventLogInfo("scheduled", task.toString()));
        }
        queue.add(task);
        
        if (Thread.currentThread() != channelInvokerThreadRef.get()) {
            @SuppressWarnings("deprecation")
            final AsyncInvoker invoker = channel.getInvoker();
            invoker.invoke(new Runnable() {
                public void run() {
                    channelInvokerThreadRef.set(Thread.currentThread());
                    try
                    {
                        ChannelNotificationThreadsControl.markChannelNotificationThread(warmStandby.getChannel());
                        try {
                            processQueue();
                        } finally {
                            ChannelNotificationThreadsControl.unmarkChannelNotificationThread(warmStandby.getChannel());
                        }
                    }
                    finally {
                        channelInvokerThreadRef.set(null);
                    }
                }
            });
        }
    }

    private void executeTask(final Runnable task) {
        if (task == null) {
            throw new IllegalArgumentException(info + " null task");
        }
        task.run();
    }
    
    
    private void doOpening() {

        paused = false;

        final WSOpenFuture f = this.openFuture;

        Endpoint endpoint = peekFirstUncheckedEndpoint();
        if (endpoint != null) {
            if (log.isDebug()) {
                log.debug(getEventLogInfo("opening", WSConfig.endpointInfo(endpoint)));
            }
            int delay = 0;
            WSDelayType delayType = null;
            if (lastOpenedEndpoint != null) {
                if (endpoint.equals(lastOpenedEndpoint)) {
                    delay = 0;
                    delayType = WSDelayType.ReconnectionDelay;
                    int reconnectionRandomDelayRange = getConfig().getReconnectionRandomDelayRange();
                    if (reconnectionRandomDelayRange > 0) {
                        int r = rnd.nextInt(reconnectionRandomDelayRange);
                        if (r == 0) {
                            r = 1;
                        }
                        delay += r;
                    }
                } else if (checkedEndpoints.size() == 1) {
                    delay = getConfig().getBackupDelay();
                    delayType = WSDelayType.BackupDelay;
                }
            }
            if (delay == 0) {
                Integer timeout = config.getTimeout();
                Long openTimeout = timeout != null ? timeout.longValue() : null;
                final Endpoint finalEndpoint = endpoint;
                try {
                    f.setFuture(connectionOperations.openAsync(finalEndpoint, openTimeout));
                }
                catch(final Throwable ex) {
                    handleTriedUnsuccessfully(new ChannelClosedEvent(channel, ex, ChannelState.Opening, finalEndpoint));
                    return;
                }
            } else {
                final WSDelayType finalDelayType = delayType; 
                final int finalDelay = delay;
                f.schedule(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (guard) {
                            if (!f.isActual()) {
                                return;
                            }
                            final Endpoint endpoint = peekFirstUncheckedEndpoint();
                            if (endpoint != null) {
                                Integer timeout = config.getTimeout();
                                final Long openTimeout = timeout != null ? timeout.longValue() : null;
                                try {
                                    f.setFuture(connectionOperations.openAsync(endpoint, openTimeout));
                                }
                                catch(Throwable ex) {
                                    handleTriedUnsuccessfully(new ChannelClosedEvent(channel, ex, ChannelState.Opening, endpoint));
                                    return;
                                }
                            }
                            else {
                                handleAllTriedUnsuccessfully();
                            }
                        }
                    }
                    @Override
                    public String toString() {
                        return "ActionOpenAsync " + finalDelayType.name() + "=" + finalDelay;
                    }
                }, delay, delayType);
            }
            return;
        }
        
        handleAllTriedUnsuccessfully();
    }

    private void handleAllTriedUnsuccessfully() {
        
        final Throwable ex;
        if (isPoolEmpty()) {
            ex = createEmptyPoolException();
        } else {
            ex = createNoAvailableServersException();
        }

        openFuture.hintFailed(ex);
        
        scheduleTask(new Runnable() {

            WSOpenFuture f = openFuture; 
            final int retryNumber = retryNo;
            IWSHandler h = handler;
            
            @Override
            public String toString() {
                return "Task-handleAllTriedUnsuccessfully";
            }

            public void run() {
                if (log.isDebug()) {
                    log.debug(getEventLogInfo("allTriedUnsuccessfully", null));
                }
                
                if (h != null) {
                    try {
                        h.onAllEndpointsTriedUnsuccessfully(new WSAllTriedUnsuccessfullyEvent(warmStandby, retryNumber+1));
                    } catch (Throwable ex) {
                        if (log.isError()) {
                            log.error(info + " exception in handler "+h, ex);
                        }
                    }
                }
                
                synchronized (guard) {
                    if (!f.isActual()) {
                        return;
                    }

                    f.failed(ex);
                    
                    if (!f.isActual()) {
                        return;
                    }

                    lastOpenedEndpoint = null;
                    checkedEndpoints.clear();

                    if (isPoolEmpty()) {
                        paused = true;
                        return;
                    }
                    
                    if (!autoRestoreEnabled) {
                        f.getCloseFuture().success(null);
                        return;
                    }


                    createNewOpenContext();
                    
                    f = openFuture;
                    
                    int delay = config.getRetryDelay(retryNo++);
                    
                    if (delay == 0) {
                        doOpening();
                    }
                    else {
                        final int finalDelay = delay;
                        final WSOpenFuture finalFuture = f;
                        f.schedule(new Runnable() {
                            @Override
                            public void run() {
                                synchronized (guard) {
                                    if (!finalFuture.isActual()) {
                                        return;
                                    }
                                    doOpening();
                                }
                            }
                            @Override
                            public String toString() {
                                return "ActionDoOpening retryDelay=" + finalDelay;
                            }
                        }, delay, WSDelayType.RetryDelay);
                    }
                }
                
            }
        });
    }

    private boolean isPoolEmpty() {
        return getConfig().getEndpoints().isEmpty();
    }


    
    void handleOpenSuccess(final WSOpenFuture f, final ChannelOpenedEvent event) {
        scheduleTask(new Runnable() {
            IWSHandler h = handler;
            
            @Override
            public String toString() {
                return "Task-handleOpenSuccess";
            }

            @Override
            public void run() {
                if (log.isDebug()) {
                    log.debug(getEventLogInfo("notifyOpenSuccess", WSConfig.endpointInfo(event.getEndpoint())));
                }
                
                if (h != null) {
                    try {
                        h.onChannelOpened(new WSOpenedEvent(warmStandby, event));
                    }
                    catch(Throwable ex) {
                        if (log.isError()) {
                            log.error(info + " exception in handler "+h, ex);
                        }
                    }
                }
                
                f.notifySuccess();
                
                synchronized (guard) {
                    if (!f.isActual()) {
                        return;
                    }
                    
                    retryNo = 0;
                    lastOpenedEndpoint = event.getEndpoint();
                    checkedEndpoints.clear();
                }
            }
        });
    }
    
    
    void handleCloseSuccess(final WSCloseFuture f) {
        scheduleTask(new Runnable() {
            
            @Override
            public String toString() {
                return "Task-handleCloseSuccess";
            }

            @Override
            public void run() {
                if (log.isDebug()) {
                    log.debug(getEventLogInfo("notifyCloseSuccess", null));
                }
                
                f.notifySuccess();
            }
        });
    }
    
    
    
    void handleOpenFailed(final WSOpenFuture f) {
        scheduleTask(new Runnable() {
            
            @Override
            public String toString() {
                return "Task-handleOpenFailed";
            }

            @Override
            public void run() {
                if (log.isDebug()) {
                    log.debug(getEventLogInfo("notifyOpenFailed", null));
                }
                f.notifyFailed();
                
                synchronized (guard) {
                    if (!f.isActual()) {
                        return;
                    }
                    
                    lastOpenedEndpoint = null;
                    checkedEndpoints.clear();
                }
            }
        });
    }
    
    
    void proccessClosedEvent(ChannelClosedEvent event) {
        
        if (log.isDebug()) {
            log.debug(info + ".proccessClosedEvent : " 
                    + WSConfig.endpointInfo(event.getEndpoint()) + causeToString(event.getCause()));
        }
        
        
        boolean disconnected = event.getPreviousChannelState() != ChannelState.Opening;

        
        if (disconnected) {
            handleChannelDisconnected(event);
        } else {
            if (event.getCause() == null) {
                openFuture.failed(null);
                openFuture.getCloseFuture().success(null);
                return;
            }
            handleTriedUnsuccessfully(event);
        }
    }
    
    
    void handleChannelDisconnected(final ChannelClosedEvent event) {
        
        scheduleTask(new Runnable() {
            WSOpenFuture f = openFuture;
            IWSHandler h = handler;
            
            @Override
            public String toString() {
                return "Task-handleChannelDisconnected";
            }

            @Override
            public void run() {
                if (log.isDebug()) {
                    log.debug(getEventLogInfo("notifyChannelDisconnected", 
                            WSConfig.endpointInfo(event.getEndpoint()) + causeToString(event.getCause())));
                }
                
                if (h != null) {
                    try {
                        h.onChannelDisconnected(new WSDisconnectedEvent(warmStandby, event));
                    }
                    catch(Throwable ex) {
                        if (log.isError()) {
                            log.error(info + " exception in handler "+h, ex);
                        }
                    }
                }
                
                postProcessChannelClosedEvent(event, f, true);
            }
        });
    }
    
    void handleTriedUnsuccessfully(final ChannelClosedEvent event) {
        scheduleTask(new Runnable() {
            WSOpenFuture f = openFuture;
            IWSHandler h = handler;
            
            @Override
            public String toString() {
                return "Task-handleTriedUnsuccessfully";
            }

            @Override
            public void run() {
                if (log.isDebug()) {
                    log.debug(getEventLogInfo("notifyTriedUnsuccessfully", 
                            WSConfig.endpointInfo(event.getEndpoint()) + causeToString(event.getCause())));
                }

                WSTriedUnsuccessfullyEvent wsevent = null;

                if (h != null) {
                    wsevent = new WSTriedUnsuccessfullyEvent(warmStandby, event);
                    try {
                        h.onEndpointTriedUnsuccessfully(wsevent);
                    }
                    catch(Throwable ex) {
                        if (log.isError()) {
                            log.error(info + " exception in handler "+h, ex);
                        }
                    }
                }
                
                
                boolean retryAgain = false;

                if (event.getCause() instanceof RegistrationException) {
                    if (wsevent != null && !wsevent.isRestoringStopped()) {
                        retryAgain = true;
                    }
                }
                
                postProcessChannelClosedEvent(event, f, retryAgain);
            }
        });
    }    
    
    protected void postProcessChannelClosedEvent(final ChannelClosedEvent event, final WSOpenFuture openFuture, final boolean retryAgain) {
        
        synchronized (guard) {


            Throwable cause = event.getCause();
            if (cause == null) {
                openFuture.failed(null);
                openFuture.getCloseFuture().success(null);
                return;
            }
            
            if (!retryAgain && cause instanceof RegistrationException) {
                openFuture.failed(cause);
                openFuture.getCloseFuture().success(null);
                return;
            }

            if (!openFuture.isActual()) {
                return;
            }

            boolean disconnected = event.getPreviousChannelState() != ChannelState.Opening;
            if (disconnected) {
                retryNo = 0;
                checkedEndpoints.clear();
                lastOpenedEndpoint = null;

                if (!autoRestoreEnabled) {
                    openFuture.getCloseFuture().success(null);
                    return;
                }

                if (event.getReceivedMessageCount() == 0
                        && (event.getSentMessagesCount() > 0 || event.isAddpInitializationFailed())) {
                    lastOpenedEndpoint = null;
                    checkedEndpoints.add(event.getEndpoint());
                }
                else {
                    lastOpenedEndpoint = event.getEndpoint();
                }
                createNewOpenContext();
                doOpening();
                return;
            }
            
            if (!retryAgain) {
                checkedEndpoints.add(event.getEndpoint());
            }
            
            doOpening();
        }
    }

    @SuppressWarnings("deprecation")
    private void releaseChannelInvoker() {
        // release default invoker if the channel have been closed already
        channel.getInvoker().dispose();
    }


    private Endpoint peekFirstUncheckedEndpoint() {

        List<Endpoint> pool = getConfig().getEndpoints();
        Set<Endpoint> poolChecked = checkedEndpoints;

        Endpoint lastOpened = lastOpenedEndpoint;
        if (lastOpened != null && !poolChecked.contains(lastOpened)
                && pool.contains(lastOpened)) {
            return lastOpened;
        }

        for (Endpoint endpoint : pool) {
            if (endpoint == null) {
                continue;
            }
            if (poolChecked.contains(endpoint)) {
                continue;
            }
            return endpoint;
        }
        return null;
    }

    @Override
    public void onChanged(WSConfigOption option) {
        WSDelayType delayType = option.getDelayType();
        if (delayType != null) {
            openFuture.reschedule(delayType);
        }
        else if (option == WSConfigOption.EndpointPool) {
            if (paused && !config.getEndpoints().isEmpty()) {
                scheduleTask(new Runnable() {
                    @Override
                    public String toString() {
                        return "Task-handleHasEndpoints";
                    }

                    @Override
                    public void run() {
                        synchronized (guard) {
                            if (!paused || config.getEndpoints().isEmpty()) {
                                return;
                            }
                            createNewOpenContext();
                            doOpening();
                        }
                    }
                });
            }
        }
    }
    
    
    @Override
    public void onChannelOpened(final EventObject event) {
        ChannelOpenedEvent openedEvent = (ChannelOpenedEvent)event;
        if (log.isDebug()) {
            log.debug(info + ".onChannelOpened : " 
                    + WSConfig.endpointInfo(openedEvent.getEndpoint()));
        }
//        if (channelEventSkipCounter.get() > 0) {
//            channelEventSkipCounter.decrementAndGet();
//            if (log.isDebug()) {
//                log.debug(info + " skipped channel opened event " 
//                        + WSConfig.endpointInfo(openedEvent.getEndpoint()));
//            }
//            return;
//        }
        try {
            channelInvokerThreadRef.set(Thread.currentThread());
            openFuture.success(openedEvent);
        }
        finally {
            processQueue();
            channelInvokerThreadRef.set(null);
        }
    }

    @Override
    public void onChannelClosed(final ChannelClosedEvent event) {
        if (log.isDebug()) {
            log.debug(info + ".onChannelClosed : " 
                    + WSConfig.endpointInfo(event.getEndpoint()) + causeToString(event.getCause()));
        }
//        if (channelEventSkipCounter.get() > 0) {
//            channelEventSkipCounter.decrementAndGet();
//            if (log.isDebug()) {
//                log.debug(info + " skipped channel closed event " 
//                        + WSConfig.endpointInfo(event.getEndpoint()) + causeToString(event.getCause()));
//            }
//            return;
//        }
        try {
            channelInvokerThreadRef.set(Thread.currentThread());
            synchronized (guard) {
                proccessClosedEvent(event);
            }
        }
        finally {
            processQueue();
            channelInvokerThreadRef.set(null);
        }
    }

    @Override
    public void onChannelError(ChannelErrorEvent event) {
    }

    private void processQueue() {
        Runnable task = null;
        while (!queue.isEmpty()) {
            task = queue.poll();
            executeTask(task);
        }
        releaseChannelInvoker();
    }

//    public void skipNextChannelEvent() {
//        channelEventSkipCounter.incrementAndGet();
//    }

    public String getName() {
        return name;
    }
    
    
    private static String causeToString(Throwable cause) {
        if (cause == null) {
            return " cause: null";
        }
        StringBuilder sb = new StringBuilder(4096);
        sb.append(' ');
        while (cause != null) {
            sb.append(cause.getClass().getSimpleName());
            sb.append(": ");
            sb.append(cause.getMessage());
            sb.append(' ');
            cause = cause.getCause();
        }
        sb.setLength(sb.length()-1);
        return sb.toString();
    }
    
}

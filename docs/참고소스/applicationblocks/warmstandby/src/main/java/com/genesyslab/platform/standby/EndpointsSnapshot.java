/*
 * Copyright (c) 2006 - 2019 Genesys Telecommunications Laboratories, Inc.
 * All rights reserved.
 *
 * For more  information on the Genesys Telecommunications Laboratories, Inc.
 * please see <http://www.genesyslab.com/>.
 */
package com.genesyslab.platform.standby;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.genesyslab.platform.commons.log.ILogger;
import com.genesyslab.platform.commons.log.Log;
import com.genesyslab.platform.commons.protocol.Endpoint;


class EndpointsSnapshot extends CopyOnWriteArrayList<Endpoint> {
    
    private static final ILogger log = Log.getLogger(EndpointsSnapshot.class);
    private final WSConfig.ChangeListener listener;
    private final String info;

    EndpointsSnapshot(String info, WSConfig.ChangeListener listener) {
        super();
        this.info = info;
        this.listener = listener;
    }

    EndpointsSnapshot(List<? extends Endpoint> value, String info, WSConfig.ChangeListener listener) {
        super(verifyEndpoints(value, info));
        this.info = info;
        this.listener = listener;
    }

    EndpointsSnapshot(Collection<? extends Endpoint> value, String info, boolean distinct, WSConfig.ChangeListener listener) {
        super();
        verifyEndpoints(value, info);
        this.info = info;
        if (value!=null)
        if (distinct) {
            addAllAbsent(value);
        } else {
            addAll(value) ;
        }
        this.listener = listener;
    }

    private final void notifyModified() {
        if (listener != null) {
            listener.onChanged(WSConfig.WSConfigOption.EndpointPool);
        }
    }
    private final void notifyModifiedIf(boolean flag) {
        if (flag && listener != null) {
            listener.onChanged(WSConfig.WSConfigOption.EndpointPool);
        }
    }


    @Override
    public boolean add(Endpoint e) {
        if (e == null) {
            if (log.isError()) {
                log.error(info + " try add null endpoint");
            }
            throw new IllegalArgumentException(info + " try add null endpoint", null);
        }
        if (log.isDebug()) {
            log.debug(info + ".addEndpoint(" + WSConfig.endpointInfo(e) + ")");
        }
        boolean modified = super.add(e);
        notifyModified();
        return modified;
    }

    @Override
    public void add(int index, Endpoint element) {
        if (element == null) {
            if (log.isError()) {
                log.error(info + " try add null endpoint");
            }
            throw new IllegalArgumentException(info + " try add null endpoint", null);
        }
        if (log.isDebug()) {
            log.debug(info + ".addEndpoint(" + WSConfig.endpointInfo(element) + ")");
        }
        super.add(index, element);
        notifyModified();
    }

    @Override
    public boolean addIfAbsent(Endpoint e) {
        if (e == null) {
            if (log.isError()) {
                log.error(info + " try add null endpoint");
            }
            throw new IllegalArgumentException(info + " try add null endpoint", null);
        }
        if (log.isDebug()) {
            log.debug(info + ".addEndpoint(" + WSConfig.endpointInfo(e) + ")");
        }
        final boolean modified = super.addIfAbsent(e);
        notifyModifiedIf(modified);
        return modified;
    }


    @Override
    public int addAllAbsent(Collection<? extends Endpoint> c) {
        c = new ArrayList<Endpoint>(c);
        verifyEndpoints(c, info);
        int added = super.addAllAbsent(c);
        notifyModifiedIf(added > 0);
        return added;
    }

    @Override
    public boolean addAll(Collection<? extends Endpoint> c) {
        c = new ArrayList<Endpoint>(c);
        verifyEndpoints(c, info);
        boolean modified = super.addAll(c);
        notifyModifiedIf(modified);
        return modified;
    }

    @Override
    public boolean addAll(int index, Collection<? extends Endpoint> c) {
        c = new ArrayList<Endpoint>(c);
        verifyEndpoints(c, info);
        boolean modified = super.addAll(index, c);
        notifyModifiedIf(modified);
        return modified;
    }
    
    private static Collection<? extends Endpoint> verifyEndpoints(Collection<? extends Endpoint> c, String info) {
        if (c != null) {
            for(Endpoint e: c) {
                if (e == null) {
                    if (log.isError()) {
                        log.error(info + " try add null endpoint");
                    }
                    throw new IllegalArgumentException(info + " try add null endpoint", null);
                }
                if (log.isDebug()) {
                    log.debug(info + ".addEndpoint(" + WSConfig.endpointInfo(e) + ")");
                }
            }
        }
        return c;
    }

    @Override
    public Endpoint set(int index, Endpoint element) {
        if (element == null) {
            if (log.isError()) {
                log.error(info + " try set null endpoint");
            }
            throw new IllegalArgumentException(info + " try set null endpoint", null);
        }
        Endpoint result = super.set(index, element);
        notifyModified();
        return result;
    }


    @Override
    public void clear() {
        super.clear();
        notifyModified();
    }

    @Override
    public Endpoint remove(int index) {
        Endpoint modified = super.remove(index);
        notifyModified();
        return modified;
    }

    @Override
    public boolean remove(Object o) {
        boolean modified = super.remove(o);
        notifyModifiedIf(modified);
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = super.removeAll(c);
        notifyModifiedIf(modified);
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = super.retainAll(c);
        notifyModifiedIf(modified);
        return modified;
    }
}

/*
 * Copyright (c) 2006 - 2019 Genesys Telecommunications Laboratories, Inc.
 * All rights reserved.
 *
 * For more  information on the Genesys Telecommunications Laboratories, Inc.
 * please see <http://www.genesyslab.com/>.
 */
package com.genesyslab.platform.standby;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import com.genesyslab.platform.commons.log.ILogger;
import com.genesyslab.platform.commons.log.Log;
import com.genesyslab.platform.commons.protocol.Endpoint;


/**
 * The warm standby configuration helps to define endpoint pool and other delays and timeout properties</li>
 */
public final class WSConfig implements Cloneable {
    
    private static final ILogger log = Log.getLogger(WSConfig.class);

    private volatile EndpointsSnapshot endpoints;
    private volatile int reconnectionRandomDelayRange;
    private volatile int backupDelay;
    private volatile int[] retryDelay = { 1000 };
    private volatile Integer timeout;
    private ChangeListener listener;
    private final String name;

    /**
     * Create a warm standby configuration instance without a specified name.
     */
    public WSConfig() {
        this(null, null);
    }

    /**
     * Create a warm standby configuration instance with a specified name.
     */
    public WSConfig(final String name) {
        this(name, null);
    }

    WSConfig(final ChangeListener listener) {
        this(null, listener);
    }

    WSConfig(final String name, final ChangeListener listener) {
        this.name = name;
        this.listener = listener;
        this.endpoints = new EndpointsSnapshot(prefix(), listener);
    }

    private String prefix() {
        return listener != null ? listener.toString() : "WSConfig";
    }

    /**
     * Returns name.
     * @return name.
     */
    public String getName() {
        return name;
    }


    /**
     * Gets list of endpoints.
     * <p>
     * <b>Note:</b> if endpoints with same app,host and port occurs multiple
     * times then only first one will be used while opening.
     * </p>
     * <p>
     * The iteration through endpoints always starts from the begin of the
     * ednpoint pool. <br>
     * Endpoint from which channel has been disconnected and endpoints which
     * have been already checked unsuccessfully after disconnection, are skipped
     * from the iteration. <br>
     * If some other order of iteration is desired than in the appropriate event
     * handler (onChannelDisconnected, onEndpointCheckUnsuccessfully, ..) the
     * endpoints order in the pool can be changed appropriately.
     * </p>
     *
     * @return list of endpoints.
     */
    public List<Endpoint> getEndpoints() {
        return endpoints;
    }

    /**
     * Sets endpoints collection.
     * <p>
     * <b>Note:</b> if endpoints with same app,host and port will be passed
     * multiple times then only first one will be used while opening.
     * </p>
     * <p>
     * The iteration through endpoints always starts from the begin of the
     * ednpoint pool. <br>
     * Endpoint from which channel has been disconnected and endpoints which
     * have been already checked unsuccessfully after disconnection, are skipped
     * from the iteration. <br>
     * If some other order of iteration is desired than in the appropriate event
     * handler (onChannelDisconnected, onEndpointCheckUnsuccessfully, ..) the
     * endpoints order in the pool can be changed appropriately.
     * </p>
     * 
     * @param value collection of endpoints.
     * @return self reference
     * @throws IllegalArgumentException
     *             if null endpoint occurs in the collection.
     * @see #setEndpoints(Endpoint[])
     * @see #setEndpoints(List)
     */
    public WSConfig setEndpointsCollection(final Collection<? extends Endpoint> value) {
        if (log.isDebug()) {
            log.debug(prefix() + ".setEndpoints(" + endpointsCollectionToString(value) + ")");
        }
        if (value == null) {
            this.endpoints.clear();
        } else {
            for (final Endpoint e : value) {
                if (e == null) {
                    throw new IllegalArgumentException(prefix() + " null endpoint", null);
                }
            }
            this.endpoints = new EndpointsSnapshot(value, prefix(), false, listener);
        }
        if (listener != null) {
            listener.onChanged(WSConfigOption.EndpointPool);
        }
        return this;
    }

    /**
     * Sets endpoints list.
     * <p>
     * <b>Note:</b> if endpoints with same app,host and port will be passed
     * multiple times then only first one will be used while opening.
     * </p>
     * <p>
     * The iteration through endpoints always starts from the begin of the
     * ednpoint pool. <br>
     * Endpoint from which channel has been disconnected and endpoints which
     * have been already checked unsuccessfully after disconnection, are skipped
     * from the iteration. <br>
     * If some other order of iteration is desired than in the appropriate event
     * handler (onChannelDisconnected, onEndpointCheckUnsuccessfully, ..) the
     * endpoints order in the pool can be changed appropriately.
     * </p>
     *
     * @param value list of endpoints.
     * @return self reference
     * @throws IllegalArgumentException
     *             if null endpoint occurs in the list.
     * @see #setEndpoints(Endpoint[])
     * @see #setEndpointsCollection(Collection)
     */
    public WSConfig setEndpoints(final List<? extends Endpoint> value) {
        return setEndpointsCollection(value);
    }

    static String endpointsCollectionToString(final Collection<? extends Endpoint> value) {
        if (value == null) {
            return "null";
        }
        final StringBuilder sb = new StringBuilder(4096);
        sb.append('{');
        for (Endpoint e : value) {
            sb.append(endpointInfo(e));
            sb.append(',');
        }
        if (sb.length() > 1) {
            sb.setLength(sb.length()-1);
        }
        sb.append('}');
        return sb.toString();
    }

    static String endpointsToString(final List<? extends Endpoint> value) {
        return endpointsCollectionToString(value);
    }

    static String endpointInfo(final Endpoint endpoint) {
        if (endpoint == null) {
            return "null";
        }
        if (endpoint.hasName()) {
            return endpoint.getName();
        }
        final StringBuilder sb = new StringBuilder(4096);
        sb.append(endpoint.getHost());
        sb.append(':');
        sb.append(endpoint.getPort());
        return sb.toString();
    }

    /**
     * Sets endpoints array.
     * <p>
     * <b>Note:</b> if endpoints with same app,host and port will be passed
     * multiple times then only first one will be used while opening.
     * </p>
     * <p>
     * The iteration through endpoints always starts from the begin of the
     * ednpoint pool. <br>
     * Endpoint from which channel has been disconnected and endpoints which
     * have been already checked unsuccessfully after disconnection, are skipped
     * from the iteration. <br>
     * If some other order of iteration is desired than in the appropriate event
     * handler (onChannelDisconnected, onEndpointCheckUnsuccessfully, ..) the
     * endpoints order in the pool can be changed appropriately.
     * </p>
     * 
     * @param value array of endpoints.
     * @return self reference
     * @throws IllegalArgumentException
     *             if null endpoint occurs in the array.
     * @see #setEndpoints(List)
     * @see #setEndpointsCollection(Collection)
     */
    public WSConfig setEndpoints(final Endpoint... value) {
        setEndpoints(value != null ? Arrays.asList(value) : (List<Endpoint>) null);
        return this;
    }

    /**
     * Gets the random delay range before reconnection to the last opened
     * endpoint in case of disconnection.
     * 
     * @return current range for random delay in milliseconds.
     */
    public int getReconnectionRandomDelayRange() {
        return reconnectionRandomDelayRange;
    }

    /**
     * Sets the random delay range before reconnection to the last opened
     * endpoint in case of disconnection.
     * 
     * @param delayMs
     *            new range for random delay in milliseconds.
     * @returns this
     */
    public WSConfig setReconnectionRandomDelayRange(final int delayMs) {
        if (log.isDebug()) {
            log.debug(prefix() + ".setReconnectionRandomDelayRange(" + delayMs + ")");
        }
        if (delayMs < 0) {
            throw new IllegalArgumentException(prefix() + " negative reconnectionRandomDelayRange", null);
        }
        if (this.reconnectionRandomDelayRange != delayMs) {
            this.reconnectionRandomDelayRange = delayMs;
            if (listener != null) {
                listener.onChanged(WSConfigOption.ReconnectionRandomDelayRange);
            }
        }
        return this;
    }

    /**
     * Gets the backup delay that is applied after failure of the first
     * reconnection attempt and before switching to backup endpoint. It
     * represents time which is enough for backup server to switchover to
     * primary mode.
     * 
     * @return current backup delay in milliseconds.
     */
    public int getBackupDelay() {
        return backupDelay;
    }

    /**
     * Sets the backup delay that is applied after failure of the first
     * reconnection attempt and before switching to backup endpoint. It
     * represents time which is enough for backup server to switchover to
     * primary mode.
     * 
     * @param delayMs
     *            new backup delay in milliseconds.
     * @returns this
     * @throws IllegalArgumentException
     *             if the argument delayMs is negative.
     */
    public WSConfig setBackupDelay(final int delayMs) {
        if (log.isDebug()) {
            log.debug(prefix() + ".setBackupDelay(" + delayMs + ")");
        }
        if (delayMs < 0) {
            throw new IllegalArgumentException(prefix() + " negative backupDelay", null);
        }
        if (this.backupDelay != delayMs) {
            this.backupDelay = delayMs;
            if (listener != null) {
                listener.onChanged(WSConfigOption.BackupDelay);
            }
        }
        return this;
    }


    /**
     * Gets the open timeout that is used for connection to endpoints.
     * 
     * @return current open timeout in milliseconds.
     */
    public Integer getTimeout() {
        return timeout;
    }

    /**
     * Sets the open timeout that is used for connection to the endpoints.
     * 
     * @param valueMs
     *            new open timeout in milliseconds.
     * @returns this
     */
    public WSConfig setTimeout(final Integer valueMs) {
        if (log.isDebug()) {
            log.debug(prefix() + ".setTimeout(" + valueMs + ")");
        }
        this.timeout = valueMs;
        return this;
    }

    /**
     * Gets retry delay that is applied after all endpoints has been checked
     * unsuccessfully and before next iteration will be started.
     * @param retryNumber
     * @return retry delay in milliseconds.
     */
    public int getRetryDelay(final int retryNumber) {
        if (retryNumber < 0) {
            throw new IllegalArgumentException(prefix() + " negative retryNumber", null);
        }
        int[] retryDelays = this.retryDelay;
        if (retryDelays != null && retryDelays.length > 0) {
            return retryDelays[Math.min(retryDelays.length - 1, retryNumber)];
        }
        return 0;
    }

    /**
     * Gets retry delays that is applied after all endpoints has been checked
     * unsuccessfully and before next iteration will be started.
     * <p>
     * The default value is 1000 ms.
     * </p>
     * 
     * @return array of delays in milliseconds.
     */
    public int[] getRetryDelay() {
        return retryDelay;
    }

    /**
     * Sets retry delays that is applied after all endpoints has been checked
     * unsuccessfully and before next iteration will be started.
     * 
     * @param delayMs
     *            array of delays in milliseconds, if it's null then there is no
     *            any delay.
     * @returns this
     * @throws IllegalArgumentException
     *             if the argument delayMs contains negative delay.
     */
    public WSConfig setRetryDelay(final int... delayMs) {
        if (log.isDebug()) {
            log.debug(prefix() + ".setRetryDelay(" 
                            + (delayMs == null ? "null" : Arrays.toString(delayMs))  
                            + ")");
        }
        if (delayMs != null) {
            for (int delay : delayMs) {
                if (delay < 0) {
                    throw new IllegalArgumentException(prefix() + " negative retryDelay",
                            null);
                }
            }
        }
        int[] newDelayMs = delayMs == null ? delayMs : Arrays.copyOf(delayMs, delayMs.length);
        
        int lenOld = retryDelay == null ? 0 : retryDelay.length;
        int lenNew = newDelayMs == null ? 0 : newDelayMs.length;
        if (!(lenOld == 0 && lenNew == 0)) {
            if (!Arrays.equals(this.retryDelay, newDelayMs)) {
                this.retryDelay = newDelayMs;
                if (listener != null) {
                    listener.onChanged(WSConfigOption.RetryDelay);
                }
            }
        }
        return this;
    }

    void apply(final WSConfig configuration) {
        if (configuration == null) {
            throw new IllegalArgumentException(prefix() + " null configuration", null);
        }
        setReconnectionRandomDelayRange(configuration.getReconnectionRandomDelayRange());
        setBackupDelay(configuration.getBackupDelay());
        setTimeout(configuration.getTimeout());
        setRetryDelay(configuration.getRetryDelay());
        setEndpoints(configuration.getEndpoints());
    }

    /**
     * Compares with other configurations ignoring its names.
     * @param wsConfig
     * @return
     */
    public boolean equalsIgnoreName(final WSConfig wsConfig) {
        if (this == wsConfig) {
            return true;
        }
        if (wsConfig == null ) {
            return false;
        }

        if (reconnectionRandomDelayRange != wsConfig.reconnectionRandomDelayRange) {
            return false;
        }
        if (backupDelay != wsConfig.backupDelay) {
            return false;
        }
        if (endpoints != null ? !endpoints.equals(wsConfig.endpoints) : wsConfig.endpoints != null) {
            return false;
        }
        if (!Arrays.equals(retryDelay, wsConfig.retryDelay)) {
            return false;
        }
        if (timeout != null ? !timeout.equals(wsConfig.timeout) : wsConfig.timeout != null) {
            return false;
        }
        return true;
    }

    /**
     * Compares two configurations ignoring its names.
     * @param wsConfig1
     * @param wsConfig2
     * @return
     */
    public static boolean equalsIgnoreName(final WSConfig wsConfig1, final WSConfig wsConfig2) {
        if (wsConfig1 == null) {
            return wsConfig2 == null;
        }
        return wsConfig1.equalsIgnoreName( wsConfig2 );
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final WSConfig wsConfig = (WSConfig) o;

        if (reconnectionRandomDelayRange != wsConfig.reconnectionRandomDelayRange) {
            return false;
        }
        if (backupDelay != wsConfig.backupDelay) {
            return false;
        }
        if (endpoints != null ? !endpoints.equals(wsConfig.endpoints) : wsConfig.endpoints != null) {
            return false;
        }
        if (!Arrays.equals(retryDelay, wsConfig.retryDelay)) {
            return false;
        }
        if (timeout != null ? !timeout.equals(wsConfig.timeout) : wsConfig.timeout != null) {
            return false;
        }

        return name != null ? name.equals(wsConfig.name) : wsConfig.name == null;
    }

    @Override
    public int hashCode() {
        int result = endpoints != null ? endpoints.hashCode() : 0;
        result = 31 * result + reconnectionRandomDelayRange;
        result = 31 * result + backupDelay;
        result = 31 * result + Arrays.hashCode(retryDelay);
        result = 31 * result + (timeout != null ? timeout.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }


    @Override
    public Object clone() {
        try {
            final WSConfig obj = (WSConfig) super.clone();

            obj.listener = null;
            obj.endpoints = (EndpointsSnapshot) endpoints.clone();

            final int[] tmp = retryDelay;
            obj.retryDelay = (tmp != null) ? Arrays.copyOf(tmp, tmp.length) : null;

            return obj;
        } catch (final CloneNotSupportedException ex) { // it will never happens
            throw new UnsupportedOperationException(ex);
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(1024);
        sb.append('{');
        if (name != null) {
            sb.append("\"name\":\"");
            sb.append(name);
            sb.append("\",");
        }
        if (reconnectionRandomDelayRange != 0) {
            sb.append("\"reconnectionRandomDelayRange\":");
            sb.append(reconnectionRandomDelayRange);
            sb.append(",");
        }
        if (backupDelay != 0) {
            sb.append("\"backupDelay\":");
            sb.append(backupDelay);
            sb.append(",");
        }
        if (retryDelay != null && retryDelay.length > 0) {
            sb.append("\"retryDelay\":[");
            for(int delay : retryDelay) {
                sb.append(delay);
                sb.append(",");
            }
            sb.setLength(sb.length()-1);
            sb.append("],");
        }
        if (timeout != null) {
            sb.append("\"timeout\":");
            sb.append(timeout);
            sb.append(",");
        }

        if (!endpoints.isEmpty()) {
            sb.append("\"endpoints\":[");
            for(Endpoint e : endpoints) {
                sb.append("\"");
                sb.append(e);
                sb.append("\",");
            }
            sb.setLength(sb.length()-1);
            sb.append("]");
        }
        if (sb.charAt(sb.length()-1) == ',') {
            sb.setLength(sb.length()-1);
        }
        sb.append('}');
        return sb.toString();
    }

 
    enum WSConfigOption {
        ReconnectionRandomDelayRange(WSDelayType.ReconnectionDelay),
        BackupDelay(WSDelayType.BackupDelay),
        RetryDelay(WSDelayType.RetryDelay),
        EndpointPool(null);

        private final WSDelayType delayType;

        WSConfigOption(final WSDelayType delayType) {
            this.delayType = delayType;
        }

        public WSDelayType getDelayType() {
            return delayType;
        }
    }

    interface ChangeListener {
        void onChanged(WSConfigOption option);
    }
}

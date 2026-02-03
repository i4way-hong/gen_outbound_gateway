// ===============================================================================
//  Genesys Platform SDK Application Blocks
// ===============================================================================
//
//  Any authorized distribution of any copy of this code (including any related
//  documentation) must reproduce the following restrictions, disclaimer and copyright
//  notice:
//
//  The Genesys name, trademarks and/or logo(s) of Genesys shall not be used to name
//  (even as a part of another name), endorse and/or promote products derived from
//  this code without prior written permission from Genesys Telecommunications
//  Laboratories, Inc.
//
//  The use, copy, and/or distribution of this code is subject to the terms of the Genesys
//  Developer License Agreement.  This code shall not be used, copied, and/or
//  distributed under any other license agreement.
//
//  THIS CODE IS PROVIDED BY GENESYS TELECOMMUNICATIONS LABORATORIES, INC.
//  ("GENESYS") "AS IS" WITHOUT ANY WARRANTY OF ANY KIND. GENESYS HEREBY
//  DISCLAIMS ALL EXPRESS, IMPLIED, OR STATUTORY CONDITIONS, REPRESENTATIONS AND
//  WARRANTIES WITH RESPECT TO THIS CODE (OR ANY PART THEREOF), INCLUDING, BUT
//  NOT LIMITED TO, IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
//  PARTICULAR PURPOSE OR NON-INFRINGEMENT. GENESYS AND ITS SUPPLIERS SHALL
//  NOT BE LIABLE FOR ANY DAMAGE SUFFERED AS A RESULT OF USING THIS CODE. IN NO
//  EVENT SHALL GENESYS AND ITS SUPPLIERS BE LIABLE FOR ANY DIRECT, INDIRECT,
//  CONSEQUENTIAL, ECONOMIC, INCIDENTAL, OR SPECIAL DAMAGES (INCLUDING, BUT
//  NOT LIMITED TO, ANY LOST REVENUES OR PROFITS).
//
//  Copyright (c) 2006 - 2019 Genesys Telecommunications Laboratories, Inc. All rights reserved.
// ===============================================================================
package com.genesyslab.platform.apptemplate.lmslogger;

import com.genesyslab.platform.apptemplate.lmslogger.impl.SilentLmsEventLogger;
import com.genesyslab.platform.apptemplate.lmslogger.impl.SilentLmsLoggerFactory;
import com.genesyslab.platform.apptemplate.lmslogger.impl.SimpleLmsLoggerFactory;

import com.genesyslab.platform.apptemplate.lmslogger.jul.JulLmsLoggerFactory;
import com.genesyslab.platform.apptemplate.lmslogger.log4j.Log4jLmsLoggerFactory;
import com.genesyslab.platform.apptemplate.lmslogger.slf4j.Slf4jLmsLoggerFactory;
import com.genesyslab.platform.apptemplate.lmslogger.log4j2.Log4j2LmsLoggerFactory;

import com.genesyslab.platform.apptemplate.configuration.log.GAppLoggingOptions;
import com.genesyslab.platform.apptemplate.configuration.IGApplicationConfiguration;

import com.genesyslab.platform.management.protocol.messageserver.LogCategory;

import com.genesyslab.platform.commons.log.Log;
import com.genesyslab.platform.commons.log.ILogger;
import com.genesyslab.platform.commons.log.Slf4JChecker;
import com.genesyslab.platform.commons.log.Log4JChecker;
import com.genesyslab.platform.commons.log.Log4J2Checker;

import com.genesyslab.platform.commons.PsdkCustomization;
import com.genesyslab.platform.commons.PsdkCustomization.PsdkOption;

import java.lang.reflect.Method;
import java.lang.reflect.Constructor;


/**
 * Abstract factory class for LMS event loggers.<br/>
 * It's the entry point of the LMS logging functionality.
 * <p/>
 * Common usage scenario is simple:<code><pre>
 * LmsLoggerFactory.{@link #createInstance(LmsMessageConveyor) createInstance}(new LmsMessageConveyor());
 * LmsEventLogger lmsLogger = LmsLoggerFactory.{@link #getLmsLogger(Class) getLmsLogger}(getClass());
 * </pre></code>
 * If application has own LMS file declarations, it may use custom LmsEnum enumeration(s):<code><pre>
 * LmsLoggerFactory.{@link #createInstance(LmsMessageConveyor) createInstance}(
 *         new LmsMessageConveyor(CommonLmsEnum.class, MyAppLmsEnum.class));
 * ...
 * </pre></code>
 * <i><b>Note:</b> If application uses the
 * {@link com.genesyslab.platform.apptemplate.application.GFApplicationConfigurationManager
 * Application Configuration Manager}, there is no need to configure <code>LmsLofferFactory</code> -
 * it will be done by the manager. It only needs to have properly constructed
 * <code>LmsMessageConveyor</code></i>
 * <p/>
 * Wrapping of <code>LmsEventLogger</code>'s and <code>LmsLoggerFactory</code> allows
 * applications to make LMS logging redirection/reconfiguration in a runtime.<br/>
 * So, it is possible to start application with LMS logging switched off, or with some "default"
 * configuration, for example, logging to Slf4j, and later, when a new application configuration
 * obtained, to change logging target to Log4j2.<code><pre>
 * // Reconfigure LMS loggers to direct logs to Log4j2:
 * LmsLoggerFactory.{@link #setLoggerFactoryImpl(String, LmsMessageConveyor) setLoggerFactoryImpl}(Log.LOG_FACTORY_LOG4J2, lmsMessageConveyor);
 * </pre></code>
 * Application Template AB contains several built-in implementations of <code>LmsLoggerFactory</code>.<br/>
 * Those ones are adapters for the following logging frameworks: <code>Log4j2</code>, <code>Slf4j</code>,
 * <code>Log4j</code>, and <code>java.util.logging</code>.<br/>
 * There are also simple custom factories to write logs to <code>stdout</code>, and to switch LMS logging off.
 *
 * @see LmsMessageConveyor
 */
public abstract class LmsLoggerFactory {

    public static final String LMS_LOGGER_FACTORY_CLASS_PROP =
            "com.genesyslab.platform.apptemplate.lmslogger.factory";


    private static volatile WrappedLoggerFactory lmsLoggerFactory = null;

    private static final ILogger LOG = Log.getLogger(LmsLoggerFactory.class);


    private final LmsMessageConveyor lmsConveyor;


    /**
     * Protected constructor of the abstract class.
     *
     * @param lmsc the LMS events definitions conveyor.
     */
    protected LmsLoggerFactory(final LmsMessageConveyor lmsc) {
        this.lmsConveyor = lmsc;
    }


    /**
     * Returns reference to the static <code>LmsLoggerFactory</code> singleton instance.
     *
     * @return the LmsLoggerFactory reference, or null if it has not been initialized yet.
     * @see #createInstance(LmsMessageConveyor)
     * @see #setLoggerFactoryImpl(LmsLoggerFactory)
     */
    public static LmsLoggerFactory getLoggerFactory() {
        return lmsLoggerFactory;
    }


    /**
     * Creates and initializes the application singleton instance of <code>LmsLoggerFactory</code>.
     * <p/>
     * This is the recommended way for initialization of LMS events logging functionality.<br/>
     * It allows deployment time and runtime reconfiguration of LMS loggers.
     * <p/>
     * To choose loggers factory implementation it looks to the following places:<ol>
     * <li>System property {@value #LMS_LOGGER_FACTORY_CLASS_PROP}.<br/>
     *     If it contains FQCN of specific class extending <code>LmsLoggerFactory</code>,
     *     or alias name of built-in implementation, than the correspondent class constructor
     *     with <code>LmsMessageConveyor</code> parameter is used to initialize the static logger factory.</li>
     * <li>Platform SDK customization option {@link PsdkOption#PsdkLoggerFactory} (and its correspondent
     *     system property).<br/>
     *     If previously described system property is not specified, it checks this one.<br/>
     *     If the PSDK Commons Logging implementation is configured with this option using
     *     alias name of built-in implementation, this value is also used by this method to synchronize
     *     target logging framework.</li>
     * <li>If these two options are not provided, "silent" loggers factory will be used. I.e. no logs
     *     will be generated by default.<br/>
     *     Note: it is still possible to enable it in a runtime with call like
     *     <code>LmsLoggerFactory.setLoggerFactoryImpl(Log.LOG_FACTORY_LOG4J2, lmsMessageConveyor)</code>.</li>
     * </ol>
     * 
     * @param lmsConveyor the LMS events definitions conveyor or null for the default LmsMessageConveyor.
     * @return wrapped instance of LmsLoggerFactory.
     */
    public static synchronized LmsLoggerFactory createInstance(
            final LmsMessageConveyor lmsConveyor) {
        return doSetFactory(createLmsLoggerFactory(lmsConveyor));
    }


    /**
     * Gets an LmsEventLogger instance by name.<br/>
     * It uses statically initialized and wrapped logger factory instance.<br/>
     * So, this it may be used after initialization with {@link #createInstance(LmsMessageConveyor)}.<br/>
     * Its wrapping allows automatic reconfiguration of LmsEventLogger implementation without need to
     * recreate LmsEventLogger's instances by user application.
     *
     * @param name the logger name.
     * @return wrapped instance of named LmsEventLogger.
     * @see #createInstance(LmsMessageConveyor)
     */
    public static synchronized LmsEventLogger getLogger(final String name) {
        if (lmsLoggerFactory == null) {
            doSetFactory(createLmsLoggerFactory(null));
        }
        return lmsLoggerFactory.getLmsLogger(name);
    }

    /**
     * Gets an LmsEventLogger instance by class.<br/>
     * It uses statically initialized and wrapped logger factory instance.<br/>
     * So, this it may be used after initialization with {@link #createInstance(LmsMessageConveyor)}.<br/>
     * Its wrapping allows automatic reconfiguration of LmsEventLogger implementation without need to
     * recreate LmsEventLogger's instances by user application.
     *
     * @param clazz class to get the logger for.
     * @return wrapped instance of LmsEventLogger named by the class.
     * @see #createInstance(LmsMessageConveyor)
     */
    public static synchronized LmsEventLogger getLogger(final Class<?> clazz) {
        if (lmsLoggerFactory == null) {
            doSetFactory(createLmsLoggerFactory(null));
        }
        return lmsLoggerFactory.getLmsLogger(clazz);
    }


    /**
     * Sets LMS logging system implementation.
     *
     * @param factory new factory that creates loggers.
     * @return wrapped instance of LmsLoggerFactory.
     */
    public static synchronized LmsLoggerFactory setLoggerFactoryImpl(
            final LmsLoggerFactory factory) {
        return doSetFactory(factory);
    }

    /**
     * Sets LMS logging system implementation.<br/>
     * It may initialize LMS logging to work with one of PSDK built-in implementations,
     * or user defined one, by its fully qualified class name or alias name.<br/>
     * Following alias names are supported:<ul>
     * <li>{@link Log#LOG_FACTORY_CONSOLE} (<code>"console"</code>) - This implementation of LMS logging functionality
     *                                       prints LMS events to <code>stdout</code>;</li>
     * <li>{@link Log#LOG_FACTORY_LOG4J2} (<code>"log4j2"</code>) - This factory is to send LMS events to Log4j v2.x
     *                                       logging framework;</li>
     * <li>{@link Log#LOG_FACTORY_SLF4J} (<code>"slf4j"</code>) - This factory is to send LMS events to Slf4j API;</li>
     * <li>{@link Log#LOG_FACTORY_LOG4J} (<code>"log4j"</code>) - This factory is to send LMS events to Log4j v1.x
     *                                      logging framework;</li>
     * <li>{@link Log#LOG_FACTORY_JUL} (<code>"jul"</code>) - This factory is to send LMS events to
     *                                    <code>java.util.logging</code> facade;</li>
     * <li>{@link Log#LOG_FACTORY_AUTO} (<code>"auto"</code>) - It tries to detect available logging framework
     *                                    for LMS events logging in the following order: <code>Log4j2</code>,
     *                                    <code>Slf4j</code>, <code>Log4j</code>;
     *                                    if none of these frameworks is available, <code>java.util.logging</code>
     *                                    will be used;</li>
     * <li>{@link Log#LOG_FACTORY_NONE} (<code>"none"</code>) - It is to do not log events from LMS events loggers.</li>
     * </ul>
     *
     * @param factoryName name of standard PSDK logger factory or class name of custom user implementation of it.
     * @param lmsConveyor reference to new LmsMessageConveyor or null to reuse one from the current factory instance.
     * @return wrapped instance of LmsLoggerFactory.
     * @throws IllegalArgumentException in case of wrong value of the given factory name.
     */
    public static synchronized LmsLoggerFactory setLoggerFactoryImpl(
            final String factoryName,
            final LmsMessageConveyor lmsConveyor) {
        if (factoryName == null || factoryName.isEmpty()) {
            throw new IllegalArgumentException("No LmsLoggerFactory name given");
        }
        LmsMessageConveyor lmsc = lmsConveyor;
        if (lmsc == null && lmsLoggerFactory != null) {
            lmsc = lmsLoggerFactory.getMessageConveyor();
        }
        if (lmsc == null) {
            lmsc = new LmsMessageConveyor();
        }
        LmsLoggerFactory factory = createLmsLoggerFactory(factoryName, lmsc, true);
        if (factory == null) {
            throw new IllegalArgumentException(
                    "Failed to instantiate LmsLoggerFactory class: " + factoryName);
        }
        return doSetFactory(factory);
    }


    /**
     * Sets LMS logging system implementation.
     *
     * @param factory new factory that creates loggers.
     * @return wrapped instance of the LmsLoggerFactory.
     */
    private static LmsLoggerFactory doSetFactory(
            final LmsLoggerFactory factory) {
        LmsLoggerFactory newFactory = factory;
        if (newFactory == null) {
            newFactory = new SilentLmsLoggerFactory();
        }

        if (LOG.isDebug()) {
            LOG.debug("Setting LmsLoggerFactory to " + newFactory.getClass().getName());
        }

        return lmsLoggerFactory = new WrappedLoggerFactory(newFactory);
    }


    /**
     * Internal helper method for default (startup) setup of <code>LmsLoggerFactory</code>.
     *
     * @param lmsConveyor the LMS messages conveyor for the new factory instance.
     * @return newly created instance of <code>LmsLoggerFactory</code>.
     */
    private static LmsLoggerFactory createLmsLoggerFactory(
            final LmsMessageConveyor lmsConveyor) {
        final LmsMessageConveyor lmsc = (lmsConveyor != null) ? lmsConveyor : new LmsMessageConveyor();

        LmsLoggerFactory factory = null;
        String logSystemName = null;
        try {
            logSystemName = System.getProperty(LMS_LOGGER_FACTORY_CLASS_PROP);
        } catch (final Exception ex) {
            LOG.debug("Exception reading LMS logger factory implementation declaration", ex);
        }
        if (logSystemName != null && !logSystemName.isEmpty()) {
            try {
                factory = createLmsLoggerFactory(logSystemName, lmsc, true);
            } catch (final Exception ex) {
                LOG.debug("Exception creating LMS logger factory", ex);
            }
        } 

        if (factory == null) {
            logSystemName = PsdkCustomization.getOption(PsdkOption.PsdkLoggerFactory);
            if (logSystemName != null && !logSystemName.isEmpty()) {
                try {
                    factory = createLmsLoggerFactory(logSystemName, lmsc, false);
                } catch (final Exception ex) {
                    LOG.debug("Exception creating default LMS logger factory", ex);
                }
            }
        }

        if (factory != null) {
            return factory;
        }

        return new SilentLmsLoggerFactory(lmsc);
    }

    /**
     * Internal helper method for <code>LmsLoggerFactory</code> implementation resolution.
     *
     * @param logSystemName alias or class name of specific implementation of the factory.
     * @param lmsConveyor the LMS messages conveyor for the new factory instance.
     * @param checkClassName if <code>true</code>, the method tries to recognize <b>logSystemName</b>
     *                as a FQCN of factory implementation; elsewhere its being checked for alias name only.
     * @return newly created instance of <code>LmsLoggerFactory</code> or null.
     * @throws IllegalArgumentException if <b>logSystemName</b> contains invalid value for LmsLoggerFactory
     *                 identification.
     */
    private static LmsLoggerFactory createLmsLoggerFactory(
            final String logSystemName,
            final LmsMessageConveyor lmsConveyor,
            final boolean checkClassName) {
        if (logSystemName != null && !logSystemName.isEmpty()) {
            Class<?> clazz = null;
            if (logSystemName.equalsIgnoreCase(Log.LOG_FACTORY_AUTO)) {
                if (Log4J2Checker.isAvailable()) {
                    clazz = Log4j2LmsLoggerFactory.class;
                } else if (Slf4JChecker.isAvailable()) {
                    clazz = Slf4jLmsLoggerFactory.class;
                } else if (Log4JChecker.isAvailable()) {
                    clazz = Log4jLmsLoggerFactory.class;
                } else { // use 'default' logging system for "auto" - 'java.util.logging'
                    clazz = JulLmsLoggerFactory.class;
                }
            } else if (logSystemName.equalsIgnoreCase(Log.LOG_FACTORY_LOG4J2)) {
                clazz = Log4j2LmsLoggerFactory.class;
            } else if (logSystemName.equalsIgnoreCase(Log.LOG_FACTORY_SLF4J)) {
                clazz = Slf4jLmsLoggerFactory.class;
            } else if (logSystemName.equalsIgnoreCase(Log.LOG_FACTORY_LOG4J)) {
                clazz = Log4jLmsLoggerFactory.class;
            } else if (logSystemName.equalsIgnoreCase(Log.LOG_FACTORY_JUL)) {
                clazz = JulLmsLoggerFactory.class;
            } else if (logSystemName.equalsIgnoreCase(Log.LOG_FACTORY_CONSOLE)) {
                clazz = SimpleLmsLoggerFactory.class;
            } else if (logSystemName.equalsIgnoreCase(Log.LOG_FACTORY_NONE)) {
                clazz = SilentLmsLoggerFactory.class;
            }

            if (clazz == null && checkClassName) {
                try {
                    clazz = Class.forName(logSystemName);
                } catch (Exception ex) {
                    throw new IllegalArgumentException(
                            "Failed to get LmsLoggerFactory class: " + logSystemName, ex);
                }
            }
            if (clazz != null) {
                return createLmsLoggerFactory(clazz, lmsConveyor);
            }
        }
        return null;
    }

    /**
     * Internal helper method for <code>LmsLoggerFactory</code> instantiation.
     *
     * @param factoryClass specific implementation class of LMS loggers factory.
     * @param lmsConveyor the LMS messages conveyor for the new factory instance.
     * @return newly created instance of <code>LmsLoggerFactory</code>.
     * @throws IllegalArgumentException if <b>factoryClass</b> is not a valid implementation of LmsLoggerFactory,
     *                 or there is some error in its instantiation.
     */
    private static LmsLoggerFactory createLmsLoggerFactory(
            final Class<?> factoryClass,
            final LmsMessageConveyor lmsConveyor) {
        if (factoryClass == null) {
            throw new IllegalArgumentException("Given factory class is null");
        }
        if (LmsLoggerFactory.class.isAssignableFrom(factoryClass)) {
            try {
                final Method builderMethod = factoryClass.getMethod("newBuilder");
                if (builderMethod != null) {
                    final Class<?> builderType = builderMethod.getReturnType();
                    if ((builderType != null)
                            && AbstractFactoryBuilder.class.isAssignableFrom(builderType)) {
                        final AbstractFactoryBuilder<?,?> builder =
                                (AbstractFactoryBuilder<?,?>) builderMethod.invoke(null);
                        if (builder != null) {
                            return builder
                                    .withLmsConveyor(lmsConveyor)
                                    .withAppConfig(null)
                                    .build();
                        }
                    } else {
                        if (LOG.isWarn()) {
                            LOG.warn("Invalid return type of "
                                    + factoryClass.getName() + ".newBuilder()");
                        }
                    }
                }
            } catch (final NoSuchMethodException ex) {
                if (LOG.isInfo()) {
                    LOG.info("LmsLoggerFactory " + factoryClass.getName()
                            + " has no 'newBuilder()' declared");
                }
            } catch (final Exception ex) {
                if (LOG.isWarn()) {
                    LOG.warn("Failed to build LmsLoggerFactory: "
                            + factoryClass.getName(), ex);
                }
            }
            try {
                final Constructor<?> ctr = factoryClass.getConstructor(LmsMessageConveyor.class);
                if (ctr != null) {
                    return (LmsLoggerFactory) ctr.newInstance(lmsConveyor);
                }
            } catch (final Exception ex) {
                if (LOG.isWarn()) {
                    LOG.warn("Failed to instantiate LmsLoggerFactory: "
                            + factoryClass.getName(), ex);
                }
            }
            if (LOG.isWarn()) {
                LOG.warn("Failed to instantiate: 'new " + factoryClass.getName()
                        + "(LmsMessageConveyor)' - creating custom factory without conveyor...");
            }
            try {
                return (LmsLoggerFactory) factoryClass.getDeclaredConstructor().newInstance();
            } catch (Exception ex) {
                throw new IllegalArgumentException(
                    "Failed to instantiate LmsLoggerFactory: "
                    + factoryClass.getName(), ex);
            }
        } else {
            throw new IllegalArgumentException("Given factory class is not LmsLoggerFactory");
        }
    }

    /**
     * Returns reference to the LmsMessageConveyor instance of this logger factory.
     *
     * @return the LMS Message Conveyor.
     */
    public LmsMessageConveyor getMessageConveyor() {
        return lmsConveyor;
    }

    /**
     * Gets an LmsEventLogger instance by name.
     *
     * @param name the logger name.
     * @return LmsEventLogger named logger instance.
     */
    public abstract LmsEventLogger getLmsLogger(final String name);

    /**
     * Gets an LmsEventLogger instance by class.
     *
     * @param clazz class to get the logger for.
     * @return LmsEventLogger instance named by the class.
     */
    public abstract LmsEventLogger getLmsLogger(final Class<?> clazz);


    @SuppressWarnings("unchecked")
    public abstract static class AbstractFactoryBuilder
            <F extends LmsLoggerFactory, B extends AbstractFactoryBuilder<F, B>> {

        protected IGApplicationConfiguration appConfig = null;

        private LmsMessageConveyor conveyor = null;
        private GAppLoggingOptions logOptions = null; 


        public B withLmsConveyor(final LmsMessageConveyor conveyor) {
            this.conveyor = conveyor;
            return (B) this;
        }

        public B withAppConfig(final IGApplicationConfiguration appConfig) {
            this.appConfig = appConfig;
            return (B) this;
        }

        public abstract F build();

        protected LmsMessageConveyor getLmsConveyor() {
            return conveyor != null ? conveyor : new LmsMessageConveyor();
        }

        protected GAppLoggingOptions getLogOptions() {
            if (logOptions == null && appConfig != null) {
                logOptions = new GAppLoggingOptions(appConfig, LOG);
            }
            return logOptions;
        }
    }


    /**
     * The LmsLoggerFactory wrapper to provide application with wrapped LmsEventLogger's.
     */
    protected static class WrappedLoggerFactory extends LmsLoggerFactory {

        private final LmsLoggerFactory factory;

        public WrappedLoggerFactory(final LmsLoggerFactory factory) {
            super(factory.getMessageConveyor());
            this.factory = factory;
        }

        protected LmsLoggerFactory getUnwrappedLoggerFactory() {
            return factory;
        }

        @Override
        public LmsMessageConveyor getMessageConveyor() {
            return factory.getMessageConveyor();
        }

        private LmsEventLogger getNotWrappedLogger(final String name) {
            return unwrapLogger(factory.getLmsLogger(name));
        }

        @Override
        public LmsEventLogger getLmsLogger(final String name) {
            return new WrappedLogger(factory.getLmsLogger(name), name, this);
        }

        @Override
        public LmsEventLogger getLmsLogger(final Class<?> clazz) {
            return new WrappedLogger(factory.getLmsLogger(clazz), clazz.getName(), this);
        }
    }


    /**
     * The LmsEventLogger wrapper to be implicitly used by applications.<br/>
     * It makes possible automatic reconfiguration of LMS events logging without recreation
     * of applications' loggers instances.
     */
    protected static class WrappedLogger implements LmsEventLogger, java.io.Serializable {

        private static final long serialVersionUID = 7530783326341836299L;

        private volatile LogRef logRef;

        public WrappedLogger(
                final LmsEventLogger logger,
                final String name,
                final WrappedLoggerFactory ref) {
            logRef = new LogRef(name, unwrapLogger(logger), ref);
        }

        @Override
        public ILogger createChildLogger(final String name) {
            return this;
        }

        // update logger if user set up new factory at a runtime
        private void updateLogger() {
            final WrappedLoggerFactory factory = LmsLoggerFactory.lmsLoggerFactory;
            final LogRef ref = this.logRef;

            if (factory != null && factory != ref.getFactory()
                    && (ref.getFactory() == null || factory != ref.getFactory().getUnwrappedLoggerFactory())) {
                LmsEventLogger logger = SilentLmsEventLogger.SINGLETON;
                final String name = ref.loggerName();

                try {
                    logger = factory.getNotWrappedLogger(name);
                } catch (final Throwable thr) {
                    LOG.error("Exception getting LmsEventLogger", thr);
                } finally {
                    logRef = new LogRef(name, logger, factory);
                }
            }
        }

        @Override
        public void log(
                final LogCategory category,
                final LmsMessageTemplate key,
                final Object... args) {
            updateLogger();
            logRef.getLogger().log(category, key, args);
        }

        @Override
        public void log(
                final LogCategory category,
                final int key,
                final Object... args) {
            updateLogger();
            logRef.getLogger().log(category, key, args);
        }

        @Override
        public void log(
                final LmsMessageTemplate key,
                final Object... args) {
            updateLogger();
            logRef.getLogger().log(key, args);
        }

        @Override
        public void log(
                final int key,
                final Object... args) {
            updateLogger();
            logRef.getLogger().log(key, args);
        }


        @Override
        public boolean isDebug() {
            updateLogger();
            return logRef.getLogger().isDebug();
        }

        @Override
        public void debug(final Object message) {
            updateLogger();
            logRef.getLogger().debug(message);
        }

        @Override
        public void debug(final Object message, final Throwable thr) {
            updateLogger();
            logRef.getLogger().debug(message, thr);
        }

        @Override
        public void debugFormat(final String message, final Object args) {
            updateLogger();
            logRef.getLogger().debugFormat(message, args);
        }

        @Override
        public void debug(final LmsMessageTemplate key, final Object... args) {
            updateLogger();
            logRef.getLogger().debug(key, args);
        }

        @Override
        public void debug(final LogCategory category, final LmsMessageTemplate key, final Object... args) {
            updateLogger();
            logRef.getLogger().debug(category, key, args);
        }

        @Override
        public void debug(final String message, final Object... args) {
            updateLogger();
            logRef.getLogger().debug(message, args);
        }


        @Override
        public void trace(final String message, final Object... args) {
            updateLogger();
            logRef.getLogger().trace(message, args);
        }


        @Override
        public boolean isInfo() {
            updateLogger();
            return logRef.getLogger().isInfo();
        }

        @Override
        public void info(final Object message) {
            updateLogger();
            logRef.getLogger().info(message);
        }

        @Override
        public void info(final Object message, final Throwable thr) {
            updateLogger();
            logRef.getLogger().info(message, thr);
        }

        @Override
        public void infoFormat(final String message, final Object args) {
            updateLogger();
            logRef.getLogger().infoFormat(message, args);
        }

        @Override
        public void info(final LmsMessageTemplate key, final Object... args) {
            updateLogger();
            logRef.getLogger().info(key, args);
        }

        @Override
        public void info(final LogCategory category, final LmsMessageTemplate key, final Object... args) {
            updateLogger();
            logRef.getLogger().info(category, key, args);
        }

        @Override
        public void info(final String message, final Object... args) {
            updateLogger();
            logRef.getLogger().info(message, args);
        }


        @Override
        public boolean isWarn() {
            updateLogger();
            return logRef.getLogger().isWarn();
        }

        @Override
        public void warn(final Object message) {
            updateLogger();
            logRef.getLogger().warn(message);
        }

        @Override
        public void warn(final Object message, final Throwable thr) {
            updateLogger();
            logRef.getLogger().warn(message, thr);
        }

        @Override
        public void warnFormat(final String message, final Object args) {
            updateLogger();
            logRef.getLogger().warnFormat(message, args);
        }

        @Override
        public void warn(final LmsMessageTemplate key, final Object... args) {
            updateLogger();
            logRef.getLogger().warn(key, args);
        }

        @Override
        public void warn(final LogCategory category, final LmsMessageTemplate key, final Object... args) {
            updateLogger();
            logRef.getLogger().warn(category, key, args);
        }

        @Override
        public void warn(final String message, final Object... args) {
            updateLogger();
            logRef.getLogger().warn(message, args);
        }


        @Override
        public boolean isError() {
            updateLogger();
            return logRef.getLogger().isError();
        }

        @Override
        public void error(final Object message) {
            updateLogger();
            logRef.getLogger().error(message);
        }

        @Override
        public void error(final Object message, final Throwable thr) {
            updateLogger();
            logRef.getLogger().error(message, thr);
        }

        @Override
        public void errorFormat(final String message, final Object args) {
            updateLogger();
            logRef.getLogger().errorFormat(message, args);
        }

        @Override
        public void error(final LmsMessageTemplate key, final Object... args) {
            updateLogger();
            logRef.getLogger().error(key, args);
        }

        @Override
        public void error(final LogCategory category, final LmsMessageTemplate key, final Object... args) {
            updateLogger();
            logRef.getLogger().error(category, key, args);
        }

        @Override
        public void error(final String message, final Object... args) {
            updateLogger();
            logRef.getLogger().error(message, args);
        }


        @Override
        public boolean isFatalError() {
            updateLogger();
            return logRef.getLogger().isFatalError();
        }

        @Override
        public void fatalError(final Object message) {
            updateLogger();
            logRef.getLogger().fatalError(message);
        }

        @Override
        public void fatalError(final Object message, final Throwable thr) {
            updateLogger();
            logRef.getLogger().fatalError(message, thr);
        }

        @Override
        public void fatalErrorFormat(final String message, final Object args) {
            updateLogger();
            logRef.getLogger().fatalErrorFormat(message, args);
        }

        @Override
        public void fatalError(final LmsMessageTemplate key, final Object... args) {
            updateLogger();
            logRef.getLogger().fatalError(key, args);
        }

        @Override
        public void fatalError(final LogCategory category, final LmsMessageTemplate key, final Object... args) {
            updateLogger();
            logRef.getLogger().fatalError(category, key, args);
        }

        @Override
        public void fatal(final LmsMessageTemplate key, final Object... args) {
            updateLogger();
            logRef.getLogger().fatal(key, args);
        }

        @Override
        public void fatal(final LogCategory category, final LmsMessageTemplate key, final Object... args) {
            updateLogger();
            logRef.getLogger().fatal(category, key, args);
        }

        @Override
        public void fatal(final String message, final Object... args) {
            updateLogger();
            logRef.getLogger().fatal(message, args);
        }


        /**
         * Internal helper structure to keep reference to current log factory for particular logger.
         */
        private static class LogRef implements java.io.Serializable {

            private static final long serialVersionUID = 9083969754116572204L;

            protected final String               name;
            protected final transient LmsEventLogger       logger;
            protected final transient WrappedLoggerFactory factoryRef;

            public LogRef(
                    final String name,
                    final LmsEventLogger logger,
                    final WrappedLoggerFactory factoryRef) {
                this.name = name;
                this.logger = logger;
                this.factoryRef = factoryRef;
            }

            public WrappedLoggerFactory getFactory() {
                return this.factoryRef; 
            }

            public LmsEventLogger getLogger() {
                return this.logger; 
            }

            public String loggerName() {
                return this.name; 
            }
        }
    }

    protected static LmsEventLogger unwrapLogger(final LmsEventLogger logger) {
        LmsEventLogger res = logger;
        while (res instanceof WrappedLogger) {
            LmsEventLogger logR = ((WrappedLogger) res).logRef.logger;
            if (logR != null) {
                res = logR;
            }
        }
        return res;
    }
}

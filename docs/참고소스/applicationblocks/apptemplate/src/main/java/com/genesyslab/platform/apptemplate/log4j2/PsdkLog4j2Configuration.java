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
package com.genesyslab.platform.apptemplate.log4j2;

import com.genesyslab.platform.apptemplate.configuration.IGApplicationConfiguration;

import com.genesyslab.platform.apptemplate.configuration.log.TargetType;
import com.genesyslab.platform.apptemplate.configuration.log.VerboseLevel;
import com.genesyslab.platform.apptemplate.configuration.log.TargetDescriptor;
import com.genesyslab.platform.apptemplate.configuration.log.GAppLoggingOptions;
import com.genesyslab.platform.apptemplate.configuration.log.GFileTargetDescriptor;
import com.genesyslab.platform.apptemplate.configuration.log.CustomLoggerExtConfig;
import com.genesyslab.platform.apptemplate.configuration.log.ExpirationConfig;
import com.genesyslab.platform.apptemplate.configuration.log.ExpirationConfig.ExpirationStrategy;
import com.genesyslab.platform.apptemplate.configuration.log.GAppLogExtOptions;
import com.genesyslab.platform.apptemplate.configuration.log.SegmentationConfig;
import com.genesyslab.platform.apptemplate.configuration.log.SegmentationConfig.SegmentationStrategy;

import com.genesyslab.platform.apptemplate.log4j2plugin.GLmsLayout;
import com.genesyslab.platform.apptemplate.log4j2plugin.GLogFileAppender;
import com.genesyslab.platform.apptemplate.log4j2plugin.FileHeaderProvider;
import com.genesyslab.platform.apptemplate.log4j2plugin.GLogRolloverStrategy;
import com.genesyslab.platform.apptemplate.log4j2plugin.GLogSegmentationStrategy;
import com.genesyslab.platform.apptemplate.log4j2plugin.GMessageServerAppender;

import com.genesyslab.platform.configuration.protocol.types.CfgAppType;

import com.genesyslab.platform.commons.collections.KeyValueCollection;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.xml.XmlConfiguration;
import org.apache.logging.log4j.core.config.plugins.util.PluginType;
import org.apache.logging.log4j.core.config.plugins.util.PluginRegistry;

import java.util.Map;
import java.util.List;
import java.util.Locale;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

import java.net.URL;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.ByteArrayInputStream;


/**
 * Log4j2 logging configuration structure implementation
 * with initialization from Genesys CME Application logging settings.
 * <p/>
 * It's designed for usage by {@link Log4j2Configurator}.
 *
 * @see Log4j2Configurator
 * @see com.genesyslab.platform.apptemplate.configuration.GApplicationConfiguration GApplicationConfiguration
 */
public class PsdkLog4j2Configuration
        extends XmlConfiguration
        implements java.io.Serializable {

    private static final long serialVersionUID = -4896268368613853320L;

    private static final String CONF_APPTPL_PLUGIN_PACKAGE =
            "com.genesyslab.platform.apptemplate.log4j2plugin";

    private static final String PLUGIN_NAME_APPENDERS    = "Appenders";
    private static final String PLUGIN_NAME_CONSOLE      =   "Console";

    private static final String PLUGIN_NAME_LOGGER           = "Logger";
    private static final String PLUGIN_NAME_LOGGERS          = "Loggers";
    private static final String PLUGIN_NAME_ROOT_LOGGER      =   "Root";
    private static final String PLUGIN_NAME_ASYNCROOT_LOGGER =   "AsyncRoot";
    private static final String PLUGIN_NAME_APPENDER_REF     =     "AppenderRef";

    private static final HashSet<String> CUSTOM_LOGGER_ATTRIBUTES;

    static {
        CUSTOM_LOGGER_ATTRIBUTES = new HashSet<String>();
        CUSTOM_LOGGER_ATTRIBUTES.add("level");
        CUSTOM_LOGGER_ATTRIBUTES.add("additivity");
        CUSTOM_LOGGER_ATTRIBUTES.add("includeLocation");
    }


    public static final String PSDK_APPTPL_APPENDER_PREFIX   = "PSDKAppTpl-";
    public static final String PSDK_APPTPL_MS_APPENDER_NAME  = "PSDKAppTpl-MessageServer";

    public static final String LOG_EXT_SECTION_NAME = "log-extended";
    public static final String LOG_EXT_LOGGER_PREFIX = "logger-";


    private final IGApplicationConfiguration theAppConfig;
    private final GAppLoggingOptions         theLogOptions;


    /**
     * Internal configuration structure constructor.
     *
     * @param configSource the configuration profile source;
     *            may be {@link #NULL_CONFIG_SOURCE} if no configuration profile used.
     * @param appConfig the application configuration source.
     * @param logOpts the application logging options initialized from the "<code>log</code>" section.
     * @see #parse(IGApplicationConfiguration)
     * @see #parse(KeyValueCollection)
     */
    protected PsdkLog4j2Configuration(
            final ConfigurationSource configSource,
            final IGApplicationConfiguration appConfig,
            final GAppLoggingOptions logOpts) {
        super((LoggerContext) Log4j2Configurator.getLoggerContext(), configSource);
        this.theAppConfig = appConfig;
        this.theLogOptions = logOpts;
    }

    /**
     * The configuration structure constructor.
     *
     * @param logOptions the application logging options.
     * @throws NullPointerException if given <code>logOptions</code> value is <code>null</code>.
     */
    public PsdkLog4j2Configuration(
            final GAppLoggingOptions logOptions) {
        super((LoggerContext) Log4j2Configurator.getLoggerContext(),
                getConfigProfile(logOptions));
        this.theAppConfig = null;
        this.theLogOptions = logOptions;
    }


    private static ConfigurationSource getConfigProfile(
            final GAppLoggingOptions logOptions) {
        if (logOptions == null) {
            throw new NullPointerException("logOptions");
        }
        ConfigurationSource res = null;
        final String profile = logOptions.getLog4j2ConfigProfile();
        try {
            if (profile != null && !profile.isEmpty()) {
                final File file = new File(profile);
                if (file.isFile() && file.canRead() && file.length() > 0) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Trying to read Log4j2 config profile: " + file.getAbsolutePath());
                    }
                    res = new ConfigurationSource(new FileInputStream(file), file);
                }
                if (res == null) {
                    final URL url = ClassLoader.getSystemResource(profile);
                    if (url != null) {
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("Trying to read Log4j2 config profile rs: " + url.toString());
                        }
                        res = new ConfigurationSource(url.openStream(), url);
                    } else {
                        LOGGER.error("Error getting Log4j2 configuration profile: no file or resource found ("
                                + profile + ")");
                    }
                }
            }
            if (res == null) {
                res = getEmptyConfigSource();
            }
        } catch (final Exception ex) {
            LOGGER.error("Failed to get Log4j2 configuration profile", ex);
        }
        return res;
    }


    /**
     * Returns the source application configuration.
     * It may be <code>null</code> if this instance was created by logging options without
     * application configuration providing.
     *
     * @return the application configuration or <code>null</code>.
     */
    public IGApplicationConfiguration getAppConfig() {
        return theAppConfig;
    }

    /**
     * Returns the source logging configuration options.
     *
     * @return the logging configuration options.
     */
    public GAppLoggingOptions getLogOptions() {
        return theLogOptions;
    }


    /**
     * Helper method for making Log4j2 logging configuration by given
     * Genesys "log" section style configuration options.
     *
     * @param logOptions the source logging configuration options.
     * @return Log4j2 configuration structure.
     * @throws NullPointerException if given <code>logOptions</code> value is <code>null</code>.
     */
    public static PsdkLog4j2Configuration parse(
            final KeyValueCollection logOptions) {
        if (logOptions == null) {
            throw new NullPointerException("logOptions");
        }
        final GAppLoggingOptions logOpts = new GAppLoggingOptions(logOptions, null);
        return new PsdkLog4j2Configuration(
                getConfigProfile(logOpts), null, logOpts);
    }

    /**
     * Helper method for making Log4j2 logging configuration by given application configuration.
     *
     * @param appConfig the source application configuration.
     * @return Log4j2 configuration structure.
     * @throws NullPointerException if given <code>appConfig</code> value is <code>null</code>.
     */
    public static PsdkLog4j2Configuration parse(
            final IGApplicationConfiguration appConfig) {
        if (appConfig == null) {
            throw new NullPointerException("appConfig");
        }
        final GAppLoggingOptions logOpts = new GAppLoggingOptions(appConfig, null);
        return new PsdkLog4j2Configuration(
                    getConfigProfile(logOpts), appConfig, logOpts);
    }


    @Override
    public void initialize() {
        if (!pluginPackages.contains(CONF_APPTPL_PLUGIN_PACKAGE)) {
            pluginPackages.add(CONF_APPTPL_PLUGIN_PACKAGE);
        }
        super.initialize();
    }

    @Override
    public void setup() {

        // "Appenders" section:
        Node appsNode = null;
        // "Loggers" section:
        Node loggersNode = null;
        // The Root Logger:
        Node rootLogger = null;

        // The application logger(s) (subset of the all configuration loggers):
        final LinkedList<Node> appLoggers = new LinkedList<Node>();

        boolean setRootLogLevel = false;

        if (getConfigurationSource() != ConfigurationSource.NULL_SOURCE) {
            super.setup();
            // remove "startup" appender(s) and collect list of "affected" loggers:
            setupRemoveStartupAppenders(appLoggers);

            for (final Node node: rootNode.getChildren()) {
                if (PLUGIN_NAME_APPENDERS.equalsIgnoreCase(node.getName())) {
                    appsNode = node;
                } else if (PLUGIN_NAME_LOGGERS.equalsIgnoreCase(node.getName())) {
                    loggersNode = node;
                    for (final Node loggerNode: loggersNode.getChildren()) {
                        if (PLUGIN_NAME_ROOT_LOGGER.equalsIgnoreCase(loggerNode.getName())
                                || PLUGIN_NAME_ASYNCROOT_LOGGER.equalsIgnoreCase(loggerNode.getName())) {
                            rootLogger = loggerNode;
                        }
                    }
                }
            }
        }

        // "Appenders" section:
        if (appsNode == null) {
            appsNode = new Node(rootNode, PLUGIN_NAME_APPENDERS,
                    pluginManager.getPluginType(PLUGIN_NAME_APPENDERS));
            rootNode.getChildren().add(appsNode);
        }

        // "Loggers" section:
        if (loggersNode == null) {
            loggersNode = new Node(rootNode, PLUGIN_NAME_LOGGERS,
                    pluginManager.getPluginType(PLUGIN_NAME_LOGGERS));
            rootNode.getChildren().add(loggersNode);
        }

        // Create custom loggers by the 'log-extended' section:
        setupCustomLoggers(theLogOptions.getLogExtendedOptions(), loggersNode);

        // The Root Logger:
        Boolean enableThread = theLogOptions.getEnableThread();
        if (enableThread != null && enableThread) {
            boolean haveDisruptor;
            try {
                haveDisruptor = (Class.forName("com.lmax.disruptor.EventFactory") != null);
            } catch(final Exception ex) {
                haveDisruptor = false;
            }
            if (!haveDisruptor) {
                LOGGER.warn("Unable to use asynchronous loggers (option \"log\"/\"enable-thread\"): LMAX Disruptor library is not available");
                enableThread = null;
            }
        }

        if (rootLogger == null) {
            setRootLogLevel = !loggersNode.hasChildren();
            final String rootLoggerDef = (enableThread != null && enableThread)
                    ? PLUGIN_NAME_ASYNCROOT_LOGGER
                    : PLUGIN_NAME_ROOT_LOGGER;
            rootLogger = new Node(loggersNode, rootLoggerDef,
                    pluginManager.getPluginType(rootLoggerDef));
            loggersNode.getChildren().add(rootLogger);
        } else {
            Node newRootLogger = null;
            if (PLUGIN_NAME_ASYNCROOT_LOGGER.equalsIgnoreCase(rootNode.getName())) {
                if (enableThread != null && !enableThread) {
                    // the application configuration switches off thread usage: 
                    newRootLogger = new Node(loggersNode, PLUGIN_NAME_ROOT_LOGGER,
                            pluginManager.getPluginType(PLUGIN_NAME_ROOT_LOGGER));
                }
            } else if (PLUGIN_NAME_ROOT_LOGGER.equalsIgnoreCase(rootNode.getName())) {
                if (enableThread != null && enableThread) {
                    // the application configuration switches on thread usage: 
                    newRootLogger = new Node(loggersNode, PLUGIN_NAME_ASYNCROOT_LOGGER,
                            pluginManager.getPluginType(PLUGIN_NAME_ASYNCROOT_LOGGER));
                }
            }
            if (newRootLogger != null) {
                newRootLogger.getAttributes().putAll(rootLogger.getAttributes());
                newRootLogger.getChildren().addAll(rootLogger.getChildren());
                final List<Node> loggers = loggersNode.getChildren();
                for (int i = 0; i < loggers.size(); i++) {
                    if (loggers.get(i) == rootLogger) {
                        loggers.set(i, newRootLogger);
                        break;
                    }
                }
            }
        }

        VerboseLevel rootLoggerVerbosity = theLogOptions.getVerbose();
        if (rootLoggerVerbosity == null) {
            rootLoggerVerbosity = VerboseLevel.STANDARD;
        }
        if (setRootLogLevel) {
            rootLogger.getAttributes().put("level", logLevelByVerbosity(rootLoggerVerbosity));
        }
        if (enableThread != null && enableThread) {
            rootLogger.getAttributes().put("includeLocation",
                    theLogOptions.getIncludeLocation().toString());
        }

        if (appLoggers.isEmpty()) {
            appLoggers.add(rootLogger);
        }

        setupNewAppenders(theLogOptions.getOutputDescriptors(),
                rootLoggerVerbosity, appsNode, appLoggers);
    }

    @Override
    public void start() {
        super.start();
        getAppenders().values().forEach(app -> {
            Layout<?> l = app.getLayout();
            if (l instanceof GLmsLayout) {
                final FileHeaderProvider fhp = ((GLmsLayout) l).getFileHeaderProvider();
                if (fhp instanceof GFileHeaderProvider) {
                    ((GFileHeaderProvider) fhp).setAppConfiguration(theAppConfig);
                }
            }
        });
    }

    @Override
    public void stop() {
        LOGGER.debug("Stopping configuration {}", this);
        super.stop();
        getAppenders().values().forEach(app -> {
            if (app instanceof GMessageServerAppender) app.stop();
        });
    }

    protected void setupRemoveStartupAppenders(
            final List<Node> appLoggers) {
        for (final Node node: rootNode.getChildren()) {
            if (PLUGIN_NAME_APPENDERS.equalsIgnoreCase(node.getName())) {
                final Iterator<Node> appenders = node.getChildren().iterator();
                while (appenders.hasNext()) {
                    final Node appenderNode = appenders.next();
                    final String name = appenderNode.getAttributes().get("name");
                    if (name != null && name.startsWith(PSDK_APPTPL_APPENDER_PREFIX)) {
                        LOGGER.info("Removing application startup log appender '" + name + "'");
                        appenders.remove();
                    }
                }
            } else if (PLUGIN_NAME_LOGGERS.equalsIgnoreCase(node.getName())) {
                final Iterator<Node> loggers = node.getChildren().iterator();
                while (loggers.hasNext()) {
                    final Node loggerNode = loggers.next();
                    final Iterator<Node> children = loggerNode.getChildren().iterator();
                    while (children.hasNext()) {
                        final Node child = children.next();
                        if (child.getName().equalsIgnoreCase(PLUGIN_NAME_APPENDER_REF)) {
                            final String name = child.getAttributes().get("ref");
                            if (name != null && name.startsWith(PSDK_APPTPL_APPENDER_PREFIX)) {
                                if (LOGGER.isInfoEnabled()) {
                                    String loggerName = loggerNode.getAttributes().get("name");
                                    if (loggerName == null || loggerName.isEmpty()) {
                                        loggerName = loggerNode.getName();
                                    }
                                    LOGGER.info("Removing application startup log appender ref '"
                                            + name + "' at '" + loggerName + "'");
                                }
                                children.remove();
                                if (!appLoggers.contains(loggerNode)) {
                                    appLoggers.add(loggerNode);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    protected void setupCustomLoggers(
            final GAppLogExtOptions logExtOptions,
            final Node loggersNode) {
        Map<String, CustomLoggerExtConfig> customLoggers = null;
        if (logExtOptions != null) {
            customLoggers = logExtOptions.getCustomLoggers();
        }
        if (customLoggers != null) {
            for (final CustomLoggerExtConfig loggerConf: customLoggers.values()) {
                final String loggerName = loggerConf.getName();
                Node loggerNode = null;
                for (final Node child: loggersNode.getChildren()) {
                    if (loggerName.equals(child.getAttributes().get("name"))) {
                        loggerNode = child;
                        break;
                    }
                }
                if (loggerNode == null) {
                    loggerNode = new Node(loggersNode, PLUGIN_NAME_LOGGER,
                            pluginManager.getPluginType(PLUGIN_NAME_LOGGER));
                    loggerNode.getAttributes().put("name", loggerName);
                    loggersNode.getChildren().add(loggerNode);
                }
                final Map<String, String> loggerProps = loggerConf.getProperties();
                if (loggerProps != null && !loggerProps.isEmpty()) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Applying custom logger properties on '" + loggerName + "'");
                    }
                    for (Map.Entry<String, String> prop: loggerProps.entrySet()) {
                        if (CUSTOM_LOGGER_ATTRIBUTES.contains(prop.getKey())) {
                            loggerNode.getAttributes().put(prop.getKey(), prop.getValue());
                        } else {
                            if (LOGGER.isWarnEnabled()) {
                                LOGGER.warn("Unsupported custom logger property on '" + loggerName
                                        + "' - \"" + prop.getKey() + '"');
                            }
                        }
                    }
                }
            }
        }
    }


    private static final AtomicInteger appenderId = new AtomicInteger();

    protected void setupNewAppenders(
            final List<TargetDescriptor> targets,
            final VerboseLevel rootLoggerVerbosity,
            final Node appsNode,
            final List<Node> appLoggers) {
        if (targets == null || targets.isEmpty()) {
            return;
        }

        String fileAppName = null;

        for (TargetDescriptor tgt: targets) {
            if (tgt instanceof GFileTargetDescriptor) {
                if (fileAppName == null) {
                    fileAppName = PSDK_APPTPL_APPENDER_PREFIX + "LogSectionLogFile-" + appenderId.incrementAndGet();
                }
                final Node fileAppender = createFileAppenderConfigNode(appsNode, fileAppName,
                        (GFileTargetDescriptor) tgt);
                if (fileAppender != null) {
                    appsNode.getChildren().add(fileAppender);
                    addAppenderRef(appLoggers, fileAppender, rootLoggerVerbosity);
                    fileAppName = null;
                }

            } else if (tgt.getTargetType() == TargetType.STDOUT) {
                final Node consAppender = createConsoleAppenderConfigNode(appsNode, tgt);
                if (consAppender != null) {
                    appsNode.getChildren().add(consAppender);
                    addAppenderRef(appLoggers, consAppender, rootLoggerVerbosity);
                }

            } else if (tgt.getTargetType() == TargetType.STDERR) {
                final Node consAppender = createConsoleAppenderConfigNode(appsNode, tgt);
                if (consAppender != null) {
                    appsNode.getChildren().add(consAppender);
                    addAppenderRef(appLoggers, consAppender, rootLoggerVerbosity);
                }

            } else if (tgt.getTargetType() == TargetType.MESSAGESERVER) {
                final Node msAppender = createGMSAppenderConfigNode(appsNode);
                if (msAppender != null) {
                    appsNode.getChildren().add(msAppender);
                    addAppenderRef(appLoggers, msAppender,
                            maxLevel(rootLoggerVerbosity, tgt.getSupportedVerboseLevel()));
                }
            }
        }
    }

    private VerboseLevel maxLevel(
            final VerboseLevel l1,
            final VerboseLevel l2) {
        if (l1 == null) {
            return l2;
        }
        if (l2 == null) {
            return l1;
        }
        return l1.ordinal() > l2.ordinal() ? l1 : l2;
    }


    @Override
    public Configuration reconfigure() {
        try {
            ConfigurationSource source = getConfigurationSource();
            if (source != null) {
                if (source != ConfigurationSource.NULL_SOURCE) {
                    if (source.getFile() == null && source.getURL() == null
                            && source.getLocation() == null) {
                        final InputStream is = source.getInputStream();
                        if (is != null) {
                            is.reset();
                        }
                    } else {
                        source = source.resetInputStream();
                    }
                }
            } else {
                source = getEmptyConfigSource();
            }
            return new PsdkLog4j2Configuration(
                    source, theAppConfig, theLogOptions);
        } catch (final IOException ex) {
            LOGGER.error("Cannot locate file {}", getConfigurationSource(), ex);
        }
        return null;
    }


    @SuppressWarnings("unchecked")
    protected <PluginClass> PluginType<PluginClass> getAppTemplatePlugin(
            final String   typename,
            final Class<PluginClass> pluginClass) {
        final PluginType<PluginClass> pType = (PluginType<PluginClass>)
                pluginManager.getPluginType(typename);
        if (pType != null) {
            return pType;
        }
        final PluginRegistry preg = PluginRegistry.getInstance();
        final Map<String, List<PluginType<?>>> plugs = preg.loadFromPackage(
                pluginClass.getPackage().getName());
        final List<PluginType<?>> msappTypes = plugs.get("core");
        if (msappTypes != null && !msappTypes.isEmpty()) {
            for (PluginType<?> plg: msappTypes) {
                if (pluginClass.isAssignableFrom(
                        plg.getPluginClass())) {
                    return (PluginType<PluginClass>) plg;
                }
            }
        }
        LOGGER.error("AppTemplate Log4j2 plugin type '{}' is not available", typename);
        return null;
    }

    private void addPropertyNode(
            final Node parentNode,
            final String name,
            final String value,
            final boolean isRequired) {
        if (value == null) {
            if (isRequired) {
                throw new IllegalArgumentException(
                        "Missing required parameter '" + name + "'");
            }
        } else {
            final Node propertyNode = new Node(parentNode, "property",
                    pluginManager.getPluginType("property"));
            propertyNode.getAttributes().put("name", name);
            propertyNode.setValue(value);

            parentNode.getChildren().add(propertyNode);
        }
    }

    private void addAppenderRef(
            final List<Node> loggersNodes,
            final Node appenderNode,
            final VerboseLevel verbosity) {
        if (verbosity == VerboseLevel.NONE) {
            return;
        }
        if (loggersNodes != null && appenderNode != null) {
            final String appName = appenderNode.getAttributes().get("name");
            final PluginType<?> pluginType = pluginManager.getPluginType(PLUGIN_NAME_APPENDER_REF);
            for (final Node loggerNode: loggersNodes) {
                Node appRef = new Node(loggerNode, PLUGIN_NAME_APPENDER_REF, pluginType);
                appRef.getAttributes().put("ref", appName);
                if (verbosity != null) {
                    appRef.getAttributes().put("level", logLevelByVerbosity(verbosity));
                }
                loggerNode.getChildren().add(appRef);
            }
        }
    }


    protected Node createConsoleAppenderConfigNode(
            final Node parentNode,
            final TargetDescriptor target) {
        final Node consAppender = new Node(parentNode, PLUGIN_NAME_CONSOLE,
                pluginManager.getPluginType(PLUGIN_NAME_CONSOLE));

        final TargetType tgtType = target.getTargetType();
        consAppender.getAttributes().put("name", PSDK_APPTPL_APPENDER_PREFIX
                + (tgtType != null ? tgtType.name().toLowerCase(Locale.ENGLISH) : "null"));

        consAppender.getChildren().add(createLayoutNode(consAppender));

        if (target.getTargetType() == TargetType.STDOUT) {
            consAppender.getAttributes().put("target", "SYSTEM_OUT");
        } else if (target.getTargetType() == TargetType.STDERR) {
            consAppender.getAttributes().put("target", "SYSTEM_ERR");
        }

        if (target.getSupportedVerboseLevel() != null) {
            final Node filter = new Node(consAppender, "ThresholdFilter",
                    pluginManager.getPluginType("ThresholdFilter"));
            filter.getAttributes().put("level", logLevelByVerbosity(target.getSupportedVerboseLevel()));
            consAppender.getChildren().add(filter);
        }

        return consAppender;
    }

    protected Node createFileAppenderConfigNode(
            final Node parentNode,
            final String appName,
            final GFileTargetDescriptor target) {
        String fileName = target.getFilename();
        if (fileName == null || fileName.isEmpty()) {
            LOGGER.warn("No filename in the file target descriptor");
            return null;
        }
        if (!fileName.endsWith(".log")) {
            fileName = fileName + ".log";
        }

        final PluginType<GLogFileAppender> lfType =
                getAppTemplatePlugin("GLogFile", GLogFileAppender.class);
        if (lfType == null) {
            return null;
        }

        final Node fileAppender = new Node(parentNode, "GLogFile", lfType);
        fileAppender.getAttributes().put("name", appName);
        fileAppender.getAttributes().put("fileName", fileName);
        final String logdir = theLogOptions.getDefaultLogdir();
        if (logdir != null) {
            fileAppender.getAttributes().put("defaultLogdir", logdir);
        }
        fileAppender.getAttributes().put("charset", theLogOptions.getFileEncoding());

        if (target.getSupportedVerboseLevel() != null) {
            final Node filter = new Node(fileAppender, "ThresholdFilter",
                    pluginManager.getPluginType("ThresholdFilter"));
            filter.getAttributes().put("level",
                    logLevelByVerbosity(target.getSupportedVerboseLevel()));
            fileAppender.getChildren().add(filter);
        }

        SegmentationConfig segmCfg = theLogOptions.getSegment();
        SegmentationStrategy segmStr = null;
        if (segmCfg != null) {
            segmStr = segmCfg.getStrategy();
        }
        if (segmStr == null) {
            segmStr = SegmentationStrategy.OFF;
        }
        final Node expStrat = new Node(fileAppender, "GLogSegmentationStrategy",
                getAppTemplatePlugin("GLogSegmentationStrategy", GLogSegmentationStrategy.class));
        expStrat.getAttributes().put("segmStrategy", segmStr.name());
        expStrat.getAttributes().put("segmSize",
                Integer.toString(segmCfg != null ? segmCfg.getSegment() : 0));
        fileAppender.getChildren().add(expStrat);

        ExpirationConfig expCfg = theLogOptions.getExpire();
        ExpirationStrategy expStr = null;
        if (expCfg != null) {
            expStr = expCfg.getStrategy();
        }
        if (expStr == null) {
            expStr = ExpirationStrategy.OFF;
        }
        final Node roStrat = new Node(fileAppender, "GLogRolloverStrategy",
                getAppTemplatePlugin("GLogRolloverStrategy", GLogRolloverStrategy.class));
        roStrat.getAttributes().put("expStrategy", expStr.name());
        roStrat.getAttributes().put("expLimit",
                Integer.toString(expCfg != null ? expCfg.getExpire() : 0));
        final String compress = theLogOptions.getCompressMethod();
        if (compress != null) {
            roStrat.getAttributes().put("compression", compress);
        }
        fileAppender.getChildren().add(roStrat);

        fileAppender.getChildren().add(createLayoutNode(fileAppender));

        return fileAppender;
    }

    protected Node createGMSAppenderConfigNode(
            final Node parentNode) {
        Node msAppender = null;

        final PluginType<GMessageServerAppender> msappType =
                getAppTemplatePlugin("GMessageServer", GMessageServerAppender.class);

        if (msappType != null) {
            LOGGER.info("GMessageServerAppender plugin class: {}",
                    msappType.getPluginClass().getName());

            msAppender = new Node(parentNode, msappType.getElementName(), msappType);
            msAppender.getAttributes().put("name", PSDK_APPTPL_MS_APPENDER_NAME);
            msAppender.getAttributes().put("bufferSize", theLogOptions.getMsBufferSize().toString());

            if (theAppConfig != null) {
                addPropertyNode(msAppender, "ClientName",
                        theAppConfig.getApplicationName(), true);
                CfgAppType appType = theAppConfig.getApplicationType();
                addPropertyNode(msAppender, "ClientType",
                        appType != null ? appType.asInteger().toString() : null, true);
                addPropertyNode(msAppender, "ClientId",
                        theAppConfig.getDbid() != null ? theAppConfig.getDbid().toString() : null, true);
                addPropertyNode(msAppender, "ClientHost",
                        theLogOptions.getEventlogHost(), true);
            }
            String intLvl = theLogOptions.getMsgsrvIntmsgsLevel();
            if (intLvl != null && !intLvl.isEmpty()) {
                Level lvl = Level.toLevel(intLvl, null);
                if (lvl != null) {
                    if (lvl.isLessSpecificThan(Level.DEBUG)) {
                        LOGGER.warn("GMessageServerAppender: too low level for 'MsgsrvIntmsgsLevel' ("
                                + lvl + ") - switching to INFO");
                        lvl = Level.INFO;
                    }
                } else {
                    LOGGER.warn("GMessageServerAppender: invalid value for 'MsgsrvIntmsgsLevel'");
                }
                if (lvl != null) {
                    addPropertyNode(msAppender, GMessageServerAppender.PROP_INT_MSGS_LEVEL, lvl.name(), false);
                }
            }
        } else {
            LOGGER.error("GMessageServerAppender plugin is not available");
        }

        return msAppender;
    }

    protected Node createLayoutNode(
            final Node parentAppender) {
        final Node fileLayout = new Node(parentAppender, "GLmsLayout",
                getAppTemplatePlugin("GLmsLayout", GLmsLayout.class));

        if (theAppConfig != null) {
            fileLayout.getAttributes().put("appName", theAppConfig.getApplicationName());

            final CfgAppType theAppType = theAppConfig.getApplicationType();
            if (theAppType != null) {
                fileLayout.getAttributes().put("appType", theAppType.asInteger().toString());
            }

            final Integer theAppDbid = theAppConfig.getDbid();
            if (theAppDbid != null) {
                fileLayout.getAttributes().put("appId", theAppDbid.toString());
            }
        }

        final String theLogHost = theLogOptions.getEventlogHost();
        if (theLogHost != null) {
            fileLayout.getAttributes().put("hostName", theLogHost);
        }

        if (theLogOptions.getTimeFormatting() != null) {
            fileLayout.getAttributes().put("timeFormat", theLogOptions.getTimeFormatting().name());
        }
        if (theLogOptions.getTimeConverting() != null) {
            fileLayout.getAttributes().put("timeConversion", theLogOptions.getTimeConverting().name());
        }
        if (theLogOptions.getMessageFormat() != null) {
            fileLayout.getAttributes().put("messageFormat", theLogOptions.getMessageFormat().name());
        }
        if (theLogOptions.getMessageHeaderDelimiter() != null) {
            fileLayout.getAttributes().put("messageHeaderDelimiter", theLogOptions.getMessageHeaderDelimiter());
        }

        final String outputPattern = theLogOptions.getOutputPattern();
        if (outputPattern != null) {
            fileLayout.getAttributes().put("outputPattern", outputPattern);
        }
        final String customMessageFormat = theLogOptions.getCustomMessageFormat();
        if (customMessageFormat != null) {
            fileLayout.getAttributes().put("customMessageFormat", customMessageFormat);
        }
        final Boolean useNativeLevels = theLogOptions.getUseNativeLevels();
        if (useNativeLevels != null) {
            fileLayout.getAttributes().put("useNativeLevels", useNativeLevels.toString());
        }

        final String fileEncoding = theLogOptions.getFileEncoding();
        if (fileEncoding != null) {
            fileLayout.getAttributes().put("charset", fileEncoding);
        }

        final String fileHeaderProvider = theLogOptions.getFileHeaderProvider();
        if (fileHeaderProvider != null) {
            fileLayout.getAttributes().put("fileHeaderProvider", fileHeaderProvider);
        }

        return fileLayout;
    }


    protected static ConfigurationSource getEmptyConfigSource() {
        try {
            return new ConfigurationSource(new ByteArrayInputStream(
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Configuration/>".getBytes()));
        } catch (final IOException ex) {
            throw new RuntimeException("Exception initializing Empty Config Source", ex);
        }
    }


    protected static String logLevelByVerbosity(
            final VerboseLevel level) {
        if (level != null) {
            switch (level) {
            case ALL:
                return Level.ALL.name();
            case DEBUG:
                return Level.DEBUG.name();
            case TRACE:
                return Level.INFO.name();
            case INTERACTION:
                return Level.WARN.name();
            case STANDARD:
                return Level.ERROR.name();
            case ALARM:
                return Level.FATAL.name();
            case NONE:
                return Level.OFF.name();
            default: // just in case...
                return Level.TRACE.name();
            }
        }
        return "";
    }


    @Override
    public String toString() {
        String name = null;
        if (theAppConfig != null && theAppConfig.getApplicationName() != null) {
            name = theAppConfig.getApplicationName();
        }
        if (name != null && !name.isEmpty()) {
            name = "('" + name + "')";
        } else {
            name = "";
        }
        return getClass().getSimpleName() + name + ": " + theLogOptions;
    }
}

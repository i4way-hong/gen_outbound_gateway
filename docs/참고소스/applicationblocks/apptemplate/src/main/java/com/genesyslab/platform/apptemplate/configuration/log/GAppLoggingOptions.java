// ===============================================================================
//  Genesys Platform SDK
// ===============================================================================
//
// Any authorized distribution of any copy of this code (including any related
// documentation) must reproduce the following restrictions, disclaimer and copyright
// notice:
//
// The Genesys name, trademarks and/or logo(s) of Genesys shall not be used to name
// (even as a part of another name), endorse and/or promote products derived from
// this code without prior written permission from Genesys Telecommunications
// Laboratories, Inc.
//
// The use, copy, and/or distribution of this code is subject to the terms of the Genesys
// Developer License Agreement.  This code shall not be used, copied, and/or
// distributed under any other license agreement.
//
// THIS CODE IS PROVIDED BY GENESYS TELECOMMUNICATIONS LABORATORIES, INC.
// ("GENESYS") "AS IS" WITHOUT ANY WARRANTY OF ANY KIND. GENESYS HEREBY
// DISCLAIMS ALL EXPRESS, IMPLIED, OR STATUTORY CONDITIONS, REPRESENTATIONS AND
// WARRANTIES WITH RESPECT TO THIS CODE (OR ANY PART THEREOF), INCLUDING, BUT
// NOT LIMITED TO, IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
// PARTICULAR PURPOSE OR NON-INFRINGEMENT. GENESYS AND ITS SUPPLIERS SHALL
// NOT BE LIABLE FOR ANY DAMAGE SUFFERED AS A RESULT OF USING THIS CODE. IN NO
// EVENT SHALL GENESYS AND ITS SUPPLIERS BE LIABLE FOR ANY DIRECT, INDIRECT,
// CONSEQUENTIAL, ECONOMIC, INCIDENTAL, OR SPECIAL DAMAGES (INCLUDING, BUT
// NOT LIMITED TO, ANY LOST REVENUES OR PROFITS).
//
// Copyright (c) 2009 - 2019 Genesys Telecommunications Laboratories, Inc. All rights reserved.
// ===============================================================================
package com.genesyslab.platform.apptemplate.configuration.log;

import static com.genesyslab.platform.apptemplate.configuration.log.LogOptionsDescriptions.*;

import com.genesyslab.platform.apptemplate.util.ConfigurationUtil;
import com.genesyslab.platform.apptemplate.configuration.IGApplicationConfiguration;

import com.genesyslab.platform.commons.log.ILogger;
import com.genesyslab.platform.commons.log.NullLoggerImpl;

import com.genesyslab.platform.commons.collections.KeyValuePair;
import com.genesyslab.platform.commons.collections.KeyValueCollection;

import java.util.List;
import java.util.Locale;
import java.util.EnumSet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.File;


/**
 * Parser of "log" section of CME Application objects' Options.
 * <p/>
 * This class is automatically used by
 * {@link com.genesyslab.platform.apptemplate.application.GFApplicationConfigurationManager
 * Application Configuration Manager} and
 * {@link com.genesyslab.platform.apptemplate.log4j2.Log4j2Configurator Log4j2 Configurator}.
 * For a case, when application does not use the configuration manager, does need some custom logging configuration,
 * or for some testing purposes, it is possible to use this class to create logging configuration.<br/>
 * Simple application configuration usage:<code><pre>
 * CfgApplication theApp = confService.retrieveObject(
 *         CfgApplication.class, new CfgApplicationQuery(myAppName));
 * GApplicationConfiguration <b>appConfig</b> = new GCOMApplicationConfiguration(theApp);
 *
 * <b>GAppLoggingOptions</b> logOpts = new <b>GAppLoggingOptions</b>(<b>appConfig</b>, null);
 * </pre></code>
 * Or simple initialization without application configuration reading (without ConfService usage):<code><pre>
 * KeyValueCollection <b>logSection</b> = new KeyValueCollection();
 * <b>logSection</b>.addString("verbose", "all");
 * <b>logSection</b>.addString("message-format", "full");
 * <b>logSection</b>.addString("standard", "Log4j2ConfiguratorTest-std");
 * <b>logSection</b>.addString("all", "stdout, Log4j2ConfiguratorTest-all");
 *
 * KeyValueCollection <b>logExtSection</b> = new KeyValueCollection();
 * <b>logExtSection</b>.addString("level-reassign-14005", "ALARM");
 * <b>logExtSection</b>.addString("level-reassign-14006", "ALARM");
 * <b>logExtSection</b>.addString("logger-psdk", "com.genesyslab.platform: level=debug");
 * <b>logExtSection</b>.addString("logger-apache", "org.apache: level=error");
 *
 * <b>GAppLoggingOptions</b> logOpts = new <b>GAppLoggingOptions</b>(<b>logSection</b>, <b>logExtSection</b>, null);
 * </pre></code>
 */
public class GAppLoggingOptions
        implements Cloneable {

    public static final String LOG_SECTION_NAME = "log";
    public static final String LOG_EXT_SECTION_NAME = "log-extended";

    public static final String TARGET_TYPE_STDOUT = "stdout";
    public static final String TARGET_TYPE_STDERR = "stderr";
    public static final String TARGET_TYPE_NETWORK = "network";
    public static final String TARGET_TYPE_MEMORY = "memory";

    private final KeyValueCollection options = new KeyValueCollection();
    private final GAppLogExtOptions extOptions;

    private final transient ILogger logger;

    static {
        // It is to force initialization of the constants map "OptionDescriptor.OPTIONS_MAP":
        LogOptionsDescriptions.CHECK_POINT_OPT.getClass();
        LogOptionsDescriptions.class.getDeclaredFields();
    }


    /**
     * Creates logging options parsing helper class instance by given application configuration.
     * <p/>
     * Simple application configuration usage:<code><pre>
     * CfgApplication theApp = confService.retrieveObject(
     *         CfgApplication.class, new CfgApplicationQuery(myAppName));
     * GApplicationConfiguration <b>appConfig</b> = new GCOMApplicationConfiguration(theApp);
     *
     * <b>GAppLoggingOptions</b> logOpts = new <b>GAppLoggingOptions</b>(<b>appConfig</b>, null);
     * </pre></code>
     *
     * @param appConfig the application configuration.
     * @param logger optional "status" logger to print errors of options parsing methods.
     */
    public GAppLoggingOptions(
            final IGApplicationConfiguration appConfig,
            final ILogger logger) {
        this(getLogSection(appConfig, LOG_SECTION_NAME),
             getLogSection(appConfig, LOG_EXT_SECTION_NAME),
             logger);
    }

    /**
     * Creates logging options parsing helper class instance by given logging configuration options.
     * <p/>
     * Or simple initialization without application configuration reading (without ConfService usage):<code><pre>
     * KeyValueCollection <b>logSection</b> = new KeyValueCollection();
     * <b>logSection</b>.addString("verbose", "all");
     * <b>logSection</b>.addString("message-format", "full");
     * <b>logSection</b>.addString("standard", "Log4j2ConfiguratorTest-std");
     * <b>logSection</b>.addString("all", "stdout, Log4j2ConfiguratorTest-all");
     *
     * <b>GAppLoggingOptions</b> logOpts = new <b>GAppLoggingOptions</b>(<b>logSection</b>, null);
     * </pre></code>
     *
     * @param logOptions the application configuration.
     * @param logger optional "status" logger to print errors of options parsing methods.
     */
    public GAppLoggingOptions(
            final KeyValueCollection logOptions,
            final ILogger logger) {
        this(logOptions, null, logger);
    }

    /**
     * Creates logging options parsing helper class instance by given logging configuration options.
     * <p/>
     * Or simple initialization without application configuration reading (without ConfService usage):<code><pre>
     * KeyValueCollection <b>logSection</b> = new KeyValueCollection();
     * <b>logSection</b>.addString("verbose", "all");
     * <b>logSection</b>.addString("message-format", "full");
     * <b>logSection</b>.addString("standard", "Log4j2ConfiguratorTest-std");
     * <b>logSection</b>.addString("all", "stdout, Log4j2ConfiguratorTest-all");
     *
     * KeyValueCollection <b>logExtSection</b> = new KeyValueCollection();
     * <b>logExtSection</b>.addString("level-reassign-14005", "ALARM");
     * <b>logExtSection</b>.addString("level-reassign-14006", "ALARM");
     * <b>logExtSection</b>.addString("logger-psdk", "com.genesyslab.platform: level=debug");
     * <b>logExtSection</b>.addString("logger-apache", "org.apache: level=error");
     *
     * <b>GAppLoggingOptions</b> logOpts = new <b>GAppLoggingOptions</b>(<b>logSection</b>, <b>logExtSection</b>, null);
     * </pre></code>
     *
     * @param logOptions the application log Options section.
     * @param logExtOptions the application log-extended Options section.
     * @param logger optional "status" logger to print errors of options parsing methods.
     */
    public GAppLoggingOptions(
            final KeyValueCollection logOptions,
            final KeyValueCollection logExtOptions,
            final ILogger logger) {
        if (logger != null) {
            this.logger = logger;
        } else {
            this.logger = NullLoggerImpl.SINGLETON;
        }

        options.setComparator(new Comparator<String>() {
            public int compare(final String o1, final String o2) {
                return o1.compareToIgnoreCase(o2);
            }
        });

        if (logOptions != null) {
            for (final Object oPair: logOptions) {
                final KeyValuePair pair = (KeyValuePair) oPair;
                final String key = pair.getStringKey().toLowerCase(Locale.ENGLISH);
                final OptionDescriptor<?> opt = OptionDescriptor.OPTIONS_MAP.get(key);
                if (opt != null) {
                    final String optName = opt.optionName();
                    if (!options.containsKey(optName)) {
                        // TODO parse values, so, it could be possible to compare values like "10mb" and "10 MB", etc
                        options.addString(optName, pair.getValue().toString());
                    } else {
                        this.logger.warn("Given log options contain duplication for option "
                                + optName + ", ignoring duplicated option");
                    }
                } else { // Keep unknown option value "as-is"
                    options.addObject(pair.getStringKey(), pair.getValue());
                }
            }
        }

        if (logExtOptions != null) {
            extOptions = new GAppLogExtOptions(logExtOptions, logger);
        } else {
            extOptions = null;
        }
    }

    @Override
    public GAppLoggingOptions clone() {
        try {
            return (GAppLoggingOptions) super.clone();
        } catch (final CloneNotSupportedException e) {
            // Its not expected
            return this;
        }
    }


    /*
     * Static helper method for the constructor of this class.
     */
    private static KeyValueCollection getLogSection(
            final IGApplicationConfiguration appConfig,
            final String sectionName) {
        if (appConfig != null) {
            final KeyValueCollection opts = appConfig.getOptions();
            if (opts != null) {
                return opts.getList(sectionName);
            }
        }
        return null;
    }


    /**
     * Returns the logging thread enabling option value.
     *
     * @see LogOptionsDescriptions#ENABLE_THREAD_OPT
     */
    public Boolean getEnableThread() {
        return getBooleanOptionValue(ENABLE_THREAD_OPT);
    }

    /**
     * Returns the enable location option value to be used with Log4j2 asynchronous loggers.
     *
     * @see LogOptionsDescriptions#ENABLE_LOCATION_FOR_THREAD_OPT
     * @see #getEnableThread()
     */
    public Boolean getIncludeLocation() {
        return getBooleanOptionValue(ENABLE_LOCATION_FOR_THREAD_OPT);
    }

    /**
     * Returns the message format option value.
     *
     * @see LogOptionsDescriptions#MESSAGE_FORMAT_OPT
     */
    public MessageFormat getMessageFormat() {
        return getEnumOptionValue(MESSAGE_FORMAT_OPT);
    }

    /**
     * Returns the message header delimiter option value.
     *
     * @see LogOptionsDescriptions#MESSAGE_HEADER_DELIMITER_OPT
     */
    public String getMessageHeaderDelimiter() {
        return getFirstOptionValue(MESSAGE_HEADER_DELIMITER_OPT);
    }

    /**
     * Returns the custom message format option value.
     *
     * @see LogOptionsDescriptions#CUSTOM_MESSAGE_FORMAT_OPT
     * @since 9.0.002.01
     */
    public String getCustomMessageFormat() {
        final String val = getFirstOptionValue(CUSTOM_MESSAGE_FORMAT_OPT);
        if (val != null && !val.isEmpty()) {
            return val;
        }
        return CUSTOM_MESSAGE_FORMAT_OPT.defaultValue();
    }

    /**
     * Returns the custom message format option value.
     *
     * @see LogOptionsDescriptions#OUTPUT_PATTERN_OPT
     * @since 9.0.002.05
     */
    public String getOutputPattern() {
        final String val = getFirstOptionValue(OUTPUT_PATTERN_OPT);
        if (val != null && !val.isEmpty()) {
            return val;
        }
        return OUTPUT_PATTERN_OPT.defaultValue();
    }

    /**
     * Returns flag value for enabling of native log levels usage in standard LMS loggers formatter.
     *
     * @see LogOptionsDescriptions#USE_NATIVE_LEVELS_OPT
     * @since 9.0.002.09
     */
    public Boolean getUseNativeLevels() {
        return getBooleanOptionValue(USE_NATIVE_LEVELS_OPT);
    }

    /**
     * Returns the time format configuration option.
     *
     * @return the time format configuration option.
     * @see LogOptionsDescriptions#TIME_FORMAT_OPT
     */
    public TimeFormat getTimeFormatting() {
        return getEnumOptionValue(TIME_FORMAT_OPT);
    }

    /**
     * Returns the time convert configuration option.
     *
     * @return the time convert configuration option.
     * @see LogOptionsDescriptions#TIME_CONVERT_OPT
     */
    public TimeUsage getTimeConverting() {
        return getEnumOptionValue(TIME_CONVERT_OPT);
    }

    /**
     * Returns the verbose configuration option.
     *
     * @return the verbose level configuration option.
     * @see LogOptionsDescriptions#VERBOSE_OPT
     */
    public VerboseLevel getVerbose() {
        return getEnumOptionValue(VERBOSE_OPT);
    }

    /**
     * Returns the log segmentation configuration option.
     *
     * @return the segmentation configuration.
     * @see LogOptionsDescriptions#SEGMENT_OPT
     */
    public SegmentationConfig getSegment() {
        final String val = getFirstOptionValue(SEGMENT_OPT);
        if (val != null && !val.isEmpty()) {
            try {
                SegmentationConfig parsed = SegmentationConfig.parse(val, logger);
                if (parsed != null) {
                    return parsed;
                }
            } catch (final Exception e) {
                if (logger.isError()) {
                    logger.error("Error parsing segment option ["
                            + val + "]: set default - no segmentattion");
                }
            }
        }
        return SEGMENT_OPT.defaultValue();
    }

    /**
     * Returns the log expiration configuration option.
     *
     * @return the expiration configuration.
     * @see LogOptionsDescriptions#EXPIRE_OPT
     */
    public ExpirationConfig getExpire() {
        final String val = getFirstOptionValue(EXPIRE_OPT);
        if (val != null && !val.isEmpty()) {
            try {
                final ExpirationConfig parsed = ExpirationConfig.parse(val, logger);
                if (parsed != null) {
                    return parsed;
                }
            } catch (final Exception e) {
                if (logger.isError()) {
                    logger.error("Error parsing 'expire' option: " + val, e);
                }
            }
        }
        return EXPIRE_OPT.defaultValue();
    }

    /**
     * Specifies method that will be used for archiving log files.
     *
     * @return the configured compression method or null.
     * @see LogOptionsDescriptions#COMPRESS_METHOD_OPT
     * @since 9.0.003.01
     */
    public String getCompressMethod() {
        final String val = getFirstOptionValue(COMPRESS_METHOD_OPT);
        if (val != null && !val.isEmpty()) {
            return val;
        }
        return COMPRESS_METHOD_OPT.defaultValue();
    }

    /**
     * Returns the messagefile option value.
     *
     * @see LogOptionsDescriptions#MESSAGEFILE_OPT
     */
    public String getMessageFile() {
        final String val = getFirstOptionValue(MESSAGEFILE_OPT);
        if (val != null && !val.isEmpty()) {
            return val;
        }
        return MESSAGEFILE_OPT.defaultValue();
    }

    /**
     * Returns the logfile encoding option value.
     *
     * @see LogOptionsDescriptions#LOGFILE_ENCODING_OPT
     */
    public String getFileEncoding() {
        final String val = getFirstOptionValue(LOGFILE_ENCODING_OPT);
        if (val != null && !val.isEmpty()) {
            return val;
        }
        return LOGFILE_ENCODING_OPT.defaultValue();
    }

    /**
     * Returns the logfile header provider option value.
     *
     * @see LogOptionsDescriptions#FILE_HEADER_PROVIDER_OPT
     */
    public String getFileHeaderProvider() {
        final String val = getFirstOptionValue(FILE_HEADER_PROVIDER_OPT);
        if (val != null && !val.isEmpty()) {
            return val;
        }
        return FILE_HEADER_PROVIDER_OPT.defaultValue();
    }

    /**
     * Returns the logging application host name option value.
     *
     * @see LogOptionsDescriptions#EVENTLOG_HOST_OPT
     */
    public String getEventlogHost() {
        final String val = getFirstOptionValue(EVENTLOG_HOST_OPT);
        if (val != null && !val.isEmpty()) {
            return val;
        }
        return EVENTLOG_HOST_OPT.defaultValue();
    }

    /**
     * Returns the logs attributes printing enabling option value.
     *
     * @see LogOptionsDescriptions#PRINT_ATTRIBUTES_OPT
     */
    public Boolean getPrintAttributes() {
        return getBooleanOptionValue(PRINT_ATTRIBUTES_OPT);
    }

    /**
     * Returns the AppTemplate Log4j2 configuration profile option value.
     *
     * @see LogOptionsDescriptions#LOG4j2_CONFIG_PROFILE_OPT
     */
    public String getLog4j2ConfigProfile() {
        final String val = getFirstOptionValue(LOG4j2_CONFIG_PROFILE_OPT);
        if (val != null && !val.isEmpty()) {
            return val;
        }
        return LOG4j2_CONFIG_PROFILE_OPT.defaultValue();
    }

    /**
     * Returns the AppTemplate default log directory option value.
     *
     * @see LogOptionsDescriptions#DEFAULT_LOGDIR_OPT
     * @since 9.0.005.00
     */
    public String getDefaultLogdir() {
        final String val = getFirstOptionValue(DEFAULT_LOGDIR_OPT);
        if (val != null && !val.isEmpty()) {
            return val;
        }
        return DEFAULT_LOGDIR_OPT.defaultValue();
    }

    /**
     * Returns queue size for messages to be kept in memory while connection to the Message Server is not available.
     *
     * @see LogOptionsDescriptions#MS_BUFFER_SIZE
     * @since 9.0.008.02
     */
    public Integer getMsBufferSize() {
        return getIntegerOptionValue(MS_BUFFER_SIZE);
    }

    /**
     * Returns log messages level filter on Message Server Appender for Platform SDK internal events.
     *
     * @see LogOptionsDescriptions#X_MSGSRV_INTMSGS_LEVEL
     * @since 9.0.005.02
     */
    public String getMsgsrvIntmsgsLevel() {
        final String val = getFirstOptionValue(X_MSGSRV_INTMSGS_LEVEL);
        if (val != null && !val.isEmpty()) {
            return val;
        }
        return X_MSGSRV_INTMSGS_LEVEL.defaultValue();
    }

    /**
     * Returns normalized list of logging output targets declarations with correspondent log levels
     * including network, stdout, stderr, and log files with their names.
     * <p/>
     * This list is being built by content of log levels named options: "<code>all</code>",
     * "<code>debug</code>", "<code>trace</code>", "<code>interaction</code>", "<code>standard</code>",
     * and "<code>alarm</code>".<br/>
     * For example, with logging configuration like following:
     * <code><pre>
     * KeyValueCollection log = new KeyValueCollection();
     * log.addString("verbose", "interaction");
     * log.addString("<b>interaction</b>", "app-int.log");
     * log.addString("<b>standard</b>", "stdout, network, app-std.log");
     *
     * GAppLoggingOptions logOpts = new GAppLoggingOptions(log, null);
     * List&lt;TargetDescriptor&gt; <b>loggingTargets</b> = logOpts.getOutputDescriptors();
     * </pre></code>
     * we'll have the next logging targets ("appenders" in terms of a logging framework):
     * <code><pre>
     * <b>loggingTargets</b> = [
     *   LoggingTarget(type=STDOUT,level=STANDARD),
     *   LoggingTarget(type=MESSAGESERVER,level=STANDARD),
     *   LoggingTarget(type=FILELOGGER,level=STANDARD,filename=app-std.log),
     *   LoggingTarget(type=FILELOGGER,level=INTERACTION,filename=app-int.log)
     * ]
     * </pre></code>
     *
     * @return collection of logging targets descriptions.
     */
    public List<TargetDescriptor> getOutputDescriptors() {
        final List<TargetDescriptor> result = new ArrayList<TargetDescriptor>();
        for (final VerboseLevel level : VerboseLevel.values()) {
            result.addAll(getOutputDescriptorsForVerboseLevel(level));
        }
        return consolidateOutputDescriptors(result);
    }


    /**
     * Returns configuration options defined by application "log-extended" Options section.
     *
     * @return options defined by the "log-extended" section or <code>null</code>.
     */
    public GAppLogExtOptions getLogExtendedOptions() {
        return extOptions;
    }


    private static List<TargetDescriptor> consolidateOutputDescriptors(
            final List<TargetDescriptor> list2consolidate) {
        final List<TargetDescriptor> consolidatesList = new ArrayList<TargetDescriptor>();
        if (list2consolidate == null || list2consolidate.size() == 0) {
            return consolidatesList;
        }
        for (final TargetDescriptor descriptor : list2consolidate) {
            int index = -1;
            for (int i = 0; i < consolidatesList.size(); i++) {
                if (descriptor.equals(consolidatesList.get(i))) {
                    index = i;
                    break;
                }
            }
            if (index < 0) {
                consolidatesList.add(descriptor);
            } else {
                if (consolidatesList.get(index).getSupportedVerboseLevel().ordinal()
                        > descriptor.getSupportedVerboseLevel().ordinal()) {
                    consolidatesList.set(index, descriptor);
                }
            }
        }
        return consolidatesList;
    }

    private List<TargetDescriptor> getOutputDescriptorsForVerboseLevel(
            final VerboseLevel level) {
        final List<TargetDescriptor> result = new ArrayList<TargetDescriptor>();
        final String optionName = level.name().toLowerCase(Locale.ENGLISH);
        final String optionValue = options.getString(optionName);
        final List<String> delimitedOptions = parseOutputOptionValue(optionValue);
        for (String option : delimitedOptions) {
            final TargetDescriptor descriptor =
                    createOutputDescriptorFromString(option, level);
            if (descriptor != null) {
                result.add(descriptor);
            }
        }
        return result;
    }


    /**
     * This method parses the string which is the value for output option.
     *
     * @param outputOptionValue The comma-separated string
     * @return The substrings which were comma-separated, trimmed from spaces, '"' signs and commas
     */
    private static List<String> parseOutputOptionValue(
            final String outputOptionValue) {
        final List<String> result = new ArrayList<String>();
        if (outputOptionValue == null || outputOptionValue.length() == 0) {
            return result;
        }

        boolean inquote = false;
        int previousCommaPos = -1;
        int currentCommaPos;
        for (int i = 0; i < outputOptionValue.length(); ++i) {
            if (outputOptionValue.charAt(i) == '"') {
                inquote = !inquote;
            }
            if (inquote) {
                continue;
            }
            if (outputOptionValue.charAt(i) == ',') { //here we are at the position of comma
                currentCommaPos = i;
                if (currentCommaPos - previousCommaPos <= 1) {
                    previousCommaPos = currentCommaPos;
                    continue; //no sense in adding empty string
                }
                if (previousCommaPos == -1) {
                    previousCommaPos = 0;
                }
                addTruncatedString(result, outputOptionValue.substring(previousCommaPos, currentCommaPos));
                previousCommaPos = currentCommaPos;
            }
        }
        if (previousCommaPos < 0) {
            result.clear();
            addTruncatedString(result, outputOptionValue);
        } else {
            addTruncatedString(result, outputOptionValue.substring(previousCommaPos, outputOptionValue.length()));
        }
        return result;
    }


    private static final Pattern TRUNC_PATTERN = Pattern.compile("^[\\s,\"]*(.*?)[\\s,\"]*$", 0);

    private static void addTruncatedString(
            final List<String> result,
            final String val) {
        if (val != null && val.length() > 0) {
            final Matcher matcher = TRUNC_PATTERN.matcher(val);
            if (matcher.find()) {
                final String str = matcher.group(1);
                if (str.length() > 0) {
                    result.add(str);
                }
            }
        }
    }

    private static TargetDescriptor createOutputDescriptorFromString(
            final String value2Parse,
            final VerboseLevel level) {
        final String targetString = value2Parse;

        if (TARGET_TYPE_STDOUT.equalsIgnoreCase(targetString)) {
            return new TargetDescriptor(TargetType.STDOUT, level);
        } else if (TARGET_TYPE_STDERR.equalsIgnoreCase(targetString)) {
            return new TargetDescriptor(TargetType.STDERR, level);
        } else if (TARGET_TYPE_NETWORK.equalsIgnoreCase(targetString)) {
            return new TargetDescriptor(TargetType.MESSAGESERVER, level);
        //} else if (TARGET_TYPE_MEMORY.equalsIgnoreCase(targetString)) {
        //    return new TargetDescriptor(TargetType.MEMORY, level);
        } else {
            try {
                final File file = new File(targetString);
                final String filename = file.getName();
                if (filename != null && filename.trim().length() > 0) {
                    return new GFileTargetDescriptor(file.getPath(), level);
                }
            } catch (final Exception ex) {
                return null;
            }
            return null;
        }
    }


    @Override
    public String toString() {
        final StringBuilder ret = new StringBuilder("GAppLoggingOptions(verbose=").append(getVerbose());
        final List<TargetDescriptor> tgts = getOutputDescriptors();
        if (tgts != null) {
            for (final TargetDescriptor tgt: tgts) {
                ret.append(',').append(tgt.getTargetType()).append('/').append(tgt.getSupportedVerboseLevel());
            }
        }
        ret.append(')');
        return ret.toString();
    }

    @Override
    public int hashCode() {
        return options.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GAppLoggingOptions)) {
            return false;
        }
        return options.equals(((GAppLoggingOptions) obj).options);
    }


    protected Boolean getBooleanOptionValue(
            final OptionDescriptor<Boolean> optDescr) {
        final String val = getFirstOptionValue(optDescr);
        if (val == null || val.length() == 0) {
            return optDescr.defaultValue();
        }
        if (ConfigurationUtil.isTrue(val)) {
            return Boolean.TRUE;
        }
        if (ConfigurationUtil.isFalse(val)) {
            return Boolean.FALSE;
        }
        final Boolean defVal = optDescr.defaultValue();
        if (logger.isError()) {
            logger.errorFormat(
                    "Error parsing {0} option value '{1}'. Using default '{2}'.",
                    new Object[] {optDescr.optionName(), val, defVal});
        }
        return defVal;
    }
    protected Integer getIntegerOptionValue(
            final OptionDescriptor<Integer> optDescr) {
        final String val = getFirstOptionValue(optDescr);
        if (val == null || val.length() == 0) {
            return optDescr.defaultValue();
        }
        try{
            Integer result = Integer.parseInt(val);
            return result;
        } catch (Exception e) {
            Integer defVal = optDescr.defaultValue();
            if (logger.isError()) {
                logger.errorFormat(
                        "Error parsing {0} option value '{1}'. Using default '{2}'.",
                        new Object[] {optDescr.optionName(), val, defVal});
            }
            return defVal;
        }
    }

    protected <T extends Enum<T>> T getEnumOptionValue(
            final OptionDescriptor<T> optDescr) {
        final String val = getFirstOptionValue(optDescr);
        if (val == null || val.isEmpty()) {
            return optDescr.defaultValue();
        }
        try {
            final Class<T> c = optDescr.defaultValue().getDeclaringClass();
            for (final T e : EnumSet.allOf(c)) {
                if (val.equalsIgnoreCase(e.name())) {
                    return e;
                }
            }

            return optDescr.defaultValue();
        } catch (final Exception ex) {
            final T defVal = optDescr.defaultValue();
            if (logger.isError()) {
                logger.errorFormat("Error parsing [{0}] option. Set default [{1}]",
                        new Object[] {optDescr.optionName(), defVal});
            }
            return defVal;
        }
    }

    protected String getFirstOptionValue(
            final OptionDescriptor<?> descriptor) {
        final String val = options.getString(descriptor.optionName());
        if (val != null) {
            return val.trim();
        }
        return null;
    }
}

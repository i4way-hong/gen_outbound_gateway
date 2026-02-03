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

import com.genesyslab.platform.apptemplate.lmslogger.impl.LmsFileData;
import com.genesyslab.platform.apptemplate.lmslogger.impl.LmsEnumsCache;
import com.genesyslab.platform.apptemplate.lmslogger.impl.LmsEnumsProcessor;

import com.genesyslab.platform.apptemplate.configuration.log.GAppLogExtOptions;
import com.genesyslab.platform.apptemplate.configuration.log.GAppLoggingOptions;
import com.genesyslab.platform.apptemplate.configuration.IGApplicationConfiguration;

import com.genesyslab.platform.commons.log.ILogger;
import com.genesyslab.platform.commons.log.NullLoggerImpl;

import com.genesyslab.platform.commons.collections.KeyValueCollection;

import java.net.URL;
import java.io.IOException;

import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Enumeration;


/**
 * Container of LMS events templates with correspondent LMS file(s) localization.
 * <p/>
 * Usual application configuration supposes to have one instance of <code>LmsMessageConveyor</code>.<br/>
 * It should be created on application start and used for initialization of static {@link LmsLoggerFactory}
 * instance with {@link LmsLoggerFactory#createInstance(LmsMessageConveyor)}.
 * <p/>
 * It is possible to reconfigure active <code>LmsMessageConveyor</code> in runtime, or
 * create new instance and reinitialize static <code>LmsLoggerFactory</code> with it.
 *
 * @see LmsLoggerFactory
 * @see LmsLoggerFactory#createInstance(LmsMessageConveyor)
 * @see #newBuilder()
 * @see Builder
 */
public class LmsMessageConveyor implements Cloneable {

    public static final String MESSAGEFILE_PROPERTY_NAME =
            "com.genesyslab.platform.apptemplate.lmslogger.messagefile";

    public static final String DEFAULT_MESSAGEFILE_VALUE = "common.lms";

    /**
     * @deprecated
     * @see GAppLogExtOptions
     * @see GAppLogExtOptions#LEVEL_REASSIGN_OPT_PREFIX
     */
    @Deprecated
    public static final String LEVEL_REASSIGN_OPT_PREFIX = GAppLogExtOptions.LEVEL_REASSIGN_OPT_PREFIX;

    /**
     * @deprecated
     * @see GAppLogExtOptions
     * @see GAppLogExtOptions#LEVEL_REASSIGN_DISABLE_EXT_OPT
     */
    @Deprecated
    public static final String LEVEL_REASSIGN_DISABLE_EXT_OPT = GAppLogExtOptions.LEVEL_REASSIGN_DISABLE_EXT_OPT;


    private final List<Class<?>> lmsEnums;
    private final Boolean allowUnknownEvents;

    private List<LmsFileData> lmsFiles = null;

    private transient final HashMap<Integer, LmsMessageTemplate> mapByIdEnums =
            new HashMap<Integer, LmsMessageTemplate>();

    private HashMap<Integer, LmsMessageTemplate> mapById = null;


    private static final ILogger log = NullLoggerImpl.SINGLETON; // TODO change to status logger


    /**
     * Default constructor of LMS events conveyor.<br/>
     * It loads common LMS events templates enumeration {@link CommonLmsEnum},
     * and available generated users' enumerations handled with the AppTemplate annotations processor.
     *
     * @see #newBuilder()
     */
    public LmsMessageConveyor() {
        this(null, Boolean.FALSE);
    }

    /**
     * Creates LMS messages conveyor by specified templates enumeration.
     * <p/>
     * <b><i>Note</i></b>: If an application uses own LMS events enumeration and needs
     * to use common {@link CommonLmsEnum} as well, it is required to create conveyor specifying
     * all of the enumerations classes including {@link CommonLmsEnum CommonLmsEnum.class}.<br/>
     * For example:<br/><code>LmsMessageConveyor lmsEvents =
     *   new LmsMessageConveyor(MyAppLmsEnum.class, CommonLmsEnum.class);</code>
     *
     * @param enum1 class of LmsMessageTemplate based enumeration.
     * @see #newBuilder()
     */
    public <E1 extends Enum<E1> & LmsMessageTemplate>
            LmsMessageConveyor(final Class<E1> enum1) {
        this.lmsEnums = new LinkedList<Class<?>>();
        this.lmsEnums.add((Class<?>) enum1);
        this.allowUnknownEvents = Boolean.FALSE;

        initEnumsMap();
        initEventsMap(null);
    }

    /**
     * Creates LMS messages conveyor by specified templates enumerations.
     * <p/>
     * <b><i>Note</i></b>: If an application uses own LMS events enumeration(-s) and needs
     * to use common {@link CommonLmsEnum} as well, it is required to create conveyor specifying
     * all of the enumerations classes including {@link CommonLmsEnum CommonLmsEnum.class}.<br/>
     * For example:<br/><code>LmsMessageConveyor lmsEvents =
     *   new LmsMessageConveyor(MyAppLmsEnum.class, CommonLmsEnum.class);</code>
     *
     * @param enum1 class of generated LmsMessageTemplate based enumeration.
     * @param enum2 other class of generated LmsMessageTemplate based enumeration.
     * @see #newBuilder()
     */
    public <E1 extends Enum<E1> & LmsMessageTemplate,
            E2 extends Enum<E2> & LmsMessageTemplate>
                    LmsMessageConveyor(
                            final Class<E1> enum1,
                            final Class<E2> enum2) {
        this.lmsEnums = new LinkedList<Class<?>>();
        this.lmsEnums.add(enum1);
        this.lmsEnums.add(enum2);
        this.allowUnknownEvents = Boolean.FALSE;

        initEnumsMap();
        initEventsMap(null);
    }


    /**
     * Internal constructor for the conveyor builder.
     *
     * @see #newBuilder()
     */
    protected LmsMessageConveyor(
            final List<Class<?>> lmsEnums,
            final Boolean allowUnknownEvents) {
        this.lmsEnums = new LinkedList<Class<?>>();
        if (lmsEnums != null) {
            for (Class<?> lmsEnum: lmsEnums) {
                this.lmsEnums.add(lmsEnum);
            }
        } else {
            this.lmsEnums.add(CommonLmsEnum.class);
            initLmsEnumsList(LmsMessageConveyor.class.getClassLoader(), this.lmsEnums);
        }
        this.allowUnknownEvents = allowUnknownEvents;

        initEnumsMap();
        initEventsMap(null);
    }


    /**
     * Creates and returns new instance of <code>LmsMessageConveyor</code> builder.
     *
     * @return New instance of the builder.
     * @see Builder
     */
    public static Builder newBuilder() {
        return new Builder();
    }


    /**
     * Builder for LmsMessageConveyor component.
     * <p/>
     * Common use cases are following:<ul>
     * <li>to create conveyor instance with default configuration - with {@link CommonLmsEnum}
     * and without support of unknown LMS events:<code><pre>
     * LmsMessageConveyor lms = LmsMessageConveyor.newBuilder().build();</pre></code></li>
     * <li>to use custom application LMS enum:<code><pre>
     * LmsMessageConveyor lms = LmsMessageConveyor.newBuilder()
     *     .addLmsEnum(CommonLmsEnum.class)
     *     .addLmsEnum(MyAppLmsEnum.class)
     *     .build();</pre></code></li>
     * </pre></code></li>
     * <li>to use custom application LMS enum with support of unknown LMS events:<code><pre>
     * LmsMessageConveyor lms = LmsMessageConveyor.newBuilder()
     *     .addLmsEnum(CommonLmsEnum.class)
     *     .addLmsEnum(MyAppLmsEnum.class)
     *     .allowUnknownMessages(true)
     *     .build();</pre></code></li>
     * </pre></code></li>
     * </ul>
     * <i><b>Note:</b> Be aware - if application explicitly uses custom LMS enumerations, it should take
     * care for explicit adding (or ignoring) <code>CommonLmsEnum</code>. If this enumeration
     * is not included in the list, and given custom enumeration(s) does not contain its
     * declarations, then "common LMS events" may be treated by this conveyor as unknown.</i>
     *
     * @see LmsMessageConveyor#newBuilder()
     */
    public static class Builder {

        protected List<Class<?>> lmsEnums = new LinkedList<Class<?>>();
        protected Boolean allowUnknownEvents = Boolean.FALSE;

        /**
         * Adds LMS enumeration to the list of declarations for the conveyor.<br/>
         * It may be called several times to include several enums.<br/>
         * Order of this method calls may be important in case, when enumerations
         * may contain own declarations for a same event ID.<br/>
         * In such a case, last declarations will override previously declared ones.
         *
         * @param lmsEnum the LMS enumeration class.
         */
        public <E extends Enum<E> & LmsMessageTemplate> Builder addLmsEnum(
                final Class<E> lmsEnum) {
            lmsEnums.add(lmsEnum);
            return Builder.this;
        }

        /**
         * Enables or disables support of "unknown" messages.<br/>
         * This is about the case, when application tries to log event with unsupported ID. For example,
         * <code>lmsLogger.log(43210, "internal error");</code> when no message ID 43210
         * defined in the conveyor enumeration(s) and LMS file(s).
         * <p/>
         * There are following supported values for this option:<ul>
         * <li><code>FALSE</code> (the default value) - to throw {@link IllegalArgumentException}
         * on undefined events logging;</li>
         * <li><code>TRUE</code> - to allow undefined events logging;</li>
         * <li><code>null</code> - to skip undefined events;</li>
         * </ul>
         *
         * @param allowUnknownEvents enabling option value for the unknown events support feature.
         */
        public Builder allowUnknownMessages(final Boolean allowUnknownEvents) {
            this.allowUnknownEvents = allowUnknownEvents;
            return Builder.this;
        }

        /**
         * Creates new instance of <code>LmsMessageConveyor</code> with given parameters.
         */
        public LmsMessageConveyor build() {
            if (lmsEnums.isEmpty()) {
                return new LmsMessageConveyor(null, allowUnknownEvents);
            } else {
                return new LmsMessageConveyor(lmsEnums, allowUnknownEvents);
            }
        }
    }


    @SuppressWarnings("unchecked")
    public LmsMessageConveyor clone() {
        final LmsMessageConveyor clone;
        try {
            clone = (LmsMessageConveyor) super.clone();
        } catch (final CloneNotSupportedException e) {
            /* not expected */
            return this;
        }
        if (clone != null && mapById != null) {
            clone.mapById = (HashMap<Integer, LmsMessageTemplate>) mapById.clone();
        }
        return clone;
    }


    /**
     * Initializes events IDs mapping for the given enumerations classes.
     */
    private void initEnumsMap() {
        log.debug("Initializing LMS enums map");

        // Initialize referred generated enumerations:
        if (lmsEnums != null) {
            for (Class<?> enumCls: lmsEnums) {
                if (!Enum.class.isAssignableFrom(enumCls)) {
                    if (log.isDebug()) {
                        log.debug("Given class '" + enumCls.getName()
                                + "' is not enumeration");
                    }
                    continue;
                }
                @SuppressWarnings("unchecked")
                final Enum<?>[] values = ((Class<Enum<?>>) enumCls).getEnumConstants();
                if (values != null) {
                    if (values.length == 0) {
                        if (log.isWarn()) {
                            log.warn("Given class '" + enumCls.getName()
                                    + "' has no enumeration values");
                        }
                    } else {
                        if (values[0] instanceof LmsMessageTemplate) {
                            if (log.isDebug()) {
                                log.debug("Loading enumeration '" + enumCls.getName()
                                        + "' with " + values.length + " declaration(s)");
                            }
                            for (Enum<?> item: values) {
                                final LmsMessageTemplate elem = (LmsMessageTemplate) item;
                                final Integer key = Integer.valueOf(elem.getId());
                                if (log.isDebug()) {
                                    if (mapByIdEnums.containsKey(key)) {
                                        log.debug("Enumeration '" + enumCls.getName()
                                                + "' overrides " + key + " event");
                                    }
                                }
                                mapByIdEnums.put(key, elem);
                            }
                        } else {
                            if (log.isWarn()) {
                                log.warn("Given class '" + enumCls.getName()
                                        + "' does not implement LmsMessageTemplate");
                            }
                        }
                    }
                } else {
                    if (log.isWarn()) {
                        log.warn("Given class '" + enumCls.getName() + "' is not enumeration");
                    }
                }
            }
        }

        log.debugFormat("Initialization of LMS enums map finished with {0} records", mapByIdEnums.size());
    }


    /**
     * Loads and initializes conveyor configuration based on <code>"log"</code> and
     * <code>"log-extended"</code> sections of configuration server application object options.
     *
     * @param appConfig the application configuration.
     * @return Reference to itself.
     * @throws NullPointerException if given application configuration is null,
     *         or it does not contain "log" section.
     */
    public LmsMessageConveyor loadConfiguration(
            final IGApplicationConfiguration appConfig) {
        if (appConfig == null) {
            throw new NullPointerException("Application configuration");
        }
        return loadConfiguration(new GAppLoggingOptions(appConfig, log));
    }

    /**
     * Loads and initializes conveyor configuration based on <code>"log"</code> and
     * <code>"log-extended"</code> sections of configuration server application object options.
     *
     * @param optsLog "log" section of the application options.
     * @param optsLogExt "log-extended" section of the application options.
     * @return Reference to itself.
     */
    public LmsMessageConveyor loadConfiguration(
            final KeyValueCollection optsLog,
            final KeyValueCollection optsLogExt) {
        final GAppLoggingOptions logOptions =
                new GAppLoggingOptions(optsLog, optsLogExt, log);
        final GAppLogExtOptions logExtOptions = logOptions.getLogExtendedOptions();
        return loadConfiguration(logOptions.getMessageFile(),
                (logExtOptions != null) ? logExtOptions.getLevelReassigns() : null);
    }

    /**
     * Loads and initializes conveyor configuration based on <code>"log"</code> and
     * <code>"log-extended"</code> sections of configuration server application object options.
     *
     * @param logOptions the logging configuration options of the application options.
     * @return Reference to itself.
     * @throws NullPointerException if given logging options parameter is <code>null</code>.
     */
    public LmsMessageConveyor loadConfiguration(
            final GAppLoggingOptions logOptions) {
        if (logOptions == null) {
            throw new NullPointerException("Application logging options");
        }
        final GAppLogExtOptions logExtOptions = logOptions.getLogExtendedOptions();
        return loadConfiguration(logOptions.getMessageFile(),
                (logExtOptions != null) ? logExtOptions.getLevelReassigns() : null);
    }

    /**
     * Loads content and initializes messages IDs mapping of given LMS files.
     *
     * @param messagefiles LMS files names separated with semicolon, empty string; or null.
     * @return Reference to itself.
     * @see #loadConfiguration(String, Map)
     */
    public LmsMessageConveyor loadConfiguration(
            final String messagefiles) {
        return loadConfiguration(messagefiles, null);
    }

    /**
     * Loads content and initializes messages IDs mapping of given LMS files.
     *
     * @param messagefiles LMS file names separated with semicolon, empty string or null.
     * @param levelsReassign map with events levels reassignments or null.
     * @return Reference to itself.
     */
    public LmsMessageConveyor loadConfiguration(
            final String messagefiles,
            final Map<Integer, LmsLogLevel> levelsReassign) {
        this.lmsFiles = readLmsFiles(messagefiles);

        initEventsMap(levelsReassign);

        return LmsMessageConveyor.this;
    }


    /**
     * Reads application LMS file(s).
     * Beside the given LMS files list, at the first it tries to read
     * {@value #DEFAULT_MESSAGEFILE_VALUE}.
     *
     * @param messagefile name(s) of LMS files to read (comma separated list).
     * @return list of successfully read LMS events declarations.
     */
    protected List<LmsFileData> readLmsFiles(
            final String messagefile) {
        String lmsfiles = messagefile;
        final List<LmsFileData> lmsFilesNew = new LinkedList<LmsFileData>();

        if (lmsfiles == null) {
            try {
                lmsfiles = System.getProperty(MESSAGEFILE_PROPERTY_NAME);
            } catch (final Exception ex) {
                if (log.isWarn()) {
                    log.warn("Error getting default LMS file name", ex);
                }
            }
        }

        LmsFileData lms = readLmsFile(DEFAULT_MESSAGEFILE_VALUE);
        if (lms != null) {
            lmsFilesNew.add(lms);
        }

        if (lmsfiles != null && !lmsfiles.isEmpty()) {
            final String[] files = lmsfiles.split("[,;]");
            if (files != null) {
                for (final String lmsFile: files) {
                    lms = readLmsFile(lmsFile);
                    if (lms != null) {
                        lmsFilesNew.add(lms);
                    }
                }
            }
        }

        return lmsFilesNew;
    }

    /**
     * Reads single LMS file.
     *
     * @param lmsfile the name of LMS file.
     * @return Parsed LMS file data, or null if LMS file does not contain valid events declarations,
     *             or there was an exception while reading it.
     */
    protected LmsFileData readLmsFile(
            final String lmsfile) {
        try {
            final LmsFileData lms = new LmsFileData(lmsfile);
            if (!lms.getTemplates().isEmpty()) {
                if (log.isDebug()) {
                    log.debug("Loaded LMS file '" + lmsfile
                            + "' with " + lms.getTemplates().size()
                            + " event declaration(s)");
                }
                return lms;
            } else {
                if (log.isWarn()) {
                    log.warn("No LMS events found in '" + lmsfile + "'");
                }
            }
        } catch (final Exception ex) {
            if (log.isWarn()) {
                log.warn("Exception loading file '" + lmsfile + "'", ex);
            }
        }

        return null;
    }


    /**
     * Initializes events IDs mapping for given enumerations with appliance of loaded LMS files content.
     */
    @SuppressWarnings("unchecked")
    private void initEventsMap(final Map<Integer, LmsLogLevel> levelsReassign) {
        log.debug("Initializing LMS events map");

        final HashMap<Integer, LmsMessageTemplate> mapByIdNew;
        if (mapByIdEnums != null && mapByIdEnums.size() > 0) {
            mapByIdNew = (HashMap<Integer, LmsMessageTemplate>) mapByIdEnums.clone();
        } else {
            log.warn("LMS conveyor has no enums declarations loaded");
            mapByIdNew = new HashMap<Integer, LmsMessageTemplate>();
        }

        // Initialize loaded LMS files records:
        if (lmsFiles != null && lmsFiles.size() > 0) {
            for (LmsFileData file: lmsFiles) {
                for (LmsMessageTemplate elem: file.getTemplates()) {
                    mapByIdNew.put(elem.getId(), elem);
                }
            }
        } else {
            log.warn("LMS conveyor got no LMS files to loaded");
        }

        if (levelsReassign != null && levelsReassign.size() > 0) {
            for (Map.Entry<Integer, LmsLogLevel> entry: levelsReassign.entrySet()) {
                LmsMessageTemplate elem = mapByIdNew.get(entry.getKey());
                if (elem != null) {
                    LmsFileData.LmsMessageTemplImpl newElem =
                            new LmsFileData.LmsMessageTemplImpl(
                                    elem.getId(), elem.getName(), entry.getValue(), elem.getMessage());
                    mapByIdNew.put(entry.getKey(), newElem);
                    if (log.isDebug()) {
                        log.debug("LMS event level for " + entry.getKey()
                                + " reassigned to " + entry.getValue().name());
                    }
                } else {
                    LmsFileData.LmsMessageTemplImpl reasignElem =
                            new LmsFileData.LmsMessageTemplImpl(
                                    entry.getKey(), null, entry.getValue(), null);
                    mapByIdNew.put(entry.getKey(), reasignElem);
                    if (log.isWarn()) {
                        log.warn("LMS level reassignment for unknown event "
                                + entry.getKey() + " to " + entry.getValue().name());
                    }
                }
            }
        }

        mapById = mapByIdNew;

        log.debugFormat("Initialization of LMS events map finished with {0} records", mapById.size());
    }


    /**
     * Returns LMS event definition (template) from the conveyor configuration.
     *
     * @param key the LMS event ID.
     * @return LMS event template from the conveyor LMS declarations or null.
     * @throws NullPointerException if given <code>key</code> parameter value is null.
     */
    public LmsMessageTemplate getLmsEvent(final LmsMessageTemplate key) {
        if (key == null) {
            throw new NullPointerException("LMS enum key is null");
        }
        final LmsMessageTemplate ret = mapById.get(key.getId());
        if (ret != null && ret.getName() != null) {
            return ret;
        }
        return null;
    }

    /**
     * Returns LMS event definition (template) from the conveyor configuration.
     *
     * @param key the LMS event ID.
     * @param args the LMS event parameters.
     * @return LMS event template or null.
     * @throws NullPointerException if given <code>key</code> parameter value is null.
     * @throws IllegalArgumentException if no such event declared and unknown events are disallowed.
     * @see Builder#allowUnknownMessages(Boolean)
     */
    public LmsMessageTemplate getLmsEvent(
            final Integer key,
            final Object... args) {
        if (key == null) {
            throw new NullPointerException("LMS enum key is null");
        }
        final LmsMessageTemplate ret = mapById.get(key);
        if (ret != null && ret.getName() != null) {
            return ret;
        }
        final LmsLogLevel level;
        if (ret != null) { // reassignment of unknown event level:
            level = ret.getLevel();
        } else {
            level = LmsLogLevel.UNKNOWN;
        }
        return getDefaultEvent(key, level, args);
    }


    private static final String UNKNOWN_MSG_NANE = "UNKNOWN_MSG";

    private static final String[] UNKNOWN_MSG_FORMAT = {
            "UnknownMsg",
            "UnknownMsg('%s')",
            "UnknownMsg('%s','%s')",
            "UnknownMsg('%s','%s','%s')",
            "UnknownMsg('%s','%s','%s','%s')",
            "UnknownMsg('%s','%s','%s','%s',...)"};


    /**
     * Internal method to handle "unknown" LMS log event.
     *
     * @param key the event ID which was not found in the conveyor configuration.
     * @param level the LMS event level of the message.
     * @param args the parameters of the LMS log event.
     * @return "unknown" event template or null.
     * @throws IllegalArgumentException if unknown events are disallowed.
     * @see Builder#allowUnknownMessages(Boolean)
     */
    protected LmsMessageTemplate getDefaultEvent(
            final int key,
            final LmsLogLevel level,
            final Object... args) {
        if (allowUnknownEvents != null) {
            if (!allowUnknownEvents) {
                throw new IllegalArgumentException(
                        "Undefined LMS message id " + key);
            }
            if (args == null) {
                return new UnknownLmsEvent(key, level, UNKNOWN_MSG_FORMAT[0]);
            }
            if (args.length >= UNKNOWN_MSG_FORMAT.length) {
                return new UnknownLmsEvent(key, level,
                        UNKNOWN_MSG_FORMAT[UNKNOWN_MSG_FORMAT.length - 1]);
            }
            return new UnknownLmsEvent(key, level, UNKNOWN_MSG_FORMAT[args.length]);
        }
        throw null;
    }


    /**
     * Internal class to represent "unknown" LMS log events.
     *
     * @see Builder#allowUnknownMessages(Boolean)
     */
    protected static class UnknownLmsEvent implements LmsMessageTemplate {

        private final Integer     messageId;
        private final LmsLogLevel messageLevel;
        private final String      messageFormat;

        protected UnknownLmsEvent(
                final Integer messageId,
                final LmsLogLevel messageLevel,
                final String messageFormat) {
            this.messageId = messageId;
            this.messageLevel = messageLevel;
            this.messageFormat = messageFormat;
        }

        @Override
        public Integer getId() {
            return messageId;
        }

        @Override
        public LmsLogLevel getLevel() {
            return messageLevel;
        }

        @Override
        public String getName() {
            return UNKNOWN_MSG_NANE;
        }

        @Override
        public String getMessage() {
            return messageFormat;
        }
    }


    private void initLmsEnumsList(
            final ClassLoader loader,
            final List<Class<?>> outList) {
        final Set<String> names = loadEnumsCacheFiles(loader);
        if (names != null) {
            for (final String name: names) {
                try {
                    final Class<?> clazz = Class.forName(name, true, loader);
                    if (clazz != null
                            && Enum.class.isAssignableFrom(clazz)
                            && LmsMessageTemplate.class.isAssignableFrom(clazz)) {
                        if (!outList.contains(clazz)) {
                            outList.add(clazz);
                        }
                    }
                } catch (final Exception ex) {
                    log.warn("Unable to preload LmsEnums", ex);
                }
            }
        }
    }

    private Set<String> loadEnumsCacheFiles(
            final ClassLoader loader) {
        final LmsEnumsCache cache = new LmsEnumsCache();
        try {
            final Enumeration<URL> resources = loader.getResources(LmsEnumsProcessor.ENUMS_SET_FILE);
            if (resources != null) {
                cache.loadCacheFiles(resources);
                return cache.getNames();
            }
        } catch (final IOException ioe) {
            log.warn("Unable to preload LmsEnums", ioe);
        }
        return null;
    }
}

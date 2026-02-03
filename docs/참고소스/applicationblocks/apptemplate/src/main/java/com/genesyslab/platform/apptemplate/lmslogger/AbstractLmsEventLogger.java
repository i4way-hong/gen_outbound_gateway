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

import com.genesyslab.platform.management.protocol.messageserver.LogCategory;

import com.genesyslab.platform.commons.log.AbstractLogger;

import java.text.MessageFormat;


/**
 * Abstract base class with common methods implementation of {@link LmsEventLogger} interface.
 *
 * @see LmsLoggerFactory#getLogger(Class)
 * @see LmsLoggerFactory#getLogger(String)
 */
public abstract class AbstractLmsEventLogger
        extends AbstractLogger
        implements LmsEventLogger {

    protected static final String CTX_LMSATTR_ID       = "LMSMessageID";
    protected static final String CTX_LMSATTR_NAME     = "LMSMessageName";
    protected static final String CTX_LMSATTR_LEVEL    = "LMSMessageLevel";
    protected static final String CTX_LMSATTR_CATEGORY = "LMSCategory";

    protected static final String CTX_LMSATTR_PREFIX   = "LMS";

    protected static final String CTX_ATTRMAP_PREFIX   = "GMSATTR:";

    protected static final String LOG_WRAPPER_FQCN =
            "com.genesyslab.platform.apptemplate.lmslogger.LmsLoggerFactory$WrappedLogger";

    protected static final LmsMessageTemplate DEFAULT_LMS_EVENT =
            CommonLmsEnum.GCTI_DEBUG;

    protected final LmsMessageConveyor lmsMessages;
    protected final boolean printAttributes;

    protected AbstractLmsEventLogger(final LmsMessageConveyor imc) {
        this.lmsMessages = imc;
        this.printAttributes = true;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void log(
            final LogCategory category,
            final LmsMessageTemplate key,
            final Object... args) {
        LmsMessageTemplate lmsEvent = key;
        if (key instanceof Enum) {
            lmsEvent = lmsMessages.getLmsEvent(key);
            if (lmsEvent == null) {
                lmsEvent = key;
            }
        }
        doLogEvent(category, null, lmsEvent, args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void log(
            final LogCategory category,
            final int key,
            final Object... args) {
        doLogEvent(category, null, lmsMessages.getLmsEvent(key, args), args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void log(
            final LmsMessageTemplate key,
            final Object... args) {
        LmsMessageTemplate lmsEvent = key;
        if (key instanceof Enum) {
            lmsEvent = lmsMessages.getLmsEvent(key);
            if (lmsEvent == null) {
                lmsEvent = key;
            }
        }
        doLogEvent(LogCategory.Default, null, lmsEvent, args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void log(
            final int key,
            final Object... args) {
        doLogEvent(LogCategory.Default, null, lmsMessages.getLmsEvent(key, args), args);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void fatalError(final Object message) {
        if (message instanceof LmsMessageTemplate) {
            fatalError(LogCategory.Default, (LmsMessageTemplate) message);
        } else {
            super.fatalError(message);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fatalError(final Object message, final Throwable e) {
        if (message instanceof LmsMessageTemplate) {
            fatalError(LogCategory.Default, (LmsMessageTemplate) message, e);
        } else {
            super.fatalError(message, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fatalError(
            final LmsMessageTemplate key,
            final Object... args) {
        fatalError(LogCategory.Default, key, args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fatalError(
            final LogCategory category,
            final LmsMessageTemplate key,
            final Object... args) {
        LmsMessageTemplate lmsEvent = key;
        if (key instanceof Enum) {
            lmsEvent = lmsMessages.getLmsEvent(key);
            if (lmsEvent == null) {
                lmsEvent = key;
            }
        }
        doLogEvent(category, Level.FATAL_ERROR, lmsEvent, args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fatal(
            final LmsMessageTemplate key,
            final Object... args) {
        fatal(LogCategory.Default, key, args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fatal(
            final LogCategory category,
            final LmsMessageTemplate key,
            final Object... args) {
        if (isFatalError()) {
            LmsMessageTemplate lmsEvent = key;
            if (key instanceof Enum) {
                lmsEvent = lmsMessages.getLmsEvent(key);
                if (lmsEvent == null) {
                    lmsEvent = key;
                }
            }
            doLogEvent(category, Level.FATAL_ERROR, lmsEvent, args);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fatal(final String message, final Object... args) {
        if (isFatalError()) {
            log(MessageFormat.format(message, args),
                    getThrowableArg(args), Level.FATAL_ERROR);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void error(final Object message) {
        if (message instanceof LmsMessageTemplate) {
            error(LogCategory.Default, (LmsMessageTemplate) message);
        } else {
            super.error(message);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(final Object message, final Throwable e) {
        if (message instanceof LmsMessageTemplate) {
            error(LogCategory.Default, (LmsMessageTemplate) message, e);
        } else {
            super.error(message, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(
            final LmsMessageTemplate key,
            final Object... args) {
        error(LogCategory.Default, key, args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(
            final LogCategory category,
            final LmsMessageTemplate key,
            final Object... args) {
        if (isError()) {
            LmsMessageTemplate lmsEvent = key;
            if (key instanceof Enum) {
                lmsEvent = lmsMessages.getLmsEvent(key);
                if (lmsEvent == null) {
                    lmsEvent = key;
                }
            }
            doLogEvent(category, Level.ERROR, lmsEvent, args);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(final String message, final Object... args) {
        if (isError()) {
            log(MessageFormat.format(message, args),
                    getThrowableArg(args), Level.ERROR);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void warn(final Object message) {
        if (message instanceof LmsMessageTemplate) {
            warn(LogCategory.Default, (LmsMessageTemplate) message);
        } else {
            super.warn(message);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void warn(final Object message, final Throwable e) {
        if (message instanceof LmsMessageTemplate) {
            warn(LogCategory.Default, (LmsMessageTemplate) message, e);
        } else {
            super.warn(message, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void warn(
            final LmsMessageTemplate key,
            final Object... args) {
        warn(LogCategory.Default, key, args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void warn(
            final LogCategory category,
            final LmsMessageTemplate key,
            final Object... args) {
        if (isWarn()) {
            LmsMessageTemplate lmsEvent = key;
            if (key instanceof Enum) {
                lmsEvent = lmsMessages.getLmsEvent(key);
                if (lmsEvent == null) {
                    lmsEvent = key;
                }
            }
            doLogEvent(category, Level.WARN, lmsEvent, args);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void warn(final String message, final Object... args) {
        if (isWarn()) {
            log(MessageFormat.format(message, args),
                    getThrowableArg(args), Level.WARN);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void info(final Object message) {
        if (message instanceof LmsMessageTemplate) {
            info(LogCategory.Default, (LmsMessageTemplate) message);
        } else {
            super.info(message);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void info(final Object message, final Throwable e) {
        if (message instanceof LmsMessageTemplate) {
            info(LogCategory.Default, (LmsMessageTemplate) message, e);
        } else {
            super.info(message, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void info(
            final LmsMessageTemplate key,
            final Object... args) {
        info(LogCategory.Default, key, args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void info(
            final LogCategory category,
            final LmsMessageTemplate key,
            final Object... args) {
        if (isInfo()) {
            LmsMessageTemplate lmsEvent = key;
            if (key instanceof Enum) {
                lmsEvent = lmsMessages.getLmsEvent(key);
                if (lmsEvent == null) {
                    lmsEvent = key;
                }
            }
            doLogEvent(category, Level.INFO, lmsEvent, args);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void info(final String message, final Object... args) {
        if (isInfo()) {
            log(MessageFormat.format(message, args),
                    getThrowableArg(args), Level.INFO);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void debug(final Object message) {
        if (message instanceof LmsMessageTemplate) {
            debug(LogCategory.Default, (LmsMessageTemplate) message);
        } else {
            super.debug(message);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void debug(final Object message, final Throwable e) {
        if (message instanceof LmsMessageTemplate) {
            debug(LogCategory.Default, (LmsMessageTemplate) message, e);
        } else {
            super.debug(message, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void debug(
            final LmsMessageTemplate key,
            final Object... args) {
        debug(LogCategory.Default, key, args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void debug(
            final LogCategory category,
            final LmsMessageTemplate key,
            final Object... args) {
        if (isDebug()) {
            LmsMessageTemplate lmsEvent = key;
            if (key instanceof Enum) {
                lmsEvent = lmsMessages.getLmsEvent(key);
                if (lmsEvent == null) {
                    lmsEvent = key;
                }
            }
            doLogEvent(category, Level.DEBUG, lmsEvent, args);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void debug(final String message, final Object... args) {
        if (isDebug()) {
            log(MessageFormat.format(message, args),
                    getThrowableArg(args), Level.DEBUG);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void trace(final String message, final Object... args) {
        if (isDebug()) { // TODO debug -> trace level (PSDK-8323)
            log(MessageFormat.format(message, args),
                    getThrowableArg(args), Level.DEBUG);
        }
    }


    /**
     * Logs a localized message.<br/>
     * Specific LmsEventLogger class should implement this method to process log messages.
     *
     * @param category the log event category.
     * @param logLevel the PSDK log level value.
     * @param key the key used for localization.
     * @param args optional arguments.
     */
    protected abstract void doLogEvent(
            LogCategory category,
            Level logLevel,
            LmsMessageTemplate key,
            Object... args);


    /**
     * Returns the last argument value as <code>Throwable</code>.
     *
     * @param args arguments of a log request.
     * @return the last argument if it is <code>Throwable</code>, or <code>null</code>.
     */
    protected static Throwable getThrowableArg(final Object... args) {
        if (args != null && args.length > 0) {
            final Object val = args[args.length - 1];
            if (val instanceof Throwable) {
                return (Throwable) val;
            }
            if (val != null && args.length == 1) {
                if (val instanceof Object[]) {
                    final Object[] args0 = (Object[]) val;
                    if (args0.length > 0) {
                        final Object val0 = args0[args0.length - 1];
                        if (val0 instanceof Throwable) {
                            return (Throwable) val0;
                        }
                    }
                }
            }
        }
        return null;
    }
}

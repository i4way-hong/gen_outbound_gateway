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

import java.util.Map;
import java.util.HashMap;


/**
 * Platform SDK specific extended logging configuration option for applying of custom properties
 * on loggers configuration.<br/>
 * It represents single logger configuration record in "log-extended" section of CME Application Options.
 * <p/>
 * For example:<code><pre>
 * [log-extended]
 *     "logger-<i>&lt;ID&gt;</i>" = "<i>&lt;logger.name&gt;</i><b>:</b> level=debug, additivity=false"
 * </pre></code>
 * It's included in {@link GAppLogExtOptions}, which may contain list of such descriptions,
 * and is a part of {@link GAppLoggingOptions}.<br/>
 * The custom logger declaration supports following custom properties:<ul>
 * <li><b>level</b> - the Level to be associated with the Logger;</li>
 * <li><b>additivity</b> ("<i>true</i>"/"<i>false</i>") - <code>true</code> if the logger should be additive,
 *     <code>false</code> otherwise;</li>
 * <li><b>includeLocation</b> ("<i>true</i>"/"<i>false</i>") - whether location should be passed downstream.</li>
 * </ul>
 *
 * @see GAppLogExtOptions
 * @see GAppLoggingOptions
 */
public class CustomLoggerExtConfig implements Cloneable {

    private final String loggerId;
    private final String loggerName;
    private final Map<String, String> properties;


    /**
     * Creates extended logger configuration options description structure.
     *
     * @param loggerId the ID of the logger customization record.
     * @param loggerExtOpts the logger extended options.
     * @throws IllegalArgumentException if given arguments contain <code>null</code> or insufficient value.
     * @see GAppLogExtOptions
     */
    public CustomLoggerExtConfig(
            final String loggerId,
            final String loggerExtOpts) {
        if (loggerId == null) {
            throw new IllegalArgumentException("loggerId can't be null");
        }
        this.loggerId = loggerId;

        if (loggerExtOpts == null || loggerExtOpts.isEmpty()) {
            throw new IllegalArgumentException("loggerExtOpts can't be empty");
        }

        int pos = loggerExtOpts.indexOf(':');
        if (pos <= 0) {
            throw new IllegalArgumentException("loggerExtOpts does not contain logger name");
        }
        this.loggerName = loggerExtOpts.substring(0, pos).trim();
        if (this.loggerName.isEmpty()) {
            throw new IllegalArgumentException("loggerExtOpts contains empty logger name");
        }

        this.properties = new HashMap<String, String>();

        final String loggerDef = loggerExtOpts.substring(pos + 1).trim();
        final String[] opts = loggerDef.split(",");
        if (opts != null && opts.length > 0) {
            for (final String opt: opts) {
                pos = opt.indexOf('=');
                if (pos > 0) {
                    final String optName = opt.substring(0, pos).trim();
                    final String optVal = opt.substring(pos + 1).trim();
                    this.properties.put(optName, optVal);
                }
            }
        }
        if (this.properties.isEmpty()) {
            throw new IllegalArgumentException("loggerExtOpts does not contain properties declarations");
        }
    }


    @Override
    public CustomLoggerExtConfig clone() {
        try {
            return (CustomLoggerExtConfig) super.clone();
        } catch (final CloneNotSupportedException ex) {
            return this;
        }
    }


    /**
     * Returns identifier of the custom logger record.
     *
     * @return the custom logger configuration identifier.
     */
    public String getId() {
        return loggerId;
    }

    /**
     * Returns the custom logger name.
     *
     * @return the custom logger name.
     */
    public String getName() {
        return loggerName;
    }

    /**
     * Returns property value of the custom logger configuration.
     *
     * @param name the property name.
     * @return value of the logger configuration property.
     */
    public String getProperty(final String name) {
        return properties.get(name);
    }

    /**
     * Returns the custom logger configuration properties.
     *
     * @return map with logger configuration properties.
     */
    public Map<String, String> getProperties() {
        return properties;
    }
}

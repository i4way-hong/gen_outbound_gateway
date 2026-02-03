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
package com.genesyslab.platform.apptemplate.configuration;

import com.genesyslab.platform.applicationblocks.com.objects.CfgDeltaApplication;

import com.genesyslab.platform.commons.protocol.runtime.ToStringHelper;
import com.genesyslab.platform.commons.protocol.runtime.LogHiddenAttributes;

import com.genesyslab.platform.commons.log.Log;
import com.genesyslab.platform.commons.log.ILogger;
import com.genesyslab.platform.commons.collections.KeyValuePair;
import com.genesyslab.platform.commons.collections.KeyValueCollection;

import java.util.Set;
import java.util.HashSet;
import java.util.Collections;


/**
 * Helper to hide message attributes or complex type attributes from <code>msg.toString()</code> output.
 * Supported complex types: attributes that extend <code>CompoundValue</code> or <code>AbstractPrintable</code>
 * This helper parses application options and apply result for <code>ToStringHelper</code>.
 * Options format:
 * <pre><code>
 * [log-hidden-attributes]
 *   &lt;ProtocolName&gt;.&lt;Message Name | Complex Attribute Name&gt; = &lt;Attributes List&gt;
 * For example:
 * [log-hidden-attributes]
 *   ContactServer.InteractionContent = "Text, StructuredText"
 * </code></pre>
 * If another options format required, use <code>ToStringHelper</code> directly.
 *
 * @see ToStringHelper
 */
public class MessagePrinterHelper {

    private static final ILogger log = Log.getLogger(MessagePrinterHelper.class);


    /**
     * Applies new hidden attributes configuration:
     * <pre><code>
     *   MessagePrinterHelper.setHiddenAttributes(cfgApp.getOptions().getList("log-hidden-attributes"));
     * </code></pre>
     *
     * @param configSection application options section
     */
    public static void setHiddenAttributes(final KeyValueCollection configSection) {
        log.debug("Setting new log hidden attributes configuration");
        if (configSection == null) {
            ToStringHelper.setHiddenAttributes(null);
        } else {
            ToStringHelper.setHiddenAttributes(parseOptions(configSection));
        }
    }

    /**
     * <p>Checks if <code>CfgDeltaApplication</code> has changes in hidden attributes section.</p>
     *
     * <p><i><b>Note</b>: There is a special kind of event on update notification, when
     * all options sections were removed. Unfortunately, this notification does not contain
     * information about existence of the required section before the update.
     * This method in PSDK 8.5 versions could miss this possible configuration change.
     * In PSDK 9.0 it treats "remove of all sections" as a change
     * even if there was no requested section defined before this.</i></p>
     *
     * @param delta application delta, received from Configuration Server.
     * @param sectionName section name in application options, i.e. "log-hidden-attributes".
     * @return <code>true</code> - if given delta object contains changes in the options section,
     *         or objects Options content was reset (cleared); otherwise - <code>false</code>.
     * @throws NullPointerException if section name is <code>null</code>
     */
    public static boolean isConfigurationChanged(
            final CfgDeltaApplication delta,
            final String sectionName)
                    throws NullPointerException {
        if (sectionName == null) {
            throw new NullPointerException("sectionName is null");
        }
        if (delta != null) {
            if (hasSection(delta.getAddedOptions(), sectionName)
                    || hasSection(delta.getChangedOptions(), sectionName)) {
                return true;
            }
            final KeyValueCollection delOpts = delta.getDeletedOptions();
            if ((delOpts != null)
                && (delOpts.isEmpty()
                    || hasSection(delOpts, sectionName))) {
                return true;
            }
        }
        return false;
    }


    private static boolean hasSection(
            final KeyValueCollection options,
            final String sectionName) {
        return options != null && options.containsKey(sectionName);
    }

    private static LogHiddenAttributes parseOptions(
            final KeyValueCollection logSection) {
        final LogHiddenAttributes config = new LogHiddenAttributes();
        for (final Object obj : logSection) {
            if (obj instanceof KeyValuePair) {
                final KeyValuePair pair = (KeyValuePair)obj;
                final String key = pair.getStringKey();
                if (key == null) continue;
                final String[] keyParts = key.split("\\.");
                if (keyParts.length >= 2) {	
                    final String protocolName = keyParts[0].trim();
                    final String className = keyParts[1].trim();

                    final String value = pair.getStringValue();
                    if (value == null || value.length() == 0) {
                        config.setHiddenAttributes(
                                protocolName, className, Collections.<String>emptySet());
                    } else {
                        final Set<String> attributeSet = new HashSet<String>();
                        final String[] valueParts = value.split("[,;]");
                        for (final String s : valueParts) {
                            attributeSet.add(s.trim());
                        }
                        config.setHiddenAttributes(protocolName, className, attributeSet);
                    }
                }
            }
        }
        return config;
    }
}

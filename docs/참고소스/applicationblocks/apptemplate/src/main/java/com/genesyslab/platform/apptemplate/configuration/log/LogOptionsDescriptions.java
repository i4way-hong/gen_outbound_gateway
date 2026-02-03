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

import com.genesyslab.platform.apptemplate.util.ConfigurationUtil;

import java.util.Map;
import java.util.List;
import java.util.Locale;
import java.util.HashMap;
import java.util.Collections;
import java.util.AbstractList;


/**
 * Consolidated container for the Genesys Common Log Options descriptions.
 * <p/>
 * <a href="https://docs.genesys.com/Special:Repository/85fr_ref-co.pdf?id=4b6870e4-2d5f-479c-ac29-11e43011db63">
 * Framework 8.5. Configuration Options. Reference Manual.</a>
 */
public final class LogOptionsDescriptions {

    private LogOptionsDescriptions() {}

    /**
     * A property descriptor class to represent information about
     * particular property in "log" section of CME Application "Options".
     *
     * @param <OptionValueType> Type of parsed value of the property.
     */
    public static final class OptionDescriptor<OptionValueType> {

        private final String          name;
        private final List<String>    names;
        private final OptionValueType defaultValue;

        private static final HashMap<String, OptionDescriptor<?>> optionsMap =
                new HashMap<String, OptionDescriptor<?>>();

        public static final Map<String, OptionDescriptor<?>> OPTIONS_MAP =
                Collections.unmodifiableMap(optionsMap);

        /**
         * Protected constructor for internal creation of the descriptions.
         * It is not supposed to create it out of this class.
         *
         * @param names the list of possible key names of the property.
         * @param defaultValue the default value of the property.
         */
        protected OptionDescriptor(
                final String[] names,
                final OptionValueType defaultValue) {
            this.name = names[0];
            this.names = new AbstractList<String>() {
                @Override
                public int size() {
                    return names.length;
                }
                @Override
                public String get(final int index) {
                    if (index >= 0 && index < names.length) {
                        return names[index];
                    }
                    throw new IndexOutOfBoundsException(
                            Integer.toString(index) + " out of "
                            + "[0," + (names.length - 1) + "]");
                }
            };
            this.defaultValue = defaultValue;

            for (String nm: names) {
                optionsMap.put(nm.toLowerCase(Locale.ENGLISH), OptionDescriptor.this);
            }
        }

        /**
         * Returns the name of the property.
         *
         * @return The name of the property.
         */
        public String optionName() {
            return name;
        }

        /**
         * Returns a read-only list of possible key names of the property.
         *
         * @return The list of possible key names of the property.
         */
        public List<String> optionNames() {
            return names;
        }

        /**
         * Returns the default value of the property.
         *
         * @return The default value of the property.
         */
        public OptionValueType defaultValue() {
            return defaultValue;
        }
    }


    /**
     * Platform SDK AppTemplate AB specific property to let user applications be able
     * to override the applications' host name in log files and message server
     * events.<br/>
     * It is used by the AppTemplate Log4j2 logging configuration functions in
     * {@link com.genesyslab.platform.apptemplate.log4j2.PsdkLog4j2Configuration PsdkLog4j2Configuration}
     * and {@link com.genesyslab.platform.apptemplate.log4j2.Log4j2Configurator Log4j2Configurator}.
     * <p/>
     * The log option name (case insensitive): "<code>EventLogHost</code>", "<code>event-log-host</code>",
     * or "<code>event_log_host</code>".
     * <p/>
     * For example:<pre>
     * event-log-host = node-1-virtual-host</pre>
     */
    public static final OptionDescriptor<String> EVENTLOG_HOST_OPT =
            new OptionDescriptor<String>(
                    new String[] { "EventLogHost", "event-log-host", "event_log_host" },
                    ConfigurationUtil.getLocalhostName());

    /**
     * Specifies the file name for application-specific log events. The name must be
     * valid for the operating system on which the application is running. The option
     * value can also contain the absolute path to the application-specific *.lms file.
     * Otherwise, an application looks for the file in its working directory.
     * <p/>
     * The log option name (case insensitive): "<code>MessageFile</code>", "<code>message-file</code>",
     * or "<code>message_file</code>".
     * <p/><table border="0">
     * <tr><td>Default Value:</td><td>As specified by a particular application</td></tr>
     * <tr><td>Valid Values:</td><td>Any valid message file ("<code><i>&lt;filename&gt;</i>.lms</code>")</td></tr>
     * <tr><td>Changes Take Effect:</td><td>Immediately, if an application cannot find its "<code>*.lms</code>"
     * file at startup.</td></tr>
     * </table>
     * <p/>
     * For example:<pre>
     * message-file = my-app.lms</pre>
     * <p/>
     * <i><b>Warning!</b> An application that does not find its *.lms file at startup cannot
     * generate application-specific log events and send them to Message Server.</i>
     */
    public static final OptionDescriptor<String> MESSAGEFILE_OPT =
            new OptionDescriptor<String>(
                    new String[] { "MessageFile", "message-file", "message_file" },
                    null);

    /**
     * Specifies if log output is created, and if so, the minimum level of log events
     * generated. Log event levels, starting with the highest priority level, are
     * Standard, Interaction, Trace, and Debug.
     * <p/>
     * The log option name (case insensitive): "<code>Verbose</code>".
     * <p/><table border="0">
     * <tr><td>Default Value:</td><td>"<code>all</code>"</td></tr>
     * <tr><td rowspan="6" valign="top">Valid Values:</td>
     * <td valign="top">"<code>all</code>"</td><td>- All log events (that is, log events of the Standard, Trace,
     * Interaction, and Debug levels) are generated.</td></tr>
     * <tr><td valign="top">"<code>debug</code>"</td><td>- The same as all.</td></tr>
     * <tr><td valign="top">"<code>trace</code>"</td><td>- Log events of Trace level and higher (that is, log events of
     * Standard, Interaction, and Trace levels) are generated, but log
     * events of the Debug level are not generated.</td></tr>
     * <tr><td valign="top">"<code>interaction</code>"</td><td>- Log events of Interaction level and higher
     * (that is, log events of Standard and Interaction levels) are generated, but log events
     * of Trace and Debug levels are not generated.</td></tr>
     * <tr><td valign="top">"<code>standard</code>"</td><td>- Log events of Standard level are generated, but log events of
     * Interaction, Trace, and Debug levels are not generated.</td></tr>
     * <tr><td valign="top">"<code>none</code>"</td><td>- No log output is produced.</td></tr>
     * <tr><td>Changes Take Effect:</td><td>Immediately</td></tr>
     * </table>
     * <p/>
     * <i><b>Note:</b> For definitions of the Standard, Interaction, Trace, and Debug log
     * levels, refer to the Framework Management Layer User’s Guide or
     * Framework Genesys Administrator Help.</i>
     * <p/>
     * To configure log outputs, set log level options ("<code>all</code>", "<code>alarm</code>",
     * "<code>standard</code>", "<code>interaction</code>", "<code>trace</code>", and/or "<code>debug</code>")
     * to the desired types of log output ("<code>stdout</code>", "<code>stderr</code>", "<code>network</code>",
     * "<code>memory</code>", and/or [filename], for log file output).<br/>
     * You can use:<ul>
     * <li>One log level option to specify different log outputs.</li>
     * <li>One log output type for different log levels.</li>
     * <li>Several log output types simultaneously, to log events of the same or
     *     different log levels.</li></ul>
     * You must separate the log output types by a comma when you are configuring
     * more than one output for the same log level.
     */
    public static final OptionDescriptor<VerboseLevel> VERBOSE_OPT =
            new OptionDescriptor<VerboseLevel>(
                    new String[] { "Verbose" },
                    VerboseLevel.ALL);

    /**
     * Specifies the outputs to which an application sends all log events. The log
     * output types must be separated by a comma when more than one output is
     * configured.
     * <p/>
     * The log option name (case insensitive): "<code>All</code>".
     * <p/><table border="0">
     * <tr><td>Default Value:</td><td>No default value.</td></tr>
     * <tr><td colspan="2">Valid Values (log output types):</td></tr>
     * <tr><td><code>stdout</code></td><td>Log events are sent to the Standard output (stdout).</td></tr>
     * <tr><td><code>stderr</code></td><td>Log events are sent to the Standard error output (stderr).</td></tr>
     * <tr><td valign="top"><code>network</code></td><td>Log events are sent to Message Server, which can reside
     *   anywhere on the network. Message Server stores the log events in the Log Database.<br/>
     *   Setting the <code>all</code> log level option to the <code>network</code> output enables an
     *   application to send log events of the <code>Standard</code>, <code>Interaction</code>,
     *   and <code>Trace</code> levels to Message Server. <code>Debug</code>-level log events are
     *   neither sent to Message Server nor stored in the Log Database.</td></tr>
     * <tr><td valign="top"><code>[filename]</code></td><td>Log events are stored in a file with the specified name.
     *   If a path is not specified, the file is created in the application’s working directory.</td></tr>
     * <tr><td>Changes&nbsp;Take&nbsp;Effect:</td><td>Immediately</td></tr>
     * </table>
     * <p/>
     * For example:<pre>
     * all = stdout, logfile</pre>
     * <p/>
     * <i><b>Note<sup>1</sup>:</b> The log output options are activated according to the setting of the
     * {@link #VERBOSE_OPT Verbose} configuration option.</i>
     * <p/>
     * <i><b>Note<sup>2</sup>:</b> To ease the troubleshooting process, consider using unique names for
     * log files that different applications generate.</i>
     * <p/>
     * <i><b>Warning!</b> Directing log output to the console (by using
     * "<code>stdout</code>", "<code>stderr</code>" settings) can affect application performance.
     * Avoid using these log output settings in a production environment.</i>
     *
     * @see #VERBOSE_OPT
     */
    public static final OptionDescriptor<String> ALL_OPT =
            new OptionDescriptor<String>(
                    new String[] { "all" },
                    null);

    /**
     * Specifies the outputs to which an application sends the log events of the <code>Alarm</code>
     * level. The log output types must be separated by a comma when more than one output is configured.
     * <p/>
     * The log option name (case insensitive): "<code>Alarm</code>".
     * <p/><table border="0">
     * <tr><td>Default Value:</td><td>No default value.</td></tr>
     * <tr><td colspan="2">Valid Values (log output types):</td></tr>
     * <tr><td><code>stdout</code></td><td>Log events are sent to the Standard output (stdout).</td></tr>
     * <tr><td><code>stderr</code></td><td>Log events are sent to the Standard error output (stderr).</td></tr>
     * <tr><td valign="top"><code>network</code></td><td>Log events are sent to Message Server, which can reside
     *   anywhere on the network. Message Server stores the log events in the Log Database.</td></tr>
     * <tr><td valign="top"><code>[filename]</code></td><td>Log events are stored in a file with the specified name.
     *   If a path is not specified, the file is created in the application’s working directory.</td></tr>
     * <tr><td>Changes&nbsp;Take&nbsp;Effect:</td><td>Immediately</td></tr>
     * </table>
     * <p/>
     * For example:<pre>
     * alarm = stderr, network</pre>
     * <p/>
     * <i><b>Note<sup>1</sup>:</b> The log output options are activated according to the setting of the
     * {@link #VERBOSE_OPT Verbose} configuration option.</i>
     * <p/>
     * <i><b>Note<sup>2</sup>:</b> To ease the troubleshooting process, consider using unique names for
     * log files that different applications generate.</i>
     * <p/>
     * <i><b>Warning!</b> Directing log output to the console (by using
     * "<code>stdout</code>", "<code>stderr</code>" settings) can affect application performance.
     * Avoid using these log output settings in a production environment.</i>
     *
     * @see #VERBOSE_OPT
     */
    public static final OptionDescriptor<String> ALARM_OPT =
            new OptionDescriptor<String>(
                    new String[] { "alarm" },
                    null);

    /**
     * Specifies the outputs to which an application sends the log events of the
     * <code>Standard</code> level. The log output types must be separated by a comma when more
     * than one output is configured.
     * <p/>
     * The log option name (case insensitive): "<code>Standard</code>".
     * <p/><table border="0">
     * <tr><td>Default Value:</td><td>No default value.</td></tr>
     * <tr><td colspan="2">Valid Values (log output types):</td></tr>
     * <tr><td><code>stdout</code></td><td>Log events are sent to the Standard output (stdout).</td></tr>
     * <tr><td><code>stderr</code></td><td>Log events are sent to the Standard error output (stderr).</td></tr>
     * <tr><td valign="top"><code>network</code></td><td>Log events are sent to Message Server, which can reside
     *   anywhere on the network. Message Server stores the log events in the Log Database.</td></tr>
     * <tr><td valign="top"><code>[filename]</code></td><td>Log events are stored in a file with the specified name.
     *   If a path is not specified, the file is created in the application’s working directory.</td></tr>
     * <tr><td>Changes&nbsp;Take&nbsp;Effect:</td><td>Immediately</td></tr>
     * </table>
     * <p/>
     * For example:<pre>
     * standard = stdout, logfile</pre>
     * <p/>
     * <i><b>Note<sup>1</sup>:</b> The log output options are activated according to the setting of the
     * {@link #VERBOSE_OPT Verbose} configuration option.</i>
     * <p/>
     * <i><b>Note<sup>2</sup>:</b> To ease the troubleshooting process, consider using unique names for
     * log files that different applications generate.</i>
     * <p/>
     * <i><b>Warning!</b> Directing log output to the console (by using
     * "<code>stdout</code>", "<code>stderr</code>" settings) can affect application performance.
     * Avoid using these log output settings in a production environment.</i>
     *
     * @see #VERBOSE_OPT
     */
    public static final OptionDescriptor<String> STANDARD_OPT =
            new OptionDescriptor<String>(
                    new String[] { "standard" },
                    null);

    /**
     * Specifies the outputs to which an application sends the log events of the
     * <code>Interaction</code> level and higher (that is, log events of the <code>Standard</code> and
     * <code>Interaction</code> levels). The log outputs must be separated by a comma when
     * more than one output is configured.
     * <p/>
     * The log option name (case insensitive): "<code>Interaction</code>".
     * <p/><table border="0">
     * <tr><td>Default Value:</td><td>No default value.</td></tr>
     * <tr><td colspan="2">Valid Values (log output types):</td></tr>
     * <tr><td><code>stdout</code></td><td>Log events are sent to the Standard output (stdout).</td></tr>
     * <tr><td><code>stderr</code></td><td>Log events are sent to the Standard error output (stderr).</td></tr>
     * <tr><td valign="top"><code>network</code></td><td>Log events are sent to Message Server, which can reside
     *   anywhere on the network. Message Server stores the log events in the Log Database.</td></tr>
     * <tr><td valign="top"><code>[filename]</code></td><td>Log events are stored in a file with the specified name.
     *   If a path is not specified, the file is created in the application’s working directory.</td></tr>
     * <tr><td>Changes&nbsp;Take&nbsp;Effect:</td><td>Immediately</td></tr>
     * </table>
     * <p/>
     * For example:<pre>
     * interaction = stdout, logfile</pre>
     * <p/>
     * <i><b>Note<sup>1</sup>:</b> The log output options are activated according to the setting of the
     * {@link #VERBOSE_OPT Verbose} configuration option.</i>
     * <p/>
     * <i><b>Note<sup>2</sup>:</b> To ease the troubleshooting process, consider using unique names for
     * log files that different applications generate.</i>
     * <p/>
     * <i><b>Warning!</b> Directing log output to the console (by using
     * "<code>stdout</code>", "<code>stderr</code>" settings) can affect application performance.
     * Avoid using these log output settings in a production environment.</i>
     *
     * @see #VERBOSE_OPT
     */
    public static final OptionDescriptor<String> INTERACTION_OPT =
            new OptionDescriptor<String>(
                    new String[] { "interaction" },
                    null);

    /**
     * Specifies the outputs to which an application sends the log events of the <code>Trace</code>
     * level and higher (that is, log events of the <code>Standard</code>, <code>Interaction</code>,
     * and <code>Trace</code> levels). The log outputs must be separated by a comma when more than one
     * output is configured.
     * <p/>
     * The log option name (case insensitive): "<code>Trace</code>".
     * <p/><table border="0">
     * <tr><td>Default Value:</td><td>No default value.</td></tr>
     * <tr><td colspan="2">Valid Values (log output types):</td></tr>
     * <tr><td><code>stdout</code></td><td>Log events are sent to the Standard output (stdout).</td></tr>
     * <tr><td><code>stderr</code></td><td>Log events are sent to the Standard error output (stderr).</td></tr>
     * <tr><td valign="top"><code>network</code></td><td>Log events are sent to Message Server, which can reside
     *   anywhere on the network. Message Server stores the log events in the Log Database.<br/>
     *   Setting the <code>all</code> log level option to the <code>network</code> output enables an
     *   application to send log events of the <code>Standard</code>, <code>Interaction</code>,
     *   and <code>Trace</code> levels to Message Server. <code>Debug</code>-level log events are
     *   neither sent to Message Server nor stored in the Log Database.</td></tr>
     * <tr><td valign="top"><code>[filename]</code></td><td>Log events are stored in a file with the specified name.
     *   If a path is not specified, the file is created in the application’s working directory.</td></tr>
     * <tr><td>Changes&nbsp;Take&nbsp;Effect:</td><td>Immediately</td></tr>
     * </table>
     * <p/>
     * For example:<pre>
     * trace = stdout, logfile</pre>
     * <p/>
     * <i><b>Note<sup>1</sup>:</b> The log output options are activated according to the setting of the
     * {@link #VERBOSE_OPT Verbose} configuration option.</i>
     * <p/>
     * <i><b>Note<sup>2</sup>:</b> To ease the troubleshooting process, consider using unique names for
     * log files that different applications generate.</i>
     * <p/>
     * <i><b>Warning!</b> Directing log output to the console (by using
     * "<code>stdout</code>", "<code>stderr</code>" settings) can affect application performance.
     * Avoid using these log output settings in a production environment.</i>
     *
     * @see #VERBOSE_OPT
     */
    public static final OptionDescriptor<String> TRACE_OPT =
            new OptionDescriptor<String>(
                    new String[] { "trace" },
                    null);

    /**
     * Specifies the outputs to which an application sends the log events of the <code>Debug</code>
     * level and higher (that is, log events of the <code>Standard</code>, <code>Interaction</code>,
     * <code>Trace</code>, and <code>Debug</code> levels).
     * The log output types must be separated by a comma when more than one output is configured.
     * <p/>
     * The log option name (case insensitive): "<code>Debug</code>".
     * <p/><table border="0">
     * <tr><td>Default Value:</td><td>No default value.</td></tr>
     * <tr><td colspan="2">Valid Values (log output types):</td></tr>
     * <tr><td><code>stdout</code></td><td>Log events are sent to the Standard output (stdout).</td></tr>
     * <tr><td><code>stderr</code></td><td>Log events are sent to the Standard error output (stderr).</td></tr>
     * <tr><td valign="top"><code>network</code></td><td>Log events are sent to Message Server, which can reside
     *   anywhere on the network. Message Server stores the log events in the Log Database.</td></tr>
     * <tr><td valign="top"><code>[filename]</code></td><td>Log events are stored in a file with the specified name.
     *   If a path is not specified, the file is created in the application’s working directory.</td></tr>
     * <tr><td>Changes&nbsp;Take&nbsp;Effect:</td><td>Immediately</td></tr>
     * </table>
     * <p/>
     * For example:<pre>
     * debug = stdout, logfile</pre>
     * <p/>
     * <i><b>Note<sup>1</sup>:</b> The log output options are activated according to the setting of the
     * {@link #VERBOSE_OPT Verbose} configuration option.</i>
     * <p/>
     * <i><b>Note<sup>2</sup>:</b> To ease the troubleshooting process, consider using unique names for
     * log files that different applications generate.</i>
     * <p/>
     * <i><b>Note<sup>3</sup>:</b> Debug-level log events are never sent to Message Server or stored in
     * the Log Database.
     * <p/>
     * <i><b>Warning!</b> Directing log output to the console (by using
     * "<code>stdout</code>", "<code>stderr</code>" settings) can affect application performance.
     * Avoid using these log output settings in a production environment.</i>
     *
     * @see #VERBOSE_OPT
     */
    public static final OptionDescriptor<String> DEBUG_OPT =
            new OptionDescriptor<String>(
                    new String[] { "debug" },
                    null);


    /**
     * Specifies whether there is a segmentation limit for a log file. If there is, sets the
     * mode of measurement, along with the maximum size. If the current log
     * segment exceeds the size set by this option, the file is closed and a new one is
     * created. This option is ignored if log output is not configured to be sent to a log file.
     * <p/>
     * The log option name (case insensitive): "<code>Segment</code>".
     * <p/><table border="0">
     * <tr><td>Default Value:</td><td>"<code>100 MB</code>"</td></tr>
     * <tr><td rowspan="4" valign="top">Valid Values:</td>
     * <td>"<code>false</code>"</td><td>- No segmentation is allowed.</td></tr>
     * <tr><td>"<code><i>&lt;number&gt;</i>[ KB]</code>"</td>
     * <td>- Sets the maximum segment size, in kilobytes. The minimum
     * segment size is 100 KB.</td></tr>
     * <tr><td>"<code><i>&lt;number&gt;</i> MB</code>"</td>
     * <td>- Sets the maximum segment size, in megabytes.</td></tr>
     * <tr><td>"<code><i>&lt;number&gt;</i> hr</code>"</td>
     * <td>- Sets the number of hours for the segment to stay open. The
     * minimum number is 1 hour.</td></tr>
     * <tr><td>Changes Take Effect:</td><td>Immediately</td></tr>
     * </table>
     */
    public static final OptionDescriptor<SegmentationConfig> SEGMENT_OPT =
            new OptionDescriptor<SegmentationConfig>(
                    new String[] { "Segment" },
                    SegmentationConfig.DEFAULT_SEGMENTATION);

    /**
     * Determines whether log files expire. If they do, sets the measurement for
     * determining when they expire, along with the maximum number of files
     * (segments) or days before the files are removed. This option is ignored if log
     * output is not configured to be sent to a log file.
     * <p/>
     * The log option name (case insensitive): "<code>Expire</code>".
     * <p/><table border="0">
     * <tr><td>Default Value:</td><td><code>10</code></td></tr>
     * <tr><td rowspan="3" valign="top">Valid Values:</td>
     * <td><code>false</code></td><td>- No expiration; all generated segments are stored.</td></tr>
     * <tr><td>"<code>&lt;number&gt;[ file]</code>"</td>
     * <td valign="top">- Sets the maximum number of log files to store. Specify a number from
     * <code>1–1000</code>.</td></tr>
     * <tr><td>"<code>&lt;number&gt; day</code>"</td><td>- Sets the maximum number of days before log files are
     * deleted. Specify a number from <code>1–100</code>.</td></tr>
     * <tr><td>Changes Take Effect:</td><td colspan="2">Immediately</td></tr>
     * </table>
     * <p/>
     * <i><b>Note:</b> If an option’s value is set incorrectly (out of the range of valid
     * values), it will be automatically reset to <code>10</code>.</i>
     */
    public static final OptionDescriptor<ExpirationConfig> EXPIRE_OPT =
            new OptionDescriptor<ExpirationConfig>(
                    new String[] { "Expire" },
                    ExpirationConfig.DEFAULT_EXPIRATION);

    /**
     * Platform SDK AppTemplate AB specific property to specify method that will be used for archiving log files.
     * <p/>
     * The log option name (case insensitive): "<code>CompressMethod</code>", "<code>compress-method</code>",
     * or "<code>compress_method</code>".
     * <p/><table border="0">
     * <tr><td>Default Value:</td><td><code>none</code></td></tr>
     * <tr><td rowspan="4" valign="top">Valid Values:</td>
     * <td><code>none</code></td><td>- No archiving; all generated log files are stored "as-is".</td></tr>
     * <tr><td>"<code>gzip</code>"</td>
     * <td valign="top">- GZip archiving is to be used for "historical" log files.</td></tr>
     * <tr><td>"<code>zip</code>"</td>
     * <td valign="top">- Zip archiving is to be used for "historical" log files.</td></tr>
     * <tr><td>"<code>zip<i>&lt;digit&gt;</i></code>"</td>
     * <td valign="top">- Zip archiving with given compression level is to be used for "historical" log files.</td></tr>
     * <tr><td>Changes Take Effect:</td><td colspan="2">Immediately</td></tr>
     * </table>
     * @since 9.0.003.01
     */
    public static final OptionDescriptor<String> COMPRESS_METHOD_OPT =
            new OptionDescriptor<String>(
                    new String[] { "CompressMethod", "compress-method", "compress_method" },
                    null);

    /**
     * Specifies the system in which an application calculates the log record time
     * when generating a log file. The time is converted from the time in seconds
     * since "00:00:00 UTC, January 1, 1970".
     * <p/>
     * The log option name (case insensitive): "<code>TimeConvert</code>", "<code>time-convert</code>",
     * or "<code>time_convert</code>".
     * <p/><table border="0">
     * <tr><td>Default Value:</td><td>"<code>local</code>"</td></tr>
     * <tr><td rowspan="2" valign="top">Valid Values:</td>
     * <td valign="top">"<code>local</code>"</td><td>- The time of log record generation is expressed
     * as a local time, based on the time zone and any seasonal adjustments. Time zone
     * information of the application’s host computer is used.</td></tr>
     * <tr><td>"<code>utc</code>"</td><td>- The time of log record generation is expressed as Coordinated
     * Universal Time (UTC).</td></tr>
     * <tr><td>Changes Take Effect:</td><td colspan="2">Immediately</td></tr>
     * </table>
     */
    public static final OptionDescriptor<TimeUsage> TIME_CONVERT_OPT =
            new OptionDescriptor<TimeUsage>(
                    new String[] { "TimeConvert", "time-convert", "time_convert" },
                    TimeUsage.LOCAL);

    /**
     * Specifies how to represent, in a log file, the time when an application generates
     * log records.
     * A log record’s time field in the ISO 8601 format looks like this:
     * "<code>2001-07-24T04:58:10.123</code>".
     * <p/>
     * The log option name (case insensitive): "<code>TimeFormat</code>", "<code>time-format</code>",
     * or "<code>time_format</code>".
     * <p/><table border="0">
     * <tr><td>Default Value:</td><td>"<code>time</code>"</td></tr>
     * <tr><td rowspan="3" valign="top">Valid Values:</td>
     * <td valign="top">"<code>time</code>"</td><td>- The time string is formatted according to
     * "<code>HH:MM:SS.sss</code>" (hours, minutes, seconds, and milliseconds) format.</td></tr>
     * <tr><td>"<code>locale</code>"</td><td>- The time string is formatted according to the system’s locale.</td></tr>
     * <tr><td valign="top">"<code>iso8601</code>"</td><td>- The date in the time string is formatted according
     * to the ISO 8601 format. Fractional seconds are given in milliseconds.</td></tr>
     * <tr><td>Changes Take Effect:</td><td colspan="2">Immediately</td></tr>
     * </table>
     */
    public static final OptionDescriptor<TimeFormat> TIME_FORMAT_OPT =
            new OptionDescriptor<TimeFormat>(
                    new String[] { "TimeFormat", "time-format", "time_format" },
                    TimeFormat.TIME);

    /**
     * Specifies the format of log record headers that an application uses when
     * writing logs in the log file. Using compressed log record headers improves
     * application performance and reduces the log file's size.
     * With the value set to <code>short</code>:<ul>
     * <li>A header of the log file or the log file segment contains information about
     * the application (such as the application name, application type, host type,
     * and time zone), whereas single log records within the file or segment omit
     * this information.</li>
     * <li>A log message priority is abbreviated to Std, Int, Trc, or Dbg, for Standard,
     * Interaction, Trace, or Debug messages, respectively.</li>
     * <li>The message ID does not contain the prefix GCTI or the application type ID.</li></ul>
     * <p/>
     * The log option name (case insensitive): "<code>MessageFormat</code>", "<code>message-format</code>",
     * or "<code>message_format</code>".
     * <p/><table border="0">
     * <tr><td>Default Value:</td><td>"<code>medium</code>"</td></tr>
     * <tr><td rowspan="6" valign="top">Valid Values:</td>
     * <td>"<code>short</code>"</td><td>- An application uses compressed headers when writing log records in its log file.</td></tr>
     * <tr><td>"<code>medium</code>"</td><td>- An application uses medium size headers when writing log records in its log file.</td></tr>
     * <tr><td>"<code>full</code>"</td><td>- An application uses complete headers when writing log records in its log file.</td></tr>
     * <tr><td valign="top">"<code>shortcsv</code>"&nbsp;<b>*</b></td><td>- An application uses compressed headers with comma delimiter
     * when writing log records in its log file.</td></tr>
     * <tr><td valign="top">"<code>shorttsv</code>"&nbsp;<b>*</b></td><td>- An application uses compressed headers with tab char delimiter
     * when writing log records in its log file.</td></tr>
     * <tr><td valign="top">"<code>shortdsv</code>"&nbsp;<b>*</b></td><td>- An application uses compressed headers with
     * {@link #MESSAGE_HEADER_DELIMITER_OPT} delimiter when writing log records in its log file.</td></tr>
     * <tr><td>Changes Take Effect:</td><td colspan="2">Immediately</td></tr>
     * </table>
     * <p/>
     * <i><b>*</b> - values marked with the asterisk are valid for Platform SDK AppTemplate based applications only.</i>
     * <p/>A log record in the <code>full</code> format looks like this:
     * <pre>2002-05-07T18:11:38.196 Standard localhost cfg_dbserver GCTI-00-05060 Application started</pre>
     * A log record in the <code>short</code> format looks like this:
     * <pre>2002-05-07T18:15:33.952 Std 05060 Application started</pre>
     * <p/>
     * <i><b>Note:</b> Whether the <code>full</code>, <code>short</code>, or any other format is used,
     * time is printed in the format specified by the {@link #TIME_FORMAT_OPT} option.</i>
     */
    public static final OptionDescriptor<MessageFormat> MESSAGE_FORMAT_OPT =
            new OptionDescriptor<MessageFormat>(
                    new String[] { "MessageFormat", "message-format", "message_format" },
                    MessageFormat.SHORT);

    /**
     * Platform SDK AppTemplate AB specific option.<br/>
     * It enables support of native log levels (like "Error", "Warn", etc) to be used for non-LMS messages
     * in common LMS events formats instead of LMS levels like "Standard", "Interaction", etc.
     * <p/>
     * The log option name (case insensitive): "<code>UseNativeLevels</code>",
     * "<code>use-native-levels</code>", or "<code>use_native_levels</code>".
     * <p/>
     * <i><b>Note:</b> This option is experimental and its value procession may get changed.</i>
     *
     * @see #MESSAGE_FORMAT_OPT
     * @since 9.0.002.09
     */
    public static final OptionDescriptor<Boolean> USE_NATIVE_LEVELS_OPT =
            new OptionDescriptor<Boolean>(
                    new String[] { "UseNativeLevels", "use-native-levels", "use_native_levels" },
                    null);

    /**
     * Platform SDK AppTemplate AB specific property as a parameter for
     * "<code>shortdsv</code>" message format ({@link #MESSAGE_FORMAT_OPT}).
     * <p/>
     * The log option name (case insensitive): "<code>MessageHeaderDelimiter</code>", "<code>message-header-delimiter</code>",
     * or "<code>message_header_delimiter</code>".<br/>
     * Its default value is "<code>|</code>".
     */
    public static final OptionDescriptor<String> MESSAGE_HEADER_DELIMITER_OPT =
            new OptionDescriptor<String>(
                    new String[] {
                            "MessageHeaderDelimiter", "message-header-delimiter", "message_header_delimiter" },
                    "|");

    /**
     * Platform SDK AppTemplate AB specific option.<br/>
     * Value of this option is used as a log message pattern if {@link #MESSAGE_FORMAT_OPT}
     * option value is equal to "<code>custom</code>".<br/>
     * In comparison with {@link #OUTPUT_PATTERN_OPT}, this option provides predefined messages
     * prefix containing timestamp (by {@link #TIME_FORMAT_OPT}/{@link #TIME_CONVERT_OPT}),
     * and the LMS-style log level.
     * <p/>
     * The log option name (case insensitive): "<code>CustomMessageFormat</code>",
     * "<code>custom-message-format</code>", or "<code>custom_message_format</code>".
     * <p/>
     * <i><b>Note:</b> This option is experimental and its value procession may get changed.</i>
     *
     * @see #MESSAGE_FORMAT_OPT
     * @see MessageFormat#Custom
     * @see #OUTPUT_PATTERN_OPT
     * @since 9.0.002.01
     */
    public static final OptionDescriptor<String> CUSTOM_MESSAGE_FORMAT_OPT =
            new OptionDescriptor<String>(
                    new String[] { "CustomMessageFormat", "custom-message-format", "custom_message_format" },
                    null);

    /**
     * Platform SDK AppTemplate AB specific option.<br/>
     * Value of this option is used as a log message pattern if {@link #MESSAGE_FORMAT_OPT}
     * option value is equal to "<code>custom</code>".<br/>
     * In comparison with {@link #CUSTOM_MESSAGE_FORMAT_OPT}, this option does not provide predefined
     * messages prefix like a timestamp with log level.
     * <p/>
     * The log option name (case insensitive): "<code>OutputPattern</code>",
     * "<code>output-pattern</code>", or "<code>output_pattern</code>".
     * <p/>
     * <i><b>Note:</b> This option is experimental and its value procession may get changed.</i>
     *
     * @see #MESSAGE_FORMAT_OPT
     * @see MessageFormat#Custom
     * @see #CUSTOM_MESSAGE_FORMAT_OPT
     * @since 9.0.002.05
     */
    public static final OptionDescriptor<String> OUTPUT_PATTERN_OPT =
            new OptionDescriptor<String>(
                    new String[] { "OutputPattern", "output-pattern", "output_pattern" },
                    null);


    /**
     * Platform SDK AppTemplate AB specific property for configuration of the
     * log files encoding.
     * <p/>
     * The log option name (case insensitive): "<code>FileEncoding</code>", "<code>file-encoding</code>",
     * or "<code>file_encoding</code>".<br/>
     * Its default value is "<code>UTF-8</code>".
     */
    public static final OptionDescriptor<String> LOGFILE_ENCODING_OPT =
            new OptionDescriptor<String>(
                    new String[] { "FileEncoding", "file-encoding", "file_encoding" },
                    "UTF-8");


    /**
     * Platform SDK AppTemplate AB specific property for customization of the
     * log files header content.
     * <p/>
     * The log option name (case insensitive): "<code>FileHeaderProvider</code>", "<code>file-header-provider</code>",
     * or "<code>file_header_provider</code>".<br/>
     * If this option is not specified, provider is been taken with SPI declaration for interface
     * {@link com.genesyslab.platform.apptemplate.log4j2plugin.FileHeaderProvider}.
     */
    public static final OptionDescriptor<String> FILE_HEADER_PROVIDER_OPT =
            new OptionDescriptor<String>(
                    new String[] {
                            "FileHeaderProvider", "file-header-provider", "file_header_provider" },
                    null);


    /**
     * Turns on/off operating system file buffering. The option is applicable only to
     * the <code>stderr</code> and <code>stdout</code> output.
     * Setting this option to true increases the output performance.
     * <p/>
     * The log option name (case insensitive): "<code>Buffering</code>".
     * <p/><table border="0">
     * <tr><td>Default Value:</td><td><code>true</code></td></tr>
     * <tr><td rowspan="2" valign="top">Valid Values:</td>
     * <td><code>true</code></td><td>- enables buffering.</td></tr>
     * <tr><td><code>false</code></td><td>- disables buffering.</td></tr>
     * <tr><td>Changes Take Effect:</td><td colspan="2">Immediately</td></tr>
     * </table>
     * <p/>
     * <i><b>Note:</b> Platform SDK AppTemplate AB logging configuration logic
     * does not support this option. It relays on jvm buffering of <code>System.out</code></i>
     */
    public static final OptionDescriptor<Boolean> BUFFERING_OPT =
            new OptionDescriptor<Boolean>(
                    new String[] { "Buffering" },
                    Boolean.TRUE);

    /**
     * Specifies, in hours, how often the application generates a check point log
     * event, to divide the log into sections of equal time. By default, the application
     * generates this log event every hour. Setting the option to 0 prevents the
     * generation of check-point events.
     * <p/>
     * The log option name (case insensitive): "<code>CheckPoint</code>", "<code>check-point</code>",
     * or "<code>check_point</code>".
     * <p/><table border="0">
     * <tr><td>Default Value:</td><td><code>1</code></td></tr>
     * <tr><td>Valid Values:</td><td><code>0–24</code></td></tr>
     * <tr><td>Changes Take Effect:</td><td>Immediately</td></tr>
     * </table>
     * <p/>
     * <i><b>Note:</b> Platform SDK AppTemplate AB does not support this option.</i>
     */
    public static final OptionDescriptor<Integer> CHECK_POINT_OPT =
            new OptionDescriptor<Integer>(
                    new String[] { "CheckPoint", "check-point", "check_point" },
                    1);


    /**
     * Specifies whether to enable or disable the logging thread. If set to true (the
     * logging thread is enabled), the logs are stored in an internal queue to be written
     * to the specified output by a dedicated logging thread. This setting also enables
     * the log throttling feature, which allows the verbose level to be dynamically
     * reduced when a logging performance issue is detected. Refer to the Framework
     * 8.5 Management Framework User’s Guide for more information about the log
     * throttling feature.<br/>
     * If this option is set to false (the logging thread is disabled), each log is written
     * directly to the outputs by the thread that initiated the log request. This setting
     * also disables the log throttling feature.
     * <p/>
     * The log option name (case insensitive): "<code>EnableThread</code>", "<code>enable-thread</code>",
     * or "<code>enable_thread</code>".
     * <p/><table border="0">
     * <tr><td>Default Value:</td><td><code>false</code></td></tr>
     * <tr><td>Valid Values:</td><td><code>true</code>, <code>false</code></td></tr>
     * <tr><td>Changes Take Effect:</td><td>Immediately</td></tr>
     * </table>
     * <p/>
     * <i><b>Note*:</b> Platform SDK AppTemplate AB does not support the log throttling feature.</i>
     */
    public static final OptionDescriptor<Boolean> ENABLE_THREAD_OPT =
            new OptionDescriptor<Boolean>(
                    new String[] { "EnableThread", "enable-thread", "enable_thread" },
                    Boolean.FALSE);

    /**
     * Platform SDK AppTemplate AB specific option to enable the call location information passing
     * to the Log4j2 logging thread, which was enabled with option {@link #ENABLE_THREAD_OPT}.
     * <p/>
     * The log option name (case insensitive): "<code>EnableLocationForThread</code>",
     * "<code>enable-location-for-thread</code>", or "<code>enable_location_for_thread</code>".
     * <p/>
     * If one of the layouts is configured with a location-related attribute like HTML
     * <a href="https://logging.apache.org/log4j/2.x/manual/layouts.html#HtmlLocationInfo">locationInfo</a>,
     * or one of the patterns <code>%C</code> or <code>$class</code>, <code>%F</code> or <code>%file</code>,
     * <code>%l</code> or <code>%location</code>, <code>%L</code> or <code>%line</code>, <code>%M</code>
     * or <code>%method</code>, Log4j will take a snapshot of the stack, and walk the stack trace
     * to find the location information.<br/>
     * This is an expensive operation: 1.3 - 5 times slower for synchronous loggers.
     * Synchronous loggers wait as long as possible before they take this stack snapshot.
     * If no location is required, the snapshot will never be taken.<br/>
     * However, asynchronous loggers need to make this decision before passing the log message
     * to another thread; the location information will be lost after that point. The performance impact
     * of taking a stack trace snapshot is even higher for asynchronous loggers: logging with location is
     * 4 - 20 times slower than without location. For this reason, asynchronous loggers
     * do not include location information by default.
     *
     * @see #ENABLE_THREAD_OPT
     */
    public static final OptionDescriptor<Boolean> ENABLE_LOCATION_FOR_THREAD_OPT =
            new OptionDescriptor<Boolean>(
                    new String[] {
                            "EnableLocationForThread", "enable-location-for-thread", "enable_location_for_thread" },
                    Boolean.FALSE);

    /**
     * Specifies whether a startup segment of the log, containing the initial
     * configuration options, is to be kept. If it is, this option can be set to true or to a
     * specific size. If set to true, the size of the initial segment will be equal to the
     * size of the regular log segment defined by the segment option. The value of this
     * option will be ignored if segmentation is turned off (that is, if the segment
     * option is set to false).
     * <p/>
     * The log option name (case insensitive): "<code>KeepStartupFile</code>",
     * "<code>keep-startup-file</code>", or "<code>keep_startup_file</code>".
     * <p/><table border="0">
     * <tr><td>Default Value:</td><td>"false"</td></tr>
     * <tr><td rowspan="4" valign="top">Valid Values:</td>
     * <td>"false"</td><td>- No startup segment of the log is kept.</td></tr>
     * <tr><td>"true"</td><td>- A startup segment of the log is kept. The size of the segment
     *   equals the value of the segment option.</td></tr>
     * <tr><td>"<i>&lt;number&gt;</i>&nbsp;KB"</td><td>- Sets the maximum size, in kilobytes,
     *   for a startup segment of the log.</td></tr>
     * <tr><td>"<i>&lt;number&gt;</i>&nbsp;MB"</td><td>- Sets the maximum size, in megabytes,
     *   for a startup segment of the log.</td></tr>
     * <tr><td>Changes Take Effect:</td><td colspan="2">After restart</td></tr>
     * </table>
     * <p/>
     * <i><b>Note*:</b> Platform SDK AppTemplate AB does not support this option.</i>
     */
    public static final OptionDescriptor<String> KEEP_STARTUP_FILE_OPT =
            new OptionDescriptor<String>(
                    new String[] { "KeepStartupFile", "keep-startup-file", "keep_startup_file" },
                    null);

    /**
     * Specifies the name of the file to which the application regularly prints a
     * snapshot of the memory output, if it is configured to do this (see "Log Output
     * Options" on page 21). The new snapshot overwrites the previously written
     * data. If the application terminates abnormally, this file will contain the latest
     * log messages. Memory output is not recommended for processors with a CPU
     * frequency lower than 600 MHz.
     * <p/>
     * The log option name (case insensitive): "<code>Memory</code>".
     * <p/><table border="0">
     * <tr><td>Default Value:</td><td>No default value</td></tr>
     * <tr><td>Valid Values:</td><td><code><i>&lt;string&gt;</i></code> (memory file name)</td></tr>
     * <tr><td>Changes Take Effect:</td><td>Immediately</td></tr>
     * </table>
     * <p/>
     * <i><b>Note:</b> If the file specified as the memory file is located on a network drive,
     * the application does not create a snapshot file (with the extension
     * "<code>*.memory.log</code>"). Logging output to a file at a network location
     * is not recommended and could cause performance degradation.</i>
     * <p/>
     * <i><b>Note*:</b> Platform SDK AppTemplate AB does not support this option.</i>
     */
    public static final OptionDescriptor<String> MEMORY_OPT =
            new OptionDescriptor<String>(
                    new String[] { "Memory" },
                    null);

    /**
     * Specifies the buffer size for log output to the memory, if configured.
     * <p/>
     * The log option name (case insensitive): "<code>MemoryStorageSize</code>",
     * "<code>memory-storage-size</code>", or "<code>memory_storage_size</code>".
     * <p/><table border="0">
     * <tr><td>Default Value:</td><td><code>2 MB</code></td></tr>
     * <tr><td rowspan="2" valign="top">Valid Values:</td>
     * <td>"<code><i>&lt;number&gt;</i>[ KB]</code>"</td>
     * <td>- The size of the memory output, in kilobytes. The minimum value is 128 KB.</td></tr>
     * <tr><td>"<code><i>&lt;number&gt;</i> MB</code>"</td>
     * <td>- The size of the memory output, in megabytes. The maximum value is 64 MB.</td></tr>
     * <tr><td>Changes Take Effect:</td><td colspan="2">When memory output is created</td></tr>
     * </table>
     * <p/>
     * <i><b>Note*:</b> Platform SDK AppTemplate AB does not support this option.</i>
     */
    public static final OptionDescriptor<String> MEMORY_STORAGE_SIZE_OPT =
            new OptionDescriptor<String>(
                    new String[] {
                            "MemoryStorageSize", "memory-storage-size", "memory_storage_size" },
                    "2 MB");

    /**
     * Specifies if memory-mapped files, including memory log output (with file
     * extension ".memory.log") and snapshot files (with file extension ".snapshot.log")
     * are disabled for file outputs.
     * <p/>
     * The log option name (case insensitive): "<code>NoMemoryMapping</code>",
     * "<code>no-memory-mapping</code>", or "<code>no_memory_mapping</code>".
     * <p/><table border="0">
     * <tr><td>Default Value:</td><td><code>false<code></td></tr>
     * <tr><td>Valid Values:</td><td><code>true</code>, <code>false</code></td></tr>
     * <tr><td>Changes Take Effect:</td><td>At restart</td></tr>
     * </table>
     * <p/>
     * <i><b>Note*:</b> Platform SDK AppTemplate AB does not support this option.</i>
     */
    public static final OptionDescriptor<Boolean> NO_MEMORY_MAPPING_OPT =
            new OptionDescriptor<Boolean>(
                    new String[] {
                            "NoMemoryMapping", "no-memory-mapping", "no_memory_mapping" },
                    Boolean.FALSE);

    /**
     * Specifies whether the application attaches extended attributes, if any exist, to a
     * log event that it sends to log output. Typically, log events of the Interaction log
     * level and Audit-related log events contain extended attributes. Setting this
     * option to <code>true</code> enables audit capabilities, but negatively affects performance.<br/>
     * Genesys recommends enabling this option for Solution Control Server and
     * Configuration Server when using audit tracking. For other applications, refer
     * to Genesys Combined Log Events Help to find out whether an application
     * generates Interaction-level and Audit-related log events; if it does, enable the
     * option only when testing new interaction scenarios.
     * <p/>
     * The log option name (case insensitive): "<code>PrintAttributes</code>",
     * "<code>print-attributes</code>", or "<code>print_attributes</code>".
     * <p/><table border="0">
     * <tr><td>Default Value:</td><td><code>false</code></td></tr>
     * <tr><td rowspan="2" valign="top">Valid Values:</td>
     *   <td><code>true</code></td><td>- Attaches extended attributes, if any exist,
     *     to a log event sent to log output.</td></tr>
     * <tr><td><code>false</code></td><td>- Does not attach extended attributes
     *   to a log event sent to log output.</td></tr>
     * <tr><td>Changes Take Effect:</td><td colspan="2">Immediately</td></tr>
     * </table>
     */
    public static final OptionDescriptor<Boolean> PRINT_ATTRIBUTES_OPT =
            new OptionDescriptor<Boolean>(
                    new String[] { "PrintAttributes", "print-attributes", "print_attributes" },
                    Boolean.FALSE);

    /**
     * A snapshot file is created for each log output file to temporarily store logs that
     * have not been flushed to the log file. This option specifies the folder, either a
     * full path or a path relative to the application’s working directory, in which
     * the application creates the memory-mapped snapshot file associated with the log
     * file. If this option is not configured, or a value is not specified (the default),
     * the file is created in the log output folder.
     * <p/>
     * The log option name (case insensitive): "<code>Snapshot</code>".
     * <p/><table border="0">
     * <tr><td>Default Value:</td><td><i>No value</i></td></tr>
     * <tr><td rowspan="2" valign="top">Valid Values:</td>
     * <td>No value or not specified (default)</td>
     * <td>- Snapshot is created in log output folder.</td></tr>
     * <tr><td>"<code><i>&lt;path&gt;/&lt;folder&gt;</i></code>"</td>
     * <td>- Full or relative path and folder in which snapshot is created.</td></tr>
     * <tr><td>Changes Take Effect:</td><td>Immediately</td></tr>
     * </table>
     * <p/>
     * <i><b>Note:</b> Do not write the snapshot file to a network drive, because
     * disconnection of the network drive might cause application failure. If
     * the application detects that the output folder is a network drive, the
     * snapshot file will be disabled. However, this detection may not be
     * possible for Storage Area Network (SAN) devices because of
     * operating system limitations.</i> 
     * <p/>
     * <i><b>Note*:</b> Platform SDK AppTemplate AB does not support this option.</i>
     */
    public static final OptionDescriptor<String> SNAPSHOT_OPT =
            new OptionDescriptor<String>(
                    new String[] { "Snapshot" },
                    null);

    /**
     * Specifies the folder, including full path to it, in which an application creates
     * temporary files related to network log output. If you change the option value
     * while the application is running, the change does not affect the currently open
     * network output.
     * <p/>
     * The log option name (case insensitive): "<code>Spool</code>".
     * <p/><table border="0">
     * <tr><td>Default Value:</td><td>The application’s working directory</td></tr>
     * <tr><td>Valid Values:</td><td>Any valid folder, with the full path to it</td></tr>
     * <tr><td>Changes Take Effect:</td><td>Immediately</td></tr>
     * </table>
     * <p/>
     * <i><b>Note*:</b> Platform SDK AppTemplate AB does not support this option.</i>
     */
    public static final OptionDescriptor<String> SPOOL_OPT =
            new OptionDescriptor<String>(
                    new String[] { "Spool" },
                    null);

    /**
     * Specifies, in seconds, how long to keep the throttled verbose level. When this
     * period of time has expired, the original log verbose level will be restored when
     * the log queue size has decreased to less than 50% of the threshold.
     * <p/>
     * The log option name (case insensitive): "<code>ThrottlePeriod</code>",
     * "<code>throttle-period</code>", or "<code>throttle_period</code>".
     * <p/><table border="0">
     * <tr><td>Default Value:</td><td><code>30</code></td></tr>
     * <tr><td>Valid Values:</td><td><code>0–3600</code></td></tr>
     * <tr><td>Changes Take Effect:</td><td>Immediately</td></tr>
     * </table>
     * <p/>
     * <i><b>Note:</b> This option applies only if enable-thread is set to true.</i>
     * <p/>
     * <i><b>Note*:</b> Platform SDK AppTemplate AB does not support this option.</i>
     */
    public static final OptionDescriptor<Integer> THROTTLE_PERIOD_OPT =
            new OptionDescriptor<Integer>(
                    new String[] { "ThrottlePeriod", "throttle-period", "throttle_period" },
                    30);

    /**
     * Specifies the size of the internal log queue at which the verbose level is to be
     * reduced so as to lessen the load generated by logging. If this option is set to 0
     * (zero), throttling does not occur. For more information about log throttling,
     * refer to the Framework 8.5 Management Layer User’s Guide.
     * <p/>
     * The log option name (case insensitive): "<code>ThrottleThreshold</code>",
     * "<code>throttle-threshold</code>", or "<code>throttle_threshold</code>".
     * <p/><table border="0">
     * <tr><td>Default Value:</td><td><code>5000</code></td></tr>
     * <tr><td>Valid Values:</td><td><code>0–10000</code></td></tr>
     * <tr><td>Changes Take Effect:</td><td>Immediately</td></tr>
     * </table>
     * <p/>
     * <i><b>Note:</b> This option applies only if enable-thread is set to true.</i>
     * <p/>
     * <i><b>Note*:</b> Platform SDK AppTemplate AB does not support this option.</i>
     */
    public static final OptionDescriptor<Integer> THROTTLE_THRESHOLD_OPT =
            new OptionDescriptor<Integer>(
                    new String[] { "ThrottleThreshold", "throttle-threshold", "throttle_threshold" },
                    5000);

    /**
     * Platform SDK AppTemplate AB specific option.
     * <p/>
     * The log option name (case insensitive): "<code>Log4j2ConfigProfile</code>",
     * "<code>log4j2-config-profile</code>", or "<code>log4j2_config_profile</code>".
     * <p/>
     * <i><b>Note:</b> This option is experimental and its value procession may get changed.</i>
     */
    public static final OptionDescriptor<String> LOG4j2_CONFIG_PROFILE_OPT =
            new OptionDescriptor<String>(
                    new String[] {
                            "Log4j2ConfigProfile", "log4j2-config-profile", "log4j2_config_profile" },
                    null);

    /**
     * Default root directory for the log files.
     * If specified, it is applied to file names defined in options like {@link #STANDARD_OPT},
     * {@link #INTERACTION_OPT}, {@link #DEBUG_OPT}, etc.<br/>
     * It's used if log file name is not absolute path, and is not started from "<code>./</code>", or "<code>../</code>".<br/>
     * <i><b>Note</b>: This option value may be overridden with jvm system property "<code>default-logdir</code>".</i>
     * <p/>
     * Platform SDK AppTemplate AB specific option.
     * <p/>
     * The log option name (case insensitive): "<code>DefaultLogdir</code>",
     * "<code>default-logdir</code>", or "<code>default_logdir</code>".
     *
     * @since 9.0.005.00
     */
    public static final OptionDescriptor<String> DEFAULT_LOGDIR_OPT =
            new OptionDescriptor<String>(
                    new String[] {
                            "DefaultLogdir", "default-logdir", "default_logdir" },
                    null);

    /**
     * Platform SDK AppTemplate AB specific property to set log messages filter on Message Server Appender
     * for Platform SDK internal events.<br/>
     * This value should not be lower than INFO level to do not cause unlimited recursion/avalanche.
     * <p/>
     * The log option name (case insensitive): "<code>msgsrv_intMsgsLevel</code>".<br/>
     * <p/><table border="0">
     * <tr><td>Default Value:</td><td>"<code>info</code>"</td></tr>
     * <tr><td>Valid Values:</td><td>"<code>info</code>", "<code>warn</code>", "<code>error</code>", "<code>fatal</code>",
     *     "<code>off</code>"</td></tr>
     * <tr><td>Changes Take Effect:</td><td>Immediately</td></tr>
     * </table>
     *
     * @since 9.0.005.02
     */
    public static final OptionDescriptor<String> X_MSGSRV_INTMSGS_LEVEL =
            new OptionDescriptor<String>(
                    new String[] {
                            "msgsrv-intMsgsLevel", "msgsrv_intMsgsLevel" },
                    "info");

    /**
     * Generates Debug log records about open connection, socket select, timer
     * creation and deletion, write, security-related, and DNS operations, and
     * connection library function calls. This option is the same as enabling or
     * disabling all of the previous x-conn-debug-<op type> options.
     * <p/>
     * The log option name (case insensitive): "<code>x-conn-debug-all</code>",
     * or "<code>x_conn_debug_all</code>".
     * <p/><table border="0">
     * <tr><td>Default Value:</td><td>0</td></tr>
     * <tr><td rowspan="2" align="left" valign="top">Valid Values:</td>
     *     <td>0</td><td>- Log records are not generated</td></tr>
     * <tr><td>1</td><td>- Log records are generated</td></tr>
     * <tr><td>Changes Take Effect:</td><td colspan="2">After restart</td></tr>
     * </table>
     * <p/>
     * <i><b>Warning!</b> Use this option only when requested by Genesys Customer Care</i>
     * <p/>
     * <i><b>Note*:</b> Platform SDK AppTemplate AB does not support this option.</i>
     */
    public static final OptionDescriptor<Boolean> X_CONN_DEBUG_ALL_OPT =
            new OptionDescriptor<Boolean>(
                    new String[] { "x-conn-debug-all", "x_conn_debug_all" },
                    Boolean.FALSE);

    /**
     * Generates Debug log records about connection library function calls.
     * <p/>
     * The log option name (case insensitive): "<code>x-conn-debug-api</code>",
     * or "<code>x_conn_debug_api</code>".
     * <p/><table border="0">
     * <tr><td>Default Value:</td><td>0</td></tr>
     * <tr><td rowspan="2" align="left" valign="top">Valid Values:</td>
     *     <td>0</td><td>- Log records are not generated</td></tr>
     * <tr><td>1</td><td>- Log records are generated</td></tr>
     * <tr><td>Changes Take Effect:</td><td colspan="2">After restart</td></tr>
     * </table>
     * <p/>
     * <i><b>Warning!</b> Use this option only when requested by Genesys Customer Care</i>
     * <p/>
     * <i><b>Note*:</b> Platform SDK AppTemplate AB does not support this option.</i>
     */
    public static final OptionDescriptor<Boolean> X_CONN_DEBUG_API_OPT =
            new OptionDescriptor<Boolean>(
                    new String[] { "x-conn-debug-api", "x_conn_debug_api" },
                    Boolean.FALSE);

    /**
     * Generates Debug log records about DNS operations.
     * <p/>
     * The log option name (case insensitive): "<code>x-conn-debug-dns</code>",
     * or "<code>x_conn_debug_dns</code>".
     * <p/><table border="0">
     * <tr><td>Default Value:</td><td>0</td></tr>
     * <tr><td rowspan="2" align="left" valign="top">Valid Values:</td>
     *     <td>0</td><td>- Log records are not generated</td></tr>
     * <tr><td>1</td><td>- Log records are generated</td></tr>
     * <tr><td>Changes Take Effect:</td><td colspan="2">After restart</td></tr>
     * </table>
     * <p/>
     * <i><b>Warning!</b> Use this option only when requested by Genesys Customer Care</i>
     * <p/>
     * <i><b>Note*:</b> Platform SDK AppTemplate AB does not support this option.</i>
     */
    public static final OptionDescriptor<Boolean> X_CONN_DEBUG_DNS_OPT =
            new OptionDescriptor<Boolean>(
                    new String[] { "x-conn-debug-dns", "x_conn_debug_dns" },
                    Boolean.FALSE);

    /**
     * Generates Debug log records about "open connection" operations of the application.
     * <p/>
     * The log option name (case insensitive): "<code>x-conn-debug-open</code>",
     * or "<code>x_conn_debug_open</code>".
     * <p/><table border="0">
     * <tr><td>Default Value:</td><td>0</td></tr>
     * <tr><td rowspan="2" align="left" valign="top">Valid Values:</td>
     *     <td>0</td><td>- Log records are not generated</td></tr>
     * <tr><td>1</td><td>- Log records are generated</td></tr>
     * <tr><td>Changes Take Effect:</td><td colspan="2">After restart</td></tr>
     * </table>
     * <p/>
     * <i><b>Warning!</b> Use this option only when requested by Genesys Customer Care</i>
     * <p/>
     * <i><b>Note*:</b> Platform SDK AppTemplate AB does not support this option.</i>
     */
    public static final OptionDescriptor<Boolean> X_CONN_DEBUG_OPEN_OPT =
            new OptionDescriptor<Boolean>(
                    new String[] { "x-conn-debug-open", "x_conn_debug_open" },
                    Boolean.FALSE);

    /**
     * Generates Debug log records about security-related operations, such as
     * Transport Layer Security and security certificates.
     * <p/>
     * The log option name (case insensitive): "<code>x-conn-debug-security</code>",
     * or "<code>x_conn_debug_security</code>".
     * <p/><table border="0">
     * <tr><td>Default Value:</td><td>0</td></tr>
     * <tr><td rowspan="2" align="left" valign="top">Valid Values:</td>
     *     <td>0</td><td>- Log records are not generated</td></tr>
     * <tr><td>1</td><td>- Log records are generated</td></tr>
     * <tr><td>Changes Take Effect:</td><td colspan="2">After restart</td></tr>
     * </table>
     * <p/>
     * <i><b>Warning!</b> Use this option only when requested by Genesys Customer Care</i>
     * <p/>
     * <i><b>Note*:</b> Platform SDK AppTemplate AB does not support this option.</i>
     */
    public static final OptionDescriptor<Boolean> X_CONN_DEBUG_SECURITY_OPT =
            new OptionDescriptor<Boolean>(
                    new String[] { "x-conn-debug-security", "x_conn_debug_security" },
                    Boolean.FALSE);

    /**
     * Generates Debug log records about "socket select" operations of the application.
     * <p/>
     * The log option name (case insensitive): "<code>x-conn-debug-select</code>",
     * or "<code>x_conn_debug_select</code>".
     * <p/><table border="0">
     * <tr><td>Default Value:</td><td>0</td></tr>
     * <tr><td rowspan="2" align="left" valign="top">Valid Values:</td>
     *     <td>0</td><td>- Log records are not generated</td></tr>
     * <tr><td>1</td><td>- Log records are generated</td></tr>
     * <tr><td>Changes Take Effect:</td><td colspan="2">After restart</td></tr>
     * </table>
     * <p/>
     * <i><b>Warning!</b> Use this option only when requested by Genesys Customer Care</i>
     * <p/>
     * <i><b>Note*:</b> Platform SDK AppTemplate AB does not support this option.</i>
     */
    public static final OptionDescriptor<Boolean> X_CONN_DEBUG_SELECT_OPT =
            new OptionDescriptor<Boolean>(
                    new String[] { "x-conn-debug-select", "x_conn_debug_select" },
                    Boolean.FALSE);

    /**
     * Generates Debug log records about the timer creation and deletion operations
     * of the application.
     * <p/>
     * The log option name (case insensitive): "<code>x-conn-debug-timers</code>",
     * or "<code>x_conn_debug_timers</code>".
     * <p/><table border="0">
     * <tr><td>Default Value:</td><td>0</td></tr>
     * <tr><td rowspan="2" align="left" valign="top">Valid Values:</td>
     *     <td>0</td><td>- Log records are not generated</td></tr>
     * <tr><td>1</td><td>- Log records are generated</td></tr>
     * <tr><td>Changes Take Effect:</td><td colspan="2">After restart</td></tr>
     * </table>
     * <p/>
     * <i><b>Warning!</b> Use this option only when requested by Genesys Customer Care</i>
     * <p/>
     * <i><b>Note*:</b> Platform SDK AppTemplate AB does not support this option.</i>
     */
    public static final OptionDescriptor<Boolean> X_CONN_DEBUG_TIMERS_OPT =
            new OptionDescriptor<Boolean>(
                    new String[] { "x-conn-debug-timers", "x_conn_debug_timers" },
                    Boolean.FALSE);

    /**
     * Generates Debug log records about "write" operations of the application.
     * <p/>
     * The log option name (case insensitive): "<code>x-conn-debug-write</code>",
     * or "<code>x_conn_debug_write</code>".
     * <p/><table border="0">
     * <tr><td>Default Value:</td><td>0</td></tr>
     * <tr><td rowspan="2" align="left" valign="top">Valid Values:</td>
     *     <td>0</td><td>- Log records are not generated</td></tr>
     * <tr><td>1</td><td>- Log records are generated</td></tr>
     * <tr><td>Changes Take Effect:</td><td colspan="2">After restart</td></tr>
     * </table>
     * <p/>
     * <i><b>Warning!</b> Use this option only when requested by Genesys Customer Care</i>
     * <p/>
     * <i><b>Note*:</b> Platform SDK AppTemplate AB does not support this option.</i>
     */
    public static final OptionDescriptor<Boolean> X_CONN_DEBUG_WRITE_OPT =
            new OptionDescriptor<Boolean>(
                    new String[] { "x-conn-debug-write", "x_conn_debug_write" },
                    Boolean.FALSE);

    public static final int DEAULT_MS_BUFFER_SIZE = 2048;
     /**
     * Size of queue for messages which should be kept in when the connection to Message Server is not available.
     * <p/>
     * The log option name (case insensitive): "<code>queue-size</code>"
     * <p/><table border="0">
     * <tr><td>Default Value:</td><td>2048</td></tr>
     * <tr><td>Changes Take Effect:</td><td colspan="2">After restart</td></tr>
     * </table>
     * <p/>
     * <i><b>Warning!</b> Use this option only when requested by Genesys Customer Care</i>
     * <p/>
     */
    public static final OptionDescriptor<Integer> MS_BUFFER_SIZE =
            new OptionDescriptor<Integer>(
                    new String[] { "ms-buffer-size", "ms_buffer_size" },
                    DEAULT_MS_BUFFER_SIZE);
}

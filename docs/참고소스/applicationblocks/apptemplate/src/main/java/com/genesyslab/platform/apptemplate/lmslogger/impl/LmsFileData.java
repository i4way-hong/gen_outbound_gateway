// ===============================================================================
// Genesys Platform SDK
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
package com.genesyslab.platform.apptemplate.lmslogger.impl;

import com.genesyslab.platform.apptemplate.lmslogger.LmsLogLevel;
import com.genesyslab.platform.apptemplate.lmslogger.LmsFileHeader;
import com.genesyslab.platform.apptemplate.lmslogger.LmsLoadException;
import com.genesyslab.platform.apptemplate.lmslogger.LmsMessageTemplate;

import com.genesyslab.platform.commons.util.CompareUtils;

import java.util.HashMap;
import java.util.Collection;
import java.util.Enumeration;

import java.net.URL;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;


/**
 * LMS file parser.<br/>
 * It is designed to be internally used in {@link LmsMessageConveyor} and {@link LmsEnumGenerator}.
 *
 * @see com.genesyslab.platform.apptemplate.lmslogger.LmsMessageConveyor LmsMessageConveyor
 */
@SuppressWarnings("all")
public final class LmsFileData {

    private File lmsFile;

    private LmsFileHeader header;

    private final HashMap<Integer, LmsMessageTemplate> templates =
            new HashMap<Integer, LmsMessageTemplate>();


    /**
     * Reads and parses content of given LMS file.
     *
     * @param filename name of LMS file or resource.
     * @throws LmsLoadException if no such file available or there is something wrong with its content
     *             (see exception message for details).
     */
    public LmsFileData(final String filename) throws LmsLoadException {
        lmsFile = new File(filename);
        if (lmsFile.exists()) {
            loadFrom(lmsFile);

        } else {
            try {
                final Enumeration<URL> resources = ClassLoader.getSystemResources(filename);
                if (resources != null && resources.hasMoreElements()) {
                    final URL url = resources.nextElement();
                    lmsFile = new File(url.toExternalForm());
                    //if (resources.hasMoreElements()) {
                    //    // WARN: Multiple resources with name 'filename' found. Using the first one - 'url'.
                    //}
                    InputStreamReader isr = null;
                    try {
                        isr = new InputStreamReader(url.openStream());
                        BufferedReader sr = null;
                        try {
                            sr = new BufferedReader(isr);
                            loadFrom(sr);
                        } finally {
                            if (sr != null) {
                                sr.close();
                            }
                        }
                    } finally {
                        if (isr != null) {
                            isr.close();
                        }
                    }
                }
            } catch (final IOException ioe) {
                throw new LmsLoadException(
                        "Failed to load LMS resource '" + filename + "'", ioe);
            }
        }
    }


    /**
     * Returns the LMS file source.
     */
    public File getLmsFile() {
        return lmsFile;
    }

    /**
     * Returns the LMS file header.
     */
    public LmsFileHeader getHeader() {
        return header;
    }

    /**
     * Returns collection of loaded {@link LmsMessageTemplate LmsMessageTemplate}'s.
     */
    public Collection<LmsMessageTemplate> getTemplates() {
        return templates.values();
    }

    /**
     * Parses the *.lms file into the Header and Message templates.
     *
     * @param file the file to load.
     */
    protected void loadFrom(final File file) throws LmsLoadException {
        if (!file.exists()) {
            throw new LmsLoadException(
                    "The specified file \"" + file.getName() + "\" not found");
        }
        try {
            FileReader in = null;
            try {
                in = new FileReader(file);
                BufferedReader sr = null;
                try {
                    sr = new BufferedReader(in);
                    loadFrom(sr);
                } finally {
                    if (sr != null) {
                        sr.close();
                    }
                }
            } finally {
                if (in != null) {
                    in.close();
                }
            }
        } catch (final LmsLoadException ex) {
            throw new LmsLoadException(
                    "The file \"" + file.getName() + "\" has incorrect format", ex);
        } catch (final Exception ex) {
            throw new LmsLoadException(
                    "Error reading file \"" + file.getName() + "\"", ex);
        }
    }

    /**
     * Loads the *.lms file header and log templates from the "reader".
     *
     * @param reader the source to be read.
     * @throws LoggerException if reader does not contain the valid
     *             lms file header (should be first string).
     */
    protected void loadFrom(final BufferedReader reader)
            throws LmsLoadException {
        String header;
        try {
            while (true) { // skip possible leading empty strings
                header = reader.readLine();
                if (header == null) {
                    break;
                }
                header = header.trim();
                if (header.length() > 0) {
                    break;
                }
            }
        } catch (final Exception ex) {
            throw new LmsLoadException("Error reading from textReader", ex);
        }

        if (header == null) {
            throw new LmsLoadException(
                    "Error: Valid header expected to be first non-empty string");
        }

        this.header = parseHeader(header);

        try {
            doLoadTemplates(reader);
        } catch (final LmsLoadException ex) {
            throw ex;
        } catch (final Exception ex) {
            throw new LmsLoadException("Error reading from textReader", ex);
        }
    }

    protected void doLoadTemplates(final BufferedReader reader)
            throws LmsLoadException, IOException {
        String str;
        while ((str = reader.readLine()) != null) {
            try {
                final LmsMessageTemplate template = parseMessageLine(str);
                if (template != null) {
                    if (!templates.containsKey(template.getId())) {
                        templates.put(template.getId(), template);
                    }
                }
            } catch (final LmsLoadException ex) {
                //getLogger().errorFormat(
                //        "String skipped due to error while parsing string of lms file {0}",
                //        ex.getMessage());
            }
        }
    }

    /**
     * Gets the {@link LmsMessageTemplate LmsMessageTemplate} with the specified ID.
     * 
     * @param msgid the ID of {@link LmsMessageTemplate LmsMessageTemplate}.
     * @return Found {@link LmsMessageTemplate LmsMessageTemplate}.
     * @throws IllegalArgumentException
     *             If the {@link LmsMessageTemplate LmsMessageTemplate} with
     *             specified ID not found.
     */
    public LmsMessageTemplate getTemplate(final int msgid) {
        LmsMessageTemplate result = templates.get(Integer.valueOf(msgid));
        if (result == null) {
            throw new IllegalArgumentException(
                    "Template with specified key [" + msgid + "] not found");
        }
        return result;
    }

    /**
     * Parses Message template from string.
     *
     * @param line the string value from LMS file.
     * @return The parsed template. Value can be null if the string is empty
     *         or starts with ';' (i.e. the comment string).
     */
    private static LmsMessageTemplate parseMessageLine(
            final String line)
                    throws LmsLoadException {
        if (line == null || line.length() == 0) {
            return null;
        }
        String trimmedLine = line.trim();
        if (trimmedLine == null || trimmedLine.length() == 0) {
            return null;
        }

        if (trimmedLine.indexOf('|') == -1 || trimmedLine.startsWith(";")) {
            return null;
        }

        String[] strParts = line.split("\\|"); // split does not work with "|"
        // char, must use"\\|"

        if (strParts.length < 4) {
            throw new LmsLoadException(
                    "String [" + line + "] in lms file has incorrect format: less than 4 groups found");
        }

        Integer id;
        try {
            id = Integer.parseInt(strParts[0]);
        } catch (final Exception ex) {
            throw new LmsLoadException(
                    "String [" + line + "] in lms file has incorrect ID format ["
                    + strParts[0] + "]", ex);
        }

        LmsLogLevel level = LmsLogLevel.valueOf(strParts[1]);
        if (level == null) {
            throw new LmsLoadException(
                    "String [" + line + "] in lms file has incorrect LEVEL value ["
                    + strParts[1] + "]");
        }

        String name = strParts[2];
        if (name == null || name.isEmpty()) {
            throw new LmsLoadException(
                    "String [" + line + "] in lms file has no NAME declared");
        }

        String message = strParts[3];
        if (message == null || message.isEmpty()) {
            throw new LmsLoadException(
                    "String [" + line + "] in lms file has no MESSAGE value declared");
        }
        // PSDK-8334 AppTemplate LmsEventLogger message formatting pattern correction:
        message = message.replace("%ld", "%d").replace("%lu", "%d");

        return new LmsMessageTemplImpl(id, name, level, message);
    }

    /**
     * Parses the header from the string value.
     * 
     * @param header the header value.
     */
    private static LmsFileHeader parseHeader(final String header)
            throws LmsLoadException {
        final String errormsg = "Incorrect header format: " + header; // just in case :)

        final String[] strParts = header.split("\\|", -1);
        if (strParts.length < 5) {
            throw new LmsLoadException(errormsg);
        }

        for (int i = 0; i < 5; ++i) {
            if (strParts[i] == null) {
                throw new LmsLoadException(errormsg);
            }
            if (strParts[i].trim().length() == 0) {
                throw new LmsLoadException(errormsg);
            }
        }

        return new LmsFileHeader(
                strParts[0].trim(),
                strParts[1].trim(),
                strParts[2].trim(),
                strParts[3].trim(),
                strParts[4].trim());
    }


    @Override
    public String toString() {
        return "LmsFileData(" + lmsFile + " as [" + header + "])";
    }


    /**
     * Internal implementation of <code>LmsMessageTemplate</code> for representation of "localized"
     * LMS messages.<br/>
     * Such message template instances will be created by actual LMS file content in a runtime,
     * and registered in {@link LmsMessageConveyor}.
     */
    public static class LmsMessageTemplImpl implements LmsMessageTemplate {

        private final Integer id;
        private final String name;
        private final LmsLogLevel level;
        private final String message;

        public LmsMessageTemplImpl(
                final Integer id,
                final String name,
                final LmsLogLevel level,
                final String message) {
            this.id = id;
            this.name = name;
            this.level = level;
            this.message = message;
        }

        @Override
        public Integer getId() {
            return id;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public LmsLogLevel getLevel() {
            return level;
        }

        @Override
        public String getMessage() {
            return message;
        }

        /**
         * Returns String format of the LMS Event in LMS file record format.
         *
         * @return String representation of the event.
         */
        @Override
        public String toString() {
            return String.valueOf(id) + "|" + level + "|" + name + "|" + message;
        }

        @Override
        public int hashCode() {
            int hash = getClass().hashCode();
            if (id != null) {
                hash = hash * 31 + id.hashCode();
            }
            if (name != null) {
                hash = hash * 31 + name.hashCode();
            }
            if (level != null) {
                hash = hash * 31 + level.hashCode();
            }
            if (message != null) {
                hash = hash * 31 + message.hashCode();
            }
            return hash;
        }

        @Override
        public boolean equals(final Object obj) {
            if (!(obj instanceof LmsMessageTemplate)) {
                return false;
            }
            if (obj == this) {
                return true;
            }
            final LmsMessageTemplate other = (LmsMessageTemplate) obj;
            return CompareUtils.equals(id, other.getId())
                && CompareUtils.equals(level, other.getLevel())
                && CompareUtils.equals(name, other.getName())
                && CompareUtils.equals(message, other.getMessage());
        }
    }
}

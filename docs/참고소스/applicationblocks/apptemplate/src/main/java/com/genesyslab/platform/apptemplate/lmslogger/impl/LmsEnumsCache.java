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
package com.genesyslab.platform.apptemplate.lmslogger.impl;

import java.net.URL;

import java.util.Set;
import java.util.HashSet;
import java.util.Enumeration;

import java.io.IOException;
import java.io.OutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;


/**
 * Base class for keeping pre-processed annotations information on generated LMS enumerations.
 * <p/>
 * <i><b>Note:</b> This class is a part of PSDK internal functionality for LMS declarations support.<br/>
 * It is not supposed for direct usage from applications.</i>
 */
public class LmsEnumsCache {

    private final HashSet<String> enumsSet = new HashSet<String>();

    public void add(final String entry) {
        enumsSet.add(entry);
    }

    public Set<String> getNames() {
        return enumsSet;
    }

    public void writeCache(final OutputStream os) throws IOException {
        final DataOutputStream out = new DataOutputStream(new BufferedOutputStream(os));
        try {
            out.writeInt(enumsSet.size());
            for (final String enumname: enumsSet) {
                out.writeUTF(enumname);
            }
        } finally {
            out.close();
        }
    }

    public void loadCacheFiles(final Enumeration<URL> resources) throws IOException {
        enumsSet.clear();
        while (resources.hasMoreElements()) {
            final URL url = resources.nextElement();
            DataInputStream in = null;
            try {
                in = new DataInputStream(new BufferedInputStream(url.openStream()));
                final int count = in.readInt();
                for (int i = 0; i < count; i++) {
                    add(in.readUTF());
                }
            } finally {
                if (in != null) {
                    in.close();
                }
            }
        }
    }
}

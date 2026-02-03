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

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.TimeZone;

import com.genesyslab.platform.apptemplate.lmslogger.impl.LmsFileData;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

import java.io.File;
import java.io.PrintStream;
import java.io.FileNotFoundException;


/**
 * Platform SDK AppTemplate AB Code generation Tool for LMS enumerations.
 * <p/>It takes given '.lms' file and generates Java source file with AppTemplate specific LMS enumeration.
 *
 * <p/>Usage:<br/><code>
 * &nbsp;&nbsp;%> java -cp apptemplate.jar com.genesyslab.platform.apptemplate.lmslogger.LmsEnumGenerator
 * &lt;LMS-filename&gt; &lt;target-enum-name&gt; &lt;target-enum-package&gt; [&lt;out-src-dir&gt;]
 * </code>
 *
 * <p/>Default value for <code>&lt;out-src-dir&gt;</code> is <code>"src/main/java"</code>.
 *
 * <p/>For example,<br/><code>
 * &nbsp;&nbsp;%> java ...LmsEnumGenerator MyApp.lms MyAppEnum com.genesyslab.myapp src/main/java
 * </code><br/>
 * takes 'MyApp.lms' and generates correspondent LMS enumeration into
 * <code>'src/main/java/com/genesyslab/myapp/MyAppEnum.java'</code>.
 */
@SuppressWarnings("all")
public class LmsEnumGenerator {

    protected static final String PATH_SEPARATOR = "/";

    private static final String DEFAULT_OUT_PATH = "src/generated";


    /**
     * The Generator utility entry point.
     *
     * @param args command line arguments.
     * @throws LmsLoadException in cases of LMS file load exception.
     * @throws IllegalArgumentException if output directory does not exist.
     * @throws FileNotFoundException if failed to create the output file.
     */
    public static void main(final String[] args)
            throws LmsLoadException, FileNotFoundException,
                    IllegalArgumentException {

        if (args.length < 3) {
            System.out.println("Platform SDK AppTemplate AB Code generation Tool for LMS enumerations.");
            System.out.print("It takes given '.lms' file and generates Java source file with AppTemplate ");
            System.out.println("specific LMS enumeration.");
            System.out.println();
            System.out.println("Usage:\n    %> java -cp apptemplate.jar com.genesyslab.platform.apptemplate.lmslogger."
                    + "LmsEnumGenerator <LMS-filename> <target-enum-name> <target-enum-package> [<out-src-dir>]");
            System.out.println();
            System.out.println("Default value for <out-src-dir> is \"" + DEFAULT_OUT_PATH + "\"");
            System.out.println();
            System.out.println("For example,");
            System.out.println("    %> java com.genesyslab.platform.apptemplate.lmslogger.LmsEnumGenerator "
                    + "MyApp.lms MyAppEnum com.genesyslab.myapp src/main/java");
            System.out.println("will take 'MyApp.lms' and generate correspondent LMS enum in "
                    + "'src/main/java/com/genesyslab/myapp/MyAppEnum.java'");

            System.exit(1);
        }

        String lsmFilename = args[0];

        String enumName = args[1];
        String packageName = args[2];

        String outputDir = null;
        if (args.length > 3) {
            outputDir = args[3];
        }
        if (outputDir == null || outputDir.isEmpty()) {
            outputDir = DEFAULT_OUT_PATH;
        }

        String packname = packageName.replaceAll("\\.", PATH_SEPARATOR);
        String fileName = outputDir + PATH_SEPARATOR + packname + PATH_SEPARATOR + enumName + ".java";

        LmsFileData lms = new LmsFileData(lsmFilename);

        File outFile = new File(fileName);
        System.out.println("Generating " + enumName + " for " + packageName
                + "\n\tfrom " + (new File(lsmFilename)).getAbsolutePath()
                + "\n\tto file " + outFile.getAbsolutePath());

        File dir = outFile.getParentFile();
        if (dir != null) {
            dir.mkdirs();
        }

        PrintStream out = new PrintStream(outFile);

        LmsEnumGenerator generator = new LmsEnumGenerator();
        generator.printEnum(lms, out, packageName, enumName);

        out.close();
    }


    /**
     * Generates full java source text of target enumeration including copyright header.
     *
     * @param lms loaded Messages file data.
     * @param out output stream to print full content of resulting java text.
     * @param packageName java package of target enumeration.
     * @param enumName class name of target enumeration.
     */
    public void printEnum(
            final LmsFileData lms,
            final PrintStream out,
            final String packageName,
            final String enumName) {
        printEnumFileHeader(out, packageName, enumName, lms);
        printEnumItems(out, lms, enumName);
        printEnumFileLmsHeader(out, packageName, enumName, lms);
        printEnumFileFooter(out, enumName);
    }


    protected void printEnumFileHeader(
            final PrintStream out,
            final String packageName,
            final String enumName,
            final LmsFileData lms) {
        out.println("// ===============================================================================");
        out.println("//  Genesys Platform SDK Application Blocks");
        out.println("// ===============================================================================");
        out.println("//");
        out.println("//  Any authorized distribution of any copy of this code (including any related");
        out.println("//  documentation) must reproduce the following restrictions, disclaimer and copyright");
        out.println("//  notice:");
        out.println("//");
        out.println("//  The Genesys name, trademarks and/or logo(s) of Genesys shall not be used to name");
        out.println("//  (even as a part of another name), endorse and/or promote products derived from");
        out.println("//  this code without prior written permission from Genesys Telecommunications");
        out.println("//  Laboratories, Inc.");
        out.println("//");
        out.println("//  The use, copy, and/or distribution of this code is subject to the terms of the Genesys");
        out.println("//  Developer License Agreement.  This code shall not be used, copied, and/or");
        out.println("//  distributed under any other license agreement.");
        out.println("//");
        out.println("//  THIS CODE IS PROVIDED BY GENESYS TELECOMMUNICATIONS LABORATORIES, INC.");
        out.println("//  (\"GENESYS\") \"AS IS\" WITHOUT ANY WARRANTY OF ANY KIND. GENESYS HEREBY");
        out.println("//  DISCLAIMS ALL EXPRESS, IMPLIED, OR STATUTORY CONDITIONS, REPRESENTATIONS AND");
        out.println("//  WARRANTIES WITH RESPECT TO THIS CODE (OR ANY PART THEREOF), INCLUDING, BUT");
        out.println("//  NOT LIMITED TO, IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A");
        out.println("//  PARTICULAR PURPOSE OR NON-INFRINGEMENT. GENESYS AND ITS SUPPLIERS SHALL");
        out.println("//  NOT BE LIABLE FOR ANY DAMAGE SUFFERED AS A RESULT OF USING THIS CODE. IN NO");
        out.println("//  EVENT SHALL GENESYS AND ITS SUPPLIERS BE LIABLE FOR ANY DIRECT, INDIRECT,");
        out.println("//  CONSEQUENTIAL, ECONOMIC, INCIDENTAL, OR SPECIAL DAMAGES (INCLUDING, BUT");
        out.println("//  NOT LIMITED TO, ANY LOST REVENUES OR PROFITS).");
        out.println("//");
        out.println("//  Copyright (c) 2006 - 2019 Genesys Telecommunications Laboratories, Inc. All rights reserved.");
        out.println("// ===============================================================================");
        out.println();
        out.println("// ATTENTION! DO NOT MODIFY THIS FILE - IT IS AUTOMATICALLY GENERATED!");
        out.println();

        out.print("package ");
        out.print(packageName);
        out.println(";");

        if (!"com.genesyslab.platform.apptemplate.lmslogger".equals(packageName)) {
            out.println();
            out.println("import com.genesyslab.platform.apptemplate.lmslogger.LmsEnumType;");
            out.println("import com.genesyslab.platform.apptemplate.lmslogger.LmsLogLevel;");
            out.println("import com.genesyslab.platform.apptemplate.lmslogger.LmsFileHeader;");
            out.println("import com.genesyslab.platform.apptemplate.lmslogger.LmsMessageTemplate;");
        }

        out.println();
        out.println("import javax.annotation.Generated;");
        out.println();
        out.println();

        final String lmsFilename = lms.getLmsFile().getName();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); 
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC")); 

        out.println("/**");
        out.print(" * This LMS events enumeration declarations were generated from file <code>\"");
        out.print(lmsFilename);
        out.println("\"</code>.<br/>");

        LmsFileHeader lmsHead = lms.getHeader();
        out.println(" * It has following LMS header properties:");
        out.println(" * <table border\"1\">");
        out.println(" * <tr align=\"left\"><th>Id</th><th>Purpose</th><th>Number</th><th>Name</th><th>Version</th></tr>");
        out.print(" * <tr align=\"left\"><td>\"");
        out.print(lmsHead.getId());
        out.print("\"</td><td>\"");
        out.print(lmsHead.getPurpose());
        out.print("\"</td><td>\"");
        out.print(lmsHead.getNumber());
        out.print("\"</td><td>\"");
        out.print(lmsHead.getName());
        out.print("\"</td><td>\"");
        out.print(lmsHead.getVersion());
        out.println("\"</td></tr>");
        out.println(" * </table>");
        out.println(" */");

        out.print("@LmsEnumType(id=\"");
        out.print(lmsHead.getId());
        out.print("\", purpose=\"");
        out.print(lmsHead.getPurpose());
        out.print("\", number=\"");
        out.print(lmsHead.getNumber());
        out.print("\", name=\"");
        out.print(lmsHead.getName());
        out.print("\", version=\"");
        out.print(lmsHead.getVersion());
        out.println("\")");

        out.print("@Generated(value=\"");
        out.print(getClass().getName());
        out.print("\",\n        date=\"");
        out.printf(dateFormat.format(new Date()));
        out.println("\")");

        out.print("public enum ");
        out.print(enumName);
        out.println(" implements LmsMessageTemplate {");
    }

    protected void printEnumFileLmsHeader(
            final PrintStream out,
            final String packageName,
            final String enumName,
            final LmsFileData lms) {
        LmsFileHeader lmsHead = lms.getHeader();

        out.println();
        out.println();
        out.print("    private static final long serialVersionUID = ");
        out.print(6849794470754667710L ^ (((long) packageName.hashCode()) << 16) ^ ((long) enumName.hashCode()));
        out.println("L;");

        out.println();
        out.println();
        out.println("    /**");
        out.println("     * LMS File header line content from the file, which was used for generation of this enumeration.");
        out.println("     * <p/>For instance, this enumeration header line is:<code><pre>");

        out.print("     * ");
        out.print(lmsHead.getId());
        out.print('|');
        out.print(lmsHead.getPurpose());
        out.print('|');
        out.print(lmsHead.getNumber());
        out.print('|');
        out.print(lmsHead.getName());
        out.print('|');
        out.print(lmsHead.getVersion());
        out.println("|*</pre></code>");

        out.println("     * And it is represented as following <code>LmsFileHeader</code>:<br/><table border = \"1\">");
        out.println("     *  <tr><th>Id</th><th>Purpose</th><th>Number</th><th>Name</th><th>Version</th></tr>");

        out.print("     *  <tr align=\"right\"><td>");
        out.print(lmsHead.getId());
        out.print("</td><td>");
        out.print(lmsHead.getPurpose());
        out.print("</td><td>");
        out.print(lmsHead.getNumber());
        out.print("</td><td>");
        out.print(lmsHead.getName());
        out.print("</td><td>");
        out.print(lmsHead.getVersion());
        out.println("</td></tr>");

        out.println("     * </table>");
        out.println("     */");

        out.println("    public static final LmsFileHeader _LMS_FILE_HEADER =");
        out.print("            new LmsFileHeader(\"");
        out.print(lmsHead.getId());
        out.print("\", \"");
        out.print(lmsHead.getPurpose());
        out.print("\", \"");
        out.print(lmsHead.getNumber());
        out.print("\", \"");
        out.print(lmsHead.getName());
        out.print("\", \"");
        out.print(lmsHead.getVersion());
        out.println("\");");
    }

    protected void printEnumFileFooter(
            final PrintStream out,
            final String enumName) {
        out.println();
        out.println();
        out.println("    private final Integer     id;");
        out.println("    private final LmsLogLevel level;");
        out.println("    private final String      message;");
        out.println();

        out.print("    private ");
        out.print(enumName);
        out.println("(final Integer id, final LmsLogLevel level, final String message) {");
        out.println("        this.id = id;");
        out.println("        this.level = level;");
        out.println("        this.message = message;");
        out.println("    }");
        out.println();
        out.println("    @Override");
        out.println("    public Integer getId() {");
        out.println("        return id;");
        out.println("    }");
        out.println();
        out.println("    @Override");
        out.println("    public LmsLogLevel getLevel() {");
        out.println("        return level;");
        out.println("    }");
        out.println();
        out.println("    @Override");
        out.println("    public String getName() {");
        out.println("        return name();");
        out.println("    }");
        out.println();
        out.println("    @Override");
        out.println("    public String getMessage() {");
        out.println("        return message;");
        out.println("    }");
        out.println("}");
    }

    protected void printEnumItems(
            final PrintStream out,
            final LmsFileData lms,
            final String enumName) {
        Collection<LmsMessageTemplate> itemsCol = lms.getTemplates();
        LmsMessageTemplate[] items = itemsCol.toArray(
                new LmsMessageTemplate[itemsCol.size()]);
        Arrays.sort(items, new Comparator<LmsMessageTemplate>() {
            @Override
            public int compare(final LmsMessageTemplate o1, final LmsMessageTemplate o2) {
                return o1.getId() - o2.getId();
            }
        });

        LmsMessageTemplate item;
        for (int i = 0; i < items.length; i++) {
            item = items[i];
            out.println();

            String javadoc = getEnumItemJavadocStr(item);
            if (javadoc != null && !javadoc.isEmpty()) {
                out.println("    /**");
                out.print("     * ");
                out.println(javadoc);
                out.println("     */");
            }

            out.print("    ");
            out.print(item.getName());
            out.print('(');
            out.print(item.getId());
            out.print(", LmsLogLevel.");
            out.print(item.getLevel().name());
            out.print(',');
            if (item.getMessage().length() > 30) {
                out.println();
                out.print("           ");
            }
            out.print(" \"");
            out.print(item.getMessage().replaceAll("\"", "\\\\\""));
            out.print("\")");
            if (i + 1 < items.length) {
                out.println(',');
            } else {
                out.println(';');
            }
        }
    }

    protected String getEnumItemJavadocStr(final LmsMessageTemplate item) {
        return item.getLevel().name() + " log level event, id = " + item.getId()
                + ", default message text/format:<br/>\n     * <code>\""
                + item.getMessage() + "\"</code>";
    }
}

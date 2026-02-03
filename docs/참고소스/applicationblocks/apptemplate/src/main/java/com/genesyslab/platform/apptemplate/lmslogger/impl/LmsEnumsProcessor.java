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

import com.genesyslab.platform.apptemplate.lmslogger.LmsEnumType;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.SupportedAnnotationTypes;

import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.util.Elements;
import javax.lang.model.util.SimpleElementVisitor8;

import javax.tools.FileObject;
import javax.tools.Diagnostic.Kind;

import javax.tools.StandardLocation;

import java.util.Set;

import java.io.IOException;
import java.io.OutputStream;


/**
 * Annotation processor for generated LMS enumerations.
 * <p/>
 * <i><b>Note:</b> This class is a part of PSDK internal functionality for LMS declarations support.<br/>
 * It is not supposed for direct usage from applications.</i>
 */
@SupportedAnnotationTypes("com.genesyslab.platform.apptemplate.lmslogger.LmsEnumType")
public class LmsEnumsProcessor extends AbstractProcessor {

    /**
     * The location of the LmsEnums data file. This file is written by this processor, and read by
     * {@link com.genesyslab.platform.apptemplate.lmslogger.LmsMessageConveyor}.
     */
    public static final String ENUMS_SET_FILE =
            "META-INF/com/genesyslab/platform/apptemplate/lmslogger/LmsEnums.dat";

    private final LmsEnumsCache enumsCache = new LmsEnumsCache();


    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    @Override
    public boolean process(
            final Set<? extends TypeElement> annotations,
            final RoundEnvironment roundEnv) {
        try {
            final Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(LmsEnumType.class);
            if (elements.isEmpty()) {
                return false;
            }
            collectEnums(elements);
            writeEnumsFile(elements.toArray(new Element[elements.size()]));
            return true;
        } catch (final IOException e) {
            error(e.getMessage());
            return false;
        }
    }

    private void error(final CharSequence message) {
        processingEnv.getMessager().printMessage(Kind.ERROR, message);
    }


    private void collectEnums(final Iterable<? extends Element> elements) {
        final Elements elementUtils = processingEnv.getElementUtils();
        final ElementVisitor<String, LmsEnumType> pluginVisitor =
                new LmsEnumTypeVisitor(elementUtils);
        for (final Element element : elements) {
            final LmsEnumType plugin = element.getAnnotation(LmsEnumType.class);
            final String entry = element.accept(pluginVisitor, plugin);
            enumsCache.add(entry);
        }
    }

    private void writeEnumsFile(final Element... elements) throws IOException {
        final FileObject fo = processingEnv.getFiler().createResource(
                StandardLocation.CLASS_OUTPUT, "", ENUMS_SET_FILE, elements);
        final OutputStream out = fo.openOutputStream();
        try {
            enumsCache.writeCache(out);
        } finally {
            out.close();
        }
    }

    /**
     * ElementVisitor to scan the LmsEnum annotation.
     */
    private static class LmsEnumTypeVisitor extends SimpleElementVisitor8<String, LmsEnumType> {

        private final Elements elements;

        private LmsEnumTypeVisitor(final Elements elements) {
            this.elements = elements;
        }

        @Override
        public String visitType(final TypeElement e, final LmsEnumType enumType) {
            if (enumType == null) {
                throw new NullPointerException("LmsEnumType annotation is null.");
            }
            return elements.getBinaryName(e).toString();
        }
    }
}

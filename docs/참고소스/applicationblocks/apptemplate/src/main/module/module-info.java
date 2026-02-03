// ===============================================================================
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
// Copyright (c) 2005 - 2019 Genesys Telecommunications Laboratories, Inc. All rights reserved.
// ===============================================================================

module com.genesyslab.platform.apptemplate {

    requires java.base;
    requires java.logging;
    requires java.compiler;
    requires java.annotation;

    requires static java.xml;

    requires static org.slf4j; // org.slf4j v1.7;
    requires static org.apache.log4j;
    requires static org.apache.logging.log4j;
    requires static org.apache.logging.log4j.core;

    requires transitive com.genesyslab.platform.kvlists;
    requires transitive com.genesyslab.platform.commons;
    requires transitive com.genesyslab.platform.connection;
    requires transitive com.genesyslab.platform.protocol;
    requires transitive com.genesyslab.platform.commonsappblock;
    requires transitive com.genesyslab.platform.configurationprotocol;
    requires transitive com.genesyslab.platform.managementprotocol;

    requires transitive com.genesyslab.platform.comappblock;
    requires transitive com.genesyslab.platform.warmstandbyappblock;

    requires com.genesyslab.platform.apptemplate.log4j2plugin;

    exports com.genesyslab.platform.apptemplate.application;
    exports com.genesyslab.platform.apptemplate.configuration;
    exports com.genesyslab.platform.apptemplate.configuration.log;
    exports com.genesyslab.platform.apptemplate.filtering;
    exports com.genesyslab.platform.apptemplate.lmslogger;
    exports com.genesyslab.platform.apptemplate.log4j2;

    exports com.genesyslab.platform.apptemplate.util
             to com.genesyslab.platform.cloudtemplate;

    provides javax.annotation.processing.Processor
             with com.genesyslab.platform.apptemplate.lmslogger.impl.LmsEnumsProcessor;

    provides com.genesyslab.platform.apptemplate.log4j2plugin.FileHeaderProvider
             with com.genesyslab.platform.apptemplate.log4j2.GFileHeaderProvider;
    provides com.genesyslab.platform.apptemplate.log4j2plugin.GMessageServerDeliveryManager
             with com.genesyslab.platform.apptemplate.lmslogger.log4j2.GMessageServerDeliveryManagerImpl;
}

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
package com.genesyslab.platform.apptemplate.application;

import com.genesyslab.platform.apptemplate.lmslogger.LmsEventLogger;
import com.genesyslab.platform.apptemplate.lmslogger.LmsLoggerFactory;
import com.genesyslab.platform.apptemplate.configuration.IGApplicationConfiguration;

import com.genesyslab.platform.applicationblocks.com.IConfService;


/**
 * The application runtime context interface provided by the application
 * configuration manager ({@link GFApplicationConfigurationManager}).
 *
 * @see GFApplicationConfigurationManager
 */
public interface GFApplicationContext {

    /**
     * Provides reference to the configuration service.
     *
     * @return reference to the configuration service.
     */
    IConfService getConfService();

    /**
     * Returns the application configuration current state snapshot.
     *
     * @return the application configuration snapshot.
     */
    IGApplicationConfiguration getConfiguration();

    /**
     * Returns facade interface of {@link LmsEventLogger}'s factory.<br/>
     * It allows applications to create separate LMS events loggers for different modules,
     * though, it's OK to share the one, which is already created by the application
     * manager - {@link #getLmsEventLogger()}.
     *
     * @return the factory of LmsEventLogger's.
     * @see #getLmsEventLogger()
     */
    LmsLoggerFactory getLmsLoggerFactory();

    /**
     * Returns the application LmsEventLogger.<br/>
     * Application may use (and share between its modules) this logger instance,
     * or it may use the logger factory ({@link #getLmsLoggerFactory()})
     * to create named loggers for own needs.
     *
     * @return the application LmsEventLogger instance.
     */
    LmsEventLogger getLmsEventLogger();
}

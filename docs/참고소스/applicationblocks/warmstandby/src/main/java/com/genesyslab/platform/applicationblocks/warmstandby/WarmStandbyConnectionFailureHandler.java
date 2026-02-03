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
package com.genesyslab.platform.applicationblocks.warmstandby;


/**
 * An interface to be implemented for the custom handling of connection
 * failure events.
 *
 * <p/>Users can create their own algorithm to restore connection functionality
 * by creating a custom implementation of the <code>WarmStandbyConnectionFailureHandler</code>
 * interface and then configuring the <code>WarmStandbyService</code> instance to use it. The
 * <code>WarmStandbyService</code> will call the custom implementation and provide failure
 * context, so the user can specify whether to schedule the next reconnect task,
 * switchover, or stop the service.
 *
 * <p/>Here are some samples of the handler implementation:
 *
 * <h3>Empty Handler Passing to Default Behavior</h3>
 * <code><pre>
 *   class MyDummyFailureHandler
 *                   implements WarmStandbyConnectionFailureHandler {
 *       public boolean handleRegistrationFailure(
 *               WarmStandbyConnectionFailureContext context) {
 *           System.out.println(RegistrationFailure);
 *           return false;
 *       }
 *       public boolean handleConnectionFailure(
 *               WarmStandbyConnectionFailureContext context) {
 *           System.out.println(ConnectionFailure);
 *           return false;
 *       }
 *   }
 * </pre></code>
 *
 * <p/><h3>Duplicate Default Behavior</h3>
 * <code><pre>
 *   class MyDefaultFailureHandler
 *                   implements WarmStandbyConnectionFailureHandler {
 *       public boolean handleRegistrationFailure(
 *               WarmStandbyConnectionFailureContext context) {
 *           context.doStandbyOff();
 *           return true;
 *       }
 *       public boolean handleConnectionFailure(
 *               WarmStandbyConnectionFailureContext context) {
 *           WarmStandbyService service = context.getService();
 *           WarmStandbyConfiguration config =
 *               service.getConfiguration();
 *
 *           if (service.getAttempt() &gt; config.getAttempts()) {
 *               Short cfgSwitchovers = config.getSwitchovers();
 *               if (cfgSwitchovers == null
 *                   || service.getSwitchover() &lt; cfgSwitchovers) {
 *                   context.doSwitchover();
 *               } else {
 *                   if (log.isDebug()) {
 *                       log.debug("Switchover "
 *                         + service.getSwitchover()
 *                         + " of " + cfgSwitchovers
 *                         + ", WarmStandbyService is stopped");
 *                   }
 *                   context.doStandbyOff();
 *                   return true;
 *               }
 *           }
 *           context.scheduleReconnect(config.getTimeout());
 *           return true;
 *       }
 *   }
 * </pre></code>
 *
 * <p/><h3>Some synthetic handler with trick</h3>
 * Scenario: A <code>WarmStandbyConfiguration</code> object for Genesys Configuration
 * Server with an unavailable active server and with the wrong initial value
 * for user password.
 *
 * <p/>In this scenario, we can create custom handler which performs fast switchover
 * on connection failure, corrects the password on registration failure, and
 * initiates a reconnect with the new password:
 * <code><pre>
 *       final ConfServerProtocol cfgProtocol =
 *           new ConfServerProtocol(confEPBad1);
 *       cfgProtocol.setClientName("default");
 *       cfgProtocol.setClientApplicationType(CfgAppType.CFGSCE.ordinal());
 *       cfgProtocol.setUserName("default");
 *       cfgProtocol.setUserPassword("&lt;wrong-pass&gt;");
 *       cfgProtocol.setTimeout(2000);
 *
 *       WarmStandbyConnectionFailureHandler handler =
 *           new WarmStandbyConnectionFailureHandler() {
 *               public boolean handleRegistrationFailure(
 *                       WarmStandbyConnectionFailureContext context) {
 *                   System.out.println("handleRegistrationFailure");
 *                   ((ConfServerProtocol) context.getService().getChannel())
 *                       .setUserPassword("&lt;good-password&gt;");
 *                   context.scheduleReconnect(100);
 *                   return true;
 *               }
 *               public boolean handleConnectionFailure(
 *                       WarmStandbyConnectionFailureContext context) {
 *                   System.out.println("handleConnectionFailure");
 *                   context.doSwitchover();
 *                   context.scheduleReconnect(100);
 *                   return true;
 *               }
 *       };
 *
 *       WarmStandbyService service = new WarmStandbyService(cfgProtocol);
 *       WarmStandbyConfiguration config = new WarmStandbyConfiguration(
 *               confEPBad1, confEPGood1,
 *               1000, (short) 2, (short) 3);
 *       service.applyConfiguration(config);
 *       service.setConnectionFailureHandler(handler);
 *       service.start();
 *       cfgProtocol.beginOpen();
 * </pre></code>
 */
public interface WarmStandbyConnectionFailureHandler {

    /**
     * This call-back method is called in case of RegistrationException.
     * It means that target server is available, but for some reason
     * it does not allow the client connection. It may be caused by
     * wrong login credentials, or some other exception while client
     * registration procedure on opened connection.
     * Usually it means that there is no need to try reconnect.
     * <p/>
     * Custom code may use provided context to access WarmStandbyService instance,
     * to get WarmStandbyConfiguration, and the client protocol connection.
     * Also the context allows user to schedule next reconnect task,
     * switchover, or stop service.
     *
     * @param context failure context
     * @return true if the failure event is handled, or
     *         false if user wants to let the service to perform default
     *         operation for the event (stop the WarmStandby service)
     * @see com.genesyslab.platform.commons.protocol.RegistrationException
     */
    boolean handleRegistrationFailure(
            WarmStandbyConnectionFailureContext context);

    /**
     * This call-back method is called in case of connection failure
     * (except RegistrationException).
     * <p/>
     * Custom code may use provided context to access WarmStandbyService instance,
     * to get WarmStandbyConfiguration, and the client protocol connection.
     * Also the context allows user to schedule next reconnect task,
     * switchover, or stop service.
     *
     * @param context failure context
     * @return true if the failure event is handled, or
     *         false if user wants to let the service to perform default
     *         operation for the event
     */
    boolean handleConnectionFailure(
            WarmStandbyConnectionFailureContext context);
}

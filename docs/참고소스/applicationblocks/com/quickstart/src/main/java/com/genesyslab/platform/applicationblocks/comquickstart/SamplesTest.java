//===============================================================================
// Any authorized distribution of any copy of this code (including any related
// documentation) must reproduce the following restrictions, disclaimer and copyright
// notice:

// The Genesys name, trademarks and/or logo(s) of Genesys shall not be used to name
// (even as a part of another name), endorse and/or promote products derived from
// this code without prior written permission from Genesys Telecommunications
// Laboratories, Inc.

// The use, copy, and/or distribution of this code is subject to the terms of the Genesys
// Developer License Agreement.  This code shall not be used, copied, and/or
// distributed under any other license agreement.

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

// Copyright (c) 2006 - 2019 Genesys Telecommunications Laboratories, Inc. All rights reserved.
//===============================================================================
package com.genesyslab.platform.applicationblocks.comquickstart;

import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.objects.CfgApplication;
import com.genesyslab.platform.applicationblocks.com.objects.CfgPerson;
import com.genesyslab.platform.applicationblocks.com.ConfigException;

import com.genesyslab.platform.commons.protocol.ProtocolException;

import java.util.ResourceBundle;


/**
 * Small test application to execute some of described samples in test mode
 * to ensure that provided code is working.
 *
 * @author <a href="mailto:makagon@genesyslab.com">Petr Makagon</a>
 * @author <a href="mailto:vladb@genesyslab.com">Vladislav Baranovsky</a>
 * @author <a href="mailto:afilatov@genesyslab.com">Alexander Filatov</a>
 * @author <a href="mailto:abrazhny@genesyslab.com">Anton Brazhnyk</a>
 * @author <a href="mailto:svolokh@genesyslab.com">Sergii Volokh</a>
 */
public class SamplesTest {

    /**
     * Small function to be executed to check that main Configuration Service operations
     * are working. Some kind of test.
     * Can be used for automatic tests with dependance on Genesys environment.
     *
     * @param args programm commandline arguments
     * @throws ConfigException in case of any configuration service exception
     * @throws ProtocolException in case of any configuration protocol exception
     * @throws InterruptedException if process was interrupted
     */
    public static void main(final String[] args)
            throws ConfigException, InterruptedException, ProtocolException {
        ResourceBundle properties = ResourceBundle.getBundle("quickstart");

        String configServerHost;
        int    configServerPort;
        String configServerUser;
        String configServerPass;

        String tempAppName   = "AppName4Test"; // Uniq name for temp app to be created,
                                               // changed and deleted.
        String tempAgentName = "AgentName4Test"; // Uniq name for temp agent to be created,
                                                 // changed and deleted.

        System.out.println("ComJavaQuickStart started execution.");

        String someAppName = properties.getString("ConfServerClientName");
        if (someAppName == null || someAppName.equals("")) {
            someAppName = "default";
        }
        configServerHost = properties.getString("ConfServerHost");
        configServerPort = Integer.parseInt(properties.getString("ConfServerPort"));
        configServerUser = properties.getString("ConfServerUser");
        configServerPass = properties.getString("ConfServerPassword");


        IConfService service = InitializationSamples.initializeConfigService(
                tempAppName, configServerHost, configServerPort,
                configServerUser, configServerPass
        );

        ReadConfigurationSamples.readAndPrintCfgApplication(service, someAppName);

        ReadConfigurationSamples.readAllCfgApplicationsAsync(service);

        // Create application test (remove if it is already created before):
        DeleteConfObjectSamples.deleteApplication(service, tempAppName);
        CfgApplication app = CreateApplicationSamples.createSrvApplicationExt(service, tempAppName);
        // SAVE the application - the sample method does not save it:
        app.save();

        // Some tests with CfgPerson:
        // Delete person if already exists and then recreate:
        DeleteConfObjectSamples.deletePerson(service, tempAgentName);
        CfgPerson agent = AgentInformationSamples.createPersonRecord(service, tempAgentName);
        agent.save();
        AgentInformationSamples.readPersonPermissions(service, agent);
        AgentInformationSamples.modifyPersonPermissions(service, agent);
        agent.delete();

        EventsSubscriptionSamples.testEvents(service, tempAppName);

        DeleteConfObjectSamples.deleteApplication(service, tempAppName);

        // Closes protocol connection and release ConfService instance:
        InitializationSamples.uninitializeConfigService(service);

        System.out.println("ComJavaQuickStart finished execution.");
    }
}

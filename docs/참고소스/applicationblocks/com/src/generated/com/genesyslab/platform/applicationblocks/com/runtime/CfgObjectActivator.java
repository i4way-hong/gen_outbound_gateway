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

/* ATTENTION! DO NOT MODIFY THIS FILE - IT IS AUTOMATICALLY GENERATED! */

package com.genesyslab.platform.applicationblocks.com.runtime;

import com.genesyslab.platform.applicationblocks.com.CfgBase;
import com.genesyslab.platform.applicationblocks.com.ICfgBase;
import com.genesyslab.platform.applicationblocks.com.IConfService;

import com.genesyslab.platform.applicationblocks.com.runtime.factory.*;

import com.genesyslab.platform.configuration.protocol.obj.*;

import javax.annotation.Generated;

import org.w3c.dom.Node;

import java.util.Map;
import java.util.HashMap;


/**
 * Internal facade and container for shared objects factories.
 *
 * <p/><b><i>Note:</i></b> It is designed for COM AB internal use only.<br/>
 * It may be a subject of structural and functional changes without notice.
 */
@Generated(value="com.genesyslab.platform.applicationblocks.com.generator.COMGenerator",
           date="2023-11-02T09:44:20-07:00")
public final class CfgObjectActivator {

    private static Map<String, ICfgObjectFactory> objectsFactories;

    private CfgObjectActivator() {
    }

    @SuppressWarnings("unchecked")
    public static <T extends CfgBase> T createInstance(
                     final String       objectType,
                     final IConfService confService,
                     final Node         xmlData,
                     final Object[]     additionalParams) {
        ICfgObjectFactory factory = objectsFactories.get(objectType);

        if (factory != null) {
            return (T) factory.create(confService, xmlData, additionalParams);
        }

        throw new IllegalArgumentException("Object Activator: invalid object type");
    }

    @SuppressWarnings("unchecked")
    public static <T extends CfgBase> T createInstance(
                     final IConfService   confService,
                     final ConfObjectBase objData,
                     final Object[]       additionalParams) {
        ICfgObjectFactory factory = objectsFactories.get(objData.getClassInfo().getName());

        if (factory != null) {
            return (T) factory.create(confService, objData, additionalParams);
        }

        throw new IllegalArgumentException("Object Activator: invalid object type");
    }

    public static Class<? extends ICfgBase> getType(final String objectType) {
        ICfgObjectFactory factory = objectsFactories.get(objectType);

        if (factory != null) {
            return factory.getObjectType();
        }

        return null;
    }

    static {
        objectsFactories = new HashMap<String, ICfgObjectFactory>();

        objectsFactories.put("CfgSwitch", new CfgSwitchFactory());
        objectsFactories.put("CfgDN", new CfgDNFactory());
        objectsFactories.put("CfgPerson", new CfgPersonFactory());
        objectsFactories.put("CfgPersonBrief", new CfgPersonBriefFactory());
        objectsFactories.put("CfgPlace", new CfgPlaceFactory());
        objectsFactories.put("CfgAgentGroup", new CfgAgentGroupFactory());
        objectsFactories.put("CfgPlaceGroup", new CfgPlaceGroupFactory());
        objectsFactories.put("CfgTenant", new CfgTenantFactory());
        objectsFactories.put("CfgTenantBrief", new CfgTenantBriefFactory());
        objectsFactories.put("CfgService", new CfgServiceFactory());
        objectsFactories.put("CfgApplication", new CfgApplicationFactory());
        objectsFactories.put("CfgHost", new CfgHostFactory());
        objectsFactories.put("CfgPhysicalSwitch", new CfgPhysicalSwitchFactory());
        objectsFactories.put("CfgScript", new CfgScriptFactory());
        objectsFactories.put("CfgSkill", new CfgSkillFactory());
        objectsFactories.put("CfgActionCode", new CfgActionCodeFactory());
        objectsFactories.put("CfgAgentLogin", new CfgAgentLoginFactory());
        objectsFactories.put("CfgTransaction", new CfgTransactionFactory());
        objectsFactories.put("CfgDNGroup", new CfgDNGroupFactory());
        objectsFactories.put("CfgStatDay", new CfgStatDayFactory());
        objectsFactories.put("CfgStatTable", new CfgStatTableFactory());
        objectsFactories.put("CfgAppPrototype", new CfgAppPrototypeFactory());
        objectsFactories.put("CfgAccessGroup", new CfgAccessGroupFactory());
        objectsFactories.put("CfgAccessGroupBrief", new CfgAccessGroupBriefFactory());
        objectsFactories.put("CfgFolder", new CfgFolderFactory());
        objectsFactories.put("CfgField", new CfgFieldFactory());
        objectsFactories.put("CfgFormat", new CfgFormatFactory());
        objectsFactories.put("CfgTableAccess", new CfgTableAccessFactory());
        objectsFactories.put("CfgCallingList", new CfgCallingListFactory());
        objectsFactories.put("CfgCampaign", new CfgCampaignFactory());
        objectsFactories.put("CfgTreatment", new CfgTreatmentFactory());
        objectsFactories.put("CfgFilter", new CfgFilterFactory());
        objectsFactories.put("CfgTimeZone", new CfgTimeZoneFactory());
        objectsFactories.put("CfgVoicePrompt", new CfgVoicePromptFactory());
        objectsFactories.put("CfgIVRPort", new CfgIVRPortFactory());
        objectsFactories.put("CfgIVR", new CfgIVRFactory());
        objectsFactories.put("CfgAlarmCondition", new CfgAlarmConditionFactory());
        objectsFactories.put("CfgEnumerator", new CfgEnumeratorFactory());
        objectsFactories.put("CfgEnumeratorValue", new CfgEnumeratorValueFactory());
        objectsFactories.put("CfgObjectiveTable", new CfgObjectiveTableFactory());
        objectsFactories.put("CfgCampaignGroup", new CfgCampaignGroupFactory());
        objectsFactories.put("CfgGVPReseller", new CfgGVPResellerFactory());
        objectsFactories.put("CfgGVPCustomer", new CfgGVPCustomerFactory());
        objectsFactories.put("CfgGVPIVRProfile", new CfgGVPIVRProfileFactory());
        objectsFactories.put("CfgScheduledTask", new CfgScheduledTaskFactory());
        objectsFactories.put("CfgRole", new CfgRoleFactory());
        objectsFactories.put("CfgPersonLastLogin", new CfgPersonLastLoginFactory());
        objectsFactories.put("CfgGroup", new CfgGroupFactory());
        objectsFactories.put("CfgAddress", new CfgAddressFactory());
        objectsFactories.put("CfgPhones", new CfgPhonesFactory());
        objectsFactories.put("CfgOSinfo", new CfgOSFactory());
        objectsFactories.put("CfgSwitchAccessCode", new CfgSwitchAccessCodeFactory());
        objectsFactories.put("CfgDelSwitchAccess", new CfgDelSwitchAccessFactory());
        objectsFactories.put("CfgDNAccessNumber", new CfgDNAccessNumberFactory());
        objectsFactories.put("CfgPersonRank", new CfgAppRankFactory());
        objectsFactories.put("CfgSkillLevel", new CfgSkillLevelFactory());
        objectsFactories.put("CfgAgentLoginInfo", new CfgAgentLoginInfoFactory());
        objectsFactories.put("CfgGroupDN", new CfgDNInfoFactory());
        objectsFactories.put("CfgServerInfo", new CfgServerFactory());
        objectsFactories.put("CfgSubcode", new CfgSubcodeFactory());
        objectsFactories.put("CfgStatInterval", new CfgStatIntervalFactory());
        objectsFactories.put("CfgCallingListInfo", new CfgCallingListInfoFactory());
        objectsFactories.put("CfgCampaignGroupInfo", new CfgCampaignGroupInfoFactory());
        objectsFactories.put("CfgDetectEvent", new CfgDetectEventFactory());
        objectsFactories.put("CfgRemovalEvent", new CfgRemovalEventFactory());
        objectsFactories.put("CfgID", new CfgIDFactory());
        objectsFactories.put("CfgSolutionComponent", new CfgSolutionComponentFactory());
        objectsFactories.put("CfgMemberID", new CfgMemberIDFactory());
        objectsFactories.put("CfgOwnerID", new CfgOwnerIDFactory());
        objectsFactories.put("CfgObjectID", new CfgObjectIDFactory());
        objectsFactories.put("CfgResourceID", new CfgResourceIDFactory());
        objectsFactories.put("CfgParentID", new CfgParentIDFactory());
        objectsFactories.put("CfgACE", new CfgACEFactory());
        objectsFactories.put("CfgACL", new CfgACLFactory());
        objectsFactories.put("CfgACEID", new CfgACEIDFactory());
        objectsFactories.put("CfgACLID", new CfgACLIDFactory());
        objectsFactories.put("CfgConnectionInfo", new CfgConnInfoFactory());
        objectsFactories.put("CfgSolutionComponentDefinition", new CfgSolutionComponentDefinitionFactory());
        objectsFactories.put("CfgServiceInfo", new CfgServiceInfoFactory());
        objectsFactories.put("CfgAgentInfo", new CfgAgentInfoFactory());
        objectsFactories.put("CfgAppServicePermission", new CfgAppServicePermissionFactory());
        objectsFactories.put("CfgObjectiveTableRecord", new CfgObjectiveTableRecordFactory());
        objectsFactories.put("CfgObjectResource", new CfgObjectResourceFactory());
        objectsFactories.put("CfgPortInfo", new CfgPortInfoFactory());
        objectsFactories.put("CfgServerVersion", new CfgServerVersionFactory());
        objectsFactories.put("CfgServerHostID", new CfgServerHostIDFactory());
        objectsFactories.put("CfgUpdatePackageRecord", new CfgUpdatePackageRecordFactory());
        objectsFactories.put("CfgRoleMember", new CfgRoleMemberFactory());
        objectsFactories.put("CfgDeltaGroup", new CfgDeltaGroupFactory());
        objectsFactories.put("CfgDeltaAgentInfo", new CfgDeltaAgentInfoFactory());
        objectsFactories.put("CfgDeltaSwitch", new CfgDeltaSwitchFactory());
        objectsFactories.put("CfgDeltaDN", new CfgDeltaDNFactory());
        objectsFactories.put("CfgDeltaPerson", new CfgDeltaPersonFactory());
        objectsFactories.put("CfgDeltaPlace", new CfgDeltaPlaceFactory());
        objectsFactories.put("CfgDeltaAgentGroup", new CfgDeltaAgentGroupFactory());
        objectsFactories.put("CfgDeltaPlaceGroup", new CfgDeltaPlaceGroupFactory());
        objectsFactories.put("CfgDeltaTenant", new CfgDeltaTenantFactory());
        objectsFactories.put("CfgDeltaService", new CfgDeltaServiceFactory());
        objectsFactories.put("CfgDeltaApplication", new CfgDeltaApplicationFactory());
        objectsFactories.put("CfgDeltaHost", new CfgDeltaHostFactory());
        objectsFactories.put("CfgDeltaPhysicalSwitch", new CfgDeltaPhysicalSwitchFactory());
        objectsFactories.put("CfgDeltaScript", new CfgDeltaScriptFactory());
        objectsFactories.put("CfgDeltaSkill", new CfgDeltaSkillFactory());
        objectsFactories.put("CfgDeltaActionCode", new CfgDeltaActionCodeFactory());
        objectsFactories.put("CfgDeltaAgentLogin", new CfgDeltaAgentLoginFactory());
        objectsFactories.put("CfgDeltaTransaction", new CfgDeltaTransactionFactory());
        objectsFactories.put("CfgDeltaDNGroup", new CfgDeltaDNGroupFactory());
        objectsFactories.put("CfgDeltaStatDay", new CfgDeltaStatDayFactory());
        objectsFactories.put("CfgDeltaStatTable", new CfgDeltaStatTableFactory());
        objectsFactories.put("CfgDeltaAppPrototype", new CfgDeltaAppPrototypeFactory());
        objectsFactories.put("CfgDeltaAccessGroup", new CfgDeltaAccessGroupFactory());
        objectsFactories.put("CfgDeltaFolder", new CfgDeltaFolderFactory());
        objectsFactories.put("CfgDeltaField", new CfgDeltaFieldFactory());
        objectsFactories.put("CfgDeltaFormat", new CfgDeltaFormatFactory());
        objectsFactories.put("CfgDeltaTableAccess", new CfgDeltaTableAccessFactory());
        objectsFactories.put("CfgDeltaCallingList", new CfgDeltaCallingListFactory());
        objectsFactories.put("CfgDeltaCampaign", new CfgDeltaCampaignFactory());
        objectsFactories.put("CfgDeltaTreatment", new CfgDeltaTreatmentFactory());
        objectsFactories.put("CfgDeltaFilter", new CfgDeltaFilterFactory());
        objectsFactories.put("CfgDeltaTimeZone", new CfgDeltaTimeZoneFactory());
        objectsFactories.put("CfgDeltaVoicePrompt", new CfgDeltaVoicePromptFactory());
        objectsFactories.put("CfgDeltaIVRPort", new CfgDeltaIVRPortFactory());
        objectsFactories.put("CfgDeltaIVR", new CfgDeltaIVRFactory());
        objectsFactories.put("CfgDeltaAlarmCondition", new CfgDeltaAlarmConditionFactory());
        objectsFactories.put("CfgDeltaEnumerator", new CfgDeltaEnumeratorFactory());
        objectsFactories.put("CfgDeltaEnumeratorValue", new CfgDeltaEnumeratorValueFactory());
        objectsFactories.put("CfgDeltaObjectiveTable", new CfgDeltaObjectiveTableFactory());
        objectsFactories.put("CfgDeltaCampaignGroup", new CfgDeltaCampaignGroupFactory());
        objectsFactories.put("CfgDeltaGVPReseller", new CfgDeltaGVPResellerFactory());
        objectsFactories.put("CfgDeltaGVPCustomer", new CfgDeltaGVPCustomerFactory());
        objectsFactories.put("CfgDeltaGVPIVRProfile", new CfgDeltaGVPIVRProfileFactory());
        objectsFactories.put("CfgDeltaScheduledTask", new CfgDeltaScheduledTaskFactory());
        objectsFactories.put("CfgDeltaRole", new CfgDeltaRoleFactory());
        objectsFactories.put("CfgDeltaPersonLastLogin", new CfgDeltaPersonLastLoginFactory());
    }
}

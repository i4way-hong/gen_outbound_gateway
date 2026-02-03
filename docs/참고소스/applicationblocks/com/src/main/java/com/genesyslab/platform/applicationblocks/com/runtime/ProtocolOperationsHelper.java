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
package com.genesyslab.platform.applicationblocks.com.runtime;

import com.genesyslab.platform.applicationblocks.com.ICfgObject;
import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.Subscription;
import com.genesyslab.platform.applicationblocks.com.NotificationQuery;
import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.applicationblocks.com.ConfigServerException;
import com.genesyslab.platform.applicationblocks.com.ConfigRuntimeException;
import com.genesyslab.platform.applicationblocks.com.objects.CfgACEID;
import com.genesyslab.platform.applicationblocks.com.objects.CfgApplication;
import com.genesyslab.platform.configuration.protocol.confserver.events.EventAccountRead;
import com.genesyslab.platform.configuration.protocol.confserver.events.EventAccountUpdated;
import com.genesyslab.platform.configuration.protocol.confserver.events.EventError;
import com.genesyslab.platform.configuration.protocol.confserver.events.EventObjectCreated;
import com.genesyslab.platform.configuration.protocol.confserver.events.EventObjectDeleted;
import com.genesyslab.platform.configuration.protocol.confserver.events.EventObjectUpdated;
import com.genesyslab.platform.configuration.protocol.confserver.events.EventPermissionsRead;
import com.genesyslab.platform.configuration.protocol.confserver.events.EventPermissionsUpdated;
import com.genesyslab.platform.configuration.protocol.confserver.events.EventObjectPermissionsRead;
import com.genesyslab.platform.configuration.protocol.confserver.events.EventNotificationRegistered;
import com.genesyslab.platform.configuration.protocol.confserver.events.EventNotificationUnregistered;
import com.genesyslab.platform.configuration.protocol.confserver.requests.objects.RequestCreateObject;
import com.genesyslab.platform.configuration.protocol.confserver.requests.objects.RequestUpdateObject;
import com.genesyslab.platform.configuration.protocol.confserver.requests.objects.RequestDeleteObject;
import com.genesyslab.platform.configuration.protocol.confserver.requests.objects.RequestRegisterNotification;
import com.genesyslab.platform.configuration.protocol.confserver.requests.objects.RequestUnregisterNotification;
import com.genesyslab.platform.configuration.protocol.confserver.requests.security.RequestReadAccount;
import com.genesyslab.platform.configuration.protocol.confserver.requests.security.RequestReadPermissions;
import com.genesyslab.platform.configuration.protocol.confserver.requests.security.RequestUpdateAccount;
import com.genesyslab.platform.configuration.protocol.confserver.requests.security.RequestUpdatePermissions;
import com.genesyslab.platform.configuration.protocol.confserver.requests.security.RequestReadObjectPermissions;
import com.genesyslab.platform.configuration.protocol.types.CfgObjectType;
import com.genesyslab.platform.configuration.protocol.types.CfgStructureType;
import com.genesyslab.platform.configuration.protocol.metadata.CfgDescriptionStructure;
import com.genesyslab.platform.configuration.protocol.metadata.CfgMetadata;
import com.genesyslab.platform.configuration.protocol.obj.ConfObject;
import com.genesyslab.platform.configuration.protocol.obj.ConfStructure;
import com.genesyslab.platform.configuration.protocol.obj.ConfObjectDelta;
import com.genesyslab.platform.configuration.protocol.obj.ConfStructureCollection;
import com.genesyslab.platform.configuration.protocol.utilities.CfgUtilities;
import com.genesyslab.platform.configuration.protocol.utilities.CfgErrorCode;
import com.genesyslab.platform.commons.protocol.Message;
import com.genesyslab.platform.commons.protocol.Protocol;
import com.genesyslab.platform.commons.protocol.ProtocolException;
import com.genesyslab.platform.commons.GEnum;
import com.genesyslab.platform.commons.collections.KeyValueCollection;

import java.util.Iterator;
import java.util.Hashtable;


/**
 * Internal helper class for low level Configuration Protocol operations.
 *
 * <p/><b><i>Note:</i></b> It is designed for COM AB internal use only.<br/>
 * It may be a subject of structural or functional changes without notice.
 *
 * @author <a href="mailto:afilatov@genesyslab.com">Alexander Filatov</a>
 * @author <a href="mailto:svolokh@genesyslab.com">Sergii Volokh</a>
 */
public final class ProtocolOperationsHelper {

    private ProtocolOperationsHelper() {}


    /**
     * Requests configuration server for new configuration object creation.
     *
     * @param protocol reference to opened configuration server connection.
     * @param object configuration object to be created on server.
     * @param parameters additional object parameters.
     * @return successful configuration server response message.
     * @throws ConfigServerException in case of error response from server ({@link EventError}).
     * @throws ConfigException in case of any request procession exception.
     */
    public static EventObjectCreated createObject(
            final Protocol protocol,
            final ConfObject object,
            final KeyValueCollection parameters)
                throws ConfigException {
        RequestCreateObject request = RequestCreateObject.create(
                object, parameters);
        Message response = null;

        try {
            response = protocol.request(request);
        } catch (Exception ex) {
            throw new ConfigException(
                    "Exception on object create request", ex);
        }

        if (response == null) {
            throw new ConfigException(
                    "Error creating object: server response timeout");
        }

        if (response.messageId() == EventObjectCreated.ID) {
            // everything went fine. The object has been created. We now have to reload the object
            // so that it has all new attributes, including DBID.
            return (EventObjectCreated) response;
        }

        if (response.messageId() == EventError.ID) {
            // an error has occurred
            throw createConfigServerException((EventError) response);
        }

        throw new ConfigException(
                "Unexpected response on object creation request: "
                + response);
    }

    /**
     * Updates object on configuration server with given delta information.
     *
     * @param protocol reference to opened configuration server connection.
     * @param delta delta information for object update.
     * @param parameters additional object parameters.
     * @return successful configuration server response message.
     * @throws ConfigServerException in case of error response from server ({@link EventError}).
     * @throws ConfigException in case of any request procession exception.
     */
    public static EventObjectUpdated updateObject(
            final Protocol protocol,
            final ConfObjectDelta delta,
            final KeyValueCollection parameters)
                    throws ConfigException {
        RequestUpdateObject request = RequestUpdateObject.create(delta, parameters);
        Message response = null;

        try {
            response = protocol.request(request);
        } catch (ProtocolException ex) {
            throw new ConfigException(
                    "Exception on object update request", ex);
        }

        if (response == null) {
            throw new ConfigException(
                    "Error updating object: server response timeout");
        }

        if (response.messageId() == EventObjectUpdated.ID) {
            // everything went fine.
            return (EventObjectUpdated) response;
        }

        if (response.messageId() == EventError.ID) {
            throw createConfigServerException((EventError) response);
        }

        throw new ConfigException(
                "Unexpected response on object update request: "
                + response);
    }

    /**
     * Deletes object from the configuration server database.
     *
     * @param protocol reference to opened configuration server connection.
     * @param objectType the configuration object type.
     * @param objectDbid the configuration object DBID.
     * @return successful configuration server response message.
     * @throws ConfigServerException in case of error response from server ({@link EventError}).
     * @throws ConfigException in case of any request procession exception.
     */
    public static EventObjectDeleted deleteObject(
            final Protocol protocol,
            final CfgObjectType objectType,
            final int objectDbid)
                throws ConfigException {
        RequestDeleteObject request = RequestDeleteObject.create(
                objectType.ordinal(),
                objectDbid
        );
        Message response = null;

        try {
            response = protocol.request(request);
        } catch (Exception ex) {
            throw new ConfigException(
                    "Exception on object delete request", ex);
        }

        if (response == null) {
            throw new ConfigException(
                    "Error deleting object: server response timeout");
        }

        if (response.messageId() == EventObjectDeleted.ID) {
            // everything went fine.
            return (EventObjectDeleted) response;
        }

        if (response.messageId() == EventError.ID) {
            throw createConfigServerException((EventError) response);
        }

        throw new ConfigException(
                "Unexcpected response on object delete request: "
                + response);
    }


    public static Subscription subscribe(
            final Protocol protocol,
            final ICfgObject obj)
                    throws ConfigException {
        NotificationQuery query = new NotificationQuery();

        query.setObjectDbid((Integer) obj.getProperty(MiscConstants.DbidPropertyName));
        query.setObjectType(obj.getObjectType());

        return subscribe(protocol, query);
    }

    public static Subscription subscribe(
            final Protocol protocol,
            final NotificationQuery query)
                throws ConfigException {
        KeyValueCollection objectFilter = new KeyValueCollection();
        Hashtable<String, Object> filter = query.getFilter();

        for (String key : filter.keySet()) {
            Object val = filter.get(key);

            if (val instanceof GEnum) {
                objectFilter.addObject(key, ((GEnum) val).ordinal());
            } else {
                objectFilter.addObject(key, val);
            }
        }

        KeyValueCollection subscriptionFilter = new KeyValueCollection();
        subscriptionFilter.addList(MiscConstants.SubscriptionKey, objectFilter);

        Message registerNotification = RequestRegisterNotification.create(subscriptionFilter);
        Message eventMessage = null;

        try {
            eventMessage = protocol.request(registerNotification);
        } catch (Exception ex) {
            throw new ConfigException("Exception on subscription request", ex);
        }

        if (eventMessage == null) {
            throw new ConfigException("Timeout waiting for the server response");

        } else if (eventMessage instanceof EventNotificationRegistered) {
            return new SubscriptionImpl(query);

        } else if (eventMessage instanceof EventError) {
            throw createConfigServerException((EventError) eventMessage);

        } else {
            throw new ConfigException("Error on RegisterNotification: got unexpected "
                    + eventMessage.toString());
        }
    }

    public static void unsubscribe(final Protocol protocol, final Subscription subscription)
            throws ConfigException {
        KeyValueCollection objectFilter = new KeyValueCollection();
        Hashtable<String, Object> filter =
                subscription.getQuery().getFilter();
        for (String key : filter.keySet()) {
            Object val = filter.get(key);

            if (val instanceof GEnum) {
                objectFilter.addObject(key, ((GEnum) val).ordinal());
            } else {
                objectFilter.addObject(key, val);
            }
        }

        KeyValueCollection subscriptionFilter = new KeyValueCollection();
        subscriptionFilter.addList(MiscConstants.SubscriptionKey, objectFilter);

        Message unregisterNotification = RequestUnregisterNotification.create(subscriptionFilter);
        Message eventMessage = null;

        try {
            eventMessage = protocol.request(unregisterNotification);
        } catch (Exception ex) {
            throw new ConfigException("Exception on unsubscription request", ex);
        }

        if (eventMessage == null) {
            throw new ConfigException("Timeout waiting for the server response");

        } else if (eventMessage instanceof EventNotificationUnregistered) {
            // Ok

        } else if (eventMessage instanceof EventError) {
            throw createConfigServerException((EventError) eventMessage);

        } else {
            throw new ConfigException("Error on UnregisterNotification: got unexpected "
                    + eventMessage.toString());
        }
    }


    public static ConfStructure readPermissions(
            final IConfService  confService,
            final int           objectDbid,
            final CfgObjectType objectType)
                throws ConfigException {
        ConfStructure confID = new ConfStructure(
                confService.getMetaData(),
                CfgStructureType.CFGID);
        confID.setPropertyValue("CSID", 0);
        confID.setPropertyValue(MiscConstants.TypePropertyName, objectType.asInteger());
        confID.setPropertyValue(MiscConstants.DbidPropertyName, objectDbid);

        RequestReadPermissions readRequest = RequestReadPermissions.create(confID);
        Message response = null;

        try {
            response = confService.getProtocol().request(readRequest);
        } catch (Exception ex) {
            throw new ConfigRuntimeException(
                    "Error occured when reading object permissions", ex);
        }

        if (response == null) {
            throw new ConfigRuntimeException(
                    "Timeout waiting for the server response");

        } else if (response.messageId() == EventPermissionsRead.ID) {
            // everything went fine.
            return ((EventPermissionsRead) response).getConfPermissionsACL();

        } else if (response.messageId() == EventError.ID) {
            throw createConfigServerException((EventError) response);

        } else {
            throw new ConfigException(
                    "Received unknown event when reading object permissions: "
                    + response);
        }
    }

    public static int readObjectPermissions(
            final IConfService  confService,
            final int           objectDbid,
            final CfgObjectType objectType,
            final int           accountDbid,
            final CfgObjectType accountType)
                throws ConfigException {
        ConfStructure objectID = new ConfStructure(
                confService.getMetaData(),
                CfgStructureType.CFGID);
        objectID.setPropertyValue("CSID", 0);
        objectID.setPropertyValue(MiscConstants.TypePropertyName, objectType.asInteger());
        objectID.setPropertyValue(MiscConstants.DbidPropertyName, objectDbid);

        ConfStructure accountID = new ConfStructure(
                confService.getMetaData(),
                CfgStructureType.CFGID);
        accountID.setPropertyValue("CSID", 0);
        accountID.setPropertyValue(MiscConstants.TypePropertyName, accountType.asInteger());
        accountID.setPropertyValue(MiscConstants.DbidPropertyName, accountDbid);

        RequestReadObjectPermissions readRequest =
                RequestReadObjectPermissions.create(objectID, accountID);
        Message response = null;

        try {
            response = confService.getProtocol().request(readRequest);
        } catch (Exception ex) {
            throw new ConfigRuntimeException(
                    "Error occured when reading object permissions", ex);
        }

        if (response == null) {
            throw new ConfigRuntimeException(
                    "Timeout waiting for the server response");

        } else if (response.messageId() == EventObjectPermissionsRead.ID) {
            // everything went fine.
            return ((EventObjectPermissionsRead) response).getPermissions();

        } else if (response.messageId() == EventError.ID) {
            throw createConfigServerException((EventError) response);

        } else {
            throw new ConfigException(
                    "Received unknown event when reading object permissions: "
                    + response);
        }
    }

    public static boolean updatePermissions(
            final IConfService  confService,
            final int           objectDbid,
            final CfgObjectType objectType,
            final int           accountDbid,
            final CfgObjectType accountType,
            final Integer       newPermissionsMask,
            final boolean       recursion)
                    throws ConfigException {
        // First, read object permissions:
        ConfStructure permissions = readPermissions(confService, objectDbid, objectType);

        ConfStructureCollection acls = (ConfStructureCollection)
                permissions.getPropertyValue("ACEs");

        // Correct the list:
        boolean updated = false;
        ConfStructure acl, id;
        Iterator<ConfStructure> aclIterator = acls.iterator();
        while (aclIterator.hasNext()) {
            acl = aclIterator.next();
            id = (ConfStructure) acl.getPropertyValue("ID");
            int dbid = (Integer) id.getPropertyValue(MiscConstants.DbidPropertyName);
            if (dbid == accountDbid) {
                if (accountType.asInteger().equals(intPropertyValue(
                        id.getPropertyValue(MiscConstants.TypePropertyName)))) {
                    if (newPermissionsMask != null) {
                        if (newPermissionsMask.equals(acl.getPropertyValue(
                                MiscConstants.AccessMaskPropertyName))) {
                            return false; // - we already have the permissions
                        } else {
                            acl.setPropertyValue(MiscConstants.AccessMaskPropertyName,
                                    newPermissionsMask);
                        }
                    } else {
                        aclIterator.remove();
                    }
                    updated = true;
                    break;
                }
            }
        }
        aclIterator = null;

        if (!updated) {
            if (newPermissionsMask != null) {
                ConfStructure newElem = acls.createStructure();
                ConfStructure newId = (ConfStructure) newElem.getOrCreatePropertyValue("ID");
                newId.setPropertyValue(MiscConstants.TypePropertyName, accountType.asInteger());
                newId.setPropertyValue(MiscConstants.DbidPropertyName, accountDbid);
                newElem.setPropertyValue(MiscConstants.AccessMaskPropertyName, newPermissionsMask);
                acls.add(newElem);
            } else {
                return false;
            }
        }

        Integer count = (Integer) permissions.getPropertyValue("count");
        if (count != null) {
            permissions.setPropertyValue("count", acls.size());
        }

        // And update permissions:
        RequestUpdatePermissions messageUpdate =
                RequestUpdatePermissions.create(permissions, recursion ? 1 : 0);

        Message response = null;
        try {
            response = confService.getProtocol().request(messageUpdate);
        } catch (Exception e) {
            throw new ConfigException(
                    "Error occurred when updating permissions", e);
        }

        if (response == null) {
            throw new ConfigException(
                    "Error occured when updating permissions: got null event as a result.");

        } else if (response.messageId() == EventPermissionsUpdated.ID) {
            // everything went fine.
            return true;

        } else if (response.messageId() == EventError.ID) {
            throw createConfigServerException((EventError) response);

        } else {
            throw new ConfigException(
                    "Received unexpected response when updating permissions: " + response);
        }
    }

	/**
	 * Reads "Logon As" account for the CfgAppliaction object of the server type.
	 * 
	 * @param confService service to communicate with Configuration Server
	 * @param application CfgAppliaction which account should be read
	 * @return object that identifies logon account for this application
	 * @throws IllegalArgumentException if confService or application is null
	 * @throws ConfigException if logon account can't be read from server
	 */
	public static CfgACEID getLogonAs(final IConfService confService,
			final CfgApplication application) throws ConfigException {
		if (confService == null)
			throw new IllegalArgumentException("confService can't be null");
		if (application == null)
			throw new IllegalArgumentException("application can't be null");

		CfgMetadata metadata = confService.getMetaData();
		if (metadata == null) {
			throw new ConfigException("Protocol metadata is null");
		}

		ConfStructure appId = new ConfStructure(metadata.getStructure(
				CfgDescriptionStructure.class, CfgStructureType.CFGID));
		appId.setPropertyValue("type", CfgObjectType.CFGApplication.asInteger());
		appId.setPropertyValue("DBID", application.getDBID());

		RequestReadAccount request = RequestReadAccount.create(appId);

		Message response = null;
		try {
			Protocol protocol = confService.getProtocol();
			response = protocol.request(request);
		} catch (Throwable ex) {
			throw new ConfigException("Exception on object read request", ex);
		}

		if (response == null) {
			throw new ConfigException(
					"Error occured when reading account: got null event as a result.");
		} else if (response.messageId() == EventAccountRead.ID) {
			EventAccountRead account = (EventAccountRead) response;
			return new CfgACEID(confService, account.getAccountID(), null);
		} else if (response.messageId() == EventError.ID) {
			throw createConfigServerException((EventError) response);
		} else
			throw new ConfigException(
					"Unexpected response on object update request: " + response);
	}

	/**
	 * Sets "Logon As" account for the CfgAppliaction object of the server type.
	 * 
	 * @param confService service to communicate with Configuration Server
	 * @param application CfgAppliaction which account should be updated
	 * @param accountType supported types are CfgPerson and CfgAccessGroup
	 * @param accountDBID dbid of the person or an access group object
	 * @throws IllegalArgumentException if argument is null, if accountDBID is zero or negative, if accountType has unexpected value
	 * @throws ConfigException if account wasn't updated
	 */
	public static void setLogonAs(final IConfService confService,
			final CfgApplication application, 
			final CfgObjectType accountType,
			int accountDBID) throws ConfigException {
		if (confService == null)
			throw new IllegalArgumentException("confService can't be null");
		if (application == null)
			throw new IllegalArgumentException("application can't be null");
		if (accountType == null)
			throw new IllegalArgumentException("account type can't be null");
		if (accountDBID<1) {
			throw new IllegalArgumentException("account dbid can't be zero or negative");
		}

		if (CfgObjectType.CFGPerson.ordinal() != accountType.ordinal()
				&& CfgObjectType.CFGAccessGroup.ordinal() != accountType.ordinal()) {
			throw new IllegalArgumentException(
					"Account should be either CFGPerson or CFGAccessGroup");
		}

		CfgMetadata metadata = confService.getMetaData();
		if (metadata == null) {
			throw new ConfigException("Protocol metadata is null");
		}
		ConfStructure appId = new ConfStructure(metadata.getStructure(
				CfgDescriptionStructure.class, CfgStructureType.CFGID));
		appId.setPropertyValue("type", CfgObjectType.CFGApplication.asInteger());
		appId.setPropertyValue("DBID", application.getDBID());

		ConfStructure accId = new ConfStructure(metadata.getStructure(
				CfgDescriptionStructure.class, CfgStructureType.CFGID));
		accId.setPropertyValue("type", accountType.asInteger());
		accId.setPropertyValue("DBID", accountDBID);

		RequestUpdateAccount request = RequestUpdateAccount.create(appId, accId);		

		Message response = null;

		try {
			Protocol protocol = confService.getProtocol();
			response = protocol.request(request);
		} catch (Throwable ex) {
			throw new ConfigException("Exception on object update request", ex);
		}

		if (response == null) {
			throw new ConfigException(
					"Error occured when updating account: got null event as a result.");
		} else if (response.messageId() == EventAccountUpdated.ID) {
			// everything went fine.
			return;
		} else if (response.messageId() == EventError.ID) {
			throw createConfigServerException((EventError) response);
		} else
			throw new ConfigException(
					"Unexpected response on object update request: " + response);
	}

    /**
     * Creates COM AB exception representing error from configuration server.
     *
     * @param eventError configuration server error event.
     * @return exception instance.
     */
    public static ConfigServerException createConfigServerException(
            final EventError eventError) {
        CfgErrorCode errCode = CfgErrorCode.getErrorCode(eventError.getErrorCode());
        String message = eventError.getDescription();

        return new ConfigServerException(
                errCode.getErrorType(),
                errCode.getObjectType(),
                errCode.getObjectProperty(),
                message);
    }


    private static Integer intPropertyValue(final Object value) {
        if (value == null) {
            return null;
        }
        if (value.getClass() == Integer.class) {
            return (Integer) value;
        }
        if (value instanceof GEnum) {
            return ((GEnum) value).asInteger();
        }
        throw new IllegalArgumentException(
                "Invalid value type for enumeration: " + value.getClass());
    }
}

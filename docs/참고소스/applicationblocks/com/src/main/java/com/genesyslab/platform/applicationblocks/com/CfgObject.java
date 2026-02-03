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
package com.genesyslab.platform.applicationblocks.com;

import com.genesyslab.platform.applicationblocks.com.objects.CfgACL;
import com.genesyslab.platform.applicationblocks.com.runtime.MiscConstants;
import com.genesyslab.platform.applicationblocks.com.runtime.ProtocolOperationsHelper;
import com.genesyslab.platform.configuration.protocol.confserver.events.EventObjectCreated;
import com.genesyslab.platform.configuration.protocol.confserver.events.EventObjectDeleted;
import com.genesyslab.platform.configuration.protocol.types.CfgObjectType;
import com.genesyslab.platform.configuration.protocol.metadata.CfgDescriptionObject;
import com.genesyslab.platform.configuration.protocol.obj.ConfObject;
import com.genesyslab.platform.configuration.protocol.obj.ConfObjectBase;
import com.genesyslab.platform.configuration.protocol.obj.ConfObjectDelta;
import com.genesyslab.platform.configuration.protocol.utilities.ConfDeltaUtility;
import com.genesyslab.platform.configuration.protocol.xml.ConfDataSaxHandler;
import com.genesyslab.platform.commons.protocol.Protocol;
import com.genesyslab.platform.commons.xmlfactory.XmlFactories;
import com.genesyslab.platform.commons.collections.KeyValueCollection;

import org.w3c.dom.Node;

import java.util.List;
import java.util.Collection;
import java.util.concurrent.locks.Lock;

import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;


/**
 * This is a base class for all standalone Configuration Server objects.
 *
 * @author <a href="mailto:makagon@genesyslab.com">Petr Makagon</a>
 * @author <a href="mailto:vladb@genesyslab.com">Vladislav Baranovsky</a>
 * @author <a href="mailto:afilatov@genesyslab.com">Alexander Filatov</a>
 * @author <a href="mailto:abrazhny@genesyslab.com">Anton Brazhnyk</a>
 * @author <a href="mailto:svolokh@genesyslab.com">Sergii Volokh</a>
 */
public abstract class CfgObject
        extends CfgBase
        implements ICfgObject {

    /**
     * Backup copy of DOM data.
     */
    private ConfObject confObjectDataBackup = null;

    /**
     * Configuration object parameters out of DOM structure.
     */
    private KeyValueCollection      parameters;

    /**
     * String representation of configuration object path in the Configuration server.
     */
    private String                  objectPath;


    protected CfgObject(
            final IConfService confService,
            final ConfObjectBase objectData,
            final boolean isSaved,
            final Object[] additionalParameters) {
        super(confService, objectData, isSaved, null);

        this.parameters = null;
        if (additionalParameters != null) {
            if (additionalParameters[0] != null
                    && (additionalParameters[0] instanceof KeyValueCollection)) {
                this.parameters = (KeyValueCollection) additionalParameters[0];
            }

            if (additionalParameters[1] != null) {
                this.objectPath = additionalParameters[1].toString();
            }
        }

        ensureDataBackup();
    }

    /**
     * Main constructor designed for usage from generated classes in deserializing constructors.
     *
     * @param confService configuration service instance
     * @param xmlData bound data from Configuration SDK message
     * @param additionalParameters additional parameters from Configuration SDK message
     */
    protected CfgObject(
            final IConfService confService,
            final Node xmlData,
            final Object[] additionalParameters) {
        this(confService, parseDom(confService, xmlData, null), true, null);

        if (!((CfgDescriptionObject) getRawObjectData().getClassInfo()).getCfgEnum()
                .equals(getObjectType())) {
            throw new IllegalArgumentException(
                    "Configuration object and data structure types are incompatible:"
                    + getObjectType() + " for "
                    + getRawObjectData().getClassInfo().getName());
        }

        this.parameters = null;
        if (additionalParameters != null) {
            if (additionalParameters[0] != null
                    && (additionalParameters[0] instanceof KeyValueCollection)) {
                this.parameters = (KeyValueCollection) additionalParameters[0];
            }

            if (additionalParameters[1] != null) {
                this.objectPath = additionalParameters[1].toString();
            }
        }

        ensureDataBackup();
    }


    private static ConfObject parseDom(
            final IConfService confService,
            final Node xmlData,
            final ICfgObject parent) {
        ConfDataSaxHandler handler = new ConfDataSaxHandler(confService.getMetaData());
        SAXResult saxRes = new SAXResult(handler);

        try {
            XmlFactories.newTransformer().transform(new DOMSource(xmlData), saxRes);
        } catch (Exception e) {
            throw new ConfigRuntimeException("XML parsing exception", e);
        }

        Collection<ConfObjectBase> objs = handler.getParsedData();
        if (objs != null && !objs.isEmpty()) {
            return (ConfObject) objs.iterator().next();
        }


        throw new ConfigRuntimeException("Failed to find ConfObject in "
                + ((xmlData == null) ? "null" : ("<" + xmlData.getNodeName() + ">")));
    }


    /**
     * Returns the configuration object type.
     *
     * @return object type
     */
    public CfgObjectType getObjectType() {
        return ((CfgDescriptionObject) getMetaData()).getCfgEnum();
    }

    /**
     * Returns the dbid of the current object, or 0 if object has not been saved.
     *
     * @return Configuration object dbid or 0
     */
    public int getObjectDbid() {
        Integer dbid = (Integer) getProperty(MiscConstants.DbidPropertyName);
        if (dbid != null) {
            return dbid;
        }
        return 0;
    }

    /**
     * Returns path of the object in the Configuration Server.
     *
     * <p/>Empty or null value means that object read request was done without
     * {@link ICfgFilterBasedQuery#getDoRequestObjectPath()} option enabled, configuration server did not return
     * this value for some reason, or this multiple objects read response does not contain paths for
     * all the objects read.
     *
     * <p/><b><i>Note:</i></b> Be aware that to track moving of the object to different folder ("to different path")
     * in the configuration server, it is required to subscribe for
     * {@link com.genesyslab.platform.applicationblocks.com.objects.CfgFolder CfgFolder}'s changes
     * and look which {@link com.genesyslab.platform.applicationblocks.com.objects.CfgFolder CfgFolder}
     * got updated to contain this particular object.
     *
     * @return object path or null
     * @see ICfgFilterBasedQuery#getDoRequestObjectPath()
     */
    public final String getObjectPath() {
        return objectPath;
    }

    /**
     * This property specifies DBID of the folder, in which the object resides in the Configuration Server.
     *
     * <p/>Zero or null value means that object read request was done without
     * {@link ICfgFilterBasedQuery#getDoRequestFolderId()} option enabled, configuration server did not return
     * this value for some reason, or this multiple objects read response does not contain folders DBIDs for
     * all the objects read.
     *
     * <p/><b><i>Note:</i></b> Be aware that to track moving of the object to different folder
     * in the configuration server, it is required to subscribe for
     * {@link com.genesyslab.platform.applicationblocks.com.objects.CfgFolder CfgFolder}'s changes
     * and look which {@link com.genesyslab.platform.applicationblocks.com.objects.CfgFolder CfgFolder}
     * got updated to contain this particular member.
     *
     * @return folder DBID, 0 or null
     * @see ICfgFilterBasedQuery#getDoRequestFolderId()
     */
    public final Integer getFolderId() {
        if (parameters == null) {
            return 0;
        }

        return parameters.getInt(MiscConstants.FolderDbidName);
    }

    /**
     * This property specifies DBID of the folder, in which the object will reside in the Configuration Server.<br/>
     * This method is to be used while the object is been created, but not modified.
     * It can't be changed after object is created and saved in the configuration server.<br/>
     * To change the object's folder dbid ("move object to other folder"), there is following way:
     * read "target" {@link com.genesyslab.platform.applicationblocks.com.objects.CfgFolder CfgFolder},
     * add the new member and save it.
     *
     * @param dbid folder DBID.
     * @throws ConfigRuntimeException is object is saved.
     */
    public final void setFolderId(final Integer dbid) {
        if (isSaved()) {
            throw new ConfigRuntimeException(
                "Can't change this property because object has been saved.");
        }
        doSetFolderId(dbid);
    }

    protected final void doSetFolderId(final Integer dbid) {
        if (parameters == null) {
            parameters = new KeyValueCollection();
        } else if (parameters.containsKey(MiscConstants.FolderDbidName)) {
            parameters.remove(MiscConstants.FolderDbidName);
        }
        if (dbid != null) {
            parameters.addInt(MiscConstants.FolderDbidName, dbid);
        }
    }


    @Override
    protected void reloadObjectWithNewData(final ConfObject newObject) {
        super.reloadObjectWithNewData(newObject);
        resetDataBackup();
    }

    protected void reloadObjectWithNewData(
            final ConfObject newObject,
            final String objectPath,
            final Integer folderDbid) {
        reloadObjectWithNewData(newObject);
        if (folderDbid != null) {
            doSetFolderId(folderDbid);
        }
        if (objectPath != null) {
            this.objectPath = objectPath;
        }
    }


    /**
     * Synchronizes changes in a class with Configuration Server. Internally generates the correct delta
     * object and sends it to Configuration Server.
     *
     * @throws ConfigException exception while requesting configuration information update
     */
    public void save()
            throws ConfigException {
        save(getConfigurationService().getProtocol());
    }

    /**
     * Synchronizes changes in a class with Configuration Server.
     * Internally generates the correct delta object and sends it
     * to Configuration Server.
     *
     * @param protocol opened configuration protocol instance
     * @throws ConfigException exception while requesting configuration information update
     */
    private void save(final Protocol protocol)
            throws ConfigException {
        final Lock writeLock = lockObject().writeLock();
        writeLock.lock();

        try {
            flushChildrenContent();

            if (getLogger().isInfo()) {
                getLogger().infoFormat("Saving object... [{0}], Dbid: [{1}]",
                        new Object[] {getObjectType(), getObjectDbid()});
            }

            if (isSaved()) {
                updateSavedObject(protocol);
            } else {
                createNewObject(protocol);
            }

            if (getLogger().isInfo()) {
                getLogger().infoFormat(
                        "Save operation successful. [{0}], Dbid: [{1}]",
                        new Object[] {getObjectType(), getObjectDbid()});
            }

            resetDataBackup();

            if (getConfigurationService().getPolicy().getCacheOnSave(this)) {
                updateCacheOnSave();
            }

        } finally {
            writeLock.unlock();
        }
    }

    private void createNewObject(final Protocol protocol)
            throws ConfigException {
        ConfObject newObject = (ConfObject) getRawObjectData();

        if (getLogger().isDebug()) {
            getLogger().debugFormat(
                    "This is a new object and it will be created using the following XML:\r\n {0}",
                    newObject);
        }

        EventObjectCreated evCrt;
        try {
            evCrt = ProtocolOperationsHelper.createObject(
                    protocol, newObject, parameters);
        } catch (Exception ex) {
            if (getLogger().isInfo()) {
                getLogger().infoFormat(
                        "Unable to create object! [{0}], Dbid: [{1}]\r\n {2}",
                        new Object[] {getObjectType(), getObjectDbid(), ex});
            }
            if (ex instanceof ConfigException) {
                throw (ConfigException) ex;
            } else {
                throw new ConfigException("Unable to create object!", ex);
            }
        }

        newObject = evCrt.getObject();
        if (newObject != null) {
            reloadObjectWithNewData(newObject, null, evCrt.getFolderDbid());
            setIsSaved(true);
        }
    }


    private void updateSavedObject(final Protocol protocol)
            throws ConfigException {
        ConfDeltaUtility utils = new ConfDeltaUtility(
                getConfigurationService().getMetaData());

        ConfObjectDelta delta = null; 
        try {
            delta = utils.createDelta(confObjectDataBackup, (ConfObject) getRawObjectData());
        } catch (Exception ex) {
            getLogger().debug("Unable to create delta", ex);
            throw new ConfigException("Unable to update object!", ex);
        }

        if (getLogger().isDebug()) {
            getLogger().debugFormat(
                    "This is an update of an existing object "
                            + "and it will be updated using the following delta:\r\n {0}",
                    delta);
        }

        try {
            ProtocolOperationsHelper.updateObject(
                    protocol, delta, parameters);
        } catch (Exception ex) {
            if (getLogger().isInfo()) {
                getLogger().infoFormat(
                        "Unable to update object! [{0}], Dbid: [{1}]\r\n {2}",
                        new Object[] {getObjectType(), getObjectDbid(), ex});
            }
            if (ex instanceof ConfigException) {
                throw (ConfigException) ex;
            } else {
                throw new ConfigException("Unable to update object!", ex);
            }
        }
    }

    private void updateCacheOnSave()
                throws ConfigException {
        if (getLogger().isInfo()) {
            getLogger().infoFormat(
                    "Adding to cache. [{0}], Dbid: [{1}]",
                    new Object[] {getObjectType(), getObjectDbid()});
        }
        if (getConfigurationService().getCache() == null) {
            throw new NullPointerException("Cache is not initialized");
        }
        try {
            if (getConfigurationService().getCache().contains(this)) {
                getConfigurationService().getCache().update(this);
            } else {
                getConfigurationService().getCache().add(this);
            }
        } catch (Exception ex) {
            if (getLogger().isInfo()) {
                getLogger().infoFormat(
                        "Unable to add/update to cache! [{0}], Dbid [{1}]\r\n {2}",
                        new Object[] {getObjectType(), getObjectDbid(), ex});
            }
            // throw new ConfigException("Unable to add/update to cache", ex);
        }
    }


    /**
     * Deletes the configuration object from the Configuration Server.
     *
     * @throws ConfigException in case of exception on object remove request
     */
    public void delete()
            throws ConfigException {
        final Lock writeLock = lockObject().writeLock();
        writeLock.lock();

        try {
            try {
                delete(getConfigurationService().getProtocol());
            } catch (Exception ex) {
                if (ex instanceof ConfigException) {
                    throw (ConfigException) ex;
                } else {
                    throw new ConfigException("Exception on object delete operation", ex);
                }
            }

            setIsSaved(false);
            updateChildrenSavedState();

            // use 'super.' to avoid data backup creation
            super.setProperty(MiscConstants.DbidPropertyName, 0);

        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Deletes the configuration object from the Configuration Server.
     *
     * @param protocol configuration protocol instance
     * @throws ConfigException in case of exception on object remove request
     */
    private void delete(final Protocol protocol)
            throws ConfigException {
        int idObj = getObjectDbid();

        if (!isSaved()) {
            throw new ConfigException("This object has not been saved.");
        }

        if (idObj == 0) {
            throw new ConfigException("No DBID set");
        }

        EventObjectDeleted evDel = ProtocolOperationsHelper.deleteObject(
                protocol, getObjectType(), idObj);

        if (evDel != null) {
            confObjectDataBackup = null;

            if (getLogger().isInfo()) {
                getLogger().infoFormat(
                        "Deleted object [{0}], Dbid: [{1}] from Configuration Server.",
                        new Object[] {getObjectType(), getObjectDbid()});
            }

            if (getConfigurationService().getCache() != null) {
                if (getConfigurationService().getCache().getPolicy()
                        .getRemoveOnDelete(this)) {
                    getConfigurationService().getCache().remove(this);
                    if (getLogger().isInfo()) {
                        getLogger().infoFormat(
                                "Deleted object [{0}], Dbid: [{1}] from cache.",
                                new Object[] {getObjectType(), getObjectDbid()});
                    }
                }
            }
        }
    }

    /**
     * Retrieves the latest data from the Configuration Server and updates the object's data.
     *
     * @throws ConfigException exception while requesting configuration information
     */
    public void refresh()
            throws ConfigException {
        final Lock writeLock = lockObject().writeLock();
        writeLock.lock();

        try {
            getConfigurationService().refreshObject(this);
            resetDataBackup();

            if (getLogger().isInfo()) {
                getLogger().infoFormat(
                        "Refreshed object [{0}], Dbid: {1}.",
                        new Object[] {getObjectType(), getObjectDbid()});
            }

        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Updates the current object from the passed delta object.
     *
     * @param deltaObject the delta object received from configuration server
     */
    public void update(final ICfgDelta deltaObject) {
        if (deltaObject == null) {
            throw new NullPointerException("deltaObject");
        }
        // TODO check: we trying to apply delta on locally changed object
        //if (confObjectDataBackup != null) {
        //    throw new ConfigRuntimeException("Apply delta on modified object");
        //}
        ConfDeltaUtility util = new ConfDeltaUtility(
                getConfigurationService().getMetaData());

        final Lock writeLock = lockObject().writeLock();
        writeLock.lock();

        try {
            util.applyDelta(
                    (ConfObject) getRawObjectData(),
                    (ConfObjectDelta) deltaObject.getRawObjectData());

            reloadObjectWithNewData((ConfObject) getRawObjectData());

        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Retrieves a permission mask (see
     * {@link com.genesyslab.platform.configuration.protocol.types.CfgPermissions CfgPermissions} enumeration)
     * of accessing the object by a specified account (another configuration object).
     * For example, called on CfgTenant with CfgAgent as a parameter, returns the set of permissions for this
     * agent to access this tenant.
     *
     * @param accountObject account (can be any Configuration Server class).
     * @return Permissions mask.
     * @throws ConfigException exception while requesting configuration information
     * @see com.genesyslab.platform.configuration.protocol.types.CfgPermissions
     * @see #retrieveACL()
     * @see #updateACL(CfgObject, Integer, boolean)
     */
    public int retrieveAccountPermissions(final CfgObject accountObject)
            throws ConfigException {
        if (accountObject == null) {
            throw new NullPointerException("accountObject can't be null");
        }

        Integer accountObjDbid = (Integer)accountObject.getProperty(MiscConstants.DbidPropertyName);
        if (accountObjDbid == null) {
            throw new IllegalArgumentException("argument accountObject doesn't contain DBID property");
        }
        Integer objDbid = (Integer) getProperty(MiscConstants.DbidPropertyName);
        if (objDbid == null) {
            throw new NullPointerException("ConfObject DBID");
        }
        
		return ProtocolOperationsHelper.readObjectPermissions(
                getConfigurationService(),
                objDbid,
                getObjectType(),
                accountObjDbid,
                accountObject.getObjectType()
        );
    }

    /**
     * Retrieves a full list of permissions to access this object for all user accounts.
     *
     * @return A list of PermissionDescriptor objects
     * @throws ConfigException exception while requesting configuration information
     * @deprecated
     * @see #retrieveACL()
     * @see #retrieveAccountPermissions(CfgObject)
     */
    @Deprecated
    public List<PermissionDescriptor> retrievePermissions()
            throws ConfigException {
        return com.genesyslab.platform.applicationblocks.com.runtime.PermissionDescriptorImpl
                .wrap(ProtocolOperationsHelper.readPermissions(
                        getConfigurationService(),
                        getObjectDbid(),
                        getObjectType()));
    }

    /**
     * Retrieves a full list of permissions to access this object for all user accounts.
     *
     * @return Structure with a list of permissions descriptions.
     * @throws ConfigException exception while requesting configuration information.
     * @see #retrieveAccountPermissions(CfgObject)
     * @see #updateACL(CfgObject, Integer, boolean)
     */
    public CfgACL retrieveACL()
            throws ConfigException {
        return new CfgACL(getConfigurationService(),
                ProtocolOperationsHelper.readPermissions(
                        getConfigurationService(),
                        getObjectDbid(),
                        getObjectType()),
                null);
    }

    /**
     * Changes the permissions on this object for the specified account.
     *
     * @param accountObject Account (can be any Configuration Server class)
     * @param newPermissionsMask New permissions mask or null. See
     *           {@link com.genesyslab.platform.configuration.protocol.types.CfgPermissions CfgPermissions}
     *           enumeration. <code>Null</code> means that the account record should be removed from the ACL.
     * @param recursive This flag, if set to <code>true</code>, directs Configuration Server to remove permissions
     *           at the objects subordinate to the one specified in the <i>accountObject</i> parameter.
     *           This way all the subordinate objects will receive permissions identical
     *           to their parent object. However, this is not the way to set up an individual
     *           permission recursively. For this purpose the NoPropagate flag
     *           in the permission record should be used. The flag is only relevant
     *           to the following object types: CfgFolder, CfgTenant, CfgSwitch, CfgIVR, CfgEnumerator.
     * @return <code>true</code> if ACL was updated, or <code>false</code> if no change was made.
     * @throws ConfigException exception while requesting configuration information update
     * @see com.genesyslab.platform.configuration.protocol.types.CfgPermissions
     * @see #retrieveACL()
     * @see #retrieveAccountPermissions(CfgObject)
     */
    public boolean updateACL(
                final CfgObject accountObject,
                final Integer newPermissionsMask,
                final boolean recursive)
            throws ConfigException {
        if (accountObject == null) {
            throw new NullPointerException("accountObject can't be null");
        }

        int dbidObj = getObjectDbid();
        if (dbidObj == 0) {
            throw new ConfigRuntimeException("Config object has not been saved");
        }

        int dbidAccount = accountObject.getObjectDbid();
        if (dbidAccount == 0) {
            throw new ConfigRuntimeException("Account object has not been saved");
        }

        return ProtocolOperationsHelper.updatePermissions(
            getConfigurationService(),
            dbidObj, getObjectType(),
            dbidAccount, accountObject.getObjectType(),
            newPermissionsMask,
            recursive);
    }

    /**
     * Removes the specified account from the current object's access list.<br/>
     * This method is deprecated - use <code>
     * {@link #updateACL(com.genesyslab.platform.applicationblocks.com.CfgObject, java.lang.Integer, boolean)
     * updateACL(accountObject, <b>null</b>, recursive)}</code>
     * instead of it.
     *
     * @param accountObject the account object to remove from the access list
     * @param recursive specifies whether to remove the account recursively from child objects
     * @throws ConfigException exception while updating configuration information
     * @deprecated
     * @see #updateACL(CfgObject, Integer, boolean)
     */
    @Deprecated
    public void removeAccount(final CfgObject accountObject, final boolean recursive)
            throws ConfigException {
        updateACL(accountObject, null, recursive);
    }

    /**
     * Changes the permissions on this object from the specified account.  This method
     * sets the permissions recursively on all child objects by default.
     *
     * @param accountObject Account (can be any Configuration Server class)
     * @param newPermissionsMask New permissions mask. See
     *           {@link com.genesyslab.platform.configuration.protocol.types.CfgPermissions CfgPermissions}
     *           enumeration.
     * @throws ConfigException exception while requesting configuration information update
     * @see com.genesyslab.platform.configuration.protocol.types.CfgPermissions
     * @deprecated
     * @see #updateACL(CfgObject, Integer, boolean)
     */
    @Deprecated
    public void setAccountPermissions(final CfgObject accountObject, final int newPermissionsMask)
            throws ConfigException {
        updateACL(accountObject, newPermissionsMask, true);
    }

    /**
     * Changes the permissions on this object from the specified account.
     *
     * @param accountObject Account (can be any Configuration Server class)
     * @param newPermissionsMask New permissions mask. See
     *           {@link com.genesyslab.platform.configuration.protocol.types.CfgPermissions CfgPermissions}
     *           enumeration.
     * @param recursive This flag, if set to TRUE, directs Configuration Server to remove permissions
     *           at the objects subordinate to the one specified in the <i>accountObject</i> parameter.
     *           This way all the subordinate objects will receive permissions identical
     *           to their parent object. However, this is not the way to set up an individual
     *           permission recursively. For this purpose the NoPropagate flag
     *           in the permission record should be used. The flag is only relevant
     *           to the following object types: CfgFolder, CfgTenant, CfgSwitch, CfgIVR, CfgEnumerator
     * @throws ConfigException exception while requesting configuration information update
     * @see com.genesyslab.platform.configuration.protocol.types.CfgPermissions
     * @deprecated
     * @see #updateACL(CfgObject, Integer, boolean)
     */
    @Deprecated
    public void setAccountPermissions(
                final CfgObject accountObject,
                final int newPermissionsMask,
                final boolean recursive)
            throws ConfigException {
        updateACL(accountObject, newPermissionsMask, recursive);
    }


    /**
     * This internal method is designed for "lazy initialization" of DOM data backup.
     * It is automatically called before any object property change and it has to
     * create backup copy of object DOM data if it has not been created before.
     */
    @Override
    protected void ensureDataBackup() {
        if ((confObjectDataBackup == null) && isSaved()) {
            confObjectDataBackup = (ConfObject) getRawObjectData().clone();
        }
    }

    protected void resetDataBackup() {
        if (isSaved()) {
            confObjectDataBackup = (ConfObject) getRawObjectData().clone();
        } else {
            confObjectDataBackup = null;
        }
    }


    @Override
    public CfgObject clone()
            throws CloneNotSupportedException {

        final Lock writeLock = lockObject().writeLock();
        writeLock.lock();

        try {
            CfgObject ret = (CfgObject) super.clone();

            if (confObjectDataBackup != null) { // TODO no need?
                ret.confObjectDataBackup = (ConfObject) confObjectDataBackup.clone();
            }
            if (parameters != null) { // TODO no need?
                ret.parameters = (KeyValueCollection) parameters.clone();
            }

            return ret;

        } finally {
            writeLock.unlock();
        }
    }
}

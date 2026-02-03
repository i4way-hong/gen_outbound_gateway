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

import com.genesyslab.platform.applicationblocks.com.runtime.*;

import com.genesyslab.platform.configuration.protocol.metadata.*;

import com.genesyslab.platform.configuration.protocol.types.CfgObjectType;
import com.genesyslab.platform.configuration.protocol.types.CfgObjectProperty;
import com.genesyslab.platform.configuration.protocol.xml.ConfDataSource;

import com.genesyslab.platform.configuration.protocol.obj.ConfObject;
import com.genesyslab.platform.configuration.protocol.obj.ConfStructure;
import com.genesyslab.platform.configuration.protocol.obj.ConfObjectBase;
import com.genesyslab.platform.configuration.protocol.obj.ConfDataCollection;
import com.genesyslab.platform.configuration.protocol.obj.ConfStringCollection;
import com.genesyslab.platform.configuration.protocol.obj.ConfIntegerCollection;
import com.genesyslab.platform.configuration.protocol.obj.ConfStructureCollection;

import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.Protocol;

import com.genesyslab.platform.commons.xmlfactory.XmlFactories;

import com.genesyslab.platform.commons.GEnum;
import com.genesyslab.platform.commons.collections.KeyValueCollection;

import com.genesyslab.platform.commons.log.Log;
import com.genesyslab.platform.commons.log.ILogger;

import javax.xml.transform.dom.DOMResult;

import org.w3c.dom.Node;
import org.w3c.dom.Document;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import java.util.*;
import java.util.Map.Entry;


/**
 * The parent class for all configuration objects categories including
 * configuration objects child structures and classes.
 *
 * @author <a href="mailto:makagon@genesyslab.com">Petr Makagon</a>
 * @author <a href="mailto:vladb@genesyslab.com">Vladislav Baranovsky</a>
 * @author <a href="mailto:afilatov@genesyslab.com">Alexander Filatov</a>
 * @author <a href="mailto:abrazhny@genesyslab.com">Anton Brazhnyk</a>
 * @author <a href="mailto:svolokh@genesyslab.com">Sergii Volokh</a>
 */
public abstract class CfgBase
        implements ICfgBase {

    /**
     * Reference to the configuration cfgService to use for updating this object.
     */
    private IConfService configurationService;

    private ConfObjectBase confObjectData = null;
    private HashMap<String, Object> confObjectChildren = null;

    /**
     * The parent object of the current object/structure.
     */
    private ICfgObject parent;

    private boolean isSaved;


    private CfgDescriptionClass dataClass;

    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();


    /**
     * Private logger for this class.
     */
    private static final ILogger log = Log.getLogger(CfgBase.class);


    /**
     * This constructor is used from particular wrapping objects and structures
     * for data initialization from external DOM structure.
     * Usually it is data from the Configuration Protocol.
     *
     * @param confService       a reference to Configuration Service
     * @param confObjectData    tree with configuration object data
     * @param isSaved           saved object state flag
     * @param parent            for a structure, a reference to its parent
     */
    protected CfgBase(
            final IConfService   confService,
            final ConfObjectBase confObjectData,
            final boolean        isSaved,
            final ICfgObject     parent) {

        this.configurationService = confService;
        this.isSaved = isSaved;

        initStruct(confObjectData, parent);
    }


    protected void initStruct(
            final ConfObjectBase confObjectData,
            final ICfgObject     parent) {
        this.parent = parent;

        this.confObjectData = confObjectData;
        this.dataClass = this.confObjectData.getClassInfo();

        this.confObjectChildren = null;
    }


    protected final ILogger getLogger() {
        return log;
    }

    protected final ReadWriteLock lockObject() {
        return rwLock;
    }

    /**
     * Used to access the configuration cfgService that this object was
     * created with.
     *
     * @return reference to the configuration service.
     */
    public final IConfService getConfigurationService() {
        return configurationService;
    }

    /**
     * Provides the location of the server to which this object belongs.
     *
     * @return the configuration server active endpoint.
     */
    public final Endpoint getEndpoint() {
        if (configurationService == null) {
            return null;
        }
        Protocol proto = configurationService.getProtocol();
        if (proto == null) {
            return null;
        }
        return proto.getEndpoint();
    }

    /**
     * Used to represent configuration object or structure to XML.
     * Method returns copy of actual object properties values, so it can be used directly.
     * <code><pre>
     *   Node newNode = cfgObject.toXml();
     *   doc.adoptNode(newNode);
     *   doc.appendChild(newNode);
     * </pre></code>
     *
     * @return DOM node containing object/structure elements values.
     */
    public Node toXml() {
        final Lock readLock = lockObject().readLock();
        readLock.lock();

        try {
            flushChildrenContent();

            ConfDataSource source = new ConfDataSource(confObjectData);

            try {
                DOMResult domTgt = new DOMResult(XmlFactories.newDocumentBuilderNS().newDocument());
                XmlFactories.newTransformer().transform(source, domTgt);
                return ((Document) domTgt.getNode()).getDocumentElement().getFirstChild();
            } catch (Exception e) {
                throw new ConfigRuntimeException(
                        "Exception while XML creation", e);
            }

        } finally {
            readLock.unlock();
        }
    }

    /**
     * Used to retrieve reference to the parent of the current configuration object structure.
     *
     * @return reference to the parent object of this structure, this object by itself, or null.
     */
    protected ICfgObject getParent() {
        return parent;
    }

    /**
     * Positive object saved state means that the object is read from the server or
     * it is locally created and then successfully saved.
     * Local modifications of the objects properties does not affect this state -
     * object "is saved", but "is not updated on the server" in this case.
     *
     * @return true if object is saved in the configuration server.
     */
    public final boolean isSaved() {
        return isSaved;
    }

    /**
     * Package internal method to set new saved state of the object.
     *
     * @param savedState new saved state.
     * @see #isSaved()
     */
    final void setIsSaved(final boolean savedState) {
        this.isSaved = savedState;
    }


    /**
     * Internal method to get DOM representation of the object.<br/>
     *
     * <b><i>Note:</i></b> Starting from Platform SDK 8.5.0 version it returns
     * DOM copy of the actual data structures, but not original values content.
     * So, changes in its content will not affect actual object properties values.
     *
     * @return DOM data object.
     * @deprecated
     * @see #toXml()
     */
    @Deprecated
    public final Node getData() {
        return toXml();
    }

    /**
     * Returns reference to the underlying configuration protocol object structure.
     *
     * <p/><b><i>Note:</i></b> It is designed for internal usage.<br/>
     * Any modifications in the underlying structures may bring inconsistency
     * with covering COM AB classes.
     *
     * @return original configuration protocol object instance reference.
     */
    public ConfObjectBase getRawObjectData() {
        return confObjectData;
    }

    protected void reloadObjectWithNewData(
            final ConfObject newObject) {
        if (confObjectChildren != null) {
            confObjectChildren.clear();
        }
        confObjectData = newObject;
    }

    protected void flushChildrenContent() {
        ensureDataBackup();

        if (confObjectChildren != null) {
            for (Entry<String, Object> entry: confObjectChildren.entrySet()) {
                Object obj = entry.getValue();

                if (obj instanceof CfgBase) {
                    ((CfgBase) obj).flushChildrenContent();

                } else if (obj instanceof IChildStructureWrapper) {
                    ((IChildStructureWrapper) obj).flush();
                }
            }
        }
    }

    /**
     * Internal method to provide XML data manipulation functions with information about
     * objects' properties. This description is specific for each of configuration
     * object class.
     *
     * @return internal structure with object type properties
     */
    public final CfgDescriptionClass getMetaData() {
        return dataClass;
    }


    protected Integer getLinkValue(
            final String propertyName) {
        return (Integer) getProperty(null, propertyName, false);
    }

    @SuppressWarnings("unchecked")
    public Collection<Integer> getLinkListCollection(
            final String propertyName) {
        Collection<Integer> ret = (Collection<Integer>) getProperty(
                null, propertyName, false);
        if (ret != null && !ret.isEmpty()) {
            return ret;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private Object getPropertyImpl(
            final ConfObjectBase confStruct,
            final CfgDescriptionAttribute attrInfo,
            final Class<?> retClass,
            final boolean resolveLinks)
                    throws ConfigException {

        if (attrInfo.isCfgType(CfgTypeMask.List)) {
            if (attrInfo.isCfgType(CfgTypeMask.KvList)) {
                Object value = confStruct.getPropertyValue(attrInfo.getIndex());
                if (value == null && !dataClass.isCfgType(CfgTypeMask.Delta)) {
                    value = new KeyValueCollection();
                    confStruct.setPropertyValue(attrInfo.getIndex(), value);
                }
                return value;

            } else if (attrInfo.isCfgType(CfgTypeMask.Link)) {
                ConfIntegerCollection value = (ConfIntegerCollection)
                        confStruct.getPropertyValue(attrInfo.getIndex());

                if (resolveLinks) {
                    if (value == null && !dataClass.isCfgType(CfgTypeMask.Delta)) {
                        value = new ConfIntegerCollection(
                                (CfgDescriptionAttributeReference) attrInfo);
                        confStruct.setPropertyValue(attrInfo.getIndex(), value);
                    }
                    if (value != null) {
                        return PropertiesAccessHelper.resolveLinksList(
                                CfgBase.this, confStruct, attrInfo);
                    }
                }

                return value;

            } else if (attrInfo.isCfgType(CfgTypeMask.Integer)) {
                ConfIntegerCollection value = (ConfIntegerCollection)
                        confStruct.getPropertyValue(attrInfo.getIndex());

                if (value != null) {
                    if (dataClass.isCfgType(CfgTypeMask.Delta)
                            && ((CfgDescriptionAttributeReference) attrInfo)
                                    .getCfgClass().isCfgType(CfgTypeMask.Structure)) {
                        CfgStructListWrapper<CfgStructure> wrapper = null;
                        if (confObjectChildren != null) {
                            wrapper = (CfgStructListWrapper<CfgStructure>)
                                    confObjectChildren.get(attrInfo.getSchemaName());
                        }
                        if (wrapper != null) {
                            return wrapper;
                        }

                        CfgBase structParent = CfgBase.this;
                        while (structParent.parent instanceof CfgBase) {
                            structParent = (CfgBase) structParent.parent;
                        }
                        wrapper = new CfgStructListWrapper<CfgStructure>(
                                confStruct, attrInfo, structParent);
                        if (confObjectChildren == null) {
                            confObjectChildren = new HashMap<String, Object>();
                        }
                        confObjectChildren.put(attrInfo.getSchemaName(), wrapper);
                        return wrapper;
                    }

                } else if (!dataClass.isCfgType(CfgTypeMask.Delta)) {
                    value = new ConfIntegerCollection(
                            (CfgDescriptionAttributeReference) attrInfo);
                    confStruct.setPropertyValue(attrInfo.getIndex(), value);
                }

                return value;

            } else if (attrInfo.isCfgType(CfgTypeMask.Structure)) {
                CfgStructListWrapper<CfgStructure> wrapper = null;
                if (confObjectChildren != null) {
                    wrapper = (CfgStructListWrapper<CfgStructure>)
                            confObjectChildren.get(attrInfo.getSchemaName());
                }
                if (wrapper != null) {
                    return wrapper;
                }

                ConfStructureCollection value = (ConfStructureCollection)
                        confStruct.getPropertyValue(attrInfo.getIndex());

                if (value == null && !dataClass.isCfgType(CfgTypeMask.Delta)) {
                    value = new ConfStructureCollection(
                            (CfgDescriptionAttributeReferenceClass) attrInfo);
                }

                if (value != null) {
                    CfgBase structParent = CfgBase.this;
                    while (structParent.parent instanceof CfgBase) {
                        structParent = (CfgBase) structParent.parent;
                    }
                    wrapper = new CfgStructListWrapper<CfgStructure>(
                            confStruct, attrInfo, structParent);
                    if (confObjectChildren == null) {
                        confObjectChildren = new HashMap<String, Object>();
                    }
                    confObjectChildren.put(attrInfo.getSchemaName(), wrapper);
                    return wrapper;
                }

                return null;

            } else if (attrInfo.isCfgType(CfgTypeMask.String)) {
                if (dataClass.isCfgType(CfgTypeMask.Delta)
                        && ((CfgDescriptionAttributeReference) attrInfo)
                                .getCfgClass().isCfgType(CfgTypeMask.Structure)) {
                    ConfStringCollection value = (ConfStringCollection)
                            confStruct.getPropertyValue(attrInfo.getIndex());
                    CfgStructListWrapper<ICfgStructure> wrapper = null;

                    if (value != null) {
                        CfgBase structParent = CfgBase.this;
                        while (structParent.parent instanceof CfgBase) {
                            structParent = (CfgBase) structParent.parent;
                        }
                        wrapper = new CfgStructListWrapper<ICfgStructure>(
                                confStruct, attrInfo, structParent);
                        if (confObjectChildren == null) {
                            confObjectChildren = new HashMap<String, Object>();
                        }
                        confObjectChildren.put(attrInfo.getSchemaName(), wrapper);
                    }

                    return wrapper;

                } else {
                    ConfStringCollection value = (ConfStringCollection)
                            confStruct.getPropertyValue(attrInfo.getIndex());

                    if (value == null && !dataClass.isCfgType(CfgTypeMask.Delta)) {
                        value = new ConfStringCollection(
                                (CfgDescriptionAttributeReference) attrInfo);
                    }

                    return value;
                }
            }

        } else if (attrInfo.isCfgType(CfgTypeMask.Class)) {
            CfgBase wrapper = null;
            if (confObjectChildren != null) {
                wrapper = (CfgBase) confObjectChildren.get(attrInfo.getSchemaName());
            }
            if (wrapper != null) {
                return wrapper;
            }
            Object value = confStruct.getPropertyValue(attrInfo.getIndex());
            if (value != null && attrInfo.isCfgType(CfgTypeMask.Structure)) {
                wrapper = CfgObjectActivator.createInstance(configurationService,
                        (ConfStructure) value, new Object[] {parent == null ? CfgBase.this : parent});
            }
            if (wrapper != null) {
                if (confObjectChildren == null) {
                    confObjectChildren = new HashMap<String, Object>();
                }
                confObjectChildren.put(attrInfo.getSchemaName(), wrapper);
            }

            return wrapper;

        } else if (attrInfo.isCfgType(CfgTypeMask.Link)) {
            Object value = confStruct.getPropertyValue(attrInfo.getIndex());

            if (resolveLinks && value != null && retClass != null
                    && ICfgObject.class.isAssignableFrom(retClass)) {
                int refDbid = 0;
                if (value instanceof Integer) {
                    refDbid = (Integer) value;
                }
                if (refDbid != 0) {
                    value = resolveLink(
                            ((CfgDescriptionAttributeReferenceLink) attrInfo).getObjectType(),
                            refDbid);
                } else {
                    value = null;
                }
            }

            return value;

        } else if (attrInfo.isCfgType(CfgTypeMask.Enum)) {
            Object value = confStruct.getPropertyValue(attrInfo.getIndex());

            if (value != null && retClass != null && GEnum.class.isAssignableFrom(retClass)) {
                if (value instanceof Integer) {
                    GEnum enumVal = GEnum.getValue(retClass, (Integer) value);
                    if (enumVal != null) {
                        return enumVal;
                    }

                } else if (!(value instanceof GEnum)) {
                    throw new ConfigRuntimeException(
                            "Invalid enumeration attribute type of "
                            + attrInfo.getName() + ": " + value.getClass().getName());
                }
            }

            return value;

        } else if (attrInfo.isCfgType(CfgTypeMask.Time)) {
            Object value = confStruct.getPropertyValue(attrInfo.getIndex());
            if (value != null) {
                if (value.getClass() == Integer.class) {
                    Calendar data = Calendar.getInstance();
                    data.setTimeInMillis(1000L * ((Integer) value).longValue());
                    value = data;
                }
            }
            return value;

        } else if (attrInfo.isCfgType(CfgTypeMask.Primitive)) {
            return confStruct.getPropertyValue(attrInfo.getIndex());
        }

        throw new RuntimeException("Attribute type " + attrInfo.getTypeMaskString()
                + " is not handled");
    }

    private Object getPropertyImpl(
            final Class<?> retClass,
            final String propertyName,
            final boolean resoveLinks)
                    throws ConfigException {
        CfgDescriptionAttribute attrInfo = dataClass.getAttribute(propertyName);

        if (attrInfo != null) {
            return getPropertyImpl(confObjectData, attrInfo, retClass, resoveLinks);
        }

        if (dataClass.isCfgType(CfgTypeMask.Delta)) {
            attrInfo = ((ICfgClassOperationalInfo) dataClass)
                    .getSubjectClassDescription().getAttribute(propertyName);
            if (attrInfo != null) {
                if (dataClass.isCfgType(CfgTypeMask.Object) || dataClass.isCfgType(CfgTypeMask.Group)) {
                    ConfObjectBase deltaObj = (ConfObjectBase) confObjectData.getPropertyValue(0);
                    if (deltaObj != null) {
                        return getPropertyImpl(deltaObj, attrInfo, retClass, resoveLinks);
                    }
                    return null;
                }
                return null;
            }
        }

        // This is the case of Cfg groups and brief info structures.
        // They don't have DBID, it is located in their child,
        // CFG group or ID (for brief infos).
        if (dataClass.isCfgType(CfgTypeMask.Object)
                && propertyName.equals(MiscConstants.DbidPropertyName)) {
            return ((ConfObject) confObjectData).getObjectDbid();
        }

        throw new ConfigRuntimeException(
                "There is no property '" + propertyName
                        + "' in class " + dataClass.getName() + " in current schema version.");
    }

    /**
     * This method is used to get any configuration object's property using its
     * string name.
     *
     * @param propertyName The name of the property
     * @return property value
     */
    public Object getProperty(final String propertyName) {
        return getProperty(null, propertyName, true);
    }

    /**
     * This method is used to get any configuration object's property using its
     * string name.
     *
     * @param retClass class of return object - used to handle configuration
     *                 protocol enumeration types Conf* (extending GEnum)
     * @param propertyName The name of the property
     * @return property value instantiated as typified object
     */
    protected Object getProperty(
            final Class<?> retClass,
            final String propertyName) {
        return getProperty(retClass, propertyName, true);
    }

    /**
     * This method is used to get any configuration object's property using its
     * string name.
     *
     * @param retClass class of return object - used to handle configuration
     *                 protocol enumeration types Conf* (extending GEnum)
     * @param propertyName The name of the property
     * @return property value instantiated as typified object
     */
    protected Object getProperty(
            final Class<?> retClass,
            final String propertyName,
            final boolean resolveLinks) {
        Object tempObject = null;

        final Lock readLock = lockObject().readLock();
        readLock.lock();

        try {
            tempObject = getPropertyImpl(retClass, propertyName, resolveLinks);
        } catch (ConfigException e) {
            throw new ConfigRuntimeException(
                    "Error getting property value for '" + propertyName + "'", e);
        } finally {
            readLock.unlock();
        }

        return tempObject;
    }

    /**
     * This method is used to set any configuration object's property using its
     * string name. Any changes will not be synchronized with Configuration Server
     * until the save() method is called.
     *
     * @param propertyName The name of the property
     * @param value New property value
     * @throws NullPointerException if property new value is null.
     * @throws ConfigRuntimeException if there is some problem with the attribute or value for it
     *             (see message or inner exception for details).
     */
    public void setProperty(final String propertyName, final Object value) {
        if (value == null) {
            throw new IllegalArgumentException("New attribute value is null");
        }

        final Lock writeLock = lockObject().writeLock();
        writeLock.lock();

        try {
            ensureDataBackup();

            setPropertyImpl(propertyName, value);
        } catch (ConfigException e) {
            throw new ConfigRuntimeException(
                    "Error setting property value for '" + propertyName + "'", e);
        } finally {
            writeLock.unlock();
        }
    }

    private void setPropertyImpl(
            final String propertyName,
            final Object propertyValue)
                    throws ConfigException {

        CfgDescriptionAttribute attrInfo = dataClass.getAttribute(propertyName);

        if (attrInfo != null) {
            setPropertyImpl(confObjectData, attrInfo, propertyValue);
            return;
        }

        if (dataClass.isCfgType(CfgTypeMask.Delta)) {
            attrInfo = ((ICfgClassOperationalInfo) dataClass)
                    .getSubjectClassDescription().getAttribute(propertyName);
            if (attrInfo != null) {
                ConfObjectBase deltaObj = (ConfObjectBase) confObjectData.getPropertyValue(0);
                if (deltaObj != null) {
                    setPropertyImpl(deltaObj, attrInfo, propertyValue);
                } // TODO create child?
                return;
            }
        }

        // This is the case of Cfg groups and brief info structures.
        // They don't have DBID, it is located in their child,
        // CFG group or ID (for brief infos).
        if (dataClass.isCfgType(CfgTypeMask.Object)
                && propertyName.equals(MiscConstants.DbidPropertyName)) {
            if (dataClass.isCfgType(CfgTypeMask.BriefInfo)) {
                CfgBase id = (CfgBase) getPropertyImpl(null, "ID", false);
                if (id != null) {
                    id.setPropertyImpl(propertyName, propertyValue);
                }
                return;
            }
            if (dataClass.isCfgType(CfgTypeMask.Group)) {
                CfgDescriptionAttribute attrGroupInfo = dataClass.getAttribute(propertyName);
                if (dataClass.isCfgType(CfgTypeMask.Delta)) {
                    attrGroupInfo = dataClass.getAttribute("deltaGroupInfo");
                    //if (attrGroupInfo != null) {
                    //    attrGroupInfo = 
                    //}
                } else {
                    attrGroupInfo = dataClass.getAttribute("groupInfo");
                }
                if (attrGroupInfo != null) {
                    ConfObjectBase groupInfo = (ConfObjectBase) confObjectData.getOrCreatePropertyValue(
                            attrGroupInfo.getSchemaName());
                    groupInfo.setPropertyValue(propertyName, propertyValue);
                    return;
                }
            }
        }

        throw new ConfigRuntimeException(
                "There is no property '" + propertyName
                        + "' in this class in current schema version.");
    }

    private void setPropertyImpl(
            final ConfObjectBase confStruct,
            final CfgDescriptionAttribute attrInfo,
            final Object value) {

        if (attrInfo.isCfgType(CfgTypeMask.List)) {
            if (attrInfo.isCfgType(CfgTypeMask.KvList)) {
                confStruct.setPropertyValue(attrInfo.getIndex(), value);

            } else if (attrInfo.isCfgType(CfgTypeMask.Link)) {
                Object newValue = null;

                if (value instanceof ConfIntegerCollection) {
                    newValue = (ConfIntegerCollection) value;

                } else if (value instanceof Collection) {
                    ConfIntegerCollection ids = new ConfIntegerCollection(
                            (CfgDescriptionAttributeReference) attrInfo);
                    for (Object elem: (Collection<?>) value) {
                        if (elem == null) {
                            continue;
                        }
                        if (elem.getClass() == Integer.class) {
                            ids.add((Integer) elem);
                        } else if (elem instanceof ICfgObject) {
                            ids.add(((ICfgObject) elem).getObjectDbid());
                        } else {
                            throw new IllegalArgumentException(
                                    "Illegal value type of a link value in the list object property: "
                                    + elem.getClass());
                        }
                    }
                    newValue = ids;

                } else if (value != null) {
                    throw new IllegalArgumentException(
                            "Illegal value type for a link list object property: "
                            + value.getClass());
                }

                confStruct.setPropertyValue(attrInfo.getIndex(), newValue);

            } else if (attrInfo.isCfgType(CfgTypeMask.Structure)) {
                if (confObjectChildren != null) {
                    confObjectChildren.remove(attrInfo.getSchemaName());
                }
                ConfDataCollection<ConfStructure> newValue = null;

                if (value instanceof ConfStructureCollection) {
                    newValue = (ConfStructureCollection) value;

                } else if (value instanceof Collection) {//StructuresBroker) {
                    newValue = new ConfStructureCollection(
                            (CfgDescriptionAttributeReferenceClass) attrInfo);
                    @SuppressWarnings("unchecked")
                    Collection<CfgStructure> cValue = (Collection<CfgStructure>) value;
                    for (CfgStructure elem: cValue) {
                        newValue.add((ConfStructure) elem.getRawObjectData());
                    }

                } else if (value != null) {
                    throw new IllegalArgumentException(
                            "Illegal value type for a structures list object property: "
                            + value.getClass());
                }

                confStruct.setPropertyValue(attrInfo.getIndex(), newValue);
            }

        } else if (attrInfo.isCfgType(CfgTypeMask.Class)) {
            if (confObjectChildren != null) {
                confObjectChildren.remove(attrInfo.getSchemaName());
            }

            ConfObjectBase newValue = null;
            if (value instanceof CfgBase) {
                newValue = ((CfgBase) value).getRawObjectData();

            } else if (value instanceof ConfObjectBase) {
                newValue = (ConfObjectBase) value;

            } else if (value != null) {
                throw new IllegalArgumentException(
                        "Illegal value type for a structures type property: "
                        + value.getClass());
            }

            confStruct.setPropertyValue(attrInfo.getIndex(), newValue);

        } else if (attrInfo.isCfgType(CfgTypeMask.Link)) {
            Integer newValue = null;

            if (value instanceof Integer) {
                newValue = (Integer) value;

            } else if (value instanceof ICfgObject) {
                newValue = ((ICfgObject) value).getObjectDbid();

            } else if (value != null) {
                throw new IllegalArgumentException(
                        "Illegal value type for a link type property: "
                        + value.getClass());
            }

            confStruct.setPropertyValue(attrInfo.getIndex(), newValue);

        } else if (attrInfo.isCfgType(CfgTypeMask.Primitive)) {
            confStruct.setPropertyValue(attrInfo.getIndex(), value);

        } else {
            throw new RuntimeException("Attribute type " + attrInfo.getTypeMaskString()
                    + " is not handled");
        }
    }


    protected ICfgObject resolveLink(
            final CfgObjectType objectType,
            final int dbid)
                    throws ConfigException {
        ICfgObject obj = null;

        if (configurationService.getPolicy()
                .getAttemptLinkResolutionThroughCache(objectType)) {
            if (log.isDebug()) {
                log.debugFormat(
                        "Resolving link through cache... [{0}], dbid: [{1}]",
                        new Object[] {objectType, dbid});
            }
            obj = configurationService.getCache()
                    .retrieve(ICfgObject.class, objectType, dbid);
        }

        if (obj == null) {
            if (log.isDebug()) {
                log.debugFormat(
                        "Resolving link through configuration server... "
                                + "[{0}], dbid: [{1}]",
                        new Object[] {objectType, dbid});
            }
            try {
                obj = configurationService.retrieveObject(
                        objectType, dbid);
            } catch (ConfigException ex) {
                if (log.isError()) {
                    log.error("Exception on linked object retrieve", ex);
                }
                throw new ConfigRuntimeException(
                        "Exception on linked object retrieve", ex);
            }
        }

        return obj;
    }


    /**
     * This internal method is designed for "lazy initialization" of DOM data backup.
     * It is automatically called before any object property change and it has to
     * create backup copy of object DOM data if it has not been created before.
     */
    protected void ensureDataBackup() {
        if (parent instanceof CfgBase) {
            ((CfgBase) parent).ensureDataBackup();
        }
    }

    protected void updateChildrenSavedState() {
        if (confObjectChildren != null) {
            for (CfgDescriptionAttribute info : dataClass.getAttributes()) {
                if (info.isCfgType(CfgTypeMask.Class)) {
                    if (info.isCfgType(CfgTypeMask.List)) {
                        Collection<?> structureCollection =
                                (Collection<?>) confObjectChildren.get(
                                        info.getSchemaName());

                        if (structureCollection != null) {
                            for (Object item : structureCollection) {
                                ((CfgBase) item).setIsSaved(this.isSaved());
                            }
                        }
                    } else {
                        CfgBase structure = (CfgBase) confObjectChildren.get(
                                info.getSchemaName());

                        if (structure != null) {
                            structure.setIsSaved(this.isSaved());
                        }
                    }
                }
            }
        }
    }


    /**
     * Returns a string representation of the configuration information.
     *
     * @return data string dump for debugging purposes
     */
    @Override
    public String toString() {
        StringBuffer output = new StringBuffer();

        output.append("Configuration object: type = ");
        output.append(getClass().getSimpleName());
        output.append(", properties = {\n");

        StringBuilder propertiesOutput = toStringProperties();

        // format properties output
        String propFormat = propertiesOutput.toString();
        propFormat = propFormat.replaceAll("\n", "\n" + MiscConstants.ToStringIndent);
        propFormat = propFormat.trim();

        if (propFormat.length() > 0) {
            output.append(MiscConstants.ToStringIndent)
                .append(propFormat)
                .append("\n");
        }
        output.append("}");

        return output.toString();
    }

    protected StringBuilder toStringProperties() {
        StringBuilder propertiesOutput = new StringBuilder();

        for (CfgDescriptionAttribute info : getMetaData().getAttributes()) {
            String key = info.getSchemaName();
            Object val = getProperty(null, key, false);

            if (info.getCfgEnum() == CfgObjectProperty.CFGPassword) {
                val = "[output suppressed]";
            } else if (info.isCfgType(CfgTypeMask.Enum)) {
                if (val != null && val.getClass() == Integer.class) {
                    GEnum eVal = GEnum.getValue(((CfgDescriptionAttributeEnumItem<?>) info)
                            .getEnumDescription().getCfgEnumClass(), (Integer) val);
                    if (eVal != null) {
                        val = eVal;
                    }
                }
            } else if (val instanceof Calendar) {
                val = java.text.DateFormat.getDateTimeInstance(
                        java.text.DateFormat.LONG, java.text.DateFormat.LONG)
                        .format(((Calendar) val).getTime());
            }

            if (val != null) {
                propertiesOutput.append(key).append(" : ");
                propertiesOutput.append(val);
                propertiesOutput.append("\n");
            }
        }

        return propertiesOutput;
    }

    @Override
    public CfgBase clone()
            throws CloneNotSupportedException {
        final Lock writeLock = lockObject().writeLock();
        writeLock.lock();

        CfgBase ret = null;

        try {
            ret = (CfgBase) super.clone();
            ret.confObjectData = confObjectData.clone();
            if (ret.confObjectChildren!=null){
                HashMap<String, Object> children = new HashMap<String, Object>();
                for (Map.Entry<String,Object> entry: ret.confObjectChildren.entrySet()){
                    String key = entry.getKey();
                    if (null == key) continue;
                    Object value = entry.getValue();
                    Object newValue = null;
                    try {
                        if (value instanceof CfgBase) {
                            newValue = ((CfgBase) value).clone();
                        }
                        else if (value instanceof IChildStructureWrapper){
                            newValue = ((IChildStructureWrapper) value).clone();
                        }
                    } catch (final CloneNotSupportedException e) {
                        newValue = null;
                    }
                    children.put(key, newValue);
                }
                ret.confObjectChildren = children;
            }
            //ret.confObjectChildren = null;
        } finally {
            writeLock.unlock();
        }

        return ret;
    }


    //**************************************** Equals ****************************************
    @Override
    public int hashCode() {
        int code = getClass().hashCode();
        final Lock lock = lockObject().readLock();
        lock.lock();
        try{
            for (CfgDescriptionAttribute attribute :dataClass.getAttributes()){
                String name = attribute.getSchemaName();
                Object value = null;
                try{
                    value = getPropertyImpl(null, name, false);
                }catch (Exception e){
                    value = null;
                }
                if (value==null) continue;
                code ^= 13 * name.hashCode();
                code ^= calcObjectHashCode(value);
            }
        }finally {
            lock.unlock();
        }
        return code;
    }

    @Override
    public boolean equals(Object obj) {
        if (this==obj) return true;
        if (obj==null) return false;
        if (!getClass().equals(obj.getClass())) return false;
        CfgBase cfgBase = (CfgBase) obj;
        final Lock lock = rwLock.readLock();
        final Lock baseLock = cfgBase.rwLock.readLock();
        lock.lock();
        baseLock.lock();
        try{
            for (CfgDescriptionAttribute attribute :dataClass.getAttributes()) {
                String name = attribute.getSchemaName();
                Object value1 = null;
                Object value2 = null;
                try {
                    value1 = getPropertyImpl(null, name, false);
                    value2 = cfgBase.getPropertyImpl(null, name, false);
                } catch (Exception e) {
                    value1 = null;
                    value2 = null;
                }
                if ((value1 == null) && (value2 == null)) continue;
                if ((value1 == null) || (value2 == null)) return false;
                if (!compareObjects(value1, value2)) return false;
            }
        }finally {
            baseLock.unlock();
            lock.unlock();
        }
        return true;
    }

    //**************************************** Equals utils****************************************
    static boolean compareObjects(final Object source, final Object comparable){
        if (source==comparable) return true;
        if ((source==null) || (comparable==null)) return false;
        if (!source.getClass().equals(comparable.getClass())) return false;
        if (source instanceof CfgBase)
        {
            return source.equals(comparable);
        }
        if (source instanceof Map){
            return compareMap((Map<?,?>) source, (Map<?,?>) comparable);
        }
        if (source instanceof Collection){
            return compareCollection((Collection<?>) source, (Collection<?>) comparable);
        }
        return source.equals(comparable);
    }

    static boolean compareMap(final Map<?,?> source, final Map<?,?> comparable){
        if (source==comparable) return true;
        if ((source==null) || (comparable==null)) return false;
        if (!source.getClass().equals(comparable.getClass())) return false;
        if (source.size()!=comparable.size()) return false;
        for (Map.Entry<?,?> entry: source.entrySet()) {
            if (!comparable.containsKey(entry.getKey())) return false;
            Object value2 = comparable.get(entry.getKey());
            if ((value2==null) && (entry.getValue()==null)) continue;
            if ((value2==null) || (entry.getValue()==null)) return false;
            if (!compareObjects(entry.getValue(), value2)) return false;
        }
        return true;
    }

    static boolean compareCollection(final Collection<?> source, final Collection<?> comparable) {
        if (source==comparable) return true;
        if ((source==null) || (comparable==null)) return false;
        if (!source.getClass().equals(comparable.getClass())) return false;
        if (source.size()!=comparable.size()) return false;

        int countNotNull = 0;
        for (final Object value: source) if (value != null) countNotNull++;
        for (final Object value: comparable) if (value != null) countNotNull--;

        if (countNotNull != 0) return false;
        for (Object value: source){
            boolean found = false;
            for (Object value2: comparable){
                if (compareObjects(value, value2)) {
                    found=true;
                    break;
                }
            }
            if (!found) return false;
        }
        return true;
    }

    static int calcObjectHashCode(final Object value) {
        if (value instanceof CfgBase) {
            return 31*value.hashCode();
        }
        if (value instanceof Collection) {
            return calcListHashCode((Collection<?>) value);
        }
        return 29*value.hashCode();
    }

    static int calcListHashCode(final Collection<?> source) {
        if (source==null) return Collection.class.hashCode();
        int code = source.getClass().hashCode();
        for (final Object value: source) {
            code ^= calcObjectHashCode(value);
        }
        return code;
    }

    static int calcMapHashCode(final Map<?,?> source){
        if (source==null) return Map.class.hashCode();
        int code = source.getClass().hashCode();
        for (final Map.Entry<?,?> entry: source.entrySet()) {
            code ^=19*entry.getKey().hashCode();
            if (entry.getValue() != null)
                code ^= calcObjectHashCode(entry.getValue());
        }
        return code;
    }
}

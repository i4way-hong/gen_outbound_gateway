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

import com.genesyslab.platform.applicationblocks.com.CfgBase;
import com.genesyslab.platform.applicationblocks.com.ICfgStructure;

import com.genesyslab.platform.configuration.protocol.metadata.CfgTypeMask;
import com.genesyslab.platform.configuration.protocol.metadata.CfgDescriptionClass;
import com.genesyslab.platform.configuration.protocol.metadata.CfgDescriptionStructure;
import com.genesyslab.platform.configuration.protocol.metadata.CfgDescriptionAttribute;
import com.genesyslab.platform.configuration.protocol.metadata.CfgDescriptionAttributeReference;
import com.genesyslab.platform.configuration.protocol.metadata.CfgDescriptionAttributeReferenceClass;

import com.genesyslab.platform.configuration.protocol.obj.ConfStructure;
import com.genesyslab.platform.configuration.protocol.obj.ConfObjectBase;
import com.genesyslab.platform.configuration.protocol.obj.ConfStringCollection;
import com.genesyslab.platform.configuration.protocol.obj.ConfIntegerCollection;
import com.genesyslab.platform.configuration.protocol.obj.ConfStructureCollection;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ListIterator;


/**
 * Internal class for handling of structures list type objects attributes.
 *
 * <p/><b><i>Note:</i></b> It is designed for COM AB internal use only.<br/>
 * It may be a subject of structural and functional changes without notice.
 *
 * @param <T> type of referred structures.
 */
public class CfgStructListWrapper<T extends ICfgStructure>
        implements List<T>, IChildStructureWrapper, Cloneable {

    private final ConfObjectBase          confObjectBase;
    private final CfgDescriptionAttribute attrInfo;
    private final CfgBase                 parent;

    private CfgDescriptionAttribute structKeyAttribute = null;

    private List<T> baseList;


    public CfgStructListWrapper(
            final ConfObjectBase confObjectBase,
            final CfgDescriptionAttribute attrInfo,
            final CfgBase parent) {
        this.confObjectBase = confObjectBase;
        this.attrInfo = attrInfo;
        this.parent = parent;

        if (!attrInfo.isCfgType(CfgTypeMask.List)) {
            throw new IllegalArgumentException(
                    "Illegal attribute type for structure list wrapper at \""
                    + attrInfo.getName() + "\" - "
                    + attrInfo.getTypeMaskString());
        }

        if (attrInfo.isCfgType(CfgTypeMask.Integer)
                || attrInfo.isCfgType(CfgTypeMask.Link)
                || attrInfo.isCfgType(CfgTypeMask.String)) {
            CfgDescriptionClass structInfo =
                    ((CfgDescriptionAttributeReference) attrInfo).getCfgClass();
            if (!structInfo.isCfgType(CfgTypeMask.Structure)) {
                throw new IllegalArgumentException(
                        "Invalid attribute value for structures collection: ConfIntegerCollection for "
                        + attrInfo.getName());
            }
            for (CfgDescriptionAttribute attr: structInfo.getAttributes()) {
                if (attr.isKey()/* && attr.isCfgType(CfgTypeMask.Link)*/) {
                    structKeyAttribute = attr;
                    break;
                }
            }
            if (structKeyAttribute == null) {
                throw new IllegalArgumentException(
                        "Structure '" + attrInfo.getName() + "'->"
                        + structInfo.getName() + " has no key attribute");
            }

        } else if (!attrInfo.isCfgType(CfgTypeMask.Structure)) {
            throw new IllegalArgumentException(
                    "Illegal attribute type for structure list wrapper at \""
                    + attrInfo.getName() + "\" - "
                    + attrInfo.getTypeMaskString());
        }

        refresh();
    }


    @SuppressWarnings("unchecked")
    public void refresh() {
        baseList = new ArrayList<T>();

        Object attrValue = confObjectBase.getPropertyValue(attrInfo.getIndex());

        if (attrValue != null) {
            if (attrValue instanceof ConfStructureCollection) {
                for (ConfStructure struct: (ConfStructureCollection) attrValue) {
                    add((T) CfgObjectActivator.createInstance(
                            parent.getConfigurationService(), struct, new Object[] {parent}));
                }

            } else if (attrValue instanceof ConfIntegerCollection) {
                CfgDescriptionClass structInfo =
                        ((CfgDescriptionAttributeReference) attrInfo).getCfgClass();
                if (!structInfo.isCfgType(CfgTypeMask.Structure)) {
                    throw new IllegalArgumentException(
                            "Invalid attribute value for structures collection: ConfIntegerCollection for "
                            + attrInfo.getName());
                }
                for (Integer id: (ConfIntegerCollection) attrValue) {
                    ConfStructure struct = new ConfStructure((CfgDescriptionStructure) structInfo);
                    struct.setPropertyValue(structKeyAttribute.getIndex(), id);
                    add((T) CfgObjectActivator.createInstance(
                            parent.getConfigurationService(), struct, new Object[] {parent}));
                }

            } else if (attrValue instanceof ConfStringCollection) {
                if (attrInfo.isCfgType(CfgTypeMask.String)
                        && ((CfgDescriptionAttributeReference) attrInfo)
                                .getCfgClass().isCfgType(CfgTypeMask.Structure)) {
                    CfgDescriptionClass structInfo =
                            ((CfgDescriptionAttributeReference) attrInfo).getCfgClass();
                    if (!structInfo.isCfgType(CfgTypeMask.Structure)) {
                        throw new IllegalArgumentException(
                                "Invalid attribute value for structures collection: ConfStringCollection for "
                                + attrInfo.getName());
                    }
                    for (String id: (ConfStringCollection) attrValue) {
                        ConfStructure struct = new ConfStructure((CfgDescriptionStructure) structInfo);
                        struct.setPropertyValue(structKeyAttribute.getIndex(), id);
                        add((T) CfgObjectActivator.createInstance(
                                parent.getConfigurationService(), struct, new Object[] {parent}));
                    }
                }

            } else {
                throw new IllegalArgumentException(
                        "Invalid attribute value for structures collection: "
                        + attrValue.getClass());
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void flush() {
        Collection newValue = null;

        if (attrInfo.isCfgType(CfgTypeMask.Integer)
                || attrInfo.isCfgType(CfgTypeMask.Link)) {
            newValue = new ConfIntegerCollection((CfgDescriptionAttributeReference) attrInfo);
            for (T elem: this) {
                newValue.add(elem.getRawObjectData()
                        .getPropertyValue(structKeyAttribute.getIndex()));
            }

        } else if (attrInfo.isCfgType(CfgTypeMask.String)) {
            newValue = new ConfStringCollection((CfgDescriptionAttributeReference) attrInfo);
            for (T elem: this) {
                newValue.add(elem.getRawObjectData()
                        .getPropertyValue(structKeyAttribute.getIndex()));
            }

        } else {
            newValue = new ConfStructureCollection((CfgDescriptionAttributeReferenceClass) attrInfo);
            for (T elem: this) {
                newValue.add(elem.getRawObjectData());
            }
        }

        confObjectBase.setPropertyValue(attrInfo.getIndex(), newValue);
    }


    public int size() {
        return baseList.size();
    }

    public boolean isEmpty() {
        return baseList.isEmpty();
    }

    public boolean contains(final Object o) {
        return baseList.contains(o);
    }

    public Iterator<T> iterator() {
        return baseList.iterator();
    }

    public Object[] toArray() {
        return baseList.toArray();
    }

    @SuppressWarnings("hiding")
    public <T> T[] toArray(final T[] a) {
        return baseList.toArray(a);
    }

    public boolean add(final T e) {
        return baseList.add(e);
    }

    public boolean remove(final Object o) {
        return baseList.remove(o);
    }

    public boolean containsAll(final Collection<?> c) {
        return baseList.containsAll(c);
    }

    public boolean addAll(final Collection<? extends T> c) {
        return baseList.addAll(c);
    }

    public boolean addAll(final int index, final Collection<? extends T> c) {
        return baseList.addAll(index, c);
    }

    public boolean removeAll(final Collection<?> c) {
        return baseList.removeAll(c);
    }

    public boolean retainAll(final Collection<?> c) {
        return baseList.retainAll(c);
    }

    public void clear() {
        baseList.clear();
    }

    public T get(final int index) {
        return baseList.get(index);
    }

    public T set(final int index, final T element) {
        return baseList.set(index, element);
    }

    public void add(final int index, final T element) {
        baseList.add(index, element);
    }

    public T remove(final int index) {
        return baseList.remove(index);
    }

    public int indexOf(final Object o) {
        return baseList.indexOf(o);
    }

    public int lastIndexOf(final Object o) {
        return baseList.lastIndexOf(o);
    }

    public ListIterator<T> listIterator() {
        return baseList.listIterator();
    }

    public ListIterator<T> listIterator(final int index) {
        return baseList.listIterator(index);
    }

    public List<T> subList(final int fromIndex, final int toIndex) {
        return baseList.subList(fromIndex, toIndex);
    }


    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();

        output.append("StructList(")
                .append(((CfgDescriptionAttributeReference) attrInfo)
                        .getCfgClass().getName())
                .append(")[")
                .append(size())
                .append("] = {\n");

        for (T obj : this) {
            StringBuilder propOut = new StringBuilder();
            propOut.append(obj.toString());

            int pos = propOut.indexOf("\n");
            while (pos >= 0) {
                propOut.insert(pos + 1, MiscConstants.ToStringIndent);
                pos = propOut.indexOf("\n", pos + MiscConstants.ToStringIndent.length() + 2);
            }

            output.append(MiscConstants.ToStringIndent)
                    .append(propOut.toString())
                    .append("\n");
        }

        output.append("}");

        return output.toString();
    }

    @SuppressWarnings("unchecked")
    public Object clone() throws CloneNotSupportedException {
        CfgStructListWrapper<T> result = (CfgStructListWrapper<T>) super.clone();
        result.baseList =new ArrayList<T>(result.baseList);
        return result;
    }
}

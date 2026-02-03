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
import com.genesyslab.platform.applicationblocks.com.ICfgObject;

import com.genesyslab.platform.configuration.protocol.obj.ConfObjectBase;
import com.genesyslab.platform.configuration.protocol.obj.ConfIntegerCollection;
import com.genesyslab.platform.configuration.protocol.metadata.CfgDescriptionAttributeReference;
import com.genesyslab.platform.configuration.protocol.metadata.CfgDescriptionObject;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.ListIterator;


/**
 * Internal class for handling of link list type objects attributes.
 *
 * <p/><b><i>Note:</i></b> It is designed for COM AB internal use only.<br/>
 * It may be a subject of structural and functional changes without notice.
 *
 * @param <T> type of referred objects.
 */
public final class CfgLinkListWrapper<T extends ICfgObject>
        implements List<T>, IChildStructureWrapper, Cloneable {

    private final CfgDescriptionAttributeReference attrInfo;

    private CfgBase          cfgBase;
    private ConfObjectBase   confObjectBase;

    private volatile boolean initialized;

    private ConfIntegerCollection linksCollection;
    private List<T>               objsCollection;

    private final Object lockObject = new Object();


    public CfgLinkListWrapper(
            final CfgBase cfgBase,
            final ConfObjectBase confObjectBase,
            final CfgDescriptionAttributeReference attrInfo) {
        this.cfgBase = cfgBase;
        this.attrInfo = attrInfo;
        this.confObjectBase = confObjectBase;

        this.initialized = false;
        this.objsCollection = null;
        this.linksCollection = (ConfIntegerCollection) this.confObjectBase
                .getPropertyValue(attrInfo.getIndex());
    }


    private void doRefresh() {
        linksCollection = (ConfIntegerCollection) this.confObjectBase
                .getPropertyValue(attrInfo.getIndex());

        if (linksCollection == null) {
            objsCollection = null;

        } else {
            objsCollection = createLinksList();
            if (objsCollection == null) {
                objsCollection = new ArrayList<T>();
            }
        }
    }


    public void refresh() {
        synchronized (lockObject) {
            if (initialized) {
                doRefresh();
            }
        }
    }

    public void flush() {
        // TODO implement it
    }


    @SuppressWarnings("unchecked")
    private List<T> createLinksList() {
        try {
            return (List<T>) PropertiesAccessHelper.resolveLinksList(
                    cfgBase, confObjectBase, attrInfo);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }


    public T remove(final int index) {
        synchronized (lockObject) {
            if (!initialized) {
                doRefresh();
                initialized = true;
            }

            if (linksCollection != null) {
                linksCollection.remove(index);
            } else {
                throw new IndexOutOfBoundsException(
                        "index " + index + " on null collection");
            }
            final T ret = (objsCollection != null)
                    ? objsCollection.remove(index) : null;

            return ret;
        }
    }

    public int indexOf(final Object o) {
        synchronized (lockObject) {
            if (!initialized) {
                doRefresh();
                initialized = true;
            }

            return (objsCollection != null) ? objsCollection.indexOf(o) : -1;
        }
    }

    public int lastIndexOf(final Object o) {
        synchronized (lockObject) {
            if (!initialized) {
                doRefresh();
                initialized = true;
            }

            return (objsCollection != null) ? objsCollection.lastIndexOf(o) : -1;
        }
    }

    public ListIterator<T> listIterator() {
        synchronized (lockObject) {
            if (!initialized) {
                doRefresh();
                initialized = true;
            }

            return (objsCollection != null) ? objsCollection.listIterator()
                    : Collections.<T> emptyList().listIterator();
        }
    }

    public ListIterator<T> listIterator(final int index) {
        synchronized (lockObject) {
            if (!initialized) {
                doRefresh();
                initialized = true;
            }

            if (objsCollection != null) {
                return objsCollection.listIterator(index);
            }
            if (index == 0) {
                return Collections.<T> emptyList().listIterator();
            }
            throw new IndexOutOfBoundsException("index " + index + " on null collection");
        }
    }

    public List<T> subList(final int fromIndex, final int toIndex) {
        synchronized (lockObject) {
            if (!initialized) {
                doRefresh();
                initialized = true;
            }

            if (objsCollection != null) {
                return objsCollection.subList(fromIndex, toIndex);
            }

            if (fromIndex != 0) {
                throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
            }
            if (toIndex != 0) {
                throw new IndexOutOfBoundsException("toIndex = " + toIndex);
            }
            return Collections.<T> emptyList();
        }
    }

    public boolean add(final T item) {
        synchronized (lockObject) {
            if (!initialized) {
                doRefresh();
                initialized = true;
            }

            if (linksCollection == null) {
                linksCollection = new ConfIntegerCollection(attrInfo);
            }
            if (linksCollection.add(item.getObjectDbid())) {
                if (objsCollection == null) {
                    objsCollection = new ArrayList<T>();
                }
                return objsCollection.add(item);
            }

            return false;
        }
    }

    public boolean containsAll(final Collection<?> c) {
        synchronized (lockObject) {
            if (!initialized) {
                doRefresh();
                initialized = true;
            }

            if (objsCollection != null) {
                return objsCollection.containsAll(c);
            }

            if (c.isEmpty()) {
                return true;
            }
            return false;
        }
    }

    public boolean addAll(final Collection<? extends T> c) {
        synchronized (lockObject) {
            if (!initialized) {
                doRefresh();
                initialized = true;
            }

            if (linksCollection == null) {
                linksCollection = new ConfIntegerCollection(attrInfo);
            }
            if (objsCollection == null) {
                objsCollection = new ArrayList<T>();
            }

            boolean added = false;
            for (final T elem: c) {
                linksCollection.add(elem.getObjectDbid());
                objsCollection.add(elem);
                added = true;
            }

            return added;
        }
    }

    public boolean addAll(final int index, final Collection<? extends T> c) {
        synchronized (lockObject) {
            if (!initialized) {
                doRefresh();
                initialized = true;
            }

            if (linksCollection == null) {
                linksCollection = new ConfIntegerCollection(attrInfo);
            }
            if (objsCollection == null) {
                objsCollection = new ArrayList<T>();
            }

            int pos = index;
            for (final T elem: c) {
                linksCollection.add(pos, elem.getObjectDbid());
                objsCollection.add(pos, elem);
                pos++;
            }

            return (pos != index);
        }
    }

    public boolean removeAll(final Collection<?> c) {
        synchronized (lockObject) {
            if (!initialized) {
                doRefresh();
                initialized = true;
            }

            return batchRemove(c, true);
        }
    }

    public boolean retainAll(final Collection<?> c) {
        synchronized (lockObject) {
            if (!initialized) {
                doRefresh();
                initialized = true;
            }

            return batchRemove(c, false);
        }
    }

    private boolean batchRemove(final Collection<?> c, final boolean complement) {
        boolean modified = false;
        if (objsCollection != null) {
            final int size = objsCollection.size();
            for (int r = size - 1; r < size; r++) {
                final T elem = objsCollection.get(r);
                if (c.contains(elem) == complement) {
                    if (linksCollection != null) {
                        linksCollection.remove(r);
                    }
                    objsCollection.remove(r);
                    modified = true;
                }
            }
        }
        return modified;
    }


    public boolean remove(final Object item) {
        synchronized (lockObject) {
            if (!initialized) {
                doRefresh();
                initialized = true;
            }

            if (objsCollection != null) {
                int idx = objsCollection.indexOf(item);
                if (idx >= 0) {
                    if (linksCollection != null) {
                        linksCollection.remove(idx);
                    }
                    objsCollection.remove(idx);
                    return true;
                }
            }

            return false;
        }
    }

    public boolean isEmpty() {
        synchronized (lockObject) {
            return (linksCollection != null) ? linksCollection.isEmpty() : true;
        }
    }

    public boolean contains(final Object o) {
        synchronized (lockObject) {
            if (!initialized) {
                doRefresh();
                initialized = true;
            }

            return (objsCollection != null) ? objsCollection.contains(o) : false;
        }
    }

    public void clear() {
        synchronized (lockObject) {
            if (linksCollection != null) {
                linksCollection.clear();
            }
            if (objsCollection != null) {
                objsCollection.clear();
            }
        }
    }

    public T get(final int index) {
        synchronized (lockObject) {
            if (!initialized) {
                doRefresh();
                initialized = true;
            }

            return (objsCollection != null) ? objsCollection.get(index) : null;
        }
    }

    public T set(final int index, final T element) {
        synchronized (lockObject) {
            if (!initialized) {
                doRefresh();
                initialized = true;
            }

            if (linksCollection == null) {
                linksCollection = new ConfIntegerCollection(attrInfo);
            }
            if (objsCollection == null) {
                objsCollection = new ArrayList<T>();
            }

            linksCollection.set(index, element.getObjectDbid());
            T ret = objsCollection.set(index, element);

            return ret;
        }
    }

    public void add(final int index, final T element) {
        synchronized (lockObject) {
            if (!initialized) {
                doRefresh();
                initialized = true;
            }

            if (linksCollection == null) {
                linksCollection = new ConfIntegerCollection(attrInfo);
            }
            if (objsCollection == null) {
                objsCollection = new ArrayList<T>();
            }

            linksCollection.add(index, element.getObjectDbid());
            objsCollection.add(index, element);
        }
    }

    public int size() {
        synchronized (lockObject) {
            return (linksCollection != null) ? linksCollection.size() : 0;
        }
    }

    public Iterator<T> iterator() {
        synchronized (lockObject) {
            if (!initialized) {
                doRefresh();
                initialized = true;
            }

            return (objsCollection != null) ? objsCollection.iterator()
                    : Collections.<T> emptyList().iterator();
        }
    }

    public Object[] toArray() {
        synchronized (lockObject) {
            if (!initialized) {
                doRefresh();
                initialized = true;
            }

            return (objsCollection != null) ? objsCollection.toArray() : new Object[0];
        }
    }

    @SuppressWarnings("hiding")
    public <T> T[] toArray(final T[] a) {
        synchronized (lockObject) {
            if (!initialized) {
                doRefresh();
                initialized = true;
            }

            if (objsCollection != null) {
                return objsCollection.toArray(a);
            }

            if (a.length > 0) {
                a[0] = null;
            }
            return a;
        }
    }


    @Override
    public String toString() {
        synchronized (lockObject) {
            final StringBuilder output = new StringBuilder("LinkList");

            output.append("(")
                    .append(((CfgDescriptionObject)
                            ((CfgDescriptionAttributeReference) attrInfo).getCfgClass()).getCfgEnum())
                    .append(")");

            if (linksCollection != null) {
                output.append("[")
                        .append(linksCollection.size())
                        .append("] = {\n");

                for (final Integer linkDBID : linksCollection) {
                    output.append(MiscConstants.ToStringIndent)
                            .append(linkDBID).append("\n");
                }

                output.append("}");

            } else {
                output.append(" = NULL");
            }

            return output.toString();
        }
    }

    public Object clone() throws CloneNotSupportedException {
        synchronized (lockObject) {
            @SuppressWarnings("unchecked")
            final CfgLinkListWrapper<T> result = (CfgLinkListWrapper<T>) super.clone();
            result.linksCollection = (ConfIntegerCollection) result.confObjectBase
                    .getPropertyValue(attrInfo.getIndex());
            return result;
        }
    }
}

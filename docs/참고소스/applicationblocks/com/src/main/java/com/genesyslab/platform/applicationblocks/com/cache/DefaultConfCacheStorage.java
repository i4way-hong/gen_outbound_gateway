//===============================================================================
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
// Copyright (c) 2007 - 2019 Genesys Telecommunications Laboratories, Inc. All rights reserved.
//===============================================================================
package com.genesyslab.platform.applicationblocks.com.cache;

import com.genesyslab.platform.applicationblocks.com.ICfgObject;

import com.genesyslab.platform.configuration.protocol.types.CfgObjectType;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Hashtable;


/**
 * The default implementation of the configuration cache storage.
 *
 * This storage implementation is thread safe.
 */
public final class DefaultConfCacheStorage
        implements IConfCacheStorage {

    private final Hashtable<CfgObjectType, Hashtable<Integer, ICfgObject>> objects =
            new Hashtable<CfgObjectType, Hashtable<Integer, ICfgObject>>();

    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();


    private class StorageObjectEnumeration<T> implements Iterable<T> {

        private final Object helper;
        private final Class<T> baseClass;

        protected StorageObjectEnumeration(
                final Class<T> cls,
                final Object helper) {
            this.baseClass = cls;
            this.helper = helper;
        }

        public Iterator<T> iterator() {
            return getIteratorInternal();
        }

        private Iterator<T> getIteratorInternal() {
            Collection<T> objectsCol = null;

            if (helper == null) {
                objectsCol = copyAll();
            } else if (helper instanceof CfgObjectType) {
                objectsCol = copyByType();
            } else if (helper instanceof CacheKey) {
                objectsCol = copyByKey();
            }

            if (objectsCol != null) {
                return objectsCol.iterator();
            }
            return null;
        }

        @SuppressWarnings("unchecked")
        private Collection<T> copyByKey() {
            CacheKey cacheKey = (CacheKey) helper;
            ICfgObject obj = null;

            final Lock readLock = getReadLock();
            readLock.lock();

            try {
                Hashtable<Integer, ICfgObject> objs = objects.get(cacheKey.getObjectType());
                if (objs != null) {
                    obj = objs.get(cacheKey.getObjectDbid());
                }

            } finally {
                readLock.unlock();
            }

            List<T> objectsCol = new ArrayList<T>();
            if (obj != null) {
                if (baseClass.isAssignableFrom(obj.getClass())) {
                    objectsCol.add((T) obj);
                }
            }

            return objectsCol;
        }

        @SuppressWarnings("unchecked")
        private Collection<T> copyByType() {
            CfgObjectType objType = (CfgObjectType) helper;
            Hashtable<Integer, ICfgObject> table;
            List<T> objectsCol = new ArrayList<T>();

            final Lock readLock = getReadLock();
            readLock.lock();

            try {
                table = objects.get(objType);

                if (table != null) {
                    for (ICfgObject obj : table.values()) {
                        if (baseClass.isAssignableFrom(obj.getClass())) {
                            objectsCol.add((T) obj);
                        }
                    }
                }

            } finally {
                readLock.unlock();
            }

            return objectsCol;
        }

        @SuppressWarnings("unchecked")
        private Collection<T> copyAll() {
            final List<T> objectsCol = new ArrayList<T>();

            final Lock readLock = getReadLock();
            readLock.lock();

            try {
                for (Hashtable<Integer, ICfgObject> table : objects.values()) {
                    if (table != null) {
                        for (ICfgObject obj : table.values()) {
                            if (baseClass.isAssignableFrom(obj.getClass())) {
                                objectsCol.add((T) obj);
                            }
                        }
                    }
                }

            } finally {
                readLock.unlock();
            }

            return objectsCol;
        }
    }

    Lock getReadLock() {
        return rwLock.readLock();
    }

    Lock getWriteLock() {
        return rwLock.writeLock();
    }

    /**
     * Adds the specified object into the cache.
     *
     * @param obj object to add
     * @throws IllegalArgumentException The object being added
     *             has not been saved in the configuration server
     *          or the object being added
     *             is already in the cache (dbid and type are used
     *             for the purposes of determining equality)
     */
    public void add(final ICfgObject obj) {
        add(obj, false);
    }

    /**
     * Updates an existing configuration object in the storage
     * with the passed copy.
     *
     * If the passed object does not already exist in the cache, it is added
     * to the cache. The object's dbid and type are used to determine equality.
     *
     * @param obj The new version of a cached configuration object
     */
    public void update(final ICfgObject obj) {
        add(obj, true);
    }

    private void add(final ICfgObject obj, final boolean replaceOldCopy) {
        if (obj == null) {
            throw new NullPointerException("obj");
        }
        CfgObjectType type = obj.getObjectType();
        Integer       dbid = obj.getObjectDbid();
        if (dbid == 0) {
            throw new IllegalArgumentException(
                    "The passed object has not been saved in Configuration Server");
        }

        final Lock writeLock = getWriteLock();
        writeLock.lock();

        try {
            Hashtable<Integer, ICfgObject> objList = objects.get(type);

            if (objList != null) {
                if (objList.containsKey(dbid) && !replaceOldCopy) {
                    throw new IllegalArgumentException("Object already in cache");
                }

            } else {
                objList = new Hashtable<Integer, ICfgObject>();
                objects.put(type, objList);
            }

            objList.put(dbid, obj);

        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Removes the specified configuration object from the storage.
     *
     * The object is located using its dbid and type.
     *
     * @param obj The configuration object to remove
     * @return true if object successfully deleted, false otherwise
     */
    public boolean remove(final ICfgObject obj) {
        CfgObjectType type = obj.getObjectType();
        Integer       dbid = obj.getObjectDbid();

        boolean res = false;

        final Lock writeLock = getWriteLock();
        writeLock.lock();

        try {
            Hashtable<Integer, ICfgObject> objList = objects.get(type);

            if (objList != null) {
                res = (objList.remove(dbid) != null);
                if (objList.isEmpty()) {
                    objects.remove(type);
                }
            }

        } finally {
            writeLock.unlock();
        }

        return res;
    }


    /**
     * Removes all items in storage.
     */
    public void clear() {
        final Lock writeLock = getWriteLock();
        writeLock.lock();
        try {
            objects.clear();
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Retrieves a list of all objects in the storage.
     *
     * @param <T> The type of object in the resulting list
     * @param cls class of objects to be retrieved
     * @return An enumerable list of the requested objects
     */
    public <T extends ICfgObject> Iterable<T> retrieve(final Class<T> cls) {
        return new StorageObjectEnumeration<T>(cls, null);
    }

    /**
     * Retrieves a list of objects in storage utilizing a "helper"
     * parameter.
     *
     * @param <T> The type of object in the resulting list
     * @param cls class of objects to be retrieved
     * @param helper can be either "null" to retrieve all objects,
     *            or CfgObjectType to retrieve by type
     * @return An enumerable list of the requested objects
     */
    public <T extends ICfgObject> Iterable<T> retrieve(
            final Class<T> cls, final Object helper) {
        return new StorageObjectEnumeration<T>(cls, helper);
    }
}

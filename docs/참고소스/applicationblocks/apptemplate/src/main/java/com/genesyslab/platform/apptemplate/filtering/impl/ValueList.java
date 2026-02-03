/*
 * Copyright (c) 2006 - 2019 Genesys Telecommunications Laboratories, Inc. 
 * All rights reserved.
 *
 * For more  information on the Genesys Telecommunications Laboratories, Inc.
 * please see <http://www.genesyslab.com/>.
 */
package com.genesyslab.platform.apptemplate.filtering.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Pattern;

import com.genesyslab.platform.commons.GEnum;
import com.genesyslab.platform.commons.log.ILogger;
import com.genesyslab.platform.commons.log.Log;


/**
 * Represent value list of strings, integers, enums or patterns.
 * NOTE: integers and enums converted to strings.
 * This implementation is thread safe.
 */
public class ValueList implements Cloneable {

    static ILogger log = Log.getMessageFilteringLogger();

    private static final int DEFAULT_CAPACITY;
    static {
        int n = 1024;
        String property = "com.genesyslab.platform.filtering.valuelist.capacity";
        try {
            property = System.getProperty(property, "1024");
            n = Integer.parseInt(property);
        } catch (final Exception ex) { 
            log.warn("Invalid default ValueList capacity: " + property, ex);
        }
        DEFAULT_CAPACITY = n;
    }

    private int capacity = DEFAULT_CAPACITY;

    private LinkedHashMap<String, String> values = new LinkedHashMap<String, String>(capacity); 
    private HashMap<String, Pattern> patterns = new HashMap<String, Pattern>();
    private final boolean readonly;
    private final ReentrantReadWriteLock sync = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock syncRead = sync.readLock();
    private final ReentrantReadWriteLock.WriteLock syncWrite = sync.writeLock();


    public ValueList() {
        this(null, false);
    }

    public ValueList(ValueList values) {
        this(values, false);
    }

    public ValueList(ValueList values, boolean readOnly) {
        super();
        if (values != null) {
            put(values);
        }
        this.readonly = readOnly;
    }


    /**
     * Gets count of values. 
     * @return count of values.
     */
    public int size() {
        try {
            syncRead.lock();  
            return values.size() + patterns.size();
        } finally {
          syncRead.unlock();  
        }
    }

    /**
     * Checks if this value list contains any element from specified value list. 
     * NOTE: if this value will contain any patterns then they will be ignored in the operation.
     * @param list list of constant values and patterns.
     * @return true if this value list contains any element from specified value list or some value match to 
     * any pattern from list passed as argument.
     * @throws NullPointerException if argument list is null.
     */
    public boolean containsAny(ValueList list) throws NullPointerException {
        try {
            syncRead.lock(); 
            LinkedHashMap<String, String> values2 = list.values;
            HashMap<String, Pattern> patterns2 = list.patterns;

            if (values2.size() < values.size()) {
                for(String v : values2.keySet()) {
                    if (values.containsKey(v)) {
                        return true;
                    }
                }
            } else {
                for(String v : values.keySet()) {
                    if (values2.containsKey(v)) {
                        return true;
                    }
                }
            }

            if (patterns2 != null) {
                Iterator<Entry<String, Pattern>> it = patterns2.entrySet().iterator();
                while (it.hasNext()) {
                    Pattern p = it.next().getValue();
                    for(String v : values.keySet()) {
                        if (p.matcher(v).matches()) {
                            return true;
                        }
                    }
                }
            }
            return false;
        } finally {
          syncRead.unlock();
        }
    }


    private void checkReadOnly() {
        if (readonly) {
            throw new UnsupportedOperationException("value list is readonly");
        }
    }


    /**
     * Removes all values from list (if it isn't read-only list). 
     */
    public void clear() {
        if (readonly) {
            return;
        }
        try {
            syncWrite.lock();
            values.clear();
        } finally {
            syncWrite.unlock();
        }
    }

    private boolean putStringValue(String value) {
        try {
            syncWrite.lock();
            if (capacity > 0 && values.size() >= capacity) {
                return false; // TODO: may be better throws OverflowException
            }
            return values.put(value, value) == null;
        } finally {
            syncWrite.unlock();
        }
    }

    private boolean putPatternValue(Pattern value) {
        try {
            syncWrite.lock();
            if (patterns.size() >= capacity) {
                return false; // TODO: may be better throws OverflowException
            }
            return patterns.put(value.toString(), value) == null;
        } finally {
            syncWrite.unlock();
        }
    }

    private boolean removeStringValue(String value) {
        try {
            syncWrite.lock();
            return values.remove(value) != null;
        } finally {
            syncWrite.unlock();
        }
    }

    private boolean removePatternValue(Pattern value) {
        try {
            syncWrite.lock();
            return patterns.remove(value.toString()) != null;
        } finally {
            syncWrite.unlock();
        }
    }


    /**
     * Puts string value if it haven't contained yet.
     * @param value new value.
     * @return true if new value added. 
     * @throws NullPointerException if value argument is null.
     * @throws UnsupportedOperationException if this value list is readonly.
     */
    public boolean put(String value) throws NullPointerException, UnsupportedOperationException {
        checkReadOnly();
        if (value == null) {
            NullPointerException ex = new NullPointerException("value argument is null");
            if (log.isDebug()) {
                log.error("error in ValueList.put(String)", ex);
            }
            throw ex;
        }
        return putStringValue(value);
    }

    /**
     * Puts new integer value if it haven't contained yet.
     * NOTE: integer will be converted to string. Therefore integer 1 will be equivalent to string '1'
     * @param value new value.
     * @return true if new value added. 
     * @throws NullPointerException if value argument is null.
     * @throws UnsupportedOperationException if this value list is readonly.
     */
    public boolean put(Integer value) throws NullPointerException, UnsupportedOperationException {
        checkReadOnly();
        if (value == null) {
            NullPointerException ex = new NullPointerException("value argument is null");
            if (log.isDebug()) {
                log.error("error in ValueList.put(Integer)", ex);
            }
            throw ex;
        }
        return putStringValue(value.toString());
    }

    /**
     * Puts new enum value if it haven't contained yet.
     * @param value new value.
     * NOTE: enum value will be converted to string.
     * @return true if new value added. 
     * @throws NullPointerException if value argument is null.
     * @throws UnsupportedOperationException if this value list is readonly.
     */
    public boolean put(GEnum value) throws NullPointerException {
        checkReadOnly();
        if (value == null) {
            NullPointerException ex = new NullPointerException("value argument is null");
            if (log.isDebug()) {
                log.error("error in ValueList.put(GEnum)", ex);
            }
            throw ex;
        }
        return putStringValue(value.name());
    }

    /**
     * Puts new pattern value if it haven't contained yet.
     * @param value new value.
     * @return true if new value added. 
     * @throws NullPointerException if value argument is null.
     * @throws UnsupportedOperationException if this value list is readonly.
     */
    public boolean put(Pattern value) throws NullPointerException {
        checkReadOnly();
        if (value == null) {
            NullPointerException ex = new NullPointerException("value argument is null");
            if (log.isDebug()) {
                log.error("error in ValueList.put(Pattern)", ex);
            }
            throw ex;
        }
        return putPatternValue(value);
    }

    /**
     * Puts new value if it haven't contained yet.
     * @param value new value.
     * @return true if new value added. 
     * @throws IllegalArgumentException if value type isn't supported.
     * @throws NullPointerException if value argument is null.
     * @throws UnsupportedOperationException if this value list is readonly.
     */
    public boolean put(Object value) 
            throws IllegalArgumentException, NullPointerException {
        checkReadOnly();
        if (value == null) {
            NullPointerException ex = new NullPointerException("value argument is null");
            if (log.isDebug()) {
                log.error("error in ValueList.put(Object)", ex);
            }
            throw ex;
        }
        if (value instanceof String) {
            return put((String)value);
        }
        if (value instanceof Integer) {
            return put((Integer)value);
        }
        if (value instanceof GEnum) {
            return put((GEnum)value);
        }
        if (value instanceof Pattern) {
            return put((Pattern)value);
        }
        {
            IllegalArgumentException ex = new IllegalArgumentException("unsupported argument type: " + value.getClass().getCanonicalName());
            if (log.isDebug()) {
                log.error("unsupported argument type: " + value.getClass().getCanonicalName(), ex);
            }
            throw ex;
        }
    }

    /**
     * Puts non exists values from specified value list.
     * @param list source of new values.
     * @return true if even one value added.
     * @throws NullPointerException if list argument is null
     * @throws UnsupportedOperationException if this value list is readonly.
     */
    public boolean put(ValueList list) 
            throws NullPointerException, UnsupportedOperationException {
        checkReadOnly();
        if (list == null) {
            NullPointerException ex = new NullPointerException("list argument is null");
            if (log.isDebug()) {
                log.error("error in ValueList.put(ValueList)", ex);
            }
            throw ex;
        }
        boolean result = false;
        for(String value : list.values.keySet()) {
            result |= put(value);
        }
        for(Pattern pattern : list.patterns.values()) {
            result |= put(pattern);
        }
        return result;
    }

    /**
     * Removes value if it exists.
     * @param value to remove.
     * @return true if value removed.
     * @throws NullPointerException if value argument is null.
     * @throws UnsupportedOperationException if this value list is readonly.
     */
    public boolean remove(String value) throws NullPointerException {
        checkReadOnly();
        if (value == null) {
            NullPointerException ex = new NullPointerException("value argument is null");
            if (log.isDebug()) {
                log.error("error in ValueList.remove(String)", ex);
            }
            throw ex;
        }
        return removeStringValue(value);
    }

    /**
     * Removes value if it exists.
     * @param value to remove.
     * @return true if value removed.
     * @throws NullPointerException if value argument is null.
     * @throws UnsupportedOperationException if this value list is readonly.
     */
    public boolean remove(Integer value) throws NullPointerException {
        checkReadOnly();
        if (value == null) {
            NullPointerException ex = new NullPointerException("value argument is null");
            if (log.isDebug()) {
                log.error("error in ValueList.remove(String)", ex);
            }
            throw ex;
        }
        return removeStringValue(value.toString());
    }

    /**
     * Removes value if it exists.
     * @param value to remove.
     * @return true if value removed.
     * @throws NullPointerException if value argument is null.
     * @throws UnsupportedOperationException if this value list is readonly.
     */
    public boolean remove(GEnum value) throws NullPointerException {
        checkReadOnly();
        if (value == null) {
            NullPointerException ex = new NullPointerException("value argument is null");
            if (log.isDebug()) {
                log.error("error in ValueList.remove(String)", ex);
            }
            throw ex;
        }
        return removeStringValue(value.name());
    }

    /**
     * Removes value if it exists.
     * @param value to remove.
     * @return true if value removed.
     * @throws NullPointerException if value argument is null.
     * @throws UnsupportedOperationException if this value list is readonly.
     */
    public boolean remove(Pattern value) throws NullPointerException {
        checkReadOnly();
        if (value == null) {
            NullPointerException ex = new NullPointerException("value argument is null");
            if (log.isDebug()) {
                log.error("error in ValueList.remove(String)", ex);
            }
            throw ex;
        }
        return removePatternValue(value);
    }

    /**
     * Removes value if it exists.
     * @param value to remove.
     * @return true if value removed.
     * @throws NullPointerException if value argument is null.
     * @throws UnsupportedOperationException if this value list is readonly.
     */
    public boolean remove(Object value) throws NullPointerException, IllegalArgumentException {
        checkReadOnly();
        if (value == null) {
            NullPointerException ex = new NullPointerException("value argument is null");
            if (log.isDebug()) {
                log.error("error in ValueList.remove(String)", ex);
            }
            throw ex;
        }
        if (value instanceof String) {
            return remove((String)value);
        }
        if (value instanceof Integer) {
            return remove((Integer)value);
        }
        if (value instanceof GEnum) {
            return remove((GEnum)value);
        }
        if (value instanceof Pattern) {
            return remove((Pattern)value);
        }
        {
            IllegalArgumentException ex = new IllegalArgumentException("unsupported argument type: " + value.getClass().getCanonicalName());
            if (log.isDebug()) {
                log.error("unsupported argument type: " + value.getClass().getCanonicalName(), ex);
            }
            throw ex;
        }
    }

    /**
     * Removes values if it exists.
     * @param list values which should be removed from this value list.
     * @return true if any value removed .
     * @throws NullPointerException list argument is null.
     * @throws UnsupportedOperationException if this value list is readonly.
     */
    public boolean remove(ValueList list) throws NullPointerException {
        checkReadOnly();
        if (list == null) {
            NullPointerException ex = new NullPointerException("list argument is null");
            if (log.isDebug()) {
                log.error("error in ValueList.remove(ValueList)", ex);
            }
            throw ex;
        }

        LinkedHashMap<String, String> values2 = list.values;
        HashMap<String, Pattern> patterns2 = list.patterns;

        boolean result = false;
        try {
            syncWrite.lock();
            for(String value : values2.keySet()) {
                result |= remove(value);
            }
            for(Entry<String, Pattern> e : patterns2.entrySet()) {
                result |= remove(e.getValue());
            }
        } finally {
            syncWrite.unlock();
        }
        return result;
    }

    public boolean set(ValueList list) {
        checkReadOnly();
        if (list == null) {
            NullPointerException ex = new NullPointerException("list argument is null");
            if (log.isDebug()) {
                log.error("error in ValueList.remove(ValueList)", ex);
            }
            throw ex;
        }

        LinkedHashMap<String, String> values2 = list.values;
        HashMap<String, Pattern> patterns2 = list.patterns;

        boolean result = false;
        try {
            syncWrite.lock();
            clear();
            for(String value : values2.keySet()) {
                result |= remove(value);
            }
            for(Entry<String, Pattern> e : patterns2.entrySet()) {
                result |= remove(e.getValue());
            }
        } finally {
            syncWrite.unlock();
        }
        return result;
    }


    @SuppressWarnings("unchecked")
    @Override
    public Object clone() {
        ValueList obj;
        try {
            syncRead.lock();
            try {
                obj = (ValueList)super.clone();
            }
            catch(CloneNotSupportedException ex) {
                throw new InternalError("it shouldn't be happend");
            }

            obj.values = (LinkedHashMap<String, String>) values.clone();
            obj.patterns = (HashMap<String, Pattern>) patterns.clone();
        } finally {
            syncRead.unlock();
        }
        return obj;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = Math.max(0, capacity);
    }
}

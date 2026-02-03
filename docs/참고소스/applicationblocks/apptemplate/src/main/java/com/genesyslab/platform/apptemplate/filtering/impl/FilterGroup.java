package com.genesyslab.platform.apptemplate.filtering.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.genesyslab.platform.commons.protocol.Message;


/**
 * Groups elementary {@link BaseFilter filter} objects to perform
 * complex message filtering operations, for example to trace messages 
 * by stored variable value from another message.
 *
 */
public class FilterGroup extends BaseFilter {

    private ArrayList<BaseFilter> filters = new ArrayList<BaseFilter>();

    public FilterGroup(String key) {
        super(key);
    }

    @Override
    public boolean isMessageAccepted(Message message, FilterContext context) {
        List<BaseFilter> filters = this.filters;
        boolean result = false;
        for(BaseFilter filter : filters) {
            result |= filter.isMessageAccepted(message, context);
        }

        return result;
    }

    /**
     * Gets filter keys.
     * @return Set of filter keys.
     */
    public Set<String> getFilterKeys() {
        List<BaseFilter> filters = this.filters;
        Set<String> keys = new HashSet<String>(filters.size());
        for(BaseFilter filter : filters) {
            keys.add(filter.getKey());
        }
        return keys;
    }

    /**
     * Gets filter by key.
     * @param key of filter to get.
     * @return filter.
     * @throws NullPointerException if key argument is null. 
     */
    public BaseFilter getFilter(String key) throws NullPointerException  {
        if (key == null) {
            throw new NullPointerException("key argument is null");
        }
        List<BaseFilter> filters = this.filters;
        for(BaseFilter f : filters) {
            if (key.equals(f.getKey())) {
                return f;
            }
        }
        return null;
    }

    /**
     * Adds/Replaces filter by key.
     * @param key of filter.
     * @param filter which should be added.
     * @return true if filter was added or false if it replaced other filter.
     * @throws NullPointerException if any argument is null.
     * @throws IllegalStateException when filter group already registered (used).
     */
    public boolean putFilter(String key, BaseFilter filter) throws NullPointerException {
        if (key == null) {
            throw new NullPointerException("key argument is null");
        }
        if (filter == null) {
            throw new NullPointerException("filter argument is null");
        }
        if (isRegistered()) {
            throw new IllegalStateException("can't modify used filter group");
        }

        int count=filters.size();
        for(int i=0; i<count; i++) {
            BaseFilter f = filters.get(i);
            if (key.equals(f.getKey())) {
                filters.set(i, filter);
                return true;
            }
        }
        filters.add(filter);
        return false;
    }

    /**
     * Removes filter by key.
     * @param key of filter which must be removed (if exists).
     * @return true if removed else false.
     * @throws IllegalStateException when filter group already registered (used).
     * @throws NullPointerException when key argument is null.
     */
    public boolean removeFilter(String key) throws NullPointerException {
        if (key == null) {
            throw new NullPointerException("key argument is null");
        }
        if (isRegistered()) {
            throw new IllegalStateException("can't modify used filter group");
        }
        
        int count=filters.size();
        for(int i=0; i<count; i++) {
            BaseFilter f = filters.get(i);
            if (key.equals(f.getKey())) {
                filters.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Removes all filters.
     * @throws IllegalStateException when filter group already registered (used).
     */
    public void removeAllFilters() {
        if (isRegistered()) {
            throw new IllegalStateException("can't modify used filter group");
        }
        this.filters = new ArrayList<BaseFilter>();
    }

    /**
     * Registers filter variables and so on.
     * @param context filter context.
     */
    protected void doRegister(FilterContext context) {
        super.doRegister(context);
        ArrayList<BaseFilter> filters = this.filters;
        for (BaseFilter subfilter : filters) {
            subfilter.doRegister(context);
        }
    }

    /**
     * Unregisters filter variables and so on. 
     * @param context filter context.
     */
    protected void doUnregister(FilterContext context) {
        ArrayList<BaseFilter> filters = this.filters;            
        for (BaseFilter subfilter : filters) {
            subfilter.doUnregister(context);
        }                
        super.doUnregister(context);
    } 

    @SuppressWarnings("unchecked")
    @Override
    public Object clone() {
        FilterGroup obj= (FilterGroup) super.clone();
        obj.filters = (ArrayList<BaseFilter>) filters.clone();
        int count = obj.filters.size();
        for(int i=count-1; i>=0; i--) {
            BaseFilter filter = obj.filters.get(i);
            obj.filters.set(i, (BaseFilter) filter.clone());
        }
        return obj;
    }
}

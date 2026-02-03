/*
 * Copyright (c) 2006 - 2019 Genesys Telecommunications Laboratories, Inc. 
 * All rights reserved.
 *
 * For more  information on the Genesys Telecommunications Laboratories, Inc.
 * please see <http://www.genesyslab.com/>.
 */
package com.genesyslab.platform.apptemplate.filtering.impl.configuration;


import com.genesyslab.platform.apptemplate.filtering.impl.BaseFilter;
import com.genesyslab.platform.commons.collections.KeyValueCollection;
import com.genesyslab.platform.commons.log.ILogger;
import com.genesyslab.platform.commons.log.Log;

/**
 * Creates or modifies {@link BaseFilter filter} regarding provided configuration options.
 */
public abstract class FilterFactory {

    public static final String filter_key = "log-filter.";
    public static final String filter_type = "filter-type";


    private static final ILogger log = Log.getMessageFilteringLogger();

    /**
     * Creates filter and apply filter options.
     */
    public abstract BaseFilter create(String filterName, KeyValueCollection options);

    /**
     * Creates or modifies filter options.
     * @param filter The filter object to which option should be applied.
     * @param key Option name
     * @param value Option value
     * @return True if option was successfully parsed and applied to filter.
     */
    public abstract boolean applyKeyValueOption(BaseFilter filter, String key, String value);
    
    /**
     * Removes configuration option from the provided filter.
     * @param filter The filter object from which option should be removed.
     * @param key Option name
     * @return True if option was successfully removed from filter.
     */
    public abstract boolean removeKeyValueOption(BaseFilter filter, String key);

    
    /**
     * Expected string: log-filter.filtername
     * If format doesn't match, null will be returned.
     * @param key String that contains filter name.
     * @return parsed name or null
     */
    public static String parseName(String key) {        
        if (key == null)
            throw new IllegalArgumentException("String with filter name can't be null.");
        
        String name = null;
        key = key.trim();
        if (key != null && key.startsWith(filter_key) && key.length() > filter_key.length()) {
            name = key.substring(filter_key.length()); 

            if (name.charAt(0) == '!' || name.charAt(0) == '-' || name.charAt(0) == ' ') {
                if (log.isWarn()) {
                    log.warnFormat(
                            "Filter name ''{0}'' can''t start with ''{1}'', expected format is ''log-filter.name''",
                            new Object[] { name, name.charAt(0) });
                }
                name = null;
            }
        }
        return name;
    }

    

 

}

package com.genoutbound.gateway.genesys.cfg.service.support;

import com.genesyslab.platform.commons.collections.KeyValueCollection;
import com.genesyslab.platform.commons.collections.KeyValuePair;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

public final class CfgValueFormatter {

    private CfgValueFormatter() {
    }

    public static String toIsoString(Calendar calendar) {
        if (calendar == null) {
            return null;
        }
        return OffsetDateTime.ofInstant(calendar.toInstant(), ZoneId.systemDefault()).toString();
    }

    public static Map<String, Object> toMap(KeyValueCollection collection) {
        Map<String, Object> result = new LinkedHashMap<>();
        if (collection == null) {
            return result;
        }
        for (Object obj : collection) {
            if (!(obj instanceof KeyValuePair pair)) {
                continue;
            }
            Object value = pair.getValue();
            if (value instanceof KeyValueCollection nested) {
                result.put(pair.getStringKey(), toMap(nested));
            } else {
                result.put(pair.getStringKey(), value);
            }
        }
        return result;
    }
}
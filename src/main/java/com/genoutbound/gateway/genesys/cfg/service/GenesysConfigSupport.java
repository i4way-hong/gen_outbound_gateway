package com.genoutbound.gateway.genesys.cfg.service;

import com.genesyslab.platform.applicationblocks.com.ConfigException;
import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.genesyslab.platform.applicationblocks.com.objects.CfgPerson;
import com.genesyslab.platform.applicationblocks.com.queries.CfgPersonQuery;
import com.genesyslab.platform.commons.collections.KeyValueCollection;
import com.genesyslab.platform.commons.collections.KeyValuePair;
import com.genoutbound.gateway.core.ApiException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;

/**
 * Config 서비스 공통 유틸리티를 제공합니다.
 */
public abstract class GenesysConfigSupport {

    protected final GenesysConfigClient configClient;

    protected GenesysConfigSupport(GenesysConfigClient configClient) {
        this.configClient = configClient;
    }

    protected <T> Collection<T> safeCollection(Collection<T> source) {
        return source == null ? List.of() : source;
    }

    protected int resolveTenantDbid(Integer tenantDbid) {
        int resolved = tenantDbid == null ? configClient.getTenantDbid() : tenantDbid;
        if (resolved <= 0) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "tenantDbid 설정이 필요합니다.");
        }
        return resolved;
    }

    protected int resolveSwitchDbid(Integer switchDbid) {
        int resolved = switchDbid == null ? configClient.getPrimarySwitchDbid() : switchDbid;
        if (resolved <= 0) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "switchDbid 설정이 필요합니다.");
        }
        return resolved;
    }

    protected void ensureNotExists(Object existing, String message) {
        if (existing != null) {
            throw new ApiException(HttpStatus.CONFLICT, message);
        }
    }

    protected KeyValueCollection toUserProperties(Map<String, Map<String, String>> properties) {
        KeyValueCollection collection = new KeyValueCollection();
        for (Map.Entry<String, Map<String, String>> sectionEntry : properties.entrySet()) {
            if (sectionEntry.getKey() == null || sectionEntry.getKey().isBlank()) {
                continue;
            }
            KeyValueCollection sectionOptions = new KeyValueCollection();
            Map<String, String> values = sectionEntry.getValue();
            if (values != null) {
                for (Map.Entry<String, String> optionEntry : values.entrySet()) {
                    if (optionEntry.getKey() == null || optionEntry.getKey().isBlank()) {
                        continue;
                    }
                    sectionOptions.addString(optionEntry.getKey(), optionEntry.getValue());
                }
            }
            collection.addPair(new KeyValuePair(sectionEntry.getKey(), sectionOptions));
        }
        return collection;
    }

    protected Map<String, Map<String, String>> fromUserProperties(KeyValueCollection properties) {
        Map<String, Map<String, String>> result = new LinkedHashMap<>();
        if (properties == null) {
            return result;
        }
        for (Object obj : properties) {
            KeyValuePair sectionPair = (KeyValuePair) obj;
            String sectionName = sectionPair.getStringKey();
            KeyValueCollection sectionOptions = sectionPair.getTKVValue();
            Map<String, String> options = new LinkedHashMap<>();
            if (sectionOptions != null) {
                for (Object optionObj : sectionOptions) {
                    KeyValuePair optionPair = (KeyValuePair) optionObj;
                    options.put(optionPair.getStringKey(), optionPair.getStringValue());
                }
            }
            result.put(sectionName, options);
        }
        return result;
    }

    protected CfgPerson getPersonByDbid(IConfService service, int tenantDbid, int personDbid) throws ConfigException {
        CfgPersonQuery query = new CfgPersonQuery();
        query.setTenantDbid(tenantDbid);
        query.setDbid(personDbid);
        CfgPerson person = service.retrieveObject(CfgPerson.class, query);
        if (person == null) {
            throw new ApiException(HttpStatus.NOT_FOUND, "상담사를 찾을 수 없습니다.");
        }
        return person;
    }

    protected CfgPerson findPersonByEmployeeId(IConfService service, int tenantDbid, String employeeId)
            throws ConfigException {
        CfgPersonQuery query = new CfgPersonQuery();
        query.setTenantDbid(tenantDbid);
        query.setEmployeeId(employeeId);
        CfgPerson person = service.retrieveObject(CfgPerson.class, query);
        if (person == null) {
            throw new ApiException(HttpStatus.NOT_FOUND, "상담사를 찾을 수 없습니다.");
        }
        return person;
    }
}

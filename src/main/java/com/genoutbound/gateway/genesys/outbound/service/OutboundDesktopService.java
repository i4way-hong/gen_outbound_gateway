package com.genoutbound.gateway.genesys.outbound.service;

import com.genesyslab.platform.commons.collections.KeyValueCollection;
import com.genesyslab.platform.commons.collections.KeyValuePair;
import com.genesyslab.platform.outbound.protocol.outbounddesktop.AddRecord;
import com.genesyslab.platform.outbound.protocol.outbounddesktop.AddRecordAcknowledge;
import com.genesyslab.platform.outbound.protocol.outbounddesktop.CallResult;
import com.genesyslab.platform.outbound.protocol.outbounddesktop.ChainAttribute;
import com.genesyslab.platform.outbound.protocol.outbounddesktop.DoNotCall;
import com.genesyslab.platform.outbound.protocol.outbounddesktop.DoNotCallAcknowledge;
import com.genesyslab.platform.outbound.protocol.outbounddesktop.OutboundDesktopBinding;
import com.genesyslab.platform.outbound.protocol.outbounddesktop.PhoneType;
import com.genesyslab.platform.outbound.protocol.outbounddesktop.RecordStatus;
import com.genesyslab.platform.outbound.protocol.outbounddesktop.RecordType;
import com.genoutbound.gateway.core.ApiException;
import com.genesyslab.platform.voice.protocol.tserver.CommonProperties;
import com.genoutbound.gateway.genesys.outbound.OutboundDesktopProperties;
import com.genoutbound.gateway.genesys.outbound.dto.OutboundDesktopAddRecordAcknowledgeRequest;
import com.genoutbound.gateway.genesys.outbound.dto.OutboundDesktopAddRecordRequest;
import com.genoutbound.gateway.genesys.outbound.dto.OutboundDesktopDoNotCallAcknowledgeRequest;
import com.genoutbound.gateway.genesys.outbound.dto.OutboundDesktopDoNotCallRequest;
import com.genoutbound.gateway.genesys.outbound.dto.OutboundDesktopPayloadResponse;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * Outbound Desktop 메시지(POC) 생성 및 KV 마샬링을 담당합니다.
 */
@Service
public class OutboundDesktopService {

    private static final Logger log = LoggerFactory.getLogger(OutboundDesktopService.class);
    private final OutboundDesktopClient outboundDesktopClient;
    private final OutboundDesktopProperties properties;

    public OutboundDesktopService(OutboundDesktopClient outboundDesktopClient, OutboundDesktopProperties properties) {
        this.outboundDesktopClient = outboundDesktopClient;
        this.properties = properties;
    }

    public OutboundDesktopPayloadResponse buildAddRecord(OutboundDesktopAddRecordRequest request) {
        log.debug("OutboundDesktop AddRecord 요청: {}", request);
        AddRecord message = new AddRecord();
        applyAddRecord(message, request);
        OutboundDesktopPayloadResponse response = toPayloadResponse(message);
        log.debug("OutboundDesktop AddRecord 응답: {}", response);
        return response;
    }

    public OutboundDesktopPayloadResponse sendAddRecord(OutboundDesktopAddRecordRequest request) {
        log.debug("OutboundDesktop AddRecord 전송 요청: {}", request);
        AddRecord message = new AddRecord();
        applyAddRecord(message, request);
        CommonProperties userEvent = buildUserEvent(message);
        outboundDesktopClient.sendUserEvent(resolveCommunicationDn(request.communicationDn()), userEvent);
        OutboundDesktopPayloadResponse response = toPayloadResponse(message, false);
        log.debug("OutboundDesktop AddRecord 전송 응답: {}", response);
        return response;
    }

    public OutboundDesktopPayloadResponse buildAddRecordAcknowledge(OutboundDesktopAddRecordAcknowledgeRequest request) {
        log.debug("OutboundDesktop AddRecordAcknowledge 요청: {}", request);
        AddRecordAcknowledge message = new AddRecordAcknowledge();
        applyAddRecordAcknowledge(message, request);
        OutboundDesktopPayloadResponse response = toPayloadResponse(message);
        log.debug("OutboundDesktop AddRecordAcknowledge 응답: {}", response);
        return response;
    }

    public OutboundDesktopPayloadResponse sendAddRecordAcknowledge(OutboundDesktopAddRecordAcknowledgeRequest request) {
        log.debug("OutboundDesktop AddRecordAcknowledge 전송 요청: {}", request);
        AddRecordAcknowledge message = new AddRecordAcknowledge();
        applyAddRecordAcknowledge(message, request);
        CommonProperties userEvent = buildUserEvent(message);
        outboundDesktopClient.sendUserEvent(resolveCommunicationDn(request.communicationDn()), userEvent);
        OutboundDesktopPayloadResponse response = toPayloadResponse(message, false);
        log.debug("OutboundDesktop AddRecordAcknowledge 전송 응답: {}", response);
        return response;
    }

    public OutboundDesktopPayloadResponse buildDoNotCall(OutboundDesktopDoNotCallRequest request) {
        log.debug("OutboundDesktop DoNotCall 요청: {}", request);
        DoNotCall message = new DoNotCall();
        applyDoNotCall(message, request);
        OutboundDesktopPayloadResponse response = toPayloadResponse(message);
        log.debug("OutboundDesktop DoNotCall 응답: {}", response);
        return response;
    }

    public OutboundDesktopPayloadResponse sendDoNotCall(OutboundDesktopDoNotCallRequest request) {
        log.debug("OutboundDesktop DoNotCall 전송 요청: {}", request);
        DoNotCall message = new DoNotCall();
        applyDoNotCall(message, request);
        CommonProperties userEvent = buildUserEvent(message);
        outboundDesktopClient.sendUserEvent(resolveCommunicationDn(request.communicationDn()), userEvent);
        OutboundDesktopPayloadResponse response = toPayloadResponse(message, false);
        log.debug("OutboundDesktop DoNotCall 전송 응답: {}", response);
        return response;
    }

    public OutboundDesktopPayloadResponse buildDoNotCallAcknowledge(OutboundDesktopDoNotCallAcknowledgeRequest request) {
        log.debug("OutboundDesktop DoNotCallAcknowledge 요청: {}", request);
        DoNotCallAcknowledge message = new DoNotCallAcknowledge();
        applyDoNotCallAcknowledge(message, request);
        OutboundDesktopPayloadResponse response = toPayloadResponse(message);
        log.debug("OutboundDesktop DoNotCallAcknowledge 응답: {}", response);
        return response;
    }

    public OutboundDesktopPayloadResponse sendDoNotCallAcknowledge(OutboundDesktopDoNotCallAcknowledgeRequest request) {
        log.debug("OutboundDesktop DoNotCallAcknowledge 전송 요청: {}", request);
        DoNotCallAcknowledge message = new DoNotCallAcknowledge();
        applyDoNotCallAcknowledge(message, request);
        CommonProperties userEvent = buildUserEvent(message);
        outboundDesktopClient.sendUserEvent(resolveCommunicationDn(request.communicationDn()), userEvent);
        OutboundDesktopPayloadResponse response = toPayloadResponse(message, false);
        log.debug("OutboundDesktop DoNotCallAcknowledge 전송 응답: {}", response);
        return response;
    }

    private void applyAddRecord(AddRecord message, OutboundDesktopAddRecordRequest request) {
        if (request.applicationId() != null) {
            message.setApplicationId(request.applicationId());
        }
        if (request.campaignName() != null) {
            message.setCampaignName(request.campaignName());
        }
        if (request.callingList() != null) {
            message.setCallingList(request.callingList());
        }
        if (request.phone() != null) {
            message.setPhone(request.phone());
        }
        if (request.agentId() != null) {
            message.setAgentId(request.agentId());
        }
        if (request.attempts() != null) {
            message.setAttempts(request.attempts());
        }
        if (request.callResultCode() != null) {
            message.setCallResult(CallResult.toEnum(request.callResultCode()));
        }
        if (request.callTime() != null) {
            message.setCallTime(request.callTime());
        }
        Date parsed = parseDate(request.dateTime());
        if (parsed != null) {
            message.setDateTime(parsed);
        }
        if (request.from() != null) {
            message.setFrom(request.from());
        }
        if (request.until() != null) {
            message.setUntil(request.until());
        }
        if (request.tzName() != null) {
            message.setTzName(request.tzName());
        }
        if (request.phoneType() != null) {
            message.setPhoneType(PhoneType.toEnum(request.phoneType()));
        }
        if (request.recordStatus() != null) {
            message.setRecordStatus(RecordStatus.toEnum(request.recordStatus()));
        }
        if (request.recordType() != null) {
            message.setRecordType(RecordType.toEnum(request.recordType()));
        }
        if (request.otherFields() != null && !request.otherFields().isEmpty()) {
            message.setOtherFields(toKeyValueCollection(request.otherFields()));
        }
    }

    private void applyAddRecordAcknowledge(AddRecordAcknowledge message, OutboundDesktopAddRecordAcknowledgeRequest request) {
        if (request.applicationId() != null) {
            message.setApplicationId(request.applicationId());
        }
        if (request.campaignName() != null) {
            message.setCampaignName(request.campaignName());
        }
        if (request.callingList() != null) {
            message.setCallingList(request.callingList());
        }
        if (request.phone() != null) {
            message.setPhone(request.phone());
        }
        if (request.attempts() != null) {
            message.setAttempts(request.attempts());
        }
        if (request.callResultCode() != null) {
            message.setCallResult(CallResult.toEnum(request.callResultCode()));
        }
        if (request.chainId() != null) {
            message.setChainId(request.chainId());
        }
        if (request.chainN() != null) {
            message.setChainN(request.chainN());
        }
        if (request.from() != null) {
            message.setFrom(request.from());
        }
        if (request.until() != null) {
            message.setUntil(request.until());
        }
        if (request.tzName() != null) {
            message.setTzName(request.tzName());
        }
        if (request.phoneType() != null) {
            message.setPhoneType(PhoneType.toEnum(request.phoneType()));
        }
        if (request.recordStatus() != null) {
            message.setRecordStatus(RecordStatus.toEnum(request.recordStatus()));
        }
        if (request.recordType() != null) {
            message.setRecordType(RecordType.toEnum(request.recordType()));
        }
        if (request.otherFields() != null && !request.otherFields().isEmpty()) {
            message.setOtherFields(toKeyValueCollection(request.otherFields()));
        }
    }

    private void applyDoNotCall(DoNotCall message, OutboundDesktopDoNotCallRequest request) {
        if (request.applicationId() != null) {
            message.setApplicationId(request.applicationId());
        }
        if (request.campaignName() != null) {
            message.setCampaignName(request.campaignName());
        }
        if (request.callingList() != null) {
            message.setCallingList(request.callingList());
        }
        if (request.chainAttr() != null) {
            message.setChainAttr(resolveChainAttr(request.chainAttr()));
        }
        if (request.customerId() != null) {
            message.setCustomerId(request.customerId());
        }
        if (request.recordHandle() != null) {
            message.setRecordHandle(request.recordHandle());
        }
        if (request.phone() != null) {
            message.setPhone(request.phone());
        }
    }

    private void applyDoNotCallAcknowledge(DoNotCallAcknowledge message, OutboundDesktopDoNotCallAcknowledgeRequest request) {
        if (request.applicationId() != null) {
            message.setApplicationId(request.applicationId());
        }
        if (request.campaignName() != null) {
            message.setCampaignName(request.campaignName());
        }
        if (request.callingList() != null) {
            message.setCallingList(request.callingList());
        }
        if (request.chainAttr() != null) {
            message.setChainAttr(resolveChainAttr(request.chainAttr()));
        }
        if (request.recordHandle() != null) {
            message.setRecordHandle(request.recordHandle());
        }
        if (request.phone() != null) {
            message.setPhone(request.phone());
        }
        if (request.otherFields() != null && !request.otherFields().isEmpty()) {
            message.setOtherFields(toKeyValueCollection(request.otherFields()));
        }
    }

    private ChainAttribute resolveChainAttr(String value) {
        ChainAttribute chain = ChainAttribute.toEnum(value);
        return chain != null ? chain : ChainAttribute.create(value);
    }

    private Date parseDate(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Date.from(OffsetDateTime.parse(value).toInstant());
        } catch (DateTimeParseException ex) {
            try {
                return Date.from(Instant.parse(value));
            } catch (DateTimeParseException inner) {
                log.warn("dateTime 파싱 실패: {}", value);
                return null;
            }
        }
    }

    private KeyValueCollection toKeyValueCollection(Map<String, String> fields) {
        KeyValueCollection collection = new KeyValueCollection();
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            if (entry.getKey() == null || entry.getKey().isBlank()) {
                continue;
            }
            collection.addString(entry.getKey(), entry.getValue());
        }
        return collection;
    }

    private OutboundDesktopPayloadResponse toPayloadResponse(Object message) {
        return toPayloadResponse(message, true);
    }

    private OutboundDesktopPayloadResponse toPayloadResponse(Object message, boolean simulated) {
        KeyValueCollection kv = marshal(message);
        Map<String, Object> payload = toMap(kv);
        return new OutboundDesktopPayloadResponse(message.getClass().getSimpleName(), payload, simulated);
    }

    private KeyValueCollection marshal(Object message) {
        try {
            return OutboundDesktopBinding.marshal(message);
        } catch (OutboundDesktopBinding.KVBindingException ex) {
            throw new ApiException(HttpStatus.BAD_GATEWAY, "OutboundDesktop 마샬링 실패");
        }
    }

    private CommonProperties buildUserEvent(Object message) {
        KeyValueCollection kv = marshal(message);
        CommonProperties props = CommonProperties.create();
        props.setUserEvent(properties.getUserEventId());
        props.setUserData(kv);
        return props;
    }

    private String resolveCommunicationDn(String communicationDn) {
        if (communicationDn == null || communicationDn.isBlank()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "communicationDn 설정이 필요합니다.");
        }
        return communicationDn;
    }

    private Map<String, Object> toMap(KeyValueCollection collection) {
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

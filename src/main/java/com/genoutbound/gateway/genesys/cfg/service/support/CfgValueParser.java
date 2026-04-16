package com.genoutbound.gateway.genesys.cfg.service.support;

import com.genoutbound.gateway.core.ApiException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import org.springframework.http.HttpStatus;

public final class CfgValueParser {

    private CfgValueParser() {
    }

    public static Calendar parseIsoDateTime(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            OffsetDateTime parsed = OffsetDateTime.parse(value);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(parsed.toInstant().toEpochMilli());
            return calendar;
        } catch (DateTimeParseException ex) {
            throw new ApiException(HttpStatus.BAD_REQUEST, fieldName + " 값이 올바르지 않습니다.");
        }
    }

    public static <E> E parseEnum(String value, Class<E> enumType, String fieldName) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            if (enumType.isEnum()) {
                @SuppressWarnings({"rawtypes", "unchecked"})
                E result = (E) Enum.valueOf((Class) enumType, value);
                if (result == null) {
                    throw new ApiException(HttpStatus.BAD_REQUEST, fieldName + " 값이 올바르지 않습니다.");
                }
                return result;
            }
            E result = enumType.cast(enumType.getMethod("valueOf", String.class).invoke(null, value));
            if (result == null) {
                throw new ApiException(HttpStatus.BAD_REQUEST, fieldName + " 값이 올바르지 않습니다.");
            }
            return result;
        } catch (IllegalArgumentException | ReflectiveOperationException ex) {
            throw new ApiException(HttpStatus.BAD_REQUEST, fieldName + " 값이 올바르지 않습니다.");
        }
    }
}

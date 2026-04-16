package com.genoutbound.gateway.core.logging;

public final class SensitiveLogMasker {

    private SensitiveLogMasker() {
    }

    public static String masked(Object value) {
        if (value == null) {
            return "null";
        }
        return value.getClass().getSimpleName() + "{masked}";
    }
}
package com.genoutbound.gateway.security.permission;

public final class PermissionCodes {

    public static final String ADMIN_UI = "PERM_ADMIN_UI";
    public static final String STATUS_READ = "PERM_STATUS_READ";
    public static final String CONFIG_READ = "PERM_CONFIG_READ";
    public static final String CONFIG_WRITE = "PERM_CONFIG_WRITE";
    public static final String OUTBOUND_READ = "PERM_OUTBOUND_READ";
    public static final String OUTBOUND_WRITE = "PERM_OUTBOUND_WRITE";
    public static final String STAT_READ = "PERM_STAT_READ";
    public static final String TSERVER_WRITE = "PERM_TSERVER_WRITE";
    public static final String SCS_READ = "PERM_SCS_READ";

    private static final java.util.List<String> ALL_CODES = java.util.List.of(
        ADMIN_UI,
        STATUS_READ,
        CONFIG_READ,
        CONFIG_WRITE,
        OUTBOUND_READ,
        OUTBOUND_WRITE,
        STAT_READ,
        TSERVER_WRITE,
        SCS_READ
    );

    private PermissionCodes() {
    }

    public static java.util.List<String> allCodes() {
        return ALL_CODES;
    }

    public static boolean isAllowed(String code) {
        return code != null && ALL_CODES.contains(code);
    }
}
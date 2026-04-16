package com.genoutbound.gateway.config;

public final class AdminConsolePaths {

    public static final String CONSOLE_BASE = "/console";
    public static final String WILDCARD = CONSOLE_BASE + "/**";

    public static final String SESSION_BASE = CONSOLE_BASE + "/session";
    public static final String SESSION_NEW = SESSION_BASE + "/new";
    public static final String SESSION_END = SESSION_BASE + "/end";

    public static final String USERS_BASE = CONSOLE_BASE + "/users";
    public static final String ROLES_BASE = CONSOLE_BASE + "/roles";
    public static final String PERMISSIONS_BASE = CONSOLE_BASE + "/permissions";

    private AdminConsolePaths() {
    }
}

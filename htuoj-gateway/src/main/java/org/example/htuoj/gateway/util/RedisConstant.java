package org.example.htuoj.gateway.util;

public class RedisConstant {
    public static final String USER_TOKEN_PREFIX = "user:token:";
    public static final Integer USER_TOKEN_TTL = 60;
    public static final String LOGIN_CODE_KEY = "login:code:";
    public static final String REGISTER_CODE_KEY = "register:code:";
    public static final Long LOGIN_CODE_TTL = 1L;
    public static final Long REGISTER_CODE_TTL = 1L;
    public static final String USER_TOP_LIST_KEY = "user:top:";
}

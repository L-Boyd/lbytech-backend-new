package com.lbytech.lbytech_backend_new.constant;

import java.util.concurrent.TimeUnit;

/**
 * redis相关常量
 */
public class RedisConstants {

    // 登录用户有效期，单位：秒
    public static final Long USER_LOGIN_TTL = 36000L;
    public static final TimeUnit USER_LOGIN_TTL_TIME_UNIT = TimeUnit.SECONDS;

}

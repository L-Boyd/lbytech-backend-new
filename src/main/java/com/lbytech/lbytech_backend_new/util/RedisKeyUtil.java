package com.lbytech.lbytech_backend_new.util;

import com.lbytech.lbytech_backend_new.constant.ThumbConstant;
import org.springframework.stereotype.Component;

@Component
public class RedisKeyUtil {

    public static String getUserThumbKey(String userEmail) {
        return ThumbConstant.USER_THUMB_KEY_PREFIX + userEmail;
    }

    public static String getTempThumbKey(String time) {
        return ThumbConstant.TEMP_THUMB_KEY_PREFIX.formatted(time);
    }

}

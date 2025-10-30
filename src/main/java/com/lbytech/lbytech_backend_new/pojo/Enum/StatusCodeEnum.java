package com.lbytech.lbytech_backend_new.pojo.Enum;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.HashMap;

/**
 * 状态码枚举类
 */
@Getter
public enum StatusCodeEnum {
    SUCCESS(200, "操作成功"),
    FAIL(400, "操作失败"),
    UNAUTHORIZED(401, "未授权");

    private int code;
    private String message;
    StatusCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    // 使用@JsonValue注解，让枚举序列化为Map对象
    @JsonValue
    public Object toJson() {
        return new HashMap<String, Object>() {{
            put("code", code);
            put("message", message);
        }};
    }
}

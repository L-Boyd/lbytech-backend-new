package com.lbytech.lbytech_backend_new.pojo.Enum;

/**
 * 状态码枚举类
 */
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
}

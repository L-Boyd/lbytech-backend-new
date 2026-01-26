package com.lbytech.lbytech_backend_new.pojo.Enum;

public enum SoftDeleteStatusEnum {
    NOT_DELETED(0, "未删除"),
    DELETED(1, "已删除");

    private final Integer code;
    private final String info;

    SoftDeleteStatusEnum(int code, String info) {
        this.code = code;
        this.info = info;
    }

    public Integer getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}

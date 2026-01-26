package com.lbytech.lbytech_backend_new.pojo.Enum;

public enum CommentStatusEnum {
    PENDING(0, "审核中"),
    APPROVED(1, "正常"),
    REJECTED(2, "禁止查看"),
    AMBIGUOUS(3, "待人工审核"),
    DISABLED(4, "已删除");


    private final Integer code;
    private final String info;

    CommentStatusEnum(Integer code, String info) {
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

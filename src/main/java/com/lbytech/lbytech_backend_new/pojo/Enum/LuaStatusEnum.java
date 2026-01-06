package com.lbytech.lbytech_backend_new.pojo.Enum;

import lombok.Getter;

/**
 * Lua脚本执行状态枚举
  */
@Getter
public enum LuaStatusEnum {  
    // 成功  
    SUCCESS(1L),
    // 失败  
    FAIL(-1L),
    // 笔记不存在
    NOTEBOOK_NOT_EXIST(-2L),
    ;
  
    private final long value;
  
    LuaStatusEnum(long value) {
        this.value = value;  
    }  
}

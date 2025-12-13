package com.lbytech.lbytech_backend_new.pojo.Enum;
  
import lombok.Getter;

/**
 * 点赞类型
  */
@Getter  
public enum ThumbTypeEnum {  
    // 点赞  
    INCR(1),  
    // 取消点赞  
    DECR(-1),  
    // 不发生改变  
    NON(0),  
    ;

    private final int value;

    ThumbTypeEnum(int value) {
        this.value = value;
    }
}

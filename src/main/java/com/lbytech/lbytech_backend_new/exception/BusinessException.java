package com.lbytech.lbytech_backend_new.exception;

import com.lbytech.lbytech_backend_new.pojo.Enum.StatusCodeEnum;
import lombok.Data;

@Data
public class BusinessException extends RuntimeException{

    // 异常码
    private StatusCodeEnum code;

    // 描述
    private String description;

    public BusinessException(StatusCodeEnum code, String description) {
        super(description);
        this.code = code;
        this.description = description;
    }

}

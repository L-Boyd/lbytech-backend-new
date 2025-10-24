package com.lbytech.lbytech_backend_new.pojo.vo;

import com.lbytech.lbytech_backend_new.pojo.Enum.StatusCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 返回给前端的数据
 */
@Data
@AllArgsConstructor
public class BaseResponse<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // 状态码
    private StatusCodeEnum statusCode;

    // 数据
    private T data;

}
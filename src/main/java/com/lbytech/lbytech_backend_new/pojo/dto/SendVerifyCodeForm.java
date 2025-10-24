package com.lbytech.lbytech_backend_new.pojo.dto;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

/**
 * 发送验证码表单
 */
@Data
public class SendVerifyCodeForm {

    @NotNull
    // 邮箱
    String email;

}

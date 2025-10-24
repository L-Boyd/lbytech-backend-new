package com.lbytech.lbytech_backend_new.pojo.dto;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class UserRegisterFrom {

    @NotNull
    // 邮箱
    String email;

    @NotNull
    // 密码
    String password;

    @NotNull
    // 发到邮箱的验证码
    String verifyCode;

}

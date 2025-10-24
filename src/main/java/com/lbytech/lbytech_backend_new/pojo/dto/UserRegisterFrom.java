package com.lbytech.lbytech_backend_new.pojo.dto;

import lombok.Data;

@Data
public class UserRegisterFrom {

    // 邮箱
    String email;

    // 密码
    String password;

    // 发到邮箱的验证码
    String verifyCode;

}

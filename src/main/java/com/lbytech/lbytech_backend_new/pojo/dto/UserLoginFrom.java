package com.lbytech.lbytech_backend_new.pojo.dto;

import lombok.Data;

@Data
public class UserLoginFrom {

    private String email;
    private String verifyCode;
    private String password;

}

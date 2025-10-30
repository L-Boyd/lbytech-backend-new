package com.lbytech.lbytech_backend_new.pojo.dto;

import lombok.Getter;

@Getter
public class ChangePasswordFrom {

    private String email;
    private String verifyCode;
    private String newPassword;

}

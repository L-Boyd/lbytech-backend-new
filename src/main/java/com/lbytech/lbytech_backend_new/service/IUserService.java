package com.lbytech.lbytech_backend_new.service;


public interface IUserService {

        /**
         * 发送验证码
         *
         * @param email
         */
        boolean sendVerifyCode(String email);

        /**
         * 用户注册
         *
         * @param email
         * @param password
         * @param verifyCode
         * @return
         */
        boolean userRegister(String email, String password, String verifyCode);


}

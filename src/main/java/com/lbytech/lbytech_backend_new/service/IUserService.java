package com.lbytech.lbytech_backend_new.service;


import com.lbytech.lbytech_backend_new.pojo.vo.UserVO;

public interface IUserService {

        /**
         * 发送验证码
         *
         * @param email
         */
        void sendVerifyCode(String email);

        /**
         * 用户注册
         *
         * @param email
         * @param password
         * @param verifyCode
         * @return
         */
        void userRegister(String email, String password, String verifyCode);

        /**
         * 用验证码登录
         *
         * @param email
         * @param password
         * @return
         */
        UserVO loginByVerifyCode(String email, String password);

         /**
          * 用密码登录
          *
          * @param email
          * @param password
          * @return
          */
        UserVO loginByPassword(String email, String password);

        /**
         * 更改密码
         *
         * @param email
         * @param verifyCode
         * @param newPassword
         */
        void changePassword(String email, String verifyCode, String newPassword);

}

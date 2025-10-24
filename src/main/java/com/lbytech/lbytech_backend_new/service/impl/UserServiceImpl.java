package com.lbytech.lbytech_backend_new.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.lbytech.lbytech_backend_new.service.IMailService;
import com.lbytech.lbytech_backend_new.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements IUserService {

    @Autowired
    private IMailService mailService;

    /**
     * 发送验证码
     *
     * @param email
     */
    @Override
    public boolean sendVerifyCode(String email) {
        // 生成6位验证码
        String verifyCode = RandomUtil.randomNumbers(6);
        // 发送验证码邮件
        mailService.sendVerifyCodeEmail(email, verifyCode);
        log.info("验证码已发送到邮箱: {}", email);
        return true;
    }

    /**
     * 用户注册
     *
     * @param email
     * @param password
     * @param verifyCode
     * @return
     */
    @Override
    public boolean userRegister(String email, String password, String verifyCode) {
        return true;
    }

}

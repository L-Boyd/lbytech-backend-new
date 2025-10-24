package com.lbytech.lbytech_backend_new.service;

import jakarta.mail.MessagingException;

/**
 * 邮件服务接口
 */
public interface IMailService {

    /**
     * 发送简单文本邮件
     */
    void sendSimpleMail(String to, String subject, String content);

    /**
     * 发送HTML邮件
     */
    void sendHtmlMail(String to, String subject, String content) throws MessagingException;

    /**
     * 发送验证码邮件
     */
    void sendVerifyCodeEmail(String toEmail, String verifyCode);

}

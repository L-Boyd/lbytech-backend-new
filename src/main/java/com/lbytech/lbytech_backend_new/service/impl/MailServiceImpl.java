package com.lbytech.lbytech_backend_new.service.impl;

import com.lbytech.lbytech_backend_new.service.IMailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements IMailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendSimpleMail(String toEmail, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(content);

        mailSender.send(message);
    }

    @Override
    public void sendHtmlMail(String to, String subject, String content) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
    }

    @Override
    public void sendVerifyCodeEmail(String toEmail, String verifyCode) {
        try {
            String subject = "【LBYtech】邮箱验证码";
            String content = buildVerifyCodeContent(verifyCode);

            sendHtmlMail(toEmail, subject, content);
        } catch (Exception e) {
            throw new RuntimeException("发送验证码邮件失败", e);
        }
    }

    /**
     * 构建验证码邮件内容
     */
    private String buildVerifyCodeContent(String verifyCode) {
        return "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>"
                + "<h2 style='color: #333;'>邮箱验证码</h2>"
                + "<p>您好！</p>"
                + "<p>您的验证码是：<strong style='color: #007bff; font-size: 20px;'>" + verifyCode + "</strong></p>"
                + "<p>验证码有效期为5分钟，请及时使用。</p>"
                + "<p style='color: #666; font-size: 12px;'>如非本人操作，请忽略此邮件。</p>"
                + "</div>";
    }
}

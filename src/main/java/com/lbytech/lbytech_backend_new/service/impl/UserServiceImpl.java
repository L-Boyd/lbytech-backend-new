package com.lbytech.lbytech_backend_new.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lbytech.lbytech_backend_new.mapper.UserMapper;
import com.lbytech.lbytech_backend_new.pojo.entity.User;
import com.lbytech.lbytech_backend_new.service.IMailService;
import com.lbytech.lbytech_backend_new.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private IMailService mailService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 发送验证码
     *
     * @param email
     */
    @Override
    public boolean sendVerifyCode(String email) {
        // 验证邮箱格式
        if (!isValidEmail(email)) {
            log.error("邮箱格式错误: {}", email);
            return false;
        }

        // 从Redis中获取验证码
        String cachedVerifyCode = stringRedisTemplate.opsForValue().get(email);
        if (StrUtil.isNotBlank(cachedVerifyCode)) {
            log.error("邮箱 {} 已发送验证码，5分钟内不能重复发送", email);
            return false;
        }

        // 生成6位验证码
        String verifyCode = RandomUtil.randomNumbers(6);

        // 缓存验证码，过期时间为5分钟
        stringRedisTemplate.opsForValue().set(email, verifyCode, 5, TimeUnit.MINUTES);

        // 发送验证码邮件
        mailService.sendVerifyCodeEmail(email, verifyCode);
        log.info("验证码已发送到邮箱: {}", email);

        return true;
    }

    /**
     * 验证邮箱格式
     */
    private boolean isValidEmail(String email) {
        if (StrUtil.isBlank(email)) {
            return false;
        }
        // 简单的邮箱格式验证
        // 邮箱格式：用户名@域名.后缀
        // 用户名：可以包含字母、数字、下划线、点号和加号
        // 域名：可以包含字母、数字和点号
        // 后缀：至少2个字符
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
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
        User user = this.lambdaQuery().eq(User::getEmail, email).one();
        if (user != null) {
            log.error("邮箱 {} 已注册", email);
            return false;
        }

        // 校验验证码
        String cachedVerifyCode = stringRedisTemplate.opsForValue().get(email);
        if (StrUtil.isBlank(cachedVerifyCode) || !cachedVerifyCode.equals(verifyCode)) {
            log.error("验证码错误或过期");
            return false;
        }

        // 创建用户
        user = new User(email, password);
        this.save(user);

        // 注册成功后，删除Redis中的验证码
        stringRedisTemplate.delete(email);

        return true;
    }

}

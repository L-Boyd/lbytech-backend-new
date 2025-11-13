package com.lbytech.lbytech_backend_new.intercepetor;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.lbytech.lbytech_backend_new.constant.RedisConstants;
import com.lbytech.lbytech_backend_new.pojo.entity.User;
import com.lbytech.lbytech_backend_new.pojo.vo.UserVO;
import com.lbytech.lbytech_backend_new.util.UserHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 刷新token拦截器
 * 所有请求都拦截，然后刷新token有效期
 */
public class RefreshTokenInterceptor implements HandlerInterceptor {

    private StringRedisTemplate stringRedisTemplate;

    public RefreshTokenInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求头中的token
        String token = request.getHeader("token");
        if (StrUtil.isBlank(token)) {
            return true;
        }
        // 基于token获取redis中的用户
        Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(token);
        // .entires会判断，如果没这个就会返回空Map
        if (userMap.isEmpty()) {
            return true;
        }
        // 存在，保存用户信息到ThreadLocal
        UserVO userVO = BeanUtil.fillBeanWithMap(userMap, new UserVO(), false);
        UserHolder.saveUser(userVO);

        // 刷新token有效期
        stringRedisTemplate.expire(token, RedisConstants.USER_LOGIN_TTL, RedisConstants.USER_LOGIN_TTL_TIME_UNIT);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 移除用户，防止内存泄漏
        UserHolder.removeUser();
    }

}
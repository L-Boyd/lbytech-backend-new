package com.lbytech.lbytech_backend_new.intercepetor;

import com.lbytech.lbytech_backend_new.exception.BusinessException;
import com.lbytech.lbytech_backend_new.pojo.Enum.StatusCodeEnum;
import com.lbytech.lbytech_backend_new.pojo.entity.User;
import com.lbytech.lbytech_backend_new.pojo.vo.UserVO;
import com.lbytech.lbytech_backend_new.util.UserHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 上传文件拦截器
 */
public class UploadInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断是否需要拦截（ThreadLocal中是否有用户）
        UserVO userVO = UserHolder.getUser();
        if (UserHolder.getUser() == null) {
            throw new BusinessException(StatusCodeEnum.UNAUTHORIZED, "用户未登录");
        } else if (!userVO.getEmail().equals("1508201988@qq.com")) {
            throw new BusinessException(StatusCodeEnum.UNAUTHORIZED, "用户权限不足");
        }

        // 放行
        return true;
    }

}
package com.lbytech.lbytech_backend_new.util;

import com.lbytech.lbytech_backend_new.pojo.vo.UserVO;

/**
 * 用户信息 holder
 */
public class UserHolder {
    private static final ThreadLocal<UserVO> threadLocal = new ThreadLocal<>();

    public static void saveUser(UserVO user){
        threadLocal.set(user);
    }

    public static UserVO getUser(){
        return threadLocal.get();
    }

    public static void removeUser(){
        threadLocal.remove();
    }
}
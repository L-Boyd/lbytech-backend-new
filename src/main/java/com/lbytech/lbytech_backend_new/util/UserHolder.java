package com.lbytech.lbytech_backend_new.util;

import com.lbytech.lbytech_backend_new.pojo.entity.User;

/**
 * 用户信息 holder
 */
public class UserHolder {
    private static final ThreadLocal<User> threadLocal = new ThreadLocal<>();

    public static void saveUser(User user){
        threadLocal.set(user);
    }

    public static User getUser(){
        return threadLocal.get();
    }

    public static void removeUser(){
        threadLocal.remove();
    }
}
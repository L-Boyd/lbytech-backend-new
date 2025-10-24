package com.lbytech.lbytech_backend_new.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 用户实体类
 */
@Data
public class User {
    // 主键
    private Integer id;

    // 邮箱
    private String email;

    // 密码
    private String password;

    public User(String password, String email) {
        this.password = password;
        this.email = email;
    }
}

package com.lbytech.lbytech_backend_new.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 用户实体类
 */
@AllArgsConstructor
@Data
public class User {
        // 主键
        private Long id;

        // 邮箱
        private String email;

        // 密码
        private String password;
}

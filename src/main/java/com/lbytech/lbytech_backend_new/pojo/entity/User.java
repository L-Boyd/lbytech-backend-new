package com.lbytech.lbytech_backend_new.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 用户实体类
 */
@Data
public class User {
    // 主键，使用数据库自增
    @TableId(type = IdType.AUTO)
    private Integer id;

    // 邮箱
    private String email;

    // 密码
    private String password;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}

package com.lbytech.lbytech_backend_new.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
@NoArgsConstructor  // 没有无参构造函数的话，会尝试把password往localDateTime映射然后报错。有无参构造函数，mybatis会用无参+setter来转换。
public class User {
    // 主键，使用数据库自增
    @TableId(type = IdType.AUTO)
    private Integer id;

    // 邮箱
    private String email;

    // 密码
    private String password;

    // 创建时间
    private LocalDateTime createTime;

    public User(String email, String password, LocalDateTime createTime) {
        this.email = email;
        this.password = password;
        this.createTime = createTime;
    }
}

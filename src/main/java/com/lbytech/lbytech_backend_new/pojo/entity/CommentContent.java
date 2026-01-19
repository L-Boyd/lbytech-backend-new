package com.lbytech.lbytech_backend_new.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("comment_contents")
@Data
public class CommentContent {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long commentId; // 关联的评论ID

    private String content; // 评论内容

    private LocalDateTime createTime; // 创建时间

    private LocalDateTime updateTime; // 更新时间

    private Integer isDelete; // 逻辑删除字段，0：未删除，1：已删除
}
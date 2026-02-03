package com.lbytech.lbytech_backend_new.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("comment_info")
@Data
public class CommentInfo {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String userEmail; // 用户Email

    private Integer notebookId; // 笔记id

    private Long parentId; // 关联的1级评论id，如果是一级评论，则值为0

    private Integer likeCount; // 点赞数

    private Integer status; // 状态，0：审核中，1：正常，2：禁止查看，3：等待人工审核，4：删除

    private LocalDateTime createTime; // 创建时间

    private LocalDateTime updateTime; // 更新时间

    private Integer isDeleted; // 逻辑删除字段，0：未删除，1：已删除
}
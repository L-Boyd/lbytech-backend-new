package com.lbytech.lbytech_backend_new.pojo.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
* 点赞记录表
*/
@Data
@TableName("thumb_record")
@NoArgsConstructor
public class ThumbRecord {

    /**
    * 记录id
    */
    private Long id;

    /**
    * 用户邮箱
    */
    private String userEmail;

    /**
    * 笔记id
    */
    private Integer notebookId;

    /**
    * 创建时间
    */
    private LocalDateTime createTime;

}

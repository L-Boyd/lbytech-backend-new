package com.lbytech.lbytech_backend_new.pojo.vo;

import lombok.Data;

@Data
public class NotebookVO {

    private Integer id;

    private String fileName;
    private String fileUrl;

    /**
     * 点赞数
     */
    private Integer thumbCount;

     /**
      * 是否已点赞
      */
    private Boolean isThumbed;

}

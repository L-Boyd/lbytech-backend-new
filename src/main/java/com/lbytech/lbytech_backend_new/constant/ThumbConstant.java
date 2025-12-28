package com.lbytech.lbytech_backend_new.constant;

public interface ThumbConstant {

    /**
     * redis用户点赞键前缀
     */
    String USER_THUMB_KEY_PREFIX = "thumb:";

    /**
     * redis临时点赞键前缀
     */
    String TEMP_THUMB_KEY_PREFIX = "thumb:temp:%s";

     /**
      * 未点赞状态值
      */
    Long UN_THUMB_CONSTANT = 0L;

}

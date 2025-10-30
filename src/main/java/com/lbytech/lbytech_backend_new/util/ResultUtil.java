package com.lbytech.lbytech_backend_new.util;

import com.lbytech.lbytech_backend_new.pojo.Enum.StatusCodeEnum;
import com.lbytech.lbytech_backend_new.pojo.vo.BaseResponse;

public class ResultUtil {

        /**
         * 成功
         *
         * @param data
         * @param <T>
         * @return
         */
        public static <T> BaseResponse<T> success(T data) {
            return new BaseResponse<>(StatusCodeEnum.SUCCESS, data);
        }

        /**
         * 失败
         *
         * @param data
         * @param <T>
         * @return
         */
        public static <T> BaseResponse<T> fail(T data) {
            return new BaseResponse<>(StatusCodeEnum.FAIL, data);
        }

        /**
         * 失败
         *
         * @param code
         * @param data
         * @param <T>
         * @return
         */
        public static <T> BaseResponse<T> fail(StatusCodeEnum code, T data) {
            return new BaseResponse<>(code, data);
        }
}

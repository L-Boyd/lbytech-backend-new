package com.lbytech.lbytech_backend_new.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.lbytech.lbytech_backend_new.exception.BusinessException;
import com.lbytech.lbytech_backend_new.pojo.Enum.StatusCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

/**
 * 阿里云OSS工具类
 */
@Slf4j
@Data
@AllArgsConstructor
public class AliOssUtil {

    private String endpoint;
    private String accessKeyID;
    private String accessKeySecret;
    private String bucketName;

    /**
     * 上传文件到OSS
     * @param file 文件
     * @return 文件URL
     */
    public String uploadFile(MultipartFile file) {

        // OSS客户端
        OSS ossClient = new OSSClientBuilder()
                .build(endpoint, accessKeyID, accessKeySecret);
        // 上传文件
        try {
            ossClient.putObject(bucketName, file.getOriginalFilename(), file.getInputStream());
        } catch (Exception e) {
            throw new BusinessException(StatusCodeEnum.FAIL, "文件上传失败");
        } finally {
            // 关闭OSS客户端
            ossClient.shutdown();
        }
        // 返回文件URL
        String url = "https://" + bucketName + "." + endpoint + "/" + file.getOriginalFilename();
        log.info("文件上传成功，URL为：{}", url);
        return url;

    }

    /**
     * 上传文件到OSS，自定义文件名
     * @param file 文件
     * @return 文件URL
     */
    public String uploadFile(MultipartFile file, String fileName) {

        // OSS客户端
        OSS ossClient = new OSSClientBuilder()
                .build(endpoint, accessKeyID, accessKeySecret);
        // 上传文件
        try {
            ossClient.putObject(bucketName, fileName, file.getInputStream());
        } catch (Exception e) {
            throw new BusinessException(StatusCodeEnum.FAIL, "文件上传失败");
        } finally {
            // 关闭OSS客户端
            ossClient.shutdown();
        }
        // 返回文件URL
        String url = "https://" + bucketName + "." + endpoint + "/" + file.getOriginalFilename();
        log.info("文件上传成功，URL为：{}", url);
        return url;

    }
}

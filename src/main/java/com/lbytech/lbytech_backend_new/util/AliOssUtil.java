package com.lbytech.lbytech_backend_new.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
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
    public String uploadFile(MultipartFile file) throws Exception {

        // OSS客户端
        OSS ossClient = new OSSClientBuilder()
                .build(endpoint, accessKeyID, accessKeySecret);
        // 上传文件
        ossClient.putObject(bucketName, file.getOriginalFilename(), file.getInputStream());
        // 关闭OSS客户端
        ossClient.shutdown();
        // 返回文件URL
        String url = "https://" + bucketName + "." + endpoint + "/" + file.getOriginalFilename();
        log.info("文件上传成功，URL为：{}", url);
        return url;

    }
}

package com.lbytech.lbytech_backend_new.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import com.lbytech.lbytech_backend_new.exception.BusinessException;
import com.lbytech.lbytech_backend_new.pojo.Enum.StatusCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

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
     *
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
     *
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

    /**
     * 从OSS读取文件内容
     *
     * @param fileUrl 文件URL
     * @return 文件内容
     */
    public String getMdFileContent(String fileUrl) {
        // 校验参数
        if (fileUrl == null) {
            throw new BusinessException(StatusCodeEnum.FAIL, "OSS文件URL为空，无法读取内容");
        }

        OSS ossClient = new OSSClientBuilder()
                .build(endpoint, accessKeyID, accessKeySecret);

        OSSObject ossObject = null;
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            // 解析OSS文件URL，获取文件的objectName（OSS中文件的唯一路径）
            String objectName = this.parseObjectNameFromUrl(fileUrl);
            log.info("解析OSS URL成功，objectName：{}", objectName);

            // 检查文件是否存在（避免文件不存在导致的异常）
            if (!ossClient.doesObjectExist(bucketName, objectName)) {
                throw new BusinessException(StatusCodeEnum.FAIL, "OSS文件不存在：" + fileUrl);
            }

            // 获取OSS文件对象，打开输入流
            ossObject = ossClient.getObject(bucketName, objectName);
            inputStream = ossObject.getObjectContent();

            // 读取文件内容（markdown文件用UTF-8编码，避免中文乱码）
            reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            StringBuilder contentBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line).append("\n"); // 保留换行符，保证markdown格式
            }

            // 返回文件内容
            String content = contentBuilder.toString();
            log.info("读取OSS文件内容成功，文件大小：{} 字符", content.length());
            return content;
        } catch (Exception e) {
            throw new BusinessException(StatusCodeEnum.FAIL, "读取OSS文件失败：" + e.getMessage());
        } finally {
            // 关闭资源
            try {
                if (reader != null) reader.close();
                if (inputStream != null) inputStream.close();
                if (ossObject != null) ossObject.close();
                if (ossClient != null) ossClient.shutdown();
            } catch (Exception e) {
                log.error("关闭OSS资源失败", e);
            }
        }
    }

    /**
     * 辅助方法：从OSS完整URL解析出objectName
     * 示例URL：https://lbytech-notebook.oss-cn-beijing.aliyuncs.com/notebook/2024/01/test.md
     * 解析后objectName：notebook/2024/01/test.md
     */
    private String parseObjectNameFromUrl(String fileUrl) throws Exception {
        try {
            URL url = new URL(fileUrl);
            // 获取URL的路径部分（比如：/notebook/2024/01/test.md），去掉开头的/
            String path = url.getPath().substring(1);
            // 解码URL编码的字符（比如空格%20、中文等）
            return java.net.URLDecoder.decode(path, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new BusinessException(StatusCodeEnum.FAIL, "解析OSS URL失败：" + e.getMessage());
        }
    }
}

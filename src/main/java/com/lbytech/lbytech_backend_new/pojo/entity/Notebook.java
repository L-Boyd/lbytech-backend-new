package com.lbytech.lbytech_backend_new.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@TableName("notebook")
@Data
@NoArgsConstructor
public class Notebook {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("file_name")
    private String fileName;

    @TableField("file_url")
    private String fileUrl;

    @TableField("create_time")
    private LocalDateTime createTime;

    public Notebook(String fileName, String fileUrl, LocalDateTime createTime) {
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object o) {
        return this.fileName.equals(((Notebook) o).getFileName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName, fileUrl);
    }
}

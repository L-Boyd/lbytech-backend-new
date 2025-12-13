package com.lbytech.lbytech_backend_new.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lbytech.lbytech_backend_new.pojo.entity.Notebook;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.Map;

public interface NotebookMapper extends BaseMapper<Notebook> {

    // <notebookId, 点赞数>
    void batchUpdateThumbCount(@Param("countMap") Map<Integer, Integer> countMap);

}

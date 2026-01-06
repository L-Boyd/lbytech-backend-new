package com.lbytech.lbytech_backend_new.runner;


import com.lbytech.lbytech_backend_new.pojo.entity.Notebook;
import com.lbytech.lbytech_backend_new.service.INotebookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 预热缓存逻辑:
 * 1. 预热笔记id集合缓存
 */
@Component
@Order(1)   // n越小，优先级越高
@Slf4j
public class CacheRunner implements CommandLineRunner {

    @Autowired
    private INotebookService notebookService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void run(String... args) throws Exception {
        log.info("Running CacheRunner");

        List<Notebook> notebookList = notebookService.list();
        List<String> notebookIdList = notebookList.stream()
                .map(Notebook::getId)
                .map(String::valueOf)
                .collect(Collectors.toList());

        stringRedisTemplate.delete("notebook:id_set");
        stringRedisTemplate.opsForSet()
                .add("notebook:id_set", notebookIdList.toArray(new String[0]));

        log.info("notebookIdList: {}", notebookIdList);
        log.info("CacheRunner finished");
    }
}

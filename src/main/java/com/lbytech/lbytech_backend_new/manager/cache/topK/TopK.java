package com.lbytech.lbytech_backend_new.manager.cache.topK;

import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * https://github.com/go-kratos/aegis/tree/main/topk.
 * TopK 算法的接口.
 */
public interface TopK {
    AddResult add(String key, int increment);

    List<Item> list();

    BlockingQueue<Item> expelled();

    void fading();

    long total();
}

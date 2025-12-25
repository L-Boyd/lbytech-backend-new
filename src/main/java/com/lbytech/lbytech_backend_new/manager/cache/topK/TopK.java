package com.lbytech.lbytech_backend_new.manager.cache.topK;

import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * https://github.com/go-kratos/aegis/tree/main/topk.
 * TopK 算法的接口.
 */
public interface TopK {
    AddResult add(String key, int increment);

    /**
     * @return 当前 TopK 中的所有项
     */
    List<Item> list();

    /**
     * @return 被挤出的项的队列
     */
    BlockingQueue<Item> expelled();

    /**
     * 执行衰减操作，减少所有项的计数.
     */
    void fading();

    /**
     * @return 当前 TopK 中的所有项的总计数
     */
    long total();
}

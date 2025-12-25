package com.lbytech.lbytech_backend_new.manager.cache.topK;

import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * https://github.com/go-kratos/aegis/tree/main/topk.
 * TopK 算法的接口.
 */
public interface TopK {
    /**
     * 添加一个项到 TopK 中.
     * @param key 项的键
     * @param increment 项的增量
     * @return 添加结果，包含被挤出的项、是否是热门项、以及项的键
     */
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

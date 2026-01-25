package com.lbytech.lbytech_backend_new.ai.service.strategy;

import com.lbytech.lbytech_backend_new.ai.pojo.ChatModelEnum;
import com.lbytech.lbytech_backend_new.exception.BusinessException;
import com.lbytech.lbytech_backend_new.pojo.Enum.StatusCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 支付策略工厂类
 * 封装策略的创建逻辑，客户端只传标识，无需手动new策略对象
 */
@Component
public class AiServiceFactory {

    private static final Map<ChatModelEnum, AiServiceStrategy> aiServiceMap = new HashMap<>();

    public AiServiceFactory(List<AiServiceStrategy> strategyList) {
        strategyList.forEach(strategy -> {
            aiServiceMap.put(strategy.getModel(), strategy);
        });
    }

    // 根据支付类型，返回对应的支付策略
    public static AiServiceStrategy getStrategy(ChatModelEnum model) {
        AiServiceStrategy strategy = aiServiceMap.get(model);
        if (strategy == null) {
            throw new BusinessException(StatusCodeEnum.FAIL, "不支持的模型：" + model);
        }
        return strategy;
    }
}
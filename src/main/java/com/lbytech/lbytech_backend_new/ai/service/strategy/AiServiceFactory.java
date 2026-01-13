package com.lbytech.lbytech_backend_new.ai.service.strategy;

import com.lbytech.lbytech_backend_new.ai.pojo.ChatModelEnum;
import com.lbytech.lbytech_backend_new.exception.BusinessException;
import com.lbytech.lbytech_backend_new.pojo.Enum.StatusCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * 支付策略工厂类（可选，企业级优化）
 * 封装策略的创建逻辑，客户端只传标识，无需手动new策略对象
 */
@Component
public class AiServiceFactory {

    private static DeepSeekAiServiceStrategy deepSeekAiServiceStrategy;

    private static QwenAiServiceStrategy qwenAiServiceStrategy;

    public AiServiceFactory(DeepSeekAiServiceStrategy deepSeekAiServiceStrategy, QwenAiServiceStrategy qwenAiServiceStrategy) {
        this.deepSeekAiServiceStrategy = deepSeekAiServiceStrategy;
        this.qwenAiServiceStrategy = qwenAiServiceStrategy;
    }

    // 根据支付类型，返回对应的支付策略
    public static AiServiceStrategy getStrategy(ChatModelEnum model) {
        if (model == ChatModelEnum.DEEPSEEK) {
            return deepSeekAiServiceStrategy;
        } else if (model == ChatModelEnum.QWEN) {
            return qwenAiServiceStrategy;
        } else {
            throw new BusinessException(StatusCodeEnum.FAIL, "不支持的模型：" + model);
        }
    }
}
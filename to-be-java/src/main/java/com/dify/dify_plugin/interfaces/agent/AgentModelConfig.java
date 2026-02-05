package com.dify.dify_plugin.interfaces.agent;

import com.dify.dify_plugin.entities.model.message.PromptMessage;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

/**
 * Agent 模型配置。
 * 对应 Python 版本的 AgentModelConfig。
 */
@Getter
public class AgentModelConfig {
    /** 模型提供方。 */
    private final String provider;
    /** 模型名称。 */
    private final String model;
    /** 模型模式（如 chat）。 */
    private final String mode;
    /** 历史提示消息列表，每个实例都应该独立。 */
    private final List<PromptMessage> historyPromptMessages;

    public AgentModelConfig(String provider, String model, String mode) {
        this.provider = provider;
        this.model = model;
        this.mode = mode;
        // 每次构造都创建新的列表，避免多个实例共享同一引用。
        this.historyPromptMessages = new ArrayList<>();
    }
}

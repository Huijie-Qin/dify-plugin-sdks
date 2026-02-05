package com.dify.dify_plugin.interfaces.agent;

import com.dify.dify_plugin.core.runtime.Session;
import com.dify.dify_plugin.entities.agent.AgentInvokeMessage;
import com.dify.dify_plugin.entities.agent.AgentRuntime;
import java.util.Collections;
import java.util.Map;

/**
 * Agent 策略基类。
 * 对应 Python 的 AgentStrategy，构造函数不可被覆盖。
 */
public abstract class AgentStrategy {
    /** 运行时信息。 */
    protected final AgentRuntime runtime;
    /** 会话信息。 */
    protected final Session session;

    protected AgentStrategy(AgentRuntime runtime, Session session) {
        this.runtime = runtime;
        this.session = session;
    }

    /**
     * 创建文本消息的快捷方法。
     *
     * @param content 文本内容
     * @return Agent 调用消息
     */
    protected AgentInvokeMessage createTextMessage(String content) {
        return new AgentInvokeMessage(content);
    }

    /**
     * 子类实现具体调用逻辑。
     *
     * @param parameters 调用参数
     * @return 迭代消息结果
     */
    protected abstract Iterable<AgentInvokeMessage> invoke(Map<String, Object> parameters);

    /**
     * 提供一个默认空实现，便于测试或占位调用。
     */
    protected Iterable<AgentInvokeMessage> emptyInvoke() {
        return Collections.emptyList();
    }
}

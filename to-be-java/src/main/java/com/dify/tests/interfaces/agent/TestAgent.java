package com.dify.tests.interfaces.agent;

import com.dify.dify_plugin.core.runtime.Session;
import com.dify.dify_plugin.core.server.stdio.request_reader.StdioRequestReader;
import com.dify.dify_plugin.core.server.stdio.response_writer.StdioResponseWriter;
import com.dify.dify_plugin.entities.agent.AgentInvokeMessage;
import com.dify.dify_plugin.entities.agent.AgentRuntime;
import com.dify.dify_plugin.entities.model.message.PromptMessage;
import com.dify.dify_plugin.entities.model.message.PromptMessageRole;
import com.dify.dify_plugin.interfaces.agent.AgentModelConfig;
import com.dify.dify_plugin.interfaces.agent.AgentStrategy;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 对应 python/tests/interfaces/agent/test_agent.py 的逻辑实现。
 */
public class TestAgent {
    /**
     * 构造 AgentModelConfig 的辅助方法。
     */
    private AgentModelConfig makeAgentModelConfig() {
        return new AgentModelConfig(
                "openai",
                "gpt-4o-mini",
                "chat"
        );
    }

    /**
     * 验证两个配置实例的 historyPromptMessages 不共享引用。
     */
    public void testAgentModelConfigEnsureHistoryPromptMessagesNotShared() {
        PromptMessage promptMessage = new PromptMessage(
                PromptMessageRole.USER,
                "Content",
                null
        );
        AgentModelConfig cfg1 = makeAgentModelConfig();
        AgentModelConfig cfg2 = makeAgentModelConfig();

        assert cfg1.getHistoryPromptMessages() != cfg2.getHistoryPromptMessages();

        // 修改 cfg1 的历史消息列表不应影响 cfg2
        cfg1.getHistoryPromptMessages().add(promptMessage);
        assert cfg2.getHistoryPromptMessages().isEmpty();
    }

    /**
     * 验证 AgentStrategy 构造函数未被覆盖。
     */
    public void testConstructorOfAgentStrategy() {
        class AgentStrategyImpl extends AgentStrategy {
            protected AgentStrategyImpl(AgentRuntime runtime, Session session) {
                super(runtime, session);
            }

            @Override
            protected Iterable<AgentInvokeMessage> invoke(java.util.Map<String, Object> parameters) {
                return Collections.singletonList(createTextMessage("Hello, world!"));
            }
        }

        ExecutorService executor = Executors.newFixedThreadPool(1);
        Session session = new Session(
                "test",
                executor,
                new StdioRequestReader(),
                new StdioResponseWriter()
        );
        AgentStrategy agentStrategy = new AgentStrategyImpl(
                new AgentRuntime("test"),
                session
        );
        assert agentStrategy != null;
        executor.shutdown();
    }
}

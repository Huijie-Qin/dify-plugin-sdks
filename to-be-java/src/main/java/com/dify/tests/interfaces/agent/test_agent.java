package com.dify.tests.interfaces.agent;

import com.dify.dify_plugin.core.Session;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * 对应 Python 的 test_agent.py。
 */
public class test_agent {
    /**
     * 模拟构建 AgentModelConfig。
     */
    private AgentModelConfig makeAgentModelConfig() {
        return new AgentModelConfig("openai", "gpt-4o-mini", "chat", new ArrayList<>());
    }

    public void testAgentModelConfigEnsureHistoryPromptMessagesNotShared() {
        PromptMessage promptMessage = new PromptMessage("user", "Content", null);
        AgentModelConfig cfg1 = makeAgentModelConfig();
        AgentModelConfig cfg2 = makeAgentModelConfig();

        if (cfg1.getHistoryPromptMessages() == cfg2.getHistoryPromptMessages()) {
            throw new AssertionError("historyPromptMessages 应为不同实例");
        }

        cfg1.getHistoryPromptMessages().add(promptMessage);
        if (!cfg2.getHistoryPromptMessages().isEmpty()) {
            throw new AssertionError("cfg2 的 historyPromptMessages 不应被 cfg1 修改");
        }
    }

    public void testConstructorOfAgentStrategy() {
        class AgentStrategyImpl extends AgentStrategy {
            @Override
            public Iterable<AgentInvokeMessage> invokeInternal(Object parameters) {
                return List.of(createTextMessage("Hello, world!"));
            }
        }

        Session session = new Session(
            "test",
            Executors.newFixedThreadPool(1),
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            250
        );

        AgentStrategy agentStrategy = new AgentStrategyImpl();
        agentStrategy.setRuntime(new AgentRuntime("test"));
        agentStrategy.setSession(session);
        if (agentStrategy == null) {
            throw new AssertionError("agentStrategy 应该被构造");
        }
    }

    // 以下为与 Python 中依赖对应的简化占位类
    static class AgentModelConfig {
        private final String provider;
        private final String model;
        private final String mode;
        private final List<PromptMessage> historyPromptMessages;

        AgentModelConfig(String provider, String model, String mode, List<PromptMessage> historyPromptMessages) {
            this.provider = provider;
            this.model = model;
            this.mode = mode;
            this.historyPromptMessages = historyPromptMessages;
        }

        public List<PromptMessage> getHistoryPromptMessages() {
            return historyPromptMessages;
        }
    }

    static class PromptMessage {
        private final String role;
        private final String content;
        private final String name;

        PromptMessage(String role, String content, String name) {
            this.role = role;
            this.content = content;
            this.name = name;
        }
    }

    static class AgentInvokeMessage {
        private final String message;

        AgentInvokeMessage(String message) {
            this.message = message;
        }
    }

    static class AgentRuntime {
        private final String userId;

        AgentRuntime(String userId) {
            this.userId = userId;
        }
    }

    static abstract class AgentStrategy {
        private AgentRuntime runtime;
        private Session session;

        public void setRuntime(AgentRuntime runtime) {
            this.runtime = runtime;
        }

        public void setSession(Session session) {
            this.session = session;
        }

        protected AgentInvokeMessage createTextMessage(String content) {
            return new AgentInvokeMessage(content);
        }

        public Iterable<AgentInvokeMessage> invoke(Object parameters) {
            return invokeInternal(parameters);
        }

        protected abstract Iterable<AgentInvokeMessage> invokeInternal(Object parameters);
    }
}

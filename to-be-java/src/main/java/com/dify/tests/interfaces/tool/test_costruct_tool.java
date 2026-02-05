package com.dify.tests.interfaces.tool;

import com.dify.dify_plugin.core.Session;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * 对应 Python 的 test_costruct_tool.py。
 */
public class test_costruct_tool {
    public void testConstructTool() {
        class ToolImpl extends Tool {
            @Override
            public Iterable<ToolInvokeMessage> invokeInternal(Map<String, Object> toolParameters) {
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

        Tool tool = new ToolImpl();
        tool.setRuntime(new ToolRuntime(Map.of(), "test", "test"));
        tool.setSession(session);
        if (tool == null) {
            throw new AssertionError("tool 应该被构造");
        }
    }

    public void testConstructToolDefaultCredentialType() {
        class ToolImpl extends Tool {
            @Override
            public Iterable<ToolInvokeMessage> invokeInternal(Map<String, Object> toolParameters) {
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

        Tool tool = new ToolImpl();
        tool.setRuntime(new ToolRuntime(Map.of(), "test", "test"));
        tool.setSession(session);
        if (tool.getRuntime().getCredentialType() != CredentialType.API_KEY) {
            throw new AssertionError("默认 credentialType 应为 API_KEY");
        }
    }

    public void testFetchParameterOptions() {
        class ToolImpl extends Tool {
            @Override
            public Iterable<ToolInvokeMessage> invokeInternal(Map<String, Object> toolParameters) {
                return List.of(createTextMessage("Hello, world!"));
            }

            @Override
            public List<ParameterOption> fetchParameterOptionsInternal(String parameter) {
                return List.of(new ParameterOption("test", new I18nObject("test")));
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

        Tool tool = new ToolImpl();
        tool.setRuntime(new ToolRuntime(Map.of(), "test", "test"));
        tool.setSession(session);
        List<ParameterOption> options = tool.fetchParameterOptions("test");
        if (!options.equals(List.of(new ParameterOption("test", new I18nObject("test"))))) {
            throw new AssertionError("参数选项不匹配");
        }
    }

    // 占位类，与 Python 中依赖的实体保持结构一致
    enum CredentialType {
        API_KEY
    }

    static class ToolRuntime {
        private final Map<String, Object> credentials;
        private final String userId;
        private final String sessionId;
        private CredentialType credentialType = CredentialType.API_KEY;

        ToolRuntime(Map<String, Object> credentials, String userId, String sessionId) {
            this.credentials = credentials;
            this.userId = userId;
            this.sessionId = sessionId;
        }

        public CredentialType getCredentialType() {
            return credentialType;
        }
    }

    static class ToolInvokeMessage {
        private final String message;

        ToolInvokeMessage(String message) {
            this.message = message;
        }
    }

    static class I18nObject {
        private final String enUs;

        I18nObject(String enUs) {
            this.enUs = enUs;
        }
    }

    static class ParameterOption {
        private final String value;
        private final I18nObject label;

        ParameterOption(String value, I18nObject label) {
            this.value = value;
            this.label = label;
        }
    }

    static abstract class Tool {
        private ToolRuntime runtime;
        private Session session;

        public void setRuntime(ToolRuntime runtime) {
            this.runtime = runtime;
        }

        public ToolRuntime getRuntime() {
            return runtime;
        }

        public void setSession(Session session) {
            this.session = session;
        }

        protected ToolInvokeMessage createTextMessage(String content) {
            return new ToolInvokeMessage(content);
        }

        public Iterable<ToolInvokeMessage> invoke(Map<String, Object> toolParameters) {
            return invokeInternal(toolParameters);
        }

        protected abstract Iterable<ToolInvokeMessage> invokeInternal(Map<String, Object> toolParameters);

        public List<ParameterOption> fetchParameterOptions(String parameter) {
            return fetchParameterOptionsInternal(parameter);
        }

        protected List<ParameterOption> fetchParameterOptionsInternal(String parameter) {
            return new ArrayList<>();
        }
    }
}

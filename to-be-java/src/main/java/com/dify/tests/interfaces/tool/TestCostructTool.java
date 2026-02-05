package com.dify.tests.interfaces.tool;

import com.dify.dify_plugin.core.runtime.Session;
import com.dify.dify_plugin.core.server.stdio.request_reader.StdioRequestReader;
import com.dify.dify_plugin.core.server.stdio.response_writer.StdioResponseWriter;
import com.dify.dify_plugin.entities.I18nObject;
import com.dify.dify_plugin.entities.ParameterOption;
import com.dify.dify_plugin.entities.provider_config.CredentialType;
import com.dify.dify_plugin.entities.tool.ToolInvokeMessage;
import com.dify.dify_plugin.entities.tool.ToolRuntime;
import com.dify.dify_plugin.interfaces.tool.Tool;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 对应 python/tests/interfaces/tool/test_costruct_tool.py 的逻辑实现。
 */
public class TestCostructTool {
    /**
     * 测试 Tool 的构造函数未被覆盖。
     */
    public void testConstructTool() {
        class ToolImpl extends Tool {
            protected ToolImpl(ToolRuntime runtime, Session session) {
                super(runtime, session);
            }

            @Override
            protected Iterable<ToolInvokeMessage> invoke(Map<String, Object> toolParameters) {
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

        Tool tool = new ToolImpl(
                new ToolRuntime(Collections.emptyMap(), "test", "test"),
                session
        );
        assert tool != null;
        executor.shutdown();
    }

    /**
     * 测试 ToolRuntime 默认凭证类型为 API_KEY。
     */
    public void testConstructToolDefaultCredentialType() {
        class ToolImpl extends Tool {
            protected ToolImpl(ToolRuntime runtime, Session session) {
                super(runtime, session);
            }

            @Override
            protected Iterable<ToolInvokeMessage> invoke(Map<String, Object> toolParameters) {
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

        Tool tool = new ToolImpl(
                new ToolRuntime(Collections.emptyMap(), "test", "test"),
                session
        );
        assert tool != null;
        assert tool.getRuntime().getCredentialType() == CredentialType.API_KEY;
        executor.shutdown();
    }

    /**
     * 测试 Tool 可以获取参数选项。
     */
    public void testFetchParameterOptions() {
        class ToolImpl extends Tool {
            protected ToolImpl(ToolRuntime runtime, Session session) {
                super(runtime, session);
            }

            @Override
            protected Iterable<ToolInvokeMessage> invoke(Map<String, Object> toolParameters) {
                return Collections.singletonList(createTextMessage("Hello, world!"));
            }

            @Override
            protected List<ParameterOption> fetchParameterOptionsInternal(String parameter) {
                return List.of(new ParameterOption("test", new I18nObject("test")));
            }
        }

        ExecutorService executor = Executors.newFixedThreadPool(1);
        Session session = new Session(
                "test",
                executor,
                new StdioRequestReader(),
                new StdioResponseWriter()
        );

        Tool tool = new ToolImpl(
                new ToolRuntime(Collections.emptyMap(), "test", "test"),
                session
        );
        List<ParameterOption> expected = List.of(new ParameterOption("test", new I18nObject("test")));
        assert tool.fetchParameterOptions("test").equals(expected);
        executor.shutdown();
    }
}

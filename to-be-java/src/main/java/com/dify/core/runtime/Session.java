package com.dify.core.runtime;

import com.dify.config.config.InstallMethod;
import com.dify.core.server.__base.request_reader.RequestReader;
import com.dify.core.server.__base.response_writer.ResponseWriter;
import com.dify.core.server.tcp.request_reader.TCPReaderWriter;
import com.dify.invocations.File;
import com.dify.invocations.StorageInvocation;
import com.dify.invocations.ToolInvocation;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.Getter;

/**
 * 运行时 Session，负责保存上下文与注册各类调用器。
 */
@Getter
public class Session {

    /**
     * 当前 Session ID。
     */
    private final String sessionId;

    /**
     * 线程池执行器。
     */
    private final ExecutorService executor;

    /**
     * 请求读取器。
     */
    private final RequestReader reader;

    /**
     * 响应写入器。
     */
    private final ResponseWriter writer;

    /**
     * 会话级上下文。
     */
    private final SessionContext context;

    /**
     * 对话 ID。
     */
    private final String conversationId;

    /**
     * 消息 ID。
     */
    private final String messageId;

    /**
     * 应用 ID。
     */
    private final String appId;

    /**
     * 端点 ID。
     */
    private final String endpointId;

    /**
     * 安装方式。
     */
    private final InstallMethod installMethod;

    /**
     * Dify 插件守护进程地址。
     */
    private final String difyPluginDaemonUrl;

    /**
     * 最大调用超时时间（秒）。
     */
    private final int maxInvocationTimeout;

    /**
     * 模型调用集合。
     */
    private ModelInvocations model;

    /**
     * 工具调用。
     */
    private ToolInvocation tool;

    /**
     * 应用调用集合。
     */
    private AppInvocations app;

    /**
     * 工作流节点调用集合。
     */
    private WorkflowNodeInvocations workflowNode;

    /**
     * 存储调用。
     */
    private StorageInvocation storage;

    /**
     * 文件调用。
     */
    private File file;

    public Session(
        String sessionId,
        ExecutorService executor,
        RequestReader reader,
        ResponseWriter writer,
        InstallMethod installMethod,
        String difyPluginDaemonUrl,
        String conversationId,
        String messageId,
        String appId,
        String endpointId,
        SessionContext context,
        int maxInvocationTimeout
    ) {
        this.sessionId = sessionId;
        this.executor = executor;
        this.reader = reader;
        this.writer = writer;
        this.installMethod = installMethod;
        this.difyPluginDaemonUrl = difyPluginDaemonUrl;
        this.conversationId = conversationId;
        this.messageId = messageId;
        this.appId = appId;
        this.endpointId = endpointId;
        this.context = context == null ? new SessionContext() : context;
        this.maxInvocationTimeout = maxInvocationTimeout;

        registerInvocations();
    }

    /**
     * 注册各类调用器。
     */
    private void registerInvocations() {
        this.model = new ModelInvocations(this);
        this.tool = new ToolInvocation(this);
        this.app = new AppInvocations(this);
        this.workflowNode = new WorkflowNodeInvocations(this);
        this.storage = new StorageInvocation(this);
        this.file = new File(this);
    }

    /**
     * 创建空 Session（用于测试或占位）。
     */
    public static Session emptySession() {
        return new Session(
            "",
            Executors.newSingleThreadExecutor(),
            new TCPReaderWriter("", 0, ""),
            new TCPReaderWriter("", 0, ""),
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            250
        );
    }

    /**
     * 判断是否支持反向调用。
     */
    public boolean supportsBackwardsInvoke() {
        return Objects.nonNull(sessionId) && writer != null;
    }
}

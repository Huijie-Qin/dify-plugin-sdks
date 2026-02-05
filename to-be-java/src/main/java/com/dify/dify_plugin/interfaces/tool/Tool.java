package com.dify.dify_plugin.interfaces.tool;

import com.dify.dify_plugin.core.runtime.Session;
import com.dify.dify_plugin.entities.ParameterOption;
import com.dify.dify_plugin.entities.tool.ToolInvokeMessage;
import com.dify.dify_plugin.entities.tool.ToolRuntime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Tool 基类。
 */
public abstract class Tool {
    /** 运行时信息。 */
    protected final ToolRuntime runtime;
    /** 会话信息。 */
    protected final Session session;

    protected Tool(ToolRuntime runtime, Session session) {
        this.runtime = runtime;
        this.session = session;
    }

    /**
     * 创建文本消息的快捷方法。
     *
     * @param content 文本内容
     * @return Tool 调用消息
     */
    protected ToolInvokeMessage createTextMessage(String content) {
        return new ToolInvokeMessage(content);
    }

    /**
     * 子类实现具体调用逻辑。
     *
     * @param toolParameters 工具参数
     * @return 迭代消息结果
     */
    protected abstract Iterable<ToolInvokeMessage> invoke(Map<String, Object> toolParameters);

    /**
     * 对外提供参数选项获取入口。
     *
     * @param parameter 参数名称
     * @return 参数选项列表
     */
    public List<ParameterOption> fetchParameterOptions(String parameter) {
        return fetchParameterOptionsInternal(parameter);
    }

    /**
     * 供子类重写的参数选项获取方法，默认返回空列表。
     */
    protected List<ParameterOption> fetchParameterOptionsInternal(String parameter) {
        return Collections.emptyList();
    }

    public ToolRuntime getRuntime() {
        return runtime;
    }
}

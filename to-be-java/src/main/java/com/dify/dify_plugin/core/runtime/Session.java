package com.dify.dify_plugin.core.runtime;

import com.dify.dify_plugin.core.server.stdio.request_reader.StdioRequestReader;
import com.dify.dify_plugin.core.server.stdio.response_writer.StdioResponseWriter;
import java.util.concurrent.ExecutorService;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 会话对象，保存会话运行所需的基础组件。
 * 对应 Python 的 Session，用于在测试中验证构造逻辑。
 */
@Getter
@AllArgsConstructor
public class Session {
    /** 会话唯一标识。 */
    private final String sessionId;
    /** 执行器，用于并发任务。 */
    private final ExecutorService executor;
    /** 标准输入读取器。 */
    private final StdioRequestReader reader;
    /** 标准输出写入器。 */
    private final StdioResponseWriter writer;
}

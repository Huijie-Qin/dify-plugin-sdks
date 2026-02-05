package com.dify.dify_plugin.core.server.serverless;

import com.dify.dify_plugin.core.server.__base.response_writer;

/**
 * 对应 Python 的 ServerlessResponseWriter。
 */
public class response_writer extends response_writer {
    @Override
    public void sessionMessage(String sessionId, byte[] data) {
        // Serverless 环境下的输出逻辑
    }

    @Override
    public byte[] sessionMessageText(String sessionId, byte[] data) {
        return data;
    }
}

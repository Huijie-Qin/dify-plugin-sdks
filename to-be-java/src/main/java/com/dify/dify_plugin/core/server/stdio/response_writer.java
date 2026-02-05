package com.dify.dify_plugin.core.server.stdio;

import com.dify.dify_plugin.core.server.__base.response_writer;

/**
 * 对应 Python 的 StdioResponseWriter。
 */
public class response_writer extends response_writer {
    @Override
    public void sessionMessage(String sessionId, byte[] data) {
        // Python 中写入 stdout
    }

    @Override
    public byte[] sessionMessageText(String sessionId, byte[] data) {
        // Python 中写入 stdout，并返回文本
        return data;
    }
}

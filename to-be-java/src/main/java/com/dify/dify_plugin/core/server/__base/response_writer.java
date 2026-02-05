package com.dify.dify_plugin.core.server.__base;

import java.util.HashMap;
import java.util.Map;

/**
 * 对应 Python 的 ResponseWriter 抽象类。
 */
public abstract class response_writer {
    /**
     * 写入 session 消息。
     */
    public abstract void sessionMessage(String sessionId, byte[] data);

    /**
     * 写入 session 文本消息。
     */
    public abstract byte[] sessionMessageText(String sessionId, byte[] data);

    /**
     * 生成 stream invoke object。
     */
    public byte[] streamInvokeObject(Map<String, Object> data) {
        // Python 中使用 JSON 序列化，这里返回空字节数组占位。
        return new byte[0];
    }

    /**
     * 生成 stream output message。
     */
    public byte[] streamOutputMessage(Event event, String sessionId, Map<String, Object> data) {
        return new byte[0];
    }

    /**
     * 生成心跳消息。
     */
    public byte[] heartbeatMessage() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("event", Event.HEARTBEAT);
        return new byte[0];
    }
}

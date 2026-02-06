package com.dify.invocations.app;

import com.dify.core.entities.invocation.InvokeType;
import com.dify.core.runtime.BackwardsInvocation;
import java.util.Map;

/**
 * Chat 应用调用封装。
 */
public class ChatAppInvocation extends BackwardsInvocation<Map<String, Object>> {

    /**
     * 调用 chat 应用。
     *
     * @param appId 应用 ID
     * @param query 用户输入
     * @param inputs 输入参数
     * @param responseMode streaming 或 blocking
     * @param conversationId 会话 ID（可选）
     * @return 当 responseMode=streaming 时返回流；否则返回单次响应
     */
    public Object invoke(
        String appId,
        String query,
        Map<String, Object> inputs,
        String responseMode,
        String conversationId
    ) {
        if ("streaming".equals(responseMode)) {
            return invokeStreaming(appId, query, inputs, conversationId);
        }
        return invokeBlocking(appId, query, inputs, responseMode, conversationId);
    }

    /**
     * streaming 模式：直接返回响应流。
     */
    public Iterable<Map<String, Object>> invokeStreaming(
        String appId,
        String query,
        Map<String, Object> inputs,
        String conversationId
    ) {
        return backwardsInvoke(
            InvokeType.App,
            Map.class,
            Map.of(
                "app_id", appId,
                "query", query,
                "inputs", inputs,
                "response_mode", "streaming",
                "conversation_id", conversationId
            )
        );
    }

    /**
     * blocking 模式：读取首个响应即返回。
     */
    public Map<String, Object> invokeBlocking(
        String appId,
        String query,
        Map<String, Object> inputs,
        String responseMode,
        String conversationId
    ) {
        Iterable<Map<String, Object>> response = backwardsInvoke(
            InvokeType.App,
            Map.class,
            Map.of(
                "app_id", appId,
                "query", query,
                "inputs", inputs,
                "response_mode", responseMode,
                "conversation_id", conversationId
            )
        );

        for (Map<String, Object> data : response) {
            return data;
        }

        throw new RuntimeException("No response from chat");
    }
}

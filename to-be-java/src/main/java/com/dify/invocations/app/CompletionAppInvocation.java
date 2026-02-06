package com.dify.invocations.app;

import com.dify.core.entities.invocation.InvokeType;
import com.dify.core.runtime.BackwardsInvocation;
import java.util.Map;

/**
 * Completion 应用调用封装。
 */
public class CompletionAppInvocation extends BackwardsInvocation<Map<String, Object>> {

    /**
     * 调用 completion 应用。
     *
     * @param appId 应用 ID
     * @param inputs 输入参数
     * @param responseMode streaming 或 blocking
     * @return 当 responseMode=streaming 时返回流；否则返回单次响应
     */
    public Object invoke(String appId, Map<String, Object> inputs, String responseMode) {
        if ("streaming".equals(responseMode)) {
            return invokeStreaming(appId, inputs);
        }
        return invokeBlocking(appId, inputs, responseMode);
    }

    /**
     * streaming 模式：返回响应流。
     */
    public Iterable<Map<String, Object>> invokeStreaming(String appId, Map<String, Object> inputs) {
        return backwardsInvoke(
            InvokeType.App,
            Map.class,
            Map.of(
                "app_id", appId,
                "inputs", inputs,
                "response_mode", "streaming"
            )
        );
    }

    /**
     * blocking 模式：读取首个响应即返回。
     */
    public Map<String, Object> invokeBlocking(String appId, Map<String, Object> inputs, String responseMode) {
        Iterable<Map<String, Object>> response = backwardsInvoke(
            InvokeType.App,
            Map.class,
            Map.of(
                "app_id", appId,
                "inputs", inputs,
                "response_mode", responseMode
            )
        );

        for (Map<String, Object> data : response) {
            return data;
        }

        throw new RuntimeException("No response from completion");
    }
}

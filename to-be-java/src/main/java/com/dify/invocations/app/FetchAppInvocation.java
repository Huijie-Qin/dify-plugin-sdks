package com.dify.invocations.app;

import com.dify.core.entities.invocation.InvokeType;
import com.dify.core.runtime.BackwardsInvocation;
import java.util.Map;

/**
 * 获取应用信息的反向调用。
 */
public class FetchAppInvocation extends BackwardsInvocation<Map<String, Object>> {

    /**
     * 获取应用信息。
     *
     * @param appId 应用 ID
     * @return 应用信息
     */
    public Map<String, Object> get(String appId) {
        Iterable<Map<String, Object>> response = backwardsInvoke(
            InvokeType.FetchApp,
            Map.class,
            Map.of("app_id", appId)
        );

        for (Map<String, Object> data : response) {
            return data;
        }

        throw new RuntimeException("No response");
    }
}

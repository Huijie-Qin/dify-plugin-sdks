package com.dify.invocations;

import com.dify.core.entities.invocation.InvokeType;
import com.dify.core.runtime.BackwardsInvocation;
import com.dify.entities.tool.ToolInvokeMessage;
import com.dify.entities.tool.ToolProviderType;
import java.util.HashMap;
import java.util.Map;

/**
 * 工具调用相关的封装。
 */
public class ToolInvocation extends BackwardsInvocation<ToolInvokeMessage> {

    /**
     * 调用内置工具。
     */
    public Iterable<ToolInvokeMessage> invokeBuiltinTool(String provider, String toolName, Map<String, Object> parameters) {
        return invoke(ToolProviderType.BUILT_IN, provider, toolName, parameters, null);
    }

    /**
     * 调用工作流工具。
     */
    public Iterable<ToolInvokeMessage> invokeWorkflowTool(String provider, String toolName, Map<String, Object> parameters) {
        return invoke(ToolProviderType.WORKFLOW, provider, toolName, parameters, null);
    }

    /**
     * 调用 API 工具。
     */
    public Iterable<ToolInvokeMessage> invokeApiTool(String provider, String toolName, Map<String, Object> parameters) {
        return invoke(ToolProviderType.API, provider, toolName, parameters, null);
    }

    /**
     * 通用工具调用。
     *
     * @param providerType 工具类型
     * @param provider 工具供应商
     * @param toolName 工具名称
     * @param parameters 工具参数
     * @param credentialId 可选凭证 ID
     * @return 工具调用消息流
     */
    public Iterable<ToolInvokeMessage> invoke(
        ToolProviderType providerType,
        String provider,
        String toolName,
        Map<String, Object> parameters,
        String credentialId
    ) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("tool_type", providerType.getValue());
        payload.put("provider", provider);
        payload.put("tool", toolName);
        payload.put("tool_parameters", parameters);

        if (getSession() != null && getSession().getAppId() != null) {
            payload.put("app_id", getSession().getAppId());
        }

        if (credentialId != null) {
            // 优先使用传入的凭证 ID
            payload.put("credential_id", credentialId);
        } else if (getSession() != null) {
            // 其次使用 session 中的凭证
            String sessionCredentialId = getSession().getContext().getCredentials().getCredentialId(provider);
            if (sessionCredentialId != null) {
                payload.put("credential_id", sessionCredentialId);
            }
        }

        return backwardsInvoke(
            InvokeType.Tool,
            ToolInvokeMessage.class,
            payload
        );
    }
}

package com.dify.dify_plugin.entities.tool;

import com.dify.dify_plugin.entities.provider_config.CredentialType;
import java.util.Map;
import lombok.Getter;

/**
 * Tool 运行时信息。
 */
@Getter
public class ToolRuntime {
    /** 凭证信息。 */
    private final Map<String, Object> credentials;
    /** 用户 ID。 */
    private final String userId;
    /** 会话 ID。 */
    private final String sessionId;
    /** 凭证类型，默认 API_KEY。 */
    private CredentialType credentialType;

    public ToolRuntime(Map<String, Object> credentials, String userId, String sessionId) {
        this(credentials, userId, sessionId, CredentialType.API_KEY);
    }

    public ToolRuntime(
            Map<String, Object> credentials,
            String userId,
            String sessionId,
            CredentialType credentialType
    ) {
        this.credentials = credentials;
        this.userId = userId;
        this.sessionId = sessionId;
        this.credentialType = credentialType == null ? CredentialType.API_KEY : credentialType;
    }
}

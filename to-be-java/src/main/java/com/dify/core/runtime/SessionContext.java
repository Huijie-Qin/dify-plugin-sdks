package com.dify.core.runtime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Session 上下文信息。
 * <p>
 * 当前用于存储会话级凭证，后续可扩展消息/会话 ID 等信息。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionContext {

    /**
     * 会话级凭证信息。
     */
    @Builder.Default
    private InvokeCredentials credentials = new InvokeCredentials();
}

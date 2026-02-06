package com.dify.core.runtime;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 调用凭证信息。
 * <p>
 * 用于存储 Session 级别的工具凭证映射。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvokeCredentials {

    /**
     * 工具 provider -> credential id 映射。
     */
    @Builder.Default
    private Map<String, String> toolCredentials = new HashMap<>();

    /**
     * 根据 provider 获取 credential id。
     */
    public String getCredentialId(String provider) {
        return toolCredentials.get(provider);
    }
}

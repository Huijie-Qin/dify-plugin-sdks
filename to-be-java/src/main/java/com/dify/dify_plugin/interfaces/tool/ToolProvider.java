package com.dify.dify_plugin.interfaces.tool;

import java.util.Map;

/**
 * ToolProvider 基类。
 */
public class ToolProvider {
    /**
     * OAuth 授权地址获取方法。
     * 默认抛出异常，提醒子类实现。
     */
    public String oauthGetAuthorizationUrl(String redirectUri, Map<String, Object> state) {
        throw new UnsupportedOperationException("Not implemented");
    }
}

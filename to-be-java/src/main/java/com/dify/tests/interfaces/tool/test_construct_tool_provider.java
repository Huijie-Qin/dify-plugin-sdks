package com.dify.tests.interfaces.tool;

/**
 * 对应 Python 的 test_construct_tool_provider.py。
 */
public class test_construct_tool_provider {
    public void testConstructToolProvider() {
        ToolProvider provider = new ToolProvider();
        if (provider == null) {
            throw new AssertionError("provider 应该被构造");
        }
    }

    public void testOauthGetAuthorizationUrl() {
        ToolProvider provider = new ToolProvider();
        try {
            provider.oauthGetAuthorizationUrl("", null);
            throw new AssertionError("应当抛出 NotImplementedError");
        } catch (UnsupportedOperationException ex) {
            // 符合预期
        }
    }

    static class ToolProvider {
        public String oauthGetAuthorizationUrl(String redirectUri, Object params) {
            throw new UnsupportedOperationException("NotImplemented");
        }
    }
}

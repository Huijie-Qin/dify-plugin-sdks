package com.dify.tests.interfaces.tool;

import com.dify.dify_plugin.interfaces.tool.ToolProvider;

/**
 * 对应 python/tests/interfaces/tool/test_construct_tool_provider.py 的逻辑实现。
 */
public class TestConstructToolProvider {
    /**
     * 测试 ToolProvider 可以在不实现任何方法时被构造。
     */
    public void testConstructToolProvider() {
        ToolProvider provider = new ToolProvider();
        assert provider != null;
    }

    /**
     * 测试默认的 oauthGetAuthorizationUrl 会抛出未实现异常。
     */
    public void testOauthGetAuthorizationUrl() {
        ToolProvider provider = new ToolProvider();
        boolean thrown = false;
        try {
            provider.oauthGetAuthorizationUrl("", java.util.Collections.emptyMap());
        } catch (UnsupportedOperationException ex) {
            thrown = true;
        }
        assert thrown;
    }
}

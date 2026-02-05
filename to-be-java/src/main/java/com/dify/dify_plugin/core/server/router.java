package com.dify.dify_plugin.core.server;

import java.util.HashMap;
import java.util.Map;

/**
 * 对应 Python 的 router.py。
 * 负责路由分发请求。
 */
public class router {
    private final Map<String, Object> handlers = new HashMap<>();

    public void register(String path, Object handler) {
        handlers.put(path, handler);
    }

    public Object dispatch(String path, Object request) {
        Object handler = handlers.get(path);
        if (handler == null) {
            return null;
        }
        return handler;
    }
}

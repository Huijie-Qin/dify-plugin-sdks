package com.dify.dify_plugin.core;

import java.util.HashMap;
import java.util.Map;

/**
 * 对应 Python 的 PluginRegistration。
 * <p>
 * 该类负责注册与获取各种插件组件（工具、模型、触发器等）。
 * </p>
 */
class PluginRegistration {
    private final Map<String, Object> toolProviders = new HashMap<>();
    private final Map<String, Object> modelProviders = new HashMap<>();
    private final Map<String, Object> endpointProviders = new HashMap<>();
    private final Map<String, Object> agentStrategyProviders = new HashMap<>();
    private final Map<String, Object> datasourceProviders = new HashMap<>();
    private final Map<String, Object> triggerProviders = new HashMap<>();

    public void registerToolProvider(String name, Object provider) {
        toolProviders.put(name, provider);
    }

    public Object getToolProviderCls(String name) {
        return toolProviders.get(name);
    }

    public void registerModelProvider(String name, Object provider) {
        modelProviders.put(name, provider);
    }

    public Object getModelProviderInstance(String name) {
        return modelProviders.get(name);
    }

    public void registerEndpointProvider(String name, Object provider) {
        endpointProviders.put(name, provider);
    }

    public Object getEndpointProvider(String name) {
        return endpointProviders.get(name);
    }

    public void registerAgentStrategyProvider(String name, Object provider) {
        agentStrategyProviders.put(name, provider);
    }

    public Object getAgentStrategyProvider(String name) {
        return agentStrategyProviders.get(name);
    }

    public void registerDatasourceProvider(String name, Object provider) {
        datasourceProviders.put(name, provider);
    }

    public Object getDatasourceProvider(String name) {
        return datasourceProviders.get(name);
    }

    public void registerTriggerProvider(String name, Object provider) {
        triggerProviders.put(name, provider);
    }

    public Object getTriggerProvider(String name) {
        return triggerProviders.get(name);
    }
}

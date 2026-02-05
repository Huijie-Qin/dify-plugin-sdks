package com.dify.dify_plugin.core.entities;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 对应 Python 的 DatasourceProviderMapping。
 * 用于把数据源 Provider 与其配置及具体数据源实现进行映射。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DatasourceProviderMapping {
    private String provider;
    private Object providerCls;
    private Object configuration;
    private Map<String, Object> websiteCrawlDatasourceMapping = new HashMap<>();
    private Map<String, Object> onlineDocumentDatasourceMapping = new HashMap<>();
    private Map<String, Object> onlineDriveDatasourceMapping = new HashMap<>();

    public DatasourceProviderMapping(
        String provider,
        Object providerCls,
        Object configuration,
        Map<String, Object> websiteCrawlDatasourceMapping,
        Map<String, Object> onlineDocumentDatasourceMapping,
        Map<String, Object> onlineDriveDatasourceMapping
    ) {
        this.provider = provider;
        this.providerCls = providerCls;
        this.configuration = configuration;
        this.websiteCrawlDatasourceMapping = websiteCrawlDatasourceMapping == null
            ? new HashMap<>()
            : websiteCrawlDatasourceMapping;
        this.onlineDocumentDatasourceMapping = onlineDocumentDatasourceMapping == null
            ? new HashMap<>()
            : onlineDocumentDatasourceMapping;
        this.onlineDriveDatasourceMapping = onlineDriveDatasourceMapping == null
            ? new HashMap<>()
            : onlineDriveDatasourceMapping;
    }
}

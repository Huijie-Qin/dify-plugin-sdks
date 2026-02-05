package com.dify.dify_plugin.core.entities;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 对应 Python 的 InitializeMessage。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InitializeMessage {
    public enum Type {
        HANDSHAKE("handshake"),
        ASSET_CHUNK("asset_chunk"),
        MANIFEST_DECLARATION("manifest_declaration"),
        TOOL_DECLARATION("tool_declaration"),
        MODEL_DECLARATION("model_declaration"),
        ENDPOINT_DECLARATION("endpoint_declaration"),
        AGENT_STRATEGY_DECLARATION("agent_strategy_declaration"),
        DATASOURCE_DECLARATION("datasource_declaration"),
        TRIGGER_DECLARATION("trigger_declaration"),
        END("end");

        private final String value;

        Type(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 资源分片。
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AssetChunk {
        /**
         * 文件名。
         */
        private String filename;
        /**
         * base64 编码的数据。
         */
        private String data;
        /**
         * 是否为最后一块。
         */
        private boolean end;
    }

    /**
     * 仅包含 key 的结构。
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Key {
        private String key;
    }

    private Type type;
    /**
     * data 既可能是 Map，也可能是 List。
     */
    private Object data;
}

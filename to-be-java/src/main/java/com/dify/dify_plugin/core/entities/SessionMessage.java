package com.dify.dify_plugin.core.entities;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 对应 Python 的 SessionMessage。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionMessage {
    public enum Type {
        STREAM("stream"),
        INVOKE("invoke"),
        END("end"),
        ERROR("error");

        private final String value;

        Type(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 消息类型。
     */
    private Type type;
    /**
     * 消息数据体。
     */
    private Map<String, Object> data = new HashMap<>();

    /**
     * 将对象转换为 Map，便于 JSON 化。
     *
     * @return Map 结构
     */
    public Map<String, Object> toMap() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", type.getValue());
        payload.put("data", data);
        return payload;
    }
}

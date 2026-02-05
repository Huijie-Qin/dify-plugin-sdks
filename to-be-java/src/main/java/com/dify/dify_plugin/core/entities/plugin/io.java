package com.dify.dify_plugin.core.entities.plugin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 对应 Python 的 io.py。
 */
enum PluginInStreamEvent {
    Request("request"),
    BackwardInvocationResponse("backwards_response");

    private final String value;

    PluginInStreamEvent(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static PluginInStreamEvent valueOfString(String v) {
        for (PluginInStreamEvent event : values()) {
            if (event.value.equals(v)) {
                return event;
            }
        }
        throw new IllegalArgumentException("Invalid value for PluginInStream.Event: " + v);
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class PluginInStreamBase {
    private String sessionId;
    private PluginInStreamEvent event;
    private Object data;
    private String conversationId;
    private String messageId;
    private String appId;
    private String endpointId;
    private Object context;
}

@Data
@NoArgsConstructor
class PluginInStream extends PluginInStreamBase {
    private Object reader;
    private Object writer;

    public PluginInStream(
        String sessionId,
        PluginInStreamEvent event,
        Object data,
        Object reader,
        Object writer,
        String conversationId,
        String messageId,
        String appId,
        String endpointId,
        Object context
    ) {
        super(sessionId, event, data, conversationId, messageId, appId, endpointId, context);
        this.reader = reader;
        this.writer = writer;
    }
}

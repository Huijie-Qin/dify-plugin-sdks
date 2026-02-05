package com.dify.dify_plugin.core.server.__base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 对应 Python 的 writer_entities.py。
 */
enum Event {
    LOG,
    ERROR,
    SESSION,
    HEARTBEAT
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class StreamOutputMessage {
    private Event event;
    private String sessionId;
    private Object data;
}

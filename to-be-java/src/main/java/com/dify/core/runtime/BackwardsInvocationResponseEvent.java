package com.dify.core.runtime;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 反向调用响应事件。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BackwardsInvocationResponseEvent {

    /**
     * 事件类型。
     */
    public enum Event {
        response("response"),
        Error("error"),
        End("end");

        private final String value;

        Event(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static Event fromValue(String value) {
            for (Event event : values()) {
                if (event.value.equals(value)) {
                    return event;
                }
            }
            throw new IllegalArgumentException("Unknown event: " + value);
        }
    }

    private String backwardsRequestId;
    private Event event;
    private String message;
    private Map<String, Object> data;
}

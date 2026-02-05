package com.dify.dify_plugin.entities.agent;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Agent 调用消息。
 * 当前仅保留文本内容用于测试。
 */
@Data
@AllArgsConstructor
public class AgentInvokeMessage {
    /** 文本内容。 */
    private String content;
}

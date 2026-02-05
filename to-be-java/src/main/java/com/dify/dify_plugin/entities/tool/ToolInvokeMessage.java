package com.dify.dify_plugin.entities.tool;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Tool 调用消息。
 */
@Data
@AllArgsConstructor
public class ToolInvokeMessage {
    /** 文本内容。 */
    private String content;
}

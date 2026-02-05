package com.dify.dify_plugin.entities.model.message;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 提示消息模型。
 */
@Data
@AllArgsConstructor
public class PromptMessage {
    /** 角色类型。 */
    private PromptMessageRole role;
    /** 消息内容。 */
    private String content;
    /** 可选的名称。 */
    private String name;
}

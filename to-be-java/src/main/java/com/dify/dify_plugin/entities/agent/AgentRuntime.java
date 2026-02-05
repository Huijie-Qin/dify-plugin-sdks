package com.dify.dify_plugin.entities.agent;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Agent 运行时信息。
 */
@Getter
@AllArgsConstructor
public class AgentRuntime {
    /** 当前用户 ID。 */
    private final String userId;
}

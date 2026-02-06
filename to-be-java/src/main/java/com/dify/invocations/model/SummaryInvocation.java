package com.dify.invocations.model;

import com.dify.core.entities.invocation.InvokeType;
import com.dify.core.runtime.BackwardsInvocation;
import com.dify.entities.model.llm.SummaryResult;
import java.util.Map;

/**
 * 文本摘要调用封装。
 */
public class SummaryInvocation extends BackwardsInvocation<SummaryResult> {

    /**
     * 调用摘要能力。
     *
     * @param text 原始文本
     * @param instruction 摘要指令
     * @param minSummarizeLength 小于该长度时直接返回原文
     * @return 摘要文本
     */
    public String invoke(String text, String instruction, int minSummarizeLength) {
        if (text.length() < minSummarizeLength) {
            return text;
        }

        Map<String, Object> data = Map.of(
            "text", text,
            "instruction", instruction
        );

        for (SummaryResult llmResult : backwardsInvoke(
            InvokeType.SYSTEM_SUMMARY,
            SummaryResult.class,
            data
        )) {
            return llmResult.getSummary();
        }

        throw new RuntimeException("No response from summary");
    }
}

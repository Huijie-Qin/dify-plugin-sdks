package com.dify.core.runtime;

import com.dify.invocations.model.LLMInvocation;
import com.dify.invocations.model.LLMStructuredOutputInvocation;
import com.dify.invocations.model.ModerationInvocation;
import com.dify.invocations.model.RerankInvocation;
import com.dify.invocations.model.Speech2TextInvocation;
import com.dify.invocations.model.SummaryInvocation;
import com.dify.invocations.model.TTSInvocation;
import com.dify.invocations.model.TextEmbeddingInvocation;
import lombok.Getter;

/**
 * 模型相关反向调用集合。
 */
@Getter
public class ModelInvocations {

    private final LLMInvocation llm;
    private final LLMStructuredOutputInvocation llmStructuredOutput;
    private final TextEmbeddingInvocation textEmbedding;
    private final RerankInvocation rerank;
    private final Speech2TextInvocation speech2text;
    private final TTSInvocation tts;
    private final ModerationInvocation moderation;
    private final SummaryInvocation summary;

    public ModelInvocations(Session session) {
        this.llm = new LLMInvocation(session);
        this.llmStructuredOutput = new LLMStructuredOutputInvocation(session);
        this.textEmbedding = new TextEmbeddingInvocation(session);
        this.rerank = new RerankInvocation(session);
        this.speech2text = new Speech2TextInvocation(session);
        this.tts = new TTSInvocation(session);
        this.moderation = new ModerationInvocation(session);
        this.summary = new SummaryInvocation(session);
    }
}

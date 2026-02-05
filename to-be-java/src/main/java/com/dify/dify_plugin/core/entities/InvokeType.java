package com.dify.dify_plugin.core.entities;

import java.util.Arrays;

/**
 * 对应 Python 的 InvokeType 枚举。
 * <p>
 * 该枚举用于描述插件回调/反向调用的类型。
 * </p>
 */
public enum InvokeType {
    Tool("tool"),
    LLM("llm"),
    LLMStructuredOutput("llm_structured_output"),
    TextEmbedding("text_embedding"),
    MultimodalEmbedding("multimodal_embedding"),
    Rerank("rerank"),
    MultimodalRerank("multimodal_rerank"),
    TTS("tts"),
    Speech2Text("speech2text"),
    Moderation("moderation"),
    NodeParameterExtractor("node_parameter_extractor"),
    NodeQuestionClassifier("node_question_classifier"),
    App("app"),
    Storage("storage"),
    UploadFile("upload_file"),
    SYSTEM_SUMMARY("system_summary"),
    FetchApp("fetch_app");

    private final String value;

    InvokeType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * 根据字符串值获取对应的枚举。
     *
     * @param value 字符串值
     * @return 对应的枚举
     */
    public static InvokeType valueOfString(String value) {
        return Arrays.stream(values())
            .filter(item -> item.value.equals(value))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("invalid type value " + value));
    }
}

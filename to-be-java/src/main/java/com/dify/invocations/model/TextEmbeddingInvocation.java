package com.dify.invocations.model;

import com.dify.core.entities.invocation.InvokeType;
import com.dify.core.runtime.BackwardsInvocation;
import com.dify.entities.model.EmbeddingInputType;
import com.dify.entities.model.text_embedding.MultiModalContent;
import com.dify.entities.model.text_embedding.MultiModalEmbeddingModelConfig;
import com.dify.entities.model.text_embedding.MultiModalEmbeddingResult;
import com.dify.entities.model.text_embedding.TextEmbeddingModelConfig;
import com.dify.entities.model.text_embedding.TextEmbeddingResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 文本向量与多模态向量调用封装。
 */
public class TextEmbeddingInvocation extends BackwardsInvocation<TextEmbeddingResult> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 调用文本向量接口。
     */
    public TextEmbeddingResult invoke(
        TextEmbeddingModelConfig modelConfig,
        List<String> texts,
        EmbeddingInputType inputType
    ) {
        Map<String, Object> payload = OBJECT_MAPPER.convertValue(modelConfig, Map.class);
        payload.put("texts", texts);
        payload.put("input_type", inputType.getValue());

        for (TextEmbeddingResult data : backwardsInvoke(
            InvokeType.TextEmbedding,
            TextEmbeddingResult.class,
            payload
        )) {
            return data;
        }

        throw new RuntimeException("No response from text embedding");
    }

    /**
     * 调用多模态向量接口。
     */
    public MultiModalEmbeddingResult invokeMultimodal(
        MultiModalEmbeddingModelConfig modelConfig,
        List<MultiModalContent> documents,
        EmbeddingInputType inputType
    ) {
        Map<String, Object> payload = OBJECT_MAPPER.convertValue(modelConfig, Map.class);
        payload.put("documents", documents.stream()
            .map(document -> OBJECT_MAPPER.convertValue(document, Map.class))
            .collect(Collectors.toList()));
        payload.put("input_type", inputType.getValue());

        for (MultiModalEmbeddingResult data : backwardsInvoke(
            InvokeType.MultimodalEmbedding,
            MultiModalEmbeddingResult.class,
            payload
        )) {
            return data;
        }

        throw new RuntimeException("No response from multimodal embedding");
    }
}

package com.dify.invocations.model;

import com.dify.core.entities.invocation.InvokeType;
import com.dify.core.runtime.BackwardsInvocation;
import com.dify.entities.model.rerank.MultiModalRerankModelConfig;
import com.dify.entities.model.rerank.MultiModalRerankResult;
import com.dify.entities.model.rerank.RerankModelConfig;
import com.dify.entities.model.rerank.RerankResult;
import com.dify.entities.model.text_embedding.MultiModalContent;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * rerank 调用封装。
 */
public class RerankInvocation extends BackwardsInvocation<RerankResult> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 调用文本 rerank。
     */
    public RerankResult invoke(RerankModelConfig modelConfig, List<String> docs, String query) {
        Map<String, Object> payload = OBJECT_MAPPER.convertValue(modelConfig, Map.class);
        payload.put("docs", docs);
        payload.put("query", query);

        for (RerankResult data : backwardsInvoke(
            InvokeType.Rerank,
            RerankResult.class,
            payload
        )) {
            return data;
        }

        throw new RuntimeException("No response from rerank");
    }

    /**
     * 调用多模态 rerank。
     */
    public MultiModalRerankResult invokeMultimodal(
        MultiModalRerankModelConfig modelConfig,
        MultiModalContent query,
        List<MultiModalContent> docs
    ) {
        Map<String, Object> payload = OBJECT_MAPPER.convertValue(modelConfig, Map.class);
        payload.put("query", OBJECT_MAPPER.convertValue(query, Map.class));
        payload.put("docs", docs.stream()
            .map(doc -> OBJECT_MAPPER.convertValue(doc, Map.class))
            .collect(Collectors.toList()));

        for (MultiModalRerankResult data : backwardsInvoke(
            InvokeType.MultimodalRerank,
            MultiModalRerankResult.class,
            payload
        )) {
            return data;
        }

        throw new RuntimeException("No response from multimodal rerank");
    }
}

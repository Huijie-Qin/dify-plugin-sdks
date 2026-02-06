package com.dify.invocations.model;

import com.dify.core.entities.invocation.InvokeType;
import com.dify.core.runtime.BackwardsInvocation;
import com.dify.entities.model.llm.LLMModelConfig;
import com.dify.entities.model.llm.LLMResultChunkWithStructuredOutput;
import com.dify.entities.model.llm.LLMResultWithStructuredOutput;
import com.dify.entities.model.llm.LLMUsage;
import com.dify.entities.model.message.AssistantPromptMessage;
import com.dify.entities.model.message.PromptMessage;
import com.dify.entities.model.message.PromptMessageTool;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 结构化输出的 LLM 调用封装。
 */
public class LLMStructuredOutputInvocation extends BackwardsInvocation<LLMResultChunkWithStructuredOutput> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 调用带结构化输出的 LLM。
     */
    public Object invoke(
        LLMModelConfig modelConfig,
        List<PromptMessage> promptMessages,
        Map<String, Object> structuredOutputSchema,
        List<PromptMessageTool> tools,
        List<String> stop,
        boolean stream
    ) {
        if (stream) {
            return invokeStreaming(modelConfig, promptMessages, structuredOutputSchema, tools, stop);
        }
        return invokeBlocking(modelConfig, promptMessages, structuredOutputSchema, tools, stop);
    }

    /**
     * streaming 模式：返回结构化输出的流。
     */
    public Iterable<LLMResultChunkWithStructuredOutput> invokeStreaming(
        LLMModelConfig modelConfig,
        List<PromptMessage> promptMessages,
        Map<String, Object> structuredOutputSchema,
        List<PromptMessageTool> tools,
        List<String> stop
    ) {
        Map<String, Object> data = buildPayload(modelConfig, promptMessages, structuredOutputSchema, tools, stop, true);
        return backwardsInvoke(InvokeType.LLMStructuredOutput, LLMResultChunkWithStructuredOutput.class, data);
    }

    /**
     * blocking 模式：合并流数据，得到结构化输出结果。
     */
    public LLMResultWithStructuredOutput invokeBlocking(
        LLMModelConfig modelConfig,
        List<PromptMessage> promptMessages,
        Map<String, Object> structuredOutputSchema,
        List<PromptMessageTool> tools,
        List<String> stop
    ) {
        Map<String, Object> data = buildPayload(modelConfig, promptMessages, structuredOutputSchema, tools, stop, false);

        LLMResultWithStructuredOutput result = new LLMResultWithStructuredOutput(
            modelConfig.getModel(),
            new AssistantPromptMessage(""),
            LLMUsage.emptyUsage(),
            null
        );

        for (LLMResultChunkWithStructuredOutput llmResult : backwardsInvoke(
            InvokeType.LLMStructuredOutput,
            LLMResultChunkWithStructuredOutput.class,
            data
        )) {
            if (llmResult.getDelta().getMessage().getContent() instanceof String) {
                String current = result.getMessage().getContent();
                result.getMessage().setContent(current + llmResult.getDelta().getMessage().getContent());
            }
            if (!llmResult.getDelta().getMessage().getToolCalls().isEmpty()) {
                result.getMessage().setToolCalls(llmResult.getDelta().getMessage().getToolCalls());
            }
            if (llmResult.getDelta().getUsage() != null) {
                result.getUsage().setPromptTokens(result.getUsage().getPromptTokens()
                    + llmResult.getDelta().getUsage().getPromptTokens());
                result.getUsage().setCompletionTokens(result.getUsage().getCompletionTokens()
                    + llmResult.getDelta().getUsage().getCompletionTokens());
                result.getUsage().setTotalTokens(result.getUsage().getTotalTokens()
                    + llmResult.getDelta().getUsage().getTotalTokens());

                result.getUsage().setCompletionPrice(llmResult.getDelta().getUsage().getCompletionPrice());
                result.getUsage().setPromptPrice(llmResult.getDelta().getUsage().getPromptPrice());
                result.getUsage().setTotalPrice(llmResult.getDelta().getUsage().getTotalPrice());
                result.getUsage().setCurrency(llmResult.getDelta().getUsage().getCurrency());
                result.getUsage().setLatency(llmResult.getDelta().getUsage().getLatency());
            }

            // 处理结构化输出
            if (llmResult.getStructuredOutput() != null) {
                result.setStructuredOutput(llmResult.getStructuredOutput());
            }
        }

        return result;
    }

    /**
     * 构建请求 payload。
     */
    private Map<String, Object> buildPayload(
        LLMModelConfig modelConfig,
        List<PromptMessage> promptMessages,
        Map<String, Object> structuredOutputSchema,
        List<PromptMessageTool> tools,
        List<String> stop,
        boolean stream
    ) {
        Map<String, Object> data = OBJECT_MAPPER.convertValue(modelConfig, Map.class);
        data.put("prompt_messages", promptMessages.stream()
            .map(message -> OBJECT_MAPPER.convertValue(message, Map.class))
            .collect(Collectors.toList()));
        data.put("structured_output_schema", structuredOutputSchema);
        data.put("tools", tools == null ? null : tools.stream()
            .map(tool -> OBJECT_MAPPER.convertValue(tool, Map.class))
            .collect(Collectors.toList()));
        data.put("stop", stop);
        data.put("stream", stream);
        return data;
    }
}

package com.dify.invocations.model;

import com.dify.core.entities.invocation.InvokeType;
import com.dify.core.runtime.BackwardsInvocation;
import com.dify.entities.model.moderation.ModerationModelConfig;
import com.dify.entities.model.moderation.ModerationResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

/**
 * 内容安全审核调用封装。
 */
public class ModerationInvocation extends BackwardsInvocation<ModerationResult> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 调用 moderation 模型。
     *
     * @param modelConfig 模型配置
     * @param text 待检测文本
     * @return 是否通过审核
     */
    public boolean invoke(ModerationModelConfig modelConfig, String text) {
        Map<String, Object> payload = OBJECT_MAPPER.convertValue(modelConfig, Map.class);
        payload.put("text", text);

        for (ModerationResult data : backwardsInvoke(
            InvokeType.Moderation,
            ModerationResult.class,
            payload
        )) {
            return data.getResult();
        }

        throw new RuntimeException("No response from moderation");
    }
}

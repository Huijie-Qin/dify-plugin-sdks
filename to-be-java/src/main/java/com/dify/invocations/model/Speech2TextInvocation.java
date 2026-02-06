package com.dify.invocations.model;

import com.dify.core.entities.invocation.InvokeType;
import com.dify.core.runtime.BackwardsInvocation;
import com.dify.entities.model.speech2text.Speech2TextModelConfig;
import com.dify.entities.model.speech2text.Speech2TextResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.HexFormat;
import java.util.Map;

/**
 * 语音转文字调用封装。
 */
public class Speech2TextInvocation extends BackwardsInvocation<Speech2TextResult> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final HexFormat HEX_FORMAT = HexFormat.of();

    /**
     * 调用 speech2text。
     *
     * @param modelConfig 模型配置
     * @param file 文件输入流
     * @return 识别后的文本
     */
    public String invoke(Speech2TextModelConfig modelConfig, InputStream file) {
        byte[] bytes;
        try {
            bytes = file.readAllBytes();
        } catch (IOException exception) {
            throw new RuntimeException("read speech2text file failed", exception);
        }

        Map<String, Object> payload = OBJECT_MAPPER.convertValue(modelConfig, Map.class);
        payload.put("file", HEX_FORMAT.formatHex(bytes));

        for (Speech2TextResult data : backwardsInvoke(
            InvokeType.Speech2Text,
            Speech2TextResult.class,
            payload
        )) {
            return data.getResult();
        }

        throw new RuntimeException("No response from speech2text");
    }
}

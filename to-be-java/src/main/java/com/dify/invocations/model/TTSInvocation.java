package com.dify.invocations.model;

import com.dify.core.entities.invocation.InvokeType;
import com.dify.core.runtime.BackwardsInvocation;
import com.dify.entities.model.tts.TTSModelConfig;
import com.dify.entities.model.tts.TTSResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HexFormat;
import java.util.Iterator;
import java.util.Map;

/**
 * 文本转语音调用封装。
 */
public class TTSInvocation extends BackwardsInvocation<TTSResult> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final HexFormat HEX_FORMAT = HexFormat.of();

    /**
     * 调用 TTS，返回音频字节流。
     */
    public Iterable<byte[]> invoke(TTSModelConfig modelConfig, String contentText) {
        Map<String, Object> payload = OBJECT_MAPPER.convertValue(modelConfig, Map.class);
        payload.put("content_text", contentText);

        Iterable<TTSResult> response = backwardsInvoke(
            InvokeType.TTS,
            TTSResult.class,
            payload
        );

        return () -> new Iterator<>() {
            private final Iterator<TTSResult> delegate = response.iterator();

            @Override
            public boolean hasNext() {
                return delegate.hasNext();
            }

            @Override
            public byte[] next() {
                TTSResult data = delegate.next();
                return HEX_FORMAT.parseHex(data.getResult());
            }
        };
    }
}

package com.dify.invocations;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 上传文件返回结果。
 * <p>
 * 对应 Python 中的 UploadFileResponse，主要负责承载上传后的文件信息，并提供类型推断与参数转换。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadFileResponse {

    /**
     * 文件类型枚举。
     */
    public enum Type {
        DOCUMENT("document"),
        IMAGE("image"),
        VIDEO("video"),
        AUDIO("audio");

        private final String value;

        Type(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        /**
         * 根据 mimeType 推断文件类型。
         */
        public static Type fromMimeType(String mimeType) {
            if (mimeType == null) {
                return DOCUMENT;
            }
            if (mimeType.startsWith("image/")) {
                return IMAGE;
            }
            if (mimeType.startsWith("video/")) {
                return VIDEO;
            }
            if (mimeType.startsWith("audio/")) {
                return AUDIO;
            }
            return DOCUMENT;
        }
    }

    private String id;
    private String name;
    private long size;
    private String extension;
    private String mimeType;
    private Type type;
    private String previewUrl;

    /**
     * 在数据未携带 type 时，根据 mimeType 自动补齐。
     */
    public void applyDefaultTypeIfMissing() {
        if (type == null) {
            type = Type.fromMimeType(mimeType);
        }
    }

    /**
     * 转换为应用参数，供其他接口使用。
     */
    public Map<String, Object> toAppParameter() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("upload_file_id", id);
        payload.put("transfer_method", "local_file");
        payload.put("type", Type.fromMimeType(mimeType).getValue());
        return payload;
    }
}

package com.dify.invocations;

import com.dify.core.entities.invocation.InvokeType;
import com.dify.core.runtime.BackwardsInvocation;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件相关的反向调用封装。
 */
public class File extends BackwardsInvocation<Map<String, Object>> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 上传文件。
     *
     * @param filename 文件名
     * @param content 文件内容
     * @param mimetype 文件 mime 类型
     * @return 上传后的文件信息
     */
    public UploadFileResponse upload(String filename, byte[] content, String mimetype) {
        // 先通过反向调用获取签名上传地址
        for (Map<String, Object> response : backwardsInvoke(
            InvokeType.UploadFile,
            Map.class,
            Map.of(
                "filename", filename,
                "mimetype", mimetype
            )
        )) {
            Object urlValue = response.get("url");
            if (urlValue == null) {
                throw new RuntimeException("upload file failed, could not get signed url");
            }

            String url = urlValue.toString();
            // 使用 HTTP POST 上传文件内容
            HttpResponse<String> uploadResponse = sendMultipartUpload(url, filename, content, mimetype);
            if (uploadResponse.statusCode() != 201) {
                throw new RuntimeException(
                    "upload file failed, status code: " + uploadResponse.statusCode() + ", response: " + uploadResponse.body()
                );
            }

            // 将响应 JSON 转为 UploadFileResponse
            UploadFileResponse result = parseUploadFileResponse(uploadResponse.body());
            result.applyDefaultTypeIfMissing();
            return result;
        }

        throw new RuntimeException("upload file failed, empty response from server");
    }

    /**
     * 发送 multipart/form-data 上传请求。
     */
    private HttpResponse<String> sendMultipartUpload(String url, String filename, byte[] content, String mimetype) {
        String boundary = "----DifyBoundary" + System.currentTimeMillis();
        byte[] body = buildMultipartBody(boundary, filename, content, mimetype);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .timeout(Duration.ofSeconds(30))
            .header("Content-Type", "multipart/form-data; boundary=" + boundary)
            .POST(HttpRequest.BodyPublishers.ofByteArray(body))
            .build();

        try {
            return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        } catch (IOException | InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("upload file failed, request error", exception);
        }
    }

    /**
     * 构建 multipart/form-data 请求体。
     */
    private byte[] buildMultipartBody(String boundary, String filename, byte[] content, String mimetype) {
        String lineBreak = "\r\n";
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            String header = "--" + boundary + lineBreak
                + "Content-Disposition: form-data; name=\"file\"; filename=\"" + filename + "\"" + lineBreak
                + "Content-Type: " + mimetype + lineBreak
                + lineBreak;
            outputStream.write(header.getBytes(StandardCharsets.UTF_8));
            outputStream.write(content);
            outputStream.write(lineBreak.getBytes(StandardCharsets.UTF_8));
            outputStream.write(("--" + boundary + "--" + lineBreak).getBytes(StandardCharsets.UTF_8));
            return outputStream.toByteArray();
        } catch (IOException exception) {
            throw new RuntimeException("upload file failed, could not build multipart body", exception);
        }
    }

    /**
     * 将 JSON 字符串解析为 UploadFileResponse。
     */
    private UploadFileResponse parseUploadFileResponse(String responseBody) {
        try {
            return OBJECT_MAPPER.readValue(responseBody, UploadFileResponse.class);
        } catch (IOException exception) {
            throw new RuntimeException("upload file failed, could not parse response", exception);
        }
    }
}

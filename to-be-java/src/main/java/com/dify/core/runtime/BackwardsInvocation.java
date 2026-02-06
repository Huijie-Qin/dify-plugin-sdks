package com.dify.core.runtime;

import com.dify.config.config.InstallMethod;
import com.dify.core.entities.invocation.InvokeType;
import com.dify.core.entities.plugin.io.PluginInStream;
import com.dify.core.entities.plugin.io.PluginInStreamBase;
import com.dify.core.entities.plugin.io.PluginInStreamEvent;
import com.dify.core.server.__base.request_reader.RequestReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * 反向调用抽象基类。
 *
 * @param <T> 反向调用返回的数据类型
 */
public abstract class BackwardsInvocation<T> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final Session session;

    protected BackwardsInvocation(Session session) {
        this.session = session;
    }

    public Session getSession() {
        return session;
    }

    /**
     * 生成反向调用请求 ID。
     */
    protected String generateBackwardsRequestId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 发起反向调用（根据运行时环境自动选择传输方式）。
     */
    protected Iterable<T> backwardsInvoke(InvokeType type, Class<T> dataType, Map<String, Object> data) {
        String backwardsRequestId = generateBackwardsRequestId();

        if (session == null) {
            throw new RuntimeException("current tool runtime does not support backwards invoke");
        }
        if (session.getInstallMethod() == InstallMethod.Local || session.getInstallMethod() == InstallMethod.Remote) {
            return fullDuplexBackwardsInvoke(backwardsRequestId, type, dataType, data);
        }
        return httpBackwardsInvoke(backwardsRequestId, type, dataType, data);
    }

    /**
     * 将输入流中的数据行转换为目标类型。
     */
    protected Iterable<T> lineConverterWrapper(Iterable<PluginInStreamBase> generator, Class<T> dataType) {
        return () -> new Iterator<>() {
            private final Iterator<PluginInStreamBase> delegate = generator.iterator();
            private int emptyResponseCount = 0;
            private final int maxTimeoutCount = session == null ? 250 : session.getMaxInvocationTimeout();
            private T nextItem;
            private boolean finished;

            @Override
            public boolean hasNext() {
                if (finished) {
                    return false;
                }
                if (nextItem != null) {
                    return true;
                }
                while (delegate.hasNext()) {
                    PluginInStreamBase chunk = delegate.next();
                    if (chunk == null) {
                        emptyResponseCount++;
                        if (emptyResponseCount >= maxTimeoutCount) {
                            throw new RuntimeException(
                                "invocation exited without response after " + maxTimeoutCount + " seconds"
                            );
                        }
                        continue;
                    }

                    BackwardsInvocationResponseEvent event = OBJECT_MAPPER.convertValue(
                        chunk.getData(),
                        BackwardsInvocationResponseEvent.class
                    );

                    if (event.getEvent() == BackwardsInvocationResponseEvent.Event.End) {
                        finished = true;
                        return false;
                    }

                    if (event.getEvent() == BackwardsInvocationResponseEvent.Event.Error) {
                        throw new RuntimeException(event.getMessage());
                    }

                    if (event.getData() == null) {
                        finished = true;
                        return false;
                    }

                    emptyResponseCount = 0;
                    try {
                        nextItem = convertData(event.getData(), dataType);
                    } catch (Exception exception) {
                        throw new RuntimeException("Failed to parse response: " + exception.getMessage(), exception);
                    }
                    return true;
                }
                finished = true;
                return false;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T item = nextItem;
                nextItem = null;
                return item;
            }
        };
    }

    /**
     * HTTP 方式的反向调用。
     */
    protected Iterable<T> httpBackwardsInvoke(
        String backwardsRequestId,
        InvokeType type,
        Class<T> dataType,
        Map<String, Object> data
    ) {
        if (session == null || session.getDifyPluginDaemonUrl() == null) {
            throw new RuntimeException("current tool runtime does not support backwards invoke");
        }

        String url = session.getDifyPluginDaemonUrl() + "/backwards-invocation/transaction";
        String payload = session.getWriter().sessionMessageText(
            session.getSessionId(),
            session.getWriter().streamInvokeObject(
                Map.of(
                    "type", type.getValue(),
                    "backwards_request_id", backwardsRequestId,
                    "request", data
                )
            )
        );

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .timeout(Duration.ofSeconds(session.getMaxInvocationTimeout()))
            .header("Dify-Plugin-Session-ID", session.getSessionId())
            .POST(HttpRequest.BodyPublishers.ofString(payload))
            .build();

        Iterable<PluginInStreamBase> generator = () -> new Iterator<>() {
            private final HttpResponse<java.io.InputStream> response = sendRequest(request);
            private final BufferedReader reader = new BufferedReader(
                new InputStreamReader(response.body(), StandardCharsets.UTF_8)
            );
            private String nextLine;
            private boolean finished;

            @Override
            public boolean hasNext() {
                if (finished) {
                    return false;
                }
                if (nextLine != null) {
                    return true;
                }
                try {
                    while (true) {
                        String line = reader.readLine();
                        if (line == null) {
                            finished = true;
                            return false;
                        }
                        if (!line.isEmpty()) {
                            nextLine = line;
                            return true;
                        }
                    }
                } catch (IOException exception) {
                    throw new RuntimeException("read http response failed", exception);
                }
            }

            @Override
            public PluginInStreamBase next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                String line = nextLine;
                nextLine = null;
                Map<String, Object> raw = parseJson(line);
                return PluginInStreamBase.builder()
                    .sessionId(String.valueOf(raw.get("session_id")))
                    .event(PluginInStreamEvent.valueOf(String.valueOf(raw.get("event"))))
                    .data(castToMap(raw.get("data")))
                    .build();
            }
        };

        return lineConverterWrapper(generator, dataType);
    }

    /**
     * 全双工方式的反向调用（通常用于本地或远程安装模式）。
     */
    protected Iterable<T> fullDuplexBackwardsInvoke(
        String backwardsRequestId,
        InvokeType type,
        Class<T> dataType,
        Map<String, Object> data
    ) {
        if (session == null) {
            throw new RuntimeException("current tool runtime does not support backwards invoke");
        }

        session.getWriter().sessionMessage(
            session.getSessionId(),
            session.getWriter().streamInvokeObject(
                Map.of(
                    "type", type.getValue(),
                    "backwards_request_id", backwardsRequestId,
                    "request", data
                )
            )
        );

        Predicate<PluginInStream> filter = stream -> stream.getEvent() == PluginInStreamEvent.BackwardInvocationResponse
            && backwardsRequestId.equals(stream.getData().get("backwards_request_id"));

        RequestReader.Reader reader = session.getReader().read(filter);
        Iterable<T> result = lineConverterWrapper(reader.read(1), dataType);
        return () -> new Iterator<>() {
            private final Iterator<T> delegate = result.iterator();
            private boolean closed;

            @Override
            public boolean hasNext() {
                boolean hasNext = delegate.hasNext();
                if (!hasNext) {
                    closeReader();
                }
                return hasNext;
            }

            @Override
            public T next() {
                T next = delegate.next();
                if (!delegate.hasNext()) {
                    closeReader();
                }
                return next;
            }

            private void closeReader() {
                if (closed) {
                    return;
                }
                closed = true;
                try {
                    reader.close();
                } catch (Exception exception) {
                    throw new RuntimeException(\"close reader failed\", exception);
                }
            }
        };
    }

    private HttpResponse<java.io.InputStream> sendRequest(HttpRequest request) {
        try {
            return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofInputStream());
        } catch (IOException | InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("http backwards invoke failed", exception);
        }
    }

    private Map<String, Object> parseJson(String line) {
        try {
            return OBJECT_MAPPER.readValue(line, new TypeReference<>() {});
        } catch (JsonProcessingException exception) {
            throw new RuntimeException("parse stream line failed", exception);
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> castToMap(Object data) {
        if (data instanceof Map) {
            return (Map<String, Object>) data;
        }
        return OBJECT_MAPPER.convertValue(data, new TypeReference<>() {});
    }

    private T convertData(Map<String, Object> data, Class<T> dataType) {
        if (dataType == Map.class) {
            return dataType.cast(data);
        }
        if (dataType == String.class) {
            return dataType.cast(OBJECT_MAPPER.convertValue(data, String.class));
        }
        return OBJECT_MAPPER.convertValue(data, dataType);
    }
}

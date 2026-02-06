package com.dify.invocations;

import com.dify.core.entities.invocation.InvokeType;
import com.dify.core.runtime.BackwardsInvocation;
import java.util.HexFormat;
import java.util.Map;

/**
 * 存储相关的反向调用。
 */
public class StorageInvocation extends BackwardsInvocation<Map<String, Object>> {

    private static final HexFormat HEX_FORMAT = HexFormat.of();

    /**
     * 写入持久化存储。
     *
     * @param key 存储键
     * @param value 存储值（原始二进制）
     */
    public void set(String key, byte[] value) {
        for (Map<String, Object> data : backwardsInvoke(
            InvokeType.Storage,
            Map.class,
            Map.of(
                "opt", "set",
                "key", key,
                "value", HEX_FORMAT.formatHex(value)
            )
        )) {
            Object responseData = data.get("data");
            if ("ok".equals(responseData)) {
                return;
            }
            throw new StorageInvocationError("unexpected data: " + responseData);
        }

        throw new StorageInvocationError("no data found");
    }

    /**
     * 读取持久化存储。
     *
     * @param key 存储键
     * @return 解码后的二进制数据
     */
    public byte[] get(String key) {
        for (Map<String, Object> data : backwardsInvoke(
            InvokeType.Storage,
            Map.class,
            Map.of(
                "opt", "get",
                "key", key
            )
        )) {
            Object responseData = data.get("data");
            if (responseData == null) {
                throw new StorageInvocationError("unexpected data: null");
            }
            return HEX_FORMAT.parseHex(responseData.toString());
        }

        throw new StorageInvocationError("no data found");
    }

    /**
     * 删除持久化存储。
     *
     * @param key 存储键
     */
    public void delete(String key) {
        for (Map<String, Object> data : backwardsInvoke(
            InvokeType.Storage,
            Map.class,
            Map.of(
                "opt", "del",
                "key", key
            )
        )) {
            Object responseData = data.get("data");
            if ("ok".equals(responseData)) {
                return;
            }
            throw new StorageInvocationError("unexpected data: " + responseData);
        }

        throw new StorageInvocationError("no data found");
    }

    /**
     * 判断持久化存储中是否存在指定 key。
     *
     * @param key 存储键
     * @return 是否存在
     */
    public boolean exist(String key) {
        for (Map<String, Object> data : backwardsInvoke(
            InvokeType.Storage,
            Map.class,
            Map.of(
                "opt", "exist",
                "key", key
            )
        )) {
            Object responseData = data.get("data");
            if (responseData instanceof Boolean) {
                return (Boolean) responseData;
            }
            return Boolean.parseBoolean(String.valueOf(responseData));
        }

        throw new StorageInvocationError("no data found");
    }
}

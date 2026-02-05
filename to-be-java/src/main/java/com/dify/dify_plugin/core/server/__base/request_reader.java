package com.dify.dify_plugin.core.server.__base;

import java.util.Iterator;
import java.util.function.Predicate;

/**
 * 对应 Python 的 RequestReader 抽象类。
 */
public abstract class request_reader {
    /**
     * 读取并返回 ReaderContext，支持使用过滤条件。
     *
     * @param filter 过滤条件
     * @return ReaderContext
     */
    public abstract ReaderContext read(Predicate<Object> filter);

    /**
     * ReaderContext 封装读取行为，Python 中使用 context manager。
     */
    public interface ReaderContext extends AutoCloseable {
        /**
         * 按轮询读取数据。
         *
         * @param timeoutForRound 超时时间（秒）
         * @return 迭代器
         */
        Iterator<Object> read(int timeoutForRound);
    }
}

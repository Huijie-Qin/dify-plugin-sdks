package com.dify.dify_plugin.core.server.__base;

import java.util.Iterator;
import java.util.function.Predicate;

/**
 * 对应 Python 的 filter_reader.py，提供过滤读取能力。
 */
public class filter_reader {
    private final request_reader.ReaderContext readerContext;
    private final Predicate<Object> filter;

    public filter_reader(request_reader.ReaderContext readerContext, Predicate<Object> filter) {
        this.readerContext = readerContext;
        this.filter = filter;
    }

    /**
     * 返回过滤后的迭代器。
     *
     * @param timeoutForRound 轮询超时
     * @return 过滤后的迭代器
     */
    public Iterator<Object> read(int timeoutForRound) {
        Iterator<Object> iterator = readerContext.read(timeoutForRound);
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Object next() {
                Object value = iterator.next();
                if (filter.test(value)) {
                    return value;
                }
                return null;
            }
        };
    }
}

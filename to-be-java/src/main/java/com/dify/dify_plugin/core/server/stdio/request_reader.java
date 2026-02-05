package com.dify.dify_plugin.core.server.stdio;

import com.dify.dify_plugin.core.server.__base.request_reader;
import java.util.Iterator;
import java.util.function.Predicate;

/**
 * 对应 Python 的 StdioRequestReader。
 */
public class request_reader extends request_reader {
    @Override
    public ReaderContext read(Predicate<Object> filter) {
        // Python 中从标准输入读取并转换为 PluginInStream。
        return timeoutForRound -> new Iterator<>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };
    }
}

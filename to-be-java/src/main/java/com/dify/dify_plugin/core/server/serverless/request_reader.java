package com.dify.dify_plugin.core.server.serverless;

import com.dify.dify_plugin.core.server.__base.request_reader;
import java.util.Iterator;
import java.util.function.Predicate;

/**
 * 对应 Python 的 ServerlessRequestReader。
 */
public class request_reader extends request_reader {
    @Override
    public ReaderContext read(Predicate<Object> filter) {
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

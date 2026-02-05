package com.dify.dify_plugin.core.utils;

/**
 * 对应 Python 的 position_helper.py。
 */
public class position_helper {
    /**
     * 将行号和列号拼接成可读字符串。
     *
     * @param line 行号
     * @param column 列号
     * @return 位置字符串
     */
    public static String toReadablePosition(int line, int column) {
        return "line " + line + ", column " + column;
    }
}

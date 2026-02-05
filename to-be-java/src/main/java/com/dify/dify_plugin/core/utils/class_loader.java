package com.dify.dify_plugin.core.utils;

/**
 * 对应 Python 的 class_loader.py。
 * 负责动态加载类/模块。
 */
public class class_loader {
    /**
     * 根据全限定名加载类。
     *
     * @param className 类名
     * @return Class 对象
     */
    public static Class<?> loadClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("无法加载类: " + className, e);
        }
    }
}

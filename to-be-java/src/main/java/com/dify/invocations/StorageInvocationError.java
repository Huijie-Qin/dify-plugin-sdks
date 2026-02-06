package com.dify.invocations;

/**
 * StorageInvocationError 对应 Python 中的自定义异常。
 * 当存储调用过程中返回异常数据时抛出。
 */
public class StorageInvocationError extends RuntimeException {
    public StorageInvocationError(String message) {
        super(message);
    }
}

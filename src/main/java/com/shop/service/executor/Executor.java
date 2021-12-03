package com.shop.service.executor;

public interface Executor<T, K> {
    boolean isValid(String value);

    K execute(T value);
}

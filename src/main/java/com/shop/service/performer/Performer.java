package com.shop.service.performer;

public interface Performer<T, K> {
    boolean isValid(Object value);

    K perform(T value);
}

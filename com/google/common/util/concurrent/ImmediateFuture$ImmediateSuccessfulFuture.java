package com.google.common.util.concurrent;

import javax.annotation.Nullable;

class ImmediateFuture$ImmediateSuccessfulFuture<V> extends ImmediateFuture<V> {
    static final ImmediateFuture$ImmediateSuccessfulFuture<Object> NULL = new ImmediateFuture$ImmediateSuccessfulFuture(null);
    @Nullable
    private final V value;

    ImmediateFuture$ImmediateSuccessfulFuture(@Nullable V value) {
        this.value = value;
    }

    public V get() {
        return this.value;
    }
}

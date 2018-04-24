package com.google.common.util.concurrent;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

@GwtIncompatible
class ImmediateFuture$ImmediateSuccessfulCheckedFuture<V, X extends Exception> extends ImmediateFuture<V> implements CheckedFuture<V, X> {
    @Nullable
    private final V value;

    ImmediateFuture$ImmediateSuccessfulCheckedFuture(@Nullable V value) {
        this.value = value;
    }

    public V get() {
        return this.value;
    }

    public V checkedGet() {
        return this.value;
    }

    public V checkedGet(long timeout, TimeUnit unit) {
        Preconditions.checkNotNull(unit);
        return this.value;
    }
}

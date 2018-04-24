package com.google.common.util.concurrent;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;

@GwtIncompatible
class Futures$MappingCheckedFuture<V, X extends Exception> extends AbstractCheckedFuture<V, X> {
    final Function<? super Exception, X> mapper;

    Futures$MappingCheckedFuture(ListenableFuture<V> delegate, Function<? super Exception, X> mapper) {
        super(delegate);
        this.mapper = (Function) Preconditions.checkNotNull(mapper);
    }

    protected X mapException(Exception e) {
        return (Exception) this.mapper.apply(e);
    }
}

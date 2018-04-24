package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import javax.annotation.Nullable;

final class AbstractTransformFuture$AsyncTransformFuture<I, O> extends AbstractTransformFuture<I, O, AsyncFunction<? super I, ? extends O>, ListenableFuture<? extends O>> {
    AbstractTransformFuture$AsyncTransformFuture(ListenableFuture<? extends I> inputFuture, AsyncFunction<? super I, ? extends O> function) {
        super(inputFuture, function);
    }

    ListenableFuture<? extends O> doTransform(AsyncFunction<? super I, ? extends O> function, @Nullable I input) throws Exception {
        ListenableFuture<? extends O> outputFuture = function.apply(input);
        Preconditions.checkNotNull(outputFuture, "AsyncFunction.apply returned null instead of a Future. Did you mean to return immediateFuture(null)?");
        return outputFuture;
    }

    void setResult(ListenableFuture<? extends O> result) {
        setFuture(result);
    }
}

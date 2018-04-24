package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

@GwtCompatible
@CanIgnoreReturnValue
@Beta
public final class Futures$FutureCombiner<V> {
    private final boolean allMustSucceed;
    private final ImmutableList<ListenableFuture<? extends V>> futures;

    private Futures$FutureCombiner(boolean allMustSucceed, ImmutableList<ListenableFuture<? extends V>> futures) {
        this.allMustSucceed = allMustSucceed;
        this.futures = futures;
    }

    public <C> ListenableFuture<C> callAsync(AsyncCallable<C> combiner, Executor executor) {
        return new CombinedFuture(this.futures, this.allMustSucceed, executor, (AsyncCallable) combiner);
    }

    public <C> ListenableFuture<C> callAsync(AsyncCallable<C> combiner) {
        return callAsync(combiner, MoreExecutors.directExecutor());
    }

    @CanIgnoreReturnValue
    public <C> ListenableFuture<C> call(Callable<C> combiner, Executor executor) {
        return new CombinedFuture(this.futures, this.allMustSucceed, executor, (Callable) combiner);
    }

    @CanIgnoreReturnValue
    public <C> ListenableFuture<C> call(Callable<C> combiner) {
        return call(combiner, MoreExecutors.directExecutor());
    }
}

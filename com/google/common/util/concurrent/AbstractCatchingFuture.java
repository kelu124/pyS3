package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.AbstractFuture.TrustedFuture;
import com.google.errorprone.annotations.ForOverride;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;

@GwtCompatible
abstract class AbstractCatchingFuture<V, X extends Throwable, F, T> extends TrustedFuture<V> implements Runnable {
    @Nullable
    Class<X> exceptionType;
    @Nullable
    F fallback;
    @Nullable
    ListenableFuture<? extends V> inputFuture;

    private static final class AsyncCatchingFuture<V, X extends Throwable> extends AbstractCatchingFuture<V, X, AsyncFunction<? super X, ? extends V>, ListenableFuture<? extends V>> {
        AsyncCatchingFuture(ListenableFuture<? extends V> input, Class<X> exceptionType, AsyncFunction<? super X, ? extends V> fallback) {
            super(input, exceptionType, fallback);
        }

        ListenableFuture<? extends V> doFallback(AsyncFunction<? super X, ? extends V> fallback, X cause) throws Exception {
            ListenableFuture<? extends V> replacement = fallback.apply(cause);
            Preconditions.checkNotNull(replacement, "AsyncFunction.apply returned null instead of a Future. Did you mean to return immediateFuture(null)?");
            return replacement;
        }

        void setResult(ListenableFuture<? extends V> result) {
            setFuture(result);
        }
    }

    private static final class CatchingFuture<V, X extends Throwable> extends AbstractCatchingFuture<V, X, Function<? super X, ? extends V>, V> {
        CatchingFuture(ListenableFuture<? extends V> input, Class<X> exceptionType, Function<? super X, ? extends V> fallback) {
            super(input, exceptionType, fallback);
        }

        @Nullable
        V doFallback(Function<? super X, ? extends V> fallback, X cause) throws Exception {
            return fallback.apply(cause);
        }

        void setResult(@Nullable V result) {
            set(result);
        }
    }

    @ForOverride
    @Nullable
    abstract T doFallback(F f, X x) throws Exception;

    @ForOverride
    abstract void setResult(@Nullable T t);

    static <X extends Throwable, V> ListenableFuture<V> create(ListenableFuture<? extends V> input, Class<X> exceptionType, Function<? super X, ? extends V> fallback) {
        CatchingFuture<V, X> future = new CatchingFuture(input, exceptionType, fallback);
        input.addListener(future, MoreExecutors.directExecutor());
        return future;
    }

    static <V, X extends Throwable> ListenableFuture<V> create(ListenableFuture<? extends V> input, Class<X> exceptionType, Function<? super X, ? extends V> fallback, Executor executor) {
        CatchingFuture<V, X> future = new CatchingFuture(input, exceptionType, fallback);
        input.addListener(future, MoreExecutors.rejectionPropagatingExecutor(executor, future));
        return future;
    }

    static <X extends Throwable, V> ListenableFuture<V> create(ListenableFuture<? extends V> input, Class<X> exceptionType, AsyncFunction<? super X, ? extends V> fallback) {
        AsyncCatchingFuture<V, X> future = new AsyncCatchingFuture(input, exceptionType, fallback);
        input.addListener(future, MoreExecutors.directExecutor());
        return future;
    }

    static <X extends Throwable, V> ListenableFuture<V> create(ListenableFuture<? extends V> input, Class<X> exceptionType, AsyncFunction<? super X, ? extends V> fallback, Executor executor) {
        AsyncCatchingFuture<V, X> future = new AsyncCatchingFuture(input, exceptionType, fallback);
        input.addListener(future, MoreExecutors.rejectionPropagatingExecutor(executor, future));
        return future;
    }

    AbstractCatchingFuture(ListenableFuture<? extends V> inputFuture, Class<X> exceptionType, F fallback) {
        this.inputFuture = (ListenableFuture) Preconditions.checkNotNull(inputFuture);
        this.exceptionType = (Class) Preconditions.checkNotNull(exceptionType);
        this.fallback = Preconditions.checkNotNull(fallback);
    }

    public final void run() {
        int i;
        int i2;
        int i3 = 1;
        ListenableFuture<? extends V> localInputFuture = this.inputFuture;
        Class<X> localExceptionType = this.exceptionType;
        F localFallback = this.fallback;
        if (localInputFuture == null) {
            i = 1;
        } else {
            i = 0;
        }
        if (localExceptionType == null) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        i2 |= i;
        if (localFallback != null) {
            i3 = 0;
        }
        if (((i3 | i2) | isCancelled()) == 0) {
            this.inputFuture = null;
            this.exceptionType = null;
            this.fallback = null;
            Object sourceResult = null;
            X throwable = null;
            try {
                sourceResult = Futures.getDone(localInputFuture);
            } catch (ExecutionException e) {
                Throwable throwable2 = (Throwable) Preconditions.checkNotNull(e.getCause());
            } catch (X e2) {
                throwable = e2;
            }
            if (throwable == null) {
                set(sourceResult);
            } else if (Platform.isInstanceOfThrowableClass(throwable, localExceptionType)) {
                try {
                    setResult(doFallback(localFallback, throwable));
                } catch (Throwable t) {
                    setException(t);
                }
            } else {
                setException(throwable);
            }
        }
    }

    protected final void afterDone() {
        maybePropagateCancellation(this.inputFuture);
        this.inputFuture = null;
        this.exceptionType = null;
        this.fallback = null;
    }
}

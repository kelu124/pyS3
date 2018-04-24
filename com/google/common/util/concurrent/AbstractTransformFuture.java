package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.AbstractFuture.TrustedFuture;
import com.google.errorprone.annotations.ForOverride;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;

@GwtCompatible
abstract class AbstractTransformFuture<I, O, F, T> extends TrustedFuture<O> implements Runnable {
    @Nullable
    F function;
    @Nullable
    ListenableFuture<? extends I> inputFuture;

    private static final class TransformFuture<I, O> extends AbstractTransformFuture<I, O, Function<? super I, ? extends O>, O> {
        TransformFuture(ListenableFuture<? extends I> inputFuture, Function<? super I, ? extends O> function) {
            super(inputFuture, function);
        }

        @Nullable
        O doTransform(Function<? super I, ? extends O> function, @Nullable I input) {
            return function.apply(input);
        }

        void setResult(@Nullable O result) {
            set(result);
        }
    }

    @ForOverride
    @Nullable
    abstract T doTransform(F f, @Nullable I i) throws Exception;

    @ForOverride
    abstract void setResult(@Nullable T t);

    static <I, O> ListenableFuture<O> create(ListenableFuture<I> input, AsyncFunction<? super I, ? extends O> function) {
        AsyncTransformFuture<I, O> output = new AsyncTransformFuture(input, function);
        input.addListener(output, MoreExecutors.directExecutor());
        return output;
    }

    static <I, O> ListenableFuture<O> create(ListenableFuture<I> input, AsyncFunction<? super I, ? extends O> function, Executor executor) {
        Preconditions.checkNotNull(executor);
        AsyncTransformFuture<I, O> output = new AsyncTransformFuture(input, function);
        input.addListener(output, MoreExecutors.rejectionPropagatingExecutor(executor, output));
        return output;
    }

    static <I, O> ListenableFuture<O> create(ListenableFuture<I> input, Function<? super I, ? extends O> function) {
        Preconditions.checkNotNull(function);
        TransformFuture<I, O> output = new TransformFuture(input, function);
        input.addListener(output, MoreExecutors.directExecutor());
        return output;
    }

    static <I, O> ListenableFuture<O> create(ListenableFuture<I> input, Function<? super I, ? extends O> function, Executor executor) {
        Preconditions.checkNotNull(function);
        TransformFuture<I, O> output = new TransformFuture(input, function);
        input.addListener(output, MoreExecutors.rejectionPropagatingExecutor(executor, output));
        return output;
    }

    AbstractTransformFuture(ListenableFuture<? extends I> inputFuture, F function) {
        this.inputFuture = (ListenableFuture) Preconditions.checkNotNull(inputFuture);
        this.function = Preconditions.checkNotNull(function);
    }

    public final void run() {
        int i;
        int i2 = 1;
        ListenableFuture<? extends I> localInputFuture = this.inputFuture;
        F localFunction = this.function;
        boolean isCancelled = isCancelled();
        if (localInputFuture == null) {
            i = 1;
        } else {
            i = 0;
        }
        i |= isCancelled;
        if (localFunction != null) {
            i2 = 0;
        }
        if ((i2 | i) == 0) {
            this.inputFuture = null;
            this.function = null;
            try {
                try {
                    setResult(doTransform(localFunction, Futures.getDone(localInputFuture)));
                } catch (UndeclaredThrowableException e) {
                    setException(e.getCause());
                } catch (Throwable t) {
                    setException(t);
                }
            } catch (CancellationException e2) {
                cancel(false);
            } catch (ExecutionException e3) {
                setException(e3.getCause());
            } catch (RuntimeException e4) {
                setException(e4);
            } catch (Error e5) {
                setException(e5);
            }
        }
    }

    protected final void afterDone() {
        maybePropagateCancellation(this.inputFuture);
        this.inputFuture = null;
        this.function = null;
    }
}

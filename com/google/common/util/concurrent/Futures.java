package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.Queues;
import com.google.common.util.concurrent.CollectionFuture.ListFuture;
import com.google.common.util.concurrent.ImmediateFuture.ImmediateCancelledFuture;
import com.google.common.util.concurrent.ImmediateFuture.ImmediateFailedCheckedFuture;
import com.google.common.util.concurrent.ImmediateFuture.ImmediateFailedFuture;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
@Beta
public final class Futures extends GwtFuturesCatchingSpecialization {
    private static final AsyncFunction<ListenableFuture<Object>, Object> DEREFERENCER = new 2();

    private Futures() {
    }

    @GwtIncompatible
    public static <V, X extends Exception> CheckedFuture<V, X> makeChecked(ListenableFuture<V> future, Function<? super Exception, X> mapper) {
        return new MappingCheckedFuture((ListenableFuture) Preconditions.checkNotNull(future), mapper);
    }

    public static <V> ListenableFuture<V> immediateFuture(@Nullable V value) {
        if (value == null) {
            return ImmediateFuture$ImmediateSuccessfulFuture.NULL;
        }
        return new ImmediateFuture$ImmediateSuccessfulFuture(value);
    }

    @GwtIncompatible
    public static <V, X extends Exception> CheckedFuture<V, X> immediateCheckedFuture(@Nullable V value) {
        return new ImmediateFuture$ImmediateSuccessfulCheckedFuture(value);
    }

    public static <V> ListenableFuture<V> immediateFailedFuture(Throwable throwable) {
        Preconditions.checkNotNull(throwable);
        return new ImmediateFailedFuture(throwable);
    }

    public static <V> ListenableFuture<V> immediateCancelledFuture() {
        return new ImmediateCancelledFuture();
    }

    @GwtIncompatible
    public static <V, X extends Exception> CheckedFuture<V, X> immediateFailedCheckedFuture(X exception) {
        Preconditions.checkNotNull(exception);
        return new ImmediateFailedCheckedFuture(exception);
    }

    @Partially$GwtIncompatible("AVAILABLE but requires exceptionType to be Throwable.class")
    public static <V, X extends Throwable> ListenableFuture<V> catching(ListenableFuture<? extends V> input, Class<X> exceptionType, Function<? super X, ? extends V> fallback) {
        return AbstractCatchingFuture.create((ListenableFuture) input, (Class) exceptionType, (Function) fallback);
    }

    @Partially$GwtIncompatible("AVAILABLE but requires exceptionType to be Throwable.class")
    public static <V, X extends Throwable> ListenableFuture<V> catching(ListenableFuture<? extends V> input, Class<X> exceptionType, Function<? super X, ? extends V> fallback, Executor executor) {
        return AbstractCatchingFuture.create((ListenableFuture) input, (Class) exceptionType, (Function) fallback, executor);
    }

    @CanIgnoreReturnValue
    @Partially$GwtIncompatible("AVAILABLE but requires exceptionType to be Throwable.class")
    public static <V, X extends Throwable> ListenableFuture<V> catchingAsync(ListenableFuture<? extends V> input, Class<X> exceptionType, AsyncFunction<? super X, ? extends V> fallback) {
        return AbstractCatchingFuture.create((ListenableFuture) input, (Class) exceptionType, (AsyncFunction) fallback);
    }

    @CanIgnoreReturnValue
    @Partially$GwtIncompatible("AVAILABLE but requires exceptionType to be Throwable.class")
    public static <V, X extends Throwable> ListenableFuture<V> catchingAsync(ListenableFuture<? extends V> input, Class<X> exceptionType, AsyncFunction<? super X, ? extends V> fallback, Executor executor) {
        return AbstractCatchingFuture.create((ListenableFuture) input, (Class) exceptionType, (AsyncFunction) fallback, executor);
    }

    @GwtIncompatible
    public static <V> ListenableFuture<V> withTimeout(ListenableFuture<V> delegate, long time, TimeUnit unit, ScheduledExecutorService scheduledExecutor) {
        return TimeoutFuture.create(delegate, time, unit, scheduledExecutor);
    }

    public static <I, O> ListenableFuture<O> transformAsync(ListenableFuture<I> input, AsyncFunction<? super I, ? extends O> function) {
        return AbstractTransformFuture.create((ListenableFuture) input, (AsyncFunction) function);
    }

    public static <I, O> ListenableFuture<O> transformAsync(ListenableFuture<I> input, AsyncFunction<? super I, ? extends O> function, Executor executor) {
        return AbstractTransformFuture.create((ListenableFuture) input, (AsyncFunction) function, executor);
    }

    public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> input, Function<? super I, ? extends O> function) {
        return AbstractTransformFuture.create((ListenableFuture) input, (Function) function);
    }

    public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> input, Function<? super I, ? extends O> function, Executor executor) {
        return AbstractTransformFuture.create((ListenableFuture) input, (Function) function, executor);
    }

    @GwtIncompatible
    public static <I, O> Future<O> lazyTransform(Future<I> input, Function<? super I, ? extends O> function) {
        Preconditions.checkNotNull(input);
        Preconditions.checkNotNull(function);
        return new 1(input, function);
    }

    public static <V> ListenableFuture<V> dereference(ListenableFuture<? extends ListenableFuture<? extends V>> nested) {
        return transformAsync(nested, DEREFERENCER);
    }

    @SafeVarargs
    @Beta
    public static <V> ListenableFuture<List<V>> allAsList(ListenableFuture<? extends V>... futures) {
        return new ListFuture(ImmutableList.copyOf((Object[]) futures), true);
    }

    @Beta
    public static <V> ListenableFuture<List<V>> allAsList(Iterable<? extends ListenableFuture<? extends V>> futures) {
        return new ListFuture(ImmutableList.copyOf((Iterable) futures), true);
    }

    @SafeVarargs
    public static <V> FutureCombiner<V> whenAllComplete(ListenableFuture<? extends V>... futures) {
        return new FutureCombiner(false, ImmutableList.copyOf((Object[]) futures), null);
    }

    public static <V> FutureCombiner<V> whenAllComplete(Iterable<? extends ListenableFuture<? extends V>> futures) {
        return new FutureCombiner(false, ImmutableList.copyOf((Iterable) futures), null);
    }

    @SafeVarargs
    public static <V> FutureCombiner<V> whenAllSucceed(ListenableFuture<? extends V>... futures) {
        return new FutureCombiner(true, ImmutableList.copyOf((Object[]) futures), null);
    }

    public static <V> FutureCombiner<V> whenAllSucceed(Iterable<? extends ListenableFuture<? extends V>> futures) {
        return new FutureCombiner(true, ImmutableList.copyOf((Iterable) futures), null);
    }

    public static <V> ListenableFuture<V> nonCancellationPropagating(ListenableFuture<V> future) {
        return new NonCancellationPropagatingFuture(future);
    }

    @SafeVarargs
    @Beta
    public static <V> ListenableFuture<List<V>> successfulAsList(ListenableFuture<? extends V>... futures) {
        return new ListFuture(ImmutableList.copyOf((Object[]) futures), false);
    }

    @Beta
    public static <V> ListenableFuture<List<V>> successfulAsList(Iterable<? extends ListenableFuture<? extends V>> futures) {
        return new ListFuture(ImmutableList.copyOf((Iterable) futures), false);
    }

    @GwtIncompatible
    @Beta
    public static <T> ImmutableList<ListenableFuture<T>> inCompletionOrder(Iterable<? extends ListenableFuture<? extends T>> futures) {
        ConcurrentLinkedQueue<SettableFuture<T>> delegates = Queues.newConcurrentLinkedQueue();
        Builder<ListenableFuture<T>> listBuilder = ImmutableList.builder();
        SerializingExecutor executor = new SerializingExecutor(MoreExecutors.directExecutor());
        for (ListenableFuture<? extends T> future : futures) {
            Object delegate = SettableFuture.create();
            delegates.add(delegate);
            future.addListener(new 3(delegates, future), executor);
            listBuilder.add(delegate);
        }
        return listBuilder.build();
    }

    public static <V> void addCallback(ListenableFuture<V> future, FutureCallback<? super V> callback) {
        addCallback(future, callback, MoreExecutors.directExecutor());
    }

    public static <V> void addCallback(ListenableFuture<V> future, FutureCallback<? super V> callback, Executor executor) {
        Preconditions.checkNotNull(callback);
        future.addListener(new 4(future, callback), executor);
    }

    @CanIgnoreReturnValue
    public static <V> V getDone(Future<V> future) throws ExecutionException {
        Preconditions.checkState(future.isDone(), "Future was expected to be done: %s", (Object) future);
        return Uninterruptibles.getUninterruptibly(future);
    }

    @GwtIncompatible
    @CanIgnoreReturnValue
    public static <V, X extends Exception> V getChecked(Future<V> future, Class<X> exceptionClass) throws Exception {
        return FuturesGetChecked.getChecked(future, exceptionClass);
    }

    @GwtIncompatible
    @CanIgnoreReturnValue
    public static <V, X extends Exception> V getChecked(Future<V> future, Class<X> exceptionClass, long timeout, TimeUnit unit) throws Exception {
        return FuturesGetChecked.getChecked(future, exceptionClass, timeout, unit);
    }

    @GwtIncompatible
    @CanIgnoreReturnValue
    public static <V> V getUnchecked(Future<V> future) {
        Preconditions.checkNotNull(future);
        try {
            return Uninterruptibles.getUninterruptibly(future);
        } catch (ExecutionException e) {
            wrapAndThrowUnchecked(e.getCause());
            throw new AssertionError();
        }
    }

    @GwtIncompatible
    private static void wrapAndThrowUnchecked(Throwable cause) {
        if (cause instanceof Error) {
            throw new ExecutionError((Error) cause);
        }
        throw new UncheckedExecutionException(cause);
    }
}

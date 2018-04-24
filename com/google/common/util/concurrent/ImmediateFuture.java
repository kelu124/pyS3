package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

@GwtCompatible(emulated = true)
abstract class ImmediateFuture<V> implements ListenableFuture<V> {
    private static final Logger log = Logger.getLogger(ImmediateFuture.class.getName());

    static final class ImmediateCancelledFuture<V> extends TrustedFuture<V> {
        ImmediateCancelledFuture() {
            cancel(false);
        }
    }

    @GwtIncompatible
    static class ImmediateFailedCheckedFuture<V, X extends Exception> extends ImmediateFuture<V> implements CheckedFuture<V, X> {
        private final X thrown;

        ImmediateFailedCheckedFuture(X thrown) {
            this.thrown = thrown;
        }

        public V get() throws ExecutionException {
            throw new ExecutionException(this.thrown);
        }

        public V checkedGet() throws Exception {
            throw this.thrown;
        }

        public V checkedGet(long timeout, TimeUnit unit) throws Exception {
            Preconditions.checkNotNull(unit);
            throw this.thrown;
        }
    }

    static final class ImmediateFailedFuture<V> extends TrustedFuture<V> {
        ImmediateFailedFuture(Throwable thrown) {
            setException(thrown);
        }
    }

    public abstract V get() throws ExecutionException;

    ImmediateFuture() {
    }

    public void addListener(Runnable listener, Executor executor) {
        Preconditions.checkNotNull(listener, "Runnable was null.");
        Preconditions.checkNotNull(executor, "Executor was null.");
        try {
            executor.execute(listener);
        } catch (RuntimeException e) {
            log.log(Level.SEVERE, "RuntimeException while executing runnable " + listener + " with executor " + executor, e);
        }
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    public V get(long timeout, TimeUnit unit) throws ExecutionException {
        Preconditions.checkNotNull(unit);
        return get();
    }

    public boolean isCancelled() {
        return false;
    }

    public boolean isDone() {
        return true;
    }
}

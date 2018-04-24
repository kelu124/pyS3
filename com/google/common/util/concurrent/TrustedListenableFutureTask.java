package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.RunnableFuture;
import javax.annotation.Nullable;

@GwtCompatible
class TrustedListenableFutureTask<V> extends TrustedFuture<V> implements RunnableFuture<V> {
    private TrustedFutureInterruptibleTask task;

    private final class TrustedFutureInterruptibleTask extends InterruptibleTask {
        private final Callable<V> callable;

        TrustedFutureInterruptibleTask(Callable<V> callable) {
            this.callable = (Callable) Preconditions.checkNotNull(callable);
        }

        void runInterruptibly() {
            if (!TrustedListenableFutureTask.this.isDone()) {
                try {
                    TrustedListenableFutureTask.this.set(this.callable.call());
                } catch (Throwable t) {
                    TrustedListenableFutureTask.this.setException(t);
                }
            }
        }

        boolean wasInterrupted() {
            return TrustedListenableFutureTask.this.wasInterrupted();
        }

        public String toString() {
            return this.callable.toString();
        }
    }

    static <V> TrustedListenableFutureTask<V> create(Callable<V> callable) {
        return new TrustedListenableFutureTask(callable);
    }

    static <V> TrustedListenableFutureTask<V> create(Runnable runnable, @Nullable V result) {
        return new TrustedListenableFutureTask(Executors.callable(runnable, result));
    }

    TrustedListenableFutureTask(Callable<V> callable) {
        this.task = new TrustedFutureInterruptibleTask(callable);
    }

    public void run() {
        TrustedFutureInterruptibleTask localTask = this.task;
        if (localTask != null) {
            localTask.run();
        }
    }

    protected void afterDone() {
        super.afterDone();
        if (wasInterrupted()) {
            TrustedFutureInterruptibleTask localTask = this.task;
            if (localTask != null) {
                localTask.interruptTask();
            }
        }
        this.task = null;
    }

    public String toString() {
        return super.toString() + " (delegate = " + this.task + ")";
    }
}

package com.google.common.util.concurrent;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.AbstractFuture.TrustedFuture;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.annotation.Nullable;

@GwtIncompatible
final class TimeoutFuture<V> extends TrustedFuture<V> {
    @Nullable
    private ListenableFuture<V> delegateRef;
    @Nullable
    private Future<?> timer;

    private static final class Fire<V> implements Runnable {
        @Nullable
        TimeoutFuture<V> timeoutFutureRef;

        Fire(TimeoutFuture<V> timeoutFuture) {
            this.timeoutFutureRef = timeoutFuture;
        }

        public void run() {
            TimeoutFuture<V> timeoutFuture = this.timeoutFutureRef;
            if (timeoutFuture != null) {
                ListenableFuture<V> delegate = timeoutFuture.delegateRef;
                if (delegate != null) {
                    this.timeoutFutureRef = null;
                    if (delegate.isDone()) {
                        timeoutFuture.setFuture(delegate);
                        return;
                    }
                    try {
                        timeoutFuture.setException(new TimeoutException("Future timed out: " + delegate));
                    } finally {
                        delegate.cancel(true);
                    }
                }
            }
        }
    }

    static <V> ListenableFuture<V> create(ListenableFuture<V> delegate, long time, TimeUnit unit, ScheduledExecutorService scheduledExecutor) {
        TimeoutFuture<V> result = new TimeoutFuture(delegate);
        Fire<V> fire = new Fire(result);
        result.timer = scheduledExecutor.schedule(fire, time, unit);
        delegate.addListener(fire, MoreExecutors.directExecutor());
        return result;
    }

    private TimeoutFuture(ListenableFuture<V> delegate) {
        this.delegateRef = (ListenableFuture) Preconditions.checkNotNull(delegate);
    }

    protected void afterDone() {
        maybePropagateCancellation(this.delegateRef);
        Future<?> localTimer = this.timer;
        if (localTimer != null) {
            localTimer.cancel(false);
        }
        this.delegateRef = null;
        this.timer = null;
    }
}

package com.google.common.util.concurrent;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.util.concurrent.ForwardingListenableFuture.SimpleForwardingListenableFuture;
import java.util.concurrent.Callable;
import java.util.concurrent.Delayed;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@GwtIncompatible
final class MoreExecutors$ScheduledListeningDecorator extends MoreExecutors$ListeningDecorator implements ListeningScheduledExecutorService {
    final ScheduledExecutorService delegate;

    private static final class ListenableScheduledTask<V> extends SimpleForwardingListenableFuture<V> implements ListenableScheduledFuture<V> {
        private final ScheduledFuture<?> scheduledDelegate;

        public ListenableScheduledTask(ListenableFuture<V> listenableDelegate, ScheduledFuture<?> scheduledDelegate) {
            super(listenableDelegate);
            this.scheduledDelegate = scheduledDelegate;
        }

        public boolean cancel(boolean mayInterruptIfRunning) {
            boolean cancelled = super.cancel(mayInterruptIfRunning);
            if (cancelled) {
                this.scheduledDelegate.cancel(mayInterruptIfRunning);
            }
            return cancelled;
        }

        public long getDelay(TimeUnit unit) {
            return this.scheduledDelegate.getDelay(unit);
        }

        public int compareTo(Delayed other) {
            return this.scheduledDelegate.compareTo(other);
        }
    }

    @GwtIncompatible
    private static final class NeverSuccessfulListenableFutureTask extends AbstractFuture<Void> implements Runnable {
        private final Runnable delegate;

        public NeverSuccessfulListenableFutureTask(Runnable delegate) {
            this.delegate = (Runnable) Preconditions.checkNotNull(delegate);
        }

        public void run() {
            try {
                this.delegate.run();
            } catch (Throwable t) {
                setException(t);
                RuntimeException propagate = Throwables.propagate(t);
            }
        }
    }

    MoreExecutors$ScheduledListeningDecorator(ScheduledExecutorService delegate) {
        super(delegate);
        this.delegate = (ScheduledExecutorService) Preconditions.checkNotNull(delegate);
    }

    public ListenableScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        TrustedListenableFutureTask<Void> task = TrustedListenableFutureTask.create(command, null);
        return new ListenableScheduledTask(task, this.delegate.schedule(task, delay, unit));
    }

    public <V> ListenableScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        TrustedListenableFutureTask<V> task = TrustedListenableFutureTask.create(callable);
        return new ListenableScheduledTask(task, this.delegate.schedule(task, delay, unit));
    }

    public ListenableScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        NeverSuccessfulListenableFutureTask task = new NeverSuccessfulListenableFutureTask(command);
        return new ListenableScheduledTask(task, this.delegate.scheduleAtFixedRate(task, initialDelay, period, unit));
    }

    public ListenableScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        NeverSuccessfulListenableFutureTask task = new NeverSuccessfulListenableFutureTask(command);
        return new ListenableScheduledTask(task, this.delegate.scheduleWithFixedDelay(task, initialDelay, delay, unit));
    }
}

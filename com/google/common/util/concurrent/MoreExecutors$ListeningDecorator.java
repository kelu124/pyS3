package com.google.common.util.concurrent;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@GwtIncompatible
class MoreExecutors$ListeningDecorator extends AbstractListeningExecutorService {
    private final ExecutorService delegate;

    MoreExecutors$ListeningDecorator(ExecutorService delegate) {
        this.delegate = (ExecutorService) Preconditions.checkNotNull(delegate);
    }

    public final boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return this.delegate.awaitTermination(timeout, unit);
    }

    public final boolean isShutdown() {
        return this.delegate.isShutdown();
    }

    public final boolean isTerminated() {
        return this.delegate.isTerminated();
    }

    public final void shutdown() {
        this.delegate.shutdown();
    }

    public final List<Runnable> shutdownNow() {
        return this.delegate.shutdownNow();
    }

    public final void execute(Runnable command) {
        this.delegate.execute(command);
    }
}

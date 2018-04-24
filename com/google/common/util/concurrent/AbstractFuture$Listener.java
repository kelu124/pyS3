package com.google.common.util.concurrent;

import java.util.concurrent.Executor;
import javax.annotation.Nullable;

final class AbstractFuture$Listener {
    static final AbstractFuture$Listener TOMBSTONE = new AbstractFuture$Listener(null, null);
    final Executor executor;
    @Nullable
    AbstractFuture$Listener next;
    final Runnable task;

    AbstractFuture$Listener(Runnable task, Executor executor) {
        this.task = task;
        this.executor = executor;
    }
}

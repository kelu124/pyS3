package com.google.common.util.concurrent;

import java.util.concurrent.locks.LockSupport;
import javax.annotation.Nullable;

final class AbstractFuture$Waiter {
    static final AbstractFuture$Waiter TOMBSTONE = new AbstractFuture$Waiter(false);
    @Nullable
    volatile AbstractFuture$Waiter next;
    @Nullable
    volatile Thread thread;

    AbstractFuture$Waiter(boolean unused) {
    }

    AbstractFuture$Waiter() {
        AbstractFuture.access$200().putThread(this, Thread.currentThread());
    }

    void setNext(AbstractFuture$Waiter next) {
        AbstractFuture.access$200().putNext(this, next);
    }

    void unpark() {
        Thread w = this.thread;
        if (w != null) {
            this.thread = null;
            LockSupport.unpark(w);
        }
    }
}

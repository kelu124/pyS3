package com.google.common.util.concurrent;

import com.google.common.annotations.GwtIncompatible;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import javax.annotation.concurrent.GuardedBy;

@GwtIncompatible
final class MoreExecutors$DirectExecutorService extends AbstractListeningExecutorService {
    private final Object lock;
    @GuardedBy("lock")
    private int runningTasks;
    @GuardedBy("lock")
    private boolean shutdown;

    private MoreExecutors$DirectExecutorService() {
        this.lock = new Object();
        this.runningTasks = 0;
        this.shutdown = false;
    }

    public void execute(Runnable command) {
        startTask();
        try {
            command.run();
        } finally {
            endTask();
        }
    }

    public boolean isShutdown() {
        boolean z;
        synchronized (this.lock) {
            z = this.shutdown;
        }
        return z;
    }

    public void shutdown() {
        synchronized (this.lock) {
            this.shutdown = true;
            if (this.runningTasks == 0) {
                this.lock.notifyAll();
            }
        }
    }

    public List<Runnable> shutdownNow() {
        shutdown();
        return Collections.emptyList();
    }

    public boolean isTerminated() {
        boolean z;
        synchronized (this.lock) {
            z = this.shutdown && this.runningTasks == 0;
        }
        return z;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean awaitTermination(long r10, java.util.concurrent.TimeUnit r12) throws java.lang.InterruptedException {
        /*
        r9 = this;
        r0 = r12.toNanos(r10);
        r5 = r9.lock;
        monitor-enter(r5);
    L_0x0007:
        r4 = r9.shutdown;	 Catch:{ all -> 0x001b }
        if (r4 == 0) goto L_0x0012;
    L_0x000b:
        r4 = r9.runningTasks;	 Catch:{ all -> 0x001b }
        if (r4 != 0) goto L_0x0012;
    L_0x000f:
        r4 = 1;
        monitor-exit(r5);	 Catch:{ all -> 0x001b }
    L_0x0011:
        return r4;
    L_0x0012:
        r6 = 0;
        r4 = (r0 > r6 ? 1 : (r0 == r6 ? 0 : -1));
        if (r4 > 0) goto L_0x001e;
    L_0x0018:
        r4 = 0;
        monitor-exit(r5);	 Catch:{ all -> 0x001b }
        goto L_0x0011;
    L_0x001b:
        r4 = move-exception;
        monitor-exit(r5);	 Catch:{ all -> 0x001b }
        throw r4;
    L_0x001e:
        r2 = java.lang.System.nanoTime();	 Catch:{ all -> 0x001b }
        r4 = java.util.concurrent.TimeUnit.NANOSECONDS;	 Catch:{ all -> 0x001b }
        r6 = r9.lock;	 Catch:{ all -> 0x001b }
        r4.timedWait(r6, r0);	 Catch:{ all -> 0x001b }
        r6 = java.lang.System.nanoTime();	 Catch:{ all -> 0x001b }
        r6 = r6 - r2;
        r0 = r0 - r6;
        goto L_0x0007;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.MoreExecutors$DirectExecutorService.awaitTermination(long, java.util.concurrent.TimeUnit):boolean");
    }

    private void startTask() {
        synchronized (this.lock) {
            if (this.shutdown) {
                throw new RejectedExecutionException("Executor already shutdown");
            }
            this.runningTasks++;
        }
    }

    private void endTask() {
        synchronized (this.lock) {
            int numRunning = this.runningTasks - 1;
            this.runningTasks = numRunning;
            if (numRunning == 0) {
                this.lock.notifyAll();
            }
        }
    }
}

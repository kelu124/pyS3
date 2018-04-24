package com.google.common.util.concurrent;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.Queues;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.concurrent.GuardedBy;

@GwtIncompatible
final class ListenerCallQueue<L> implements Runnable {
    private static final Logger logger = Logger.getLogger(ListenerCallQueue.class.getName());
    private final Executor executor;
    @GuardedBy("this")
    private boolean isThreadScheduled;
    private final L listener;
    @GuardedBy("this")
    private final Queue<Callback<L>> waitQueue = Queues.newArrayDeque();

    static abstract class Callback<L> {
        private final String methodCall;

        abstract void call(L l);

        Callback(String methodCall) {
            this.methodCall = methodCall;
        }

        void enqueueOn(Iterable<ListenerCallQueue<L>> queues) {
            for (ListenerCallQueue<L> queue : queues) {
                queue.add(this);
            }
        }
    }

    ListenerCallQueue(L listener, Executor executor) {
        this.listener = Preconditions.checkNotNull(listener);
        this.executor = (Executor) Preconditions.checkNotNull(executor);
    }

    synchronized void add(Callback<L> callback) {
        this.waitQueue.add(callback);
    }

    void execute() {
        boolean scheduleTaskRunner = false;
        synchronized (this) {
            if (!this.isThreadScheduled) {
                this.isThreadScheduled = true;
                scheduleTaskRunner = true;
            }
        }
        if (scheduleTaskRunner) {
            try {
                this.executor.execute(this);
            } catch (RuntimeException e) {
                synchronized (this) {
                    this.isThreadScheduled = false;
                    logger.log(Level.SEVERE, "Exception while running callbacks for " + this.listener + " on " + this.executor, e);
                    throw e;
                }
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
        r7 = this;
        r2 = 1;
    L_0x0001:
        monitor-enter(r7);	 Catch:{ all -> 0x0051 }
        r3 = r7.isThreadScheduled;	 Catch:{ all -> 0x005a }
        com.google.common.base.Preconditions.checkState(r3);	 Catch:{ all -> 0x005a }
        r3 = r7.waitQueue;	 Catch:{ all -> 0x005a }
        r1 = r3.poll();	 Catch:{ all -> 0x005a }
        r1 = (com.google.common.util.concurrent.ListenerCallQueue.Callback) r1;	 Catch:{ all -> 0x005a }
        if (r1 != 0) goto L_0x001e;
    L_0x0011:
        r3 = 0;
        r7.isThreadScheduled = r3;	 Catch:{ all -> 0x005a }
        r2 = 0;
        monitor-exit(r7);	 Catch:{ all -> 0x005a }
        if (r2 == 0) goto L_0x001d;
    L_0x0018:
        monitor-enter(r7);
        r3 = 0;
        r7.isThreadScheduled = r3;	 Catch:{ all -> 0x005d }
        monitor-exit(r7);	 Catch:{ all -> 0x005d }
    L_0x001d:
        return;
    L_0x001e:
        monitor-exit(r7);	 Catch:{ all -> 0x005a }
        r3 = r7.listener;	 Catch:{ RuntimeException -> 0x0025 }
        r1.call(r3);	 Catch:{ RuntimeException -> 0x0025 }
        goto L_0x0001;
    L_0x0025:
        r0 = move-exception;
        r3 = logger;	 Catch:{ all -> 0x0051 }
        r4 = java.util.logging.Level.SEVERE;	 Catch:{ all -> 0x0051 }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0051 }
        r5.<init>();	 Catch:{ all -> 0x0051 }
        r6 = "Exception while executing callback: ";
        r5 = r5.append(r6);	 Catch:{ all -> 0x0051 }
        r6 = r7.listener;	 Catch:{ all -> 0x0051 }
        r5 = r5.append(r6);	 Catch:{ all -> 0x0051 }
        r6 = ".";
        r5 = r5.append(r6);	 Catch:{ all -> 0x0051 }
        r6 = r1.methodCall;	 Catch:{ all -> 0x0051 }
        r5 = r5.append(r6);	 Catch:{ all -> 0x0051 }
        r5 = r5.toString();	 Catch:{ all -> 0x0051 }
        r3.log(r4, r5, r0);	 Catch:{ all -> 0x0051 }
        goto L_0x0001;
    L_0x0051:
        r3 = move-exception;
        if (r2 == 0) goto L_0x0059;
    L_0x0054:
        monitor-enter(r7);
        r4 = 0;
        r7.isThreadScheduled = r4;	 Catch:{ all -> 0x0060 }
        monitor-exit(r7);	 Catch:{ all -> 0x0060 }
    L_0x0059:
        throw r3;
    L_0x005a:
        r3 = move-exception;
        monitor-exit(r7);	 Catch:{ all -> 0x005a }
        throw r3;	 Catch:{ all -> 0x0051 }
    L_0x005d:
        r3 = move-exception;
        monitor-exit(r7);	 Catch:{ all -> 0x005d }
        throw r3;
    L_0x0060:
        r3 = move-exception;
        monitor-exit(r7);	 Catch:{ all -> 0x0060 }
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.ListenerCallQueue.run():void");
    }
}

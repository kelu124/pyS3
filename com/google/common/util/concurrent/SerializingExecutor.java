package com.google.common.util.concurrent;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.Executor;
import java.util.logging.Logger;
import javax.annotation.concurrent.GuardedBy;

@GwtIncompatible
final class SerializingExecutor implements Executor {
    private static final Logger log = Logger.getLogger(SerializingExecutor.class.getName());
    private final Executor executor;
    private final Object internalLock = new Object();
    @GuardedBy("internalLock")
    private boolean isWorkerRunning = false;
    @GuardedBy("internalLock")
    private final Deque<Runnable> queue = new ArrayDeque();
    @GuardedBy("internalLock")
    private int suspensions = 0;

    private final class QueueWorker implements Runnable {
        private QueueWorker() {
        }

        public void run() {
            try {
                workOnQueue();
            } catch (Error e) {
                synchronized (SerializingExecutor.this.internalLock) {
                    SerializingExecutor.this.isWorkerRunning = false;
                    throw e;
                }
            }
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private void workOnQueue() {
            /*
            r7 = this;
        L_0x0000:
            r2 = 0;
            r3 = com.google.common.util.concurrent.SerializingExecutor.this;
            r4 = r3.internalLock;
            monitor-enter(r4);
            r3 = com.google.common.util.concurrent.SerializingExecutor.this;	 Catch:{ all -> 0x004b }
            r3 = r3.suspensions;	 Catch:{ all -> 0x004b }
            if (r3 != 0) goto L_0x001e;
        L_0x0010:
            r3 = com.google.common.util.concurrent.SerializingExecutor.this;	 Catch:{ all -> 0x004b }
            r3 = r3.queue;	 Catch:{ all -> 0x004b }
            r3 = r3.poll();	 Catch:{ all -> 0x004b }
            r0 = r3;
            r0 = (java.lang.Runnable) r0;	 Catch:{ all -> 0x004b }
            r2 = r0;
        L_0x001e:
            if (r2 != 0) goto L_0x0028;
        L_0x0020:
            r3 = com.google.common.util.concurrent.SerializingExecutor.this;	 Catch:{ all -> 0x004b }
            r5 = 0;
            r3.isWorkerRunning = r5;	 Catch:{ all -> 0x004b }
            monitor-exit(r4);	 Catch:{ all -> 0x004b }
            return;
        L_0x0028:
            monitor-exit(r4);	 Catch:{ all -> 0x004b }
            r2.run();	 Catch:{ RuntimeException -> 0x002d }
            goto L_0x0000;
        L_0x002d:
            r1 = move-exception;
            r3 = com.google.common.util.concurrent.SerializingExecutor.log;
            r4 = java.util.logging.Level.SEVERE;
            r5 = new java.lang.StringBuilder;
            r5.<init>();
            r6 = "Exception while executing runnable ";
            r5 = r5.append(r6);
            r5 = r5.append(r2);
            r5 = r5.toString();
            r3.log(r4, r5, r1);
            goto L_0x0000;
        L_0x004b:
            r3 = move-exception;
            monitor-exit(r4);	 Catch:{ all -> 0x004b }
            throw r3;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.SerializingExecutor.QueueWorker.workOnQueue():void");
        }
    }

    public SerializingExecutor(Executor executor) {
        this.executor = (Executor) Preconditions.checkNotNull(executor);
    }

    public void execute(Runnable task) {
        synchronized (this.internalLock) {
            this.queue.add(task);
        }
        startQueueWorker();
    }

    public void executeFirst(Runnable task) {
        synchronized (this.internalLock) {
            this.queue.addFirst(task);
        }
        startQueueWorker();
    }

    public void suspend() {
        synchronized (this.internalLock) {
            this.suspensions++;
        }
    }

    public void resume() {
        synchronized (this.internalLock) {
            Preconditions.checkState(this.suspensions > 0);
            this.suspensions--;
        }
        startQueueWorker();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void startQueueWorker() {
        /*
        r4 = this;
        r2 = r4.internalLock;
        monitor-enter(r2);
        r1 = r4.queue;	 Catch:{ all -> 0x0013 }
        r1 = r1.peek();	 Catch:{ all -> 0x0013 }
        if (r1 != 0) goto L_0x000d;
    L_0x000b:
        monitor-exit(r2);	 Catch:{ all -> 0x0013 }
    L_0x000c:
        return;
    L_0x000d:
        r1 = r4.suspensions;	 Catch:{ all -> 0x0013 }
        if (r1 <= 0) goto L_0x0016;
    L_0x0011:
        monitor-exit(r2);	 Catch:{ all -> 0x0013 }
        goto L_0x000c;
    L_0x0013:
        r1 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0013 }
        throw r1;
    L_0x0016:
        r1 = r4.isWorkerRunning;	 Catch:{ all -> 0x0013 }
        if (r1 == 0) goto L_0x001c;
    L_0x001a:
        monitor-exit(r2);	 Catch:{ all -> 0x0013 }
        goto L_0x000c;
    L_0x001c:
        r1 = 1;
        r4.isWorkerRunning = r1;	 Catch:{ all -> 0x0013 }
        monitor-exit(r2);	 Catch:{ all -> 0x0013 }
        r0 = 1;
        r1 = r4.executor;	 Catch:{ all -> 0x003a }
        r2 = new com.google.common.util.concurrent.SerializingExecutor$QueueWorker;	 Catch:{ all -> 0x003a }
        r3 = 0;
        r2.<init>();	 Catch:{ all -> 0x003a }
        r1.execute(r2);	 Catch:{ all -> 0x003a }
        r0 = 0;
        if (r0 == 0) goto L_0x000c;
    L_0x002f:
        r2 = r4.internalLock;
        monitor-enter(r2);
        r1 = 0;
        r4.isWorkerRunning = r1;	 Catch:{ all -> 0x0037 }
        monitor-exit(r2);	 Catch:{ all -> 0x0037 }
        goto L_0x000c;
    L_0x0037:
        r1 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0037 }
        throw r1;
    L_0x003a:
        r1 = move-exception;
        if (r0 == 0) goto L_0x0044;
    L_0x003d:
        r2 = r4.internalLock;
        monitor-enter(r2);
        r3 = 0;
        r4.isWorkerRunning = r3;	 Catch:{ all -> 0x0045 }
        monitor-exit(r2);	 Catch:{ all -> 0x0045 }
    L_0x0044:
        throw r1;
    L_0x0045:
        r1 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0045 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.SerializingExecutor.startQueueWorker():void");
    }
}

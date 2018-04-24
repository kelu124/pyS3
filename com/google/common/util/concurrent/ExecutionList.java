package com.google.common.util.concurrent;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.concurrent.GuardedBy;

@GwtIncompatible
public final class ExecutionList {
    private static final Logger log = Logger.getLogger(ExecutionList.class.getName());
    @GuardedBy("this")
    private boolean executed;
    @GuardedBy("this")
    private RunnableExecutorPair runnables;

    public void add(Runnable runnable, Executor executor) {
        Preconditions.checkNotNull(runnable, "Runnable was null.");
        Preconditions.checkNotNull(executor, "Executor was null.");
        synchronized (this) {
            if (this.executed) {
                executeListener(runnable, executor);
                return;
            }
            this.runnables = new RunnableExecutorPair(runnable, executor, this.runnables);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void execute() {
        /*
        r5 = this;
        monitor-enter(r5);
        r3 = r5.executed;	 Catch:{ all -> 0x001a }
        if (r3 == 0) goto L_0x0007;
    L_0x0005:
        monitor-exit(r5);	 Catch:{ all -> 0x001a }
    L_0x0006:
        return;
    L_0x0007:
        r3 = 1;
        r5.executed = r3;	 Catch:{ all -> 0x001a }
        r0 = r5.runnables;	 Catch:{ all -> 0x001a }
        r3 = 0;
        r5.runnables = r3;	 Catch:{ all -> 0x001a }
        monitor-exit(r5);	 Catch:{ all -> 0x001a }
        r1 = 0;
    L_0x0011:
        if (r0 == 0) goto L_0x001d;
    L_0x0013:
        r2 = r0;
        r0 = r0.next;
        r2.next = r1;
        r1 = r2;
        goto L_0x0011;
    L_0x001a:
        r3 = move-exception;
        monitor-exit(r5);	 Catch:{ all -> 0x001a }
        throw r3;
    L_0x001d:
        if (r1 == 0) goto L_0x0006;
    L_0x001f:
        r3 = r1.runnable;
        r4 = r1.executor;
        executeListener(r3, r4);
        r1 = r1.next;
        goto L_0x001d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.ExecutionList.execute():void");
    }

    private static void executeListener(Runnable runnable, Executor executor) {
        try {
            executor.execute(runnable);
        } catch (RuntimeException e) {
            log.log(Level.SEVERE, "RuntimeException while executing runnable " + runnable + " with executor " + executor, e);
        }
    }
}

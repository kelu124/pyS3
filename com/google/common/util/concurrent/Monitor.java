package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.j2objc.annotations.Weak;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.concurrent.GuardedBy;

@GwtIncompatible
@Beta
public final class Monitor {
    @GuardedBy("lock")
    private Guard activeGuards;
    private final boolean fair;
    private final ReentrantLock lock;

    @Beta
    public static abstract class Guard {
        final Condition condition;
        @Weak
        final Monitor monitor;
        @GuardedBy("monitor.lock")
        Guard next;
        @GuardedBy("monitor.lock")
        int waiterCount = 0;

        public abstract boolean isSatisfied();

        protected Guard(Monitor monitor) {
            this.monitor = (Monitor) Preconditions.checkNotNull(monitor, "monitor");
            this.condition = monitor.lock.newCondition();
        }
    }

    public boolean enterIf(com.google.common.util.concurrent.Monitor.Guard r5, long r6, java.util.concurrent.TimeUnit r8) {
        /* JADX: method processing error */
/*
Error: java.util.NoSuchElementException
	at java.util.HashMap$HashIterator.nextNode(HashMap.java:1431)
	at java.util.HashMap$KeyIterator.next(HashMap.java:1453)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.applyRemove(BlockFinallyExtract.java:535)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.extractFinally(BlockFinallyExtract.java:175)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.processExceptionHandler(BlockFinallyExtract.java:79)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:51)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r4 = this;
        r1 = r5.monitor;
        if (r1 == r4) goto L_0x000a;
    L_0x0004:
        r1 = new java.lang.IllegalMonitorStateException;
        r1.<init>();
        throw r1;
    L_0x000a:
        r1 = r4.enter(r6, r8);
        if (r1 != 0) goto L_0x0012;
    L_0x0010:
        r0 = 0;
    L_0x0011:
        return r0;
    L_0x0012:
        r0 = 0;
        r0 = r5.isSatisfied();	 Catch:{ all -> 0x001f }
        if (r0 != 0) goto L_0x0011;
    L_0x0019:
        r1 = r4.lock;
        r1.unlock();
        goto L_0x0011;
    L_0x001f:
        r1 = move-exception;
        if (r0 != 0) goto L_0x0027;
    L_0x0022:
        r2 = r4.lock;
        r2.unlock();
    L_0x0027:
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.Monitor.enterIf(com.google.common.util.concurrent.Monitor$Guard, long, java.util.concurrent.TimeUnit):boolean");
    }

    public boolean enterIfInterruptibly(com.google.common.util.concurrent.Monitor.Guard r5, long r6, java.util.concurrent.TimeUnit r8) throws java.lang.InterruptedException {
        /* JADX: method processing error */
/*
Error: java.util.NoSuchElementException
	at java.util.HashMap$HashIterator.nextNode(HashMap.java:1431)
	at java.util.HashMap$KeyIterator.next(HashMap.java:1453)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.applyRemove(BlockFinallyExtract.java:535)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.extractFinally(BlockFinallyExtract.java:175)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.processExceptionHandler(BlockFinallyExtract.java:79)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:51)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r4 = this;
        r2 = r5.monitor;
        if (r2 == r4) goto L_0x000a;
    L_0x0004:
        r2 = new java.lang.IllegalMonitorStateException;
        r2.<init>();
        throw r2;
    L_0x000a:
        r0 = r4.lock;
        r2 = r0.tryLock(r6, r8);
        if (r2 != 0) goto L_0x0014;
    L_0x0012:
        r1 = 0;
    L_0x0013:
        return r1;
    L_0x0014:
        r1 = 0;
        r1 = r5.isSatisfied();	 Catch:{ all -> 0x001f }
        if (r1 != 0) goto L_0x0013;
    L_0x001b:
        r0.unlock();
        goto L_0x0013;
    L_0x001f:
        r2 = move-exception;
        if (r1 != 0) goto L_0x0025;
    L_0x0022:
        r0.unlock();
    L_0x0025:
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.Monitor.enterIfInterruptibly(com.google.common.util.concurrent.Monitor$Guard, long, java.util.concurrent.TimeUnit):boolean");
    }

    public boolean tryEnterIf(com.google.common.util.concurrent.Monitor.Guard r4) {
        /* JADX: method processing error */
/*
Error: java.util.NoSuchElementException
	at java.util.HashMap$HashIterator.nextNode(HashMap.java:1431)
	at java.util.HashMap$KeyIterator.next(HashMap.java:1453)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.applyRemove(BlockFinallyExtract.java:535)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.extractFinally(BlockFinallyExtract.java:175)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.processExceptionHandler(BlockFinallyExtract.java:79)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:51)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r3 = this;
        r2 = r4.monitor;
        if (r2 == r3) goto L_0x000a;
    L_0x0004:
        r2 = new java.lang.IllegalMonitorStateException;
        r2.<init>();
        throw r2;
    L_0x000a:
        r0 = r3.lock;
        r2 = r0.tryLock();
        if (r2 != 0) goto L_0x0014;
    L_0x0012:
        r1 = 0;
    L_0x0013:
        return r1;
    L_0x0014:
        r1 = 0;
        r1 = r4.isSatisfied();	 Catch:{ all -> 0x001f }
        if (r1 != 0) goto L_0x0013;
    L_0x001b:
        r0.unlock();
        goto L_0x0013;
    L_0x001f:
        r2 = move-exception;
        if (r1 != 0) goto L_0x0025;
    L_0x0022:
        r0.unlock();
    L_0x0025:
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.Monitor.tryEnterIf(com.google.common.util.concurrent.Monitor$Guard):boolean");
    }

    public boolean waitForUninterruptibly(com.google.common.util.concurrent.Monitor.Guard r13, long r14, java.util.concurrent.TimeUnit r16) {
        /* JADX: method processing error */
/*
Error: java.util.NoSuchElementException
	at java.util.HashMap$HashIterator.nextNode(HashMap.java:1431)
	at java.util.HashMap$KeyIterator.next(HashMap.java:1453)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.applyRemove(BlockFinallyExtract.java:535)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.extractFinally(BlockFinallyExtract.java:175)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.processExceptionHandler(BlockFinallyExtract.java:79)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:51)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r12 = this;
        r8 = toSafeNanos(r14, r16);
        r5 = r13.monitor;
        if (r5 != r12) goto L_0x0018;
    L_0x0008:
        r5 = 1;
    L_0x0009:
        r10 = r12.lock;
        r10 = r10.isHeldByCurrentThread();
        r5 = r5 & r10;
        if (r5 != 0) goto L_0x001a;
    L_0x0012:
        r5 = new java.lang.IllegalMonitorStateException;
        r5.<init>();
        throw r5;
    L_0x0018:
        r5 = 0;
        goto L_0x0009;
    L_0x001a:
        r5 = r13.isSatisfied();
        if (r5 == 0) goto L_0x0022;
    L_0x0020:
        r5 = 1;
    L_0x0021:
        return r5;
    L_0x0022:
        r4 = 1;
        r6 = initNanoTime(r8);
        r1 = java.lang.Thread.interrupted();
        r2 = r8;
    L_0x002c:
        r5 = r12.awaitNanos(r13, r2, r4);	 Catch:{ InterruptedException -> 0x003a, all -> 0x0053 }
        if (r1 == 0) goto L_0x0021;
    L_0x0032:
        r10 = java.lang.Thread.currentThread();
        r10.interrupt();
        goto L_0x0021;
    L_0x003a:
        r0 = move-exception;
        r1 = 1;
        r5 = r13.isSatisfied();	 Catch:{ InterruptedException -> 0x003a, all -> 0x0053 }
        if (r5 == 0) goto L_0x004d;
    L_0x0042:
        r5 = 1;
        if (r1 == 0) goto L_0x0021;
    L_0x0045:
        r10 = java.lang.Thread.currentThread();
        r10.interrupt();
        goto L_0x0021;
    L_0x004d:
        r4 = 0;
        r2 = remainingNanos(r6, r8);	 Catch:{ InterruptedException -> 0x003a, all -> 0x0053 }
        goto L_0x002c;
    L_0x0053:
        r5 = move-exception;
        if (r1 == 0) goto L_0x005d;
    L_0x0056:
        r10 = java.lang.Thread.currentThread();
        r10.interrupt();
    L_0x005d:
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.Monitor.waitForUninterruptibly(com.google.common.util.concurrent.Monitor$Guard, long, java.util.concurrent.TimeUnit):boolean");
    }

    public Monitor() {
        this(false);
    }

    public Monitor(boolean fair) {
        this.activeGuards = null;
        this.fair = fair;
        this.lock = new ReentrantLock(fair);
    }

    public void enter() {
        this.lock.lock();
    }

    public void enterInterruptibly() throws InterruptedException {
        this.lock.lockInterruptibly();
    }

    public boolean enter(long time, TimeUnit unit) {
        long startTime;
        long timeoutNanos = toSafeNanos(time, unit);
        ReentrantLock lock = this.lock;
        if (!this.fair && lock.tryLock()) {
            return true;
        }
        boolean interrupted = Thread.interrupted();
        long remainingNanos;
        try {
            boolean tryLock;
            startTime = System.nanoTime();
            remainingNanos = timeoutNanos;
            while (true) {
                tryLock = lock.tryLock(remainingNanos, TimeUnit.NANOSECONDS);
                break;
            }
            if (!interrupted) {
                return tryLock;
            }
            Thread.currentThread().interrupt();
            return tryLock;
        } catch (InterruptedException e) {
            interrupted = true;
            remainingNanos = remainingNanos(startTime, timeoutNanos);
        } catch (Throwable th) {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public boolean enterInterruptibly(long time, TimeUnit unit) throws InterruptedException {
        return this.lock.tryLock(time, unit);
    }

    public boolean tryEnter() {
        return this.lock.tryLock();
    }

    public void enterWhen(Guard guard) throws InterruptedException {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        ReentrantLock lock = this.lock;
        boolean signalBeforeWaiting = lock.isHeldByCurrentThread();
        lock.lockInterruptibly();
        boolean satisfied = false;
        try {
            if (!guard.isSatisfied()) {
                await(guard, signalBeforeWaiting);
            }
            satisfied = true;
        } finally {
            if (!satisfied) {
                leave();
            }
        }
    }

    public void enterWhenUninterruptibly(Guard guard) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        ReentrantLock lock = this.lock;
        boolean signalBeforeWaiting = lock.isHeldByCurrentThread();
        lock.lock();
        boolean satisfied = false;
        try {
            if (!guard.isSatisfied()) {
                awaitUninterruptibly(guard, signalBeforeWaiting);
            }
            satisfied = true;
        } finally {
            if (!satisfied) {
                leave();
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean enterWhen(com.google.common.util.concurrent.Monitor.Guard r11, long r12, java.util.concurrent.TimeUnit r14) throws java.lang.InterruptedException {
        /*
        r10 = this;
        r6 = toSafeNanos(r12, r14);
        r8 = r11.monitor;
        if (r8 == r10) goto L_0x000e;
    L_0x0008:
        r8 = new java.lang.IllegalMonitorStateException;
        r8.<init>();
        throw r8;
    L_0x000e:
        r0 = r10.lock;
        r1 = r0.isHeldByCurrentThread();
        r4 = 0;
        r8 = r10.fair;
        if (r8 != 0) goto L_0x004f;
    L_0x001a:
        r8 = java.lang.Thread.interrupted();
        if (r8 == 0) goto L_0x0026;
    L_0x0020:
        r8 = new java.lang.InterruptedException;
        r8.<init>();
        throw r8;
    L_0x0026:
        r8 = r0.tryLock();
        if (r8 == 0) goto L_0x004f;
    L_0x002c:
        r2 = 0;
        r3 = 1;
        r8 = r11.isSatisfied();	 Catch:{ all -> 0x0067 }
        if (r8 != 0) goto L_0x0040;
    L_0x0034:
        r8 = 0;
        r8 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1));
        if (r8 != 0) goto L_0x005b;
    L_0x003a:
        r8 = r10.awaitNanos(r11, r6, r1);	 Catch:{ all -> 0x0067 }
        if (r8 == 0) goto L_0x0060;
    L_0x0040:
        r2 = 1;
    L_0x0041:
        r3 = 0;
        if (r2 != 0) goto L_0x004e;
    L_0x0044:
        if (r3 == 0) goto L_0x004b;
    L_0x0046:
        if (r1 != 0) goto L_0x004b;
    L_0x0048:
        r10.signalNextWaiter();	 Catch:{ all -> 0x0062 }
    L_0x004b:
        r0.unlock();
    L_0x004e:
        return r2;
    L_0x004f:
        r4 = initNanoTime(r6);
        r8 = r0.tryLock(r12, r14);
        if (r8 != 0) goto L_0x002c;
    L_0x0059:
        r2 = 0;
        goto L_0x004e;
    L_0x005b:
        r6 = remainingNanos(r4, r6);	 Catch:{ all -> 0x0067 }
        goto L_0x003a;
    L_0x0060:
        r2 = 0;
        goto L_0x0041;
    L_0x0062:
        r8 = move-exception;
        r0.unlock();
        throw r8;
    L_0x0067:
        r8 = move-exception;
        if (r2 != 0) goto L_0x0074;
    L_0x006a:
        if (r3 == 0) goto L_0x0071;
    L_0x006c:
        if (r1 != 0) goto L_0x0071;
    L_0x006e:
        r10.signalNextWaiter();	 Catch:{ all -> 0x0075 }
    L_0x0071:
        r0.unlock();
    L_0x0074:
        throw r8;
    L_0x0075:
        r8 = move-exception;
        r0.unlock();
        throw r8;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.Monitor.enterWhen(com.google.common.util.concurrent.Monitor$Guard, long, java.util.concurrent.TimeUnit):boolean");
    }

    public boolean enterWhenUninterruptibly(Guard guard, long time, TimeUnit unit) {
        long timeoutNanos = toSafeNanos(time, unit);
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        ReentrantLock lock = this.lock;
        long startTime = 0;
        boolean signalBeforeWaiting = lock.isHeldByCurrentThread();
        boolean interrupted = Thread.interrupted();
        long remainingNanos;
        try {
            boolean satisfied;
            if (this.fair || !lock.tryLock()) {
                startTime = initNanoTime(timeoutNanos);
                remainingNanos = timeoutNanos;
                while (true) {
                    break;
                }
                if (!lock.tryLock(remainingNanos, TimeUnit.NANOSECONDS)) {
                    satisfied = false;
                    if (interrupted) {
                        Thread.currentThread().interrupt();
                    }
                    return satisfied;
                }
            }
            while (!guard.isSatisfied()) {
                if (startTime == 0) {
                    startTime = initNanoTime(timeoutNanos);
                    remainingNanos = timeoutNanos;
                } else {
                    remainingNanos = remainingNanos(startTime, timeoutNanos);
                }
                satisfied = awaitNanos(guard, remainingNanos, signalBeforeWaiting);
            }
            satisfied = true;
            if (!satisfied) {
                lock.unlock();
            }
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
            return satisfied;
        } catch (InterruptedException e) {
            interrupted = true;
            signalBeforeWaiting = false;
        } catch (InterruptedException e2) {
            interrupted = true;
            remainingNanos = remainingNanos(startTime, timeoutNanos);
        } catch (Throwable th) {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public boolean enterIf(Guard guard) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        ReentrantLock lock = this.lock;
        lock.lock();
        boolean satisfied = false;
        try {
            satisfied = guard.isSatisfied();
            return satisfied;
        } finally {
            if (!satisfied) {
                lock.unlock();
            }
        }
    }

    public boolean enterIfInterruptibly(Guard guard) throws InterruptedException {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        ReentrantLock lock = this.lock;
        lock.lockInterruptibly();
        boolean satisfied = false;
        try {
            satisfied = guard.isSatisfied();
            return satisfied;
        } finally {
            if (!satisfied) {
                lock.unlock();
            }
        }
    }

    public void waitFor(Guard guard) throws InterruptedException {
        if (((guard.monitor == this ? 1 : 0) & this.lock.isHeldByCurrentThread()) == 0) {
            throw new IllegalMonitorStateException();
        } else if (!guard.isSatisfied()) {
            await(guard, true);
        }
    }

    public void waitForUninterruptibly(Guard guard) {
        if (((guard.monitor == this ? 1 : 0) & this.lock.isHeldByCurrentThread()) == 0) {
            throw new IllegalMonitorStateException();
        } else if (!guard.isSatisfied()) {
            awaitUninterruptibly(guard, true);
        }
    }

    public boolean waitFor(Guard guard, long time, TimeUnit unit) throws InterruptedException {
        long timeoutNanos = toSafeNanos(time, unit);
        if (((guard.monitor == this ? 1 : 0) & this.lock.isHeldByCurrentThread()) == 0) {
            throw new IllegalMonitorStateException();
        } else if (guard.isSatisfied()) {
            return true;
        } else {
            if (!Thread.interrupted()) {
                return awaitNanos(guard, timeoutNanos, true);
            }
            throw new InterruptedException();
        }
    }

    public void leave() {
        ReentrantLock lock = this.lock;
        try {
            if (lock.getHoldCount() == 1) {
                signalNextWaiter();
            }
            lock.unlock();
        } catch (Throwable th) {
            lock.unlock();
        }
    }

    public boolean isFair() {
        return this.fair;
    }

    public boolean isOccupied() {
        return this.lock.isLocked();
    }

    public boolean isOccupiedByCurrentThread() {
        return this.lock.isHeldByCurrentThread();
    }

    public int getOccupiedDepth() {
        return this.lock.getHoldCount();
    }

    public int getQueueLength() {
        return this.lock.getQueueLength();
    }

    public boolean hasQueuedThreads() {
        return this.lock.hasQueuedThreads();
    }

    public boolean hasQueuedThread(Thread thread) {
        return this.lock.hasQueuedThread(thread);
    }

    public boolean hasWaiters(Guard guard) {
        return getWaitQueueLength(guard) > 0;
    }

    public int getWaitQueueLength(Guard guard) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        this.lock.lock();
        try {
            int i = guard.waiterCount;
            return i;
        } finally {
            this.lock.unlock();
        }
    }

    private static long toSafeNanos(long time, TimeUnit unit) {
        long timeoutNanos = unit.toNanos(time);
        if (timeoutNanos <= 0) {
            return 0;
        }
        return timeoutNanos > 6917529027641081853L ? 6917529027641081853L : timeoutNanos;
    }

    private static long initNanoTime(long timeoutNanos) {
        if (timeoutNanos <= 0) {
            return 0;
        }
        long startTime = System.nanoTime();
        return startTime == 0 ? 1 : startTime;
    }

    private static long remainingNanos(long startTime, long timeoutNanos) {
        return timeoutNanos <= 0 ? 0 : timeoutNanos - (System.nanoTime() - startTime);
    }

    @GuardedBy("lock")
    private void signalNextWaiter() {
        for (Guard guard = this.activeGuards; guard != null; guard = guard.next) {
            if (isSatisfied(guard)) {
                guard.condition.signal();
                return;
            }
        }
    }

    @GuardedBy("lock")
    private boolean isSatisfied(Guard guard) {
        try {
            return guard.isSatisfied();
        } catch (Throwable throwable) {
            signalAllWaiters();
            RuntimeException propagate = Throwables.propagate(throwable);
        }
    }

    @GuardedBy("lock")
    private void signalAllWaiters() {
        for (Guard guard = this.activeGuards; guard != null; guard = guard.next) {
            guard.condition.signalAll();
        }
    }

    @GuardedBy("lock")
    private void beginWaitingFor(Guard guard) {
        int waiters = guard.waiterCount;
        guard.waiterCount = waiters + 1;
        if (waiters == 0) {
            guard.next = this.activeGuards;
            this.activeGuards = guard;
        }
    }

    @GuardedBy("lock")
    private void endWaitingFor(Guard guard) {
        int waiters = guard.waiterCount - 1;
        guard.waiterCount = waiters;
        if (waiters == 0) {
            Guard p = this.activeGuards;
            Guard pred = null;
            while (p != guard) {
                pred = p;
                p = p.next;
            }
            if (pred == null) {
                this.activeGuards = p.next;
            } else {
                pred.next = p.next;
            }
            p.next = null;
        }
    }

    @GuardedBy("lock")
    private void await(Guard guard, boolean signalBeforeWaiting) throws InterruptedException {
        if (signalBeforeWaiting) {
            signalNextWaiter();
        }
        beginWaitingFor(guard);
        while (true) {
            try {
                guard.condition.await();
                if (guard.isSatisfied()) {
                    break;
                }
            } finally {
                endWaitingFor(guard);
            }
        }
    }

    @GuardedBy("lock")
    private void awaitUninterruptibly(Guard guard, boolean signalBeforeWaiting) {
        if (signalBeforeWaiting) {
            signalNextWaiter();
        }
        beginWaitingFor(guard);
        while (true) {
            try {
                guard.condition.awaitUninterruptibly();
                if (guard.isSatisfied()) {
                    break;
                }
            } finally {
                endWaitingFor(guard);
            }
        }
    }

    @GuardedBy("lock")
    private boolean awaitNanos(Guard guard, long nanos, boolean signalBeforeWaiting) throws InterruptedException {
        boolean z;
        boolean firstTime = true;
        while (nanos > 0) {
            if (firstTime) {
                if (signalBeforeWaiting) {
                    try {
                        signalNextWaiter();
                    } catch (Throwable th) {
                        if (!firstTime) {
                            endWaitingFor(guard);
                        }
                    }
                }
                beginWaitingFor(guard);
                firstTime = false;
            }
            nanos = guard.condition.awaitNanos(nanos);
            if (guard.isSatisfied()) {
                z = true;
                if (!firstTime) {
                    endWaitingFor(guard);
                }
                return z;
            }
        }
        z = false;
        if (!firstTime) {
            endWaitingFor(guard);
        }
        return z;
    }
}

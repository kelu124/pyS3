package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.logging.Level;
import java.util.logging.Logger;

@GwtCompatible(emulated = true)
abstract class InterruptibleTask implements Runnable {
    private static final AtomicHelper ATOMIC_HELPER;
    private static final Logger log = Logger.getLogger(InterruptibleTask.class.getName());
    private volatile boolean doneInterrupting;
    private volatile Thread runner;

    private static abstract class AtomicHelper {
        abstract boolean compareAndSetRunner(InterruptibleTask interruptibleTask, Thread thread, Thread thread2);

        private AtomicHelper() {
        }
    }

    private static final class SafeAtomicHelper extends AtomicHelper {
        final AtomicReferenceFieldUpdater<InterruptibleTask, Thread> runnerUpdater;

        SafeAtomicHelper(AtomicReferenceFieldUpdater runnerUpdater) {
            super();
            this.runnerUpdater = runnerUpdater;
        }

        boolean compareAndSetRunner(InterruptibleTask task, Thread expect, Thread update) {
            return this.runnerUpdater.compareAndSet(task, expect, update);
        }
    }

    private static final class SynchronizedAtomicHelper extends AtomicHelper {
        private SynchronizedAtomicHelper() {
            super();
        }

        boolean compareAndSetRunner(InterruptibleTask task, Thread expect, Thread update) {
            synchronized (task) {
                if (task.runner == expect) {
                    task.runner = update;
                }
            }
            return true;
        }
    }

    abstract void runInterruptibly();

    abstract boolean wasInterrupted();

    InterruptibleTask() {
    }

    static {
        AtomicHelper helper;
        try {
            helper = new SafeAtomicHelper(AtomicReferenceFieldUpdater.newUpdater(InterruptibleTask.class, Thread.class, "runner"));
        } catch (Throwable reflectionFailure) {
            log.log(Level.SEVERE, "SafeAtomicHelper is broken!", reflectionFailure);
            helper = new SynchronizedAtomicHelper();
        }
        ATOMIC_HELPER = helper;
    }

    public final void run() {
        if (ATOMIC_HELPER.compareAndSetRunner(this, null, Thread.currentThread())) {
            try {
                runInterruptibly();
                if (wasInterrupted()) {
                    while (!this.doneInterrupting) {
                        Thread.yield();
                    }
                }
            } catch (Throwable th) {
                if (wasInterrupted()) {
                    while (!this.doneInterrupting) {
                        Thread.yield();
                    }
                }
            }
        }
    }

    final void interruptTask() {
        Thread currentRunner = this.runner;
        if (currentRunner != null) {
            currentRunner.interrupt();
        }
        this.doneInterrupting = true;
    }
}

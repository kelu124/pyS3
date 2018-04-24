package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.Sets;
import java.util.Set;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.logging.Level;
import java.util.logging.Logger;

@GwtCompatible(emulated = true)
abstract class AggregateFutureState {
    private static final AtomicHelper ATOMIC_HELPER;
    private static final Logger log = Logger.getLogger(AggregateFutureState.class.getName());
    private volatile int remaining;
    private volatile Set<Throwable> seenExceptions = null;

    private static abstract class AtomicHelper {
        abstract void compareAndSetSeenExceptions(AggregateFutureState aggregateFutureState, Set<Throwable> set, Set<Throwable> set2);

        abstract int decrementAndGetRemainingCount(AggregateFutureState aggregateFutureState);

        private AtomicHelper() {
        }
    }

    private static final class SafeAtomicHelper extends AtomicHelper {
        final AtomicIntegerFieldUpdater<AggregateFutureState> remainingCountUpdater;
        final AtomicReferenceFieldUpdater<AggregateFutureState, Set<Throwable>> seenExceptionsUpdater;

        SafeAtomicHelper(AtomicReferenceFieldUpdater seenExceptionsUpdater, AtomicIntegerFieldUpdater remainingCountUpdater) {
            super();
            this.seenExceptionsUpdater = seenExceptionsUpdater;
            this.remainingCountUpdater = remainingCountUpdater;
        }

        void compareAndSetSeenExceptions(AggregateFutureState state, Set<Throwable> expect, Set<Throwable> update) {
            this.seenExceptionsUpdater.compareAndSet(state, expect, update);
        }

        int decrementAndGetRemainingCount(AggregateFutureState state) {
            return this.remainingCountUpdater.decrementAndGet(state);
        }
    }

    private static final class SynchronizedAtomicHelper extends AtomicHelper {
        private SynchronizedAtomicHelper() {
            super();
        }

        void compareAndSetSeenExceptions(AggregateFutureState state, Set<Throwable> expect, Set<Throwable> update) {
            synchronized (state) {
                if (state.seenExceptions == expect) {
                    state.seenExceptions = update;
                }
            }
        }

        int decrementAndGetRemainingCount(AggregateFutureState state) {
            int access$300;
            synchronized (state) {
                state.remaining = state.remaining - 1;
                access$300 = state.remaining;
            }
            return access$300;
        }
    }

    abstract void addInitialException(Set<Throwable> set);

    static {
        AtomicHelper helper;
        try {
            helper = new SafeAtomicHelper(AtomicReferenceFieldUpdater.newUpdater(AggregateFutureState.class, Set.class, "seenExceptions"), AtomicIntegerFieldUpdater.newUpdater(AggregateFutureState.class, "remaining"));
        } catch (Throwable reflectionFailure) {
            log.log(Level.SEVERE, "SafeAtomicHelper is broken!", reflectionFailure);
            helper = new SynchronizedAtomicHelper();
        }
        ATOMIC_HELPER = helper;
    }

    AggregateFutureState(int remainingFutures) {
        this.remaining = remainingFutures;
    }

    final Set<Throwable> getOrInitSeenExceptions() {
        Set<Throwable> seenExceptionsLocal = this.seenExceptions;
        if (seenExceptionsLocal != null) {
            return seenExceptionsLocal;
        }
        seenExceptionsLocal = Sets.newConcurrentHashSet();
        addInitialException(seenExceptionsLocal);
        ATOMIC_HELPER.compareAndSetSeenExceptions(this, null, seenExceptionsLocal);
        return this.seenExceptions;
    }

    final int decrementRemainingAndGet() {
        return ATOMIC_HELPER.decrementAndGetRemainingCount(this);
    }
}

package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.Monitor.Guard;
import com.google.common.util.concurrent.Service.Listener;
import com.google.common.util.concurrent.Service.State;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.annotation.concurrent.GuardedBy;

@GwtIncompatible
@Beta
public abstract class AbstractService implements Service {
    private static final Callback<Listener> RUNNING_CALLBACK = new Callback<Listener>("running()") {
        void call(Listener listener) {
            listener.running();
        }
    };
    private static final Callback<Listener> STARTING_CALLBACK = new Callback<Listener>("starting()") {
        void call(Listener listener) {
            listener.starting();
        }
    };
    private static final Callback<Listener> STOPPING_FROM_RUNNING_CALLBACK = stoppingCallback(State.RUNNING);
    private static final Callback<Listener> STOPPING_FROM_STARTING_CALLBACK = stoppingCallback(State.STARTING);
    private static final Callback<Listener> TERMINATED_FROM_NEW_CALLBACK = terminatedCallback(State.NEW);
    private static final Callback<Listener> TERMINATED_FROM_RUNNING_CALLBACK = terminatedCallback(State.RUNNING);
    private static final Callback<Listener> TERMINATED_FROM_STOPPING_CALLBACK = terminatedCallback(State.STOPPING);
    private final Guard hasReachedRunning = new HasReachedRunningGuard();
    private final Guard isStartable = new IsStartableGuard();
    private final Guard isStoppable = new IsStoppableGuard();
    private final Guard isStopped = new IsStoppedGuard();
    @GuardedBy("monitor")
    private final List<ListenerCallQueue<Listener>> listeners = Collections.synchronizedList(new ArrayList());
    private final Monitor monitor = new Monitor();
    @GuardedBy("monitor")
    private volatile StateSnapshot snapshot = new StateSnapshot(State.NEW);

    private final class HasReachedRunningGuard extends Guard {
        HasReachedRunningGuard() {
            super(AbstractService.this.monitor);
        }

        public boolean isSatisfied() {
            return AbstractService.this.state().compareTo(State.RUNNING) >= 0;
        }
    }

    private final class IsStartableGuard extends Guard {
        IsStartableGuard() {
            super(AbstractService.this.monitor);
        }

        public boolean isSatisfied() {
            return AbstractService.this.state() == State.NEW;
        }
    }

    private final class IsStoppableGuard extends Guard {
        IsStoppableGuard() {
            super(AbstractService.this.monitor);
        }

        public boolean isSatisfied() {
            return AbstractService.this.state().compareTo(State.RUNNING) <= 0;
        }
    }

    private final class IsStoppedGuard extends Guard {
        IsStoppedGuard() {
            super(AbstractService.this.monitor);
        }

        public boolean isSatisfied() {
            return AbstractService.this.state().isTerminal();
        }
    }

    protected abstract void doStart();

    protected abstract void doStop();

    private static Callback<Listener> terminatedCallback(final State from) {
        return new Callback<Listener>("terminated({from = " + from + "})") {
            void call(Listener listener) {
                listener.terminated(from);
            }
        };
    }

    private static Callback<Listener> stoppingCallback(final State from) {
        return new Callback<Listener>("stopping({from = " + from + "})") {
            void call(Listener listener) {
                listener.stopping(from);
            }
        };
    }

    protected AbstractService() {
    }

    @CanIgnoreReturnValue
    public final Service startAsync() {
        if (this.monitor.enterIf(this.isStartable)) {
            try {
                this.snapshot = new StateSnapshot(State.STARTING);
                starting();
                doStart();
            } catch (Throwable startupFailure) {
                notifyFailed(startupFailure);
            } finally {
                this.monitor.leave();
                executeListeners();
            }
            return this;
        }
        throw new IllegalStateException("Service " + this + " has already been started");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @com.google.errorprone.annotations.CanIgnoreReturnValue
    public final com.google.common.util.concurrent.Service stopAsync() {
        /*
        r6 = this;
        r2 = r6.monitor;
        r3 = r6.isStoppable;
        r2 = r2.enterIf(r3);
        if (r2 == 0) goto L_0x003e;
    L_0x000a:
        r0 = r6.state();	 Catch:{ Throwable -> 0x0032 }
        r2 = com.google.common.util.concurrent.AbstractService.C08166.$SwitchMap$com$google$common$util$concurrent$Service$State;	 Catch:{ Throwable -> 0x0032 }
        r3 = r0.ordinal();	 Catch:{ Throwable -> 0x0032 }
        r2 = r2[r3];	 Catch:{ Throwable -> 0x0032 }
        switch(r2) {
            case 1: goto L_0x003f;
            case 2: goto L_0x0056;
            case 3: goto L_0x0071;
            case 4: goto L_0x0083;
            case 5: goto L_0x0083;
            case 6: goto L_0x0083;
            default: goto L_0x0019;
        };	 Catch:{ Throwable -> 0x0032 }
    L_0x0019:
        r2 = new java.lang.AssertionError;	 Catch:{ Throwable -> 0x0032 }
        r3 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0032 }
        r3.<init>();	 Catch:{ Throwable -> 0x0032 }
        r4 = "Unexpected state: ";
        r3 = r3.append(r4);	 Catch:{ Throwable -> 0x0032 }
        r3 = r3.append(r0);	 Catch:{ Throwable -> 0x0032 }
        r3 = r3.toString();	 Catch:{ Throwable -> 0x0032 }
        r2.<init>(r3);	 Catch:{ Throwable -> 0x0032 }
        throw r2;	 Catch:{ Throwable -> 0x0032 }
    L_0x0032:
        r1 = move-exception;
        r6.notifyFailed(r1);	 Catch:{ all -> 0x0067 }
        r2 = r6.monitor;
        r2.leave();
        r6.executeListeners();
    L_0x003e:
        return r6;
    L_0x003f:
        r2 = new com.google.common.util.concurrent.AbstractService$StateSnapshot;	 Catch:{ Throwable -> 0x0032 }
        r3 = com.google.common.util.concurrent.Service.State.TERMINATED;	 Catch:{ Throwable -> 0x0032 }
        r2.<init>(r3);	 Catch:{ Throwable -> 0x0032 }
        r6.snapshot = r2;	 Catch:{ Throwable -> 0x0032 }
        r2 = com.google.common.util.concurrent.Service.State.NEW;	 Catch:{ Throwable -> 0x0032 }
        r6.terminated(r2);	 Catch:{ Throwable -> 0x0032 }
    L_0x004d:
        r2 = r6.monitor;
        r2.leave();
        r6.executeListeners();
        goto L_0x003e;
    L_0x0056:
        r2 = new com.google.common.util.concurrent.AbstractService$StateSnapshot;	 Catch:{ Throwable -> 0x0032 }
        r3 = com.google.common.util.concurrent.Service.State.STARTING;	 Catch:{ Throwable -> 0x0032 }
        r4 = 1;
        r5 = 0;
        r2.<init>(r3, r4, r5);	 Catch:{ Throwable -> 0x0032 }
        r6.snapshot = r2;	 Catch:{ Throwable -> 0x0032 }
        r2 = com.google.common.util.concurrent.Service.State.STARTING;	 Catch:{ Throwable -> 0x0032 }
        r6.stopping(r2);	 Catch:{ Throwable -> 0x0032 }
        goto L_0x004d;
    L_0x0067:
        r2 = move-exception;
        r3 = r6.monitor;
        r3.leave();
        r6.executeListeners();
        throw r2;
    L_0x0071:
        r2 = new com.google.common.util.concurrent.AbstractService$StateSnapshot;	 Catch:{ Throwable -> 0x0032 }
        r3 = com.google.common.util.concurrent.Service.State.STOPPING;	 Catch:{ Throwable -> 0x0032 }
        r2.<init>(r3);	 Catch:{ Throwable -> 0x0032 }
        r6.snapshot = r2;	 Catch:{ Throwable -> 0x0032 }
        r2 = com.google.common.util.concurrent.Service.State.RUNNING;	 Catch:{ Throwable -> 0x0032 }
        r6.stopping(r2);	 Catch:{ Throwable -> 0x0032 }
        r6.doStop();	 Catch:{ Throwable -> 0x0032 }
        goto L_0x004d;
    L_0x0083:
        r2 = new java.lang.AssertionError;	 Catch:{ Throwable -> 0x0032 }
        r3 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0032 }
        r3.<init>();	 Catch:{ Throwable -> 0x0032 }
        r4 = "isStoppable is incorrectly implemented, saw: ";
        r3 = r3.append(r4);	 Catch:{ Throwable -> 0x0032 }
        r3 = r3.append(r0);	 Catch:{ Throwable -> 0x0032 }
        r3 = r3.toString();	 Catch:{ Throwable -> 0x0032 }
        r2.<init>(r3);	 Catch:{ Throwable -> 0x0032 }
        throw r2;	 Catch:{ Throwable -> 0x0032 }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.AbstractService.stopAsync():com.google.common.util.concurrent.Service");
    }

    public final void awaitRunning() {
        this.monitor.enterWhenUninterruptibly(this.hasReachedRunning);
        try {
            checkCurrentState(State.RUNNING);
        } finally {
            this.monitor.leave();
        }
    }

    public final void awaitRunning(long timeout, TimeUnit unit) throws TimeoutException {
        if (this.monitor.enterWhenUninterruptibly(this.hasReachedRunning, timeout, unit)) {
            try {
                checkCurrentState(State.RUNNING);
            } finally {
                this.monitor.leave();
            }
        } else {
            throw new TimeoutException("Timed out waiting for " + this + " to reach the RUNNING state.");
        }
    }

    public final void awaitTerminated() {
        this.monitor.enterWhenUninterruptibly(this.isStopped);
        try {
            checkCurrentState(State.TERMINATED);
        } finally {
            this.monitor.leave();
        }
    }

    public final void awaitTerminated(long timeout, TimeUnit unit) throws TimeoutException {
        if (this.monitor.enterWhenUninterruptibly(this.isStopped, timeout, unit)) {
            try {
                checkCurrentState(State.TERMINATED);
            } finally {
                this.monitor.leave();
            }
        } else {
            throw new TimeoutException("Timed out waiting for " + this + " to reach a terminal state. " + "Current state: " + state());
        }
    }

    @GuardedBy("monitor")
    private void checkCurrentState(State expected) {
        State actual = state();
        if (actual == expected) {
            return;
        }
        if (actual == State.FAILED) {
            throw new IllegalStateException("Expected the service " + this + " to be " + expected + ", but the service has FAILED", failureCause());
        }
        throw new IllegalStateException("Expected the service " + this + " to be " + expected + ", but was " + actual);
    }

    protected final void notifyStarted() {
        this.monitor.enter();
        try {
            if (this.snapshot.state != State.STARTING) {
                IllegalStateException failure = new IllegalStateException("Cannot notifyStarted() when the service is " + this.snapshot.state);
                notifyFailed(failure);
                throw failure;
            }
            if (this.snapshot.shutdownWhenStartupFinishes) {
                this.snapshot = new StateSnapshot(State.STOPPING);
                doStop();
            } else {
                this.snapshot = new StateSnapshot(State.RUNNING);
                running();
            }
            this.monitor.leave();
            executeListeners();
        } catch (Throwable th) {
            this.monitor.leave();
            executeListeners();
        }
    }

    protected final void notifyStopped() {
        this.monitor.enter();
        try {
            State previous = this.snapshot.state;
            if (previous == State.STOPPING || previous == State.RUNNING) {
                this.snapshot = new StateSnapshot(State.TERMINATED);
                terminated(previous);
                return;
            }
            IllegalStateException failure = new IllegalStateException("Cannot notifyStopped() when the service is " + previous);
            notifyFailed(failure);
            throw failure;
        } finally {
            this.monitor.leave();
            executeListeners();
        }
    }

    protected final void notifyFailed(Throwable cause) {
        Preconditions.checkNotNull(cause);
        this.monitor.enter();
        try {
            State previous = state();
            switch (previous) {
                case NEW:
                case TERMINATED:
                    throw new IllegalStateException("Failed while in state:" + previous, cause);
                case STARTING:
                case RUNNING:
                case STOPPING:
                    this.snapshot = new StateSnapshot(State.FAILED, false, cause);
                    failed(previous, cause);
                    break;
                case FAILED:
                    break;
                default:
                    throw new AssertionError("Unexpected state: " + previous);
            }
            this.monitor.leave();
            executeListeners();
        } catch (Throwable th) {
            this.monitor.leave();
            executeListeners();
        }
    }

    public final boolean isRunning() {
        return state() == State.RUNNING;
    }

    public final State state() {
        return this.snapshot.externalState();
    }

    public final Throwable failureCause() {
        return this.snapshot.failureCause();
    }

    public final void addListener(Listener listener, Executor executor) {
        Preconditions.checkNotNull(listener, "listener");
        Preconditions.checkNotNull(executor, "executor");
        this.monitor.enter();
        try {
            if (!state().isTerminal()) {
                this.listeners.add(new ListenerCallQueue(listener, executor));
            }
            this.monitor.leave();
        } catch (Throwable th) {
            this.monitor.leave();
        }
    }

    public String toString() {
        return getClass().getSimpleName() + " [" + state() + "]";
    }

    private void executeListeners() {
        if (!this.monitor.isOccupiedByCurrentThread()) {
            for (int i = 0; i < this.listeners.size(); i++) {
                ((ListenerCallQueue) this.listeners.get(i)).execute();
            }
        }
    }

    @GuardedBy("monitor")
    private void starting() {
        STARTING_CALLBACK.enqueueOn(this.listeners);
    }

    @GuardedBy("monitor")
    private void running() {
        RUNNING_CALLBACK.enqueueOn(this.listeners);
    }

    @GuardedBy("monitor")
    private void stopping(State from) {
        if (from == State.STARTING) {
            STOPPING_FROM_STARTING_CALLBACK.enqueueOn(this.listeners);
        } else if (from == State.RUNNING) {
            STOPPING_FROM_RUNNING_CALLBACK.enqueueOn(this.listeners);
        } else {
            throw new AssertionError();
        }
    }

    @GuardedBy("monitor")
    private void terminated(State from) {
        switch (from) {
            case NEW:
                TERMINATED_FROM_NEW_CALLBACK.enqueueOn(this.listeners);
                return;
            case RUNNING:
                TERMINATED_FROM_RUNNING_CALLBACK.enqueueOn(this.listeners);
                return;
            case STOPPING:
                TERMINATED_FROM_STOPPING_CALLBACK.enqueueOn(this.listeners);
                return;
            default:
                throw new AssertionError();
        }
    }

    @GuardedBy("monitor")
    private void failed(final State from, final Throwable cause) {
        new Callback<Listener>("failed({from = " + from + ", cause = " + cause + "})") {
            void call(Listener listener) {
                listener.failed(from, cause);
            }
        }.enqueueOn(this.listeners);
    }
}

package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import javax.annotation.Nullable;

@GwtCompatible
final class CombinedFuture<V> extends AggregateFuture<Object, V> {

    private abstract class CombinedFutureInterruptibleTask extends InterruptibleTask {
        private final Executor listenerExecutor;
        volatile boolean thrownByExecute = true;

        abstract void setValue() throws Exception;

        public CombinedFutureInterruptibleTask(Executor listenerExecutor) {
            this.listenerExecutor = (Executor) Preconditions.checkNotNull(listenerExecutor);
        }

        final void runInterruptibly() {
            this.thrownByExecute = false;
            if (!CombinedFuture.this.isDone()) {
                try {
                    setValue();
                } catch (ExecutionException e) {
                    CombinedFuture.this.setException(e.getCause());
                } catch (CancellationException e2) {
                    CombinedFuture.this.cancel(false);
                } catch (Throwable e3) {
                    CombinedFuture.this.setException(e3);
                }
            }
        }

        final boolean wasInterrupted() {
            return CombinedFuture.this.wasInterrupted();
        }

        final void execute() {
            try {
                this.listenerExecutor.execute(this);
            } catch (RejectedExecutionException e) {
                if (this.thrownByExecute) {
                    CombinedFuture.this.setException(e);
                }
            }
        }
    }

    private final class AsyncCallableInterruptibleTask extends CombinedFutureInterruptibleTask {
        private final AsyncCallable<V> callable;

        public AsyncCallableInterruptibleTask(AsyncCallable<V> callable, Executor listenerExecutor) {
            super(listenerExecutor);
            this.callable = (AsyncCallable) Preconditions.checkNotNull(callable);
        }

        void setValue() throws Exception {
            CombinedFuture.this.setFuture(this.callable.call());
        }
    }

    private final class CallableInterruptibleTask extends CombinedFutureInterruptibleTask {
        private final Callable<V> callable;

        public CallableInterruptibleTask(Callable<V> callable, Executor listenerExecutor) {
            super(listenerExecutor);
            this.callable = (Callable) Preconditions.checkNotNull(callable);
        }

        void setValue() throws Exception {
            CombinedFuture.this.set(this.callable.call());
        }
    }

    private final class CombinedFutureRunningState extends RunningState {
        private CombinedFutureInterruptibleTask task;

        CombinedFutureRunningState(ImmutableCollection<? extends ListenableFuture<? extends Object>> futures, boolean allMustSucceed, CombinedFutureInterruptibleTask task) {
            super(futures, allMustSucceed, false);
            this.task = task;
        }

        void collectOneValue(boolean allMustSucceed, int index, @Nullable Object returnValue) {
        }

        void handleAllCompleted() {
            CombinedFutureInterruptibleTask localTask = this.task;
            if (localTask != null) {
                localTask.execute();
            } else {
                Preconditions.checkState(CombinedFuture.this.isDone());
            }
        }

        void releaseResourcesAfterFailure() {
            super.releaseResourcesAfterFailure();
            this.task = null;
        }

        void interruptTask() {
            CombinedFutureInterruptibleTask localTask = this.task;
            if (localTask != null) {
                localTask.interruptTask();
            }
        }
    }

    CombinedFuture(ImmutableCollection<? extends ListenableFuture<?>> futures, boolean allMustSucceed, Executor listenerExecutor, AsyncCallable<V> callable) {
        init(new CombinedFutureRunningState(futures, allMustSucceed, new AsyncCallableInterruptibleTask(callable, listenerExecutor)));
    }

    CombinedFuture(ImmutableCollection<? extends ListenableFuture<?>> futures, boolean allMustSucceed, Executor listenerExecutor, Callable<V> callable) {
        init(new CombinedFutureRunningState(futures, allMustSucceed, new CallableInterruptibleTask(callable, listenerExecutor)));
    }
}

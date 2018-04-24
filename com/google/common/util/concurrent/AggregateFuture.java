package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;

@GwtCompatible
abstract class AggregateFuture<InputT, OutputT> extends TrustedFuture<OutputT> {
    private static final Logger logger = Logger.getLogger(AggregateFuture.class.getName());
    private RunningState runningState;

    abstract class RunningState extends AggregateFutureState implements Runnable {
        private final boolean allMustSucceed;
        private final boolean collectsValues;
        private ImmutableCollection<? extends ListenableFuture<? extends InputT>> futures;

        abstract void collectOneValue(boolean z, int i, @Nullable InputT inputT);

        abstract void handleAllCompleted();

        RunningState(ImmutableCollection<? extends ListenableFuture<? extends InputT>> futures, boolean allMustSucceed, boolean collectsValues) {
            super(futures.size());
            this.futures = (ImmutableCollection) Preconditions.checkNotNull(futures);
            this.allMustSucceed = allMustSucceed;
            this.collectsValues = collectsValues;
        }

        public final void run() {
            decrementCountAndMaybeComplete();
        }

        private void init() {
            if (this.futures.isEmpty()) {
                handleAllCompleted();
            } else if (this.allMustSucceed) {
                int i = 0;
                i$ = this.futures.iterator();
                while (i$.hasNext()) {
                    final ListenableFuture<? extends InputT> listenable = (ListenableFuture) i$.next();
                    int i2 = i + 1;
                    final int index = i;
                    listenable.addListener(new Runnable() {
                        public void run() {
                            try {
                                RunningState.this.handleOneInputDone(index, listenable);
                            } finally {
                                RunningState.this.decrementCountAndMaybeComplete();
                            }
                        }
                    }, MoreExecutors.directExecutor());
                    i = i2;
                }
            } else {
                i$ = this.futures.iterator();
                while (i$.hasNext()) {
                    ((ListenableFuture) i$.next()).addListener(this, MoreExecutors.directExecutor());
                }
            }
        }

        private void handleException(Throwable throwable) {
            Preconditions.checkNotNull(throwable);
            boolean completedWithFailure = false;
            boolean firstTimeSeeingThisException = true;
            if (this.allMustSucceed) {
                completedWithFailure = AggregateFuture.this.setException(throwable);
                if (completedWithFailure) {
                    releaseResourcesAfterFailure();
                } else {
                    firstTimeSeeingThisException = AggregateFuture.addCausalChain(getOrInitSeenExceptions(), throwable);
                }
            }
            if (((((!completedWithFailure ? 1 : 0) & this.allMustSucceed) & firstTimeSeeingThisException) | (throwable instanceof Error)) != 0) {
                AggregateFuture.logger.log(Level.SEVERE, throwable instanceof Error ? "Input Future failed with Error" : "Got more than one input Future failure. Logging failures after the first", throwable);
            }
        }

        final void addInitialException(Set<Throwable> seen) {
            if (!AggregateFuture.this.isCancelled()) {
                AggregateFuture.addCausalChain(seen, AggregateFuture.this.trustedGetException());
            }
        }

        private void handleOneInputDone(int index, Future<? extends InputT> future) {
            boolean z = false;
            if (this.allMustSucceed || !AggregateFuture.this.isDone() || AggregateFuture.this.isCancelled()) {
                z = true;
            }
            Preconditions.checkState(z, "Future was done before all dependencies completed");
            try {
                Preconditions.checkState(future.isDone(), "Tried to set value from future which is not done");
                if (this.allMustSucceed) {
                    if (future.isCancelled()) {
                        AggregateFuture.this.runningState = null;
                        AggregateFuture.this.cancel(false);
                        return;
                    }
                    InputT result = Futures.getDone(future);
                    if (this.collectsValues) {
                        collectOneValue(this.allMustSucceed, index, result);
                    }
                } else if (this.collectsValues && !future.isCancelled()) {
                    collectOneValue(this.allMustSucceed, index, Futures.getDone(future));
                }
            } catch (ExecutionException e) {
                handleException(e.getCause());
            } catch (Throwable t) {
                handleException(t);
            }
        }

        private void decrementCountAndMaybeComplete() {
            int newRemaining = decrementRemainingAndGet();
            Preconditions.checkState(newRemaining >= 0, "Less than 0 remaining futures");
            if (newRemaining == 0) {
                processCompleted();
            }
        }

        private void processCompleted() {
            if (((!this.allMustSucceed ? 1 : 0) & this.collectsValues) != 0) {
                int i = 0;
                Iterator i$ = this.futures.iterator();
                while (i$.hasNext()) {
                    int i2 = i + 1;
                    handleOneInputDone(i, (ListenableFuture) i$.next());
                    i = i2;
                }
            }
            handleAllCompleted();
        }

        void releaseResourcesAfterFailure() {
            this.futures = null;
        }

        void interruptTask() {
        }
    }

    AggregateFuture() {
    }

    protected final void afterDone() {
        super.afterDone();
        RunningState localRunningState = this.runningState;
        if (localRunningState != null) {
            this.runningState = null;
            ImmutableCollection<? extends ListenableFuture<? extends InputT>> futures = localRunningState.futures;
            boolean wasInterrupted = wasInterrupted();
            if (wasInterrupted()) {
                localRunningState.interruptTask();
            }
            if (((futures != null ? 1 : 0) & isCancelled()) != 0) {
                Iterator i$ = futures.iterator();
                while (i$.hasNext()) {
                    ((ListenableFuture) i$.next()).cancel(wasInterrupted);
                }
            }
        }
    }

    final void init(RunningState runningState) {
        this.runningState = runningState;
        runningState.init();
    }

    private static boolean addCausalChain(Set<Throwable> seen, Throwable t) {
        while (t != null) {
            if (!seen.add(t)) {
                return false;
            }
            t = t.getCause();
        }
        return true;
    }
}

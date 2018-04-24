package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@GwtIncompatible
@Beta
public interface Service {

    @Beta
    public static abstract class Listener {
        public void starting() {
        }

        public void running() {
        }

        public void stopping(State from) {
        }

        public void terminated(State from) {
        }

        public void failed(State from, Throwable failure) {
        }
    }

    @Beta
    public enum State {
        NEW {
            boolean isTerminal() {
                return false;
            }
        },
        STARTING {
            boolean isTerminal() {
                return false;
            }
        },
        RUNNING {
            boolean isTerminal() {
                return false;
            }
        },
        STOPPING {
            boolean isTerminal() {
                return false;
            }
        },
        TERMINATED {
            boolean isTerminal() {
                return true;
            }
        },
        FAILED {
            boolean isTerminal() {
                return true;
            }
        };

        abstract boolean isTerminal();
    }

    void addListener(Listener listener, Executor executor);

    void awaitRunning();

    void awaitRunning(long j, TimeUnit timeUnit) throws TimeoutException;

    void awaitTerminated();

    void awaitTerminated(long j, TimeUnit timeUnit) throws TimeoutException;

    Throwable failureCause();

    boolean isRunning();

    @CanIgnoreReturnValue
    Service startAsync();

    State state();

    @CanIgnoreReturnValue
    Service stopAsync();
}

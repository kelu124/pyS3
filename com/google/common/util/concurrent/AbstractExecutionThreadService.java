package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Supplier;
import com.google.common.util.concurrent.Service.Listener;
import com.google.common.util.concurrent.Service.State;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

@GwtIncompatible
@Beta
public abstract class AbstractExecutionThreadService implements Service {
    private static final Logger logger = Logger.getLogger(AbstractExecutionThreadService.class.getName());
    private final Service delegate = new C07941();

    class C07941 extends AbstractService {

        class C07921 implements Supplier<String> {
            C07921() {
            }

            public String get() {
                return AbstractExecutionThreadService.this.serviceName();
            }
        }

        class C07932 implements Runnable {
            C07932() {
            }

            public void run() {
                try {
                    AbstractExecutionThreadService.this.startUp();
                    C07941.this.notifyStarted();
                    if (C07941.this.isRunning()) {
                        AbstractExecutionThreadService.this.run();
                    }
                    AbstractExecutionThreadService.this.shutDown();
                    C07941.this.notifyStopped();
                } catch (Throwable t) {
                    C07941.this.notifyFailed(t);
                }
            }
        }

        C07941() {
        }

        protected final void doStart() {
            MoreExecutors.renamingDecorator(AbstractExecutionThreadService.this.executor(), new C07921()).execute(new C07932());
        }

        protected void doStop() {
            AbstractExecutionThreadService.this.triggerShutdown();
        }

        public String toString() {
            return AbstractExecutionThreadService.this.toString();
        }
    }

    class C07952 implements Executor {
        C07952() {
        }

        public void execute(Runnable command) {
            MoreExecutors.newThread(AbstractExecutionThreadService.this.serviceName(), command).start();
        }
    }

    protected abstract void run() throws Exception;

    protected AbstractExecutionThreadService() {
    }

    protected void startUp() throws Exception {
    }

    protected void shutDown() throws Exception {
    }

    protected void triggerShutdown() {
    }

    protected Executor executor() {
        return new C07952();
    }

    public String toString() {
        return serviceName() + " [" + state() + "]";
    }

    public final boolean isRunning() {
        return this.delegate.isRunning();
    }

    public final State state() {
        return this.delegate.state();
    }

    public final void addListener(Listener listener, Executor executor) {
        this.delegate.addListener(listener, executor);
    }

    public final Throwable failureCause() {
        return this.delegate.failureCause();
    }

    @CanIgnoreReturnValue
    public final Service startAsync() {
        this.delegate.startAsync();
        return this;
    }

    @CanIgnoreReturnValue
    public final Service stopAsync() {
        this.delegate.stopAsync();
        return this;
    }

    public final void awaitRunning() {
        this.delegate.awaitRunning();
    }

    public final void awaitRunning(long timeout, TimeUnit unit) throws TimeoutException {
        this.delegate.awaitRunning(timeout, unit);
    }

    public final void awaitTerminated() {
        this.delegate.awaitTerminated();
    }

    public final void awaitTerminated(long timeout, TimeUnit unit) throws TimeoutException {
        this.delegate.awaitTerminated(timeout, unit);
    }

    protected String serviceName() {
        return getClass().getSimpleName();
    }
}

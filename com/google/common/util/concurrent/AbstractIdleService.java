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

@GwtIncompatible
@Beta
public abstract class AbstractIdleService implements Service {
    private final Service delegate = new DelegateService();
    private final Supplier<String> threadNameSupplier = new ThreadNameSupplier();

    class C08021 implements Executor {
        C08021() {
        }

        public void execute(Runnable command) {
            MoreExecutors.newThread((String) AbstractIdleService.this.threadNameSupplier.get(), command).start();
        }
    }

    private final class DelegateService extends AbstractService {

        class C08031 implements Runnable {
            C08031() {
            }

            public void run() {
                try {
                    AbstractIdleService.this.startUp();
                    DelegateService.this.notifyStarted();
                } catch (Throwable t) {
                    DelegateService.this.notifyFailed(t);
                }
            }
        }

        class C08042 implements Runnable {
            C08042() {
            }

            public void run() {
                try {
                    AbstractIdleService.this.shutDown();
                    DelegateService.this.notifyStopped();
                } catch (Throwable t) {
                    DelegateService.this.notifyFailed(t);
                }
            }
        }

        private DelegateService() {
        }

        protected final void doStart() {
            MoreExecutors.renamingDecorator(AbstractIdleService.this.executor(), AbstractIdleService.this.threadNameSupplier).execute(new C08031());
        }

        protected final void doStop() {
            MoreExecutors.renamingDecorator(AbstractIdleService.this.executor(), AbstractIdleService.this.threadNameSupplier).execute(new C08042());
        }

        public String toString() {
            return AbstractIdleService.this.toString();
        }
    }

    private final class ThreadNameSupplier implements Supplier<String> {
        private ThreadNameSupplier() {
        }

        public String get() {
            return AbstractIdleService.this.serviceName() + " " + AbstractIdleService.this.state();
        }
    }

    protected abstract void shutDown() throws Exception;

    protected abstract void startUp() throws Exception;

    protected AbstractIdleService() {
    }

    protected Executor executor() {
        return new C08021();
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

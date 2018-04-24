package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.util.concurrent.Service.Listener;
import com.google.common.util.concurrent.Service.State;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.concurrent.GuardedBy;

@GwtIncompatible
@Beta
public abstract class AbstractScheduledService implements Service {
    private static final Logger logger = Logger.getLogger(AbstractScheduledService.class.getName());
    private final AbstractService delegate = new ServiceDelegate();

    public static abstract class Scheduler {
        abstract Future<?> schedule(AbstractService abstractService, ScheduledExecutorService scheduledExecutorService, Runnable runnable);

        public static Scheduler newFixedDelaySchedule(long initialDelay, long delay, TimeUnit unit) {
            Preconditions.checkNotNull(unit);
            Preconditions.checkArgument(delay > 0, "delay must be > 0, found %s", delay);
            final long j = initialDelay;
            final long j2 = delay;
            final TimeUnit timeUnit = unit;
            return new Scheduler() {
                public Future<?> schedule(AbstractService service, ScheduledExecutorService executor, Runnable task) {
                    return executor.scheduleWithFixedDelay(task, j, j2, timeUnit);
                }
            };
        }

        public static Scheduler newFixedRateSchedule(long initialDelay, long period, TimeUnit unit) {
            Preconditions.checkNotNull(unit);
            Preconditions.checkArgument(period > 0, "period must be > 0, found %s", period);
            final long j = initialDelay;
            final long j2 = period;
            final TimeUnit timeUnit = unit;
            return new Scheduler() {
                public Future<?> schedule(AbstractService service, ScheduledExecutorService executor, Runnable task) {
                    return executor.scheduleAtFixedRate(task, j, j2, timeUnit);
                }
            };
        }

        private Scheduler() {
        }
    }

    @Beta
    public static abstract class CustomScheduler extends Scheduler {

        private class ReschedulableCallable extends ForwardingFuture<Void> implements Callable<Void> {
            @GuardedBy("lock")
            private Future<Void> currentFuture;
            private final ScheduledExecutorService executor;
            private final ReentrantLock lock = new ReentrantLock();
            private final AbstractService service;
            private final Runnable wrappedRunnable;

            public void reschedule() {
                /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.ssa.SSATransform.placePhi(SSATransform.java:82)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:50)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r8 = this;
                r4 = com.google.common.util.concurrent.AbstractScheduledService.CustomScheduler.this;	 Catch:{ Throwable -> 0x0035 }
                r1 = r4.getNextSchedule();	 Catch:{ Throwable -> 0x0035 }
                r2 = 0;
                r4 = r8.lock;
                r4.lock();
                r4 = r8.currentFuture;	 Catch:{ Throwable -> 0x003c, all -> 0x0044 }
                if (r4 == 0) goto L_0x0018;	 Catch:{ Throwable -> 0x003c, all -> 0x0044 }
            L_0x0010:
                r4 = r8.currentFuture;	 Catch:{ Throwable -> 0x003c, all -> 0x0044 }
                r4 = r4.isCancelled();	 Catch:{ Throwable -> 0x003c, all -> 0x0044 }
                if (r4 != 0) goto L_0x0028;	 Catch:{ Throwable -> 0x003c, all -> 0x0044 }
            L_0x0018:
                r4 = r8.executor;	 Catch:{ Throwable -> 0x003c, all -> 0x0044 }
                r6 = r1.delay;	 Catch:{ Throwable -> 0x003c, all -> 0x0044 }
                r5 = r1.unit;	 Catch:{ Throwable -> 0x003c, all -> 0x0044 }
                r4 = r4.schedule(r8, r6, r5);	 Catch:{ Throwable -> 0x003c, all -> 0x0044 }
                r8.currentFuture = r4;	 Catch:{ Throwable -> 0x003c, all -> 0x0044 }
            L_0x0028:
                r4 = r8.lock;
                r4.unlock();
            L_0x002d:
                if (r2 == 0) goto L_0x0034;
            L_0x002f:
                r4 = r8.service;
                r4.notifyFailed(r2);
            L_0x0034:
                return;
            L_0x0035:
                r3 = move-exception;
                r4 = r8.service;
                r4.notifyFailed(r3);
                goto L_0x0034;
            L_0x003c:
                r0 = move-exception;
                r2 = r0;
                r4 = r8.lock;
                r4.unlock();
                goto L_0x002d;
            L_0x0044:
                r4 = move-exception;
                r5 = r8.lock;
                r5.unlock();
                throw r4;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.AbstractScheduledService.CustomScheduler.ReschedulableCallable.reschedule():void");
            }

            ReschedulableCallable(AbstractService service, ScheduledExecutorService executor, Runnable runnable) {
                this.wrappedRunnable = runnable;
                this.executor = executor;
                this.service = service;
            }

            public Void call() throws Exception {
                this.wrappedRunnable.run();
                reschedule();
                return null;
            }

            public boolean cancel(boolean mayInterruptIfRunning) {
                this.lock.lock();
                try {
                    boolean cancel = this.currentFuture.cancel(mayInterruptIfRunning);
                    return cancel;
                } finally {
                    this.lock.unlock();
                }
            }

            public boolean isCancelled() {
                this.lock.lock();
                try {
                    boolean isCancelled = this.currentFuture.isCancelled();
                    return isCancelled;
                } finally {
                    this.lock.unlock();
                }
            }

            protected Future<Void> delegate() {
                throw new UnsupportedOperationException("Only cancel and isCancelled is supported by this future");
            }
        }

        @Beta
        protected static final class Schedule {
            private final long delay;
            private final TimeUnit unit;

            public Schedule(long delay, TimeUnit unit) {
                this.delay = delay;
                this.unit = (TimeUnit) Preconditions.checkNotNull(unit);
            }
        }

        protected abstract Schedule getNextSchedule() throws Exception;

        public CustomScheduler() {
            super();
        }

        final Future<?> schedule(AbstractService service, ScheduledExecutorService executor, Runnable runnable) {
            ReschedulableCallable task = new ReschedulableCallable(service, executor, runnable);
            task.reschedule();
            return task;
        }
    }

    private final class ServiceDelegate extends AbstractService {
        private volatile ScheduledExecutorService executorService;
        private final ReentrantLock lock;
        private volatile Future<?> runningTask;
        private final Runnable task;

        class C08081 implements Supplier<String> {
            C08081() {
            }

            public String get() {
                return AbstractScheduledService.this.serviceName() + " " + ServiceDelegate.this.state();
            }
        }

        class C08092 implements Runnable {
            C08092() {
            }

            public void run() {
                ServiceDelegate.this.lock.lock();
                try {
                    AbstractScheduledService.this.startUp();
                    ServiceDelegate.this.runningTask = AbstractScheduledService.this.scheduler().schedule(AbstractScheduledService.this.delegate, ServiceDelegate.this.executorService, ServiceDelegate.this.task);
                    ServiceDelegate.this.notifyStarted();
                    ServiceDelegate.this.lock.unlock();
                } catch (Throwable th) {
                    ServiceDelegate.this.lock.unlock();
                }
            }
        }

        class C08103 implements Runnable {
            C08103() {
            }

            public void run() {
                try {
                    ServiceDelegate.this.lock.lock();
                    if (ServiceDelegate.this.state() != State.STOPPING) {
                        ServiceDelegate.this.lock.unlock();
                        return;
                    }
                    AbstractScheduledService.this.shutDown();
                    ServiceDelegate.this.lock.unlock();
                    ServiceDelegate.this.notifyStopped();
                } catch (Throwable t) {
                    ServiceDelegate.this.notifyFailed(t);
                }
            }
        }

        class Task implements Runnable {
            Task() {
            }

            public void run() {
                ServiceDelegate.this.lock.lock();
                try {
                    if (!ServiceDelegate.this.runningTask.isCancelled()) {
                        AbstractScheduledService.this.runOneIteration();
                        ServiceDelegate.this.lock.unlock();
                    }
                } catch (Throwable t) {
                    try {
                        AbstractScheduledService.this.shutDown();
                    } catch (Exception ignored) {
                        AbstractScheduledService.logger.log(Level.WARNING, "Error while attempting to shut down the service after failure.", ignored);
                    }
                    ServiceDelegate.this.notifyFailed(t);
                    ServiceDelegate.this.runningTask.cancel(false);
                } finally {
                    ServiceDelegate.this.lock.unlock();
                }
            }
        }

        private ServiceDelegate() {
            this.lock = new ReentrantLock();
            this.task = new Task();
        }

        protected final void doStart() {
            this.executorService = MoreExecutors.renamingDecorator(AbstractScheduledService.this.executor(), new C08081());
            this.executorService.execute(new C08092());
        }

        protected final void doStop() {
            this.runningTask.cancel(false);
            this.executorService.execute(new C08103());
        }

        public String toString() {
            return AbstractScheduledService.this.toString();
        }
    }

    protected abstract void runOneIteration() throws Exception;

    protected abstract Scheduler scheduler();

    protected AbstractScheduledService() {
    }

    protected void startUp() throws Exception {
    }

    protected void shutDown() throws Exception {
    }

    protected ScheduledExecutorService executor() {
        final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
            public Thread newThread(Runnable runnable) {
                return MoreExecutors.newThread(AbstractScheduledService.this.serviceName(), runnable);
            }
        });
        addListener(new Listener() {
            public void terminated(State from) {
                executor.shutdown();
            }

            public void failed(State from, Throwable failure) {
                executor.shutdown();
            }
        }, MoreExecutors.directExecutor());
        return executor;
    }

    protected String serviceName() {
        return getClass().getSimpleName();
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
}

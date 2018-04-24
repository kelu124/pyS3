package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.base.Throwables;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@GwtCompatible(emulated = true)
public final class MoreExecutors {
    private MoreExecutors() {
    }

    @GwtIncompatible
    @Beta
    public static ExecutorService getExitingExecutorService(ThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit) {
        return new Application().getExitingExecutorService(executor, terminationTimeout, timeUnit);
    }

    @GwtIncompatible
    @Beta
    public static ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit) {
        return new Application().getExitingScheduledExecutorService(executor, terminationTimeout, timeUnit);
    }

    @GwtIncompatible
    @Beta
    public static void addDelayedShutdownHook(ExecutorService service, long terminationTimeout, TimeUnit timeUnit) {
        new Application().addDelayedShutdownHook(service, terminationTimeout, timeUnit);
    }

    @GwtIncompatible
    @Beta
    public static ExecutorService getExitingExecutorService(ThreadPoolExecutor executor) {
        return new Application().getExitingExecutorService(executor);
    }

    @GwtIncompatible
    @Beta
    public static ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor executor) {
        return new Application().getExitingScheduledExecutorService(executor);
    }

    @GwtIncompatible
    private static void useDaemonThreadFactory(ThreadPoolExecutor executor) {
        executor.setThreadFactory(new ThreadFactoryBuilder().setDaemon(true).setThreadFactory(executor.getThreadFactory()).build());
    }

    @GwtIncompatible
    @Deprecated
    public static ListeningExecutorService sameThreadExecutor() {
        return new DirectExecutorService(null);
    }

    @GwtIncompatible
    public static ListeningExecutorService newDirectExecutorService() {
        return new DirectExecutorService(null);
    }

    public static Executor directExecutor() {
        return DirectExecutor.INSTANCE;
    }

    @GwtIncompatible
    public static ListeningExecutorService listeningDecorator(ExecutorService delegate) {
        if (delegate instanceof ListeningExecutorService) {
            return (ListeningExecutorService) delegate;
        }
        return delegate instanceof ScheduledExecutorService ? new ScheduledListeningDecorator((ScheduledExecutorService) delegate) : new ListeningDecorator(delegate);
    }

    @GwtIncompatible
    public static ListeningScheduledExecutorService listeningDecorator(ScheduledExecutorService delegate) {
        return delegate instanceof ListeningScheduledExecutorService ? (ListeningScheduledExecutorService) delegate : new ScheduledListeningDecorator(delegate);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @com.google.common.annotations.GwtIncompatible
    static <T> T invokeAnyImpl(com.google.common.util.concurrent.ListeningExecutorService r25, java.util.Collection<? extends java.util.concurrent.Callable<T>> r26, boolean r27, long r28, java.util.concurrent.TimeUnit r30) throws java.lang.InterruptedException, java.util.concurrent.ExecutionException, java.util.concurrent.TimeoutException {
        /*
        com.google.common.base.Preconditions.checkNotNull(r25);
        com.google.common.base.Preconditions.checkNotNull(r30);
        r13 = r26.size();
        if (r13 <= 0) goto L_0x0084;
    L_0x000c:
        r19 = 1;
    L_0x000e:
        com.google.common.base.Preconditions.checkArgument(r19);
        r10 = com.google.common.collect.Lists.newArrayListWithCapacity(r13);
        r9 = com.google.common.collect.Queues.newLinkedBlockingQueue();
        r0 = r30;
        r1 = r28;
        r20 = r0.toNanos(r1);
        r5 = 0;
        if (r27 == 0) goto L_0x0087;
    L_0x0024:
        r14 = java.lang.System.nanoTime();	 Catch:{ all -> 0x0098 }
    L_0x0028:
        r12 = r26.iterator();	 Catch:{ all -> 0x0098 }
        r19 = r12.next();	 Catch:{ all -> 0x0098 }
        r19 = (java.util.concurrent.Callable) r19;	 Catch:{ all -> 0x0098 }
        r0 = r25;
        r1 = r19;
        r19 = submitAndAddQueueListener(r0, r1, r9);	 Catch:{ all -> 0x0098 }
        r0 = r19;
        r10.add(r0);	 Catch:{ all -> 0x0098 }
        r13 = r13 + -1;
        r4 = 1;
        r6 = r5;
    L_0x0043:
        r8 = r9.poll();	 Catch:{ all -> 0x00c7 }
        r8 = (java.util.concurrent.Future) r8;	 Catch:{ all -> 0x00c7 }
        if (r8 != 0) goto L_0x0064;
    L_0x004b:
        if (r13 <= 0) goto L_0x008a;
    L_0x004d:
        r13 = r13 + -1;
        r19 = r12.next();	 Catch:{ all -> 0x00c7 }
        r19 = (java.util.concurrent.Callable) r19;	 Catch:{ all -> 0x00c7 }
        r0 = r25;
        r1 = r19;
        r19 = submitAndAddQueueListener(r0, r1, r9);	 Catch:{ all -> 0x00c7 }
        r0 = r19;
        r10.add(r0);	 Catch:{ all -> 0x00c7 }
        r4 = r4 + 1;
    L_0x0064:
        if (r8 == 0) goto L_0x00ee;
    L_0x0066:
        r4 = r4 + -1;
        r19 = r8.get();	 Catch:{ ExecutionException -> 0x00dc, RuntimeException -> 0x00e1 }
        r11 = r10.iterator();
    L_0x0070:
        r22 = r11.hasNext();
        if (r22 == 0) goto L_0x00eb;
    L_0x0076:
        r8 = r11.next();
        r8 = (java.util.concurrent.Future) r8;
        r22 = 1;
        r0 = r22;
        r8.cancel(r0);
        goto L_0x0070;
    L_0x0084:
        r19 = 0;
        goto L_0x000e;
    L_0x0087:
        r14 = 0;
        goto L_0x0028;
    L_0x008a:
        if (r4 != 0) goto L_0x00b1;
    L_0x008c:
        if (r6 != 0) goto L_0x00ec;
    L_0x008e:
        r5 = new java.util.concurrent.ExecutionException;	 Catch:{ all -> 0x00c7 }
        r19 = 0;
        r0 = r19;
        r5.<init>(r0);	 Catch:{ all -> 0x00c7 }
    L_0x0097:
        throw r5;	 Catch:{ all -> 0x0098 }
    L_0x0098:
        r19 = move-exception;
    L_0x0099:
        r11 = r10.iterator();
    L_0x009d:
        r22 = r11.hasNext();
        if (r22 == 0) goto L_0x00ea;
    L_0x00a3:
        r8 = r11.next();
        r8 = (java.util.concurrent.Future) r8;
        r22 = 1;
        r0 = r22;
        r8.cancel(r0);
        goto L_0x009d;
    L_0x00b1:
        if (r27 == 0) goto L_0x00d5;
    L_0x00b3:
        r19 = java.util.concurrent.TimeUnit.NANOSECONDS;	 Catch:{ all -> 0x00c7 }
        r0 = r20;
        r2 = r19;
        r8 = r9.poll(r0, r2);	 Catch:{ all -> 0x00c7 }
        r8 = (java.util.concurrent.Future) r8;	 Catch:{ all -> 0x00c7 }
        if (r8 != 0) goto L_0x00ca;
    L_0x00c1:
        r19 = new java.util.concurrent.TimeoutException;	 Catch:{ all -> 0x00c7 }
        r19.<init>();	 Catch:{ all -> 0x00c7 }
        throw r19;	 Catch:{ all -> 0x00c7 }
    L_0x00c7:
        r19 = move-exception;
        r5 = r6;
        goto L_0x0099;
    L_0x00ca:
        r16 = java.lang.System.nanoTime();	 Catch:{ all -> 0x00c7 }
        r22 = r16 - r14;
        r20 = r20 - r22;
        r14 = r16;
        goto L_0x0064;
    L_0x00d5:
        r8 = r9.take();	 Catch:{ all -> 0x00c7 }
        r8 = (java.util.concurrent.Future) r8;	 Catch:{ all -> 0x00c7 }
        goto L_0x0064;
    L_0x00dc:
        r7 = move-exception;
        r5 = r7;
    L_0x00de:
        r6 = r5;
        goto L_0x0043;
    L_0x00e1:
        r18 = move-exception;
        r5 = new java.util.concurrent.ExecutionException;	 Catch:{ all -> 0x00c7 }
        r0 = r18;
        r5.<init>(r0);	 Catch:{ all -> 0x00c7 }
        goto L_0x00de;
    L_0x00ea:
        throw r19;
    L_0x00eb:
        return r19;
    L_0x00ec:
        r5 = r6;
        goto L_0x0097;
    L_0x00ee:
        r5 = r6;
        goto L_0x00de;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.MoreExecutors.invokeAnyImpl(com.google.common.util.concurrent.ListeningExecutorService, java.util.Collection, boolean, long, java.util.concurrent.TimeUnit):T");
    }

    @GwtIncompatible
    private static <T> ListenableFuture<T> submitAndAddQueueListener(ListeningExecutorService executorService, Callable<T> task, BlockingQueue<Future<T>> queue) {
        ListenableFuture<T> future = executorService.submit(task);
        future.addListener(new 1(queue, future), directExecutor());
        return future;
    }

    @GwtIncompatible
    @Beta
    public static ThreadFactory platformThreadFactory() {
        if (!isAppEngine()) {
            return Executors.defaultThreadFactory();
        }
        try {
            return (ThreadFactory) Class.forName("com.google.appengine.api.ThreadManager").getMethod("currentRequestThreadFactory", new Class[0]).invoke(null, new Object[0]);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Couldn't invoke ThreadManager.currentRequestThreadFactory", e);
        } catch (ClassNotFoundException e2) {
            throw new RuntimeException("Couldn't invoke ThreadManager.currentRequestThreadFactory", e2);
        } catch (NoSuchMethodException e3) {
            throw new RuntimeException("Couldn't invoke ThreadManager.currentRequestThreadFactory", e3);
        } catch (InvocationTargetException e4) {
            throw Throwables.propagate(e4.getCause());
        }
    }

    @GwtIncompatible
    private static boolean isAppEngine() {
        if (System.getProperty("com.google.appengine.runtime.environment") == null) {
            return false;
        }
        try {
            if (Class.forName("com.google.apphosting.api.ApiProxy").getMethod("getCurrentEnvironment", new Class[0]).invoke(null, new Object[0]) != null) {
                return true;
            }
            return false;
        } catch (ClassNotFoundException e) {
            return false;
        } catch (InvocationTargetException e2) {
            return false;
        } catch (IllegalAccessException e3) {
            return false;
        } catch (NoSuchMethodException e4) {
            return false;
        }
    }

    @GwtIncompatible
    static Thread newThread(String name, Runnable runnable) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(runnable);
        Thread result = platformThreadFactory().newThread(runnable);
        try {
            result.setName(name);
        } catch (SecurityException e) {
        }
        return result;
    }

    @GwtIncompatible
    static Executor renamingDecorator(Executor executor, Supplier<String> nameSupplier) {
        Preconditions.checkNotNull(executor);
        Preconditions.checkNotNull(nameSupplier);
        return isAppEngine() ? executor : new 2(executor, nameSupplier);
    }

    @GwtIncompatible
    static ExecutorService renamingDecorator(ExecutorService service, Supplier<String> nameSupplier) {
        Preconditions.checkNotNull(service);
        Preconditions.checkNotNull(nameSupplier);
        return isAppEngine() ? service : new 3(service, nameSupplier);
    }

    @GwtIncompatible
    static ScheduledExecutorService renamingDecorator(ScheduledExecutorService service, Supplier<String> nameSupplier) {
        Preconditions.checkNotNull(service);
        Preconditions.checkNotNull(nameSupplier);
        return isAppEngine() ? service : new 4(service, nameSupplier);
    }

    @GwtIncompatible
    @CanIgnoreReturnValue
    @Beta
    public static boolean shutdownAndAwaitTermination(ExecutorService service, long timeout, TimeUnit unit) {
        long halfTimeoutNanos = unit.toNanos(timeout) / 2;
        service.shutdown();
        try {
            if (!service.awaitTermination(halfTimeoutNanos, TimeUnit.NANOSECONDS)) {
                service.shutdownNow();
                service.awaitTermination(halfTimeoutNanos, TimeUnit.NANOSECONDS);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            service.shutdownNow();
        }
        return service.isTerminated();
    }

    static Executor rejectionPropagatingExecutor(Executor delegate, AbstractFuture<?> future) {
        Preconditions.checkNotNull(delegate);
        Preconditions.checkNotNull(future);
        return delegate == directExecutor() ? delegate : new 5(delegate, future);
    }
}

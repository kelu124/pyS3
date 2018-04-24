package com.google.common.util.concurrent;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@GwtIncompatible
@VisibleForTesting
class MoreExecutors$Application {
    MoreExecutors$Application() {
    }

    final ExecutorService getExitingExecutorService(ThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit) {
        MoreExecutors.access$000(executor);
        ExecutorService service = Executors.unconfigurableExecutorService(executor);
        addDelayedShutdownHook(service, terminationTimeout, timeUnit);
        return service;
    }

    final ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit) {
        MoreExecutors.access$000(executor);
        ScheduledExecutorService service = Executors.unconfigurableScheduledExecutorService(executor);
        addDelayedShutdownHook(service, terminationTimeout, timeUnit);
        return service;
    }

    final void addDelayedShutdownHook(ExecutorService service, long terminationTimeout, TimeUnit timeUnit) {
        Preconditions.checkNotNull(service);
        Preconditions.checkNotNull(timeUnit);
        final ExecutorService executorService = service;
        final long j = terminationTimeout;
        final TimeUnit timeUnit2 = timeUnit;
        addShutdownHook(MoreExecutors.newThread("DelayedShutdownHook-for-" + service, new Runnable() {
            public void run() {
                try {
                    executorService.shutdown();
                    executorService.awaitTermination(j, timeUnit2);
                } catch (InterruptedException e) {
                }
            }
        }));
    }

    final ExecutorService getExitingExecutorService(ThreadPoolExecutor executor) {
        return getExitingExecutorService(executor, 120, TimeUnit.SECONDS);
    }

    final ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor executor) {
        return getExitingScheduledExecutorService(executor, 120, TimeUnit.SECONDS);
    }

    @VisibleForTesting
    void addShutdownHook(Thread hook) {
        Runtime.getRuntime().addShutdownHook(hook);
    }
}

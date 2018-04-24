package com.google.common.util.concurrent;

import com.google.common.base.Supplier;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

class MoreExecutors$3 extends WrappingExecutorService {
    final /* synthetic */ Supplier val$nameSupplier;

    MoreExecutors$3(ExecutorService x0, Supplier supplier) {
        this.val$nameSupplier = supplier;
        super(x0);
    }

    protected <T> Callable<T> wrapTask(Callable<T> callable) {
        return Callables.threadRenaming((Callable) callable, this.val$nameSupplier);
    }

    protected Runnable wrapTask(Runnable command) {
        return Callables.threadRenaming(command, this.val$nameSupplier);
    }
}

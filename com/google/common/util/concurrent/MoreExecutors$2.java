package com.google.common.util.concurrent;

import com.google.common.base.Supplier;
import java.util.concurrent.Executor;

class MoreExecutors$2 implements Executor {
    final /* synthetic */ Executor val$executor;
    final /* synthetic */ Supplier val$nameSupplier;

    MoreExecutors$2(Executor executor, Supplier supplier) {
        this.val$executor = executor;
        this.val$nameSupplier = supplier;
    }

    public void execute(Runnable command) {
        this.val$executor.execute(Callables.threadRenaming(command, this.val$nameSupplier));
    }
}

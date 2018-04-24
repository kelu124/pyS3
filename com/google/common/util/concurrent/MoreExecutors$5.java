package com.google.common.util.concurrent;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;

class MoreExecutors$5 implements Executor {
    volatile boolean thrownFromDelegate = true;
    final /* synthetic */ Executor val$delegate;
    final /* synthetic */ AbstractFuture val$future;

    MoreExecutors$5(Executor executor, AbstractFuture abstractFuture) {
        this.val$delegate = executor;
        this.val$future = abstractFuture;
    }

    public void execute(final Runnable command) {
        try {
            this.val$delegate.execute(new Runnable() {
                public void run() {
                    MoreExecutors$5.this.thrownFromDelegate = false;
                    command.run();
                }
            });
        } catch (RejectedExecutionException e) {
            if (this.thrownFromDelegate) {
                this.val$future.setException(e);
            }
        }
    }
}

package com.google.common.util.concurrent;

import com.google.common.base.Function;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

class Futures$1 implements Future<O> {
    final /* synthetic */ Function val$function;
    final /* synthetic */ Future val$input;

    Futures$1(Future future, Function function) {
        this.val$input = future;
        this.val$function = function;
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        return this.val$input.cancel(mayInterruptIfRunning);
    }

    public boolean isCancelled() {
        return this.val$input.isCancelled();
    }

    public boolean isDone() {
        return this.val$input.isDone();
    }

    public O get() throws InterruptedException, ExecutionException {
        return applyTransformation(this.val$input.get());
    }

    public O get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return applyTransformation(this.val$input.get(timeout, unit));
    }

    private O applyTransformation(I input) throws ExecutionException {
        try {
            return this.val$function.apply(input);
        } catch (Throwable t) {
            ExecutionException executionException = new ExecutionException(t);
        }
    }
}

package com.google.common.util.concurrent;

import java.util.concurrent.Executor;
import javax.annotation.Nullable;

final class ExecutionList$RunnableExecutorPair {
    final Executor executor;
    @Nullable
    ExecutionList$RunnableExecutorPair next;
    final Runnable runnable;

    ExecutionList$RunnableExecutorPair(Runnable runnable, Executor executor, ExecutionList$RunnableExecutorPair next) {
        this.runnable = runnable;
        this.executor = executor;
        this.next = next;
    }
}

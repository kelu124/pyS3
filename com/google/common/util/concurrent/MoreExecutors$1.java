package com.google.common.util.concurrent;

import java.util.concurrent.BlockingQueue;

class MoreExecutors$1 implements Runnable {
    final /* synthetic */ ListenableFuture val$future;
    final /* synthetic */ BlockingQueue val$queue;

    MoreExecutors$1(BlockingQueue blockingQueue, ListenableFuture listenableFuture) {
        this.val$queue = blockingQueue;
        this.val$future = listenableFuture;
    }

    public void run() {
        this.val$queue.add(this.val$future);
    }
}

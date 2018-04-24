package com.google.common.util.concurrent;

import java.util.concurrent.ConcurrentLinkedQueue;

class Futures$3 implements Runnable {
    final /* synthetic */ ConcurrentLinkedQueue val$delegates;
    final /* synthetic */ ListenableFuture val$future;

    Futures$3(ConcurrentLinkedQueue concurrentLinkedQueue, ListenableFuture listenableFuture) {
        this.val$delegates = concurrentLinkedQueue;
        this.val$future = listenableFuture;
    }

    public void run() {
        ((SettableFuture) this.val$delegates.remove()).setFuture(this.val$future);
    }
}

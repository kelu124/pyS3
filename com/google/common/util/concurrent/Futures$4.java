package com.google.common.util.concurrent;

import java.util.concurrent.ExecutionException;

class Futures$4 implements Runnable {
    final /* synthetic */ FutureCallback val$callback;
    final /* synthetic */ ListenableFuture val$future;

    Futures$4(ListenableFuture listenableFuture, FutureCallback futureCallback) {
        this.val$future = listenableFuture;
        this.val$callback = futureCallback;
    }

    public void run() {
        try {
            this.val$callback.onSuccess(Futures.getDone(this.val$future));
        } catch (ExecutionException e) {
            this.val$callback.onFailure(e.getCause());
        } catch (RuntimeException e2) {
            this.val$callback.onFailure(e2);
        } catch (Error e3) {
            this.val$callback.onFailure(e3);
        }
    }
}

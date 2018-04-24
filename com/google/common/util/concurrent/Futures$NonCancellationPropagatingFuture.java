package com.google.common.util.concurrent;

final class Futures$NonCancellationPropagatingFuture<V> extends TrustedFuture<V> {
    Futures$NonCancellationPropagatingFuture(final ListenableFuture<V> delegate) {
        delegate.addListener(new Runnable() {
            public void run() {
                Futures$NonCancellationPropagatingFuture.this.setFuture(delegate);
            }
        }, MoreExecutors.directExecutor());
    }
}

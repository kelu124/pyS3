package com.google.common.util.concurrent;

import javax.annotation.Nullable;

final class AbstractFuture$Cancellation {
    @Nullable
    final Throwable cause;
    final boolean wasInterrupted;

    AbstractFuture$Cancellation(boolean wasInterrupted, @Nullable Throwable cause) {
        this.wasInterrupted = wasInterrupted;
        this.cause = cause;
    }
}

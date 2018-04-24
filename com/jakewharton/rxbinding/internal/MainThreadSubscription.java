package com.jakewharton.rxbinding.internal;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Keep;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import rx.Subscription;

public abstract class MainThreadSubscription implements Subscription, Runnable {
    private static final Handler mainThread = new Handler(Looper.getMainLooper());
    private static final AtomicIntegerFieldUpdater<MainThreadSubscription> unsubscribedUpdater = AtomicIntegerFieldUpdater.newUpdater(MainThreadSubscription.class, "unsubscribed");
    @Keep
    private volatile int unsubscribed;

    protected abstract void onUnsubscribe();

    public final boolean isUnsubscribed() {
        return this.unsubscribed != 0;
    }

    public final void unsubscribe() {
        if (!unsubscribedUpdater.compareAndSet(this, 0, 1)) {
            return;
        }
        if (Looper.getMainLooper() == Looper.myLooper()) {
            onUnsubscribe();
        } else {
            mainThread.post(this);
        }
    }

    public final void run() {
        onUnsubscribe();
    }
}

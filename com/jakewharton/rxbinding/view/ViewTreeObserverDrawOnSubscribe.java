package com.jakewharton.rxbinding.view;

import android.annotation.TargetApi;
import android.view.View;
import android.view.ViewTreeObserver.OnDrawListener;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import com.jakewharton.rxbinding.internal.Preconditions;
import rx.Observable$OnSubscribe;
import rx.Subscriber;

@TargetApi(16)
final class ViewTreeObserverDrawOnSubscribe implements Observable$OnSubscribe<Void> {
    private final View view;

    public ViewTreeObserverDrawOnSubscribe(View view) {
        this.view = view;
    }

    public void call(final Subscriber<? super Void> subscriber) {
        Preconditions.checkUiThread();
        final OnDrawListener listener = new OnDrawListener() {
            public void onDraw() {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(null);
                }
            }
        };
        this.view.getViewTreeObserver().addOnDrawListener(listener);
        subscriber.add(new MainThreadSubscription() {
            protected void onUnsubscribe() {
                ViewTreeObserverDrawOnSubscribe.this.view.getViewTreeObserver().removeOnDrawListener(listener);
            }
        });
    }
}

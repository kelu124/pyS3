package com.jakewharton.rxbinding.view;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import com.jakewharton.rxbinding.internal.Preconditions;
import rx.Observable$OnSubscribe;
import rx.Subscriber;

final class ViewAttachesOnSubscribe implements Observable$OnSubscribe<Void> {
    private final boolean callOnAttach;
    private final View view;

    ViewAttachesOnSubscribe(View view, boolean callOnAttach) {
        this.view = view;
        this.callOnAttach = callOnAttach;
    }

    public void call(final Subscriber<? super Void> subscriber) {
        Preconditions.checkUiThread();
        final OnAttachStateChangeListener listener = new OnAttachStateChangeListener() {
            public void onViewAttachedToWindow(@NonNull View v) {
                if (ViewAttachesOnSubscribe.this.callOnAttach && !subscriber.isUnsubscribed()) {
                    subscriber.onNext(null);
                }
            }

            public void onViewDetachedFromWindow(@NonNull View v) {
                if (!ViewAttachesOnSubscribe.this.callOnAttach && !subscriber.isUnsubscribed()) {
                    subscriber.onNext(null);
                }
            }
        };
        this.view.addOnAttachStateChangeListener(listener);
        subscriber.add(new MainThreadSubscription() {
            protected void onUnsubscribe() {
                ViewAttachesOnSubscribe.this.view.removeOnAttachStateChangeListener(listener);
            }
        });
    }
}

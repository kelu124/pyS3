package com.jakewharton.rxbinding.view;

import android.view.View;
import android.view.View.OnClickListener;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import com.jakewharton.rxbinding.internal.Preconditions;
import rx.Observable$OnSubscribe;
import rx.Subscriber;

final class ViewClickOnSubscribe implements Observable$OnSubscribe<Void> {
    private final View view;

    class C09082 extends MainThreadSubscription {
        C09082() {
        }

        protected void onUnsubscribe() {
            ViewClickOnSubscribe.this.view.setOnClickListener(null);
        }
    }

    ViewClickOnSubscribe(View view) {
        this.view = view;
    }

    public void call(final Subscriber<? super Void> subscriber) {
        Preconditions.checkUiThread();
        this.view.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(null);
                }
            }
        });
        subscriber.add(new C09082());
    }
}

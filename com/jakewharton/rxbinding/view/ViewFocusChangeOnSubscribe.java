package com.jakewharton.rxbinding.view;

import android.view.View;
import android.view.View.OnFocusChangeListener;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import com.jakewharton.rxbinding.internal.Preconditions;
import rx.Observable$OnSubscribe;
import rx.Subscriber;

final class ViewFocusChangeOnSubscribe implements Observable$OnSubscribe<Boolean> {
    private final View view;

    class C09122 extends MainThreadSubscription {
        C09122() {
        }

        protected void onUnsubscribe() {
            ViewFocusChangeOnSubscribe.this.view.setOnFocusChangeListener(null);
        }
    }

    public ViewFocusChangeOnSubscribe(View view) {
        this.view = view;
    }

    public void call(final Subscriber<? super Boolean> subscriber) {
        Preconditions.checkUiThread();
        this.view.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(Boolean.valueOf(hasFocus));
                }
            }
        });
        subscriber.add(new C09122());
        subscriber.onNext(Boolean.valueOf(this.view.hasFocus()));
    }
}

package com.jakewharton.rxbinding.view;

import android.view.View;
import android.view.View.OnSystemUiVisibilityChangeListener;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import com.jakewharton.rxbinding.internal.Preconditions;
import rx.Observable$OnSubscribe;
import rx.Subscriber;

final class ViewSystemUiVisibilityChangeOnSubscribe implements Observable$OnSubscribe<Integer> {
    private final View view;

    class C09262 extends MainThreadSubscription {
        C09262() {
        }

        protected void onUnsubscribe() {
            ViewSystemUiVisibilityChangeOnSubscribe.this.view.setOnSystemUiVisibilityChangeListener(null);
        }
    }

    public ViewSystemUiVisibilityChangeOnSubscribe(View view) {
        this.view = view;
    }

    public void call(final Subscriber<? super Integer> subscriber) {
        Preconditions.checkUiThread();
        this.view.setOnSystemUiVisibilityChangeListener(new OnSystemUiVisibilityChangeListener() {
            public void onSystemUiVisibilityChange(int visibility) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(Integer.valueOf(visibility));
                }
            }
        });
        subscriber.add(new C09262());
    }
}

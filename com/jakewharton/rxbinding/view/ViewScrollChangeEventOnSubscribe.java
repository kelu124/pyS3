package com.jakewharton.rxbinding.view;

import android.annotation.TargetApi;
import android.view.View;
import android.view.View.OnScrollChangeListener;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import com.jakewharton.rxbinding.internal.Preconditions;
import rx.Observable$OnSubscribe;
import rx.Subscriber;

@TargetApi(23)
final class ViewScrollChangeEventOnSubscribe implements Observable$OnSubscribe<ViewScrollChangeEvent> {
    private final View view;

    class C09242 extends MainThreadSubscription {
        C09242() {
        }

        protected void onUnsubscribe() {
            ViewScrollChangeEventOnSubscribe.this.view.setOnScrollChangeListener(null);
        }
    }

    ViewScrollChangeEventOnSubscribe(View view) {
        this.view = view;
    }

    public void call(final Subscriber<? super ViewScrollChangeEvent> subscriber) {
        Preconditions.checkUiThread();
        this.view.setOnScrollChangeListener(new OnScrollChangeListener() {
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(ViewScrollChangeEvent.create(ViewScrollChangeEventOnSubscribe.this.view, scrollX, scrollY, oldScrollX, oldScrollY));
                }
            }
        });
        subscriber.add(new C09242());
    }
}

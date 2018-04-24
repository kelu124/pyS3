package com.jakewharton.rxbinding.view;

import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnHoverListener;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import com.jakewharton.rxbinding.internal.Preconditions;
import rx.Observable$OnSubscribe;
import rx.Subscriber;
import rx.functions.Func1;

final class ViewHoverOnSubscribe implements Observable$OnSubscribe<MotionEvent> {
    private final Func1<? super MotionEvent, Boolean> handled;
    private final View view;

    class C09162 extends MainThreadSubscription {
        C09162() {
        }

        protected void onUnsubscribe() {
            ViewHoverOnSubscribe.this.view.setOnHoverListener(null);
        }
    }

    public ViewHoverOnSubscribe(View view, Func1<? super MotionEvent, Boolean> handled) {
        this.view = view;
        this.handled = handled;
    }

    public void call(final Subscriber<? super MotionEvent> subscriber) {
        Preconditions.checkUiThread();
        this.view.setOnHoverListener(new OnHoverListener() {
            public boolean onHover(View v, @NonNull MotionEvent event) {
                if (!((Boolean) ViewHoverOnSubscribe.this.handled.call(event)).booleanValue()) {
                    return false;
                }
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(event);
                }
                return true;
            }
        });
        subscriber.add(new C09162());
    }
}

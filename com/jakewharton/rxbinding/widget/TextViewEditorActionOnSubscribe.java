package com.jakewharton.rxbinding.widget;

import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import com.jakewharton.rxbinding.internal.Preconditions;
import rx.Observable$OnSubscribe;
import rx.Subscriber;
import rx.functions.Func1;

final class TextViewEditorActionOnSubscribe implements Observable$OnSubscribe<Integer> {
    private final Func1<? super Integer, Boolean> handled;
    private final TextView view;

    class C09972 extends MainThreadSubscription {
        C09972() {
        }

        protected void onUnsubscribe() {
            TextViewEditorActionOnSubscribe.this.view.setOnEditorActionListener(null);
        }
    }

    public TextViewEditorActionOnSubscribe(TextView view, Func1<? super Integer, Boolean> handled) {
        this.view = view;
        this.handled = handled;
    }

    public void call(final Subscriber<? super Integer> subscriber) {
        Preconditions.checkUiThread();
        this.view.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (!((Boolean) TextViewEditorActionOnSubscribe.this.handled.call(Integer.valueOf(actionId))).booleanValue()) {
                    return false;
                }
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(Integer.valueOf(actionId));
                }
                return true;
            }
        });
        subscriber.add(new C09972());
    }
}

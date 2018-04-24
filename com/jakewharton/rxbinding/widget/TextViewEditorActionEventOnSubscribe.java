package com.jakewharton.rxbinding.widget;

import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import com.jakewharton.rxbinding.internal.Preconditions;
import rx.Observable$OnSubscribe;
import rx.Subscriber;
import rx.functions.Func1;

final class TextViewEditorActionEventOnSubscribe implements Observable$OnSubscribe<TextViewEditorActionEvent> {
    private final Func1<? super TextViewEditorActionEvent, Boolean> handled;
    private final TextView view;

    class C09952 extends MainThreadSubscription {
        C09952() {
        }

        protected void onUnsubscribe() {
            TextViewEditorActionEventOnSubscribe.this.view.setOnEditorActionListener(null);
        }
    }

    public TextViewEditorActionEventOnSubscribe(TextView view, Func1<? super TextViewEditorActionEvent, Boolean> handled) {
        this.view = view;
        this.handled = handled;
    }

    public void call(final Subscriber<? super TextViewEditorActionEvent> subscriber) {
        Preconditions.checkUiThread();
        this.view.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent keyEvent) {
                TextViewEditorActionEvent event = TextViewEditorActionEvent.create(v, actionId, keyEvent);
                if (!((Boolean) TextViewEditorActionEventOnSubscribe.this.handled.call(event)).booleanValue()) {
                    return false;
                }
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(event);
                }
                return true;
            }
        });
        subscriber.add(new C09952());
    }
}

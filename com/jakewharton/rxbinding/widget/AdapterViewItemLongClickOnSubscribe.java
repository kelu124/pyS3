package com.jakewharton.rxbinding.widget;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import com.jakewharton.rxbinding.internal.Preconditions;
import rx.Observable$OnSubscribe;
import rx.Subscriber;
import rx.functions.Func0;

final class AdapterViewItemLongClickOnSubscribe implements Observable$OnSubscribe<Integer> {
    private final Func0<Boolean> handled;
    private final AdapterView<?> view;

    class C09442 extends MainThreadSubscription {
        C09442() {
        }

        protected void onUnsubscribe() {
            AdapterViewItemLongClickOnSubscribe.this.view.setOnItemLongClickListener(null);
        }
    }

    public AdapterViewItemLongClickOnSubscribe(AdapterView<?> view, Func0<Boolean> handled) {
        this.view = view;
        this.handled = handled;
    }

    public void call(final Subscriber<? super Integer> subscriber) {
        Preconditions.checkUiThread();
        this.view.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (!((Boolean) AdapterViewItemLongClickOnSubscribe.this.handled.call()).booleanValue()) {
                    return false;
                }
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(Integer.valueOf(position));
                }
                return true;
            }
        });
        subscriber.add(new C09442());
    }
}

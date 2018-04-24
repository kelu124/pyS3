package com.jakewharton.rxbinding.widget;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import com.jakewharton.rxbinding.internal.Preconditions;
import rx.Observable$OnSubscribe;
import rx.Subscriber;

final class AutoCompleteTextViewItemClickEventOnSubscribe implements Observable$OnSubscribe<AdapterViewItemClickEvent> {
    private final AutoCompleteTextView view;

    class C09502 extends MainThreadSubscription {
        C09502() {
        }

        protected void onUnsubscribe() {
            AutoCompleteTextViewItemClickEventOnSubscribe.this.view.setOnItemClickListener(null);
        }
    }

    public AutoCompleteTextViewItemClickEventOnSubscribe(AutoCompleteTextView view) {
        this.view = view;
    }

    public void call(final Subscriber<? super AdapterViewItemClickEvent> subscriber) {
        Preconditions.checkUiThread();
        this.view.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(AdapterViewItemClickEvent.create(parent, view, position, id));
                }
            }
        });
        subscriber.add(new C09502());
    }
}

package com.jakewharton.rxbinding.widget;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import com.jakewharton.rxbinding.internal.Preconditions;
import rx.Observable$OnSubscribe;
import rx.Subscriber;

final class AdapterViewItemSelectionOnSubscribe implements Observable$OnSubscribe<Integer> {
    private final AdapterView<?> view;

    class C09462 extends MainThreadSubscription {
        C09462() {
        }

        protected void onUnsubscribe() {
            AdapterViewItemSelectionOnSubscribe.this.view.setOnItemSelectedListener(null);
        }
    }

    public AdapterViewItemSelectionOnSubscribe(AdapterView<?> view) {
        this.view = view;
    }

    public void call(final Subscriber<? super Integer> subscriber) {
        Preconditions.checkUiThread();
        this.view.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(Integer.valueOf(position));
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(Integer.valueOf(-1));
                }
            }
        });
        subscriber.add(new C09462());
        subscriber.onNext(Integer.valueOf(this.view.getSelectedItemPosition()));
    }
}

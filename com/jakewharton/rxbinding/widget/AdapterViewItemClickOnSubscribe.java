package com.jakewharton.rxbinding.widget;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import com.jakewharton.rxbinding.internal.Preconditions;
import rx.Observable$OnSubscribe;
import rx.Subscriber;

final class AdapterViewItemClickOnSubscribe implements Observable$OnSubscribe<Integer> {
    private final AdapterView<?> view;

    class C09402 extends MainThreadSubscription {
        C09402() {
        }

        protected void onUnsubscribe() {
            AdapterViewItemClickOnSubscribe.this.view.setOnItemClickListener(null);
        }
    }

    public AdapterViewItemClickOnSubscribe(AdapterView<?> view) {
        this.view = view;
    }

    public void call(final Subscriber<? super Integer> subscriber) {
        Preconditions.checkUiThread();
        this.view.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(Integer.valueOf(position));
                }
            }
        });
        subscriber.add(new C09402());
    }
}

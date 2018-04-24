package com.jakewharton.rxbinding.widget;

import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import com.jakewharton.rxbinding.internal.Preconditions;
import rx.Observable$OnSubscribe;
import rx.Subscriber;

final class CompoundButtonCheckedChangeOnSubscribe implements Observable$OnSubscribe<Boolean> {
    private final CompoundButton view;

    class C09522 extends MainThreadSubscription {
        C09522() {
        }

        protected void onUnsubscribe() {
            CompoundButtonCheckedChangeOnSubscribe.this.view.setOnCheckedChangeListener(null);
        }
    }

    public CompoundButtonCheckedChangeOnSubscribe(CompoundButton view) {
        this.view = view;
    }

    public void call(final Subscriber<? super Boolean> subscriber) {
        Preconditions.checkUiThread();
        this.view.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(Boolean.valueOf(isChecked));
                }
            }
        });
        subscriber.add(new C09522());
        subscriber.onNext(Boolean.valueOf(this.view.isChecked()));
    }
}

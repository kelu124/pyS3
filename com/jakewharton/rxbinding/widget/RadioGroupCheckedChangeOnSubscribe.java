package com.jakewharton.rxbinding.widget;

import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import com.jakewharton.rxbinding.internal.Preconditions;
import rx.Observable$OnSubscribe;
import rx.Subscriber;

final class RadioGroupCheckedChangeOnSubscribe implements Observable$OnSubscribe<Integer> {
    private final RadioGroup view;

    class C09542 extends MainThreadSubscription {
        C09542() {
        }

        protected void onUnsubscribe() {
            RadioGroupCheckedChangeOnSubscribe.this.view.setOnCheckedChangeListener(null);
        }
    }

    public RadioGroupCheckedChangeOnSubscribe(RadioGroup view) {
        this.view = view;
    }

    public void call(final Subscriber<? super Integer> subscriber) {
        Preconditions.checkUiThread();
        this.view.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(Integer.valueOf(checkedId));
                }
            }
        });
        subscriber.add(new C09542());
        subscriber.onNext(Integer.valueOf(this.view.getCheckedRadioButtonId()));
    }
}

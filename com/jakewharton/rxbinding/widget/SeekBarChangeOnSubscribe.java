package com.jakewharton.rxbinding.widget;

import android.support.annotation.Nullable;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import com.jakewharton.rxbinding.internal.Preconditions;
import rx.Observable$OnSubscribe;
import rx.Subscriber;

final class SeekBarChangeOnSubscribe implements Observable$OnSubscribe<Integer> {
    @Nullable
    private final Boolean shouldBeFromUser;
    private final SeekBar view;

    class C09892 extends MainThreadSubscription {
        C09892() {
        }

        protected void onUnsubscribe() {
            SeekBarChangeOnSubscribe.this.view.setOnSeekBarChangeListener(null);
        }
    }

    public SeekBarChangeOnSubscribe(SeekBar view, @Nullable Boolean shouldBeFromUser) {
        this.view = view;
        this.shouldBeFromUser = shouldBeFromUser;
    }

    public void call(final Subscriber<? super Integer> subscriber) {
        Preconditions.checkUiThread();
        this.view.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (!subscriber.isUnsubscribed()) {
                    if (SeekBarChangeOnSubscribe.this.shouldBeFromUser == null || SeekBarChangeOnSubscribe.this.shouldBeFromUser.booleanValue() == fromUser) {
                        subscriber.onNext(Integer.valueOf(progress));
                    }
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        subscriber.add(new C09892());
        subscriber.onNext(Integer.valueOf(this.view.getProgress()));
    }
}

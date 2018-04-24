package com.jakewharton.rxbinding.widget;

import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import com.jakewharton.rxbinding.internal.Preconditions;
import rx.Observable$OnSubscribe;
import rx.Subscriber;

final class SeekBarChangeEventOnSubscribe implements Observable$OnSubscribe<SeekBarChangeEvent> {
    private final SeekBar view;

    class C09872 extends MainThreadSubscription {
        C09872() {
        }

        protected void onUnsubscribe() {
            SeekBarChangeEventOnSubscribe.this.view.setOnSeekBarChangeListener(null);
        }
    }

    public SeekBarChangeEventOnSubscribe(SeekBar view) {
        this.view = view;
    }

    public void call(final Subscriber<? super SeekBarChangeEvent> subscriber) {
        Preconditions.checkUiThread();
        this.view.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(SeekBarProgressChangeEvent.create(seekBar, progress, fromUser));
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(SeekBarStartChangeEvent.create(seekBar));
                }
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(SeekBarStopChangeEvent.create(seekBar));
                }
            }
        });
        subscriber.add(new C09872());
        subscriber.onNext(SeekBarProgressChangeEvent.create(this.view, this.view.getProgress(), false));
    }
}

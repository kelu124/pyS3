package com.jakewharton.rxbinding.widget;

import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import com.jakewharton.rxbinding.internal.Preconditions;
import rx.Observable$OnSubscribe;
import rx.Subscriber;

final class RatingBarRatingChangeEventOnSubscribe implements Observable$OnSubscribe<RatingBarChangeEvent> {
    private final RatingBar view;

    class C09562 extends MainThreadSubscription {
        C09562() {
        }

        protected void onUnsubscribe() {
            RatingBarRatingChangeEventOnSubscribe.this.view.setOnRatingBarChangeListener(null);
        }
    }

    public RatingBarRatingChangeEventOnSubscribe(RatingBar view) {
        this.view = view;
    }

    public void call(final Subscriber<? super RatingBarChangeEvent> subscriber) {
        Preconditions.checkUiThread();
        this.view.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(RatingBarChangeEvent.create(ratingBar, rating, fromUser));
                }
            }
        });
        subscriber.add(new C09562());
        subscriber.onNext(RatingBarChangeEvent.create(this.view, this.view.getRating(), false));
    }
}

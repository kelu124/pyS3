package com.jakewharton.rxbinding.widget;

import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import com.jakewharton.rxbinding.internal.Preconditions;
import rx.Observable$OnSubscribe;
import rx.Subscriber;

final class RatingBarRatingChangeOnSubscribe implements Observable$OnSubscribe<Float> {
    private final RatingBar view;

    class C09582 extends MainThreadSubscription {
        C09582() {
        }

        protected void onUnsubscribe() {
            RatingBarRatingChangeOnSubscribe.this.view.setOnRatingBarChangeListener(null);
        }
    }

    public RatingBarRatingChangeOnSubscribe(RatingBar view) {
        this.view = view;
    }

    public void call(final Subscriber<? super Float> subscriber) {
        Preconditions.checkUiThread();
        this.view.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(Float.valueOf(rating));
                }
            }
        });
        subscriber.add(new C09582());
        subscriber.onNext(Float.valueOf(this.view.getRating()));
    }
}

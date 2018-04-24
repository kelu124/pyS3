package com.jakewharton.rxbinding.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.RatingBar;
import rx.Observable;
import rx.functions.Action1;

public final class RxRatingBar {
    @CheckResult
    @NonNull
    public static Observable<Float> ratingChanges(@NonNull RatingBar view) {
        return Observable.create(new RatingBarRatingChangeOnSubscribe(view));
    }

    @CheckResult
    @NonNull
    public static Observable<RatingBarChangeEvent> ratingChangeEvents(@NonNull RatingBar view) {
        return Observable.create(new RatingBarRatingChangeEventOnSubscribe(view));
    }

    @CheckResult
    @NonNull
    public static Action1<? super Float> rating(@NonNull final RatingBar view) {
        return new Action1<Float>() {
            public void call(Float value) {
                view.setRating(value.floatValue());
            }
        };
    }

    @CheckResult
    @NonNull
    public static Action1<? super Boolean> isIndicator(@NonNull final RatingBar view) {
        return new Action1<Boolean>() {
            public void call(Boolean value) {
                view.setIsIndicator(value.booleanValue());
            }
        };
    }
}

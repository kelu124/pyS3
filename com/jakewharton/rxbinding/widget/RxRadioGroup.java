package com.jakewharton.rxbinding.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.RadioGroup;
import rx.Observable;
import rx.functions.Action1;

public final class RxRadioGroup {
    @CheckResult
    @NonNull
    public static Observable<Integer> checkedChanges(@NonNull RadioGroup view) {
        return Observable.create(new RadioGroupCheckedChangeOnSubscribe(view)).distinctUntilChanged();
    }

    @CheckResult
    @NonNull
    public static Action1<? super Integer> checked(@NonNull final RadioGroup view) {
        return new Action1<Integer>() {
            public void call(Integer value) {
                if (value.intValue() == -1) {
                    view.clearCheck();
                } else {
                    view.check(value.intValue());
                }
            }
        };
    }
}

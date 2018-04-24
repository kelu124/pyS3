package com.jakewharton.rxbinding.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.SeekBar;
import rx.Observable;

public final class RxSeekBar {
    @CheckResult
    @NonNull
    public static Observable<Integer> changes(@NonNull SeekBar view) {
        return Observable.create(new SeekBarChangeOnSubscribe(view, null));
    }

    @CheckResult
    @NonNull
    public static Observable<Integer> userChanges(@NonNull SeekBar view) {
        return Observable.create(new SeekBarChangeOnSubscribe(view, Boolean.valueOf(true)));
    }

    @CheckResult
    @NonNull
    public static Observable<Integer> systemChanges(@NonNull SeekBar view) {
        return Observable.create(new SeekBarChangeOnSubscribe(view, Boolean.valueOf(false)));
    }

    @CheckResult
    @NonNull
    public static Observable<SeekBarChangeEvent> changeEvents(@NonNull SeekBar view) {
        return Observable.create(new SeekBarChangeEventOnSubscribe(view));
    }

    private RxSeekBar() {
        throw new AssertionError("No instances.");
    }
}

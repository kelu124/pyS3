package com.jakewharton.rxbinding.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.TextSwitcher;
import rx.functions.Action1;

public final class RxTextSwitcher {
    @CheckResult
    @NonNull
    public static Action1<? super CharSequence> text(@NonNull final TextSwitcher view) {
        return new Action1<CharSequence>() {
            public void call(CharSequence text) {
                view.setText(text);
            }
        };
    }

    @CheckResult
    @NonNull
    public static Action1<? super CharSequence> currentText(@NonNull final TextSwitcher view) {
        return new Action1<CharSequence>() {
            public void call(CharSequence textRes) {
                view.setCurrentText(textRes);
            }
        };
    }

    private RxTextSwitcher() {
        throw new AssertionError("No instances.");
    }
}

package com.jakewharton.rxbinding.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.CheckedTextView;
import rx.functions.Action1;

public final class RxCheckedTextView {
    @CheckResult
    @NonNull
    public static Action1<? super Boolean> check(@NonNull final CheckedTextView view) {
        return new Action1<Boolean>() {
            public void call(Boolean check) {
                view.setChecked(check.booleanValue());
            }
        };
    }

    private RxCheckedTextView() {
        throw new AssertionError("No instances.");
    }
}

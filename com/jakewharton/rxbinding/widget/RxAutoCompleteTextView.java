package com.jakewharton.rxbinding.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.AutoCompleteTextView;
import rx.Observable;

public final class RxAutoCompleteTextView {
    @CheckResult
    @NonNull
    public static Observable<AdapterViewItemClickEvent> itemClickEvents(@NonNull AutoCompleteTextView view) {
        return Observable.create(new AutoCompleteTextViewItemClickEventOnSubscribe(view));
    }

    private RxAutoCompleteTextView() {
        throw new AssertionError("No instances.");
    }
}

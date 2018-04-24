package com.jakewharton.rxbinding.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.Adapter;
import rx.Observable;

public final class RxAdapter {
    @CheckResult
    @NonNull
    public static <T extends Adapter> Observable<T> dataChanges(@NonNull T adapter) {
        return Observable.create(new AdapterDataChangeOnSubscribe(adapter));
    }

    private RxAdapter() {
        throw new AssertionError("No instances.");
    }
}

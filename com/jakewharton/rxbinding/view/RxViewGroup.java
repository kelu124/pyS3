package com.jakewharton.rxbinding.view;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import rx.Observable;

public final class RxViewGroup {
    @CheckResult
    @NonNull
    public static Observable<ViewGroupHierarchyChangeEvent> changeEvents(@NonNull ViewGroup viewGroup) {
        return Observable.create(new ViewGroupHierarchyChangeEventOnSubscribe(viewGroup));
    }

    private RxViewGroup() {
        throw new AssertionError("No instances.");
    }
}

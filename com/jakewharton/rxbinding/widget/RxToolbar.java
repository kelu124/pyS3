package com.jakewharton.rxbinding.widget;

import android.annotation.TargetApi;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.Toolbar;
import rx.Observable;

@TargetApi(21)
public final class RxToolbar {
    @CheckResult
    @NonNull
    public static Observable<MenuItem> itemClicks(@NonNull Toolbar view) {
        return Observable.create(new ToolbarItemClickOnSubscribe(view));
    }

    @CheckResult
    @NonNull
    public static Observable<Void> navigationClicks(@NonNull Toolbar view) {
        return Observable.create(new ToolbarNavigationClickOnSubscribe(view));
    }

    private RxToolbar() {
        throw new AssertionError("No instances.");
    }
}

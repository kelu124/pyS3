package com.jakewharton.rxbinding.widget;

import android.annotation.TargetApi;
import android.view.MenuItem;
import android.widget.Toolbar;
import android.widget.Toolbar.OnMenuItemClickListener;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import com.jakewharton.rxbinding.internal.Preconditions;
import rx.Observable$OnSubscribe;
import rx.Subscriber;

@TargetApi(21)
final class ToolbarItemClickOnSubscribe implements Observable$OnSubscribe<MenuItem> {
    private final Toolbar view;

    class C10032 extends MainThreadSubscription {
        C10032() {
        }

        protected void onUnsubscribe() {
            ToolbarItemClickOnSubscribe.this.view.setOnMenuItemClickListener(null);
        }
    }

    public ToolbarItemClickOnSubscribe(Toolbar view) {
        this.view = view;
    }

    public void call(final Subscriber<? super MenuItem> subscriber) {
        Preconditions.checkUiThread();
        this.view.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(item);
                }
                return true;
            }
        });
        subscriber.add(new C10032());
    }
}

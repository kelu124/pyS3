package com.jakewharton.rxbinding.widget;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import com.jakewharton.rxbinding.internal.Preconditions;
import rx.Observable$OnSubscribe;
import rx.Subscriber;
import rx.functions.Func1;

final class AdapterViewItemLongClickEventOnSubscribe implements Observable$OnSubscribe<AdapterViewItemLongClickEvent> {
    private final Func1<? super AdapterViewItemLongClickEvent, Boolean> handled;
    private final AdapterView<?> view;

    class C09422 extends MainThreadSubscription {
        C09422() {
        }

        protected void onUnsubscribe() {
            AdapterViewItemLongClickEventOnSubscribe.this.view.setOnItemLongClickListener(null);
        }
    }

    public AdapterViewItemLongClickEventOnSubscribe(AdapterView<?> view, Func1<? super AdapterViewItemLongClickEvent, Boolean> handled) {
        this.view = view;
        this.handled = handled;
    }

    public void call(final Subscriber<? super AdapterViewItemLongClickEvent> subscriber) {
        Preconditions.checkUiThread();
        this.view.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AdapterViewItemLongClickEvent event = AdapterViewItemLongClickEvent.create(parent, view, position, id);
                if (!((Boolean) AdapterViewItemLongClickEventOnSubscribe.this.handled.call(event)).booleanValue()) {
                    return false;
                }
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(event);
                }
                return true;
            }
        });
        subscriber.add(new C09422());
    }
}

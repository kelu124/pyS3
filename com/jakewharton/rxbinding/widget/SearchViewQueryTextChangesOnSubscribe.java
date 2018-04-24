package com.jakewharton.rxbinding.widget;

import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import com.jakewharton.rxbinding.internal.Preconditions;
import rx.Observable$OnSubscribe;
import rx.Subscriber;

final class SearchViewQueryTextChangesOnSubscribe implements Observable$OnSubscribe<CharSequence> {
    private final SearchView view;

    class C09852 extends MainThreadSubscription {
        C09852() {
        }

        protected void onUnsubscribe() {
            SearchViewQueryTextChangesOnSubscribe.this.view.setOnQueryTextListener(null);
        }
    }

    SearchViewQueryTextChangesOnSubscribe(SearchView view) {
        this.view = view;
    }

    public void call(final Subscriber<? super CharSequence> subscriber) {
        Preconditions.checkUiThread();
        this.view.setOnQueryTextListener(new OnQueryTextListener() {
            public boolean onQueryTextChange(String s) {
                if (subscriber.isUnsubscribed()) {
                    return false;
                }
                subscriber.onNext(s);
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });
        subscriber.add(new C09852());
        subscriber.onNext(this.view.getQuery());
    }
}

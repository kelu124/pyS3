package com.jakewharton.rxbinding.widget;

import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import com.jakewharton.rxbinding.internal.Preconditions;
import rx.Observable$OnSubscribe;
import rx.Subscriber;

final class SearchViewQueryTextChangeEventsOnSubscribe implements Observable$OnSubscribe<SearchViewQueryTextEvent> {
    private final SearchView view;

    class C09832 extends MainThreadSubscription {
        C09832() {
        }

        protected void onUnsubscribe() {
            SearchViewQueryTextChangeEventsOnSubscribe.this.view.setOnQueryTextListener(null);
        }
    }

    SearchViewQueryTextChangeEventsOnSubscribe(SearchView view) {
        this.view = view;
    }

    public void call(final Subscriber<? super SearchViewQueryTextEvent> subscriber) {
        Preconditions.checkUiThread();
        this.view.setOnQueryTextListener(new OnQueryTextListener() {
            public boolean onQueryTextChange(String s) {
                if (subscriber.isUnsubscribed()) {
                    return false;
                }
                subscriber.onNext(SearchViewQueryTextEvent.create(SearchViewQueryTextChangeEventsOnSubscribe.this.view, s, false));
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                if (subscriber.isUnsubscribed()) {
                    return false;
                }
                subscriber.onNext(SearchViewQueryTextEvent.create(SearchViewQueryTextChangeEventsOnSubscribe.this.view, SearchViewQueryTextChangeEventsOnSubscribe.this.view.getQuery(), true));
                return true;
            }
        });
        subscriber.add(new C09832());
        subscriber.onNext(SearchViewQueryTextEvent.create(this.view, this.view.getQuery(), false));
    }
}

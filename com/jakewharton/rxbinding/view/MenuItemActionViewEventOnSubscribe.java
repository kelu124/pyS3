package com.jakewharton.rxbinding.view;

import android.view.MenuItem;
import android.view.MenuItem.OnActionExpandListener;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import com.jakewharton.rxbinding.internal.Preconditions;
import com.jakewharton.rxbinding.view.MenuItemActionViewEvent.Kind;
import rx.Observable$OnSubscribe;
import rx.Subscriber;
import rx.functions.Func1;

final class MenuItemActionViewEventOnSubscribe implements Observable$OnSubscribe<MenuItemActionViewEvent> {
    private final Func1<? super MenuItemActionViewEvent, Boolean> handled;
    private final MenuItem menuItem;

    class C08872 extends MainThreadSubscription {
        C08872() {
        }

        protected void onUnsubscribe() {
            MenuItemActionViewEventOnSubscribe.this.menuItem.setOnActionExpandListener(null);
        }
    }

    MenuItemActionViewEventOnSubscribe(MenuItem menuItem, Func1<? super MenuItemActionViewEvent, Boolean> handled) {
        this.menuItem = menuItem;
        this.handled = handled;
    }

    public void call(final Subscriber<? super MenuItemActionViewEvent> subscriber) {
        Preconditions.checkUiThread();
        this.menuItem.setOnActionExpandListener(new OnActionExpandListener() {
            public boolean onMenuItemActionExpand(MenuItem item) {
                return onEvent(MenuItemActionViewEvent.create(MenuItemActionViewEventOnSubscribe.this.menuItem, Kind.EXPAND));
            }

            public boolean onMenuItemActionCollapse(MenuItem item) {
                return onEvent(MenuItemActionViewEvent.create(MenuItemActionViewEventOnSubscribe.this.menuItem, Kind.COLLAPSE));
            }

            private boolean onEvent(MenuItemActionViewEvent event) {
                if (!((Boolean) MenuItemActionViewEventOnSubscribe.this.handled.call(event)).booleanValue()) {
                    return false;
                }
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(event);
                }
                return true;
            }
        });
        subscriber.add(new C08872());
    }
}

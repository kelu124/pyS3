package rx.internal.util;

import rx.Subscription;

public class SynchronizedSubscription implements Subscription {
    private final Subscription f219s;

    public SynchronizedSubscription(Subscription s) {
        this.f219s = s;
    }

    public synchronized void unsubscribe() {
        this.f219s.unsubscribe();
    }

    public synchronized boolean isUnsubscribed() {
        return this.f219s.isUnsubscribed();
    }
}

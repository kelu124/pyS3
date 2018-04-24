package rx;

final class Observable$EmptyHolder {
    static final Observable<Object> INSTANCE = Observable.create(new C12911());

    static class C12911 implements Observable$OnSubscribe<Object> {
        C12911() {
        }

        public void call(Subscriber<? super Object> subscriber) {
            subscriber.onCompleted();
        }
    }

    private Observable$EmptyHolder() {
    }
}

package rx;

class Observable$ThrowObservable<T> extends Observable<T> {

    class C12931 implements Observable$OnSubscribe<T> {
        final /* synthetic */ Throwable val$exception;

        C12931(Throwable th) {
            this.val$exception = th;
        }

        public void call(Subscriber<? super T> observer) {
            observer.onError(this.val$exception);
        }
    }

    public Observable$ThrowObservable(Throwable exception) {
        super(new C12931(exception));
    }
}

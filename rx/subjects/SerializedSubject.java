package rx.subjects;

import rx.Observable$OnSubscribe;
import rx.Subscriber;
import rx.observers.SerializedObserver;

public class SerializedSubject<T, R> extends Subject<T, R> {
    private final Subject<T, R> actual;
    private final SerializedObserver<T> observer;

    class C15851 implements Observable$OnSubscribe<R> {
        final /* synthetic */ Subject val$actual;

        C15851(Subject subject) {
            this.val$actual = subject;
        }

        public void call(Subscriber<? super R> child) {
            this.val$actual.unsafeSubscribe(child);
        }
    }

    public SerializedSubject(Subject<T, R> actual) {
        super(new C15851(actual));
        this.actual = actual;
        this.observer = new SerializedObserver(actual);
    }

    public void onCompleted() {
        this.observer.onCompleted();
    }

    public void onError(Throwable e) {
        this.observer.onError(e);
    }

    public void onNext(T t) {
        this.observer.onNext(t);
    }

    public boolean hasObservers() {
        return this.actual.hasObservers();
    }
}

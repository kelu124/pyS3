package rx;

import rx.functions.Action0;
import rx.functions.Action1;

class Observable$29 extends Subscriber<T> {
    final /* synthetic */ Observable this$0;
    final /* synthetic */ Action0 val$onComplete;
    final /* synthetic */ Action1 val$onError;
    final /* synthetic */ Action1 val$onNext;

    Observable$29(Observable observable, Action0 action0, Action1 action1, Action1 action12) {
        this.this$0 = observable;
        this.val$onComplete = action0;
        this.val$onError = action1;
        this.val$onNext = action12;
    }

    public final void onCompleted() {
        this.val$onComplete.call();
    }

    public final void onError(Throwable e) {
        this.val$onError.call(e);
    }

    public final void onNext(T args) {
        this.val$onNext.call(args);
    }
}

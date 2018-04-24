package rx;

import rx.functions.Func1;

class Observable$25 implements Func1<Observable<? extends Notification<?>>, Observable<?>> {
    final /* synthetic */ Observable this$0;
    final /* synthetic */ Func1 val$notificationHandler;

    class C12881 implements Func1<Notification<?>, Throwable> {
        C12881() {
        }

        public Throwable call(Notification<?> notification) {
            return notification.getThrowable();
        }
    }

    Observable$25(Observable observable, Func1 func1) {
        this.this$0 = observable;
        this.val$notificationHandler = func1;
    }

    public Observable<?> call(Observable<? extends Notification<?>> notifications) {
        return (Observable) this.val$notificationHandler.call(notifications.map(new C12881()));
    }
}

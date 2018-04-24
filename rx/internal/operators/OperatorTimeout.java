package rx.internal.operators;

import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;

public final class OperatorTimeout<T> extends OperatorTimeoutBase<T> {

    class C14871 implements FirstTimeoutStub<T> {
        final /* synthetic */ TimeUnit val$timeUnit;
        final /* synthetic */ long val$timeout;

        C14871(long j, TimeUnit timeUnit) {
            this.val$timeout = j;
            this.val$timeUnit = timeUnit;
        }

        public Subscription call(final TimeoutSubscriber<T> timeoutSubscriber, final Long seqId, Worker inner) {
            return inner.schedule(new Action0() {
                public void call() {
                    timeoutSubscriber.onTimeout(seqId.longValue());
                }
            }, this.val$timeout, this.val$timeUnit);
        }
    }

    class C14892 implements TimeoutStub<T> {
        final /* synthetic */ TimeUnit val$timeUnit;
        final /* synthetic */ long val$timeout;

        C14892(long j, TimeUnit timeUnit) {
            this.val$timeout = j;
            this.val$timeUnit = timeUnit;
        }

        public Subscription call(final TimeoutSubscriber<T> timeoutSubscriber, final Long seqId, T t, Worker inner) {
            return inner.schedule(new Action0() {
                public void call() {
                    timeoutSubscriber.onTimeout(seqId.longValue());
                }
            }, this.val$timeout, this.val$timeUnit);
        }
    }

    public /* bridge */ /* synthetic */ Subscriber call(Subscriber x0) {
        return super.call(x0);
    }

    public OperatorTimeout(long timeout, TimeUnit timeUnit, Observable<? extends T> other, Scheduler scheduler) {
        super(new C14871(timeout, timeUnit), new C14892(timeout, timeUnit), other, scheduler);
    }
}

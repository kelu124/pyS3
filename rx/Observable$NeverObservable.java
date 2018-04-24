package rx;

class Observable$NeverObservable<T> extends Observable<T> {

    class C12921 implements Observable$OnSubscribe<T> {
        C12921() {
        }

        public void call(Subscriber<? super T> subscriber) {
        }
    }

    private static class Holder {
        static final Observable$NeverObservable<?> INSTANCE = new Observable$NeverObservable();

        private Holder() {
        }
    }

    static <T> Observable$NeverObservable<T> instance() {
        return Holder.INSTANCE;
    }

    Observable$NeverObservable() {
        super(new C12921());
    }
}

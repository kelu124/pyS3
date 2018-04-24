package rx;

import rx.functions.Func2;

final class Observable$CountHolder {
    static final Func2<Integer, Object, Integer> INSTANCE = new C12891();

    static class C12891 implements Func2<Integer, Object, Integer> {
        C12891() {
        }

        public final Integer call(Integer count, Object o) {
            return Integer.valueOf(count.intValue() + 1);
        }
    }

    private Observable$CountHolder() {
    }
}

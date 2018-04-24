package rx;

import rx.functions.Func2;

final class Observable$CountLongHolder {
    static final Func2<Long, Object, Long> INSTANCE = new C12901();

    static class C12901 implements Func2<Long, Object, Long> {
        C12901() {
        }

        public final Long call(Long count, Object o) {
            return Long.valueOf(count.longValue() + 1);
        }
    }

    private Observable$CountLongHolder() {
    }
}

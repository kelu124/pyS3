package com.google.common.escape;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
final class Platform {
    private static final ThreadLocal<char[]> DEST_TL = new C06891();

    static class C06891 extends ThreadLocal<char[]> {
        C06891() {
        }

        protected char[] initialValue() {
            return new char[1024];
        }
    }

    private Platform() {
    }

    static char[] charBufferFromThreadLocal() {
        return (char[]) DEST_TL.get();
    }
}

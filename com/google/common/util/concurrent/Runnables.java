package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
@Beta
public final class Runnables {
    private static final Runnable EMPTY_RUNNABLE = new C08351();

    static class C08351 implements Runnable {
        C08351() {
        }

        public void run() {
        }
    }

    public static Runnable doNothing() {
        return EMPTY_RUNNABLE;
    }

    private Runnables() {
    }
}

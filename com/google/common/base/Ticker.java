package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;

@GwtCompatible
@Beta
public abstract class Ticker {
    private static final Ticker SYSTEM_TICKER = new C04311();

    static class C04311 extends Ticker {
        C04311() {
        }

        public long read() {
            return Platform.systemNanoTime();
        }
    }

    @CanIgnoreReturnValue
    public abstract long read();

    protected Ticker() {
    }

    public static Ticker systemTicker() {
        return SYSTEM_TICKER;
    }
}

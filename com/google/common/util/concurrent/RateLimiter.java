package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import javax.annotation.concurrent.ThreadSafe;

@GwtIncompatible
@ThreadSafe
@Beta
public abstract class RateLimiter {
    private volatile Object mutexDoNotUseDirectly;
    private final SleepingStopwatch stopwatch;

    static abstract class SleepingStopwatch {

        static class C08341 extends SleepingStopwatch {
            final Stopwatch stopwatch = Stopwatch.createStarted();

            C08341() {
            }

            protected long readMicros() {
                return this.stopwatch.elapsed(TimeUnit.MICROSECONDS);
            }

            protected void sleepMicrosUninterruptibly(long micros) {
                if (micros > 0) {
                    Uninterruptibles.sleepUninterruptibly(micros, TimeUnit.MICROSECONDS);
                }
            }
        }

        protected abstract long readMicros();

        protected abstract void sleepMicrosUninterruptibly(long j);

        protected SleepingStopwatch() {
        }

        public static final SleepingStopwatch createFromSystemTimer() {
            return new C08341();
        }
    }

    abstract double doGetRate();

    abstract void doSetRate(double d, long j);

    abstract long queryEarliestAvailable(long j);

    abstract long reserveEarliestAvailable(int i, long j);

    public static RateLimiter create(double permitsPerSecond) {
        return create(SleepingStopwatch.createFromSystemTimer(), permitsPerSecond);
    }

    @VisibleForTesting
    static RateLimiter create(SleepingStopwatch stopwatch, double permitsPerSecond) {
        RateLimiter rateLimiter = new SmoothBursty(stopwatch, 1.0d);
        rateLimiter.setRate(permitsPerSecond);
        return rateLimiter;
    }

    public static RateLimiter create(double permitsPerSecond, long warmupPeriod, TimeUnit unit) {
        Preconditions.checkArgument(warmupPeriod >= 0, "warmupPeriod must not be negative: %s", warmupPeriod);
        return create(SleepingStopwatch.createFromSystemTimer(), permitsPerSecond, warmupPeriod, unit, 3.0d);
    }

    @VisibleForTesting
    static RateLimiter create(SleepingStopwatch stopwatch, double permitsPerSecond, long warmupPeriod, TimeUnit unit, double coldFactor) {
        RateLimiter rateLimiter = new SmoothWarmingUp(stopwatch, warmupPeriod, unit, coldFactor);
        rateLimiter.setRate(permitsPerSecond);
        return rateLimiter;
    }

    private Object mutex() {
        Object mutex = this.mutexDoNotUseDirectly;
        if (mutex == null) {
            synchronized (this) {
                try {
                    mutex = this.mutexDoNotUseDirectly;
                    if (mutex == null) {
                        Object mutex2 = new Object();
                        try {
                            this.mutexDoNotUseDirectly = mutex2;
                            mutex = mutex2;
                        } catch (Throwable th) {
                            Throwable th2 = th;
                            mutex = mutex2;
                            throw th2;
                        }
                    }
                } catch (Throwable th3) {
                    th2 = th3;
                    throw th2;
                }
            }
        }
        return mutex;
    }

    RateLimiter(SleepingStopwatch stopwatch) {
        this.stopwatch = (SleepingStopwatch) Preconditions.checkNotNull(stopwatch);
    }

    public final void setRate(double permitsPerSecond) {
        boolean z = permitsPerSecond > 0.0d && !Double.isNaN(permitsPerSecond);
        Preconditions.checkArgument(z, "rate must be positive");
        synchronized (mutex()) {
            doSetRate(permitsPerSecond, this.stopwatch.readMicros());
        }
    }

    public final double getRate() {
        double doGetRate;
        synchronized (mutex()) {
            doGetRate = doGetRate();
        }
        return doGetRate;
    }

    @CanIgnoreReturnValue
    public double acquire() {
        return acquire(1);
    }

    @CanIgnoreReturnValue
    public double acquire(int permits) {
        long microsToWait = reserve(permits);
        this.stopwatch.sleepMicrosUninterruptibly(microsToWait);
        return (1.0d * ((double) microsToWait)) / ((double) TimeUnit.SECONDS.toMicros(1));
    }

    final long reserve(int permits) {
        long reserveAndGetWaitLength;
        checkPermits(permits);
        synchronized (mutex()) {
            reserveAndGetWaitLength = reserveAndGetWaitLength(permits, this.stopwatch.readMicros());
        }
        return reserveAndGetWaitLength;
    }

    public boolean tryAcquire(long timeout, TimeUnit unit) {
        return tryAcquire(1, timeout, unit);
    }

    public boolean tryAcquire(int permits) {
        return tryAcquire(permits, 0, TimeUnit.MICROSECONDS);
    }

    public boolean tryAcquire() {
        return tryAcquire(1, 0, TimeUnit.MICROSECONDS);
    }

    public boolean tryAcquire(int permits, long timeout, TimeUnit unit) {
        long timeoutMicros = Math.max(unit.toMicros(timeout), 0);
        checkPermits(permits);
        synchronized (mutex()) {
            long nowMicros = this.stopwatch.readMicros();
            if (canAcquire(nowMicros, timeoutMicros)) {
                long microsToWait = reserveAndGetWaitLength(permits, nowMicros);
                this.stopwatch.sleepMicrosUninterruptibly(microsToWait);
                return true;
            }
            return false;
        }
    }

    private boolean canAcquire(long nowMicros, long timeoutMicros) {
        return queryEarliestAvailable(nowMicros) - timeoutMicros <= nowMicros;
    }

    final long reserveAndGetWaitLength(int permits, long nowMicros) {
        return Math.max(reserveEarliestAvailable(permits, nowMicros) - nowMicros, 0);
    }

    public String toString() {
        return String.format(Locale.ROOT, "RateLimiter[stableRate=%3.1fqps]", new Object[]{Double.valueOf(getRate())});
    }

    private static void checkPermits(int permits) {
        Preconditions.checkArgument(permits > 0, "Requested permits (%s) must be positive", permits);
    }
}

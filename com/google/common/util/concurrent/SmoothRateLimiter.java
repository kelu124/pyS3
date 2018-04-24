package com.google.common.util.concurrent;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.math.LongMath;
import java.util.concurrent.TimeUnit;

@GwtIncompatible
abstract class SmoothRateLimiter extends RateLimiter {
    double maxPermits;
    private long nextFreeTicketMicros;
    double stableIntervalMicros;
    double storedPermits;

    static final class SmoothBursty extends SmoothRateLimiter {
        final double maxBurstSeconds;

        SmoothBursty(SleepingStopwatch stopwatch, double maxBurstSeconds) {
            super(stopwatch);
            this.maxBurstSeconds = maxBurstSeconds;
        }

        void doSetRate(double permitsPerSecond, double stableIntervalMicros) {
            double d = 0.0d;
            double oldMaxPermits = this.maxPermits;
            this.maxPermits = this.maxBurstSeconds * permitsPerSecond;
            if (oldMaxPermits == Double.POSITIVE_INFINITY) {
                this.storedPermits = this.maxPermits;
                return;
            }
            if (oldMaxPermits != 0.0d) {
                d = (this.storedPermits * this.maxPermits) / oldMaxPermits;
            }
            this.storedPermits = d;
        }

        long storedPermitsToWaitTime(double storedPermits, double permitsToTake) {
            return 0;
        }

        double coolDownIntervalMicros() {
            return this.stableIntervalMicros;
        }
    }

    static final class SmoothWarmingUp extends SmoothRateLimiter {
        private double coldFactor;
        private double slope;
        private double thresholdPermits;
        private final long warmupPeriodMicros;

        SmoothWarmingUp(SleepingStopwatch stopwatch, long warmupPeriod, TimeUnit timeUnit, double coldFactor) {
            super(stopwatch);
            this.warmupPeriodMicros = timeUnit.toMicros(warmupPeriod);
            this.coldFactor = coldFactor;
        }

        void doSetRate(double permitsPerSecond, double stableIntervalMicros) {
            double oldMaxPermits = this.maxPermits;
            double coldIntervalMicros = stableIntervalMicros * this.coldFactor;
            this.thresholdPermits = (0.5d * ((double) this.warmupPeriodMicros)) / stableIntervalMicros;
            this.maxPermits = this.thresholdPermits + ((2.0d * ((double) this.warmupPeriodMicros)) / (stableIntervalMicros + coldIntervalMicros));
            this.slope = (coldIntervalMicros - stableIntervalMicros) / (this.maxPermits - this.thresholdPermits);
            if (oldMaxPermits == Double.POSITIVE_INFINITY) {
                this.storedPermits = 0.0d;
            } else {
                this.storedPermits = oldMaxPermits == 0.0d ? this.maxPermits : (this.storedPermits * this.maxPermits) / oldMaxPermits;
            }
        }

        long storedPermitsToWaitTime(double storedPermits, double permitsToTake) {
            double availablePermitsAboveThreshold = storedPermits - this.thresholdPermits;
            long micros = 0;
            if (availablePermitsAboveThreshold > 0.0d) {
                double permitsAboveThresholdToTake = Math.min(availablePermitsAboveThreshold, permitsToTake);
                micros = (long) ((permitsAboveThresholdToTake * (permitsToTime(availablePermitsAboveThreshold) + permitsToTime(availablePermitsAboveThreshold - permitsAboveThresholdToTake))) / 2.0d);
                permitsToTake -= permitsAboveThresholdToTake;
            }
            return (long) (((double) micros) + (this.stableIntervalMicros * permitsToTake));
        }

        private double permitsToTime(double permits) {
            return this.stableIntervalMicros + (this.slope * permits);
        }

        double coolDownIntervalMicros() {
            return ((double) this.warmupPeriodMicros) / this.maxPermits;
        }
    }

    abstract double coolDownIntervalMicros();

    abstract void doSetRate(double d, double d2);

    abstract long storedPermitsToWaitTime(double d, double d2);

    private SmoothRateLimiter(SleepingStopwatch stopwatch) {
        super(stopwatch);
        this.nextFreeTicketMicros = 0;
    }

    final void doSetRate(double permitsPerSecond, long nowMicros) {
        resync(nowMicros);
        double stableIntervalMicros = ((double) TimeUnit.SECONDS.toMicros(1)) / permitsPerSecond;
        this.stableIntervalMicros = stableIntervalMicros;
        doSetRate(permitsPerSecond, stableIntervalMicros);
    }

    final double doGetRate() {
        return ((double) TimeUnit.SECONDS.toMicros(1)) / this.stableIntervalMicros;
    }

    final long queryEarliestAvailable(long nowMicros) {
        return this.nextFreeTicketMicros;
    }

    final long reserveEarliestAvailable(int requiredPermits, long nowMicros) {
        resync(nowMicros);
        long returnValue = this.nextFreeTicketMicros;
        double storedPermitsToSpend = Math.min((double) requiredPermits, this.storedPermits);
        this.nextFreeTicketMicros = LongMath.saturatedAdd(this.nextFreeTicketMicros, storedPermitsToWaitTime(this.storedPermits, storedPermitsToSpend) + ((long) (this.stableIntervalMicros * (((double) requiredPermits) - storedPermitsToSpend))));
        this.storedPermits -= storedPermitsToSpend;
        return returnValue;
    }

    void resync(long nowMicros) {
        if (nowMicros > this.nextFreeTicketMicros) {
            this.storedPermits = Math.min(this.maxPermits, this.storedPermits + (((double) (nowMicros - this.nextFreeTicketMicros)) / coolDownIntervalMicros()));
            this.nextFreeTicketMicros = nowMicros;
        }
    }
}

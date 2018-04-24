package com.google.common.math;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.math.BigInteger;

@GwtIncompatible
final class DoubleUtils {
    static final int EXPONENT_BIAS = 1023;
    static final long EXPONENT_MASK = 9218868437227405312L;
    static final long IMPLICIT_BIT = 4503599627370496L;
    private static final long ONE_BITS = Double.doubleToRawLongBits(1.0d);
    static final int SIGNIFICAND_BITS = 52;
    static final long SIGNIFICAND_MASK = 4503599627370495L;
    static final long SIGN_MASK = Long.MIN_VALUE;

    private DoubleUtils() {
    }

    static double nextDown(double d) {
        return -Math.nextUp(-d);
    }

    static long getSignificand(double d) {
        Preconditions.checkArgument(isFinite(d), "not a normal value");
        long bits = Double.doubleToRawLongBits(d) & 4503599627370495L;
        return Math.getExponent(d) == -1023 ? bits << 1 : 4503599627370496L | bits;
    }

    static boolean isFinite(double d) {
        return Math.getExponent(d) <= 1023;
    }

    static boolean isNormal(double d) {
        return Math.getExponent(d) >= -1022;
    }

    static double scaleNormalize(double x) {
        return Double.longBitsToDouble(ONE_BITS | (Double.doubleToRawLongBits(x) & 4503599627370495L));
    }

    static double bigToDouble(BigInteger x) {
        BigInteger absX = x.abs();
        int exponent = absX.bitLength() - 1;
        if (exponent < 63) {
            return (double) x.longValue();
        }
        if (exponent > 1023) {
            return ((double) x.signum()) * Double.POSITIVE_INFINITY;
        }
        long signifRounded;
        int shift = (exponent - 52) - 1;
        long twiceSignifFloor = absX.shiftRight(shift).longValue();
        long signifFloor = (twiceSignifFloor >> 1) & 4503599627370495L;
        boolean increment = (1 & twiceSignifFloor) != 0 && ((1 & signifFloor) != 0 || absX.getLowestSetBit() < shift);
        if (increment) {
            signifRounded = signifFloor + 1;
        } else {
            signifRounded = signifFloor;
        }
        return Double.longBitsToDouble(((((long) (exponent + 1023)) << 52) + signifRounded) | (((long) x.signum()) & Long.MIN_VALUE));
    }

    static double ensureNonNegative(double value) {
        Preconditions.checkArgument(!Double.isNaN(value));
        if (value > 0.0d) {
            return value;
        }
        return 0.0d;
    }
}

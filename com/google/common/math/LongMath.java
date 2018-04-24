package com.google.common.math;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.primitives.UnsignedLongs;
import com.itextpdf.text.html.HtmlTags;
import com.itextpdf.text.pdf.codec.TIFFConstants;
import com.itextpdf.text.pdf.codec.wmf.MetaDo;
import java.math.RoundingMode;
import org.bytedeco.javacpp.dc1394;
import org.bytedeco.javacpp.opencv_videoio;

@GwtCompatible(emulated = true)
public final class LongMath {
    @VisibleForTesting
    static final long FLOOR_SQRT_MAX_LONG = 3037000499L;
    @VisibleForTesting
    static final long MAX_POWER_OF_SQRT2_UNSIGNED = -5402926248376769404L;
    @VisibleForTesting
    static final long MAX_SIGNED_POWER_OF_TWO = 4611686018427387904L;
    private static final int SIEVE_30 = -545925251;
    static final int[] biggestBinomials = new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 3810779, 121977, 16175, 4337, 1733, 887, opencv_videoio.CV_CAP_PROP_XI_SENSOR_OUTPUT_CHANNEL_COUNT, dc1394.DC1394_COLOR_CODING_RAW8, TIFFConstants.TIFFTAG_CELLLENGTH, 206, 169, 143, 125, 111, 101, 94, 88, 83, 79, 76, 74, 72, 70, 69, 68, 67, 67, 66, 66, 66, 66};
    @VisibleForTesting
    static final int[] biggestSimpleBinomials = new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 2642246, 86251, 11724, 3218, MetaDo.META_TEXTOUT, 684, 419, TIFFConstants.TIFFTAG_YPOSITION, 214, 169, 139, 119, 105, 95, 87, 81, 76, 73, 70, 68, 66, 64, 63, 62, 62, 61, 61, 61};
    static final long[] factorials = new long[]{1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800, 39916800, 479001600, 6227020800L, 87178291200L, 1307674368000L, 20922789888000L, 355687428096000L, 6402373705728000L, 121645100408832000L, 2432902008176640000L};
    @GwtIncompatible
    @VisibleForTesting
    static final long[] halfPowersOf10 = new long[]{3, 31, 316, 3162, 31622, 316227, 3162277, 31622776, 316227766, 3162277660L, 31622776601L, 316227766016L, 3162277660168L, 31622776601683L, 316227766016837L, 3162277660168379L, 31622776601683793L, 316227766016837933L, 3162277660168379331L};
    @VisibleForTesting
    static final byte[] maxLog10ForLeadingZeros = new byte[]{(byte) 19, (byte) 18, (byte) 18, (byte) 18, (byte) 18, (byte) 17, (byte) 17, (byte) 17, (byte) 16, (byte) 16, (byte) 16, (byte) 15, (byte) 15, (byte) 15, (byte) 15, (byte) 14, (byte) 14, (byte) 14, (byte) 13, (byte) 13, (byte) 13, (byte) 12, (byte) 12, (byte) 12, (byte) 12, (byte) 11, (byte) 11, (byte) 11, (byte) 10, (byte) 10, (byte) 10, (byte) 9, (byte) 9, (byte) 9, (byte) 9, (byte) 8, (byte) 8, (byte) 8, (byte) 7, (byte) 7, (byte) 7, (byte) 6, (byte) 6, (byte) 6, (byte) 6, (byte) 5, (byte) 5, (byte) 5, (byte) 4, (byte) 4, (byte) 4, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 2, (byte) 2, (byte) 2, (byte) 1, (byte) 1, (byte) 1, (byte) 0, (byte) 0, (byte) 0};
    private static final long[][] millerRabinBaseSets = new long[][]{new long[]{291830, 126401071349994536L}, new long[]{885594168, 725270293939359937L, 3569819667048198375L}, new long[]{273919523040L, 15, 7363882082L, 992620450144556L}, new long[]{47636622961200L, 2, 2570940, 211991001, 3749873356L}, new long[]{7999252175582850L, 2, 4130806001517L, 149795463772692060L, 186635894390467037L, 3967304179347715805L}, new long[]{585226005592931976L, 2, 123635709730000L, 9233062284813009L, 43835965440333360L, 761179012939631437L, 1263739024124850375L}, new long[]{Long.MAX_VALUE, 2, 325, 9375, 28178, 450775, 9780504, 1795265022}};
    @GwtIncompatible
    @VisibleForTesting
    static final long[] powersOf10 = new long[]{1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000, 10000000000L, 100000000000L, 1000000000000L, 10000000000000L, 100000000000000L, 1000000000000000L, 10000000000000000L, 100000000000000000L, 1000000000000000000L};

    static /* synthetic */ class C07581 {
        static final /* synthetic */ int[] $SwitchMap$java$math$RoundingMode = new int[RoundingMode.values().length];

        static {
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.UNNECESSARY.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.DOWN.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.FLOOR.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.UP.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.CEILING.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_DOWN.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_UP.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_EVEN.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
        }
    }

    private enum MillerRabinTester {
        SMALL {
            long mulMod(long a, long b, long m) {
                return (a * b) % m;
            }

            long squareMod(long a, long m) {
                return (a * a) % m;
            }
        },
        LARGE {
            private long plusMod(long a, long b, long m) {
                return a >= m - b ? (a + b) - m : a + b;
            }

            private long times2ToThe32Mod(long a, long m) {
                int remainingPowersOf2 = 32;
                do {
                    int shift = Math.min(remainingPowersOf2, Long.numberOfLeadingZeros(a));
                    a = UnsignedLongs.remainder(a << shift, m);
                    remainingPowersOf2 -= shift;
                } while (remainingPowersOf2 > 0);
                return a;
            }

            long mulMod(long a, long b, long m) {
                long aHi = a >>> 32;
                long bHi = b >>> 32;
                long aLo = a & 4294967295L;
                long bLo = b & 4294967295L;
                long result = times2ToThe32Mod(aHi * bHi, m) + (aHi * bLo);
                if (result < 0) {
                    result = UnsignedLongs.remainder(result, m);
                }
                return plusMod(times2ToThe32Mod(result + (aLo * bHi), m), UnsignedLongs.remainder(aLo * bLo, m), m);
            }

            long squareMod(long a, long m) {
                long aHi = a >>> 32;
                long aLo = a & 4294967295L;
                long result = times2ToThe32Mod(aHi * aHi, m);
                long hiLo = (aHi * aLo) * 2;
                if (hiLo < 0) {
                    hiLo = UnsignedLongs.remainder(hiLo, m);
                }
                return plusMod(times2ToThe32Mod(result + hiLo, m), UnsignedLongs.remainder(aLo * aLo, m), m);
            }
        };

        abstract long mulMod(long j, long j2, long j3);

        abstract long squareMod(long j, long j2);

        static boolean test(long base, long n) {
            return (n <= LongMath.FLOOR_SQRT_MAX_LONG ? SMALL : LARGE).testWitness(base, n);
        }

        private long powMod(long a, long p, long m) {
            long res = 1;
            while (p != 0) {
                if ((1 & p) != 0) {
                    res = mulMod(res, a, m);
                }
                a = squareMod(a, m);
                p >>= 1;
            }
            return res;
        }

        private boolean testWitness(long base, long n) {
            int r = Long.numberOfTrailingZeros(n - 1);
            long d = (n - 1) >> r;
            base %= n;
            if (base == 0) {
                return true;
            }
            long a = powMod(base, d, n);
            if (a == 1) {
                return true;
            }
            int j = 0;
            while (a != n - 1) {
                j++;
                if (j == r) {
                    return false;
                }
                a = squareMod(a, n);
            }
            return true;
        }
    }

    @Beta
    public static long ceilingPowerOfTwo(long x) {
        MathPreconditions.checkPositive("x", x);
        if (x <= 4611686018427387904L) {
            return 1 << (-Long.numberOfLeadingZeros(x - 1));
        }
        throw new ArithmeticException("ceilingPowerOfTwo(" + x + ") is not representable as a long");
    }

    @Beta
    public static long floorPowerOfTwo(long x) {
        MathPreconditions.checkPositive("x", x);
        return 1 << (63 - Long.numberOfLeadingZeros(x));
    }

    public static boolean isPowerOfTwo(long x) {
        int i = 1;
        int i2 = x > 0 ? 1 : 0;
        if (((x - 1) & x) != 0) {
            i = 0;
        }
        return i & i2;
    }

    @VisibleForTesting
    static int lessThanBranchFree(long x, long y) {
        return (int) ((((x - y) ^ -1) ^ -1) >>> 63);
    }

    public static int log2(long x, RoundingMode mode) {
        MathPreconditions.checkPositive("x", x);
        switch (C07581.$SwitchMap$java$math$RoundingMode[mode.ordinal()]) {
            case 1:
                MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(x));
                break;
            case 2:
            case 3:
                break;
            case 4:
            case 5:
                return 64 - Long.numberOfLeadingZeros(x - 1);
            case 6:
            case 7:
            case 8:
                int leadingZeros = Long.numberOfLeadingZeros(x);
                return lessThanBranchFree(MAX_POWER_OF_SQRT2_UNSIGNED >>> leadingZeros, x) + (63 - leadingZeros);
            default:
                throw new AssertionError("impossible");
        }
        return 63 - Long.numberOfLeadingZeros(x);
    }

    @GwtIncompatible
    public static int log10(long x, RoundingMode mode) {
        MathPreconditions.checkPositive("x", x);
        int logFloor = log10Floor(x);
        long floorPow = powersOf10[logFloor];
        switch (C07581.$SwitchMap$java$math$RoundingMode[mode.ordinal()]) {
            case 1:
                boolean z;
                if (x == floorPow) {
                    z = true;
                } else {
                    z = false;
                }
                MathPreconditions.checkRoundingUnnecessary(z);
                return logFloor;
            case 2:
            case 3:
                return logFloor;
            case 4:
            case 5:
                return logFloor + lessThanBranchFree(floorPow, x);
            case 6:
            case 7:
            case 8:
                return logFloor + lessThanBranchFree(halfPowersOf10[logFloor], x);
            default:
                throw new AssertionError();
        }
    }

    @GwtIncompatible
    static int log10Floor(long x) {
        int y = maxLog10ForLeadingZeros[Long.numberOfLeadingZeros(x)];
        return y - lessThanBranchFree(x, powersOf10[y]);
    }

    @GwtIncompatible
    public static long pow(long b, int k) {
        long j = 0;
        MathPreconditions.checkNonNegative("exponent", k);
        if (-2 > b || b > 2) {
            long accum = 1;
            while (true) {
                switch (k) {
                    case 0:
                        return accum;
                    case 1:
                        return accum * b;
                    default:
                        if ((k & 1) == 0) {
                            j = 1;
                        } else {
                            j = b;
                        }
                        accum *= j;
                        b *= b;
                        k >>= 1;
                }
            }
        } else {
            switch ((int) b) {
                case -2:
                    if (k < 64) {
                        return (k & 1) == 0 ? 1 << k : -(1 << k);
                    } else {
                        return 0;
                    }
                case -1:
                    if ((k & 1) != 0) {
                        return -1;
                    }
                    return 1;
                case 0:
                    if (k == 0) {
                        return 1;
                    }
                    return 0;
                case 1:
                    return 1;
                case 2:
                    if (k < 64) {
                        j = 1 << k;
                    }
                    return j;
                default:
                    throw new AssertionError();
            }
        }
    }

    @GwtIncompatible
    public static long sqrt(long x, RoundingMode mode) {
        MathPreconditions.checkNonNegative("x", x);
        if (fitsInInt(x)) {
            return (long) IntMath.sqrt((int) x, mode);
        }
        long guess = (long) Math.sqrt((double) x);
        long guessSquared = guess * guess;
        switch (C07581.$SwitchMap$java$math$RoundingMode[mode.ordinal()]) {
            case 1:
                MathPreconditions.checkRoundingUnnecessary(guessSquared == x);
                return guess;
            case 2:
            case 3:
                if (x < guessSquared) {
                    return guess - 1;
                }
                return guess;
            case 4:
            case 5:
                if (x > guessSquared) {
                    return guess + 1;
                }
                return guess;
            case 6:
            case 7:
            case 8:
                long sqrtFloor = guess - ((long) (x < guessSquared ? 1 : 0));
                return sqrtFloor + ((long) lessThanBranchFree((sqrtFloor * sqrtFloor) + sqrtFloor, x));
            default:
                throw new AssertionError();
        }
    }

    @GwtIncompatible
    public static long divide(long p, long q, RoundingMode mode) {
        Preconditions.checkNotNull(mode);
        long div = p / q;
        long rem = p - (q * div);
        if (rem == 0) {
            return div;
        }
        boolean increment;
        int signum = ((int) ((p ^ q) >> 63)) | 1;
        switch (C07581.$SwitchMap$java$math$RoundingMode[mode.ordinal()]) {
            case 1:
                MathPreconditions.checkRoundingUnnecessary(rem == 0);
                break;
            case 2:
                break;
            case 3:
                increment = signum < 0;
                break;
            case 4:
                increment = true;
                break;
            case 5:
                increment = signum > 0;
                break;
            case 6:
            case 7:
            case 8:
                long absRem = Math.abs(rem);
                long cmpRemToHalfDivisor = absRem - (Math.abs(q) - absRem);
                if (cmpRemToHalfDivisor != 0) {
                    increment = cmpRemToHalfDivisor > 0;
                    break;
                }
                increment = (mode == RoundingMode.HALF_UP ? 1 : 0) | (((1 & div) != 0 ? 1 : 0) & (mode == RoundingMode.HALF_EVEN ? 1 : 0));
                break;
            default:
                throw new AssertionError();
        }
        increment = false;
        if (increment) {
            return div + ((long) signum);
        }
        return div;
    }

    @GwtIncompatible
    public static int mod(long x, int m) {
        return (int) mod(x, (long) m);
    }

    @GwtIncompatible
    public static long mod(long x, long m) {
        if (m <= 0) {
            throw new ArithmeticException("Modulus must be positive");
        }
        long result = x % m;
        return result >= 0 ? result : result + m;
    }

    public static long gcd(long a, long b) {
        MathPreconditions.checkNonNegative(HtmlTags.f32A, a);
        MathPreconditions.checkNonNegative(HtmlTags.f33B, b);
        if (a == 0) {
            return b;
        }
        if (b == 0) {
            return a;
        }
        int aTwos = Long.numberOfTrailingZeros(a);
        a >>= aTwos;
        int bTwos = Long.numberOfTrailingZeros(b);
        b >>= bTwos;
        while (a != b) {
            long delta = a - b;
            long minDeltaOrZero = delta & (delta >> 63);
            a = (delta - minDeltaOrZero) - minDeltaOrZero;
            b += minDeltaOrZero;
            a >>= Long.numberOfTrailingZeros(a);
        }
        return a << Math.min(aTwos, bTwos);
    }

    @GwtIncompatible
    public static long checkedAdd(long a, long b) {
        int i;
        int i2 = 1;
        long result = a + b;
        if ((a ^ b) < 0) {
            i = 1;
        } else {
            i = 0;
        }
        if ((a ^ result) < 0) {
            i2 = 0;
        }
        MathPreconditions.checkNoOverflow(i2 | i);
        return result;
    }

    @GwtIncompatible
    public static long checkedSubtract(long a, long b) {
        int i;
        int i2 = 1;
        long result = a - b;
        if ((a ^ b) >= 0) {
            i = 1;
        } else {
            i = 0;
        }
        if ((a ^ result) < 0) {
            i2 = 0;
        }
        MathPreconditions.checkNoOverflow(i2 | i);
        return result;
    }

    @GwtIncompatible
    public static long checkedMultiply(long a, long b) {
        boolean z = false;
        int leadingZeros = ((Long.numberOfLeadingZeros(a) + Long.numberOfLeadingZeros(a ^ -1)) + Long.numberOfLeadingZeros(b)) + Long.numberOfLeadingZeros(b ^ -1);
        if (leadingZeros > 65) {
            return a * b;
        }
        boolean z2;
        int i;
        if (leadingZeros >= 64) {
            z2 = true;
        } else {
            z2 = false;
        }
        MathPreconditions.checkNoOverflow(z2);
        if (a >= 0) {
            i = 1;
        } else {
            i = 0;
        }
        MathPreconditions.checkNoOverflow((b != Long.MIN_VALUE ? 1 : 0) | i);
        long result = a * b;
        if (a == 0 || result / a == b) {
            z = true;
        }
        MathPreconditions.checkNoOverflow(z);
        return result;
    }

    @GwtIncompatible
    public static long checkedPow(long b, int k) {
        boolean z = true;
        MathPreconditions.checkNonNegative("exponent", k);
        if (((b <= 2 ? 1 : 0) & (b >= -2 ? 1 : 0)) == 0) {
            long accum = 1;
            while (true) {
                switch (k) {
                    case 0:
                        return accum;
                    case 1:
                        return checkedMultiply(accum, b);
                    default:
                        if ((k & 1) != 0) {
                            accum = checkedMultiply(accum, b);
                        }
                        k >>= 1;
                        if (k > 0) {
                            boolean z2;
                            if (-3037000499L > b || b > FLOOR_SQRT_MAX_LONG) {
                                z2 = false;
                            } else {
                                z2 = true;
                            }
                            MathPreconditions.checkNoOverflow(z2);
                            b *= b;
                        }
                        break;
                }
            }
        }
        switch ((int) b) {
            case -2:
                if (k >= 64) {
                    z = false;
                }
                MathPreconditions.checkNoOverflow(z);
                return (k & 1) == 0 ? 1 << k : -1 << k;
            case -1:
                if ((k & 1) != 0) {
                    return -1;
                }
                return 1;
            case 0:
                if (k == 0) {
                    return 1;
                }
                return 0;
            case 1:
                return 1;
            case 2:
                boolean z3;
                if (k < 63) {
                    z3 = true;
                } else {
                    z3 = false;
                }
                MathPreconditions.checkNoOverflow(z3);
                return 1 << k;
            default:
                throw new AssertionError();
        }
    }

    @Beta
    public static long saturatedAdd(long a, long b) {
        int i;
        int i2 = 1;
        long naiveSum = a + b;
        if ((a ^ b) < 0) {
            i = 1;
        } else {
            i = 0;
        }
        if ((a ^ naiveSum) < 0) {
            i2 = 0;
        }
        return (i2 | i) != 0 ? naiveSum : Long.MAX_VALUE + ((naiveSum >>> 63) ^ 1);
    }

    @Beta
    public static long saturatedSubtract(long a, long b) {
        int i;
        int i2 = 1;
        long naiveDifference = a - b;
        if ((a ^ b) >= 0) {
            i = 1;
        } else {
            i = 0;
        }
        if ((a ^ naiveDifference) < 0) {
            i2 = 0;
        }
        return (i2 | i) != 0 ? naiveDifference : Long.MAX_VALUE + ((naiveDifference >>> 63) ^ 1);
    }

    @Beta
    public static long saturatedMultiply(long a, long b) {
        int leadingZeros = ((Long.numberOfLeadingZeros(a) + Long.numberOfLeadingZeros(-1 ^ a)) + Long.numberOfLeadingZeros(b)) + Long.numberOfLeadingZeros(-1 ^ b);
        if (leadingZeros > 65) {
            return a * b;
        }
        long limit = Long.MAX_VALUE + ((a ^ b) >>> 63);
        if ((((b == Long.MIN_VALUE ? 1 : 0) & (a < 0 ? 1 : 0)) | (leadingZeros < 64 ? 1 : 0)) != 0) {
            return limit;
        }
        long result = a * b;
        if (a == 0 || result / a == b) {
            return result;
        }
        return limit;
    }

    @Beta
    public static long saturatedPow(long b, int k) {
        MathPreconditions.checkNonNegative("exponent", k);
        if (((b <= 2 ? 1 : 0) & (b >= -2 ? 1 : 0)) != 0) {
            switch ((int) b) {
                case -2:
                    if (k >= 64) {
                        return ((long) (k & 1)) + Long.MAX_VALUE;
                    }
                    return (k & 1) == 0 ? 1 << k : -1 << k;
                case -1:
                    if ((k & 1) != 0) {
                        return -1;
                    }
                    return 1;
                case 0:
                    if (k == 0) {
                        return 1;
                    }
                    return 0;
                case 1:
                    return 1;
                case 2:
                    if (k >= 63) {
                        return Long.MAX_VALUE;
                    }
                    return 1 << k;
                default:
                    throw new AssertionError();
            }
        }
        long accum = 1;
        long limit = Long.MAX_VALUE + ((b >>> 63) & ((long) (k & 1)));
        while (true) {
            switch (k) {
                case 0:
                    return accum;
                case 1:
                    return saturatedMultiply(accum, b);
                default:
                    if ((k & 1) != 0) {
                        accum = saturatedMultiply(accum, b);
                    }
                    k >>= 1;
                    if (k > 0) {
                        int i;
                        if (-3037000499L > b) {
                            i = 1;
                        } else {
                            i = 0;
                        }
                        if (((b > FLOOR_SQRT_MAX_LONG ? 1 : 0) | i) != 0) {
                            return limit;
                        }
                        b *= b;
                    }
            }
        }
    }

    @GwtIncompatible
    public static long factorial(int n) {
        MathPreconditions.checkNonNegative("n", n);
        return n < factorials.length ? factorials[n] : Long.MAX_VALUE;
    }

    public static long binomial(int n, int k) {
        MathPreconditions.checkNonNegative("n", n);
        MathPreconditions.checkNonNegative("k", k);
        Preconditions.checkArgument(k <= n, "k (%s) > n (%s)", k, n);
        if (k > (n >> 1)) {
            k = n - k;
        }
        switch (k) {
            case 0:
                return 1;
            case 1:
                return (long) n;
            default:
                if (n < factorials.length) {
                    return factorials[n] / (factorials[k] * factorials[n - k]);
                }
                if (k >= biggestBinomials.length || n > biggestBinomials[k]) {
                    return Long.MAX_VALUE;
                }
                long result;
                int i;
                if (k >= biggestSimpleBinomials.length || n > biggestSimpleBinomials[k]) {
                    int nBits = log2((long) n, RoundingMode.CEILING);
                    result = 1;
                    long numerator = (long) n;
                    long denominator = 1;
                    int numeratorBits = nBits;
                    i = 2;
                    n--;
                    while (i <= k) {
                        if (numeratorBits + nBits < 63) {
                            numerator *= (long) n;
                            denominator *= (long) i;
                            numeratorBits += nBits;
                        } else {
                            result = multiplyFraction(result, numerator, denominator);
                            numerator = (long) n;
                            denominator = (long) i;
                            numeratorBits = nBits;
                        }
                        i++;
                        n--;
                    }
                    return multiplyFraction(result, numerator, denominator);
                }
                result = (long) n;
                n--;
                for (i = 2; i <= k; i++) {
                    result = (result * ((long) n)) / ((long) i);
                    n--;
                }
                return result;
        }
    }

    static long multiplyFraction(long x, long numerator, long denominator) {
        if (x == 1) {
            return numerator / denominator;
        }
        long commonDivisor = gcd(x, denominator);
        return (numerator / (denominator / commonDivisor)) * (x / commonDivisor);
    }

    static boolean fitsInInt(long x) {
        return ((long) ((int) x)) == x;
    }

    public static long mean(long x, long y) {
        return (x & y) + ((x ^ y) >> 1);
    }

    @GwtIncompatible
    @Beta
    public static boolean isPrime(long n) {
        if (n < 2) {
            MathPreconditions.checkNonNegative("n", n);
            return false;
        } else if (n == 2 || n == 3 || n == 5 || n == 7 || n == 11 || n == 13) {
            return true;
        } else {
            if ((SIEVE_30 & (1 << ((int) (n % 30)))) != 0 || n % 7 == 0 || n % 11 == 0 || n % 13 == 0) {
                return false;
            }
            if (n < 289) {
                return true;
            }
            for (long[] baseSet : millerRabinBaseSets) {
                if (n <= baseSet[0]) {
                    for (int i = 1; i < baseSet.length; i++) {
                        if (!MillerRabinTester.test(baseSet[i], n)) {
                            return false;
                        }
                    }
                    return true;
                }
            }
            throw new AssertionError();
        }
    }

    private LongMath() {
    }
}

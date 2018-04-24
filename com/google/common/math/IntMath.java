package com.google.common.math;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import com.itextpdf.text.html.HtmlTags;
import java.math.RoundingMode;
import org.apache.poi.hssf.usermodel.HSSFShapeTypes;
import org.bytedeco.javacpp.ARToolKitPlus;
import org.bytedeco.javacpp.avutil;
import org.bytedeco.javacpp.opencv_videoio;

@GwtCompatible(emulated = true)
public final class IntMath {
    @VisibleForTesting
    static final int FLOOR_SQRT_MAX_INT = 46340;
    @VisibleForTesting
    static final int MAX_POWER_OF_SQRT2_UNSIGNED = -1257966797;
    @VisibleForTesting
    static final int MAX_SIGNED_POWER_OF_TWO = 1073741824;
    @VisibleForTesting
    static int[] biggestBinomials = new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE, 65536, 2345, opencv_videoio.CV_CAP_PROP_XI_GAMMAC, HSSFShapeTypes.ActionButtonForwardNext, 110, 75, 58, 49, 43, 39, 37, 35, 34, 34, 33};
    private static final int[] factorials = new int[]{1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800, 39916800, 479001600};
    @VisibleForTesting
    static final int[] halfPowersOf10 = new int[]{3, 31, 316, 3162, 31622, 316227, 3162277, 31622776, 316227766, Integer.MAX_VALUE};
    @VisibleForTesting
    static final byte[] maxLog10ForLeadingZeros = new byte[]{(byte) 9, (byte) 9, (byte) 9, (byte) 8, (byte) 8, (byte) 8, (byte) 7, (byte) 7, (byte) 7, (byte) 6, (byte) 6, (byte) 6, (byte) 6, (byte) 5, (byte) 5, (byte) 5, (byte) 4, (byte) 4, (byte) 4, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 2, (byte) 2, (byte) 2, (byte) 1, (byte) 1, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 0};
    @VisibleForTesting
    static final int[] powersOf10 = new int[]{1, 10, 100, 1000, 10000, ARToolKitPlus.AR_AREA_MAX, avutil.AV_TIME_BASE, 10000000, 100000000, 1000000000};

    static /* synthetic */ class C07561 {
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

    @Beta
    public static int ceilingPowerOfTwo(int x) {
        MathPreconditions.checkPositive("x", x);
        if (x <= 1073741824) {
            return 1 << (-Integer.numberOfLeadingZeros(x - 1));
        }
        throw new ArithmeticException("ceilingPowerOfTwo(" + x + ") not representable as an int");
    }

    @Beta
    public static int floorPowerOfTwo(int x) {
        MathPreconditions.checkPositive("x", x);
        return Integer.highestOneBit(x);
    }

    public static boolean isPowerOfTwo(int x) {
        int i = 1;
        int i2 = x > 0 ? 1 : 0;
        if (((x - 1) & x) != 0) {
            i = 0;
        }
        return i & i2;
    }

    @VisibleForTesting
    static int lessThanBranchFree(int x, int y) {
        return (((x - y) ^ -1) ^ -1) >>> 31;
    }

    public static int log2(int x, RoundingMode mode) {
        MathPreconditions.checkPositive("x", x);
        switch (C07561.$SwitchMap$java$math$RoundingMode[mode.ordinal()]) {
            case 1:
                MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(x));
                break;
            case 2:
            case 3:
                break;
            case 4:
            case 5:
                return 32 - Integer.numberOfLeadingZeros(x - 1);
            case 6:
            case 7:
            case 8:
                int leadingZeros = Integer.numberOfLeadingZeros(x);
                return lessThanBranchFree(MAX_POWER_OF_SQRT2_UNSIGNED >>> leadingZeros, x) + (31 - leadingZeros);
            default:
                throw new AssertionError();
        }
        return 31 - Integer.numberOfLeadingZeros(x);
    }

    @GwtIncompatible
    public static int log10(int x, RoundingMode mode) {
        MathPreconditions.checkPositive("x", x);
        int logFloor = log10Floor(x);
        int floorPow = powersOf10[logFloor];
        switch (C07561.$SwitchMap$java$math$RoundingMode[mode.ordinal()]) {
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

    private static int log10Floor(int x) {
        int y = maxLog10ForLeadingZeros[Integer.numberOfLeadingZeros(x)];
        return y - lessThanBranchFree(x, powersOf10[y]);
    }

    @GwtIncompatible
    public static int pow(int b, int k) {
        int i = 0;
        MathPreconditions.checkNonNegative("exponent", k);
        switch (b) {
            case -2:
                if (k < 32) {
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
                if (k < 32) {
                    i = 1 << k;
                }
                return i;
            default:
                int accum = 1;
                while (true) {
                    switch (k) {
                        case 0:
                            return accum;
                        case 1:
                            return b * accum;
                        default:
                            if ((k & 1) == 0) {
                                i = 1;
                            } else {
                                i = b;
                            }
                            accum *= i;
                            b *= b;
                            k >>= 1;
                    }
                }
        }
    }

    @GwtIncompatible
    public static int sqrt(int x, RoundingMode mode) {
        MathPreconditions.checkNonNegative("x", x);
        int sqrtFloor = sqrtFloor(x);
        switch (C07561.$SwitchMap$java$math$RoundingMode[mode.ordinal()]) {
            case 1:
                boolean z;
                if (sqrtFloor * sqrtFloor == x) {
                    z = true;
                } else {
                    z = false;
                }
                MathPreconditions.checkRoundingUnnecessary(z);
                return sqrtFloor;
            case 2:
            case 3:
                return sqrtFloor;
            case 4:
            case 5:
                return sqrtFloor + lessThanBranchFree(sqrtFloor * sqrtFloor, x);
            case 6:
            case 7:
            case 8:
                return sqrtFloor + lessThanBranchFree((sqrtFloor * sqrtFloor) + sqrtFloor, x);
            default:
                throw new AssertionError();
        }
    }

    private static int sqrtFloor(int x) {
        return (int) Math.sqrt((double) x);
    }

    public static int divide(int p, int q, RoundingMode mode) {
        boolean z = true;
        Preconditions.checkNotNull(mode);
        if (q == 0) {
            throw new ArithmeticException("/ by zero");
        }
        int div = p / q;
        int rem = p - (q * div);
        if (rem == 0) {
            return div;
        }
        boolean increment;
        int signum = ((p ^ q) >> 31) | 1;
        switch (C07561.$SwitchMap$java$math$RoundingMode[mode.ordinal()]) {
            case 1:
                if (rem != 0) {
                    z = false;
                }
                MathPreconditions.checkRoundingUnnecessary(z);
                break;
            case 2:
                break;
            case 3:
                if (signum < 0) {
                    increment = true;
                } else {
                    increment = false;
                }
                break;
            case 4:
                increment = true;
                break;
            case 5:
                if (signum > 0) {
                    increment = true;
                } else {
                    increment = false;
                }
                break;
            case 6:
            case 7:
            case 8:
                int absRem = Math.abs(rem);
                int cmpRemToHalfDivisor = absRem - (Math.abs(q) - absRem);
                if (cmpRemToHalfDivisor != 0) {
                    if (cmpRemToHalfDivisor > 0) {
                        increment = true;
                    } else {
                        increment = false;
                    }
                    break;
                }
                if (mode != RoundingMode.HALF_UP) {
                    if ((((div & 1) != 0 ? 1 : 0) & (mode == RoundingMode.HALF_EVEN ? 1 : 0)) == 0) {
                        increment = false;
                        break;
                    }
                }
                increment = true;
            default:
                throw new AssertionError();
        }
        increment = false;
        return increment ? div + signum : div;
    }

    public static int mod(int x, int m) {
        if (m <= 0) {
            throw new ArithmeticException("Modulus " + m + " must be > 0");
        }
        int result = x % m;
        return result >= 0 ? result : result + m;
    }

    public static int gcd(int a, int b) {
        MathPreconditions.checkNonNegative(HtmlTags.f32A, a);
        MathPreconditions.checkNonNegative(HtmlTags.f33B, b);
        if (a == 0) {
            return b;
        }
        if (b == 0) {
            return a;
        }
        int aTwos = Integer.numberOfTrailingZeros(a);
        a >>= aTwos;
        int bTwos = Integer.numberOfTrailingZeros(b);
        b >>= bTwos;
        while (a != b) {
            int delta = a - b;
            int minDeltaOrZero = delta & (delta >> 31);
            a = (delta - minDeltaOrZero) - minDeltaOrZero;
            b += minDeltaOrZero;
            a >>= Integer.numberOfTrailingZeros(a);
        }
        return a << Math.min(aTwos, bTwos);
    }

    public static int checkedAdd(int a, int b) {
        long result = ((long) a) + ((long) b);
        MathPreconditions.checkNoOverflow(result == ((long) ((int) result)));
        return (int) result;
    }

    public static int checkedSubtract(int a, int b) {
        long result = ((long) a) - ((long) b);
        MathPreconditions.checkNoOverflow(result == ((long) ((int) result)));
        return (int) result;
    }

    public static int checkedMultiply(int a, int b) {
        long result = ((long) a) * ((long) b);
        MathPreconditions.checkNoOverflow(result == ((long) ((int) result)));
        return (int) result;
    }

    public static int checkedPow(int b, int k) {
        boolean z = false;
        MathPreconditions.checkNonNegative("exponent", k);
        switch (b) {
            case -2:
                if (k < 32) {
                    z = true;
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
                if (k < 31) {
                    z = true;
                }
                MathPreconditions.checkNoOverflow(z);
                return 1 << k;
            default:
                int accum = 1;
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
                                int i;
                                int i2;
                                if (-46340 <= b) {
                                    i = 1;
                                } else {
                                    i = 0;
                                }
                                if (b <= FLOOR_SQRT_MAX_INT) {
                                    i2 = 1;
                                } else {
                                    i2 = 0;
                                }
                                MathPreconditions.checkNoOverflow(i2 & i);
                                b *= b;
                            }
                    }
                }
        }
    }

    @Beta
    public static int saturatedAdd(int a, int b) {
        return Ints.saturatedCast(((long) a) + ((long) b));
    }

    @Beta
    public static int saturatedSubtract(int a, int b) {
        return Ints.saturatedCast(((long) a) - ((long) b));
    }

    @Beta
    public static int saturatedMultiply(int a, int b) {
        return Ints.saturatedCast(((long) a) * ((long) b));
    }

    @Beta
    public static int saturatedPow(int b, int k) {
        MathPreconditions.checkNonNegative("exponent", k);
        switch (b) {
            case -2:
                if (k >= 32) {
                    return (k & 1) + Integer.MAX_VALUE;
                }
                return (k & 1) == 0 ? 1 << k : -1 << k;
            case -1:
                if ((k & 1) != 0) {
                    return -1;
                }
                return 1;
            case 0:
                if (k != 0) {
                    return 0;
                }
                return 1;
            case 1:
                return 1;
            case 2:
                if (k >= 31) {
                    return Integer.MAX_VALUE;
                }
                return 1 << k;
            default:
                int accum = 1;
                int limit = Integer.MAX_VALUE + ((b >>> 31) & (k & 1));
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
                                if (-46340 > b) {
                                    i = 1;
                                } else {
                                    i = 0;
                                }
                                if (((b > FLOOR_SQRT_MAX_INT ? 1 : 0) | i) != 0) {
                                    return limit;
                                }
                                b *= b;
                            }
                    }
                }
        }
    }

    public static int factorial(int n) {
        MathPreconditions.checkNonNegative("n", n);
        return n < factorials.length ? factorials[n] : Integer.MAX_VALUE;
    }

    @GwtIncompatible
    public static int binomial(int n, int k) {
        MathPreconditions.checkNonNegative("n", n);
        MathPreconditions.checkNonNegative("k", k);
        Preconditions.checkArgument(k <= n, "k (%s) > n (%s)", k, n);
        if (k > (n >> 1)) {
            k = n - k;
        }
        if (k >= biggestBinomials.length || n > biggestBinomials[k]) {
            return Integer.MAX_VALUE;
        }
        switch (k) {
            case 0:
                return 1;
            case 1:
                return n;
            default:
                long result = 1;
                for (int i = 0; i < k; i++) {
                    result = (result * ((long) (n - i))) / ((long) (i + 1));
                }
                return (int) result;
        }
    }

    public static int mean(int x, int y) {
        return (x & y) + ((x ^ y) >> 1);
    }

    @GwtIncompatible
    @Beta
    public static boolean isPrime(int n) {
        return LongMath.isPrime((long) n);
    }

    private IntMath() {
    }
}

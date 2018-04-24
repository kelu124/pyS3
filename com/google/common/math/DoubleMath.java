package com.google.common.math;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Booleans;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Iterator;

@GwtCompatible(emulated = true)
public final class DoubleMath {
    private static final double LN_2 = Math.log(2.0d);
    @VisibleForTesting
    static final int MAX_FACTORIAL = 170;
    private static final double MAX_INT_AS_DOUBLE = 2.147483647E9d;
    private static final double MAX_LONG_AS_DOUBLE_PLUS_ONE = 9.223372036854776E18d;
    private static final double MIN_INT_AS_DOUBLE = -2.147483648E9d;
    private static final double MIN_LONG_AS_DOUBLE = -9.223372036854776E18d;
    @VisibleForTesting
    static final double[] everySixteenthFactorial = new double[]{1.0d, 2.0922789888E13d, 2.631308369336935E35d, 1.2413915592536073E61d, 1.2688693218588417E89d, 7.156945704626381E118d, 9.916779348709496E149d, 1.974506857221074E182d, 3.856204823625804E215d, 5.5502938327393044E249d, 4.7147236359920616E284d};

    static /* synthetic */ class C07551 {
        static final /* synthetic */ int[] $SwitchMap$java$math$RoundingMode = new int[RoundingMode.values().length];

        static {
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.UNNECESSARY.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.FLOOR.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.CEILING.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.DOWN.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.UP.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_EVEN.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_UP.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_DOWN.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
        }
    }

    @GwtIncompatible
    static double roundIntermediate(double x, RoundingMode mode) {
        if (DoubleUtils.isFinite(x)) {
            double z;
            switch (C07551.$SwitchMap$java$math$RoundingMode[mode.ordinal()]) {
                case 1:
                    MathPreconditions.checkRoundingUnnecessary(isMathematicalInteger(x));
                    return x;
                case 2:
                    if (x >= 0.0d || isMathematicalInteger(x)) {
                        return x;
                    }
                    return (double) (((long) x) - 1);
                case 3:
                    if (x <= 0.0d || isMathematicalInteger(x)) {
                        return x;
                    }
                    return (double) (((long) x) + 1);
                case 4:
                    return x;
                case 5:
                    if (isMathematicalInteger(x)) {
                        return x;
                    }
                    return (double) (((long) (x > 0.0d ? 1 : -1)) + ((long) x));
                case 6:
                    return Math.rint(x);
                case 7:
                    z = Math.rint(x);
                    return Math.abs(x - z) == 0.5d ? x + Math.copySign(0.5d, x) : z;
                case 8:
                    z = Math.rint(x);
                    if (Math.abs(x - z) != 0.5d) {
                        return z;
                    }
                    return x;
                default:
                    throw new AssertionError();
            }
        }
        throw new ArithmeticException("input is infinite or NaN");
    }

    @GwtIncompatible
    public static int roundToInt(double x, RoundingMode mode) {
        int i;
        int i2 = 1;
        double z = roundIntermediate(x, mode);
        if (z > -2.147483649E9d) {
            i = 1;
        } else {
            i = 0;
        }
        if (z >= 2.147483648E9d) {
            i2 = 0;
        }
        MathPreconditions.checkInRange(i2 & i);
        return (int) z;
    }

    @GwtIncompatible
    public static long roundToLong(double x, RoundingMode mode) {
        int i;
        int i2 = 1;
        double z = roundIntermediate(x, mode);
        if (MIN_LONG_AS_DOUBLE - z < 1.0d) {
            i = 1;
        } else {
            i = 0;
        }
        if (z >= MAX_LONG_AS_DOUBLE_PLUS_ONE) {
            i2 = 0;
        }
        MathPreconditions.checkInRange(i2 & i);
        return (long) z;
    }

    @GwtIncompatible
    public static BigInteger roundToBigInteger(double x, RoundingMode mode) {
        int i = 1;
        x = roundIntermediate(x, mode);
        int i2 = MIN_LONG_AS_DOUBLE - x < 1.0d ? 1 : 0;
        if (x >= MAX_LONG_AS_DOUBLE_PLUS_ONE) {
            i = 0;
        }
        if ((i & i2) != 0) {
            return BigInteger.valueOf((long) x);
        }
        BigInteger result = BigInteger.valueOf(DoubleUtils.getSignificand(x)).shiftLeft(Math.getExponent(x) - 52);
        return x < 0.0d ? result.negate() : result;
    }

    @GwtIncompatible
    public static boolean isPowerOfTwo(double x) {
        return x > 0.0d && DoubleUtils.isFinite(x) && LongMath.isPowerOfTwo(DoubleUtils.getSignificand(x));
    }

    public static double log2(double x) {
        return Math.log(x) / LN_2;
    }

    @GwtIncompatible
    public static int log2(double x, RoundingMode mode) {
        int i = 1;
        boolean z = x > 0.0d && DoubleUtils.isFinite(x);
        Preconditions.checkArgument(z, "x must be positive and finite");
        int exponent = Math.getExponent(x);
        if (!DoubleUtils.isNormal(x)) {
            return log2(4.503599627370496E15d * x, mode) - 52;
        }
        boolean increment;
        int i2;
        switch (C07551.$SwitchMap$java$math$RoundingMode[mode.ordinal()]) {
            case 1:
                MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(x));
                break;
            case 2:
                break;
            case 3:
                if (isPowerOfTwo(x)) {
                    increment = false;
                } else {
                    increment = true;
                }
                break;
            case 4:
                if (exponent < 0) {
                    i2 = 1;
                } else {
                    i2 = 0;
                }
                if (isPowerOfTwo(x)) {
                    i = 0;
                }
                increment = i2 & i;
                break;
            case 5:
                if (exponent >= 0) {
                    i2 = 1;
                } else {
                    i2 = 0;
                }
                if (isPowerOfTwo(x)) {
                    i = 0;
                }
                increment = i2 & i;
                break;
            case 6:
            case 7:
            case 8:
                double xScaled = DoubleUtils.scaleNormalize(x);
                if (xScaled * xScaled > 2.0d) {
                    increment = true;
                } else {
                    increment = false;
                }
                break;
            default:
                throw new AssertionError();
        }
        increment = false;
        if (increment) {
            return exponent + 1;
        }
        return exponent;
    }

    @GwtIncompatible
    public static boolean isMathematicalInteger(double x) {
        return DoubleUtils.isFinite(x) && (x == 0.0d || 52 - Long.numberOfTrailingZeros(DoubleUtils.getSignificand(x)) <= Math.getExponent(x));
    }

    public static double factorial(int n) {
        MathPreconditions.checkNonNegative("n", n);
        if (n > 170) {
            return Double.POSITIVE_INFINITY;
        }
        double accum = 1.0d;
        for (int i = (n & -16) + 1; i <= n; i++) {
            accum *= (double) i;
        }
        return everySixteenthFactorial[n >> 4] * accum;
    }

    public static boolean fuzzyEquals(double a, double b, double tolerance) {
        MathPreconditions.checkNonNegative("tolerance", tolerance);
        return Math.copySign(a - b, 1.0d) <= tolerance || a == b || (Double.isNaN(a) && Double.isNaN(b));
    }

    public static int fuzzyCompare(double a, double b, double tolerance) {
        if (fuzzyEquals(a, b, tolerance)) {
            return 0;
        }
        if (a < b) {
            return -1;
        }
        if (a > b) {
            return 1;
        }
        return Booleans.compare(Double.isNaN(a), Double.isNaN(b));
    }

    @GwtIncompatible
    @Deprecated
    public static double mean(double... values) {
        boolean z;
        if (values.length > 0) {
            z = true;
        } else {
            z = false;
        }
        Preconditions.checkArgument(z, "Cannot take mean of 0 values");
        long count = 1;
        double mean = checkFinite(values[0]);
        for (int index = 1; index < values.length; index++) {
            checkFinite(values[index]);
            count++;
            mean += (values[index] - mean) / ((double) count);
        }
        return mean;
    }

    @Deprecated
    public static double mean(int... values) {
        Preconditions.checkArgument(values.length > 0, "Cannot take mean of 0 values");
        long sum = 0;
        for (int i : values) {
            sum += (long) i;
        }
        return ((double) sum) / ((double) values.length);
    }

    @Deprecated
    public static double mean(long... values) {
        boolean z;
        if (values.length > 0) {
            z = true;
        } else {
            z = false;
        }
        Preconditions.checkArgument(z, "Cannot take mean of 0 values");
        long count = 1;
        double mean = (double) values[0];
        for (int index = 1; index < values.length; index++) {
            count++;
            mean += (((double) values[index]) - mean) / ((double) count);
        }
        return mean;
    }

    @GwtIncompatible
    @Deprecated
    public static double mean(Iterable<? extends Number> values) {
        return mean(values.iterator());
    }

    @GwtIncompatible
    @Deprecated
    public static double mean(Iterator<? extends Number> values) {
        Preconditions.checkArgument(values.hasNext(), "Cannot take mean of 0 values");
        long count = 1;
        double mean = checkFinite(((Number) values.next()).doubleValue());
        while (values.hasNext()) {
            count++;
            mean += (checkFinite(((Number) values.next()).doubleValue()) - mean) / ((double) count);
        }
        return mean;
    }

    @GwtIncompatible
    @CanIgnoreReturnValue
    private static double checkFinite(double argument) {
        Preconditions.checkArgument(DoubleUtils.isFinite(argument));
        return argument;
    }

    private DoubleMath() {
    }
}

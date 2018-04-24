package org.apache.poi.ss.formula.functions;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.bytedeco.javacpp.avutil;

final class MathX {
    private MathX() {
    }

    public static double round(double n, int p) {
        if (Double.isNaN(n) || Double.isInfinite(n)) {
            return Double.NaN;
        }
        return new BigDecimal(NumberToTextConverter.toText(n)).setScale(p, RoundingMode.HALF_UP).doubleValue();
    }

    public static double roundUp(double n, int p) {
        if (Double.isNaN(n) || Double.isInfinite(n)) {
            return Double.NaN;
        }
        return BigDecimal.valueOf(n).setScale(p, RoundingMode.UP).doubleValue();
    }

    public static double roundDown(double n, int p) {
        if (Double.isNaN(n) || Double.isInfinite(n)) {
            return Double.NaN;
        }
        return BigDecimal.valueOf(n).setScale(p, RoundingMode.DOWN).doubleValue();
    }

    public static short sign(double d) {
        int i = d == 0.0d ? 0 : d < 0.0d ? -1 : 1;
        return (short) i;
    }

    public static double average(double[] values) {
        double sum = 0.0d;
        for (double d : values) {
            sum += d;
        }
        return sum / ((double) values.length);
    }

    public static double sum(double[] values) {
        double sum = 0.0d;
        for (double d : values) {
            sum += d;
        }
        return sum;
    }

    public static double sumsq(double[] values) {
        double sumsq = 0.0d;
        for (int i = 0; i < values.length; i++) {
            sumsq += values[i] * values[i];
        }
        return sumsq;
    }

    public static double product(double[] values) {
        double product = 0.0d;
        if (values != null && values.length > 0) {
            product = 1.0d;
            for (double d : values) {
                product *= d;
            }
        }
        return product;
    }

    public static double min(double[] values) {
        double min = Double.POSITIVE_INFINITY;
        for (double min2 : values) {
            min = Math.min(min, min2);
        }
        return min;
    }

    public static double max(double[] values) {
        double max = Double.NEGATIVE_INFINITY;
        for (double max2 : values) {
            max = Math.max(max, max2);
        }
        return max;
    }

    public static double floor(double n, double s) {
        double f = 0.0d;
        if ((n < 0.0d && s > 0.0d) || ((n > 0.0d && s < 0.0d) || (s == 0.0d && n != 0.0d))) {
            return Double.NaN;
        }
        if (!(n == 0.0d || s == 0.0d)) {
            f = Math.floor(n / s) * s;
        }
        return f;
    }

    public static double ceiling(double n, double s) {
        double c = 0.0d;
        if ((n < 0.0d && s > 0.0d) || (n > 0.0d && s < 0.0d)) {
            return Double.NaN;
        }
        if (!(n == 0.0d || s == 0.0d)) {
            c = Math.ceil(n / s) * s;
        }
        return c;
    }

    public static double factorial(int n) {
        double d = 1.0d;
        if (n < 0) {
            return Double.NaN;
        }
        if (n > 170) {
            return Double.POSITIVE_INFINITY;
        }
        for (int i = 1; i <= n; i++) {
            d *= (double) i;
        }
        return d;
    }

    public static double mod(double n, double d) {
        if (d == 0.0d) {
            return Double.NaN;
        }
        if (sign(n) == sign(d)) {
            return n % d;
        }
        return ((n % d) + d) % d;
    }

    public static double acosh(double d) {
        return Math.log(Math.sqrt(Math.pow(d, 2.0d) - 1.0d) + d);
    }

    public static double asinh(double d) {
        return Math.log(Math.sqrt((d * d) + 1.0d) + d);
    }

    public static double atanh(double d) {
        return Math.log((1.0d + d) / (1.0d - d)) / 2.0d;
    }

    public static double cosh(double d) {
        return (Math.pow(avutil.M_E, d) + Math.pow(avutil.M_E, -d)) / 2.0d;
    }

    public static double sinh(double d) {
        return (Math.pow(avutil.M_E, d) - Math.pow(avutil.M_E, -d)) / 2.0d;
    }

    public static double tanh(double d) {
        double ePowX = Math.pow(avutil.M_E, d);
        double ePowNegX = Math.pow(avutil.M_E, -d);
        return (ePowX - ePowNegX) / (ePowX + ePowNegX);
    }

    public static double nChooseK(int n, int k) {
        double d = 1.0d;
        if (n < 0 || k < 0 || n < k) {
            return Double.NaN;
        }
        int minnk = Math.min(n - k, k);
        for (int i = Math.max(n - k, k); i < n; i++) {
            d *= (double) (i + 1);
        }
        return d / factorial(minnk);
    }
}

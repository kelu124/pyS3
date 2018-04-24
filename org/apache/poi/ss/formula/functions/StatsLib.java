package org.apache.poi.ss.formula.functions;

import java.util.Arrays;

final class StatsLib {
    private StatsLib() {
    }

    public static double avedev(double[] v) {
        double s = 0.0d;
        for (double d : v) {
            s += d;
        }
        double m = s / ((double) v.length);
        s = 0.0d;
        for (double d2 : v) {
            s += Math.abs(d2 - m);
        }
        return s / ((double) v.length);
    }

    public static double stdev(double[] v) {
        if (v == null || v.length <= 1) {
            return Double.NaN;
        }
        return Math.sqrt(devsq(v) / ((double) (v.length - 1)));
    }

    public static double var(double[] v) {
        if (v == null || v.length <= 1) {
            return Double.NaN;
        }
        return devsq(v) / ((double) (v.length - 1));
    }

    public static double varp(double[] v) {
        if (v == null || v.length <= 1) {
            return Double.NaN;
        }
        return devsq(v) / ((double) v.length);
    }

    public static double median(double[] v) {
        if (v == null || v.length < 1) {
            return Double.NaN;
        }
        int n = v.length;
        Arrays.sort(v);
        return n % 2 == 0 ? (v[n / 2] + v[(n / 2) - 1]) / 2.0d : v[n / 2];
    }

    public static double devsq(double[] v) {
        if (v == null || v.length < 1) {
            return Double.NaN;
        }
        int i;
        double s = 0.0d;
        for (double d : v) {
            s += d;
        }
        double m = s / ((double) n);
        s = 0.0d;
        for (i = 0; i < n; i++) {
            s += (v[i] - m) * (v[i] - m);
        }
        if (n == 1) {
            return 0.0d;
        }
        return s;
    }

    public static double kthLargest(double[] v, int k) {
        int index = k - 1;
        if (v == null || v.length <= index || index < 0) {
            return Double.NaN;
        }
        Arrays.sort(v);
        return v[(v.length - index) - 1];
    }

    public static double kthSmallest(double[] v, int k) {
        int index = k - 1;
        if (v == null || v.length <= index || index < 0) {
            return Double.NaN;
        }
        Arrays.sort(v);
        return v[index];
    }
}

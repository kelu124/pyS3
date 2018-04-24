package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.functions.NumericFunction.OneArg;

public final class Odd extends OneArg {
    private static final long PARITY_MASK = -2;

    protected double evaluate(double d) {
        if (d == 0.0d) {
            return 1.0d;
        }
        return d > 0.0d ? (double) calcOdd(d) : (double) (-calcOdd(-d));
    }

    private static long calcOdd(double d) {
        double dpm1 = d + 1.0d;
        long x = ((long) dpm1) & PARITY_MASK;
        return Double.compare((double) x, dpm1) == 0 ? x - 1 : x + 1;
    }
}

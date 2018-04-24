package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.functions.NumericFunction.OneArg;

public final class Even extends OneArg {
    private static final long PARITY_MASK = -2;

    protected double evaluate(double d) {
        if (d == 0.0d) {
            return 0.0d;
        }
        long result;
        if (d > 0.0d) {
            result = calcEven(d);
        } else {
            result = -calcEven(-d);
        }
        return (double) result;
    }

    private static long calcEven(double d) {
        long x = ((long) d) & PARITY_MASK;
        return ((double) x) == d ? x : x + 2;
    }
}

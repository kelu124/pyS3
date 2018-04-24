package org.apache.poi.ss.formula.functions;

public final class Sumxmy2 extends XYNumericFunction {
    private static final Accumulator XMinusYSquaredAccumulator = new C11781();

    static class C11781 implements Accumulator {
        C11781() {
        }

        public double accumulate(double x, double y) {
            double xmy = x - y;
            return xmy * xmy;
        }
    }

    protected Accumulator createAccumulator() {
        return XMinusYSquaredAccumulator;
    }
}

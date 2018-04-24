package org.apache.poi.ss.formula.functions;

public final class Sumx2my2 extends XYNumericFunction {
    private static final Accumulator XSquaredMinusYSquaredAccumulator = new C11761();

    static class C11761 implements Accumulator {
        C11761() {
        }

        public double accumulate(double x, double y) {
            return (x * x) - (y * y);
        }
    }

    protected Accumulator createAccumulator() {
        return XSquaredMinusYSquaredAccumulator;
    }
}

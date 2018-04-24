package org.apache.poi.ss.formula.functions;

public final class Sumx2py2 extends XYNumericFunction {
    private static final Accumulator XSquaredPlusYSquaredAccumulator = new C11771();

    static class C11771 implements Accumulator {
        C11771() {
        }

        public double accumulate(double x, double y) {
            return (x * x) + (y * y);
        }
    }

    protected Accumulator createAccumulator() {
        return XSquaredPlusYSquaredAccumulator;
    }
}

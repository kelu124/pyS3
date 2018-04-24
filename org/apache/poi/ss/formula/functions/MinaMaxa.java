package org.apache.poi.ss.formula.functions;

public abstract class MinaMaxa extends MultiOperandNumericFunction {
    public static final Function MAXA = new C11651();
    public static final Function MINA = new C11662();

    static class C11651 extends MinaMaxa {
        C11651() {
        }

        protected double evaluate(double[] values) {
            return values.length > 0 ? MathX.max(values) : 0.0d;
        }
    }

    static class C11662 extends MinaMaxa {
        C11662() {
        }

        protected double evaluate(double[] values) {
            return values.length > 0 ? MathX.min(values) : 0.0d;
        }
    }

    protected MinaMaxa() {
        super(true, true);
    }
}

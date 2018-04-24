package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.OperandResolver;
import org.apache.poi.ss.formula.eval.ValueEval;

public abstract class AggregateFunction extends MultiOperandNumericFunction {
    public static final Function AVEDEV = new C11322();
    public static final Function AVERAGE = new C11333();
    public static final Function DEVSQ = new C11344();
    public static final Function LARGE = new LargeSmall(true);
    public static final Function MAX = new C11355();
    public static final Function MEDIAN = new C11366();
    public static final Function MIN = new C11377();
    public static final Function PERCENTILE = new Percentile();
    public static final Function PRODUCT = new C11388();
    public static final Function SMALL = new LargeSmall(false);
    public static final Function STDEV = new C11399();
    public static final Function SUM = new AggregateFunction() {
        protected double evaluate(double[] values) {
            return MathX.sum(values);
        }
    };
    public static final Function SUMSQ = new AggregateFunction() {
        protected double evaluate(double[] values) {
            return MathX.sumsq(values);
        }
    };
    public static final Function VAR = new AggregateFunction() {
        protected double evaluate(double[] values) throws EvaluationException {
            if (values.length >= 1) {
                return StatsLib.var(values);
            }
            throw new EvaluationException(ErrorEval.DIV_ZERO);
        }
    };
    public static final Function VARP = new AggregateFunction() {
        protected double evaluate(double[] values) throws EvaluationException {
            if (values.length >= 1) {
                return StatsLib.varp(values);
            }
            throw new EvaluationException(ErrorEval.DIV_ZERO);
        }
    };

    static class C11322 extends AggregateFunction {
        C11322() {
        }

        protected double evaluate(double[] values) {
            return StatsLib.avedev(values);
        }
    }

    static class C11333 extends AggregateFunction {
        C11333() {
        }

        protected double evaluate(double[] values) throws EvaluationException {
            if (values.length >= 1) {
                return MathX.average(values);
            }
            throw new EvaluationException(ErrorEval.DIV_ZERO);
        }
    }

    static class C11344 extends AggregateFunction {
        C11344() {
        }

        protected double evaluate(double[] values) {
            return StatsLib.devsq(values);
        }
    }

    static class C11355 extends AggregateFunction {
        C11355() {
        }

        protected double evaluate(double[] values) {
            return values.length > 0 ? MathX.max(values) : 0.0d;
        }
    }

    static class C11366 extends AggregateFunction {
        C11366() {
        }

        protected double evaluate(double[] values) {
            return StatsLib.median(values);
        }
    }

    static class C11377 extends AggregateFunction {
        C11377() {
        }

        protected double evaluate(double[] values) {
            return values.length > 0 ? MathX.min(values) : 0.0d;
        }
    }

    static class C11388 extends AggregateFunction {
        C11388() {
        }

        protected double evaluate(double[] values) {
            return MathX.product(values);
        }
    }

    static class C11399 extends AggregateFunction {
        C11399() {
        }

        protected double evaluate(double[] values) throws EvaluationException {
            if (values.length >= 1) {
                return StatsLib.stdev(values);
            }
            throw new EvaluationException(ErrorEval.DIV_ZERO);
        }
    }

    private static final class LargeSmall extends Fixed2ArgFunction {
        private final boolean _isLarge;

        protected LargeSmall(boolean isLarge) {
            this._isLarge = isLarge;
        }

        public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1) {
            try {
                double dn = OperandResolver.coerceValueToDouble(OperandResolver.getSingleValue(arg1, srcRowIndex, srcColumnIndex));
                if (dn < 1.0d) {
                    return ErrorEval.NUM_ERROR;
                }
                int k = (int) Math.ceil(dn);
                try {
                    double[] ds = ValueCollector.collectValues(arg0);
                    if (k > ds.length) {
                        return ErrorEval.NUM_ERROR;
                    }
                    double result = this._isLarge ? StatsLib.kthLargest(ds, k) : StatsLib.kthSmallest(ds, k);
                    NumericFunction.checkValue(result);
                    return new NumberEval(result);
                } catch (EvaluationException e) {
                    return e.getErrorEval();
                }
            } catch (EvaluationException e2) {
                return ErrorEval.VALUE_INVALID;
            }
        }
    }

    private static final class Percentile extends Fixed2ArgFunction {
        protected Percentile() {
        }

        public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1) {
            try {
                double dn = OperandResolver.coerceValueToDouble(OperandResolver.getSingleValue(arg1, srcRowIndex, srcColumnIndex));
                if (dn < 0.0d || dn > 1.0d) {
                    return ErrorEval.NUM_ERROR;
                }
                try {
                    double[] ds = ValueCollector.collectValues(arg0);
                    int N = ds.length;
                    if (N == 0 || N > 8191) {
                        return ErrorEval.NUM_ERROR;
                    }
                    double result;
                    double n = (((double) (N - 1)) * dn) + 1.0d;
                    if (n == 1.0d) {
                        result = StatsLib.kthSmallest(ds, 1);
                    } else if (Double.compare(n, (double) N) == 0) {
                        result = StatsLib.kthLargest(ds, 1);
                    } else {
                        int k = (int) n;
                        result = StatsLib.kthSmallest(ds, k) + ((StatsLib.kthSmallest(ds, k + 1) - StatsLib.kthSmallest(ds, k)) * (n - ((double) k)));
                    }
                    NumericFunction.checkValue(result);
                    return new NumberEval(result);
                } catch (EvaluationException e) {
                    return e.getErrorEval();
                }
            } catch (EvaluationException e2) {
                return ErrorEval.VALUE_INVALID;
            }
        }
    }

    static final class ValueCollector extends MultiOperandNumericFunction {
        private static final ValueCollector instance = new ValueCollector();

        public ValueCollector() {
            super(false, false);
        }

        public static double[] collectValues(ValueEval... operands) throws EvaluationException {
            return instance.getNumberArray(operands);
        }

        protected double evaluate(double[] values) {
            throw new IllegalStateException("should not be called");
        }
    }

    protected AggregateFunction() {
        super(false, false);
    }

    static Function subtotalInstance(Function func) {
        final AggregateFunction arg = (AggregateFunction) func;
        return new AggregateFunction() {
            protected double evaluate(double[] values) throws EvaluationException {
                return arg.evaluate(values);
            }

            public boolean isSubtotalCounted() {
                return false;
            }
        };
    }
}

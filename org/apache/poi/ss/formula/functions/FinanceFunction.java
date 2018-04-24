package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.BoolEval;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.MissingArgEval;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.ValueEval;

public abstract class FinanceFunction implements Function3Arg, Function4Arg {
    private static final ValueEval DEFAULT_ARG3 = NumberEval.ZERO;
    private static final ValueEval DEFAULT_ARG4 = BoolEval.FALSE;
    public static final Function FV = new C11521();
    public static final Function NPER = new C11532();
    public static final Function PMT = new C11543();
    public static final Function PV = new C11554();

    static class C11521 extends FinanceFunction {
        C11521() {
        }

        protected double evaluate(double rate, double arg1, double arg2, double arg3, boolean type) {
            return FinanceLib.fv(rate, arg1, arg2, arg3, type);
        }
    }

    static class C11532 extends FinanceFunction {
        C11532() {
        }

        protected double evaluate(double rate, double arg1, double arg2, double arg3, boolean type) {
            return FinanceLib.nper(rate, arg1, arg2, arg3, type);
        }
    }

    static class C11543 extends FinanceFunction {
        C11543() {
        }

        protected double evaluate(double rate, double arg1, double arg2, double arg3, boolean type) {
            return FinanceLib.pmt(rate, arg1, arg2, arg3, type);
        }
    }

    static class C11554 extends FinanceFunction {
        C11554() {
        }

        protected double evaluate(double rate, double arg1, double arg2, double arg3, boolean type) {
            return FinanceLib.pv(rate, arg1, arg2, arg3, type);
        }
    }

    protected abstract double evaluate(double d, double d2, double d3, double d4, boolean z) throws EvaluationException;

    protected FinanceFunction() {
    }

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2) {
        return evaluate(srcRowIndex, srcColumnIndex, arg0, arg1, arg2, DEFAULT_ARG3);
    }

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2, ValueEval arg3) {
        return evaluate(srcRowIndex, srcColumnIndex, arg0, arg1, arg2, arg3, DEFAULT_ARG4);
    }

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2, ValueEval arg3, ValueEval arg4) {
        try {
            double result = evaluate(NumericFunction.singleOperandEvaluate(arg0, srcRowIndex, srcColumnIndex), NumericFunction.singleOperandEvaluate(arg1, srcRowIndex, srcColumnIndex), NumericFunction.singleOperandEvaluate(arg2, srcRowIndex, srcColumnIndex), NumericFunction.singleOperandEvaluate(arg3, srcRowIndex, srcColumnIndex), NumericFunction.singleOperandEvaluate(arg4, srcRowIndex, srcColumnIndex) != 0.0d);
            NumericFunction.checkValue(result);
            return new NumberEval(result);
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }

    public ValueEval evaluate(ValueEval[] args, int srcRowIndex, int srcColumnIndex) {
        ValueEval arg3;
        switch (args.length) {
            case 3:
                return evaluate(srcRowIndex, srcColumnIndex, args[0], args[1], args[2], DEFAULT_ARG3, DEFAULT_ARG4);
            case 4:
                arg3 = args[3];
                if (arg3 == MissingArgEval.instance) {
                    arg3 = DEFAULT_ARG3;
                }
                return evaluate(srcRowIndex, srcColumnIndex, args[0], args[1], args[2], arg3, DEFAULT_ARG4);
            case 5:
                arg3 = args[3];
                if (arg3 == MissingArgEval.instance) {
                    arg3 = DEFAULT_ARG3;
                }
                ValueEval arg4 = args[4];
                if (arg4 == MissingArgEval.instance) {
                    arg4 = DEFAULT_ARG4;
                }
                return evaluate(srcRowIndex, srcColumnIndex, args[0], args[1], args[2], arg3, arg4);
            default:
                return ErrorEval.VALUE_INVALID;
        }
    }

    protected double evaluate(double[] ds) throws EvaluationException {
        double arg3 = 0.0d;
        double arg4 = 0.0d;
        switch (ds.length) {
            case 3:
                break;
            case 4:
                break;
            case 5:
                arg4 = ds[4];
                break;
            default:
                throw new IllegalStateException("Wrong number of arguments");
        }
        arg3 = ds[3];
        return evaluate(ds[0], ds[1], ds[2], arg3, arg4 != 0.0d);
    }
}

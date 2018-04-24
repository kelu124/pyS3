package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.TwoDEval;
import org.apache.poi.ss.formula.eval.BlankEval;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.MissingArgEval;
import org.apache.poi.ss.formula.eval.OperandResolver;
import org.apache.poi.ss.formula.eval.RefEval;
import org.apache.poi.ss.formula.eval.ValueEval;

public final class Index implements Function2Arg, Function3Arg, Function4Arg {
    static final /* synthetic */ boolean $assertionsDisabled = (!Index.class.desiredAssertionStatus());

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1) {
        TwoDEval reference = convertFirstArg(arg0);
        int columnIx = 0;
        try {
            int rowIx = resolveIndexArg(arg1, srcRowIndex, srcColumnIndex);
            if (!reference.isColumn()) {
                if (!reference.isRow()) {
                    return ErrorEval.REF_INVALID;
                }
                columnIx = rowIx;
                rowIx = 0;
            }
            return getValueFromArea(reference, rowIx, columnIx);
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2) {
        try {
            return getValueFromArea(convertFirstArg(arg0), resolveIndexArg(arg1, srcRowIndex, srcColumnIndex), resolveIndexArg(arg2, srcRowIndex, srcColumnIndex));
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2, ValueEval arg3) {
        throw new RuntimeException("Incomplete code - don't know how to support the 'area_num' parameter yet)");
    }

    private static TwoDEval convertFirstArg(ValueEval arg0) {
        ValueEval firstArg = arg0;
        if (firstArg instanceof RefEval) {
            return ((RefEval) firstArg).offset(0, 0, 0, 0);
        }
        if (firstArg instanceof TwoDEval) {
            return (TwoDEval) firstArg;
        }
        throw new RuntimeException("Incomplete code - cannot handle first arg of type (" + firstArg.getClass().getName() + ")");
    }

    public ValueEval evaluate(ValueEval[] args, int srcRowIndex, int srcColumnIndex) {
        switch (args.length) {
            case 2:
                return evaluate(srcRowIndex, srcColumnIndex, args[0], args[1]);
            case 3:
                return evaluate(srcRowIndex, srcColumnIndex, args[0], args[1], args[2]);
            case 4:
                return evaluate(srcRowIndex, srcColumnIndex, args[0], args[1], args[2], args[3]);
            default:
                return ErrorEval.VALUE_INVALID;
        }
    }

    private static ValueEval getValueFromArea(TwoDEval ae, int pRowIx, int pColumnIx) throws EvaluationException {
        if (!$assertionsDisabled && pRowIx < 0) {
            throw new AssertionError();
        } else if ($assertionsDisabled || pColumnIx >= 0) {
            TwoDEval result = ae;
            if (pRowIx != 0) {
                if (pRowIx > ae.getHeight()) {
                    throw new EvaluationException(ErrorEval.REF_INVALID);
                }
                result = result.getRow(pRowIx - 1);
            }
            if (pColumnIx == 0) {
                return result;
            }
            if (pColumnIx <= ae.getWidth()) {
                return result.getColumn(pColumnIx - 1);
            }
            throw new EvaluationException(ErrorEval.REF_INVALID);
        } else {
            throw new AssertionError();
        }
    }

    private static int resolveIndexArg(ValueEval arg, int srcCellRow, int srcCellCol) throws EvaluationException {
        int i = 0;
        ValueEval ev = OperandResolver.getSingleValue(arg, srcCellRow, srcCellCol);
        if (!(ev == MissingArgEval.instance || ev == BlankEval.instance)) {
            i = OperandResolver.coerceValueToInt(ev);
            if (i < 0) {
                throw new EvaluationException(ErrorEval.VALUE_INVALID);
            }
        }
        return i;
    }
}

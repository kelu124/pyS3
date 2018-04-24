package org.apache.poi.ss.formula.atp;

import org.apache.poi.ss.formula.OperationEvaluationContext;
import org.apache.poi.ss.formula.WorkbookEvaluator;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.formula.functions.FreeRefFunction;

final class IfError implements FreeRefFunction {
    public static final FreeRefFunction instance = new IfError();

    private IfError() {
    }

    public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec) {
        if (args.length != 2) {
            return ErrorEval.VALUE_INVALID;
        }
        try {
            return evaluateInternal(args[0], args[1], ec.getRowIndex(), ec.getColumnIndex());
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }

    private static ValueEval evaluateInternal(ValueEval arg, ValueEval iferror, int srcCellRow, int srcCellCol) throws EvaluationException {
        arg = WorkbookEvaluator.dereferenceResult(arg, srcCellRow, srcCellCol);
        return arg instanceof ErrorEval ? iferror : arg;
    }
}

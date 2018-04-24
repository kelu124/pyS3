package org.apache.poi.ss.formula.atp;

import org.apache.poi.ss.formula.OperationEvaluationContext;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.formula.functions.FreeRefFunction;

final class NetworkdaysFunction implements FreeRefFunction {
    public static final FreeRefFunction instance = new NetworkdaysFunction(ArgumentsEvaluator.instance);
    private ArgumentsEvaluator evaluator;

    private NetworkdaysFunction(ArgumentsEvaluator anEvaluator) {
        this.evaluator = anEvaluator;
    }

    public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec) {
        if (args.length < 2 || args.length > 3) {
            return ErrorEval.VALUE_INVALID;
        }
        int srcCellRow = ec.getRowIndex();
        int srcCellCol = ec.getColumnIndex();
        try {
            double start = this.evaluator.evaluateDateArg(args[0], srcCellRow, srcCellCol);
            double end = this.evaluator.evaluateDateArg(args[1], srcCellRow, srcCellCol);
            if (start > end) {
                return ErrorEval.NAME_INVALID;
            }
            return new NumberEval((double) WorkdayCalculator.instance.calculateWorkdays(start, end, this.evaluator.evaluateDatesArg(args.length == 3 ? args[2] : null, srcCellRow, srcCellCol)));
        } catch (EvaluationException e) {
            return ErrorEval.VALUE_INVALID;
        }
    }
}

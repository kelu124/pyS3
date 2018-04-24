package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.OperationEvaluationContext;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.OperandResolver;
import org.apache.poi.ss.formula.eval.ValueEval;

public class Oct2Dec extends Fixed1ArgFunction implements FreeRefFunction {
    static final int MAX_NUMBER_OF_PLACES = 10;
    static final int OCTAL_BASE = 8;
    public static final FreeRefFunction instance = new Oct2Dec();

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval numberVE) {
        try {
            return new NumberEval(BaseNumberUtils.convertToDecimal(OperandResolver.coerceValueToString(numberVE), 8, 10));
        } catch (IllegalArgumentException e) {
            return ErrorEval.NUM_ERROR;
        }
    }

    public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec) {
        if (args.length != 1) {
            return ErrorEval.VALUE_INVALID;
        }
        return evaluate(ec.getRowIndex(), ec.getColumnIndex(), args[0]);
    }
}

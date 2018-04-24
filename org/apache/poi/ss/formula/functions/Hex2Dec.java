package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.OperationEvaluationContext;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.OperandResolver;
import org.apache.poi.ss.formula.eval.RefEval;
import org.apache.poi.ss.formula.eval.ValueEval;

public class Hex2Dec extends Fixed1ArgFunction implements FreeRefFunction {
    static final int HEXADECIMAL_BASE = 16;
    static final int MAX_NUMBER_OF_PLACES = 10;
    public static final FreeRefFunction instance = new Hex2Dec();

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval numberVE) {
        String hex;
        if (numberVE instanceof RefEval) {
            RefEval re = (RefEval) numberVE;
            hex = OperandResolver.coerceValueToString(re.getInnerValueEval(re.getFirstSheetIndex()));
        } else {
            hex = OperandResolver.coerceValueToString(numberVE);
        }
        try {
            return new NumberEval(BaseNumberUtils.convertToDecimal(hex, 16, 10));
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

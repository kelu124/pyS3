package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.OperandResolver;
import org.apache.poi.ss.formula.eval.ValueEval;

public class IPMT extends NumericFunction {
    public double eval(ValueEval[] args, int srcCellRow, int srcCellCol) throws EvaluationException {
        if (args.length != 4) {
            throw new EvaluationException(ErrorEval.VALUE_INVALID);
        }
        double result = Finance.ipmt(OperandResolver.coerceValueToDouble(OperandResolver.getSingleValue(args[0], srcCellRow, srcCellCol)), OperandResolver.coerceValueToInt(OperandResolver.getSingleValue(args[1], srcCellRow, srcCellCol)), OperandResolver.coerceValueToInt(OperandResolver.getSingleValue(args[2], srcCellRow, srcCellCol)), OperandResolver.coerceValueToDouble(OperandResolver.getSingleValue(args[3], srcCellRow, srcCellCol)));
        NumericFunction.checkValue(result);
        return result;
    }
}

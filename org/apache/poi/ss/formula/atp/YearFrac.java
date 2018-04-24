package org.apache.poi.ss.formula.atp;

import org.apache.poi.ss.formula.OperationEvaluationContext;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.OperandResolver;
import org.apache.poi.ss.formula.eval.StringEval;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.formula.functions.FreeRefFunction;
import org.apache.poi.ss.usermodel.DateUtil;

final class YearFrac implements FreeRefFunction {
    public static final FreeRefFunction instance = new YearFrac();

    private YearFrac() {
    }

    public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec) {
        int srcCellRow = ec.getRowIndex();
        int srcCellCol = ec.getColumnIndex();
        int basis = 0;
        try {
            switch (args.length) {
                case 2:
                    break;
                case 3:
                    basis = evaluateIntArg(args[2], srcCellRow, srcCellCol);
                    break;
                default:
                    return ErrorEval.VALUE_INVALID;
            }
            return new NumberEval(YearFracCalculator.calculate(evaluateDateArg(args[0], srcCellRow, srcCellCol), evaluateDateArg(args[1], srcCellRow, srcCellCol), basis));
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }

    private static double evaluateDateArg(ValueEval arg, int srcCellRow, int srcCellCol) throws EvaluationException {
        ValueEval ve = OperandResolver.getSingleValue(arg, srcCellRow, (short) srcCellCol);
        if (!(ve instanceof StringEval)) {
            return OperandResolver.coerceValueToDouble(ve);
        }
        String strVal = ((StringEval) ve).getStringValue();
        Double dVal = OperandResolver.parseDouble(strVal);
        if (dVal != null) {
            return dVal.doubleValue();
        }
        return DateUtil.getExcelDate(DateParser.parseDate(strVal), false);
    }

    private static int evaluateIntArg(ValueEval arg, int srcCellRow, int srcCellCol) throws EvaluationException {
        return OperandResolver.coerceValueToInt(OperandResolver.getSingleValue(arg, srcCellRow, (short) srcCellCol));
    }
}

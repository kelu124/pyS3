package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.BlankEval;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.MissingArgEval;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.OperandResolver;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.usermodel.DateUtil;

public final class WeekdayFunc implements Function {
    public static final Function instance = new WeekdayFunc();

    private WeekdayFunc() {
    }

    public ValueEval evaluate(ValueEval[] args, int srcRowIndex, int srcColumnIndex) {
        try {
            if (args.length < 1 || args.length > 2) {
                return ErrorEval.VALUE_INVALID;
            }
            double serialDate = OperandResolver.coerceValueToDouble(OperandResolver.getSingleValue(args[0], srcRowIndex, srcColumnIndex));
            if (!DateUtil.isValidExcelDate(serialDate)) {
                return ErrorEval.NUM_ERROR;
            }
            double result;
            int weekday = DateUtil.getJavaCalendar(serialDate, false).get(7);
            int returnOption = 1;
            if (args.length == 2) {
                ValueEval ve = OperandResolver.getSingleValue(args[1], srcRowIndex, srcColumnIndex);
                if (ve == MissingArgEval.instance || ve == BlankEval.instance) {
                    return ErrorEval.NUM_ERROR;
                }
                returnOption = OperandResolver.coerceValueToInt(ve);
                if (returnOption == 2) {
                    returnOption = 11;
                }
            }
            if (returnOption == 1) {
                result = (double) weekday;
            } else if (returnOption == 3) {
                result = (double) (((weekday + 6) - 1) % 7);
            } else if (returnOption < 11 || returnOption > 17) {
                return ErrorEval.NUM_ERROR;
            } else {
                result = (double) ((((weekday + 6) - (returnOption - 10)) % 7) + 1);
            }
            return new NumberEval(result);
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }
}

package org.apache.poi.ss.formula.functions;

import java.util.Calendar;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.util.LocaleUtil;

public final class DateFunc extends Fixed3ArgFunction {
    public static final Function instance = new DateFunc();

    private DateFunc() {
    }

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2) {
        try {
            double result = evaluate(getYear(NumericFunction.singleOperandEvaluate(arg0, srcRowIndex, srcColumnIndex)), (int) (NumericFunction.singleOperandEvaluate(arg1, srcRowIndex, srcColumnIndex) - 1.0d), (int) NumericFunction.singleOperandEvaluate(arg2, srcRowIndex, srcColumnIndex));
            NumericFunction.checkValue(result);
            return new NumberEval(result);
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }

    private static double evaluate(int year, int month, int pDay) throws EvaluationException {
        if (year < 0) {
            throw new EvaluationException(ErrorEval.VALUE_INVALID);
        }
        while (month < 0) {
            year--;
            month += 12;
        }
        if (year == 1900 && month == 1 && pDay == 29) {
            return 60.0d;
        }
        int day = pDay;
        if (year == 1900 && ((month == 0 && day >= 60) || (month == 1 && day >= 30))) {
            day--;
        }
        Calendar c = LocaleUtil.getLocaleCalendar(year, month, day);
        if (pDay < 0 && c.get(1) == 1900 && month > 1 && c.get(2) < 2) {
            c.add(5, 1);
        }
        return DateUtil.getExcelDate(c.getTime(), false);
    }

    private static int getYear(double d) {
        int year = (int) d;
        if (year < 0) {
            return -1;
        }
        return year < 1900 ? year + 1900 : year;
    }
}

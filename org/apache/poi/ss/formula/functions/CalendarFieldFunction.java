package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.OperandResolver;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.usermodel.DateUtil;

public final class CalendarFieldFunction extends Fixed1ArgFunction {
    public static final Function DAY = new CalendarFieldFunction(5);
    public static final Function HOUR = new CalendarFieldFunction(11);
    public static final Function MINUTE = new CalendarFieldFunction(12);
    public static final Function MONTH = new CalendarFieldFunction(2);
    public static final Function SECOND = new CalendarFieldFunction(13);
    public static final Function YEAR = new CalendarFieldFunction(1);
    private final int _dateFieldId;

    private CalendarFieldFunction(int dateFieldId) {
        this._dateFieldId = dateFieldId;
    }

    public final ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0) {
        try {
            double val = OperandResolver.coerceValueToDouble(OperandResolver.getSingleValue(arg0, srcRowIndex, srcColumnIndex));
            if (val < 0.0d) {
                return ErrorEval.NUM_ERROR;
            }
            return new NumberEval((double) getCalField(val));
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }

    private int getCalField(double serialDate) {
        if (((int) serialDate) == 0) {
            switch (this._dateFieldId) {
                case 1:
                    return 1900;
                case 2:
                    return 1;
                case 5:
                    return 0;
            }
        }
        int result = DateUtil.getJavaCalendarUTC(5.78125E-6d + serialDate, false).get(this._dateFieldId);
        if (this._dateFieldId == 2) {
            return result + 1;
        }
        return result;
    }
}

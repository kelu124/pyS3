package org.apache.poi.ss.formula.functions;

import java.util.Calendar;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.OperandResolver;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.util.LocaleUtil;
import org.bytedeco.javacpp.dc1394;

public class Days360 extends Var2or3ArgFunction {
    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1) {
        try {
            return new NumberEval(evaluate(NumericFunction.singleOperandEvaluate(arg0, srcRowIndex, srcColumnIndex), NumericFunction.singleOperandEvaluate(arg1, srcRowIndex, srcColumnIndex), false));
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2) {
        try {
            double d0 = NumericFunction.singleOperandEvaluate(arg0, srcRowIndex, srcColumnIndex);
            double d1 = NumericFunction.singleOperandEvaluate(arg1, srcRowIndex, srcColumnIndex);
            Boolean method = OperandResolver.coerceValueToBoolean(OperandResolver.getSingleValue(arg2, srcRowIndex, srcColumnIndex), false);
            return new NumberEval(evaluate(d0, d1, method == null ? false : method.booleanValue()));
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }

    private static double evaluate(double d0, double d1, boolean method) {
        Calendar realStart = getDate(d0);
        Calendar realEnd = getDate(d1);
        int[] startingDate = getStartingDate(realStart, method);
        int[] endingDate = getEndingDate(realEnd, realStart, method);
        return (double) ((((endingDate[0] * dc1394.DC1394_COLOR_CODING_RGB16S) + (endingDate[1] * 30)) + endingDate[2]) - (((startingDate[0] * dc1394.DC1394_COLOR_CODING_RGB16S) + (startingDate[1] * 30)) + startingDate[2]));
    }

    private static Calendar getDate(double date) {
        Calendar processedDate = LocaleUtil.getLocaleCalendar();
        processedDate.setTime(DateUtil.getJavaDate(date, false));
        return processedDate;
    }

    private static int[] getStartingDate(Calendar realStart, boolean method) {
        Calendar d = realStart;
        int yyyy = d.get(1);
        int mm = d.get(2);
        int dd = Math.min(30, d.get(5));
        if (!method && isLastDayOfMonth(d)) {
            dd = 30;
        }
        return new int[]{yyyy, mm, dd};
    }

    private static int[] getEndingDate(Calendar realEnd, Calendar realStart, boolean method) {
        Calendar d = realEnd;
        int yyyy = d.get(1);
        int mm = d.get(2);
        int dd = Math.min(30, d.get(5));
        if (!method && realEnd.get(5) == 31) {
            if (realStart.get(5) < 30) {
                d.set(5, 1);
                d.add(2, 1);
                yyyy = d.get(1);
                mm = d.get(2);
                dd = 1;
            } else {
                dd = 30;
            }
        }
        return new int[]{yyyy, mm, dd};
    }

    private static boolean isLastDayOfMonth(Calendar date) {
        return date.get(5) == date.getActualMaximum(5);
    }
}

package org.apache.poi.ss.formula.atp;

import java.util.Calendar;
import java.util.regex.Pattern;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.util.LocaleUtil;

public class DateParser {
    private DateParser() {
    }

    public static Calendar parseDate(String strVal) throws EvaluationException {
        String[] parts = Pattern.compile("/").split(strVal);
        if (parts.length != 3) {
            throw new EvaluationException(ErrorEval.VALUE_INVALID);
        }
        String part2 = parts[2];
        int spacePos = part2.indexOf(32);
        if (spacePos > 0) {
            part2 = part2.substring(0, spacePos);
        }
        try {
            int f0 = Integer.parseInt(parts[0]);
            int f1 = Integer.parseInt(parts[1]);
            int f2 = Integer.parseInt(part2);
            if (f0 < 0 || f1 < 0 || f2 < 0 || (f0 > 12 && f1 > 12 && f2 > 12)) {
                throw new EvaluationException(ErrorEval.VALUE_INVALID);
            } else if (f0 >= 1900 && f0 < 9999) {
                return makeDate(f0, f1, f2);
            } else {
                throw new RuntimeException("Unable to determine date format for text '" + strVal + "'");
            }
        } catch (NumberFormatException e) {
            throw new EvaluationException(ErrorEval.VALUE_INVALID);
        }
    }

    private static Calendar makeDate(int year, int month, int day) throws EvaluationException {
        if (month < 1 || month > 12) {
            throw new EvaluationException(ErrorEval.VALUE_INVALID);
        }
        Calendar cal = LocaleUtil.getLocaleCalendar(year, month - 1, 1, 0, 0, 0);
        if (day < 1 || day > cal.getActualMaximum(5)) {
            throw new EvaluationException(ErrorEval.VALUE_INVALID);
        }
        cal.set(5, day);
        return cal;
    }
}

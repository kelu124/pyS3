package org.apache.poi.ss.formula.functions;

import java.util.Calendar;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.util.LocaleUtil;

public final class Today extends Fixed0ArgFunction {
    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex) {
        Calendar now = LocaleUtil.getLocaleCalendar();
        now.clear(10);
        now.set(11, 0);
        now.clear(12);
        now.clear(13);
        now.clear(14);
        return new NumberEval(DateUtil.getExcelDate(now.getTime()));
    }
}

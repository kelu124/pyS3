package org.apache.poi.ss.formula.functions;

import java.util.Date;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.usermodel.DateUtil;

public final class Now extends Fixed0ArgFunction {
    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex) {
        return new NumberEval(DateUtil.getExcelDate(new Date(System.currentTimeMillis())));
    }
}

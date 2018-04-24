package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.OperandResolver;
import org.apache.poi.ss.formula.eval.StringEval;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.bytedeco.javacpp.avutil;

public class Rept extends Fixed2ArgFunction {
    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval text, ValueEval number_times) {
        try {
            String strText1 = OperandResolver.coerceValueToString(OperandResolver.getSingleValue(text, srcRowIndex, srcColumnIndex));
            try {
                int numberOfTimeInt = (int) OperandResolver.coerceValueToDouble(number_times);
                StringBuffer strb = new StringBuffer(strText1.length() * numberOfTimeInt);
                for (int i = 0; i < numberOfTimeInt; i++) {
                    strb.append(strText1);
                }
                if (strb.toString().length() > avutil.FF_LAMBDA_MAX) {
                    return ErrorEval.VALUE_INVALID;
                }
                return new StringEval(strb.toString());
            } catch (EvaluationException e) {
                return ErrorEval.VALUE_INVALID;
            }
        } catch (EvaluationException e2) {
            return e2.getErrorEval();
        }
    }
}

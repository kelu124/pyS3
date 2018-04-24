package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.OperandResolver;
import org.apache.poi.ss.formula.eval.StringEval;
import org.apache.poi.ss.formula.eval.ValueEval;

public class Code extends Fixed1ArgFunction {
    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval textArg) {
        try {
            String text = OperandResolver.coerceValueToString(OperandResolver.getSingleValue(textArg, srcRowIndex, srcColumnIndex));
            if (text.length() == 0) {
                return ErrorEval.VALUE_INVALID;
            }
            return new StringEval(String.valueOf(text.charAt(0)));
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }
}

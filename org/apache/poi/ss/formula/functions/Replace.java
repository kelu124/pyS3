package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.StringEval;
import org.apache.poi.ss.formula.eval.ValueEval;

public final class Replace extends Fixed4ArgFunction {
    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2, ValueEval arg3) {
        try {
            String oldStr = TextFunction.evaluateStringArg(arg0, srcRowIndex, srcColumnIndex);
            int startNum = TextFunction.evaluateIntArg(arg1, srcRowIndex, srcColumnIndex);
            int numChars = TextFunction.evaluateIntArg(arg2, srcRowIndex, srcColumnIndex);
            String newStr = TextFunction.evaluateStringArg(arg3, srcRowIndex, srcColumnIndex);
            if (startNum < 1 || numChars < 0) {
                return ErrorEval.VALUE_INVALID;
            }
            StringBuffer strBuff = new StringBuffer(oldStr);
            if (startNum <= oldStr.length() && numChars != 0) {
                strBuff.delete(startNum - 1, (startNum - 1) + numChars);
            }
            if (startNum > strBuff.length()) {
                strBuff.append(newStr);
            } else {
                strBuff.insert(startNum - 1, newStr);
            }
            return new StringEval(strBuff.toString());
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }
}

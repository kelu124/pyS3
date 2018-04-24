package org.apache.poi.ss.formula.functions;

import java.util.regex.Matcher;
import org.apache.poi.ss.formula.OperationEvaluationContext;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.OperandResolver;
import org.apache.poi.ss.formula.eval.StringEval;
import org.apache.poi.ss.formula.eval.ValueEval;

public class ImReal extends Fixed1ArgFunction implements FreeRefFunction {
    public static final FreeRefFunction instance = new ImReal();

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval inumberVE) {
        try {
            Matcher m = Imaginary.COMPLEX_NUMBER_PATTERN.matcher(OperandResolver.coerceValueToString(OperandResolver.getSingleValue(inumberVE, srcRowIndex, srcColumnIndex)));
            String real = "";
            if (!m.matches()) {
                return ErrorEval.NUM_ERROR;
            }
            String realGroup = m.group(2);
            boolean hasRealPart = realGroup.length() != 0;
            if (realGroup.length() == 0) {
                return new StringEval(String.valueOf(0));
            }
            if (hasRealPart) {
                String sign = "";
                String realSign = m.group(1);
                if (!(realSign.length() == 0 || realSign.equals("+"))) {
                    sign = realSign;
                }
                String groupRealNumber = m.group(2);
                if (groupRealNumber.length() != 0) {
                    real = sign + groupRealNumber;
                } else {
                    real = sign + "1";
                }
            }
            return new StringEval(real);
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }

    public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec) {
        if (args.length != 1) {
            return ErrorEval.VALUE_INVALID;
        }
        return evaluate(ec.getRowIndex(), ec.getColumnIndex(), args[0]);
    }
}

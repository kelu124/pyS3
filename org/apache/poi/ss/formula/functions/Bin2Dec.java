package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.OperationEvaluationContext;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.OperandResolver;
import org.apache.poi.ss.formula.eval.RefEval;
import org.apache.poi.ss.formula.eval.ValueEval;

public class Bin2Dec extends Fixed1ArgFunction implements FreeRefFunction {
    public static final FreeRefFunction instance = new Bin2Dec();

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval numberVE) {
        String number;
        if (numberVE instanceof RefEval) {
            RefEval re = (RefEval) numberVE;
            number = OperandResolver.coerceValueToString(re.getInnerValueEval(re.getFirstSheetIndex()));
        } else {
            number = OperandResolver.coerceValueToString(numberVE);
        }
        if (number.length() > 10) {
            return ErrorEval.NUM_ERROR;
        }
        String unsigned;
        boolean isPositive;
        String value;
        if (number.length() < 10) {
            unsigned = number;
            isPositive = true;
        } else {
            unsigned = number.substring(1);
            isPositive = number.startsWith("0");
        }
        if (isPositive) {
            try {
                value = String.valueOf(getDecimalValue(unsigned));
            } catch (NumberFormatException e) {
                return ErrorEval.NUM_ERROR;
            }
        }
        value = "-" + String.valueOf(getDecimalValue(toggleBits(unsigned)) + 1);
        return new NumberEval((double) Long.parseLong(value));
    }

    private int getDecimalValue(String unsigned) {
        int sum = 0;
        int numBits = unsigned.length();
        int power = numBits - 1;
        for (int i = 0; i < numBits; i++) {
            sum += (int) (((double) Integer.parseInt(unsigned.substring(i, i + 1))) * Math.pow(2.0d, (double) power));
            power--;
        }
        return sum;
    }

    private static String toggleBits(String s) {
        String s2 = Long.toBinaryString(Long.parseLong(s, 2) ^ ((1 << s.length()) - 1));
        while (s2.length() < s.length()) {
            s2 = '0' + s2;
        }
        return s2;
    }

    public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec) {
        if (args.length != 1) {
            return ErrorEval.VALUE_INVALID;
        }
        return evaluate(ec.getRowIndex(), ec.getColumnIndex(), args[0]);
    }
}

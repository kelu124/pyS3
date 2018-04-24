package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.OperandResolver;
import org.apache.poi.ss.formula.eval.StringEval;
import org.apache.poi.ss.formula.eval.ValueEval;

public class Roman extends Fixed2ArgFunction {
    public static final String[] ROMAN = new String[]{"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
    public static final int[] VALUES = new int[]{1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval numberVE, ValueEval formVE) {
        try {
            int number = OperandResolver.coerceValueToInt(OperandResolver.getSingleValue(numberVE, srcRowIndex, srcColumnIndex));
            if (number < 0) {
                return ErrorEval.VALUE_INVALID;
            }
            if (number > 3999) {
                return ErrorEval.VALUE_INVALID;
            }
            if (number == 0) {
                return new StringEval("");
            }
            try {
                int form = OperandResolver.coerceValueToInt(OperandResolver.getSingleValue(formVE, srcRowIndex, srcColumnIndex));
                if (form > 4 || form < 0) {
                    return ErrorEval.VALUE_INVALID;
                }
                String result = integerToRoman(number);
                if (form == 0) {
                    return new StringEval(result);
                }
                return new StringEval(makeConcise(result, form));
            } catch (EvaluationException e) {
                return ErrorEval.NUM_ERROR;
            }
        } catch (EvaluationException e2) {
            return ErrorEval.VALUE_INVALID;
        }
    }

    private String integerToRoman(int number) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 13; i++) {
            while (number >= VALUES[i]) {
                number -= VALUES[i];
                result.append(ROMAN[i]);
            }
        }
        return result.toString();
    }

    public String makeConcise(String result, int form) {
        if (form > 0) {
            result = result.replaceAll("XLV", "VL").replaceAll("XCV", "VC").replaceAll("CDL", "LD").replaceAll("CML", "LM").replaceAll("CMVC", "LMVL");
        }
        if (form == 1) {
            result = result.replaceAll("CDXC", "LDXL").replaceAll("CDVC", "LDVL").replaceAll("CMXC", "LMXL").replaceAll("XCIX", "VCIV").replaceAll("XLIX", "VLIV");
        }
        if (form > 1) {
            result = result.replaceAll("XLIX", "IL").replaceAll("XCIX", "IC").replaceAll("CDXC", "XD").replaceAll("CDVC", "XDV").replaceAll("CDIC", "XDIX").replaceAll("LMVL", "XMV").replaceAll("CMIC", "XMIX").replaceAll("CMXC", "XM");
        }
        if (form > 2) {
            result = result.replaceAll("XDV", "VD").replaceAll("XDIX", "VDIV").replaceAll("XMV", "VM").replaceAll("XMIX", "VMIV");
        }
        if (form == 4) {
            return result.replaceAll("VDIV", "ID").replaceAll("VMIV", "IM");
        }
        return result;
    }
}

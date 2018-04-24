package org.apache.poi.ss.formula.functions;

import java.util.Locale;
import org.apache.poi.ss.formula.OperationEvaluationContext;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.OperandResolver;
import org.apache.poi.ss.formula.eval.StringEval;
import org.apache.poi.ss.formula.eval.ValueEval;

public class Complex extends Var2or3ArgFunction implements FreeRefFunction {
    public static final String DEFAULT_SUFFIX = "i";
    public static final String SUPPORTED_SUFFIX = "j";
    public static final FreeRefFunction instance = new Complex();

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval real_num, ValueEval i_num) {
        return evaluate(srcRowIndex, srcColumnIndex, real_num, i_num, new StringEval("i"));
    }

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval real_num, ValueEval i_num, ValueEval suffix) {
        try {
            try {
                double realNum = OperandResolver.coerceValueToDouble(OperandResolver.getSingleValue(real_num, srcRowIndex, srcColumnIndex));
                try {
                    try {
                        double realINum = OperandResolver.coerceValueToDouble(OperandResolver.getSingleValue(i_num, srcRowIndex, srcColumnIndex));
                        String suffixValue = OperandResolver.coerceValueToString(suffix);
                        if (suffixValue.length() == 0) {
                            suffixValue = "i";
                        }
                        if (suffixValue.equals("i".toUpperCase(Locale.ROOT)) || suffixValue.equals(SUPPORTED_SUFFIX.toUpperCase(Locale.ROOT))) {
                            return ErrorEval.VALUE_INVALID;
                        }
                        if (!suffixValue.equals("i") && !suffixValue.equals(SUPPORTED_SUFFIX)) {
                            return ErrorEval.VALUE_INVALID;
                        }
                        StringBuffer strb = new StringBuffer("");
                        if (realNum != 0.0d) {
                            if (isDoubleAnInt(realNum)) {
                                strb.append((int) realNum);
                            } else {
                                strb.append(realNum);
                            }
                        }
                        if (realINum != 0.0d) {
                            if (strb.length() != 0 && realINum > 0.0d) {
                                strb.append("+");
                            }
                            if (!(realINum == 1.0d || realINum == -1.0d)) {
                                if (isDoubleAnInt(realINum)) {
                                    strb.append((int) realINum);
                                } else {
                                    strb.append(realINum);
                                }
                            }
                            strb.append(suffixValue);
                        }
                        return new StringEval(strb.toString());
                    } catch (EvaluationException e) {
                        return ErrorEval.VALUE_INVALID;
                    }
                } catch (EvaluationException e2) {
                    return e2.getErrorEval();
                }
            } catch (EvaluationException e3) {
                return ErrorEval.VALUE_INVALID;
            }
        } catch (EvaluationException e22) {
            return e22.getErrorEval();
        }
    }

    private boolean isDoubleAnInt(double number) {
        return number == Math.floor(number) && !Double.isInfinite(number);
    }

    public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec) {
        if (args.length == 2) {
            return evaluate(ec.getRowIndex(), ec.getColumnIndex(), args[0], args[1]);
        }
        if (args.length == 3) {
            return evaluate(ec.getRowIndex(), ec.getColumnIndex(), args[0], args[1], args[2]);
        }
        return ErrorEval.VALUE_INVALID;
    }
}

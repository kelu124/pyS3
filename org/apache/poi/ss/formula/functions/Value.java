package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.OperandResolver;
import org.apache.poi.ss.formula.eval.ValueEval;

public final class Value extends Fixed1ArgFunction {
    private static final int MIN_DISTANCE_BETWEEN_THOUSANDS_SEPARATOR = 4;
    private static final Double ZERO = new Double(0.0d);

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0) {
        try {
            Double result = convertTextToNumber(OperandResolver.coerceValueToString(OperandResolver.getSingleValue(arg0, srcRowIndex, srcColumnIndex)));
            if (result == null) {
                return ErrorEval.VALUE_INVALID;
            }
            return new NumberEval(result.doubleValue());
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }

    private static Double convertTextToNumber(String strText) {
        boolean foundCurrency = false;
        boolean foundUnaryPlus = false;
        boolean foundUnaryMinus = false;
        boolean foundPercentage = false;
        int len = strText.length();
        int i = 0;
        while (i < len) {
            boolean foundDecimalPoint;
            int lastThousandsSeparatorIndex;
            StringBuffer stringBuffer;
            String remainingTextTrimmed;
            double d;
            double result;
            char ch = strText.charAt(i);
            if (!(Character.isDigit(ch) || ch == '.')) {
                switch (ch) {
                    case ' ':
                        break;
                    case '$':
                        if (!foundCurrency) {
                            foundCurrency = true;
                            break;
                        }
                        return null;
                    case '+':
                        if (!foundUnaryMinus && !foundUnaryPlus) {
                            foundUnaryPlus = true;
                            break;
                        }
                        return null;
                    case '-':
                        if (!foundUnaryMinus && !foundUnaryPlus) {
                            foundUnaryMinus = true;
                            break;
                        }
                        return null;
                        break;
                    default:
                        return null;
                }
                i++;
            }
            if (i >= len) {
                foundDecimalPoint = false;
                lastThousandsSeparatorIndex = -32768;
                stringBuffer = new StringBuffer(len);
                while (i < len) {
                    ch = strText.charAt(i);
                    if (Character.isDigit(ch)) {
                        switch (ch) {
                            case ' ':
                                remainingTextTrimmed = strText.substring(i).trim();
                                if (!remainingTextTrimmed.equals("%")) {
                                    if (remainingTextTrimmed.length() > 0) {
                                        break;
                                    }
                                    return null;
                                }
                                foundPercentage = true;
                                break;
                            case '%':
                                foundPercentage = true;
                                break;
                            case ',':
                                if (!foundDecimalPoint) {
                                    if (i - lastThousandsSeparatorIndex < 4) {
                                        lastThousandsSeparatorIndex = i;
                                        break;
                                    }
                                    return null;
                                }
                                return null;
                            case '.':
                                if (!foundDecimalPoint) {
                                    if (i - lastThousandsSeparatorIndex < 4) {
                                        foundDecimalPoint = true;
                                        stringBuffer.append('.');
                                        break;
                                    }
                                    return null;
                                }
                                return null;
                            case 'E':
                            case 'e':
                                if (i - lastThousandsSeparatorIndex < 4) {
                                    stringBuffer.append(strText.substring(i));
                                    i = len;
                                    break;
                                }
                                return null;
                            default:
                                return null;
                        }
                    }
                    stringBuffer.append(ch);
                    i++;
                }
                if (foundDecimalPoint && i - lastThousandsSeparatorIndex < 4) {
                    return null;
                }
                try {
                    d = Double.parseDouble(stringBuffer.toString());
                    if (foundUnaryMinus) {
                        result = d;
                    } else {
                        result = -d;
                    }
                    if (foundPercentage) {
                        result /= 100.0d;
                    }
                    return Double.valueOf(result);
                } catch (NumberFormatException e) {
                    return null;
                }
            } else if (foundCurrency || foundUnaryMinus || foundUnaryPlus) {
                return null;
            } else {
                return ZERO;
            }
        }
        if (i >= len) {
            foundDecimalPoint = false;
            lastThousandsSeparatorIndex = -32768;
            stringBuffer = new StringBuffer(len);
            while (i < len) {
                ch = strText.charAt(i);
                if (Character.isDigit(ch)) {
                    switch (ch) {
                        case ' ':
                            remainingTextTrimmed = strText.substring(i).trim();
                            if (!remainingTextTrimmed.equals("%")) {
                                foundPercentage = true;
                                break;
                            } else if (remainingTextTrimmed.length() > 0) {
                                break;
                            } else {
                                return null;
                            }
                        case '%':
                            foundPercentage = true;
                            break;
                        case ',':
                            if (!foundDecimalPoint) {
                                return null;
                            }
                            if (i - lastThousandsSeparatorIndex < 4) {
                                lastThousandsSeparatorIndex = i;
                                break;
                            }
                            return null;
                        case '.':
                            if (!foundDecimalPoint) {
                                return null;
                            }
                            if (i - lastThousandsSeparatorIndex < 4) {
                                foundDecimalPoint = true;
                                stringBuffer.append('.');
                                break;
                            }
                            return null;
                        case 'E':
                        case 'e':
                            if (i - lastThousandsSeparatorIndex < 4) {
                                stringBuffer.append(strText.substring(i));
                                i = len;
                                break;
                            }
                            return null;
                        default:
                            return null;
                    }
                }
                stringBuffer.append(ch);
                i++;
            }
            if (foundDecimalPoint) {
            }
            d = Double.parseDouble(stringBuffer.toString());
            if (foundUnaryMinus) {
                result = d;
            } else {
                result = -d;
            }
            if (foundPercentage) {
                result /= 100.0d;
            }
            return Double.valueOf(result);
        }
        if (!foundCurrency) {
        }
        return null;
    }
}

package org.apache.poi.ss.formula.functions;

import java.util.Locale;
import org.apache.poi.ss.formula.eval.BoolEval;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.OperandResolver;
import org.apache.poi.ss.formula.eval.StringEval;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.usermodel.DataFormatter;

public abstract class TextFunction implements Function {
    public static final Function CHAR = new C11801();
    public static final Function CLEAN = new C11867();
    public static final Function CONCATENATE = new C11889();
    public static final Function EXACT = new Fixed2ArgFunction() {
        public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1) {
            try {
                return BoolEval.valueOf(TextFunction.evaluateStringArg(arg0, srcRowIndex, srcColumnIndex).equals(TextFunction.evaluateStringArg(arg1, srcRowIndex, srcColumnIndex)));
            } catch (EvaluationException e) {
                return e.getErrorEval();
            }
        }
    };
    public static final Function FIND = new SearchFind(true);
    public static final Function LEFT = new LeftRight(true);
    public static final Function LEN = new C11812();
    public static final Function LOWER = new C11823();
    public static final Function MID = new C11878();
    public static final Function PROPER = new C11845();
    public static final Function RIGHT = new LeftRight(false);
    public static final Function SEARCH = new SearchFind(false);
    public static final Function TEXT = new Fixed2ArgFunction() {
        public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1) {
            try {
                try {
                    return new StringEval(TextFunction.formatter.formatRawCellContents(TextFunction.evaluateDoubleArg(arg0, srcRowIndex, srcColumnIndex), -1, TextFunction.evaluateStringArg(arg1, srcRowIndex, srcColumnIndex)));
                } catch (Exception e) {
                    return ErrorEval.VALUE_INVALID;
                }
            } catch (EvaluationException e2) {
                return e2.getErrorEval();
            }
        }
    };
    public static final Function TRIM = new C11856();
    public static final Function UPPER = new C11834();
    protected static final DataFormatter formatter = new DataFormatter();

    static class C11801 extends Fixed1ArgFunction {
        C11801() {
        }

        public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0) {
            try {
                int arg = TextFunction.evaluateIntArg(arg0, srcRowIndex, srcColumnIndex);
                if (arg >= 0 && arg < 256) {
                    return new StringEval(String.valueOf((char) arg));
                }
                throw new EvaluationException(ErrorEval.VALUE_INVALID);
            } catch (EvaluationException e) {
                return e.getErrorEval();
            }
        }
    }

    private static abstract class SingleArgTextFunc extends Fixed1ArgFunction {
        protected abstract ValueEval evaluate(String str);

        protected SingleArgTextFunc() {
        }

        public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0) {
            try {
                return evaluate(TextFunction.evaluateStringArg(arg0, srcRowIndex, srcColumnIndex));
            } catch (EvaluationException e) {
                return e.getErrorEval();
            }
        }
    }

    static class C11812 extends SingleArgTextFunc {
        C11812() {
        }

        protected ValueEval evaluate(String arg) {
            return new NumberEval((double) arg.length());
        }
    }

    static class C11823 extends SingleArgTextFunc {
        C11823() {
        }

        protected ValueEval evaluate(String arg) {
            return new StringEval(arg.toLowerCase(Locale.ROOT));
        }
    }

    static class C11834 extends SingleArgTextFunc {
        C11834() {
        }

        protected ValueEval evaluate(String arg) {
            return new StringEval(arg.toUpperCase(Locale.ROOT));
        }
    }

    static class C11845 extends SingleArgTextFunc {
        C11845() {
        }

        protected ValueEval evaluate(String text) {
            StringBuilder sb = new StringBuilder();
            boolean shouldMakeUppercase = true;
            for (char ch : text.toCharArray()) {
                if (shouldMakeUppercase) {
                    sb.append(String.valueOf(ch).toUpperCase(Locale.ROOT));
                } else {
                    sb.append(String.valueOf(ch).toLowerCase(Locale.ROOT));
                }
                if (Character.isLetter(ch)) {
                    shouldMakeUppercase = false;
                } else {
                    shouldMakeUppercase = true;
                }
            }
            return new StringEval(sb.toString());
        }
    }

    static class C11856 extends SingleArgTextFunc {
        C11856() {
        }

        protected ValueEval evaluate(String arg) {
            return new StringEval(arg.trim());
        }
    }

    static class C11867 extends SingleArgTextFunc {
        C11867() {
        }

        protected ValueEval evaluate(String arg) {
            StringBuilder result = new StringBuilder();
            for (char c : arg.toCharArray()) {
                if (isPrintable(c)) {
                    result.append(c);
                }
            }
            return new StringEval(result.toString());
        }

        private boolean isPrintable(char c) {
            return c >= ' ';
        }
    }

    static class C11878 extends Fixed3ArgFunction {
        C11878() {
        }

        public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2) {
            try {
                String text = TextFunction.evaluateStringArg(arg0, srcRowIndex, srcColumnIndex);
                int startCharNum = TextFunction.evaluateIntArg(arg1, srcRowIndex, srcColumnIndex);
                int numChars = TextFunction.evaluateIntArg(arg2, srcRowIndex, srcColumnIndex);
                int startIx = startCharNum - 1;
                if (startIx < 0) {
                    return ErrorEval.VALUE_INVALID;
                }
                if (numChars < 0) {
                    return ErrorEval.VALUE_INVALID;
                }
                int len = text.length();
                if (numChars < 0 || startIx > len) {
                    return new StringEval("");
                }
                return new StringEval(text.substring(startIx, Math.min(startIx + numChars, len)));
            } catch (EvaluationException e) {
                return e.getErrorEval();
            }
        }
    }

    static class C11889 implements Function {
        C11889() {
        }

        public ValueEval evaluate(ValueEval[] args, int srcRowIndex, int srcColumnIndex) {
            StringBuilder sb = new StringBuilder();
            ValueEval[] arr$ = args;
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                try {
                    sb.append(TextFunction.evaluateStringArg(arr$[i$], srcRowIndex, srcColumnIndex));
                    i$++;
                } catch (EvaluationException e) {
                    return e.getErrorEval();
                }
            }
            return new StringEval(sb.toString());
        }
    }

    private static final class LeftRight extends Var1or2ArgFunction {
        private static final ValueEval DEFAULT_ARG1 = new NumberEval(1.0d);
        private final boolean _isLeft;

        protected LeftRight(boolean isLeft) {
            this._isLeft = isLeft;
        }

        public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0) {
            return evaluate(srcRowIndex, srcColumnIndex, arg0, DEFAULT_ARG1);
        }

        public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1) {
            try {
                String arg = TextFunction.evaluateStringArg(arg0, srcRowIndex, srcColumnIndex);
                int index = TextFunction.evaluateIntArg(arg1, srcRowIndex, srcColumnIndex);
                if (index < 0) {
                    return ErrorEval.VALUE_INVALID;
                }
                String result;
                if (this._isLeft) {
                    result = arg.substring(0, Math.min(arg.length(), index));
                } else {
                    result = arg.substring(Math.max(0, arg.length() - index));
                }
                return new StringEval(result);
            } catch (EvaluationException e) {
                return e.getErrorEval();
            }
        }
    }

    private static final class SearchFind extends Var2or3ArgFunction {
        private final boolean _isCaseSensitive;

        public SearchFind(boolean isCaseSensitive) {
            this._isCaseSensitive = isCaseSensitive;
        }

        public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1) {
            try {
                return eval(TextFunction.evaluateStringArg(arg1, srcRowIndex, srcColumnIndex), TextFunction.evaluateStringArg(arg0, srcRowIndex, srcColumnIndex), 0);
            } catch (EvaluationException e) {
                return e.getErrorEval();
            }
        }

        public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2) {
            try {
                String needle = TextFunction.evaluateStringArg(arg0, srcRowIndex, srcColumnIndex);
                String haystack = TextFunction.evaluateStringArg(arg1, srcRowIndex, srcColumnIndex);
                int startpos = TextFunction.evaluateIntArg(arg2, srcRowIndex, srcColumnIndex) - 1;
                if (startpos < 0) {
                    return ErrorEval.VALUE_INVALID;
                }
                return eval(haystack, needle, startpos);
            } catch (EvaluationException e) {
                return e.getErrorEval();
            }
        }

        private ValueEval eval(String haystack, String needle, int startIndex) {
            int result;
            if (this._isCaseSensitive) {
                result = haystack.indexOf(needle, startIndex);
            } else {
                result = haystack.toUpperCase(Locale.ROOT).indexOf(needle.toUpperCase(Locale.ROOT), startIndex);
            }
            if (result == -1) {
                return ErrorEval.VALUE_INVALID;
            }
            return new NumberEval((double) (result + 1));
        }
    }

    protected abstract ValueEval evaluateFunc(ValueEval[] valueEvalArr, int i, int i2) throws EvaluationException;

    protected static String evaluateStringArg(ValueEval eval, int srcRow, int srcCol) throws EvaluationException {
        return OperandResolver.coerceValueToString(OperandResolver.getSingleValue(eval, srcRow, srcCol));
    }

    protected static int evaluateIntArg(ValueEval arg, int srcCellRow, int srcCellCol) throws EvaluationException {
        return OperandResolver.coerceValueToInt(OperandResolver.getSingleValue(arg, srcCellRow, srcCellCol));
    }

    protected static double evaluateDoubleArg(ValueEval arg, int srcCellRow, int srcCellCol) throws EvaluationException {
        return OperandResolver.coerceValueToDouble(OperandResolver.getSingleValue(arg, srcCellRow, srcCellCol));
    }

    public final ValueEval evaluate(ValueEval[] args, int srcCellRow, int srcCellCol) {
        try {
            return evaluateFunc(args, srcCellRow, srcCellCol);
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }
}

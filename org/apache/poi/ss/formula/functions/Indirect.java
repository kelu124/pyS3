package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.FormulaParseException;
import org.apache.poi.ss.formula.FormulaParser;
import org.apache.poi.ss.formula.FormulaParsingWorkbook;
import org.apache.poi.ss.formula.OperationEvaluationContext;
import org.apache.poi.ss.formula.eval.BlankEval;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.formula.eval.MissingArgEval;
import org.apache.poi.ss.formula.eval.OperandResolver;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.usermodel.Table;

public final class Indirect implements FreeRefFunction {
    public static final FreeRefFunction instance = new Indirect();

    private Indirect() {
    }

    public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec) {
        if (args.length < 1) {
            return ErrorEval.VALUE_INVALID;
        }
        try {
            boolean isA1style;
            String text = OperandResolver.coerceValueToString(OperandResolver.getSingleValue(args[0], ec.getRowIndex(), ec.getColumnIndex()));
            switch (args.length) {
                case 1:
                    isA1style = true;
                    break;
                case 2:
                    isA1style = evaluateBooleanArg(args[1], ec);
                    break;
                default:
                    return ErrorEval.VALUE_INVALID;
            }
            return evaluateIndirect(ec, text, isA1style);
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }

    private static boolean evaluateBooleanArg(ValueEval arg, OperationEvaluationContext ec) throws EvaluationException {
        ValueEval ve = OperandResolver.getSingleValue(arg, ec.getRowIndex(), ec.getColumnIndex());
        if (ve == BlankEval.instance || ve == MissingArgEval.instance) {
            return false;
        }
        return OperandResolver.coerceValueToBoolean(ve, false).booleanValue();
    }

    private static ValueEval evaluateIndirect(OperationEvaluationContext ec, String text, boolean isA1style) {
        String workbookName;
        String sheetName;
        String refText;
        int plingPos = text.lastIndexOf(33);
        if (plingPos < 0) {
            workbookName = null;
            sheetName = null;
            refText = text;
        } else {
            String[] parts = parseWorkbookAndSheetName(text.subSequence(0, plingPos));
            if (parts == null) {
                return ErrorEval.REF_INVALID;
            }
            workbookName = parts[0];
            sheetName = parts[1];
            refText = text.substring(plingPos + 1);
        }
        if (Table.isStructuredReference.matcher(refText).matches()) {
            try {
                return ec.getArea3DEval(FormulaParser.parseStructuredReference(refText, (FormulaParsingWorkbook) ec.getWorkbook(), ec.getRowIndex()));
            } catch (FormulaParseException e) {
                return ErrorEval.REF_INVALID;
            }
        }
        String refStrPart1;
        String refStrPart2;
        int colonPos = refText.indexOf(58);
        if (colonPos < 0) {
            refStrPart1 = refText.trim();
            refStrPart2 = null;
        } else {
            refStrPart1 = refText.substring(0, colonPos).trim();
            refStrPart2 = refText.substring(colonPos + 1).trim();
        }
        return ec.getDynamicReference(workbookName, sheetName, refStrPart1, refStrPart2, isA1style);
    }

    private static String[] parseWorkbookAndSheetName(CharSequence text) {
        int lastIx = text.length() - 1;
        if (lastIx < 0 || canTrim(text)) {
            return null;
        }
        char firstChar = text.charAt(0);
        if (Character.isWhitespace(firstChar)) {
            return null;
        }
        int rbPos;
        if (firstChar == '\'') {
            if (text.charAt(lastIx) != '\'') {
                return null;
            }
            firstChar = text.charAt(1);
            if (Character.isWhitespace(firstChar)) {
                return null;
            }
            String wbName;
            int sheetStartPos;
            if (firstChar == '[') {
                rbPos = text.toString().lastIndexOf(93);
                if (rbPos < 0) {
                    return null;
                }
                wbName = unescapeString(text.subSequence(2, rbPos));
                if (wbName == null || canTrim(wbName)) {
                    return null;
                }
                sheetStartPos = rbPos + 1;
            } else {
                wbName = null;
                sheetStartPos = 1;
            }
            if (unescapeString(text.subSequence(sheetStartPos, lastIx)) == null) {
                return null;
            }
            return new String[]{wbName, unescapeString(text.subSequence(sheetStartPos, lastIx))};
        } else if (firstChar == '[') {
            rbPos = text.toString().lastIndexOf(93);
            if (rbPos < 0) {
                return null;
            }
            if (canTrim(text.subSequence(1, rbPos))) {
                return null;
            }
            if (canTrim(text.subSequence(rbPos + 1, text.length()))) {
                return null;
            }
            return new String[]{wbName.toString(), text.subSequence(rbPos + 1, text.length()).toString()};
        } else {
            return new String[]{null, text.toString()};
        }
    }

    private static String unescapeString(CharSequence text) {
        int len = text.length();
        StringBuilder sb = new StringBuilder(len);
        int i = 0;
        while (i < len) {
            char ch = text.charAt(i);
            if (ch == '\'') {
                i++;
                if (i >= len) {
                    return null;
                }
                ch = text.charAt(i);
                if (ch != '\'') {
                    return null;
                }
            }
            sb.append(ch);
            i++;
        }
        return sb.toString();
    }

    private static boolean canTrim(CharSequence text) {
        int lastIx = text.length() - 1;
        if (lastIx < 0) {
            return false;
        }
        if (Character.isWhitespace(text.charAt(0))) {
            return true;
        }
        if (Character.isWhitespace(text.charAt(lastIx))) {
            return true;
        }
        return false;
    }
}

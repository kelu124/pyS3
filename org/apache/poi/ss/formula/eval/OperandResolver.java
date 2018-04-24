package org.apache.poi.ss.formula.eval;

import com.itextpdf.text.pdf.PdfBoolean;
import java.util.regex.Pattern;

public final class OperandResolver {
    private static final String Digits = "(\\p{Digit}+)";
    private static final String Exp = "[eE][+-]?(\\p{Digit}+)";
    private static final String fpRegex = "[\\x00-\\x20]*[+-]?(((((\\p{Digit}+)(\\.)?((\\p{Digit}+)?)([eE][+-]?(\\p{Digit}+))?)|(\\.((\\p{Digit}+))([eE][+-]?(\\p{Digit}+))?))))[\\x00-\\x20]*";

    private OperandResolver() {
    }

    public static ValueEval getSingleValue(ValueEval arg, int srcCellRow, int srcCellCol) throws EvaluationException {
        ValueEval result;
        if (arg instanceof RefEval) {
            result = chooseSingleElementFromRef((RefEval) arg);
        } else if (arg instanceof AreaEval) {
            result = chooseSingleElementFromArea((AreaEval) arg, srcCellRow, srcCellCol);
        } else {
            result = arg;
        }
        if (!(result instanceof ErrorEval)) {
            return result;
        }
        throw new EvaluationException((ErrorEval) result);
    }

    public static ValueEval chooseSingleElementFromArea(AreaEval ae, int srcCellRow, int srcCellCol) throws EvaluationException {
        ValueEval result = chooseSingleElementFromAreaInternal(ae, srcCellRow, srcCellCol);
        if (!(result instanceof ErrorEval)) {
            return result;
        }
        throw new EvaluationException((ErrorEval) result);
    }

    private static ValueEval chooseSingleElementFromAreaInternal(AreaEval ae, int srcCellRow, int srcCellCol) throws EvaluationException {
        if (ae.isColumn()) {
            if (ae.isRow()) {
                return ae.getRelativeValue(0, 0);
            }
            if (ae.containsRow(srcCellRow)) {
                return ae.getAbsoluteValue(srcCellRow, ae.getFirstColumn());
            }
            throw EvaluationException.invalidValue();
        } else if (ae.isRow()) {
            if (ae.containsColumn(srcCellCol)) {
                return ae.getAbsoluteValue(ae.getFirstRow(), srcCellCol);
            }
            throw EvaluationException.invalidValue();
        } else if (ae.containsRow(srcCellRow) && ae.containsColumn(srcCellCol)) {
            return ae.getAbsoluteValue(ae.getFirstRow(), ae.getFirstColumn());
        } else {
            throw EvaluationException.invalidValue();
        }
    }

    private static ValueEval chooseSingleElementFromRef(RefEval ref) {
        return ref.getInnerValueEval(ref.getFirstSheetIndex());
    }

    public static int coerceValueToInt(ValueEval ev) throws EvaluationException {
        if (ev == BlankEval.instance) {
            return 0;
        }
        return (int) Math.floor(coerceValueToDouble(ev));
    }

    public static double coerceValueToDouble(ValueEval ev) throws EvaluationException {
        if (ev == BlankEval.instance) {
            return 0.0d;
        }
        if (ev instanceof NumericValueEval) {
            return ((NumericValueEval) ev).getNumberValue();
        }
        if (ev instanceof StringEval) {
            Double dd = parseDouble(((StringEval) ev).getStringValue());
            if (dd != null) {
                return dd.doubleValue();
            }
            throw EvaluationException.invalidValue();
        }
        throw new RuntimeException("Unexpected arg eval type (" + ev.getClass().getName() + ")");
    }

    public static Double parseDouble(String pText) {
        Double d = null;
        if (Pattern.matches(fpRegex, pText)) {
            try {
                d = Double.valueOf(Double.parseDouble(pText));
            } catch (NumberFormatException e) {
            }
        }
        return d;
    }

    public static String coerceValueToString(ValueEval ve) {
        if (ve instanceof StringValueEval) {
            return ((StringValueEval) ve).getStringValue();
        }
        if (ve == BlankEval.instance) {
            return "";
        }
        throw new IllegalArgumentException("Unexpected eval class (" + ve.getClass().getName() + ")");
    }

    public static Boolean coerceValueToBoolean(ValueEval ve, boolean stringsAreBlanks) throws EvaluationException {
        if (ve == null || ve == BlankEval.instance) {
            return null;
        }
        if (ve instanceof BoolEval) {
            return Boolean.valueOf(((BoolEval) ve).getBooleanValue());
        }
        if (ve == BlankEval.instance) {
            return null;
        }
        if (ve instanceof StringEval) {
            if (stringsAreBlanks) {
                return null;
            }
            String str = ((StringEval) ve).getStringValue();
            if (str.equalsIgnoreCase(PdfBoolean.TRUE)) {
                return Boolean.TRUE;
            }
            if (str.equalsIgnoreCase(PdfBoolean.FALSE)) {
                return Boolean.FALSE;
            }
            throw new EvaluationException(ErrorEval.VALUE_INVALID);
        } else if (ve instanceof NumericValueEval) {
            double d = ((NumericValueEval) ve).getNumberValue();
            if (Double.isNaN(d)) {
                throw new EvaluationException(ErrorEval.VALUE_INVALID);
            }
            return Boolean.valueOf(d != 0.0d);
        } else if (ve instanceof ErrorEval) {
            throw new EvaluationException((ErrorEval) ve);
        } else {
            throw new RuntimeException("Unexpected eval (" + ve.getClass().getName() + ")");
        }
    }
}

package org.apache.poi.ss.formula.eval;

import java.util.HashMap;
import java.util.Map;
import org.apache.poi.ss.usermodel.FormulaError;

public final class ErrorEval implements ValueEval {
    public static final ErrorEval CIRCULAR_REF_ERROR = new ErrorEval(FormulaError.CIRCULAR_REF);
    public static final ErrorEval DIV_ZERO = new ErrorEval(FormulaError.DIV0);
    public static final ErrorEval FUNCTION_NOT_IMPLEMENTED = new ErrorEval(FormulaError.FUNCTION_NOT_IMPLEMENTED);
    public static final ErrorEval NA = new ErrorEval(FormulaError.NA);
    public static final ErrorEval NAME_INVALID = new ErrorEval(FormulaError.NAME);
    public static final ErrorEval NULL_INTERSECTION = new ErrorEval(FormulaError.NULL);
    public static final ErrorEval NUM_ERROR = new ErrorEval(FormulaError.NUM);
    public static final ErrorEval REF_INVALID = new ErrorEval(FormulaError.REF);
    public static final ErrorEval VALUE_INVALID = new ErrorEval(FormulaError.VALUE);
    private static final Map<FormulaError, ErrorEval> evals = new HashMap();
    private FormulaError _error;

    public static ErrorEval valueOf(int errorCode) {
        ErrorEval eval = (ErrorEval) evals.get(FormulaError.forInt(errorCode));
        if (eval != null) {
            return eval;
        }
        throw new RuntimeException("Unhandled error type for code " + errorCode);
    }

    public static String getText(int errorCode) {
        if (FormulaError.isValidCode(errorCode)) {
            return FormulaError.forInt(errorCode).getString();
        }
        return "~non~std~err(" + errorCode + ")~";
    }

    private ErrorEval(FormulaError error) {
        this._error = error;
        evals.put(error, this);
    }

    public int getErrorCode() {
        return this._error.getLongCode();
    }

    public String getErrorString() {
        return this._error.getString();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(64);
        sb.append(getClass().getName()).append(" [");
        sb.append(this._error.getString());
        sb.append("]");
        return sb.toString();
    }
}

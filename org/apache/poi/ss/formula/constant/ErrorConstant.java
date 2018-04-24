package org.apache.poi.ss.formula.constant;

import org.apache.poi.ss.usermodel.FormulaError;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

public class ErrorConstant {
    private static final ErrorConstant DIV_0 = new ErrorConstant(FormulaError.DIV0.getCode());
    private static final ErrorConstant NA = new ErrorConstant(FormulaError.NA.getCode());
    private static final ErrorConstant NAME = new ErrorConstant(FormulaError.NAME.getCode());
    private static final ErrorConstant NULL = new ErrorConstant(FormulaError.NULL.getCode());
    private static final ErrorConstant NUM = new ErrorConstant(FormulaError.NUM.getCode());
    private static final ErrorConstant REF = new ErrorConstant(FormulaError.REF.getCode());
    private static final ErrorConstant VALUE = new ErrorConstant(FormulaError.VALUE.getCode());
    private static final POILogger logger = POILogFactory.getLogger(ErrorConstant.class);
    private final int _errorCode;

    static /* synthetic */ class C11181 {
        static final /* synthetic */ int[] $SwitchMap$org$apache$poi$ss$usermodel$FormulaError = new int[FormulaError.values().length];

        static {
            try {
                $SwitchMap$org$apache$poi$ss$usermodel$FormulaError[FormulaError.NULL.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$apache$poi$ss$usermodel$FormulaError[FormulaError.DIV0.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$apache$poi$ss$usermodel$FormulaError[FormulaError.VALUE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$apache$poi$ss$usermodel$FormulaError[FormulaError.REF.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$org$apache$poi$ss$usermodel$FormulaError[FormulaError.NAME.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$org$apache$poi$ss$usermodel$FormulaError[FormulaError.NUM.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$org$apache$poi$ss$usermodel$FormulaError[FormulaError.NA.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
        }
    }

    private ErrorConstant(int errorCode) {
        this._errorCode = errorCode;
    }

    public int getErrorCode() {
        return this._errorCode;
    }

    public String getText() {
        if (FormulaError.isValidCode(this._errorCode)) {
            return FormulaError.forInt(this._errorCode).getString();
        }
        return "unknown error code (" + this._errorCode + ")";
    }

    public static ErrorConstant valueOf(int errorCode) {
        if (FormulaError.isValidCode(errorCode)) {
            switch (C11181.$SwitchMap$org$apache$poi$ss$usermodel$FormulaError[FormulaError.forInt(errorCode).ordinal()]) {
                case 1:
                    return NULL;
                case 2:
                    return DIV_0;
                case 3:
                    return VALUE;
                case 4:
                    return REF;
                case 5:
                    return NAME;
                case 6:
                    return NUM;
                case 7:
                    return NA;
            }
        }
        logger.log(5, new Object[]{"Warning - unexpected error code (" + errorCode + ")"});
        return new ErrorConstant(errorCode);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(64);
        sb.append(getClass().getName()).append(" [");
        sb.append(getText());
        sb.append("]");
        return sb.toString();
    }
}

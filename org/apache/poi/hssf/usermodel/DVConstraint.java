package org.apache.poi.hssf.usermodel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;
import org.apache.poi.hssf.model.HSSFFormulaParser;
import org.apache.poi.hssf.record.DVRecord;
import org.apache.poi.ss.formula.FormulaRenderer;
import org.apache.poi.ss.formula.FormulaRenderingWorkbook;
import org.apache.poi.ss.formula.FormulaType;
import org.apache.poi.ss.formula.ptg.NumberPtg;
import org.apache.poi.ss.formula.ptg.Ptg;
import org.apache.poi.ss.formula.ptg.StringPtg;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationConstraint.OperatorType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.util.LocaleUtil;

public class DVConstraint implements DataValidationConstraint {
    private String[] _explicitListValues;
    private String _formula1;
    private String _formula2;
    private int _operator;
    private final int _validationType;
    private Double _value1;
    private Double _value2;

    static final class FormulaPair {
        private final Ptg[] _formula1;
        private final Ptg[] _formula2;

        FormulaPair(Ptg[] formula1, Ptg[] formula2) {
            Ptg[] ptgArr = null;
            this._formula1 = formula1 == null ? null : (Ptg[]) formula1.clone();
            if (formula2 != null) {
                ptgArr = (Ptg[]) formula2.clone();
            }
            this._formula2 = ptgArr;
        }

        public Ptg[] getFormula1() {
            return this._formula1;
        }

        public Ptg[] getFormula2() {
            return this._formula2;
        }
    }

    private static class FormulaValuePair {
        private String _formula;
        private String _value;

        private FormulaValuePair() {
        }

        public String formula() {
            return this._formula;
        }

        public Double value() {
            if (this._value == null) {
                return null;
            }
            return new Double(this._value);
        }

        public String string() {
            if (this._formula != null) {
                return this._formula;
            }
            if (this._value != null) {
                return this._value;
            }
            return null;
        }
    }

    private DVConstraint(int validationType, int comparisonOperator, String formulaA, String formulaB, Double value1, Double value2, String[] excplicitListValues) {
        this._validationType = validationType;
        this._operator = comparisonOperator;
        this._formula1 = formulaA;
        this._formula2 = formulaB;
        this._value1 = value1;
        this._value2 = value2;
        this._explicitListValues = excplicitListValues == null ? null : (String[]) excplicitListValues.clone();
    }

    private DVConstraint(String listFormula, String[] excplicitListValues) {
        this(3, 0, listFormula, null, null, null, excplicitListValues);
    }

    public static DVConstraint createNumericConstraint(int validationType, int comparisonOperator, String expr1, String expr2) {
        Double value1;
        Double value2;
        switch (validationType) {
            case 0:
                if (!(expr1 == null && expr2 == null)) {
                    throw new IllegalArgumentException("expr1 and expr2 must be null for validation type 'any'");
                }
            case 1:
            case 2:
            case 6:
                if (expr1 != null) {
                    OperatorType.validateSecondArg(comparisonOperator, expr2);
                    break;
                }
                throw new IllegalArgumentException("expr1 must be supplied");
            default:
                throw new IllegalArgumentException("Validation Type (" + validationType + ") not supported with this method");
        }
        String formula1 = getFormulaFromTextExpression(expr1);
        if (formula1 == null) {
            value1 = convertNumber(expr1);
        } else {
            value1 = null;
        }
        String formula2 = getFormulaFromTextExpression(expr2);
        if (formula2 == null) {
            value2 = convertNumber(expr2);
        } else {
            value2 = null;
        }
        return new DVConstraint(validationType, comparisonOperator, formula1, formula2, value1, value2, null);
    }

    public static DVConstraint createFormulaListConstraint(String listFormula) {
        return new DVConstraint(listFormula, null);
    }

    public static DVConstraint createExplicitListConstraint(String[] explicitListValues) {
        return new DVConstraint(null, explicitListValues);
    }

    public static DVConstraint createTimeConstraint(int comparisonOperator, String expr1, String expr2) {
        if (expr1 == null) {
            throw new IllegalArgumentException("expr1 must be supplied");
        }
        Double value1;
        Double value2;
        OperatorType.validateSecondArg(comparisonOperator, expr1);
        String formula1 = getFormulaFromTextExpression(expr1);
        if (formula1 == null) {
            value1 = convertTime(expr1);
        } else {
            value1 = null;
        }
        String formula2 = getFormulaFromTextExpression(expr2);
        if (formula2 == null) {
            value2 = convertTime(expr2);
        } else {
            value2 = null;
        }
        return new DVConstraint(5, comparisonOperator, formula1, formula2, value1, value2, null);
    }

    public static DVConstraint createDateConstraint(int comparisonOperator, String expr1, String expr2, String dateFormat) {
        if (expr1 == null) {
            throw new IllegalArgumentException("expr1 must be supplied");
        }
        Double value1;
        Double value2;
        OperatorType.validateSecondArg(comparisonOperator, expr2);
        SimpleDateFormat df = null;
        if (dateFormat != null) {
            df = new SimpleDateFormat(dateFormat, LocaleUtil.getUserLocale());
            df.setTimeZone(LocaleUtil.getUserTimeZone());
        }
        String formula1 = getFormulaFromTextExpression(expr1);
        if (formula1 == null) {
            value1 = convertDate(expr1, df);
        } else {
            value1 = null;
        }
        String formula2 = getFormulaFromTextExpression(expr2);
        if (formula2 == null) {
            value2 = convertDate(expr2, df);
        } else {
            value2 = null;
        }
        return new DVConstraint(4, comparisonOperator, formula1, formula2, value1, value2, null);
    }

    private static String getFormulaFromTextExpression(String textExpr) {
        if (textExpr == null) {
            return null;
        }
        if (textExpr.length() < 1) {
            throw new IllegalArgumentException("Empty string is not a valid formula/value expression");
        } else if (textExpr.charAt(0) == '=') {
            return textExpr.substring(1);
        } else {
            return null;
        }
    }

    private static Double convertNumber(String numberStr) {
        if (numberStr == null) {
            return null;
        }
        try {
            return new Double(numberStr);
        } catch (NumberFormatException e) {
            throw new RuntimeException("The supplied text '" + numberStr + "' could not be parsed as a number");
        }
    }

    private static Double convertTime(String timeStr) {
        if (timeStr == null) {
            return null;
        }
        return new Double(DateUtil.convertTime(timeStr));
    }

    private static Double convertDate(String dateStr, SimpleDateFormat dateFormat) {
        if (dateStr == null) {
            return null;
        }
        Date dateVal;
        if (dateFormat == null) {
            dateVal = DateUtil.parseYYYYMMDDDate(dateStr);
        } else {
            try {
                dateVal = dateFormat.parse(dateStr);
            } catch (ParseException e) {
                throw new RuntimeException("Failed to parse date '" + dateStr + "' using specified format '" + dateFormat + "'", e);
            }
        }
        return new Double(DateUtil.getExcelDate(dateVal));
    }

    public static DVConstraint createCustomFormulaConstraint(String formula) {
        if (formula != null) {
            return new DVConstraint(7, 0, formula, null, null, null, null);
        }
        throw new IllegalArgumentException("formula must be supplied");
    }

    public int getValidationType() {
        return this._validationType;
    }

    public boolean isListValidationType() {
        return this._validationType == 3;
    }

    public boolean isExplicitList() {
        return this._validationType == 3 && this._explicitListValues != null;
    }

    public int getOperator() {
        return this._operator;
    }

    public void setOperator(int operator) {
        this._operator = operator;
    }

    public String[] getExplicitListValues() {
        return this._explicitListValues;
    }

    public void setExplicitListValues(String[] explicitListValues) {
        if (this._validationType != 3) {
            throw new RuntimeException("Cannot setExplicitListValues on non-list constraint");
        }
        this._formula1 = null;
        this._explicitListValues = explicitListValues;
    }

    public String getFormula1() {
        return this._formula1;
    }

    public void setFormula1(String formula1) {
        this._value1 = null;
        this._explicitListValues = null;
        this._formula1 = formula1;
    }

    public String getFormula2() {
        return this._formula2;
    }

    public void setFormula2(String formula2) {
        this._value2 = null;
        this._formula2 = formula2;
    }

    public Double getValue1() {
        return this._value1;
    }

    public void setValue1(double value1) {
        this._formula1 = null;
        this._value1 = new Double(value1);
    }

    public Double getValue2() {
        return this._value2;
    }

    public void setValue2(double value2) {
        this._formula2 = null;
        this._value2 = new Double(value2);
    }

    FormulaPair createFormulas(HSSFSheet sheet) {
        Ptg[] formula1;
        Ptg[] formula2;
        if (isListValidationType()) {
            formula1 = createListFormula(sheet);
            formula2 = Ptg.EMPTY_PTG_ARRAY;
        } else {
            formula1 = convertDoubleFormula(this._formula1, this._value1, sheet);
            formula2 = convertDoubleFormula(this._formula2, this._value2, sheet);
        }
        return new FormulaPair(formula1, formula2);
    }

    private Ptg[] createListFormula(HSSFSheet sheet) {
        if (this._explicitListValues == null) {
            HSSFWorkbook wb = sheet.getWorkbook();
            return HSSFFormulaParser.parse(this._formula1, wb, FormulaType.DATAVALIDATION_LIST, wb.getSheetIndex(sheet));
        }
        StringBuffer sb = new StringBuffer(this._explicitListValues.length * 16);
        for (int i = 0; i < this._explicitListValues.length; i++) {
            if (i > 0) {
                sb.append('\u0000');
            }
            sb.append(this._explicitListValues[i]);
        }
        return new Ptg[]{new StringPtg(sb.toString())};
    }

    private static Ptg[] convertDoubleFormula(String formula, Double value, HSSFSheet sheet) {
        if (formula == null) {
            if (value == null) {
                return Ptg.EMPTY_PTG_ARRAY;
            }
            return new Ptg[]{new NumberPtg(value.doubleValue())};
        } else if (value != null) {
            throw new IllegalStateException("Both formula and value cannot be present");
        } else {
            HSSFWorkbook wb = sheet.getWorkbook();
            return HSSFFormulaParser.parse(formula, wb, FormulaType.CELL, wb.getSheetIndex(sheet));
        }
    }

    static DVConstraint createDVConstraint(DVRecord dvRecord, FormulaRenderingWorkbook book) {
        switch (dvRecord.getDataType()) {
            case 0:
                return new DVConstraint(0, dvRecord.getConditionOperator(), null, null, null, null, null);
            case 1:
            case 2:
            case 4:
            case 5:
            case 6:
                FormulaValuePair pair1 = toFormulaString(dvRecord.getFormula1(), book);
                FormulaValuePair pair2 = toFormulaString(dvRecord.getFormula2(), book);
                return new DVConstraint(dvRecord.getDataType(), dvRecord.getConditionOperator(), pair1.formula(), pair2.formula(), pair1.value(), pair2.value(), null);
            case 3:
                if (!dvRecord.getListExplicitFormula()) {
                    return createFormulaListConstraint(toFormulaString(dvRecord.getFormula1(), book).string());
                }
                String values = toFormulaString(dvRecord.getFormula1(), book).string();
                if (values.startsWith("\"")) {
                    values = values.substring(1);
                }
                if (values.endsWith("\"")) {
                    values = values.substring(0, values.length() - 1);
                }
                return createExplicitListConstraint(values.split(Pattern.quote("\u0000")));
            case 7:
                return createCustomFormulaConstraint(toFormulaString(dvRecord.getFormula1(), book).string());
            default:
                throw new UnsupportedOperationException("validationType=" + dvRecord.getDataType());
        }
    }

    private static FormulaValuePair toFormulaString(Ptg[] ptgs, FormulaRenderingWorkbook book) {
        FormulaValuePair pair = new FormulaValuePair();
        if (ptgs != null && ptgs.length > 0) {
            String string = FormulaRenderer.toFormulaString(book, ptgs);
            if (ptgs.length == 1 && ptgs[0].getClass() == NumberPtg.class) {
                pair._value = string;
            } else {
                pair._formula = string;
            }
        }
        return pair;
    }
}

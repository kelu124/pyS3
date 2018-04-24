package org.apache.poi.hssf.model;

import org.apache.poi.hssf.usermodel.HSSFEvaluationWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.FormulaParseException;
import org.apache.poi.ss.formula.FormulaParser;
import org.apache.poi.ss.formula.FormulaParsingWorkbook;
import org.apache.poi.ss.formula.FormulaRenderer;
import org.apache.poi.ss.formula.FormulaType;
import org.apache.poi.ss.formula.ptg.Ptg;
import org.apache.poi.util.Internal;
import org.apache.poi.util.Removal;

@Internal
public final class HSSFFormulaParser {
    private static FormulaParsingWorkbook createParsingWorkbook(HSSFWorkbook book) {
        return HSSFEvaluationWorkbook.create(book);
    }

    private HSSFFormulaParser() {
    }

    public static Ptg[] parse(String formula, HSSFWorkbook workbook) throws FormulaParseException {
        return parse(formula, workbook, FormulaType.CELL);
    }

    @Removal(version = "3.17")
    public static Ptg[] parse(String formula, HSSFWorkbook workbook, int formulaType) throws FormulaParseException {
        return parse(formula, workbook, FormulaType.forInt(formulaType));
    }

    public static Ptg[] parse(String formula, HSSFWorkbook workbook, FormulaType formulaType) throws FormulaParseException {
        return parse(formula, workbook, formulaType, -1);
    }

    @Removal(version = "3.17")
    public static Ptg[] parse(String formula, HSSFWorkbook workbook, int formulaType, int sheetIndex) throws FormulaParseException {
        return parse(formula, workbook, FormulaType.forInt(formulaType), sheetIndex);
    }

    public static Ptg[] parse(String formula, HSSFWorkbook workbook, FormulaType formulaType, int sheetIndex) throws FormulaParseException {
        return FormulaParser.parse(formula, createParsingWorkbook(workbook), formulaType, sheetIndex);
    }

    public static String toFormulaString(HSSFWorkbook book, Ptg[] ptgs) {
        return FormulaRenderer.toFormulaString(HSSFEvaluationWorkbook.create(book), ptgs);
    }
}

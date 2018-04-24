package org.apache.poi.ss.formula.ptg;

import org.apache.poi.ss.formula.EvaluationWorkbook$ExternalSheet;
import org.apache.poi.ss.formula.EvaluationWorkbook$ExternalSheetRange;
import org.apache.poi.ss.formula.FormulaRenderingWorkbook;
import org.apache.poi.ss.formula.SheetNameFormatter;

final class ExternSheetNameResolver {
    private ExternSheetNameResolver() {
    }

    public static String prependSheetName(FormulaRenderingWorkbook book, int field_1_index_extern_sheet, String cellRefText) {
        StringBuffer sb;
        EvaluationWorkbook$ExternalSheet externalSheet = book.getExternalSheet(field_1_index_extern_sheet);
        if (externalSheet != null) {
            String wbName = externalSheet.getWorkbookName();
            String sheetName = externalSheet.getSheetName();
            if (wbName != null) {
                sb = new StringBuffer(((wbName.length() + sheetName.length()) + cellRefText.length()) + 4);
                SheetNameFormatter.appendFormat(sb, wbName, sheetName);
            } else {
                sb = new StringBuffer((sheetName.length() + cellRefText.length()) + 4);
                SheetNameFormatter.appendFormat(sb, sheetName);
            }
            if (externalSheet instanceof EvaluationWorkbook$ExternalSheetRange) {
                EvaluationWorkbook$ExternalSheetRange r = (EvaluationWorkbook$ExternalSheetRange) externalSheet;
                if (!r.getFirstSheetName().equals(r.getLastSheetName())) {
                    sb.append(':');
                    SheetNameFormatter.appendFormat(sb, r.getLastSheetName());
                }
            }
        } else {
            String firstSheetName = book.getSheetFirstNameByExternSheet(field_1_index_extern_sheet);
            String lastSheetName = book.getSheetLastNameByExternSheet(field_1_index_extern_sheet);
            sb = new StringBuffer((firstSheetName.length() + cellRefText.length()) + 4);
            if (firstSheetName.length() < 1) {
                sb.append("#REF");
            } else {
                SheetNameFormatter.appendFormat(sb, firstSheetName);
                if (!firstSheetName.equals(lastSheetName)) {
                    sb.append(':');
                    sb.append(lastSheetName);
                }
            }
        }
        sb.append('!');
        sb.append(cellRefText);
        return sb.toString();
    }
}

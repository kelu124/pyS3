package org.apache.poi.ss.util;

import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.text.AttributedString;
import java.util.Locale;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.Internal;

public class SheetUtil {
    private static final char defaultChar = '0';
    private static final FormulaEvaluator dummyEvaluator = new C07971();
    private static final double fontHeightMultiple = 2.0d;
    private static final FontRenderContext fontRenderContext = new FontRenderContext(null, true, true);

    static class C07971 implements FormulaEvaluator {
        C07971() {
        }

        public void clearAllCachedResultValues() {
        }

        public void notifySetFormula(Cell cell) {
        }

        public void notifyDeleteCell(Cell cell) {
        }

        public void notifyUpdateCell(Cell cell) {
        }

        public CellValue evaluate(Cell cell) {
            return null;
        }

        public Cell evaluateInCell(Cell cell) {
            return null;
        }

        public void setupReferencedWorkbooks(Map<String, FormulaEvaluator> map) {
        }

        public void setDebugEvaluationOutputForNextEval(boolean value) {
        }

        public void setIgnoreMissingWorkbooks(boolean ignore) {
        }

        public void evaluateAll() {
        }

        public int evaluateFormulaCell(Cell cell) {
            return cell.getCachedFormulaResultType();
        }

        @Internal(since = "POI 3.15 beta 3")
        public CellType evaluateFormulaCellEnum(Cell cell) {
            return cell.getCachedFormulaResultTypeEnum();
        }
    }

    public static double getCellWidth(Cell cell, int defaultCharWidth, DataFormatter formatter, boolean useMergedCells) {
        Sheet sheet = cell.getSheet();
        Workbook wb = sheet.getWorkbook();
        Row row = cell.getRow();
        int column = cell.getColumnIndex();
        int colspan = 1;
        for (CellRangeAddress region : sheet.getMergedRegions()) {
            if (containsCell(region, row.getRowNum(), column)) {
                if (!useMergedCells) {
                    return -1.0d;
                }
                cell = row.getCell(region.getFirstColumn());
                colspan = (region.getLastColumn() + 1) - region.getFirstColumn();
            }
        }
        CellStyle style = cell.getCellStyle();
        CellType cellType = cell.getCellTypeEnum();
        if (cellType == CellType.FORMULA) {
            cellType = cell.getCachedFormulaResultTypeEnum();
        }
        Font font = wb.getFontAt(style.getFontIndex());
        double width = -1.0d;
        AttributedString str;
        if (cellType == CellType.STRING) {
            RichTextString rt = cell.getRichStringCellValue();
            String[] lines = rt.getString().split("\\n");
            int i = 0;
            while (i < lines.length) {
                String txt = lines[i] + defaultChar;
                str = new AttributedString(txt);
                copyAttributes(font, str, 0, txt.length());
                if (rt.numFormattingRuns() > 0) {
                    width = getCellWidth(defaultCharWidth, colspan, style, width, str);
                    i++;
                } else {
                    width = getCellWidth(defaultCharWidth, colspan, style, width, str);
                    i++;
                }
            }
            return width;
        }
        String sval = null;
        if (cellType == CellType.NUMERIC) {
            try {
                sval = formatter.formatCellValue(cell, dummyEvaluator);
            } catch (Exception e) {
                sval = String.valueOf(cell.getNumericCellValue());
            }
        } else if (cellType == CellType.BOOLEAN) {
            sval = String.valueOf(cell.getBooleanCellValue()).toUpperCase(Locale.ROOT);
        }
        if (sval == null) {
            return -1.0d;
        }
        txt = sval + defaultChar;
        str = new AttributedString(txt);
        copyAttributes(font, str, 0, txt.length());
        return getCellWidth(defaultCharWidth, colspan, style, -1.0d, str);
    }

    private static double getCellWidth(int defaultCharWidth, int colspan, CellStyle style, double minWidth, AttributedString str) {
        Rectangle2D bounds;
        TextLayout layout = new TextLayout(str.getIterator(), fontRenderContext);
        if (style.getRotation() != (short) 0) {
            AffineTransform trans = new AffineTransform();
            trans.concatenate(AffineTransform.getRotateInstance(((((double) style.getRotation()) * fontHeightMultiple) * 3.141592653589793d) / 360.0d));
            trans.concatenate(AffineTransform.getScaleInstance(1.0d, fontHeightMultiple));
            bounds = layout.getOutline(trans).getBounds();
        } else {
            bounds = layout.getBounds();
        }
        return Math.max(minWidth, (((bounds.getX() + bounds.getWidth()) / ((double) colspan)) / ((double) defaultCharWidth)) + ((double) style.getIndention()));
    }

    public static double getColumnWidth(Sheet sheet, int column, boolean useMergedCells) {
        return getColumnWidth(sheet, column, useMergedCells, sheet.getFirstRowNum(), sheet.getLastRowNum());
    }

    public static double getColumnWidth(Sheet sheet, int column, boolean useMergedCells, int firstRow, int lastRow) {
        DataFormatter formatter = new DataFormatter();
        int defaultCharWidth = getDefaultCharWidth(sheet.getWorkbook());
        double width = -1.0d;
        for (int rowIdx = firstRow; rowIdx <= lastRow; rowIdx++) {
            Row row = sheet.getRow(rowIdx);
            if (row != null) {
                width = Math.max(width, getColumnWidthForRow(row, column, defaultCharWidth, formatter, useMergedCells));
            }
        }
        return width;
    }

    @Internal
    public static int getDefaultCharWidth(Workbook wb) {
        Font defaultFont = wb.getFontAt((short) 0);
        AttributedString str = new AttributedString(String.valueOf(defaultChar));
        copyAttributes(defaultFont, str, 0, 1);
        return (int) new TextLayout(str.getIterator(), fontRenderContext).getAdvance();
    }

    private static double getColumnWidthForRow(Row row, int column, int defaultCharWidth, DataFormatter formatter, boolean useMergedCells) {
        if (row == null) {
            return -1.0d;
        }
        Cell cell = row.getCell(column);
        if (cell != null) {
            return getCellWidth(cell, defaultCharWidth, formatter, useMergedCells);
        }
        return -1.0d;
    }

    public static boolean canComputeColumnWidth(Font font) {
        AttributedString str = new AttributedString("1w");
        copyAttributes(font, str, 0, "1w".length());
        if (new TextLayout(str.getIterator(), fontRenderContext).getBounds().getWidth() > 0.0d) {
            return true;
        }
        return false;
    }

    private static void copyAttributes(Font font, AttributedString str, int startIdx, int endIdx) {
        str.addAttribute(TextAttribute.FAMILY, font.getFontName(), startIdx, endIdx);
        str.addAttribute(TextAttribute.SIZE, Float.valueOf((float) font.getFontHeightInPoints()));
        if (font.getBoldweight() == (short) 700) {
            str.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD, startIdx, endIdx);
        }
        if (font.getItalic()) {
            str.addAttribute(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE, startIdx, endIdx);
        }
        if (font.getUnderline() == (byte) 1) {
            str.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON, startIdx, endIdx);
        }
    }

    public static boolean containsCell(CellRangeAddress cr, int rowIx, int colIx) {
        return cr.isInRange(rowIx, colIx);
    }

    public static Cell getCellWithMerges(Sheet sheet, int rowIx, int colIx) {
        Row r = sheet.getRow(rowIx);
        if (r != null) {
            Cell c = r.getCell(colIx);
            if (c != null) {
                return c;
            }
        }
        for (CellRangeAddress mergedRegion : sheet.getMergedRegions()) {
            if (mergedRegion.isInRange(rowIx, colIx)) {
                r = sheet.getRow(mergedRegion.getFirstRow());
                if (r != null) {
                    return r.getCell(mergedRegion.getFirstColumn());
                }
            }
        }
        return null;
    }
}

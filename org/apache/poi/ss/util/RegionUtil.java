package org.apache.poi.ss.util;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public final class RegionUtil {

    private static final class CellPropertySetter {
        private final String _propertyName;
        private final Short _propertyValue;

        public CellPropertySetter(String propertyName, int value) {
            this._propertyName = propertyName;
            this._propertyValue = Short.valueOf((short) value);
        }

        public void setProperty(Row row, int column) {
            CellUtil.setCellStyleProperty(CellUtil.getCell(row, column), this._propertyName, this._propertyValue);
        }
    }

    private RegionUtil() {
    }

    public static void setBorderLeft(int border, CellRangeAddress region, Sheet sheet, Workbook workbook) {
        setBorderLeft(border, region, sheet);
    }

    public static void setBorderLeft(int border, CellRangeAddress region, Sheet sheet) {
        int rowStart = region.getFirstRow();
        int rowEnd = region.getLastRow();
        int column = region.getFirstColumn();
        CellPropertySetter cps = new CellPropertySetter(CellUtil.BORDER_LEFT, border);
        for (int i = rowStart; i <= rowEnd; i++) {
            cps.setProperty(CellUtil.getRow(i, sheet), column);
        }
    }

    public static void setLeftBorderColor(int color, CellRangeAddress region, Sheet sheet, Workbook workbook) {
        setLeftBorderColor(color, region, sheet);
    }

    public static void setLeftBorderColor(int color, CellRangeAddress region, Sheet sheet) {
        int rowStart = region.getFirstRow();
        int rowEnd = region.getLastRow();
        int column = region.getFirstColumn();
        CellPropertySetter cps = new CellPropertySetter(CellUtil.LEFT_BORDER_COLOR, color);
        for (int i = rowStart; i <= rowEnd; i++) {
            cps.setProperty(CellUtil.getRow(i, sheet), column);
        }
    }

    public static void setBorderRight(int border, CellRangeAddress region, Sheet sheet, Workbook workbook) {
        setBorderRight(border, region, sheet);
    }

    public static void setBorderRight(int border, CellRangeAddress region, Sheet sheet) {
        int rowStart = region.getFirstRow();
        int rowEnd = region.getLastRow();
        int column = region.getLastColumn();
        CellPropertySetter cps = new CellPropertySetter(CellUtil.BORDER_RIGHT, border);
        for (int i = rowStart; i <= rowEnd; i++) {
            cps.setProperty(CellUtil.getRow(i, sheet), column);
        }
    }

    public static void setRightBorderColor(int color, CellRangeAddress region, Sheet sheet, Workbook workbook) {
        setRightBorderColor(color, region, sheet);
    }

    public static void setRightBorderColor(int color, CellRangeAddress region, Sheet sheet) {
        int rowStart = region.getFirstRow();
        int rowEnd = region.getLastRow();
        int column = region.getLastColumn();
        CellPropertySetter cps = new CellPropertySetter(CellUtil.RIGHT_BORDER_COLOR, color);
        for (int i = rowStart; i <= rowEnd; i++) {
            cps.setProperty(CellUtil.getRow(i, sheet), column);
        }
    }

    public static void setBorderBottom(int border, CellRangeAddress region, Sheet sheet, Workbook workbook) {
        setBorderBottom(border, region, sheet);
    }

    public static void setBorderBottom(int border, CellRangeAddress region, Sheet sheet) {
        int colStart = region.getFirstColumn();
        int colEnd = region.getLastColumn();
        int rowIndex = region.getLastRow();
        CellPropertySetter cps = new CellPropertySetter(CellUtil.BORDER_BOTTOM, border);
        Row row = CellUtil.getRow(rowIndex, sheet);
        for (int i = colStart; i <= colEnd; i++) {
            cps.setProperty(row, i);
        }
    }

    public static void setBottomBorderColor(int color, CellRangeAddress region, Sheet sheet, Workbook workbook) {
        setBottomBorderColor(color, region, sheet);
    }

    public static void setBottomBorderColor(int color, CellRangeAddress region, Sheet sheet) {
        int colStart = region.getFirstColumn();
        int colEnd = region.getLastColumn();
        int rowIndex = region.getLastRow();
        CellPropertySetter cps = new CellPropertySetter(CellUtil.BOTTOM_BORDER_COLOR, color);
        Row row = CellUtil.getRow(rowIndex, sheet);
        for (int i = colStart; i <= colEnd; i++) {
            cps.setProperty(row, i);
        }
    }

    public static void setBorderTop(int border, CellRangeAddress region, Sheet sheet, Workbook workbook) {
        setBorderTop(border, region, sheet);
    }

    public static void setBorderTop(int border, CellRangeAddress region, Sheet sheet) {
        int colStart = region.getFirstColumn();
        int colEnd = region.getLastColumn();
        int rowIndex = region.getFirstRow();
        CellPropertySetter cps = new CellPropertySetter(CellUtil.BORDER_TOP, border);
        Row row = CellUtil.getRow(rowIndex, sheet);
        for (int i = colStart; i <= colEnd; i++) {
            cps.setProperty(row, i);
        }
    }

    public static void setTopBorderColor(int color, CellRangeAddress region, Sheet sheet, Workbook workbook) {
        setTopBorderColor(color, region, sheet);
    }

    public static void setTopBorderColor(int color, CellRangeAddress region, Sheet sheet) {
        int colStart = region.getFirstColumn();
        int colEnd = region.getLastColumn();
        int rowIndex = region.getFirstRow();
        CellPropertySetter cps = new CellPropertySetter(CellUtil.TOP_BORDER_COLOR, color);
        Row row = CellUtil.getRow(rowIndex, sheet);
        for (int i = colStart; i <= colEnd; i++) {
            cps.setProperty(row, i);
        }
    }
}

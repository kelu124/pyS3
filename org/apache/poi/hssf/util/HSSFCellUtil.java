package org.apache.poi.hssf.util;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.util.Removal;

@Deprecated
@Removal(version = "3.17")
public final class HSSFCellUtil {
    private HSSFCellUtil() {
    }

    public static HSSFRow getRow(int rowIndex, HSSFSheet sheet) {
        return (HSSFRow) CellUtil.getRow(rowIndex, sheet);
    }

    public static HSSFCell getCell(HSSFRow row, int columnIndex) {
        return (HSSFCell) CellUtil.getCell(row, columnIndex);
    }

    public static HSSFCell createCell(HSSFRow row, int column, String value, HSSFCellStyle style) {
        return (HSSFCell) CellUtil.createCell(row, column, value, style);
    }

    public static HSSFCell createCell(HSSFRow row, int column, String value) {
        return createCell(row, column, value, null);
    }

    public static void setAlignment(HSSFCell cell, HSSFWorkbook workbook, short align) {
        setAlignment(cell, HorizontalAlignment.forInt(align));
    }

    public static void setAlignment(HSSFCell cell, HorizontalAlignment align) {
        CellUtil.setAlignment(cell, align);
    }

    public static void setFont(HSSFCell cell, HSSFWorkbook workbook, HSSFFont font) {
        CellUtil.setFont(cell, font);
    }

    public static void setCellStyleProperty(HSSFCell cell, HSSFWorkbook workbook, String propertyName, Object propertyValue) {
        CellUtil.setCellStyleProperty(cell, propertyName, propertyValue);
    }

    public static HSSFCell translateUnicodeValues(HSSFCell cell) {
        CellUtil.translateUnicodeValues(cell);
        return cell;
    }
}

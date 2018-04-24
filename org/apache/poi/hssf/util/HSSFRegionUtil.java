package org.apache.poi.hssf.util;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;

public final class HSSFRegionUtil {
    private HSSFRegionUtil() {
    }

    public static void setBorderLeft(int border, CellRangeAddress region, HSSFSheet sheet, HSSFWorkbook workbook) {
        RegionUtil.setBorderLeft(border, region, sheet, workbook);
    }

    public static void setLeftBorderColor(int color, CellRangeAddress region, HSSFSheet sheet, HSSFWorkbook workbook) {
        RegionUtil.setLeftBorderColor(color, region, sheet, workbook);
    }

    public static void setBorderRight(int border, CellRangeAddress region, HSSFSheet sheet, HSSFWorkbook workbook) {
        RegionUtil.setBorderRight(border, region, sheet, workbook);
    }

    public static void setRightBorderColor(int color, CellRangeAddress region, HSSFSheet sheet, HSSFWorkbook workbook) {
        RegionUtil.setRightBorderColor(color, region, sheet, workbook);
    }

    public static void setBorderBottom(int border, CellRangeAddress region, HSSFSheet sheet, HSSFWorkbook workbook) {
        RegionUtil.setBorderBottom(border, region, sheet, workbook);
    }

    public static void setBottomBorderColor(int color, CellRangeAddress region, HSSFSheet sheet, HSSFWorkbook workbook) {
        RegionUtil.setBottomBorderColor(color, region, sheet, workbook);
    }

    public static void setBorderTop(int border, CellRangeAddress region, HSSFSheet sheet, HSSFWorkbook workbook) {
        RegionUtil.setBorderTop(border, region, sheet, workbook);
    }

    public static void setTopBorderColor(int color, CellRangeAddress region, HSSFSheet sheet, HSSFWorkbook workbook) {
        RegionUtil.setTopBorderColor(color, region, sheet, workbook);
    }
}

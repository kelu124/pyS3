package org.apache.poi.hssf.usermodel;

import java.util.HashSet;
import java.util.Iterator;
import org.apache.poi.hssf.record.ExtendedFormatRecord;
import org.apache.poi.hssf.record.FontRecord;
import org.apache.poi.hssf.record.common.UnicodeString;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

public class HSSFOptimiser {
    public static void optimiseFonts(HSSFWorkbook workbook) {
        int i;
        short[] newPos = new short[(workbook.getWorkbook().getNumberOfFontRecords() + 1)];
        boolean[] zapRecords = new boolean[newPos.length];
        for (i = 0; i < newPos.length; i++) {
            newPos[i] = (short) i;
            zapRecords[i] = false;
        }
        FontRecord[] frecs = new FontRecord[newPos.length];
        for (i = 0; i < newPos.length; i++) {
            if (i != 4) {
                frecs[i] = workbook.getWorkbook().getFontRecordAt(i);
            }
        }
        i = 5;
        while (i < newPos.length) {
            int earlierDuplicate = -1;
            int j = 0;
            while (j < i && earlierDuplicate == -1) {
                if (j != 4 && workbook.getWorkbook().getFontRecordAt(j).sameProperties(frecs[i])) {
                    earlierDuplicate = j;
                }
                j++;
            }
            if (earlierDuplicate != -1) {
                newPos[i] = (short) earlierDuplicate;
                zapRecords[i] = true;
            }
            i++;
        }
        for (i = 5; i < newPos.length; i++) {
            short preDeletePos = newPos[i];
            short newPosition = preDeletePos;
            for (short j2 = (short) 0; j2 < preDeletePos; j2++) {
                if (zapRecords[j2]) {
                    newPosition = (short) (newPosition - 1);
                }
            }
            newPos[i] = newPosition;
        }
        for (i = 5; i < newPos.length; i++) {
            if (zapRecords[i]) {
                workbook.getWorkbook().removeFontRecord(frecs[i]);
            }
        }
        workbook.resetFontCache();
        for (i = 0; i < workbook.getWorkbook().getNumExFormats(); i++) {
            ExtendedFormatRecord xfr = workbook.getWorkbook().getExFormatAt(i);
            xfr.setFontIndex(newPos[xfr.getFontIndex()]);
        }
        HashSet<UnicodeString> doneUnicodeStrings = new HashSet();
        for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
            Iterator it = workbook.getSheetAt(sheetNum).iterator();
            while (it.hasNext()) {
                for (Cell cell : (Row) it.next()) {
                    if (cell.getCellTypeEnum() == CellType.STRING) {
                        UnicodeString u = ((HSSFRichTextString) cell.getRichStringCellValue()).getRawUnicodeString();
                        if (!doneUnicodeStrings.contains(u)) {
                            for (short i2 = (short) 5; i2 < newPos.length; i2 = (short) (i2 + 1)) {
                                if (i2 != newPos[i2]) {
                                    u.swapFontUse(i2, newPos[i2]);
                                }
                            }
                            doneUnicodeStrings.add(u);
                        }
                    }
                }
            }
        }
    }

    public static void optimiseCellStyles(HSSFWorkbook workbook) {
        int i;
        int sheetNum;
        short[] newPos = new short[workbook.getWorkbook().getNumExFormats()];
        boolean[] isUsed = new boolean[newPos.length];
        boolean[] zapRecords = new boolean[newPos.length];
        for (i = 0; i < newPos.length; i++) {
            isUsed[i] = false;
            newPos[i] = (short) i;
            zapRecords[i] = false;
        }
        ExtendedFormatRecord[] xfrs = new ExtendedFormatRecord[newPos.length];
        for (i = 0; i < newPos.length; i++) {
            xfrs[i] = workbook.getWorkbook().getExFormatAt(i);
        }
        for (i = 21; i < newPos.length; i++) {
            int earlierDuplicate = -1;
            for (int j = 0; j < i && earlierDuplicate == -1; j++) {
                if (workbook.getWorkbook().getExFormatAt(j).equals(xfrs[i])) {
                    earlierDuplicate = j;
                }
            }
            if (earlierDuplicate != -1) {
                newPos[i] = (short) earlierDuplicate;
                zapRecords[i] = true;
            }
            if (earlierDuplicate != -1) {
                isUsed[earlierDuplicate] = true;
            }
        }
        for (sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
            Iterator it = workbook.getSheetAt(sheetNum).iterator();
            while (it.hasNext()) {
                for (Cell cellI : (Row) it.next()) {
                    isUsed[((HSSFCell) cellI).getCellValueRecord().getXFIndex()] = true;
                }
            }
        }
        for (i = 21; i < isUsed.length; i++) {
            if (!isUsed[i]) {
                zapRecords[i] = true;
                newPos[i] = (short) 0;
            }
        }
        for (i = 21; i < newPos.length; i++) {
            short preDeletePos = newPos[i];
            short newPosition = preDeletePos;
            for (short j2 = (short) 0; j2 < preDeletePos; j2++) {
                if (zapRecords[j2]) {
                    newPosition = (short) (newPosition - 1);
                }
            }
            newPos[i] = newPosition;
        }
        int max = newPos.length;
        int removed = 0;
        i = 21;
        while (i < max) {
            if (zapRecords[i + removed]) {
                workbook.getWorkbook().removeExFormatRecord(i);
                i--;
                max--;
                removed++;
            }
            i++;
        }
        for (sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
            it = workbook.getSheetAt(sheetNum).iterator();
            while (it.hasNext()) {
                for (Cell cellI2 : (Row) it.next()) {
                    HSSFCell cell = (HSSFCell) cellI2;
                    cell.setCellStyle(workbook.getCellStyleAt(newPos[cell.getCellValueRecord().getXFIndex()]));
                }
            }
        }
    }
}

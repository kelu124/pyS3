package org.apache.poi.hssf.dev;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ddf.EscherRecord;
import org.apache.poi.hssf.record.DrawingGroupRecord;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.util.StringUtil;

public class BiffDrawingToXml {
    private static final String EXCLUDE_WORKBOOK_RECORDS = "-exclude-workbook";
    private static final String SHEET_INDEXES_PARAM = "-sheet-indexes";
    private static final String SHEET_NAME_PARAM = "-sheet-name";

    private static int getAttributeIndex(String attribute, String[] params) {
        for (int i = 0; i < params.length; i++) {
            if (attribute.equals(params[i])) {
                return i;
            }
        }
        return -1;
    }

    private static boolean isExcludeWorkbookRecords(String[] params) {
        return -1 != getAttributeIndex(EXCLUDE_WORKBOOK_RECORDS, params);
    }

    private static List<Integer> getIndexesByName(String[] params, HSSFWorkbook workbook) {
        List<Integer> list = new ArrayList();
        int pos = getAttributeIndex(SHEET_NAME_PARAM, params);
        if (-1 != pos) {
            if (pos >= params.length) {
                throw new IllegalArgumentException("sheet name param value was not specified");
            }
            int sheetPos = workbook.getSheetIndex(params[pos + 1]);
            if (-1 == sheetPos) {
                throw new IllegalArgumentException("specified sheet name has not been found in xls file");
            }
            list.add(Integer.valueOf(sheetPos));
        }
        return list;
    }

    private static List<Integer> getIndexesByIdArray(String[] params) {
        List<Integer> list = new ArrayList();
        int pos = getAttributeIndex(SHEET_INDEXES_PARAM, params);
        if (-1 != pos) {
            if (pos >= params.length) {
                throw new IllegalArgumentException("sheet list value was not specified");
            }
            for (String sheet : params[pos + 1].split(",")) {
                list.add(Integer.valueOf(Integer.parseInt(sheet)));
            }
        }
        return list;
    }

    private static List<Integer> getSheetsIndexes(String[] params, HSSFWorkbook workbook) {
        List<Integer> list = new ArrayList();
        list.addAll(getIndexesByIdArray(params));
        list.addAll(getIndexesByName(params, workbook));
        if (list.size() == 0) {
            int size = workbook.getNumberOfSheets();
            for (int i = 0; i < size; i++) {
                list.add(Integer.valueOf(i));
            }
        }
        return list;
    }

    private static String getInputFileName(String[] params) {
        return params[params.length - 1];
    }

    private static String getOutputFileName(String input) {
        if (input.contains("xls")) {
            return input.replace(".xls", ".xml");
        }
        return input + ".xml";
    }

    public static void main(String[] params) throws IOException {
        if (params.length == 0) {
            System.out.println("Usage: BiffDrawingToXml [options] inputWorkbook");
            System.out.println("Options:");
            System.out.println("  -exclude-workbook            exclude workbook-level records");
            System.out.println("  -sheet-indexes   <indexes>   output sheets with specified indexes");
            System.out.println("  -sheet-namek  <names>        output sheets with specified name");
            return;
        }
        String input = getInputFileName(params);
        FileInputStream inp = new FileInputStream(input);
        FileOutputStream outputStream = new FileOutputStream(getOutputFileName(input));
        writeToFile(outputStream, inp, isExcludeWorkbookRecords(params), params);
        inp.close();
        outputStream.close();
    }

    public static void writeToFile(OutputStream fos, InputStream xlsWorkbook, boolean excludeWorkbookRecords, String[] params) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook(xlsWorkbook);
        DrawingGroupRecord r = (DrawingGroupRecord) workbook.getInternalWorkbook().findFirstRecordBySid(DrawingGroupRecord.sid);
        StringBuilder builder = new StringBuilder();
        builder.append("<workbook>\n");
        String tab = "\t";
        if (!(excludeWorkbookRecords || r == null)) {
            r.decode();
            for (EscherRecord record : r.getEscherRecords()) {
                builder.append(record.toXml(tab));
            }
        }
        for (Integer i : getSheetsIndexes(params, workbook)) {
            HSSFPatriarch p = workbook.getSheetAt(i.intValue()).getDrawingPatriarch();
            if (p != null) {
                builder.append(tab).append("<sheet").append(i).append(">\n");
                builder.append(p.getBoundAggregate().toXml(tab + "\t"));
                builder.append(tab).append("</sheet").append(i).append(">\n");
            }
        }
        builder.append("</workbook>\n");
        fos.write(builder.toString().getBytes(StringUtil.UTF8));
        fos.close();
        workbook.close();
    }
}

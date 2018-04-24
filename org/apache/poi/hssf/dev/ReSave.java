package org.apache.poi.hssf.dev;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ReSave {
    public static void main(String[] args) throws Exception {
        boolean initDrawing = false;
        boolean saveToMemory = false;
        OutputStream bos = new ByteArrayOutputStream();
        for (String filename : args) {
            if (filename.equals("-dg")) {
                initDrawing = true;
            } else if (filename.equals("-bos")) {
                saveToMemory = true;
            } else {
                OutputStream os;
                System.out.print("reading " + filename + "...");
                FileInputStream is = new FileInputStream(filename);
                HSSFWorkbook wb = new HSSFWorkbook(is);
                System.out.println("done");
                for (int i = 0; i < wb.getNumberOfSheets(); i++) {
                    HSSFSheet sheet = wb.getSheetAt(i);
                    if (initDrawing) {
                        sheet.getDrawingPatriarch();
                    }
                }
                if (saveToMemory) {
                    bos.reset();
                    os = bos;
                } else {
                    String outputFile = filename.replace(".xls", "-saved.xls");
                    System.out.print("saving to " + outputFile + "...");
                    os = new FileOutputStream(outputFile);
                }
                try {
                    wb.write(os);
                    os.close();
                    System.out.println("done");
                    wb.close();
                    is.close();
                } catch (Throwable th) {
                    wb.close();
                    is.close();
                }
            }
        }
    }
}

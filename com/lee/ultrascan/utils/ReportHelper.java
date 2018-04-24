package com.lee.ultrascan.utils;

import android.content.Context;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.lee.ultrascan.C0796R;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ReportHelper {
    private static final String FONT_ASSETS_PATH = "assets/font/simhei.ttf";
    public static final String PDF_FILE_EXTENSION = ".pdf";
    public static final String REPORT_FILE_DIR = "UltraScan/report";
    private static final String REPORT_XLS_FULL_FILENAME = "report.xls";

    private static File getReportFileDir() {
        File sdCardDir = FileUtils.getExternalStorageDir();
        if (sdCardDir == null) {
            return null;
        }
        File reportFileDir = new File(sdCardDir, REPORT_FILE_DIR);
        if (reportFileDir.isDirectory() || reportFileDir.mkdirs()) {
            return reportFileDir;
        }
        return null;
    }

    public static void exportExcelRecord(Context context, String time, String id, double backfat, boolean isPregnant, String remarks) throws IOException {
        File reportFileDir = getReportFileDir();
        if (reportFileDir == null) {
            throw new IOException("get report file dir failed");
        }
        HSSFWorkbook workbook;
        HSSFSheet sheet;
        HSSFRow row;
        File file = new File(reportFileDir, REPORT_XLS_FULL_FILENAME);
        if (file.exists()) {
            workbook = new HSSFWorkbook(new FileInputStream(file));
            sheet = workbook.getSheetAt(0);
        } else {
            workbook = new HSSFWorkbook();
            sheet = workbook.createSheet();
            if (file.createNewFile()) {
            }
            row = sheet.createRow(0);
            row.createCell(0).setCellValue(context.getString(C0796R.string.excel_report_title_time));
            row.createCell(1).setCellValue(context.getString(C0796R.string.excel_report_title_id));
            row.createCell(2).setCellValue(context.getString(C0796R.string.excel_report_title_backfat));
            row.createCell(3).setCellValue(context.getString(C0796R.string.excel_report_title_remarks));
        }
        row = sheet.createRow(sheet.getLastRowNum() + 1);
        row.createCell(0).setCellValue(time);
        row.createCell(1).setCellValue(id);
        HSSFCell cell = row.createCell(2);
        DecimalFormat fatDecimalFormat = new DecimalFormat("###.#");
        fatDecimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        cell.setCellValue(backfat == 0.0d ? "--" : fatDecimalFormat.format(backfat));
        row.createCell(3).setCellValue(remarks);
        FileOutputStream outputStream = new FileOutputStream(file);
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

    public static File generateReportFile(Context context, File imageFile, String time, String id, boolean isPregnant, String remarks) throws IOException, DocumentException {
        try {
            File reportFileDir = getReportFileDir();
            if (reportFileDir == null) {
                throw new IOException("get report file dir failed");
            }
            File pdfFile = FileUtils.openFile(reportFileDir, "report_" + time.replace(":", "-").replace(".", "_") + PDF_FILE_EXTENSION);
            if (!pdfFile.exists()) {
                pdfFile.createNewFile();
            }
            BaseFont simheiFont = BaseFont.createFont(FONT_ASSETS_PATH, BaseFont.IDENTITY_H, false);
            Font font = new Font(simheiFont, 20.0f, 0);
            FileOutputStream outputStream = new FileOutputStream(pdfFile);
            Document pdfDocument = new Document();
            PdfWriter writer = PdfWriter.getInstance(pdfDocument, outputStream);
            pdfDocument.open();
            pdfDocument.setMargins(20.0f, 20.0f, 20.0f, 20.0f);
            Paragraph paragraph = new Paragraph(context.getString(C0796R.string.report_content_title), new Font(simheiFont, 26.0f, 0));
            paragraph.setAlignment(1);
            pdfDocument.add(paragraph);
            paragraph = new Paragraph(context.getString(C0796R.string.report_content_time, new Object[]{time}), font);
            paragraph.setAlignment(0);
            pdfDocument.add(paragraph);
            Paragraph idParagraph = new Paragraph(context.getString(C0796R.string.report_content_id, new Object[]{id}), font);
            idParagraph.setAlignment(0);
            pdfDocument.add(idParagraph);
            Image image = Image.getInstance(imageFile.getAbsolutePath());
            image.setAlignment(1);
            image.scalePercent(50.0f);
            pdfDocument.add(image);
            Paragraph remarksTitleParagraph = new Paragraph(context.getString(C0796R.string.report_content_remarks), font);
            remarksTitleParagraph.setAlignment(0);
            pdfDocument.add(remarksTitleParagraph);
            Paragraph remarksParagraph = new Paragraph(remarks, font);
            remarksParagraph.setAlignment(0);
            pdfDocument.add(remarksParagraph);
            pdfDocument.close();
            return pdfFile;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

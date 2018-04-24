package org.apache.poi.ss.usermodel;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.formula.udf.UDFFinder;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;

public interface Workbook extends Closeable, Iterable<Sheet> {
    public static final int PICTURE_TYPE_DIB = 7;
    public static final int PICTURE_TYPE_EMF = 2;
    public static final int PICTURE_TYPE_JPEG = 5;
    public static final int PICTURE_TYPE_PICT = 4;
    public static final int PICTURE_TYPE_PNG = 6;
    public static final int PICTURE_TYPE_WMF = 3;
    public static final int SHEET_STATE_HIDDEN = 1;
    public static final int SHEET_STATE_VERY_HIDDEN = 2;
    public static final int SHEET_STATE_VISIBLE = 0;

    int addPicture(byte[] bArr, int i);

    void addToolPack(UDFFinder uDFFinder);

    Sheet cloneSheet(int i);

    void close() throws IOException;

    CellStyle createCellStyle();

    DataFormat createDataFormat();

    Font createFont();

    Name createName();

    Sheet createSheet();

    Sheet createSheet(String str);

    Font findFont(short s, short s2, short s3, String str, boolean z, boolean z2, short s4, byte b);

    Font findFont(boolean z, short s, short s2, String str, boolean z2, boolean z3, short s3, byte b);

    int getActiveSheetIndex();

    List<? extends Name> getAllNames();

    List<? extends PictureData> getAllPictures();

    CellStyle getCellStyleAt(int i);

    CreationHelper getCreationHelper();

    int getFirstVisibleTab();

    Font getFontAt(short s);

    boolean getForceFormulaRecalculation();

    MissingCellPolicy getMissingCellPolicy();

    Name getName(String str);

    Name getNameAt(int i);

    int getNameIndex(String str);

    List<? extends Name> getNames(String str);

    int getNumCellStyles();

    short getNumberOfFonts();

    int getNumberOfNames();

    int getNumberOfSheets();

    String getPrintArea(int i);

    Sheet getSheet(String str);

    Sheet getSheetAt(int i);

    int getSheetIndex(String str);

    int getSheetIndex(Sheet sheet);

    String getSheetName(int i);

    SpreadsheetVersion getSpreadsheetVersion();

    boolean isHidden();

    boolean isSheetHidden(int i);

    boolean isSheetVeryHidden(int i);

    int linkExternalWorkbook(String str, Workbook workbook);

    void removeName(int i);

    void removeName(String str);

    void removeName(Name name);

    void removePrintArea(int i);

    void removeSheetAt(int i);

    void setActiveSheet(int i);

    void setFirstVisibleTab(int i);

    void setForceFormulaRecalculation(boolean z);

    void setHidden(boolean z);

    void setMissingCellPolicy(MissingCellPolicy missingCellPolicy);

    void setPrintArea(int i, int i2, int i3, int i4, int i5);

    void setPrintArea(int i, String str);

    void setSelectedTab(int i);

    void setSheetHidden(int i, int i2);

    void setSheetHidden(int i, boolean z);

    void setSheetName(int i, String str);

    void setSheetOrder(String str, int i);

    Iterator<Sheet> sheetIterator();

    void write(OutputStream outputStream) throws IOException;
}

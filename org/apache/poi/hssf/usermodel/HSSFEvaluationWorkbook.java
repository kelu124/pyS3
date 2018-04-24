package org.apache.poi.hssf.usermodel;

import org.apache.poi.hssf.model.InternalWorkbook;
import org.apache.poi.hssf.record.NameRecord;
import org.apache.poi.hssf.record.aggregates.FormulaRecordAggregate;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.formula.EvaluationCell;
import org.apache.poi.ss.formula.EvaluationName;
import org.apache.poi.ss.formula.EvaluationSheet;
import org.apache.poi.ss.formula.EvaluationWorkbook;
import org.apache.poi.ss.formula.EvaluationWorkbook.ExternalName;
import org.apache.poi.ss.formula.EvaluationWorkbook.ExternalSheet;
import org.apache.poi.ss.formula.EvaluationWorkbook.ExternalSheetRange;
import org.apache.poi.ss.formula.FormulaParsingWorkbook;
import org.apache.poi.ss.formula.FormulaRenderingWorkbook;
import org.apache.poi.ss.formula.SheetIdentifier;
import org.apache.poi.ss.formula.SheetRangeIdentifier;
import org.apache.poi.ss.formula.ptg.Area3DPtg;
import org.apache.poi.ss.formula.ptg.NamePtg;
import org.apache.poi.ss.formula.ptg.NameXPtg;
import org.apache.poi.ss.formula.ptg.Ptg;
import org.apache.poi.ss.formula.ptg.Ref3DPtg;
import org.apache.poi.ss.formula.udf.UDFFinder;
import org.apache.poi.ss.usermodel.Table;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.Internal;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

@Internal
public final class HSSFEvaluationWorkbook implements FormulaRenderingWorkbook, EvaluationWorkbook, FormulaParsingWorkbook {
    private static POILogger logger = POILogFactory.getLogger(HSSFEvaluationWorkbook.class);
    private final InternalWorkbook _iBook;
    private final HSSFWorkbook _uBook;

    public static HSSFEvaluationWorkbook create(HSSFWorkbook book) {
        if (book == null) {
            return null;
        }
        return new HSSFEvaluationWorkbook(book);
    }

    private HSSFEvaluationWorkbook(HSSFWorkbook book) {
        this._uBook = book;
        this._iBook = book.getWorkbook();
    }

    public void clearAllCachedResultValues() {
    }

    public HSSFName createName() {
        return this._uBook.createName();
    }

    public int getExternalSheetIndex(String sheetName) {
        return this._iBook.checkExternSheet(this._uBook.getSheetIndex(sheetName));
    }

    public int getExternalSheetIndex(String workbookName, String sheetName) {
        return this._iBook.getExternalSheetIndex(workbookName, sheetName);
    }

    public Ptg get3DReferencePtg(CellReference cr, SheetIdentifier sheet) {
        return new Ref3DPtg(cr, getSheetExtIx(sheet));
    }

    public Ptg get3DReferencePtg(AreaReference areaRef, SheetIdentifier sheet) {
        return new Area3DPtg(areaRef, getSheetExtIx(sheet));
    }

    public NameXPtg getNameXPtg(String name, SheetIdentifier sheet) {
        return this._iBook.getNameXPtg(name, getSheetExtIx(sheet), this._uBook.getUDFFinder());
    }

    public EvaluationName getName(String name, int sheetIndex) {
        for (int i = 0; i < this._iBook.getNumNames(); i++) {
            NameRecord nr = this._iBook.getNameRecord(i);
            if (nr.getSheetNumber() == sheetIndex + 1 && name.equalsIgnoreCase(nr.getNameText())) {
                return new Name(nr, i);
            }
        }
        return sheetIndex == -1 ? null : getName(name, -1);
    }

    public int getSheetIndex(EvaluationSheet evalSheet) {
        return this._uBook.getSheetIndex(((HSSFEvaluationSheet) evalSheet).getHSSFSheet());
    }

    public int getSheetIndex(String sheetName) {
        return this._uBook.getSheetIndex(sheetName);
    }

    public String getSheetName(int sheetIndex) {
        return this._uBook.getSheetName(sheetIndex);
    }

    public EvaluationSheet getSheet(int sheetIndex) {
        return new HSSFEvaluationSheet(this._uBook.getSheetAt(sheetIndex));
    }

    public int convertFromExternSheetIndex(int externSheetIndex) {
        return this._iBook.getFirstSheetIndexFromExternSheetIndex(externSheetIndex);
    }

    public ExternalSheet getExternalSheet(int externSheetIndex) {
        ExternalSheet sheet = this._iBook.getExternalSheet(externSheetIndex);
        if (sheet == null) {
            int localSheetIndex = convertFromExternSheetIndex(externSheetIndex);
            if (localSheetIndex == -1 || localSheetIndex == -2) {
                return null;
            }
            String sheetName = getSheetName(localSheetIndex);
            int lastLocalSheetIndex = this._iBook.getLastSheetIndexFromExternSheetIndex(externSheetIndex);
            if (lastLocalSheetIndex == localSheetIndex) {
                sheet = new ExternalSheet(null, sheetName);
            } else {
                sheet = new ExternalSheetRange(null, sheetName, getSheetName(lastLocalSheetIndex));
            }
        }
        return sheet;
    }

    public ExternalSheet getExternalSheet(String firstSheetName, String lastSheetName, int externalWorkbookNumber) {
        throw new IllegalStateException("XSSF-style external references are not supported for HSSF");
    }

    public ExternalName getExternalName(int externSheetIndex, int externNameIndex) {
        return this._iBook.getExternalName(externSheetIndex, externNameIndex);
    }

    public ExternalName getExternalName(String nameName, String sheetName, int externalWorkbookNumber) {
        throw new IllegalStateException("XSSF-style external names are not supported for HSSF");
    }

    public String resolveNameXText(NameXPtg n) {
        return this._iBook.resolveNameXText(n.getSheetRefIndex(), n.getNameIndex());
    }

    public String getSheetFirstNameByExternSheet(int externSheetIndex) {
        return this._iBook.findSheetFirstNameFromExternSheet(externSheetIndex);
    }

    public String getSheetLastNameByExternSheet(int externSheetIndex) {
        return this._iBook.findSheetLastNameFromExternSheet(externSheetIndex);
    }

    public String getNameText(NamePtg namePtg) {
        return this._iBook.getNameRecord(namePtg.getIndex()).getNameText();
    }

    public EvaluationName getName(NamePtg namePtg) {
        int ix = namePtg.getIndex();
        return new Name(this._iBook.getNameRecord(ix), ix);
    }

    public Ptg[] getFormulaTokens(EvaluationCell evalCell) {
        return ((FormulaRecordAggregate) ((HSSFEvaluationCell) evalCell).getHSSFCell().getCellValueRecord()).getFormulaTokens();
    }

    public UDFFinder getUDFFinder() {
        return this._uBook.getUDFFinder();
    }

    private int getSheetExtIx(SheetIdentifier sheetIden) {
        if (sheetIden == null) {
            return -1;
        }
        String workbookName = sheetIden.getBookName();
        String firstSheetName = sheetIden.getSheetIdentifier().getName();
        String lastSheetName = firstSheetName;
        if (sheetIden instanceof SheetRangeIdentifier) {
            lastSheetName = ((SheetRangeIdentifier) sheetIden).getLastSheetIdentifier().getName();
        }
        if (workbookName != null) {
            return this._iBook.getExternalSheetIndex(workbookName, firstSheetName, lastSheetName);
        }
        return this._iBook.checkExternSheet(this._uBook.getSheetIndex(firstSheetName), this._uBook.getSheetIndex(lastSheetName));
    }

    public SpreadsheetVersion getSpreadsheetVersion() {
        return SpreadsheetVersion.EXCEL97;
    }

    public Table getTable(String name) {
        throw new IllegalStateException("XSSF-style tables are not supported for HSSF");
    }
}

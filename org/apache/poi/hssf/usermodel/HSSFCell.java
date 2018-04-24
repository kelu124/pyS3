package org.apache.poi.hssf.usermodel;

import com.itextpdf.text.Annotation;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.hssf.model.HSSFFormulaParser;
import org.apache.poi.hssf.model.InternalWorkbook;
import org.apache.poi.hssf.record.BlankRecord;
import org.apache.poi.hssf.record.BoolErrRecord;
import org.apache.poi.hssf.record.CellValueRecordInterface;
import org.apache.poi.hssf.record.ExtendedFormatRecord;
import org.apache.poi.hssf.record.FormulaRecord;
import org.apache.poi.hssf.record.HyperlinkRecord;
import org.apache.poi.hssf.record.LabelSSTRecord;
import org.apache.poi.hssf.record.NumberRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RecordBase;
import org.apache.poi.hssf.record.aggregates.FormulaRecordAggregate;
import org.apache.poi.hssf.record.common.UnicodeString;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.formula.FormulaType;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.ptg.ExpPtg;
import org.apache.poi.ss.formula.ptg.Ptg;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaError;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.util.LocaleUtil;

public class HSSFCell implements Cell {
    public static final short ENCODING_COMPRESSED_UNICODE = (short) 0;
    public static final short ENCODING_UNCHANGED = (short) -1;
    public static final short ENCODING_UTF_16 = (short) 1;
    private static final String FILE_FORMAT_NAME = "BIFF8";
    private static final String LAST_COLUMN_NAME = SpreadsheetVersion.EXCEL97.getLastColumnName();
    public static final int LAST_COLUMN_NUMBER = SpreadsheetVersion.EXCEL97.getLastColumnIndex();
    private final HSSFWorkbook _book;
    private CellType _cellType;
    private HSSFComment _comment;
    private CellValueRecordInterface _record;
    private final HSSFSheet _sheet;
    private HSSFRichTextString _stringValue;

    static /* synthetic */ class C10631 {
        static final /* synthetic */ int[] $SwitchMap$org$apache$poi$common$usermodel$HyperlinkType = new int[HyperlinkType.values().length];
        static final /* synthetic */ int[] $SwitchMap$org$apache$poi$ss$usermodel$CellType = new int[CellType.values().length];

        static {
            try {
                $SwitchMap$org$apache$poi$common$usermodel$HyperlinkType[HyperlinkType.EMAIL.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$apache$poi$common$usermodel$HyperlinkType[HyperlinkType.URL.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$apache$poi$common$usermodel$HyperlinkType[HyperlinkType.FILE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$apache$poi$common$usermodel$HyperlinkType[HyperlinkType.DOCUMENT.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$org$apache$poi$ss$usermodel$CellType[CellType.STRING.ordinal()] = 1;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$org$apache$poi$ss$usermodel$CellType[CellType.BLANK.ordinal()] = 2;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$org$apache$poi$ss$usermodel$CellType[CellType.FORMULA.ordinal()] = 3;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$org$apache$poi$ss$usermodel$CellType[CellType.NUMERIC.ordinal()] = 4;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$org$apache$poi$ss$usermodel$CellType[CellType.BOOLEAN.ordinal()] = 5;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$org$apache$poi$ss$usermodel$CellType[CellType.ERROR.ordinal()] = 6;
            } catch (NoSuchFieldError e10) {
            }
        }
    }

    protected HSSFCell(HSSFWorkbook book, HSSFSheet sheet, int row, short col) {
        checkBounds(col);
        this._stringValue = null;
        this._book = book;
        this._sheet = sheet;
        setCellType(CellType.BLANK, false, row, col, sheet.getSheet().getXFIndexForColAt(col));
    }

    public HSSFSheet getSheet() {
        return this._sheet;
    }

    public HSSFRow getRow() {
        return this._sheet.getRow(getRowIndex());
    }

    protected HSSFCell(HSSFWorkbook book, HSSFSheet sheet, int row, short col, CellType type) {
        checkBounds(col);
        this._cellType = CellType._NONE;
        this._stringValue = null;
        this._book = book;
        this._sheet = sheet;
        setCellType(type, false, row, col, sheet.getSheet().getXFIndexForColAt(col));
    }

    protected HSSFCell(HSSFWorkbook book, HSSFSheet sheet, CellValueRecordInterface cval) {
        this._record = cval;
        this._cellType = determineType(cval);
        this._stringValue = null;
        this._book = book;
        this._sheet = sheet;
        switch (C10631.$SwitchMap$org$apache$poi$ss$usermodel$CellType[this._cellType.ordinal()]) {
            case 1:
                this._stringValue = new HSSFRichTextString(book.getWorkbook(), (LabelSSTRecord) cval);
                return;
            case 3:
                this._stringValue = new HSSFRichTextString(((FormulaRecordAggregate) cval).getStringValue());
                return;
            default:
                return;
        }
    }

    private static CellType determineType(CellValueRecordInterface cval) {
        if (cval instanceof FormulaRecordAggregate) {
            return CellType.FORMULA;
        }
        Record record = (Record) cval;
        switch (record.getSid()) {
            case (short) 253:
                return CellType.STRING;
            case (short) 513:
                return CellType.BLANK;
            case (short) 515:
                return CellType.NUMERIC;
            case (short) 517:
                return ((BoolErrRecord) record).isBoolean() ? CellType.BOOLEAN : CellType.ERROR;
            default:
                throw new RuntimeException("Bad cell value rec (" + cval.getClass().getName() + ")");
        }
    }

    protected InternalWorkbook getBoundWorkbook() {
        return this._book.getWorkbook();
    }

    public int getRowIndex() {
        return this._record.getRow();
    }

    protected void updateCellNum(short num) {
        this._record.setColumn(num);
    }

    public int getColumnIndex() {
        return this._record.getColumn() & 65535;
    }

    public CellAddress getAddress() {
        return new CellAddress((Cell) this);
    }

    public void setCellType(int cellType) {
        setCellType(CellType.forInt(cellType));
    }

    public void setCellType(CellType cellType) {
        notifyFormulaChanging();
        if (isPartOfArrayFormulaGroup()) {
            notifyArrayFormulaChanging();
        }
        setCellType(cellType, true, this._record.getRow(), this._record.getColumn(), this._record.getXFIndex());
    }

    private void setCellType(CellType cellType, boolean setValue, int row, short col, short styleIndex) {
        switch (C10631.$SwitchMap$org$apache$poi$ss$usermodel$CellType[cellType.ordinal()]) {
            case 1:
                LabelSSTRecord lrec;
                if (cellType == this._cellType) {
                    lrec = this._record;
                } else {
                    lrec = new LabelSSTRecord();
                    lrec.setColumn(col);
                    lrec.setRow(row);
                    lrec.setXFIndex(styleIndex);
                }
                if (setValue) {
                    String str = convertCellValueToString();
                    if (str == null) {
                        setCellType(CellType.BLANK, false, row, col, styleIndex);
                        return;
                    }
                    int sstIndex = this._book.getWorkbook().addSSTString(new UnicodeString(str));
                    lrec.setSSTIndex(sstIndex);
                    UnicodeString us = this._book.getWorkbook().getSSTString(sstIndex);
                    this._stringValue = new HSSFRichTextString();
                    this._stringValue.setUnicodeString(us);
                }
                this._record = lrec;
                break;
            case 2:
                BlankRecord brec;
                if (cellType != this._cellType) {
                    brec = new BlankRecord();
                } else {
                    brec = this._record;
                }
                brec.setColumn(col);
                brec.setXFIndex(styleIndex);
                brec.setRow(row);
                this._record = brec;
                break;
            case 3:
                FormulaRecordAggregate frec;
                if (cellType != this._cellType) {
                    frec = this._sheet.getSheet().getRowsAggregate().createFormula(row, col);
                } else {
                    frec = (FormulaRecordAggregate) this._record;
                    frec.setRow(row);
                    frec.setColumn(col);
                }
                if (setValue) {
                    frec.getFormulaRecord().setValue(getNumericCellValue());
                }
                frec.setXFIndex(styleIndex);
                this._record = frec;
                break;
            case 4:
                NumberRecord nrec;
                if (cellType != this._cellType) {
                    nrec = new NumberRecord();
                } else {
                    nrec = this._record;
                }
                nrec.setColumn(col);
                if (setValue) {
                    nrec.setValue(getNumericCellValue());
                }
                nrec.setXFIndex(styleIndex);
                nrec.setRow(row);
                this._record = nrec;
                break;
            case 5:
                BoolErrRecord boolRec;
                if (cellType != this._cellType) {
                    boolRec = new BoolErrRecord();
                } else {
                    boolRec = this._record;
                }
                boolRec.setColumn(col);
                if (setValue) {
                    boolRec.setValue(convertCellValueToBoolean());
                }
                boolRec.setXFIndex(styleIndex);
                boolRec.setRow(row);
                this._record = boolRec;
                break;
            case 6:
                BoolErrRecord errRec;
                if (cellType != this._cellType) {
                    errRec = new BoolErrRecord();
                } else {
                    errRec = this._record;
                }
                errRec.setColumn(col);
                if (setValue) {
                    errRec.setValue(FormulaError.VALUE.getCode());
                }
                errRec.setXFIndex(styleIndex);
                errRec.setRow(row);
                this._record = errRec;
                break;
            default:
                throw new IllegalStateException("Invalid cell type: " + cellType);
        }
        if (!(cellType == this._cellType || this._cellType == CellType._NONE)) {
            this._sheet.getSheet().replaceValueRecord(this._record);
        }
        this._cellType = cellType;
    }

    public int getCellType() {
        return getCellTypeEnum().getCode();
    }

    public CellType getCellTypeEnum() {
        return this._cellType;
    }

    public void setCellValue(double value) {
        if (Double.isInfinite(value)) {
            setCellErrorValue(FormulaError.DIV0.getCode());
        } else if (Double.isNaN(value)) {
            setCellErrorValue(FormulaError.NUM.getCode());
        } else {
            int row = this._record.getRow();
            short col = this._record.getColumn();
            short styleIndex = this._record.getXFIndex();
            switch (C10631.$SwitchMap$org$apache$poi$ss$usermodel$CellType[this._cellType.ordinal()]) {
                case 3:
                    ((FormulaRecordAggregate) this._record).setCachedDoubleResult(value);
                    return;
                case 4:
                    break;
                default:
                    setCellType(CellType.NUMERIC, false, row, col, styleIndex);
                    break;
            }
            ((NumberRecord) this._record).setValue(value);
        }
    }

    public void setCellValue(Date value) {
        setCellValue(DateUtil.getExcelDate(value, this._book.getWorkbook().isUsing1904DateWindowing()));
    }

    public void setCellValue(Calendar value) {
        setCellValue(DateUtil.getExcelDate(value, this._book.getWorkbook().isUsing1904DateWindowing()));
    }

    public void setCellValue(String value) {
        setCellValue(value == null ? null : new HSSFRichTextString(value));
    }

    public void setCellValue(RichTextString value) {
        int row = this._record.getRow();
        short col = this._record.getColumn();
        short styleIndex = this._record.getXFIndex();
        if (value == null) {
            notifyFormulaChanging();
            setCellType(CellType.BLANK, false, row, col, styleIndex);
        } else if (value.length() > SpreadsheetVersion.EXCEL97.getMaxTextLength()) {
            throw new IllegalArgumentException("The maximum length of cell contents (text) is 32,767 characters");
        } else if (this._cellType == CellType.FORMULA) {
            this._record.setCachedStringResult(value.getString());
            this._stringValue = new HSSFRichTextString(value.getString());
        } else {
            if (this._cellType != CellType.STRING) {
                setCellType(CellType.STRING, false, row, col, styleIndex);
            }
            HSSFRichTextString hvalue = (HSSFRichTextString) value;
            int index = this._book.getWorkbook().addSSTString(hvalue.getUnicodeString());
            ((LabelSSTRecord) this._record).setSSTIndex(index);
            this._stringValue = hvalue;
            this._stringValue.setWorkbookReferences(this._book.getWorkbook(), (LabelSSTRecord) this._record);
            this._stringValue.setUnicodeString(this._book.getWorkbook().getSSTString(index));
        }
    }

    public void setCellFormula(String formula) {
        if (isPartOfArrayFormulaGroup()) {
            notifyArrayFormulaChanging();
        }
        int row = this._record.getRow();
        short col = this._record.getColumn();
        short styleIndex = this._record.getXFIndex();
        if (formula == null) {
            notifyFormulaChanging();
            setCellType(CellType.BLANK, false, row, col, styleIndex);
            return;
        }
        Ptg[] ptgs = HSSFFormulaParser.parse(formula, this._book, FormulaType.CELL, this._book.getSheetIndex(this._sheet));
        setCellType(CellType.FORMULA, false, row, col, styleIndex);
        FormulaRecordAggregate agg = this._record;
        FormulaRecord frec = agg.getFormulaRecord();
        frec.setOptions((short) 2);
        frec.setValue(0.0d);
        if (agg.getXFIndex() == (short) 0) {
            agg.setXFIndex((short) 15);
        }
        agg.setParsedExpression(ptgs);
    }

    private void notifyFormulaChanging() {
        if (this._record instanceof FormulaRecordAggregate) {
            ((FormulaRecordAggregate) this._record).notifyFormulaChanging();
        }
    }

    public String getCellFormula() {
        if (this._record instanceof FormulaRecordAggregate) {
            return HSSFFormulaParser.toFormulaString(this._book, ((FormulaRecordAggregate) this._record).getFormulaTokens());
        }
        throw typeMismatch(CellType.FORMULA, this._cellType, true);
    }

    private static RuntimeException typeMismatch(CellType expectedTypeCode, CellType actualTypeCode, boolean isFormulaCell) {
        return new IllegalStateException("Cannot get a " + expectedTypeCode + " value from a " + actualTypeCode + " " + (isFormulaCell ? "formula " : "") + "cell");
    }

    private static void checkFormulaCachedValueType(CellType expectedTypeCode, FormulaRecord fr) {
        CellType cachedValueType = CellType.forInt(fr.getCachedResultType());
        if (cachedValueType != expectedTypeCode) {
            throw typeMismatch(expectedTypeCode, cachedValueType, true);
        }
    }

    public double getNumericCellValue() {
        switch (C10631.$SwitchMap$org$apache$poi$ss$usermodel$CellType[this._cellType.ordinal()]) {
            case 2:
                return 0.0d;
            case 3:
                FormulaRecord fr = ((FormulaRecordAggregate) this._record).getFormulaRecord();
                checkFormulaCachedValueType(CellType.NUMERIC, fr);
                return fr.getValue();
            case 4:
                return ((NumberRecord) this._record).getValue();
            default:
                throw typeMismatch(CellType.NUMERIC, this._cellType, false);
        }
    }

    public Date getDateCellValue() {
        if (this._cellType == CellType.BLANK) {
            return null;
        }
        double value = getNumericCellValue();
        if (this._book.getWorkbook().isUsing1904DateWindowing()) {
            return DateUtil.getJavaDate(value, true);
        }
        return DateUtil.getJavaDate(value, false);
    }

    public String getStringCellValue() {
        return getRichStringCellValue().getString();
    }

    public HSSFRichTextString getRichStringCellValue() {
        switch (C10631.$SwitchMap$org$apache$poi$ss$usermodel$CellType[this._cellType.ordinal()]) {
            case 1:
                return this._stringValue;
            case 2:
                return new HSSFRichTextString("");
            case 3:
                FormulaRecordAggregate fra = this._record;
                checkFormulaCachedValueType(CellType.STRING, fra.getFormulaRecord());
                String strVal = fra.getStringValue();
                if (strVal == null) {
                    strVal = "";
                }
                return new HSSFRichTextString(strVal);
            default:
                throw typeMismatch(CellType.STRING, this._cellType, false);
        }
    }

    public void setCellValue(boolean value) {
        int row = this._record.getRow();
        short col = this._record.getColumn();
        short styleIndex = this._record.getXFIndex();
        switch (C10631.$SwitchMap$org$apache$poi$ss$usermodel$CellType[this._cellType.ordinal()]) {
            case 3:
                ((FormulaRecordAggregate) this._record).setCachedBooleanResult(value);
                return;
            case 5:
                break;
            default:
                setCellType(CellType.BOOLEAN, false, row, col, styleIndex);
                break;
        }
        ((BoolErrRecord) this._record).setValue(value);
    }

    public void setCellErrorValue(byte errorCode) {
        setCellErrorValue(FormulaError.forInt(errorCode));
    }

    public void setCellErrorValue(FormulaError error) {
        int row = this._record.getRow();
        short col = this._record.getColumn();
        short styleIndex = this._record.getXFIndex();
        switch (C10631.$SwitchMap$org$apache$poi$ss$usermodel$CellType[this._cellType.ordinal()]) {
            case 3:
                ((FormulaRecordAggregate) this._record).setCachedErrorResult(error.getCode());
                return;
            case 6:
                break;
            default:
                setCellType(CellType.ERROR, false, row, col, styleIndex);
                break;
        }
        ((BoolErrRecord) this._record).setValue(error);
    }

    private boolean convertCellValueToBoolean() {
        switch (C10631.$SwitchMap$org$apache$poi$ss$usermodel$CellType[this._cellType.ordinal()]) {
            case 1:
                return Boolean.valueOf(this._book.getWorkbook().getSSTString(((LabelSSTRecord) this._record).getSSTIndex()).getString()).booleanValue();
            case 2:
            case 6:
                return false;
            case 3:
                FormulaRecord fr = ((FormulaRecordAggregate) this._record).getFormulaRecord();
                checkFormulaCachedValueType(CellType.BOOLEAN, fr);
                return fr.getCachedBooleanValue();
            case 4:
                return ((NumberRecord) this._record).getValue() != 0.0d;
            case 5:
                return ((BoolErrRecord) this._record).getBooleanValue();
            default:
                throw new RuntimeException("Unexpected cell type (" + this._cellType + ")");
        }
    }

    private String convertCellValueToString() {
        switch (C10631.$SwitchMap$org$apache$poi$ss$usermodel$CellType[this._cellType.ordinal()]) {
            case 1:
                return this._book.getWorkbook().getSSTString(((LabelSSTRecord) this._record).getSSTIndex()).getString();
            case 2:
                return "";
            case 3:
                FormulaRecordAggregate fra = this._record;
                FormulaRecord fr = fra.getFormulaRecord();
                switch (C10631.$SwitchMap$org$apache$poi$ss$usermodel$CellType[CellType.forInt(fr.getCachedResultType()).ordinal()]) {
                    case 1:
                        return fra.getStringValue();
                    case 4:
                        return NumberToTextConverter.toText(fr.getValue());
                    case 5:
                        return fr.getCachedBooleanValue() ? "TRUE" : "FALSE";
                    case 6:
                        return FormulaError.forInt(fr.getCachedErrorValue()).getString();
                    default:
                        throw new IllegalStateException("Unexpected formula result type (" + this._cellType + ")");
                }
            case 4:
                return NumberToTextConverter.toText(((NumberRecord) this._record).getValue());
            case 5:
                return ((BoolErrRecord) this._record).getBooleanValue() ? "TRUE" : "FALSE";
            case 6:
                return FormulaError.forInt(((BoolErrRecord) this._record).getErrorValue()).getString();
            default:
                throw new IllegalStateException("Unexpected cell type (" + this._cellType + ")");
        }
    }

    public boolean getBooleanCellValue() {
        switch (C10631.$SwitchMap$org$apache$poi$ss$usermodel$CellType[this._cellType.ordinal()]) {
            case 2:
                return false;
            case 3:
                FormulaRecord fr = ((FormulaRecordAggregate) this._record).getFormulaRecord();
                checkFormulaCachedValueType(CellType.BOOLEAN, fr);
                return fr.getCachedBooleanValue();
            case 5:
                return ((BoolErrRecord) this._record).getBooleanValue();
            default:
                throw typeMismatch(CellType.BOOLEAN, this._cellType, false);
        }
    }

    public byte getErrorCellValue() {
        switch (C10631.$SwitchMap$org$apache$poi$ss$usermodel$CellType[this._cellType.ordinal()]) {
            case 3:
                FormulaRecord fr = ((FormulaRecordAggregate) this._record).getFormulaRecord();
                checkFormulaCachedValueType(CellType.ERROR, fr);
                return (byte) fr.getCachedErrorValue();
            case 6:
                return ((BoolErrRecord) this._record).getErrorValue();
            default:
                throw typeMismatch(CellType.ERROR, this._cellType, false);
        }
    }

    public void setCellStyle(CellStyle style) {
        setCellStyle((HSSFCellStyle) style);
    }

    public void setCellStyle(HSSFCellStyle style) {
        if (style == null) {
            this._record.setXFIndex((short) 15);
            return;
        }
        short styleIndex;
        style.verifyBelongsToWorkbook(this._book);
        if (style.getUserStyleName() != null) {
            styleIndex = applyUserCellStyle(style);
        } else {
            styleIndex = style.getIndex();
        }
        this._record.setXFIndex(styleIndex);
    }

    public HSSFCellStyle getCellStyle() {
        short styleIndex = this._record.getXFIndex();
        return new HSSFCellStyle(styleIndex, this._book.getWorkbook().getExFormatAt(styleIndex), this._book);
    }

    protected CellValueRecordInterface getCellValueRecord() {
        return this._record;
    }

    private static void checkBounds(int cellIndex) {
        if (cellIndex < 0 || cellIndex > LAST_COLUMN_NUMBER) {
            throw new IllegalArgumentException("Invalid column index (" + cellIndex + ").  Allowable column range for " + FILE_FORMAT_NAME + " is (0.." + LAST_COLUMN_NUMBER + ") or ('A'..'" + LAST_COLUMN_NAME + "')");
        }
    }

    public void setAsActiveCell() {
        int row = this._record.getRow();
        short col = this._record.getColumn();
        this._sheet.getSheet().setActiveCellRow(row);
        this._sheet.getSheet().setActiveCellCol(col);
    }

    public String toString() {
        switch (C10631.$SwitchMap$org$apache$poi$ss$usermodel$CellType[getCellTypeEnum().ordinal()]) {
            case 1:
                return getStringCellValue();
            case 2:
                return "";
            case 3:
                return getCellFormula();
            case 4:
                if (!DateUtil.isCellDateFormatted(this)) {
                    return String.valueOf(getNumericCellValue());
                }
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", LocaleUtil.getUserLocale());
                sdf.setTimeZone(LocaleUtil.getUserTimeZone());
                return sdf.format(getDateCellValue());
            case 5:
                return getBooleanCellValue() ? "TRUE" : "FALSE";
            case 6:
                return ErrorEval.getText(((BoolErrRecord) this._record).getErrorValue());
            default:
                return "Unknown Cell Type: " + getCellTypeEnum();
        }
    }

    public void setCellComment(Comment comment) {
        if (comment == null) {
            removeCellComment();
            return;
        }
        comment.setRow(this._record.getRow());
        comment.setColumn(this._record.getColumn());
        this._comment = (HSSFComment) comment;
    }

    public HSSFComment getCellComment() {
        if (this._comment == null) {
            this._comment = this._sheet.findCellComment(this._record.getRow(), this._record.getColumn());
        }
        return this._comment;
    }

    public void removeCellComment() {
        HSSFComment comment = this._sheet.findCellComment(this._record.getRow(), this._record.getColumn());
        this._comment = null;
        if (comment != null) {
            this._sheet.getDrawingPatriarch().removeShape(comment);
        }
    }

    public HSSFHyperlink getHyperlink() {
        return this._sheet.getHyperlink(this._record.getRow(), this._record.getColumn());
    }

    public void setHyperlink(Hyperlink hyperlink) {
        if (hyperlink == null) {
            removeHyperlink();
            return;
        }
        HSSFHyperlink link = (HSSFHyperlink) hyperlink;
        link.setFirstRow(this._record.getRow());
        link.setLastRow(this._record.getRow());
        link.setFirstColumn(this._record.getColumn());
        link.setLastColumn(this._record.getColumn());
        switch (C10631.$SwitchMap$org$apache$poi$common$usermodel$HyperlinkType[link.getTypeEnum().ordinal()]) {
            case 1:
            case 2:
                link.setLabel(Annotation.URL);
                break;
            case 3:
                link.setLabel(Annotation.FILE);
                break;
            case 4:
                link.setLabel("place");
                break;
        }
        List<RecordBase> records = this._sheet.getSheet().getRecords();
        records.add(records.size() - 1, link.record);
    }

    public void removeHyperlink() {
        Iterator<RecordBase> it = this._sheet.getSheet().getRecords().iterator();
        while (it.hasNext()) {
            RecordBase rec = (RecordBase) it.next();
            if (rec instanceof HyperlinkRecord) {
                HyperlinkRecord link = (HyperlinkRecord) rec;
                if (link.getFirstColumn() == this._record.getColumn() && link.getFirstRow() == this._record.getRow()) {
                    it.remove();
                    return;
                }
            }
        }
    }

    public int getCachedFormulaResultType() {
        return getCachedFormulaResultTypeEnum().getCode();
    }

    public CellType getCachedFormulaResultTypeEnum() {
        if (this._cellType == CellType.FORMULA) {
            return CellType.forInt(((FormulaRecordAggregate) this._record).getFormulaRecord().getCachedResultType());
        }
        throw new IllegalStateException("Only formula cells have cached results");
    }

    void setCellArrayFormula(CellRangeAddress range) {
        setCellType(CellType.FORMULA, false, this._record.getRow(), this._record.getColumn(), this._record.getXFIndex());
        this._record.setParsedExpression(new Ptg[]{new ExpPtg(range.getFirstRow(), range.getFirstColumn())});
    }

    public CellRangeAddress getArrayFormulaRange() {
        if (this._cellType == CellType.FORMULA) {
            return ((FormulaRecordAggregate) this._record).getArrayFormulaRange();
        }
        throw new IllegalStateException("Cell " + new CellReference((Cell) this).formatAsString() + " is not part of an array formula.");
    }

    public boolean isPartOfArrayFormulaGroup() {
        if (this._cellType != CellType.FORMULA) {
            return false;
        }
        return ((FormulaRecordAggregate) this._record).isPartOfArrayFormula();
    }

    void notifyArrayFormulaChanging(String msg) {
        if (getArrayFormulaRange().getNumberOfCells() > 1) {
            throw new IllegalStateException(msg);
        }
        getRow().getSheet().removeArrayFormula(this);
    }

    void notifyArrayFormulaChanging() {
        notifyArrayFormulaChanging("Cell " + new CellReference((Cell) this).formatAsString() + " is part of a multi-cell array formula. " + "You cannot change part of an array.");
    }

    private short applyUserCellStyle(HSSFCellStyle style) {
        if (style.getUserStyleName() == null) {
            throw new IllegalArgumentException("Expected user-defined style");
        }
        InternalWorkbook iwb = this._book.getWorkbook();
        short userXf = (short) -1;
        short numfmt = iwb.getNumExFormats();
        for (short i = (short) 0; i < numfmt; i = (short) (i + 1)) {
            ExtendedFormatRecord xf = iwb.getExFormatAt(i);
            if (xf.getXFType() == (short) 0 && xf.getParentIndex() == style.getIndex()) {
                userXf = i;
                break;
            }
        }
        if (userXf != (short) -1) {
            return userXf;
        }
        ExtendedFormatRecord xfr = iwb.createCellXF();
        xfr.cloneStyleFrom(iwb.getExFormatAt(style.getIndex()));
        xfr.setIndentionOptions((short) 0);
        xfr.setXFType((short) 0);
        xfr.setParentIndex(style.getIndex());
        return (short) numfmt;
    }
}

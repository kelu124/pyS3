package org.apache.poi.hssf.model;

import com.itextpdf.text.pdf.codec.wmf.MetaDo;
import com.lee.ultrascan.library.ProbeParams;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.poi.ddf.EscherBSERecord;
import org.apache.poi.ddf.EscherBoolProperty;
import org.apache.poi.ddf.EscherContainerRecord;
import org.apache.poi.ddf.EscherDgRecord;
import org.apache.poi.ddf.EscherDggRecord;
import org.apache.poi.ddf.EscherDggRecord.FileIdCluster;
import org.apache.poi.ddf.EscherOptRecord;
import org.apache.poi.ddf.EscherProperties;
import org.apache.poi.ddf.EscherRGBProperty;
import org.apache.poi.ddf.EscherRecord;
import org.apache.poi.ddf.EscherSimpleProperty;
import org.apache.poi.ddf.EscherSpRecord;
import org.apache.poi.ddf.EscherSplitMenuColorsRecord;
import org.apache.poi.hssf.record.BOFRecord;
import org.apache.poi.hssf.record.BackupRecord;
import org.apache.poi.hssf.record.BookBoolRecord;
import org.apache.poi.hssf.record.BoundSheetRecord;
import org.apache.poi.hssf.record.CodepageRecord;
import org.apache.poi.hssf.record.CountryRecord;
import org.apache.poi.hssf.record.DSFRecord;
import org.apache.poi.hssf.record.DateWindow1904Record;
import org.apache.poi.hssf.record.DrawingGroupRecord;
import org.apache.poi.hssf.record.EOFRecord;
import org.apache.poi.hssf.record.EscherAggregate;
import org.apache.poi.hssf.record.ExtSSTRecord;
import org.apache.poi.hssf.record.ExtendedFormatRecord;
import org.apache.poi.hssf.record.FileSharingRecord;
import org.apache.poi.hssf.record.FnGroupCountRecord;
import org.apache.poi.hssf.record.FontRecord;
import org.apache.poi.hssf.record.FormatRecord;
import org.apache.poi.hssf.record.HideObjRecord;
import org.apache.poi.hssf.record.HyperlinkRecord;
import org.apache.poi.hssf.record.InterfaceEndRecord;
import org.apache.poi.hssf.record.InterfaceHdrRecord;
import org.apache.poi.hssf.record.MMSRecord;
import org.apache.poi.hssf.record.NameCommentRecord;
import org.apache.poi.hssf.record.NameRecord;
import org.apache.poi.hssf.record.PaletteRecord;
import org.apache.poi.hssf.record.PasswordRecord;
import org.apache.poi.hssf.record.PasswordRev4Record;
import org.apache.poi.hssf.record.PrecisionRecord;
import org.apache.poi.hssf.record.ProtectRecord;
import org.apache.poi.hssf.record.ProtectionRev4Record;
import org.apache.poi.hssf.record.RecalcIdRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RefreshAllRecord;
import org.apache.poi.hssf.record.SSTRecord;
import org.apache.poi.hssf.record.StyleRecord;
import org.apache.poi.hssf.record.TabIdRecord;
import org.apache.poi.hssf.record.UseSelFSRecord;
import org.apache.poi.hssf.record.WindowOneRecord;
import org.apache.poi.hssf.record.WindowProtectRecord;
import org.apache.poi.hssf.record.WriteAccessRecord;
import org.apache.poi.hssf.record.WriteProtectRecord;
import org.apache.poi.hssf.record.common.UnicodeString;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFShape;
import org.apache.poi.poifs.crypt.CryptoFunctions;
import org.apache.poi.ss.formula.EvaluationWorkbook.ExternalName;
import org.apache.poi.ss.formula.EvaluationWorkbook.ExternalSheet;
import org.apache.poi.ss.formula.EvaluationWorkbook.ExternalSheetRange;
import org.apache.poi.ss.formula.FormulaShifter;
import org.apache.poi.ss.formula.ptg.Area3DPtg;
import org.apache.poi.ss.formula.ptg.NameXPtg;
import org.apache.poi.ss.formula.ptg.OperandPtg;
import org.apache.poi.ss.formula.ptg.Ptg;
import org.apache.poi.ss.formula.ptg.Ref3DPtg;
import org.apache.poi.ss.formula.udf.UDFFinder;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.Internal;
import org.apache.poi.util.LocaleUtil;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;
import org.bytedeco.javacpp.avutil;
import org.bytedeco.javacpp.opencv_videoio;

@Internal
public final class InternalWorkbook {
    private static final short CODEPAGE = (short) 1200;
    private static final int DEBUG = 1;
    private static final int MAX_SENSITIVE_SHEET_NAME_LEN = 31;
    public static final String OLD_WORKBOOK_DIR_ENTRY_NAME = "Book";
    public static final String[] WORKBOOK_DIR_ENTRY_NAMES = new String[]{"Workbook", "WORKBOOK", "BOOK"};
    private static final POILogger log = POILogFactory.getLogger(InternalWorkbook.class);
    private final List<BoundSheetRecord> boundsheets = new ArrayList();
    private final Map<String, NameCommentRecord> commentRecords = new LinkedHashMap();
    private DrawingManager2 drawingManager;
    private List<EscherBSERecord> escherBSERecords = new ArrayList();
    private FileSharingRecord fileShare;
    private final List<FormatRecord> formats = new ArrayList();
    private final List<HyperlinkRecord> hyperlinks = new ArrayList();
    private LinkTable linkTable;
    private int maxformatid = -1;
    private int numfonts = 0;
    private int numxfs = 0;
    private final WorkbookRecordList records = new WorkbookRecordList();
    protected SSTRecord sst;
    private boolean uses1904datewindowing = false;
    private WindowOneRecord windowOne;
    private WriteAccessRecord writeAccess;
    private WriteProtectRecord writeProtect;

    private InternalWorkbook() {
    }

    public static InternalWorkbook createWorkbook(List<Record> recs) {
        if (log.check(1)) {
            log.log(1, "Workbook (readfile) created with reclen=", Integer.valueOf(recs.size()));
        }
        InternalWorkbook retval = new InternalWorkbook();
        List<Record> records = new ArrayList(recs.size() / 3);
        retval.records.setRecords(records);
        int k = 0;
        while (k < recs.size()) {
            Record rec = (Record) recs.get(k);
            if (rec.getSid() == (short) 10) {
                records.add(rec);
                if (log.check(1)) {
                    log.log(1, "found workbook eof record at " + k);
                }
                while (k < recs.size()) {
                    rec = (Record) recs.get(k);
                    switch (rec.getSid()) {
                        case opencv_videoio.CV_CAP_PROP_XI_AEAG_ROI_OFFSET_Y /*440*/:
                            retval.hyperlinks.add((HyperlinkRecord) rec);
                            break;
                        default:
                            break;
                    }
                    k++;
                }
                if (retval.windowOne == null) {
                    retval.windowOne = createWindowOne();
                }
                if (log.check(1)) {
                    log.log(1, "exit create workbook from existing file function");
                }
                return retval;
            }
            switch (rec.getSid()) {
                case (short) 18:
                    if (log.check(1)) {
                        log.log(1, "found protect record at " + k);
                    }
                    retval.records.setProtpos(k);
                    break;
                case (short) 23:
                    throw new RuntimeException("Extern sheet is part of LinkTable");
                case (short) 24:
                case (short) 430:
                    if (log.check(1)) {
                        log.log(1, "found SupBook record at " + k);
                    }
                    retval.linkTable = new LinkTable(recs, k, retval.records, retval.commentRecords);
                    k += retval.linkTable.getRecordCount() - 1;
                    continue;
                case (short) 34:
                    if (log.check(1)) {
                        log.log(1, "found datewindow1904 record at " + k);
                    }
                    retval.uses1904datewindowing = ((DateWindow1904Record) rec).getWindowing() == (short) 1;
                    break;
                case (short) 49:
                    if (log.check(1)) {
                        log.log(1, "found font record at " + k);
                    }
                    retval.records.setFontpos(k);
                    retval.numfonts++;
                    break;
                case (short) 61:
                    if (log.check(1)) {
                        log.log(1, "found WindowOneRecord at " + k);
                    }
                    retval.windowOne = (WindowOneRecord) rec;
                    break;
                case (short) 64:
                    if (log.check(1)) {
                        log.log(1, "found backup record at " + k);
                    }
                    retval.records.setBackuppos(k);
                    break;
                case (short) 91:
                    if (log.check(1)) {
                        log.log(1, "found FileSharing at " + k);
                    }
                    retval.fileShare = (FileSharingRecord) rec;
                    break;
                case (short) 92:
                    if (log.check(1)) {
                        log.log(1, "found WriteAccess at " + k);
                    }
                    retval.writeAccess = (WriteAccessRecord) rec;
                    break;
                case (short) 133:
                    if (log.check(1)) {
                        log.log(1, "found boundsheet record at " + k);
                    }
                    retval.boundsheets.add((BoundSheetRecord) rec);
                    retval.records.setBspos(k);
                    break;
                case (short) 134:
                    if (log.check(1)) {
                        log.log(1, "found WriteProtect at " + k);
                    }
                    retval.writeProtect = (WriteProtectRecord) rec;
                    break;
                case (short) 146:
                    if (log.check(1)) {
                        log.log(1, "found palette record at " + k);
                    }
                    retval.records.setPalettepos(k);
                    break;
                case (short) 224:
                    if (log.check(1)) {
                        log.log(1, "found XF record at " + k);
                    }
                    retval.records.setXfpos(k);
                    retval.numxfs++;
                    break;
                case (short) 252:
                    if (log.check(1)) {
                        log.log(1, "found sst record at " + k);
                    }
                    retval.sst = (SSTRecord) rec;
                    break;
                case (short) 317:
                    if (log.check(1)) {
                        log.log(1, "found tabid record at " + k);
                    }
                    retval.records.setTabpos(k);
                    break;
                case (short) 1054:
                    if (log.check(1)) {
                        log.log(1, "found format record at " + k);
                    }
                    retval.formats.add((FormatRecord) rec);
                    retval.maxformatid = retval.maxformatid >= ((FormatRecord) rec).getIndexCode() ? retval.maxformatid : ((FormatRecord) rec).getIndexCode();
                    break;
                case (short) 2196:
                    NameCommentRecord ncr = (NameCommentRecord) rec;
                    if (log.check(1)) {
                        log.log(1, "found NameComment at " + k);
                    }
                    retval.commentRecords.put(ncr.getNameText(), ncr);
                    break;
                default:
                    if (log.check(1)) {
                        log.log(1, "ignoring record (sid=" + rec.getSid() + ") at " + k);
                        break;
                    }
                    break;
            }
            records.add(rec);
            k++;
        }
        while (k < recs.size()) {
            rec = (Record) recs.get(k);
            switch (rec.getSid()) {
                case opencv_videoio.CV_CAP_PROP_XI_AEAG_ROI_OFFSET_Y /*440*/:
                    retval.hyperlinks.add((HyperlinkRecord) rec);
                    break;
                default:
                    break;
            }
            k++;
        }
        if (retval.windowOne == null) {
            retval.windowOne = createWindowOne();
        }
        if (log.check(1)) {
            log.log(1, "exit create workbook from existing file function");
        }
        return retval;
    }

    public static InternalWorkbook createWorkbook() {
        int k;
        if (log.check(1)) {
            log.log(1, "creating new workbook from scratch");
        }
        InternalWorkbook retval = new InternalWorkbook();
        List<Record> records = new ArrayList(30);
        retval.records.setRecords(records);
        List<FormatRecord> formats = retval.formats;
        records.add(createBOF());
        records.add(new InterfaceHdrRecord(1200));
        records.add(createMMS());
        records.add(InterfaceEndRecord.instance);
        records.add(createWriteAccess());
        records.add(createCodepage());
        records.add(createDSF());
        records.add(createTabId());
        retval.records.setTabpos(records.size() - 1);
        records.add(createFnGroupCount());
        records.add(createWindowProtect());
        records.add(createProtect());
        retval.records.setProtpos(records.size() - 1);
        records.add(createPassword());
        records.add(createProtectionRev4());
        records.add(createPasswordRev4());
        retval.windowOne = createWindowOne();
        records.add(retval.windowOne);
        records.add(createBackup());
        retval.records.setBackuppos(records.size() - 1);
        records.add(createHideObj());
        records.add(createDateWindow1904());
        records.add(createPrecision());
        records.add(createRefreshAll());
        records.add(createBookBool());
        records.add(createFont());
        records.add(createFont());
        records.add(createFont());
        records.add(createFont());
        retval.records.setFontpos(records.size() - 1);
        retval.numfonts = 4;
        for (int i = 0; i <= 7; i++) {
            FormatRecord rec = createFormat(i);
            retval.maxformatid = retval.maxformatid >= rec.getIndexCode() ? retval.maxformatid : rec.getIndexCode();
            formats.add(rec);
            records.add(rec);
        }
        for (k = 0; k < 21; k++) {
            records.add(createExtendedFormat(k));
            retval.numxfs++;
        }
        retval.records.setXfpos(records.size() - 1);
        for (k = 0; k < 6; k++) {
            records.add(createStyle(k));
        }
        records.add(createUseSelFS());
        for (k = 0; k < 1; k++) {
            BoundSheetRecord bsr = createBoundSheet(k);
            records.add(bsr);
            retval.boundsheets.add(bsr);
            retval.records.setBspos(records.size() - 1);
        }
        records.add(createCountry());
        for (k = 0; k < 1; k++) {
            retval.getOrCreateLinkTable().checkExternSheet(k);
        }
        retval.sst = new SSTRecord();
        records.add(retval.sst);
        records.add(createExtendedSST());
        records.add(EOFRecord.instance);
        if (log.check(1)) {
            log.log(1, "exit create new workbook from scratch");
        }
        return retval;
    }

    public NameRecord getSpecificBuiltinRecord(byte name, int sheetNumber) {
        return getOrCreateLinkTable().getSpecificBuiltinRecord(name, sheetNumber);
    }

    public void removeBuiltinRecord(byte name, int sheetIndex) {
        this.linkTable.removeBuiltinRecord(name, sheetIndex);
    }

    public int getNumRecords() {
        return this.records.size();
    }

    public FontRecord getFontRecordAt(int idx) {
        int index = idx;
        if (index > 4) {
            index--;
        }
        if (index <= this.numfonts - 1) {
            return (FontRecord) this.records.get((this.records.getFontpos() - (this.numfonts - 1)) + index);
        }
        throw new ArrayIndexOutOfBoundsException("There are only " + this.numfonts + " font records, you asked for " + idx);
    }

    public int getFontIndex(FontRecord font) {
        int i = 0;
        while (i <= this.numfonts) {
            if (((FontRecord) this.records.get((this.records.getFontpos() - (this.numfonts - 1)) + i)) != font) {
                i++;
            } else if (i > 3) {
                return i + 1;
            } else {
                return i;
            }
        }
        throw new IllegalArgumentException("Could not find that font!");
    }

    public FontRecord createNewFont() {
        FontRecord rec = createFont();
        this.records.add(this.records.getFontpos() + 1, rec);
        this.records.setFontpos(this.records.getFontpos() + 1);
        this.numfonts++;
        return rec;
    }

    public void removeFontRecord(FontRecord rec) {
        this.records.remove(rec);
        this.numfonts--;
    }

    public int getNumberOfFontRecords() {
        return this.numfonts;
    }

    public void setSheetBof(int sheetIndex, int pos) {
        if (log.check(1)) {
            log.log(1, "setting bof for sheetnum =", Integer.valueOf(sheetIndex), " at pos=", Integer.valueOf(pos));
        }
        checkSheets(sheetIndex);
        getBoundSheetRec(sheetIndex).setPositionOfBof(pos);
    }

    private BoundSheetRecord getBoundSheetRec(int sheetIndex) {
        return (BoundSheetRecord) this.boundsheets.get(sheetIndex);
    }

    public BackupRecord getBackupRecord() {
        return (BackupRecord) this.records.get(this.records.getBackuppos());
    }

    public void setSheetName(int sheetnum, String sheetname) {
        checkSheets(sheetnum);
        if (sheetname.length() > 31) {
            sheetname = sheetname.substring(0, 31);
        }
        ((BoundSheetRecord) this.boundsheets.get(sheetnum)).setSheetname(sheetname);
    }

    public boolean doesContainsSheetName(String name, int excludeSheetIdx) {
        String aName = name;
        if (aName.length() > 31) {
            aName = aName.substring(0, 31);
        }
        for (int i = 0; i < this.boundsheets.size(); i++) {
            BoundSheetRecord boundSheetRecord = getBoundSheetRec(i);
            if (excludeSheetIdx != i) {
                String bName = boundSheetRecord.getSheetname();
                if (bName.length() > 31) {
                    bName = bName.substring(0, 31);
                }
                if (aName.equalsIgnoreCase(bName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setSheetOrder(String sheetname, int pos) {
        int sheetNumber = getSheetIndex(sheetname);
        this.boundsheets.add(pos, this.boundsheets.remove(sheetNumber));
        int initialBspos = this.records.getBspos();
        int pos0 = initialBspos - (this.boundsheets.size() - 1);
        Record removed = this.records.get(pos0 + sheetNumber);
        this.records.remove(pos0 + sheetNumber);
        this.records.add(pos0 + pos, removed);
        this.records.setBspos(initialBspos);
    }

    public String getSheetName(int sheetIndex) {
        return getBoundSheetRec(sheetIndex).getSheetname();
    }

    public boolean isSheetHidden(int sheetnum) {
        return getBoundSheetRec(sheetnum).isHidden();
    }

    public boolean isSheetVeryHidden(int sheetnum) {
        return getBoundSheetRec(sheetnum).isVeryHidden();
    }

    public void setSheetHidden(int sheetnum, boolean hidden) {
        getBoundSheetRec(sheetnum).setHidden(hidden);
    }

    public void setSheetHidden(int sheetnum, int hidden) {
        BoundSheetRecord bsr = getBoundSheetRec(sheetnum);
        boolean h = false;
        boolean vh = false;
        if (hidden != 0) {
            if (hidden == 1) {
                h = true;
            } else if (hidden == 2) {
                vh = true;
            } else {
                throw new IllegalArgumentException("Invalid hidden flag " + hidden + " given, must be 0, 1 or 2");
            }
        }
        bsr.setHidden(h);
        bsr.setVeryHidden(vh);
    }

    public int getSheetIndex(String name) {
        for (int k = 0; k < this.boundsheets.size(); k++) {
            if (getSheetName(k).equalsIgnoreCase(name)) {
                return k;
            }
        }
        return -1;
    }

    private void checkSheets(int sheetnum) {
        if (this.boundsheets.size() > sheetnum) {
            return;
        }
        if (this.boundsheets.size() + 1 <= sheetnum) {
            throw new RuntimeException("Sheet number out of bounds!");
        }
        BoundSheetRecord bsr = createBoundSheet(sheetnum);
        this.records.add(this.records.getBspos() + 1, bsr);
        this.records.setBspos(this.records.getBspos() + 1);
        this.boundsheets.add(bsr);
        getOrCreateLinkTable().checkExternSheet(sheetnum);
        fixTabIdRecord();
    }

    public void removeSheet(int sheetIndex) {
        if (this.boundsheets.size() > sheetIndex) {
            this.records.remove((this.records.getBspos() - (this.boundsheets.size() - 1)) + sheetIndex);
            this.boundsheets.remove(sheetIndex);
            fixTabIdRecord();
        }
        int sheetNum1Based = sheetIndex + 1;
        for (int i = 0; i < getNumNames(); i++) {
            NameRecord nr = getNameRecord(i);
            if (nr.getSheetNumber() == sheetNum1Based) {
                nr.setSheetNumber(0);
            } else if (nr.getSheetNumber() > sheetNum1Based) {
                nr.setSheetNumber(nr.getSheetNumber() - 1);
            }
        }
        if (this.linkTable != null) {
            this.linkTable.removeSheet(sheetIndex);
        }
    }

    private void fixTabIdRecord() {
        Record rec = this.records.get(this.records.getTabpos());
        if (this.records.getTabpos() > 0) {
            TabIdRecord tir = (TabIdRecord) rec;
            short[] tia = new short[this.boundsheets.size()];
            for (short k = (short) 0; k < tia.length; k = (short) (k + 1)) {
                tia[k] = k;
            }
            tir.setTabIdArray(tia);
        }
    }

    public int getNumSheets() {
        if (log.check(1)) {
            log.log(1, "getNumSheets=", Integer.valueOf(this.boundsheets.size()));
        }
        return this.boundsheets.size();
    }

    public int getNumExFormats() {
        if (log.check(1)) {
            log.log(1, "getXF=", Integer.valueOf(this.numxfs));
        }
        return this.numxfs;
    }

    public ExtendedFormatRecord getExFormatAt(int index) {
        return (ExtendedFormatRecord) this.records.get((this.records.getXfpos() - (this.numxfs - 1)) + index);
    }

    public void removeExFormatRecord(ExtendedFormatRecord rec) {
        this.records.remove(rec);
        this.numxfs--;
    }

    public void removeExFormatRecord(int index) {
        this.records.remove((this.records.getXfpos() - (this.numxfs - 1)) + index);
        this.numxfs--;
    }

    public ExtendedFormatRecord createCellXF() {
        ExtendedFormatRecord xf = createExtendedFormat();
        this.records.add(this.records.getXfpos() + 1, xf);
        this.records.setXfpos(this.records.getXfpos() + 1);
        this.numxfs++;
        return xf;
    }

    public StyleRecord getStyleRecord(int xfIndex) {
        for (int i = this.records.getXfpos(); i < this.records.size(); i++) {
            Record r = this.records.get(i);
            if (!(r instanceof ExtendedFormatRecord) && (r instanceof StyleRecord)) {
                StyleRecord sr = (StyleRecord) r;
                if (sr.getXFIndex() == xfIndex) {
                    return sr;
                }
            }
        }
        return null;
    }

    public StyleRecord createStyleRecord(int xfIndex) {
        StyleRecord newSR = new StyleRecord();
        newSR.setXFIndex(xfIndex);
        int addAt = -1;
        for (int i = this.records.getXfpos(); i < this.records.size() && addAt == -1; i++) {
            Record r = this.records.get(i);
            if (!((r instanceof ExtendedFormatRecord) || (r instanceof StyleRecord))) {
                addAt = i;
            }
        }
        if (addAt == -1) {
            throw new IllegalStateException("No XF Records found!");
        }
        this.records.add(addAt, newSR);
        return newSR;
    }

    public int addSSTString(UnicodeString string) {
        if (log.check(1)) {
            log.log(1, "insert to sst string='", string);
        }
        if (this.sst == null) {
            insertSST();
        }
        return this.sst.addString(string);
    }

    public UnicodeString getSSTString(int str) {
        if (this.sst == null) {
            insertSST();
        }
        UnicodeString retval = this.sst.getString(str);
        if (log.check(1)) {
            log.log(1, "Returning SST for index=", Integer.valueOf(str), " String= ", retval);
        }
        return retval;
    }

    public void insertSST() {
        if (log.check(1)) {
            log.log(1, "creating new SST via insertSST!");
        }
        this.sst = new SSTRecord();
        this.records.add(this.records.size() - 1, createExtendedSST());
        this.records.add(this.records.size() - 2, this.sst);
    }

    public int serialize(int offset, byte[] data) {
        if (log.check(1)) {
            log.log(1, "Serializing Workbook with offsets");
        }
        int pos = 0;
        SSTRecord sst = null;
        int sstPos = 0;
        boolean wroteBoundSheets = false;
        for (int k = 0; k < this.records.size(); k++) {
            Record record = this.records.get(k);
            int len = 0;
            if (record instanceof SSTRecord) {
                sst = (SSTRecord) record;
                sstPos = pos;
            }
            if (record.getSid() == (short) 255 && sst != null) {
                record = sst.createExtSSTRecord(sstPos + offset);
            }
            if (!(record instanceof BoundSheetRecord)) {
                len = record.serialize(pos + offset, data);
            } else if (!wroteBoundSheets) {
                for (int i = 0; i < this.boundsheets.size(); i++) {
                    len += getBoundSheetRec(i).serialize((pos + offset) + len, data);
                }
                wroteBoundSheets = true;
            }
            pos += len;
        }
        if (log.check(1)) {
            log.log(1, "Exiting serialize workbook");
        }
        return pos;
    }

    public void preSerialize() {
        if (this.records.getTabpos() > 0 && ((TabIdRecord) this.records.get(this.records.getTabpos()))._tabids.length < this.boundsheets.size()) {
            fixTabIdRecord();
        }
    }

    public int getSize() {
        int retval = 0;
        SSTRecord sst = null;
        for (int k = 0; k < this.records.size(); k++) {
            Record record = this.records.get(k);
            if (record instanceof SSTRecord) {
                sst = (SSTRecord) record;
            }
            if (record.getSid() != (short) 255 || sst == null) {
                retval += record.getRecordSize();
            } else {
                retval += sst.calcExtSSTRecordSize();
            }
        }
        return retval;
    }

    private static BOFRecord createBOF() {
        BOFRecord retval = new BOFRecord();
        retval.setVersion(BOFRecord.VERSION);
        retval.setType(5);
        retval.setBuild(BOFRecord.BUILD);
        retval.setBuildYear(BOFRecord.BUILD_YEAR);
        retval.setHistoryBitMask(65);
        retval.setRequiredVersion(6);
        return retval;
    }

    private static MMSRecord createMMS() {
        MMSRecord retval = new MMSRecord();
        retval.setAddMenuCount((byte) 0);
        retval.setDelMenuCount((byte) 0);
        return retval;
    }

    private static WriteAccessRecord createWriteAccess() {
        WriteAccessRecord retval = new WriteAccessRecord();
        String defaultUserName = "POI";
        try {
            String username = System.getProperty("user.name");
            if (username == null) {
                username = defaultUserName;
            }
            retval.setUsername(username);
        } catch (AccessControlException e) {
            retval.setUsername(defaultUserName);
        }
        return retval;
    }

    private static CodepageRecord createCodepage() {
        CodepageRecord retval = new CodepageRecord();
        retval.setCodepage((short) 1200);
        return retval;
    }

    private static DSFRecord createDSF() {
        return new DSFRecord(false);
    }

    private static TabIdRecord createTabId() {
        return new TabIdRecord();
    }

    private static FnGroupCountRecord createFnGroupCount() {
        FnGroupCountRecord retval = new FnGroupCountRecord();
        retval.setCount((short) 14);
        return retval;
    }

    private static WindowProtectRecord createWindowProtect() {
        return new WindowProtectRecord(false);
    }

    private static ProtectRecord createProtect() {
        return new ProtectRecord(false);
    }

    private static PasswordRecord createPassword() {
        return new PasswordRecord(0);
    }

    private static ProtectionRev4Record createProtectionRev4() {
        return new ProtectionRev4Record(false);
    }

    private static PasswordRev4Record createPasswordRev4() {
        return new PasswordRev4Record(0);
    }

    private static WindowOneRecord createWindowOne() {
        WindowOneRecord retval = new WindowOneRecord();
        retval.setHorizontalHold((short) 360);
        retval.setVerticalHold(EscherProperties.BLIP__PICTURELINE);
        retval.setWidth((short) 14940);
        retval.setHeight((short) 9150);
        retval.setOptions((short) 56);
        retval.setActiveSheetIndex(0);
        retval.setFirstVisibleTab(0);
        retval.setNumSelectedTabs((short) 1);
        retval.setTabWidthRatio((short) 600);
        return retval;
    }

    private static BackupRecord createBackup() {
        BackupRecord retval = new BackupRecord();
        retval.setBackup((short) 0);
        return retval;
    }

    private static HideObjRecord createHideObj() {
        HideObjRecord retval = new HideObjRecord();
        retval.setHideObj((short) 0);
        return retval;
    }

    private static DateWindow1904Record createDateWindow1904() {
        DateWindow1904Record retval = new DateWindow1904Record();
        retval.setWindowing((short) 0);
        return retval;
    }

    private static PrecisionRecord createPrecision() {
        PrecisionRecord retval = new PrecisionRecord();
        retval.setFullPrecision(true);
        return retval;
    }

    private static RefreshAllRecord createRefreshAll() {
        return new RefreshAllRecord(false);
    }

    private static BookBoolRecord createBookBool() {
        BookBoolRecord retval = new BookBoolRecord();
        retval.setSaveLinkValues((short) 0);
        return retval;
    }

    private static FontRecord createFont() {
        FontRecord retval = new FontRecord();
        retval.setFontHeight(EscherAggregate.ST_ACTIONBUTTONMOVIE);
        retval.setAttributes((short) 0);
        retval.setColorPaletteIndex(Font.COLOR_NORMAL);
        retval.setBoldWeight((short) 400);
        retval.setFontName(HSSFFont.FONT_ARIAL);
        return retval;
    }

    private static FormatRecord createFormat(int id) {
        switch (id) {
            case 0:
                return new FormatRecord(5, BuiltinFormats.getBuiltinFormat(5));
            case 1:
                return new FormatRecord(6, BuiltinFormats.getBuiltinFormat(6));
            case 2:
                return new FormatRecord(7, BuiltinFormats.getBuiltinFormat(7));
            case 3:
                return new FormatRecord(8, BuiltinFormats.getBuiltinFormat(8));
            case 4:
                return new FormatRecord(42, BuiltinFormats.getBuiltinFormat(42));
            case 5:
                return new FormatRecord(41, BuiltinFormats.getBuiltinFormat(41));
            case 6:
                return new FormatRecord(44, BuiltinFormats.getBuiltinFormat(44));
            case 7:
                return new FormatRecord(43, BuiltinFormats.getBuiltinFormat(43));
            default:
                throw new IllegalArgumentException("Unexpected id " + id);
        }
    }

    private static ExtendedFormatRecord createExtendedFormat(int id) {
        ExtendedFormatRecord retval = new ExtendedFormatRecord();
        switch (id) {
            case 0:
                retval.setFontIndex((short) 0);
                retval.setFormatIndex((short) 0);
                retval.setCellOptions((short) -11);
                retval.setAlignmentOptions((short) 32);
                retval.setIndentionOptions((short) 0);
                retval.setBorderOptions((short) 0);
                retval.setPaletteOptions((short) 0);
                retval.setAdtlPaletteOptions((short) 0);
                retval.setFillPaletteOptions((short) 8384);
                break;
            case 1:
                retval.setFontIndex((short) 1);
                retval.setFormatIndex((short) 0);
                retval.setCellOptions((short) -11);
                retval.setAlignmentOptions((short) 32);
                retval.setIndentionOptions((short) -3072);
                retval.setBorderOptions((short) 0);
                retval.setPaletteOptions((short) 0);
                retval.setAdtlPaletteOptions((short) 0);
                retval.setFillPaletteOptions((short) 8384);
                break;
            case 2:
                retval.setFontIndex((short) 1);
                retval.setFormatIndex((short) 0);
                retval.setCellOptions((short) -11);
                retval.setAlignmentOptions((short) 32);
                retval.setIndentionOptions((short) -3072);
                retval.setBorderOptions((short) 0);
                retval.setPaletteOptions((short) 0);
                retval.setAdtlPaletteOptions((short) 0);
                retval.setFillPaletteOptions((short) 8384);
                break;
            case 3:
                retval.setFontIndex((short) 2);
                retval.setFormatIndex((short) 0);
                retval.setCellOptions((short) -11);
                retval.setAlignmentOptions((short) 32);
                retval.setIndentionOptions((short) -3072);
                retval.setBorderOptions((short) 0);
                retval.setPaletteOptions((short) 0);
                retval.setAdtlPaletteOptions((short) 0);
                retval.setFillPaletteOptions((short) 8384);
                break;
            case 4:
                retval.setFontIndex((short) 2);
                retval.setFormatIndex((short) 0);
                retval.setCellOptions((short) -11);
                retval.setAlignmentOptions((short) 32);
                retval.setIndentionOptions((short) -3072);
                retval.setBorderOptions((short) 0);
                retval.setPaletteOptions((short) 0);
                retval.setAdtlPaletteOptions((short) 0);
                retval.setFillPaletteOptions((short) 8384);
                break;
            case 5:
                retval.setFontIndex((short) 0);
                retval.setFormatIndex((short) 0);
                retval.setCellOptions((short) -11);
                retval.setAlignmentOptions((short) 32);
                retval.setIndentionOptions((short) -3072);
                retval.setBorderOptions((short) 0);
                retval.setPaletteOptions((short) 0);
                retval.setAdtlPaletteOptions((short) 0);
                retval.setFillPaletteOptions((short) 8384);
                break;
            case 6:
                retval.setFontIndex((short) 0);
                retval.setFormatIndex((short) 0);
                retval.setCellOptions((short) -11);
                retval.setAlignmentOptions((short) 32);
                retval.setIndentionOptions((short) -3072);
                retval.setBorderOptions((short) 0);
                retval.setPaletteOptions((short) 0);
                retval.setAdtlPaletteOptions((short) 0);
                retval.setFillPaletteOptions((short) 8384);
                break;
            case 7:
                retval.setFontIndex((short) 0);
                retval.setFormatIndex((short) 0);
                retval.setCellOptions((short) -11);
                retval.setAlignmentOptions((short) 32);
                retval.setIndentionOptions((short) -3072);
                retval.setBorderOptions((short) 0);
                retval.setPaletteOptions((short) 0);
                retval.setAdtlPaletteOptions((short) 0);
                retval.setFillPaletteOptions((short) 8384);
                break;
            case 8:
                retval.setFontIndex((short) 0);
                retval.setFormatIndex((short) 0);
                retval.setCellOptions((short) -11);
                retval.setAlignmentOptions((short) 32);
                retval.setIndentionOptions((short) -3072);
                retval.setBorderOptions((short) 0);
                retval.setPaletteOptions((short) 0);
                retval.setAdtlPaletteOptions((short) 0);
                retval.setFillPaletteOptions((short) 8384);
                break;
            case 9:
                retval.setFontIndex((short) 0);
                retval.setFormatIndex((short) 0);
                retval.setCellOptions((short) -11);
                retval.setAlignmentOptions((short) 32);
                retval.setIndentionOptions((short) -3072);
                retval.setBorderOptions((short) 0);
                retval.setPaletteOptions((short) 0);
                retval.setAdtlPaletteOptions((short) 0);
                retval.setFillPaletteOptions((short) 8384);
                break;
            case 10:
                retval.setFontIndex((short) 0);
                retval.setFormatIndex((short) 0);
                retval.setCellOptions((short) -11);
                retval.setAlignmentOptions((short) 32);
                retval.setIndentionOptions((short) -3072);
                retval.setBorderOptions((short) 0);
                retval.setPaletteOptions((short) 0);
                retval.setAdtlPaletteOptions((short) 0);
                retval.setFillPaletteOptions((short) 8384);
                break;
            case 11:
                retval.setFontIndex((short) 0);
                retval.setFormatIndex((short) 0);
                retval.setCellOptions((short) -11);
                retval.setAlignmentOptions((short) 32);
                retval.setIndentionOptions((short) -3072);
                retval.setBorderOptions((short) 0);
                retval.setPaletteOptions((short) 0);
                retval.setAdtlPaletteOptions((short) 0);
                retval.setFillPaletteOptions((short) 8384);
                break;
            case 12:
                retval.setFontIndex((short) 0);
                retval.setFormatIndex((short) 0);
                retval.setCellOptions((short) -11);
                retval.setAlignmentOptions((short) 32);
                retval.setIndentionOptions((short) -3072);
                retval.setBorderOptions((short) 0);
                retval.setPaletteOptions((short) 0);
                retval.setAdtlPaletteOptions((short) 0);
                retval.setFillPaletteOptions((short) 8384);
                break;
            case 13:
                retval.setFontIndex((short) 0);
                retval.setFormatIndex((short) 0);
                retval.setCellOptions((short) -11);
                retval.setAlignmentOptions((short) 32);
                retval.setIndentionOptions((short) -3072);
                retval.setBorderOptions((short) 0);
                retval.setPaletteOptions((short) 0);
                retval.setAdtlPaletteOptions((short) 0);
                retval.setFillPaletteOptions((short) 8384);
                break;
            case 14:
                retval.setFontIndex((short) 0);
                retval.setFormatIndex((short) 0);
                retval.setCellOptions((short) -11);
                retval.setAlignmentOptions((short) 32);
                retval.setIndentionOptions((short) -3072);
                retval.setBorderOptions((short) 0);
                retval.setPaletteOptions((short) 0);
                retval.setAdtlPaletteOptions((short) 0);
                retval.setFillPaletteOptions((short) 8384);
                break;
            case 15:
                retval.setFontIndex((short) 0);
                retval.setFormatIndex((short) 0);
                retval.setCellOptions((short) 1);
                retval.setAlignmentOptions((short) 32);
                retval.setIndentionOptions((short) 0);
                retval.setBorderOptions((short) 0);
                retval.setPaletteOptions((short) 0);
                retval.setAdtlPaletteOptions((short) 0);
                retval.setFillPaletteOptions((short) 8384);
                break;
            case 16:
                retval.setFontIndex((short) 1);
                retval.setFormatIndex((short) 43);
                retval.setCellOptions((short) -11);
                retval.setAlignmentOptions((short) 32);
                retval.setIndentionOptions((short) -2048);
                retval.setBorderOptions((short) 0);
                retval.setPaletteOptions((short) 0);
                retval.setAdtlPaletteOptions((short) 0);
                retval.setFillPaletteOptions((short) 8384);
                break;
            case 17:
                retval.setFontIndex((short) 1);
                retval.setFormatIndex((short) 41);
                retval.setCellOptions((short) -11);
                retval.setAlignmentOptions((short) 32);
                retval.setIndentionOptions((short) -2048);
                retval.setBorderOptions((short) 0);
                retval.setPaletteOptions((short) 0);
                retval.setAdtlPaletteOptions((short) 0);
                retval.setFillPaletteOptions((short) 8384);
                break;
            case 18:
                retval.setFontIndex((short) 1);
                retval.setFormatIndex((short) 44);
                retval.setCellOptions((short) -11);
                retval.setAlignmentOptions((short) 32);
                retval.setIndentionOptions((short) -2048);
                retval.setBorderOptions((short) 0);
                retval.setPaletteOptions((short) 0);
                retval.setAdtlPaletteOptions((short) 0);
                retval.setFillPaletteOptions((short) 8384);
                break;
            case 19:
                retval.setFontIndex((short) 1);
                retval.setFormatIndex((short) 42);
                retval.setCellOptions((short) -11);
                retval.setAlignmentOptions((short) 32);
                retval.setIndentionOptions((short) -2048);
                retval.setBorderOptions((short) 0);
                retval.setPaletteOptions((short) 0);
                retval.setAdtlPaletteOptions((short) 0);
                retval.setFillPaletteOptions((short) 8384);
                break;
            case 20:
                retval.setFontIndex((short) 1);
                retval.setFormatIndex((short) 9);
                retval.setCellOptions((short) -11);
                retval.setAlignmentOptions((short) 32);
                retval.setIndentionOptions((short) -2048);
                retval.setBorderOptions((short) 0);
                retval.setPaletteOptions((short) 0);
                retval.setAdtlPaletteOptions((short) 0);
                retval.setFillPaletteOptions((short) 8384);
                break;
            case 21:
                retval.setFontIndex((short) 5);
                retval.setFormatIndex((short) 0);
                retval.setCellOptions((short) 1);
                retval.setAlignmentOptions((short) 32);
                retval.setIndentionOptions(ProbeParams.BBP_CTL_DECI1_BYPASS);
                retval.setBorderOptions((short) 0);
                retval.setPaletteOptions((short) 0);
                retval.setAdtlPaletteOptions((short) 0);
                retval.setFillPaletteOptions((short) 8384);
                break;
            case 22:
                retval.setFontIndex((short) 6);
                retval.setFormatIndex((short) 0);
                retval.setCellOptions((short) 1);
                retval.setAlignmentOptions((short) 32);
                retval.setIndentionOptions((short) 23552);
                retval.setBorderOptions((short) 0);
                retval.setPaletteOptions((short) 0);
                retval.setAdtlPaletteOptions((short) 0);
                retval.setFillPaletteOptions((short) 8384);
                break;
            case 23:
                retval.setFontIndex((short) 0);
                retval.setFormatIndex((short) 49);
                retval.setCellOptions((short) 1);
                retval.setAlignmentOptions((short) 32);
                retval.setIndentionOptions((short) 23552);
                retval.setBorderOptions((short) 0);
                retval.setPaletteOptions((short) 0);
                retval.setAdtlPaletteOptions((short) 0);
                retval.setFillPaletteOptions((short) 8384);
                break;
            case 24:
                retval.setFontIndex((short) 0);
                retval.setFormatIndex((short) 8);
                retval.setCellOptions((short) 1);
                retval.setAlignmentOptions((short) 32);
                retval.setIndentionOptions((short) 23552);
                retval.setBorderOptions((short) 0);
                retval.setPaletteOptions((short) 0);
                retval.setAdtlPaletteOptions((short) 0);
                retval.setFillPaletteOptions((short) 8384);
                break;
            case 25:
                retval.setFontIndex((short) 6);
                retval.setFormatIndex((short) 8);
                retval.setCellOptions((short) 1);
                retval.setAlignmentOptions((short) 32);
                retval.setIndentionOptions((short) 23552);
                retval.setBorderOptions((short) 0);
                retval.setPaletteOptions((short) 0);
                retval.setAdtlPaletteOptions((short) 0);
                retval.setFillPaletteOptions((short) 8384);
                break;
            default:
                throw new IllegalStateException("Unrecognized format id: " + id);
        }
        return retval;
    }

    private static ExtendedFormatRecord createExtendedFormat() {
        ExtendedFormatRecord retval = new ExtendedFormatRecord();
        retval.setFontIndex((short) 0);
        retval.setFormatIndex((short) 0);
        retval.setCellOptions((short) 1);
        retval.setAlignmentOptions((short) 32);
        retval.setIndentionOptions((short) 0);
        retval.setBorderOptions((short) 0);
        retval.setPaletteOptions((short) 0);
        retval.setAdtlPaletteOptions((short) 0);
        retval.setFillPaletteOptions((short) 8384);
        retval.setTopBorderPaletteIdx((short) 8);
        retval.setBottomBorderPaletteIdx((short) 8);
        retval.setLeftBorderPaletteIdx((short) 8);
        retval.setRightBorderPaletteIdx((short) 8);
        return retval;
    }

    private static StyleRecord createStyle(int id) {
        StyleRecord retval = new StyleRecord();
        switch (id) {
            case 0:
                retval.setXFIndex(16);
                retval.setBuiltinStyle(3);
                retval.setOutlineStyleLevel(-1);
                break;
            case 1:
                retval.setXFIndex(17);
                retval.setBuiltinStyle(6);
                retval.setOutlineStyleLevel(-1);
                break;
            case 2:
                retval.setXFIndex(18);
                retval.setBuiltinStyle(4);
                retval.setOutlineStyleLevel(-1);
                break;
            case 3:
                retval.setXFIndex(19);
                retval.setBuiltinStyle(7);
                retval.setOutlineStyleLevel(-1);
                break;
            case 4:
                retval.setXFIndex(0);
                retval.setBuiltinStyle(0);
                retval.setOutlineStyleLevel(-1);
                break;
            case 5:
                retval.setXFIndex(20);
                retval.setBuiltinStyle(5);
                retval.setOutlineStyleLevel(-1);
                break;
            default:
                throw new IllegalStateException("Unrecognized style id: " + id);
        }
        return retval;
    }

    private static PaletteRecord createPalette() {
        return new PaletteRecord();
    }

    private static UseSelFSRecord createUseSelFS() {
        return new UseSelFSRecord(false);
    }

    private static BoundSheetRecord createBoundSheet(int id) {
        return new BoundSheetRecord("Sheet" + (id + 1));
    }

    private static CountryRecord createCountry() {
        CountryRecord retval = new CountryRecord();
        retval.setDefaultCountry((short) 1);
        if (LocaleUtil.getUserLocale().toString().equals("ru_RU")) {
            retval.setCurrentCountry((short) 7);
        } else {
            retval.setCurrentCountry((short) 1);
        }
        return retval;
    }

    private static ExtSSTRecord createExtendedSST() {
        ExtSSTRecord retval = new ExtSSTRecord();
        retval.setNumStringsPerBucket((short) 8);
        return retval;
    }

    private LinkTable getOrCreateLinkTable() {
        if (this.linkTable == null) {
            this.linkTable = new LinkTable((short) getNumSheets(), this.records);
        }
        return this.linkTable;
    }

    public int linkExternalWorkbook(String name, Workbook externalWorkbook) {
        return getOrCreateLinkTable().linkExternalWorkbook(name, externalWorkbook);
    }

    public String findSheetFirstNameFromExternSheet(int externSheetIndex) {
        return findSheetNameFromIndex(this.linkTable.getFirstInternalSheetIndexForExtIndex(externSheetIndex));
    }

    public String findSheetLastNameFromExternSheet(int externSheetIndex) {
        return findSheetNameFromIndex(this.linkTable.getLastInternalSheetIndexForExtIndex(externSheetIndex));
    }

    private String findSheetNameFromIndex(int internalSheetIndex) {
        if (internalSheetIndex < 0) {
            return "";
        }
        if (internalSheetIndex >= this.boundsheets.size()) {
            return "";
        }
        return getSheetName(internalSheetIndex);
    }

    public ExternalSheet getExternalSheet(int externSheetIndex) {
        String[] extNames = this.linkTable.getExternalBookAndSheetName(externSheetIndex);
        if (extNames == null) {
            return null;
        }
        if (extNames.length == 2) {
            return new ExternalSheet(extNames[0], extNames[1]);
        }
        return new ExternalSheetRange(extNames[0], extNames[1], extNames[2]);
    }

    public ExternalName getExternalName(int externSheetIndex, int externNameIndex) {
        String nameName = this.linkTable.resolveNameXText(externSheetIndex, externNameIndex, this);
        if (nameName == null) {
            return null;
        }
        return new ExternalName(nameName, externNameIndex, this.linkTable.resolveNameXIx(externSheetIndex, externNameIndex));
    }

    public int getFirstSheetIndexFromExternSheetIndex(int externSheetNumber) {
        return this.linkTable.getFirstInternalSheetIndexForExtIndex(externSheetNumber);
    }

    public int getLastSheetIndexFromExternSheetIndex(int externSheetNumber) {
        return this.linkTable.getLastInternalSheetIndexForExtIndex(externSheetNumber);
    }

    public short checkExternSheet(int sheetNumber) {
        return (short) getOrCreateLinkTable().checkExternSheet(sheetNumber);
    }

    public short checkExternSheet(int firstSheetNumber, int lastSheetNumber) {
        return (short) getOrCreateLinkTable().checkExternSheet(firstSheetNumber, lastSheetNumber);
    }

    public int getExternalSheetIndex(String workbookName, String sheetName) {
        return getOrCreateLinkTable().getExternalSheetIndex(workbookName, sheetName, sheetName);
    }

    public int getExternalSheetIndex(String workbookName, String firstSheetName, String lastSheetName) {
        return getOrCreateLinkTable().getExternalSheetIndex(workbookName, firstSheetName, lastSheetName);
    }

    public int getNumNames() {
        if (this.linkTable == null) {
            return 0;
        }
        return this.linkTable.getNumNames();
    }

    public NameRecord getNameRecord(int index) {
        return this.linkTable.getNameRecord(index);
    }

    public NameCommentRecord getNameCommentRecord(NameRecord nameRecord) {
        return (NameCommentRecord) this.commentRecords.get(nameRecord.getNameText());
    }

    public NameRecord createName() {
        return addName(new NameRecord());
    }

    public NameRecord addName(NameRecord name) {
        getOrCreateLinkTable().addName(name);
        return name;
    }

    public NameRecord createBuiltInName(byte builtInName, int sheetNumber) {
        if (sheetNumber < 0 || sheetNumber + 1 > avutil.FF_LAMBDA_MAX) {
            throw new IllegalArgumentException("Sheet number [" + sheetNumber + "]is not valid ");
        }
        NameRecord name = new NameRecord(builtInName, sheetNumber);
        if (this.linkTable.nameAlreadyExists(name)) {
            throw new RuntimeException("Builtin (" + builtInName + ") already exists for sheet (" + sheetNumber + ")");
        }
        addName(name);
        return name;
    }

    public void removeName(int nameIndex) {
        if (this.linkTable.getNumNames() > nameIndex) {
            this.records.remove(findFirstRecordLocBySid((short) 24) + nameIndex);
            this.linkTable.removeName(nameIndex);
        }
    }

    public void updateNameCommentRecordCache(NameCommentRecord commentRecord) {
        if (this.commentRecords.containsValue(commentRecord)) {
            for (Entry<String, NameCommentRecord> entry : this.commentRecords.entrySet()) {
                if (((NameCommentRecord) entry.getValue()).equals(commentRecord)) {
                    this.commentRecords.remove(entry.getKey());
                    break;
                }
            }
        }
        this.commentRecords.put(commentRecord.getNameText(), commentRecord);
    }

    public short getFormat(String format, boolean createIfNotFound) {
        for (FormatRecord r : this.formats) {
            if (r.getFormatString().equals(format)) {
                return (short) r.getIndexCode();
            }
        }
        if (createIfNotFound) {
            return (short) createFormat(format);
        }
        return (short) -1;
    }

    public List<FormatRecord> getFormats() {
        return this.formats;
    }

    public int createFormat(String formatString) {
        int i = 164;
        if (this.maxformatid >= 164) {
            i = this.maxformatid + 1;
        }
        this.maxformatid = i;
        FormatRecord rec = new FormatRecord(this.maxformatid, formatString);
        int pos = 0;
        while (pos < this.records.size() && this.records.get(pos).getSid() != FormatRecord.sid) {
            pos++;
        }
        pos += this.formats.size();
        this.formats.add(rec);
        this.records.add(pos, rec);
        return this.maxformatid;
    }

    public Record findFirstRecordBySid(short sid) {
        Iterator i$ = this.records.iterator();
        while (i$.hasNext()) {
            Record record = (Record) i$.next();
            if (record.getSid() == sid) {
                return record;
            }
        }
        return null;
    }

    public int findFirstRecordLocBySid(short sid) {
        int index = 0;
        Iterator i$ = this.records.iterator();
        while (i$.hasNext()) {
            if (((Record) i$.next()).getSid() == sid) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public Record findNextRecordBySid(short sid, int pos) {
        int matches = 0;
        Iterator i$ = this.records.iterator();
        while (i$.hasNext()) {
            Record record = (Record) i$.next();
            if (record.getSid() == sid) {
                int matches2 = matches + 1;
                if (matches == pos) {
                    matches = matches2;
                    return record;
                }
                matches = matches2;
            }
        }
        return null;
    }

    public List<HyperlinkRecord> getHyperlinks() {
        return this.hyperlinks;
    }

    public List<Record> getRecords() {
        return this.records.getRecords();
    }

    public boolean isUsing1904DateWindowing() {
        return this.uses1904datewindowing;
    }

    public PaletteRecord getCustomPalette() {
        int palettePos = this.records.getPalettepos();
        if (palettePos != -1) {
            Record rec = this.records.get(palettePos);
            if (rec instanceof PaletteRecord) {
                return (PaletteRecord) rec;
            }
            throw new RuntimeException("InternalError: Expected PaletteRecord but got a '" + rec + "'");
        }
        PaletteRecord palette = createPalette();
        this.records.add(1, palette);
        this.records.setPalettepos(1);
        return palette;
    }

    public DrawingManager2 findDrawingGroup() {
        if (this.drawingManager != null) {
            return this.drawingManager;
        }
        Iterator i$ = this.records.iterator();
        while (i$.hasNext()) {
            EscherDggRecord dgg;
            EscherContainerRecord bStore;
            EscherRecord er;
            Record r = (Record) i$.next();
            if (r instanceof DrawingGroupRecord) {
                DrawingGroupRecord dg = (DrawingGroupRecord) r;
                dg.processChildRecords();
                EscherContainerRecord cr = dg.getEscherContainer();
                if (cr != null) {
                    dgg = null;
                    bStore = null;
                    Iterator<EscherRecord> it = cr.getChildIterator();
                    while (it.hasNext()) {
                        er = (EscherRecord) it.next();
                        if (er instanceof EscherDggRecord) {
                            dgg = (EscherDggRecord) er;
                        } else if (er.getRecordId() == EscherContainerRecord.BSTORE_CONTAINER) {
                            bStore = (EscherContainerRecord) er;
                        }
                    }
                    if (dgg != null) {
                        this.drawingManager = new DrawingManager2(dgg);
                        if (bStore != null) {
                            for (EscherRecord bs : bStore.getChildRecords()) {
                                if (bs instanceof EscherBSERecord) {
                                    this.escherBSERecords.add((EscherBSERecord) bs);
                                }
                            }
                        }
                        return this.drawingManager;
                    }
                } else {
                    continue;
                }
            }
        }
        int dgLoc = findFirstRecordLocBySid(DrawingGroupRecord.sid);
        if (dgLoc != -1) {
            dgg = null;
            bStore = null;
            for (EscherRecord er2 : ((DrawingGroupRecord) this.records.get(dgLoc)).getEscherRecords()) {
                if (er2 instanceof EscherDggRecord) {
                    dgg = (EscherDggRecord) er2;
                } else if (er2.getRecordId() == EscherContainerRecord.BSTORE_CONTAINER) {
                    bStore = (EscherContainerRecord) er2;
                }
            }
            if (dgg != null) {
                this.drawingManager = new DrawingManager2(dgg);
                if (bStore != null) {
                    for (EscherRecord bs2 : bStore.getChildRecords()) {
                        if (bs2 instanceof EscherBSERecord) {
                            this.escherBSERecords.add((EscherBSERecord) bs2);
                        }
                    }
                }
            }
        }
        return this.drawingManager;
    }

    public void createDrawingGroup() {
        if (this.drawingManager == null) {
            EscherContainerRecord dggContainer = new EscherContainerRecord();
            EscherDggRecord dgg = new EscherDggRecord();
            EscherOptRecord opt = new EscherOptRecord();
            EscherSplitMenuColorsRecord splitMenuColors = new EscherSplitMenuColorsRecord();
            dggContainer.setRecordId(EscherContainerRecord.DGG_CONTAINER);
            dggContainer.setOptions((short) 15);
            dgg.setRecordId(EscherDggRecord.RECORD_ID);
            dgg.setOptions((short) 0);
            dgg.setShapeIdMax(1024);
            dgg.setNumShapesSaved(0);
            dgg.setDrawingsSaved(0);
            dgg.setFileIdClusters(new FileIdCluster[0]);
            this.drawingManager = new DrawingManager2(dgg);
            EscherContainerRecord bstoreContainer = null;
            if (this.escherBSERecords.size() > 0) {
                bstoreContainer = new EscherContainerRecord();
                bstoreContainer.setRecordId(EscherContainerRecord.BSTORE_CONTAINER);
                bstoreContainer.setOptions((short) ((this.escherBSERecords.size() << 4) | 15));
                for (EscherRecord escherRecord : this.escherBSERecords) {
                    bstoreContainer.addChildRecord(escherRecord);
                }
            }
            opt.setRecordId(EscherOptRecord.RECORD_ID);
            opt.setOptions((short) 51);
            opt.addEscherProperty(new EscherBoolProperty((short) 191, 524296));
            opt.addEscherProperty(new EscherRGBProperty(EscherProperties.FILL__FILLCOLOR, 134217793));
            opt.addEscherProperty(new EscherRGBProperty(EscherProperties.LINESTYLE__COLOR, HSSFShape.LINESTYLE__COLOR_DEFAULT));
            splitMenuColors.setRecordId(EscherSplitMenuColorsRecord.RECORD_ID);
            splitMenuColors.setOptions((short) 64);
            splitMenuColors.setColor1(134217741);
            splitMenuColors.setColor2(134217740);
            splitMenuColors.setColor3(134217751);
            splitMenuColors.setColor4(268435703);
            dggContainer.addChildRecord(dgg);
            if (bstoreContainer != null) {
                dggContainer.addChildRecord(bstoreContainer);
            }
            dggContainer.addChildRecord(opt);
            dggContainer.addChildRecord(splitMenuColors);
            int dgLoc = findFirstRecordLocBySid(DrawingGroupRecord.sid);
            DrawingGroupRecord drawingGroup;
            if (dgLoc == -1) {
                drawingGroup = new DrawingGroupRecord();
                drawingGroup.addEscherRecord(dggContainer);
                getRecords().add(findFirstRecordLocBySid((short) 140) + 1, drawingGroup);
                return;
            }
            drawingGroup = new DrawingGroupRecord();
            drawingGroup.addEscherRecord(dggContainer);
            getRecords().set(dgLoc, drawingGroup);
        }
    }

    public WindowOneRecord getWindowOne() {
        return this.windowOne;
    }

    public EscherBSERecord getBSERecord(int pictureIndex) {
        return (EscherBSERecord) this.escherBSERecords.get(pictureIndex - 1);
    }

    public int addBSERecord(EscherBSERecord e) {
        EscherContainerRecord bstoreContainer;
        createDrawingGroup();
        this.escherBSERecords.add(e);
        EscherContainerRecord dggContainer = (EscherContainerRecord) ((DrawingGroupRecord) getRecords().get(findFirstRecordLocBySid(DrawingGroupRecord.sid))).getEscherRecord(0);
        if (dggContainer.getChild(1).getRecordId() == EscherContainerRecord.BSTORE_CONTAINER) {
            bstoreContainer = (EscherContainerRecord) dggContainer.getChild(1);
        } else {
            bstoreContainer = new EscherContainerRecord();
            bstoreContainer.setRecordId(EscherContainerRecord.BSTORE_CONTAINER);
            List<EscherRecord> childRecords = dggContainer.getChildRecords();
            childRecords.add(1, bstoreContainer);
            dggContainer.setChildRecords(childRecords);
        }
        bstoreContainer.setOptions((short) ((this.escherBSERecords.size() << 4) | 15));
        bstoreContainer.addChildRecord(e);
        return this.escherBSERecords.size();
    }

    public DrawingManager2 getDrawingManager() {
        return this.drawingManager;
    }

    public WriteProtectRecord getWriteProtect() {
        if (this.writeProtect == null) {
            this.writeProtect = new WriteProtectRecord();
            int i = 0;
            while (i < this.records.size() && !(this.records.get(i) instanceof BOFRecord)) {
                i++;
            }
            this.records.add(i + 1, this.writeProtect);
        }
        return this.writeProtect;
    }

    public WriteAccessRecord getWriteAccess() {
        if (this.writeAccess == null) {
            this.writeAccess = createWriteAccess();
            int i = 0;
            while (i < this.records.size() && !(this.records.get(i) instanceof InterfaceEndRecord)) {
                i++;
            }
            this.records.add(i + 1, this.writeAccess);
        }
        return this.writeAccess;
    }

    public FileSharingRecord getFileSharing() {
        if (this.fileShare == null) {
            this.fileShare = new FileSharingRecord();
            int i = 0;
            while (i < this.records.size() && !(this.records.get(i) instanceof WriteAccessRecord)) {
                i++;
            }
            this.records.add(i + 1, this.fileShare);
        }
        return this.fileShare;
    }

    public boolean isWriteProtected() {
        boolean z = true;
        if (this.fileShare == null) {
            return false;
        }
        if (getFileSharing().getReadOnly() != (short) 1) {
            z = false;
        }
        return z;
    }

    public void writeProtectWorkbook(String password, String username) {
        FileSharingRecord frec = getFileSharing();
        WriteAccessRecord waccess = getWriteAccess();
        getWriteProtect();
        frec.setReadOnly((short) 1);
        frec.setPassword((short) CryptoFunctions.createXorVerifier1(password));
        frec.setUsername(username);
        waccess.setUsername(username);
    }

    public void unwriteProtectWorkbook() {
        this.records.remove(this.fileShare);
        this.records.remove(this.writeProtect);
        this.fileShare = null;
        this.writeProtect = null;
    }

    public String resolveNameXText(int refIndex, int definedNameIndex) {
        return this.linkTable.resolveNameXText(refIndex, definedNameIndex, this);
    }

    public NameXPtg getNameXPtg(String name, int sheetRefIndex, UDFFinder udf) {
        LinkTable lnk = getOrCreateLinkTable();
        NameXPtg xptg = lnk.getNameXPtg(name, sheetRefIndex);
        if (xptg != null || udf.findFunction(name) == null) {
            return xptg;
        }
        return lnk.addNameXPtg(name);
    }

    public NameXPtg getNameXPtg(String name, UDFFinder udf) {
        return getNameXPtg(name, -1, udf);
    }

    public void cloneDrawings(InternalSheet sheet) {
        findDrawingGroup();
        if (this.drawingManager != null) {
            if (sheet.aggregateDrawingRecords(this.drawingManager, false) != -1) {
                EscherContainerRecord escherContainer = ((EscherAggregate) sheet.findFirstRecordBySid(EscherAggregate.sid)).getEscherContainer();
                if (escherContainer != null) {
                    EscherDggRecord dgg = this.drawingManager.getDgg();
                    int dgId = this.drawingManager.findNewDrawingGroupId();
                    dgg.addCluster(dgId, 0);
                    dgg.setDrawingsSaved(dgg.getDrawingsSaved() + 1);
                    EscherDgRecord dg = null;
                    Iterator<EscherRecord> it = escherContainer.getChildIterator();
                    while (it.hasNext()) {
                        EscherRecord er = (EscherRecord) it.next();
                        if (er instanceof EscherDgRecord) {
                            dg = (EscherDgRecord) er;
                            dg.setOptions((short) (dgId << 4));
                        } else if (er instanceof EscherContainerRecord) {
                            for (EscherContainerRecord shapeContainer : ((EscherContainerRecord) er).getChildRecords()) {
                                for (EscherRecord shapeChildRecord : shapeContainer.getChildRecords()) {
                                    int recordId = shapeChildRecord.getRecordId();
                                    if (recordId == -4086) {
                                        EscherSpRecord sp = (EscherSpRecord) shapeChildRecord;
                                        int shapeId = this.drawingManager.allocateShapeId((short) dgId, dg);
                                        dg.setNumShapes(dg.getNumShapes() - 1);
                                        sp.setShapeId(shapeId);
                                    } else if (recordId == -4085) {
                                        EscherSimpleProperty prop = (EscherSimpleProperty) ((EscherOptRecord) shapeChildRecord).lookup(MetaDo.META_SETROP2);
                                        if (prop != null) {
                                            EscherBSERecord bse = getBSERecord(prop.getPropertyValue());
                                            bse.setRef(bse.getRef() + 1);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public NameRecord cloneFilter(int filterDbNameIndex, int newSheetIndex) {
        NameRecord origNameRecord = getNameRecord(filterDbNameIndex);
        int newExtSheetIx = checkExternSheet(newSheetIndex);
        Ptg[] ptgs = origNameRecord.getNameDefinition();
        for (int i = 0; i < ptgs.length; i++) {
            Ptg ptg = ptgs[i];
            if (ptg instanceof Area3DPtg) {
                Area3DPtg a3p = (Area3DPtg) ((OperandPtg) ptg).copy();
                a3p.setExternSheetIndex(newExtSheetIx);
                ptgs[i] = a3p;
            } else if (ptg instanceof Ref3DPtg) {
                Ref3DPtg r3p = (Ref3DPtg) ((OperandPtg) ptg).copy();
                r3p.setExternSheetIndex(newExtSheetIx);
                ptgs[i] = r3p;
            }
        }
        NameRecord newNameRecord = createBuiltInName((byte) 13, newSheetIndex + 1);
        newNameRecord.setNameDefinition(ptgs);
        newNameRecord.setHidden(true);
        return newNameRecord;
    }

    public void updateNamesAfterCellShift(FormulaShifter shifter) {
        for (int i = 0; i < getNumNames(); i++) {
            NameRecord nr = getNameRecord(i);
            Ptg[] ptgs = nr.getNameDefinition();
            if (shifter.adjustFormula(ptgs, nr.getSheetNumber())) {
                nr.setNameDefinition(ptgs);
            }
        }
    }

    public RecalcIdRecord getRecalcId() {
        RecalcIdRecord record = (RecalcIdRecord) findFirstRecordBySid((short) 449);
        if (record != null) {
            return record;
        }
        record = new RecalcIdRecord();
        this.records.add(findFirstRecordLocBySid((short) 140) + 1, record);
        return record;
    }

    public boolean changeExternalReference(String oldUrl, String newUrl) {
        return this.linkTable.changeExternalReference(oldUrl, newUrl);
    }
}

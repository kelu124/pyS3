package org.apache.poi.hssf.usermodel;

import com.google.common.base.Ascii;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.POIDocument;
import org.apache.poi.ddf.EscherBSERecord;
import org.apache.poi.ddf.EscherBitmapBlip;
import org.apache.poi.ddf.EscherBlipRecord;
import org.apache.poi.ddf.EscherMetafileBlip;
import org.apache.poi.ddf.EscherRecord;
import org.apache.poi.hpsf.ClassID;
import org.apache.poi.hssf.OldExcelFormatException;
import org.apache.poi.hssf.model.HSSFFormulaParser;
import org.apache.poi.hssf.model.InternalSheet;
import org.apache.poi.hssf.model.InternalSheet.UnsupportedBOFType;
import org.apache.poi.hssf.model.InternalWorkbook;
import org.apache.poi.hssf.model.RecordStream;
import org.apache.poi.hssf.record.AbstractEscherHolderRecord;
import org.apache.poi.hssf.record.DrawingGroupRecord;
import org.apache.poi.hssf.record.LabelRecord;
import org.apache.poi.hssf.record.LabelSSTRecord;
import org.apache.poi.hssf.record.NameRecord;
import org.apache.poi.hssf.record.RecalcIdRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RecordFactory;
import org.apache.poi.hssf.record.UnknownRecord;
import org.apache.poi.hssf.record.common.UnicodeString;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.poifs.crypt.Decryptor;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.DocumentNode;
import org.apache.poi.poifs.filesystem.EntryUtils;
import org.apache.poi.poifs.filesystem.FilteringDirectoryNode;
import org.apache.poi.poifs.filesystem.NPOIFSDocument;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.poifs.filesystem.Ole10Native;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.formula.FormulaShifter;
import org.apache.poi.ss.formula.FormulaType;
import org.apache.poi.ss.formula.SheetNameFormatter;
import org.apache.poi.ss.formula.udf.AggregatingUDFFinder;
import org.apache.poi.ss.formula.udf.IndexedUDFFinder;
import org.apache.poi.ss.formula.udf.UDFFinder;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.util.Configurator;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.Internal;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

public final class HSSFWorkbook extends POIDocument implements Workbook {
    private static final Pattern COMMA_PATTERN = Pattern.compile(",");
    private static final int DEBUG = 1;
    public static final int INITIAL_CAPACITY = Configurator.getIntValue("HSSFWorkbook.SheetInitialCapacity", 3);
    private static final int MAX_STYLES = 4030;
    private static POILogger log = POILogFactory.getLogger(HSSFWorkbook.class);
    protected List<HSSFSheet> _sheets;
    private UDFFinder _udfFinder;
    private Map<Short, HSSFFont> fonts;
    private HSSFDataFormat formatter;
    private MissingCellPolicy missingCellPolicy;
    private ArrayList<HSSFName> names;
    private boolean preserveNodes;
    private InternalWorkbook workbook;

    public static HSSFWorkbook create(InternalWorkbook book) {
        return new HSSFWorkbook(book);
    }

    public HSSFWorkbook() {
        this(InternalWorkbook.createWorkbook());
    }

    private HSSFWorkbook(InternalWorkbook book) {
        super((DirectoryNode) null);
        this.missingCellPolicy = MissingCellPolicy.RETURN_NULL_AND_BLANK;
        this._udfFinder = new IndexedUDFFinder(AggregatingUDFFinder.DEFAULT);
        this.workbook = book;
        this._sheets = new ArrayList(INITIAL_CAPACITY);
        this.names = new ArrayList(INITIAL_CAPACITY);
    }

    public HSSFWorkbook(POIFSFileSystem fs) throws IOException {
        this(fs, true);
    }

    public HSSFWorkbook(NPOIFSFileSystem fs) throws IOException {
        this(fs.getRoot(), true);
    }

    public HSSFWorkbook(POIFSFileSystem fs, boolean preserveNodes) throws IOException {
        this(fs.getRoot(), fs, preserveNodes);
    }

    public static String getWorkbookDirEntryName(DirectoryNode directory) {
        String[] arr$ = InternalWorkbook.WORKBOOK_DIR_ENTRY_NAMES;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            String wbName = arr$[i$];
            try {
                directory.getEntry(wbName);
                return wbName;
            } catch (FileNotFoundException e) {
                i$++;
            }
        }
        try {
            directory.getEntry(Decryptor.DEFAULT_POIFS_ENTRY);
            throw new EncryptedDocumentException("The supplied spreadsheet seems to be an Encrypted .xlsx file. It must be decrypted before use by XSSF, it cannot be used by HSSF");
        } catch (FileNotFoundException e2) {
            directory.getEntry(InternalWorkbook.OLD_WORKBOOK_DIR_ENTRY_NAME);
            throw new OldExcelFormatException("The supplied spreadsheet seems to be Excel 5.0/7.0 (BIFF5) format. POI only supports BIFF8 format (from Excel versions 97/2000/XP/2003)");
        } catch (FileNotFoundException e3) {
            throw new IllegalArgumentException("The supplied POIFSFileSystem does not contain a BIFF8 'Workbook' entry. Is it really an excel file?");
        }
    }

    public HSSFWorkbook(DirectoryNode directory, POIFSFileSystem fs, boolean preserveNodes) throws IOException {
        this(directory, preserveNodes);
    }

    public HSSFWorkbook(DirectoryNode directory, boolean preserveNodes) throws IOException {
        super(directory);
        this.missingCellPolicy = MissingCellPolicy.RETURN_NULL_AND_BLANK;
        this._udfFinder = new IndexedUDFFinder(AggregatingUDFFinder.DEFAULT);
        String workbookName = getWorkbookDirEntryName(directory);
        this.preserveNodes = preserveNodes;
        if (!preserveNodes) {
            this.directory = null;
        }
        this._sheets = new ArrayList(INITIAL_CAPACITY);
        this.names = new ArrayList(INITIAL_CAPACITY);
        List<Record> records = RecordFactory.createRecords(directory.createDocumentInputStream(workbookName));
        this.workbook = InternalWorkbook.createWorkbook(records);
        setPropertiesFromWorkbook(this.workbook);
        int recOffset = this.workbook.getNumRecords();
        convertLabelRecords(records, recOffset);
        RecordStream rs = new RecordStream(records, recOffset);
        while (rs.hasNext()) {
            try {
                this._sheets.add(new HSSFSheet(this, InternalSheet.createSheet(rs)));
            } catch (UnsupportedBOFType eb) {
                log.log(5, "Unsupported BOF found of type " + eb.getType());
            }
        }
        for (int i = 0; i < this.workbook.getNumNames(); i++) {
            NameRecord nameRecord = this.workbook.getNameRecord(i);
            this.names.add(new HSSFName(this, nameRecord, this.workbook.getNameCommentRecord(nameRecord)));
        }
    }

    public HSSFWorkbook(InputStream s) throws IOException {
        this(s, true);
    }

    public HSSFWorkbook(InputStream s, boolean preserveNodes) throws IOException {
        this(new NPOIFSFileSystem(s).getRoot(), preserveNodes);
    }

    private void setPropertiesFromWorkbook(InternalWorkbook book) {
        this.workbook = book;
    }

    private void convertLabelRecords(List<Record> records, int offset) {
        if (log.check(1)) {
            log.log(1, "convertLabelRecords called");
        }
        for (int k = offset; k < records.size(); k++) {
            Record rec = (Record) records.get(k);
            if (rec.getSid() == (short) 516) {
                LabelRecord oldrec = (LabelRecord) rec;
                records.remove(k);
                LabelSSTRecord newrec = new LabelSSTRecord();
                int stringid = this.workbook.addSSTString(new UnicodeString(oldrec.getValue()));
                newrec.setRow(oldrec.getRow());
                newrec.setColumn(oldrec.getColumn());
                newrec.setXFIndex(oldrec.getXFIndex());
                newrec.setSSTIndex(stringid);
                records.add(k, newrec);
            }
        }
        if (log.check(1)) {
            log.log(1, "convertLabelRecords exit");
        }
    }

    public MissingCellPolicy getMissingCellPolicy() {
        return this.missingCellPolicy;
    }

    public void setMissingCellPolicy(MissingCellPolicy missingCellPolicy) {
        this.missingCellPolicy = missingCellPolicy;
    }

    public void setSheetOrder(String sheetname, int pos) {
        int oldSheetIndex = getSheetIndex(sheetname);
        this._sheets.add(pos, this._sheets.remove(oldSheetIndex));
        this.workbook.setSheetOrder(sheetname, pos);
        FormulaShifter shifter = FormulaShifter.createForSheetShift(oldSheetIndex, pos);
        for (HSSFSheet sheet : this._sheets) {
            sheet.getSheet().updateFormulasAfterCellShift(shifter, -1);
        }
        this.workbook.updateNamesAfterCellShift(shifter);
        int active = getActiveSheetIndex();
        if (active == oldSheetIndex) {
            setActiveSheet(pos);
        } else if (active < oldSheetIndex && active < pos) {
        } else {
            if (active > oldSheetIndex && active > pos) {
                return;
            }
            if (pos > oldSheetIndex) {
                setActiveSheet(active - 1);
            } else {
                setActiveSheet(active + 1);
            }
        }
    }

    private void validateSheetIndex(int index) {
        int lastSheetIx = this._sheets.size() - 1;
        if (index < 0 || index > lastSheetIx) {
            String range = "(0.." + lastSheetIx + ")";
            if (lastSheetIx == -1) {
                range = "(no sheets)";
            }
            throw new IllegalArgumentException("Sheet index (" + index + ") is out of range " + range);
        }
    }

    public void setSelectedTab(int index) {
        validateSheetIndex(index);
        int nSheets = this._sheets.size();
        int i = 0;
        while (i < nSheets) {
            getSheetAt(i).setSelected(i == index);
            i++;
        }
        this.workbook.getWindowOne().setNumSelectedTabs((short) 1);
    }

    public void setSelectedTabs(int[] indexes) {
        Collection list = new ArrayList(indexes.length);
        for (int index : indexes) {
            list.add(Integer.valueOf(index));
        }
        setSelectedTabs(list);
    }

    public void setSelectedTabs(Collection<Integer> indexes) {
        for (Integer intValue : indexes) {
            validateSheetIndex(intValue.intValue());
        }
        Set<Integer> set = new HashSet(indexes);
        int nSheets = this._sheets.size();
        for (int i = 0; i < nSheets; i++) {
            getSheetAt(i).setSelected(set.contains(Integer.valueOf(i)));
        }
        this.workbook.getWindowOne().setNumSelectedTabs((short) set.size());
    }

    public Collection<Integer> getSelectedTabs() {
        Collection<Integer> indexes = new ArrayList();
        int nSheets = this._sheets.size();
        for (int i = 0; i < nSheets; i++) {
            if (getSheetAt(i).isSelected()) {
                indexes.add(Integer.valueOf(i));
            }
        }
        return Collections.unmodifiableCollection(indexes);
    }

    public void setActiveSheet(int index) {
        validateSheetIndex(index);
        int nSheets = this._sheets.size();
        int i = 0;
        while (i < nSheets) {
            getSheetAt(i).setActive(i == index);
            i++;
        }
        this.workbook.getWindowOne().setActiveSheetIndex(index);
    }

    public int getActiveSheetIndex() {
        return this.workbook.getWindowOne().getActiveSheetIndex();
    }

    public void setFirstVisibleTab(int index) {
        this.workbook.getWindowOne().setFirstVisibleTab(index);
    }

    public int getFirstVisibleTab() {
        return this.workbook.getWindowOne().getFirstVisibleTab();
    }

    public void setSheetName(int sheetIx, String name) {
        if (name == null) {
            throw new IllegalArgumentException("sheetName must not be null");
        } else if (this.workbook.doesContainsSheetName(name, sheetIx)) {
            throw new IllegalArgumentException("The workbook already contains a sheet named '" + name + "'");
        } else {
            validateSheetIndex(sheetIx);
            this.workbook.setSheetName(sheetIx, name);
        }
    }

    public String getSheetName(int sheetIndex) {
        validateSheetIndex(sheetIndex);
        return this.workbook.getSheetName(sheetIndex);
    }

    public boolean isHidden() {
        return this.workbook.getWindowOne().getHidden();
    }

    public void setHidden(boolean hiddenFlag) {
        this.workbook.getWindowOne().setHidden(hiddenFlag);
    }

    public boolean isSheetHidden(int sheetIx) {
        validateSheetIndex(sheetIx);
        return this.workbook.isSheetHidden(sheetIx);
    }

    public boolean isSheetVeryHidden(int sheetIx) {
        validateSheetIndex(sheetIx);
        return this.workbook.isSheetVeryHidden(sheetIx);
    }

    public void setSheetHidden(int sheetIx, boolean hidden) {
        validateSheetIndex(sheetIx);
        this.workbook.setSheetHidden(sheetIx, hidden);
    }

    public void setSheetHidden(int sheetIx, int hidden) {
        validateSheetIndex(sheetIx);
        WorkbookUtil.validateSheetState(hidden);
        this.workbook.setSheetHidden(sheetIx, hidden);
    }

    public int getSheetIndex(String name) {
        return this.workbook.getSheetIndex(name);
    }

    public int getSheetIndex(Sheet sheet) {
        for (int i = 0; i < this._sheets.size(); i++) {
            if (this._sheets.get(i) == sheet) {
                return i;
            }
        }
        return -1;
    }

    public HSSFSheet createSheet() {
        boolean isOnlySheet = true;
        HSSFSheet sheet = new HSSFSheet(this);
        this._sheets.add(sheet);
        this.workbook.setSheetName(this._sheets.size() - 1, "Sheet" + (this._sheets.size() - 1));
        if (this._sheets.size() != 1) {
            isOnlySheet = false;
        }
        sheet.setSelected(isOnlySheet);
        sheet.setActive(isOnlySheet);
        return sheet;
    }

    public HSSFSheet cloneSheet(int sheetIndex) {
        validateSheetIndex(sheetIndex);
        HSSFSheet srcSheet = (HSSFSheet) this._sheets.get(sheetIndex);
        String srcName = this.workbook.getSheetName(sheetIndex);
        HSSFSheet clonedSheet = srcSheet.cloneSheet(this);
        clonedSheet.setSelected(false);
        clonedSheet.setActive(false);
        String name = getUniqueSheetName(srcName);
        int newSheetIndex = this._sheets.size();
        this._sheets.add(clonedSheet);
        this.workbook.setSheetName(newSheetIndex, name);
        int filterDbNameIndex = findExistingBuiltinNameRecordIdx(sheetIndex, (byte) 13);
        if (filterDbNameIndex != -1) {
            this.names.add(new HSSFName(this, this.workbook.cloneFilter(filterDbNameIndex, newSheetIndex)));
        }
        return clonedSheet;
    }

    private String getUniqueSheetName(String srcName) {
        int uniqueIndex = 2;
        String baseName = srcName;
        int bracketPos = srcName.lastIndexOf(40);
        if (bracketPos > 0 && srcName.endsWith(")")) {
            try {
                uniqueIndex = Integer.parseInt(srcName.substring(bracketPos + 1, srcName.length() - ")".length()).trim()) + 1;
                baseName = srcName.substring(0, bracketPos).trim();
            } catch (NumberFormatException e) {
            }
        }
        while (true) {
            String name;
            int uniqueIndex2 = uniqueIndex + 1;
            String index = Integer.toString(uniqueIndex);
            if ((baseName.length() + index.length()) + 2 < 31) {
                name = baseName + " (" + index + ")";
            } else {
                name = baseName.substring(0, (31 - index.length()) - 2) + "(" + index + ")";
            }
            if (this.workbook.getSheetIndex(name) == -1) {
                return name;
            }
            uniqueIndex = uniqueIndex2;
        }
    }

    public HSSFSheet createSheet(String sheetname) {
        boolean isOnlySheet = true;
        if (sheetname == null) {
            throw new IllegalArgumentException("sheetName must not be null");
        } else if (this.workbook.doesContainsSheetName(sheetname, this._sheets.size())) {
            throw new IllegalArgumentException("The workbook already contains a sheet named '" + sheetname + "'");
        } else {
            HSSFSheet sheet = new HSSFSheet(this);
            this.workbook.setSheetName(this._sheets.size(), sheetname);
            this._sheets.add(sheet);
            if (this._sheets.size() != 1) {
                isOnlySheet = false;
            }
            sheet.setSelected(isOnlySheet);
            sheet.setActive(isOnlySheet);
            return sheet;
        }
    }

    public Iterator<Sheet> sheetIterator() {
        return new SheetIterator(this);
    }

    public Iterator<Sheet> iterator() {
        return sheetIterator();
    }

    public int getNumberOfSheets() {
        return this._sheets.size();
    }

    private HSSFSheet[] getSheets() {
        HSSFSheet[] result = new HSSFSheet[this._sheets.size()];
        this._sheets.toArray(result);
        return result;
    }

    public HSSFSheet getSheetAt(int index) {
        validateSheetIndex(index);
        return (HSSFSheet) this._sheets.get(index);
    }

    public HSSFSheet getSheet(String name) {
        HSSFSheet retval = null;
        for (int k = 0; k < this._sheets.size(); k++) {
            if (this.workbook.getSheetName(k).equalsIgnoreCase(name)) {
                retval = (HSSFSheet) this._sheets.get(k);
            }
        }
        return retval;
    }

    public void removeSheetAt(int index) {
        validateSheetIndex(index);
        boolean wasSelected = getSheetAt(index).isSelected();
        this._sheets.remove(index);
        this.workbook.removeSheet(index);
        int nSheets = this._sheets.size();
        if (nSheets >= 1) {
            int newSheetIndex = index;
            if (newSheetIndex >= nSheets) {
                newSheetIndex = nSheets - 1;
            }
            if (wasSelected) {
                boolean someOtherSheetIsStillSelected = false;
                for (int i = 0; i < nSheets; i++) {
                    if (getSheetAt(i).isSelected()) {
                        someOtherSheetIsStillSelected = true;
                        break;
                    }
                }
                if (!someOtherSheetIsStillSelected) {
                    setSelectedTab(newSheetIndex);
                }
            }
            int active = getActiveSheetIndex();
            if (active == index) {
                setActiveSheet(newSheetIndex);
            } else if (active > index) {
                setActiveSheet(active - 1);
            }
        }
    }

    public void setBackupFlag(boolean backupValue) {
        this.workbook.getBackupRecord().setBackup(backupValue ? (short) 1 : (short) 0);
    }

    public boolean getBackupFlag() {
        return this.workbook.getBackupRecord().getBackup() != (short) 0;
    }

    int findExistingBuiltinNameRecordIdx(int sheetIndex, byte builtinCode) {
        int defNameIndex = 0;
        while (defNameIndex < this.names.size()) {
            NameRecord r = this.workbook.getNameRecord(defNameIndex);
            if (r == null) {
                throw new RuntimeException("Unable to find all defined names to iterate over");
            } else if (r.isBuiltInName() && r.getBuiltInName() == builtinCode && r.getSheetNumber() - 1 == sheetIndex) {
                return defNameIndex;
            } else {
                defNameIndex++;
            }
        }
        return -1;
    }

    HSSFName createBuiltInName(byte builtinCode, int sheetIndex) {
        HSSFName newName = new HSSFName(this, this.workbook.createBuiltInName(builtinCode, sheetIndex + 1), null);
        this.names.add(newName);
        return newName;
    }

    HSSFName getBuiltInName(byte builtinCode, int sheetIndex) {
        int index = findExistingBuiltinNameRecordIdx(sheetIndex, builtinCode);
        if (index < 0) {
            return null;
        }
        return (HSSFName) this.names.get(index);
    }

    public HSSFFont createFont() {
        this.workbook.createNewFont();
        short fontindex = (short) (getNumberOfFonts() - 1);
        if (fontindex > (short) 3) {
            fontindex = (short) (fontindex + 1);
        }
        if (fontindex != Font.COLOR_NORMAL) {
            return getFontAt(fontindex);
        }
        throw new IllegalArgumentException("Maximum number of fonts was exceeded");
    }

    @Deprecated
    public HSSFFont findFont(short boldWeight, short color, short fontHeight, String name, boolean italic, boolean strikeout, short typeOffset, byte underline) {
        short numberOfFonts = getNumberOfFonts();
        for (short i = (short) 0; i <= numberOfFonts; i = (short) (i + 1)) {
            if (i != (short) 4) {
                HSSFFont hssfFont = getFontAt(i);
                if (hssfFont.getBoldweight() == boldWeight && hssfFont.getColor() == color && hssfFont.getFontHeight() == fontHeight && hssfFont.getFontName().equals(name) && hssfFont.getItalic() == italic && hssfFont.getStrikeout() == strikeout && hssfFont.getTypeOffset() == typeOffset && hssfFont.getUnderline() == underline) {
                    return hssfFont;
                }
            }
        }
        return null;
    }

    public HSSFFont findFont(boolean bold, short color, short fontHeight, String name, boolean italic, boolean strikeout, short typeOffset, byte underline) {
        short numberOfFonts = getNumberOfFonts();
        for (short i = (short) 0; i <= numberOfFonts; i = (short) (i + 1)) {
            if (i != (short) 4) {
                HSSFFont hssfFont = getFontAt(i);
                if (hssfFont.getBold() == bold && hssfFont.getColor() == color && hssfFont.getFontHeight() == fontHeight && hssfFont.getFontName().equals(name) && hssfFont.getItalic() == italic && hssfFont.getStrikeout() == strikeout && hssfFont.getTypeOffset() == typeOffset && hssfFont.getUnderline() == underline) {
                    return hssfFont;
                }
            }
        }
        return null;
    }

    public short getNumberOfFonts() {
        return (short) this.workbook.getNumberOfFontRecords();
    }

    public HSSFFont getFontAt(short idx) {
        if (this.fonts == null) {
            this.fonts = new HashMap();
        }
        Short sIdx = Short.valueOf(idx);
        if (this.fonts.containsKey(sIdx)) {
            return (HSSFFont) this.fonts.get(sIdx);
        }
        HSSFFont retval = new HSSFFont(idx, this.workbook.getFontRecordAt(idx));
        this.fonts.put(sIdx, retval);
        return retval;
    }

    protected void resetFontCache() {
        this.fonts = new HashMap();
    }

    public HSSFCellStyle createCellStyle() {
        if (this.workbook.getNumExFormats() == MAX_STYLES) {
            throw new IllegalStateException("The maximum number of cell styles was exceeded. You can define up to 4000 styles in a .xls workbook");
        }
        return new HSSFCellStyle((short) (getNumCellStyles() - 1), this.workbook.createCellXF(), this);
    }

    public int getNumCellStyles() {
        return this.workbook.getNumExFormats();
    }

    public HSSFCellStyle getCellStyleAt(int idx) {
        return new HSSFCellStyle((short) idx, this.workbook.getExFormatAt(idx), this);
    }

    public void close() throws IOException {
        super.close();
    }

    public void write() throws IOException {
        validateInPlaceWritePossible();
        new NPOIFSDocument((DocumentNode) this.directory.getEntry(getWorkbookDirEntryName(this.directory))).replaceContents(new ByteArrayInputStream(getBytes()));
        writeProperties();
        this.directory.getFileSystem().writeFilesystem();
    }

    public void write(File newFile) throws IOException {
        NPOIFSFileSystem fs = POIFSFileSystem.create(newFile);
        try {
            write(fs);
            fs.writeFilesystem();
        } finally {
            fs.close();
        }
    }

    public void write(OutputStream stream) throws IOException {
        NPOIFSFileSystem fs = new NPOIFSFileSystem();
        try {
            write(fs);
            fs.writeFilesystem(stream);
        } finally {
            fs.close();
        }
    }

    private void write(NPOIFSFileSystem fs) throws IOException {
        List<String> excepts = new ArrayList(1);
        fs.createDocument(new ByteArrayInputStream(getBytes()), "Workbook");
        writeProperties(fs, excepts);
        if (this.preserveNodes) {
            excepts.addAll(Arrays.asList(InternalWorkbook.WORKBOOK_DIR_ENTRY_NAMES));
            EntryUtils.copyNodes(new FilteringDirectoryNode(this.directory, excepts), new FilteringDirectoryNode(fs.getRoot(), excepts));
            fs.getRoot().setStorageClsid(this.directory.getStorageClsid());
        }
    }

    public byte[] getBytes() {
        int k;
        if (log.check(1)) {
            log.log(1, "HSSFWorkbook.getBytes()");
        }
        HSSFSheet[] sheets = getSheets();
        int nSheets = sheets.length;
        this.workbook.preSerialize();
        for (HSSFSheet sheet : sheets) {
            sheet.getSheet().preSerialize();
            sheet.preSerialize();
        }
        int totalsize = this.workbook.getSize();
        SheetRecordCollector[] srCollectors = new SheetRecordCollector[nSheets];
        for (k = 0; k < nSheets; k++) {
            this.workbook.setSheetBof(k, totalsize);
            SheetRecordCollector src = new SheetRecordCollector();
            sheets[k].getSheet().visitContainedRecords(src, totalsize);
            totalsize += src.getTotalSize();
            srCollectors[k] = src;
        }
        byte[] retval = new byte[totalsize];
        int pos = this.workbook.serialize(0, retval);
        for (k = 0; k < nSheets; k++) {
            src = srCollectors[k];
            int serializedSize = src.serialize(pos, retval);
            if (serializedSize != src.getTotalSize()) {
                throw new IllegalStateException("Actual serialized sheet size (" + serializedSize + ") differs from pre-calculated size (" + src.getTotalSize() + ") for sheet (" + k + ")");
            }
            pos += serializedSize;
        }
        return retval;
    }

    InternalWorkbook getWorkbook() {
        return this.workbook;
    }

    public int getNumberOfNames() {
        return this.names.size();
    }

    public HSSFName getName(String name) {
        int nameIndex = getNameIndex(name);
        if (nameIndex < 0) {
            return null;
        }
        return (HSSFName) this.names.get(nameIndex);
    }

    public List<HSSFName> getNames(String name) {
        List<HSSFName> nameList = new ArrayList();
        Iterator i$ = this.names.iterator();
        while (i$.hasNext()) {
            HSSFName nr = (HSSFName) i$.next();
            if (nr.getNameName().equals(name)) {
                nameList.add(nr);
            }
        }
        return Collections.unmodifiableList(nameList);
    }

    public HSSFName getNameAt(int nameIndex) {
        int nNames = this.names.size();
        if (nNames < 1) {
            throw new IllegalStateException("There are no defined names in this workbook");
        } else if (nameIndex >= 0 && nameIndex <= nNames) {
            return (HSSFName) this.names.get(nameIndex);
        } else {
            throw new IllegalArgumentException("Specified name index " + nameIndex + " is outside the allowable range (0.." + (nNames - 1) + ").");
        }
    }

    public List<HSSFName> getAllNames() {
        return Collections.unmodifiableList(this.names);
    }

    public NameRecord getNameRecord(int nameIndex) {
        return getWorkbook().getNameRecord(nameIndex);
    }

    public String getNameName(int index) {
        return getNameAt(index).getNameName();
    }

    public void setPrintArea(int sheetIndex, String reference) {
        NameRecord name = this.workbook.getSpecificBuiltinRecord((byte) 6, sheetIndex + 1);
        if (name == null) {
            name = this.workbook.createBuiltInName((byte) 6, sheetIndex + 1);
        }
        String[] parts = COMMA_PATTERN.split(reference);
        StringBuffer sb = new StringBuffer(32);
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            SheetNameFormatter.appendFormat(sb, getSheetName(sheetIndex));
            sb.append("!");
            sb.append(parts[i]);
        }
        name.setNameDefinition(HSSFFormulaParser.parse(sb.toString(), this, FormulaType.NAMEDRANGE, sheetIndex));
    }

    public void setPrintArea(int sheetIndex, int startColumn, int endColumn, int startRow, int endRow) {
        setPrintArea(sheetIndex, new CellReference(startRow, startColumn, true, true).formatAsString() + ":" + new CellReference(endRow, endColumn, true, true).formatAsString());
    }

    public String getPrintArea(int sheetIndex) {
        NameRecord name = this.workbook.getSpecificBuiltinRecord((byte) 6, sheetIndex + 1);
        if (name == null) {
            return null;
        }
        return HSSFFormulaParser.toFormulaString(this, name.getNameDefinition());
    }

    public void removePrintArea(int sheetIndex) {
        getWorkbook().removeBuiltinRecord((byte) 6, sheetIndex + 1);
    }

    public HSSFName createName() {
        HSSFName newName = new HSSFName(this, this.workbook.createName());
        this.names.add(newName);
        return newName;
    }

    public int getNameIndex(String name) {
        for (int k = 0; k < this.names.size(); k++) {
            if (getNameName(k).equalsIgnoreCase(name)) {
                return k;
            }
        }
        return -1;
    }

    int getNameIndex(HSSFName name) {
        for (int k = 0; k < this.names.size(); k++) {
            if (name == this.names.get(k)) {
                return k;
            }
        }
        return -1;
    }

    public void removeName(int index) {
        this.names.remove(index);
        this.workbook.removeName(index);
    }

    public HSSFDataFormat createDataFormat() {
        if (this.formatter == null) {
            this.formatter = new HSSFDataFormat(this.workbook);
        }
        return this.formatter;
    }

    public void removeName(String name) {
        removeName(getNameIndex(name));
    }

    public void removeName(Name name) {
        removeName(getNameIndex((HSSFName) name));
    }

    public HSSFPalette getCustomPalette() {
        return new HSSFPalette(this.workbook.getCustomPalette());
    }

    public void insertChartRecord() {
        this.workbook.getRecords().add(this.workbook.findFirstRecordLocBySid((short) 252), new UnknownRecord(235, new byte[]{(byte) 15, (byte) 0, (byte) 0, (byte) -16, (byte) 82, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 6, (byte) -16, Ascii.CAN, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 8, (byte) 0, (byte) 0, (byte) 2, (byte) 0, (byte) 0, (byte) 0, (byte) 2, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 3, (byte) 0, (byte) 0, (byte) 0, (byte) 51, (byte) 0, (byte) 11, (byte) -16, (byte) 18, (byte) 0, (byte) 0, (byte) 0, (byte) -65, (byte) 0, (byte) 8, (byte) 0, (byte) 8, (byte) 0, (byte) -127, (byte) 1, (byte) 9, (byte) 0, (byte) 0, (byte) 8, (byte) -64, (byte) 1, (byte) 64, (byte) 0, (byte) 0, (byte) 8, (byte) 64, (byte) 0, (byte) 30, (byte) -15, (byte) 16, (byte) 0, (byte) 0, (byte) 0, (byte) 13, (byte) 0, (byte) 0, (byte) 8, (byte) 12, (byte) 0, (byte) 0, (byte) 8, (byte) 23, (byte) 0, (byte) 0, (byte) 8, (byte) -9, (byte) 0, (byte) 0, (byte) 16}));
    }

    public void dumpDrawingGroupRecords(boolean fat) {
        DrawingGroupRecord r = (DrawingGroupRecord) this.workbook.findFirstRecordBySid(DrawingGroupRecord.sid);
        r.decode();
        List<EscherRecord> escherRecords = r.getEscherRecords();
        PrintWriter w = new PrintWriter(new OutputStreamWriter(System.out, Charset.defaultCharset()));
        for (EscherRecord escherRecord : escherRecords) {
            if (fat) {
                System.out.println(escherRecord.toString());
            } else {
                escherRecord.display(w, 0);
            }
        }
        w.flush();
    }

    void initDrawings() {
        if (this.workbook.findDrawingGroup() != null) {
            for (HSSFSheet sh : this._sheets) {
                sh.getDrawingPatriarch();
            }
            return;
        }
        this.workbook.createDrawingGroup();
    }

    public int addPicture(byte[] pictureData, int format) {
        EscherBlipRecord blipRecord;
        int blipSize;
        short escherTag;
        initDrawings();
        byte[] uid = DigestUtils.md5(pictureData);
        switch (format) {
            case 2:
                break;
            case 3:
                if (LittleEndian.getInt(pictureData) == -1698247209) {
                    byte[] picDataNoHeader = new byte[(pictureData.length - 22)];
                    System.arraycopy(pictureData, 22, picDataNoHeader, 0, pictureData.length - 22);
                    pictureData = picDataNoHeader;
                    break;
                }
                break;
            default:
                EscherBlipRecord blipRecordBitmap = new EscherBitmapBlip();
                blipRecord = blipRecordBitmap;
                blipRecordBitmap.setUID(uid);
                blipRecordBitmap.setMarker((byte) -1);
                blipRecordBitmap.setPictureData(pictureData);
                blipSize = pictureData.length + 25;
                escherTag = (short) 255;
                break;
        }
        EscherBlipRecord blipRecordMeta = new EscherMetafileBlip();
        blipRecord = blipRecordMeta;
        blipRecordMeta.setUID(uid);
        blipRecordMeta.setPictureData(pictureData);
        blipRecordMeta.setFilter((byte) -2);
        blipSize = blipRecordMeta.getCompressedSize() + 58;
        escherTag = (short) 0;
        blipRecord.setRecordId((short) (format - 4072));
        switch (format) {
            case 2:
                blipRecord.setOptions(HSSFPictureData.MSOBI_EMF);
                break;
            case 3:
                blipRecord.setOptions(HSSFPictureData.MSOBI_WMF);
                break;
            case 4:
                blipRecord.setOptions(HSSFPictureData.MSOBI_PICT);
                break;
            case 5:
                blipRecord.setOptions(HSSFPictureData.MSOBI_JPEG);
                break;
            case 6:
                blipRecord.setOptions(HSSFPictureData.MSOBI_PNG);
                break;
            case 7:
                blipRecord.setOptions(HSSFPictureData.MSOBI_DIB);
                break;
            default:
                throw new IllegalStateException("Unexpected picture format: " + format);
        }
        EscherBSERecord r = new EscherBSERecord();
        r.setRecordId(EscherBSERecord.RECORD_ID);
        r.setOptions((short) ((format << 4) | 2));
        r.setBlipTypeMacOS((byte) format);
        r.setBlipTypeWin32((byte) format);
        r.setUid(uid);
        r.setTag(escherTag);
        r.setSize(blipSize);
        r.setRef(0);
        r.setOffset(0);
        r.setBlipRecord(blipRecord);
        return this.workbook.addBSERecord(r);
    }

    public List<HSSFPictureData> getAllPictures() {
        List<HSSFPictureData> pictures = new ArrayList();
        for (Record r : this.workbook.getRecords()) {
            if (r instanceof AbstractEscherHolderRecord) {
                ((AbstractEscherHolderRecord) r).decode();
                searchForPictures(((AbstractEscherHolderRecord) r).getEscherRecords(), pictures);
            }
        }
        return Collections.unmodifiableList(pictures);
    }

    private void searchForPictures(List<EscherRecord> escherRecords, List<HSSFPictureData> pictures) {
        for (EscherRecord escherRecord : escherRecords) {
            if (escherRecord instanceof EscherBSERecord) {
                EscherBlipRecord blip = ((EscherBSERecord) escherRecord).getBlipRecord();
                if (blip != null) {
                    pictures.add(new HSSFPictureData(blip));
                }
            }
            searchForPictures(escherRecord.getChildRecords(), pictures);
        }
    }

    protected static Map<String, ClassID> getOleMap() {
        Map<String, ClassID> olemap = new HashMap();
        olemap.put("PowerPoint Document", ClassID.PPT_SHOW);
        for (String str : InternalWorkbook.WORKBOOK_DIR_ENTRY_NAMES) {
            olemap.put(str, ClassID.XLS_WORKBOOK);
        }
        return olemap;
    }

    public int addOlePackage(POIFSFileSystem poiData, String label, String fileName, String command) throws IOException {
        DirectoryNode root = poiData.getRoot();
        for (Entry<String, ClassID> entry : getOleMap().entrySet()) {
            if (root.hasEntry((String) entry.getKey())) {
                root.setStorageClsid((ClassID) entry.getValue());
                break;
            }
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        poiData.writeFilesystem(bos);
        return addOlePackage(bos.toByteArray(), label, fileName, command);
    }

    public int addOlePackage(byte[] oleData, String label, String fileName, String command) throws IOException {
        if (this.directory == null) {
            this.directory = new NPOIFSFileSystem().getRoot();
            this.preserveNodes = true;
        }
        int storageId = 0;
        DirectoryEntry oleDir = null;
        do {
            storageId++;
            String storageStr = "MBD" + HexDump.toHex(storageId);
            if (!this.directory.hasEntry(storageStr)) {
                oleDir = this.directory.createDirectory(storageStr);
                oleDir.setStorageClsid(ClassID.OLE10_PACKAGE);
                continue;
            }
        } while (oleDir == null);
        oleDir.createDocument("\u0001Ole", new ByteArrayInputStream(new byte[]{(byte) 1, (byte) 0, (byte) 0, (byte) 2, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0}));
        Ole10Native oleNative = new Ole10Native(label, fileName, command, oleData);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        oleNative.writeOut(bos);
        oleDir.createDocument(Ole10Native.OLE10_NATIVE, new ByteArrayInputStream(bos.toByteArray()));
        return storageId;
    }

    public int linkExternalWorkbook(String name, Workbook workbook) {
        return this.workbook.linkExternalWorkbook(name, workbook);
    }

    public boolean isWriteProtected() {
        return this.workbook.isWriteProtected();
    }

    public void writeProtectWorkbook(String password, String username) {
        this.workbook.writeProtectWorkbook(password, username);
    }

    public void unwriteProtectWorkbook() {
        this.workbook.unwriteProtectWorkbook();
    }

    public List<HSSFObjectData> getAllEmbeddedObjects() {
        List objects = new ArrayList();
        for (HSSFSheet sheet : this._sheets) {
            getAllEmbeddedObjects(sheet, objects);
        }
        return Collections.unmodifiableList(objects);
    }

    private void getAllEmbeddedObjects(HSSFSheet sheet, List<HSSFObjectData> objects) {
        HSSFShapeContainer patriarch = sheet.getDrawingPatriarch();
        if (patriarch != null) {
            getAllEmbeddedObjects(patriarch, (List) objects);
        }
    }

    private void getAllEmbeddedObjects(HSSFShapeContainer parent, List<HSSFObjectData> objects) {
        for (HSSFShape shape : parent.getChildren()) {
            if (shape instanceof HSSFObjectData) {
                objects.add((HSSFObjectData) shape);
            } else if (shape instanceof HSSFShapeContainer) {
                getAllEmbeddedObjects((HSSFShapeContainer) shape, (List) objects);
            }
        }
    }

    public HSSFCreationHelper getCreationHelper() {
        return new HSSFCreationHelper(this);
    }

    UDFFinder getUDFFinder() {
        return this._udfFinder;
    }

    public void addToolPack(UDFFinder toopack) {
        this._udfFinder.add(toopack);
    }

    public void setForceFormulaRecalculation(boolean value) {
        getWorkbook().getRecalcId().setEngineId(0);
    }

    public boolean getForceFormulaRecalculation() {
        RecalcIdRecord recalc = (RecalcIdRecord) getWorkbook().findFirstRecordBySid((short) 449);
        return (recalc == null || recalc.getEngineId() == 0) ? false : true;
    }

    public boolean changeExternalReference(String oldUrl, String newUrl) {
        return this.workbook.changeExternalReference(oldUrl, newUrl);
    }

    public DirectoryNode getRootDirectory() {
        return this.directory;
    }

    @Internal
    public InternalWorkbook getInternalWorkbook() {
        return this.workbook;
    }

    public SpreadsheetVersion getSpreadsheetVersion() {
        return SpreadsheetVersion.EXCEL97;
    }
}

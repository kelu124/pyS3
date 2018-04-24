package org.apache.poi.hssf.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.record.CRNCountRecord;
import org.apache.poi.hssf.record.CRNRecord;
import org.apache.poi.hssf.record.ExternSheetRecord;
import org.apache.poi.hssf.record.ExternalNameRecord;
import org.apache.poi.hssf.record.NameCommentRecord;
import org.apache.poi.hssf.record.NameRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.SupBookRecord;
import org.apache.poi.ss.formula.SheetNameFormatter;
import org.apache.poi.ss.formula.ptg.ErrPtg;
import org.apache.poi.ss.formula.ptg.NameXPtg;
import org.apache.poi.ss.formula.ptg.Ptg;
import org.apache.poi.ss.usermodel.Workbook;

final class LinkTable {
    private final List<NameRecord> _definedNames;
    private final ExternSheetRecord _externSheetRecord;
    private ExternalBookBlock[] _externalBookBlocks;
    private final int _recordCount;
    private final WorkbookRecordList _workbookRecordList;

    private static final class CRNBlock {
        private final CRNCountRecord _countRecord;
        private final CRNRecord[] _crns;

        public CRNBlock(RecordStream rs) {
            this._countRecord = (CRNCountRecord) rs.getNext();
            CRNRecord[] crns = new CRNRecord[this._countRecord.getNumberOfCRNs()];
            for (int i = 0; i < crns.length; i++) {
                crns[i] = (CRNRecord) rs.getNext();
            }
            this._crns = crns;
        }

        public CRNRecord[] getCrns() {
            return (CRNRecord[]) this._crns.clone();
        }
    }

    private static final class ExternalBookBlock {
        private final CRNBlock[] _crnBlocks;
        private final SupBookRecord _externalBookRecord;
        private ExternalNameRecord[] _externalNameRecords;

        public ExternalBookBlock(RecordStream rs) {
            this._externalBookRecord = (SupBookRecord) rs.getNext();
            List<Object> temp = new ArrayList();
            while (rs.peekNextClass() == ExternalNameRecord.class) {
                temp.add(rs.getNext());
            }
            this._externalNameRecords = new ExternalNameRecord[temp.size()];
            temp.toArray(this._externalNameRecords);
            temp.clear();
            while (rs.peekNextClass() == CRNCountRecord.class) {
                temp.add(new CRNBlock(rs));
            }
            this._crnBlocks = new CRNBlock[temp.size()];
            temp.toArray(this._crnBlocks);
        }

        public ExternalBookBlock(String url, String[] sheetNames) {
            this._externalBookRecord = SupBookRecord.createExternalReferences(url, sheetNames);
            this._crnBlocks = new CRNBlock[0];
        }

        public ExternalBookBlock(int numberOfSheets) {
            this._externalBookRecord = SupBookRecord.createInternalReferences((short) numberOfSheets);
            this._externalNameRecords = new ExternalNameRecord[0];
            this._crnBlocks = new CRNBlock[0];
        }

        public ExternalBookBlock() {
            this._externalBookRecord = SupBookRecord.createAddInFunctions();
            this._externalNameRecords = new ExternalNameRecord[0];
            this._crnBlocks = new CRNBlock[0];
        }

        public SupBookRecord getExternalBookRecord() {
            return this._externalBookRecord;
        }

        public String getNameText(int definedNameIndex) {
            return this._externalNameRecords[definedNameIndex].getText();
        }

        public int getNameIx(int definedNameIndex) {
            return this._externalNameRecords[definedNameIndex].getIx();
        }

        public int getIndexOfName(String name) {
            for (int i = 0; i < this._externalNameRecords.length; i++) {
                if (this._externalNameRecords[i].getText().equalsIgnoreCase(name)) {
                    return i;
                }
            }
            return -1;
        }

        public int getNumberOfNames() {
            return this._externalNameRecords.length;
        }

        public int addExternalName(ExternalNameRecord rec) {
            ExternalNameRecord[] tmp = new ExternalNameRecord[(this._externalNameRecords.length + 1)];
            System.arraycopy(this._externalNameRecords, 0, tmp, 0, this._externalNameRecords.length);
            tmp[tmp.length - 1] = rec;
            this._externalNameRecords = tmp;
            return this._externalNameRecords.length - 1;
        }
    }

    public LinkTable(List<Record> inputList, int startIndex, WorkbookRecordList workbookRecordList, Map<String, NameCommentRecord> commentRecords) {
        this._workbookRecordList = workbookRecordList;
        RecordStream rs = new RecordStream(inputList, startIndex);
        List<ExternalBookBlock> temp = new ArrayList();
        while (rs.peekNextClass() == SupBookRecord.class) {
            temp.add(new ExternalBookBlock(rs));
        }
        this._externalBookBlocks = new ExternalBookBlock[temp.size()];
        temp.toArray(this._externalBookBlocks);
        temp.clear();
        if (this._externalBookBlocks.length <= 0) {
            this._externSheetRecord = null;
        } else if (rs.peekNextClass() != ExternSheetRecord.class) {
            this._externSheetRecord = null;
        } else {
            this._externSheetRecord = readExtSheetRecord(rs);
        }
        this._definedNames = new ArrayList();
        while (true) {
            Class<? extends Record> nextClass = rs.peekNextClass();
            if (nextClass == NameRecord.class) {
                this._definedNames.add((NameRecord) rs.getNext());
            } else if (nextClass == NameCommentRecord.class) {
                NameCommentRecord ncr = (NameCommentRecord) rs.getNext();
                commentRecords.put(ncr.getNameText(), ncr);
            } else {
                this._recordCount = rs.getCountRead();
                this._workbookRecordList.getRecords().addAll(inputList.subList(startIndex, this._recordCount + startIndex));
                return;
            }
        }
    }

    private static ExternSheetRecord readExtSheetRecord(RecordStream rs) {
        List<ExternSheetRecord> temp = new ArrayList(2);
        while (rs.peekNextClass() == ExternSheetRecord.class) {
            temp.add((ExternSheetRecord) rs.getNext());
        }
        int nItems = temp.size();
        if (nItems < 1) {
            throw new RuntimeException("Expected an EXTERNSHEET record but got (" + rs.peekNextClass().getName() + ")");
        } else if (nItems == 1) {
            return (ExternSheetRecord) temp.get(0);
        } else {
            ExternSheetRecord[] esrs = new ExternSheetRecord[nItems];
            temp.toArray(esrs);
            return ExternSheetRecord.combine(esrs);
        }
    }

    public LinkTable(int numberOfSheets, WorkbookRecordList workbookRecordList) {
        this._workbookRecordList = workbookRecordList;
        this._definedNames = new ArrayList();
        this._externalBookBlocks = new ExternalBookBlock[]{new ExternalBookBlock(numberOfSheets)};
        this._externSheetRecord = new ExternSheetRecord();
        this._recordCount = 2;
        SupBookRecord supbook = this._externalBookBlocks[0].getExternalBookRecord();
        int idx = findFirstRecordLocBySid((short) 140);
        if (idx < 0) {
            throw new RuntimeException("CountryRecord not found");
        }
        this._workbookRecordList.add(idx + 1, this._externSheetRecord);
        this._workbookRecordList.add(idx + 1, supbook);
    }

    public int getRecordCount() {
        return this._recordCount;
    }

    public NameRecord getSpecificBuiltinRecord(byte builtInCode, int sheetNumber) {
        for (NameRecord record : this._definedNames) {
            if (record.getBuiltInName() == builtInCode && record.getSheetNumber() == sheetNumber) {
                return record;
            }
        }
        return null;
    }

    public void removeBuiltinRecord(byte name, int sheetIndex) {
        NameRecord record = getSpecificBuiltinRecord(name, sheetIndex);
        if (record != null) {
            this._definedNames.remove(record);
        }
    }

    public int getNumNames() {
        return this._definedNames.size();
    }

    public NameRecord getNameRecord(int index) {
        return (NameRecord) this._definedNames.get(index);
    }

    public void addName(NameRecord name) {
        this._definedNames.add(name);
        int idx = findFirstRecordLocBySid((short) 23);
        if (idx == -1) {
            idx = findFirstRecordLocBySid(SupBookRecord.sid);
        }
        if (idx == -1) {
            idx = findFirstRecordLocBySid((short) 140);
        }
        this._workbookRecordList.add(idx + this._definedNames.size(), name);
    }

    public void removeName(int namenum) {
        this._definedNames.remove(namenum);
    }

    public boolean nameAlreadyExists(NameRecord name) {
        for (int i = getNumNames() - 1; i >= 0; i--) {
            NameRecord rec = getNameRecord(i);
            if (rec != name && isDuplicatedNames(name, rec)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isDuplicatedNames(NameRecord firstName, NameRecord lastName) {
        return lastName.getNameText().equalsIgnoreCase(firstName.getNameText()) && isSameSheetNames(firstName, lastName);
    }

    private static boolean isSameSheetNames(NameRecord firstName, NameRecord lastName) {
        return lastName.getSheetNumber() == firstName.getSheetNumber();
    }

    public String[] getExternalBookAndSheetName(int extRefIndex) {
        SupBookRecord ebr = this._externalBookBlocks[this._externSheetRecord.getExtbookIndexFromRefIndex(extRefIndex)].getExternalBookRecord();
        if (!ebr.isExternalReferences()) {
            return null;
        }
        int shIx1 = this._externSheetRecord.getFirstSheetIndexFromRefIndex(extRefIndex);
        int shIx2 = this._externSheetRecord.getLastSheetIndexFromRefIndex(extRefIndex);
        String firstSheetName = null;
        String lastSheetName = null;
        if (shIx1 >= 0) {
            firstSheetName = ebr.getSheetNames()[shIx1];
        }
        if (shIx2 >= 0) {
            lastSheetName = ebr.getSheetNames()[shIx2];
        }
        if (shIx1 == shIx2) {
            return new String[]{ebr.getURL(), firstSheetName};
        }
        return new String[]{ebr.getURL(), firstSheetName, lastSheetName};
    }

    private int getExternalWorkbookIndex(String workbookName) {
        for (int i = 0; i < this._externalBookBlocks.length; i++) {
            SupBookRecord ebr = this._externalBookBlocks[i].getExternalBookRecord();
            if (ebr.isExternalReferences() && workbookName.equals(ebr.getURL())) {
                return i;
            }
        }
        return -1;
    }

    public int linkExternalWorkbook(String name, Workbook externalWorkbook) {
        int extBookIndex = getExternalWorkbookIndex(name);
        if (extBookIndex != -1) {
            return extBookIndex;
        }
        int sn;
        String[] sheetNames = new String[externalWorkbook.getNumberOfSheets()];
        for (sn = 0; sn < sheetNames.length; sn++) {
            sheetNames[sn] = externalWorkbook.getSheetName(sn);
        }
        ExternalBookBlock block = new ExternalBookBlock("\u0000" + name, sheetNames);
        extBookIndex = extendExternalBookBlocks(block);
        int idx = findFirstRecordLocBySid((short) 23);
        if (idx == -1) {
            idx = this._workbookRecordList.size();
        }
        this._workbookRecordList.add(idx, block.getExternalBookRecord());
        for (sn = 0; sn < sheetNames.length; sn++) {
            this._externSheetRecord.addRef(extBookIndex, sn, sn);
        }
        return extBookIndex;
    }

    public int getExternalSheetIndex(String workbookName, String firstSheetName, String lastSheetName) {
        int externalBookIndex = getExternalWorkbookIndex(workbookName);
        if (externalBookIndex == -1) {
            throw new RuntimeException("No external workbook with name '" + workbookName + "'");
        }
        SupBookRecord ebrTarget = this._externalBookBlocks[externalBookIndex].getExternalBookRecord();
        int firstSheetIndex = getSheetIndex(ebrTarget.getSheetNames(), firstSheetName);
        int lastSheetIndex = getSheetIndex(ebrTarget.getSheetNames(), lastSheetName);
        int result = this._externSheetRecord.getRefIxForSheet(externalBookIndex, firstSheetIndex, lastSheetIndex);
        if (result < 0) {
            return this._externSheetRecord.addRef(externalBookIndex, firstSheetIndex, lastSheetIndex);
        }
        return result;
    }

    private static int getSheetIndex(String[] sheetNames, String sheetName) {
        for (int i = 0; i < sheetNames.length; i++) {
            if (sheetNames[i].equals(sheetName)) {
                return i;
            }
        }
        throw new RuntimeException("External workbook does not contain sheet '" + sheetName + "'");
    }

    public int getFirstInternalSheetIndexForExtIndex(int extRefIndex) {
        if (extRefIndex >= this._externSheetRecord.getNumOfRefs() || extRefIndex < 0) {
            return -1;
        }
        return this._externSheetRecord.getFirstSheetIndexFromRefIndex(extRefIndex);
    }

    public int getLastInternalSheetIndexForExtIndex(int extRefIndex) {
        if (extRefIndex >= this._externSheetRecord.getNumOfRefs() || extRefIndex < 0) {
            return -1;
        }
        return this._externSheetRecord.getLastSheetIndexFromRefIndex(extRefIndex);
    }

    public void removeSheet(int sheetIdx) {
        this._externSheetRecord.removeSheet(sheetIdx);
    }

    public int checkExternSheet(int sheetIndex) {
        return checkExternSheet(sheetIndex, sheetIndex);
    }

    public int checkExternSheet(int firstSheetIndex, int lastSheetIndex) {
        int i;
        int thisWbIndex = -1;
        for (i = 0; i < this._externalBookBlocks.length; i++) {
            if (this._externalBookBlocks[i].getExternalBookRecord().isInternalReferences()) {
                thisWbIndex = i;
                break;
            }
        }
        if (thisWbIndex < 0) {
            throw new RuntimeException("Could not find 'internal references' EXTERNALBOOK");
        }
        i = this._externSheetRecord.getRefIxForSheet(thisWbIndex, firstSheetIndex, lastSheetIndex);
        return i >= 0 ? i : this._externSheetRecord.addRef(thisWbIndex, firstSheetIndex, lastSheetIndex);
    }

    private int findFirstRecordLocBySid(short sid) {
        int index = 0;
        Iterator<Record> iterator = this._workbookRecordList.iterator();
        while (iterator.hasNext()) {
            if (((Record) iterator.next()).getSid() == sid) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public String resolveNameXText(int refIndex, int definedNameIndex, InternalWorkbook workbook) {
        int extBookIndex = this._externSheetRecord.getExtbookIndexFromRefIndex(refIndex);
        int firstTabIndex = this._externSheetRecord.getFirstSheetIndexFromRefIndex(refIndex);
        if (firstTabIndex == -1) {
            throw new RuntimeException("Referenced sheet could not be found");
        } else if (this._externalBookBlocks[extBookIndex]._externalNameRecords.length > definedNameIndex) {
            return this._externalBookBlocks[extBookIndex].getNameText(definedNameIndex);
        } else {
            if (firstTabIndex == -2) {
                NameRecord nr = getNameRecord(definedNameIndex);
                int sheetNumber = nr.getSheetNumber();
                StringBuffer text = new StringBuffer();
                if (sheetNumber > 0) {
                    SheetNameFormatter.appendFormat(text, workbook.getSheetName(sheetNumber - 1));
                    text.append("!");
                }
                text.append(nr.getNameText());
                return text.toString();
            }
            throw new ArrayIndexOutOfBoundsException("Ext Book Index relative but beyond the supported length, was " + extBookIndex + " but maximum is " + this._externalBookBlocks.length);
        }
    }

    public int resolveNameXIx(int refIndex, int definedNameIndex) {
        return this._externalBookBlocks[this._externSheetRecord.getExtbookIndexFromRefIndex(refIndex)].getNameIx(definedNameIndex);
    }

    public NameXPtg getNameXPtg(String name, int sheetRefIndex) {
        for (int i = 0; i < this._externalBookBlocks.length; i++) {
            int definedNameIndex = this._externalBookBlocks[i].getIndexOfName(name);
            if (definedNameIndex >= 0) {
                int thisSheetRefIndex = findRefIndexFromExtBookIndex(i);
                if (thisSheetRefIndex >= 0 && (sheetRefIndex == -1 || thisSheetRefIndex == sheetRefIndex)) {
                    return new NameXPtg(thisSheetRefIndex, definedNameIndex);
                }
            }
        }
        return null;
    }

    public NameXPtg addNameXPtg(String name) {
        int extBlockIndex = -1;
        ExternalBookBlock extBlock = null;
        for (int i = 0; i < this._externalBookBlocks.length; i++) {
            if (this._externalBookBlocks[i].getExternalBookRecord().isAddInFunctions()) {
                extBlock = this._externalBookBlocks[i];
                extBlockIndex = i;
                break;
            }
        }
        if (extBlock == null) {
            extBlock = new ExternalBookBlock();
            extBlockIndex = extendExternalBookBlocks(extBlock);
            this._workbookRecordList.add(findFirstRecordLocBySid((short) 23), extBlock.getExternalBookRecord());
            this._externSheetRecord.addRef(this._externalBookBlocks.length - 1, -2, -2);
        }
        ExternalNameRecord extNameRecord = new ExternalNameRecord();
        extNameRecord.setText(name);
        extNameRecord.setParsedExpression(new Ptg[]{ErrPtg.REF_INVALID});
        int nameIndex = extBlock.addExternalName(extNameRecord);
        int supLinkIndex = 0;
        Iterator<Record> iterator = this._workbookRecordList.iterator();
        while (iterator.hasNext()) {
            Record record = (Record) iterator.next();
            if ((record instanceof SupBookRecord) && ((SupBookRecord) record).isAddInFunctions()) {
                break;
            }
            supLinkIndex++;
        }
        this._workbookRecordList.add(supLinkIndex + extBlock.getNumberOfNames(), extNameRecord);
        return new NameXPtg(this._externSheetRecord.getRefIxForSheet(extBlockIndex, -2, -2), nameIndex);
    }

    private int extendExternalBookBlocks(ExternalBookBlock newBlock) {
        ExternalBookBlock[] tmp = new ExternalBookBlock[(this._externalBookBlocks.length + 1)];
        System.arraycopy(this._externalBookBlocks, 0, tmp, 0, this._externalBookBlocks.length);
        tmp[tmp.length - 1] = newBlock;
        this._externalBookBlocks = tmp;
        return this._externalBookBlocks.length - 1;
    }

    private int findRefIndexFromExtBookIndex(int extBookIndex) {
        return this._externSheetRecord.findRefIndexFromExtBookIndex(extBookIndex);
    }

    public boolean changeExternalReference(String oldUrl, String newUrl) {
        for (ExternalBookBlock ex : this._externalBookBlocks) {
            SupBookRecord externalRecord = ex.getExternalBookRecord();
            if (externalRecord.isExternalReferences() && externalRecord.getURL().equals(oldUrl)) {
                externalRecord.setURL(newUrl);
                return true;
            }
        }
        return false;
    }
}

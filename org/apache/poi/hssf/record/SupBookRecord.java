package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndianOutput;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;
import org.apache.poi.util.StringUtil;

public final class SupBookRecord extends StandardRecord {
    protected static final char CH_ALT_STARTUP_DIR = '\u0007';
    protected static final char CH_DOWN_DIR = '\u0003';
    protected static final char CH_LIB_DIR = '\b';
    protected static final char CH_LONG_VOLUME = '\u0005';
    protected static final char CH_SAME_VOLUME = '\u0002';
    protected static final char CH_STARTUP_DIR = '\u0006';
    protected static final char CH_UP_DIR = '\u0004';
    protected static final char CH_VOLUME = '\u0001';
    protected static final String PATH_SEPERATOR = System.getProperty("file.separator");
    private static final short SMALL_RECORD_SIZE = (short) 4;
    private static final short TAG_ADD_IN_FUNCTIONS = (short) 14849;
    private static final short TAG_INTERNAL_REFERENCES = (short) 1025;
    private static final POILogger logger = POILogFactory.getLogger(SupBookRecord.class);
    public static final short sid = (short) 430;
    private boolean _isAddInFunctions;
    private short field_1_number_of_sheets;
    private String field_2_encoded_url;
    private String[] field_3_sheet_names;

    public static SupBookRecord createInternalReferences(short numberOfSheets) {
        return new SupBookRecord(false, numberOfSheets);
    }

    public static SupBookRecord createAddInFunctions() {
        return new SupBookRecord(true, (short) 1);
    }

    public static SupBookRecord createExternalReferences(String url, String[] sheetNames) {
        return new SupBookRecord(url, sheetNames);
    }

    private SupBookRecord(boolean isAddInFuncs, short numberOfSheets) {
        this.field_1_number_of_sheets = numberOfSheets;
        this.field_2_encoded_url = null;
        this.field_3_sheet_names = null;
        this._isAddInFunctions = isAddInFuncs;
    }

    public SupBookRecord(String url, String[] sheetNames) {
        this.field_1_number_of_sheets = (short) sheetNames.length;
        this.field_2_encoded_url = url;
        this.field_3_sheet_names = sheetNames;
        this._isAddInFunctions = false;
    }

    public boolean isExternalReferences() {
        return this.field_3_sheet_names != null;
    }

    public boolean isInternalReferences() {
        return this.field_3_sheet_names == null && !this._isAddInFunctions;
    }

    public boolean isAddInFunctions() {
        return this.field_3_sheet_names == null && this._isAddInFunctions;
    }

    public SupBookRecord(RecordInputStream in) {
        int recLen = in.remaining();
        this.field_1_number_of_sheets = in.readShort();
        if (recLen > 4) {
            this._isAddInFunctions = false;
            this.field_2_encoded_url = in.readString();
            String[] sheetNames = new String[this.field_1_number_of_sheets];
            for (int i = 0; i < sheetNames.length; i++) {
                sheetNames[i] = in.readString();
            }
            this.field_3_sheet_names = sheetNames;
            return;
        }
        this.field_2_encoded_url = null;
        this.field_3_sheet_names = null;
        short nextShort = in.readShort();
        if (nextShort == TAG_INTERNAL_REFERENCES) {
            this._isAddInFunctions = false;
        } else if (nextShort == TAG_ADD_IN_FUNCTIONS) {
            this._isAddInFunctions = true;
            if (this.field_1_number_of_sheets != (short) 1) {
                throw new RuntimeException("Expected 0x0001 for number of sheets field in 'Add-In Functions' but got (" + this.field_1_number_of_sheets + ")");
            }
        } else {
            throw new RuntimeException("invalid EXTERNALBOOK code (" + Integer.toHexString(nextShort) + ")");
        }
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[SUPBOOK ");
        if (isExternalReferences()) {
            sb.append("External References]\n");
            sb.append(" .url     = ").append(this.field_2_encoded_url).append("\n");
            sb.append(" .nSheets = ").append(this.field_1_number_of_sheets).append("\n");
            for (String sheetname : this.field_3_sheet_names) {
                sb.append("    .name = ").append(sheetname).append("\n");
            }
            sb.append("[/SUPBOOK");
        } else if (this._isAddInFunctions) {
            sb.append("Add-In Functions");
        } else {
            sb.append("Internal References");
            sb.append(" nSheets=").append(this.field_1_number_of_sheets);
        }
        sb.append("]");
        return sb.toString();
    }

    protected int getDataSize() {
        if (!isExternalReferences()) {
            return 4;
        }
        int sum = 2 + StringUtil.getEncodedSize(this.field_2_encoded_url);
        for (String encodedSize : this.field_3_sheet_names) {
            sum += StringUtil.getEncodedSize(encodedSize);
        }
        return sum;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(this.field_1_number_of_sheets);
        if (isExternalReferences()) {
            StringUtil.writeUnicodeString(out, this.field_2_encoded_url);
            for (String writeUnicodeString : this.field_3_sheet_names) {
                StringUtil.writeUnicodeString(out, writeUnicodeString);
            }
            return;
        }
        out.writeShort(this._isAddInFunctions ? 14849 : 1025);
    }

    public void setNumberOfSheets(short number) {
        this.field_1_number_of_sheets = number;
    }

    public short getNumberOfSheets() {
        return this.field_1_number_of_sheets;
    }

    public short getSid() {
        return sid;
    }

    public String getURL() {
        String encodedUrl = this.field_2_encoded_url;
        switch (encodedUrl.charAt(0)) {
            case '\u0000':
                return encodedUrl.substring(1);
            case '\u0001':
                return decodeFileName(encodedUrl);
            case '\u0002':
                return encodedUrl.substring(1);
            default:
                return encodedUrl;
        }
    }

    private static String decodeFileName(String encodedUrl) {
        StringBuilder sb = new StringBuilder();
        int i = 1;
        while (i < encodedUrl.length()) {
            char c = encodedUrl.charAt(i);
            switch (c) {
                case '\u0001':
                    i++;
                    char driveLetter = encodedUrl.charAt(i);
                    if (driveLetter != '@') {
                        sb.append(driveLetter).append(":");
                        break;
                    }
                    sb.append("\\\\");
                    break;
                case '\u0002':
                    sb.append(PATH_SEPERATOR);
                    break;
                case '\u0003':
                    sb.append(PATH_SEPERATOR);
                    break;
                case '\u0004':
                    sb.append("..").append(PATH_SEPERATOR);
                    break;
                case '\u0005':
                    logger.log(5, new Object[]{"Found unexpected key: ChLongVolume - IGNORING"});
                    break;
                case '\u0006':
                case '\u0007':
                case '\b':
                    logger.log(5, new Object[]{"EXCEL.EXE path unkown - using this directoy instead: ."});
                    sb.append(".").append(PATH_SEPERATOR);
                    break;
                default:
                    sb.append(c);
                    break;
            }
            i++;
        }
        return sb.toString();
    }

    public String[] getSheetNames() {
        return (String[]) this.field_3_sheet_names.clone();
    }

    public void setURL(String pUrl) {
        this.field_2_encoded_url = this.field_2_encoded_url.substring(0, 1) + pUrl;
    }
}

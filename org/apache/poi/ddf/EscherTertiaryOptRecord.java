package org.apache.poi.ddf;

public class EscherTertiaryOptRecord extends AbstractEscherOptRecord {
    public static final short RECORD_ID = (short) -3806;

    public String getRecordName() {
        return "TertiaryOpt";
    }
}

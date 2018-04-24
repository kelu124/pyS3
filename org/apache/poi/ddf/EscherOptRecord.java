package org.apache.poi.ddf;

import org.apache.poi.util.Internal;

public class EscherOptRecord extends AbstractEscherOptRecord {
    public static final String RECORD_DESCRIPTION = "msofbtOPT";
    public static final short RECORD_ID = (short) -4085;

    public short getInstance() {
        setInstance((short) this.properties.size());
        return super.getInstance();
    }

    @Internal
    public short getOptions() {
        getInstance();
        getVersion();
        return super.getOptions();
    }

    public String getRecordName() {
        return "Opt";
    }

    public short getVersion() {
        setVersion((short) 3);
        return super.getVersion();
    }

    public void setVersion(short value) {
        if (value != (short) 3) {
            throw new IllegalArgumentException("msofbtOPT can have only '0x3' version");
        }
        super.setVersion(value);
    }
}

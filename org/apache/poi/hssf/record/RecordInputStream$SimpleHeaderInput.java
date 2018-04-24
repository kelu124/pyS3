package org.apache.poi.hssf.record;

import java.io.InputStream;
import org.apache.poi.util.LittleEndianInput;

final class RecordInputStream$SimpleHeaderInput implements BiffHeaderInput {
    private final LittleEndianInput _lei;

    public RecordInputStream$SimpleHeaderInput(InputStream in) {
        this._lei = RecordInputStream.getLEI(in);
    }

    public int available() {
        return this._lei.available();
    }

    public int readDataSize() {
        return this._lei.readUShort();
    }

    public int readRecordSID() {
        return this._lei.readUShort();
    }
}

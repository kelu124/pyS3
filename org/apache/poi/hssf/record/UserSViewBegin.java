package org.apache.poi.hssf.record;

import java.util.Locale;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;

public final class UserSViewBegin extends StandardRecord {
    public static final short sid = (short) 426;
    private byte[] _rawData;

    public UserSViewBegin(byte[] data) {
        this._rawData = data;
    }

    public UserSViewBegin(RecordInputStream in) {
        this._rawData = in.readRemainder();
    }

    public void serialize(LittleEndianOutput out) {
        out.write(this._rawData);
    }

    protected int getDataSize() {
        return this._rawData.length;
    }

    public short getSid() {
        return sid;
    }

    public byte[] getGuid() {
        byte[] guid = new byte[16];
        System.arraycopy(this._rawData, 0, guid, 0, guid.length);
        return guid;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[").append("USERSVIEWBEGIN").append("] (0x");
        sb.append(Integer.toHexString(426).toUpperCase(Locale.ROOT) + ")\n");
        sb.append("  rawData=").append(HexDump.toHex(this._rawData)).append("\n");
        sb.append("[/").append("USERSVIEWBEGIN").append("]\n");
        return sb.toString();
    }

    public Object clone() {
        return cloneViaReserialise();
    }
}

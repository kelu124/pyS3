package org.apache.poi.hpsf;

import com.lee.ultrascan.service.ProbeMessageID;
import org.apache.poi.util.Internal;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

@Internal
class TypedPropertyValue {
    private static final POILogger logger = POILogFactory.getLogger(TypedPropertyValue.class);
    private int _type;
    private Object _value;

    TypedPropertyValue() {
    }

    TypedPropertyValue(byte[] data, int startOffset) {
        read(data, startOffset);
    }

    TypedPropertyValue(int type, Object value) {
        this._type = type;
        this._value = value;
    }

    Object getValue() {
        return this._value;
    }

    int read(byte[] data, int startOffset) {
        int offset = startOffset;
        this._type = LittleEndian.getShort(data, offset);
        offset += 2;
        short padding = LittleEndian.getShort(data, offset);
        offset += 2;
        if (padding != (short) 0) {
            logger.log(5, "TypedPropertyValue padding at offset " + offset + " MUST be 0, but it's value is " + padding);
        }
        return (offset + readValue(data, offset)) - startOffset;
    }

    int readValue(byte[] data, int offset) {
        switch (this._type) {
            case 0:
            case 1:
                this._value = null;
                return 0;
            case 2:
            case 4:
                this._value = Short.valueOf(LittleEndian.getShort(data, offset));
                return 4;
            case 3:
            case 22:
                this._value = Integer.valueOf(LittleEndian.getInt(data, offset));
                return 4;
            case 5:
                this._value = Double.valueOf(LittleEndian.getDouble(data, offset));
                return 8;
            case 6:
                this._value = new Currency(data, offset);
                return 8;
            case 7:
                this._value = new Date(data, offset);
                return 8;
            case 8:
                this._value = new CodePageString(data, offset);
                return ((CodePageString) this._value).getSize();
            case 10:
            case 19:
            case 23:
                this._value = Long.valueOf(LittleEndian.getUInt(data, offset));
                return 4;
            case 11:
                this._value = new VariantBool(data, offset);
                return 2;
            case 14:
                this._value = new Decimal(data, offset);
                return 16;
            case 16:
                this._value = Byte.valueOf(data[offset]);
                return 1;
            case 17:
                this._value = Short.valueOf(LittleEndian.getUByte(data, offset));
                return 2;
            case 18:
                this._value = Integer.valueOf(LittleEndian.getUShort(data, offset));
                return 4;
            case 20:
                this._value = Long.valueOf(LittleEndian.getLong(data, offset));
                return 8;
            case 21:
                this._value = LittleEndian.getByteArray(data, offset, 8);
                return 8;
            case 30:
                this._value = new CodePageString(data, offset);
                return ((CodePageString) this._value).getSize();
            case 31:
                this._value = new UnicodeString(data, offset);
                return ((UnicodeString) this._value).getSize();
            case 64:
                this._value = new Filetime(data, offset);
                return 8;
            case 65:
                this._value = new Blob(data, offset);
                return ((Blob) this._value).getSize();
            case 66:
            case 67:
            case 68:
            case 69:
                this._value = new IndirectPropertyName(data, offset);
                return ((IndirectPropertyName) this._value).getSize();
            case 70:
                this._value = new Blob(data, offset);
                return ((Blob) this._value).getSize();
            case 71:
                this._value = new ClipboardData(data, offset);
                return ((ClipboardData) this._value).getSize();
            case 72:
                this._value = new GUID(data, offset);
                return 16;
            case 73:
                this._value = new VersionedStream(data, offset);
                return ((VersionedStream) this._value).getSize();
            case 4098:
            case 4099:
            case ProbeMessageID.ID_SET_FMOTOR_DOCK_NDIR_STEPS /*4100*/:
            case ProbeMessageID.ID_SET_FMOTOR_DOCK_NDIR_PHASE /*4101*/:
            case ProbeMessageID.ID_SET_FMOTOR_SCAN_PDIR_SPEED /*4102*/:
            case ProbeMessageID.ID_SET_FMOTOR_SCAN_PDIR_STEPS /*4103*/:
            case ProbeMessageID.ID_SET_FMOTOR_SCAN_NDIR_SPEED /*4104*/:
            case ProbeMessageID.ID_SET_FMOTOR_MOVE_PDIR_SPEED /*4106*/:
            case ProbeMessageID.ID_SET_FMOTOR_MOVE_NDIR_SPEED /*4107*/:
            case ProbeMessageID.ID_DOCK_FMOTOR /*4108*/:
            case 4112:
            case 4113:
            case 4114:
            case 4115:
            case 4116:
            case 4117:
            case 4126:
            case 4127:
            case 4160:
            case 4167:
            case 4168:
                this._value = new Vector((short) (this._type & 4095));
                return ((Vector) this._value).read(data, offset);
            case 8194:
            case ProbeMessageID.ID_SCAN_GET_FRAME_ACK /*8195*/:
            case 8196:
            case 8197:
            case 8198:
            case 8199:
            case 8200:
            case 8202:
            case 8203:
            case 8204:
            case 8206:
            case 8208:
            case 8209:
            case 8210:
            case 8211:
            case 8214:
            case 8215:
                this._value = new Array();
                return ((Array) this._value).read(data, offset);
            default:
                throw new UnsupportedOperationException("Unknown (possibly, incorrect) TypedPropertyValue type: " + this._type);
        }
    }

    int readValuePadded(byte[] data, int offset) {
        int nonPadded = readValue(data, offset);
        return (nonPadded & 3) == 0 ? nonPadded : nonPadded + (4 - (nonPadded & 3));
    }
}

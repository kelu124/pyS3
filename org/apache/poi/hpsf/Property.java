package org.apache.poi.hpsf;

import android.support.v4.view.MotionEventCompat;
import com.itextpdf.text.pdf.BaseField;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.poi.util.CodePageUtil;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.POILogFactory;

public class Property {
    protected long id;
    protected long type;
    protected Object value;

    public long getID() {
        return this.id;
    }

    public long getType() {
        return this.type;
    }

    public Object getValue() {
        return this.value;
    }

    public Property(long id, long type, Object value) {
        this.id = id;
        this.type = type;
        this.value = value;
    }

    public Property(long id, byte[] src, long offset, int length, int codepage) throws UnsupportedEncodingException {
        this.id = id;
        if (id == 0) {
            this.value = readDictionary(src, offset, length, codepage);
            return;
        }
        int o = (int) offset;
        this.type = LittleEndian.getUInt(src, o);
        try {
            this.value = VariantSupport.read(src, o + 4, length, (long) ((int) this.type), codepage);
        } catch (UnsupportedVariantTypeException ex) {
            VariantSupport.writeUnsupportedTypeMessage(ex);
            this.value = ex.getValue();
        }
    }

    protected Property() {
    }

    protected Map<?, ?> readDictionary(byte[] src, long offset, int length, int codepage) throws UnsupportedEncodingException {
        if (offset < 0 || offset > ((long) src.length)) {
            throw new HPSFRuntimeException("Illegal offset " + offset + " while HPSF stream contains " + length + " bytes.");
        }
        int o = (int) offset;
        long nrEntries = LittleEndian.getUInt(src, o);
        o += 4;
        Map<Object, Object> m = new LinkedHashMap((int) nrEntries, BaseField.BORDER_WIDTH_THIN);
        int i = 0;
        while (((long) i) < nrEntries) {
            try {
                Long id = Long.valueOf(LittleEndian.getUInt(src, o));
                o += 4;
                long sLength = LittleEndian.getUInt(src, o);
                o += 4;
                StringBuffer b = new StringBuffer();
                switch (codepage) {
                    case -1:
                        b.append(new String(src, o, (int) sLength, Charset.forName("ASCII")));
                        break;
                    case 1200:
                        int nrBytes = (int) (2 * sLength);
                        byte[] h = new byte[nrBytes];
                        for (int i2 = 0; i2 < nrBytes; i2 += 2) {
                            h[i2] = src[(o + i2) + 1];
                            h[i2 + 1] = src[o + i2];
                        }
                        b.append(new String(h, 0, nrBytes, CodePageUtil.codepageToEncoding(codepage)));
                        break;
                    default:
                        b.append(new String(src, o, (int) sLength, VariantSupport.codepageToEncoding(codepage)));
                        break;
                }
                while (b.length() > 0 && b.charAt(b.length() - 1) == '\u0000') {
                    b.setLength(b.length() - 1);
                }
                if (codepage == 1200) {
                    if (sLength % 2 == 1) {
                        sLength++;
                    }
                    o = (int) (((long) o) + (sLength + sLength));
                } else {
                    o = (int) (((long) o) + sLength);
                }
                m.put(id, b.toString());
                i++;
            } catch (RuntimeException ex) {
                POILogFactory.getLogger(getClass()).log(5, new Object[]{"The property set's dictionary contains bogus data. All dictionary entries starting with the one with ID " + this.id + " will be ignored.", ex});
            }
        }
        return m;
    }

    protected int getSize() throws WritingNotSupportedException {
        int length = Variant.getVariantLength(this.type);
        if (length >= 0) {
            return length;
        }
        if (length == -2) {
            throw new WritingNotSupportedException(this.type, null);
        }
        switch ((int) this.type) {
            case 0:
                break;
            case 30:
                int l = ((String) this.value).length() + 1;
                int r = l % 4;
                if (r > 0) {
                    l += 4 - r;
                }
                length += l;
                break;
            default:
                throw new WritingNotSupportedException(this.type, this.value);
        }
        return length;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Property)) {
            return false;
        }
        Property p = (Property) o;
        Object pValue = p.getValue();
        if (this.id != p.getID()) {
            return false;
        }
        if (this.id != 0 && !typesAreEqual(this.type, p.getType())) {
            return false;
        }
        if (this.value == null && pValue == null) {
            return true;
        }
        if (this.value == null || pValue == null) {
            return false;
        }
        Class<?> valueClass = this.value.getClass();
        Class<?> pValueClass = pValue.getClass();
        if (!valueClass.isAssignableFrom(pValueClass) && !pValueClass.isAssignableFrom(valueClass)) {
            return false;
        }
        if (this.value instanceof byte[]) {
            return Util.equal((byte[]) this.value, (byte[]) pValue);
        }
        return this.value.equals(pValue);
    }

    private boolean typesAreEqual(long t1, long t2) {
        if (t1 == t2 || ((t1 == 30 && t2 == 31) || (t2 == 30 && t1 == 31))) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        long hashCode = (0 + this.id) + this.type;
        if (this.value != null) {
            hashCode += (long) this.value.hashCode();
        }
        return (int) (4294967295L & hashCode);
    }

    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append(getClass().getName());
        b.append('[');
        b.append("id: ");
        b.append(getID());
        b.append(", type: ");
        b.append(getType());
        String value = getValue();
        b.append(", value: ");
        byte[] bytes;
        if (value instanceof String) {
            b.append(value.toString());
            String s = value;
            int l = s.length();
            bytes = new byte[(l * 2)];
            for (int i = 0; i < l; i++) {
                char c = s.charAt(i);
                byte low = (byte) ((c & 255) >> 0);
                bytes[i * 2] = (byte) ((MotionEventCompat.ACTION_POINTER_INDEX_MASK & c) >> 8);
                bytes[(i * 2) + 1] = low;
            }
            b.append(" [");
            if (bytes.length > 0) {
                b.append(HexDump.dump(bytes, 0, 0));
            }
            b.append("]");
        } else if (value instanceof byte[]) {
            bytes = (byte[]) value;
            if (bytes.length > 0) {
                b.append(HexDump.dump(bytes, 0, 0));
            }
        } else {
            b.append(value.toString());
        }
        b.append(']');
        return b.toString();
    }
}

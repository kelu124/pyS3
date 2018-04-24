package org.apache.poi.hpsf;

import android.support.v4.view.MotionEventCompat;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.apache.poi.util.CodePageUtil;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

public class VariantSupport extends Variant {
    public static final int[] SUPPORTED_TYPES = new int[]{0, 2, 3, 20, 5, 64, 30, 31, 71, 11};
    private static boolean logUnsupportedTypes = false;
    private static final POILogger logger = POILogFactory.getLogger(VariantSupport.class);
    protected static List<Long> unsupportedMessage;

    public static void setLogUnsupportedTypes(boolean logUnsupportedTypes) {
        logUnsupportedTypes = logUnsupportedTypes;
    }

    public static boolean isLogUnsupportedTypes() {
        return logUnsupportedTypes;
    }

    protected static void writeUnsupportedTypeMessage(UnsupportedVariantTypeException ex) {
        if (isLogUnsupportedTypes()) {
            if (unsupportedMessage == null) {
                unsupportedMessage = new LinkedList();
            }
            Long vt = Long.valueOf(ex.getVariantType());
            if (!unsupportedMessage.contains(vt)) {
                logger.log(7, new Object[]{ex.getMessage()});
                unsupportedMessage.add(vt);
            }
        }
    }

    public boolean isSupportedType(int variantType) {
        for (int i : SUPPORTED_TYPES) {
            if (variantType == i) {
                return true;
            }
        }
        return false;
    }

    public static Object read(byte[] src, int offset, int length, long type, int codepage) throws ReadingNotSupportedException, UnsupportedEncodingException {
        TypedPropertyValue typedPropertyValue = new TypedPropertyValue((int) type, null);
        byte[] v;
        try {
            int unpadded = typedPropertyValue.readValue(src, offset);
            switch ((int) type) {
                case 0:
                case 3:
                case 5:
                case 20:
                    return typedPropertyValue.getValue();
                case 2:
                    return Integer.valueOf(((Short) typedPropertyValue.getValue()).intValue());
                case 11:
                    return Boolean.valueOf(((VariantBool) typedPropertyValue.getValue()).getValue());
                case 30:
                    return ((CodePageString) typedPropertyValue.getValue()).getJavaValue(codepage);
                case 31:
                    return ((UnicodeString) typedPropertyValue.getValue()).toJavaString();
                case 64:
                    Filetime filetime = (Filetime) typedPropertyValue.getValue();
                    return Util.filetimeToDate((int) filetime.getHigh(), (int) filetime.getLow());
                case 71:
                    return ((ClipboardData) typedPropertyValue.getValue()).toByteArray();
                default:
                    v = new byte[unpadded];
                    System.arraycopy(src, offset, v, 0, unpadded);
                    throw new ReadingNotSupportedException(type, v);
            }
        } catch (UnsupportedOperationException e) {
            int propLength = Math.min(length, src.length - offset);
            v = new byte[propLength];
            System.arraycopy(src, offset, v, 0, propLength);
            throw new ReadingNotSupportedException(type, v);
        }
    }

    public static String codepageToEncoding(int codepage) throws UnsupportedEncodingException {
        return CodePageUtil.codepageToEncoding(codepage);
    }

    public static int write(OutputStream out, long type, Object value, int codepage) throws IOException, WritingNotSupportedException {
        int length;
        byte[] b;
        switch ((int) type) {
            case 0:
                length = 0 + TypeWriter.writeUIntToStream(out, 0);
                break;
            case 2:
                length = 0 + TypeWriter.writeToStream(out, ((Integer) value).shortValue());
                break;
            case 3:
                if (value instanceof Integer) {
                    length = 0 + TypeWriter.writeToStream(out, ((Integer) value).intValue());
                    break;
                }
                throw new ClassCastException("Could not cast an object to " + Integer.class.toString() + ": " + value.getClass().toString() + ", " + value.toString());
            case 5:
                length = 0 + TypeWriter.writeToStream(out, ((Double) value).doubleValue());
                break;
            case 11:
                if (((Boolean) value).booleanValue()) {
                    out.write(255);
                    out.write(255);
                } else {
                    out.write(0);
                    out.write(0);
                }
                length = 0 + 2;
                break;
            case 20:
                length = 0 + TypeWriter.writeToStream(out, ((Long) value).longValue());
                break;
            case 30:
                length = 0 + new CodePageString((String) value, codepage).write(out);
                break;
            case 31:
                length = 0 + TypeWriter.writeUIntToStream(out, (long) (((String) value).length() + 1));
                char[] s = ((String) value).toCharArray();
                for (int i = 0; i < s.length; i++) {
                    byte highb = (byte) ((s[i] & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8);
                    out.write((byte) (s[i] & 255));
                    out.write(highb);
                    length += 2;
                }
                out.write(0);
                out.write(0);
                length += 2;
                break;
            case 64:
                long filetime = Util.dateToFileTime((Date) value);
                length = 0 + new Filetime((int) (4294967295L & filetime), (int) ((filetime >> 32) & 4294967295L)).write(out);
                break;
            case 71:
                b = (byte[]) value;
                out.write(b);
                length = b.length;
                break;
            default:
                if (value instanceof byte[]) {
                    b = (byte[]) value;
                    out.write(b);
                    length = b.length;
                    writeUnsupportedTypeMessage(new WritingNotSupportedException(type, value));
                    break;
                }
                throw new WritingNotSupportedException(type, value);
        }
        while ((length & 3) != 0) {
            out.write(0);
            length++;
        }
        return length;
    }
}

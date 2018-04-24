package org.apache.poi.hpsf;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.poi.util.LittleEndian;

public class TypeWriter {
    public static int writeToStream(OutputStream out, short n) throws IOException {
        LittleEndian.putShort(out, n);
        return 2;
    }

    public static int writeToStream(OutputStream out, int n) throws IOException {
        LittleEndian.putInt(n, out);
        return 4;
    }

    public static int writeToStream(OutputStream out, long n) throws IOException {
        LittleEndian.putLong(n, out);
        return 8;
    }

    public static void writeUShortToStream(OutputStream out, int n) throws IOException {
        if ((n & -65536) != 0) {
            throw new IllegalPropertySetDataException("Value " + n + " cannot be represented by 2 bytes.");
        }
        LittleEndian.putUShort(n, out);
    }

    public static int writeUIntToStream(OutputStream out, long n) throws IOException {
        long high = n & -4294967296L;
        if (high == 0 || high == -4294967296L) {
            LittleEndian.putUInt(n, out);
            return 4;
        }
        throw new IllegalPropertySetDataException("Value " + n + " cannot be represented by 4 bytes.");
    }

    public static int writeToStream(OutputStream out, ClassID n) throws IOException {
        byte[] b = new byte[16];
        n.write(b, 0);
        out.write(b, 0, b.length);
        return b.length;
    }

    public static void writeToStream(OutputStream out, Property[] properties, int codepage) throws IOException, UnsupportedVariantTypeException {
        if (properties != null) {
            for (Property p : properties) {
                writeUIntToStream(out, p.getID());
                writeUIntToStream(out, (long) p.getSize());
            }
            for (Property p2 : properties) {
                long type = p2.getType();
                writeUIntToStream(out, type);
                VariantSupport.write(out, (long) ((int) type), p2.getValue(), codepage);
            }
        }
    }

    public static int writeToStream(OutputStream out, double n) throws IOException {
        LittleEndian.putDouble(n, out);
        return 8;
    }
}

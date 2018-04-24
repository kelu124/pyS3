package org.apache.poi.util;

import com.itextpdf.text.xml.xmp.XmpWriter;
import com.itextpdf.xmp.XMPConst;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.nio.charset.Charset;

@Internal
public class HexDump {
    public static final String EOL = System.getProperty("line.separator");
    public static final Charset UTF8 = Charset.forName(XmpWriter.UTF8);

    private HexDump() {
    }

    public static void dump(byte[] data, long offset, OutputStream stream, int index, int length) throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
        if (stream == null) {
            throw new IllegalArgumentException("cannot write to nullstream");
        }
        OutputStreamWriter osw = new OutputStreamWriter(stream, UTF8);
        osw.write(dump(data, offset, index, length));
        osw.flush();
    }

    public static synchronized void dump(byte[] data, long offset, OutputStream stream, int index) throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
        synchronized (HexDump.class) {
            dump(data, offset, stream, index, Integer.MAX_VALUE);
        }
    }

    public static String dump(byte[] data, long offset, int index) {
        return dump(data, offset, index, Integer.MAX_VALUE);
    }

    public static String dump(byte[] data, long offset, int index, int length) {
        if (data == null || data.length == 0) {
            return "No Data" + EOL;
        }
        int data_length = (length == Integer.MAX_VALUE || length < 0 || index + length < 0) ? data.length : Math.min(data.length, index + length);
        if (index < 0 || index >= data.length) {
            throw new ArrayIndexOutOfBoundsException("illegal index: " + index + " into array of length " + data.length);
        }
        long display_offset = offset + ((long) index);
        StringBuilder buffer = new StringBuilder(74);
        for (int j = index; j < data_length; j += 16) {
            int k;
            int chars_read = data_length - j;
            if (chars_read > 16) {
                chars_read = 16;
            }
            writeHex(buffer, display_offset, 8, "");
            for (k = 0; k < 16; k++) {
                if (k < chars_read) {
                    writeHex(buffer, (long) data[k + j], 2, " ");
                } else {
                    buffer.append("   ");
                }
            }
            buffer.append(' ');
            for (k = 0; k < chars_read; k++) {
                buffer.append(toAscii(data[k + j]));
            }
            buffer.append(EOL);
            display_offset += (long) chars_read;
        }
        return buffer.toString();
    }

    public static char toAscii(int dataB) {
        char charB = (char) (dataB & 255);
        if (Character.isISOControl(charB)) {
            return '.';
        }
        switch (charB) {
            case 'Ý':
            case 'ÿ':
                charB = '.';
                break;
        }
        return charB;
    }

    public static String toHex(byte[] value) {
        StringBuilder retVal = new StringBuilder();
        retVal.append('[');
        if (value != null && value.length > 0) {
            for (int x = 0; x < value.length; x++) {
                if (x > 0) {
                    retVal.append(", ");
                }
                retVal.append(toHex(value[x]));
            }
        }
        retVal.append(']');
        return retVal.toString();
    }

    public static String toHex(short[] value) {
        StringBuilder retVal = new StringBuilder();
        retVal.append('[');
        for (int x = 0; x < value.length; x++) {
            if (x > 0) {
                retVal.append(", ");
            }
            retVal.append(toHex(value[x]));
        }
        retVal.append(']');
        return retVal.toString();
    }

    public static String toHex(byte[] value, int bytesPerLine) {
        if (value.length == 0) {
            return ": 0";
        }
        int digits = (int) Math.round((Math.log((double) value.length) / Math.log(10.0d)) + 0.5d);
        StringBuilder retVal = new StringBuilder();
        writeHex(retVal, 0, digits, "");
        retVal.append(": ");
        int i = -1;
        for (int x = 0; x < value.length; x++) {
            i++;
            if (i == bytesPerLine) {
                retVal.append('\n');
                writeHex(retVal, (long) x, digits, "");
                retVal.append(": ");
                i = 0;
            } else if (x > 0) {
                retVal.append(", ");
            }
            retVal.append(toHex(value[x]));
        }
        return retVal.toString();
    }

    public static String toHex(short value) {
        StringBuilder sb = new StringBuilder(4);
        writeHex(sb, (long) (65535 & value), 4, "");
        return sb.toString();
    }

    public static String toHex(byte value) {
        StringBuilder sb = new StringBuilder(2);
        writeHex(sb, (long) (value & 255), 2, "");
        return sb.toString();
    }

    public static String toHex(int value) {
        StringBuilder sb = new StringBuilder(8);
        writeHex(sb, ((long) value) & 4294967295L, 8, "");
        return sb.toString();
    }

    public static String toHex(long value) {
        StringBuilder sb = new StringBuilder(16);
        writeHex(sb, value, 16, "");
        return sb.toString();
    }

    public static String toHex(String value) {
        return (value == null || value.length() == 0) ? XMPConst.ARRAY_ITEM_NAME : toHex(value.getBytes(LocaleUtil.CHARSET_1252));
    }

    public static void dump(InputStream in, PrintStream out, int start, int bytesToDump) throws IOException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int c;
        if (bytesToDump != -1) {
            int bytesRemaining = bytesToDump;
            while (true) {
                int bytesRemaining2 = bytesRemaining - 1;
                if (bytesRemaining <= 0) {
                    break;
                }
                c = in.read();
                if (c == -1) {
                    break;
                }
                buf.write(c);
                bytesRemaining = bytesRemaining2;
            }
        } else {
            c = in.read();
            while (c != -1) {
                buf.write(c);
                c = in.read();
            }
        }
        byte[] data = buf.toByteArray();
        dump(data, 0, out, start, data.length);
    }

    public static String longToHex(long value) {
        StringBuilder sb = new StringBuilder(18);
        writeHex(sb, value, 16, "0x");
        return sb.toString();
    }

    public static String intToHex(int value) {
        StringBuilder sb = new StringBuilder(10);
        writeHex(sb, ((long) value) & 4294967295L, 8, "0x");
        return sb.toString();
    }

    public static String shortToHex(int value) {
        StringBuilder sb = new StringBuilder(6);
        writeHex(sb, ((long) value) & 65535, 4, "0x");
        return sb.toString();
    }

    public static String byteToHex(int value) {
        StringBuilder sb = new StringBuilder(4);
        writeHex(sb, ((long) value) & 255, 2, "0x");
        return sb.toString();
    }

    private static void writeHex(StringBuilder sb, long value, int nDigits, String prefix) {
        sb.append(prefix);
        char[] buf = new char[nDigits];
        long acc = value;
        for (int i = nDigits - 1; i >= 0; i--) {
            int digit = (int) (15 & acc);
            buf[i] = (char) (digit < 10 ? digit + 48 : (digit + 65) - 10);
            acc >>>= 4;
        }
        sb.append(buf);
    }

    public static void main(String[] args) throws Exception {
        File file = new File(args[0]);
        InputStream in = new BufferedInputStream(new FileInputStream(file));
        byte[] b = new byte[((int) file.length())];
        in.read(b);
        System.out.println(dump(b, 0, 0));
        in.close();
    }
}

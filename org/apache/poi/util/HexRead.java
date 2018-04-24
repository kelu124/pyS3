package org.apache.poi.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class HexRead {
    public static byte[] readData(String filename) throws IOException {
        InputStream stream = new FileInputStream(new File(filename));
        try {
            byte[] readData = readData(stream, -1);
            return readData;
        } finally {
            stream.close();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] readData(java.io.InputStream r6, java.lang.String r7) throws java.io.IOException {
        /*
        r2 = new java.lang.StringBuffer;	 Catch:{ all -> 0x0061 }
        r2.<init>();	 Catch:{ all -> 0x0061 }
        r1 = 0;
        r0 = r6.read();	 Catch:{ all -> 0x0061 }
    L_0x000a:
        r3 = -1;
        if (r0 == r3) goto L_0x003f;
    L_0x000d:
        switch(r0) {
            case 10: goto L_0x001d;
            case 13: goto L_0x001d;
            case 91: goto L_0x001b;
            case 93: goto L_0x0024;
            default: goto L_0x0010;
        };	 Catch:{ all -> 0x0061 }
    L_0x0010:
        if (r1 == 0) goto L_0x0016;
    L_0x0012:
        r3 = (char) r0;	 Catch:{ all -> 0x0061 }
        r2.append(r3);	 Catch:{ all -> 0x0061 }
    L_0x0016:
        r0 = r6.read();	 Catch:{ all -> 0x0061 }
        goto L_0x000a;
    L_0x001b:
        r1 = 1;
        goto L_0x0016;
    L_0x001d:
        r1 = 0;
        r2 = new java.lang.StringBuffer;	 Catch:{ all -> 0x0061 }
        r2.<init>();	 Catch:{ all -> 0x0061 }
        goto L_0x0016;
    L_0x0024:
        r1 = 0;
        r3 = r2.toString();	 Catch:{ all -> 0x0061 }
        r3 = r3.equals(r7);	 Catch:{ all -> 0x0061 }
        if (r3 == 0) goto L_0x0039;
    L_0x002f:
        r3 = 91;
        r3 = readData(r6, r3);	 Catch:{ all -> 0x0061 }
        r6.close();
        return r3;
    L_0x0039:
        r2 = new java.lang.StringBuffer;	 Catch:{ all -> 0x0061 }
        r2.<init>();	 Catch:{ all -> 0x0061 }
        goto L_0x0016;
    L_0x003f:
        r6.close();
        r3 = new java.io.IOException;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "Section '";
        r4 = r4.append(r5);
        r4 = r4.append(r7);
        r5 = "' not found";
        r4 = r4.append(r5);
        r4 = r4.toString();
        r3.<init>(r4);
        throw r3;
    L_0x0061:
        r3 = move-exception;
        r6.close();
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.poi.util.HexRead.readData(java.io.InputStream, java.lang.String):byte[]");
    }

    public static byte[] readData(String filename, String section) throws IOException {
        return readData(new FileInputStream(new File(filename)), section);
    }

    public static byte[] readData(InputStream stream, int eofChar) throws IOException {
        Byte[] polished;
        byte[] rval;
        int characterCount = 0;
        byte b = (byte) 0;
        List<Byte> bytes = new ArrayList();
        while (true) {
            int count = stream.read();
            int digitValue = -1;
            if (48 <= count && count <= 57) {
                digitValue = count - 48;
            } else if (65 <= count && count <= 70) {
                digitValue = count - 55;
            } else if (97 <= count && count <= 102) {
                digitValue = count - 87;
            } else if (35 == count) {
                readToEOL(stream);
            } else if (-1 == count || eofChar == count) {
                polished = (Byte[]) bytes.toArray(new Byte[bytes.size()]);
                rval = new byte[polished.length];
            }
            if (digitValue != -1) {
                b = (byte) (((byte) digitValue) + ((byte) (b << 4)));
                characterCount++;
                if (characterCount == 2) {
                    bytes.add(Byte.valueOf(b));
                    characterCount = 0;
                    b = (byte) 0;
                }
            }
        }
        polished = (Byte[]) bytes.toArray(new Byte[bytes.size()]);
        rval = new byte[polished.length];
        for (int j = 0; j < polished.length; j++) {
            rval[j] = polished[j].byteValue();
        }
        return rval;
    }

    public static byte[] readFromString(String data) {
        try {
            return readData(new ByteArrayInputStream(data.getBytes(StringUtil.UTF8)), -1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void readToEOL(InputStream stream) throws IOException {
        int c = stream.read();
        while (c != -1 && c != 10 && c != 13) {
            c = stream.read();
        }
    }
}

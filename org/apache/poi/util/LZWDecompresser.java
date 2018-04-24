package org.apache.poi.util;

import android.support.v4.view.InputDeviceCompat;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class LZWDecompresser {
    private final int codeLengthIncrease;
    private final boolean maskMeansCompressed;
    private final boolean positionIsBigEndian;

    protected abstract int adjustDictionaryOffset(int i);

    protected abstract int populateDictionary(byte[] bArr);

    protected LZWDecompresser(boolean maskMeansCompressed, int codeLengthIncrease, boolean positionIsBigEndian) {
        this.maskMeansCompressed = maskMeansCompressed;
        this.codeLengthIncrease = codeLengthIncrease;
        this.positionIsBigEndian = positionIsBigEndian;
    }

    public byte[] decompress(InputStream src) throws IOException {
        ByteArrayOutputStream res = new ByteArrayOutputStream();
        decompress(src, res);
        return res.toByteArray();
    }

    public void decompress(InputStream src, OutputStream res) throws IOException {
        byte[] buffer = new byte[4096];
        int pos = populateDictionary(buffer);
        byte[] dataB = new byte[(this.codeLengthIncrease + 16)];
        while (true) {
            int flag = src.read();
            if (flag != -1) {
                for (int mask = 1; mask < 256; mask <<= 1) {
                    if ((this.maskMeansCompressed ^ ((flag & mask) > 0)) == 0) {
                        int dataIPt1 = src.read();
                        int dataIPt2 = src.read();
                        if (dataIPt1 == -1 || dataIPt2 == -1) {
                            break;
                        }
                        int pntr;
                        int len = (dataIPt2 & 15) + this.codeLengthIncrease;
                        if (this.positionIsBigEndian) {
                            pntr = (dataIPt1 << 4) + (dataIPt2 >> 4);
                        } else {
                            pntr = dataIPt1 + ((dataIPt2 & 240) << 4);
                        }
                        pntr = adjustDictionaryOffset(pntr);
                        for (int i = 0; i < len; i++) {
                            dataB[i] = buffer[(pntr + i) & 4095];
                            buffer[(pos + i) & 4095] = dataB[i];
                        }
                        res.write(dataB, 0, len);
                        pos += len;
                    } else {
                        int dataI = src.read();
                        if (dataI != -1) {
                            buffer[pos & 4095] = fromInt(dataI);
                            pos++;
                            res.write(new byte[]{fromInt(dataI)});
                        }
                    }
                }
            } else {
                return;
            }
        }
    }

    public static byte fromInt(int b) {
        if (b < 128) {
            return (byte) b;
        }
        return (byte) (b + InputDeviceCompat.SOURCE_ANY);
    }

    public static int fromByte(byte b) {
        return b >= (byte) 0 ? b : b + 256;
    }
}

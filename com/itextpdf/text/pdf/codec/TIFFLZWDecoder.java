package com.itextpdf.text.pdf.codec;

import com.itextpdf.text.error_messages.MessageLocalization;

public class TIFFLZWDecoder {
    int[] andTable = new int[]{511, IEEEDouble.EXPONENT_BIAS, 2047, 4095};
    int bitPointer;
    int bitsToGet = 9;
    int bytePointer;
    byte[] data = null;
    int dstIndex;
    int f151h;
    int nextBits = 0;
    int nextData = 0;
    int predictor;
    int samplesPerPixel;
    byte[][] stringTable;
    int tableIndex;
    byte[] uncompData;
    int f152w;

    public TIFFLZWDecoder(int w, int predictor, int samplesPerPixel) {
        this.f152w = w;
        this.predictor = predictor;
        this.samplesPerPixel = samplesPerPixel;
    }

    public byte[] decode(byte[] data, byte[] uncompData, int h) {
        if (data[0] == (byte) 0 && data[1] == (byte) 1) {
            throw new UnsupportedOperationException(MessageLocalization.getComposedMessage("tiff.5.0.style.lzw.codes.are.not.supported", new Object[0]));
        }
        initializeStringTable();
        this.data = data;
        this.f151h = h;
        this.uncompData = uncompData;
        this.bytePointer = 0;
        this.bitPointer = 0;
        this.dstIndex = 0;
        this.nextData = 0;
        this.nextBits = 0;
        int oldCode = 0;
        while (true) {
            int code = getNextCode();
            if (code == 257 || this.dstIndex >= uncompData.length) {
                break;
            } else if (code == 256) {
                initializeStringTable();
                code = getNextCode();
                if (code == 257) {
                    break;
                }
                writeString(this.stringTable[code]);
                oldCode = code;
            } else if (code < this.tableIndex) {
                string = this.stringTable[code];
                writeString(string);
                addStringToTable(this.stringTable[oldCode], string[0]);
                oldCode = code;
            } else {
                string = this.stringTable[oldCode];
                string = composeString(string, string[0]);
                writeString(string);
                addStringToTable(string);
                oldCode = code;
            }
        }
        if (this.predictor == 2) {
            for (int j = 0; j < h; j++) {
                int count = this.samplesPerPixel * ((this.f152w * j) + 1);
                for (int i = this.samplesPerPixel; i < this.f152w * this.samplesPerPixel; i++) {
                    uncompData[count] = (byte) (uncompData[count] + uncompData[count - this.samplesPerPixel]);
                    count++;
                }
            }
        }
        return uncompData;
    }

    public void initializeStringTable() {
        this.stringTable = new byte[4096][];
        for (int i = 0; i < 256; i++) {
            this.stringTable[i] = new byte[1];
            this.stringTable[i][0] = (byte) i;
        }
        this.tableIndex = 258;
        this.bitsToGet = 9;
    }

    public void writeString(byte[] string) {
        int max = this.uncompData.length - this.dstIndex;
        if (string.length < max) {
            max = string.length;
        }
        System.arraycopy(string, 0, this.uncompData, this.dstIndex, max);
        this.dstIndex += max;
    }

    public void addStringToTable(byte[] oldString, byte newString) {
        int length = oldString.length;
        byte[] string = new byte[(length + 1)];
        System.arraycopy(oldString, 0, string, 0, length);
        string[length] = newString;
        byte[][] bArr = this.stringTable;
        int i = this.tableIndex;
        this.tableIndex = i + 1;
        bArr[i] = string;
        if (this.tableIndex == 511) {
            this.bitsToGet = 10;
        } else if (this.tableIndex == IEEEDouble.EXPONENT_BIAS) {
            this.bitsToGet = 11;
        } else if (this.tableIndex == 2047) {
            this.bitsToGet = 12;
        }
    }

    public void addStringToTable(byte[] string) {
        byte[][] bArr = this.stringTable;
        int i = this.tableIndex;
        this.tableIndex = i + 1;
        bArr[i] = string;
        if (this.tableIndex == 511) {
            this.bitsToGet = 10;
        } else if (this.tableIndex == IEEEDouble.EXPONENT_BIAS) {
            this.bitsToGet = 11;
        } else if (this.tableIndex == 2047) {
            this.bitsToGet = 12;
        }
    }

    public byte[] composeString(byte[] oldString, byte newString) {
        int length = oldString.length;
        byte[] string = new byte[(length + 1)];
        System.arraycopy(oldString, 0, string, 0, length);
        string[length] = newString;
        return string;
    }

    public int getNextCode() {
        try {
            int i = this.nextData << 8;
            byte[] bArr = this.data;
            int i2 = this.bytePointer;
            this.bytePointer = i2 + 1;
            this.nextData = i | (bArr[i2] & 255);
            this.nextBits += 8;
            if (this.nextBits < this.bitsToGet) {
                i = this.nextData << 8;
                bArr = this.data;
                i2 = this.bytePointer;
                this.bytePointer = i2 + 1;
                this.nextData = i | (bArr[i2] & 255);
                this.nextBits += 8;
            }
            int code = (this.nextData >> (this.nextBits - this.bitsToGet)) & this.andTable[this.bitsToGet - 9];
            this.nextBits -= this.bitsToGet;
            return code;
        } catch (ArrayIndexOutOfBoundsException e) {
            return 257;
        }
    }
}

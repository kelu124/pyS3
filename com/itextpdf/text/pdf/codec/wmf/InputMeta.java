package com.itextpdf.text.pdf.codec.wmf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Utilities;
import java.io.IOException;
import java.io.InputStream;
import org.bytedeco.javacpp.avutil;

public class InputMeta {
    InputStream in;
    int length;

    public InputMeta(InputStream in) {
        this.in = in;
    }

    public int readWord() throws IOException {
        this.length += 2;
        int k1 = this.in.read();
        if (k1 < 0) {
            return 0;
        }
        return ((this.in.read() << 8) + k1) & 65535;
    }

    public int readShort() throws IOException {
        int k = readWord();
        if (k > avutil.FF_LAMBDA_MAX) {
            return k - 65536;
        }
        return k;
    }

    public int readInt() throws IOException {
        this.length += 4;
        int k1 = this.in.read();
        if (k1 < 0) {
            return 0;
        }
        return ((k1 + (this.in.read() << 8)) + (this.in.read() << 16)) + (this.in.read() << 24);
    }

    public int readByte() throws IOException {
        this.length++;
        return this.in.read() & 255;
    }

    public void skip(int len) throws IOException {
        this.length += len;
        Utilities.skip(this.in, len);
    }

    public int getLength() {
        return this.length;
    }

    public BaseColor readColor() throws IOException {
        int red = readByte();
        int green = readByte();
        int blue = readByte();
        readByte();
        return new BaseColor(red, green, blue);
    }
}

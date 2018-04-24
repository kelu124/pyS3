package com.itextpdf.text.pdf.codec;

import java.io.IOException;
import java.io.OutputStream;
import java.util.TreeMap;

public class TiffWriter {
    private TreeMap<Integer, FieldBase> ifd = new TreeMap();

    public static abstract class FieldBase {
        private int count;
        protected byte[] data;
        private int fieldType;
        private int offset;
        private int tag;

        protected FieldBase(int tag, int fieldType, int count) {
            this.tag = tag;
            this.fieldType = fieldType;
            this.count = count;
        }

        public int getValueSize() {
            return (this.data.length + 1) & -2;
        }

        public int getTag() {
            return this.tag;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }

        public void writeField(OutputStream stream) throws IOException {
            TiffWriter.writeShort(this.tag, stream);
            TiffWriter.writeShort(this.fieldType, stream);
            TiffWriter.writeLong(this.count, stream);
            if (this.data.length <= 4) {
                stream.write(this.data);
                for (int k = this.data.length; k < 4; k++) {
                    stream.write(0);
                }
                return;
            }
            TiffWriter.writeLong(this.offset, stream);
        }

        public void writeValue(OutputStream stream) throws IOException {
            if (this.data.length > 4) {
                stream.write(this.data);
                if ((this.data.length & 1) == 1) {
                    stream.write(0);
                }
            }
        }
    }

    public static class FieldAscii extends FieldBase {
        public FieldAscii(int tag, String values) {
            super(tag, 2, values.getBytes().length + 1);
            byte[] b = values.getBytes();
            this.data = new byte[(b.length + 1)];
            System.arraycopy(b, 0, this.data, 0, b.length);
        }
    }

    public static class FieldByte extends FieldBase {
        public FieldByte(int tag, byte[] values) {
            super(tag, 1, values.length);
            this.data = values;
        }
    }

    public static class FieldImage extends FieldBase {
        public FieldImage(byte[] values) {
            super(TIFFConstants.TIFFTAG_STRIPOFFSETS, 4, 1);
            this.data = values;
        }
    }

    public static class FieldLong extends FieldBase {
        public FieldLong(int tag, int value) {
            super(tag, 4, 1);
            this.data = new byte[4];
            this.data[0] = (byte) (value >> 24);
            this.data[1] = (byte) (value >> 16);
            this.data[2] = (byte) (value >> 8);
            this.data[3] = (byte) value;
        }

        public FieldLong(int tag, int[] values) {
            super(tag, 4, values.length);
            this.data = new byte[(values.length * 4)];
            int ptr = 0;
            for (int value : values) {
                int i = ptr + 1;
                this.data[ptr] = (byte) (value >> 24);
                ptr = i + 1;
                this.data[i] = (byte) (value >> 16);
                i = ptr + 1;
                this.data[ptr] = (byte) (value >> 8);
                ptr = i + 1;
                this.data[i] = (byte) value;
            }
        }
    }

    public static class FieldRational extends FieldBase {
        public FieldRational(int tag, int[] value) {
            this(tag, new int[][]{value});
        }

        public FieldRational(int tag, int[][] values) {
            super(tag, 5, values.length);
            this.data = new byte[(values.length * 8)];
            int ptr = 0;
            for (int[] value : values) {
                int i = ptr + 1;
                this.data[ptr] = (byte) (value[0] >> 24);
                ptr = i + 1;
                this.data[i] = (byte) (value[0] >> 16);
                i = ptr + 1;
                this.data[ptr] = (byte) (value[0] >> 8);
                ptr = i + 1;
                this.data[i] = (byte) value[0];
                i = ptr + 1;
                this.data[ptr] = (byte) (value[1] >> 24);
                ptr = i + 1;
                this.data[i] = (byte) (value[1] >> 16);
                i = ptr + 1;
                this.data[ptr] = (byte) (value[1] >> 8);
                ptr = i + 1;
                this.data[i] = (byte) value[1];
            }
        }
    }

    public static class FieldShort extends FieldBase {
        public FieldShort(int tag, int value) {
            super(tag, 3, 1);
            this.data = new byte[2];
            this.data[0] = (byte) (value >> 8);
            this.data[1] = (byte) value;
        }

        public FieldShort(int tag, int[] values) {
            super(tag, 3, values.length);
            this.data = new byte[(values.length * 2)];
            int ptr = 0;
            for (int value : values) {
                int i = ptr + 1;
                this.data[ptr] = (byte) (value >> 8);
                ptr = i + 1;
                this.data[i] = (byte) value;
            }
        }
    }

    public static class FieldUndefined extends FieldBase {
        public FieldUndefined(int tag, byte[] values) {
            super(tag, 7, values.length);
            this.data = values;
        }
    }

    public void addField(FieldBase field) {
        this.ifd.put(Integer.valueOf(field.getTag()), field);
    }

    public int getIfdSize() {
        return (this.ifd.size() * 12) + 6;
    }

    public void writeFile(OutputStream stream) throws IOException {
        stream.write(77);
        stream.write(77);
        stream.write(0);
        stream.write(42);
        writeLong(8, stream);
        writeShort(this.ifd.size(), stream);
        int offset = getIfdSize() + 8;
        for (FieldBase field : this.ifd.values()) {
            int size = field.getValueSize();
            if (size > 4) {
                field.setOffset(offset);
                offset += size;
            }
            field.writeField(stream);
        }
        writeLong(0, stream);
        for (FieldBase field2 : this.ifd.values()) {
            field2.writeValue(stream);
        }
    }

    public static void writeShort(int v, OutputStream stream) throws IOException {
        stream.write((v >> 8) & 255);
        stream.write(v & 255);
    }

    public static void writeLong(int v, OutputStream stream) throws IOException {
        stream.write((v >> 24) & 255);
        stream.write((v >> 16) & 255);
        stream.write((v >> 8) & 255);
        stream.write(v & 255);
    }

    public static void compressLZW(OutputStream stream, int predictor, byte[] b, int height, int samplesPerPixel, int stride) throws IOException {
        boolean usePredictor = true;
        LZWCompressor lzwCompressor = new LZWCompressor(stream, 8, true);
        if (predictor != 2) {
            usePredictor = false;
        }
        if (usePredictor) {
            int off = 0;
            byte[] rowBuf = usePredictor ? new byte[stride] : null;
            for (int i = 0; i < height; i++) {
                System.arraycopy(b, off, rowBuf, 0, stride);
                for (int j = stride - 1; j >= samplesPerPixel; j--) {
                    rowBuf[j] = (byte) (rowBuf[j] - rowBuf[j - samplesPerPixel]);
                }
                lzwCompressor.compress(rowBuf, 0, stride);
                off += stride;
            }
        } else {
            lzwCompressor.compress(b, 0, b.length);
        }
        lzwCompressor.flush();
    }
}

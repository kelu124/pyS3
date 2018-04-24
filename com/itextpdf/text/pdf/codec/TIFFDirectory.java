package com.itextpdf.text.pdf.codec;

import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import java.io.EOFException;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class TIFFDirectory implements Serializable {
    private static final long serialVersionUID = -168636766193675380L;
    private static final int[] sizeOfType = new int[]{0, 1, 1, 2, 4, 8, 1, 1, 2, 4, 8, 4, 8};
    long IFDOffset = 8;
    Hashtable<Integer, Integer> fieldIndex = new Hashtable();
    TIFFField[] fields;
    boolean isBigEndian;
    long nextIFDOffset = 0;
    int numEntries;

    TIFFDirectory() {
    }

    private static boolean isValidEndianTag(int endian) {
        return endian == 18761 || endian == 19789;
    }

    public TIFFDirectory(RandomAccessFileOrArray stream, int directory) throws IOException {
        long global_save_offset = stream.getFilePointer();
        stream.seek(0);
        int endian = stream.readUnsignedShort();
        if (isValidEndianTag(endian)) {
            this.isBigEndian = endian == 19789;
            if (readUnsignedShort(stream) != 42) {
                throw new IllegalArgumentException(MessageLocalization.getComposedMessage("bad.magic.number.should.be.42", new Object[0]));
            }
            long ifd_offset = readUnsignedInt(stream);
            for (int i = 0; i < directory; i++) {
                if (ifd_offset == 0) {
                    throw new IllegalArgumentException(MessageLocalization.getComposedMessage("directory.number.too.large", new Object[0]));
                }
                stream.seek(ifd_offset);
                stream.skip((long) (readUnsignedShort(stream) * 12));
                ifd_offset = readUnsignedInt(stream);
            }
            stream.seek(ifd_offset);
            initialize(stream);
            stream.seek(global_save_offset);
            return;
        }
        throw new IllegalArgumentException(MessageLocalization.getComposedMessage("bad.endianness.tag.not.0x4949.or.0x4d4d", new Object[0]));
    }

    public TIFFDirectory(RandomAccessFileOrArray stream, long ifd_offset, int directory) throws IOException {
        boolean z = false;
        long global_save_offset = stream.getFilePointer();
        stream.seek(0);
        int endian = stream.readUnsignedShort();
        if (isValidEndianTag(endian)) {
            if (endian == 19789) {
                z = true;
            }
            this.isBigEndian = z;
            stream.seek(ifd_offset);
            for (int dirNum = 0; dirNum < directory; dirNum++) {
                stream.seek(((long) (readUnsignedShort(stream) * 12)) + ifd_offset);
                ifd_offset = readUnsignedInt(stream);
                stream.seek(ifd_offset);
            }
            initialize(stream);
            stream.seek(global_save_offset);
            return;
        }
        throw new IllegalArgumentException(MessageLocalization.getComposedMessage("bad.endianness.tag.not.0x4949.or.0x4d4d", new Object[0]));
    }

    private void initialize(RandomAccessFileOrArray stream) throws IOException {
        long nextTagOffset = 0;
        long maxOffset = stream.length();
        this.IFDOffset = stream.getFilePointer();
        this.numEntries = readUnsignedShort(stream);
        this.fields = new TIFFField[this.numEntries];
        for (int i = 0; i < this.numEntries && nextTagOffset < maxOffset; i++) {
            int tag = readUnsignedShort(stream);
            int type = readUnsignedShort(stream);
            int count = (int) readUnsignedInt(stream);
            boolean processTag = true;
            nextTagOffset = stream.getFilePointer() + 4;
            try {
                if (sizeOfType[type] * count > 4) {
                    long valueOffset = readUnsignedInt(stream);
                    if (valueOffset < maxOffset) {
                        stream.seek(valueOffset);
                    } else {
                        processTag = false;
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                processTag = false;
            }
            if (processTag) {
                this.fieldIndex.put(Integer.valueOf(tag), Integer.valueOf(i));
                Object obj = null;
                int j;
                switch (type) {
                    case 1:
                    case 2:
                    case 6:
                    case 7:
                        Object bvalues = new byte[count];
                        stream.readFully(bvalues, 0, count);
                        if (type != 2) {
                            obj = bvalues;
                            break;
                        }
                        int index = 0;
                        int prevIndex = 0;
                        ArrayList<String> v = new ArrayList();
                        while (index < count) {
                            int index2 = index;
                            while (index2 < count) {
                                index = index2 + 1;
                                if (bvalues[index2] != (byte) 0) {
                                    index2 = index;
                                } else {
                                    v.add(new String(bvalues, prevIndex, index - prevIndex));
                                    prevIndex = index;
                                }
                            }
                            index = index2;
                            v.add(new String(bvalues, prevIndex, index - prevIndex));
                            prevIndex = index;
                        }
                        count = v.size();
                        Object strings = new String[count];
                        for (int c = 0; c < count; c++) {
                            strings[c] = (String) v.get(c);
                        }
                        obj = strings;
                        break;
                    case 3:
                        Object cvalues = new char[count];
                        for (j = 0; j < count; j++) {
                            cvalues[j] = (char) readUnsignedShort(stream);
                        }
                        obj = cvalues;
                        break;
                    case 4:
                        Object lvalues = new long[count];
                        for (j = 0; j < count; j++) {
                            lvalues[j] = readUnsignedInt(stream);
                        }
                        obj = lvalues;
                        break;
                    case 5:
                        Object llvalues = (long[][]) Array.newInstance(Long.TYPE, new int[]{count, 2});
                        for (j = 0; j < count; j++) {
                            llvalues[j][0] = readUnsignedInt(stream);
                            llvalues[j][1] = readUnsignedInt(stream);
                        }
                        obj = llvalues;
                        break;
                    case 8:
                        Object svalues = new short[count];
                        for (j = 0; j < count; j++) {
                            svalues[j] = readShort(stream);
                        }
                        obj = svalues;
                        break;
                    case 9:
                        Object ivalues = new int[count];
                        for (j = 0; j < count; j++) {
                            ivalues[j] = readInt(stream);
                        }
                        obj = ivalues;
                        break;
                    case 10:
                        Object iivalues = (int[][]) Array.newInstance(Integer.TYPE, new int[]{count, 2});
                        for (j = 0; j < count; j++) {
                            iivalues[j][0] = readInt(stream);
                            iivalues[j][1] = readInt(stream);
                        }
                        obj = iivalues;
                        break;
                    case 11:
                        Object fvalues = new float[count];
                        for (j = 0; j < count; j++) {
                            fvalues[j] = readFloat(stream);
                        }
                        obj = fvalues;
                        break;
                    case 12:
                        Object dvalues = new double[count];
                        for (j = 0; j < count; j++) {
                            dvalues[j] = readDouble(stream);
                        }
                        obj = dvalues;
                        break;
                }
                this.fields[i] = new TIFFField(tag, type, count, obj);
            }
            stream.seek(nextTagOffset);
        }
        try {
            this.nextIFDOffset = readUnsignedInt(stream);
        } catch (Exception e2) {
            this.nextIFDOffset = 0;
        }
    }

    public int getNumEntries() {
        return this.numEntries;
    }

    public TIFFField getField(int tag) {
        Integer i = (Integer) this.fieldIndex.get(Integer.valueOf(tag));
        if (i == null) {
            return null;
        }
        return this.fields[i.intValue()];
    }

    public boolean isTagPresent(int tag) {
        return this.fieldIndex.containsKey(Integer.valueOf(tag));
    }

    public int[] getTags() {
        int[] tags = new int[this.fieldIndex.size()];
        Enumeration<Integer> e = this.fieldIndex.keys();
        int i = 0;
        while (e.hasMoreElements()) {
            int i2 = i + 1;
            tags[i] = ((Integer) e.nextElement()).intValue();
            i = i2;
        }
        return tags;
    }

    public TIFFField[] getFields() {
        return this.fields;
    }

    public byte getFieldAsByte(int tag, int index) {
        return this.fields[((Integer) this.fieldIndex.get(Integer.valueOf(tag))).intValue()].getAsBytes()[index];
    }

    public byte getFieldAsByte(int tag) {
        return getFieldAsByte(tag, 0);
    }

    public long getFieldAsLong(int tag, int index) {
        return this.fields[((Integer) this.fieldIndex.get(Integer.valueOf(tag))).intValue()].getAsLong(index);
    }

    public long getFieldAsLong(int tag) {
        return getFieldAsLong(tag, 0);
    }

    public float getFieldAsFloat(int tag, int index) {
        return this.fields[((Integer) this.fieldIndex.get(Integer.valueOf(tag))).intValue()].getAsFloat(index);
    }

    public float getFieldAsFloat(int tag) {
        return getFieldAsFloat(tag, 0);
    }

    public double getFieldAsDouble(int tag, int index) {
        return this.fields[((Integer) this.fieldIndex.get(Integer.valueOf(tag))).intValue()].getAsDouble(index);
    }

    public double getFieldAsDouble(int tag) {
        return getFieldAsDouble(tag, 0);
    }

    private short readShort(RandomAccessFileOrArray stream) throws IOException {
        if (this.isBigEndian) {
            return stream.readShort();
        }
        return stream.readShortLE();
    }

    private int readUnsignedShort(RandomAccessFileOrArray stream) throws IOException {
        if (this.isBigEndian) {
            return stream.readUnsignedShort();
        }
        return stream.readUnsignedShortLE();
    }

    private int readInt(RandomAccessFileOrArray stream) throws IOException {
        if (this.isBigEndian) {
            return stream.readInt();
        }
        return stream.readIntLE();
    }

    private long readUnsignedInt(RandomAccessFileOrArray stream) throws IOException {
        if (this.isBigEndian) {
            return stream.readUnsignedInt();
        }
        return stream.readUnsignedIntLE();
    }

    private long readLong(RandomAccessFileOrArray stream) throws IOException {
        if (this.isBigEndian) {
            return stream.readLong();
        }
        return stream.readLongLE();
    }

    private float readFloat(RandomAccessFileOrArray stream) throws IOException {
        if (this.isBigEndian) {
            return stream.readFloat();
        }
        return stream.readFloatLE();
    }

    private double readDouble(RandomAccessFileOrArray stream) throws IOException {
        if (this.isBigEndian) {
            return stream.readDouble();
        }
        return stream.readDoubleLE();
    }

    private static int readUnsignedShort(RandomAccessFileOrArray stream, boolean isBigEndian) throws IOException {
        if (isBigEndian) {
            return stream.readUnsignedShort();
        }
        return stream.readUnsignedShortLE();
    }

    private static long readUnsignedInt(RandomAccessFileOrArray stream, boolean isBigEndian) throws IOException {
        if (isBigEndian) {
            return stream.readUnsignedInt();
        }
        return stream.readUnsignedIntLE();
    }

    public static int getNumDirectories(RandomAccessFileOrArray stream) throws IOException {
        long pointer = stream.getFilePointer();
        stream.seek(0);
        int endian = stream.readUnsignedShort();
        if (isValidEndianTag(endian)) {
            boolean isBigEndian;
            if (endian == 19789) {
                isBigEndian = true;
            } else {
                isBigEndian = false;
            }
            if (readUnsignedShort(stream, isBigEndian) != 42) {
                throw new IllegalArgumentException(MessageLocalization.getComposedMessage("bad.magic.number.should.be.42", new Object[0]));
            }
            stream.seek(4);
            long offset = readUnsignedInt(stream, isBigEndian);
            int numDirectories = 0;
            while (offset != 0) {
                numDirectories++;
                try {
                    stream.seek(offset);
                    stream.skip((long) (readUnsignedShort(stream, isBigEndian) * 12));
                    offset = readUnsignedInt(stream, isBigEndian);
                } catch (EOFException e) {
                    numDirectories--;
                }
            }
            stream.seek(pointer);
            return numDirectories;
        }
        throw new IllegalArgumentException(MessageLocalization.getComposedMessage("bad.endianness.tag.not.0x4949.or.0x4d4d", new Object[0]));
    }

    public boolean isBigEndian() {
        return this.isBigEndian;
    }

    public long getIFDOffset() {
        return this.IFDOffset;
    }

    public long getNextIFDOffset() {
        return this.nextIFDOffset;
    }
}

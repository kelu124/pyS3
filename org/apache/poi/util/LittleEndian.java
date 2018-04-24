package org.apache.poi.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LittleEndian implements LittleEndianConsts {

    public static final class BufferUnderrunException extends IOException {
        private static final long serialVersionUID = 8736973884877006145L;

        BufferUnderrunException() {
            super("buffer underrun");
        }
    }

    public static byte[] getByteArray(byte[] data, int offset, int size) {
        byte[] copy = new byte[size];
        System.arraycopy(data, offset, copy, 0, size);
        return copy;
    }

    public static double getDouble(byte[] data) {
        return Double.longBitsToDouble(getLong(data, 0));
    }

    public static double getDouble(byte[] data, int offset) {
        return Double.longBitsToDouble(getLong(data, offset));
    }

    public static float getFloat(byte[] data) {
        return getFloat(data, 0);
    }

    public static float getFloat(byte[] data, int offset) {
        return Float.intBitsToFloat(getInt(data, offset));
    }

    public static int getInt(byte[] data) {
        return getInt(data, 0);
    }

    public static int getInt(byte[] data, int offset) {
        int i = offset;
        int i2 = i + 1;
        int b0 = data[i] & 255;
        i = i2 + 1;
        int b1 = data[i2] & 255;
        i2 = i + 1;
        int b2 = data[i] & 255;
        i = i2 + 1;
        return ((((data[i2] & 255) << 24) + (b2 << 16)) + (b1 << 8)) + (b0 << 0);
    }

    public static long getLong(byte[] data) {
        return getLong(data, 0);
    }

    public static long getLong(byte[] data, int offset) {
        long result = (long) (data[offset + 7] & 255);
        for (int j = (offset + 8) - 1; j >= offset; j--) {
            result = (result << 8) | ((long) (data[j] & 255));
        }
        return result;
    }

    public static short getShort(byte[] data) {
        return getShort(data, 0);
    }

    public static short getShort(byte[] data, int offset) {
        return (short) (((data[offset + 1] & 255) << 8) + ((data[offset] & 255) << 0));
    }

    public static short[] getShortArray(byte[] data, int offset, int size) {
        short[] result = new short[(size / 2)];
        for (int i = 0; i < result.length; i++) {
            result[i] = getShort(data, (i * 2) + offset);
        }
        return result;
    }

    public static short getUByte(byte[] data) {
        return (short) (data[0] & 255);
    }

    public static short getUByte(byte[] data, int offset) {
        return (short) (data[offset] & 255);
    }

    public static long getUInt(byte[] data) {
        return getUInt(data, 0);
    }

    public static long getUInt(byte[] data, int offset) {
        return 4294967295L & ((long) getInt(data, offset));
    }

    public static int getUShort(byte[] data) {
        return getUShort(data, 0);
    }

    public static int getUShort(byte[] data, int offset) {
        return ((data[offset + 1] & 255) << 8) + ((data[offset] & 255) << 0);
    }

    public static void putByte(byte[] data, int offset, int value) {
        data[offset] = (byte) value;
    }

    public static void putDouble(byte[] data, int offset, double value) {
        putLong(data, offset, Double.doubleToLongBits(value));
    }

    public static void putDouble(double value, OutputStream outputStream) throws IOException {
        putLong(Double.doubleToLongBits(value), outputStream);
    }

    public static void putFloat(byte[] data, int offset, float value) {
        putInt(data, offset, Float.floatToIntBits(value));
    }

    public static void putFloat(float value, OutputStream outputStream) throws IOException {
        putInt(Float.floatToIntBits(value), outputStream);
    }

    public static void putInt(byte[] data, int offset, int value) {
        int i = offset;
        int i2 = i + 1;
        data[i] = (byte) ((value >>> 0) & 255);
        i = i2 + 1;
        data[i2] = (byte) ((value >>> 8) & 255);
        i2 = i + 1;
        data[i] = (byte) ((value >>> 16) & 255);
        i = i2 + 1;
        data[i2] = (byte) ((value >>> 24) & 255);
    }

    public static void putInt(int value, OutputStream outputStream) throws IOException {
        outputStream.write((byte) ((value >>> 0) & 255));
        outputStream.write((byte) ((value >>> 8) & 255));
        outputStream.write((byte) ((value >>> 16) & 255));
        outputStream.write((byte) ((value >>> 24) & 255));
    }

    public static void putLong(byte[] data, int offset, long value) {
        data[offset + 0] = (byte) ((int) ((value >>> null) & 255));
        data[offset + 1] = (byte) ((int) ((value >>> 8) & 255));
        data[offset + 2] = (byte) ((int) ((value >>> 16) & 255));
        data[offset + 3] = (byte) ((int) ((value >>> 24) & 255));
        data[offset + 4] = (byte) ((int) ((value >>> 32) & 255));
        data[offset + 5] = (byte) ((int) ((value >>> 40) & 255));
        data[offset + 6] = (byte) ((int) ((value >>> 48) & 255));
        data[offset + 7] = (byte) ((int) ((value >>> 56) & 255));
    }

    public static void putLong(long value, OutputStream outputStream) throws IOException {
        outputStream.write((byte) ((int) ((value >>> null) & 255)));
        outputStream.write((byte) ((int) ((value >>> 8) & 255)));
        outputStream.write((byte) ((int) ((value >>> 16) & 255)));
        outputStream.write((byte) ((int) ((value >>> 24) & 255)));
        outputStream.write((byte) ((int) ((value >>> 32) & 255)));
        outputStream.write((byte) ((int) ((value >>> 40) & 255)));
        outputStream.write((byte) ((int) ((value >>> 48) & 255)));
        outputStream.write((byte) ((int) ((value >>> 56) & 255)));
    }

    public static void putShort(byte[] data, int offset, short value) {
        int i = offset;
        int i2 = i + 1;
        data[i] = (byte) ((value >>> 0) & 255);
        i = i2 + 1;
        data[i2] = (byte) ((value >>> 8) & 255);
    }

    public static void putShort(OutputStream outputStream, short value) throws IOException {
        outputStream.write((byte) ((value >>> 0) & 255));
        outputStream.write((byte) ((value >>> 8) & 255));
    }

    public static void putShortArray(byte[] data, int startOffset, short[] value) {
        int offset = startOffset;
        for (short s : value) {
            putShort(data, offset, s);
            offset += 2;
        }
    }

    public static void putUByte(byte[] data, int offset, short value) {
        data[offset] = (byte) (value & 255);
    }

    public static void putUInt(byte[] data, int offset, long value) {
        int i = offset;
        int i2 = i + 1;
        data[i] = (byte) ((int) ((value >>> null) & 255));
        i = i2 + 1;
        data[i2] = (byte) ((int) ((value >>> 8) & 255));
        i2 = i + 1;
        data[i] = (byte) ((int) ((value >>> 16) & 255));
        i = i2 + 1;
        data[i2] = (byte) ((int) ((value >>> 24) & 255));
    }

    public static void putUInt(long value, OutputStream outputStream) throws IOException {
        outputStream.write((byte) ((int) ((value >>> null) & 255)));
        outputStream.write((byte) ((int) ((value >>> 8) & 255)));
        outputStream.write((byte) ((int) ((value >>> 16) & 255)));
        outputStream.write((byte) ((int) ((value >>> 24) & 255)));
    }

    public static void putUShort(byte[] data, int offset, int value) {
        int i = offset;
        int i2 = i + 1;
        data[i] = (byte) ((value >>> 0) & 255);
        i = i2 + 1;
        data[i2] = (byte) ((value >>> 8) & 255);
    }

    public static void putUShort(int value, OutputStream outputStream) throws IOException {
        outputStream.write((byte) ((value >>> 0) & 255));
        outputStream.write((byte) ((value >>> 8) & 255));
    }

    public static int readInt(InputStream stream) throws IOException, BufferUnderrunException {
        int ch1 = stream.read();
        int ch2 = stream.read();
        int ch3 = stream.read();
        int ch4 = stream.read();
        if ((((ch1 | ch2) | ch3) | ch4) >= 0) {
            return (((ch4 << 24) + (ch3 << 16)) + (ch2 << 8)) + (ch1 << 0);
        }
        throw new BufferUnderrunException();
    }

    public static long readUInt(InputStream stream) throws IOException, BufferUnderrunException {
        return 4294967295L & ((long) readInt(stream));
    }

    public static long readLong(InputStream stream) throws IOException, BufferUnderrunException {
        int ch1 = stream.read();
        int ch2 = stream.read();
        int ch3 = stream.read();
        int ch4 = stream.read();
        int ch5 = stream.read();
        int ch6 = stream.read();
        int ch7 = stream.read();
        int ch8 = stream.read();
        if ((((((((ch1 | ch2) | ch3) | ch4) | ch5) | ch6) | ch7) | ch8) >= 0) {
            return (((((((((long) ch8) << 56) + (((long) ch7) << 48)) + (((long) ch6) << 40)) + (((long) ch5) << 32)) + (((long) ch4) << 24)) + ((long) (ch3 << 16))) + ((long) (ch2 << 8))) + ((long) (ch1 << 0));
        }
        throw new BufferUnderrunException();
    }

    public static short readShort(InputStream stream) throws IOException, BufferUnderrunException {
        return (short) readUShort(stream);
    }

    public static int readUShort(InputStream stream) throws IOException, BufferUnderrunException {
        int ch1 = stream.read();
        int ch2 = stream.read();
        if ((ch1 | ch2) >= 0) {
            return (ch2 << 8) + (ch1 << 0);
        }
        throw new BufferUnderrunException();
    }

    public static int ubyteToInt(byte b) {
        return b & 255;
    }

    private LittleEndian() {
    }
}

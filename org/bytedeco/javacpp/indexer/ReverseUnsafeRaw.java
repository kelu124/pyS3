package org.bytedeco.javacpp.indexer;

class ReverseUnsafeRaw extends UnsafeRaw {
    ReverseUnsafeRaw() {
    }

    short getShort(long address) {
        return Short.reverseBytes(super.getShort(address));
    }

    void putShort(long address, short s) {
        super.putShort(address, Short.reverseBytes(s));
    }

    int getInt(long address) {
        return Integer.reverseBytes(super.getInt(address));
    }

    void putInt(long address, int i) {
        super.putInt(address, Integer.reverseBytes(i));
    }

    long getLong(long address) {
        return Long.reverseBytes(super.getLong(address));
    }

    void putLong(long address, long l) {
        super.putLong(address, Long.reverseBytes(l));
    }

    float getFloat(long address) {
        return Float.intBitsToFloat(Integer.reverseBytes(super.getInt(address)));
    }

    void putFloat(long address, float f) {
        super.putFloat(address, (float) Integer.reverseBytes(Float.floatToRawIntBits(f)));
    }

    double getDouble(long address) {
        return Double.longBitsToDouble(Long.reverseBytes(super.getLong(address)));
    }

    void putDouble(long address, double d) {
        super.putDouble(address, (double) Long.reverseBytes(Double.doubleToRawLongBits(d)));
    }

    char getChar(long address) {
        return Character.reverseBytes(super.getChar(address));
    }

    void putChar(long address, char c) {
        super.putChar(address, Character.reverseBytes(c));
    }
}

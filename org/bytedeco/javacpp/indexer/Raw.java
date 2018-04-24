package org.bytedeco.javacpp.indexer;

abstract class Raw {
    static final Raw INSTANCE;

    abstract byte getByte(long j);

    abstract char getChar(long j);

    abstract double getDouble(long j);

    abstract float getFloat(long j);

    abstract int getInt(long j);

    abstract long getLong(long j);

    abstract short getShort(long j);

    abstract void putByte(long j, byte b);

    abstract void putChar(long j, char c);

    abstract void putDouble(long j, double d);

    abstract void putFloat(long j, float f);

    abstract void putInt(long j, int i);

    abstract void putLong(long j, long j2);

    abstract void putShort(long j, short s);

    Raw() {
    }

    static {
        if (UnsafeRaw.isAvailable()) {
            INSTANCE = new UnsafeRaw();
        } else {
            INSTANCE = null;
        }
    }

    static Raw getInstance() {
        return INSTANCE;
    }
}

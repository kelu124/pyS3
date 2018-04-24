package org.bytedeco.javacpp.indexer;

import java.lang.reflect.Field;
import sun.misc.Unsafe;

class UnsafeRaw extends Raw {
    protected static final Unsafe UNSAFE;

    UnsafeRaw() {
    }

    static {
        Unsafe o;
        try {
            Class c = Class.forName("sun.misc.Unsafe");
            Field f = c.getDeclaredField("theUnsafe");
            c.getDeclaredMethod("getByte", new Class[]{Long.TYPE});
            c.getDeclaredMethod("getShort", new Class[]{Long.TYPE});
            c.getDeclaredMethod("getInt", new Class[]{Long.TYPE});
            c.getDeclaredMethod("getLong", new Class[]{Long.TYPE});
            c.getDeclaredMethod("getFloat", new Class[]{Long.TYPE});
            c.getDeclaredMethod("getDouble", new Class[]{Long.TYPE});
            c.getDeclaredMethod("getChar", new Class[]{Long.TYPE});
            f.setAccessible(true);
            o = (Unsafe) f.get(null);
        } catch (ClassNotFoundException e) {
            o = null;
            UNSAFE = o;
        } catch (IllegalArgumentException e2) {
            o = null;
            UNSAFE = o;
        } catch (IllegalAccessException e3) {
            o = null;
            UNSAFE = o;
        } catch (NoSuchFieldException e4) {
            o = null;
            UNSAFE = o;
        } catch (NoSuchMethodException e5) {
            o = null;
            UNSAFE = o;
        } catch (SecurityException e6) {
            o = null;
            UNSAFE = o;
        }
        UNSAFE = o;
    }

    static boolean isAvailable() {
        return UNSAFE != null;
    }

    byte getByte(long address) {
        return UNSAFE.getByte(address);
    }

    void putByte(long address, byte b) {
        UNSAFE.putByte(address, b);
    }

    short getShort(long address) {
        return UNSAFE.getShort(address);
    }

    void putShort(long address, short s) {
        UNSAFE.putShort(address, s);
    }

    int getInt(long address) {
        return UNSAFE.getInt(address);
    }

    void putInt(long address, int i) {
        UNSAFE.putInt(address, i);
    }

    long getLong(long address) {
        return UNSAFE.getLong(address);
    }

    void putLong(long address, long l) {
        UNSAFE.putLong(address, l);
    }

    float getFloat(long address) {
        return UNSAFE.getFloat(address);
    }

    void putFloat(long address, float f) {
        UNSAFE.putFloat(address, f);
    }

    double getDouble(long address) {
        return UNSAFE.getDouble(address);
    }

    void putDouble(long address, double d) {
        UNSAFE.putDouble(address, d);
    }

    char getChar(long address) {
        return UNSAFE.getChar(address);
    }

    void putChar(long address, char c) {
        UNSAFE.putChar(address, c);
    }
}

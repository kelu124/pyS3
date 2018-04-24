package com.google.common.hash;

import com.google.common.primitives.Longs;
import java.lang.reflect.Field;
import java.nio.ByteOrder;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import sun.misc.Unsafe;

final class LittleEndianByteArray {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final LittleEndianBytes byteArray;

    private interface LittleEndianBytes {
        long getLongLittleEndian(byte[] bArr, int i);

        void putLongLittleEndian(byte[] bArr, int i, long j);
    }

    private enum JavaLittleEndianBytes implements LittleEndianBytes {
        INSTANCE {
            public long getLongLittleEndian(byte[] source, int offset) {
                return Longs.fromBytes(source[offset + 7], source[offset + 6], source[offset + 5], source[offset + 4], source[offset + 3], source[offset + 2], source[offset + 1], source[offset]);
            }

            public void putLongLittleEndian(byte[] sink, int offset, long value) {
                long mask = 255;
                for (int i = 0; i < 8; i++) {
                    sink[offset + i] = (byte) ((int) ((value & mask) >> (i * 8)));
                    mask <<= 8;
                }
            }
        }
    }

    private enum UnsafeByteArray implements LittleEndianBytes {
        UNSAFE_LITTLE_ENDIAN {
            public long getLongLittleEndian(byte[] array, int offset) {
                return UnsafeByteArray.theUnsafe.getLong(array, ((long) offset) + ((long) UnsafeByteArray.BYTE_ARRAY_BASE_OFFSET));
            }

            public void putLongLittleEndian(byte[] array, int offset, long value) {
                UnsafeByteArray.theUnsafe.putLong(array, ((long) offset) + ((long) UnsafeByteArray.BYTE_ARRAY_BASE_OFFSET), value);
            }
        },
        UNSAFE_BIG_ENDIAN {
            public long getLongLittleEndian(byte[] array, int offset) {
                return Long.reverseBytes(UnsafeByteArray.theUnsafe.getLong(array, ((long) offset) + ((long) UnsafeByteArray.BYTE_ARRAY_BASE_OFFSET)));
            }

            public void putLongLittleEndian(byte[] array, int offset, long value) {
                Object obj = array;
                UnsafeByteArray.theUnsafe.putLong(obj, ((long) offset) + ((long) UnsafeByteArray.BYTE_ARRAY_BASE_OFFSET), Long.reverseBytes(value));
            }
        };
        
        private static final int BYTE_ARRAY_BASE_OFFSET = 0;
        private static final Unsafe theUnsafe = null;

        static class C07323 implements PrivilegedExceptionAction<Unsafe> {
            C07323() {
            }

            public Unsafe run() throws Exception {
                Class<Unsafe> k = Unsafe.class;
                for (Field f : k.getDeclaredFields()) {
                    f.setAccessible(true);
                    Object x = f.get(null);
                    if (k.isInstance(x)) {
                        return (Unsafe) k.cast(x);
                    }
                }
                throw new NoSuchFieldError("the Unsafe");
            }
        }

        static {
            theUnsafe = getUnsafe();
            BYTE_ARRAY_BASE_OFFSET = theUnsafe.arrayBaseOffset(byte[].class);
            if (theUnsafe.arrayIndexScale(byte[].class) != 1) {
                throw new AssertionError();
            }
        }

        private static Unsafe getUnsafe() {
            Unsafe unsafe;
            try {
                unsafe = Unsafe.getUnsafe();
            } catch (SecurityException e) {
                try {
                    unsafe = (Unsafe) AccessController.doPrivileged(new C07323());
                } catch (PrivilegedActionException e2) {
                    throw new RuntimeException("Could not initialize intrinsics", e2.getCause());
                }
            }
            return unsafe;
        }
    }

    static {
        boolean z;
        if (LittleEndianByteArray.class.desiredAssertionStatus()) {
            z = false;
        } else {
            z = true;
        }
        $assertionsDisabled = z;
        LittleEndianBytes theGetter = JavaLittleEndianBytes.INSTANCE;
        try {
            String arch = System.getProperty("os.arch");
            if ("amd64".equals(arch) || "aarch64".equals(arch)) {
                theGetter = ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN) ? UnsafeByteArray.UNSAFE_LITTLE_ENDIAN : UnsafeByteArray.UNSAFE_BIG_ENDIAN;
            }
        } catch (Throwable th) {
        }
        byteArray = theGetter;
    }

    static long load64(byte[] input, int offset) {
        if ($assertionsDisabled || input.length >= offset + 8) {
            return byteArray.getLongLittleEndian(input, offset);
        }
        throw new AssertionError();
    }

    static long load64Safely(byte[] input, int offset, int length) {
        long result = 0;
        for (int i = 0; i < Math.min(length, 8); i++) {
            result |= (((long) input[offset + i]) & 255) << (i * 8);
        }
        return result;
    }

    static void store64(byte[] sink, int offset, long value) {
        if ($assertionsDisabled || (offset >= 0 && offset + 8 <= sink.length)) {
            byteArray.putLongLittleEndian(sink, offset, value);
            return;
        }
        throw new AssertionError();
    }

    static int load32(byte[] source, int offset) {
        return (((source[offset] & 255) | ((source[offset + 1] & 255) << 8)) | ((source[offset + 2] & 255) << 16)) | ((source[offset + 3] & 255) << 24);
    }

    static boolean usingUnsafe() {
        return byteArray instanceof UnsafeByteArray;
    }

    private LittleEndianByteArray() {
    }
}

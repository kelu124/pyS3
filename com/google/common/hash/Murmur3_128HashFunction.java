package com.google.common.hash;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javax.annotation.Nullable;

final class Murmur3_128HashFunction extends AbstractStreamingHashFunction implements Serializable {
    private static final long serialVersionUID = 0;
    private final int seed;

    private static final class Murmur3_128Hasher extends AbstractStreamingHasher {
        private static final long C1 = -8663945395140668459L;
        private static final long C2 = 5545529020109919103L;
        private static final int CHUNK_SIZE = 16;
        private long h1;
        private long h2;
        private int length = 0;

        Murmur3_128Hasher(int seed) {
            super(16);
            this.h1 = (long) seed;
            this.h2 = (long) seed;
        }

        protected void process(ByteBuffer bb) {
            bmix64(bb.getLong(), bb.getLong());
            this.length += 16;
        }

        private void bmix64(long k1, long k2) {
            this.h1 ^= mixK1(k1);
            this.h1 = Long.rotateLeft(this.h1, 27);
            this.h1 += this.h2;
            this.h1 = (this.h1 * 5) + 1390208809;
            this.h2 ^= mixK2(k2);
            this.h2 = Long.rotateLeft(this.h2, 31);
            this.h2 += this.h1;
            this.h2 = (this.h2 * 5) + 944331445;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        protected void processRemaining(java.nio.ByteBuffer r13) {
            /*
            r12 = this;
            r11 = 40;
            r10 = 32;
            r9 = 24;
            r8 = 16;
            r7 = 8;
            r0 = 0;
            r2 = 0;
            r4 = r12.length;
            r5 = r13.remaining();
            r4 = r4 + r5;
            r12.length = r4;
            r4 = r13.remaining();
            switch(r4) {
                case 1: goto L_0x00e2;
                case 2: goto L_0x00d6;
                case 3: goto L_0x00ca;
                case 4: goto L_0x00be;
                case 5: goto L_0x00b2;
                case 6: goto L_0x00a6;
                case 7: goto L_0x0098;
                case 8: goto L_0x0080;
                case 9: goto L_0x0076;
                case 10: goto L_0x0069;
                case 11: goto L_0x005c;
                case 12: goto L_0x004f;
                case 13: goto L_0x0042;
                case 14: goto L_0x0035;
                case 15: goto L_0x0026;
                default: goto L_0x001e;
            };
        L_0x001e:
            r4 = new java.lang.AssertionError;
            r5 = "Should never get here.";
            r4.<init>(r5);
            throw r4;
        L_0x0026:
            r4 = 14;
            r4 = r13.get(r4);
            r4 = com.google.common.primitives.UnsignedBytes.toInt(r4);
            r4 = (long) r4;
            r6 = 48;
            r4 = r4 << r6;
            r2 = r2 ^ r4;
        L_0x0035:
            r4 = 13;
            r4 = r13.get(r4);
            r4 = com.google.common.primitives.UnsignedBytes.toInt(r4);
            r4 = (long) r4;
            r4 = r4 << r11;
            r2 = r2 ^ r4;
        L_0x0042:
            r4 = 12;
            r4 = r13.get(r4);
            r4 = com.google.common.primitives.UnsignedBytes.toInt(r4);
            r4 = (long) r4;
            r4 = r4 << r10;
            r2 = r2 ^ r4;
        L_0x004f:
            r4 = 11;
            r4 = r13.get(r4);
            r4 = com.google.common.primitives.UnsignedBytes.toInt(r4);
            r4 = (long) r4;
            r4 = r4 << r9;
            r2 = r2 ^ r4;
        L_0x005c:
            r4 = 10;
            r4 = r13.get(r4);
            r4 = com.google.common.primitives.UnsignedBytes.toInt(r4);
            r4 = (long) r4;
            r4 = r4 << r8;
            r2 = r2 ^ r4;
        L_0x0069:
            r4 = 9;
            r4 = r13.get(r4);
            r4 = com.google.common.primitives.UnsignedBytes.toInt(r4);
            r4 = (long) r4;
            r4 = r4 << r7;
            r2 = r2 ^ r4;
        L_0x0076:
            r4 = r13.get(r7);
            r4 = com.google.common.primitives.UnsignedBytes.toInt(r4);
            r4 = (long) r4;
            r2 = r2 ^ r4;
        L_0x0080:
            r4 = r13.getLong();
            r0 = r0 ^ r4;
        L_0x0085:
            r4 = r12.h1;
            r6 = mixK1(r0);
            r4 = r4 ^ r6;
            r12.h1 = r4;
            r4 = r12.h2;
            r6 = mixK2(r2);
            r4 = r4 ^ r6;
            r12.h2 = r4;
            return;
        L_0x0098:
            r4 = 6;
            r4 = r13.get(r4);
            r4 = com.google.common.primitives.UnsignedBytes.toInt(r4);
            r4 = (long) r4;
            r6 = 48;
            r4 = r4 << r6;
            r0 = r0 ^ r4;
        L_0x00a6:
            r4 = 5;
            r4 = r13.get(r4);
            r4 = com.google.common.primitives.UnsignedBytes.toInt(r4);
            r4 = (long) r4;
            r4 = r4 << r11;
            r0 = r0 ^ r4;
        L_0x00b2:
            r4 = 4;
            r4 = r13.get(r4);
            r4 = com.google.common.primitives.UnsignedBytes.toInt(r4);
            r4 = (long) r4;
            r4 = r4 << r10;
            r0 = r0 ^ r4;
        L_0x00be:
            r4 = 3;
            r4 = r13.get(r4);
            r4 = com.google.common.primitives.UnsignedBytes.toInt(r4);
            r4 = (long) r4;
            r4 = r4 << r9;
            r0 = r0 ^ r4;
        L_0x00ca:
            r4 = 2;
            r4 = r13.get(r4);
            r4 = com.google.common.primitives.UnsignedBytes.toInt(r4);
            r4 = (long) r4;
            r4 = r4 << r8;
            r0 = r0 ^ r4;
        L_0x00d6:
            r4 = 1;
            r4 = r13.get(r4);
            r4 = com.google.common.primitives.UnsignedBytes.toInt(r4);
            r4 = (long) r4;
            r4 = r4 << r7;
            r0 = r0 ^ r4;
        L_0x00e2:
            r4 = 0;
            r4 = r13.get(r4);
            r4 = com.google.common.primitives.UnsignedBytes.toInt(r4);
            r4 = (long) r4;
            r0 = r0 ^ r4;
            goto L_0x0085;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.common.hash.Murmur3_128HashFunction.Murmur3_128Hasher.processRemaining(java.nio.ByteBuffer):void");
        }

        public HashCode makeHash() {
            this.h1 ^= (long) this.length;
            this.h2 ^= (long) this.length;
            this.h1 += this.h2;
            this.h2 += this.h1;
            this.h1 = fmix64(this.h1);
            this.h2 = fmix64(this.h2);
            this.h1 += this.h2;
            this.h2 += this.h1;
            return HashCode.fromBytesNoCopy(ByteBuffer.wrap(new byte[16]).order(ByteOrder.LITTLE_ENDIAN).putLong(this.h1).putLong(this.h2).array());
        }

        private static long fmix64(long k) {
            k = (k ^ (k >>> 33)) * -49064778989728563L;
            k = (k ^ (k >>> 33)) * -4265267296055464877L;
            return k ^ (k >>> 33);
        }

        private static long mixK1(long k1) {
            return Long.rotateLeft(k1 * C1, 31) * C2;
        }

        private static long mixK2(long k2) {
            return Long.rotateLeft(k2 * C2, 33) * C1;
        }
    }

    Murmur3_128HashFunction(int seed) {
        this.seed = seed;
    }

    public int bits() {
        return 128;
    }

    public Hasher newHasher() {
        return new Murmur3_128Hasher(this.seed);
    }

    public String toString() {
        return "Hashing.murmur3_128(" + this.seed + ")";
    }

    public boolean equals(@Nullable Object object) {
        if (!(object instanceof Murmur3_128HashFunction)) {
            return false;
        }
        if (this.seed == ((Murmur3_128HashFunction) object).seed) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return getClass().hashCode() ^ this.seed;
    }
}

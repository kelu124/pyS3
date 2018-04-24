package com.google.common.hash;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;

final class FarmHashFingerprint64 extends AbstractNonStreamingHashFunction {
    private static final long K0 = -4348849565147123417L;
    private static final long K1 = -5435081209227447693L;
    private static final long K2 = -7286425919675154353L;

    FarmHashFingerprint64() {
    }

    public HashCode hashBytes(byte[] input, int off, int len) {
        Preconditions.checkPositionIndexes(off, off + len, input.length);
        return HashCode.fromLong(fingerprint(input, off, len));
    }

    public int bits() {
        return 64;
    }

    public String toString() {
        return "Hashing.farmHashFingerprint64()";
    }

    @VisibleForTesting
    static long fingerprint(byte[] bytes, int offset, int length) {
        if (length <= 32) {
            if (length <= 16) {
                return hashLength0to16(bytes, offset, length);
            }
            return hashLength17to32(bytes, offset, length);
        } else if (length <= 64) {
            return hashLength33To64(bytes, offset, length);
        } else {
            return hashLength65Plus(bytes, offset, length);
        }
    }

    private static long shiftMix(long val) {
        return (val >>> 47) ^ val;
    }

    private static long hashLength16(long u, long v, long mul) {
        long a = (u ^ v) * mul;
        long b = (v ^ (a ^ (a >>> 47))) * mul;
        return (b ^ (b >>> 47)) * mul;
    }

    private static void weakHashLength32WithSeeds(byte[] bytes, int offset, long seedA, long seedB, long[] output) {
        long part1 = LittleEndianByteArray.load64(bytes, offset);
        long part2 = LittleEndianByteArray.load64(bytes, offset + 8);
        long part3 = LittleEndianByteArray.load64(bytes, offset + 16);
        long part4 = LittleEndianByteArray.load64(bytes, offset + 24);
        seedA += part1;
        long c = seedA;
        seedA = (seedA + part2) + part3;
        seedB = Long.rotateRight((seedB + seedA) + part4, 21) + Long.rotateRight(seedA, 44);
        output[0] = seedA + part4;
        output[1] = seedB + c;
    }

    private static long hashLength0to16(byte[] bytes, int offset, int length) {
        if (length >= 8) {
            long mul = K2 + ((long) (length * 2));
            long a = LittleEndianByteArray.load64(bytes, offset) + K2;
            long b = LittleEndianByteArray.load64(bytes, (offset + length) - 8);
            return hashLength16((Long.rotateRight(b, 37) * mul) + a, (Long.rotateRight(a, 25) + b) * mul, mul);
        } else if (length >= 4) {
            return hashLength16(((long) length) + ((((long) LittleEndianByteArray.load32(bytes, offset)) & 4294967295L) << 3), ((long) LittleEndianByteArray.load32(bytes, (offset + length) - 4)) & 4294967295L, K2 + ((long) (length * 2)));
        } else if (length <= 0) {
            return K2;
        } else {
            byte a2 = bytes[offset];
            byte b2 = bytes[(length >> 1) + offset];
            return shiftMix((((long) ((a2 & 255) + ((b2 & 255) << 8))) * K2) ^ (((long) (length + ((bytes[(length - 1) + offset] & 255) << 2))) * K0)) * K2;
        }
    }

    private static long hashLength17to32(byte[] bytes, int offset, int length) {
        long mul = K2 + ((long) (length * 2));
        long a = LittleEndianByteArray.load64(bytes, offset) * K1;
        long b = LittleEndianByteArray.load64(bytes, offset + 8);
        long c = LittleEndianByteArray.load64(bytes, (offset + length) - 8) * mul;
        return hashLength16((Long.rotateRight(a + b, 43) + Long.rotateRight(c, 30)) + (LittleEndianByteArray.load64(bytes, (offset + length) - 16) * K2), (Long.rotateRight(K2 + b, 18) + a) + c, mul);
    }

    private static long hashLength33To64(byte[] bytes, int offset, int length) {
        long mul = K2 + ((long) (length * 2));
        long a = LittleEndianByteArray.load64(bytes, offset) * K2;
        long b = LittleEndianByteArray.load64(bytes, offset + 8);
        long c = LittleEndianByteArray.load64(bytes, (offset + length) - 8) * mul;
        long y = (Long.rotateRight(a + b, 43) + Long.rotateRight(c, 30)) + (LittleEndianByteArray.load64(bytes, (offset + length) - 16) * K2);
        long z = hashLength16(y, (Long.rotateRight(K2 + b, 18) + a) + c, mul);
        long e = LittleEndianByteArray.load64(bytes, offset + 16) * mul;
        long f = LittleEndianByteArray.load64(bytes, offset + 24);
        long g = (LittleEndianByteArray.load64(bytes, (offset + length) - 32) + y) * mul;
        return hashLength16((Long.rotateRight(e + f, 43) + Long.rotateRight(g, 30)) + ((LittleEndianByteArray.load64(bytes, (offset + length) - 24) + z) * mul), (Long.rotateRight(f + a, 18) + e) + g, mul);
    }

    private static long hashLength65Plus(byte[] bytes, int offset, int length) {
        byte[] bArr;
        long y = 2480279821605975764L;
        long z = shiftMix((K2 * 2480279821605975764L) + 113) * K2;
        long[] v = new long[2];
        long[] w = new long[2];
        long x = (K2 * 81) + LittleEndianByteArray.load64(bytes, offset);
        int end = offset + (((length - 1) / 64) * 64);
        int last64offset = (((length - 1) & 63) + end) - 63;
        do {
            x = Long.rotateRight(((x + y) + v[0]) + LittleEndianByteArray.load64(bytes, offset + 8), 37) * K1;
            x ^= w[1];
            y = (Long.rotateRight((v[1] + y) + LittleEndianByteArray.load64(bytes, offset + 48), 42) * K1) + (v[0] + LittleEndianByteArray.load64(bytes, offset + 40));
            z = Long.rotateRight(w[0] + z, 33) * K1;
            weakHashLength32WithSeeds(bytes, offset, K1 * v[1], x + w[0], v);
            bArr = bytes;
            weakHashLength32WithSeeds(bArr, offset + 32, z + w[1], y + LittleEndianByteArray.load64(bytes, offset + 16), w);
            long tmp = x;
            x = z;
            z = tmp;
            offset += 64;
        } while (offset != end);
        long mul = K1 + ((255 & z) << 1);
        offset = last64offset;
        w[0] = w[0] + ((long) ((length - 1) & 63));
        v[0] = v[0] + w[0];
        w[0] = w[0] + v[0];
        x = Long.rotateRight(((x + y) + v[0]) + LittleEndianByteArray.load64(bytes, offset + 8), 37) * mul;
        x ^= w[1] * 9;
        y = (Long.rotateRight((v[1] + y) + LittleEndianByteArray.load64(bytes, offset + 48), 42) * mul) + ((v[0] * 9) + LittleEndianByteArray.load64(bytes, offset + 40));
        z = Long.rotateRight(w[0] + z, 33) * mul;
        weakHashLength32WithSeeds(bytes, offset, v[1] * mul, x + w[0], v);
        bArr = bytes;
        weakHashLength32WithSeeds(bArr, offset + 32, z + w[1], y + LittleEndianByteArray.load64(bytes, offset + 16), w);
        return hashLength16((hashLength16(v[0], w[0], mul) + (shiftMix(y) * K0)) + x, hashLength16(v[1], w[1], mul) + z, mul);
    }
}

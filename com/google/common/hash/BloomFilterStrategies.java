package com.google.common.hash;

import com.google.common.base.Preconditions;
import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import java.math.RoundingMode;
import java.util.Arrays;
import javax.annotation.Nullable;

enum BloomFilterStrategies implements Strategy {
    MURMUR128_MITZ_32 {
        public <T> boolean put(T object, Funnel<? super T> funnel, int numHashFunctions, BitArray bits) {
            long bitSize = bits.bitSize();
            long hash64 = Hashing.murmur3_128().hashObject(object, funnel).asLong();
            int hash1 = (int) hash64;
            int hash2 = (int) (hash64 >>> 32);
            boolean bitsChanged = false;
            for (int i = 1; i <= numHashFunctions; i++) {
                int combinedHash = hash1 + (i * hash2);
                if (combinedHash < 0) {
                    combinedHash ^= -1;
                }
                bitsChanged |= bits.set(((long) combinedHash) % bitSize);
            }
            return bitsChanged;
        }

        public <T> boolean mightContain(T object, Funnel<? super T> funnel, int numHashFunctions, BitArray bits) {
            long bitSize = bits.bitSize();
            long hash64 = Hashing.murmur3_128().hashObject(object, funnel).asLong();
            int hash1 = (int) hash64;
            int hash2 = (int) (hash64 >>> 32);
            for (int i = 1; i <= numHashFunctions; i++) {
                int combinedHash = hash1 + (i * hash2);
                if (combinedHash < 0) {
                    combinedHash ^= -1;
                }
                if (!bits.get(((long) combinedHash) % bitSize)) {
                    return false;
                }
            }
            return true;
        }
    },
    MURMUR128_MITZ_64 {
        public <T> boolean put(T object, Funnel<? super T> funnel, int numHashFunctions, BitArray bits) {
            long bitSize = bits.bitSize();
            byte[] bytes = Hashing.murmur3_128().hashObject(object, funnel).getBytesInternal();
            long hash1 = lowerEight(bytes);
            long hash2 = upperEight(bytes);
            boolean bitsChanged = false;
            long combinedHash = hash1;
            for (int i = 0; i < numHashFunctions; i++) {
                bitsChanged |= bits.set((Long.MAX_VALUE & combinedHash) % bitSize);
                combinedHash += hash2;
            }
            return bitsChanged;
        }

        public <T> boolean mightContain(T object, Funnel<? super T> funnel, int numHashFunctions, BitArray bits) {
            long bitSize = bits.bitSize();
            byte[] bytes = Hashing.murmur3_128().hashObject(object, funnel).getBytesInternal();
            long hash1 = lowerEight(bytes);
            long hash2 = upperEight(bytes);
            long combinedHash = hash1;
            for (int i = 0; i < numHashFunctions; i++) {
                if (!bits.get((Long.MAX_VALUE & combinedHash) % bitSize)) {
                    return false;
                }
                combinedHash += hash2;
            }
            return true;
        }

        private long lowerEight(byte[] bytes) {
            return Longs.fromBytes(bytes[7], bytes[6], bytes[5], bytes[4], bytes[3], bytes[2], bytes[1], bytes[0]);
        }

        private long upperEight(byte[] bytes) {
            return Longs.fromBytes(bytes[15], bytes[14], bytes[13], bytes[12], bytes[11], bytes[10], bytes[9], bytes[8]);
        }
    };

    static final class BitArray {
        long bitCount;
        final long[] data;

        BitArray(long bits) {
            this(new long[Ints.checkedCast(LongMath.divide(bits, 64, RoundingMode.CEILING))]);
        }

        BitArray(long[] data) {
            Preconditions.checkArgument(data.length > 0, "data length is zero!");
            this.data = data;
            long bitCount = 0;
            for (long value : data) {
                bitCount += (long) Long.bitCount(value);
            }
            this.bitCount = bitCount;
        }

        boolean set(long index) {
            if (get(index)) {
                return false;
            }
            long[] jArr = this.data;
            int i = (int) (index >>> 6);
            jArr[i] = jArr[i] | (1 << ((int) index));
            this.bitCount++;
            return true;
        }

        boolean get(long index) {
            return (this.data[(int) (index >>> 6)] & (1 << ((int) index))) != 0;
        }

        long bitSize() {
            return ((long) this.data.length) * 64;
        }

        long bitCount() {
            return this.bitCount;
        }

        BitArray copy() {
            return new BitArray((long[]) this.data.clone());
        }

        void putAll(BitArray array) {
            Preconditions.checkArgument(this.data.length == array.data.length, "BitArrays must be of equal length (%s != %s)", this.data.length, array.data.length);
            this.bitCount = 0;
            for (int i = 0; i < this.data.length; i++) {
                long[] jArr = this.data;
                jArr[i] = jArr[i] | array.data[i];
                this.bitCount += (long) Long.bitCount(this.data[i]);
            }
        }

        public boolean equals(@Nullable Object o) {
            if (!(o instanceof BitArray)) {
                return false;
            }
            return Arrays.equals(this.data, ((BitArray) o).data);
        }

        public int hashCode() {
            return Arrays.hashCode(this.data);
        }
    }
}

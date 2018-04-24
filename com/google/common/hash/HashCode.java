package com.google.common.hash;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import com.google.common.primitives.UnsignedInts;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.itextpdf.text.pdf.Barcode128;
import java.io.Serializable;
import javax.annotation.Nullable;

@Beta
public abstract class HashCode {
    private static final char[] hexDigits = "0123456789abcdef".toCharArray();

    private static final class BytesHashCode extends HashCode implements Serializable {
        private static final long serialVersionUID = 0;
        final byte[] bytes;

        BytesHashCode(byte[] bytes) {
            this.bytes = (byte[]) Preconditions.checkNotNull(bytes);
        }

        public int bits() {
            return this.bytes.length * 8;
        }

        public byte[] asBytes() {
            return (byte[]) this.bytes.clone();
        }

        public int asInt() {
            boolean z;
            if (this.bytes.length >= 4) {
                z = true;
            } else {
                z = false;
            }
            Preconditions.checkState(z, "HashCode#asInt() requires >= 4 bytes (it only has %s bytes).", this.bytes.length);
            return (((this.bytes[0] & 255) | ((this.bytes[1] & 255) << 8)) | ((this.bytes[2] & 255) << 16)) | ((this.bytes[3] & 255) << 24);
        }

        public long asLong() {
            Preconditions.checkState(this.bytes.length >= 8, "HashCode#asLong() requires >= 8 bytes (it only has %s bytes).", this.bytes.length);
            return padToLong();
        }

        public long padToLong() {
            long retVal = (long) (this.bytes[0] & 255);
            for (int i = 1; i < Math.min(this.bytes.length, 8); i++) {
                retVal |= (((long) this.bytes[i]) & 255) << (i * 8);
            }
            return retVal;
        }

        void writeBytesToImpl(byte[] dest, int offset, int maxLength) {
            System.arraycopy(this.bytes, 0, dest, offset, maxLength);
        }

        byte[] getBytesInternal() {
            return this.bytes;
        }

        boolean equalsSameBits(HashCode that) {
            if (this.bytes.length != that.getBytesInternal().length) {
                return false;
            }
            boolean areEqual = true;
            for (int i = 0; i < this.bytes.length; i++) {
                int i2;
                if (this.bytes[i] == that.getBytesInternal()[i]) {
                    i2 = 1;
                } else {
                    i2 = 0;
                }
                areEqual &= i2;
            }
            return areEqual;
        }
    }

    private static final class IntHashCode extends HashCode implements Serializable {
        private static final long serialVersionUID = 0;
        final int hash;

        IntHashCode(int hash) {
            this.hash = hash;
        }

        public int bits() {
            return 32;
        }

        public byte[] asBytes() {
            return new byte[]{(byte) this.hash, (byte) (this.hash >> 8), (byte) (this.hash >> 16), (byte) (this.hash >> 24)};
        }

        public int asInt() {
            return this.hash;
        }

        public long asLong() {
            throw new IllegalStateException("this HashCode only has 32 bits; cannot create a long");
        }

        public long padToLong() {
            return UnsignedInts.toLong(this.hash);
        }

        void writeBytesToImpl(byte[] dest, int offset, int maxLength) {
            for (int i = 0; i < maxLength; i++) {
                dest[offset + i] = (byte) (this.hash >> (i * 8));
            }
        }

        boolean equalsSameBits(HashCode that) {
            return this.hash == that.asInt();
        }
    }

    private static final class LongHashCode extends HashCode implements Serializable {
        private static final long serialVersionUID = 0;
        final long hash;

        LongHashCode(long hash) {
            this.hash = hash;
        }

        public int bits() {
            return 64;
        }

        public byte[] asBytes() {
            return new byte[]{(byte) ((int) this.hash), (byte) ((int) (this.hash >> 8)), (byte) ((int) (this.hash >> 16)), (byte) ((int) (this.hash >> 24)), (byte) ((int) (this.hash >> 32)), (byte) ((int) (this.hash >> 40)), (byte) ((int) (this.hash >> 48)), (byte) ((int) (this.hash >> 56))};
        }

        public int asInt() {
            return (int) this.hash;
        }

        public long asLong() {
            return this.hash;
        }

        public long padToLong() {
            return this.hash;
        }

        void writeBytesToImpl(byte[] dest, int offset, int maxLength) {
            for (int i = 0; i < maxLength; i++) {
                dest[offset + i] = (byte) ((int) (this.hash >> (i * 8)));
            }
        }

        boolean equalsSameBits(HashCode that) {
            return this.hash == that.asLong();
        }
    }

    public abstract byte[] asBytes();

    public abstract int asInt();

    public abstract long asLong();

    public abstract int bits();

    abstract boolean equalsSameBits(HashCode hashCode);

    public abstract long padToLong();

    abstract void writeBytesToImpl(byte[] bArr, int i, int i2);

    HashCode() {
    }

    @CanIgnoreReturnValue
    public int writeBytesTo(byte[] dest, int offset, int maxLength) {
        maxLength = Ints.min(maxLength, bits() / 8);
        Preconditions.checkPositionIndexes(offset, offset + maxLength, dest.length);
        writeBytesToImpl(dest, offset, maxLength);
        return maxLength;
    }

    byte[] getBytesInternal() {
        return asBytes();
    }

    public static HashCode fromInt(int hash) {
        return new IntHashCode(hash);
    }

    public static HashCode fromLong(long hash) {
        return new LongHashCode(hash);
    }

    public static HashCode fromBytes(byte[] bytes) {
        boolean z = true;
        if (bytes.length < 1) {
            z = false;
        }
        Preconditions.checkArgument(z, "A HashCode must contain at least 1 byte.");
        return fromBytesNoCopy((byte[]) bytes.clone());
    }

    static HashCode fromBytesNoCopy(byte[] bytes) {
        return new BytesHashCode(bytes);
    }

    public static HashCode fromString(String string) {
        boolean z;
        boolean z2 = true;
        if (string.length() >= 2) {
            z = true;
        } else {
            z = false;
        }
        Preconditions.checkArgument(z, "input string (%s) must have at least 2 characters", (Object) string);
        if (string.length() % 2 != 0) {
            z2 = false;
        }
        Preconditions.checkArgument(z2, "input string (%s) must have an even number of characters", (Object) string);
        byte[] bytes = new byte[(string.length() / 2)];
        for (int i = 0; i < string.length(); i += 2) {
            bytes[i / 2] = (byte) ((decode(string.charAt(i)) << 4) + decode(string.charAt(i + 1)));
        }
        return fromBytesNoCopy(bytes);
    }

    private static int decode(char ch) {
        if (ch >= '0' && ch <= '9') {
            return ch - 48;
        }
        if (ch >= 'a' && ch <= Barcode128.FNC1_INDEX) {
            return (ch - 97) + 10;
        }
        throw new IllegalArgumentException("Illegal hexadecimal character: " + ch);
    }

    public final boolean equals(@Nullable Object object) {
        if (!(object instanceof HashCode)) {
            return false;
        }
        HashCode that = (HashCode) object;
        if (bits() == that.bits() && equalsSameBits(that)) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        if (bits() >= 32) {
            return asInt();
        }
        byte[] bytes = getBytesInternal();
        int val = bytes[0] & 255;
        for (int i = 1; i < bytes.length; i++) {
            val |= (bytes[i] & 255) << (i * 8);
        }
        return val;
    }

    public final String toString() {
        byte[] bytes = getBytesInternal();
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(hexDigits[(b >> 4) & 15]).append(hexDigits[b & 15]);
        }
        return sb.toString();
    }
}

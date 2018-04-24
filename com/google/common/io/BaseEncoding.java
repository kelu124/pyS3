package com.google.common.io;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Ascii;
import com.google.common.base.CharMatcher;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.math.RoundingMode;
import java.util.Arrays;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
public abstract class BaseEncoding {
    private static final BaseEncoding BASE16 = new Base16Encoding("base16()", "0123456789ABCDEF");
    private static final BaseEncoding BASE32 = new StandardBaseEncoding("base32()", "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567", Character.valueOf('='));
    private static final BaseEncoding BASE32_HEX = new StandardBaseEncoding("base32Hex()", "0123456789ABCDEFGHIJKLMNOPQRSTUV", Character.valueOf('='));
    private static final BaseEncoding BASE64 = new Base64Encoding("base64()", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/", Character.valueOf('='));
    private static final BaseEncoding BASE64_URL = new Base64Encoding("base64Url()", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_", Character.valueOf('='));

    private static final class Alphabet extends CharMatcher {
        final int bitsPerChar;
        final int bytesPerChunk;
        private final char[] chars;
        final int charsPerChunk;
        private final byte[] decodabet;
        final int mask;
        private final String name;
        private final boolean[] validPadding;

        Alphabet(String name, char[] chars) {
            this.name = (String) Preconditions.checkNotNull(name);
            this.chars = (char[]) Preconditions.checkNotNull(chars);
            try {
                this.bitsPerChar = IntMath.log2(chars.length, RoundingMode.UNNECESSARY);
                int gcd = Math.min(8, Integer.lowestOneBit(this.bitsPerChar));
                try {
                    int i;
                    this.charsPerChunk = 8 / gcd;
                    this.bytesPerChunk = this.bitsPerChar / gcd;
                    this.mask = chars.length - 1;
                    byte[] decodabet = new byte[128];
                    Arrays.fill(decodabet, (byte) -1);
                    for (i = 0; i < chars.length; i++) {
                        char c = chars[i];
                        Preconditions.checkArgument(CharMatcher.ascii().matches(c), "Non-ASCII character: %s", c);
                        Preconditions.checkArgument(decodabet[c] == (byte) -1, "Duplicate character: %s", c);
                        decodabet[c] = (byte) i;
                    }
                    this.decodabet = decodabet;
                    boolean[] validPadding = new boolean[this.charsPerChunk];
                    for (i = 0; i < this.bytesPerChunk; i++) {
                        validPadding[IntMath.divide(i * 8, this.bitsPerChar, RoundingMode.CEILING)] = true;
                    }
                    this.validPadding = validPadding;
                } catch (ArithmeticException e) {
                    throw new IllegalArgumentException("Illegal alphabet " + new String(chars), e);
                }
            } catch (ArithmeticException e2) {
                throw new IllegalArgumentException("Illegal alphabet length " + chars.length, e2);
            }
        }

        char encode(int bits) {
            return this.chars[bits];
        }

        boolean isValidPaddingStartPosition(int index) {
            return this.validPadding[index % this.charsPerChunk];
        }

        boolean canDecode(char ch) {
            return ch <= Ascii.MAX && this.decodabet[ch] != (byte) -1;
        }

        int decode(char ch) throws DecodingException {
            if (ch <= Ascii.MAX && this.decodabet[ch] != (byte) -1) {
                return this.decodabet[ch];
            }
            throw new DecodingException("Unrecognized character: " + (CharMatcher.invisible().matches(ch) ? "0x" + Integer.toHexString(ch) : Character.valueOf(ch)));
        }

        private boolean hasLowerCase() {
            for (char c : this.chars) {
                if (Ascii.isLowerCase(c)) {
                    return true;
                }
            }
            return false;
        }

        private boolean hasUpperCase() {
            for (char c : this.chars) {
                if (Ascii.isUpperCase(c)) {
                    return true;
                }
            }
            return false;
        }

        Alphabet upperCase() {
            if (!hasLowerCase()) {
                return this;
            }
            Preconditions.checkState(!hasUpperCase(), "Cannot call upperCase() on a mixed-case alphabet");
            char[] upperCased = new char[this.chars.length];
            for (int i = 0; i < this.chars.length; i++) {
                upperCased[i] = Ascii.toUpperCase(this.chars[i]);
            }
            return new Alphabet(this.name + ".upperCase()", upperCased);
        }

        Alphabet lowerCase() {
            if (!hasUpperCase()) {
                return this;
            }
            Preconditions.checkState(!hasLowerCase(), "Cannot call lowerCase() on a mixed-case alphabet");
            char[] lowerCased = new char[this.chars.length];
            for (int i = 0; i < this.chars.length; i++) {
                lowerCased[i] = Ascii.toLowerCase(this.chars[i]);
            }
            return new Alphabet(this.name + ".lowerCase()", lowerCased);
        }

        public boolean matches(char c) {
            return CharMatcher.ascii().matches(c) && this.decodabet[c] != (byte) -1;
        }

        public String toString() {
            return this.name;
        }

        public boolean equals(@Nullable Object other) {
            if (!(other instanceof Alphabet)) {
                return false;
            }
            return Arrays.equals(this.chars, ((Alphabet) other).chars);
        }

        public int hashCode() {
            return Arrays.hashCode(this.chars);
        }
    }

    static class StandardBaseEncoding extends BaseEncoding {
        final Alphabet alphabet;
        private transient BaseEncoding lowerCase;
        @Nullable
        final Character paddingChar;
        private transient BaseEncoding upperCase;

        StandardBaseEncoding(String name, String alphabetChars, @Nullable Character paddingChar) {
            this(new Alphabet(name, alphabetChars.toCharArray()), paddingChar);
        }

        StandardBaseEncoding(Alphabet alphabet, @Nullable Character paddingChar) {
            this.alphabet = (Alphabet) Preconditions.checkNotNull(alphabet);
            boolean z = paddingChar == null || !alphabet.matches(paddingChar.charValue());
            Preconditions.checkArgument(z, "Padding character %s was already in alphabet", (Object) paddingChar);
            this.paddingChar = paddingChar;
        }

        CharMatcher padding() {
            return this.paddingChar == null ? CharMatcher.none() : CharMatcher.is(this.paddingChar.charValue());
        }

        int maxEncodedSize(int bytes) {
            return this.alphabet.charsPerChunk * IntMath.divide(bytes, this.alphabet.bytesPerChunk, RoundingMode.CEILING);
        }

        @GwtIncompatible
        public OutputStream encodingStream(final Writer out) {
            Preconditions.checkNotNull(out);
            return new OutputStream() {
                int bitBuffer = 0;
                int bitBufferLength = 0;
                int writtenChars = 0;

                public void write(int b) throws IOException {
                    this.bitBuffer <<= 8;
                    this.bitBuffer |= b & 255;
                    this.bitBufferLength += 8;
                    while (this.bitBufferLength >= StandardBaseEncoding.this.alphabet.bitsPerChar) {
                        out.write(StandardBaseEncoding.this.alphabet.encode((this.bitBuffer >> (this.bitBufferLength - StandardBaseEncoding.this.alphabet.bitsPerChar)) & StandardBaseEncoding.this.alphabet.mask));
                        this.writtenChars++;
                        this.bitBufferLength -= StandardBaseEncoding.this.alphabet.bitsPerChar;
                    }
                }

                public void flush() throws IOException {
                    out.flush();
                }

                public void close() throws IOException {
                    if (this.bitBufferLength > 0) {
                        out.write(StandardBaseEncoding.this.alphabet.encode((this.bitBuffer << (StandardBaseEncoding.this.alphabet.bitsPerChar - this.bitBufferLength)) & StandardBaseEncoding.this.alphabet.mask));
                        this.writtenChars++;
                        if (StandardBaseEncoding.this.paddingChar != null) {
                            while (this.writtenChars % StandardBaseEncoding.this.alphabet.charsPerChunk != 0) {
                                out.write(StandardBaseEncoding.this.paddingChar.charValue());
                                this.writtenChars++;
                            }
                        }
                    }
                    out.close();
                }
            };
        }

        void encodeTo(Appendable target, byte[] bytes, int off, int len) throws IOException {
            Preconditions.checkNotNull(target);
            Preconditions.checkPositionIndexes(off, off + len, bytes.length);
            int i = 0;
            while (i < len) {
                encodeChunkTo(target, bytes, off + i, Math.min(this.alphabet.bytesPerChunk, len - i));
                i += this.alphabet.bytesPerChunk;
            }
        }

        void encodeChunkTo(Appendable target, byte[] bytes, int off, int len) throws IOException {
            Preconditions.checkNotNull(target);
            Preconditions.checkPositionIndexes(off, off + len, bytes.length);
            Preconditions.checkArgument(len <= this.alphabet.bytesPerChunk);
            long bitBuffer = 0;
            for (int i = 0; i < len; i++) {
                bitBuffer = (bitBuffer | ((long) (bytes[off + i] & 255))) << 8;
            }
            int bitOffset = ((len + 1) * 8) - this.alphabet.bitsPerChar;
            int bitsProcessed = 0;
            while (bitsProcessed < len * 8) {
                target.append(this.alphabet.encode(((int) (bitBuffer >>> (bitOffset - bitsProcessed))) & this.alphabet.mask));
                bitsProcessed += this.alphabet.bitsPerChar;
            }
            if (this.paddingChar != null) {
                while (bitsProcessed < this.alphabet.bytesPerChunk * 8) {
                    target.append(this.paddingChar.charValue());
                    bitsProcessed += this.alphabet.bitsPerChar;
                }
            }
        }

        int maxDecodedSize(int chars) {
            return (int) (((((long) this.alphabet.bitsPerChar) * ((long) chars)) + 7) / 8);
        }

        public boolean canDecode(CharSequence chars) {
            chars = padding().trimTrailingFrom(chars);
            if (!this.alphabet.isValidPaddingStartPosition(chars.length())) {
                return false;
            }
            for (int i = 0; i < chars.length(); i++) {
                if (!this.alphabet.canDecode(chars.charAt(i))) {
                    return false;
                }
            }
            return true;
        }

        int decodeTo(byte[] target, CharSequence chars) throws DecodingException {
            Preconditions.checkNotNull(target);
            chars = padding().trimTrailingFrom(chars);
            if (this.alphabet.isValidPaddingStartPosition(chars.length())) {
                int bytesWritten = 0;
                int charIdx = 0;
                while (charIdx < chars.length()) {
                    long chunk = 0;
                    int charsProcessed = 0;
                    for (int i = 0; i < this.alphabet.charsPerChunk; i++) {
                        chunk <<= this.alphabet.bitsPerChar;
                        if (charIdx + i < chars.length()) {
                            chunk |= (long) this.alphabet.decode(chars.charAt(charIdx + charsProcessed));
                            charsProcessed++;
                        }
                    }
                    int minOffset = (this.alphabet.bytesPerChunk * 8) - (this.alphabet.bitsPerChar * charsProcessed);
                    int offset = (this.alphabet.bytesPerChunk - 1) * 8;
                    int bytesWritten2 = bytesWritten;
                    while (offset >= minOffset) {
                        bytesWritten = bytesWritten2 + 1;
                        target[bytesWritten2] = (byte) ((int) ((chunk >>> offset) & 255));
                        offset -= 8;
                        bytesWritten2 = bytesWritten;
                    }
                    charIdx += this.alphabet.charsPerChunk;
                    bytesWritten = bytesWritten2;
                }
                return bytesWritten;
            }
            throw new DecodingException("Invalid input length " + chars.length());
        }

        @GwtIncompatible
        public InputStream decodingStream(final Reader reader) {
            Preconditions.checkNotNull(reader);
            return new InputStream() {
                int bitBuffer = 0;
                int bitBufferLength = 0;
                boolean hitPadding = false;
                final CharMatcher paddingMatcher = StandardBaseEncoding.this.padding();
                int readChars = 0;

                /* JADX WARNING: inconsistent code. */
                /* Code decompiled incorrectly, please refer to instructions dump. */
                public int read() throws java.io.IOException {
                    /*
                    r6 = this;
                    r5 = 1;
                    r2 = -1;
                L_0x0002:
                    r3 = r2;
                    r1 = r3.read();
                    if (r1 != r2) goto L_0x0035;
                L_0x000a:
                    r3 = r6.hitPadding;
                    if (r3 != 0) goto L_0x00d7;
                L_0x000e:
                    r3 = com.google.common.io.BaseEncoding.StandardBaseEncoding.this;
                    r3 = r3.alphabet;
                    r4 = r6.readChars;
                    r3 = r3.isValidPaddingStartPosition(r4);
                    if (r3 != 0) goto L_0x00d7;
                L_0x001a:
                    r2 = new com.google.common.io.BaseEncoding$DecodingException;
                    r3 = new java.lang.StringBuilder;
                    r3.<init>();
                    r4 = "Invalid input length ";
                    r3 = r3.append(r4);
                    r4 = r6.readChars;
                    r3 = r3.append(r4);
                    r3 = r3.toString();
                    r2.<init>(r3);
                    throw r2;
                L_0x0035:
                    r3 = r6.readChars;
                    r3 = r3 + 1;
                    r6.readChars = r3;
                    r0 = (char) r1;
                    r3 = r6.paddingMatcher;
                    r3 = r3.matches(r0);
                    if (r3 == 0) goto L_0x0078;
                L_0x0044:
                    r3 = r6.hitPadding;
                    if (r3 != 0) goto L_0x0075;
                L_0x0048:
                    r3 = r6.readChars;
                    if (r3 == r5) goto L_0x005a;
                L_0x004c:
                    r3 = com.google.common.io.BaseEncoding.StandardBaseEncoding.this;
                    r3 = r3.alphabet;
                    r4 = r6.readChars;
                    r4 = r4 + -1;
                    r3 = r3.isValidPaddingStartPosition(r4);
                    if (r3 != 0) goto L_0x0075;
                L_0x005a:
                    r2 = new com.google.common.io.BaseEncoding$DecodingException;
                    r3 = new java.lang.StringBuilder;
                    r3.<init>();
                    r4 = "Padding cannot start at index ";
                    r3 = r3.append(r4);
                    r4 = r6.readChars;
                    r3 = r3.append(r4);
                    r3 = r3.toString();
                    r2.<init>(r3);
                    throw r2;
                L_0x0075:
                    r6.hitPadding = r5;
                    goto L_0x0002;
                L_0x0078:
                    r3 = r6.hitPadding;
                    if (r3 == 0) goto L_0x00a1;
                L_0x007c:
                    r2 = new com.google.common.io.BaseEncoding$DecodingException;
                    r3 = new java.lang.StringBuilder;
                    r3.<init>();
                    r4 = "Expected padding character but found '";
                    r3 = r3.append(r4);
                    r3 = r3.append(r0);
                    r4 = "' at index ";
                    r3 = r3.append(r4);
                    r4 = r6.readChars;
                    r3 = r3.append(r4);
                    r3 = r3.toString();
                    r2.<init>(r3);
                    throw r2;
                L_0x00a1:
                    r3 = r6.bitBuffer;
                    r4 = com.google.common.io.BaseEncoding.StandardBaseEncoding.this;
                    r4 = r4.alphabet;
                    r4 = r4.bitsPerChar;
                    r3 = r3 << r4;
                    r6.bitBuffer = r3;
                    r3 = r6.bitBuffer;
                    r4 = com.google.common.io.BaseEncoding.StandardBaseEncoding.this;
                    r4 = r4.alphabet;
                    r4 = r4.decode(r0);
                    r3 = r3 | r4;
                    r6.bitBuffer = r3;
                    r3 = r6.bitBufferLength;
                    r4 = com.google.common.io.BaseEncoding.StandardBaseEncoding.this;
                    r4 = r4.alphabet;
                    r4 = r4.bitsPerChar;
                    r3 = r3 + r4;
                    r6.bitBufferLength = r3;
                    r3 = r6.bitBufferLength;
                    r4 = 8;
                    if (r3 < r4) goto L_0x0002;
                L_0x00ca:
                    r2 = r6.bitBufferLength;
                    r2 = r2 + -8;
                    r6.bitBufferLength = r2;
                    r2 = r6.bitBuffer;
                    r3 = r6.bitBufferLength;
                    r2 = r2 >> r3;
                    r2 = r2 & 255;
                L_0x00d7:
                    return r2;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.google.common.io.BaseEncoding.StandardBaseEncoding.2.read():int");
                }

                public void close() throws IOException {
                    reader.close();
                }
            };
        }

        public BaseEncoding omitPadding() {
            return this.paddingChar == null ? this : newInstance(this.alphabet, null);
        }

        public BaseEncoding withPadChar(char padChar) {
            if (8 % this.alphabet.bitsPerChar != 0) {
                return (this.paddingChar == null || this.paddingChar.charValue() != padChar) ? newInstance(this.alphabet, Character.valueOf(padChar)) : this;
            } else {
                return this;
            }
        }

        public BaseEncoding withSeparator(String separator, int afterEveryChars) {
            Preconditions.checkArgument(padding().or(this.alphabet).matchesNoneOf(separator), "Separator (%s) cannot contain alphabet or padding characters", (Object) separator);
            return new SeparatedBaseEncoding(this, separator, afterEveryChars);
        }

        public BaseEncoding upperCase() {
            BaseEncoding result = this.upperCase;
            if (result == null) {
                Alphabet upper = this.alphabet.upperCase();
                result = upper == this.alphabet ? this : newInstance(upper, this.paddingChar);
                this.upperCase = result;
            }
            return result;
        }

        public BaseEncoding lowerCase() {
            BaseEncoding result = this.lowerCase;
            if (result == null) {
                Alphabet lower = this.alphabet.lowerCase();
                result = lower == this.alphabet ? this : newInstance(lower, this.paddingChar);
                this.lowerCase = result;
            }
            return result;
        }

        BaseEncoding newInstance(Alphabet alphabet, @Nullable Character paddingChar) {
            return new StandardBaseEncoding(alphabet, paddingChar);
        }

        public String toString() {
            StringBuilder builder = new StringBuilder("BaseEncoding.");
            builder.append(this.alphabet.toString());
            if (8 % this.alphabet.bitsPerChar != 0) {
                if (this.paddingChar == null) {
                    builder.append(".omitPadding()");
                } else {
                    builder.append(".withPadChar('").append(this.paddingChar).append("')");
                }
            }
            return builder.toString();
        }

        public boolean equals(@Nullable Object other) {
            if (!(other instanceof StandardBaseEncoding)) {
                return false;
            }
            StandardBaseEncoding that = (StandardBaseEncoding) other;
            if (this.alphabet.equals(that.alphabet) && Objects.equal(this.paddingChar, that.paddingChar)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return this.alphabet.hashCode() ^ Objects.hashCode(this.paddingChar);
        }
    }

    static final class Base16Encoding extends StandardBaseEncoding {
        final char[] encoding;

        Base16Encoding(String name, String alphabetChars) {
            this(new Alphabet(name, alphabetChars.toCharArray()));
        }

        private Base16Encoding(Alphabet alphabet) {
            super(alphabet, null);
            this.encoding = new char[512];
            Preconditions.checkArgument(alphabet.chars.length == 16);
            for (int i = 0; i < 256; i++) {
                this.encoding[i] = alphabet.encode(i >>> 4);
                this.encoding[i | 256] = alphabet.encode(i & 15);
            }
        }

        void encodeTo(Appendable target, byte[] bytes, int off, int len) throws IOException {
            Preconditions.checkNotNull(target);
            Preconditions.checkPositionIndexes(off, off + len, bytes.length);
            for (int i = 0; i < len; i++) {
                int b = bytes[off + i] & 255;
                target.append(this.encoding[b]);
                target.append(this.encoding[b | 256]);
            }
        }

        int decodeTo(byte[] target, CharSequence chars) throws DecodingException {
            Preconditions.checkNotNull(target);
            if (chars.length() % 2 == 1) {
                throw new DecodingException("Invalid input length " + chars.length());
            }
            int bytesWritten = 0;
            int i = 0;
            while (i < chars.length()) {
                int bytesWritten2 = bytesWritten + 1;
                target[bytesWritten] = (byte) ((this.alphabet.decode(chars.charAt(i)) << 4) | this.alphabet.decode(chars.charAt(i + 1)));
                i += 2;
                bytesWritten = bytesWritten2;
            }
            return bytesWritten;
        }

        BaseEncoding newInstance(Alphabet alphabet, @Nullable Character paddingChar) {
            return new Base16Encoding(alphabet);
        }
    }

    static final class Base64Encoding extends StandardBaseEncoding {
        Base64Encoding(String name, String alphabetChars, @Nullable Character paddingChar) {
            this(new Alphabet(name, alphabetChars.toCharArray()), paddingChar);
        }

        private Base64Encoding(Alphabet alphabet, @Nullable Character paddingChar) {
            super(alphabet, paddingChar);
            Preconditions.checkArgument(alphabet.chars.length == 64);
        }

        void encodeTo(Appendable target, byte[] bytes, int off, int len) throws IOException {
            Preconditions.checkNotNull(target);
            Preconditions.checkPositionIndexes(off, off + len, bytes.length);
            int remaining = len;
            int i = off;
            while (remaining >= 3) {
                int i2 = i + 1;
                i = i2 + 1;
                i2 = i + 1;
                int chunk = (((bytes[i] & 255) << 16) | ((bytes[i2] & 255) << 8)) | (bytes[i] & 255);
                target.append(this.alphabet.encode(chunk >>> 18));
                target.append(this.alphabet.encode((chunk >>> 12) & 63));
                target.append(this.alphabet.encode((chunk >>> 6) & 63));
                target.append(this.alphabet.encode(chunk & 63));
                remaining -= 3;
                i = i2;
            }
            if (i < off + len) {
                encodeChunkTo(target, bytes, i, (off + len) - i);
            }
        }

        int decodeTo(byte[] target, CharSequence chars) throws DecodingException {
            Preconditions.checkNotNull(target);
            chars = padding().trimTrailingFrom(chars);
            if (this.alphabet.isValidPaddingStartPosition(chars.length())) {
                int bytesWritten = 0;
                int i = 0;
                while (i < chars.length()) {
                    int i2 = i + 1;
                    i = i2 + 1;
                    int chunk = (this.alphabet.decode(chars.charAt(i)) << 18) | (this.alphabet.decode(chars.charAt(i2)) << 12);
                    int bytesWritten2 = bytesWritten + 1;
                    target[bytesWritten] = (byte) (chunk >>> 16);
                    if (i < chars.length()) {
                        i2 = i + 1;
                        chunk |= this.alphabet.decode(chars.charAt(i)) << 6;
                        bytesWritten = bytesWritten2 + 1;
                        target[bytesWritten2] = (byte) ((chunk >>> 8) & 255);
                        if (i2 < chars.length()) {
                            i = i2 + 1;
                            bytesWritten2 = bytesWritten + 1;
                            target[bytesWritten] = (byte) ((chunk | this.alphabet.decode(chars.charAt(i2))) & 255);
                        } else {
                            i = i2;
                        }
                    }
                    bytesWritten = bytesWritten2;
                }
                return bytesWritten;
            }
            throw new DecodingException("Invalid input length " + chars.length());
        }

        BaseEncoding newInstance(Alphabet alphabet, @Nullable Character paddingChar) {
            return new Base64Encoding(alphabet, paddingChar);
        }
    }

    public static final class DecodingException extends IOException {
        DecodingException(String message) {
            super(message);
        }

        DecodingException(Throwable cause) {
            super(cause);
        }
    }

    static final class SeparatedBaseEncoding extends BaseEncoding {
        private final int afterEveryChars;
        private final BaseEncoding delegate;
        private final String separator;
        private final CharMatcher separatorChars;

        SeparatedBaseEncoding(BaseEncoding delegate, String separator, int afterEveryChars) {
            this.delegate = (BaseEncoding) Preconditions.checkNotNull(delegate);
            this.separator = (String) Preconditions.checkNotNull(separator);
            this.afterEveryChars = afterEveryChars;
            Preconditions.checkArgument(afterEveryChars > 0, "Cannot add a separator after every %s chars", afterEveryChars);
            this.separatorChars = CharMatcher.anyOf(separator).precomputed();
        }

        CharMatcher padding() {
            return this.delegate.padding();
        }

        int maxEncodedSize(int bytes) {
            int unseparatedSize = this.delegate.maxEncodedSize(bytes);
            return (this.separator.length() * IntMath.divide(Math.max(0, unseparatedSize - 1), this.afterEveryChars, RoundingMode.FLOOR)) + unseparatedSize;
        }

        @GwtIncompatible
        public OutputStream encodingStream(Writer output) {
            return this.delegate.encodingStream(BaseEncoding.separatingWriter(output, this.separator, this.afterEveryChars));
        }

        void encodeTo(Appendable target, byte[] bytes, int off, int len) throws IOException {
            this.delegate.encodeTo(BaseEncoding.separatingAppendable(target, this.separator, this.afterEveryChars), bytes, off, len);
        }

        int maxDecodedSize(int chars) {
            return this.delegate.maxDecodedSize(chars);
        }

        public boolean canDecode(CharSequence chars) {
            return this.delegate.canDecode(this.separatorChars.removeFrom(chars));
        }

        int decodeTo(byte[] target, CharSequence chars) throws DecodingException {
            return this.delegate.decodeTo(target, this.separatorChars.removeFrom(chars));
        }

        @GwtIncompatible
        public InputStream decodingStream(Reader reader) {
            return this.delegate.decodingStream(BaseEncoding.ignoringReader(reader, this.separatorChars));
        }

        public BaseEncoding omitPadding() {
            return this.delegate.omitPadding().withSeparator(this.separator, this.afterEveryChars);
        }

        public BaseEncoding withPadChar(char padChar) {
            return this.delegate.withPadChar(padChar).withSeparator(this.separator, this.afterEveryChars);
        }

        public BaseEncoding withSeparator(String separator, int afterEveryChars) {
            throw new UnsupportedOperationException("Already have a separator");
        }

        public BaseEncoding upperCase() {
            return this.delegate.upperCase().withSeparator(this.separator, this.afterEveryChars);
        }

        public BaseEncoding lowerCase() {
            return this.delegate.lowerCase().withSeparator(this.separator, this.afterEveryChars);
        }

        public String toString() {
            return this.delegate + ".withSeparator(\"" + this.separator + "\", " + this.afterEveryChars + ")";
        }
    }

    public abstract boolean canDecode(CharSequence charSequence);

    abstract int decodeTo(byte[] bArr, CharSequence charSequence) throws DecodingException;

    @GwtIncompatible
    public abstract InputStream decodingStream(Reader reader);

    abstract void encodeTo(Appendable appendable, byte[] bArr, int i, int i2) throws IOException;

    @GwtIncompatible
    public abstract OutputStream encodingStream(Writer writer);

    public abstract BaseEncoding lowerCase();

    abstract int maxDecodedSize(int i);

    abstract int maxEncodedSize(int i);

    public abstract BaseEncoding omitPadding();

    abstract CharMatcher padding();

    public abstract BaseEncoding upperCase();

    public abstract BaseEncoding withPadChar(char c);

    public abstract BaseEncoding withSeparator(String str, int i);

    BaseEncoding() {
    }

    public String encode(byte[] bytes) {
        return encode(bytes, 0, bytes.length);
    }

    public final String encode(byte[] bytes, int off, int len) {
        Preconditions.checkPositionIndexes(off, off + len, bytes.length);
        StringBuilder result = new StringBuilder(maxEncodedSize(len));
        try {
            encodeTo(result, bytes, off, len);
            return result.toString();
        } catch (IOException impossible) {
            throw new AssertionError(impossible);
        }
    }

    @GwtIncompatible
    public final ByteSink encodingSink(final CharSink encodedSink) {
        Preconditions.checkNotNull(encodedSink);
        return new ByteSink() {
            public OutputStream openStream() throws IOException {
                return BaseEncoding.this.encodingStream(encodedSink.openStream());
            }
        };
    }

    private static byte[] extract(byte[] result, int length) {
        if (length == result.length) {
            return result;
        }
        byte[] trunc = new byte[length];
        System.arraycopy(result, 0, trunc, 0, length);
        return trunc;
    }

    public final byte[] decode(CharSequence chars) {
        try {
            return decodeChecked(chars);
        } catch (DecodingException badInput) {
            throw new IllegalArgumentException(badInput);
        }
    }

    final byte[] decodeChecked(CharSequence chars) throws DecodingException {
        chars = padding().trimTrailingFrom(chars);
        byte[] tmp = new byte[maxDecodedSize(chars.length())];
        return extract(tmp, decodeTo(tmp, chars));
    }

    @GwtIncompatible
    public final ByteSource decodingSource(final CharSource encodedSource) {
        Preconditions.checkNotNull(encodedSource);
        return new ByteSource() {
            public InputStream openStream() throws IOException {
                return BaseEncoding.this.decodingStream(encodedSource.openStream());
            }
        };
    }

    public static BaseEncoding base64() {
        return BASE64;
    }

    public static BaseEncoding base64Url() {
        return BASE64_URL;
    }

    public static BaseEncoding base32() {
        return BASE32;
    }

    public static BaseEncoding base32Hex() {
        return BASE32_HEX;
    }

    public static BaseEncoding base16() {
        return BASE16;
    }

    @GwtIncompatible
    static Reader ignoringReader(final Reader delegate, final CharMatcher toIgnore) {
        Preconditions.checkNotNull(delegate);
        Preconditions.checkNotNull(toIgnore);
        return new Reader() {
            public int read() throws IOException {
                int readChar;
                do {
                    readChar = delegate.read();
                    if (readChar == -1) {
                        break;
                    }
                } while (toIgnore.matches((char) readChar));
                return readChar;
            }

            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new UnsupportedOperationException();
            }

            public void close() throws IOException {
                delegate.close();
            }
        };
    }

    static Appendable separatingAppendable(final Appendable delegate, final String separator, final int afterEveryChars) {
        Preconditions.checkNotNull(delegate);
        Preconditions.checkNotNull(separator);
        Preconditions.checkArgument(afterEveryChars > 0);
        return new Appendable() {
            int charsUntilSeparator = afterEveryChars;

            public Appendable append(char c) throws IOException {
                if (this.charsUntilSeparator == 0) {
                    delegate.append(separator);
                    this.charsUntilSeparator = afterEveryChars;
                }
                delegate.append(c);
                this.charsUntilSeparator--;
                return this;
            }

            public Appendable append(CharSequence chars, int off, int len) throws IOException {
                throw new UnsupportedOperationException();
            }

            public Appendable append(CharSequence chars) throws IOException {
                throw new UnsupportedOperationException();
            }
        };
    }

    @GwtIncompatible
    static Writer separatingWriter(final Writer delegate, String separator, int afterEveryChars) {
        final Appendable seperatingAppendable = separatingAppendable(delegate, separator, afterEveryChars);
        return new Writer() {
            public void write(int c) throws IOException {
                seperatingAppendable.append((char) c);
            }

            public void write(char[] chars, int off, int len) throws IOException {
                throw new UnsupportedOperationException();
            }

            public void flush() throws IOException {
                delegate.flush();
            }

            public void close() throws IOException {
                delegate.close();
            }
        };
    }
}

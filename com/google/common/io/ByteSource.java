package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Ascii;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.hash.Funnels;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Iterator;

@GwtIncompatible
public abstract class ByteSource {

    private final class AsCharSource extends CharSource {
        final Charset charset;

        AsCharSource(Charset charset) {
            this.charset = (Charset) Preconditions.checkNotNull(charset);
        }

        public ByteSource asByteSource(Charset charset) {
            if (charset.equals(this.charset)) {
                return ByteSource.this;
            }
            return super.asByteSource(charset);
        }

        public Reader openStream() throws IOException {
            return new InputStreamReader(ByteSource.this.openStream(), this.charset);
        }

        public String toString() {
            return ByteSource.this.toString() + ".asCharSource(" + this.charset + ")";
        }
    }

    private static class ByteArrayByteSource extends ByteSource {
        final byte[] bytes;
        final int length;
        final int offset;

        ByteArrayByteSource(byte[] bytes) {
            this(bytes, 0, bytes.length);
        }

        ByteArrayByteSource(byte[] bytes, int offset, int length) {
            this.bytes = bytes;
            this.offset = offset;
            this.length = length;
        }

        public InputStream openStream() {
            return new ByteArrayInputStream(this.bytes, this.offset, this.length);
        }

        public InputStream openBufferedStream() throws IOException {
            return openStream();
        }

        public boolean isEmpty() {
            return this.length == 0;
        }

        public long size() {
            return (long) this.length;
        }

        public Optional<Long> sizeIfKnown() {
            return Optional.of(Long.valueOf((long) this.length));
        }

        public byte[] read() {
            return Arrays.copyOfRange(this.bytes, this.offset, this.offset + this.length);
        }

        public long copyTo(OutputStream output) throws IOException {
            output.write(this.bytes, this.offset, this.length);
            return (long) this.length;
        }

        public <T> T read(ByteProcessor<T> processor) throws IOException {
            processor.processBytes(this.bytes, this.offset, this.length);
            return processor.getResult();
        }

        public HashCode hash(HashFunction hashFunction) throws IOException {
            return hashFunction.hashBytes(this.bytes, this.offset, this.length);
        }

        public ByteSource slice(long offset, long length) {
            boolean z;
            boolean z2 = true;
            if (offset >= 0) {
                z = true;
            } else {
                z = false;
            }
            Preconditions.checkArgument(z, "offset (%s) may not be negative", offset);
            if (length < 0) {
                z2 = false;
            }
            Preconditions.checkArgument(z2, "length (%s) may not be negative", length);
            offset = Math.min(offset, (long) this.length);
            return new ByteArrayByteSource(this.bytes, this.offset + ((int) offset), (int) Math.min(length, ((long) this.length) - offset));
        }

        public String toString() {
            return "ByteSource.wrap(" + Ascii.truncate(BaseEncoding.base16().encode(this.bytes, this.offset, this.length), 30, "...") + ")";
        }
    }

    private static final class ConcatenatedByteSource extends ByteSource {
        final Iterable<? extends ByteSource> sources;

        ConcatenatedByteSource(Iterable<? extends ByteSource> sources) {
            this.sources = (Iterable) Preconditions.checkNotNull(sources);
        }

        public InputStream openStream() throws IOException {
            return new MultiInputStream(this.sources.iterator());
        }

        public boolean isEmpty() throws IOException {
            for (ByteSource source : this.sources) {
                if (!source.isEmpty()) {
                    return false;
                }
            }
            return true;
        }

        public Optional<Long> sizeIfKnown() {
            long result = 0;
            for (ByteSource source : this.sources) {
                Optional<Long> sizeIfKnown = source.sizeIfKnown();
                if (!sizeIfKnown.isPresent()) {
                    return Optional.absent();
                }
                result += ((Long) sizeIfKnown.get()).longValue();
            }
            return Optional.of(Long.valueOf(result));
        }

        public long size() throws IOException {
            long result = 0;
            for (ByteSource source : this.sources) {
                result += source.size();
            }
            return result;
        }

        public String toString() {
            return "ByteSource.concat(" + this.sources + ")";
        }
    }

    private static final class EmptyByteSource extends ByteArrayByteSource {
        static final EmptyByteSource INSTANCE = new EmptyByteSource();

        EmptyByteSource() {
            super(new byte[0]);
        }

        public CharSource asCharSource(Charset charset) {
            Preconditions.checkNotNull(charset);
            return CharSource.empty();
        }

        public byte[] read() {
            return this.bytes;
        }

        public String toString() {
            return "ByteSource.empty()";
        }
    }

    private final class SlicedByteSource extends ByteSource {
        final long length;
        final long offset;
        final /* synthetic */ ByteSource this$0;

        SlicedByteSource(ByteSource byteSource, long offset, long length) {
            boolean z;
            boolean z2 = true;
            this.this$0 = byteSource;
            if (offset >= 0) {
                z = true;
            } else {
                z = false;
            }
            Preconditions.checkArgument(z, "offset (%s) may not be negative", offset);
            if (length < 0) {
                z2 = false;
            }
            Preconditions.checkArgument(z2, "length (%s) may not be negative", length);
            this.offset = offset;
            this.length = length;
        }

        public InputStream openStream() throws IOException {
            return sliceStream(this.this$0.openStream());
        }

        public InputStream openBufferedStream() throws IOException {
            return sliceStream(this.this$0.openBufferedStream());
        }

        private InputStream sliceStream(InputStream in) throws IOException {
            Closer closer;
            if (this.offset > 0) {
                try {
                    if (ByteStreams.skipUpTo(in, this.offset) < this.offset) {
                        in.close();
                        return new ByteArrayInputStream(new byte[0]);
                    }
                } catch (Throwable th) {
                    closer.close();
                }
            }
            return ByteStreams.limit(in, this.length);
        }

        public ByteSource slice(long offset, long length) {
            boolean z;
            boolean z2 = true;
            if (offset >= 0) {
                z = true;
            } else {
                z = false;
            }
            Preconditions.checkArgument(z, "offset (%s) may not be negative", offset);
            if (length < 0) {
                z2 = false;
            }
            Preconditions.checkArgument(z2, "length (%s) may not be negative", length);
            return this.this$0.slice(this.offset + offset, Math.min(length, this.length - offset));
        }

        public boolean isEmpty() throws IOException {
            return this.length == 0 || super.isEmpty();
        }

        public Optional<Long> sizeIfKnown() {
            Optional<Long> optionalUnslicedSize = this.this$0.sizeIfKnown();
            if (!optionalUnslicedSize.isPresent()) {
                return Optional.absent();
            }
            long unslicedSize = ((Long) optionalUnslicedSize.get()).longValue();
            return Optional.of(Long.valueOf(Math.min(this.length, unslicedSize - Math.min(this.offset, unslicedSize))));
        }

        public String toString() {
            return this.this$0.toString() + ".slice(" + this.offset + ", " + this.length + ")";
        }
    }

    public abstract InputStream openStream() throws IOException;

    protected ByteSource() {
    }

    public CharSource asCharSource(Charset charset) {
        return new AsCharSource(charset);
    }

    public InputStream openBufferedStream() throws IOException {
        InputStream in = openStream();
        return in instanceof BufferedInputStream ? (BufferedInputStream) in : new BufferedInputStream(in);
    }

    public ByteSource slice(long offset, long length) {
        return new SlicedByteSource(this, offset, length);
    }

    public boolean isEmpty() throws IOException {
        Optional<Long> sizeIfKnown = sizeIfKnown();
        if (sizeIfKnown.isPresent() && ((Long) sizeIfKnown.get()).longValue() == 0) {
            return true;
        }
        Closer closer = Closer.create();
        try {
            boolean z = ((InputStream) closer.register(openStream())).read() == -1;
            closer.close();
            return z;
        } catch (Throwable th) {
            closer.close();
        }
    }

    @Beta
    public Optional<Long> sizeIfKnown() {
        return Optional.absent();
    }

    public long size() throws IOException {
        Optional<Long> sizeIfKnown = sizeIfKnown();
        if (sizeIfKnown.isPresent()) {
            return ((Long) sizeIfKnown.get()).longValue();
        }
        Closer closer = Closer.create();
        long countBySkipping;
        try {
            countBySkipping = countBySkipping((InputStream) closer.register(openStream()));
            closer.close();
            return countBySkipping;
        } catch (IOException e) {
            closer.close();
            closer = Closer.create();
            countBySkipping = ByteStreams.exhaust((InputStream) closer.register(openStream()));
            closer.close();
            return countBySkipping;
        } catch (Throwable e2) {
            try {
                RuntimeException rethrow = closer.rethrow(e2);
            } catch (Throwable th) {
                closer.close();
            }
        }
    }

    private long countBySkipping(InputStream in) throws IOException {
        long count = 0;
        while (true) {
            long skipped = ByteStreams.skipUpTo(in, 2147483647L);
            if (skipped <= 0) {
                return count;
            }
            count += skipped;
        }
    }

    @CanIgnoreReturnValue
    public long copyTo(OutputStream output) throws IOException {
        Preconditions.checkNotNull(output);
        Closer closer = Closer.create();
        try {
            long copy = ByteStreams.copy((InputStream) closer.register(openStream()), output);
            closer.close();
            return copy;
        } catch (Throwable th) {
            closer.close();
        }
    }

    @CanIgnoreReturnValue
    public long copyTo(ByteSink sink) throws IOException {
        Preconditions.checkNotNull(sink);
        Closer closer = Closer.create();
        try {
            long copy = ByteStreams.copy((InputStream) closer.register(openStream()), (OutputStream) closer.register(sink.openStream()));
            closer.close();
            return copy;
        } catch (Throwable th) {
            closer.close();
        }
    }

    public byte[] read() throws IOException {
        Closer closer = Closer.create();
        try {
            byte[] toByteArray = ByteStreams.toByteArray((InputStream) closer.register(openStream()));
            closer.close();
            return toByteArray;
        } catch (Throwable th) {
            closer.close();
        }
    }

    @CanIgnoreReturnValue
    @Beta
    public <T> T read(ByteProcessor<T> processor) throws IOException {
        Preconditions.checkNotNull(processor);
        Closer closer = Closer.create();
        try {
            T readBytes = ByteStreams.readBytes((InputStream) closer.register(openStream()), processor);
            closer.close();
            return readBytes;
        } catch (Throwable th) {
            closer.close();
        }
    }

    public HashCode hash(HashFunction hashFunction) throws IOException {
        Hasher hasher = hashFunction.newHasher();
        copyTo(Funnels.asOutputStream(hasher));
        return hasher.hash();
    }

    public boolean contentEquals(ByteSource other) throws IOException {
        Preconditions.checkNotNull(other);
        byte[] buf1 = ByteStreams.createBuffer();
        byte[] buf2 = ByteStreams.createBuffer();
        Closer closer = Closer.create();
        try {
            InputStream in1 = (InputStream) closer.register(openStream());
            InputStream in2 = (InputStream) closer.register(other.openStream());
            int read1;
            do {
                read1 = ByteStreams.read(in1, buf1, 0, buf1.length);
                if (read1 != ByteStreams.read(in2, buf2, 0, buf2.length) || !Arrays.equals(buf1, buf2)) {
                    closer.close();
                    return false;
                }
            } while (read1 == buf1.length);
            closer.close();
            return true;
        } catch (Throwable th) {
            closer.close();
        }
    }

    public static ByteSource concat(Iterable<? extends ByteSource> sources) {
        return new ConcatenatedByteSource(sources);
    }

    public static ByteSource concat(Iterator<? extends ByteSource> sources) {
        return concat(ImmutableList.copyOf((Iterator) sources));
    }

    public static ByteSource concat(ByteSource... sources) {
        return concat(ImmutableList.copyOf((Object[]) sources));
    }

    public static ByteSource wrap(byte[] b) {
        return new ByteArrayByteSource(b);
    }

    public static ByteSource empty() {
        return EmptyByteSource.INSTANCE;
    }
}

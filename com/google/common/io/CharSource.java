package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Ascii;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Iterator;
import javax.annotation.Nullable;

@GwtIncompatible
public abstract class CharSource {

    private final class AsByteSource extends ByteSource {
        final Charset charset;

        AsByteSource(Charset charset) {
            this.charset = (Charset) Preconditions.checkNotNull(charset);
        }

        public CharSource asCharSource(Charset charset) {
            if (charset.equals(this.charset)) {
                return CharSource.this;
            }
            return super.asCharSource(charset);
        }

        public InputStream openStream() throws IOException {
            return new ReaderInputStream(CharSource.this.openStream(), this.charset, 8192);
        }

        public String toString() {
            return CharSource.this.toString() + ".asByteSource(" + this.charset + ")";
        }
    }

    private static class CharSequenceCharSource extends CharSource {
        private static final Splitter LINE_SPLITTER = Splitter.onPattern("\r\n|\n|\r");
        private final CharSequence seq;

        class C07451 implements Iterable<String> {

            class C07441 extends AbstractIterator<String> {
                Iterator<String> lines = CharSequenceCharSource.LINE_SPLITTER.split(CharSequenceCharSource.this.seq).iterator();

                C07441() {
                }

                protected String computeNext() {
                    if (this.lines.hasNext()) {
                        String next = (String) this.lines.next();
                        if (this.lines.hasNext() || !next.isEmpty()) {
                            return next;
                        }
                    }
                    return (String) endOfData();
                }
            }

            C07451() {
            }

            public Iterator<String> iterator() {
                return new C07441();
            }
        }

        protected CharSequenceCharSource(CharSequence seq) {
            this.seq = (CharSequence) Preconditions.checkNotNull(seq);
        }

        public Reader openStream() {
            return new CharSequenceReader(this.seq);
        }

        public String read() {
            return this.seq.toString();
        }

        public boolean isEmpty() {
            return this.seq.length() == 0;
        }

        public long length() {
            return (long) this.seq.length();
        }

        public Optional<Long> lengthIfKnown() {
            return Optional.of(Long.valueOf((long) this.seq.length()));
        }

        private Iterable<String> lines() {
            return new C07451();
        }

        public String readFirstLine() {
            Iterator<String> lines = lines().iterator();
            return lines.hasNext() ? (String) lines.next() : null;
        }

        public ImmutableList<String> readLines() {
            return ImmutableList.copyOf(lines());
        }

        public <T> T readLines(LineProcessor<T> processor) throws IOException {
            for (String line : lines()) {
                if (!processor.processLine(line)) {
                    break;
                }
            }
            return processor.getResult();
        }

        public String toString() {
            return "CharSource.wrap(" + Ascii.truncate(this.seq, 30, "...") + ")";
        }
    }

    private static final class ConcatenatedCharSource extends CharSource {
        private final Iterable<? extends CharSource> sources;

        ConcatenatedCharSource(Iterable<? extends CharSource> sources) {
            this.sources = (Iterable) Preconditions.checkNotNull(sources);
        }

        public Reader openStream() throws IOException {
            return new MultiReader(this.sources.iterator());
        }

        public boolean isEmpty() throws IOException {
            for (CharSource source : this.sources) {
                if (!source.isEmpty()) {
                    return false;
                }
            }
            return true;
        }

        public Optional<Long> lengthIfKnown() {
            long result = 0;
            for (CharSource source : this.sources) {
                Optional<Long> lengthIfKnown = source.lengthIfKnown();
                if (!lengthIfKnown.isPresent()) {
                    return Optional.absent();
                }
                result += ((Long) lengthIfKnown.get()).longValue();
            }
            return Optional.of(Long.valueOf(result));
        }

        public long length() throws IOException {
            long result = 0;
            for (CharSource source : this.sources) {
                result += source.length();
            }
            return result;
        }

        public String toString() {
            return "CharSource.concat(" + this.sources + ")";
        }
    }

    private static final class EmptyCharSource extends CharSequenceCharSource {
        private static final EmptyCharSource INSTANCE = new EmptyCharSource();

        private EmptyCharSource() {
            super("");
        }

        public String toString() {
            return "CharSource.empty()";
        }
    }

    public abstract Reader openStream() throws IOException;

    protected CharSource() {
    }

    @Beta
    public ByteSource asByteSource(Charset charset) {
        return new AsByteSource(charset);
    }

    public BufferedReader openBufferedStream() throws IOException {
        Reader reader = openStream();
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
    }

    @Beta
    public Optional<Long> lengthIfKnown() {
        return Optional.absent();
    }

    @Beta
    public long length() throws IOException {
        Optional<Long> lengthIfKnown = lengthIfKnown();
        if (lengthIfKnown.isPresent()) {
            return ((Long) lengthIfKnown.get()).longValue();
        }
        Closer closer = Closer.create();
        try {
            long countBySkipping = countBySkipping((Reader) closer.register(openStream()));
            closer.close();
            return countBySkipping;
        } catch (Throwable th) {
            closer.close();
        }
    }

    private long countBySkipping(Reader reader) throws IOException {
        long count = 0;
        while (true) {
            long read = reader.skip(Long.MAX_VALUE);
            if (read == 0) {
                return count;
            }
            count += read;
        }
    }

    @CanIgnoreReturnValue
    public long copyTo(Appendable appendable) throws IOException {
        Preconditions.checkNotNull(appendable);
        Closer closer = Closer.create();
        try {
            long copy = CharStreams.copy((Reader) closer.register(openStream()), appendable);
            closer.close();
            return copy;
        } catch (Throwable th) {
            closer.close();
        }
    }

    @CanIgnoreReturnValue
    public long copyTo(CharSink sink) throws IOException {
        Preconditions.checkNotNull(sink);
        Closer closer = Closer.create();
        try {
            long copy = CharStreams.copy((Reader) closer.register(openStream()), (Writer) closer.register(sink.openStream()));
            closer.close();
            return copy;
        } catch (Throwable th) {
            closer.close();
        }
    }

    public String read() throws IOException {
        Closer closer = Closer.create();
        try {
            String charStreams = CharStreams.toString((Reader) closer.register(openStream()));
            closer.close();
            return charStreams;
        } catch (Throwable th) {
            closer.close();
        }
    }

    @Nullable
    public String readFirstLine() throws IOException {
        Closer closer = Closer.create();
        try {
            String readLine = ((BufferedReader) closer.register(openBufferedStream())).readLine();
            closer.close();
            return readLine;
        } catch (Throwable th) {
            closer.close();
        }
    }

    public ImmutableList<String> readLines() throws IOException {
        Closer closer = Closer.create();
        try {
            BufferedReader reader = (BufferedReader) closer.register(openBufferedStream());
            Collection result = Lists.newArrayList();
            while (true) {
                String line = reader.readLine();
                if (line != null) {
                    result.add(line);
                } else {
                    ImmutableList<String> copyOf = ImmutableList.copyOf(result);
                    closer.close();
                    return copyOf;
                }
            }
        } catch (Throwable th) {
            closer.close();
        }
    }

    @CanIgnoreReturnValue
    @Beta
    public <T> T readLines(LineProcessor<T> processor) throws IOException {
        Preconditions.checkNotNull(processor);
        Closer closer = Closer.create();
        try {
            T readLines = CharStreams.readLines((Reader) closer.register(openStream()), processor);
            closer.close();
            return readLines;
        } catch (Throwable th) {
            closer.close();
        }
    }

    public boolean isEmpty() throws IOException {
        Optional<Long> lengthIfKnown = lengthIfKnown();
        if (lengthIfKnown.isPresent() && ((Long) lengthIfKnown.get()).longValue() == 0) {
            return true;
        }
        Closer closer = Closer.create();
        try {
            boolean z = ((Reader) closer.register(openStream())).read() == -1;
            closer.close();
            return z;
        } catch (Throwable th) {
            closer.close();
        }
    }

    public static CharSource concat(Iterable<? extends CharSource> sources) {
        return new ConcatenatedCharSource(sources);
    }

    public static CharSource concat(Iterator<? extends CharSource> sources) {
        return concat(ImmutableList.copyOf((Iterator) sources));
    }

    public static CharSource concat(CharSource... sources) {
        return concat(ImmutableList.copyOf((Object[]) sources));
    }

    public static CharSource wrap(CharSequence charSequence) {
        return new CharSequenceCharSource(charSequence);
    }

    public static CharSource empty() {
        return EmptyCharSource.INSTANCE;
    }
}

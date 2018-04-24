package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.IOException;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible
public class Joiner {
    private final String separator;

    public static final class MapJoiner {
        private final Joiner joiner;
        private final String keyValueSeparator;

        private MapJoiner(Joiner joiner, String keyValueSeparator) {
            this.joiner = joiner;
            this.keyValueSeparator = (String) Preconditions.checkNotNull(keyValueSeparator);
        }

        @CanIgnoreReturnValue
        public <A extends Appendable> A appendTo(A appendable, Map<?, ?> map) throws IOException {
            return appendTo((Appendable) appendable, map.entrySet());
        }

        @CanIgnoreReturnValue
        public StringBuilder appendTo(StringBuilder builder, Map<?, ?> map) {
            return appendTo(builder, map.entrySet());
        }

        public String join(Map<?, ?> map) {
            return join(map.entrySet());
        }

        @CanIgnoreReturnValue
        @Beta
        public <A extends Appendable> A appendTo(A appendable, Iterable<? extends Entry<?, ?>> entries) throws IOException {
            return appendTo((Appendable) appendable, entries.iterator());
        }

        @CanIgnoreReturnValue
        @Beta
        public <A extends Appendable> A appendTo(A appendable, Iterator<? extends Entry<?, ?>> parts) throws IOException {
            Preconditions.checkNotNull(appendable);
            if (parts.hasNext()) {
                Entry<?, ?> entry = (Entry) parts.next();
                appendable.append(this.joiner.toString(entry.getKey()));
                appendable.append(this.keyValueSeparator);
                appendable.append(this.joiner.toString(entry.getValue()));
                while (parts.hasNext()) {
                    appendable.append(this.joiner.separator);
                    Entry<?, ?> e = (Entry) parts.next();
                    appendable.append(this.joiner.toString(e.getKey()));
                    appendable.append(this.keyValueSeparator);
                    appendable.append(this.joiner.toString(e.getValue()));
                }
            }
            return appendable;
        }

        @CanIgnoreReturnValue
        @Beta
        public StringBuilder appendTo(StringBuilder builder, Iterable<? extends Entry<?, ?>> entries) {
            return appendTo(builder, entries.iterator());
        }

        @CanIgnoreReturnValue
        @Beta
        public StringBuilder appendTo(StringBuilder builder, Iterator<? extends Entry<?, ?>> entries) {
            try {
                appendTo((Appendable) builder, (Iterator) entries);
                return builder;
            } catch (IOException impossible) {
                throw new AssertionError(impossible);
            }
        }

        @Beta
        public String join(Iterable<? extends Entry<?, ?>> entries) {
            return join(entries.iterator());
        }

        @Beta
        public String join(Iterator<? extends Entry<?, ?>> entries) {
            return appendTo(new StringBuilder(), (Iterator) entries).toString();
        }

        public MapJoiner useForNull(String nullText) {
            return new MapJoiner(this.joiner.useForNull(nullText), this.keyValueSeparator);
        }
    }

    public static Joiner on(String separator) {
        return new Joiner(separator);
    }

    public static Joiner on(char separator) {
        return new Joiner(String.valueOf(separator));
    }

    private Joiner(String separator) {
        this.separator = (String) Preconditions.checkNotNull(separator);
    }

    private Joiner(Joiner prototype) {
        this.separator = prototype.separator;
    }

    @CanIgnoreReturnValue
    public <A extends Appendable> A appendTo(A appendable, Iterable<?> parts) throws IOException {
        return appendTo((Appendable) appendable, parts.iterator());
    }

    @CanIgnoreReturnValue
    public <A extends Appendable> A appendTo(A appendable, Iterator<?> parts) throws IOException {
        Preconditions.checkNotNull(appendable);
        if (parts.hasNext()) {
            appendable.append(toString(parts.next()));
            while (parts.hasNext()) {
                appendable.append(this.separator);
                appendable.append(toString(parts.next()));
            }
        }
        return appendable;
    }

    @CanIgnoreReturnValue
    public final <A extends Appendable> A appendTo(A appendable, Object[] parts) throws IOException {
        return appendTo((Appendable) appendable, Arrays.asList(parts));
    }

    @CanIgnoreReturnValue
    public final <A extends Appendable> A appendTo(A appendable, @Nullable Object first, @Nullable Object second, Object... rest) throws IOException {
        return appendTo((Appendable) appendable, iterable(first, second, rest));
    }

    @CanIgnoreReturnValue
    public final StringBuilder appendTo(StringBuilder builder, Iterable<?> parts) {
        return appendTo(builder, parts.iterator());
    }

    @CanIgnoreReturnValue
    public final StringBuilder appendTo(StringBuilder builder, Iterator<?> parts) {
        try {
            appendTo((Appendable) builder, (Iterator) parts);
            return builder;
        } catch (IOException impossible) {
            throw new AssertionError(impossible);
        }
    }

    @CanIgnoreReturnValue
    public final StringBuilder appendTo(StringBuilder builder, Object[] parts) {
        return appendTo(builder, Arrays.asList(parts));
    }

    @CanIgnoreReturnValue
    public final StringBuilder appendTo(StringBuilder builder, @Nullable Object first, @Nullable Object second, Object... rest) {
        return appendTo(builder, iterable(first, second, rest));
    }

    public final String join(Iterable<?> parts) {
        return join(parts.iterator());
    }

    public final String join(Iterator<?> parts) {
        return appendTo(new StringBuilder(), (Iterator) parts).toString();
    }

    public final String join(Object[] parts) {
        return join(Arrays.asList(parts));
    }

    public final String join(@Nullable Object first, @Nullable Object second, Object... rest) {
        return join(iterable(first, second, rest));
    }

    public Joiner useForNull(final String nullText) {
        Preconditions.checkNotNull(nullText);
        return new Joiner(this) {
            CharSequence toString(@Nullable Object part) {
                return part == null ? nullText : Joiner.this.toString(part);
            }

            public Joiner useForNull(String nullText) {
                throw new UnsupportedOperationException("already specified useForNull");
            }

            public Joiner skipNulls() {
                throw new UnsupportedOperationException("already specified useForNull");
            }
        };
    }

    public Joiner skipNulls() {
        return new Joiner(this) {
            public <A extends Appendable> A appendTo(A appendable, Iterator<?> parts) throws IOException {
                Preconditions.checkNotNull(appendable, "appendable");
                Preconditions.checkNotNull(parts, "parts");
                while (parts.hasNext()) {
                    Object part = parts.next();
                    if (part != null) {
                        appendable.append(Joiner.this.toString(part));
                        break;
                    }
                }
                while (parts.hasNext()) {
                    part = parts.next();
                    if (part != null) {
                        appendable.append(Joiner.this.separator);
                        appendable.append(Joiner.this.toString(part));
                    }
                }
                return appendable;
            }

            public Joiner useForNull(String nullText) {
                throw new UnsupportedOperationException("already specified skipNulls");
            }

            public MapJoiner withKeyValueSeparator(String kvs) {
                throw new UnsupportedOperationException("can't use .skipNulls() with maps");
            }
        };
    }

    public MapJoiner withKeyValueSeparator(char keyValueSeparator) {
        return withKeyValueSeparator(String.valueOf(keyValueSeparator));
    }

    public MapJoiner withKeyValueSeparator(String keyValueSeparator) {
        return new MapJoiner(keyValueSeparator);
    }

    CharSequence toString(Object part) {
        Preconditions.checkNotNull(part);
        return part instanceof CharSequence ? (CharSequence) part : part.toString();
    }

    private static Iterable<Object> iterable(final Object first, final Object second, final Object[] rest) {
        Preconditions.checkNotNull(rest);
        return new AbstractList<Object>() {
            public int size() {
                return rest.length + 2;
            }

            public Object get(int index) {
                switch (index) {
                    case 0:
                        return first;
                    case 1:
                        return second;
                    default:
                        return rest[index - 2];
                }
            }
        };
    }
}

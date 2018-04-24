package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.Serializable;
import java.util.Iterator;
import javax.annotation.Nullable;

@GwtCompatible
public abstract class Converter<A, B> implements Function<A, B> {
    private final boolean handleNullAutomatically;
    @LazyInit
    private transient Converter<B, A> reverse;

    private static final class ConverterComposition<A, B, C> extends Converter<A, C> implements Serializable {
        private static final long serialVersionUID = 0;
        final Converter<A, B> first;
        final Converter<B, C> second;

        ConverterComposition(Converter<A, B> first, Converter<B, C> second) {
            this.first = first;
            this.second = second;
        }

        protected C doForward(A a) {
            throw new AssertionError();
        }

        protected A doBackward(C c) {
            throw new AssertionError();
        }

        @Nullable
        C correctedDoForward(@Nullable A a) {
            return this.second.correctedDoForward(this.first.correctedDoForward(a));
        }

        @Nullable
        A correctedDoBackward(@Nullable C c) {
            return this.first.correctedDoBackward(this.second.correctedDoBackward(c));
        }

        public boolean equals(@Nullable Object object) {
            if (!(object instanceof ConverterComposition)) {
                return false;
            }
            ConverterComposition<?, ?, ?> that = (ConverterComposition) object;
            if (this.first.equals(that.first) && this.second.equals(that.second)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return (this.first.hashCode() * 31) + this.second.hashCode();
        }

        public String toString() {
            return this.first + ".andThen(" + this.second + ")";
        }
    }

    private static final class FunctionBasedConverter<A, B> extends Converter<A, B> implements Serializable {
        private final Function<? super B, ? extends A> backwardFunction;
        private final Function<? super A, ? extends B> forwardFunction;

        private FunctionBasedConverter(Function<? super A, ? extends B> forwardFunction, Function<? super B, ? extends A> backwardFunction) {
            this.forwardFunction = (Function) Preconditions.checkNotNull(forwardFunction);
            this.backwardFunction = (Function) Preconditions.checkNotNull(backwardFunction);
        }

        protected B doForward(A a) {
            return this.forwardFunction.apply(a);
        }

        protected A doBackward(B b) {
            return this.backwardFunction.apply(b);
        }

        public boolean equals(@Nullable Object object) {
            if (!(object instanceof FunctionBasedConverter)) {
                return false;
            }
            FunctionBasedConverter<?, ?> that = (FunctionBasedConverter) object;
            if (this.forwardFunction.equals(that.forwardFunction) && this.backwardFunction.equals(that.backwardFunction)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return (this.forwardFunction.hashCode() * 31) + this.backwardFunction.hashCode();
        }

        public String toString() {
            return "Converter.from(" + this.forwardFunction + ", " + this.backwardFunction + ")";
        }
    }

    private static final class IdentityConverter<T> extends Converter<T, T> implements Serializable {
        static final IdentityConverter INSTANCE = new IdentityConverter();
        private static final long serialVersionUID = 0;

        private IdentityConverter() {
        }

        protected T doForward(T t) {
            return t;
        }

        protected T doBackward(T t) {
            return t;
        }

        public IdentityConverter<T> reverse() {
            return this;
        }

        <S> Converter<T, S> doAndThen(Converter<T, S> otherConverter) {
            return (Converter) Preconditions.checkNotNull(otherConverter, "otherConverter");
        }

        public String toString() {
            return "Converter.identity()";
        }

        private Object readResolve() {
            return INSTANCE;
        }
    }

    private static final class ReverseConverter<A, B> extends Converter<B, A> implements Serializable {
        private static final long serialVersionUID = 0;
        final Converter<A, B> original;

        ReverseConverter(Converter<A, B> original) {
            this.original = original;
        }

        protected A doForward(B b) {
            throw new AssertionError();
        }

        protected B doBackward(A a) {
            throw new AssertionError();
        }

        @Nullable
        A correctedDoForward(@Nullable B b) {
            return this.original.correctedDoBackward(b);
        }

        @Nullable
        B correctedDoBackward(@Nullable A a) {
            return this.original.correctedDoForward(a);
        }

        public Converter<A, B> reverse() {
            return this.original;
        }

        public boolean equals(@Nullable Object object) {
            if (!(object instanceof ReverseConverter)) {
                return false;
            }
            return this.original.equals(((ReverseConverter) object).original);
        }

        public int hashCode() {
            return this.original.hashCode() ^ -1;
        }

        public String toString() {
            return this.original + ".reverse()";
        }
    }

    protected abstract A doBackward(B b);

    protected abstract B doForward(A a);

    protected Converter() {
        this(true);
    }

    Converter(boolean handleNullAutomatically) {
        this.handleNullAutomatically = handleNullAutomatically;
    }

    @CanIgnoreReturnValue
    @Nullable
    public final B convert(@Nullable A a) {
        return correctedDoForward(a);
    }

    @Nullable
    B correctedDoForward(@Nullable A a) {
        if (this.handleNullAutomatically) {
            return a == null ? null : Preconditions.checkNotNull(doForward(a));
        } else {
            return doForward(a);
        }
    }

    @Nullable
    A correctedDoBackward(@Nullable B b) {
        if (this.handleNullAutomatically) {
            return b == null ? null : Preconditions.checkNotNull(doBackward(b));
        } else {
            return doBackward(b);
        }
    }

    @CanIgnoreReturnValue
    public Iterable<B> convertAll(final Iterable<? extends A> fromIterable) {
        Preconditions.checkNotNull(fromIterable, "fromIterable");
        return new Iterable<B>() {

            class C04031 implements Iterator<B> {
                private final Iterator<? extends A> fromIterator = fromIterable.iterator();

                C04031() {
                }

                public boolean hasNext() {
                    return this.fromIterator.hasNext();
                }

                public B next() {
                    return Converter.this.convert(this.fromIterator.next());
                }

                public void remove() {
                    this.fromIterator.remove();
                }
            }

            public Iterator<B> iterator() {
                return new C04031();
            }
        };
    }

    @CanIgnoreReturnValue
    public Converter<B, A> reverse() {
        Converter<B, A> converter = this.reverse;
        if (converter != null) {
            return converter;
        }
        converter = new ReverseConverter(this);
        this.reverse = converter;
        return converter;
    }

    public final <C> Converter<A, C> andThen(Converter<B, C> secondConverter) {
        return doAndThen(secondConverter);
    }

    <C> Converter<A, C> doAndThen(Converter<B, C> secondConverter) {
        return new ConverterComposition(this, (Converter) Preconditions.checkNotNull(secondConverter));
    }

    @CanIgnoreReturnValue
    @Deprecated
    @Nullable
    public final B apply(@Nullable A a) {
        return convert(a);
    }

    public boolean equals(@Nullable Object object) {
        return super.equals(object);
    }

    public static <A, B> Converter<A, B> from(Function<? super A, ? extends B> forwardFunction, Function<? super B, ? extends A> backwardFunction) {
        return new FunctionBasedConverter(forwardFunction, backwardFunction);
    }

    public static <T> Converter<T, T> identity() {
        return IdentityConverter.INSTANCE;
    }
}

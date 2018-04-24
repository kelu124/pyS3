package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Arrays;
import javax.annotation.Nullable;

@GwtCompatible
public final class Objects extends ExtraObjectsMethodsForWeb {

    @Deprecated
    public static final class ToStringHelper {
        private final String className;
        private final ValueHolder holderHead;
        private ValueHolder holderTail;
        private boolean omitNullValues;

        private static final class ValueHolder {
            String name;
            ValueHolder next;
            Object value;

            private ValueHolder() {
            }
        }

        private ToStringHelper(String className) {
            this.holderHead = new ValueHolder();
            this.holderTail = this.holderHead;
            this.omitNullValues = false;
            this.className = (String) Preconditions.checkNotNull(className);
        }

        @CanIgnoreReturnValue
        public ToStringHelper omitNullValues() {
            this.omitNullValues = true;
            return this;
        }

        @CanIgnoreReturnValue
        public ToStringHelper add(String name, @Nullable Object value) {
            return addHolder(name, value);
        }

        @CanIgnoreReturnValue
        public ToStringHelper add(String name, boolean value) {
            return addHolder(name, String.valueOf(value));
        }

        @CanIgnoreReturnValue
        public ToStringHelper add(String name, char value) {
            return addHolder(name, String.valueOf(value));
        }

        @CanIgnoreReturnValue
        public ToStringHelper add(String name, double value) {
            return addHolder(name, String.valueOf(value));
        }

        @CanIgnoreReturnValue
        public ToStringHelper add(String name, float value) {
            return addHolder(name, String.valueOf(value));
        }

        @CanIgnoreReturnValue
        public ToStringHelper add(String name, int value) {
            return addHolder(name, String.valueOf(value));
        }

        @CanIgnoreReturnValue
        public ToStringHelper add(String name, long value) {
            return addHolder(name, String.valueOf(value));
        }

        @CanIgnoreReturnValue
        public ToStringHelper addValue(@Nullable Object value) {
            return addHolder(value);
        }

        @CanIgnoreReturnValue
        public ToStringHelper addValue(boolean value) {
            return addHolder(String.valueOf(value));
        }

        @CanIgnoreReturnValue
        public ToStringHelper addValue(char value) {
            return addHolder(String.valueOf(value));
        }

        @CanIgnoreReturnValue
        public ToStringHelper addValue(double value) {
            return addHolder(String.valueOf(value));
        }

        @CanIgnoreReturnValue
        public ToStringHelper addValue(float value) {
            return addHolder(String.valueOf(value));
        }

        @CanIgnoreReturnValue
        public ToStringHelper addValue(int value) {
            return addHolder(String.valueOf(value));
        }

        @CanIgnoreReturnValue
        public ToStringHelper addValue(long value) {
            return addHolder(String.valueOf(value));
        }

        public String toString() {
            boolean omitNullValuesSnapshot = this.omitNullValues;
            String nextSeparator = "";
            StringBuilder builder = new StringBuilder(32).append(this.className).append('{');
            ValueHolder valueHolder = this.holderHead.next;
            while (valueHolder != null) {
                if (!omitNullValuesSnapshot || valueHolder.value != null) {
                    builder.append(nextSeparator);
                    nextSeparator = ", ";
                    if (valueHolder.name != null) {
                        builder.append(valueHolder.name).append('=');
                    }
                    builder.append(valueHolder.value);
                }
                valueHolder = valueHolder.next;
            }
            return builder.append('}').toString();
        }

        private ValueHolder addHolder() {
            ValueHolder valueHolder = new ValueHolder();
            this.holderTail.next = valueHolder;
            this.holderTail = valueHolder;
            return valueHolder;
        }

        private ToStringHelper addHolder(@Nullable Object value) {
            addHolder().value = value;
            return this;
        }

        private ToStringHelper addHolder(String name, @Nullable Object value) {
            ValueHolder valueHolder = addHolder();
            valueHolder.value = value;
            valueHolder.name = (String) Preconditions.checkNotNull(name);
            return this;
        }
    }

    private Objects() {
    }

    public static boolean equal(@Nullable Object a, @Nullable Object b) {
        return a == b || (a != null && a.equals(b));
    }

    public static int hashCode(@Nullable Object... objects) {
        return Arrays.hashCode(objects);
    }

    @Deprecated
    public static ToStringHelper toStringHelper(Object self) {
        return new ToStringHelper(self.getClass().getSimpleName());
    }

    @Deprecated
    public static ToStringHelper toStringHelper(Class<?> clazz) {
        return new ToStringHelper(clazz.getSimpleName());
    }

    @Deprecated
    public static ToStringHelper toStringHelper(String className) {
        return new ToStringHelper(className);
    }

    @Deprecated
    public static <T> T firstNonNull(@Nullable T first, @Nullable T second) {
        return MoreObjects.firstNonNull(first, second);
    }
}

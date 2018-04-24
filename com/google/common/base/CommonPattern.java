package com.google.common.base;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
abstract class CommonPattern {
    public abstract boolean equals(Object obj);

    abstract int flags();

    public abstract int hashCode();

    abstract CommonMatcher matcher(CharSequence charSequence);

    abstract String pattern();

    public abstract String toString();

    CommonPattern() {
    }
}

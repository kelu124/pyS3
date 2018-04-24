package com.google.common.base;

import com.google.common.annotations.GwtIncompatible;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@GwtIncompatible
final class JdkPattern extends CommonPattern implements Serializable {
    private static final long serialVersionUID = 0;
    private final Pattern pattern;

    private static final class JdkMatcher extends CommonMatcher {
        final Matcher matcher;

        JdkMatcher(Matcher matcher) {
            this.matcher = (Matcher) Preconditions.checkNotNull(matcher);
        }

        boolean matches() {
            return this.matcher.matches();
        }

        boolean find() {
            return this.matcher.find();
        }

        boolean find(int index) {
            return this.matcher.find(index);
        }

        String replaceAll(String replacement) {
            return this.matcher.replaceAll(replacement);
        }

        int end() {
            return this.matcher.end();
        }

        int start() {
            return this.matcher.start();
        }
    }

    JdkPattern(Pattern pattern) {
        this.pattern = (Pattern) Preconditions.checkNotNull(pattern);
    }

    CommonMatcher matcher(CharSequence t) {
        return new JdkMatcher(this.pattern.matcher(t));
    }

    String pattern() {
        return this.pattern.pattern();
    }

    int flags() {
        return this.pattern.flags();
    }

    public String toString() {
        return this.pattern.toString();
    }

    public int hashCode() {
        return this.pattern.hashCode();
    }

    public boolean equals(Object o) {
        if (o instanceof JdkPattern) {
            return this.pattern.equals(((JdkPattern) o).pattern);
        }
        return false;
    }
}

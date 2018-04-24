package com.google.common.escape;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;

@GwtCompatible
@Beta
public final class Escapers {
    private static final Escaper NULL_ESCAPER = new C06861();

    static class C06861 extends CharEscaper {
        C06861() {
        }

        public String escape(String string) {
            return (String) Preconditions.checkNotNull(string);
        }

        protected char[] escape(char c) {
            return null;
        }
    }

    @Beta
    public static final class Builder {
        private final Map<Character, String> replacementMap;
        private char safeMax;
        private char safeMin;
        private String unsafeReplacement;

        private Builder() {
            this.replacementMap = new HashMap();
            this.safeMin = '\u0000';
            this.safeMax = '￿';
            this.unsafeReplacement = null;
        }

        @CanIgnoreReturnValue
        public Builder setSafeRange(char safeMin, char safeMax) {
            this.safeMin = safeMin;
            this.safeMax = safeMax;
            return this;
        }

        @CanIgnoreReturnValue
        public Builder setUnsafeReplacement(@Nullable String unsafeReplacement) {
            this.unsafeReplacement = unsafeReplacement;
            return this;
        }

        @CanIgnoreReturnValue
        public Builder addEscape(char c, String replacement) {
            Preconditions.checkNotNull(replacement);
            this.replacementMap.put(Character.valueOf(c), replacement);
            return this;
        }

        public Escaper build() {
            return new ArrayBasedCharEscaper(this.replacementMap, this.safeMin, this.safeMax) {
                private final char[] replacementChars;

                protected char[] escapeUnsafe(char c) {
                    return this.replacementChars;
                }
            };
        }
    }

    private Escapers() {
    }

    public static Escaper nullEscaper() {
        return NULL_ESCAPER;
    }

    public static Builder builder() {
        return new Builder();
    }

    static UnicodeEscaper asUnicodeEscaper(Escaper escaper) {
        Preconditions.checkNotNull(escaper);
        if (escaper instanceof UnicodeEscaper) {
            return (UnicodeEscaper) escaper;
        }
        if (escaper instanceof CharEscaper) {
            return wrap((CharEscaper) escaper);
        }
        throw new IllegalArgumentException("Cannot create a UnicodeEscaper from: " + escaper.getClass().getName());
    }

    public static String computeReplacement(CharEscaper escaper, char c) {
        return stringOrNull(escaper.escape(c));
    }

    public static String computeReplacement(UnicodeEscaper escaper, int cp) {
        return stringOrNull(escaper.escape(cp));
    }

    private static String stringOrNull(char[] in) {
        return in == null ? null : new String(in);
    }

    private static UnicodeEscaper wrap(final CharEscaper escaper) {
        return new UnicodeEscaper() {
            protected char[] escape(int cp) {
                if (cp < 65536) {
                    return escaper.escape((char) cp);
                }
                char[] surrogateChars = new char[2];
                Character.toChars(cp, surrogateChars, 0);
                char[] hiChars = escaper.escape(surrogateChars[0]);
                char[] loChars = escaper.escape(surrogateChars[1]);
                if (hiChars == null && loChars == null) {
                    return null;
                }
                int hiCount;
                int loCount;
                int n;
                if (hiChars != null) {
                    hiCount = hiChars.length;
                } else {
                    hiCount = 1;
                }
                if (loChars != null) {
                    loCount = loChars.length;
                } else {
                    loCount = 1;
                }
                char[] output = new char[(hiCount + loCount)];
                if (hiChars != null) {
                    for (n = 0; n < hiChars.length; n++) {
                        output[n] = hiChars[n];
                    }
                } else {
                    output[0] = surrogateChars[0];
                }
                if (loChars != null) {
                    for (n = 0; n < loChars.length; n++) {
                        output[hiCount + n] = loChars[n];
                    }
                    return output;
                }
                output[hiCount] = surrogateChars[1];
                return output;
            }
        };
    }
}

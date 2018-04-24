package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import java.util.Arrays;
import java.util.BitSet;
import org.apache.poi.ss.util.IEEEDouble;

@GwtCompatible(emulated = true)
public abstract class CharMatcher implements Predicate<Character> {
    @Deprecated
    public static final CharMatcher ANY = any();
    @Deprecated
    public static final CharMatcher ASCII = ascii();
    @Deprecated
    public static final CharMatcher BREAKING_WHITESPACE = breakingWhitespace();
    @Deprecated
    public static final CharMatcher DIGIT = digit();
    private static final int DISTINCT_CHARS = 65536;
    @Deprecated
    public static final CharMatcher INVISIBLE = invisible();
    @Deprecated
    public static final CharMatcher JAVA_DIGIT = javaDigit();
    @Deprecated
    public static final CharMatcher JAVA_ISO_CONTROL = javaIsoControl();
    @Deprecated
    public static final CharMatcher JAVA_LETTER = javaLetter();
    @Deprecated
    public static final CharMatcher JAVA_LETTER_OR_DIGIT = javaLetterOrDigit();
    @Deprecated
    public static final CharMatcher JAVA_LOWER_CASE = javaLowerCase();
    @Deprecated
    public static final CharMatcher JAVA_UPPER_CASE = javaUpperCase();
    @Deprecated
    public static final CharMatcher NONE = none();
    @Deprecated
    public static final CharMatcher SINGLE_WIDTH = singleWidth();
    @Deprecated
    public static final CharMatcher WHITESPACE = whitespace();

    private static class Negated extends CharMatcher {
        final CharMatcher original;

        public /* bridge */ /* synthetic */ boolean apply(Object x0) {
            return super.apply((Character) x0);
        }

        Negated(CharMatcher original) {
            this.original = (CharMatcher) Preconditions.checkNotNull(original);
        }

        public boolean matches(char c) {
            return !this.original.matches(c);
        }

        public boolean matchesAllOf(CharSequence sequence) {
            return this.original.matchesNoneOf(sequence);
        }

        public boolean matchesNoneOf(CharSequence sequence) {
            return this.original.matchesAllOf(sequence);
        }

        public int countIn(CharSequence sequence) {
            return sequence.length() - this.original.countIn(sequence);
        }

        @GwtIncompatible
        void setBits(BitSet table) {
            BitSet tmp = new BitSet();
            this.original.setBits(tmp);
            tmp.flip(0, 65536);
            table.or(tmp);
        }

        public CharMatcher negate() {
            return this.original;
        }

        public String toString() {
            return this.original + ".negate()";
        }
    }

    static class NegatedFastMatcher extends Negated {
        NegatedFastMatcher(CharMatcher original) {
            super(original);
        }

        public final CharMatcher precomputed() {
            return this;
        }
    }

    private static final class And extends CharMatcher {
        final CharMatcher first;
        final CharMatcher second;

        public /* bridge */ /* synthetic */ boolean apply(Object x0) {
            return super.apply((Character) x0);
        }

        And(CharMatcher a, CharMatcher b) {
            this.first = (CharMatcher) Preconditions.checkNotNull(a);
            this.second = (CharMatcher) Preconditions.checkNotNull(b);
        }

        public boolean matches(char c) {
            return this.first.matches(c) && this.second.matches(c);
        }

        @GwtIncompatible
        void setBits(BitSet table) {
            BitSet tmp1 = new BitSet();
            this.first.setBits(tmp1);
            BitSet tmp2 = new BitSet();
            this.second.setBits(tmp2);
            tmp1.and(tmp2);
            table.or(tmp1);
        }

        public String toString() {
            return "CharMatcher.and(" + this.first + ", " + this.second + ")";
        }
    }

    static abstract class FastMatcher extends CharMatcher {
        FastMatcher() {
        }

        public /* bridge */ /* synthetic */ boolean apply(Object x0) {
            return super.apply((Character) x0);
        }

        public final CharMatcher precomputed() {
            return this;
        }

        public CharMatcher negate() {
            return new NegatedFastMatcher(this);
        }
    }

    static abstract class NamedFastMatcher extends FastMatcher {
        private final String description;

        NamedFastMatcher(String description) {
            this.description = (String) Preconditions.checkNotNull(description);
        }

        public final String toString() {
            return this.description;
        }
    }

    private static final class Any extends NamedFastMatcher {
        static final Any INSTANCE = new Any();

        private Any() {
            super("CharMatcher.any()");
        }

        public boolean matches(char c) {
            return true;
        }

        public int indexIn(CharSequence sequence) {
            return sequence.length() == 0 ? -1 : 0;
        }

        public int indexIn(CharSequence sequence, int start) {
            int length = sequence.length();
            Preconditions.checkPositionIndex(start, length);
            return start == length ? -1 : start;
        }

        public int lastIndexIn(CharSequence sequence) {
            return sequence.length() - 1;
        }

        public boolean matchesAllOf(CharSequence sequence) {
            Preconditions.checkNotNull(sequence);
            return true;
        }

        public boolean matchesNoneOf(CharSequence sequence) {
            return sequence.length() == 0;
        }

        public String removeFrom(CharSequence sequence) {
            Preconditions.checkNotNull(sequence);
            return "";
        }

        public String replaceFrom(CharSequence sequence, char replacement) {
            char[] array = new char[sequence.length()];
            Arrays.fill(array, replacement);
            return new String(array);
        }

        public String replaceFrom(CharSequence sequence, CharSequence replacement) {
            StringBuilder result = new StringBuilder(sequence.length() * replacement.length());
            for (int i = 0; i < sequence.length(); i++) {
                result.append(replacement);
            }
            return result.toString();
        }

        public String collapseFrom(CharSequence sequence, char replacement) {
            return sequence.length() == 0 ? "" : String.valueOf(replacement);
        }

        public String trimFrom(CharSequence sequence) {
            Preconditions.checkNotNull(sequence);
            return "";
        }

        public int countIn(CharSequence sequence) {
            return sequence.length();
        }

        public CharMatcher and(CharMatcher other) {
            return (CharMatcher) Preconditions.checkNotNull(other);
        }

        public CharMatcher or(CharMatcher other) {
            Preconditions.checkNotNull(other);
            return this;
        }

        public CharMatcher negate() {
            return CharMatcher.none();
        }
    }

    private static final class AnyOf extends CharMatcher {
        private final char[] chars;

        public /* bridge */ /* synthetic */ boolean apply(Object x0) {
            return super.apply((Character) x0);
        }

        public AnyOf(CharSequence chars) {
            this.chars = chars.toString().toCharArray();
            Arrays.sort(this.chars);
        }

        public boolean matches(char c) {
            return Arrays.binarySearch(this.chars, c) >= 0;
        }

        @GwtIncompatible
        void setBits(BitSet table) {
            for (char c : this.chars) {
                table.set(c);
            }
        }

        public String toString() {
            StringBuilder description = new StringBuilder("CharMatcher.anyOf(\"");
            for (char c : this.chars) {
                description.append(CharMatcher.showCharacter(c));
            }
            description.append("\")");
            return description.toString();
        }
    }

    private static final class Ascii extends NamedFastMatcher {
        static final Ascii INSTANCE = new Ascii();

        Ascii() {
            super("CharMatcher.ascii()");
        }

        public boolean matches(char c) {
            return c <= Ascii.MAX;
        }
    }

    @GwtIncompatible
    private static final class BitSetMatcher extends NamedFastMatcher {
        private final BitSet table;

        private BitSetMatcher(BitSet table, String description) {
            super(description);
            if (table.length() + 64 < table.size()) {
                table = (BitSet) table.clone();
            }
            this.table = table;
        }

        public boolean matches(char c) {
            return this.table.get(c);
        }

        void setBits(BitSet bitSet) {
            bitSet.or(this.table);
        }
    }

    private static final class BreakingWhitespace extends CharMatcher {
        static final CharMatcher INSTANCE = new BreakingWhitespace();

        private BreakingWhitespace() {
        }

        public /* bridge */ /* synthetic */ boolean apply(Object x0) {
            return super.apply((Character) x0);
        }

        public boolean matches(char c) {
            switch (c) {
                case '\t':
                case '\n':
                case '\u000b':
                case '\f':
                case '\r':
                case ' ':
                case '':
                case ' ':
                case ' ':
                case ' ':
                case ' ':
                case '　':
                    return true;
                case ' ':
                    return false;
                default:
                    if (c < ' ' || c > ' ') {
                        return false;
                    }
                    return true;
            }
        }

        public String toString() {
            return "CharMatcher.breakingWhitespace()";
        }
    }

    private static class RangesMatcher extends CharMatcher {
        private final String description;
        private final char[] rangeEnds;
        private final char[] rangeStarts;

        public /* bridge */ /* synthetic */ boolean apply(Object x0) {
            return super.apply((Character) x0);
        }

        RangesMatcher(String description, char[] rangeStarts, char[] rangeEnds) {
            boolean z;
            this.description = description;
            this.rangeStarts = rangeStarts;
            this.rangeEnds = rangeEnds;
            if (rangeStarts.length == rangeEnds.length) {
                z = true;
            } else {
                z = false;
            }
            Preconditions.checkArgument(z);
            for (int i = 0; i < rangeStarts.length; i++) {
                if (rangeStarts[i] <= rangeEnds[i]) {
                    z = true;
                } else {
                    z = false;
                }
                Preconditions.checkArgument(z);
                if (i + 1 < rangeStarts.length) {
                    if (rangeEnds[i] < rangeStarts[i + 1]) {
                        z = true;
                    } else {
                        z = false;
                    }
                    Preconditions.checkArgument(z);
                }
            }
        }

        public boolean matches(char c) {
            int index = Arrays.binarySearch(this.rangeStarts, c);
            if (index >= 0) {
                return true;
            }
            index = (index ^ -1) - 1;
            if (index < 0 || c > this.rangeEnds[index]) {
                return false;
            }
            return true;
        }

        public String toString() {
            return this.description;
        }
    }

    private static final class Digit extends RangesMatcher {
        static final Digit INSTANCE = new Digit();
        private static final String ZEROES = "0٠۰߀०০੦૦୦௦౦೦൦๐໐༠၀႐០᠐᥆᧐᭐᮰᱀᱐꘠꣐꤀꩐０";

        private static char[] zeroes() {
            return ZEROES.toCharArray();
        }

        private static char[] nines() {
            char[] nines = new char[ZEROES.length()];
            for (int i = 0; i < ZEROES.length(); i++) {
                nines[i] = (char) (ZEROES.charAt(i) + 9);
            }
            return nines;
        }

        private Digit() {
            super("CharMatcher.digit()", zeroes(), nines());
        }
    }

    private static final class ForPredicate extends CharMatcher {
        private final Predicate<? super Character> predicate;

        ForPredicate(Predicate<? super Character> predicate) {
            this.predicate = (Predicate) Preconditions.checkNotNull(predicate);
        }

        public boolean matches(char c) {
            return this.predicate.apply(Character.valueOf(c));
        }

        public boolean apply(Character character) {
            return this.predicate.apply(Preconditions.checkNotNull(character));
        }

        public String toString() {
            return "CharMatcher.forPredicate(" + this.predicate + ")";
        }
    }

    private static final class InRange extends FastMatcher {
        private final char endInclusive;
        private final char startInclusive;

        InRange(char startInclusive, char endInclusive) {
            Preconditions.checkArgument(endInclusive >= startInclusive);
            this.startInclusive = startInclusive;
            this.endInclusive = endInclusive;
        }

        public boolean matches(char c) {
            return this.startInclusive <= c && c <= this.endInclusive;
        }

        @GwtIncompatible
        void setBits(BitSet table) {
            table.set(this.startInclusive, this.endInclusive + 1);
        }

        public String toString() {
            return "CharMatcher.inRange('" + CharMatcher.showCharacter(this.startInclusive) + "', '" + CharMatcher.showCharacter(this.endInclusive) + "')";
        }
    }

    private static final class Invisible extends RangesMatcher {
        static final Invisible INSTANCE = new Invisible();
        private static final String RANGE_ENDS = "  ­؄؜۝܏ ᠎‏ ⁤⁦⁧⁨⁩⁯　﻿￹￻";
        private static final String RANGE_STARTS = "\u0000­؀؜۝܏ ᠎   ⁦⁧⁨⁩⁪　?﻿￹￺";

        private Invisible() {
            super("CharMatcher.invisible()", RANGE_STARTS.toCharArray(), RANGE_ENDS.toCharArray());
        }
    }

    private static final class Is extends FastMatcher {
        private final char match;

        Is(char match) {
            this.match = match;
        }

        public boolean matches(char c) {
            return c == this.match;
        }

        public String replaceFrom(CharSequence sequence, char replacement) {
            return sequence.toString().replace(this.match, replacement);
        }

        public CharMatcher and(CharMatcher other) {
            return other.matches(this.match) ? this : CharMatcher.none();
        }

        public CharMatcher or(CharMatcher other) {
            return other.matches(this.match) ? other : super.or(other);
        }

        public CharMatcher negate() {
            return CharMatcher.isNot(this.match);
        }

        @GwtIncompatible
        void setBits(BitSet table) {
            table.set(this.match);
        }

        public String toString() {
            return "CharMatcher.is('" + CharMatcher.showCharacter(this.match) + "')";
        }
    }

    private static final class IsEither extends FastMatcher {
        private final char match1;
        private final char match2;

        IsEither(char match1, char match2) {
            this.match1 = match1;
            this.match2 = match2;
        }

        public boolean matches(char c) {
            return c == this.match1 || c == this.match2;
        }

        @GwtIncompatible
        void setBits(BitSet table) {
            table.set(this.match1);
            table.set(this.match2);
        }

        public String toString() {
            return "CharMatcher.anyOf(\"" + CharMatcher.showCharacter(this.match1) + CharMatcher.showCharacter(this.match2) + "\")";
        }
    }

    private static final class IsNot extends FastMatcher {
        private final char match;

        IsNot(char match) {
            this.match = match;
        }

        public boolean matches(char c) {
            return c != this.match;
        }

        public CharMatcher and(CharMatcher other) {
            return other.matches(this.match) ? super.and(other) : other;
        }

        public CharMatcher or(CharMatcher other) {
            return other.matches(this.match) ? CharMatcher.any() : this;
        }

        @GwtIncompatible
        void setBits(BitSet table) {
            table.set(0, this.match);
            table.set(this.match + 1, 65536);
        }

        public CharMatcher negate() {
            return CharMatcher.is(this.match);
        }

        public String toString() {
            return "CharMatcher.isNot('" + CharMatcher.showCharacter(this.match) + "')";
        }
    }

    private static final class JavaDigit extends CharMatcher {
        static final JavaDigit INSTANCE = new JavaDigit();

        private JavaDigit() {
        }

        public /* bridge */ /* synthetic */ boolean apply(Object x0) {
            return super.apply((Character) x0);
        }

        public boolean matches(char c) {
            return Character.isDigit(c);
        }

        public String toString() {
            return "CharMatcher.javaDigit()";
        }
    }

    private static final class JavaIsoControl extends NamedFastMatcher {
        static final JavaIsoControl INSTANCE = new JavaIsoControl();

        private JavaIsoControl() {
            super("CharMatcher.javaIsoControl()");
        }

        public boolean matches(char c) {
            return c <= '\u001f' || (c >= Ascii.MAX && c <= '');
        }
    }

    private static final class JavaLetter extends CharMatcher {
        static final JavaLetter INSTANCE = new JavaLetter();

        private JavaLetter() {
        }

        public /* bridge */ /* synthetic */ boolean apply(Object x0) {
            return super.apply((Character) x0);
        }

        public boolean matches(char c) {
            return Character.isLetter(c);
        }

        public String toString() {
            return "CharMatcher.javaLetter()";
        }
    }

    private static final class JavaLetterOrDigit extends CharMatcher {
        static final JavaLetterOrDigit INSTANCE = new JavaLetterOrDigit();

        private JavaLetterOrDigit() {
        }

        public /* bridge */ /* synthetic */ boolean apply(Object x0) {
            return super.apply((Character) x0);
        }

        public boolean matches(char c) {
            return Character.isLetterOrDigit(c);
        }

        public String toString() {
            return "CharMatcher.javaLetterOrDigit()";
        }
    }

    private static final class JavaLowerCase extends CharMatcher {
        static final JavaLowerCase INSTANCE = new JavaLowerCase();

        private JavaLowerCase() {
        }

        public /* bridge */ /* synthetic */ boolean apply(Object x0) {
            return super.apply((Character) x0);
        }

        public boolean matches(char c) {
            return Character.isLowerCase(c);
        }

        public String toString() {
            return "CharMatcher.javaLowerCase()";
        }
    }

    private static final class JavaUpperCase extends CharMatcher {
        static final JavaUpperCase INSTANCE = new JavaUpperCase();

        private JavaUpperCase() {
        }

        public /* bridge */ /* synthetic */ boolean apply(Object x0) {
            return super.apply((Character) x0);
        }

        public boolean matches(char c) {
            return Character.isUpperCase(c);
        }

        public String toString() {
            return "CharMatcher.javaUpperCase()";
        }
    }

    private static final class None extends NamedFastMatcher {
        static final None INSTANCE = new None();

        private None() {
            super("CharMatcher.none()");
        }

        public boolean matches(char c) {
            return false;
        }

        public int indexIn(CharSequence sequence) {
            Preconditions.checkNotNull(sequence);
            return -1;
        }

        public int indexIn(CharSequence sequence, int start) {
            Preconditions.checkPositionIndex(start, sequence.length());
            return -1;
        }

        public int lastIndexIn(CharSequence sequence) {
            Preconditions.checkNotNull(sequence);
            return -1;
        }

        public boolean matchesAllOf(CharSequence sequence) {
            return sequence.length() == 0;
        }

        public boolean matchesNoneOf(CharSequence sequence) {
            Preconditions.checkNotNull(sequence);
            return true;
        }

        public String removeFrom(CharSequence sequence) {
            return sequence.toString();
        }

        public String replaceFrom(CharSequence sequence, char replacement) {
            return sequence.toString();
        }

        public String replaceFrom(CharSequence sequence, CharSequence replacement) {
            Preconditions.checkNotNull(replacement);
            return sequence.toString();
        }

        public String collapseFrom(CharSequence sequence, char replacement) {
            return sequence.toString();
        }

        public String trimFrom(CharSequence sequence) {
            return sequence.toString();
        }

        public String trimLeadingFrom(CharSequence sequence) {
            return sequence.toString();
        }

        public String trimTrailingFrom(CharSequence sequence) {
            return sequence.toString();
        }

        public int countIn(CharSequence sequence) {
            Preconditions.checkNotNull(sequence);
            return 0;
        }

        public CharMatcher and(CharMatcher other) {
            Preconditions.checkNotNull(other);
            return this;
        }

        public CharMatcher or(CharMatcher other) {
            return (CharMatcher) Preconditions.checkNotNull(other);
        }

        public CharMatcher negate() {
            return CharMatcher.any();
        }
    }

    private static final class Or extends CharMatcher {
        final CharMatcher first;
        final CharMatcher second;

        public /* bridge */ /* synthetic */ boolean apply(Object x0) {
            return super.apply((Character) x0);
        }

        Or(CharMatcher a, CharMatcher b) {
            this.first = (CharMatcher) Preconditions.checkNotNull(a);
            this.second = (CharMatcher) Preconditions.checkNotNull(b);
        }

        @GwtIncompatible
        void setBits(BitSet table) {
            this.first.setBits(table);
            this.second.setBits(table);
        }

        public boolean matches(char c) {
            return this.first.matches(c) || this.second.matches(c);
        }

        public String toString() {
            return "CharMatcher.or(" + this.first + ", " + this.second + ")";
        }
    }

    private static final class SingleWidth extends RangesMatcher {
        static final SingleWidth INSTANCE = new SingleWidth();

        private SingleWidth() {
            super("CharMatcher.singleWidth()", "\u0000־א׳؀ݐ฀Ḁ℀ﭐﹰ｡".toCharArray(), "ӹ־ת״ۿݿ๿₯℺﷿﻿ￜ".toCharArray());
        }
    }

    @VisibleForTesting
    static final class Whitespace extends NamedFastMatcher {
        static final Whitespace INSTANCE = new Whitespace();
        static final int MULTIPLIER = 1682554634;
        static final int SHIFT = Integer.numberOfLeadingZeros(TABLE.length() - 1);
        static final String TABLE = " 　\r   　 \u000b　   　 \t     \f 　 　　 \n 　";

        Whitespace() {
            super("CharMatcher.whitespace()");
        }

        public boolean matches(char c) {
            return TABLE.charAt((MULTIPLIER * c) >>> SHIFT) == c;
        }

        @GwtIncompatible
        void setBits(BitSet table) {
            for (int i = 0; i < TABLE.length(); i++) {
                table.set(TABLE.charAt(i));
            }
        }
    }

    public abstract boolean matches(char c);

    public static CharMatcher any() {
        return Any.INSTANCE;
    }

    public static CharMatcher none() {
        return None.INSTANCE;
    }

    public static CharMatcher whitespace() {
        return Whitespace.INSTANCE;
    }

    public static CharMatcher breakingWhitespace() {
        return BreakingWhitespace.INSTANCE;
    }

    public static CharMatcher ascii() {
        return Ascii.INSTANCE;
    }

    public static CharMatcher digit() {
        return Digit.INSTANCE;
    }

    public static CharMatcher javaDigit() {
        return JavaDigit.INSTANCE;
    }

    public static CharMatcher javaLetter() {
        return JavaLetter.INSTANCE;
    }

    public static CharMatcher javaLetterOrDigit() {
        return JavaLetterOrDigit.INSTANCE;
    }

    public static CharMatcher javaUpperCase() {
        return JavaUpperCase.INSTANCE;
    }

    public static CharMatcher javaLowerCase() {
        return JavaLowerCase.INSTANCE;
    }

    public static CharMatcher javaIsoControl() {
        return JavaIsoControl.INSTANCE;
    }

    public static CharMatcher invisible() {
        return Invisible.INSTANCE;
    }

    public static CharMatcher singleWidth() {
        return SingleWidth.INSTANCE;
    }

    public static CharMatcher is(char match) {
        return new Is(match);
    }

    public static CharMatcher isNot(char match) {
        return new IsNot(match);
    }

    public static CharMatcher anyOf(CharSequence sequence) {
        switch (sequence.length()) {
            case 0:
                return none();
            case 1:
                return is(sequence.charAt(0));
            case 2:
                return isEither(sequence.charAt(0), sequence.charAt(1));
            default:
                return new AnyOf(sequence);
        }
    }

    public static CharMatcher noneOf(CharSequence sequence) {
        return anyOf(sequence).negate();
    }

    public static CharMatcher inRange(char startInclusive, char endInclusive) {
        return new InRange(startInclusive, endInclusive);
    }

    public static CharMatcher forPredicate(Predicate<? super Character> predicate) {
        return predicate instanceof CharMatcher ? (CharMatcher) predicate : new ForPredicate(predicate);
    }

    protected CharMatcher() {
    }

    public CharMatcher negate() {
        return new Negated(this);
    }

    public CharMatcher and(CharMatcher other) {
        return new And(this, other);
    }

    public CharMatcher or(CharMatcher other) {
        return new Or(this, other);
    }

    public CharMatcher precomputed() {
        return Platform.precomputeCharMatcher(this);
    }

    @GwtIncompatible
    CharMatcher precomputedInternal() {
        BitSet table = new BitSet();
        setBits(table);
        int totalCharacters = table.cardinality();
        if (totalCharacters * 2 <= 65536) {
            return precomputedPositive(totalCharacters, table, toString());
        }
        table.flip(0, 65536);
        int negatedCharacters = 65536 - totalCharacters;
        String suffix = ".negate()";
        final String description = toString();
        return new NegatedFastMatcher(precomputedPositive(negatedCharacters, table, description.endsWith(suffix) ? description.substring(0, description.length() - suffix.length()) : description + suffix)) {
            public String toString() {
                return description;
            }
        };
    }

    @GwtIncompatible
    private static CharMatcher precomputedPositive(int totalCharacters, BitSet table, String description) {
        switch (totalCharacters) {
            case 0:
                return none();
            case 1:
                return is((char) table.nextSetBit(0));
            case 2:
                char c1 = (char) table.nextSetBit(0);
                return isEither(c1, (char) table.nextSetBit(c1 + 1));
            default:
                if (isSmall(totalCharacters, table.length())) {
                    return SmallCharMatcher.from(table, description);
                }
                return new BitSetMatcher(table, description);
        }
    }

    @GwtIncompatible
    private static boolean isSmall(int totalCharacters, int tableLength) {
        return totalCharacters <= IEEEDouble.EXPONENT_BIAS && tableLength > (totalCharacters * 4) * 16;
    }

    @GwtIncompatible
    void setBits(BitSet table) {
        for (int c = 65535; c >= 0; c--) {
            if (matches((char) c)) {
                table.set(c);
            }
        }
    }

    public boolean matchesAnyOf(CharSequence sequence) {
        return !matchesNoneOf(sequence);
    }

    public boolean matchesAllOf(CharSequence sequence) {
        for (int i = sequence.length() - 1; i >= 0; i--) {
            if (!matches(sequence.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public boolean matchesNoneOf(CharSequence sequence) {
        return indexIn(sequence) == -1;
    }

    public int indexIn(CharSequence sequence) {
        return indexIn(sequence, 0);
    }

    public int indexIn(CharSequence sequence, int start) {
        int length = sequence.length();
        Preconditions.checkPositionIndex(start, length);
        for (int i = start; i < length; i++) {
            if (matches(sequence.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    public int lastIndexIn(CharSequence sequence) {
        for (int i = sequence.length() - 1; i >= 0; i--) {
            if (matches(sequence.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    public int countIn(CharSequence sequence) {
        int count = 0;
        for (int i = 0; i < sequence.length(); i++) {
            if (matches(sequence.charAt(i))) {
                count++;
            }
        }
        return count;
    }

    public String removeFrom(CharSequence sequence) {
        String string = sequence.toString();
        int pos = indexIn(string);
        if (pos == -1) {
            return string;
        }
        char[] chars = string.toCharArray();
        int spread = 1;
        while (true) {
            pos++;
            while (pos != chars.length) {
                if (matches(chars[pos])) {
                    spread++;
                } else {
                    chars[pos - spread] = chars[pos];
                    pos++;
                }
            }
            return new String(chars, 0, pos - spread);
        }
    }

    public String retainFrom(CharSequence sequence) {
        return negate().removeFrom(sequence);
    }

    public String replaceFrom(CharSequence sequence, char replacement) {
        String string = sequence.toString();
        int pos = indexIn(string);
        if (pos == -1) {
            return string;
        }
        char[] chars = string.toCharArray();
        chars[pos] = replacement;
        for (int i = pos + 1; i < chars.length; i++) {
            if (matches(chars[i])) {
                chars[i] = replacement;
            }
        }
        return new String(chars);
    }

    public String replaceFrom(CharSequence sequence, CharSequence replacement) {
        int replacementLen = replacement.length();
        if (replacementLen == 0) {
            return removeFrom(sequence);
        }
        if (replacementLen == 1) {
            return replaceFrom(sequence, replacement.charAt(0));
        }
        String string = sequence.toString();
        int pos = indexIn(string);
        if (pos == -1) {
            return string;
        }
        int len = string.length();
        StringBuilder buf = new StringBuilder(((len * 3) / 2) + 16);
        int oldpos = 0;
        do {
            buf.append(string, oldpos, pos);
            buf.append(replacement);
            oldpos = pos + 1;
            pos = indexIn(string, oldpos);
        } while (pos != -1);
        buf.append(string, oldpos, len);
        return buf.toString();
    }

    public String trimFrom(CharSequence sequence) {
        int len = sequence.length();
        int first = 0;
        while (first < len && matches(sequence.charAt(first))) {
            first++;
        }
        int last = len - 1;
        while (last > first && matches(sequence.charAt(last))) {
            last--;
        }
        return sequence.subSequence(first, last + 1).toString();
    }

    public String trimLeadingFrom(CharSequence sequence) {
        int len = sequence.length();
        for (int first = 0; first < len; first++) {
            if (!matches(sequence.charAt(first))) {
                return sequence.subSequence(first, len).toString();
            }
        }
        return "";
    }

    public String trimTrailingFrom(CharSequence sequence) {
        for (int last = sequence.length() - 1; last >= 0; last--) {
            if (!matches(sequence.charAt(last))) {
                return sequence.subSequence(0, last + 1).toString();
            }
        }
        return "";
    }

    public String collapseFrom(CharSequence sequence, char replacement) {
        int len = sequence.length();
        int i = 0;
        while (i < len) {
            char c = sequence.charAt(i);
            if (matches(c)) {
                if (c != replacement || (i != len - 1 && matches(sequence.charAt(i + 1)))) {
                    return finishCollapseFrom(sequence, i + 1, len, replacement, new StringBuilder(len).append(sequence, 0, i).append(replacement), true);
                }
                i++;
            }
            i++;
        }
        return sequence.toString();
    }

    public String trimAndCollapseFrom(CharSequence sequence, char replacement) {
        int len = sequence.length();
        int first = 0;
        int last = len - 1;
        while (first < len && matches(sequence.charAt(first))) {
            first++;
        }
        while (last > first && matches(sequence.charAt(last))) {
            last--;
        }
        if (first == 0 && last == len - 1) {
            return collapseFrom(sequence, replacement);
        }
        return finishCollapseFrom(sequence, first, last + 1, replacement, new StringBuilder((last + 1) - first), false);
    }

    private String finishCollapseFrom(CharSequence sequence, int start, int end, char replacement, StringBuilder builder, boolean inMatchingGroup) {
        for (int i = start; i < end; i++) {
            char c = sequence.charAt(i);
            if (!matches(c)) {
                builder.append(c);
                inMatchingGroup = false;
            } else if (!inMatchingGroup) {
                builder.append(replacement);
                inMatchingGroup = true;
            }
        }
        return builder.toString();
    }

    @Deprecated
    public boolean apply(Character character) {
        return matches(character.charValue());
    }

    public String toString() {
        return super.toString();
    }

    private static String showCharacter(char c) {
        String hex = "0123456789ABCDEF";
        char[] tmp = new char[]{'\\', 'u', '\u0000', '\u0000', '\u0000', '\u0000'};
        for (int i = 0; i < 4; i++) {
            tmp[5 - i] = hex.charAt(c & 15);
            c = (char) (c >> 4);
        }
        return String.copyValueOf(tmp);
    }

    private static IsEither isEither(char c1, char c2) {
        return new IsEither(c1, c2);
    }
}

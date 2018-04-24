package com.itextpdf.text.factories;

import com.itextpdf.text.pdf.Barcode128;

public class RomanNumberFactory {
    private static final RomanDigit[] roman = new RomanDigit[]{new RomanDigit('m', 1000, false), new RomanDigit(Barcode128.CODE_AC_TO_B, 500, false), new RomanDigit(Barcode128.CODE_AB_TO_C, 100, true), new RomanDigit('l', 50, false), new RomanDigit('x', 10, true), new RomanDigit('v', 5, false), new RomanDigit(Barcode128.START_C, 1, true)};

    private static class RomanDigit {
        public char digit;
        public boolean pre;
        public int value;

        RomanDigit(char digit, int value, boolean pre) {
            this.digit = digit;
            this.value = value;
            this.pre = pre;
        }
    }

    public static final String getString(int index) {
        StringBuffer buf = new StringBuffer();
        if (index < 0) {
            buf.append('-');
            index = -index;
        }
        if (index > 3000) {
            buf.append('|');
            buf.append(getString(index / 1000));
            buf.append('|');
            index -= (index / 1000) * 1000;
        }
        int pos = 0;
        while (true) {
            RomanDigit dig = roman[pos];
            while (index >= dig.value) {
                buf.append(dig.digit);
                index -= dig.value;
            }
            if (index <= 0) {
                return buf.toString();
            }
            int j = pos;
            do {
                j++;
            } while (!roman[j].pre);
            if (roman[j].value + index >= dig.value) {
                buf.append(roman[j].digit).append(dig.digit);
                index -= dig.value - roman[j].value;
            }
            pos++;
        }
    }

    public static final String getLowerCaseString(int index) {
        return getString(index);
    }

    public static final String getUpperCaseString(int index) {
        return getString(index).toUpperCase();
    }

    public static final String getString(int index, boolean lowercase) {
        if (lowercase) {
            return getLowerCaseString(index);
        }
        return getUpperCaseString(index);
    }
}

package com.itextpdf.text.factories;

import com.itextpdf.text.SpecialSymbol;

public class GreekAlphabetFactory {
    public static final String getString(int index) {
        return getString(index, true);
    }

    public static final String getLowerCaseString(int index) {
        return getString(index);
    }

    public static final String getUpperCaseString(int index) {
        return getString(index).toUpperCase();
    }

    public static final String getString(int index, boolean lowercase) {
        if (index < 1) {
            return "";
        }
        index--;
        int bytes = 1;
        int start = 0;
        for (int symbols = 24; index >= symbols + start; symbols *= 24) {
            bytes++;
            start += symbols;
        }
        int c = index - start;
        char[] value = new char[bytes];
        while (bytes > 0) {
            bytes--;
            value[bytes] = (char) (c % 24);
            if (value[bytes] > '\u0010') {
                value[bytes] = (char) (value[bytes] + 1);
            }
            value[bytes] = (char) ((lowercase ? 945 : 913) + value[bytes]);
            value[bytes] = SpecialSymbol.getCorrespondingSymbol(value[bytes]);
            c /= 24;
        }
        return String.valueOf(value);
    }
}

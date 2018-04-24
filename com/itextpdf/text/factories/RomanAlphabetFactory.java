package com.itextpdf.text.factories;

import com.itextpdf.text.error_messages.MessageLocalization;

public class RomanAlphabetFactory {
    public static final String getString(int index) {
        if (index < 1) {
            throw new NumberFormatException(MessageLocalization.getComposedMessage("you.can.t.translate.a.negative.number.into.an.alphabetical.value", new Object[0]));
        }
        index--;
        int bytes = 1;
        int start = 0;
        for (int symbols = 26; index >= symbols + start; symbols *= 26) {
            bytes++;
            start += symbols;
        }
        int c = index - start;
        char[] value = new char[bytes];
        while (bytes > 0) {
            bytes--;
            value[bytes] = (char) ((c % 26) + 97);
            c /= 26;
        }
        return new String(value);
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

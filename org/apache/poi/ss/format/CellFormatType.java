package org.apache.poi.ss.format;

import com.google.common.base.Ascii;

public enum CellFormatType {
    GENERAL {
        CellFormatter formatter(String pattern) {
            return new CellGeneralFormatter();
        }

        boolean isSpecial(char ch) {
            return false;
        }
    },
    NUMBER {
        boolean isSpecial(char ch) {
            return false;
        }

        CellFormatter formatter(String pattern) {
            return new CellNumberFormatter(pattern);
        }
    },
    DATE {
        boolean isSpecial(char ch) {
            return ch == '\'' || (ch <= Ascii.MAX && Character.isLetter(ch));
        }

        CellFormatter formatter(String pattern) {
            return new CellDateFormatter(pattern);
        }
    },
    ELAPSED {
        boolean isSpecial(char ch) {
            return false;
        }

        CellFormatter formatter(String pattern) {
            return new CellElapsedFormatter(pattern);
        }
    },
    TEXT {
        boolean isSpecial(char ch) {
            return false;
        }

        CellFormatter formatter(String pattern) {
            return new CellTextFormatter(pattern);
        }
    };

    abstract CellFormatter formatter(String str);

    abstract boolean isSpecial(char c);
}

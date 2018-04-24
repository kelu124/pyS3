package org.apache.poi.sl.usermodel;

import java.util.Locale;

public enum AutoNumberingScheme {
    alphaLcParenBoth(8, 1),
    alphaUcParenBoth(10, 2),
    alphaLcParenRight(9, 3),
    alphaUcParenRight(11, 4),
    alphaLcPeriod(0, 5),
    alphaUcPeriod(1, 6),
    arabicParenBoth(12, 7),
    arabicParenRight(2, 8),
    arabicPeriod(3, 9),
    arabicPlain(13, 10),
    romanLcParenBoth(4, 11),
    romanUcParenBoth(14, 12),
    romanLcParenRight(5, 13),
    romanUcParenRight(15, 14),
    romanLcPeriod(6, 15),
    romanUcPeriod(7, 16),
    circleNumDbPlain(18, 17),
    circleNumWdBlackPlain(20, 18),
    circleNumWdWhitePlain(19, 19),
    arabicDbPeriod(29, 20),
    arabicDbPlain(28, 21),
    ea1ChsPeriod(17, 22),
    ea1ChsPlain(16, 23),
    ea1ChtPeriod(21, 24),
    ea1ChtPlain(20, 25),
    ea1JpnChsDbPeriod(38, 26),
    ea1JpnKorPlain(26, 27),
    ea1JpnKorPeriod(27, 28),
    arabic1Minus(23, 29),
    arabic2Minus(24, 30),
    hebrew2Minus(25, 31),
    thaiAlphaPeriod(30, 32),
    thaiAlphaParenRight(31, 33),
    thaiAlphaParenBoth(32, 34),
    thaiNumPeriod(33, 35),
    thaiNumParenRight(34, 36),
    thaiNumParenBoth(35, 37),
    hindiAlphaPeriod(36, 38),
    hindiNumPeriod(37, 39),
    hindiNumParenRight(39, 40),
    hindiAlpha1Period(39, 41);
    
    private static final String ALPHA_LIST = "abcdefghijklmnopqrstuvwxyz";
    private static final String ARABIC_LIST = "0123456789";
    private static final String CIRCLE_DB_LIST = "❶❷❸❹❺❻❼❽❾";
    private static final String WINGDINGS_BLACK_LIST = "";
    private static final String WINGDINGS_WHITE_LIST = "";
    public final int nativeId;
    public final int ooxmlId;

    private AutoNumberingScheme(int nativeId, int ooxmlId) {
        this.nativeId = nativeId;
        this.ooxmlId = ooxmlId;
    }

    public static AutoNumberingScheme forNativeID(int nativeId) {
        for (AutoNumberingScheme ans : values()) {
            if (ans.nativeId == nativeId) {
                return ans;
            }
        }
        return null;
    }

    public static AutoNumberingScheme forOoxmlID(int ooxmlId) {
        for (AutoNumberingScheme ans : values()) {
            if (ans.ooxmlId == ooxmlId) {
                return ans;
            }
        }
        return null;
    }

    public String getDescription() {
        switch (this) {
            case alphaLcPeriod:
                return "Lowercase Latin character followed by a period. Example: a., b., c., ...";
            case alphaUcPeriod:
                return "Uppercase Latin character followed by a period. Example: A., B., C., ...";
            case arabicParenRight:
                return "Arabic numeral followed by a closing parenthesis. Example: 1), 2), 3), ...";
            case arabicPeriod:
                return "Arabic numeral followed by a period. Example: 1., 2., 3., ...";
            case romanLcParenBoth:
                return "Lowercase Roman numeral enclosed in parentheses. Example: (i), (ii), (iii), ...";
            case romanLcParenRight:
                return "Lowercase Roman numeral followed by a closing parenthesis. Example: i), ii), iii), ...";
            case romanLcPeriod:
                return "Lowercase Roman numeral followed by a period. Example: i., ii., iii., ...";
            case romanUcPeriod:
                return "Uppercase Roman numeral followed by a period. Example: I., II., III., ...";
            case alphaLcParenBoth:
                return "Lowercase alphabetic character enclosed in parentheses. Example: (a), (b), (c), ...";
            case alphaLcParenRight:
                return "Lowercase alphabetic character followed by a closing parenthesis. Example: a), b), c), ...";
            case alphaUcParenBoth:
                return "Uppercase alphabetic character enclosed in parentheses. Example: (A), (B), (C), ...";
            case alphaUcParenRight:
                return "Uppercase alphabetic character followed by a closing parenthesis. Example: A), B), C), ...";
            case arabicParenBoth:
                return "Arabic numeral enclosed in parentheses. Example: (1), (2), (3), ...";
            case arabicPlain:
                return "Arabic numeral. Example: 1, 2, 3, ...";
            case romanUcParenBoth:
                return "Uppercase Roman numeral enclosed in parentheses. Example: (I), (II), (III), ...";
            case romanUcParenRight:
                return "Uppercase Roman numeral followed by a closing parenthesis. Example: I), II), III), ...";
            case ea1ChsPlain:
                return "Simplified Chinese.";
            case ea1ChsPeriod:
                return "Simplified Chinese with single-byte period.";
            case circleNumDbPlain:
                return "Double byte circle numbers.";
            case circleNumWdWhitePlain:
                return "Wingdings white circle numbers.";
            case circleNumWdBlackPlain:
                return "Wingdings black circle numbers.";
            case ea1ChtPlain:
                return "Traditional Chinese.";
            case ea1ChtPeriod:
                return "Traditional Chinese with single-byte period.";
            case arabic1Minus:
                return "Bidi Arabic 1 (AraAlpha) with ANSI minus symbol.";
            case arabic2Minus:
                return "Bidi Arabic 2 (AraAbjad) with ANSI minus symbol.";
            case hebrew2Minus:
                return "Bidi Hebrew 2 with ANSI minus symbol.";
            case ea1JpnKorPlain:
                return "Japanese/Korean.";
            case ea1JpnKorPeriod:
                return "Japanese/Korean with single-byte period.";
            case arabicDbPlain:
                return "Double-byte Arabic numbers.";
            case arabicDbPeriod:
                return "Double-byte Arabic numbers with double-byte period.";
            case thaiAlphaPeriod:
                return "Thai alphabetic character followed by a period.";
            case thaiAlphaParenRight:
                return "Thai alphabetic character followed by a closing parenthesis.";
            case thaiAlphaParenBoth:
                return "Thai alphabetic character enclosed by parentheses.";
            case thaiNumPeriod:
                return "Thai numeral followed by a period.";
            case thaiNumParenRight:
                return "Thai numeral followed by a closing parenthesis.";
            case thaiNumParenBoth:
                return "Thai numeral enclosed in parentheses.";
            case hindiAlphaPeriod:
                return "Hindi alphabetic character followed by a period.";
            case hindiNumPeriod:
                return "Hindi numeric character followed by a period.";
            case ea1JpnChsDbPeriod:
                return "Japanese with double-byte period.";
            case hindiNumParenRight:
                return "Hindi numeric character followed by a closing parenthesis.";
            case hindiAlpha1Period:
                return "Hindi alphabetic character followed by a period.";
            default:
                return "Unknown Numbered Scheme";
        }
    }

    public String format(int value) {
        return formatSeperator(formatCase(formatIndex(value)));
    }

    private String formatSeperator(String cased) {
        String name = name().toLowerCase(Locale.ROOT);
        if (name.contains("plain")) {
            return cased;
        }
        if (name.contains("parenright")) {
            return cased + ")";
        }
        if (name.contains("parenboth")) {
            return "(" + cased + ")";
        }
        if (name.contains("period")) {
            return cased + ".";
        }
        if (name.contains("minus")) {
            return cased + "-";
        }
        return cased;
    }

    private String formatCase(String index) {
        String name = name().toLowerCase(Locale.ROOT);
        if (name.contains("lc")) {
            return index.toLowerCase(Locale.ROOT);
        }
        if (name.contains("uc")) {
            return index.toUpperCase(Locale.ROOT);
        }
        return index;
    }

    private String formatIndex(int value) {
        String name = name().toLowerCase(Locale.ROOT);
        if (name.startsWith("roman")) {
            return formatRomanIndex(value);
        }
        if (name.startsWith("arabic") && !name.contains("db")) {
            return getIndexedList(value, ARABIC_LIST, false);
        }
        if (name.startsWith("alpha")) {
            return getIndexedList(value, ALPHA_LIST, true);
        }
        if (name.contains("WdWhite")) {
            return value == 10 ? "" : getIndexedList(value, WINGDINGS_WHITE_LIST, false);
        } else {
            if (name.contains("WdBlack")) {
                return value == 10 ? "" : getIndexedList(value, WINGDINGS_BLACK_LIST, false);
            } else {
                if (name.contains("NumDb")) {
                    return value == 10 ? "❿" : getIndexedList(value, CIRCLE_DB_LIST, true);
                } else {
                    return "?";
                }
            }
        }
    }

    private static String getIndexedList(int val, String list, boolean oneBased) {
        StringBuilder sb = new StringBuilder();
        addIndexedChar(val, list, oneBased, sb);
        return sb.toString();
    }

    private static void addIndexedChar(int val, String list, boolean oneBased, StringBuilder sb) {
        if (oneBased) {
            val--;
        }
        int len = list.length();
        if (val >= len) {
            addIndexedChar(val / len, list, oneBased, sb);
        }
        sb.append(list.charAt(val % len));
    }

    private String formatRomanIndex(int value) {
        int[] VALUES = new int[]{1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] ROMAN = new String[]{"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        conciseList = new String[24][];
        conciseList[0] = new String[]{"XLV", "VL"};
        conciseList[1] = new String[]{"XCV", "VC"};
        conciseList[2] = new String[]{"CDL", "LD"};
        conciseList[3] = new String[]{"CML", "LM"};
        conciseList[4] = new String[]{"CMVC", "LMVL"};
        conciseList[5] = new String[]{"CDXC", "LDXL"};
        conciseList[6] = new String[]{"CDVC", "LDVL"};
        conciseList[7] = new String[]{"CMXC", "LMXL"};
        conciseList[8] = new String[]{"XCIX", "VCIV"};
        conciseList[9] = new String[]{"XLIX", "VLIV"};
        conciseList[10] = new String[]{"XLIX", "IL"};
        conciseList[11] = new String[]{"XCIX", "IC"};
        conciseList[12] = new String[]{"CDXC", "XD"};
        conciseList[13] = new String[]{"CDVC", "XDV"};
        conciseList[14] = new String[]{"CDIC", "XDIX"};
        conciseList[15] = new String[]{"LMVL", "XMV"};
        conciseList[16] = new String[]{"CMIC", "XMIX"};
        conciseList[17] = new String[]{"CMXC", "XM"};
        conciseList[18] = new String[]{"XDV", "VD"};
        conciseList[19] = new String[]{"XDIX", "VDIV"};
        conciseList[20] = new String[]{"XMV", "VM"};
        conciseList[21] = new String[]{"XMIX", "VMIV"};
        conciseList[22] = new String[]{"VDIV", "ID"};
        conciseList[23] = new String[]{"VMIV", "IM"};
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 13; i++) {
            while (value >= VALUES[i]) {
                value -= VALUES[i];
                sb.append(ROMAN[i]);
            }
        }
        String result = sb.toString();
        for (String[] cc : conciseList) {
            result = result.replace(cc[0], cc[1]);
        }
        return result;
    }
}

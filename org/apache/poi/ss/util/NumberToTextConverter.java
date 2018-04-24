package org.apache.poi.ss.util;

public final class NumberToTextConverter {
    private static final long EXCEL_NAN_BITS = -276939487313920L;
    private static final int MAX_TEXT_LEN = 20;

    private NumberToTextConverter() {
    }

    public static String toText(double value) {
        return rawDoubleBitsToText(Double.doubleToLongBits(value));
    }

    static String rawDoubleBitsToText(long pRawBits) {
        long rawBits = pRawBits;
        boolean isNegative = rawBits < 0;
        if (isNegative) {
            rawBits &= Long.MAX_VALUE;
        }
        if (rawBits != 0) {
            ExpandedDouble ed = new ExpandedDouble(rawBits);
            if (ed.getBinaryExponent() < -1022) {
                return isNegative ? "-0" : "0";
            } else {
                if (ed.getBinaryExponent() == 1024) {
                    if (rawBits == EXCEL_NAN_BITS) {
                        return "3.484840871308E+308";
                    }
                    isNegative = false;
                }
                NormalisedDecimal nd = ed.normaliseBaseTen();
                StringBuilder sb = new StringBuilder(21);
                if (isNegative) {
                    sb.append('-');
                }
                convertToText(sb, nd);
                return sb.toString();
            }
        } else if (isNegative) {
            return "-0";
        } else {
            return "0";
        }
    }

    private static void convertToText(StringBuilder sb, NormalisedDecimal pnd) {
        String decimalDigits;
        NormalisedDecimal rnd = pnd.roundUnits();
        int decExponent = rnd.getDecimalExponent();
        if (Math.abs(decExponent) > 98) {
            decimalDigits = rnd.getSignificantDecimalDigitsLastDigitRounded();
            if (decimalDigits.length() == 16) {
                decExponent++;
            }
        } else {
            decimalDigits = rnd.getSignificantDecimalDigits();
        }
        int countSigDigits = countSignifantDigits(decimalDigits);
        if (decExponent < 0) {
            formatLessThanOne(sb, decimalDigits, decExponent, countSigDigits);
        } else {
            formatGreaterThanOne(sb, decimalDigits, decExponent, countSigDigits);
        }
    }

    private static void formatLessThanOne(StringBuilder sb, String decimalDigits, int decExponent, int countSigDigits) {
        int nLeadingZeros = (-decExponent) - 1;
        if (needsScientificNotation((nLeadingZeros + 2) + countSigDigits)) {
            sb.append(decimalDigits.charAt(0));
            if (countSigDigits > 1) {
                sb.append('.');
                sb.append(decimalDigits.subSequence(1, countSigDigits));
            }
            sb.append("E-");
            appendExp(sb, -decExponent);
            return;
        }
        sb.append("0.");
        for (int i = nLeadingZeros; i > 0; i--) {
            sb.append('0');
        }
        sb.append(decimalDigits.subSequence(0, countSigDigits));
    }

    private static void formatGreaterThanOne(StringBuilder sb, String decimalDigits, int decExponent, int countSigDigits) {
        if (decExponent > 19) {
            sb.append(decimalDigits.charAt(0));
            if (countSigDigits > 1) {
                sb.append('.');
                sb.append(decimalDigits.subSequence(1, countSigDigits));
            }
            sb.append("E+");
            appendExp(sb, decExponent);
            return;
        }
        int nFractionalDigits = (countSigDigits - decExponent) - 1;
        if (nFractionalDigits > 0) {
            sb.append(decimalDigits.subSequence(0, decExponent + 1));
            sb.append('.');
            sb.append(decimalDigits.subSequence(decExponent + 1, countSigDigits));
            return;
        }
        sb.append(decimalDigits.subSequence(0, countSigDigits));
        for (int i = -nFractionalDigits; i > 0; i--) {
            sb.append('0');
        }
    }

    private static boolean needsScientificNotation(int nDigits) {
        return nDigits > 20;
    }

    private static int countSignifantDigits(String sb) {
        int result = sb.length() - 1;
        while (sb.charAt(result) == '0') {
            result--;
            if (result < 0) {
                throw new RuntimeException("No non-zero digits found");
            }
        }
        return result + 1;
    }

    private static void appendExp(StringBuilder sb, int val) {
        if (val < 10) {
            sb.append('0');
            sb.append((char) (val + 48));
            return;
        }
        sb.append(val);
    }
}

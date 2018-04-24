package org.apache.poi.ss.formula.functions;

public class BaseNumberUtils {
    public static double convertToDecimal(String value, int base, int maxNumberOfPlaces) throws IllegalArgumentException {
        if (value == null || value.length() == 0) {
            return 0.0d;
        }
        long stringLength = (long) value.length();
        if (stringLength > ((long) maxNumberOfPlaces)) {
            throw new IllegalArgumentException();
        }
        double decimalValue = 0.0d;
        long signedDigit = 0;
        boolean hasSignedDigit = true;
        char[] arr$ = value.toCharArray();
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            long digit;
            char character = arr$[i$];
            if ('0' <= character && character <= '9') {
                digit = (long) (character - 48);
            } else if ('A' <= character && character <= 'Z') {
                digit = (long) ((character - 65) + 10);
            } else if ('a' > character || character > 'z') {
                digit = (long) base;
            } else {
                digit = (long) ((character - 97) + 10);
            }
            if (digit < ((long) base)) {
                if (hasSignedDigit) {
                    hasSignedDigit = false;
                    signedDigit = digit;
                }
                decimalValue = (((double) base) * decimalValue) + ((double) digit);
                i$++;
            } else {
                throw new IllegalArgumentException("character not allowed");
            }
        }
        boolean isNegative = !hasSignedDigit && stringLength == ((long) maxNumberOfPlaces) && signedDigit >= ((long) (base / 2));
        if (isNegative) {
            return getTwoComplement((double) base, (double) maxNumberOfPlaces, decimalValue) * -1.0d;
        }
        return decimalValue;
    }

    private static double getTwoComplement(double base, double maxNumberOfPlaces, double decimalValue) {
        return Math.pow(base, maxNumberOfPlaces) - decimalValue;
    }
}

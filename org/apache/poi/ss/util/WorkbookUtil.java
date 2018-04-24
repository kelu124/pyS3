package org.apache.poi.ss.util;

public class WorkbookUtil {
    public static final String createSafeSheetName(String nameProposal) {
        return createSafeSheetName(nameProposal, ' ');
    }

    public static final String createSafeSheetName(String nameProposal, char replaceChar) {
        if (nameProposal == null) {
            return "null";
        }
        if (nameProposal.length() < 1) {
            return "empty";
        }
        int length = Math.min(31, nameProposal.length());
        StringBuilder result = new StringBuilder(nameProposal.substring(0, length));
        int i = 0;
        while (i < length) {
            switch (result.charAt(i)) {
                case '\u0000':
                case '\u0003':
                case '*':
                case '/':
                case ':':
                case '?':
                case '[':
                case '\\':
                case ']':
                    result.setCharAt(i, replaceChar);
                    break;
                case '\'':
                    if (i != 0 && i != length - 1) {
                        break;
                    }
                    result.setCharAt(i, replaceChar);
                    break;
                    break;
                default:
                    break;
            }
            i++;
        }
        return result.toString();
    }

    public static void validateSheetName(String sheetName) {
        if (sheetName == null) {
            throw new IllegalArgumentException("sheetName must not be null");
        }
        int len = sheetName.length();
        if (len < 1 || len > 31) {
            throw new IllegalArgumentException("sheetName '" + sheetName + "' is invalid - character count MUST be greater than or equal to 1 and less than or equal to 31");
        }
        int i = 0;
        while (i < len) {
            char ch = sheetName.charAt(i);
            switch (ch) {
                case '*':
                case '/':
                case ':':
                case '?':
                case '[':
                case '\\':
                case ']':
                    throw new IllegalArgumentException("Invalid char (" + ch + ") found at index (" + i + ") in sheet name '" + sheetName + "'");
                default:
                    i++;
            }
        }
        if (sheetName.charAt(0) == '\'' || sheetName.charAt(len - 1) == '\'') {
            throw new IllegalArgumentException("Invalid sheet name '" + sheetName + "'. Sheet names must not begin or end with (').");
        }
    }

    public static void validateSheetState(int state) {
        switch (state) {
            case 0:
            case 1:
            case 2:
                return;
            default:
                throw new IllegalArgumentException("Ivalid sheet state : " + state + "\n" + "Sheet state must beone of the Workbook.SHEET_STATE_* constants");
        }
    }
}

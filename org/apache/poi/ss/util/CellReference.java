package org.apache.poi.ss.util;

import com.itextpdf.text.pdf.codec.wmf.MetaDo;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.formula.SheetNameFormatter;
import org.apache.poi.ss.usermodel.Cell;

public class CellReference {
    private static final char ABSOLUTE_REFERENCE_MARKER = '$';
    private static final Pattern CELL_REF_PATTERN = Pattern.compile("(\\$?[A-Z]+)?(\\$?[0-9]+)?", 2);
    private static final Pattern COLUMN_REF_PATTERN = Pattern.compile("\\$?([A-Z]+)", 2);
    private static final Pattern NAMED_RANGE_NAME_PATTERN = Pattern.compile("[_A-Z][_.A-Z0-9]*", 2);
    private static final Pattern ROW_REF_PATTERN = Pattern.compile("\\$?([0-9]+)");
    private static final char SHEET_NAME_DELIMITER = '!';
    private static final char SPECIAL_NAME_DELIMITER = '\'';
    private static final Pattern STRICTLY_CELL_REF_PATTERN = Pattern.compile("\\$?([A-Z]+)\\$?([0-9]+)", 2);
    private final int _colIndex;
    private final boolean _isColAbs;
    private final boolean _isRowAbs;
    private final int _rowIndex;
    private final String _sheetName;

    private static final class CellRefParts {
        final String colRef;
        final String rowRef;
        final String sheetName;

        private CellRefParts(String sheetName, String rowRef, String colRef) {
            this.sheetName = sheetName;
            if (rowRef == null) {
                rowRef = "";
            }
            this.rowRef = rowRef;
            if (colRef == null) {
                colRef = "";
            }
            this.colRef = colRef;
        }
    }

    public enum NameType {
        CELL,
        NAMED_RANGE,
        COLUMN,
        ROW,
        BAD_CELL_OR_NAMED_RANGE
    }

    public CellReference(String cellRef) {
        boolean z = false;
        if (cellRef.toUpperCase(Locale.ROOT).endsWith("#REF!")) {
            throw new IllegalArgumentException("Cell reference invalid: " + cellRef);
        }
        boolean z2;
        CellRefParts parts = separateRefParts(cellRef);
        this._sheetName = parts.sheetName;
        String colRef = parts.colRef;
        if (colRef.length() <= 0 || colRef.charAt(0) != ABSOLUTE_REFERENCE_MARKER) {
            z2 = false;
        } else {
            z2 = true;
        }
        this._isColAbs = z2;
        if (this._isColAbs) {
            colRef = colRef.substring(1);
        }
        if (colRef.length() == 0) {
            this._colIndex = -1;
        } else {
            this._colIndex = convertColStringToIndex(colRef);
        }
        String rowRef = parts.rowRef;
        if (rowRef.length() > 0 && rowRef.charAt(0) == ABSOLUTE_REFERENCE_MARKER) {
            z = true;
        }
        this._isRowAbs = z;
        if (this._isRowAbs) {
            rowRef = rowRef.substring(1);
        }
        if (rowRef.length() == 0) {
            this._rowIndex = -1;
        } else {
            this._rowIndex = Integer.parseInt(rowRef) - 1;
        }
    }

    public CellReference(int pRow, int pCol) {
        this(pRow, pCol, false, false);
    }

    public CellReference(int pRow, short pCol) {
        this(pRow, 65535 & pCol, false, false);
    }

    public CellReference(Cell cell) {
        this(cell.getRowIndex(), cell.getColumnIndex(), false, false);
    }

    public CellReference(int pRow, int pCol, boolean pAbsRow, boolean pAbsCol) {
        this(null, pRow, pCol, pAbsRow, pAbsCol);
    }

    public CellReference(String pSheetName, int pRow, int pCol, boolean pAbsRow, boolean pAbsCol) {
        if (pRow < -1) {
            throw new IllegalArgumentException("row index may not be negative, but had " + pRow);
        } else if (pCol < -1) {
            throw new IllegalArgumentException("column index may not be negative, but had " + pCol);
        } else {
            this._sheetName = pSheetName;
            this._rowIndex = pRow;
            this._colIndex = pCol;
            this._isRowAbs = pAbsRow;
            this._isColAbs = pAbsCol;
        }
    }

    public int getRow() {
        return this._rowIndex;
    }

    public short getCol() {
        return (short) this._colIndex;
    }

    public boolean isRowAbsolute() {
        return this._isRowAbs;
    }

    public boolean isColAbsolute() {
        return this._isColAbs;
    }

    public String getSheetName() {
        return this._sheetName;
    }

    public static boolean isPartAbsolute(String part) {
        return part.charAt(0) == ABSOLUTE_REFERENCE_MARKER;
    }

    public static int convertColStringToIndex(String ref) {
        int retval = 0;
        char[] refs = ref.toUpperCase(Locale.ROOT).toCharArray();
        for (int k = 0; k < refs.length; k++) {
            char thechar = refs[k];
            if (thechar != ABSOLUTE_REFERENCE_MARKER) {
                retval = (retval * 26) + ((thechar - 65) + 1);
            } else if (k != 0) {
                throw new IllegalArgumentException("Bad col ref format '" + ref + "'");
            }
        }
        return retval - 1;
    }

    public static NameType classifyCellReference(String str, SpreadsheetVersion ssVersion) {
        int len = str.length();
        if (len < 1) {
            throw new IllegalArgumentException("Empty string not allowed");
        }
        char firstChar = str.charAt(0);
        switch (firstChar) {
            case '$':
            case '.':
            case '_':
                break;
            default:
                if (!(Character.isLetter(firstChar) || Character.isDigit(firstChar))) {
                    throw new IllegalArgumentException("Invalid first char (" + firstChar + ") of cell reference or named range.  Letter expected");
                }
        }
        if (!Character.isDigit(str.charAt(len - 1))) {
            return validateNamedRangeName(str, ssVersion);
        }
        Matcher cellRefPatternMatcher = STRICTLY_CELL_REF_PATTERN.matcher(str);
        if (!cellRefPatternMatcher.matches()) {
            return validateNamedRangeName(str, ssVersion);
        }
        if (cellReferenceIsWithinRange(cellRefPatternMatcher.group(1), cellRefPatternMatcher.group(2), ssVersion)) {
            return NameType.CELL;
        }
        if (str.indexOf(36) >= 0) {
            return NameType.BAD_CELL_OR_NAMED_RANGE;
        }
        return NameType.NAMED_RANGE;
    }

    private static NameType validateNamedRangeName(String str, SpreadsheetVersion ssVersion) {
        Matcher colMatcher = COLUMN_REF_PATTERN.matcher(str);
        if (colMatcher.matches() && isColumnWithinRange(colMatcher.group(1), ssVersion)) {
            return NameType.COLUMN;
        }
        Matcher rowMatcher = ROW_REF_PATTERN.matcher(str);
        if (rowMatcher.matches() && isRowWithinRange(rowMatcher.group(1), ssVersion)) {
            return NameType.ROW;
        }
        if (NAMED_RANGE_NAME_PATTERN.matcher(str).matches()) {
            return NameType.NAMED_RANGE;
        }
        return NameType.BAD_CELL_OR_NAMED_RANGE;
    }

    public static boolean cellReferenceIsWithinRange(String colStr, String rowStr, SpreadsheetVersion ssVersion) {
        if (isColumnWithinRange(colStr, ssVersion)) {
            return isRowWithinRange(rowStr, ssVersion);
        }
        return false;
    }

    public static boolean isColumnWithnRange(String colStr, SpreadsheetVersion ssVersion) {
        return isColumnWithinRange(colStr, ssVersion);
    }

    public static boolean isColumnWithinRange(String colStr, SpreadsheetVersion ssVersion) {
        String lastCol = ssVersion.getLastColumnName();
        int lastColLength = lastCol.length();
        int numberOfLetters = colStr.length();
        if (numberOfLetters > lastColLength) {
            return false;
        }
        if (numberOfLetters != lastColLength || colStr.toUpperCase(Locale.ROOT).compareTo(lastCol) <= 0) {
            return true;
        }
        return false;
    }

    public static boolean isRowWithnRange(String rowStr, SpreadsheetVersion ssVersion) {
        return isRowWithinRange(rowStr, ssVersion);
    }

    public static boolean isRowWithinRange(String rowStr, SpreadsheetVersion ssVersion) {
        int rowNum = Integer.parseInt(rowStr) - 1;
        return rowNum >= 0 && rowNum <= ssVersion.getLastRowIndex();
    }

    private static CellRefParts separateRefParts(String reference) {
        int plingPos = reference.lastIndexOf(33);
        String sheetName = parseSheetName(reference, plingPos);
        Matcher matcher = CELL_REF_PATTERN.matcher(reference.substring(plingPos + 1).toUpperCase(Locale.ROOT));
        if (matcher.matches()) {
            return new CellRefParts(sheetName, matcher.group(2), matcher.group(1));
        }
        throw new IllegalArgumentException("Invalid CellReference: " + reference);
    }

    private static String parseSheetName(String reference, int indexOfSheetNameDelimiter) {
        if (indexOfSheetNameDelimiter < 0) {
            return null;
        }
        boolean isQuoted;
        if (reference.charAt(0) == SPECIAL_NAME_DELIMITER) {
            isQuoted = true;
        } else {
            isQuoted = false;
        }
        if (isQuoted) {
            int lastQuotePos = indexOfSheetNameDelimiter - 1;
            if (reference.charAt(lastQuotePos) != SPECIAL_NAME_DELIMITER) {
                throw new IllegalArgumentException("Mismatched quotes: (" + reference + ")");
            }
            StringBuffer sb = new StringBuffer(indexOfSheetNameDelimiter);
            int i = 1;
            while (i < lastQuotePos) {
                char ch = reference.charAt(i);
                if (ch != SPECIAL_NAME_DELIMITER) {
                    sb.append(ch);
                } else if (i >= lastQuotePos || reference.charAt(i + 1) != SPECIAL_NAME_DELIMITER) {
                    throw new IllegalArgumentException("Bad sheet name quote escaping: (" + reference + ")");
                } else {
                    i++;
                    sb.append(ch);
                }
                i++;
            }
            return sb.toString();
        } else if (reference.indexOf(32) == -1) {
            return reference.substring(0, indexOfSheetNameDelimiter);
        } else {
            throw new IllegalArgumentException("Sheet names containing spaces must be quoted: (" + reference + ")");
        }
    }

    public static String convertNumToColString(int col) {
        int excelColNum = col + 1;
        StringBuilder colRef = new StringBuilder(2);
        int colRemain = excelColNum;
        while (colRemain > 0) {
            int thisPart = colRemain % 26;
            if (thisPart == 0) {
                thisPart = 26;
            }
            colRemain = (colRemain - thisPart) / 26;
            colRef.insert(0, (char) (thisPart + 64));
        }
        return colRef.toString();
    }

    public String formatAsString() {
        StringBuffer sb = new StringBuffer(32);
        if (this._sheetName != null) {
            SheetNameFormatter.appendFormat(sb, this._sheetName);
            sb.append(SHEET_NAME_DELIMITER);
        }
        appendCellReference(sb);
        return sb.toString();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(64);
        sb.append(getClass().getName()).append(" [");
        sb.append(formatAsString());
        sb.append("]");
        return sb.toString();
    }

    public String[] getCellRefParts() {
        return new String[]{this._sheetName, Integer.toString(this._rowIndex + 1), convertNumToColString(this._colIndex)};
    }

    void appendCellReference(StringBuffer sb) {
        if (this._colIndex != -1) {
            if (this._isColAbs) {
                sb.append(ABSOLUTE_REFERENCE_MARKER);
            }
            sb.append(convertNumToColString(this._colIndex));
        }
        if (this._rowIndex != -1) {
            if (this._isRowAbs) {
                sb.append(ABSOLUTE_REFERENCE_MARKER);
            }
            sb.append(this._rowIndex + 1);
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CellReference)) {
            return false;
        }
        CellReference cr = (CellReference) o;
        if (this._rowIndex == cr._rowIndex && this._colIndex == cr._colIndex && this._isRowAbs == cr._isRowAbs && this._isColAbs == cr._isColAbs) {
            if (this._sheetName == null) {
                if (cr._sheetName == null) {
                    return true;
                }
            } else if (this._sheetName.equals(cr._sheetName)) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        int i;
        int i2 = 1;
        int i3 = 0;
        int i4 = (((this._rowIndex + MetaDo.META_OFFSETWINDOWORG) * 31) + this._colIndex) * 31;
        if (this._isRowAbs) {
            i = 1;
        } else {
            i = 0;
        }
        i = (i4 + i) * 31;
        if (!this._isColAbs) {
            i2 = 0;
        }
        i = (i + i2) * 31;
        if (this._sheetName != null) {
            i3 = this._sheetName.hashCode();
        }
        return i + i3;
    }
}

package org.apache.poi.ss.formula;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.formula.constant.ErrorConstant;
import org.apache.poi.ss.formula.function.FunctionMetadata;
import org.apache.poi.ss.formula.function.FunctionMetadataRegistry;
import org.apache.poi.ss.formula.ptg.AbstractFunctionPtg;
import org.apache.poi.ss.formula.ptg.AddPtg;
import org.apache.poi.ss.formula.ptg.Area3DPxg;
import org.apache.poi.ss.formula.ptg.AreaPtg;
import org.apache.poi.ss.formula.ptg.ArrayPtg;
import org.apache.poi.ss.formula.ptg.AttrPtg;
import org.apache.poi.ss.formula.ptg.BoolPtg;
import org.apache.poi.ss.formula.ptg.ConcatPtg;
import org.apache.poi.ss.formula.ptg.DividePtg;
import org.apache.poi.ss.formula.ptg.EqualPtg;
import org.apache.poi.ss.formula.ptg.ErrPtg;
import org.apache.poi.ss.formula.ptg.FuncPtg;
import org.apache.poi.ss.formula.ptg.FuncVarPtg;
import org.apache.poi.ss.formula.ptg.GreaterEqualPtg;
import org.apache.poi.ss.formula.ptg.GreaterThanPtg;
import org.apache.poi.ss.formula.ptg.IntPtg;
import org.apache.poi.ss.formula.ptg.IntersectionPtg;
import org.apache.poi.ss.formula.ptg.LessEqualPtg;
import org.apache.poi.ss.formula.ptg.LessThanPtg;
import org.apache.poi.ss.formula.ptg.MemAreaPtg;
import org.apache.poi.ss.formula.ptg.MemFuncPtg;
import org.apache.poi.ss.formula.ptg.MissingArgPtg;
import org.apache.poi.ss.formula.ptg.MultiplyPtg;
import org.apache.poi.ss.formula.ptg.NamePtg;
import org.apache.poi.ss.formula.ptg.NameXPtg;
import org.apache.poi.ss.formula.ptg.NameXPxg;
import org.apache.poi.ss.formula.ptg.NotEqualPtg;
import org.apache.poi.ss.formula.ptg.NumberPtg;
import org.apache.poi.ss.formula.ptg.OperandPtg;
import org.apache.poi.ss.formula.ptg.OperationPtg;
import org.apache.poi.ss.formula.ptg.ParenthesisPtg;
import org.apache.poi.ss.formula.ptg.PercentPtg;
import org.apache.poi.ss.formula.ptg.PowerPtg;
import org.apache.poi.ss.formula.ptg.Ptg;
import org.apache.poi.ss.formula.ptg.RangePtg;
import org.apache.poi.ss.formula.ptg.RefPtg;
import org.apache.poi.ss.formula.ptg.StringPtg;
import org.apache.poi.ss.formula.ptg.SubtractPtg;
import org.apache.poi.ss.formula.ptg.UnaryMinusPtg;
import org.apache.poi.ss.formula.ptg.UnaryPlusPtg;
import org.apache.poi.ss.formula.ptg.UnionPtg;
import org.apache.poi.ss.formula.ptg.ValueOperatorPtg;
import org.apache.poi.ss.usermodel.FormulaError;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Table;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.ss.util.CellReference.NameType;
import org.apache.poi.util.Internal;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

@Internal
public final class FormulaParser {
    private static final Pattern CELL_REF_PATTERN = Pattern.compile("(\\$?[A-Za-z]+)?(\\$?[0-9]+)?");
    private static final char CR = '\r';
    private static final char LF = '\n';
    private static final char TAB = '\t';
    private static final POILogger log = POILogFactory.getLogger(FormulaParser.class);
    private static final String specAll = "All";
    private static final String specData = "Data";
    private static final String specHeaders = "Headers";
    private static final String specThisRow = "This Row";
    private static final String specTotals = "Totals";
    private final FormulaParsingWorkbook _book;
    private final int _formulaLength;
    private final String _formulaString;
    private boolean _inIntersection = false;
    private int _pointer;
    private ParseNode _rootNode;
    private final int _rowIndex;
    private final int _sheetIndex;
    private final SpreadsheetVersion _ssVersion;
    private char look;

    private FormulaParser(String formula, FormulaParsingWorkbook book, int sheetIndex, int rowIndex) {
        this._formulaString = formula;
        this._pointer = 0;
        this._book = book;
        this._ssVersion = book == null ? SpreadsheetVersion.EXCEL97 : book.getSpreadsheetVersion();
        this._formulaLength = this._formulaString.length();
        this._sheetIndex = sheetIndex;
        this._rowIndex = rowIndex;
    }

    public static Ptg[] parse(String formula, FormulaParsingWorkbook workbook, FormulaType formulaType, int sheetIndex, int rowIndex) {
        FormulaParser fp = new FormulaParser(formula, workbook, sheetIndex, rowIndex);
        fp.parse();
        return fp.getRPNPtg(formulaType);
    }

    public static Ptg[] parse(String formula, FormulaParsingWorkbook workbook, FormulaType formulaType, int sheetIndex) {
        return parse(formula, workbook, formulaType, sheetIndex, -1);
    }

    public static Area3DPxg parseStructuredReference(String tableText, FormulaParsingWorkbook workbook, int rowIndex) {
        Ptg[] arr = parse(tableText, workbook, FormulaType.CELL, -1, rowIndex);
        if (arr.length == 1 && (arr[0] instanceof Area3DPxg)) {
            return (Area3DPxg) arr[0];
        }
        throw new IllegalStateException("Illegal structured reference");
    }

    private void GetChar() {
        if (!IsWhite(this.look)) {
            this._inIntersection = false;
        } else if (this.look == ' ') {
            this._inIntersection = true;
        }
        if (this._pointer > this._formulaLength) {
            throw new RuntimeException("too far");
        }
        if (this._pointer < this._formulaLength) {
            this.look = this._formulaString.charAt(this._pointer);
        } else {
            this.look = '\u0000';
            this._inIntersection = false;
        }
        this._pointer++;
    }

    private void resetPointer(int ptr) {
        this._pointer = ptr;
        if (this._pointer <= this._formulaLength) {
            this.look = this._formulaString.charAt(this._pointer - 1);
        } else {
            this.look = '\u0000';
        }
    }

    private RuntimeException expected(String s) {
        String msg;
        if (this.look != '=' || this._formulaString.substring(0, this._pointer - 1).trim().length() >= 1) {
            msg = "Parse error near char " + (this._pointer - 1) + " '" + this.look + "'" + " in specified formula '" + this._formulaString + "'. Expected " + s;
        } else {
            msg = "The specified formula '" + this._formulaString + "' starts with an equals sign which is not allowed.";
        }
        return new FormulaParseException(msg);
    }

    private static boolean IsAlpha(char c) {
        return Character.isLetter(c) || c == '$' || c == '_';
    }

    private static boolean IsDigit(char c) {
        return Character.isDigit(c);
    }

    private static boolean IsWhite(char c) {
        return c == ' ' || c == TAB || c == CR || c == LF;
    }

    private void SkipWhite() {
        while (IsWhite(this.look)) {
            GetChar();
        }
    }

    private void Match(char x) {
        if (this.look != x) {
            throw expected("'" + x + "'");
        }
        GetChar();
    }

    private String GetNum() {
        StringBuffer value = new StringBuffer();
        while (IsDigit(this.look)) {
            value.append(this.look);
            GetChar();
        }
        return value.length() == 0 ? null : value.toString();
    }

    private ParseNode parseRangeExpression() {
        ParseNode result = parseRangeable();
        boolean hasRange = false;
        while (this.look == ':') {
            int pos = this._pointer;
            GetChar();
            ParseNode nextPart = parseRangeable();
            checkValidRangeOperand("LHS", pos, result);
            checkValidRangeOperand("RHS", pos, nextPart);
            result = new ParseNode(RangePtg.instance, new ParseNode[]{result, nextPart});
            hasRange = true;
        }
        if (hasRange) {
            return augmentWithMemPtg(result);
        }
        return result;
    }

    private static ParseNode augmentWithMemPtg(ParseNode root) {
        Ptg memPtg;
        if (needsMemFunc(root)) {
            memPtg = new MemFuncPtg(root.getEncodedSize());
        } else {
            memPtg = new MemAreaPtg(root.getEncodedSize());
        }
        return new ParseNode(memPtg, root);
    }

    private static boolean needsMemFunc(ParseNode root) {
        Ptg token = root.getToken();
        if ((token instanceof AbstractFunctionPtg) || (token instanceof ExternSheetReferenceToken) || (token instanceof NamePtg) || (token instanceof NameXPtg)) {
            return true;
        }
        if ((token instanceof OperationPtg) || (token instanceof ParenthesisPtg)) {
            for (ParseNode child : root.getChildren()) {
                if (needsMemFunc(child)) {
                    return true;
                }
            }
            return false;
        } else if (token instanceof OperandPtg) {
            return false;
        } else {
            if (token instanceof OperationPtg) {
                return true;
            }
            return false;
        }
    }

    private static void checkValidRangeOperand(String sideName, int currentParsePosition, ParseNode pn) {
        if (!isValidRangeOperand(pn)) {
            throw new FormulaParseException("The " + sideName + " of the range operator ':' at position " + currentParsePosition + " is not a proper reference.");
        }
    }

    private static boolean isValidRangeOperand(ParseNode a) {
        Ptg tkn = a.getToken();
        if (tkn instanceof OperandPtg) {
            return true;
        }
        if (tkn instanceof AbstractFunctionPtg) {
            if (((AbstractFunctionPtg) tkn).getDefaultOperandClass() != (byte) 0) {
                return false;
            }
            return true;
        } else if (tkn instanceof ValueOperatorPtg) {
            return false;
        } else {
            if (tkn instanceof OperationPtg) {
                return true;
            }
            if (tkn instanceof ParenthesisPtg) {
                return isValidRangeOperand(a.getChildren()[0]);
            }
            if (tkn != ErrPtg.REF_INVALID) {
                return false;
            }
            return true;
        }
    }

    private ParseNode parseRangeable() {
        SkipWhite();
        int savePointer = this._pointer;
        SheetIdentifier sheetIden = parseSheetName();
        if (sheetIden == null) {
            resetPointer(savePointer);
        } else {
            SkipWhite();
            savePointer = this._pointer;
        }
        SimpleRangePart part1 = parseSimpleRangePart();
        if (part1 != null) {
            boolean whiteAfterPart1 = IsWhite(this.look);
            if (whiteAfterPart1) {
                SkipWhite();
            }
            SimpleRangePart part2;
            if (this.look == ':') {
                int colonPos = this._pointer;
                GetChar();
                SkipWhite();
                part2 = parseSimpleRangePart();
                if (!(part2 == null || part1.isCompatibleForArea(part2))) {
                    part2 = null;
                }
                if (part2 == null) {
                    resetPointer(colonPos);
                    if (!part1.isCell()) {
                        String prefix = "";
                        if (sheetIden != null) {
                            prefix = "'" + sheetIden.getSheetIdentifier().getName() + '!';
                        }
                        throw new FormulaParseException(prefix + part1.getRep() + "' is not a proper reference.");
                    }
                }
                return createAreaRefParseNode(sheetIden, part1, part2);
            } else if (this.look == '.') {
                GetChar();
                int dotCount = 1;
                while (this.look == '.') {
                    dotCount++;
                    GetChar();
                }
                boolean whiteBeforePart2 = IsWhite(this.look);
                SkipWhite();
                part2 = parseSimpleRangePart();
                String part1And2 = this._formulaString.substring(savePointer - 1, this._pointer - 1);
                if (part2 == null) {
                    if (sheetIden == null) {
                        return parseNonRange(savePointer);
                    }
                    throw new FormulaParseException("Complete area reference expected after sheet name at index " + this._pointer + ".");
                } else if (whiteAfterPart1 || whiteBeforePart2) {
                    if (!part1.isRowOrColumn() && !part2.isRowOrColumn()) {
                        return createAreaRefParseNode(sheetIden, part1, part2);
                    }
                    throw new FormulaParseException("Dotted range (full row or column) expression '" + part1And2 + "' must not contain whitespace.");
                } else if (dotCount == 1 && part1.isRow() && part2.isRow()) {
                    return parseNonRange(savePointer);
                } else {
                    if ((!part1.isRowOrColumn() && !part2.isRowOrColumn()) || dotCount == 2) {
                        return createAreaRefParseNode(sheetIden, part1, part2);
                    }
                    throw new FormulaParseException("Dotted range (full row or column) expression '" + part1And2 + "' must have exactly 2 dots.");
                }
            } else if (part1.isCell() && isValidCellReference(part1.getRep())) {
                return createAreaRefParseNode(sheetIden, part1, null);
            } else {
                if (sheetIden == null) {
                    return parseNonRange(savePointer);
                }
                throw new FormulaParseException("Second part of cell reference expected after sheet name at index " + this._pointer + ".");
            }
        } else if (sheetIden == null) {
            return parseNonRange(savePointer);
        } else {
            if (this.look == '#') {
                return new ParseNode(ErrPtg.valueOf(parseErrorLiteral()));
            }
            String name = parseAsName();
            if (name.length() == 0) {
                throw new FormulaParseException("Cell reference or Named Range expected after sheet name at index " + this._pointer + ".");
            }
            Ptg nameXPtg = this._book.getNameXPtg(name, sheetIden);
            if (nameXPtg != null) {
                return new ParseNode(nameXPtg);
            }
            throw new FormulaParseException("Specified name '" + name + "' for sheet " + sheetIden.asFormulaString() + " not found");
        }
    }

    private ParseNode parseStructuredReference(String tableName) {
        if (this._ssVersion.equals(SpreadsheetVersion.EXCEL2007)) {
            Table tbl = this._book.getTable(tableName);
            if (tbl == null) {
                throw new FormulaParseException("Illegal table name: '" + tableName + "'");
            }
            int savePtr1;
            String sheetName = tbl.getSheetName();
            int startCol = tbl.getStartColIndex();
            int endCol = tbl.getEndColIndex();
            int startRow = tbl.getStartRowIndex();
            int endRow = tbl.getEndRowIndex();
            int savePtr0 = this._pointer;
            GetChar();
            boolean isTotalsSpec = false;
            boolean isThisRowSpec = false;
            boolean isDataSpec = false;
            boolean isHeadersSpec = false;
            boolean isAllSpec = false;
            int nSpecQuantifiers = 0;
            while (true) {
                savePtr1 = this._pointer;
                String specName = parseAsSpecialQuantifier();
                if (specName != null) {
                    if (specName.equals(specAll)) {
                        isAllSpec = true;
                    } else if (specName.equals(specData)) {
                        isDataSpec = true;
                    } else if (specName.equals(specHeaders)) {
                        isHeadersSpec = true;
                    } else if (specName.equals(specThisRow)) {
                        isThisRowSpec = true;
                    } else if (specName.equals(specTotals)) {
                        isTotalsSpec = true;
                    } else {
                        throw new FormulaParseException("Unknown special quantifier " + specName);
                    }
                    nSpecQuantifiers++;
                    if (this.look != ',') {
                        break;
                    }
                    GetChar();
                } else {
                    break;
                }
            }
            resetPointer(savePtr1);
            boolean isThisRow = false;
            SkipWhite();
            if (this.look == '@') {
                isThisRow = true;
                GetChar();
            }
            String endColumnName = null;
            int nColQuantifiers = 0;
            savePtr1 = this._pointer;
            String startColumnName = parseAsColumnQuantifier();
            if (startColumnName == null) {
                resetPointer(savePtr1);
            } else {
                nColQuantifiers = 0 + 1;
                if (this.look == ',') {
                    throw new FormulaParseException("The formula " + this._formulaString + "is illegal: you should not use ',' with column quantifiers");
                } else if (this.look == ':') {
                    GetChar();
                    endColumnName = parseAsColumnQuantifier();
                    nColQuantifiers++;
                    if (endColumnName == null) {
                        throw new FormulaParseException("The formula " + this._formulaString + "is illegal: the string after ':' must be column quantifier");
                    }
                }
            }
            if (nColQuantifiers == 0 && nSpecQuantifiers == 0) {
                resetPointer(savePtr0);
                savePtr0 = this._pointer;
                startColumnName = parseAsColumnQuantifier();
                if (startColumnName != null) {
                    nColQuantifiers++;
                } else {
                    resetPointer(savePtr0);
                    String name = parseAsSpecialQuantifier();
                    if (name != null) {
                        if (name.equals(specAll)) {
                            isAllSpec = true;
                        } else if (name.equals(specData)) {
                            isDataSpec = true;
                        } else if (name.equals(specHeaders)) {
                            isHeadersSpec = true;
                        } else if (name.equals(specThisRow)) {
                            isThisRowSpec = true;
                        } else if (name.equals(specTotals)) {
                            isTotalsSpec = true;
                        } else {
                            throw new FormulaParseException("Unknown special quantifier " + name);
                        }
                        nSpecQuantifiers++;
                    } else {
                        throw new FormulaParseException("The formula " + this._formulaString + " is illegal");
                    }
                }
            }
            Match(']');
            if (isTotalsSpec && !tbl.isHasTotalsRow()) {
                return new ParseNode(ErrPtg.REF_INVALID);
            }
            if ((!isThisRow && !isThisRowSpec) || (this._rowIndex >= startRow && endRow >= this._rowIndex)) {
                int actualStartRow = startRow;
                int actualEndRow = endRow;
                int actualStartCol = startCol;
                int actualEndCol = endCol;
                if (nSpecQuantifiers > 0) {
                    if (!(nSpecQuantifiers == 1 && isAllSpec)) {
                        if (isDataSpec && isHeadersSpec) {
                            if (tbl.isHasTotalsRow()) {
                                actualEndRow = endRow - 1;
                            }
                        } else if (isDataSpec && isTotalsSpec) {
                            actualStartRow = startRow + 1;
                        } else if (nSpecQuantifiers == 1 && isDataSpec) {
                            actualStartRow = startRow + 1;
                            if (tbl.isHasTotalsRow()) {
                                actualEndRow = endRow - 1;
                            }
                        } else if (nSpecQuantifiers == 1 && isHeadersSpec) {
                            actualEndRow = actualStartRow;
                        } else if (nSpecQuantifiers == 1 && isTotalsSpec) {
                            actualStartRow = actualEndRow;
                        } else if ((nSpecQuantifiers == 1 && isThisRowSpec) || isThisRow) {
                            actualStartRow = this._rowIndex;
                            actualEndRow = this._rowIndex;
                        } else {
                            throw new FormulaParseException("The formula " + this._formulaString + " is illegal");
                        }
                    }
                } else if (isThisRow) {
                    actualStartRow = this._rowIndex;
                    actualEndRow = this._rowIndex;
                } else {
                    actualStartRow++;
                }
                if (nColQuantifiers == 2) {
                    if (startColumnName == null || endColumnName == null) {
                        throw new IllegalStateException("Fatal error");
                    }
                    int startIdx = tbl.findColumnIndex(startColumnName);
                    int endIdx = tbl.findColumnIndex(endColumnName);
                    if (startIdx == -1 || endIdx == -1) {
                        throw new FormulaParseException("One of the columns " + startColumnName + ", " + endColumnName + " doesn't exist in table " + tbl.getName());
                    }
                    actualStartCol = startCol + startIdx;
                    actualEndCol = startCol + endIdx;
                } else if (nColQuantifiers == 1 && !isThisRow) {
                    if (startColumnName == null) {
                        throw new IllegalStateException("Fatal error");
                    }
                    int idx = tbl.findColumnIndex(startColumnName);
                    if (idx == -1) {
                        throw new FormulaParseException("The column " + startColumnName + " doesn't exist in table " + tbl.getName());
                    }
                    actualStartCol = startCol + idx;
                    actualEndCol = actualStartCol;
                }
                CellReference cellReference = new CellReference(actualStartRow, actualStartCol);
                CellReference bottomRight = new CellReference(actualEndRow, actualEndCol);
                SheetIdentifier sheetIdentifier = new SheetIdentifier(null, new NameIdentifier(sheetName, true));
                return new ParseNode(this._book.get3DReferencePtg(new AreaReference(cellReference, bottomRight), sheetIdentifier));
            } else if (this._rowIndex >= 0) {
                return new ParseNode(ErrPtg.VALUE_INVALID);
            } else {
                throw new FormulaParseException("Formula contained [#This Row] or [@] structured reference but this row < 0. Row index must be specified for row-referencing structured references.");
            }
        }
        throw new FormulaParseException("Structured references work only on XSSF (Excel 2007+)!");
    }

    private String parseAsColumnQuantifier() {
        if (this.look != '[') {
            return null;
        }
        GetChar();
        if (this.look == '#') {
            return null;
        }
        if (this.look == '@') {
            GetChar();
        }
        StringBuilder name = new StringBuilder();
        while (this.look != ']') {
            name.append(this.look);
            GetChar();
        }
        Match(']');
        return name.toString();
    }

    private String parseAsSpecialQuantifier() {
        String str = null;
        if (this.look == '[') {
            GetChar();
            if (this.look == '#') {
                GetChar();
                str = parseAsName();
                if (str.equals("This")) {
                    str = str + ' ' + parseAsName();
                }
                Match(']');
            }
        }
        return str;
    }

    private ParseNode parseNonRange(int savePointer) {
        resetPointer(savePointer);
        if (Character.isDigit(this.look)) {
            return new ParseNode(parseNumber());
        }
        if (this.look == '\"') {
            return new ParseNode(new StringPtg(parseStringLiteral()));
        }
        String name = parseAsName();
        if (this.look == '(') {
            return function(name);
        }
        if (this.look == '[') {
            return parseStructuredReference(name);
        }
        if (name.equalsIgnoreCase("TRUE") || name.equalsIgnoreCase("FALSE")) {
            return new ParseNode(BoolPtg.valueOf(name.equalsIgnoreCase("TRUE")));
        }
        if (this._book == null) {
            throw new IllegalStateException("Need book to evaluate name '" + name + "'");
        }
        EvaluationName evalName = this._book.getName(name, this._sheetIndex);
        if (evalName == null) {
            throw new FormulaParseException("Specified named range '" + name + "' does not exist in the current workbook.");
        } else if (evalName.isRange()) {
            return new ParseNode(evalName.createPtg());
        } else {
            throw new FormulaParseException("Specified name '" + name + "' is not a range as expected.");
        }
    }

    private String parseAsName() {
        StringBuilder sb = new StringBuilder();
        if (Character.isLetter(this.look) || this.look == '_' || this.look == '\\') {
            while (isValidDefinedNameChar(this.look)) {
                sb.append(this.look);
                GetChar();
            }
            SkipWhite();
            return sb.toString();
        }
        throw expected("number, string, defined name, or data table");
    }

    private static boolean isValidDefinedNameChar(char ch) {
        if (Character.isLetterOrDigit(ch)) {
            return true;
        }
        switch (ch) {
            case '.':
            case '?':
            case '\\':
            case '_':
                return true;
            default:
                return false;
        }
    }

    private ParseNode createAreaRefParseNode(SheetIdentifier sheetIden, SimpleRangePart part1, SimpleRangePart part2) throws FormulaParseException {
        Ptg ptg;
        if (part2 == null) {
            CellReference cr = part1.getCellReference();
            if (sheetIden == null) {
                ptg = new RefPtg(cr);
            } else {
                ptg = this._book.get3DReferencePtg(cr, sheetIden);
            }
        } else {
            AreaReference areaRef = createAreaRef(part1, part2);
            if (sheetIden == null) {
                ptg = new AreaPtg(areaRef);
            } else {
                ptg = this._book.get3DReferencePtg(areaRef, sheetIden);
            }
        }
        return new ParseNode(ptg);
    }

    private AreaReference createAreaRef(SimpleRangePart part1, SimpleRangePart part2) {
        if (!part1.isCompatibleForArea(part2)) {
            throw new FormulaParseException("has incompatible parts: '" + part1.getRep() + "' and '" + part2.getRep() + "'.");
        } else if (part1.isRow()) {
            return AreaReference.getWholeRow(this._ssVersion, part1.getRep(), part2.getRep());
        } else {
            if (part1.isColumn()) {
                return AreaReference.getWholeColumn(this._ssVersion, part1.getRep(), part2.getRep());
            }
            return new AreaReference(part1.getCellReference(), part2.getCellReference());
        }
    }

    private SimpleRangePart parseSimpleRangePart() {
        int ptr = this._pointer - 1;
        boolean hasDigits = false;
        boolean hasLetters = false;
        while (ptr < this._formulaLength) {
            char ch = this._formulaString.charAt(ptr);
            if (!Character.isDigit(ch)) {
                if (!Character.isLetter(ch)) {
                    if (!(ch == '$' || ch == '_')) {
                        break;
                    }
                }
                hasLetters = true;
            } else {
                hasDigits = true;
            }
            ptr++;
        }
        if (ptr <= this._pointer - 1) {
            return null;
        }
        String rep = this._formulaString.substring(this._pointer - 1, ptr);
        if (!CELL_REF_PATTERN.matcher(rep).matches()) {
            return null;
        }
        if (hasLetters && hasDigits) {
            if (!isValidCellReference(rep)) {
                return null;
            }
        } else if (hasLetters) {
            if (!CellReference.isColumnWithinRange(rep.replace("$", ""), this._ssVersion)) {
                return null;
            }
        } else if (!hasDigits) {
            return null;
        } else {
            try {
                int i = Integer.parseInt(rep.replace("$", ""));
                if (i < 1) {
                    return null;
                }
                if (i > this._ssVersion.getMaxRows()) {
                    return null;
                }
            } catch (NumberFormatException e) {
                return null;
            }
        }
        resetPointer(ptr + 1);
        return new SimpleRangePart(rep, hasLetters, hasDigits);
    }

    private SheetIdentifier parseSheetName() {
        StringBuilder sb;
        String bookName;
        if (this.look == '[') {
            sb = new StringBuilder();
            GetChar();
            while (this.look != ']') {
                sb.append(this.look);
                GetChar();
            }
            GetChar();
            bookName = sb.toString();
        } else {
            bookName = null;
        }
        NameIdentifier iden;
        if (this.look == '\'') {
            boolean done;
            StringBuffer sb2 = new StringBuffer();
            Match('\'');
            if (this.look == '\'') {
                done = true;
            } else {
                done = false;
            }
            while (!done) {
                sb2.append(this.look);
                GetChar();
                if (this.look == '\'') {
                    Match('\'');
                    if (this.look != '\'') {
                        done = true;
                    } else {
                        done = false;
                    }
                }
            }
            iden = new NameIdentifier(sb2.toString(), true);
            SkipWhite();
            if (this.look != '!') {
                return this.look == ':' ? parseSheetRange(bookName, iden) : null;
            } else {
                GetChar();
                return new SheetIdentifier(bookName, iden);
            }
        } else if (this.look == '_' || Character.isLetter(this.look)) {
            sb = new StringBuilder();
            while (isUnquotedSheetNameChar(this.look)) {
                sb.append(this.look);
                GetChar();
            }
            iden = new NameIdentifier(sb.toString(), false);
            SkipWhite();
            if (this.look != '!') {
                return this.look == ':' ? parseSheetRange(bookName, iden) : null;
            } else {
                GetChar();
                return new SheetIdentifier(bookName, iden);
            }
        } else if (this.look != '!' || bookName == null) {
            return null;
        } else {
            GetChar();
            return new SheetIdentifier(bookName, null);
        }
    }

    private SheetIdentifier parseSheetRange(String bookname, NameIdentifier sheet1Name) {
        GetChar();
        SheetIdentifier sheet2 = parseSheetName();
        if (sheet2 != null) {
            return new SheetRangeIdentifier(bookname, sheet1Name, sheet2.getSheetIdentifier());
        }
        return null;
    }

    private static boolean isUnquotedSheetNameChar(char ch) {
        if (Character.isLetterOrDigit(ch)) {
            return true;
        }
        switch (ch) {
            case '.':
            case '_':
                return true;
            default:
                return false;
        }
    }

    private boolean isValidCellReference(String str) {
        boolean result;
        if (CellReference.classifyCellReference(str, this._ssVersion) == NameType.CELL) {
            result = true;
        } else {
            result = false;
        }
        if (result) {
            boolean isFunc;
            if (FunctionMetadataRegistry.getFunctionByName(str.toUpperCase(Locale.ROOT)) != null) {
                isFunc = true;
            } else {
                isFunc = false;
            }
            if (isFunc) {
                int savePointer = this._pointer;
                resetPointer(this._pointer + str.length());
                SkipWhite();
                if (this.look != '(') {
                    result = true;
                } else {
                    result = false;
                }
                resetPointer(savePointer);
            }
        }
        return result;
    }

    private ParseNode function(String name) {
        Ptg nameToken = null;
        if (!AbstractFunctionPtg.isBuiltInFunctionName(name)) {
            if (this._book == null) {
                throw new IllegalStateException("Need book to evaluate name '" + name + "'");
            }
            EvaluationName hName = this._book.getName(name, this._sheetIndex);
            if (hName == null) {
                nameToken = this._book.getNameXPtg(name, null);
                if (nameToken == null) {
                    if (log.check(5)) {
                        log.log(5, "FormulaParser.function: Name '" + name + "' is completely unknown in the current workbook.");
                    }
                    switch (1.$SwitchMap$org$apache$poi$ss$SpreadsheetVersion[this._book.getSpreadsheetVersion().ordinal()]) {
                        case 1:
                            addName(name);
                            nameToken = this._book.getName(name, this._sheetIndex).createPtg();
                            break;
                        case 2:
                            nameToken = new NameXPxg(name);
                            break;
                        default:
                            throw new IllegalStateException("Unexpected spreadsheet version: " + this._book.getSpreadsheetVersion().name());
                    }
                }
            } else if (hName.isFunctionName()) {
                nameToken = hName.createPtg();
            } else {
                throw new FormulaParseException("Attempt to use name '" + name + "' as a function, but defined name in workbook does not refer to a function");
            }
        }
        Match('(');
        ParseNode[] args = Arguments();
        Match(')');
        return getFunction(name, nameToken, args);
    }

    private final void addName(String functionName) {
        Name name = this._book.createName();
        name.setFunction(true);
        name.setNameName(functionName);
        name.setSheetIndex(this._sheetIndex);
    }

    private ParseNode getFunction(String name, Ptg namePtg, ParseNode[] args) {
        boolean isVarArgs = false;
        FunctionMetadata fm = FunctionMetadataRegistry.getFunctionByName(name.toUpperCase(Locale.ROOT));
        int numArgs = args.length;
        if (fm == null) {
            if (namePtg == null) {
                throw new IllegalStateException("NamePtg must be supplied for external functions");
            }
            ParseNode[] allArgs = new ParseNode[(numArgs + 1)];
            allArgs[0] = new ParseNode(namePtg);
            System.arraycopy(args, 0, allArgs, 1, numArgs);
            return new ParseNode(FuncVarPtg.create(name, numArgs + 1), allArgs);
        } else if (namePtg != null) {
            throw new IllegalStateException("NamePtg no applicable to internal functions");
        } else {
            if (!fm.hasFixedArgsLength()) {
                isVarArgs = true;
            }
            int funcIx = fm.getIndex();
            if (funcIx == 4 && args.length == 1) {
                return new ParseNode(AttrPtg.getSumSingle(), args);
            }
            AbstractFunctionPtg retval;
            validateNumArgs(args.length, fm);
            if (isVarArgs) {
                retval = FuncVarPtg.create(name, numArgs);
            } else {
                retval = FuncPtg.create(funcIx);
            }
            return new ParseNode(retval, args);
        }
    }

    private void validateNumArgs(int numArgs, FunctionMetadata fm) {
        String msg;
        if (numArgs < fm.getMinParams()) {
            msg = "Too few arguments to function '" + fm.getName() + "'. ";
            if (fm.hasFixedArgsLength()) {
                msg = msg + "Expected " + fm.getMinParams();
            } else {
                msg = msg + "At least " + fm.getMinParams() + " were expected";
            }
            throw new FormulaParseException(msg + " but got " + numArgs + ".");
        }
        int maxArgs;
        if (!fm.hasUnlimitedVarags()) {
            maxArgs = fm.getMaxParams();
        } else if (this._book != null) {
            maxArgs = this._book.getSpreadsheetVersion().getMaxFunctionArgs();
        } else {
            maxArgs = fm.getMaxParams();
        }
        if (numArgs > maxArgs) {
            msg = "Too many arguments to function '" + fm.getName() + "'. ";
            if (fm.hasFixedArgsLength()) {
                msg = msg + "Expected " + maxArgs;
            } else {
                msg = msg + "At most " + maxArgs + " were expected";
            }
            throw new FormulaParseException(msg + " but got " + numArgs + ".");
        }
    }

    private static boolean isArgumentDelimiter(char ch) {
        return ch == ',' || ch == ')';
    }

    private ParseNode[] Arguments() {
        List<ParseNode> temp = new ArrayList(2);
        SkipWhite();
        if (this.look == ')') {
            return ParseNode.EMPTY_ARRAY;
        }
        boolean missedPrevArg = true;
        while (true) {
            SkipWhite();
            if (isArgumentDelimiter(this.look)) {
                if (missedPrevArg) {
                    temp.add(new ParseNode(MissingArgPtg.instance));
                }
                if (this.look == ')') {
                    ParseNode[] result = new ParseNode[temp.size()];
                    temp.toArray(result);
                    return result;
                }
                Match(',');
                missedPrevArg = true;
            } else {
                temp.add(comparisonExpression());
                missedPrevArg = false;
                SkipWhite();
                if (!isArgumentDelimiter(this.look)) {
                    throw expected("',' or ')'");
                }
            }
        }
    }

    private ParseNode powerFactor() {
        ParseNode result = percentFactor();
        while (true) {
            SkipWhite();
            if (this.look != '^') {
                return result;
            }
            Match('^');
            result = new ParseNode(PowerPtg.instance, result, percentFactor());
        }
    }

    private ParseNode percentFactor() {
        ParseNode result = parseSimpleFactor();
        while (true) {
            SkipWhite();
            if (this.look != '%') {
                return result;
            }
            Match('%');
            result = new ParseNode(PercentPtg.instance, result);
        }
    }

    private ParseNode parseSimpleFactor() {
        SkipWhite();
        switch (this.look) {
            case '\"':
                return new ParseNode(new StringPtg(parseStringLiteral()));
            case '#':
                return new ParseNode(ErrPtg.valueOf(parseErrorLiteral()));
            case '(':
                Match('(');
                ParseNode inside = unionExpression();
                Match(')');
                return new ParseNode(ParenthesisPtg.instance, inside);
            case '+':
                Match('+');
                return parseUnary(true);
            case '-':
                Match('-');
                return parseUnary(false);
            case '{':
                Match('{');
                ParseNode arrayNode = parseArray();
                Match('}');
                return arrayNode;
            default:
                if (IsAlpha(this.look) || Character.isDigit(this.look) || this.look == '\'' || this.look == '[' || this.look == '_' || this.look == '\\') {
                    return parseRangeExpression();
                }
                if (this.look == '.') {
                    return new ParseNode(parseNumber());
                }
                throw expected("cell ref or constant literal");
        }
    }

    private ParseNode parseUnary(boolean isPlus) {
        boolean numberFollows = IsDigit(this.look) || this.look == '.';
        ParseNode factor = powerFactor();
        if (numberFollows) {
            Ptg token = factor.getToken();
            if (token instanceof NumberPtg) {
                if (isPlus) {
                    return factor;
                }
                return new ParseNode(new NumberPtg(-((NumberPtg) token).getValue()));
            } else if (token instanceof IntPtg) {
                if (isPlus) {
                    return factor;
                }
                return new ParseNode(new NumberPtg((double) (-((IntPtg) token).getValue())));
            }
        }
        return new ParseNode(isPlus ? UnaryPlusPtg.instance : UnaryMinusPtg.instance, factor);
    }

    private ParseNode parseArray() {
        List<Object[]> rowsData = new ArrayList();
        while (true) {
            rowsData.add(parseArrayRow());
            if (this.look == '}') {
                Object[][] values2d = new Object[rowsData.size()][];
                rowsData.toArray(values2d);
                checkRowLengths(values2d, values2d[0].length);
                return new ParseNode(new ArrayPtg(values2d));
            } else if (this.look != ';') {
                throw expected("'}' or ';'");
            } else {
                Match(';');
            }
        }
    }

    private void checkRowLengths(Object[][] values2d, int nColumns) {
        for (int i = 0; i < values2d.length; i++) {
            int rowLen = values2d[i].length;
            if (rowLen != nColumns) {
                throw new FormulaParseException("Array row " + i + " has length " + rowLen + " but row 0 has length " + nColumns);
            }
        }
    }

    private Object[] parseArrayRow() {
        List<Object> temp = new ArrayList();
        while (true) {
            temp.add(parseArrayItem());
            SkipWhite();
            switch (this.look) {
                case ',':
                    Match(',');
                case ';':
                case '}':
                    Object[] result = new Object[temp.size()];
                    temp.toArray(result);
                    return result;
                default:
                    throw expected("'}' or ','");
            }
        }
    }

    private Object parseArrayItem() {
        SkipWhite();
        switch (this.look) {
            case '\"':
                return parseStringLiteral();
            case '#':
                return ErrorConstant.valueOf(parseErrorLiteral());
            case '-':
                Match('-');
                SkipWhite();
                return convertArrayNumber(parseNumber(), false);
            case 'F':
            case 'T':
            case 'f':
            case 't':
                return parseBooleanLiteral();
            default:
                return convertArrayNumber(parseNumber(), true);
        }
    }

    private Boolean parseBooleanLiteral() {
        String iden = parseUnquotedIdentifier();
        if ("TRUE".equalsIgnoreCase(iden)) {
            return Boolean.TRUE;
        }
        if ("FALSE".equalsIgnoreCase(iden)) {
            return Boolean.FALSE;
        }
        throw expected("'TRUE' or 'FALSE'");
    }

    private static Double convertArrayNumber(Ptg ptg, boolean isPositive) {
        double value;
        if (ptg instanceof IntPtg) {
            value = (double) ((IntPtg) ptg).getValue();
        } else if (ptg instanceof NumberPtg) {
            value = ((NumberPtg) ptg).getValue();
        } else {
            throw new RuntimeException("Unexpected ptg (" + ptg.getClass().getName() + ")");
        }
        if (!isPositive) {
            value = -value;
        }
        return new Double(value);
    }

    private Ptg parseNumber() {
        String number2 = null;
        String exponent = null;
        String number1 = GetNum();
        if (this.look == '.') {
            GetChar();
            number2 = GetNum();
        }
        if (this.look == 'E') {
            GetChar();
            String sign = "";
            if (this.look == '+') {
                GetChar();
            } else if (this.look == '-') {
                GetChar();
                sign = "-";
            }
            String number = GetNum();
            if (number == null) {
                throw expected("Integer");
            }
            exponent = sign + number;
        }
        if (number1 != null || number2 != null) {
            return getNumberPtgFromString(number1, number2, exponent);
        }
        throw expected("Integer");
    }

    private int parseErrorLiteral() {
        Match('#');
        String part1 = parseUnquotedIdentifier().toUpperCase(Locale.ROOT);
        if (part1 == null) {
            throw expected("remainder of error constant literal");
        }
        FormulaError fe;
        switch (part1.charAt(0)) {
            case 'D':
                fe = FormulaError.DIV0;
                if (part1.equals("DIV")) {
                    Match('/');
                    Match('0');
                    Match('!');
                    return fe.getCode();
                }
                throw expected(fe.getString());
            case 'N':
                fe = FormulaError.NAME;
                if (part1.equals(fe.name())) {
                    Match('?');
                    return fe.getCode();
                }
                fe = FormulaError.NUM;
                if (part1.equals(fe.name())) {
                    Match('!');
                    return fe.getCode();
                }
                fe = FormulaError.NULL;
                if (part1.equals(fe.name())) {
                    Match('!');
                    return fe.getCode();
                }
                fe = FormulaError.NA;
                if (part1.equals("N")) {
                    Match('/');
                    if (this.look == 'A' || this.look == 'a') {
                        Match(this.look);
                        return fe.getCode();
                    }
                    throw expected(fe.getString());
                }
                throw expected("#NAME?, #NUM!, #NULL! or #N/A");
            case 'R':
                fe = FormulaError.REF;
                if (part1.equals(fe.name())) {
                    Match('!');
                    return fe.getCode();
                }
                throw expected(fe.getString());
            case 'V':
                fe = FormulaError.VALUE;
                if (part1.equals(fe.name())) {
                    Match('!');
                    return fe.getCode();
                }
                throw expected(fe.getString());
            default:
                throw expected("#VALUE!, #REF!, #DIV/0!, #NAME?, #NUM!, #NULL! or #N/A");
        }
    }

    private String parseUnquotedIdentifier() {
        if (this.look == '\'') {
            throw expected("unquoted identifier");
        }
        StringBuilder sb = new StringBuilder();
        while (true) {
            if (!Character.isLetterOrDigit(this.look) && this.look != '.') {
                break;
            }
            sb.append(this.look);
            GetChar();
        }
        if (sb.length() < 1) {
            return null;
        }
        return sb.toString();
    }

    private static Ptg getNumberPtgFromString(String number1, String number2, String exponent) {
        StringBuffer number = new StringBuffer();
        if (number2 == null) {
            number.append(number1);
            if (exponent != null) {
                number.append('E');
                number.append(exponent);
            }
            String numberStr = number.toString();
            try {
                int intVal = Integer.parseInt(numberStr);
                if (IntPtg.isInRange(intVal)) {
                    return new IntPtg(intVal);
                }
                return new NumberPtg(numberStr);
            } catch (NumberFormatException e) {
                return new NumberPtg(numberStr);
            }
        }
        if (number1 != null) {
            number.append(number1);
        }
        number.append('.');
        number.append(number2);
        if (exponent != null) {
            number.append('E');
            number.append(exponent);
        }
        return new NumberPtg(number.toString());
    }

    private String parseStringLiteral() {
        Match('\"');
        StringBuffer token = new StringBuffer();
        while (true) {
            if (this.look == '\"') {
                GetChar();
                if (this.look != '\"') {
                    return token.toString();
                }
            }
            token.append(this.look);
            GetChar();
        }
    }

    private ParseNode Term() {
        ParseNode result = powerFactor();
        while (true) {
            Ptg operator;
            SkipWhite();
            switch (this.look) {
                case '*':
                    Match('*');
                    operator = MultiplyPtg.instance;
                    break;
                case '/':
                    Match('/');
                    operator = DividePtg.instance;
                    break;
                default:
                    return result;
            }
            result = new ParseNode(operator, result, powerFactor());
        }
    }

    private ParseNode unionExpression() {
        ParseNode result = intersectionExpression();
        boolean hasUnions = false;
        while (true) {
            SkipWhite();
            switch (this.look) {
                case ',':
                    GetChar();
                    hasUnions = true;
                    result = new ParseNode(UnionPtg.instance, result, intersectionExpression());
                default:
                    if (hasUnions) {
                        return augmentWithMemPtg(result);
                    }
                    return result;
            }
        }
    }

    private ParseNode intersectionExpression() {
        ParseNode result = comparisonExpression();
        boolean hasIntersections = false;
        while (true) {
            SkipWhite();
            if (!this._inIntersection) {
                break;
            }
            try {
                hasIntersections = true;
                result = new ParseNode(IntersectionPtg.instance, result, comparisonExpression());
            } catch (FormulaParseException e) {
                resetPointer(this._pointer);
            }
        }
        if (hasIntersections) {
            return augmentWithMemPtg(result);
        }
        return result;
    }

    private ParseNode comparisonExpression() {
        ParseNode result = concatExpression();
        while (true) {
            SkipWhite();
            switch (this.look) {
                case '<':
                case '=':
                case '>':
                    result = new ParseNode(getComparisonToken(), result, concatExpression());
                default:
                    return result;
            }
        }
    }

    private Ptg getComparisonToken() {
        if (this.look == '=') {
            Match(this.look);
            return EqualPtg.instance;
        }
        boolean isGreater = this.look == '>';
        Match(this.look);
        if (!isGreater) {
            switch (this.look) {
                case '=':
                    Match('=');
                    return LessEqualPtg.instance;
                case '>':
                    Match('>');
                    return NotEqualPtg.instance;
                default:
                    return LessThanPtg.instance;
            }
        } else if (this.look != '=') {
            return GreaterThanPtg.instance;
        } else {
            Match('=');
            return GreaterEqualPtg.instance;
        }
    }

    private ParseNode concatExpression() {
        ParseNode result = additiveExpression();
        while (true) {
            SkipWhite();
            if (this.look != '&') {
                return result;
            }
            Match('&');
            result = new ParseNode(ConcatPtg.instance, result, additiveExpression());
        }
    }

    private ParseNode additiveExpression() {
        ParseNode result = Term();
        while (true) {
            Ptg operator;
            SkipWhite();
            switch (this.look) {
                case '+':
                    Match('+');
                    operator = AddPtg.instance;
                    break;
                case '-':
                    Match('-');
                    operator = SubtractPtg.instance;
                    break;
                default:
                    return result;
            }
            result = new ParseNode(operator, result, Term());
        }
    }

    private void parse() {
        this._pointer = 0;
        GetChar();
        this._rootNode = unionExpression();
        if (this._pointer <= this._formulaLength) {
            throw new FormulaParseException("Unused input [" + this._formulaString.substring(this._pointer - 1) + "] after attempting to parse the formula [" + this._formulaString + "]");
        }
    }

    private Ptg[] getRPNPtg(FormulaType formulaType) {
        new OperandClassTransformer(formulaType).transformFormula(this._rootNode);
        return ParseNode.toTokenArray(this._rootNode);
    }
}

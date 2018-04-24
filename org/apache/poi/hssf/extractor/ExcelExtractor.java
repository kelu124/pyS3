package org.apache.poi.hssf.extractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Locale;
import org.apache.poi.POIDocument;
import org.apache.poi.POIOLE2TextExtractor;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HeaderFooter;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;

public class ExcelExtractor extends POIOLE2TextExtractor implements org.apache.poi.ss.extractor.ExcelExtractor {
    private final HSSFDataFormatter _formatter;
    private boolean _includeBlankCells;
    private boolean _includeCellComments;
    private boolean _includeHeadersFooters;
    private boolean _includeSheetNames;
    private boolean _shouldEvaluateFormulas;
    private final HSSFWorkbook _wb;

    static /* synthetic */ class C10531 {
        static final /* synthetic */ int[] $SwitchMap$org$apache$poi$ss$usermodel$CellType = new int[CellType.values().length];

        static {
            try {
                $SwitchMap$org$apache$poi$ss$usermodel$CellType[CellType.STRING.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$apache$poi$ss$usermodel$CellType[CellType.NUMERIC.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$apache$poi$ss$usermodel$CellType[CellType.BOOLEAN.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$apache$poi$ss$usermodel$CellType[CellType.ERROR.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$org$apache$poi$ss$usermodel$CellType[CellType.FORMULA.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    private static final class CommandArgs {
        private final boolean _evaluateFormulas;
        private final boolean _headersFooters;
        private final File _inputFile;
        private final boolean _requestHelp;
        private final boolean _showBlankCells;
        private final boolean _showCellComments;
        private final boolean _showSheetNames;

        public CommandArgs(String[] args) throws CommandParseException {
            int nArgs = args.length;
            File inputFile = null;
            boolean requestHelp = false;
            boolean showSheetNames = true;
            boolean evaluateFormulas = true;
            boolean showCellComments = false;
            boolean showBlankCells = false;
            boolean headersFooters = true;
            int i = 0;
            while (i < nArgs) {
                String arg = args[i];
                if ("-help".equalsIgnoreCase(arg)) {
                    requestHelp = true;
                    break;
                }
                if ("-i".equals(arg)) {
                    i++;
                    if (i >= nArgs) {
                        throw new CommandParseException("Expected filename after '-i'");
                    }
                    arg = args[i];
                    if (inputFile != null) {
                        throw new CommandParseException("Only one input file can be supplied");
                    }
                    inputFile = new File(arg);
                    if (!inputFile.exists()) {
                        throw new CommandParseException("Specified input file '" + arg + "' does not exist");
                    } else if (inputFile.isDirectory()) {
                        throw new CommandParseException("Specified input file '" + arg + "' is a directory");
                    }
                } else if ("--show-sheet-names".equals(arg)) {
                    i++;
                    showSheetNames = parseBoolArg(args, i);
                } else if ("--evaluate-formulas".equals(arg)) {
                    i++;
                    evaluateFormulas = parseBoolArg(args, i);
                } else if ("--show-comments".equals(arg)) {
                    i++;
                    showCellComments = parseBoolArg(args, i);
                } else if ("--show-blanks".equals(arg)) {
                    i++;
                    showBlankCells = parseBoolArg(args, i);
                } else if ("--headers-footers".equals(arg)) {
                    i++;
                    headersFooters = parseBoolArg(args, i);
                } else {
                    throw new CommandParseException("Invalid argument '" + arg + "'");
                }
                i++;
            }
            this._requestHelp = requestHelp;
            this._inputFile = inputFile;
            this._showSheetNames = showSheetNames;
            this._evaluateFormulas = evaluateFormulas;
            this._showCellComments = showCellComments;
            this._showBlankCells = showBlankCells;
            this._headersFooters = headersFooters;
        }

        private static boolean parseBoolArg(String[] args, int i) throws CommandParseException {
            if (i >= args.length) {
                throw new CommandParseException("Expected value after '" + args[i - 1] + "'");
            }
            String value = args[i].toUpperCase(Locale.ROOT);
            if ("Y".equals(value) || "YES".equals(value) || "ON".equals(value) || "TRUE".equals(value)) {
                return true;
            }
            if ("N".equals(value) || "NO".equals(value) || "OFF".equals(value) || "FALSE".equals(value)) {
                return false;
            }
            throw new CommandParseException("Invalid value '" + args[i] + "' for '" + args[i - 1] + "'. Expected 'Y' or 'N'");
        }

        public boolean isRequestHelp() {
            return this._requestHelp;
        }

        public File getInputFile() {
            return this._inputFile;
        }

        public boolean shouldShowSheetNames() {
            return this._showSheetNames;
        }

        public boolean shouldEvaluateFormulas() {
            return this._evaluateFormulas;
        }

        public boolean shouldShowCellComments() {
            return this._showCellComments;
        }

        public boolean shouldShowBlankCells() {
            return this._showBlankCells;
        }

        public boolean shouldIncludeHeadersFooters() {
            return this._headersFooters;
        }
    }

    private static final class CommandParseException extends Exception {
        public CommandParseException(String msg) {
            super(msg);
        }
    }

    public ExcelExtractor(HSSFWorkbook wb) {
        super((POIDocument) wb);
        this._includeSheetNames = true;
        this._shouldEvaluateFormulas = true;
        this._includeCellComments = false;
        this._includeBlankCells = false;
        this._includeHeadersFooters = true;
        this._wb = wb;
        this._formatter = new HSSFDataFormatter();
    }

    public ExcelExtractor(POIFSFileSystem fs) throws IOException {
        this(fs.getRoot());
    }

    public ExcelExtractor(DirectoryNode dir) throws IOException {
        this(new HSSFWorkbook(dir, true));
    }

    private static void printUsageMessage(PrintStream ps) {
        ps.println("Use:");
        ps.println("    " + ExcelExtractor.class.getName() + " [<flag> <value> [<flag> <value> [...]]] [-i <filename.xls>]");
        ps.println("       -i <filename.xls> specifies input file (default is to use stdin)");
        ps.println("       Flags can be set on or off by using the values 'Y' or 'N'.");
        ps.println("       Following are available flags and their default values:");
        ps.println("       --show-sheet-names  Y");
        ps.println("       --evaluate-formulas Y");
        ps.println("       --show-comments     N");
        ps.println("       --show-blanks       Y");
        ps.println("       --headers-footers   Y");
    }

    public static void main(String[] args) throws IOException {
        boolean z = true;
        try {
            CommandArgs cmdArgs = new CommandArgs(args);
            if (cmdArgs.isRequestHelp()) {
                printUsageMessage(System.out);
                return;
            }
            InputStream is;
            if (cmdArgs.getInputFile() == null) {
                is = System.in;
            } else {
                is = new FileInputStream(cmdArgs.getInputFile());
            }
            HSSFWorkbook wb = new HSSFWorkbook(is);
            is.close();
            ExcelExtractor extractor = new ExcelExtractor(wb);
            extractor.setIncludeSheetNames(cmdArgs.shouldShowSheetNames());
            if (cmdArgs.shouldEvaluateFormulas()) {
                z = false;
            }
            extractor.setFormulasNotResults(z);
            extractor.setIncludeCellComments(cmdArgs.shouldShowCellComments());
            extractor.setIncludeBlankCells(cmdArgs.shouldShowBlankCells());
            extractor.setIncludeHeadersFooters(cmdArgs.shouldIncludeHeadersFooters());
            System.out.println(extractor.getText());
            extractor.close();
            wb.close();
        } catch (CommandParseException e) {
            System.err.println(e.getMessage());
            printUsageMessage(System.err);
            System.exit(1);
        }
    }

    public void setIncludeSheetNames(boolean includeSheetNames) {
        this._includeSheetNames = includeSheetNames;
    }

    public void setFormulasNotResults(boolean formulasNotResults) {
        this._shouldEvaluateFormulas = !formulasNotResults;
    }

    public void setIncludeCellComments(boolean includeCellComments) {
        this._includeCellComments = includeCellComments;
    }

    public void setIncludeBlankCells(boolean includeBlankCells) {
        this._includeBlankCells = includeBlankCells;
    }

    public void setIncludeHeadersFooters(boolean includeHeadersFooters) {
        this._includeHeadersFooters = includeHeadersFooters;
    }

    public String getText() {
        StringBuffer text = new StringBuffer();
        this._wb.setMissingCellPolicy(MissingCellPolicy.RETURN_BLANK_AS_NULL);
        for (int i = 0; i < this._wb.getNumberOfSheets(); i++) {
            HSSFSheet sheet = this._wb.getSheetAt(i);
            if (sheet != null) {
                if (this._includeSheetNames) {
                    String name = this._wb.getSheetName(i);
                    if (name != null) {
                        text.append(name);
                        text.append("\n");
                    }
                }
                if (this._includeHeadersFooters) {
                    text.append(_extractHeaderFooter(sheet.getHeader()));
                }
                int firstRow = sheet.getFirstRowNum();
                int lastRow = sheet.getLastRowNum();
                for (int j = firstRow; j <= lastRow; j++) {
                    HSSFRow row = sheet.getRow(j);
                    if (row != null) {
                        int firstCell = row.getFirstCellNum();
                        int lastCell = row.getLastCellNum();
                        if (this._includeBlankCells) {
                            firstCell = 0;
                        }
                        int k = firstCell;
                        while (k < lastCell) {
                            HSSFCell cell = row.getCell(k);
                            boolean outputContents = true;
                            if (cell == null) {
                                outputContents = this._includeBlankCells;
                            } else {
                                switch (C10531.$SwitchMap$org$apache$poi$ss$usermodel$CellType[cell.getCellTypeEnum().ordinal()]) {
                                    case 1:
                                        text.append(cell.getRichStringCellValue().getString());
                                        break;
                                    case 2:
                                        text.append(this._formatter.formatCellValue(cell));
                                        break;
                                    case 3:
                                        text.append(cell.getBooleanCellValue());
                                        break;
                                    case 4:
                                        text.append(ErrorEval.getText(cell.getErrorCellValue()));
                                        break;
                                    case 5:
                                        if (!this._shouldEvaluateFormulas) {
                                            text.append(cell.getCellFormula());
                                            break;
                                        }
                                        switch (C10531.$SwitchMap$org$apache$poi$ss$usermodel$CellType[cell.getCachedFormulaResultTypeEnum().ordinal()]) {
                                            case 1:
                                                HSSFRichTextString str = cell.getRichStringCellValue();
                                                if (str != null && str.length() > 0) {
                                                    text.append(str.toString());
                                                    break;
                                                }
                                            case 2:
                                                HSSFCellStyle style = cell.getCellStyle();
                                                text.append(this._formatter.formatRawCellContents(cell.getNumericCellValue(), style.getDataFormat(), style.getDataFormatString()));
                                                break;
                                            case 3:
                                                text.append(cell.getBooleanCellValue());
                                                break;
                                            case 4:
                                                text.append(ErrorEval.getText(cell.getErrorCellValue()));
                                                break;
                                            default:
                                                throw new IllegalStateException("Unexpected cell cached formula result type: " + cell.getCachedFormulaResultTypeEnum());
                                        }
                                    default:
                                        throw new RuntimeException("Unexpected cell type (" + cell.getCellTypeEnum() + ")");
                                }
                                HSSFComment comment = cell.getCellComment();
                                if (this._includeCellComments && comment != null) {
                                    text.append(" Comment by " + comment.getAuthor() + ": " + comment.getString().getString().replace('\n', ' '));
                                }
                            }
                            if (outputContents && k < lastCell - 1) {
                                text.append("\t");
                            }
                            k++;
                        }
                        text.append("\n");
                    }
                }
                if (this._includeHeadersFooters) {
                    text.append(_extractHeaderFooter(sheet.getFooter()));
                }
            }
        }
        return text.toString();
    }

    public static String _extractHeaderFooter(HeaderFooter hf) {
        StringBuffer text = new StringBuffer();
        if (hf.getLeft() != null) {
            text.append(hf.getLeft());
        }
        if (hf.getCenter() != null) {
            if (text.length() > 0) {
                text.append("\t");
            }
            text.append(hf.getCenter());
        }
        if (hf.getRight() != null) {
            if (text.length() > 0) {
                text.append("\t");
            }
            text.append(hf.getRight());
        }
        if (text.length() > 0) {
            text.append("\n");
        }
        return text.toString();
    }
}

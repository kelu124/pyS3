package org.apache.poi.ss.usermodel;

import com.itextpdf.text.html.HtmlTags;
import com.itextpdf.text.pdf.Barcode128;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.ss.format.CellFormat;
import org.apache.poi.ss.format.CellFormatResult;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.util.LocaleUtil;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

public class DataFormatter implements Observer {
    private static final Pattern alternateGrouping = Pattern.compile("([#0]([^.#0])[#0]{3})");
    private static final Pattern amPmPattern = Pattern.compile("((A|P)[M/P]*)", 2);
    private static final Pattern colorPattern = Pattern.compile("(\\[BLACK\\])|(\\[BLUE\\])|(\\[CYAN\\])|(\\[GREEN\\])|(\\[MAGENTA\\])|(\\[RED\\])|(\\[WHITE\\])|(\\[YELLOW\\])|(\\[COLOR\\s*\\d\\])|(\\[COLOR\\s*[0-5]\\d\\])", 2);
    private static final Pattern daysAsText = Pattern.compile("([d]{3,})", 2);
    private static final String defaultFractionFractionPartFormat = "#/##";
    private static final String defaultFractionWholePartFormat = "#";
    private static final Pattern fractionPattern = Pattern.compile("(?:([#\\d]+)\\s+)?(#+)\\s*\\/\\s*([#\\d]+)");
    private static final Pattern fractionStripper = Pattern.compile("(\"[^\"]*\")|([^ \\?#\\d\\/]+)");
    private static final String invalidDateTimeString;
    private static final Pattern localePatternGroup = Pattern.compile("(\\[\\$[^-\\]]*-[0-9A-Z]+\\])");
    private static POILogger logger = POILogFactory.getLogger(DataFormatter.class);
    private static final Pattern numPattern = Pattern.compile("[0#]+");
    private DateFormatSymbols dateSymbols;
    private DecimalFormatSymbols decimalSymbols;
    private DateFormat defaultDateformat;
    private Format defaultNumFormat;
    private final boolean emulateCSV;
    private final Map<String, Format> formats;
    private Format generalNumberFormat;
    private Locale locale;
    private final LocaleChangeObservable localeChangedObservable;
    private boolean localeIsAdapting;

    static /* synthetic */ class C11911 {
        static final /* synthetic */ int[] $SwitchMap$org$apache$poi$ss$usermodel$CellType = new int[CellType.values().length];

        static {
            try {
                $SwitchMap$org$apache$poi$ss$usermodel$CellType[CellType.NUMERIC.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$apache$poi$ss$usermodel$CellType[CellType.STRING.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$apache$poi$ss$usermodel$CellType[CellType.BOOLEAN.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$apache$poi$ss$usermodel$CellType[CellType.BLANK.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$org$apache$poi$ss$usermodel$CellType[CellType.ERROR.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    private final class CellFormatResultWrapper extends Format {
        private final CellFormatResult result;

        private CellFormatResultWrapper(CellFormatResult result) {
            this.result = result;
        }

        public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
            if (DataFormatter.this.emulateCSV) {
                return toAppendTo.append(this.result.text);
            }
            return toAppendTo.append(this.result.text.trim());
        }

        public Object parseObject(String source, ParsePosition pos) {
            return null;
        }
    }

    private static final class ConstantStringFormat extends Format {
        private static final DecimalFormat df = DataFormatter.createIntegerOnlyFormat("##########");
        private final String str;

        public ConstantStringFormat(String s) {
            this.str = s;
        }

        public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
            return toAppendTo.append(this.str);
        }

        public Object parseObject(String source, ParsePosition pos) {
            return df.parseObject(source, pos);
        }
    }

    private class LocaleChangeObservable extends Observable {
        private LocaleChangeObservable() {
        }

        void checkForLocaleChange() {
            checkForLocaleChange(LocaleUtil.getUserLocale());
        }

        void checkForLocaleChange(Locale newLocale) {
            if (DataFormatter.this.localeIsAdapting && !newLocale.equals(DataFormatter.this.locale)) {
                super.setChanged();
                notifyObservers(newLocale);
            }
        }
    }

    private static final class PhoneFormat extends Format {
        private static final DecimalFormat df = DataFormatter.createIntegerOnlyFormat("##########");
        public static final Format instance = new PhoneFormat();

        private PhoneFormat() {
        }

        public static String format(Number num) {
            String result = df.format(num);
            StringBuilder sb = new StringBuilder();
            int len = result.length();
            if (len <= 4) {
                return result;
            }
            String seg3 = result.substring(len - 4, len);
            String seg2 = result.substring(Math.max(0, len - 7), len - 4);
            String seg1 = result.substring(Math.max(0, len - 10), Math.max(0, len - 7));
            if (seg1.trim().length() > 0) {
                sb.append('(').append(seg1).append(") ");
            }
            if (seg2.trim().length() > 0) {
                sb.append(seg2).append('-');
            }
            sb.append(seg3);
            return sb.toString();
        }

        public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
            return toAppendTo.append(format((Number) obj));
        }

        public Object parseObject(String source, ParsePosition pos) {
            return df.parseObject(source, pos);
        }
    }

    private static final class SSNFormat extends Format {
        private static final DecimalFormat df = DataFormatter.createIntegerOnlyFormat("000000000");
        public static final Format instance = new SSNFormat();

        private SSNFormat() {
        }

        public static String format(Number num) {
            String result = df.format(num);
            return result.substring(0, 3) + '-' + result.substring(3, 5) + '-' + result.substring(5, 9);
        }

        public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
            return toAppendTo.append(format((Number) obj));
        }

        public Object parseObject(String source, ParsePosition pos) {
            return df.parseObject(source, pos);
        }
    }

    private static final class ZipPlusFourFormat extends Format {
        private static final DecimalFormat df = DataFormatter.createIntegerOnlyFormat("000000000");
        public static final Format instance = new ZipPlusFourFormat();

        private ZipPlusFourFormat() {
        }

        public static String format(Number num) {
            String result = df.format(num);
            return result.substring(0, 5) + '-' + result.substring(5, 9);
        }

        public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
            return toAppendTo.append(format((Number) obj));
        }

        public Object parseObject(String source, ParsePosition pos) {
            return df.parseObject(source, pos);
        }
    }

    static {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < 255; i++) {
            buf.append('#');
        }
        invalidDateTimeString = buf.toString();
    }

    public DataFormatter() {
        this(false);
    }

    public DataFormatter(boolean emulateCSV) {
        this(LocaleUtil.getUserLocale(), true, emulateCSV);
    }

    public DataFormatter(Locale locale) {
        this(locale, false);
    }

    public DataFormatter(Locale locale, boolean emulateCSV) {
        this(locale, false, emulateCSV);
    }

    private DataFormatter(Locale locale, boolean localeIsAdapting, boolean emulateCSV) {
        this.formats = new HashMap();
        this.localeChangedObservable = new LocaleChangeObservable();
        this.localeIsAdapting = true;
        this.localeChangedObservable.addObserver(this);
        this.localeChangedObservable.checkForLocaleChange(locale);
        this.localeIsAdapting = localeIsAdapting;
        this.emulateCSV = emulateCSV;
    }

    private Format getFormat(Cell cell) {
        if (cell.getCellStyle() == null) {
            return null;
        }
        int formatIndex = cell.getCellStyle().getDataFormat();
        String formatStr = cell.getCellStyle().getDataFormatString();
        if (formatStr == null || formatStr.trim().length() == 0) {
            return null;
        }
        return getFormat(cell.getNumericCellValue(), formatIndex, formatStr);
    }

    private Format getFormat(double cellValue, int formatIndex, String formatStrIn) {
        this.localeChangedObservable.checkForLocaleChange();
        String formatStr = formatStrIn;
        if (formatStr.contains(";") && formatStr.indexOf(59) != formatStr.lastIndexOf(59)) {
            try {
                CellFormat cfmt = CellFormat.getInstance(formatStr);
                Object cellValueO = Double.valueOf(cellValue);
                if (DateUtil.isADateFormat(formatIndex, formatStr) && ((Double) cellValueO).doubleValue() != 0.0d) {
                    cellValueO = DateUtil.getJavaDate(cellValue);
                }
                return new CellFormatResultWrapper(cfmt.apply(cellValueO));
            } catch (Exception e) {
                logger.log(5, new Object[]{"Formatting failed for format " + formatStr + ", falling back", e});
            }
        }
        if (this.emulateCSV && cellValue == 0.0d && formatStr.contains(defaultFractionWholePartFormat) && !formatStr.contains("0")) {
            formatStr = formatStr.replaceAll(defaultFractionWholePartFormat, "");
        }
        Format format = (Format) this.formats.get(formatStr);
        if (format != null) {
            return format;
        }
        if ("General".equalsIgnoreCase(formatStr) || "@".equals(formatStr)) {
            return this.generalNumberFormat;
        }
        format = createFormat(cellValue, formatIndex, formatStr);
        this.formats.put(formatStr, format);
        return format;
    }

    public Format createFormat(Cell cell) {
        return createFormat(cell.getNumericCellValue(), cell.getCellStyle().getDataFormat(), cell.getCellStyle().getDataFormatString());
    }

    private Format createFormat(double cellValue, int formatIndex, String sFormat) {
        this.localeChangedObservable.checkForLocaleChange();
        String formatStr = sFormat;
        Matcher colourM = colorPattern.matcher(formatStr);
        while (colourM.find()) {
            String colour = colourM.group();
            int at = formatStr.indexOf(colour);
            if (at != -1) {
                String nFormatStr = formatStr.substring(0, at) + formatStr.substring(colour.length() + at);
                if (nFormatStr.equals(formatStr)) {
                    break;
                }
                formatStr = nFormatStr;
                colourM = colorPattern.matcher(formatStr);
            } else {
                break;
            }
        }
        Matcher m = localePatternGroup.matcher(formatStr);
        while (m.find()) {
            String match = m.group();
            String symbol = match.substring(match.indexOf(36) + 1, match.indexOf(45));
            if (symbol.indexOf(36) > -1) {
                symbol = symbol.substring(0, symbol.indexOf(36)) + '\\' + symbol.substring(symbol.indexOf(36), symbol.length());
            }
            formatStr = m.replaceAll(symbol);
            m = localePatternGroup.matcher(formatStr);
        }
        if (formatStr == null || formatStr.trim().length() == 0) {
            return getDefaultFormat(cellValue);
        }
        if ("General".equalsIgnoreCase(formatStr) || "@".equals(formatStr)) {
            return this.generalNumberFormat;
        }
        if (DateUtil.isADateFormat(formatIndex, formatStr) && DateUtil.isValidExcelDate(cellValue)) {
            return createDateFormat(formatStr, cellValue);
        }
        if (formatStr.contains("#/") || formatStr.contains("?/")) {
            for (String chunk1 : formatStr.split(";")) {
                Matcher fractionMatcher = fractionPattern.matcher(fractionStripper.matcher(chunk1.replaceAll("\\?", defaultFractionWholePartFormat)).replaceAll(" ").replaceAll(" +", " "));
                if (fractionMatcher.find()) {
                    return new FractionFormat(fractionMatcher.group(1) == null ? "" : defaultFractionWholePartFormat, fractionMatcher.group(3));
                }
            }
            return new FractionFormat(defaultFractionWholePartFormat, defaultFractionFractionPartFormat);
        } else if (numPattern.matcher(formatStr).find()) {
            return createNumberFormat(formatStr, cellValue);
        } else {
            if (this.emulateCSV) {
                return new ConstantStringFormat(cleanFormatForNumber(formatStr));
            }
            return null;
        }
    }

    private Format createDateFormat(String pFormatStr, double cellValue) {
        String formatStr = pFormatStr.replaceAll("\\\\-", "-").replaceAll("\\\\,", ",").replaceAll("\\\\\\.", ".").replaceAll("\\\\ ", " ").replaceAll("\\\\/", "/").replaceAll(";@", "").replaceAll("\"/\"", "/").replace("\"\"", "'").replaceAll("\\\\T", "'T'");
        boolean hasAmPm = false;
        Matcher amPmMatcher = amPmPattern.matcher(formatStr);
        while (amPmMatcher.find()) {
            formatStr = amPmMatcher.replaceAll("@");
            hasAmPm = true;
            amPmMatcher = amPmPattern.matcher(formatStr);
        }
        formatStr = formatStr.replaceAll("@", HtmlTags.f32A);
        Matcher dateMatcher = daysAsText.matcher(formatStr);
        if (dateMatcher.find()) {
            formatStr = dateMatcher.replaceAll(dateMatcher.group(0).toUpperCase(Locale.ROOT).replaceAll("D", "E"));
        }
        StringBuilder sb = new StringBuilder();
        char[] chars = formatStr.toCharArray();
        boolean mIsMonth = true;
        List<Integer> ms = new ArrayList();
        boolean isElapsed = false;
        int j = 0;
        while (j < chars.length) {
            char c = chars[j];
            if (c == '\'') {
                sb.append(c);
                j++;
                while (j < chars.length) {
                    c = chars[j];
                    sb.append(c);
                    if (c == '\'') {
                        break;
                    }
                    j++;
                }
            } else if (c == '[' && !isElapsed) {
                isElapsed = true;
                mIsMonth = false;
                sb.append(c);
            } else if (c == ']' && isElapsed) {
                isElapsed = false;
                sb.append(c);
            } else if (isElapsed) {
                if (c == 'h' || c == 'H') {
                    sb.append('H');
                } else if (c == 'm' || c == 'M') {
                    sb.append('m');
                } else if (c == 's' || c == 'S') {
                    sb.append('s');
                } else {
                    sb.append(c);
                }
            } else if (c == 'h' || c == 'H') {
                mIsMonth = false;
                if (hasAmPm) {
                    sb.append(Barcode128.START_B);
                } else {
                    sb.append('H');
                }
            } else if (c == 'm' || c == 'M') {
                if (mIsMonth) {
                    sb.append('M');
                    ms.add(Integer.valueOf(sb.length() - 1));
                } else {
                    sb.append('m');
                }
            } else if (c == 's' || c == 'S') {
                sb.append('s');
                for (Integer intValue : ms) {
                    int index = intValue.intValue();
                    if (sb.charAt(index) == 'M') {
                        sb.replace(index, index + 1, "m");
                    }
                }
                mIsMonth = true;
                ms.clear();
            } else if (Character.isLetter(c)) {
                mIsMonth = true;
                ms.clear();
                if (c == 'y' || c == 'Y') {
                    sb.append('y');
                } else if (c == 'd' || c == 'D') {
                    sb.append(Barcode128.CODE_AC_TO_B);
                } else {
                    sb.append(c);
                }
            } else {
                sb.append(c);
            }
            j++;
        }
        try {
            return new ExcelStyleDateFormatter(sb.toString(), this.dateSymbols);
        } catch (IllegalArgumentException e) {
            return getDefaultFormat(cellValue);
        }
    }

    private String cleanFormatForNumber(String formatStr) {
        int i;
        char c;
        StringBuilder sb = new StringBuilder(formatStr);
        if (this.emulateCSV) {
            i = 0;
            while (i < sb.length()) {
                c = sb.charAt(i);
                if ((c == '_' || c == '*' || c == '?') && (i <= 0 || sb.charAt(i - 1) != '\\')) {
                    if (c == '?') {
                        sb.setCharAt(i, ' ');
                    } else if (i < sb.length() - 1) {
                        if (c == '_') {
                            sb.setCharAt(i + 1, ' ');
                        } else {
                            sb.deleteCharAt(i + 1);
                        }
                        sb.deleteCharAt(i);
                        i--;
                    }
                }
                i++;
            }
        } else {
            i = 0;
            while (i < sb.length()) {
                c = sb.charAt(i);
                if ((c == '_' || c == '*') && (i <= 0 || sb.charAt(i - 1) != '\\')) {
                    if (i < sb.length() - 1) {
                        sb.deleteCharAt(i + 1);
                    }
                    sb.deleteCharAt(i);
                    i--;
                }
                i++;
            }
        }
        i = 0;
        while (i < sb.length()) {
            c = sb.charAt(i);
            if (c == '\\' || c == '\"') {
                sb.deleteCharAt(i);
                i--;
            } else if (c == '+' && i > 0 && sb.charAt(i - 1) == 'E') {
                sb.deleteCharAt(i);
                i--;
            }
            i++;
        }
        return sb.toString();
    }

    private Format createNumberFormat(String formatStr, double cellValue) {
        String format = cleanFormatForNumber(formatStr);
        DecimalFormatSymbols symbols = this.decimalSymbols;
        Matcher agm = alternateGrouping.matcher(format);
        if (agm.find()) {
            char grouping = agm.group(2).charAt(0);
            if (grouping != ',') {
                symbols = DecimalFormatSymbols.getInstance(this.locale);
                symbols.setGroupingSeparator(grouping);
                String oldPart = agm.group(1);
                format = format.replace(oldPart, oldPart.replace(grouping, ','));
            }
        }
        try {
            DecimalFormat df = new DecimalFormat(format, symbols);
            setExcelStyleRoundingMode(df);
            return df;
        } catch (IllegalArgumentException e) {
            return getDefaultFormat(cellValue);
        }
    }

    public Format getDefaultFormat(Cell cell) {
        return getDefaultFormat(cell.getNumericCellValue());
    }

    private Format getDefaultFormat(double cellValue) {
        this.localeChangedObservable.checkForLocaleChange();
        if (this.defaultNumFormat != null) {
            return this.defaultNumFormat;
        }
        return this.generalNumberFormat;
    }

    private String performDateFormatting(Date d, Format dateFormat) {
        if (dateFormat == null) {
            dateFormat = this.defaultDateformat;
        }
        return dateFormat.format(d);
    }

    private String getFormattedDateString(Cell cell) {
        Format dateFormat = getFormat(cell);
        if (dateFormat instanceof ExcelStyleDateFormatter) {
            ((ExcelStyleDateFormatter) dateFormat).setDateToBeFormatted(cell.getNumericCellValue());
        }
        return performDateFormatting(cell.getDateCellValue(), dateFormat);
    }

    private String getFormattedNumberString(Cell cell) {
        Format numberFormat = getFormat(cell);
        double d = cell.getNumericCellValue();
        if (numberFormat == null) {
            return String.valueOf(d);
        }
        return numberFormat.format(new Double(d)).replaceFirst("E(\\d)", "E+$1");
    }

    public String formatRawCellContents(double value, int formatIndex, String formatString) {
        return formatRawCellContents(value, formatIndex, formatString, false);
    }

    public String formatRawCellContents(double value, int formatIndex, String formatString, boolean use1904Windowing) {
        this.localeChangedObservable.checkForLocaleChange();
        if (DateUtil.isADateFormat(formatIndex, formatString)) {
            if (DateUtil.isValidExcelDate(value)) {
                Format dateFormat = getFormat(value, formatIndex, formatString);
                if (dateFormat instanceof ExcelStyleDateFormatter) {
                    ((ExcelStyleDateFormatter) dateFormat).setDateToBeFormatted(value);
                }
                return performDateFormatting(DateUtil.getJavaDate(value, use1904Windowing), dateFormat);
            } else if (this.emulateCSV) {
                return invalidDateTimeString;
            }
        }
        Format numberFormat = getFormat(value, formatIndex, formatString);
        if (numberFormat == null) {
            return String.valueOf(value);
        }
        String result;
        String textValue = NumberToTextConverter.toText(value);
        if (textValue.indexOf(69) > -1) {
            result = numberFormat.format(new Double(value));
        } else {
            result = numberFormat.format(new BigDecimal(textValue));
        }
        if (result.indexOf(69) <= -1 || result.contains("E-")) {
            return result;
        }
        return result.replaceFirst("E", "E+");
    }

    public String formatCellValue(Cell cell) {
        return formatCellValue(cell, null);
    }

    public String formatCellValue(Cell cell, FormulaEvaluator evaluator) {
        this.localeChangedObservable.checkForLocaleChange();
        if (cell == null) {
            return "";
        }
        CellType cellType = cell.getCellTypeEnum();
        if (cellType == CellType.FORMULA) {
            if (evaluator == null) {
                return cell.getCellFormula();
            }
            cellType = evaluator.evaluateFormulaCellEnum(cell);
        }
        switch (C11911.$SwitchMap$org$apache$poi$ss$usermodel$CellType[cellType.ordinal()]) {
            case 1:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return getFormattedDateString(cell);
                }
                return getFormattedNumberString(cell);
            case 2:
                return cell.getRichStringCellValue().getString();
            case 3:
                return cell.getBooleanCellValue() ? "TRUE" : "FALSE";
            case 4:
                return "";
            case 5:
                return FormulaError.forInt(cell.getErrorCellValue()).getString();
            default:
                throw new RuntimeException("Unexpected celltype (" + cellType + ")");
        }
    }

    public void setDefaultNumberFormat(Format format) {
        for (Entry<String, Format> entry : this.formats.entrySet()) {
            if (entry.getValue() == this.generalNumberFormat) {
                entry.setValue(format);
            }
        }
        this.defaultNumFormat = format;
    }

    public void addFormat(String excelFormatStr, Format format) {
        this.formats.put(excelFormatStr, format);
    }

    private static DecimalFormat createIntegerOnlyFormat(String fmt) {
        DecimalFormat result = new DecimalFormat(fmt, DecimalFormatSymbols.getInstance(Locale.ROOT));
        result.setParseIntegerOnly(true);
        return result;
    }

    public static void setExcelStyleRoundingMode(DecimalFormat format) {
        setExcelStyleRoundingMode(format, RoundingMode.HALF_UP);
    }

    public static void setExcelStyleRoundingMode(DecimalFormat format, RoundingMode roundingMode) {
        format.setRoundingMode(roundingMode);
    }

    public Observable getLocaleChangedObservable() {
        return this.localeChangedObservable;
    }

    public void update(Observable observable, Object localeObj) {
        if (localeObj instanceof Locale) {
            Locale newLocale = (Locale) localeObj;
            if (this.localeIsAdapting && !newLocale.equals(this.locale)) {
                this.locale = newLocale;
                this.dateSymbols = DateFormatSymbols.getInstance(this.locale);
                this.decimalSymbols = DecimalFormatSymbols.getInstance(this.locale);
                this.generalNumberFormat = new ExcelGeneralNumberFormat(this.locale);
                this.defaultDateformat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", this.dateSymbols);
                this.defaultDateformat.setTimeZone(LocaleUtil.getUserTimeZone());
                this.formats.clear();
                Format zipFormat = ZipPlusFourFormat.instance;
                addFormat("00000\\-0000", zipFormat);
                addFormat("00000-0000", zipFormat);
                Format phoneFormat = PhoneFormat.instance;
                addFormat("[<=9999999]###\\-####;\\(###\\)\\ ###\\-####", phoneFormat);
                addFormat("[<=9999999]###-####;(###) ###-####", phoneFormat);
                addFormat("###\\-####;\\(###\\)\\ ###\\-####", phoneFormat);
                addFormat("###-####;(###) ###-####", phoneFormat);
                Format ssnFormat = SSNFormat.instance;
                addFormat("000\\-00\\-0000", ssnFormat);
                addFormat("000-00-0000", ssnFormat);
            }
        }
    }
}

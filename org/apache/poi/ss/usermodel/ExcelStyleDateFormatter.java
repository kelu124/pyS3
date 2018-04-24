package org.apache.poi.ss.usermodel;

import java.math.RoundingMode;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.apache.poi.util.LocaleUtil;

public class ExcelStyleDateFormatter extends SimpleDateFormat {
    public static final char HH_BRACKET_SYMBOL = '';
    public static final char H_BRACKET_SYMBOL = '';
    public static final char LL_BRACKET_SYMBOL = '';
    public static final char L_BRACKET_SYMBOL = '';
    public static final char MMMMM_START_SYMBOL = '';
    public static final char MMMMM_TRUNCATE_SYMBOL = '';
    public static final char MM_BRACKET_SYMBOL = '';
    public static final char M_BRACKET_SYMBOL = '';
    public static final char SS_BRACKET_SYMBOL = '';
    public static final char S_BRACKET_SYMBOL = '';
    private static final DecimalFormat format1digit;
    private static final DecimalFormat format2digits;
    private static final DecimalFormat format3digit;
    private static final DecimalFormat format4digits;
    private double dateToBeFormatted = 0.0d;

    static {
        DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance(Locale.ROOT);
        format1digit = new DecimalFormat("0", dfs);
        format2digits = new DecimalFormat("00", dfs);
        format3digit = new DecimalFormat("0", dfs);
        format4digits = new DecimalFormat("00", dfs);
        DataFormatter.setExcelStyleRoundingMode(format1digit, RoundingMode.DOWN);
        DataFormatter.setExcelStyleRoundingMode(format2digits, RoundingMode.DOWN);
        DataFormatter.setExcelStyleRoundingMode(format3digit);
        DataFormatter.setExcelStyleRoundingMode(format4digits);
    }

    public ExcelStyleDateFormatter(String pattern) {
        super(processFormatPattern(pattern), LocaleUtil.getUserLocale());
        setTimeZone(LocaleUtil.getUserTimeZone());
    }

    public ExcelStyleDateFormatter(String pattern, DateFormatSymbols formatSymbols) {
        super(processFormatPattern(pattern), formatSymbols);
        setTimeZone(LocaleUtil.getUserTimeZone());
    }

    public ExcelStyleDateFormatter(String pattern, Locale locale) {
        super(processFormatPattern(pattern), locale);
        setTimeZone(LocaleUtil.getUserTimeZone());
    }

    private static String processFormatPattern(String f) {
        return f.replaceAll("MMMMM", "MMM").replaceAll("\\[H\\]", String.valueOf(H_BRACKET_SYMBOL)).replaceAll("\\[HH\\]", String.valueOf(HH_BRACKET_SYMBOL)).replaceAll("\\[m\\]", String.valueOf(M_BRACKET_SYMBOL)).replaceAll("\\[mm\\]", String.valueOf(MM_BRACKET_SYMBOL)).replaceAll("\\[s\\]", String.valueOf(S_BRACKET_SYMBOL)).replaceAll("\\[ss\\]", String.valueOf(SS_BRACKET_SYMBOL)).replaceAll("s.000", "s.SSS").replaceAll("s.00", "s.").replaceAll("s.0", "s.");
    }

    public void setDateToBeFormatted(double date) {
        this.dateToBeFormatted = date;
    }

    public StringBuffer format(Date date, StringBuffer paramStringBuffer, FieldPosition paramFieldPosition) {
        String s = super.format(date, paramStringBuffer, paramFieldPosition).toString();
        if (s.indexOf(57345) != -1) {
            s = s.replaceAll("(\\w)\\w+", "$1");
        }
        if (!(s.indexOf(57360) == -1 && s.indexOf(57361) == -1)) {
            float hours = ((float) this.dateToBeFormatted) * 24.0f;
            s = s.replaceAll(String.valueOf(H_BRACKET_SYMBOL), format1digit.format((double) hours)).replaceAll(String.valueOf(HH_BRACKET_SYMBOL), format2digits.format((double) hours));
        }
        if (!(s.indexOf(57362) == -1 && s.indexOf(57363) == -1)) {
            float minutes = (((float) this.dateToBeFormatted) * 24.0f) * 60.0f;
            s = s.replaceAll(String.valueOf(M_BRACKET_SYMBOL), format1digit.format((double) minutes)).replaceAll(String.valueOf(MM_BRACKET_SYMBOL), format2digits.format((double) minutes));
        }
        if (!(s.indexOf(57364) == -1 && s.indexOf(57365) == -1)) {
            float seconds = (float) (((this.dateToBeFormatted * 24.0d) * 60.0d) * 60.0d);
            s = s.replaceAll(String.valueOf(S_BRACKET_SYMBOL), format1digit.format((double) seconds)).replaceAll(String.valueOf(SS_BRACKET_SYMBOL), format2digits.format((double) seconds));
        }
        if (!(s.indexOf(57366) == -1 && s.indexOf(57367) == -1)) {
            float millisTemp = (float) ((((this.dateToBeFormatted - Math.floor(this.dateToBeFormatted)) * 24.0d) * 60.0d) * 60.0d);
            float millis = millisTemp - ((float) ((int) millisTemp));
            s = s.replaceAll(String.valueOf(L_BRACKET_SYMBOL), format3digit.format((double) (10.0f * millis))).replaceAll(String.valueOf(LL_BRACKET_SYMBOL), format4digits.format((double) (100.0f * millis)));
        }
        return new StringBuffer(s);
    }

    public boolean equals(Object o) {
        if (!(o instanceof ExcelStyleDateFormatter)) {
            return false;
        }
        if (this.dateToBeFormatted == ((ExcelStyleDateFormatter) o).dateToBeFormatted) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return new Double(this.dateToBeFormatted).hashCode();
    }
}

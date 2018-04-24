package org.apache.poi.ss.usermodel;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;
import org.apache.poi.util.LocaleUtil;
import org.bytedeco.javacpp.avutil;

public class DateUtil {
    private static final int BAD_DATE = -1;
    public static final long DAY_MILLISECONDS = 86400000;
    public static final int HOURS_PER_DAY = 24;
    public static final int MINUTES_PER_HOUR = 60;
    public static final int SECONDS_PER_DAY = 86400;
    public static final int SECONDS_PER_MINUTE = 60;
    private static final Pattern TIME_SEPARATOR_PATTERN = Pattern.compile(":");
    private static final Pattern date_ptrn1 = Pattern.compile("^\\[\\$\\-.*?\\]");
    private static final Pattern date_ptrn2 = Pattern.compile("^\\[[a-zA-Z]+\\]");
    private static final Pattern date_ptrn3a = Pattern.compile("[yYmMdDhHsS]");
    private static final Pattern date_ptrn3b = Pattern.compile("^[\\[\\]yYmMdDhHsS\\-T/,. :\"\\\\]+0*[ampAMP/]*$");
    private static final Pattern date_ptrn4 = Pattern.compile("^\\[([hH]+|[mM]+|[sS]+)\\]");
    private static ThreadLocal<Boolean> lastCachedResult = new ThreadLocal();
    private static ThreadLocal<Integer> lastFormatIndex = new C11921();
    private static ThreadLocal<String> lastFormatString = new ThreadLocal();

    static class C11921 extends ThreadLocal<Integer> {
        C11921() {
        }

        protected Integer initialValue() {
            return Integer.valueOf(-1);
        }
    }

    private static final class FormatException extends Exception {
        public FormatException(String msg) {
            super(msg);
        }
    }

    protected DateUtil() {
    }

    public static double getExcelDate(Date date) {
        return getExcelDate(date, false);
    }

    public static double getExcelDate(Date date, boolean use1904windowing) {
        Calendar calStart = LocaleUtil.getLocaleCalendar();
        calStart.setTime(date);
        return internalGetExcelDate(calStart, use1904windowing);
    }

    public static double getExcelDate(Calendar date, boolean use1904windowing) {
        return internalGetExcelDate((Calendar) date.clone(), use1904windowing);
    }

    private static double internalGetExcelDate(Calendar date, boolean use1904windowing) {
        if ((!use1904windowing && date.get(1) < 1900) || (use1904windowing && date.get(1) < 1904)) {
            return -1.0d;
        }
        double value = (((double) ((((((date.get(11) * 60) + date.get(12)) * 60) + date.get(13)) * 1000) + date.get(14))) / 8.64E7d) + ((double) absoluteDay(dayStart(date), use1904windowing));
        if (!use1904windowing && value >= 60.0d) {
            return value + 1.0d;
        }
        if (use1904windowing) {
            return value - 1.0d;
        }
        return value;
    }

    public static Date getJavaDate(double date, TimeZone tz) {
        return getJavaDate(date, false, tz, false);
    }

    public static Date getJavaDate(double date) {
        return getJavaDate(date, false, null, false);
    }

    public static Date getJavaDate(double date, boolean use1904windowing, TimeZone tz) {
        return getJavaDate(date, use1904windowing, tz, false);
    }

    public static Date getJavaDate(double date, boolean use1904windowing, TimeZone tz, boolean roundSeconds) {
        Calendar calendar = getJavaCalendar(date, use1904windowing, tz, roundSeconds);
        return calendar == null ? null : calendar.getTime();
    }

    public static Date getJavaDate(double date, boolean use1904windowing) {
        return getJavaDate(date, use1904windowing, null, false);
    }

    public static void setCalendar(Calendar calendar, int wholeDays, int millisecondsInDay, boolean use1904windowing, boolean roundSeconds) {
        int startYear = 1900;
        int dayAdjust = -1;
        if (use1904windowing) {
            startYear = 1904;
            dayAdjust = 1;
        } else if (wholeDays < 61) {
            dayAdjust = 0;
        }
        calendar.set(startYear, 0, wholeDays + dayAdjust, 0, 0, 0);
        calendar.set(14, millisecondsInDay);
        if (calendar.get(14) == 0) {
            calendar.clear(14);
        }
        if (roundSeconds) {
            calendar.add(14, 500);
            calendar.clear(14);
        }
    }

    public static Calendar getJavaCalendar(double date) {
        return getJavaCalendar(date, false, (TimeZone) null, false);
    }

    public static Calendar getJavaCalendar(double date, boolean use1904windowing) {
        return getJavaCalendar(date, use1904windowing, (TimeZone) null, false);
    }

    public static Calendar getJavaCalendarUTC(double date, boolean use1904windowing) {
        return getJavaCalendar(date, use1904windowing, LocaleUtil.TIMEZONE_UTC, false);
    }

    public static Calendar getJavaCalendar(double date, boolean use1904windowing, TimeZone timeZone) {
        return getJavaCalendar(date, use1904windowing, timeZone, false);
    }

    public static Calendar getJavaCalendar(double date, boolean use1904windowing, TimeZone timeZone, boolean roundSeconds) {
        if (!isValidExcelDate(date)) {
            return null;
        }
        Calendar calendar;
        int wholeDays = (int) Math.floor(date);
        int millisecondsInDay = (int) (((date - ((double) wholeDays)) * 8.64E7d) + 0.5d);
        if (timeZone != null) {
            calendar = LocaleUtil.getLocaleCalendar(timeZone);
        } else {
            calendar = LocaleUtil.getLocaleCalendar();
        }
        setCalendar(calendar, wholeDays, millisecondsInDay, use1904windowing, roundSeconds);
        return calendar;
    }

    private static boolean isCached(String formatString, int formatIndex) {
        String cachedFormatString = (String) lastFormatString.get();
        return cachedFormatString != null && formatIndex == ((Integer) lastFormatIndex.get()).intValue() && formatString.equals(cachedFormatString);
    }

    private static void cache(String formatString, int formatIndex, boolean cached) {
        lastFormatIndex.set(Integer.valueOf(formatIndex));
        lastFormatString.set(formatString);
        lastCachedResult.set(Boolean.valueOf(cached));
    }

    public static boolean isADateFormat(int formatIndex, String formatString) {
        if (isInternalDateFormat(formatIndex)) {
            cache(formatString, formatIndex, true);
            return true;
        } else if (formatString == null || formatString.length() == 0) {
            return false;
        } else {
            if (isCached(formatString, formatIndex)) {
                return ((Boolean) lastCachedResult.get()).booleanValue();
            }
            String fs = formatString;
            int length = fs.length();
            StringBuilder sb = new StringBuilder(length);
            int i = 0;
            while (i < length) {
                char c = fs.charAt(i);
                if (i < length - 1) {
                    char nc = fs.charAt(i + 1);
                    if (c == '\\') {
                        switch (nc) {
                            case ' ':
                            case ',':
                            case '-':
                            case '.':
                            case '\\':
                                break;
                        }
                    } else if (c == ';' && nc == '@') {
                        i++;
                        i++;
                    }
                }
                sb.append(c);
                i++;
            }
            fs = sb.toString();
            if (date_ptrn4.matcher(fs).matches()) {
                cache(formatString, formatIndex, true);
                return true;
            }
            fs = date_ptrn2.matcher(date_ptrn1.matcher(fs).replaceAll("")).replaceAll("");
            int separatorIndex = fs.indexOf(59);
            if (separatorIndex > 0 && separatorIndex < fs.length() - 1) {
                fs = fs.substring(0, separatorIndex);
            }
            if (!date_ptrn3a.matcher(fs).find()) {
                return false;
            }
            boolean result = date_ptrn3b.matcher(fs).matches();
            cache(formatString, formatIndex, result);
            return result;
        }
    }

    public static boolean isInternalDateFormat(int format) {
        switch (format) {
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 45:
            case 46:
            case 47:
                return true;
            default:
                return false;
        }
    }

    public static boolean isCellDateFormatted(Cell cell) {
        if (cell == null) {
            return false;
        }
        if (!isValidExcelDate(cell.getNumericCellValue())) {
            return false;
        }
        CellStyle style = cell.getCellStyle();
        if (style == null) {
            return false;
        }
        return isADateFormat(style.getDataFormat(), style.getDataFormatString());
    }

    public static boolean isCellInternalDateFormatted(Cell cell) {
        if (cell == null) {
            return false;
        }
        if (isValidExcelDate(cell.getNumericCellValue())) {
            return isInternalDateFormat(cell.getCellStyle().getDataFormat());
        }
        return false;
    }

    public static boolean isValidExcelDate(double value) {
        return value > -4.9E-324d;
    }

    protected static int absoluteDay(Calendar cal, boolean use1904windowing) {
        return cal.get(6) + daysInPriorYears(cal.get(1), use1904windowing);
    }

    private static int daysInPriorYears(int yr, boolean use1904windowing) {
        int i = 1904;
        if ((use1904windowing || yr >= 1900) && (!use1904windowing || yr >= 1904)) {
            int yr1 = yr - 1;
            int leapDays = (((yr1 / 4) - (yr1 / 100)) + (yr1 / 400)) - 460;
            if (!use1904windowing) {
                i = 1900;
            }
            return ((yr - i) * 365) + leapDays;
        }
        throw new IllegalArgumentException("'year' must be 1900 or greater");
    }

    private static Calendar dayStart(Calendar cal) {
        cal.get(11);
        cal.set(11, 0);
        cal.set(12, 0);
        cal.set(13, 0);
        cal.set(14, 0);
        cal.get(11);
        return cal;
    }

    public static double convertTime(String timeStr) {
        try {
            return convertTimeInternal(timeStr);
        } catch (FormatException e) {
            throw new IllegalArgumentException("Bad time format '" + timeStr + "' expected 'HH:MM' or 'HH:MM:SS' - " + e.getMessage());
        }
    }

    private static double convertTimeInternal(String timeStr) throws FormatException {
        int len = timeStr.length();
        if (len < 4 || len > 8) {
            throw new FormatException("Bad length");
        }
        String secStr;
        String[] parts = TIME_SEPARATOR_PATTERN.split(timeStr);
        switch (parts.length) {
            case 2:
                secStr = "00";
                break;
            case 3:
                secStr = parts[2];
                break;
            default:
                throw new FormatException("Expected 2 or 3 fields but got (" + parts.length + ")");
        }
        String hourStr = parts[0];
        String minStr = parts[1];
        int hours = parseInt(hourStr, "hour", 24);
        int minutes = parseInt(minStr, "minute", 60);
        return ((double) ((((hours * 60) + minutes) * 60) + parseInt(secStr, "second", 60))) / 86400.0d;
    }

    public static Date parseYYYYMMDDDate(String dateStr) {
        try {
            return parseYYYYMMDDDateInternal(dateStr);
        } catch (FormatException e) {
            throw new IllegalArgumentException("Bad time format " + dateStr + " expected 'YYYY/MM/DD' - " + e.getMessage());
        }
    }

    private static Date parseYYYYMMDDDateInternal(String timeStr) throws FormatException {
        if (timeStr.length() != 10) {
            throw new FormatException("Bad length");
        }
        String yearStr = timeStr.substring(0, 4);
        String monthStr = timeStr.substring(5, 7);
        String dayStr = timeStr.substring(8, 10);
        int year = parseInt(yearStr, "year", -32768, avutil.FF_LAMBDA_MAX);
        int month = parseInt(monthStr, "month", 1, 12);
        return LocaleUtil.getLocaleCalendar(year, month - 1, parseInt(dayStr, "day", 1, 31)).getTime();
    }

    private static int parseInt(String strVal, String fieldName, int rangeMax) throws FormatException {
        return parseInt(strVal, fieldName, 0, rangeMax - 1);
    }

    private static int parseInt(String strVal, String fieldName, int lowerLimit, int upperLimit) throws FormatException {
        try {
            int result = Integer.parseInt(strVal);
            if (result >= lowerLimit && result <= upperLimit) {
                return result;
            }
            throw new FormatException(fieldName + " value (" + result + ") is outside the allowable range(0.." + upperLimit + ")");
        } catch (NumberFormatException e) {
            throw new FormatException("Bad int format '" + strVal + "' for " + fieldName + " field");
        }
    }
}

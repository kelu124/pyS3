package org.apache.poi.ss.formula.atp;

import java.util.Calendar;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.EvaluationException;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.util.LocaleUtil;
import org.bytedeco.javacpp.dc1394;

final class YearFracCalculator {
    private static final int DAYS_PER_LEAP_YEAR = 366;
    private static final int DAYS_PER_NORMAL_YEAR = 365;
    private static final int LONG_FEB_LEN = 29;
    private static final int LONG_MONTH_LEN = 31;
    private static final int MS_PER_DAY = 86400000;
    private static final int MS_PER_HOUR = 3600000;
    private static final int SHORT_FEB_LEN = 28;
    private static final int SHORT_MONTH_LEN = 30;

    private static final class SimpleDate {
        public static final int FEBRUARY = 2;
        public static final int JANUARY = 1;
        public final int day;
        public final int month;
        public long tsMilliseconds;
        public final int year;

        public SimpleDate(Calendar cal) {
            this.year = cal.get(1);
            this.month = cal.get(2) + 1;
            this.day = cal.get(5);
            this.tsMilliseconds = cal.getTimeInMillis();
        }
    }

    private YearFracCalculator() {
    }

    public static double calculate(double pStartDateVal, double pEndDateVal, int basis) throws EvaluationException {
        if (basis < 0 || basis >= 5) {
            throw new EvaluationException(ErrorEval.NUM_ERROR);
        }
        int startDateVal = (int) Math.floor(pStartDateVal);
        int endDateVal = (int) Math.floor(pEndDateVal);
        if (startDateVal == endDateVal) {
            return 0.0d;
        }
        if (startDateVal > endDateVal) {
            int temp = startDateVal;
            startDateVal = endDateVal;
            endDateVal = temp;
        }
        switch (basis) {
            case 0:
                return basis0(startDateVal, endDateVal);
            case 1:
                return basis1(startDateVal, endDateVal);
            case 2:
                return basis2(startDateVal, endDateVal);
            case 3:
                return basis3((double) startDateVal, (double) endDateVal);
            case 4:
                return basis4(startDateVal, endDateVal);
            default:
                throw new IllegalStateException("cannot happen");
        }
    }

    public static double basis0(int startDateVal, int endDateVal) {
        SimpleDate startDate = createDate(startDateVal);
        SimpleDate endDate = createDate(endDateVal);
        int date1day = startDate.day;
        int date2day = endDate.day;
        if (date1day == 31 && date2day == 31) {
            date1day = 30;
            date2day = 30;
        } else if (date1day == 31) {
            date1day = 30;
        } else if (date1day == 30 && date2day == 31) {
            date2day = 30;
        } else if (startDate.month == 2 && isLastDayOfMonth(startDate)) {
            date1day = 30;
            if (endDate.month == 2 && isLastDayOfMonth(endDate)) {
                date2day = 30;
            }
        }
        return calculateAdjusted(startDate, endDate, date1day, date2day);
    }

    public static double basis1(int startDateVal, int endDateVal) {
        double yearLength;
        SimpleDate startDate = createDate(startDateVal);
        SimpleDate endDate = createDate(endDateVal);
        if (isGreaterThanOneYear(startDate, endDate)) {
            yearLength = averageYearLength(startDate.year, endDate.year);
        } else if (shouldCountFeb29(startDate, endDate)) {
            yearLength = 366.0d;
        } else {
            yearLength = 365.0d;
        }
        return ((double) dateDiff(startDate.tsMilliseconds, endDate.tsMilliseconds)) / yearLength;
    }

    public static double basis2(int startDateVal, int endDateVal) {
        return ((double) (endDateVal - startDateVal)) / 360.0d;
    }

    public static double basis3(double startDateVal, double endDateVal) {
        return (endDateVal - startDateVal) / 365.0d;
    }

    public static double basis4(int startDateVal, int endDateVal) {
        SimpleDate startDate = createDate(startDateVal);
        SimpleDate endDate = createDate(endDateVal);
        int date1day = startDate.day;
        int date2day = endDate.day;
        if (date1day == 31) {
            date1day = 30;
        }
        if (date2day == 31) {
            date2day = 30;
        }
        return calculateAdjusted(startDate, endDate, date1day, date2day);
    }

    private static double calculateAdjusted(SimpleDate startDate, SimpleDate endDate, int date1day, int date2day) {
        return ((double) ((((endDate.year - startDate.year) * dc1394.DC1394_COLOR_CODING_RGB16S) + ((endDate.month - startDate.month) * 30)) + ((date2day - date1day) * 1))) / 360.0d;
    }

    private static boolean isLastDayOfMonth(SimpleDate date) {
        if (date.day >= 28 && date.day == getLastDayOfMonth(date)) {
            return true;
        }
        return false;
    }

    private static int getLastDayOfMonth(SimpleDate date) {
        switch (date.month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            default:
                if (isLeapYear(date.year)) {
                    return 29;
                }
                return 28;
        }
    }

    private static boolean shouldCountFeb29(SimpleDate start, SimpleDate end) {
        boolean startIsLeapYear = isLeapYear(start.year);
        if (startIsLeapYear && start.year == end.year) {
            return true;
        }
        boolean endIsLeapYear = isLeapYear(end.year);
        if (!startIsLeapYear && !endIsLeapYear) {
            return false;
        }
        if (startIsLeapYear) {
            switch (start.month) {
                case 1:
                case 2:
                    return true;
                default:
                    return false;
            }
        } else if (!endIsLeapYear) {
            return false;
        } else {
            switch (end.month) {
                case 1:
                    return false;
                case 2:
                    if (end.day != 29) {
                        return false;
                    }
                    return true;
                default:
                    return true;
            }
        }
    }

    private static int dateDiff(long startDateMS, long endDateMS) {
        long msDiff = endDateMS - startDateMS;
        switch ((int) ((msDiff % DateUtil.DAY_MILLISECONDS) / 3600000)) {
            case 0:
                return (int) (0.5d + (((double) msDiff) / 8.64E7d));
            default:
                throw new RuntimeException("Unexpected date diff between " + startDateMS + " and " + endDateMS);
        }
    }

    private static double averageYearLength(int startYear, int endYear) {
        int dayCount = 0;
        for (int i = startYear; i <= endYear; i++) {
            dayCount += DAYS_PER_NORMAL_YEAR;
            if (isLeapYear(i)) {
                dayCount++;
            }
        }
        return ((double) dayCount) / ((double) ((endYear - startYear) + 1));
    }

    private static boolean isLeapYear(int i) {
        if (i % 4 != 0) {
            return false;
        }
        if (i % 400 == 0) {
            return true;
        }
        if (i % 100 != 0) {
            return true;
        }
        return false;
    }

    private static boolean isGreaterThanOneYear(SimpleDate start, SimpleDate end) {
        if (start.year == end.year) {
            return false;
        }
        if (start.year + 1 != end.year) {
            return true;
        }
        if (start.month > end.month) {
            return false;
        }
        if (start.month < end.month || start.day < end.day) {
            return true;
        }
        return false;
    }

    private static SimpleDate createDate(int dayCount) {
        Calendar cal = LocaleUtil.getLocaleCalendar(LocaleUtil.TIMEZONE_UTC);
        DateUtil.setCalendar(cal, dayCount, 0, false, false);
        return new SimpleDate(cal);
    }
}

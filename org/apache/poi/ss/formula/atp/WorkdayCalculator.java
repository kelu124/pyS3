package org.apache.poi.ss.formula.atp;

import java.util.Calendar;
import java.util.Date;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.util.LocaleUtil;

public class WorkdayCalculator {
    public static final WorkdayCalculator instance = new WorkdayCalculator();

    private WorkdayCalculator() {
    }

    public int calculateWorkdays(double start, double end, double[] holidays) {
        int saturdaysPast = pastDaysOfWeek(start, end, 7);
        return ((((int) ((end - start) + 1.0d)) - saturdaysPast) - pastDaysOfWeek(start, end, 1)) - calculateNonWeekendHolidays(start, end, holidays);
    }

    public Date calculateWorkdays(double start, int workdays, double[] holidays) {
        int direction;
        Date startDate = DateUtil.getJavaDate(start);
        if (workdays < 0) {
            direction = -1;
        } else {
            direction = 1;
        }
        Calendar endDate = LocaleUtil.getLocaleCalendar();
        endDate.setTime(startDate);
        double excelEndDate = DateUtil.getExcelDate(endDate.getTime());
        while (workdays != 0) {
            endDate.add(6, direction);
            excelEndDate += (double) direction;
            if (!(endDate.get(7) == 7 || endDate.get(7) == 1 || isHoliday(excelEndDate, holidays))) {
                workdays -= direction;
            }
        }
        return endDate.getTime();
    }

    protected int pastDaysOfWeek(double start, double end, int dayOfWeek) {
        double d;
        int pastDaysOfWeek = 0;
        if (start < end) {
            d = start;
        } else {
            d = end;
        }
        if (end > start) {
            d = end;
        } else {
            d = start;
        }
        int endDay = (int) Math.floor(d);
        for (int startDay = (int) Math.floor(d); startDay <= endDay; startDay++) {
            Calendar today = LocaleUtil.getLocaleCalendar();
            today.setTime(DateUtil.getJavaDate((double) startDay));
            if (today.get(7) == dayOfWeek) {
                pastDaysOfWeek++;
            }
        }
        return start < end ? pastDaysOfWeek : -pastDaysOfWeek;
    }

    protected int calculateNonWeekendHolidays(double start, double end, double[] holidays) {
        double startDay;
        double endDay;
        int nonWeekendHolidays = 0;
        if (start < end) {
            startDay = start;
        } else {
            startDay = end;
        }
        if (end > start) {
            endDay = end;
        } else {
            endDay = start;
        }
        int i = 0;
        while (i < holidays.length) {
            if (isInARange(startDay, endDay, holidays[i]) && !isWeekend(holidays[i])) {
                nonWeekendHolidays++;
            }
            i++;
        }
        return start < end ? nonWeekendHolidays : -nonWeekendHolidays;
    }

    protected boolean isWeekend(double aDate) {
        Calendar date = LocaleUtil.getLocaleCalendar();
        date.setTime(DateUtil.getJavaDate(aDate));
        if (date.get(7) == 7 || date.get(7) == 1) {
            return true;
        }
        return false;
    }

    protected boolean isHoliday(double aDate, double[] holidays) {
        for (double round : holidays) {
            if (Math.round(round) == Math.round(aDate)) {
                return true;
            }
        }
        return false;
    }

    protected int isNonWorkday(double aDate, double[] holidays) {
        return (isWeekend(aDate) || isHoliday(aDate, holidays)) ? 1 : 0;
    }

    protected boolean isInARange(double start, double end, double aDate) {
        return aDate >= start && aDate <= end;
    }
}

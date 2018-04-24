package org.apache.poi.ss.format;

import com.itextpdf.text.html.HtmlTags;
import com.itextpdf.text.pdf.Barcode128;
import java.text.AttributedCharacterIterator;
import java.text.DateFormat;
import java.text.DateFormat.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import java.util.regex.Matcher;
import org.apache.poi.util.LocaleUtil;

public class CellDateFormatter extends CellFormatter {
    private static CellDateFormatter SIMPLE_DATE = null;
    private final Calendar EXCEL_EPOCH_CAL = LocaleUtil.getLocaleCalendar(1904, 0, 1);
    private boolean amPmUpper;
    private final DateFormat dateFmt;
    private String sFmt;
    private boolean showAmPm;
    private boolean showM;

    private class DatePartHandler implements PartHandler {
        private int hLen;
        private int hStart;
        private int mLen;
        private int mStart;

        private DatePartHandler() {
            this.mStart = -1;
            this.hStart = -1;
        }

        public String handlePart(Matcher m, String part, CellFormatType type, StringBuffer desc) {
            boolean z = false;
            int pos = desc.length();
            switch (part.charAt(0)) {
                case '0':
                    this.mStart = -1;
                    int sLen = part.length();
                    CellDateFormatter.this.sFmt = "%0" + (sLen + 2) + "." + sLen + "f";
                    return part.replace('0', 'S');
                case 'A':
                case 'P':
                case 'a':
                case 'p':
                    if (part.length() > 1) {
                        boolean z2;
                        this.mStart = -1;
                        CellDateFormatter.this.showAmPm = true;
                        CellDateFormatter cellDateFormatter = CellDateFormatter.this;
                        if (Character.toLowerCase(part.charAt(1)) == 'm') {
                            z2 = true;
                        } else {
                            z2 = false;
                        }
                        cellDateFormatter.showM = z2;
                        CellDateFormatter cellDateFormatter2 = CellDateFormatter.this;
                        if (CellDateFormatter.this.showM || Character.isUpperCase(part.charAt(0))) {
                            z = true;
                        }
                        cellDateFormatter2.amPmUpper = z;
                        return HtmlTags.f32A;
                    }
                    break;
                case 'D':
                case 'd':
                    this.mStart = -1;
                    if (part.length() <= 2) {
                        return part.toLowerCase(Locale.ROOT);
                    }
                    return part.toLowerCase(Locale.ROOT).replace(Barcode128.CODE_AC_TO_B, 'E');
                case 'H':
                case 'h':
                    this.mStart = -1;
                    this.hStart = pos;
                    this.hLen = part.length();
                    return part.toLowerCase(Locale.ROOT);
                case 'M':
                case 'm':
                    this.mStart = pos;
                    this.mLen = part.length();
                    if (this.hStart >= 0) {
                        return part.toLowerCase(Locale.ROOT);
                    }
                    return part.toUpperCase(Locale.ROOT);
                case 'S':
                case 's':
                    if (this.mStart >= 0) {
                        for (int i = 0; i < this.mLen; i++) {
                            desc.setCharAt(this.mStart + i, 'm');
                        }
                        this.mStart = -1;
                    }
                    return part.toLowerCase(Locale.ROOT);
                case 'Y':
                case 'y':
                    this.mStart = -1;
                    if (part.length() == 3) {
                        part = "yyyy";
                    }
                    return part.toLowerCase(Locale.ROOT);
            }
            return null;
        }

        public void finish(StringBuffer toAppendTo) {
            if (this.hStart >= 0 && !CellDateFormatter.this.showAmPm) {
                for (int i = 0; i < this.hLen; i++) {
                    toAppendTo.setCharAt(this.hStart + i, 'H');
                }
            }
        }
    }

    public CellDateFormatter(String format) {
        super(format);
        DatePartHandler partHandler = new DatePartHandler();
        StringBuffer descBuf = CellFormatPart.parseFormat(format, CellFormatType.DATE, partHandler);
        partHandler.finish(descBuf);
        this.dateFmt = new SimpleDateFormat(descBuf.toString().replaceAll("((y)(?!y))(?<!yy)", "yy"), LocaleUtil.getUserLocale());
        this.dateFmt.setTimeZone(LocaleUtil.getUserTimeZone());
    }

    public void formatValue(StringBuffer toAppendTo, Object value) {
        Date time;
        if (value == null) {
            Number value2 = Double.valueOf(0.0d);
        }
        if (value2 instanceof Number) {
            long v = value2.longValue();
            if (v == 0) {
                time = this.EXCEL_EPOCH_CAL.getTime();
            } else {
                Calendar c = (Calendar) this.EXCEL_EPOCH_CAL.clone();
                c.add(13, (int) (v / 1000));
                c.add(14, (int) (v % 1000));
                time = c.getTime();
            }
        } else {
            Object obj = value2;
        }
        AttributedCharacterIterator it = this.dateFmt.formatToCharacterIterator(time);
        boolean doneAm = false;
        boolean doneMillis = false;
        it.first();
        for (char ch = it.first(); ch != '￿'; ch = it.next()) {
            if (it.getAttribute(Field.MILLISECOND) != null) {
                if (doneMillis) {
                    continue;
                } else {
                    Date dateObj = time;
                    int pos = toAppendTo.length();
                    Formatter formatter = new Formatter(toAppendTo, Locale.ROOT);
                    try {
                        long msecs = dateObj.getTime() % 1000;
                        formatter.format(LocaleUtil.getUserLocale(), this.sFmt, new Object[]{Double.valueOf(((double) msecs) / 1000.0d)});
                        toAppendTo.delete(pos, pos + 2);
                        doneMillis = true;
                    } finally {
                        formatter.close();
                    }
                }
            } else if (it.getAttribute(Field.AM_PM) == null) {
                toAppendTo.append(ch);
            } else if (!doneAm) {
                if (this.showAmPm) {
                    if (this.amPmUpper) {
                        toAppendTo.append(Character.toUpperCase(ch));
                        if (this.showM) {
                            toAppendTo.append('M');
                        }
                    } else {
                        toAppendTo.append(Character.toLowerCase(ch));
                        if (this.showM) {
                            toAppendTo.append('m');
                        }
                    }
                }
                doneAm = true;
            }
        }
    }

    public void simpleValue(StringBuffer toAppendTo, Object value) {
        synchronized (CellDateFormatter.class) {
            if (SIMPLE_DATE == null || !SIMPLE_DATE.EXCEL_EPOCH_CAL.equals(this.EXCEL_EPOCH_CAL)) {
                SIMPLE_DATE = new CellDateFormatter("mm/d/y");
            }
        }
        SIMPLE_DATE.formatValue(toAppendTo, value);
    }
}

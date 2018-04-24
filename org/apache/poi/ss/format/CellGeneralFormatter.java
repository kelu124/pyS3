package org.apache.poi.ss.format;

import java.util.Formatter;
import java.util.Locale;
import org.apache.poi.util.LocaleUtil;

public class CellGeneralFormatter extends CellFormatter {
    public CellGeneralFormatter() {
        super("General");
    }

    public void formatValue(StringBuffer toAppendTo, Object value) {
        if (value instanceof Number) {
            double val = ((Number) value).doubleValue();
            if (val == 0.0d) {
                toAppendTo.append('0');
                return;
            }
            String fmt;
            double exp = Math.log10(Math.abs(val));
            boolean stripZeros = true;
            if (exp > 10.0d || exp < -9.0d) {
                fmt = "%1.5E";
            } else if (((double) ((long) val)) != val) {
                fmt = "%1.9f";
            } else {
                fmt = "%1.0f";
                stripZeros = false;
            }
            Formatter formatter = new Formatter(toAppendTo, LocaleUtil.getUserLocale());
            try {
                formatter.format(LocaleUtil.getUserLocale(), fmt, new Object[]{value});
                if (stripZeros) {
                    int removeFrom;
                    int removeFrom2;
                    if (fmt.endsWith("E")) {
                        removeFrom = toAppendTo.lastIndexOf("E") - 1;
                    } else {
                        removeFrom = toAppendTo.length() - 1;
                    }
                    while (toAppendTo.charAt(removeFrom) == '0') {
                        removeFrom2 = removeFrom - 1;
                        toAppendTo.deleteCharAt(removeFrom);
                        removeFrom = removeFrom2;
                    }
                    if (toAppendTo.charAt(removeFrom) == '.') {
                        removeFrom2 = removeFrom - 1;
                        toAppendTo.deleteCharAt(removeFrom);
                    }
                }
            } finally {
                formatter.close();
            }
        } else if (value instanceof Boolean) {
            toAppendTo.append(value.toString().toUpperCase(Locale.ROOT));
        } else {
            toAppendTo.append(value.toString());
        }
    }

    public void simpleValue(StringBuffer toAppendTo, Object value) {
        formatValue(toAppendTo, value);
    }
}

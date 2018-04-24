package org.apache.poi.ss.format;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CellElapsedFormatter extends CellFormatter {
    private static final double HOUR__FACTOR = 0.041666666666666664d;
    private static final double MIN__FACTOR = 6.944444444444444E-4d;
    private static final Pattern PERCENTS = Pattern.compile("%");
    private static final double SEC__FACTOR = 1.1574074074074073E-5d;
    private final String printfFmt;
    private final List<TimeSpec> specs = new ArrayList();
    private TimeSpec topmost;

    private class ElapsedPartHandler implements PartHandler {
        private ElapsedPartHandler() {
        }

        public String handlePart(Matcher m, String part, CellFormatType type, StringBuffer desc) {
            int pos = desc.length();
            switch (part.charAt(0)) {
                case '\n':
                    return "%n";
                case '\"':
                    part = part.substring(1, part.length() - 1);
                    break;
                case '*':
                    if (part.length() > 1) {
                        part = CellFormatPart.expandChar(part);
                        break;
                    }
                    break;
                case '0':
                case 'h':
                case 'm':
                case 's':
                    part = part.toLowerCase(Locale.ROOT);
                    CellElapsedFormatter.this.assignSpec(part.charAt(0), pos, part.length());
                    return part;
                case '[':
                    if (part.length() >= 3) {
                        if (CellElapsedFormatter.this.topmost != null) {
                            throw new IllegalArgumentException("Duplicate '[' times in format");
                        }
                        part = part.toLowerCase(Locale.ROOT);
                        int specLen = part.length() - 2;
                        CellElapsedFormatter.this.topmost = CellElapsedFormatter.this.assignSpec(part.charAt(1), pos, specLen);
                        return part.substring(1, specLen + 1);
                    }
                    break;
                case '\\':
                    part = part.substring(1);
                    break;
                case '_':
                    return null;
            }
            return CellElapsedFormatter.PERCENTS.matcher(part).replaceAll("%%");
        }
    }

    private static class TimeSpec {
        final double factor;
        final int len;
        double modBy = 0.0d;
        final int pos;
        final char type;

        public TimeSpec(char type, int pos, int len, double factor) {
            this.type = type;
            this.pos = pos;
            this.len = len;
            this.factor = factor;
        }

        public long valueFor(double elapsed) {
            double val;
            if (this.modBy == 0.0d) {
                val = elapsed / this.factor;
            } else {
                val = (elapsed / this.factor) % this.modBy;
            }
            if (this.type == '0') {
                return Math.round(val);
            }
            return (long) val;
        }
    }

    public CellElapsedFormatter(String pattern) {
        super(pattern);
        StringBuffer desc = CellFormatPart.parseFormat(pattern, CellFormatType.ELAPSED, new ElapsedPartHandler());
        ListIterator<TimeSpec> it = this.specs.listIterator(this.specs.size());
        while (it.hasPrevious()) {
            TimeSpec spec = (TimeSpec) it.previous();
            desc.replace(spec.pos, spec.pos + spec.len, "%0" + spec.len + "d");
            if (spec.type != this.topmost.type) {
                spec.modBy = modFor(spec.type, spec.len);
            }
        }
        this.printfFmt = desc.toString();
    }

    private TimeSpec assignSpec(char type, int pos, int len) {
        TimeSpec spec = new TimeSpec(type, pos, len, factorFor(type, len));
        this.specs.add(spec);
        return spec;
    }

    private static double factorFor(char type, int len) {
        switch (type) {
            case '0':
                return SEC__FACTOR / Math.pow(10.0d, (double) len);
            case 'h':
                return HOUR__FACTOR;
            case 'm':
                return MIN__FACTOR;
            case 's':
                return SEC__FACTOR;
            default:
                throw new IllegalArgumentException("Uknown elapsed time spec: " + type);
        }
    }

    private static double modFor(char type, int len) {
        switch (type) {
            case '0':
                return Math.pow(10.0d, (double) len);
            case 'h':
                return 24.0d;
            case 'm':
            case 's':
                return 60.0d;
            default:
                throw new IllegalArgumentException("Uknown elapsed time spec: " + type);
        }
    }

    public void formatValue(StringBuffer toAppendTo, Object value) {
        double elapsed = ((Number) value).doubleValue();
        if (elapsed < 0.0d) {
            toAppendTo.append('-');
            elapsed = -elapsed;
        }
        Object[] parts = new Long[this.specs.size()];
        for (int i = 0; i < this.specs.size(); i++) {
            parts[i] = Long.valueOf(((TimeSpec) this.specs.get(i)).valueFor(elapsed));
        }
        Formatter formatter = new Formatter(toAppendTo, Locale.ROOT);
        try {
            formatter.format(this.printfFmt, parts);
        } finally {
            formatter.close();
        }
    }

    public void simpleValue(StringBuffer toAppendTo, Object value) {
        formatValue(toAppendTo, value);
    }
}

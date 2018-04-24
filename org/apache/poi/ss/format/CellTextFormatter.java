package org.apache.poi.ss.format;

import java.util.Locale;
import java.util.regex.Matcher;

public class CellTextFormatter extends CellFormatter {
    static final CellFormatter SIMPLE_TEXT = new CellTextFormatter("@");
    private final String desc;
    private final int[] textPos;

    public CellTextFormatter(String format) {
        super(format);
        final int[] numPlaces = new int[1];
        this.desc = CellFormatPart.parseFormat(format, CellFormatType.TEXT, new PartHandler() {
            public String handlePart(Matcher m, String part, CellFormatType type, StringBuffer desc) {
                if (!part.equals("@")) {
                    return null;
                }
                int[] iArr = numPlaces;
                iArr[0] = iArr[0] + 1;
                return "\u0000";
            }
        }).toString();
        this.textPos = new int[numPlaces[0]];
        int pos = this.desc.length() - 1;
        for (int i = 0; i < this.textPos.length; i++) {
            this.textPos[i] = this.desc.lastIndexOf("\u0000", pos);
            pos = this.textPos[i] - 1;
        }
    }

    public void formatValue(StringBuffer toAppendTo, Object obj) {
        int start = toAppendTo.length();
        String text = obj.toString();
        if (obj instanceof Boolean) {
            text = text.toUpperCase(Locale.ROOT);
        }
        toAppendTo.append(this.desc);
        for (int i : this.textPos) {
            int pos = start + i;
            toAppendTo.replace(pos, pos + 1, text);
        }
    }

    public void simpleValue(StringBuffer toAppendTo, Object value) {
        SIMPLE_TEXT.formatValue(toAppendTo, value);
    }
}

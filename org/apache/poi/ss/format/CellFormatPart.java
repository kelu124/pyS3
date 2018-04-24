package org.apache.poi.ss.format;

import com.itextpdf.text.pdf.Barcode128;
import java.awt.Color;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JLabel;
import org.apache.poi.hssf.util.HSSFColor;

public class CellFormatPart {
    public static final int COLOR_GROUP = findGroup(FORMAT_PAT, "[Blue]@", "Blue");
    public static final Pattern COLOR_PAT;
    public static final int CONDITION_OPERATOR_GROUP = findGroup(FORMAT_PAT, "[>=1]@", ">=");
    public static final Pattern CONDITION_PAT;
    public static final int CONDITION_VALUE_GROUP = findGroup(FORMAT_PAT, "[>=1]@", "1");
    public static final Pattern CURRENCY_PAT;
    public static final Pattern FORMAT_PAT;
    private static final Map<String, Color> NAMED_COLORS = new TreeMap(String.CASE_INSENSITIVE_ORDER);
    public static final int SPECIFICATION_GROUP = findGroup(FORMAT_PAT, "[Blue][>1]\\a ?", "\\a ?");
    public static final Pattern SPECIFICATION_PAT;
    private final Color color;
    private CellFormatCondition condition;
    private final CellFormatter format;
    private final CellFormatType type;

    interface PartHandler {
        String handlePart(Matcher matcher, String str, CellFormatType cellFormatType, StringBuffer stringBuffer);
    }

    static {
        for (HSSFColor color : HSSFColor.getIndexHash().values()) {
            String name = color.getClass().getSimpleName();
            if (name.equals(name.toUpperCase(Locale.ROOT))) {
                short[] rgb = color.getTriplet();
                Color c = new Color(rgb[0], rgb[1], rgb[2]);
                NAMED_COLORS.put(name, c);
                if (name.indexOf(95) > 0) {
                    NAMED_COLORS.put(name.replace('_', ' '), c);
                }
                if (name.indexOf("_PERCENT") > 0) {
                    NAMED_COLORS.put(name.replace("_PERCENT", "%").replace('_', ' '), c);
                }
            }
        }
        String condition = "([<>=]=?|!=|<>)    # The operator\n  \\s*([0-9]+(?:\\.[0-9]*)?)\\s*  # The constant to test against\n";
        String currency = "(\\[\\$.{0,3}-[0-9a-f]{3}\\])";
        String color2 = "\\[(black|blue|cyan|green|magenta|red|white|yellow|color [0-9]+)\\]";
        String part = "\\\\.                 # Quoted single character\n|\"([^\\\\\"]|\\\\.)*\"         # Quoted string of characters (handles escaped quotes like \\\") \n|" + currency + "                   # Currency symbol in a given locale\n" + "|_.                             # Space as wide as a given character\n" + "|\\*.                           # Repeating fill character\n" + "|@                              # Text: cell text\n" + "|([0?\\#](?:[0?\\#,]*))         # Number: digit + other digits and commas\n" + "|e[-+]                          # Number: Scientific: Exponent\n" + "|m{1,5}                         # Date: month or minute spec\n" + "|d{1,4}                         # Date: day/date spec\n" + "|y{2,4}                         # Date: year spec\n" + "|h{1,2}                         # Date: hour spec\n" + "|s{1,2}                         # Date: second spec\n" + "|am?/pm?                        # Date: am/pm spec\n" + "|\\[h{1,2}\\]                   # Elapsed time: hour spec\n" + "|\\[m{1,2}\\]                   # Elapsed time: minute spec\n" + "|\\[s{1,2}\\]                   # Elapsed time: second spec\n" + "|[^;]                           # A character\n" + "";
        String format = "(?:" + color2 + ")?                 # Text color\n" + "(?:\\[" + condition + "\\])?               # Condition\n" + "(?:\\[\\$-[0-9a-fA-F]+\\])?                # Optional locale id, ignored currently\n" + "((?:" + part + ")+)                        # Format spec\n";
        COLOR_PAT = Pattern.compile(color2, 6);
        CONDITION_PAT = Pattern.compile(condition, 6);
        SPECIFICATION_PAT = Pattern.compile(part, 6);
        CURRENCY_PAT = Pattern.compile(currency, 6);
        FORMAT_PAT = Pattern.compile(format, 6);
    }

    public CellFormatPart(String desc) {
        Matcher m = FORMAT_PAT.matcher(desc);
        if (m.matches()) {
            this.color = getColor(m);
            this.condition = getCondition(m);
            this.type = getCellFormatType(m);
            this.format = getFormatter(m);
            return;
        }
        throw new IllegalArgumentException("Unrecognized format: " + CellFormatter.quote(desc));
    }

    public boolean applies(Object valueObject) {
        if (this.condition != null && (valueObject instanceof Number)) {
            return this.condition.pass(((Number) valueObject).doubleValue());
        } else if (valueObject != null) {
            return true;
        } else {
            throw new NullPointerException("valueObject");
        }
    }

    private static int findGroup(Pattern pat, String str, String marker) {
        Matcher m = pat.matcher(str);
        if (m.find()) {
            for (int i = 1; i <= m.groupCount(); i++) {
                String grp = m.group(i);
                if (grp != null && grp.equals(marker)) {
                    return i;
                }
            }
            throw new IllegalArgumentException("\"" + marker + "\" not found in \"" + pat.pattern() + "\"");
        }
        throw new IllegalArgumentException("Pattern \"" + pat.pattern() + "\" doesn't match \"" + str + "\"");
    }

    private static Color getColor(Matcher m) {
        String cdesc = m.group(COLOR_GROUP);
        if (cdesc == null || cdesc.length() == 0) {
            return null;
        }
        Color c = (Color) NAMED_COLORS.get(cdesc);
        if (c != null) {
            return c;
        }
        CellFormatter.logger.warning("Unknown color: " + CellFormatter.quote(cdesc));
        return c;
    }

    private CellFormatCondition getCondition(Matcher m) {
        String mdesc = m.group(CONDITION_OPERATOR_GROUP);
        if (mdesc == null || mdesc.length() == 0) {
            return null;
        }
        return CellFormatCondition.getInstance(m.group(CONDITION_OPERATOR_GROUP), m.group(CONDITION_VALUE_GROUP));
    }

    private CellFormatType getCellFormatType(Matcher matcher) {
        return formatType(matcher.group(SPECIFICATION_GROUP));
    }

    private CellFormatter getFormatter(Matcher matcher) {
        String fdesc = matcher.group(SPECIFICATION_GROUP);
        Matcher currencyM = CURRENCY_PAT.matcher(fdesc);
        if (currencyM.find()) {
            String currencyRepl;
            String currencyPart = currencyM.group(1);
            if (currencyPart.startsWith("[$-")) {
                currencyRepl = "$";
            } else {
                currencyRepl = currencyPart.substring(2, currencyPart.lastIndexOf(45));
            }
            fdesc = fdesc.replace(currencyPart, currencyRepl);
        }
        return this.type.formatter(fdesc);
    }

    private CellFormatType formatType(String fdesc) {
        fdesc = fdesc.trim();
        if (fdesc.equals("") || fdesc.equalsIgnoreCase("General")) {
            return CellFormatType.GENERAL;
        }
        Matcher m = SPECIFICATION_PAT.matcher(fdesc);
        boolean couldBeDate = false;
        boolean seenZero = false;
        while (m.find()) {
            String repl = m.group(0);
            if (repl.length() > 0) {
                char c1 = repl.charAt(0);
                char c2 = '\u0000';
                if (repl.length() > 1) {
                    c2 = Character.toLowerCase(repl.charAt(1));
                }
                switch (c1) {
                    case '#':
                    case '?':
                        return CellFormatType.NUMBER;
                    case '0':
                        seenZero = true;
                        break;
                    case '@':
                        return CellFormatType.TEXT;
                    case 'D':
                    case 'Y':
                    case 'd':
                    case 'y':
                        return CellFormatType.DATE;
                    case 'H':
                    case 'M':
                    case 'S':
                    case 'h':
                    case 'm':
                    case 's':
                        couldBeDate = true;
                        break;
                    case '[':
                        if (c2 == Barcode128.START_B || c2 == 'm' || c2 == 's') {
                            return CellFormatType.ELAPSED;
                        }
                        if (c2 == '$') {
                            return CellFormatType.NUMBER;
                        }
                        throw new IllegalArgumentException("Unsupported [] format block '" + repl + "' in '" + fdesc + "' with c2: " + c2);
                    default:
                        break;
                }
            }
        }
        if (couldBeDate) {
            return CellFormatType.DATE;
        }
        if (seenZero) {
            return CellFormatType.NUMBER;
        }
        return CellFormatType.TEXT;
    }

    static String quoteSpecial(String repl, CellFormatType type) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < repl.length(); i++) {
            char ch = repl.charAt(i);
            if (ch == '\'' && type.isSpecial('\'')) {
                sb.append('\u0000');
            } else {
                boolean special = type.isSpecial(ch);
                if (special) {
                    sb.append("'");
                }
                sb.append(ch);
                if (special) {
                    sb.append("'");
                }
            }
        }
        return sb.toString();
    }

    public CellFormatResult apply(Object value) {
        String text;
        Color textColor;
        boolean applies = applies(value);
        if (applies) {
            text = this.format.format(value);
            textColor = this.color;
        } else {
            text = this.format.simpleFormat(value);
            textColor = null;
        }
        return new CellFormatResult(applies, text, textColor);
    }

    public CellFormatResult apply(JLabel label, Object value) {
        CellFormatResult result = apply(value);
        label.setText(result.text);
        if (result.textColor != null) {
            label.setForeground(result.textColor);
        }
        return result;
    }

    CellFormatType getCellFormatType() {
        return this.type;
    }

    boolean hasCondition() {
        return this.condition != null;
    }

    public static StringBuffer parseFormat(String fdesc, CellFormatType type, PartHandler partHandler) {
        Matcher m = SPECIFICATION_PAT.matcher(fdesc);
        StringBuffer fmt = new StringBuffer();
        while (m.find()) {
            String part = group(m, 0);
            if (part.length() > 0) {
                String repl = partHandler.handlePart(m, part, type, fmt);
                if (repl == null) {
                    switch (part.charAt(0)) {
                        case '\"':
                            repl = quoteSpecial(part.substring(1, part.length() - 1), type);
                            break;
                        case '*':
                            repl = expandChar(part);
                            break;
                        case '\\':
                            repl = quoteSpecial(part.substring(1), type);
                            break;
                        case '_':
                            repl = " ";
                            break;
                        default:
                            repl = part;
                            break;
                    }
                }
                m.appendReplacement(fmt, Matcher.quoteReplacement(repl));
            }
        }
        m.appendTail(fmt);
        if (type.isSpecial('\'')) {
            int pos = 0;
            while (true) {
                pos = fmt.indexOf("''", pos);
                if (pos >= 0) {
                    fmt.delete(pos, pos + 2);
                } else {
                    pos = 0;
                    while (true) {
                        pos = fmt.indexOf("\u0000", pos);
                        if (pos >= 0) {
                            fmt.replace(pos, pos + 1, "''");
                        }
                    }
                }
            }
        }
        return fmt;
    }

    static String expandChar(String part) {
        char ch = part.charAt(1);
        return "" + ch + ch + ch;
    }

    public static String group(Matcher m, int g) {
        String str = m.group(g);
        return str == null ? "" : str;
    }

    public String toString() {
        return this.format.format;
    }
}

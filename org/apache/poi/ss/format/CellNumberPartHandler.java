package org.apache.poi.ss.format;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import org.apache.poi.ss.format.CellFormatPart.PartHandler;
import org.apache.poi.ss.format.CellNumberFormatter.Special;
import org.apache.poi.util.Internal;

@Internal
public class CellNumberPartHandler implements PartHandler {
    private Special decimalPoint;
    private Special exponent;
    private boolean improperFraction;
    private char insertSignForExponent;
    private Special numerator;
    private double scale = 1.0d;
    private Special slash;
    private final List<Special> specials = new LinkedList();

    public String handlePart(Matcher m, String part, CellFormatType type, StringBuffer descBuf) {
        int i = 1;
        int pos = descBuf.length();
        switch (part.charAt(0)) {
            case '#':
            case '0':
            case '?':
                if (this.insertSignForExponent != '\u0000') {
                    this.specials.add(new Special(this.insertSignForExponent, pos));
                    descBuf.append(this.insertSignForExponent);
                    this.insertSignForExponent = '\u0000';
                    pos++;
                }
                for (int i2 = 0; i2 < part.length(); i2++) {
                    this.specials.add(new Special(part.charAt(i2), pos + i2));
                }
                return part;
            case '%':
                this.scale *= 100.0d;
                return part;
            case '.':
                if (this.decimalPoint != null || this.specials.size() <= 0) {
                    return part;
                }
                this.decimalPoint = new Special('.', pos);
                this.specials.add(this.decimalPoint);
                return part;
            case '/':
                if (this.slash != null || this.specials.size() <= 0) {
                    return part;
                }
                this.numerator = previousNumber();
                boolean z = this.improperFraction;
                if (this.numerator != firstDigit(this.specials)) {
                    i = 0;
                }
                this.improperFraction = i | z;
                this.slash = new Special('.', pos);
                this.specials.add(this.slash);
                return part;
            case 'E':
            case 'e':
                if (this.exponent != null || this.specials.size() <= 0) {
                    return part;
                }
                this.exponent = new Special('.', pos);
                this.specials.add(this.exponent);
                this.insertSignForExponent = part.charAt(1);
                return part.substring(0, 1);
            default:
                return null;
        }
    }

    public double getScale() {
        return this.scale;
    }

    public Special getDecimalPoint() {
        return this.decimalPoint;
    }

    public Special getSlash() {
        return this.slash;
    }

    public Special getExponent() {
        return this.exponent;
    }

    public Special getNumerator() {
        return this.numerator;
    }

    public List<Special> getSpecials() {
        return this.specials;
    }

    public boolean isImproperFraction() {
        return this.improperFraction;
    }

    private Special previousNumber() {
        ListIterator<Special> it = this.specials.listIterator(this.specials.size());
        while (it.hasPrevious()) {
            Special s = (Special) it.previous();
            if (isDigitFmt(s)) {
                Special last = s;
                while (it.hasPrevious()) {
                    s = (Special) it.previous();
                    if (last.pos - s.pos > 1 || !isDigitFmt(s)) {
                        return last;
                    }
                    last = s;
                }
                return last;
            }
        }
        return null;
    }

    private static boolean isDigitFmt(Special s) {
        return s.ch == '0' || s.ch == '?' || s.ch == '#';
    }

    private static Special firstDigit(List<Special> specials) {
        for (Special s : specials) {
            if (isDigitFmt(s)) {
                return s;
            }
        }
        return null;
    }
}

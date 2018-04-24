package org.apache.poi.ss.format;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.FieldPosition;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import org.apache.poi.util.LocaleUtil;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

public class CellNumberFormatter extends CellFormatter {
    private static final POILogger LOG = POILogFactory.getLogger(CellNumberFormatter.class);
    private static final CellFormatter SIMPLE_FLOAT = new CellNumberFormatter("#.#");
    private static final CellFormatter SIMPLE_INT = new CellNumberFormatter("#");
    private static final CellFormatter SIMPLE_NUMBER = new GeneralNumberFormatter();
    private final Special afterFractional;
    private final Special afterInteger;
    private final DecimalFormat decimalFmt;
    private final Special decimalPoint;
    private final String denominatorFmt;
    private final List<Special> denominatorSpecials = new ArrayList();
    private final String desc;
    private final Special exponent;
    private final List<Special> exponentDigitSpecials = new ArrayList();
    private final List<Special> exponentSpecials = new ArrayList();
    private final List<Special> fractionalSpecials = new ArrayList();
    private final boolean improperFraction;
    private final boolean integerCommas;
    private final List<Special> integerSpecials = new ArrayList();
    private final int maxDenominator;
    private final Special numerator;
    private final String numeratorFmt;
    private final List<Special> numeratorSpecials = new ArrayList();
    private final String printfFmt;
    private final double scale;
    private final Special slash;
    private final List<Special> specials = new ArrayList();

    private static class GeneralNumberFormatter extends CellFormatter {
        private GeneralNumberFormatter() {
            super("General");
        }

        public void formatValue(StringBuffer toAppendTo, Object value) {
            if (value != null) {
                CellFormatter cf = value instanceof Number ? ((Number) value).doubleValue() % 1.0d == 0.0d ? CellNumberFormatter.SIMPLE_INT : CellNumberFormatter.SIMPLE_FLOAT : CellTextFormatter.SIMPLE_TEXT;
                cf.formatValue(toAppendTo, value);
            }
        }

        public void simpleValue(StringBuffer toAppendTo, Object value) {
            formatValue(toAppendTo, value);
        }
    }

    static class Special {
        final char ch;
        int pos;

        Special(char ch, int pos) {
            this.ch = ch;
            this.pos = pos;
        }

        public String toString() {
            return "'" + this.ch + "' @ " + this.pos;
        }
    }

    public CellNumberFormatter(String format) {
        super(format);
        CellNumberPartHandler ph = new CellNumberPartHandler();
        StringBuffer descBuf = CellFormatPart.parseFormat(format, CellFormatType.NUMBER, ph);
        this.exponent = ph.getExponent();
        this.specials.addAll(ph.getSpecials());
        this.improperFraction = ph.isImproperFraction();
        if ((ph.getDecimalPoint() == null && ph.getExponent() == null) || ph.getSlash() == null) {
            this.slash = ph.getSlash();
            this.numerator = ph.getNumerator();
        } else {
            this.slash = null;
            this.numerator = null;
        }
        int precision = interpretPrecision(ph.getDecimalPoint(), this.specials);
        int fractionPartWidth = 0;
        if (ph.getDecimalPoint() != null) {
            fractionPartWidth = precision + 1;
            if (precision == 0) {
                this.specials.remove(ph.getDecimalPoint());
                this.decimalPoint = null;
            } else {
                this.decimalPoint = ph.getDecimalPoint();
            }
        } else {
            this.decimalPoint = null;
        }
        if (this.decimalPoint != null) {
            this.afterInteger = this.decimalPoint;
        } else if (this.exponent != null) {
            this.afterInteger = this.exponent;
        } else if (this.numerator != null) {
            this.afterInteger = this.numerator;
        } else {
            this.afterInteger = null;
        }
        if (this.exponent != null) {
            this.afterFractional = this.exponent;
        } else if (this.numerator != null) {
            this.afterFractional = this.numerator;
        } else {
            this.afterFractional = null;
        }
        double[] scaleByRef = new double[]{ph.getScale()};
        this.integerCommas = interpretIntegerCommas(descBuf, this.specials, this.decimalPoint, integerEnd(), fractionalEnd(), scaleByRef);
        if (this.exponent == null) {
            this.scale = scaleByRef[0];
        } else {
            this.scale = 1.0d;
        }
        if (precision != 0) {
            this.fractionalSpecials.addAll(this.specials.subList(this.specials.indexOf(this.decimalPoint) + 1, fractionalEnd()));
        }
        if (this.exponent != null) {
            int exponentPos = this.specials.indexOf(this.exponent);
            this.exponentSpecials.addAll(specialsFor(exponentPos, 2));
            this.exponentDigitSpecials.addAll(specialsFor(exponentPos + 2));
        }
        if (this.slash != null) {
            if (this.numerator != null) {
                this.numeratorSpecials.addAll(specialsFor(this.specials.indexOf(this.numerator)));
            }
            this.denominatorSpecials.addAll(specialsFor(this.specials.indexOf(this.slash) + 1));
            if (this.denominatorSpecials.isEmpty()) {
                this.numeratorSpecials.clear();
                this.maxDenominator = 1;
                this.numeratorFmt = null;
                this.denominatorFmt = null;
            } else {
                this.maxDenominator = maxValue(this.denominatorSpecials);
                this.numeratorFmt = singleNumberFormat(this.numeratorSpecials);
                this.denominatorFmt = singleNumberFormat(this.denominatorSpecials);
            }
        } else {
            this.maxDenominator = 1;
            this.numeratorFmt = null;
            this.denominatorFmt = null;
        }
        this.integerSpecials.addAll(this.specials.subList(0, integerEnd()));
        StringBuffer fmtBuf;
        if (this.exponent == null) {
            fmtBuf = new StringBuffer("%");
            fmtBuf.append('0').append(calculateIntegerPartWidth() + fractionPartWidth).append('.').append(precision);
            fmtBuf.append("f");
            this.printfFmt = fmtBuf.toString();
            this.decimalFmt = null;
        } else {
            fmtBuf = new StringBuffer();
            boolean first = true;
            List<Special> specialList = this.integerSpecials;
            if (this.integerSpecials.size() == 1) {
                fmtBuf.append("0");
                first = false;
            } else {
                for (Special s : specialList) {
                    if (isDigitFmt(s)) {
                        fmtBuf.append(first ? '#' : '0');
                        first = false;
                    }
                }
            }
            if (this.fractionalSpecials.size() > 0) {
                fmtBuf.append('.');
                for (Special s2 : this.fractionalSpecials) {
                    if (isDigitFmt(s2)) {
                        if (!first) {
                            fmtBuf.append('0');
                        }
                        first = false;
                    }
                }
            }
            fmtBuf.append('E');
            placeZeros(fmtBuf, this.exponentSpecials.subList(2, this.exponentSpecials.size()));
            this.decimalFmt = new DecimalFormat(fmtBuf.toString(), DecimalFormatSymbols.getInstance(LocaleUtil.getUserLocale()));
            this.printfFmt = null;
        }
        this.desc = descBuf.toString();
    }

    private static void placeZeros(StringBuffer sb, List<Special> specials) {
        for (Special s : specials) {
            if (isDigitFmt(s)) {
                sb.append('0');
            }
        }
    }

    private static CellNumberStringMod insertMod(Special special, CharSequence toAdd, int where) {
        return new CellNumberStringMod(special, toAdd, where);
    }

    private static CellNumberStringMod deleteMod(Special start, boolean startInclusive, Special end, boolean endInclusive) {
        return new CellNumberStringMod(start, startInclusive, end, endInclusive);
    }

    private static CellNumberStringMod replaceMod(Special start, boolean startInclusive, Special end, boolean endInclusive, char withChar) {
        return new CellNumberStringMod(start, startInclusive, end, endInclusive, withChar);
    }

    private static String singleNumberFormat(List<Special> numSpecials) {
        return "%0" + numSpecials.size() + "d";
    }

    private static int maxValue(List<Special> s) {
        return (int) Math.round(Math.pow(10.0d, (double) s.size()) - 1.0d);
    }

    private List<Special> specialsFor(int pos, int takeFirst) {
        if (pos >= this.specials.size()) {
            return Collections.emptyList();
        }
        ListIterator<Special> it = this.specials.listIterator(pos + takeFirst);
        Special last = (Special) it.next();
        int end = pos + takeFirst;
        while (it.hasNext()) {
            Special s = (Special) it.next();
            if (!isDigitFmt(s) || s.pos - last.pos > 1) {
                break;
            }
            end++;
            last = s;
        }
        return this.specials.subList(pos, end + 1);
    }

    private List<Special> specialsFor(int pos) {
        return specialsFor(pos, 0);
    }

    private static boolean isDigitFmt(Special s) {
        return s.ch == '0' || s.ch == '?' || s.ch == '#';
    }

    private int calculateIntegerPartWidth() {
        int digitCount = 0;
        for (Special s : this.specials) {
            if (s == this.afterInteger) {
                break;
            } else if (isDigitFmt(s)) {
                digitCount++;
            }
        }
        return digitCount;
    }

    private static int interpretPrecision(Special decimalPoint, List<Special> specials) {
        int idx = specials.indexOf(decimalPoint);
        int precision = 0;
        if (idx != -1) {
            ListIterator<Special> it = specials.listIterator(idx + 1);
            while (it.hasNext() && isDigitFmt((Special) it.next())) {
                precision++;
            }
        }
        return precision;
    }

    private static boolean interpretIntegerCommas(StringBuffer sb, List<Special> specials, Special decimalPoint, int integerEnd, int fractionalEnd, double[] scale) {
        ListIterator<Special> it = specials.listIterator(integerEnd);
        boolean stillScaling = true;
        boolean integerCommas = false;
        while (it.hasPrevious()) {
            if (((Special) it.previous()).ch != ',') {
                stillScaling = false;
            } else if (stillScaling) {
                scale[0] = scale[0] / 1000.0d;
            } else {
                integerCommas = true;
            }
        }
        if (decimalPoint != null) {
            it = specials.listIterator(fractionalEnd);
            while (it.hasPrevious() && ((Special) it.previous()).ch == ',') {
                scale[0] = scale[0] / 1000.0d;
            }
        }
        it = specials.listIterator();
        int removed = 0;
        while (it.hasNext()) {
            Special s = (Special) it.next();
            s.pos -= removed;
            if (s.ch == ',') {
                removed++;
                it.remove();
                sb.deleteCharAt(s.pos);
            }
        }
        return integerCommas;
    }

    private int integerEnd() {
        return this.afterInteger == null ? this.specials.size() : this.specials.indexOf(this.afterInteger);
    }

    private int fractionalEnd() {
        return this.afterFractional == null ? this.specials.size() : this.specials.indexOf(this.afterFractional);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void formatValue(java.lang.StringBuffer r34, java.lang.Object r35) {
        /*
        r33 = this;
        r35 = (java.lang.Number) r35;
        r4 = r35.doubleValue();
        r0 = r33;
        r13 = r0.scale;
        r4 = r4 * r13;
        r13 = 0;
        r3 = (r4 > r13 ? 1 : (r4 == r13 ? 0 : -1));
        if (r3 >= 0) goto L_0x00d1;
    L_0x0011:
        r30 = 1;
    L_0x0013:
        if (r30 == 0) goto L_0x0016;
    L_0x0015:
        r4 = -r4;
    L_0x0016:
        r7 = 0;
        r0 = r33;
        r3 = r0.slash;
        if (r3 == 0) goto L_0x0027;
    L_0x001e:
        r0 = r33;
        r3 = r0.improperFraction;
        if (r3 == 0) goto L_0x00d5;
    L_0x0024:
        r7 = r4;
        r4 = 0;
    L_0x0027:
        r10 = new java.util.TreeSet;
        r10.<init>();
        r9 = new java.lang.StringBuffer;
        r0 = r33;
        r3 = r0.desc;
        r9.<init>(r3);
        r0 = r33;
        r3 = r0.exponent;
        if (r3 == 0) goto L_0x00dd;
    L_0x003b:
        r0 = r33;
        r0.writeScientific(r4, r9, r10);
    L_0x0040:
        r19 = r10.iterator();
        r3 = r19.hasNext();
        if (r3 == 0) goto L_0x0140;
    L_0x004a:
        r3 = r19.next();
        r3 = (org.apache.poi.ss.format.CellNumberStringMod) r3;
        r31 = r3;
    L_0x0052:
        r22 = new java.util.BitSet;
        r22.<init>();
        r17 = 0;
        r0 = r33;
        r3 = r0.specials;
        r26 = r3.iterator();
    L_0x0061:
        r3 = r26.hasNext();
        if (r3 == 0) goto L_0x01fb;
    L_0x0067:
        r32 = r26.next();
        r32 = (org.apache.poi.ss.format.CellNumberFormatter.Special) r32;
        r0 = r32;
        r3 = r0.pos;
        r18 = r3 + r17;
        r0 = r32;
        r3 = r0.pos;
        r0 = r22;
        r3 = r0.get(r3);
        if (r3 != 0) goto L_0x0099;
    L_0x007f:
        r0 = r18;
        r3 = r9.charAt(r0);
        r6 = 35;
        if (r3 != r6) goto L_0x0099;
    L_0x0089:
        r0 = r18;
        r9.deleteCharAt(r0);
        r17 = r17 + -1;
        r0 = r32;
        r3 = r0.pos;
        r0 = r22;
        r0.set(r3);
    L_0x0099:
        if (r31 == 0) goto L_0x0061;
    L_0x009b:
        r3 = r31.getSpecial();
        r0 = r32;
        if (r0 != r3) goto L_0x0061;
    L_0x00a3:
        r27 = r9.length();
        r0 = r32;
        r3 = r0.pos;
        r29 = r3 + r17;
        r3 = r31.getOp();
        switch(r3) {
            case 1: goto L_0x017e;
            case 2: goto L_0x0144;
            case 3: goto L_0x0188;
            default: goto L_0x00b4;
        };
    L_0x00b4:
        r3 = new java.lang.IllegalStateException;
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r11 = "Unknown op: ";
        r6 = r6.append(r11);
        r11 = r31.getOp();
        r6 = r6.append(r11);
        r6 = r6.toString();
        r3.<init>(r6);
        throw r3;
    L_0x00d1:
        r30 = 0;
        goto L_0x0013;
    L_0x00d5:
        r13 = 4607182418800017408; // 0x3ff0000000000000 float:0.0 double:1.0;
        r7 = r4 % r13;
        r13 = (long) r4;
        r4 = (double) r13;
        goto L_0x0027;
    L_0x00dd:
        r0 = r33;
        r3 = r0.improperFraction;
        if (r3 == 0) goto L_0x00eb;
    L_0x00e3:
        r6 = 0;
        r3 = r33;
        r3.writeFraction(r4, r6, r7, r9, r10);
        goto L_0x0040;
    L_0x00eb:
        r12 = new java.lang.StringBuffer;
        r12.<init>();
        r23 = new java.util.Formatter;
        r3 = org.apache.poi.util.LocaleUtil.getUserLocale();
        r0 = r23;
        r0.<init>(r12, r3);
        r3 = org.apache.poi.util.LocaleUtil.getUserLocale();	 Catch:{ all -> 0x0133 }
        r0 = r33;
        r6 = r0.printfFmt;	 Catch:{ all -> 0x0133 }
        r11 = 1;
        r11 = new java.lang.Object[r11];	 Catch:{ all -> 0x0133 }
        r13 = 0;
        r14 = java.lang.Double.valueOf(r4);	 Catch:{ all -> 0x0133 }
        r11[r13] = r14;	 Catch:{ all -> 0x0133 }
        r0 = r23;
        r0.format(r3, r6, r11);	 Catch:{ all -> 0x0133 }
        r23.close();
        r0 = r33;
        r3 = r0.numerator;
        if (r3 != 0) goto L_0x0138;
    L_0x011b:
        r0 = r33;
        r0.writeFractional(r12, r9);
        r0 = r33;
        r14 = r0.integerSpecials;
        r0 = r33;
        r0 = r0.integerCommas;
        r16 = r0;
        r11 = r33;
        r13 = r9;
        r15 = r10;
        r11.writeInteger(r12, r13, r14, r15, r16);
        goto L_0x0040;
    L_0x0133:
        r3 = move-exception;
        r23.close();
        throw r3;
    L_0x0138:
        r3 = r33;
        r6 = r12;
        r3.writeFraction(r4, r6, r7, r9, r10);
        goto L_0x0040;
    L_0x0140:
        r31 = 0;
        goto L_0x0052;
    L_0x0144:
        r3 = r31.getToAdd();
        r6 = ",";
        r3 = r3.equals(r6);
        if (r3 == 0) goto L_0x0174;
    L_0x0150:
        r0 = r32;
        r3 = r0.pos;
        r0 = r22;
        r3 = r0.get(r3);
        if (r3 == 0) goto L_0x0174;
    L_0x015c:
        r3 = r9.length();
        r3 = r3 - r27;
        r17 = r17 + r3;
        r3 = r19.hasNext();
        if (r3 == 0) goto L_0x01f7;
    L_0x016a:
        r3 = r19.next();
        r3 = (org.apache.poi.ss.format.CellNumberStringMod) r3;
        r31 = r3;
    L_0x0172:
        goto L_0x0099;
    L_0x0174:
        r3 = r29 + 1;
        r6 = r31.getToAdd();
        r9.insert(r3, r6);
        goto L_0x015c;
    L_0x017e:
        r3 = r31.getToAdd();
        r0 = r29;
        r9.insert(r0, r3);
        goto L_0x015c;
    L_0x0188:
        r0 = r32;
        r0 = r0.pos;
        r21 = r0;
        r3 = r31.isStartInclusive();
        if (r3 != 0) goto L_0x0198;
    L_0x0194:
        r21 = r21 + 1;
        r29 = r29 + 1;
    L_0x0198:
        r0 = r22;
        r1 = r21;
        r3 = r0.get(r1);
        if (r3 == 0) goto L_0x01a7;
    L_0x01a2:
        r21 = r21 + 1;
        r29 = r29 + 1;
        goto L_0x0198;
    L_0x01a7:
        r3 = r31.getEnd();
        r0 = r3.pos;
        r20 = r0;
        r3 = r31.isEndInclusive();
        if (r3 == 0) goto L_0x01b7;
    L_0x01b5:
        r20 = r20 + 1;
    L_0x01b7:
        r28 = r20 + r17;
        r0 = r29;
        r1 = r28;
        if (r0 >= r1) goto L_0x015c;
    L_0x01bf:
        r3 = "";
        r6 = r31.getToAdd();
        r3 = r3.equals(r6);
        if (r3 == 0) goto L_0x01dc;
    L_0x01cb:
        r0 = r29;
        r1 = r28;
        r9.delete(r0, r1);
    L_0x01d2:
        r0 = r22;
        r1 = r21;
        r2 = r20;
        r0.set(r1, r2);
        goto L_0x015c;
    L_0x01dc:
        r3 = r31.getToAdd();
        r6 = 0;
        r24 = r3.charAt(r6);
        r25 = r29;
    L_0x01e7:
        r0 = r25;
        r1 = r28;
        if (r0 >= r1) goto L_0x01d2;
    L_0x01ed:
        r0 = r25;
        r1 = r24;
        r9.setCharAt(r0, r1);
        r25 = r25 + 1;
        goto L_0x01e7;
    L_0x01f7:
        r31 = 0;
        goto L_0x0172;
    L_0x01fb:
        if (r30 == 0) goto L_0x0204;
    L_0x01fd:
        r3 = 45;
        r0 = r34;
        r0.append(r3);
    L_0x0204:
        r0 = r34;
        r0.append(r9);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.poi.ss.format.CellNumberFormatter.formatValue(java.lang.StringBuffer, java.lang.Object):void");
    }

    private void writeScientific(double value, StringBuffer output, Set<CellNumberStringMod> mods) {
        StringBuffer result = new StringBuffer();
        FieldPosition fractionPos = new FieldPosition(1);
        this.decimalFmt.format(value, result, fractionPos);
        writeInteger(result, output, this.integerSpecials, mods, this.integerCommas);
        writeFractional(result, output);
        int signPos = fractionPos.getEndIndex() + 1;
        char expSignRes = result.charAt(signPos);
        if (expSignRes != '-') {
            expSignRes = '+';
            result.insert(signPos, '+');
        }
        Special expSign = (Special) this.exponentSpecials.listIterator(1).next();
        char expSignFmt = expSign.ch;
        if (expSignRes == '-' || expSignFmt == '+') {
            mods.add(replaceMod(expSign, true, expSign, true, expSignRes));
        } else {
            mods.add(deleteMod(expSign, true, expSign, true));
        }
        writeInteger(new StringBuffer(result.substring(signPos + 1)), output, this.exponentDigitSpecials, mods, false);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void writeFraction(double r25, java.lang.StringBuffer r27, double r28, java.lang.StringBuffer r30, java.util.Set<org.apache.poi.ss.format.CellNumberStringMod> r31) {
        /*
        r24 = this;
        r0 = r24;
        r2 = r0.improperFraction;
        if (r2 != 0) goto L_0x0123;
    L_0x0006:
        r2 = 0;
        r2 = (r28 > r2 ? 1 : (r28 == r2 ? 0 : -1));
        if (r2 != 0) goto L_0x007c;
    L_0x000c:
        r2 = 48;
        r3 = 1;
        r3 = new java.util.List[r3];
        r5 = 0;
        r0 = r24;
        r6 = r0.numeratorSpecials;
        r3[r5] = r6;
        r2 = hasChar(r2, r3);
        if (r2 != 0) goto L_0x007c;
    L_0x001e:
        r0 = r24;
        r5 = r0.integerSpecials;
        r7 = 0;
        r2 = r24;
        r3 = r27;
        r4 = r30;
        r6 = r31;
        r2.writeInteger(r3, r4, r5, r6, r7);
        r0 = r24;
        r2 = r0.integerSpecials;
        r22 = lastSpecial(r2);
        r0 = r24;
        r2 = r0.denominatorSpecials;
        r12 = lastSpecial(r2);
        r2 = 63;
        r3 = 3;
        r3 = new java.util.List[r3];
        r5 = 0;
        r0 = r24;
        r6 = r0.integerSpecials;
        r3[r5] = r6;
        r5 = 1;
        r0 = r24;
        r6 = r0.numeratorSpecials;
        r3[r5] = r6;
        r5 = 2;
        r0 = r24;
        r6 = r0.denominatorSpecials;
        r3[r5] = r6;
        r2 = hasChar(r2, r3);
        if (r2 == 0) goto L_0x006e;
    L_0x005e:
        r2 = 0;
        r3 = 1;
        r5 = 32;
        r0 = r22;
        r2 = replaceMod(r0, r2, r12, r3, r5);
        r0 = r31;
        r0.add(r2);
    L_0x006d:
        return;
    L_0x006e:
        r2 = 0;
        r3 = 1;
        r0 = r22;
        r2 = deleteMod(r0, r2, r12, r3);
        r0 = r31;
        r0.add(r2);
        goto L_0x006d;
    L_0x007c:
        r2 = 48;
        r3 = 1;
        r3 = new java.util.List[r3];
        r5 = 0;
        r0 = r24;
        r6 = r0.numeratorSpecials;
        r3[r5] = r6;
        r2 = hasChar(r2, r3);
        if (r2 != 0) goto L_0x0188;
    L_0x008e:
        r18 = 1;
    L_0x0090:
        r2 = 48;
        r3 = 1;
        r3 = new java.util.List[r3];
        r5 = 0;
        r0 = r24;
        r6 = r0.integerSpecials;
        r3[r5] = r6;
        r2 = hasChar(r2, r3);
        if (r2 != 0) goto L_0x018c;
    L_0x00a2:
        r16 = 1;
    L_0x00a4:
        r0 = r24;
        r2 = r0.integerSpecials;
        r2 = r2.isEmpty();
        if (r2 != 0) goto L_0x00cb;
    L_0x00ae:
        r0 = r24;
        r2 = r0.integerSpecials;
        r2 = r2.size();
        r3 = 1;
        if (r2 != r3) goto L_0x0190;
    L_0x00b9:
        r2 = 35;
        r3 = 1;
        r3 = new java.util.List[r3];
        r5 = 0;
        r0 = r24;
        r6 = r0.integerSpecials;
        r3[r5] = r6;
        r2 = hasChar(r2, r3);
        if (r2 == 0) goto L_0x0190;
    L_0x00cb:
        r17 = 1;
    L_0x00cd:
        r2 = 0;
        r2 = (r28 > r2 ? 1 : (r28 == r2 ? 0 : -1));
        if (r2 != 0) goto L_0x0194;
    L_0x00d3:
        if (r17 != 0) goto L_0x00d7;
    L_0x00d5:
        if (r18 == 0) goto L_0x0194;
    L_0x00d7:
        r20 = 1;
    L_0x00d9:
        r2 = 0;
        r2 = (r28 > r2 ? 1 : (r28 == r2 ? 0 : -1));
        if (r2 == 0) goto L_0x0198;
    L_0x00df:
        if (r16 == 0) goto L_0x0198;
    L_0x00e1:
        r19 = 1;
    L_0x00e3:
        r2 = 0;
        r2 = (r25 > r2 ? 1 : (r25 == r2 ? 0 : -1));
        if (r2 != 0) goto L_0x01aa;
    L_0x00e9:
        if (r20 != 0) goto L_0x00ed;
    L_0x00eb:
        if (r19 == 0) goto L_0x01aa;
    L_0x00ed:
        r0 = r24;
        r2 = r0.integerSpecials;
        r22 = lastSpecial(r2);
        r2 = 63;
        r3 = 2;
        r3 = new java.util.List[r3];
        r5 = 0;
        r0 = r24;
        r6 = r0.integerSpecials;
        r3[r5] = r6;
        r5 = 1;
        r0 = r24;
        r6 = r0.numeratorSpecials;
        r3[r5] = r6;
        r14 = hasChar(r2, r3);
        if (r14 == 0) goto L_0x019c;
    L_0x010e:
        r2 = 1;
        r0 = r24;
        r3 = r0.numerator;
        r5 = 0;
        r6 = 32;
        r0 = r22;
        r21 = replaceMod(r0, r2, r3, r5, r6);
    L_0x011c:
        r0 = r31;
        r1 = r21;
        r0.add(r1);
    L_0x0123:
        r2 = 0;
        r2 = (r28 > r2 ? 1 : (r28 == r2 ? 0 : -1));
        if (r2 == 0) goto L_0x0139;
    L_0x0129:
        r0 = r24;
        r2 = r0.improperFraction;	 Catch:{ RuntimeException -> 0x0174 }
        if (r2 == 0) goto L_0x01bc;
    L_0x012f:
        r2 = 4607182418800017408; // 0x3ff0000000000000 float:0.0 double:1.0;
        r2 = r28 % r2;
        r6 = 0;
        r2 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1));
        if (r2 != 0) goto L_0x01bc;
    L_0x0139:
        r2 = java.lang.Math.round(r28);	 Catch:{ RuntimeException -> 0x0174 }
        r4 = (int) r2;	 Catch:{ RuntimeException -> 0x0174 }
        r11 = 1;
    L_0x013f:
        r0 = r24;
        r2 = r0.improperFraction;	 Catch:{ RuntimeException -> 0x0174 }
        if (r2 == 0) goto L_0x014f;
    L_0x0145:
        r2 = (long) r4;	 Catch:{ RuntimeException -> 0x0174 }
        r6 = (double) r11;	 Catch:{ RuntimeException -> 0x0174 }
        r6 = r6 * r25;
        r6 = java.lang.Math.round(r6);	 Catch:{ RuntimeException -> 0x0174 }
        r2 = r2 + r6;
        r4 = (int) r2;	 Catch:{ RuntimeException -> 0x0174 }
    L_0x014f:
        r0 = r24;
        r3 = r0.numeratorFmt;	 Catch:{ RuntimeException -> 0x0174 }
        r0 = r24;
        r6 = r0.numeratorSpecials;	 Catch:{ RuntimeException -> 0x0174 }
        r2 = r24;
        r5 = r30;
        r7 = r31;
        r2.writeSingleInteger(r3, r4, r5, r6, r7);	 Catch:{ RuntimeException -> 0x0174 }
        r0 = r24;
        r6 = r0.denominatorFmt;	 Catch:{ RuntimeException -> 0x0174 }
        r0 = r24;
        r9 = r0.denominatorSpecials;	 Catch:{ RuntimeException -> 0x0174 }
        r5 = r24;
        r7 = r11;
        r8 = r30;
        r10 = r31;
        r5.writeSingleInteger(r6, r7, r8, r9, r10);	 Catch:{ RuntimeException -> 0x0174 }
        goto L_0x006d;
    L_0x0174:
        r15 = move-exception;
        r2 = LOG;
        r3 = 7;
        r5 = 2;
        r5 = new java.lang.Object[r5];
        r6 = 0;
        r7 = "error while fraction evaluation";
        r5[r6] = r7;
        r6 = 1;
        r5[r6] = r15;
        r2.log(r3, r5);
        goto L_0x006d;
    L_0x0188:
        r18 = 0;
        goto L_0x0090;
    L_0x018c:
        r16 = 0;
        goto L_0x00a4;
    L_0x0190:
        r17 = 0;
        goto L_0x00cd;
    L_0x0194:
        r20 = 0;
        goto L_0x00d9;
    L_0x0198:
        r19 = 0;
        goto L_0x00e3;
    L_0x019c:
        r2 = 1;
        r0 = r24;
        r3 = r0.numerator;
        r5 = 0;
        r0 = r22;
        r21 = deleteMod(r0, r2, r3, r5);
        goto L_0x011c;
    L_0x01aa:
        r0 = r24;
        r5 = r0.integerSpecials;
        r7 = 0;
        r2 = r24;
        r3 = r27;
        r4 = r30;
        r6 = r31;
        r2.writeInteger(r3, r4, r5, r6, r7);
        goto L_0x0123;
    L_0x01bc:
        r0 = r24;
        r2 = r0.maxDenominator;	 Catch:{ RuntimeException -> 0x0174 }
        r0 = r28;
        r13 = org.apache.poi.ss.format.SimpleFraction.buildFractionMaxDenominator(r0, r2);	 Catch:{ RuntimeException -> 0x0174 }
        r4 = r13.getNumerator();	 Catch:{ RuntimeException -> 0x0174 }
        r11 = r13.getDenominator();	 Catch:{ RuntimeException -> 0x0174 }
        goto L_0x013f;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.poi.ss.format.CellNumberFormatter.writeFraction(double, java.lang.StringBuffer, double, java.lang.StringBuffer, java.util.Set):void");
    }

    private static boolean hasChar(char ch, List<Special>... numSpecials) {
        for (List<Special> specials : numSpecials) {
            for (Special s : specials) {
                if (s.ch == ch) {
                    return true;
                }
            }
        }
        return false;
    }

    private void writeSingleInteger(String fmt, int num, StringBuffer output, List<Special> numSpecials, Set<CellNumberStringMod> mods) {
        StringBuffer sb = new StringBuffer();
        Formatter formatter = new Formatter(sb, LocaleUtil.getUserLocale());
        try {
            formatter.format(LocaleUtil.getUserLocale(), fmt, new Object[]{Integer.valueOf(num)});
            writeInteger(sb, output, numSpecials, mods, false);
        } finally {
            formatter.close();
        }
    }

    private void writeInteger(StringBuffer result, StringBuffer output, List<Special> numSpecials, Set<CellNumberStringMod> mods, boolean showCommas) {
        int pos = result.indexOf(".") - 1;
        if (pos < 0) {
            if (this.exponent == null || numSpecials != this.integerSpecials) {
                pos = result.length() - 1;
            } else {
                pos = result.indexOf("E") - 1;
            }
        }
        int strip = 0;
        while (strip < pos) {
            char resultCh = result.charAt(strip);
            if (resultCh != '0' && resultCh != ',') {
                break;
            }
            strip++;
        }
        ListIterator<Special> it = numSpecials.listIterator(numSpecials.size());
        Special lastOutputIntegerDigit = null;
        int digit = 0;
        while (it.hasPrevious()) {
            if (pos >= 0) {
                resultCh = result.charAt(pos);
            } else {
                resultCh = '0';
            }
            Special s = (Special) it.previous();
            boolean followWithComma = showCommas && digit > 0 && digit % 3 == 0;
            boolean zeroStrip = false;
            if (resultCh != '0' || s.ch == '0' || s.ch == '?' || pos >= strip) {
                zeroStrip = s.ch == '?' && pos < strip;
                int i = s.pos;
                if (zeroStrip) {
                    resultCh = ' ';
                }
                output.setCharAt(i, resultCh);
                lastOutputIntegerDigit = s;
            }
            if (followWithComma) {
                mods.add(insertMod(s, zeroStrip ? " " : ",", 2));
            }
            digit++;
            pos--;
        }
        StringBuffer extraLeadingDigits = new StringBuffer();
        if (pos >= 0) {
            pos++;
            extraLeadingDigits = new StringBuffer(result.substring(0, pos));
            if (showCommas) {
                while (pos > 0) {
                    if (digit > 0 && digit % 3 == 0) {
                        extraLeadingDigits.insert(pos, ',');
                    }
                    digit++;
                    pos--;
                }
            }
            mods.add(insertMod(lastOutputIntegerDigit, extraLeadingDigits, 1));
        }
    }

    private void writeFractional(StringBuffer result, StringBuffer output) {
        if (this.fractionalSpecials.size() > 0) {
            int strip;
            int digit = result.indexOf(".") + 1;
            if (this.exponent != null) {
                strip = result.indexOf("e") - 1;
            } else {
                strip = result.length() - 1;
            }
            while (strip > digit && result.charAt(strip) == '0') {
                strip--;
            }
            for (Special s : this.fractionalSpecials) {
                char resultCh = result.charAt(digit);
                if (resultCh != '0' || s.ch == '0' || digit < strip) {
                    output.setCharAt(s.pos, resultCh);
                } else if (s.ch == '?') {
                    output.setCharAt(s.pos, ' ');
                }
                digit++;
            }
        }
    }

    public void simpleValue(StringBuffer toAppendTo, Object value) {
        SIMPLE_NUMBER.formatValue(toAppendTo, value);
    }

    private static Special lastSpecial(List<Special> s) {
        return (Special) s.get(s.size() - 1);
    }
}

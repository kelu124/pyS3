package com.itextpdf.text.pdf;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class SequenceList {
    protected static final int COMMA = 1;
    private static final int DIGIT = 1;
    private static final int DIGIT2 = 3;
    protected static final int END = 6;
    protected static final char EOT = '￿';
    private static final int FIRST = 0;
    protected static final int MINUS = 2;
    protected static final int NOT = 3;
    private static final String NOT_OTHER = "-,!0123456789";
    protected static final int NUMBER = 5;
    private static final int OTHER = 2;
    protected static final int TEXT = 4;
    protected boolean even;
    protected int high;
    protected boolean inverse;
    protected int low;
    protected int number;
    protected boolean odd;
    protected String other;
    protected int ptr = 0;
    protected char[] text;

    protected SequenceList(String range) {
        this.text = range.toCharArray();
    }

    protected char nextChar() {
        while (this.ptr < this.text.length) {
            char[] cArr = this.text;
            int i = this.ptr;
            this.ptr = i + 1;
            char c = cArr[i];
            if (c > ' ') {
                return c;
            }
        }
        return EOT;
    }

    protected void putBack() {
        this.ptr--;
        if (this.ptr < 0) {
            this.ptr = 0;
        }
    }

    protected int getType() {
        StringBuffer buf = new StringBuffer();
        int state = 0;
        while (true) {
            char c = nextChar();
            String stringBuffer;
            if (c != EOT) {
                switch (state) {
                    case 0:
                        switch (c) {
                            case '!':
                                return 3;
                            case ',':
                                return 1;
                            case '-':
                                return 2;
                            default:
                                buf.append(c);
                                if (c >= '0' && c <= '9') {
                                    state = 1;
                                    break;
                                }
                                state = 2;
                                break;
                        }
                    case 1:
                        if (c >= '0' && c <= '9') {
                            buf.append(c);
                            break;
                        }
                        putBack();
                        stringBuffer = buf.toString();
                        this.other = stringBuffer;
                        this.number = Integer.parseInt(stringBuffer);
                        return 5;
                    case 2:
                        if (NOT_OTHER.indexOf(c) < 0) {
                            buf.append(c);
                            break;
                        }
                        putBack();
                        this.other = buf.toString().toLowerCase();
                        return 4;
                    default:
                        break;
                }
            } else if (state == 1) {
                stringBuffer = buf.toString();
                this.other = stringBuffer;
                this.number = Integer.parseInt(stringBuffer);
                return 5;
            } else if (state != 2) {
                return 6;
            } else {
                this.other = buf.toString().toLowerCase();
                return 4;
            }
        }
    }

    private void otherProc() {
        if (this.other.equals("odd") || this.other.equals("o")) {
            this.odd = true;
            this.even = false;
        } else if (this.other.equals("even") || this.other.equals("e")) {
            this.odd = false;
            this.even = true;
        }
    }

    protected boolean getAttributes() {
        this.low = -1;
        this.high = -1;
        this.inverse = false;
        this.even = false;
        this.odd = false;
        int state = 2;
        while (true) {
            int type = getType();
            if (type != 6 && type != 1) {
                switch (state) {
                    case 1:
                        switch (type) {
                            case 2:
                                state = 3;
                                break;
                            case 3:
                                this.inverse = true;
                                state = 2;
                                this.high = this.low;
                                break;
                            default:
                                this.high = this.low;
                                state = 2;
                                otherProc();
                                break;
                        }
                    case 2:
                        switch (type) {
                            case 2:
                                state = 3;
                                break;
                            case 3:
                                this.inverse = true;
                                break;
                            default:
                                if (type != 5) {
                                    otherProc();
                                    break;
                                }
                                this.low = this.number;
                                state = 1;
                                break;
                        }
                    case 3:
                        switch (type) {
                            case 2:
                                break;
                            case 3:
                                this.inverse = true;
                                state = 2;
                                break;
                            case 5:
                                this.high = this.number;
                                state = 2;
                                break;
                            default:
                                state = 2;
                                otherProc();
                                break;
                        }
                    default:
                        break;
                }
            }
            if (state == 1) {
                this.high = this.low;
            }
            if (type == 6) {
                return true;
            }
            return false;
        }
    }

    public static List<Integer> expand(String ranges, int maxNumber) {
        SequenceList parse = new SequenceList(ranges);
        LinkedList<Integer> list = new LinkedList();
        boolean sair = false;
        while (!sair) {
            sair = parse.getAttributes();
            if (parse.low != -1 || parse.high != -1 || parse.even || parse.odd) {
                if (parse.low < 1) {
                    parse.low = 1;
                }
                if (parse.high < 1 || parse.high > maxNumber) {
                    parse.high = maxNumber;
                }
                if (parse.low > maxNumber) {
                    parse.low = maxNumber;
                }
                int inc = 1;
                if (parse.inverse) {
                    if (parse.low > parse.high) {
                        int t = parse.low;
                        parse.low = parse.high;
                        parse.high = t;
                    }
                    ListIterator<Integer> it = list.listIterator();
                    while (it.hasNext()) {
                        int n = ((Integer) it.next()).intValue();
                        if (!(parse.even && (n & 1) == 1) && (!(parse.odd && (n & 1) == 0) && n >= parse.low && n <= parse.high)) {
                            it.remove();
                        }
                    }
                } else if (parse.low > parse.high) {
                    inc = -1;
                    if (parse.odd || parse.even) {
                        inc = -1 - 1;
                        if (parse.even) {
                            parse.low &= -2;
                        } else {
                            parse.low -= (parse.low & 1) == 1 ? 0 : 1;
                        }
                    }
                    for (k = parse.low; k >= parse.high; k += inc) {
                        list.add(Integer.valueOf(k));
                    }
                } else {
                    if (parse.odd || parse.even) {
                        inc = 1 + 1;
                        if (parse.odd) {
                            parse.low |= 1;
                        } else {
                            parse.low = ((parse.low & 1) == 1 ? 1 : 0) + parse.low;
                        }
                    }
                    for (k = parse.low; k <= parse.high; k += inc) {
                        list.add(Integer.valueOf(k));
                    }
                }
            }
        }
        return list;
    }
}

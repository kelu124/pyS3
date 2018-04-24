package com.itextpdf.text.pdf;

import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.error_messages.MessageLocalization;
import org.bytedeco.javacpp.dc1394;

public class Barcode128 extends Barcode {
    private static final byte[][] BARS = new byte[][]{new byte[]{(byte) 2, (byte) 1, (byte) 2, (byte) 2, (byte) 2, (byte) 2}, new byte[]{(byte) 2, (byte) 2, (byte) 2, (byte) 1, (byte) 2, (byte) 2}, new byte[]{(byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 1}, new byte[]{(byte) 1, (byte) 2, (byte) 1, (byte) 2, (byte) 2, (byte) 3}, new byte[]{(byte) 1, (byte) 2, (byte) 1, (byte) 3, (byte) 2, (byte) 2}, new byte[]{(byte) 1, (byte) 3, (byte) 1, (byte) 2, (byte) 2, (byte) 2}, new byte[]{(byte) 1, (byte) 2, (byte) 2, (byte) 2, (byte) 1, (byte) 3}, new byte[]{(byte) 1, (byte) 2, (byte) 2, (byte) 3, (byte) 1, (byte) 2}, new byte[]{(byte) 1, (byte) 3, (byte) 2, (byte) 2, (byte) 1, (byte) 2}, new byte[]{(byte) 2, (byte) 2, (byte) 1, (byte) 2, (byte) 1, (byte) 3}, new byte[]{(byte) 2, (byte) 2, (byte) 1, (byte) 3, (byte) 1, (byte) 2}, new byte[]{(byte) 2, (byte) 3, (byte) 1, (byte) 2, (byte) 1, (byte) 2}, new byte[]{(byte) 1, (byte) 1, (byte) 2, (byte) 2, (byte) 3, (byte) 2}, new byte[]{(byte) 1, (byte) 2, (byte) 2, (byte) 1, (byte) 3, (byte) 2}, new byte[]{(byte) 1, (byte) 2, (byte) 2, (byte) 2, (byte) 3, (byte) 1}, new byte[]{(byte) 1, (byte) 1, (byte) 3, (byte) 2, (byte) 2, (byte) 2}, new byte[]{(byte) 1, (byte) 2, (byte) 3, (byte) 1, (byte) 2, (byte) 2}, new byte[]{(byte) 1, (byte) 2, (byte) 3, (byte) 2, (byte) 2, (byte) 1}, new byte[]{(byte) 2, (byte) 2, (byte) 3, (byte) 2, (byte) 1, (byte) 1}, new byte[]{(byte) 2, (byte) 2, (byte) 1, (byte) 1, (byte) 3, (byte) 2}, new byte[]{(byte) 2, (byte) 2, (byte) 1, (byte) 2, (byte) 3, (byte) 1}, new byte[]{(byte) 2, (byte) 1, (byte) 3, (byte) 2, (byte) 1, (byte) 2}, new byte[]{(byte) 2, (byte) 2, (byte) 3, (byte) 1, (byte) 1, (byte) 2}, new byte[]{(byte) 3, (byte) 1, (byte) 2, (byte) 1, (byte) 3, (byte) 1}, new byte[]{(byte) 3, (byte) 1, (byte) 1, (byte) 2, (byte) 2, (byte) 2}, new byte[]{(byte) 3, (byte) 2, (byte) 1, (byte) 1, (byte) 2, (byte) 2}, new byte[]{(byte) 3, (byte) 2, (byte) 1, (byte) 2, (byte) 2, (byte) 1}, new byte[]{(byte) 3, (byte) 1, (byte) 2, (byte) 2, (byte) 1, (byte) 2}, new byte[]{(byte) 3, (byte) 2, (byte) 2, (byte) 1, (byte) 1, (byte) 2}, new byte[]{(byte) 3, (byte) 2, (byte) 2, (byte) 2, (byte) 1, (byte) 1}, new byte[]{(byte) 2, (byte) 1, (byte) 2, (byte) 1, (byte) 2, (byte) 3}, new byte[]{(byte) 2, (byte) 1, (byte) 2, (byte) 3, (byte) 2, (byte) 1}, new byte[]{(byte) 2, (byte) 3, (byte) 2, (byte) 1, (byte) 2, (byte) 1}, new byte[]{(byte) 1, (byte) 1, (byte) 1, (byte) 3, (byte) 2, (byte) 3}, new byte[]{(byte) 1, (byte) 3, (byte) 1, (byte) 1, (byte) 2, (byte) 3}, new byte[]{(byte) 1, (byte) 3, (byte) 1, (byte) 3, (byte) 2, (byte) 1}, new byte[]{(byte) 1, (byte) 1, (byte) 2, (byte) 3, (byte) 1, (byte) 3}, new byte[]{(byte) 1, (byte) 3, (byte) 2, (byte) 1, (byte) 1, (byte) 3}, new byte[]{(byte) 1, (byte) 3, (byte) 2, (byte) 3, (byte) 1, (byte) 1}, new byte[]{(byte) 2, (byte) 1, (byte) 1, (byte) 3, (byte) 1, (byte) 3}, new byte[]{(byte) 2, (byte) 3, (byte) 1, (byte) 1, (byte) 1, (byte) 3}, new byte[]{(byte) 2, (byte) 3, (byte) 1, (byte) 3, (byte) 1, (byte) 1}, new byte[]{(byte) 1, (byte) 1, (byte) 2, (byte) 1, (byte) 3, (byte) 3}, new byte[]{(byte) 1, (byte) 1, (byte) 2, (byte) 3, (byte) 3, (byte) 1}, new byte[]{(byte) 1, (byte) 3, (byte) 2, (byte) 1, (byte) 3, (byte) 1}, new byte[]{(byte) 1, (byte) 1, (byte) 3, (byte) 1, (byte) 2, (byte) 3}, new byte[]{(byte) 1, (byte) 1, (byte) 3, (byte) 3, (byte) 2, (byte) 1}, new byte[]{(byte) 1, (byte) 3, (byte) 3, (byte) 1, (byte) 2, (byte) 1}, new byte[]{(byte) 3, (byte) 1, (byte) 3, (byte) 1, (byte) 2, (byte) 1}, new byte[]{(byte) 2, (byte) 1, (byte) 1, (byte) 3, (byte) 3, (byte) 1}, new byte[]{(byte) 2, (byte) 3, (byte) 1, (byte) 1, (byte) 3, (byte) 1}, new byte[]{(byte) 2, (byte) 1, (byte) 3, (byte) 1, (byte) 1, (byte) 3}, new byte[]{(byte) 2, (byte) 1, (byte) 3, (byte) 3, (byte) 1, (byte) 1}, new byte[]{(byte) 2, (byte) 1, (byte) 3, (byte) 1, (byte) 3, (byte) 1}, new byte[]{(byte) 3, (byte) 1, (byte) 1, (byte) 1, (byte) 2, (byte) 3}, new byte[]{(byte) 3, (byte) 1, (byte) 1, (byte) 3, (byte) 2, (byte) 1}, new byte[]{(byte) 3, (byte) 3, (byte) 1, (byte) 1, (byte) 2, (byte) 1}, new byte[]{(byte) 3, (byte) 1, (byte) 2, (byte) 1, (byte) 1, (byte) 3}, new byte[]{(byte) 3, (byte) 1, (byte) 2, (byte) 3, (byte) 1, (byte) 1}, new byte[]{(byte) 3, (byte) 3, (byte) 2, (byte) 1, (byte) 1, (byte) 1}, new byte[]{(byte) 3, (byte) 1, (byte) 4, (byte) 1, (byte) 1, (byte) 1}, new byte[]{(byte) 2, (byte) 2, (byte) 1, (byte) 4, (byte) 1, (byte) 1}, new byte[]{(byte) 4, (byte) 3, (byte) 1, (byte) 1, (byte) 1, (byte) 1}, new byte[]{(byte) 1, (byte) 1, (byte) 1, (byte) 2, (byte) 2, (byte) 4}, new byte[]{(byte) 1, (byte) 1, (byte) 1, (byte) 4, (byte) 2, (byte) 2}, new byte[]{(byte) 1, (byte) 2, (byte) 1, (byte) 1, (byte) 2, (byte) 4}, new byte[]{(byte) 1, (byte) 2, (byte) 1, (byte) 4, (byte) 2, (byte) 1}, new byte[]{(byte) 1, (byte) 4, (byte) 1, (byte) 1, (byte) 2, (byte) 2}, new byte[]{(byte) 1, (byte) 4, (byte) 1, (byte) 2, (byte) 2, (byte) 1}, new byte[]{(byte) 1, (byte) 1, (byte) 2, (byte) 2, (byte) 1, (byte) 4}, new byte[]{(byte) 1, (byte) 1, (byte) 2, (byte) 4, (byte) 1, (byte) 2}, new byte[]{(byte) 1, (byte) 2, (byte) 2, (byte) 1, (byte) 1, (byte) 4}, new byte[]{(byte) 1, (byte) 2, (byte) 2, (byte) 4, (byte) 1, (byte) 1}, new byte[]{(byte) 1, (byte) 4, (byte) 2, (byte) 1, (byte) 1, (byte) 2}, new byte[]{(byte) 1, (byte) 4, (byte) 2, (byte) 2, (byte) 1, (byte) 1}, new byte[]{(byte) 2, (byte) 4, (byte) 1, (byte) 2, (byte) 1, (byte) 1}, new byte[]{(byte) 2, (byte) 2, (byte) 1, (byte) 1, (byte) 1, (byte) 4}, new byte[]{(byte) 4, (byte) 1, (byte) 3, (byte) 1, (byte) 1, (byte) 1}, new byte[]{(byte) 2, (byte) 4, (byte) 1, (byte) 1, (byte) 1, (byte) 2}, new byte[]{(byte) 1, (byte) 3, (byte) 4, (byte) 1, (byte) 1, (byte) 1}, new byte[]{(byte) 1, (byte) 1, (byte) 1, (byte) 2, (byte) 4, (byte) 2}, new byte[]{(byte) 1, (byte) 2, (byte) 1, (byte) 1, (byte) 4, (byte) 2}, new byte[]{(byte) 1, (byte) 2, (byte) 1, (byte) 2, (byte) 4, (byte) 1}, new byte[]{(byte) 1, (byte) 1, (byte) 4, (byte) 2, (byte) 1, (byte) 2}, new byte[]{(byte) 1, (byte) 2, (byte) 4, (byte) 1, (byte) 1, (byte) 2}, new byte[]{(byte) 1, (byte) 2, (byte) 4, (byte) 2, (byte) 1, (byte) 1}, new byte[]{(byte) 4, (byte) 1, (byte) 1, (byte) 2, (byte) 1, (byte) 2}, new byte[]{(byte) 4, (byte) 2, (byte) 1, (byte) 1, (byte) 1, (byte) 2}, new byte[]{(byte) 4, (byte) 2, (byte) 1, (byte) 2, (byte) 1, (byte) 1}, new byte[]{(byte) 2, (byte) 1, (byte) 2, (byte) 1, (byte) 4, (byte) 1}, new byte[]{(byte) 2, (byte) 1, (byte) 4, (byte) 1, (byte) 2, (byte) 1}, new byte[]{(byte) 4, (byte) 1, (byte) 2, (byte) 1, (byte) 2, (byte) 1}, new byte[]{(byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 4, (byte) 3}, new byte[]{(byte) 1, (byte) 1, (byte) 1, (byte) 3, (byte) 4, (byte) 1}, new byte[]{(byte) 1, (byte) 3, (byte) 1, (byte) 1, (byte) 4, (byte) 1}, new byte[]{(byte) 1, (byte) 1, (byte) 4, (byte) 1, (byte) 1, (byte) 3}, new byte[]{(byte) 1, (byte) 1, (byte) 4, (byte) 3, (byte) 1, (byte) 1}, new byte[]{(byte) 4, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 3}, new byte[]{(byte) 4, (byte) 1, (byte) 1, (byte) 3, (byte) 1, (byte) 1}, new byte[]{(byte) 1, (byte) 1, (byte) 3, (byte) 1, (byte) 4, (byte) 1}, new byte[]{(byte) 1, (byte) 1, (byte) 4, (byte) 1, (byte) 3, (byte) 1}, new byte[]{(byte) 3, (byte) 1, (byte) 1, (byte) 1, (byte) 4, (byte) 1}, new byte[]{(byte) 4, (byte) 1, (byte) 1, (byte) 1, (byte) 3, (byte) 1}, new byte[]{(byte) 2, (byte) 1, (byte) 1, (byte) 4, (byte) 1, (byte) 2}, new byte[]{(byte) 2, (byte) 1, (byte) 1, (byte) 2, (byte) 1, (byte) 4}, new byte[]{(byte) 2, (byte) 1, (byte) 1, (byte) 2, (byte) 3, (byte) 2}};
    private static final byte[] BARS_STOP = new byte[]{(byte) 2, (byte) 3, (byte) 3, (byte) 1, (byte) 1, (byte) 1, (byte) 2};
    public static final char CODE_A = 'È';
    public static final char CODE_AB_TO_C = 'c';
    public static final char CODE_AC_TO_B = 'd';
    public static final char CODE_BC_TO_A = 'e';
    public static final char CODE_C = 'Ç';
    public static final char DEL = 'Ã';
    public static final char FNC1 = 'Ê';
    public static final char FNC1_INDEX = 'f';
    public static final char FNC2 = 'Å';
    public static final char FNC3 = 'Ä';
    public static final char FNC4 = 'È';
    public static final char SHIFT = 'Æ';
    public static final char STARTA = 'Ë';
    public static final char STARTB = 'Ì';
    public static final char STARTC = 'Í';
    public static final char START_A = 'g';
    public static final char START_B = 'h';
    public static final char START_C = 'i';
    private static final IntHashtable ais = new IntHashtable();
    private Barcode128CodeSet codeSet = Barcode128CodeSet.AUTO;

    public enum Barcode128CodeSet {
        A,
        B,
        C,
        AUTO;

        public char getStartSymbol() {
            switch (this) {
                case A:
                    return Barcode128.START_A;
                case C:
                    return Barcode128.START_C;
                default:
                    return Barcode128.START_B;
            }
        }
    }

    static {
        int k;
        ais.put(0, 20);
        ais.put(1, 16);
        ais.put(2, 16);
        ais.put(10, -1);
        ais.put(11, 9);
        ais.put(12, 8);
        ais.put(13, 8);
        ais.put(15, 8);
        ais.put(17, 8);
        ais.put(20, 4);
        ais.put(21, -1);
        ais.put(22, -1);
        ais.put(23, -1);
        ais.put(240, -1);
        ais.put(241, -1);
        ais.put(Callback.DEFAULT_SWIPE_ANIMATION_DURATION, -1);
        ais.put(251, -1);
        ais.put(252, -1);
        ais.put(30, -1);
        for (k = 3100; k < 3700; k++) {
            ais.put(k, 10);
        }
        ais.put(37, -1);
        for (k = 3900; k < 3940; k++) {
            ais.put(k, -1);
        }
        ais.put(400, -1);
        ais.put(401, -1);
        ais.put(402, 20);
        ais.put(403, -1);
        for (k = 410; k < 416; k++) {
            ais.put(k, 16);
        }
        ais.put(420, -1);
        ais.put(421, -1);
        ais.put(422, 6);
        ais.put(423, -1);
        ais.put(424, 6);
        ais.put(dc1394.DC1394_FEATURE_IRIS, 6);
        ais.put(426, 6);
        ais.put(7001, 17);
        ais.put(7002, -1);
        for (k = 7030; k < 7040; k++) {
            ais.put(k, -1);
        }
        ais.put(8001, 18);
        ais.put(8002, -1);
        ais.put(8003, -1);
        ais.put(8004, -1);
        ais.put(8005, 10);
        ais.put(8006, 22);
        ais.put(8007, -1);
        ais.put(8008, -1);
        ais.put(8018, 22);
        ais.put(8020, -1);
        ais.put(8100, 10);
        ais.put(8101, 14);
        ais.put(8102, 6);
        for (k = 90; k < 100; k++) {
            ais.put(k, -1);
        }
    }

    public Barcode128() {
        try {
            this.x = 0.8f;
            this.font = BaseFont.createFont("Helvetica", "winansi", false);
            this.size = 8.0f;
            this.baseline = this.size;
            this.barHeight = this.size * BaseField.BORDER_WIDTH_THICK;
            this.textAlignment = 1;
            this.codeType = 9;
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    public void setCodeSet(Barcode128CodeSet codeSet) {
        this.codeSet = codeSet;
    }

    public Barcode128CodeSet getCodeSet() {
        return this.codeSet;
    }

    public static String removeFNC1(String code) {
        int len = code.length();
        StringBuffer buf = new StringBuffer(len);
        for (int k = 0; k < len; k++) {
            char c = code.charAt(k);
            if (c >= ' ' && c <= '~') {
                buf.append(c);
            }
        }
        return buf.toString();
    }

    public static String getHumanReadableUCCEAN(String code) {
        StringBuffer buf = new StringBuffer();
        String fnc1 = String.valueOf(FNC1);
        while (true) {
            if (!code.startsWith(fnc1)) {
                int n = 0;
                int idlen = 0;
                int k = 2;
                while (k < 5 && code.length() >= k) {
                    try {
                        n = ais.get(Integer.parseInt(code.substring(0, k)));
                        if (n != 0) {
                            idlen = k;
                            break;
                        }
                        k++;
                    } catch (Exception e) {
                    }
                }
                if (idlen != 0) {
                    buf.append('(').append(code.substring(0, idlen)).append(')');
                    code = code.substring(idlen);
                    if (n <= 0) {
                        int idx = code.indexOf(202);
                        if (idx < 0) {
                            break;
                        }
                        buf.append(code.substring(0, idx));
                        code = code.substring(idx + 1);
                    } else {
                        n -= idlen;
                        if (code.length() <= n) {
                            break;
                        }
                        buf.append(removeFNC1(code.substring(0, n)));
                        code = code.substring(n);
                    }
                } else {
                    break;
                }
            }
            code = code.substring(1);
        }
        buf.append(removeFNC1(code));
        return buf.toString();
    }

    static boolean isNextDigits(String text, int textIndex, int numDigits) {
        int len = text.length();
        while (textIndex < len && numDigits > 0) {
            if (text.charAt(textIndex) == FNC1) {
                textIndex++;
            } else {
                int n = Math.min(2, numDigits);
                if (textIndex + n > len) {
                    return false;
                }
                int n2 = n;
                int textIndex2 = textIndex;
                while (true) {
                    n = n2 - 1;
                    if (n2 <= 0) {
                        break;
                    }
                    textIndex = textIndex2 + 1;
                    char c = text.charAt(textIndex2);
                    if (c < '0') {
                        return false;
                    }
                    if (c > '9') {
                        return false;
                    }
                    numDigits--;
                    n2 = n;
                    textIndex2 = textIndex;
                }
                textIndex = textIndex2;
            }
        }
        if (numDigits == 0) {
            return true;
        }
        return false;
    }

    static String getPackedRawDigits(String text, int textIndex, int numDigits) {
        StringBuilder out = new StringBuilder("");
        int start = textIndex;
        int textIndex2 = textIndex;
        while (numDigits > 0) {
            if (text.charAt(textIndex2) == FNC1) {
                out.append(FNC1_INDEX);
                textIndex2++;
            } else {
                numDigits -= 2;
                textIndex = textIndex2 + 1;
                int c1 = text.charAt(textIndex2) - 48;
                textIndex2 = textIndex + 1;
                out.append((char) ((c1 * 10) + (text.charAt(textIndex) - 48)));
            }
        }
        return ((char) (textIndex2 - start)) + out.toString();
    }

    public static String getRawText(String text, boolean ucc, Barcode128CodeSet codeSet) {
        String out = "";
        int tLen = text.length();
        if (tLen == 0) {
            out = out + codeSet.getStartSymbol();
            if (ucc) {
                out = out + FNC1_INDEX;
            }
            return out;
        }
        int c;
        int index;
        int k = 0;
        while (k < tLen) {
            c = text.charAt(k);
            if (c <= 127 || c == 202) {
                k++;
            } else {
                throw new RuntimeException(MessageLocalization.getComposedMessage("there.are.illegal.characters.for.barcode.128.in.1", text));
            }
        }
        c = text.charAt(0);
        char currentCode = START_B;
        if ((codeSet == Barcode128CodeSet.AUTO || codeSet == Barcode128CodeSet.C) && isNextDigits(text, 0, 2)) {
            currentCode = START_C;
            out = out + START_C;
            if (ucc) {
                out = out + FNC1_INDEX;
            }
            String out2 = getPackedRawDigits(text, 0, 2);
            index = 0 + out2.charAt(0);
            out = out + out2.substring(1);
        } else if (c < 32) {
            currentCode = START_A;
            out = out + START_A;
            if (ucc) {
                out = out + FNC1_INDEX;
            }
            out = out + ((char) (c + 64));
            index = 0 + 1;
        } else {
            out = out + START_B;
            if (ucc) {
                out = out + FNC1_INDEX;
            }
            if (c == 202) {
                out = out + FNC1_INDEX;
            } else {
                out = out + ((char) (c - 32));
            }
            index = 0 + 1;
        }
        if (currentCode != codeSet.getStartSymbol()) {
            throw new RuntimeException(MessageLocalization.getComposedMessage("there.are.illegal.characters.for.barcode.128.in.1", text));
        }
        while (true) {
            int index2 = index;
            if (index2 >= tLen) {
                return out;
            }
            switch (currentCode) {
                case 'g':
                    if (codeSet != Barcode128CodeSet.AUTO || !isNextDigits(text, index2, 4)) {
                        index = index2 + 1;
                        c = text.charAt(index2);
                        if (c != 202) {
                            if (c <= 95) {
                                if (c >= 32) {
                                    out = out + ((char) (c - 32));
                                    break;
                                }
                                out = out + ((char) (c + 64));
                                break;
                            }
                            currentCode = START_B;
                            out = (out + CODE_AC_TO_B) + ((char) (c - 32));
                            break;
                        }
                        out = out + FNC1_INDEX;
                        break;
                    }
                    currentCode = START_C;
                    out = out + CODE_AB_TO_C;
                    out2 = getPackedRawDigits(text, index2, 4);
                    index = index2 + out2.charAt(0);
                    out = out + out2.substring(1);
                    break;
                    break;
                case 'h':
                    if (codeSet != Barcode128CodeSet.AUTO || !isNextDigits(text, index2, 4)) {
                        index = index2 + 1;
                        c = text.charAt(index2);
                        if (c != 202) {
                            if (c >= 32) {
                                out = out + ((char) (c - 32));
                                break;
                            }
                            currentCode = START_A;
                            out = (out + CODE_BC_TO_A) + ((char) (c + 64));
                            break;
                        }
                        out = out + FNC1_INDEX;
                        break;
                    }
                    currentCode = START_C;
                    out = out + CODE_AB_TO_C;
                    out2 = getPackedRawDigits(text, index2, 4);
                    index = index2 + out2.charAt(0);
                    out = out + out2.substring(1);
                    break;
                    break;
                case 'i':
                    if (!isNextDigits(text, index2, 2)) {
                        index = index2 + 1;
                        c = text.charAt(index2);
                        if (c != 202) {
                            if (c >= 32) {
                                currentCode = START_B;
                                out = (out + CODE_AC_TO_B) + ((char) (c - 32));
                                break;
                            }
                            currentCode = START_A;
                            out = (out + CODE_BC_TO_A) + ((char) (c + 64));
                            break;
                        }
                        out = out + FNC1_INDEX;
                        break;
                    }
                    out2 = getPackedRawDigits(text, index2, 2);
                    index = index2 + out2.charAt(0);
                    out = out + out2.substring(1);
                    break;
                default:
                    index = index2;
                    break;
            }
            if (codeSet != Barcode128CodeSet.AUTO && currentCode != codeSet.getStartSymbol()) {
                throw new RuntimeException(MessageLocalization.getComposedMessage("there.are.illegal.characters.for.barcode.128.in.1", text));
            }
        }
    }

    public static String getRawText(String text, boolean ucc) {
        return getRawText(text, ucc, Barcode128CodeSet.AUTO);
    }

    public static byte[] getBarsCode128Raw(String text) {
        int k;
        int idx = text.indexOf(65535);
        if (idx >= 0) {
            text = text.substring(0, idx);
        }
        int chk = text.charAt(0);
        for (k = 1; k < text.length(); k++) {
            chk += text.charAt(k) * k;
        }
        text = text + ((char) (chk % 103));
        byte[] bars = new byte[(((text.length() + 1) * 6) + 7)];
        k = 0;
        while (k < text.length()) {
            System.arraycopy(BARS[text.charAt(k)], 0, bars, k * 6, 6);
            k++;
        }
        System.arraycopy(BARS_STOP, 0, bars, k * 6, 7);
        return bars;
    }

    public Rectangle getBarcodeSize() {
        int idx;
        String fullCode;
        boolean z = false;
        float fontX = 0.0f;
        float fontY = 0.0f;
        if (this.font != null) {
            if (this.baseline > 0.0f) {
                fontY = this.baseline - this.font.getFontDescriptor(3, this.size);
            } else {
                fontY = (-this.baseline) + this.size;
            }
            if (this.codeType == 11) {
                idx = this.code.indexOf(65535);
                if (idx < 0) {
                    fullCode = "";
                } else {
                    fullCode = this.code.substring(idx + 1);
                }
            } else if (this.codeType == 10) {
                fullCode = getHumanReadableUCCEAN(this.code);
            } else {
                fullCode = removeFNC1(this.code);
            }
            BaseFont baseFont = this.font;
            if (this.altText != null) {
                fullCode = this.altText;
            }
            fontX = baseFont.getWidthPoint(fullCode, this.size);
        }
        if (this.codeType == 11) {
            idx = this.code.indexOf(65535);
            if (idx >= 0) {
                fullCode = this.code.substring(0, idx);
            } else {
                fullCode = this.code;
            }
        } else {
            String str = this.code;
            if (this.codeType == 10) {
                z = true;
            }
            fullCode = getRawText(str, z, this.codeSet);
        }
        return new Rectangle(Math.max((((float) ((fullCode.length() + 2) * 11)) * this.x) + (BaseField.BORDER_WIDTH_MEDIUM * this.x), fontX), this.barHeight + fontY);
    }

    public Rectangle placeBarcode(PdfContentByte cb, BaseColor barColor, BaseColor textColor) {
        int idx;
        String fullCode;
        String bCode;
        if (this.codeType == 11) {
            idx = this.code.indexOf(65535);
            if (idx < 0) {
                fullCode = "";
            } else {
                fullCode = this.code.substring(idx + 1);
            }
        } else if (this.codeType == 10) {
            fullCode = getHumanReadableUCCEAN(this.code);
        } else {
            fullCode = removeFNC1(this.code);
        }
        float fontX = 0.0f;
        if (this.font != null) {
            BaseFont baseFont = this.font;
            if (this.altText != null) {
                fullCode = this.altText;
            }
            fontX = baseFont.getWidthPoint(fullCode, this.size);
        }
        if (this.codeType == 11) {
            idx = this.code.indexOf(65535);
            if (idx >= 0) {
                bCode = this.code.substring(0, idx);
            } else {
                bCode = this.code;
            }
        } else {
            bCode = getRawText(this.code, this.codeType == 10, this.codeSet);
        }
        float fullWidth = (((float) ((bCode.length() + 2) * 11)) * this.x) + (BaseField.BORDER_WIDTH_MEDIUM * this.x);
        float barStartX = 0.0f;
        float textStartX = 0.0f;
        switch (this.textAlignment) {
            case 0:
                break;
            case 2:
                if (fontX <= fullWidth) {
                    textStartX = fullWidth - fontX;
                    break;
                }
                barStartX = fontX - fullWidth;
                break;
            default:
                if (fontX <= fullWidth) {
                    textStartX = (fullWidth - fontX) / BaseField.BORDER_WIDTH_MEDIUM;
                    break;
                }
                barStartX = (fontX - fullWidth) / BaseField.BORDER_WIDTH_MEDIUM;
                break;
        }
        float barStartY = 0.0f;
        float textStartY = 0.0f;
        if (this.font != null) {
            if (this.baseline <= 0.0f) {
                textStartY = this.barHeight - this.baseline;
            } else {
                textStartY = -this.font.getFontDescriptor(3, this.size);
                barStartY = textStartY + this.baseline;
            }
        }
        byte[] bars = getBarsCode128Raw(bCode);
        boolean print = true;
        if (barColor != null) {
            cb.setColorFill(barColor);
        }
        for (byte b : bars) {
            float w = ((float) b) * this.x;
            if (print) {
                cb.rectangle(barStartX, barStartY, w - this.inkSpreading, this.barHeight);
            }
            print = !print;
            barStartX += w;
        }
        cb.fill();
        if (this.font != null) {
            if (textColor != null) {
                cb.setColorFill(textColor);
            }
            cb.beginText();
            cb.setFontAndSize(this.font, this.size);
            cb.setTextMatrix(textStartX, textStartY);
            cb.showText(fullCode);
            cb.endText();
        }
        return getBarcodeSize();
    }

    public void setCode(String code) {
        if (getCodeType() == 10 && code.startsWith("(")) {
            int idx = 0;
            StringBuilder ret = new StringBuilder("");
            while (idx >= 0) {
                int end = code.indexOf(41, idx);
                if (end < 0) {
                    throw new IllegalArgumentException(MessageLocalization.getComposedMessage("badly.formed.ucc.string.1", code));
                }
                String sai = code.substring(idx + 1, end);
                if (sai.length() < 2) {
                    throw new IllegalArgumentException(MessageLocalization.getComposedMessage("ai.too.short.1", sai));
                }
                int ai = Integer.parseInt(sai);
                int len = ais.get(ai);
                if (len == 0) {
                    throw new IllegalArgumentException(MessageLocalization.getComposedMessage("ai.not.found.1", sai));
                }
                int next;
                sai = String.valueOf(ai);
                if (sai.length() == 1) {
                    sai = "0" + sai;
                }
                idx = code.indexOf(40, end);
                if (idx < 0) {
                    next = code.length();
                } else {
                    next = idx;
                }
                ret.append(sai).append(code.substring(end + 1, next));
                if (len < 0) {
                    if (idx >= 0) {
                        ret.append(FNC1);
                    }
                } else if (((next - end) - 1) + sai.length() != len) {
                    throw new IllegalArgumentException(MessageLocalization.getComposedMessage("invalid.ai.length.1", sai));
                }
            }
            super.setCode(ret.toString());
            return;
        }
        super.setCode(code);
    }
}

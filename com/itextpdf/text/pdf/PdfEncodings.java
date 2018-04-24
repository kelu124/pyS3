package com.itextpdf.text.pdf;

import android.support.v4.view.MotionEventCompat;
import com.google.common.base.Ascii;
import com.itextpdf.text.DocWriter;
import com.itextpdf.text.ExceptionConverter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.util.HashMap;
import org.apache.poi.ss.formula.ptg.MemFuncPtg;
import org.apache.poi.ss.formula.ptg.RefErrorPtg;
import org.bytedeco.javacpp.avcodec;

public class PdfEncodings {
    static HashMap<String, ExtraEncoding> extraEncodings = new HashMap();
    static final IntHashtable pdfEncoding = new IntHashtable();
    static final char[] pdfEncodingByteToChar = new char[]{'\u0000', '\u0001', '\u0002', '\u0003', '\u0004', '\u0005', '\u0006', '\u0007', '\b', '\t', '\n', '\u000b', '\f', '\r', '\u000e', '\u000f', '\u0010', '\u0011', '\u0012', '\u0013', '\u0014', '\u0015', '\u0016', '\u0017', '\u0018', '\u0019', '\u001a', '\u001b', '\u001c', '\u001d', '\u001e', '\u001f', ' ', '!', '\"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', '0', '1', PdfWriter.VERSION_1_2, PdfWriter.VERSION_1_3, PdfWriter.VERSION_1_4, PdfWriter.VERSION_1_5, PdfWriter.VERSION_1_6, PdfWriter.VERSION_1_7, '8', '9', ':', ';', '<', '=', '>', '?', '@', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '[', '\\', ']', '^', '_', '`', 'a', 'b', Barcode128.CODE_AB_TO_C, Barcode128.CODE_AC_TO_B, Barcode128.CODE_BC_TO_A, Barcode128.FNC1_INDEX, Barcode128.START_A, Barcode128.START_B, Barcode128.START_C, 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '{', '|', '}', '~', Ascii.MAX, '•', '†', '‡', '…', '—', '–', 'ƒ', '⁄', '‹', '›', '−', '‰', '„', '“', '”', '‘', '’', '‚', '™', 'ﬁ', 'ﬂ', 'Ł', 'Œ', 'Š', 'Ÿ', 'Ž', 'ı', 'ł', 'œ', 'š', 'ž', '�', '€', '¡', '¢', '£', '¤', '¥', '¦', '§', '¨', '©', 'ª', '«', '¬', '­', '®', '¯', '°', '±', '²', '³', '´', 'µ', '¶', '·', '¸', '¹', 'º', '»', '¼', '½', '¾', '¿', 'À', 'Á', 'Â', Barcode128.DEL, Barcode128.FNC3, Barcode128.FNC2, Barcode128.SHIFT, Barcode128.CODE_C, 'È', 'É', Barcode128.FNC1, Barcode128.STARTA, Barcode128.STARTB, Barcode128.STARTC, 'Î', 'Ï', 'Ð', 'Ñ', 'Ò', 'Ó', 'Ô', 'Õ', 'Ö', '×', 'Ø', 'Ù', 'Ú', 'Û', 'Ü', 'Ý', 'Þ', 'ß', 'à', 'á', 'â', 'ã', 'ä', 'å', 'æ', 'ç', 'è', 'é', 'ê', 'ë', 'ì', 'í', 'î', 'ï', 'ð', 'ñ', 'ò', 'ó', 'ô', 'õ', 'ö', '÷', 'ø', 'ù', 'ú', 'û', 'ü', 'ý', 'þ', 'ÿ'};
    static final IntHashtable winansi = new IntHashtable();
    static final char[] winansiByteToChar = new char[]{'\u0000', '\u0001', '\u0002', '\u0003', '\u0004', '\u0005', '\u0006', '\u0007', '\b', '\t', '\n', '\u000b', '\f', '\r', '\u000e', '\u000f', '\u0010', '\u0011', '\u0012', '\u0013', '\u0014', '\u0015', '\u0016', '\u0017', '\u0018', '\u0019', '\u001a', '\u001b', '\u001c', '\u001d', '\u001e', '\u001f', ' ', '!', '\"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', '0', '1', PdfWriter.VERSION_1_2, PdfWriter.VERSION_1_3, PdfWriter.VERSION_1_4, PdfWriter.VERSION_1_5, PdfWriter.VERSION_1_6, PdfWriter.VERSION_1_7, '8', '9', ':', ';', '<', '=', '>', '?', '@', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '[', '\\', ']', '^', '_', '`', 'a', 'b', Barcode128.CODE_AB_TO_C, Barcode128.CODE_AC_TO_B, Barcode128.CODE_BC_TO_A, Barcode128.FNC1_INDEX, Barcode128.START_A, Barcode128.START_B, Barcode128.START_C, 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '{', '|', '}', '~', Ascii.MAX, '€', '�', '‚', 'ƒ', '„', '…', '†', '‡', 'ˆ', '‰', 'Š', '‹', 'Œ', '�', 'Ž', '�', '�', '‘', '’', '“', '”', '•', '–', '—', '˜', '™', 'š', '›', 'œ', '�', 'ž', 'Ÿ', ' ', '¡', '¢', '£', '¤', '¥', '¦', '§', '¨', '©', 'ª', '«', '¬', '­', '®', '¯', '°', '±', '²', '³', '´', 'µ', '¶', '·', '¸', '¹', 'º', '»', '¼', '½', '¾', '¿', 'À', 'Á', 'Â', Barcode128.DEL, Barcode128.FNC3, Barcode128.FNC2, Barcode128.SHIFT, Barcode128.CODE_C, 'È', 'É', Barcode128.FNC1, Barcode128.STARTA, Barcode128.STARTB, Barcode128.STARTC, 'Î', 'Ï', 'Ð', 'Ñ', 'Ò', 'Ó', 'Ô', 'Õ', 'Ö', '×', 'Ø', 'Ù', 'Ú', 'Û', 'Ü', 'Ý', 'Þ', 'ß', 'à', 'á', 'â', 'ã', 'ä', 'å', 'æ', 'ç', 'è', 'é', 'ê', 'ë', 'ì', 'í', 'î', 'ï', 'ð', 'ñ', 'ò', 'ó', 'ô', 'õ', 'ö', '÷', 'ø', 'ù', 'ú', 'û', 'ü', 'ý', 'þ', 'ÿ'};

    private static class Cp437Conversion implements ExtraEncoding {
        private static IntHashtable c2b = new IntHashtable();
        private static final char[] table = new char[]{Barcode128.CODE_C, 'ü', 'é', 'â', 'ä', 'à', 'å', 'ç', 'ê', 'ë', 'è', 'ï', 'î', 'ì', Barcode128.FNC3, Barcode128.FNC2, 'É', 'æ', Barcode128.SHIFT, 'ô', 'ö', 'ò', 'û', 'ù', 'ÿ', 'Ö', 'Ü', '¢', '£', '¥', '₧', 'ƒ', 'á', 'í', 'ó', 'ú', 'ñ', 'Ñ', 'ª', 'º', '¿', '⌐', '¬', '½', '¼', '¡', '«', '»', '░', '▒', '▓', '│', '┤', '╡', '╢', '╖', '╕', '╣', '║', '╗', '╝', '╜', '╛', '┐', '└', '┴', '┬', '├', '─', '┼', '╞', '╟', '╚', '╔', '╩', '╦', '╠', '═', '╬', '╧', '╨', '╤', '╥', '╙', '╘', '╒', '╓', '╫', '╪', '┘', '┌', '█', '▄', '▌', '▐', '▀', 'α', 'ß', 'Γ', 'π', 'Σ', 'σ', 'µ', 'τ', 'Φ', 'Θ', 'Ω', 'δ', '∞', 'φ', 'ε', '∩', '≡', '±', '≥', '≤', '⌠', '⌡', '÷', '≈', '°', '∙', '·', '√', 'ⁿ', '²', '■', ' '};

        private Cp437Conversion() {
        }

        static {
            for (int k = 0; k < table.length; k++) {
                c2b.put(table[k], k + 128);
            }
        }

        public byte[] charToByte(String text, String encoding) {
            char[] cc = text.toCharArray();
            byte[] b = new byte[cc.length];
            int len = cc.length;
            int k = 0;
            int ptr = 0;
            while (k < len) {
                int ptr2;
                char c = cc[k];
                if (c < '') {
                    ptr2 = ptr + 1;
                    b[ptr] = (byte) c;
                } else {
                    byte v = (byte) c2b.get(c);
                    if (v != (byte) 0) {
                        ptr2 = ptr + 1;
                        b[ptr] = v;
                    } else {
                        ptr2 = ptr;
                    }
                }
                k++;
                ptr = ptr2;
            }
            if (ptr == len) {
                return b;
            }
            byte[] b2 = new byte[ptr];
            System.arraycopy(b, 0, b2, 0, ptr);
            return b2;
        }

        public byte[] charToByte(char char1, String encoding) {
            if (char1 < '') {
                return new byte[]{(byte) char1};
            }
            if (((byte) c2b.get(char1)) == (byte) 0) {
                return new byte[0];
            }
            return new byte[]{(byte) c2b.get(char1)};
        }

        public String byteToChar(byte[] b, String encoding) {
            int len = b.length;
            char[] cc = new char[len];
            int k = 0;
            int ptr = 0;
            while (k < len) {
                int ptr2;
                int c = b[k] & 255;
                if (c < 32) {
                    ptr2 = ptr;
                } else if (c < 128) {
                    ptr2 = ptr + 1;
                    cc[ptr] = (char) c;
                } else {
                    ptr2 = ptr + 1;
                    cc[ptr] = table[c - 128];
                }
                k++;
                ptr = ptr2;
            }
            return new String(cc, 0, ptr);
        }
    }

    private static class SymbolConversion implements ExtraEncoding {
        private static final IntHashtable t1 = new IntHashtable();
        private static final IntHashtable t2 = new IntHashtable();
        private static final char[] table1 = new char[]{'\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', ' ', '!', '∀', '#', '∃', '%', '&', '∋', '(', ')', '*', '+', ',', '-', '.', '/', '0', '1', PdfWriter.VERSION_1_2, PdfWriter.VERSION_1_3, PdfWriter.VERSION_1_4, PdfWriter.VERSION_1_5, PdfWriter.VERSION_1_6, PdfWriter.VERSION_1_7, '8', '9', ':', ';', '<', '=', '>', '?', '≅', 'Α', 'Β', 'Χ', 'Δ', 'Ε', 'Φ', 'Γ', 'Η', 'Ι', 'ϑ', 'Κ', 'Λ', 'Μ', 'Ν', 'Ο', 'Π', 'Θ', 'Ρ', 'Σ', 'Τ', 'Υ', 'ς', 'Ω', 'Ξ', 'Ψ', 'Ζ', '[', '∴', ']', '⊥', '_', '̅', 'α', 'β', 'χ', 'δ', 'ε', 'ϕ', 'γ', 'η', 'ι', 'φ', 'κ', 'λ', 'μ', 'ν', 'ο', 'π', 'θ', 'ρ', 'σ', 'τ', 'υ', 'ϖ', 'ω', 'ξ', 'ψ', 'ζ', '{', '|', '}', '~', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '€', 'ϒ', '′', '≤', '⁄', '∞', 'ƒ', '♣', '♦', '♥', '♠', '↔', '←', '↑', '→', '↓', '°', '±', '″', '≥', '×', '∝', '∂', '•', '÷', '≠', '≡', '≈', '…', '│', '─', '↵', 'ℵ', 'ℑ', 'ℜ', '℘', '⊗', '⊕', '∅', '∩', '∪', '⊃', '⊇', '⊄', '⊂', '⊆', '∈', '∉', '∠', '∇', '®', '©', '™', '∏', '√', '⋅', '¬', '∧', '∨', '⇔', '⇐', '⇑', '⇒', '⇓', '◊', '〈', '\u0000', '\u0000', '\u0000', '∑', '⎛', '⎜', '⎝', '⎡', '⎢', '⎣', '⎧', '⎨', '⎩', '⎪', '\u0000', '〉', '∫', '⌠', '⎮', '⌡', '⎞', '⎟', '⎠', '⎤', '⎥', '⎦', '⎫', '⎬', '⎭', '\u0000'};
        private static final char[] table2 = new char[]{'\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', ' ', '✁', '✂', '✃', '✄', '☎', '✆', '✇', '✈', '✉', '☛', '☞', '✌', '✍', '✎', '✏', '✐', '✑', '✒', '✓', '✔', '✕', '✖', '✗', '✘', '✙', '✚', '✛', '✜', '✝', '✞', '✟', '✠', '✡', '✢', '✣', '✤', '✥', '✦', '✧', '★', '✩', '✪', '✫', '✬', '✭', '✮', '✯', '✰', '✱', '✲', '✳', '✴', '✵', '✶', '✷', '✸', '✹', '✺', '✻', '✼', '✽', '✾', '✿', '❀', '❁', '❂', '❃', '❄', '❅', '❆', '❇', '❈', '❉', '❊', '❋', '●', '❍', '■', '❏', '❐', '❑', '❒', '▲', '▼', '◆', '❖', '◗', '❘', '❙', '❚', '❛', '❜', '❝', '❞', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '❡', '❢', '❣', '❤', '❥', '❦', '❧', '♣', '♦', '♥', '♠', '①', '②', '③', '④', '⑤', '⑥', '⑦', '⑧', '⑨', '⑩', '❶', '❷', '❸', '❹', '❺', '❻', '❼', '❽', '❾', '❿', '➀', '➁', '➂', '➃', '➄', '➅', '➆', '➇', '➈', '➉', '➊', '➋', '➌', '➍', '➎', '➏', '➐', '➑', '➒', '➓', '➔', '→', '↔', '↕', '➘', '➙', '➚', '➛', '➜', '➝', '➞', '➟', '➠', '➡', '➢', '➣', '➤', '➥', '➦', '➧', '➨', '➩', '➪', '➫', '➬', '➭', '➮', '➯', '\u0000', '➱', '➲', '➳', '➴', '➵', '➶', '➷', '➸', '➹', '➺', '➻', '➼', '➽', '➾', '\u0000'};
        private final char[] byteToChar;
        private IntHashtable translation;

        static {
            int k;
            for (k = 0; k < 256; k++) {
                int v = table1[k];
                if (v != 0) {
                    t1.put(v, k);
                }
            }
            for (k = 0; k < 256; k++) {
                v = table2[k];
                if (v != 0) {
                    t2.put(v, k);
                }
            }
        }

        SymbolConversion(boolean symbol) {
            if (symbol) {
                this.translation = t1;
                this.byteToChar = table1;
                return;
            }
            this.translation = t2;
            this.byteToChar = table2;
        }

        public byte[] charToByte(String text, String encoding) {
            char[] cc = text.toCharArray();
            byte[] b = new byte[cc.length];
            int len = cc.length;
            int k = 0;
            int ptr = 0;
            while (k < len) {
                int ptr2;
                byte v = (byte) this.translation.get(cc[k]);
                if (v != (byte) 0) {
                    ptr2 = ptr + 1;
                    b[ptr] = v;
                } else {
                    ptr2 = ptr;
                }
                k++;
                ptr = ptr2;
            }
            if (ptr == len) {
                return b;
            }
            byte[] b2 = new byte[ptr];
            System.arraycopy(b, 0, b2, 0, ptr);
            return b2;
        }

        public byte[] charToByte(char char1, String encoding) {
            if (((byte) this.translation.get(char1)) == (byte) 0) {
                return new byte[0];
            }
            return new byte[]{(byte) this.translation.get(char1)};
        }

        public String byteToChar(byte[] b, String encoding) {
            int len = b.length;
            char[] cc = new char[len];
            int k = 0;
            int ptr = 0;
            while (k < len) {
                int ptr2 = ptr + 1;
                cc[ptr] = this.byteToChar[b[k] & 255];
                k++;
                ptr = ptr2;
            }
            return new String(cc, 0, ptr);
        }
    }

    private static class SymbolTTConversion implements ExtraEncoding {
        private SymbolTTConversion() {
        }

        public byte[] charToByte(char char1, String encoding) {
            if ((char1 & MotionEventCompat.ACTION_POINTER_INDEX_MASK) != 0 && (char1 & MotionEventCompat.ACTION_POINTER_INDEX_MASK) != avcodec.MB_TYPE_L0L1) {
                return new byte[0];
            }
            return new byte[]{(byte) char1};
        }

        public byte[] charToByte(String text, String encoding) {
            char[] ch = text.toCharArray();
            byte[] b = new byte[ch.length];
            int len = ch.length;
            int k = 0;
            int ptr = 0;
            while (k < len) {
                int ptr2;
                char c = ch[k];
                if ((c & MotionEventCompat.ACTION_POINTER_INDEX_MASK) == 0 || (c & MotionEventCompat.ACTION_POINTER_INDEX_MASK) == avcodec.MB_TYPE_L0L1) {
                    ptr2 = ptr + 1;
                    b[ptr] = (byte) c;
                } else {
                    ptr2 = ptr;
                }
                k++;
                ptr = ptr2;
            }
            if (ptr == len) {
                return b;
            }
            byte[] b2 = new byte[ptr];
            System.arraycopy(b, 0, b2, 0, ptr);
            return b2;
        }

        public String byteToChar(byte[] b, String encoding) {
            return null;
        }
    }

    private static class WingdingsConversion implements ExtraEncoding {
        private static final byte[] table = new byte[]{(byte) 0, (byte) 35, (byte) 34, (byte) 0, (byte) 0, (byte) 0, MemFuncPtg.sid, DocWriter.GT, (byte) 81, RefErrorPtg.sid, (byte) 0, (byte) 0, (byte) 65, (byte) 63, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) -4, (byte) 0, (byte) 0, (byte) 0, (byte) -5, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 86, (byte) 0, (byte) 88, (byte) 89, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) -75, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) -74, (byte) 0, (byte) 0, (byte) 0, (byte) -83, (byte) -81, (byte) -84, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 124, (byte) 123, (byte) 0, (byte) 0, (byte) 0, (byte) 84, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) -90, (byte) 0, (byte) 0, (byte) 0, (byte) 113, (byte) 114, (byte) 0, (byte) 0, (byte) 0, (byte) 117, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 125, (byte) 126, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) -116, (byte) -115, (byte) -114, (byte) -113, (byte) -112, (byte) -111, (byte) -110, (byte) -109, (byte) -108, (byte) -107, (byte) -127, (byte) -126, (byte) -125, (byte) -124, (byte) -123, (byte) -122, (byte) -121, (byte) -120, (byte) -119, (byte) -118, (byte) -116, (byte) -115, (byte) -114, (byte) -113, (byte) -112, (byte) -111, (byte) -110, (byte) -109, (byte) -108, (byte) -107, (byte) -24, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) -24, (byte) -40, (byte) 0, (byte) 0, (byte) -60, (byte) -58, (byte) 0, (byte) 0, (byte) -16, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) -36, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0};

        private WingdingsConversion() {
        }

        public byte[] charToByte(char char1, String encoding) {
            if (char1 == ' ') {
                return new byte[]{(byte) char1};
            } else if (char1 < '✁' || char1 > '➾' || table[char1 - 9984] == (byte) 0) {
                return new byte[0];
            } else {
                return new byte[]{table[char1 - 9984]};
            }
        }

        public byte[] charToByte(String text, String encoding) {
            char[] cc = text.toCharArray();
            byte[] b = new byte[cc.length];
            int len = cc.length;
            int k = 0;
            int ptr = 0;
            while (k < len) {
                int ptr2;
                char c = cc[k];
                if (c == ' ') {
                    ptr2 = ptr + 1;
                    b[ptr] = (byte) c;
                } else {
                    if (c >= '✁' && c <= '➾') {
                        byte v = table[c - 9984];
                        if (v != (byte) 0) {
                            ptr2 = ptr + 1;
                            b[ptr] = v;
                        }
                    }
                    ptr2 = ptr;
                }
                k++;
                ptr = ptr2;
            }
            if (ptr == len) {
                return b;
            }
            byte[] b2 = new byte[ptr];
            System.arraycopy(b, 0, b2, 0, ptr);
            return b2;
        }

        public String byteToChar(byte[] b, String encoding) {
            return null;
        }
    }

    static {
        int k;
        for (k = 128; k < 161; k++) {
            char c = winansiByteToChar[k];
            if (c != '�') {
                winansi.put(c, k);
            }
        }
        for (k = 128; k < 161; k++) {
            c = pdfEncodingByteToChar[k];
            if (c != '�') {
                pdfEncoding.put(c, k);
            }
        }
        addExtraEncoding("Wingdings", new WingdingsConversion());
        addExtraEncoding("Symbol", new SymbolConversion(true));
        addExtraEncoding("ZapfDingbats", new SymbolConversion(false));
        addExtraEncoding("SymbolTT", new SymbolTTConversion());
        addExtraEncoding("Cp437", new Cp437Conversion());
    }

    public static final byte[] convertToBytes(String text, String encoding) {
        if (text == null) {
            return new byte[0];
        }
        byte[] b;
        int k;
        if (encoding == null || encoding.length() == 0) {
            int len;
            len = text.length();
            b = new byte[len];
            for (k = 0; k < len; k++) {
                b[k] = (byte) text.charAt(k);
            }
            return b;
        }
        ExtraEncoding extra = (ExtraEncoding) extraEncodings.get(encoding.toLowerCase());
        if (extra != null) {
            b = extra.charToByte(text, encoding);
            if (b != null) {
                return b;
            }
        }
        IntHashtable hash = null;
        if (encoding.equals("Cp1252")) {
            hash = winansi;
        } else if (encoding.equals(PdfObject.TEXT_PDFDOCENCODING)) {
            hash = pdfEncoding;
        }
        char[] cc;
        int c;
        if (hash != null) {
            cc = text.toCharArray();
            len = cc.length;
            b = new byte[len];
            k = 0;
            int ptr = 0;
            while (k < len) {
                int ptr2;
                char char1 = cc[k];
                if (char1 < '' || (char1 > ' ' && char1 <= 'ÿ')) {
                    c = char1;
                } else {
                    c = hash.get(char1);
                }
                if (c != 0) {
                    ptr2 = ptr + 1;
                    b[ptr] = (byte) c;
                } else {
                    ptr2 = ptr;
                }
                k++;
                ptr = ptr2;
            }
            if (ptr == len) {
                return b;
            }
            byte[] b2 = new byte[ptr];
            System.arraycopy(b, 0, b2, 0, ptr);
            return b2;
        } else if (encoding.equals(PdfObject.TEXT_UNICODE)) {
            cc = text.toCharArray();
            b = new byte[((cc.length * 2) + 2)];
            b[0] = (byte) -2;
            b[1] = (byte) -1;
            int bptr = 2;
            for (int c2 : cc) {
                int i = bptr + 1;
                b[bptr] = (byte) (c2 >> 8);
                bptr = i + 1;
                b[i] = (byte) (c2 & 255);
            }
            return b;
        } else {
            try {
                CharsetEncoder ce = Charset.forName(encoding).newEncoder();
                ce.onUnmappableCharacter(CodingErrorAction.IGNORE);
                ByteBuffer bb = ce.encode(CharBuffer.wrap(text.toCharArray()));
                bb.rewind();
                byte[] br = new byte[bb.limit()];
                bb.get(br);
                return br;
            } catch (IOException e) {
                throw new ExceptionConverter(e);
            }
        }
    }

    public static final byte[] convertToBytes(char char1, String encoding) {
        if (encoding == null || encoding.length() == 0) {
            return new byte[]{(byte) char1};
        }
        ExtraEncoding extra = (ExtraEncoding) extraEncodings.get(encoding.toLowerCase());
        if (extra != null) {
            byte[] b = extra.charToByte(char1, encoding);
            if (b != null) {
                return b;
            }
        }
        IntHashtable hash = null;
        if (encoding.equals("Cp1252")) {
            hash = winansi;
        } else if (encoding.equals(PdfObject.TEXT_PDFDOCENCODING)) {
            hash = pdfEncoding;
        }
        if (hash != null) {
            int c;
            if (char1 < '' || (char1 > ' ' && char1 <= 'ÿ')) {
                c = char1;
            } else {
                c = hash.get(char1);
            }
            if (c == 0) {
                return new byte[0];
            }
            return new byte[]{(byte) c};
        } else if (encoding.equals(PdfObject.TEXT_UNICODE)) {
            return new byte[]{(byte) -2, (byte) -1, (byte) (char1 >> 8), (byte) (char1 & 255)};
        } else {
            try {
                CharsetEncoder ce = Charset.forName(encoding).newEncoder();
                ce.onUnmappableCharacter(CodingErrorAction.IGNORE);
                ByteBuffer bb = ce.encode(CharBuffer.wrap(new char[]{char1}));
                bb.rewind();
                byte[] br = new byte[bb.limit()];
                bb.get(br);
                return br;
            } catch (IOException e) {
                throw new ExceptionConverter(e);
            }
        }
    }

    public static final String convertToString(byte[] bytes, String encoding) {
        if (bytes == null) {
            return "";
        }
        char[] c;
        int k;
        if (encoding == null || encoding.length() == 0) {
            c = new char[bytes.length];
            for (k = 0; k < bytes.length; k++) {
                c[k] = (char) (bytes[k] & 255);
            }
            return new String(c);
        }
        ExtraEncoding extra = (ExtraEncoding) extraEncodings.get(encoding.toLowerCase());
        if (extra != null) {
            String text = extra.byteToChar(bytes, encoding);
            if (text != null) {
                return text;
            }
        }
        char[] ch = null;
        if (encoding.equals("Cp1252")) {
            ch = winansiByteToChar;
        } else if (encoding.equals(PdfObject.TEXT_PDFDOCENCODING)) {
            ch = pdfEncodingByteToChar;
        }
        if (ch != null) {
            int len = bytes.length;
            c = new char[len];
            for (k = 0; k < len; k++) {
                c[k] = ch[bytes[k] & 255];
            }
            return new String(c);
        }
        try {
            return new String(bytes, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new ExceptionConverter(e);
        }
    }

    public static boolean isPdfDocEncoding(String text) {
        if (text == null) {
            return true;
        }
        int len = text.length();
        for (int k = 0; k < len; k++) {
            char char1 = text.charAt(k);
            if (char1 >= '' && ((char1 <= ' ' || char1 > 'ÿ') && !pdfEncoding.containsKey(char1))) {
                return false;
            }
        }
        return true;
    }

    public static void addExtraEncoding(String name, ExtraEncoding enc) {
        synchronized (extraEncodings) {
            HashMap<String, ExtraEncoding> newEncodings = (HashMap) extraEncodings.clone();
            newEncodings.put(name.toLowerCase(), enc);
            extraEncodings = newEncodings;
        }
    }
}

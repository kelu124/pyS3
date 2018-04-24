package com.itextpdf.text.pdf;

import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.exceptions.InvalidPdfException;
import com.itextpdf.text.io.RandomAccessSourceFactory;
import java.io.IOException;

public class PRTokeniser {
    static final String EMPTY = "";
    public static final boolean[] delims = new boolean[]{true, true, false, false, false, false, false, false, false, false, true, true, false, true, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, false, false, false, false, true, false, false, true, true, false, false, false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, false, true, false, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, false, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};
    private final RandomAccessFileOrArray file;
    protected int generation;
    protected boolean hexString;
    protected int reference;
    protected String stringValue;
    protected TokenType type;

    public enum TokenType {
        NUMBER,
        STRING,
        NAME,
        COMMENT,
        START_ARRAY,
        END_ARRAY,
        START_DIC,
        END_DIC,
        REF,
        OTHER,
        ENDOFFILE
    }

    public PRTokeniser(RandomAccessFileOrArray file) {
        this.file = file;
    }

    public void seek(long pos) throws IOException {
        this.file.seek(pos);
    }

    public long getFilePointer() throws IOException {
        return this.file.getFilePointer();
    }

    public void close() throws IOException {
        this.file.close();
    }

    public long length() throws IOException {
        return this.file.length();
    }

    public int read() throws IOException {
        return this.file.read();
    }

    public RandomAccessFileOrArray getSafeFile() {
        return new RandomAccessFileOrArray(this.file);
    }

    public RandomAccessFileOrArray getFile() {
        return this.file;
    }

    public String readString(int size) throws IOException {
        StringBuilder buf = new StringBuilder();
        int size2 = size;
        while (true) {
            size = size2 - 1;
            if (size2 <= 0) {
                break;
            }
            int ch = read();
            if (ch == -1) {
                break;
            }
            buf.append((char) ch);
            size2 = size;
        }
        return buf.toString();
    }

    public static final boolean isWhitespace(int ch) {
        return isWhitespace(ch, true);
    }

    public static final boolean isWhitespace(int ch, boolean isWhitespace) {
        return (isWhitespace && ch == 0) || ch == 9 || ch == 10 || ch == 12 || ch == 13 || ch == 32;
    }

    public static final boolean isDelimiter(int ch) {
        return ch == 40 || ch == 41 || ch == 60 || ch == 62 || ch == 91 || ch == 93 || ch == 47 || ch == 37;
    }

    public static final boolean isDelimiterWhitespace(int ch) {
        return delims[ch + 1];
    }

    public TokenType getTokenType() {
        return this.type;
    }

    public String getStringValue() {
        return this.stringValue;
    }

    public int getReference() {
        return this.reference;
    }

    public int getGeneration() {
        return this.generation;
    }

    public void backOnePosition(int ch) {
        if (ch != -1) {
            this.file.pushBack((byte) ch);
        }
    }

    public void throwError(String error) throws IOException {
        throw new InvalidPdfException(MessageLocalization.getComposedMessage("1.at.file.pointer.2", error, String.valueOf(this.file.getFilePointer())));
    }

    public int getHeaderOffset() throws IOException {
        String str = readString(1024);
        int idx = str.indexOf("%PDF-");
        if (idx < 0) {
            idx = str.indexOf("%FDF-");
            if (idx < 0) {
                throw new InvalidPdfException(MessageLocalization.getComposedMessage("pdf.header.not.found", new Object[0]));
            }
        }
        return idx;
    }

    public char checkPdfHeader() throws IOException {
        this.file.seek(0);
        String str = readString(1024);
        if (str.indexOf("%PDF-") == 0) {
            return str.charAt(7);
        }
        throw new InvalidPdfException(MessageLocalization.getComposedMessage("pdf.header.not.found", new Object[0]));
    }

    public void checkFdfHeader() throws IOException {
        this.file.seek(0);
        if (readString(1024).indexOf("%FDF-") != 0) {
            throw new InvalidPdfException(MessageLocalization.getComposedMessage("fdf.header.not.found", new Object[0]));
        }
    }

    public long getStartxref() throws IOException {
        long pos = this.file.length() - ((long) 1024);
        if (pos < 1) {
            pos = 1;
        }
        while (pos > 0) {
            this.file.seek(pos);
            int idx = readString(1024).lastIndexOf("startxref");
            if (idx >= 0) {
                return ((long) idx) + pos;
            }
            pos = (pos - ((long) 1024)) + 9;
        }
        throw new InvalidPdfException(MessageLocalization.getComposedMessage("pdf.startxref.not.found", new Object[0]));
    }

    public static int getHex(int v) {
        if (v >= 48 && v <= 57) {
            return v - 48;
        }
        if (v >= 65 && v <= 70) {
            return (v - 65) + 10;
        }
        if (v < 97 || v > 102) {
            return -1;
        }
        return (v - 97) + 10;
    }

    public void nextValidToken() throws IOException {
        int level = 0;
        String n1 = null;
        String n2 = null;
        long ptr = 0;
        while (nextToken()) {
            if (this.type != TokenType.COMMENT) {
                switch (level) {
                    case 0:
                        if (this.type == TokenType.NUMBER) {
                            ptr = this.file.getFilePointer();
                            n1 = this.stringValue;
                            level++;
                            break;
                        }
                        return;
                    case 1:
                        if (this.type == TokenType.NUMBER) {
                            n2 = this.stringValue;
                            level++;
                            break;
                        }
                        this.file.seek(ptr);
                        this.type = TokenType.NUMBER;
                        this.stringValue = n1;
                        return;
                    default:
                        if (this.type == TokenType.OTHER && this.stringValue.equals("R")) {
                            this.type = TokenType.REF;
                            this.reference = Integer.parseInt(n1);
                            this.generation = Integer.parseInt(n2);
                            return;
                        }
                        this.file.seek(ptr);
                        this.type = TokenType.NUMBER;
                        this.stringValue = n1;
                        return;
                }
            }
        }
        if (level == 1) {
            this.type = TokenType.NUMBER;
        }
    }

    public boolean nextToken() throws IOException {
        int ch;
        do {
            ch = this.file.read();
            if (ch == -1) {
                break;
            }
        } while (isWhitespace(ch));
        if (ch == -1) {
            this.type = TokenType.ENDOFFILE;
            return false;
        }
        StringBuilder outBuf = new StringBuilder();
        this.stringValue = "";
        switch (ch) {
            case 37:
                this.type = TokenType.COMMENT;
                do {
                    ch = this.file.read();
                    if (!(ch == -1 || ch == 13)) {
                    }
                } while (ch != 10);
                break;
            case 40:
                outBuf.setLength(0);
                this.type = TokenType.STRING;
                this.hexString = false;
                int nesting = 0;
                while (true) {
                    ch = this.file.read();
                    if (ch != -1) {
                        if (ch == 40) {
                            nesting++;
                        } else if (ch == 41) {
                            nesting--;
                        } else if (ch == 92) {
                            boolean lineBreak = false;
                            ch = this.file.read();
                            switch (ch) {
                                case 10:
                                    lineBreak = true;
                                    break;
                                case 13:
                                    lineBreak = true;
                                    ch = this.file.read();
                                    if (ch != 10) {
                                        backOnePosition(ch);
                                        break;
                                    }
                                    break;
                                case 40:
                                case 41:
                                case 92:
                                    break;
                                case 98:
                                    ch = 8;
                                    break;
                                case 102:
                                    ch = 12;
                                    break;
                                case 110:
                                    ch = 10;
                                    break;
                                case 114:
                                    ch = 13;
                                    break;
                                case 116:
                                    ch = 9;
                                    break;
                                default:
                                    if (ch >= 48 && ch <= 55) {
                                        int octal = ch - 48;
                                        ch = this.file.read();
                                        if (ch >= 48 && ch <= 55) {
                                            octal = ((octal << 3) + ch) - 48;
                                            ch = this.file.read();
                                            if (ch >= 48 && ch <= 55) {
                                                ch = (((octal << 3) + ch) - 48) & 255;
                                                break;
                                            }
                                            backOnePosition(ch);
                                            ch = octal;
                                            break;
                                        }
                                        backOnePosition(ch);
                                        ch = octal;
                                        break;
                                    }
                                    break;
                            }
                            if (lineBreak) {
                                continue;
                            } else if (ch < 0) {
                            }
                        } else if (ch == 13) {
                            ch = this.file.read();
                            if (ch >= 0) {
                                if (ch != 10) {
                                    backOnePosition(ch);
                                    ch = 10;
                                }
                            }
                        }
                        if (nesting != -1) {
                            outBuf.append((char) ch);
                        }
                    }
                    if (ch == -1) {
                        throwError(MessageLocalization.getComposedMessage("error.reading.string", new Object[0]));
                        break;
                    }
                }
                break;
            case 47:
                outBuf.setLength(0);
                this.type = TokenType.NAME;
                while (true) {
                    ch = this.file.read();
                    if (delims[ch + 1]) {
                        backOnePosition(ch);
                        break;
                    }
                    if (ch == 35) {
                        ch = (getHex(this.file.read()) << 4) + getHex(this.file.read());
                    }
                    outBuf.append((char) ch);
                }
            case 60:
                int v1 = this.file.read();
                if (v1 != 60) {
                    outBuf.setLength(0);
                    this.type = TokenType.STRING;
                    this.hexString = true;
                    int v2 = 0;
                    while (true) {
                        if (!isWhitespace(v1)) {
                            if (v1 != 62) {
                                v1 = getHex(v1);
                                if (v1 >= 0) {
                                    v2 = this.file.read();
                                    while (isWhitespace(v2)) {
                                        v2 = this.file.read();
                                    }
                                    if (v2 == 62) {
                                        outBuf.append((char) (v1 << 4));
                                    } else {
                                        v2 = getHex(v2);
                                        if (v2 >= 0) {
                                            outBuf.append((char) ((v1 << 4) + v2));
                                            v1 = this.file.read();
                                        }
                                    }
                                }
                            }
                            if (v1 < 0 || v2 < 0) {
                                throwError(MessageLocalization.getComposedMessage("error.reading.string", new Object[0]));
                                break;
                            }
                        }
                        v1 = this.file.read();
                    }
                } else {
                    this.type = TokenType.START_DIC;
                    break;
                }
            case 62:
                if (this.file.read() != 62) {
                    throwError(MessageLocalization.getComposedMessage("greaterthan.not.expected", new Object[0]));
                }
                this.type = TokenType.END_DIC;
                break;
            case 91:
                this.type = TokenType.START_ARRAY;
                break;
            case 93:
                this.type = TokenType.END_ARRAY;
                break;
            default:
                outBuf.setLength(0);
                if (ch == 45 || ch == 43 || ch == 46 || (ch >= 48 && ch <= 57)) {
                    this.type = TokenType.NUMBER;
                    if (ch == 45) {
                        boolean minus = false;
                        do {
                            if (minus) {
                                minus = false;
                            } else {
                                minus = true;
                            }
                            ch = this.file.read();
                        } while (ch == 45);
                        if (minus) {
                            outBuf.append('-');
                        }
                    } else {
                        outBuf.append((char) ch);
                        ch = this.file.read();
                    }
                    while (ch != -1 && ((ch >= 48 && ch <= 57) || ch == 46)) {
                        outBuf.append((char) ch);
                        ch = this.file.read();
                    }
                } else {
                    this.type = TokenType.OTHER;
                    do {
                        outBuf.append((char) ch);
                        ch = this.file.read();
                    } while (!delims[ch + 1]);
                }
                if (ch != -1) {
                    backOnePosition(ch);
                    break;
                }
                break;
        }
        if (outBuf != null) {
            this.stringValue = outBuf.toString();
        }
        return true;
    }

    public long longValue() {
        return Long.parseLong(this.stringValue);
    }

    public int intValue() {
        return Integer.parseInt(this.stringValue);
    }

    public boolean readLineSegment(byte[] input) throws IOException {
        return readLineSegment(input, true);
    }

    public boolean readLineSegment(byte[] input, boolean isNullWhitespace) throws IOException {
        int c = -1;
        boolean eol = false;
        int len = input.length;
        if (0 < len) {
            do {
                c = read();
            } while (isWhitespace(c, isNullWhitespace));
        }
        int ptr = 0;
        while (!eol && ptr < len) {
            int ptr2;
            long cur;
            switch (c) {
                case -1:
                case 10:
                    eol = true;
                    ptr2 = ptr;
                    break;
                case 13:
                    eol = true;
                    cur = getFilePointer();
                    if (read() == 10) {
                        ptr2 = ptr;
                        break;
                    }
                    seek(cur);
                    ptr2 = ptr;
                    break;
                default:
                    ptr2 = ptr + 1;
                    input[ptr] = (byte) c;
                    break;
            }
            if (eol) {
                ptr = ptr2;
            } else if (len <= ptr2) {
                ptr = ptr2;
            } else {
                c = read();
                ptr = ptr2;
            }
        }
        if (ptr >= len) {
            eol = false;
            while (!eol) {
                c = read();
                switch (c) {
                    case -1:
                    case 10:
                        eol = true;
                        break;
                    case 13:
                        eol = true;
                        cur = getFilePointer();
                        if (read() == 10) {
                            break;
                        }
                        seek(cur);
                        break;
                    default:
                        break;
                }
            }
        }
        if (c == -1 && ptr == 0) {
            ptr2 = ptr;
            return false;
        }
        if (ptr + 2 <= len) {
            ptr2 = ptr + 1;
            input[ptr] = (byte) 32;
            input[ptr2] = (byte) 88;
        }
        return true;
    }

    public static long[] checkObjectStart(byte[] line) {
        try {
            PRTokeniser tk = new PRTokeniser(new RandomAccessFileOrArray(new RandomAccessSourceFactory().createSource(line)));
            if (!tk.nextToken() || tk.getTokenType() != TokenType.NUMBER) {
                return null;
            }
            int num = tk.intValue();
            if (!tk.nextToken() || tk.getTokenType() != TokenType.NUMBER) {
                return null;
            }
            int gen = tk.intValue();
            if (!tk.nextToken() || !tk.getStringValue().equals("obj")) {
                return null;
            }
            return new long[]{(long) num, (long) gen};
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isHexString() {
        return this.hexString;
    }
}

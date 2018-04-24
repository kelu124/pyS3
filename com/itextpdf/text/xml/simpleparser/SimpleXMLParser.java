package com.itextpdf.text.xml.simpleparser;

import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.html.HtmlTags;
import com.itextpdf.text.xml.XMLUtil;
import com.itextpdf.text.xml.simpleparser.handler.HTMLNewLineHandler;
import com.itextpdf.text.xml.simpleparser.handler.NeverNewLineHandler;
import com.itextpdf.text.xml.xmp.XmpWriter;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Stack;

public final class SimpleXMLParser {
    private static final int ATTRIBUTE_EQUAL = 13;
    private static final int ATTRIBUTE_KEY = 12;
    private static final int ATTRIBUTE_VALUE = 14;
    private static final int CDATA = 7;
    private static final int COMMENT = 8;
    private static final int ENTITY = 10;
    private static final int EXAMIN_TAG = 3;
    private static final int IN_CLOSETAG = 5;
    private static final int PI = 9;
    private static final int QUOTE = 11;
    private static final int SINGLE_TAG = 6;
    private static final int TAG_ENCOUNTERED = 2;
    private static final int TAG_EXAMINED = 4;
    private static final int TEXT = 1;
    private static final int UNKNOWN = 0;
    private String attributekey = null;
    private HashMap<String, String> attributes = null;
    private String attributevalue = null;
    private int character = 0;
    private int columns = 0;
    private final SimpleXMLDocHandlerComment comment;
    private final SimpleXMLDocHandler doc;
    private final StringBuffer entity = new StringBuffer();
    private boolean eol = false;
    private final boolean html;
    private int lines = 1;
    private int nested = 0;
    private NewLineHandler newLineHandler;
    private boolean nowhite = false;
    private int previousCharacter = -1;
    private int quoteCharacter = 34;
    private final Stack<Integer> stack;
    private int state;
    private String tag = null;
    private final StringBuffer text = new StringBuffer();

    private SimpleXMLParser(SimpleXMLDocHandler doc, SimpleXMLDocHandlerComment comment, boolean html) {
        int i = 1;
        this.doc = doc;
        this.comment = comment;
        this.html = html;
        if (html) {
            this.newLineHandler = new HTMLNewLineHandler();
        } else {
            this.newLineHandler = new NeverNewLineHandler();
        }
        this.stack = new Stack();
        if (!html) {
            i = 0;
        }
        this.state = i;
    }

    private void go(Reader r) throws IOException {
        BufferedReader reader;
        if (r instanceof BufferedReader) {
            reader = (BufferedReader) r;
        } else {
            reader = new BufferedReader(r);
        }
        this.doc.startDocument();
        while (true) {
            if (this.previousCharacter == -1) {
                this.character = reader.read();
            } else {
                this.character = this.previousCharacter;
                this.previousCharacter = -1;
            }
            if (this.character == -1) {
                if (this.html) {
                    if (this.html && this.state == 1) {
                        flush();
                    }
                    this.doc.endDocument();
                    return;
                }
                throwException(MessageLocalization.getComposedMessage("missing.end.tag", new Object[0]));
                return;
            } else if (this.character != 10 || !this.eol) {
                if (this.eol) {
                    this.eol = false;
                } else if (this.character == 10) {
                    this.lines++;
                    this.columns = 0;
                } else if (this.character == 13) {
                    this.eol = true;
                    this.character = 10;
                    this.lines++;
                    this.columns = 0;
                } else {
                    this.columns++;
                }
                switch (this.state) {
                    case 0:
                        if (this.character != 60) {
                            break;
                        }
                        saveState(1);
                        this.state = 2;
                        break;
                    case 1:
                        if (this.character != 60) {
                            if (this.character != 38) {
                                if (this.character != 32) {
                                    if (Character.isWhitespace((char) this.character)) {
                                        if (!this.html) {
                                            if (this.nowhite) {
                                                this.text.append((char) this.character);
                                            }
                                            this.nowhite = false;
                                            break;
                                        }
                                        break;
                                    }
                                    this.text.append((char) this.character);
                                    this.nowhite = true;
                                    break;
                                } else if (!this.html || !this.nowhite) {
                                    if (this.nowhite) {
                                        this.text.append((char) this.character);
                                    }
                                    this.nowhite = false;
                                    break;
                                } else {
                                    this.text.append(' ');
                                    this.nowhite = false;
                                    break;
                                }
                            }
                            saveState(this.state);
                            this.entity.setLength(0);
                            this.state = 10;
                            this.nowhite = true;
                            break;
                        }
                        flush();
                        saveState(this.state);
                        this.state = 2;
                        break;
                        break;
                    case 2:
                        initTag();
                        if (this.character != 47) {
                            if (this.character != 63) {
                                this.text.append((char) this.character);
                                this.state = 3;
                                break;
                            }
                            restoreState();
                            this.state = 9;
                            break;
                        }
                        this.state = 5;
                        break;
                    case 3:
                        if (this.character != 62) {
                            if (this.character != 47) {
                                if (this.character != 45 || !this.text.toString().equals("!-")) {
                                    if (this.character != 91 || !this.text.toString().equals("![CDATA")) {
                                        if (this.character != 69 || !this.text.toString().equals("!DOCTYP")) {
                                            if (!Character.isWhitespace((char) this.character)) {
                                                this.text.append((char) this.character);
                                                break;
                                            }
                                            doTag();
                                            this.state = 4;
                                            break;
                                        }
                                        flush();
                                        this.state = 9;
                                        break;
                                    }
                                    flush();
                                    this.state = 7;
                                    break;
                                }
                                flush();
                                this.state = 8;
                                break;
                            }
                            this.state = 6;
                            break;
                        }
                        doTag();
                        processTag(true);
                        initTag();
                        this.state = restoreState();
                        break;
                        break;
                    case 4:
                        if (this.character != 62) {
                            if (this.character != 47) {
                                if (!Character.isWhitespace((char) this.character)) {
                                    this.text.append((char) this.character);
                                    this.state = 12;
                                    break;
                                }
                                break;
                            }
                            this.state = 6;
                            break;
                        }
                        processTag(true);
                        initTag();
                        this.state = restoreState();
                        break;
                    case 5:
                        if (this.character != 62) {
                            if (!Character.isWhitespace((char) this.character)) {
                                this.text.append((char) this.character);
                                break;
                            }
                            break;
                        }
                        doTag();
                        processTag(false);
                        if (this.html || this.nested != 0) {
                            this.state = restoreState();
                            break;
                        }
                        return;
                        break;
                    case 6:
                        if (this.character != 62) {
                            throwException(MessageLocalization.getComposedMessage("expected.gt.for.tag.lt.1.gt", new Object[]{this.tag}));
                        }
                        doTag();
                        processTag(true);
                        processTag(false);
                        initTag();
                        if (this.html || this.nested != 0) {
                            this.state = restoreState();
                            break;
                        } else {
                            this.doc.endDocument();
                            return;
                        }
                        break;
                    case 7:
                        if (this.character != 62 || !this.text.toString().endsWith("]]")) {
                            this.text.append((char) this.character);
                            break;
                        }
                        this.text.setLength(this.text.length() - 2);
                        flush();
                        this.state = restoreState();
                        break;
                    case 8:
                        if (this.character != 62 || !this.text.toString().endsWith("--")) {
                            this.text.append((char) this.character);
                            break;
                        }
                        this.text.setLength(this.text.length() - 2);
                        flush();
                        this.state = restoreState();
                        break;
                    case 9:
                        if (this.character != 62) {
                            break;
                        }
                        this.state = restoreState();
                        if (this.state != 1) {
                            break;
                        }
                        this.state = 0;
                        break;
                    case 10:
                        if (this.character != 59) {
                            if ((this.character != 35 && ((this.character < 48 || this.character > 57) && ((this.character < 97 || this.character > 122) && (this.character < 65 || this.character > 90)))) || this.entity.length() >= 7) {
                                this.state = restoreState();
                                this.previousCharacter = this.character;
                                this.text.append('&').append(this.entity.toString());
                                this.entity.setLength(0);
                                break;
                            }
                            this.entity.append((char) this.character);
                            break;
                        }
                        this.state = restoreState();
                        String cent = this.entity.toString();
                        this.entity.setLength(0);
                        char ce = EntitiesToUnicode.decodeEntity(cent);
                        if (ce != '\u0000') {
                            this.text.append(ce);
                            break;
                        } else {
                            this.text.append('&').append(cent).append(';');
                            break;
                        }
                    case 11:
                        if (!this.html || this.quoteCharacter != 32 || this.character != 62) {
                            if (!this.html || this.quoteCharacter != 32 || !Character.isWhitespace((char) this.character)) {
                                if (!this.html || this.quoteCharacter != 32) {
                                    if (this.character != this.quoteCharacter) {
                                        if (" \r\n\t".indexOf(this.character) < 0) {
                                            if (this.character != 38) {
                                                this.text.append((char) this.character);
                                                break;
                                            }
                                            saveState(this.state);
                                            this.state = 10;
                                            this.entity.setLength(0);
                                            break;
                                        }
                                        this.text.append(' ');
                                        break;
                                    }
                                    flush();
                                    this.state = 4;
                                    break;
                                }
                                this.text.append((char) this.character);
                                break;
                            }
                            flush();
                            this.state = 4;
                            break;
                        }
                        flush();
                        processTag(true);
                        initTag();
                        this.state = restoreState();
                        break;
                    case 12:
                        if (!Character.isWhitespace((char) this.character)) {
                            if (this.character != 61) {
                                if (!this.html || this.character != 62) {
                                    this.text.append((char) this.character);
                                    break;
                                }
                                this.text.setLength(0);
                                processTag(true);
                                initTag();
                                this.state = restoreState();
                                break;
                            }
                            flush();
                            this.state = 14;
                            break;
                        }
                        flush();
                        this.state = 13;
                        break;
                    case 13:
                        if (this.character != 61) {
                            if (!Character.isWhitespace((char) this.character)) {
                                if (!this.html || this.character != 62) {
                                    if (!this.html || this.character != 47) {
                                        if (!this.html) {
                                            throwException(MessageLocalization.getComposedMessage("error.in.attribute.processing", new Object[0]));
                                            break;
                                        }
                                        flush();
                                        this.text.append((char) this.character);
                                        this.state = 12;
                                        break;
                                    }
                                    flush();
                                    this.state = 6;
                                    break;
                                }
                                this.text.setLength(0);
                                processTag(true);
                                initTag();
                                this.state = restoreState();
                                break;
                            }
                            break;
                        }
                        this.state = 14;
                        break;
                        break;
                    case 14:
                        if (this.character != 34 && this.character != 39) {
                            if (!Character.isWhitespace((char) this.character)) {
                                if (!this.html || this.character != 62) {
                                    if (!this.html) {
                                        throwException(MessageLocalization.getComposedMessage("error.in.attribute.processing", new Object[0]));
                                        break;
                                    }
                                    this.text.append((char) this.character);
                                    this.quoteCharacter = 32;
                                    this.state = 11;
                                    break;
                                }
                                flush();
                                processTag(true);
                                initTag();
                                this.state = restoreState();
                                break;
                            }
                            break;
                        }
                        this.quoteCharacter = this.character;
                        this.state = 11;
                        break;
                        break;
                    default:
                        break;
                }
            } else {
                this.eol = false;
            }
        }
    }

    private int restoreState() {
        if (this.stack.empty()) {
            return 0;
        }
        return ((Integer) this.stack.pop()).intValue();
    }

    private void saveState(int s) {
        this.stack.push(Integer.valueOf(s));
    }

    private void flush() {
        switch (this.state) {
            case 1:
            case 7:
                if (this.text.length() > 0) {
                    this.doc.text(this.text.toString());
                    break;
                }
                break;
            case 8:
                if (this.comment != null) {
                    this.comment.comment(this.text.toString());
                    break;
                }
                break;
            case 11:
            case 14:
                this.attributevalue = this.text.toString();
                this.attributes.put(this.attributekey, this.attributevalue);
                break;
            case 12:
                this.attributekey = this.text.toString();
                if (this.html) {
                    this.attributekey = this.attributekey.toLowerCase();
                    break;
                }
                break;
        }
        this.text.setLength(0);
    }

    private void initTag() {
        this.tag = null;
        this.attributes = new HashMap();
    }

    private void doTag() {
        if (this.tag == null) {
            this.tag = this.text.toString();
        }
        if (this.html) {
            this.tag = this.tag.toLowerCase();
        }
        this.text.setLength(0);
    }

    private void processTag(boolean start) {
        if (start) {
            this.nested++;
            this.doc.startElement(this.tag, this.attributes);
            return;
        }
        if (this.newLineHandler.isNewLineTag(this.tag)) {
            this.nowhite = false;
        }
        this.nested--;
        this.doc.endElement(this.tag);
    }

    private void throwException(String s) throws IOException {
        throw new IOException(MessageLocalization.getComposedMessage("1.near.line.2.column.3", new Object[]{s, String.valueOf(this.lines), String.valueOf(this.columns)}));
    }

    public static void parse(SimpleXMLDocHandler doc, SimpleXMLDocHandlerComment comment, Reader r, boolean html) throws IOException {
        new SimpleXMLParser(doc, comment, html).go(r);
    }

    public static void parse(SimpleXMLDocHandler doc, InputStream in) throws IOException {
        byte[] b4 = new byte[4];
        if (in.read(b4) != 4) {
            throw new IOException(MessageLocalization.getComposedMessage("insufficient.length", new Object[0]));
        }
        String encoding = XMLUtil.getEncodingName(b4);
        String decl = null;
        int c;
        if (encoding.equals(XmpWriter.UTF8)) {
            StringBuffer sb = new StringBuffer();
            while (true) {
                c = in.read();
                if (c == -1 || c == 62) {
                    decl = sb.toString();
                } else {
                    sb.append((char) c);
                }
            }
            decl = sb.toString();
        } else if (encoding.equals("CP037")) {
            ByteArrayOutputStream bi = new ByteArrayOutputStream();
            while (true) {
                c = in.read();
                if (c == -1 || c == 110) {
                    decl = new String(bi.toByteArray(), "CP037");
                } else {
                    bi.write(c);
                }
            }
            decl = new String(bi.toByteArray(), "CP037");
        }
        if (decl != null) {
            decl = getDeclaredEncoding(decl);
            if (decl != null) {
                encoding = decl;
            }
        }
        parse(doc, new InputStreamReader(in, IanaEncodings.getJavaEncoding(encoding)));
    }

    private static String getDeclaredEncoding(String decl) {
        if (decl == null) {
            return null;
        }
        int idx = decl.indexOf(HtmlTags.ENCODING);
        if (idx < 0) {
            return null;
        }
        int idx1 = decl.indexOf(34, idx);
        int idx2 = decl.indexOf(39, idx);
        if (idx1 == idx2) {
            return null;
        }
        int idx3;
        if ((idx1 < 0 && idx2 > 0) || (idx2 > 0 && idx2 < idx1)) {
            idx3 = decl.indexOf(39, idx2 + 1);
            if (idx3 >= 0) {
                return decl.substring(idx2 + 1, idx3);
            }
            return null;
        } else if ((idx2 >= 0 || idx1 <= 0) && (idx1 <= 0 || idx1 >= idx2)) {
            return null;
        } else {
            idx3 = decl.indexOf(34, idx1 + 1);
            if (idx3 >= 0) {
                return decl.substring(idx1 + 1, idx3);
            }
            return null;
        }
    }

    public static void parse(SimpleXMLDocHandler doc, Reader r) throws IOException {
        parse(doc, null, r, false);
    }

    @Deprecated
    public static String escapeXML(String s, boolean onlyASCII) {
        return XMLUtil.escapeXML(s, onlyASCII);
    }
}

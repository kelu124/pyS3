package com.itextpdf.text.pdf.hyphenation;

import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.html.HtmlTags;
import com.itextpdf.text.xml.simpleparser.SimpleXMLDocHandler;
import com.itextpdf.text.xml.simpleparser.SimpleXMLParser;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.StringTokenizer;

public class SimplePatternParser implements SimpleXMLDocHandler, PatternConsumer {
    static final int ELEM_CLASSES = 1;
    static final int ELEM_EXCEPTIONS = 2;
    static final int ELEM_HYPHEN = 4;
    static final int ELEM_PATTERNS = 3;
    PatternConsumer consumer;
    int currElement;
    ArrayList<Object> exception;
    char hyphenChar = '-';
    SimpleXMLParser parser;
    StringBuffer token = new StringBuffer();

    public void parse(InputStream stream, PatternConsumer consumer) {
        this.consumer = consumer;
        try {
            SimpleXMLParser.parse(this, stream);
            try {
                stream.close();
            } catch (Exception e) {
            }
        } catch (IOException e2) {
            throw new ExceptionConverter(e2);
        } catch (Throwable th) {
            try {
                stream.close();
            } catch (Exception e3) {
            }
        }
    }

    protected static String getPattern(String word) {
        StringBuffer pat = new StringBuffer();
        int len = word.length();
        for (int i = 0; i < len; i++) {
            if (!Character.isDigit(word.charAt(i))) {
                pat.append(word.charAt(i));
            }
        }
        return pat.toString();
    }

    protected ArrayList<Object> normalizeException(ArrayList<Object> ex) {
        ArrayList<Object> res = new ArrayList();
        for (int i = 0; i < ex.size(); i++) {
            String item = ex.get(i);
            if (item instanceof String) {
                String str = item;
                StringBuffer buf = new StringBuffer();
                for (int j = 0; j < str.length(); j++) {
                    char c = str.charAt(j);
                    if (c != this.hyphenChar) {
                        buf.append(c);
                    } else {
                        res.add(buf.toString());
                        buf.setLength(0);
                        res.add(new Hyphen(new String(new char[]{this.hyphenChar}), null, null));
                    }
                }
                if (buf.length() > 0) {
                    res.add(buf.toString());
                }
            } else {
                res.add(item);
            }
        }
        return res;
    }

    protected String getExceptionWord(ArrayList<Object> ex) {
        StringBuffer res = new StringBuffer();
        for (int i = 0; i < ex.size(); i++) {
            Object item = ex.get(i);
            if (item instanceof String) {
                res.append((String) item);
            } else if (((Hyphen) item).noBreak != null) {
                res.append(((Hyphen) item).noBreak);
            }
        }
        return res.toString();
    }

    protected static String getInterletterValues(String pat) {
        StringBuffer il = new StringBuffer();
        String word = pat + HtmlTags.f32A;
        int len = word.length();
        int i = 0;
        while (i < len) {
            char c = word.charAt(i);
            if (Character.isDigit(c)) {
                il.append(c);
                i++;
            } else {
                il.append('0');
            }
            i++;
        }
        return il.toString();
    }

    public void endDocument() {
    }

    public void endElement(String tag) {
        if (this.token.length() > 0) {
            String word = this.token.toString();
            switch (this.currElement) {
                case 1:
                    this.consumer.addClass(word);
                    break;
                case 2:
                    this.exception.add(word);
                    this.exception = normalizeException(this.exception);
                    this.consumer.addException(getExceptionWord(this.exception), (ArrayList) this.exception.clone());
                    break;
                case 3:
                    this.consumer.addPattern(getPattern(word), getInterletterValues(word));
                    break;
            }
            if (this.currElement != 4) {
                this.token.setLength(0);
            }
        }
        if (this.currElement == 4) {
            this.currElement = 2;
        } else {
            this.currElement = 0;
        }
    }

    public void startDocument() {
    }

    public void startElement(String tag, Map<String, String> h) {
        if (tag.equals("hyphen-char")) {
            String hh = (String) h.get("value");
            if (hh != null && hh.length() == 1) {
                this.hyphenChar = hh.charAt(0);
            }
        } else if (tag.equals("classes")) {
            this.currElement = 1;
        } else if (tag.equals("patterns")) {
            this.currElement = 3;
        } else if (tag.equals("exceptions")) {
            this.currElement = 2;
            this.exception = new ArrayList();
        } else if (tag.equals("hyphen")) {
            if (this.token.length() > 0) {
                this.exception.add(this.token.toString());
            }
            this.exception.add(new Hyphen((String) h.get(HtmlTags.PRE), (String) h.get("no"), (String) h.get("post")));
            this.currElement = 4;
        }
        this.token.setLength(0);
    }

    public void text(String str) {
        StringTokenizer tk = new StringTokenizer(str);
        while (tk.hasMoreTokens()) {
            String word = tk.nextToken();
            switch (this.currElement) {
                case 1:
                    this.consumer.addClass(word);
                    break;
                case 2:
                    this.exception.add(word);
                    this.exception = normalizeException(this.exception);
                    this.consumer.addException(getExceptionWord(this.exception), (ArrayList) this.exception.clone());
                    this.exception.clear();
                    break;
                case 3:
                    this.consumer.addPattern(getPattern(word), getInterletterValues(word));
                    break;
                default:
                    break;
            }
        }
    }

    public void addClass(String c) {
        System.out.println("class: " + c);
    }

    public void addException(String w, ArrayList<Object> e) {
        System.out.println("exception: " + w + " : " + e.toString());
    }

    public void addPattern(String p, String v) {
        System.out.println("pattern: " + p + " : " + v);
    }
}

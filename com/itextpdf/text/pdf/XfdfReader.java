package com.itextpdf.text.pdf;

import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.html.HtmlTags;
import com.itextpdf.text.xml.simpleparser.SimpleXMLDocHandler;
import com.itextpdf.text.xml.simpleparser.SimpleXMLParser;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class XfdfReader implements SimpleXMLDocHandler {
    private final Stack<String> fieldNames;
    private final Stack<String> fieldValues;
    HashMap<String, String> fields;
    String fileSpec;
    private boolean foundRoot;
    protected HashMap<String, List<String>> listFields;

    public XfdfReader(String filename) throws IOException {
        Throwable th;
        this.foundRoot = false;
        this.fieldNames = new Stack();
        this.fieldValues = new Stack();
        FileInputStream fin = null;
        try {
            FileInputStream fin2 = new FileInputStream(filename);
            try {
                SimpleXMLParser.parse(this, fin2);
                if (fin2 != null) {
                    try {
                        fin2.close();
                    } catch (Exception e) {
                    }
                }
            } catch (Throwable th2) {
                th = th2;
                fin = fin2;
                if (fin != null) {
                    try {
                        fin.close();
                    } catch (Exception e2) {
                    }
                }
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            if (fin != null) {
                fin.close();
            }
            throw th;
        }
    }

    public XfdfReader(byte[] xfdfIn) throws IOException {
        this(new ByteArrayInputStream(xfdfIn));
    }

    public XfdfReader(InputStream is) throws IOException {
        this.foundRoot = false;
        this.fieldNames = new Stack();
        this.fieldValues = new Stack();
        SimpleXMLParser.parse(this, is);
    }

    public HashMap<String, String> getFields() {
        return this.fields;
    }

    public String getField(String name) {
        return (String) this.fields.get(name);
    }

    public String getFieldValue(String name) {
        String field = (String) this.fields.get(name);
        if (field == null) {
            return null;
        }
        return field;
    }

    public List<String> getListValues(String name) {
        return (List) this.listFields.get(name);
    }

    public String getFileSpec() {
        return this.fileSpec;
    }

    public void startElement(String tag, Map<String, String> h) {
        if (!this.foundRoot) {
            if (tag.equals("xfdf")) {
                this.foundRoot = true;
            } else {
                throw new RuntimeException(MessageLocalization.getComposedMessage("root.element.is.not.xfdf.1", tag));
            }
        }
        if (!tag.equals("xfdf")) {
            if (tag.equals("f")) {
                this.fileSpec = (String) h.get(HtmlTags.HREF);
            } else if (tag.equals("fields")) {
                this.fields = new HashMap();
                this.listFields = new HashMap();
            } else if (tag.equals("field")) {
                this.fieldNames.push((String) h.get("name"));
            } else if (tag.equals("value")) {
                this.fieldValues.push("");
            }
        }
    }

    public void endElement(String tag) {
        if (tag.equals("value")) {
            String fName = "";
            for (int k = 0; k < this.fieldNames.size(); k++) {
                fName = fName + "." + ((String) this.fieldNames.elementAt(k));
            }
            if (fName.startsWith(".")) {
                fName = fName.substring(1);
            }
            String fVal = (String) this.fieldValues.pop();
            String old = (String) this.fields.put(fName, fVal);
            if (old != null) {
                List<String> l = (List) this.listFields.get(fName);
                if (l == null) {
                    l = new ArrayList();
                    l.add(old);
                }
                l.add(fVal);
                this.listFields.put(fName, l);
            }
        } else if (tag.equals("field") && !this.fieldNames.isEmpty()) {
            this.fieldNames.pop();
        }
    }

    public void startDocument() {
        this.fileSpec = "";
    }

    public void endDocument() {
    }

    public void text(String str) {
        if (!this.fieldNames.isEmpty() && !this.fieldValues.isEmpty()) {
            this.fieldValues.push(((String) this.fieldValues.pop()) + str);
        }
    }
}

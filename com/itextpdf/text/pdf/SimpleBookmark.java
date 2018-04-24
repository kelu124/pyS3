package com.itextpdf.text.pdf;

import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.html.HtmlTags;
import com.itextpdf.text.xml.XMLUtil;
import com.itextpdf.text.xml.simpleparser.IanaEncodings;
import com.itextpdf.text.xml.simpleparser.SimpleXMLDocHandler;
import com.itextpdf.text.xml.simpleparser.SimpleXMLParser;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.StringTokenizer;

public final class SimpleBookmark implements SimpleXMLDocHandler {
    private final Stack<HashMap<String, Object>> attr = new Stack();
    private ArrayList<HashMap<String, Object>> topList;

    private SimpleBookmark() {
    }

    private static List<HashMap<String, Object>> bookmarkDepth(PdfReader reader, PdfDictionary outline, IntHashtable pages, boolean processCurrentOutlineOnly) {
        ArrayList<HashMap<String, Object>> list = new ArrayList();
        while (outline != null) {
            HashMap<String, Object> map = new HashMap();
            map.put("Title", ((PdfString) PdfReader.getPdfObjectRelease(outline.get(PdfName.TITLE))).toUnicodeString());
            PdfArray color = (PdfArray) PdfReader.getPdfObjectRelease(outline.get(PdfName.f119C));
            if (color != null && color.size() == 3) {
                ByteBuffer out = new ByteBuffer();
                out.append(color.getAsNumber(0).floatValue()).append(' ');
                out.append(color.getAsNumber(1).floatValue()).append(' ');
                out.append(color.getAsNumber(2).floatValue());
                map.put("Color", PdfEncodings.convertToString(out.toByteArray(), null));
            }
            PdfNumber style = (PdfNumber) PdfReader.getPdfObjectRelease(outline.get(PdfName.f122F));
            if (style != null) {
                int f = style.intValue();
                String s = "";
                if ((f & 1) != 0) {
                    s = s + "italic ";
                }
                if ((f & 2) != 0) {
                    s = s + "bold ";
                }
                s = s.trim();
                if (s.length() != 0) {
                    map.put("Style", s);
                }
            }
            PdfNumber count = (PdfNumber) PdfReader.getPdfObjectRelease(outline.get(PdfName.COUNT));
            if (count != null && count.intValue() < 0) {
                map.put("Open", PdfBoolean.FALSE);
            }
            try {
                PdfObject dest = PdfReader.getPdfObjectRelease(outline.get(PdfName.DEST));
                if (dest != null) {
                    mapGotoBookmark(map, dest, pages);
                } else {
                    PdfDictionary action = (PdfDictionary) PdfReader.getPdfObjectRelease(outline.get(PdfName.f117A));
                    if (action != null) {
                        if (PdfName.GOTO.equals(PdfReader.getPdfObjectRelease(action.get(PdfName.f133S)))) {
                            dest = PdfReader.getPdfObjectRelease(action.get(PdfName.f120D));
                            if (dest != null) {
                                mapGotoBookmark(map, dest, pages);
                            }
                        } else if (PdfName.URI.equals(PdfReader.getPdfObjectRelease(action.get(PdfName.f133S)))) {
                            map.put("Action", "URI");
                            map.put("URI", ((PdfString) PdfReader.getPdfObjectRelease(action.get(PdfName.URI))).toUnicodeString());
                        } else if (PdfName.JAVASCRIPT.equals(PdfReader.getPdfObjectRelease(action.get(PdfName.f133S)))) {
                            map.put("Action", "JS");
                            map.put("Code", PdfReader.getPdfObjectRelease(action.get(PdfName.JS)).toString());
                        } else if (PdfName.GOTOR.equals(PdfReader.getPdfObjectRelease(action.get(PdfName.f133S)))) {
                            dest = PdfReader.getPdfObjectRelease(action.get(PdfName.f120D));
                            if (dest != null) {
                                if (dest.isString()) {
                                    map.put("Named", dest.toString());
                                } else if (dest.isName()) {
                                    map.put("NamedN", PdfName.decodeName(dest.toString()));
                                } else if (dest.isArray()) {
                                    PdfArray arr = (PdfArray) dest;
                                    StringBuffer s2 = new StringBuffer();
                                    s2.append(arr.getPdfObject(0).toString());
                                    s2.append(' ').append(arr.getPdfObject(1).toString());
                                    for (int k = 2; k < arr.size(); k++) {
                                        s2.append(' ').append(arr.getPdfObject(k).toString());
                                    }
                                    map.put("Page", s2.toString());
                                }
                            }
                            map.put("Action", "GoToR");
                            file = PdfReader.getPdfObjectRelease(action.get(PdfName.f122F));
                            if (file != null) {
                                if (file.isString()) {
                                    map.put("File", ((PdfString) file).toUnicodeString());
                                } else if (file.isDictionary()) {
                                    file = PdfReader.getPdfObject(((PdfDictionary) file).get(PdfName.f122F));
                                    if (file.isString()) {
                                        map.put("File", ((PdfString) file).toUnicodeString());
                                    }
                                }
                            }
                            PdfObject newWindow = PdfReader.getPdfObjectRelease(action.get(PdfName.NEWWINDOW));
                            if (newWindow != null) {
                                map.put("NewWindow", newWindow.toString());
                            }
                        } else if (PdfName.LAUNCH.equals(PdfReader.getPdfObjectRelease(action.get(PdfName.f133S)))) {
                            map.put("Action", "Launch");
                            file = PdfReader.getPdfObjectRelease(action.get(PdfName.f122F));
                            if (file == null) {
                                file = PdfReader.getPdfObjectRelease(action.get(PdfName.WIN));
                            }
                            if (file != null) {
                                if (file.isString()) {
                                    map.put("File", ((PdfString) file).toUnicodeString());
                                } else if (file.isDictionary()) {
                                    file = PdfReader.getPdfObjectRelease(((PdfDictionary) file).get(PdfName.f122F));
                                    if (file.isString()) {
                                        map.put("File", ((PdfString) file).toUnicodeString());
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
            }
            PdfDictionary first = (PdfDictionary) PdfReader.getPdfObjectRelease(outline.get(PdfName.FIRST));
            if (first != null) {
                map.put("Kids", bookmarkDepth(reader, first, pages, false));
            }
            list.add(map);
            if (processCurrentOutlineOnly) {
                outline = null;
            } else {
                outline = (PdfDictionary) PdfReader.getPdfObjectRelease(outline.get(PdfName.NEXT));
            }
        }
        return list;
    }

    private static void mapGotoBookmark(HashMap<String, Object> map, PdfObject dest, IntHashtable pages) {
        if (dest.isString()) {
            map.put("Named", dest.toString());
        } else if (dest.isName()) {
            map.put("Named", PdfName.decodeName(dest.toString()));
        } else if (dest.isArray()) {
            map.put("Page", makeBookmarkParam((PdfArray) dest, pages));
        }
        map.put("Action", "GoTo");
    }

    private static String makeBookmarkParam(PdfArray dest, IntHashtable pages) {
        StringBuffer s = new StringBuffer();
        PdfObject obj = dest.getPdfObject(0);
        if (obj.isNumber()) {
            s.append(((PdfNumber) obj).intValue() + 1);
        } else {
            s.append(pages.get(getNumber((PdfIndirectReference) obj)));
        }
        s.append(' ').append(dest.getPdfObject(1).toString().substring(1));
        for (int k = 2; k < dest.size(); k++) {
            s.append(' ').append(dest.getPdfObject(k).toString());
        }
        return s.toString();
    }

    private static int getNumber(PdfIndirectReference indirect) {
        PdfDictionary pdfObj = (PdfDictionary) PdfReader.getPdfObjectRelease((PdfObject) indirect);
        if (pdfObj.contains(PdfName.TYPE) && pdfObj.get(PdfName.TYPE).equals(PdfName.PAGES) && pdfObj.contains(PdfName.KIDS)) {
            indirect = (PdfIndirectReference) ((PdfArray) pdfObj.get(PdfName.KIDS)).getPdfObject(0);
        }
        return indirect.getNumber();
    }

    public static List<HashMap<String, Object>> getBookmark(PdfReader reader) {
        PdfObject obj = PdfReader.getPdfObjectRelease(reader.getCatalog().get(PdfName.OUTLINES));
        if (obj == null || !obj.isDictionary()) {
            return null;
        }
        return getBookmark(reader, (PdfDictionary) obj, false);
    }

    public static List<HashMap<String, Object>> getBookmark(PdfReader reader, PdfDictionary outline, boolean includeRoot) {
        PdfDictionary catalog = reader.getCatalog();
        if (outline == null) {
            return null;
        }
        IntHashtable pages = new IntHashtable();
        int numPages = reader.getNumberOfPages();
        for (int k = 1; k <= numPages; k++) {
            pages.put(reader.getPageOrigRef(k).getNumber(), k);
            reader.releasePage(k);
        }
        if (includeRoot) {
            return bookmarkDepth(reader, outline, pages, true);
        }
        return bookmarkDepth(reader, (PdfDictionary) PdfReader.getPdfObjectRelease(outline.get(PdfName.FIRST)), pages, false);
    }

    public static void eliminatePages(List<HashMap<String, Object>> list, int[] pageRange) {
        if (list != null) {
            Iterator<HashMap<String, Object>> it = list.listIterator();
            while (it.hasNext()) {
                HashMap<String, Object> map = (HashMap) it.next();
                boolean hit = false;
                if ("GoTo".equals(map.get("Action"))) {
                    String page = (String) map.get("Page");
                    if (page != null) {
                        int pageNum;
                        page = page.trim();
                        int idx = page.indexOf(32);
                        if (idx < 0) {
                            pageNum = Integer.parseInt(page);
                        } else {
                            pageNum = Integer.parseInt(page.substring(0, idx));
                        }
                        int len = pageRange.length & -2;
                        int k = 0;
                        while (k < len) {
                            if (pageNum >= pageRange[k] && pageNum <= pageRange[k + 1]) {
                                hit = true;
                                break;
                            }
                            k += 2;
                        }
                    }
                }
                List<HashMap<String, Object>> kids = (List) map.get("Kids");
                if (kids != null) {
                    eliminatePages(kids, pageRange);
                    if (kids.isEmpty()) {
                        map.remove("Kids");
                        kids = null;
                    }
                }
                if (hit) {
                    if (kids == null) {
                        it.remove();
                    } else {
                        map.remove("Action");
                        map.remove("Page");
                        map.remove("Named");
                    }
                }
            }
        }
    }

    public static void shiftPageNumbers(List<HashMap<String, Object>> list, int pageShift, int[] pageRange) {
        if (list != null) {
            Iterator<HashMap<String, Object>> it = list.listIterator();
            while (it.hasNext()) {
                HashMap<String, Object> map = (HashMap) it.next();
                if ("GoTo".equals(map.get("Action"))) {
                    String page = (String) map.get("Page");
                    if (page != null) {
                        int pageNum;
                        page = page.trim();
                        int idx = page.indexOf(32);
                        if (idx < 0) {
                            pageNum = Integer.parseInt(page);
                        } else {
                            pageNum = Integer.parseInt(page.substring(0, idx));
                        }
                        boolean hit = false;
                        if (pageRange != null) {
                            int len = pageRange.length & -2;
                            int k = 0;
                            while (k < len) {
                                if (pageNum >= pageRange[k] && pageNum <= pageRange[k + 1]) {
                                    hit = true;
                                    break;
                                }
                                k += 2;
                            }
                        } else {
                            hit = true;
                        }
                        if (hit) {
                            if (idx < 0) {
                                page = Integer.toString(pageNum + pageShift);
                            } else {
                                page = (pageNum + pageShift) + page.substring(idx);
                            }
                        }
                        map.put("Page", page);
                    }
                }
                List<HashMap<String, Object>> kids = (List) map.get("Kids");
                if (kids != null) {
                    shiftPageNumbers(kids, pageShift, pageRange);
                }
            }
        }
    }

    static void createOutlineAction(PdfDictionary outline, HashMap<String, Object> map, PdfWriter writer, boolean namedAsNames) {
        try {
            String action = (String) map.get("Action");
            String p;
            PdfArray ar;
            StringTokenizer tk;
            String fn;
            int k;
            if ("GoTo".equals(action)) {
                p = (String) map.get("Named");
                if (p == null) {
                    p = (String) map.get("Page");
                    if (p != null) {
                        ar = new PdfArray();
                        tk = new StringTokenizer(p);
                        ar.add(writer.getPageReference(Integer.parseInt(tk.nextToken())));
                        if (tk.hasMoreTokens()) {
                            fn = tk.nextToken();
                            if (fn.startsWith("/")) {
                                fn = fn.substring(1);
                            }
                            ar.add(new PdfName(fn));
                            for (k = 0; k < 4 && tk.hasMoreTokens(); k++) {
                                fn = tk.nextToken();
                                if (fn.equals("null")) {
                                    ar.add(PdfNull.PDFNULL);
                                } else {
                                    ar.add(new PdfNumber(fn));
                                }
                            }
                        } else {
                            ar.add(PdfName.XYZ);
                            ar.add(new float[]{0.0f, 10000.0f, 0.0f});
                        }
                        outline.put(PdfName.DEST, ar);
                    }
                } else if (namedAsNames) {
                    outline.put(PdfName.DEST, new PdfName(p));
                } else {
                    outline.put(PdfName.DEST, new PdfString(p, null));
                }
            } else if ("GoToR".equals(action)) {
                dic = new PdfDictionary();
                p = (String) map.get("Named");
                if (p != null) {
                    dic.put(PdfName.f120D, new PdfString(p, null));
                } else {
                    p = (String) map.get("NamedN");
                    if (p != null) {
                        dic.put(PdfName.f120D, new PdfName(p));
                    } else {
                        p = (String) map.get("Page");
                        if (p != null) {
                            ar = new PdfArray();
                            tk = new StringTokenizer(p);
                            ar.add(new PdfNumber(tk.nextToken()));
                            if (tk.hasMoreTokens()) {
                                fn = tk.nextToken();
                                if (fn.startsWith("/")) {
                                    fn = fn.substring(1);
                                }
                                ar.add(new PdfName(fn));
                                for (k = 0; k < 4 && tk.hasMoreTokens(); k++) {
                                    fn = tk.nextToken();
                                    if (fn.equals("null")) {
                                        ar.add(PdfNull.PDFNULL);
                                    } else {
                                        ar.add(new PdfNumber(fn));
                                    }
                                }
                            } else {
                                ar.add(PdfName.XYZ);
                                ar.add(new float[]{0.0f, 10000.0f, 0.0f});
                            }
                            dic.put(PdfName.f120D, ar);
                        }
                    }
                }
                file = (String) map.get("File");
                if (dic.size() > 0 && file != null) {
                    dic.put(PdfName.f133S, PdfName.GOTOR);
                    dic.put(PdfName.f122F, new PdfString(file));
                    String nw = (String) map.get("NewWindow");
                    if (nw != null) {
                        if (nw.equals(PdfBoolean.TRUE)) {
                            dic.put(PdfName.NEWWINDOW, PdfBoolean.PDFTRUE);
                        } else if (nw.equals(PdfBoolean.FALSE)) {
                            dic.put(PdfName.NEWWINDOW, PdfBoolean.PDFFALSE);
                        }
                    }
                    outline.put(PdfName.f117A, dic);
                }
            } else if ("URI".equals(action)) {
                String uri = (String) map.get("URI");
                if (uri != null) {
                    dic = new PdfDictionary();
                    dic.put(PdfName.f133S, PdfName.URI);
                    dic.put(PdfName.URI, new PdfString(uri));
                    outline.put(PdfName.f117A, dic);
                }
            } else if ("JS".equals(action)) {
                String code = (String) map.get("Code");
                if (code != null) {
                    outline.put(PdfName.f117A, PdfAction.javaScript(code, writer));
                }
            } else if ("Launch".equals(action)) {
                file = (String) map.get("File");
                if (file != null) {
                    dic = new PdfDictionary();
                    dic.put(PdfName.f133S, PdfName.LAUNCH);
                    dic.put(PdfName.f122F, new PdfString(file));
                    outline.put(PdfName.f117A, dic);
                }
            }
        } catch (Exception e) {
        }
    }

    public static Object[] iterateOutlines(PdfWriter writer, PdfIndirectReference parent, List<HashMap<String, Object>> kids, boolean namedAsNames) throws IOException {
        int k;
        PdfIndirectReference[] refs = new PdfIndirectReference[kids.size()];
        for (k = 0; k < refs.length; k++) {
            refs[k] = writer.getPdfIndirectReference();
        }
        int ptr = 0;
        int count = 0;
        Iterator<HashMap<String, Object>> it = kids.listIterator();
        while (it.hasNext()) {
            HashMap<String, Object> map = (HashMap) it.next();
            Object[] lower = null;
            List<HashMap<String, Object>> subKid = (List) map.get("Kids");
            if (!(subKid == null || subKid.isEmpty())) {
                lower = iterateOutlines(writer, refs[ptr], subKid, namedAsNames);
            }
            PdfDictionary outline = new PdfDictionary();
            count++;
            if (lower != null) {
                outline.put(PdfName.FIRST, (PdfIndirectReference) lower[0]);
                outline.put(PdfName.LAST, (PdfIndirectReference) lower[1]);
                int n = ((Integer) lower[2]).intValue();
                if (PdfBoolean.FALSE.equals(map.get("Open"))) {
                    outline.put(PdfName.COUNT, new PdfNumber(-n));
                } else {
                    outline.put(PdfName.COUNT, new PdfNumber(n));
                    count += n;
                }
            }
            outline.put(PdfName.PARENT, parent);
            if (ptr > 0) {
                outline.put(PdfName.PREV, refs[ptr - 1]);
            }
            if (ptr < refs.length - 1) {
                outline.put(PdfName.NEXT, refs[ptr + 1]);
            }
            outline.put(PdfName.TITLE, new PdfString((String) map.get("Title"), PdfObject.TEXT_UNICODE));
            String color = (String) map.get("Color");
            if (color != null) {
                try {
                    PdfArray arr = new PdfArray();
                    StringTokenizer stringTokenizer = new StringTokenizer(color);
                    for (k = 0; k < 3; k++) {
                        float f = Float.parseFloat(stringTokenizer.nextToken());
                        if (f < 0.0f) {
                            f = 0.0f;
                        }
                        if (f > BaseField.BORDER_WIDTH_THIN) {
                            f = BaseField.BORDER_WIDTH_THIN;
                        }
                        arr.add(new PdfNumber(f));
                    }
                    outline.put(PdfName.f119C, arr);
                } catch (Exception e) {
                }
            }
            String style = (String) map.get("Style");
            if (style != null) {
                style = style.toLowerCase();
                int bits = 0;
                if (style.indexOf(HtmlTags.ITALIC) >= 0) {
                    bits = 0 | 1;
                }
                if (style.indexOf(HtmlTags.BOLD) >= 0) {
                    bits |= 2;
                }
                if (bits != 0) {
                    outline.put(PdfName.f122F, new PdfNumber(bits));
                }
            }
            createOutlineAction(outline, map, writer, namedAsNames);
            writer.addToBody(outline, refs[ptr]);
            ptr++;
        }
        return new Object[]{refs[0], refs[refs.length - 1], Integer.valueOf(count)};
    }

    public static void exportToXMLNode(List<HashMap<String, Object>> list, Writer out, int indent, boolean onlyASCII) throws IOException {
        String dep = "";
        if (indent != -1) {
            for (int k = 0; k < indent; k++) {
                dep = dep + "  ";
            }
        }
        for (HashMap<String, Object> map : list) {
            String title = null;
            out.write(dep);
            out.write("<Title ");
            List<HashMap<String, Object>> kids = null;
            for (Entry<String, Object> entry : map.entrySet()) {
                String key = (String) entry.getKey();
                if (key.equals("Title")) {
                    title = (String) entry.getValue();
                } else if (key.equals("Kids")) {
                    kids = (List) entry.getValue();
                } else {
                    out.write(key);
                    out.write("=\"");
                    String value = (String) entry.getValue();
                    if (key.equals("Named") || key.equals("NamedN")) {
                        value = SimpleNamedDestination.escapeBinaryString(value);
                    }
                    out.write(XMLUtil.escapeXML(value, onlyASCII));
                    out.write("\" ");
                }
            }
            out.write(">");
            if (title == null) {
                title = "";
            }
            out.write(XMLUtil.escapeXML(title, onlyASCII));
            if (kids != null) {
                out.write("\n");
                exportToXMLNode(kids, out, indent == -1 ? indent : indent + 1, onlyASCII);
                out.write(dep);
            }
            out.write("</Title>\n");
        }
    }

    public static void exportToXML(List<HashMap<String, Object>> list, OutputStream out, String encoding, boolean onlyASCII) throws IOException {
        exportToXML((List) list, new BufferedWriter(new OutputStreamWriter(out, IanaEncodings.getJavaEncoding(encoding))), encoding, onlyASCII);
    }

    public static void exportToXML(List<HashMap<String, Object>> list, Writer wrt, String encoding, boolean onlyASCII) throws IOException {
        wrt.write("<?xml version=\"1.0\" encoding=\"");
        wrt.write(XMLUtil.escapeXML(encoding, onlyASCII));
        wrt.write("\"?>\n<Bookmark>\n");
        exportToXMLNode(list, wrt, 1, onlyASCII);
        wrt.write("</Bookmark>\n");
        wrt.flush();
    }

    public static List<HashMap<String, Object>> importFromXML(InputStream in) throws IOException {
        SimpleBookmark book = new SimpleBookmark();
        SimpleXMLParser.parse(book, in);
        return book.topList;
    }

    public static List<HashMap<String, Object>> importFromXML(Reader in) throws IOException {
        SimpleBookmark book = new SimpleBookmark();
        SimpleXMLParser.parse(book, in);
        return book.topList;
    }

    public void endDocument() {
    }

    public void endElement(String tag) {
        if (tag.equals("Bookmark")) {
            if (!this.attr.isEmpty()) {
                throw new RuntimeException(MessageLocalization.getComposedMessage("bookmark.end.tag.out.of.place", new Object[0]));
            }
        } else if (tag.equals("Title")) {
            HashMap<String, Object> attributes = (HashMap) this.attr.pop();
            attributes.put("Title", ((String) attributes.get("Title")).trim());
            String named = (String) attributes.get("Named");
            if (named != null) {
                attributes.put("Named", SimpleNamedDestination.unEscapeBinaryString(named));
            }
            named = (String) attributes.get("NamedN");
            if (named != null) {
                attributes.put("NamedN", SimpleNamedDestination.unEscapeBinaryString(named));
            }
            if (this.attr.isEmpty()) {
                this.topList.add(attributes);
                return;
            }
            HashMap<String, Object> parent = (HashMap) this.attr.peek();
            List<HashMap<String, Object>> kids = (List) parent.get("Kids");
            if (kids == null) {
                kids = new ArrayList();
                parent.put("Kids", kids);
            }
            kids.add(attributes);
        } else {
            throw new RuntimeException(MessageLocalization.getComposedMessage("invalid.end.tag.1", tag));
        }
    }

    public void startDocument() {
    }

    public void startElement(String tag, Map<String, String> h) {
        if (this.topList == null) {
            if (tag.equals("Bookmark")) {
                this.topList = new ArrayList();
            } else {
                throw new RuntimeException(MessageLocalization.getComposedMessage("root.element.is.not.bookmark.1", tag));
            }
        } else if (tag.equals("Title")) {
            HashMap<String, Object> attributes = new HashMap(h);
            attributes.put("Title", "");
            attributes.remove("Kids");
            this.attr.push(attributes);
        } else {
            throw new RuntimeException(MessageLocalization.getComposedMessage("tag.1.not.allowed", tag));
        }
    }

    public void text(String str) {
        if (!this.attr.isEmpty()) {
            HashMap<String, Object> attributes = (HashMap) this.attr.peek();
            attributes.put("Title", ((String) attributes.get("Title")) + str);
        }
    }
}

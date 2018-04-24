package com.itextpdf.text.pdf;

import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.pdf.AcroFields.Item;
import com.itextpdf.text.xml.XmlDomWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XfaForm {
    public static final String XFA_DATA_SCHEMA = "http://www.xfa.org/schema/xfa-data/1.0/";
    private AcroFieldsSearch acroFieldsSom;
    private boolean changed;
    private Node datasetsNode;
    private Xml2SomDatasets datasetsSom;
    private Document domDocument;
    private PdfReader reader;
    private Node templateNode;
    private Xml2SomTemplate templateSom;
    private boolean xfaPresent;

    public static class Xml2Som {
        protected int anform;
        protected HashMap<String, InverseStore> inverseSearch;
        protected HashMap<String, Node> name2Node;
        protected ArrayList<String> order;
        protected Stack2<String> stack;

        public static String escapeSom(String s) {
            if (s == null) {
                return "";
            }
            int idx = s.indexOf(46);
            if (idx < 0) {
                return s;
            }
            StringBuffer sb = new StringBuffer();
            int last = 0;
            while (idx >= 0) {
                sb.append(s.substring(last, idx));
                sb.append('\\');
                last = idx;
                idx = s.indexOf(46, idx + 1);
            }
            sb.append(s.substring(last));
            return sb.toString();
        }

        public static String unescapeSom(String s) {
            int idx = s.indexOf(92);
            if (idx < 0) {
                return s;
            }
            StringBuffer sb = new StringBuffer();
            int last = 0;
            while (idx >= 0) {
                sb.append(s.substring(last, idx));
                last = idx + 1;
                idx = s.indexOf(92, idx + 1);
            }
            sb.append(s.substring(last));
            return sb.toString();
        }

        protected String printStack() {
            if (this.stack.empty()) {
                return "";
            }
            StringBuffer s = new StringBuffer();
            for (int k = 0; k < this.stack.size(); k++) {
                s.append('.').append((String) this.stack.get(k));
            }
            return s.substring(1);
        }

        public static String getShortName(String s) {
            int idx = s.indexOf(".#subform[");
            if (idx < 0) {
                return s;
            }
            int last = 0;
            StringBuffer sb = new StringBuffer();
            while (idx >= 0) {
                sb.append(s.substring(last, idx));
                idx = s.indexOf("]", idx + 10);
                if (idx < 0) {
                    return sb.toString();
                }
                last = idx + 1;
                idx = s.indexOf(".#subform[", last);
            }
            sb.append(s.substring(last));
            return sb.toString();
        }

        public void inverseSearchAdd(String unstack) {
            inverseSearchAdd(this.inverseSearch, this.stack, unstack);
        }

        public static void inverseSearchAdd(HashMap<String, InverseStore> inverseSearch, Stack2<String> stack, String unstack) {
            String last = (String) stack.peek();
            InverseStore inverseStore = (InverseStore) inverseSearch.get(last);
            if (inverseStore == null) {
                inverseStore = new InverseStore();
                inverseSearch.put(last, inverseStore);
            }
            for (int k = stack.size() - 2; k >= 0; k--) {
                InverseStore store2;
                last = (String) stack.get(k);
                int idx = inverseStore.part.indexOf(last);
                if (idx < 0) {
                    inverseStore.part.add(last);
                    store2 = new InverseStore();
                    inverseStore.follow.add(store2);
                } else {
                    store2 = (InverseStore) inverseStore.follow.get(idx);
                }
                inverseStore = store2;
            }
            inverseStore.part.add("");
            inverseStore.follow.add(unstack);
        }

        public String inverseSearchGlobal(ArrayList<String> parts) {
            if (parts.isEmpty()) {
                return null;
            }
            InverseStore store = (InverseStore) this.inverseSearch.get(parts.get(parts.size() - 1));
            if (store == null) {
                return null;
            }
            int k = parts.size() - 2;
            while (k >= 0) {
                String part = (String) parts.get(k);
                int idx = store.part.indexOf(part);
                if (idx >= 0) {
                    store = (InverseStore) store.follow.get(idx);
                    k--;
                } else if (store.isSimilar(part)) {
                    return null;
                } else {
                    return store.getDefaultName();
                }
            }
            return store.getDefaultName();
        }

        public static Stack2<String> splitParts(String name) {
            String part;
            while (name.startsWith(".")) {
                name = name.substring(1);
            }
            Stack2<String> parts = new Stack2();
            int last = 0;
            while (true) {
                int pos = last;
                while (true) {
                    pos = name.indexOf(46, pos);
                    if (pos >= 0 && name.charAt(pos - 1) == '\\') {
                        pos++;
                    }
                }
                if (pos < 0) {
                    break;
                }
                part = name.substring(last, pos);
                if (!part.endsWith("]")) {
                    part = part + "[0]";
                }
                parts.add(part);
                last = pos + 1;
            }
            part = name.substring(last);
            if (!part.endsWith("]")) {
                part = part + "[0]";
            }
            parts.add(part);
            return parts;
        }

        public ArrayList<String> getOrder() {
            return this.order;
        }

        public void setOrder(ArrayList<String> order) {
            this.order = order;
        }

        public HashMap<String, Node> getName2Node() {
            return this.name2Node;
        }

        public void setName2Node(HashMap<String, Node> name2Node) {
            this.name2Node = name2Node;
        }

        public HashMap<String, InverseStore> getInverseSearch() {
            return this.inverseSearch;
        }

        public void setInverseSearch(HashMap<String, InverseStore> inverseSearch) {
            this.inverseSearch = inverseSearch;
        }
    }

    public static class AcroFieldsSearch extends Xml2Som {
        private HashMap<String, String> acroShort2LongName;

        public AcroFieldsSearch(Collection<String> items) {
            this.inverseSearch = new HashMap();
            this.acroShort2LongName = new HashMap();
            for (String itemName : items) {
                String itemShort = Xml2Som.getShortName(itemName);
                this.acroShort2LongName.put(itemShort, itemName);
                Xml2Som.inverseSearchAdd(this.inverseSearch, Xml2Som.splitParts(itemShort), itemName);
            }
        }

        public HashMap<String, String> getAcroShort2LongName() {
            return this.acroShort2LongName;
        }

        public void setAcroShort2LongName(HashMap<String, String> acroShort2LongName) {
            this.acroShort2LongName = acroShort2LongName;
        }
    }

    public static class InverseStore {
        protected ArrayList<Object> follow = new ArrayList();
        protected ArrayList<String> part = new ArrayList();

        public String getDefaultName() {
            InverseStore store = this;
            while (true) {
                InverseStore obj = store.follow.get(0);
                if (obj instanceof String) {
                    return (String) obj;
                }
                store = obj;
            }
        }

        public boolean isSimilar(String name) {
            name = name.substring(0, name.indexOf(91) + 1);
            for (int k = 0; k < this.part.size(); k++) {
                if (((String) this.part.get(k)).startsWith(name)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static class Stack2<T> extends ArrayList<T> {
        private static final long serialVersionUID = -7451476576174095212L;

        public T peek() {
            if (size() != 0) {
                return get(size() - 1);
            }
            throw new EmptyStackException();
        }

        public T pop() {
            if (size() == 0) {
                throw new EmptyStackException();
            }
            T ret = get(size() - 1);
            remove(size() - 1);
            return ret;
        }

        public T push(T item) {
            add(item);
            return item;
        }

        public boolean empty() {
            return size() == 0;
        }
    }

    public static class Xml2SomDatasets extends Xml2Som {
        public Xml2SomDatasets(Node n) {
            this.order = new ArrayList();
            this.name2Node = new HashMap();
            this.stack = new Stack2();
            this.anform = 0;
            this.inverseSearch = new HashMap();
            processDatasetsInternal(n);
        }

        public Node insertNode(Node n, String shortName) {
            Stack2<String> stack = Xml2Som.splitParts(shortName);
            Document doc = n.getOwnerDocument();
            Node n2 = null;
            n = n.getFirstChild();
            while (n.getNodeType() != (short) 1) {
                n = n.getNextSibling();
            }
            for (int k = 0; k < stack.size(); k++) {
                String part = (String) stack.get(k);
                int idx = part.lastIndexOf(91);
                String name = part.substring(0, idx);
                idx = Integer.parseInt(part.substring(idx + 1, part.length() - 1));
                int found = -1;
                n2 = n.getFirstChild();
                while (n2 != null) {
                    if (n2.getNodeType() == (short) 1 && Xml2Som.escapeSom(n2.getLocalName()).equals(name)) {
                        found++;
                        if (found == idx) {
                            break;
                        }
                    }
                    n2 = n2.getNextSibling();
                }
                while (found < idx) {
                    n2 = n.appendChild(doc.createElementNS(null, name));
                    Node attr = doc.createAttributeNS(XfaForm.XFA_DATA_SCHEMA, "dataNode");
                    attr.setNodeValue("dataGroup");
                    n2.getAttributes().setNamedItemNS(attr);
                    found++;
                }
                n = n2;
            }
            Xml2Som.inverseSearchAdd(this.inverseSearch, stack, shortName);
            this.name2Node.put(shortName, n2);
            this.order.add(shortName);
            return n2;
        }

        private static boolean hasChildren(Node n) {
            Node dataNodeN = n.getAttributes().getNamedItemNS(XfaForm.XFA_DATA_SCHEMA, "dataNode");
            if (dataNodeN != null) {
                String dataNode = dataNodeN.getNodeValue();
                if ("dataGroup".equals(dataNode)) {
                    return true;
                }
                if ("dataValue".equals(dataNode)) {
                    return false;
                }
            }
            if (!n.hasChildNodes()) {
                return false;
            }
            for (Node n2 = n.getFirstChild(); n2 != null; n2 = n2.getNextSibling()) {
                if (n2.getNodeType() == (short) 1) {
                    return true;
                }
            }
            return false;
        }

        private void processDatasetsInternal(Node n) {
            if (n != null) {
                HashMap<String, Integer> ss = new HashMap();
                for (Node n2 = n.getFirstChild(); n2 != null; n2 = n2.getNextSibling()) {
                    if (n2.getNodeType() == (short) 1) {
                        String s = Xml2Som.escapeSom(n2.getLocalName());
                        Integer i = (Integer) ss.get(s);
                        if (i == null) {
                            i = Integer.valueOf(0);
                        } else {
                            i = Integer.valueOf(i.intValue() + 1);
                        }
                        ss.put(s, i);
                        if (hasChildren(n2)) {
                            this.stack.push(s + "[" + i.toString() + "]");
                            processDatasetsInternal(n2);
                            this.stack.pop();
                        } else {
                            this.stack.push(s + "[" + i.toString() + "]");
                            String unstack = printStack();
                            this.order.add(unstack);
                            inverseSearchAdd(unstack);
                            this.name2Node.put(unstack, n2);
                            this.stack.pop();
                        }
                    }
                }
            }
        }
    }

    public static class Xml2SomTemplate extends Xml2Som {
        private boolean dynamicForm;
        private int templateLevel;

        public Xml2SomTemplate(Node n) {
            this.order = new ArrayList();
            this.name2Node = new HashMap();
            this.stack = new Stack2();
            this.anform = 0;
            this.templateLevel = 0;
            this.inverseSearch = new HashMap();
            processTemplate(n, null);
        }

        public String getFieldType(String s) {
            Node n = (Node) this.name2Node.get(s);
            if (n == null) {
                return null;
            }
            if ("exclGroup".equals(n.getLocalName())) {
                return "exclGroup";
            }
            Node ui = n.getFirstChild();
            while (ui != null && (ui.getNodeType() != (short) 1 || !"ui".equals(ui.getLocalName()))) {
                ui = ui.getNextSibling();
            }
            if (ui == null) {
                return null;
            }
            Node type = ui.getFirstChild();
            while (type != null) {
                if (type.getNodeType() == (short) 1 && (!"extras".equals(type.getLocalName()) || !"picture".equals(type.getLocalName()))) {
                    return type.getLocalName();
                }
                type = type.getNextSibling();
            }
            return null;
        }

        private void processTemplate(Node n, HashMap<String, Integer> ff) {
            if (ff == null) {
                ff = new HashMap();
            }
            HashMap<String, Integer> ss = new HashMap();
            for (Node n2 = n.getFirstChild(); n2 != null; n2 = n2.getNextSibling()) {
                if (n2.getNodeType() == (short) 1) {
                    String s = n2.getLocalName();
                    Node name;
                    String nn;
                    Integer i;
                    if ("subform".equals(s)) {
                        name = n2.getAttributes().getNamedItem("name");
                        nn = "#subform";
                        boolean annon = true;
                        if (name != null) {
                            nn = Xml2Som.escapeSom(name.getNodeValue());
                            annon = false;
                        }
                        if (annon) {
                            i = Integer.valueOf(this.anform);
                            this.anform++;
                        } else {
                            i = (Integer) ss.get(nn);
                            if (i == null) {
                                i = Integer.valueOf(0);
                            } else {
                                i = Integer.valueOf(i.intValue() + 1);
                            }
                            ss.put(nn, i);
                        }
                        this.stack.push(nn + "[" + i.toString() + "]");
                        this.templateLevel++;
                        if (annon) {
                            processTemplate(n2, ff);
                        } else {
                            processTemplate(n2, null);
                        }
                        this.templateLevel--;
                        this.stack.pop();
                    } else if ("field".equals(s) || "exclGroup".equals(s)) {
                        name = n2.getAttributes().getNamedItem("name");
                        if (name != null) {
                            nn = Xml2Som.escapeSom(name.getNodeValue());
                            i = (Integer) ff.get(nn);
                            if (i == null) {
                                i = Integer.valueOf(0);
                            } else {
                                i = Integer.valueOf(i.intValue() + 1);
                            }
                            ff.put(nn, i);
                            this.stack.push(nn + "[" + i.toString() + "]");
                            String unstack = printStack();
                            this.order.add(unstack);
                            inverseSearchAdd(unstack);
                            this.name2Node.put(unstack, n2);
                            this.stack.pop();
                        }
                    } else if (!this.dynamicForm && this.templateLevel > 0 && "occur".equals(s)) {
                        int initial = 1;
                        int min = 1;
                        int max = 1;
                        Node a = n2.getAttributes().getNamedItem("initial");
                        if (a != null) {
                            try {
                                initial = Integer.parseInt(a.getNodeValue().trim());
                            } catch (Exception e) {
                            }
                        }
                        a = n2.getAttributes().getNamedItem("min");
                        if (a != null) {
                            try {
                                min = Integer.parseInt(a.getNodeValue().trim());
                            } catch (Exception e2) {
                            }
                        }
                        a = n2.getAttributes().getNamedItem("max");
                        if (a != null) {
                            try {
                                max = Integer.parseInt(a.getNodeValue().trim());
                            } catch (Exception e3) {
                            }
                        }
                        if (initial != min || min != max) {
                            this.dynamicForm = true;
                        }
                    }
                }
            }
        }

        public boolean isDynamicForm() {
            return this.dynamicForm;
        }

        public void setDynamicForm(boolean dynamicForm) {
            this.dynamicForm = dynamicForm;
        }
    }

    public static PdfObject getXfaObject(PdfReader reader) {
        PdfDictionary af = (PdfDictionary) PdfReader.getPdfObjectRelease(reader.getCatalog().get(PdfName.ACROFORM));
        if (af == null) {
            return null;
        }
        return PdfReader.getPdfObjectRelease(af.get(PdfName.XFA));
    }

    public XfaForm(PdfReader reader) throws IOException, ParserConfigurationException, SAXException {
        this.reader = reader;
        PdfObject xfa = getXfaObject(reader);
        if (xfa == null) {
            this.xfaPresent = false;
            return;
        }
        this.xfaPresent = true;
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        if (xfa.isArray()) {
            PdfArray ar = (PdfArray) xfa;
            for (int k = 1; k < ar.size(); k += 2) {
                PdfObject ob = ar.getDirectObject(k);
                if (ob instanceof PRStream) {
                    bout.write(PdfReader.getStreamBytes((PRStream) ob));
                }
            }
        } else if (xfa instanceof PRStream) {
            bout.write(PdfReader.getStreamBytes((PRStream) xfa));
        }
        bout.close();
        DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
        fact.setNamespaceAware(true);
        this.domDocument = fact.newDocumentBuilder().parse(new ByteArrayInputStream(bout.toByteArray()));
        extractNodes();
    }

    private void extractNodes() {
        Map<String, Node> xfaNodes = extractXFANodes(this.domDocument);
        if (xfaNodes.containsKey("template")) {
            this.templateNode = (Node) xfaNodes.get("template");
            this.templateSom = new Xml2SomTemplate(this.templateNode);
        }
        if (xfaNodes.containsKey("datasets")) {
            this.datasetsNode = (Node) xfaNodes.get("datasets");
            this.datasetsSom = new Xml2SomDatasets(this.datasetsNode.getFirstChild());
        }
        if (this.datasetsNode == null) {
            createDatasetsNode(this.domDocument.getFirstChild());
        }
    }

    public static Map<String, Node> extractXFANodes(Document domDocument) {
        Map<String, Node> xfaNodes = new HashMap();
        Node n = domDocument.getFirstChild();
        while (n.getChildNodes().getLength() == 0) {
            n = n.getNextSibling();
        }
        for (n = n.getFirstChild(); n != null; n = n.getNextSibling()) {
            if (n.getNodeType() == (short) 1) {
                xfaNodes.put(n.getLocalName(), n);
            }
        }
        return xfaNodes;
    }

    private void createDatasetsNode(Node n) {
        while (n.getChildNodes().getLength() == 0) {
            n = n.getNextSibling();
        }
        if (n != null) {
            Element e = n.getOwnerDocument().createElement("xfa:datasets");
            e.setAttribute("xmlns:xfa", XFA_DATA_SCHEMA);
            this.datasetsNode = e;
            n.appendChild(this.datasetsNode);
        }
    }

    public static void setXfa(XfaForm form, PdfReader reader, PdfWriter writer) throws IOException {
        PdfDictionary af = (PdfDictionary) PdfReader.getPdfObjectRelease(reader.getCatalog().get(PdfName.ACROFORM));
        if (af != null) {
            PdfObject xfa = getXfaObject(reader);
            if (xfa.isArray()) {
                PdfArray ar = (PdfArray) xfa;
                int t = -1;
                int d = -1;
                for (int k = 0; k < ar.size(); k += 2) {
                    PdfString s = ar.getAsString(k);
                    if ("template".equals(s.toString())) {
                        t = k + 1;
                    }
                    if ("datasets".equals(s.toString())) {
                        d = k + 1;
                    }
                }
                if (t > -1 && d > -1) {
                    reader.killXref(ar.getAsIndirectObject(t));
                    reader.killXref(ar.getAsIndirectObject(d));
                    PdfStream tStream = new PdfStream(serializeDoc(form.templateNode));
                    tStream.flateCompress(writer.getCompressionLevel());
                    ar.set(t, writer.addToBody(tStream).getIndirectReference());
                    PdfStream dStream = new PdfStream(serializeDoc(form.datasetsNode));
                    dStream.flateCompress(writer.getCompressionLevel());
                    ar.set(d, writer.addToBody(dStream).getIndirectReference());
                    af.put(PdfName.XFA, new PdfArray(ar));
                    return;
                }
            }
            reader.killXref(af.get(PdfName.XFA));
            PdfStream str = new PdfStream(serializeDoc(form.domDocument));
            str.flateCompress(writer.getCompressionLevel());
            af.put(PdfName.XFA, writer.addToBody(str).getIndirectReference());
        }
    }

    public void setXfa(PdfWriter writer) throws IOException {
        setXfa(this, this.reader, writer);
    }

    public static byte[] serializeDoc(Node n) throws IOException {
        XmlDomWriter xw = new XmlDomWriter();
        ByteArrayOutputStream fout = new ByteArrayOutputStream();
        xw.setOutput(fout, null);
        xw.setCanonical(false);
        xw.write(n);
        fout.close();
        return fout.toByteArray();
    }

    public boolean isXfaPresent() {
        return this.xfaPresent;
    }

    public Document getDomDocument() {
        return this.domDocument;
    }

    public String findFieldName(String name, AcroFields af) {
        Map<String, Item> items = af.getFields();
        if (items.containsKey(name)) {
            return name;
        }
        if (this.acroFieldsSom == null) {
            if (items.isEmpty() && this.xfaPresent) {
                this.acroFieldsSom = new AcroFieldsSearch(this.datasetsSom.getName2Node().keySet());
            } else {
                this.acroFieldsSom = new AcroFieldsSearch(items.keySet());
            }
        }
        if (this.acroFieldsSom.getAcroShort2LongName().containsKey(name)) {
            return (String) this.acroFieldsSom.getAcroShort2LongName().get(name);
        }
        return this.acroFieldsSom.inverseSearchGlobal(Xml2Som.splitParts(name));
    }

    public String findDatasetsName(String name) {
        return this.datasetsSom.getName2Node().containsKey(name) ? name : this.datasetsSom.inverseSearchGlobal(Xml2Som.splitParts(name));
    }

    public Node findDatasetsNode(String name) {
        if (name == null) {
            return null;
        }
        name = findDatasetsName(name);
        if (name != null) {
            return (Node) this.datasetsSom.getName2Node().get(name);
        }
        return null;
    }

    public static String getNodeText(Node n) {
        if (n == null) {
            return "";
        }
        return getNodeText(n, "");
    }

    private static String getNodeText(Node n, String name) {
        for (Node n2 = n.getFirstChild(); n2 != null; n2 = n2.getNextSibling()) {
            if (n2.getNodeType() == (short) 1) {
                name = getNodeText(n2, name);
            } else if (n2.getNodeType() == (short) 3) {
                name = name + n2.getNodeValue();
            }
        }
        return name;
    }

    public void setNodeText(Node n, String text) {
        if (n != null) {
            while (true) {
                Node nc = n.getFirstChild();
                if (nc == null) {
                    break;
                }
                n.removeChild(nc);
            }
            if (n.getAttributes().getNamedItemNS(XFA_DATA_SCHEMA, "dataNode") != null) {
                n.getAttributes().removeNamedItemNS(XFA_DATA_SCHEMA, "dataNode");
            }
            n.appendChild(this.domDocument.createTextNode(text));
            this.changed = true;
        }
    }

    public void setXfaPresent(boolean xfaPresent) {
        this.xfaPresent = xfaPresent;
    }

    public void setDomDocument(Document domDocument) {
        this.domDocument = domDocument;
        extractNodes();
    }

    public PdfReader getReader() {
        return this.reader;
    }

    public void setReader(PdfReader reader) {
        this.reader = reader;
    }

    public boolean isChanged() {
        return this.changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public Xml2SomTemplate getTemplateSom() {
        return this.templateSom;
    }

    public void setTemplateSom(Xml2SomTemplate templateSom) {
        this.templateSom = templateSom;
    }

    public Xml2SomDatasets getDatasetsSom() {
        return this.datasetsSom;
    }

    public void setDatasetsSom(Xml2SomDatasets datasetsSom) {
        this.datasetsSom = datasetsSom;
    }

    public AcroFieldsSearch getAcroFieldsSom() {
        return this.acroFieldsSom;
    }

    public void setAcroFieldsSom(AcroFieldsSearch acroFieldsSom) {
        this.acroFieldsSom = acroFieldsSom;
    }

    public Node getDatasetsNode() {
        return this.datasetsNode;
    }

    public void fillXfaForm(File file) throws IOException {
        fillXfaForm(file, false);
    }

    public void fillXfaForm(File file, boolean readOnly) throws IOException {
        fillXfaForm(new FileInputStream(file), readOnly);
    }

    public void fillXfaForm(InputStream is) throws IOException {
        fillXfaForm(is, false);
    }

    public void fillXfaForm(InputStream is, boolean readOnly) throws IOException {
        fillXfaForm(new InputSource(is), readOnly);
    }

    public void fillXfaForm(InputSource is) throws IOException {
        fillXfaForm(is, false);
    }

    public void fillXfaForm(InputSource is, boolean readOnly) throws IOException {
        try {
            fillXfaForm(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is).getDocumentElement(), readOnly);
        } catch (ParserConfigurationException e) {
            throw new ExceptionConverter(e);
        } catch (SAXException e2) {
            throw new ExceptionConverter(e2);
        }
    }

    public void fillXfaForm(Node node) {
        fillXfaForm(node, false);
    }

    public void fillXfaForm(Node node, boolean readOnly) {
        if (readOnly) {
            NodeList nodeList = this.domDocument.getElementsByTagName("field");
            for (int i = 0; i < nodeList.getLength(); i++) {
                ((Element) nodeList.item(i)).setAttribute("access", "readOnly");
            }
        }
        NodeList allChilds = this.datasetsNode.getChildNodes();
        int len = allChilds.getLength();
        Node data = null;
        for (int k = 0; k < len; k++) {
            Node n = allChilds.item(k);
            if (n.getNodeType() == (short) 1 && n.getLocalName().equals("data") && XFA_DATA_SCHEMA.equals(n.getNamespaceURI())) {
                data = n;
                break;
            }
        }
        if (data == null) {
            data = this.datasetsNode.getOwnerDocument().createElementNS(XFA_DATA_SCHEMA, "xfa:data");
            this.datasetsNode.appendChild(data);
        }
        if (data.getChildNodes().getLength() == 0) {
            data.appendChild(this.domDocument.importNode(node, true));
        } else {
            Node firstNode = getFirstElementNode(data);
            if (firstNode != null) {
                data.replaceChild(this.domDocument.importNode(node, true), firstNode);
            }
        }
        extractNodes();
        setChanged(true);
    }

    private Node getFirstElementNode(Node src) {
        NodeList list = src.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            if (list.item(i).getNodeType() == (short) 1) {
                return list.item(i);
            }
        }
        return null;
    }
}

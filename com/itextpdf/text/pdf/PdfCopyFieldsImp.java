package com.itextpdf.text.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.exceptions.BadPasswordException;
import com.itextpdf.text.log.Counter;
import com.itextpdf.text.log.CounterFactory;
import com.itextpdf.text.pdf.AcroFields.Item;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

@Deprecated
class PdfCopyFieldsImp extends PdfWriter {
    protected static final HashMap<PdfName, Integer> fieldKeys = new HashMap();
    private static final PdfName iTextTag = new PdfName("_iTextTag_");
    protected static final HashMap<PdfName, Integer> widgetKeys = new HashMap();
    private static final Integer zero = Integer.valueOf(0);
    protected Counter COUNTER;
    private ArrayList<String> calculationOrder;
    private ArrayList<Object> calculationOrderRefs;
    boolean closing;
    HashMap<String, Object> fieldTree;
    ArrayList<AcroFields> fields;
    RandomAccessFileOrArray file;
    PdfDictionary form;
    private boolean hasSignature;
    private HashSet<Object> mergedRadioButtons;
    Document nd;
    private boolean needAppearances;
    ArrayList<PdfDictionary> pageDics;
    ArrayList<PdfIndirectReference> pageRefs;
    HashMap<PdfReader, IntHashtable> pages2intrefs;
    ArrayList<PdfReader> readers;
    HashMap<PdfReader, IntHashtable> readers2intrefs;
    PdfDictionary resources;
    private HashMap<PdfArray, ArrayList<Integer>> tabOrder;
    HashMap<PdfReader, IntHashtable> visited;

    static {
        Integer one = Integer.valueOf(1);
        widgetKeys.put(PdfName.SUBTYPE, one);
        widgetKeys.put(PdfName.CONTENTS, one);
        widgetKeys.put(PdfName.RECT, one);
        widgetKeys.put(PdfName.NM, one);
        widgetKeys.put(PdfName.M, one);
        widgetKeys.put(PdfName.F, one);
        widgetKeys.put(PdfName.BS, one);
        widgetKeys.put(PdfName.BORDER, one);
        widgetKeys.put(PdfName.AP, one);
        widgetKeys.put(PdfName.AS, one);
        widgetKeys.put(PdfName.C, one);
        widgetKeys.put(PdfName.A, one);
        widgetKeys.put(PdfName.STRUCTPARENT, one);
        widgetKeys.put(PdfName.OC, one);
        widgetKeys.put(PdfName.H, one);
        widgetKeys.put(PdfName.MK, one);
        widgetKeys.put(PdfName.DA, one);
        widgetKeys.put(PdfName.Q, one);
        widgetKeys.put(PdfName.P, one);
        fieldKeys.put(PdfName.AA, one);
        fieldKeys.put(PdfName.FT, one);
        fieldKeys.put(PdfName.TU, one);
        fieldKeys.put(PdfName.TM, one);
        fieldKeys.put(PdfName.FF, one);
        fieldKeys.put(PdfName.V, one);
        fieldKeys.put(PdfName.DV, one);
        fieldKeys.put(PdfName.DS, one);
        fieldKeys.put(PdfName.RV, one);
        fieldKeys.put(PdfName.OPT, one);
        fieldKeys.put(PdfName.MAXLEN, one);
        fieldKeys.put(PdfName.TI, one);
        fieldKeys.put(PdfName.I, one);
        fieldKeys.put(PdfName.LOCK, one);
        fieldKeys.put(PdfName.SV, one);
    }

    protected Counter getCounter() {
        return this.COUNTER;
    }

    PdfCopyFieldsImp(OutputStream os) throws DocumentException {
        this(os, '\u0000');
    }

    PdfCopyFieldsImp(OutputStream os, char pdfVersion) throws DocumentException {
        super(new PdfDocument(), os);
        this.readers = new ArrayList();
        this.readers2intrefs = new HashMap();
        this.pages2intrefs = new HashMap();
        this.visited = new HashMap();
        this.fields = new ArrayList();
        this.fieldTree = new HashMap();
        this.pageRefs = new ArrayList();
        this.pageDics = new ArrayList();
        this.resources = new PdfDictionary();
        this.closing = false;
        this.calculationOrder = new ArrayList();
        this.needAppearances = false;
        this.mergedRadioButtons = new HashSet();
        this.COUNTER = CounterFactory.getCounter(PdfCopyFields.class);
        this.pdf.addWriter(this);
        if (pdfVersion != '\u0000') {
            super.setPdfVersion(pdfVersion);
        }
        this.nd = new Document();
        this.nd.addDocListener(this.pdf);
    }

    void addDocument(PdfReader reader, List<Integer> pagesToKeep) throws DocumentException, IOException {
        if (this.readers2intrefs.containsKey(reader) || !reader.isTampered()) {
            PdfReader reader2 = new PdfReader(reader);
            reader2.selectPages(pagesToKeep);
            if (reader2.getNumberOfPages() != 0) {
                reader2.setTampered(false);
                addDocument(reader2);
                return;
            }
            return;
        }
        throw new DocumentException(MessageLocalization.getComposedMessage("the.document.was.reused", new Object[0]));
    }

    void addDocument(PdfReader reader) throws DocumentException, IOException {
        boolean needapp = false;
        if (reader.isOpenedWithFullPermissions()) {
            openDoc();
            if (this.readers2intrefs.containsKey(reader)) {
                reader = new PdfReader(reader);
            } else if (reader.isTampered()) {
                throw new DocumentException(MessageLocalization.getComposedMessage("the.document.was.reused", new Object[0]));
            } else {
                reader.consolidateNamedDestinations();
                reader.setTampered(true);
            }
            reader.shuffleSubsetNames();
            this.readers2intrefs.put(reader, new IntHashtable());
            this.readers.add(reader);
            int len = reader.getNumberOfPages();
            IntHashtable refs = new IntHashtable();
            for (int p = 1; p <= len; p++) {
                refs.put(reader.getPageOrigRef(p).getNumber(), 1);
                reader.releasePage(p);
            }
            this.pages2intrefs.put(reader, refs);
            this.visited.put(reader, new IntHashtable());
            AcroFields acro = reader.getAcroFields();
            if (!acro.isGenerateAppearances()) {
                needapp = true;
            }
            if (needapp) {
                this.needAppearances = true;
            }
            this.fields.add(acro);
            updateCalculationOrder(reader);
            return;
        }
        throw new BadPasswordException(MessageLocalization.getComposedMessage("pdfreader.not.opened.with.owner.password", new Object[0]));
    }

    private static String getCOName(PdfReader reader, PRIndirectReference ref) {
        String name = "";
        while (ref != null) {
            PdfObject obj = PdfReader.getPdfObject(ref);
            if (obj == null || obj.type() != 6) {
                break;
            }
            PdfDictionary dic = (PdfDictionary) obj;
            PdfString t = dic.getAsString(PdfName.T);
            if (t != null) {
                name = t.toUnicodeString() + "." + name;
            }
            ref = (PRIndirectReference) dic.get(PdfName.PARENT);
        }
        if (name.endsWith(".")) {
            return name.substring(0, name.length() - 1);
        }
        return name;
    }

    protected void updateCalculationOrder(PdfReader reader) {
        PdfDictionary acro = reader.getCatalog().getAsDict(PdfName.ACROFORM);
        if (acro != null) {
            PdfArray co = acro.getAsArray(PdfName.CO);
            if (co != null && co.size() != 0) {
                AcroFields af = reader.getAcroFields();
                for (int k = 0; k < co.size(); k++) {
                    PdfObject obj = co.getPdfObject(k);
                    if (obj != null && obj.isIndirect()) {
                        String name = getCOName(reader, (PRIndirectReference) obj);
                        if (af.getFieldItem(name) != null) {
                            name = "." + name;
                            if (!this.calculationOrder.contains(name)) {
                                this.calculationOrder.add(name);
                            }
                        }
                    }
                }
            }
        }
    }

    void propagate(PdfObject obj, PdfIndirectReference refo, boolean restricted) throws IOException {
        if (obj != null && !(obj instanceof PdfIndirectReference)) {
            PdfObject ob;
            PRIndirectReference ind;
            switch (obj.type()) {
                case 5:
                    Iterator<PdfObject> it = ((PdfArray) obj).listIterator();
                    while (it.hasNext()) {
                        ob = (PdfObject) it.next();
                        if (ob == null || !ob.isIndirect()) {
                            propagate(ob, null, restricted);
                        } else {
                            ind = (PRIndirectReference) ob;
                            if (!(isVisited(ind) || isPage(ind))) {
                                propagate(PdfReader.getPdfObjectRelease(ind), getNewReference(ind), restricted);
                            }
                        }
                    }
                    return;
                case 6:
                case 7:
                    PdfDictionary dic = (PdfDictionary) obj;
                    for (PdfName key : dic.getKeys()) {
                        if (!(restricted && (key.equals(PdfName.PARENT) || key.equals(PdfName.KIDS)))) {
                            ob = dic.get(key);
                            if (ob == null || !ob.isIndirect()) {
                                propagate(ob, null, restricted);
                            } else {
                                ind = (PRIndirectReference) ob;
                                if (!(setVisited(ind) || isPage(ind))) {
                                    propagate(PdfReader.getPdfObjectRelease(ind), getNewReference(ind), restricted);
                                }
                            }
                        }
                    }
                    return;
                case 10:
                    throw new RuntimeException(MessageLocalization.getComposedMessage("reference.pointing.to.reference", new Object[0]));
                default:
                    return;
            }
        }
    }

    private void adjustTabOrder(PdfArray annots, PdfIndirectReference ind, PdfNumber nn) {
        int v = nn.intValue();
        ArrayList<Integer> t = (ArrayList) this.tabOrder.get(annots);
        int size;
        int k;
        if (t == null) {
            t = new ArrayList();
            size = annots.size() - 1;
            for (k = 0; k < size; k++) {
                t.add(zero);
            }
            t.add(Integer.valueOf(v));
            this.tabOrder.put(annots, t);
            annots.add((PdfObject) ind);
            return;
        }
        size = t.size() - 1;
        for (k = size; k >= 0; k--) {
            if (((Integer) t.get(k)).intValue() <= v) {
                t.add(k + 1, Integer.valueOf(v));
                annots.add(k + 1, ind);
                size = -2;
                break;
            }
        }
        if (size != -2) {
            t.add(0, Integer.valueOf(v));
            annots.add(0, ind);
        }
    }

    protected PdfArray branchForm(HashMap<String, Object> level, PdfIndirectReference parent, String fname) throws IOException {
        PdfArray arr = new PdfArray();
        for (Entry<String, Object> entry : level.entrySet()) {
            String name = (String) entry.getKey();
            ArrayList<Object> obj = entry.getValue();
            PdfObject ind = getPdfIndirectReference();
            PdfDictionary dic = new PdfDictionary();
            if (parent != null) {
                dic.put(PdfName.PARENT, parent);
            }
            dic.put(PdfName.T, new PdfString(name, PdfObject.TEXT_UNICODE));
            String fname2 = fname + "." + name;
            int coidx = this.calculationOrder.indexOf(fname2);
            if (coidx >= 0) {
                this.calculationOrderRefs.set(coidx, ind);
            }
            if (obj instanceof HashMap) {
                dic.put(PdfName.KIDS, branchForm((HashMap) obj, ind, fname2));
                arr.add(ind);
                addToBody((PdfObject) dic, (PdfIndirectReference) ind);
            } else {
                ArrayList<Object> list = obj;
                dic.mergeDifferent((PdfDictionary) list.get(0));
                PdfDictionary pageDic;
                PdfArray annots;
                PdfNumber nn;
                if (list.size() == 3) {
                    dic.mergeDifferent((PdfDictionary) list.get(2));
                    pageDic = (PdfDictionary) this.pageDics.get(((Integer) list.get(1)).intValue() - 1);
                    annots = pageDic.getAsArray(PdfName.ANNOTS);
                    if (annots == null) {
                        annots = new PdfArray();
                        pageDic.put(PdfName.ANNOTS, annots);
                    }
                    nn = (PdfNumber) dic.get(iTextTag);
                    dic.remove(iTextTag);
                    adjustTabOrder(annots, ind, nn);
                } else {
                    PdfDictionary field = (PdfDictionary) list.get(0);
                    PdfObject v = field.getAsName(PdfName.V);
                    PdfArray kids = new PdfArray();
                    for (int k = 1; k < list.size(); k += 2) {
                        pageDic = (PdfDictionary) this.pageDics.get(((Integer) list.get(k)).intValue() - 1);
                        annots = pageDic.getAsArray(PdfName.ANNOTS);
                        if (annots == null) {
                            annots = new PdfArray();
                            pageDic.put(PdfName.ANNOTS, annots);
                        }
                        PdfObject widget = new PdfDictionary();
                        widget.merge((PdfDictionary) list.get(k + 1));
                        widget.put(PdfName.PARENT, ind);
                        nn = (PdfNumber) widget.get(iTextTag);
                        widget.remove(iTextTag);
                        PdfName as;
                        if (PdfCopy.isCheckButton(field)) {
                            as = widget.getAsName(PdfName.AS);
                            if (!(v == null || as == null)) {
                                widget.put(PdfName.AS, v);
                            }
                        } else if (PdfCopy.isRadioButton(field)) {
                            as = widget.getAsName(PdfName.AS);
                            if (!(v == null || as == null || as.equals(getOffStateName(widget)))) {
                                if (this.mergedRadioButtons.contains(list)) {
                                    widget.put(PdfName.AS, getOffStateName(widget));
                                } else {
                                    this.mergedRadioButtons.add(list);
                                    widget.put(PdfName.AS, v);
                                }
                            }
                        }
                        PdfObject wref = addToBody(widget).getIndirectReference();
                        adjustTabOrder(annots, wref, nn);
                        kids.add(wref);
                        propagate(widget, null, false);
                    }
                    dic.put(PdfName.KIDS, kids);
                }
                arr.add(ind);
                addToBody((PdfObject) dic, (PdfIndirectReference) ind);
                propagate(dic, null, false);
            }
        }
        return arr;
    }

    protected PdfName getOffStateName(PdfDictionary widget) {
        return PdfName.Off;
    }

    protected void createAcroForms() throws IOException {
        if (!this.fieldTree.isEmpty()) {
            this.form = new PdfDictionary();
            this.form.put(PdfName.DR, this.resources);
            propagate(this.resources, null, false);
            if (this.needAppearances) {
                this.form.put(PdfName.NEEDAPPEARANCES, PdfBoolean.PDFTRUE);
            }
            this.form.put(PdfName.DA, new PdfString("/Helv 0 Tf 0 g "));
            this.tabOrder = new HashMap();
            this.calculationOrderRefs = new ArrayList(this.calculationOrder);
            this.form.put(PdfName.FIELDS, branchForm(this.fieldTree, null, ""));
            if (this.hasSignature) {
                this.form.put(PdfName.SIGFLAGS, new PdfNumber(3));
            }
            PdfArray co = new PdfArray();
            for (int k = 0; k < this.calculationOrderRefs.size(); k++) {
                Object obj = this.calculationOrderRefs.get(k);
                if (obj instanceof PdfIndirectReference) {
                    co.add((PdfIndirectReference) obj);
                }
            }
            if (co.size() > 0) {
                this.form.put(PdfName.CO, co);
            }
        }
    }

    public void close() {
        if (this.closing) {
            super.close();
            return;
        }
        this.closing = true;
        try {
            closeIt();
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    protected void closeIt() throws IOException {
        int k;
        int r;
        int page;
        for (k = 0; k < this.readers.size(); k++) {
            ((PdfReader) this.readers.get(k)).removeFields();
        }
        for (r = 0; r < this.readers.size(); r++) {
            PdfReader reader = (PdfReader) this.readers.get(r);
            for (page = 1; page <= reader.getNumberOfPages(); page++) {
                this.pageRefs.add(getNewReference(reader.getPageOrigRef(page)));
                this.pageDics.add(reader.getPageN(page));
            }
        }
        mergeFields();
        createAcroForms();
        for (r = 0; r < this.readers.size(); r++) {
            reader = (PdfReader) this.readers.get(r);
            for (page = 1; page <= reader.getNumberOfPages(); page++) {
                PdfDictionary dic = reader.getPageN(page);
                PdfIndirectReference pageRef = getNewReference(reader.getPageOrigRef(page));
                dic.put(PdfName.PARENT, this.root.addPageRef(pageRef));
                propagate(dic, pageRef, false);
            }
        }
        for (Entry<PdfReader, IntHashtable> entry : this.readers2intrefs.entrySet()) {
            reader = (PdfReader) entry.getKey();
            try {
                this.file = reader.getSafeFile();
                this.file.reOpen();
                IntHashtable t = (IntHashtable) entry.getValue();
                int[] keys = t.toOrderedKeys();
                for (k = 0; k < keys.length; k++) {
                    addToBody(PdfReader.getPdfObjectRelease(new PRIndirectReference(reader, keys[k])), t.get(keys[k]));
                }
            } finally {
                try {
                    this.file.close();
                } catch (Exception e) {
                }
            }
        }
        this.pdf.close();
    }

    void addPageOffsetToField(Map<String, Item> fd, int pageOffset) {
        if (pageOffset != 0) {
            for (Item item : fd.values()) {
                for (int k = 0; k < item.size(); k++) {
                    item.forcePage(k, item.getPage(k).intValue() + pageOffset);
                }
            }
        }
    }

    void createWidgets(ArrayList<Object> list, Item item) {
        for (int k = 0; k < item.size(); k++) {
            list.add(item.getPage(k));
            PdfDictionary merged = item.getMerged(k);
            PdfObject dr = merged.get(PdfName.DR);
            if (dr != null) {
                PdfFormField.mergeResources(this.resources, (PdfDictionary) PdfReader.getPdfObject(dr));
            }
            PdfDictionary widget = new PdfDictionary();
            for (PdfName key : merged.getKeys()) {
                if (widgetKeys.containsKey(key)) {
                    widget.put(key, merged.get(key));
                }
            }
            widget.put(iTextTag, new PdfNumber(item.getTabOrder(k).intValue() + 1));
            list.add(widget);
        }
    }

    void mergeField(String name, Item item) {
        HashMap<String, Object> map = this.fieldTree;
        StringTokenizer tk = new StringTokenizer(name, ".");
        if (tk.hasMoreTokens()) {
            String s;
            HashMap<String, Object> obj;
            while (true) {
                s = tk.nextToken();
                obj = map.get(s);
                if (!tk.hasMoreTokens()) {
                    break;
                } else if (obj == null) {
                    obj = new HashMap();
                    map.put(s, obj);
                    map = obj;
                } else if (obj instanceof HashMap) {
                    map = obj;
                } else {
                    return;
                }
            }
            if (!(obj instanceof HashMap)) {
                PdfDictionary merged = item.getMerged(0);
                PdfDictionary field;
                ArrayList<Object> list;
                if (obj == null) {
                    field = new PdfDictionary();
                    if (PdfName.SIG.equals(merged.get(PdfName.FT))) {
                        this.hasSignature = true;
                    }
                    for (PdfName key : merged.getKeys()) {
                        if (fieldKeys.containsKey(key)) {
                            field.put(key, merged.get(key));
                        }
                    }
                    list = new ArrayList();
                    list.add(field);
                    createWidgets(list, item);
                    map.put(s, list);
                    return;
                }
                list = (ArrayList) obj;
                field = (PdfDictionary) list.get(0);
                PdfName type1 = (PdfName) field.get(PdfName.FT);
                PdfName type2 = (PdfName) merged.get(PdfName.FT);
                if (type1 != null && type1.equals(type2)) {
                    int flag1 = 0;
                    PdfObject f1 = field.get(PdfName.FF);
                    if (f1 != null && f1.isNumber()) {
                        flag1 = ((PdfNumber) f1).intValue();
                    }
                    int flag2 = 0;
                    PdfObject f2 = merged.get(PdfName.FF);
                    if (f2 != null && f2.isNumber()) {
                        flag2 = ((PdfNumber) f2).intValue();
                    }
                    if (type1.equals(PdfName.BTN)) {
                        if (((flag1 ^ flag2) & 65536) != 0) {
                            return;
                        }
                        if ((65536 & flag1) == 0 && ((flag1 ^ flag2) & 32768) != 0) {
                            return;
                        }
                    } else if (type1.equals(PdfName.CH) && ((flag1 ^ flag2) & 131072) != 0) {
                        return;
                    }
                    createWidgets(list, item);
                }
            }
        }
    }

    void mergeWithMaster(Map<String, Item> fd) {
        for (Entry<String, Item> entry : fd.entrySet()) {
            mergeField((String) entry.getKey(), (Item) entry.getValue());
        }
    }

    void mergeFields() {
        int pageOffset = 0;
        for (int k = 0; k < this.fields.size(); k++) {
            Map<String, Item> fd = ((AcroFields) this.fields.get(k)).getFields();
            addPageOffsetToField(fd, pageOffset);
            mergeWithMaster(fd);
            pageOffset += ((PdfReader) this.readers.get(k)).getNumberOfPages();
        }
    }

    public PdfIndirectReference getPageReference(int page) {
        return (PdfIndirectReference) this.pageRefs.get(page - 1);
    }

    protected PdfDictionary getCatalog(PdfIndirectReference rootObj) {
        try {
            PdfDictionary cat = this.pdf.getCatalog(rootObj);
            if (this.form != null) {
                cat.put(PdfName.ACROFORM, addToBody(this.form).getIndirectReference());
            }
            return cat;
        } catch (IOException e) {
            throw new ExceptionConverter(e);
        }
    }

    protected PdfIndirectReference getNewReference(PRIndirectReference ref) {
        return new PdfIndirectReference(0, getNewObjectNumber(ref.getReader(), ref.getNumber(), 0));
    }

    protected int getNewObjectNumber(PdfReader reader, int number, int generation) {
        IntHashtable refs = (IntHashtable) this.readers2intrefs.get(reader);
        int n = refs.get(number);
        if (n != 0) {
            return n;
        }
        n = getIndirectReferenceNumber();
        refs.put(number, n);
        return n;
    }

    protected boolean setVisited(PRIndirectReference ref) {
        IntHashtable refs = (IntHashtable) this.visited.get(ref.getReader());
        if (refs == null) {
            return false;
        }
        if (refs.put(ref.getNumber(), 1) != 0) {
            return true;
        }
        return false;
    }

    protected boolean isVisited(PRIndirectReference ref) {
        IntHashtable refs = (IntHashtable) this.visited.get(ref.getReader());
        if (refs != null) {
            return refs.containsKey(ref.getNumber());
        }
        return false;
    }

    protected boolean isVisited(PdfReader reader, int number, int generation) {
        return ((IntHashtable) this.readers2intrefs.get(reader)).containsKey(number);
    }

    protected boolean isPage(PRIndirectReference ref) {
        IntHashtable refs = (IntHashtable) this.pages2intrefs.get(ref.getReader());
        if (refs != null) {
            return refs.containsKey(ref.getNumber());
        }
        return false;
    }

    RandomAccessFileOrArray getReaderFile(PdfReader reader) {
        return this.file;
    }

    public void openDoc() {
        if (!this.nd.isOpen()) {
            this.nd.open();
        }
    }
}

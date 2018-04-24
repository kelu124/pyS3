package com.itextpdf.text.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.html.HtmlTags;
import com.itextpdf.text.io.RASInputStream;
import com.itextpdf.text.io.RandomAccessSourceFactory;
import com.itextpdf.text.io.WindowRandomAccessSource;
import com.itextpdf.text.pdf.PRTokeniser.TokenType;
import com.itextpdf.text.pdf.XfaForm.Xml2Som;
import com.itextpdf.text.pdf.XfaForm.Xml2SomDatasets;
import com.itextpdf.text.pdf.codec.Base64;
import com.itextpdf.text.pdf.security.PdfPKCS7;
import com.itextpdf.text.xml.XmlToTxt;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.w3c.dom.Node;

public class AcroFields {
    public static final int DA_COLOR = 2;
    public static final int DA_FONT = 0;
    public static final int DA_SIZE = 1;
    public static final int FIELD_TYPE_CHECKBOX = 2;
    public static final int FIELD_TYPE_COMBO = 6;
    public static final int FIELD_TYPE_LIST = 5;
    public static final int FIELD_TYPE_NONE = 0;
    public static final int FIELD_TYPE_PUSHBUTTON = 1;
    public static final int FIELD_TYPE_RADIOBUTTON = 3;
    public static final int FIELD_TYPE_SIGNATURE = 7;
    public static final int FIELD_TYPE_TEXT = 4;
    private static final PdfName[] buttonRemove = new PdfName[]{PdfName.MK, PdfName.f122F, PdfName.FF, PdfName.f131Q, PdfName.BS, PdfName.BORDER};
    private static final HashMap<String, String[]> stdFieldFontNames = new HashMap();
    private boolean append;
    private HashMap<Integer, BaseFont> extensionFonts = new HashMap();
    private float extraMarginLeft;
    private float extraMarginTop;
    private Map<String, TextField> fieldCache;
    Map<String, Item> fields;
    private boolean generateAppearances = true;
    private boolean lastWasString;
    private HashMap<String, BaseFont> localFonts = new HashMap();
    private ArrayList<String> orderedSignatureNames;
    PdfReader reader;
    private HashMap<String, int[]> sigNames;
    private ArrayList<BaseFont> substitutionFonts;
    private int topFirst;
    private int totalRevisions;
    PdfWriter writer;
    private XfaForm xfa;

    public static class FieldPosition {
        public int page;
        public Rectangle position;
    }

    private static class InstHit {
        IntHashtable hits;

        public InstHit(int[] inst) {
            if (inst != null) {
                this.hits = new IntHashtable();
                for (int put : inst) {
                    this.hits.put(put, 1);
                }
            }
        }

        public boolean isHit(int n) {
            if (this.hits == null) {
                return true;
            }
            return this.hits.containsKey(n);
        }
    }

    public static class Item {
        public static final int WRITE_MERGED = 1;
        public static final int WRITE_VALUE = 4;
        public static final int WRITE_WIDGET = 2;
        protected ArrayList<PdfDictionary> merged = new ArrayList();
        protected ArrayList<Integer> page = new ArrayList();
        protected ArrayList<Integer> tabOrder = new ArrayList();
        protected ArrayList<PdfDictionary> values = new ArrayList();
        protected ArrayList<PdfIndirectReference> widget_refs = new ArrayList();
        protected ArrayList<PdfDictionary> widgets = new ArrayList();

        public void writeToAll(PdfName key, PdfObject value, int writeFlags) {
            int i;
            if ((writeFlags & 1) != 0) {
                for (i = 0; i < this.merged.size(); i++) {
                    getMerged(i).put(key, value);
                }
            }
            if ((writeFlags & 2) != 0) {
                for (i = 0; i < this.widgets.size(); i++) {
                    getWidget(i).put(key, value);
                }
            }
            if ((writeFlags & 4) != 0) {
                for (i = 0; i < this.values.size(); i++) {
                    getValue(i).put(key, value);
                }
            }
        }

        public void markUsed(AcroFields parentFields, int writeFlags) {
            int i;
            if ((writeFlags & 4) != 0) {
                for (i = 0; i < size(); i++) {
                    parentFields.markUsed(getValue(i));
                }
            }
            if ((writeFlags & 2) != 0) {
                for (i = 0; i < size(); i++) {
                    parentFields.markUsed(getWidget(i));
                }
            }
        }

        public int size() {
            return this.values.size();
        }

        void remove(int killIdx) {
            this.values.remove(killIdx);
            this.widgets.remove(killIdx);
            this.widget_refs.remove(killIdx);
            this.merged.remove(killIdx);
            this.page.remove(killIdx);
            this.tabOrder.remove(killIdx);
        }

        public PdfDictionary getValue(int idx) {
            return (PdfDictionary) this.values.get(idx);
        }

        void addValue(PdfDictionary value) {
            this.values.add(value);
        }

        public PdfDictionary getWidget(int idx) {
            return (PdfDictionary) this.widgets.get(idx);
        }

        void addWidget(PdfDictionary widget) {
            this.widgets.add(widget);
        }

        public PdfIndirectReference getWidgetRef(int idx) {
            return (PdfIndirectReference) this.widget_refs.get(idx);
        }

        void addWidgetRef(PdfIndirectReference widgRef) {
            this.widget_refs.add(widgRef);
        }

        public PdfDictionary getMerged(int idx) {
            return (PdfDictionary) this.merged.get(idx);
        }

        void addMerged(PdfDictionary mergeDict) {
            this.merged.add(mergeDict);
        }

        public Integer getPage(int idx) {
            return (Integer) this.page.get(idx);
        }

        void addPage(int pg) {
            this.page.add(Integer.valueOf(pg));
        }

        void forcePage(int idx, int pg) {
            this.page.set(idx, Integer.valueOf(pg));
        }

        public Integer getTabOrder(int idx) {
            return (Integer) this.tabOrder.get(idx);
        }

        void addTabOrder(int order) {
            this.tabOrder.add(Integer.valueOf(order));
        }
    }

    private static class SorterComparator implements Comparator<Object[]> {
        private SorterComparator() {
        }

        public int compare(Object[] o1, Object[] o2) {
            return ((int[]) o1[1])[0] - ((int[]) o2[1])[0];
        }
    }

    AcroFields(PdfReader reader, PdfWriter writer) {
        this.reader = reader;
        this.writer = writer;
        try {
            this.xfa = new XfaForm(reader);
            if (writer instanceof PdfStamperImp) {
                this.append = ((PdfStamperImp) writer).isAppend();
            }
            fill();
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    void fill() {
        this.fields = new HashMap();
        PdfDictionary top = (PdfDictionary) PdfReader.getPdfObjectRelease(this.reader.getCatalog().get(PdfName.ACROFORM));
        if (top != null) {
            PdfBoolean needappearances = top.getAsBoolean(PdfName.NEEDAPPEARANCES);
            if (needappearances == null || !needappearances.booleanValue()) {
                setGenerateAppearances(true);
            } else {
                setGenerateAppearances(false);
            }
            PdfArray arrfds = (PdfArray) PdfReader.getPdfObjectRelease(top.get(PdfName.FIELDS));
            if (arrfds != null && arrfds.size() != 0) {
                int j;
                PdfDictionary annot;
                PdfDictionary dic;
                String name;
                PdfString t;
                Item item;
                for (int k = 1; k <= this.reader.getNumberOfPages(); k++) {
                    PdfDictionary page = this.reader.getPageNRelease(k);
                    PdfArray annots = (PdfArray) PdfReader.getPdfObjectRelease(page.get(PdfName.ANNOTS), page);
                    if (annots != null) {
                        for (j = 0; j < annots.size(); j++) {
                            annot = annots.getAsDict(j);
                            if (annot == null) {
                                PdfReader.releaseLastXrefPartial(annots.getAsIndirectObject(j));
                            } else if (PdfName.WIDGET.equals(annot.getAsName(PdfName.SUBTYPE))) {
                                PdfDictionary widget = annot;
                                dic = new PdfDictionary();
                                dic.putAll(annot);
                                name = "";
                                PdfDictionary value = null;
                                PdfObject lastV = null;
                                while (annot != null) {
                                    dic.mergeDifferent(annot);
                                    t = annot.getAsString(PdfName.f134T);
                                    if (t != null) {
                                        name = t.toUnicodeString() + "." + name;
                                    }
                                    if (lastV == null && annot.get(PdfName.f136V) != null) {
                                        lastV = PdfReader.getPdfObjectRelease(annot.get(PdfName.f136V));
                                    }
                                    if (value == null && t != null) {
                                        value = annot;
                                        if (annot.get(PdfName.f136V) == null && lastV != null) {
                                            value.put(PdfName.f136V, lastV);
                                        }
                                    }
                                    annot = annot.getAsDict(PdfName.PARENT);
                                }
                                if (name.length() > 0) {
                                    name = name.substring(0, name.length() - 1);
                                }
                                item = (Item) this.fields.get(name);
                                if (item == null) {
                                    item = new Item();
                                    this.fields.put(name, item);
                                }
                                if (value == null) {
                                    item.addValue(widget);
                                } else {
                                    item.addValue(value);
                                }
                                item.addWidget(widget);
                                item.addWidgetRef(annots.getAsIndirectObject(j));
                                if (top != null) {
                                    dic.mergeDifferent(top);
                                }
                                item.addMerged(dic);
                                item.addPage(k);
                                item.addTabOrder(j);
                            } else {
                                PdfReader.releaseLastXrefPartial(annots.getAsIndirectObject(j));
                            }
                        }
                    }
                }
                PdfNumber sigFlags = top.getAsNumber(PdfName.SIGFLAGS);
                if (sigFlags != null && (sigFlags.intValue() & 1) == 1) {
                    for (j = 0; j < arrfds.size(); j++) {
                        annot = arrfds.getAsDict(j);
                        if (annot == null) {
                            PdfReader.releaseLastXrefPartial(arrfds.getAsIndirectObject(j));
                        } else if (!PdfName.WIDGET.equals(annot.getAsName(PdfName.SUBTYPE))) {
                            PdfReader.releaseLastXrefPartial(arrfds.getAsIndirectObject(j));
                        } else if (((PdfArray) PdfReader.getPdfObjectRelease(annot.get(PdfName.KIDS))) == null) {
                            dic = new PdfDictionary();
                            dic.putAll(annot);
                            t = annot.getAsString(PdfName.f134T);
                            if (t != null) {
                                name = t.toUnicodeString();
                                if (!this.fields.containsKey(name)) {
                                    item = new Item();
                                    this.fields.put(name, item);
                                    item.addValue(dic);
                                    item.addWidget(dic);
                                    item.addWidgetRef(arrfds.getAsIndirectObject(j));
                                    item.addMerged(dic);
                                    item.addPage(-1);
                                    item.addTabOrder(-1);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public String[] getAppearanceStates(String fieldName) {
        Item fd = (Item) this.fields.get(fieldName);
        if (fd == null) {
            return null;
        }
        int k;
        HashSet<String> names = new LinkedHashSet();
        PdfDictionary vals = fd.getValue(0);
        PdfString stringOpt = vals.getAsString(PdfName.OPT);
        if (stringOpt != null) {
            names.add(stringOpt.toUnicodeString());
        } else {
            PdfArray arrayOpt = vals.getAsArray(PdfName.OPT);
            if (arrayOpt != null) {
                for (k = 0; k < arrayOpt.size(); k++) {
                    PdfObject pdfObject = arrayOpt.getDirectObject(k);
                    PdfString valStr = null;
                    switch (pdfObject.type()) {
                        case 3:
                            valStr = (PdfString) pdfObject;
                            break;
                        case 5:
                            valStr = ((PdfArray) pdfObject).getAsString(1);
                            break;
                    }
                    if (valStr != null) {
                        names.add(valStr.toUnicodeString());
                    }
                }
            }
        }
        for (k = 0; k < fd.size(); k++) {
            PdfDictionary dic = fd.getWidget(k).getAsDict(PdfName.AP);
            if (dic != null) {
                dic = dic.getAsDict(PdfName.f128N);
                if (dic != null) {
                    for (PdfName element : dic.getKeys()) {
                        names.add(PdfName.decodeName(element.toString()));
                    }
                }
            }
        }
        return (String[]) names.toArray(new String[names.size()]);
    }

    private String[] getListOption(String fieldName, int idx) {
        String[] strArr = null;
        Item fd = getFieldItem(fieldName);
        if (fd != null) {
            PdfArray ar = fd.getMerged(0).getAsArray(PdfName.OPT);
            if (ar != null) {
                strArr = new String[ar.size()];
                for (int k = 0; k < ar.size(); k++) {
                    PdfObject obj = ar.getDirectObject(k);
                    try {
                        if (obj.isArray()) {
                            obj = ((PdfArray) obj).getDirectObject(idx);
                        }
                        if (obj.isString()) {
                            strArr[k] = ((PdfString) obj).toUnicodeString();
                        } else {
                            strArr[k] = obj.toString();
                        }
                    } catch (Exception e) {
                        strArr[k] = "";
                    }
                }
            }
        }
        return strArr;
    }

    public String[] getListOptionExport(String fieldName) {
        return getListOption(fieldName, 0);
    }

    public String[] getListOptionDisplay(String fieldName) {
        return getListOption(fieldName, 1);
    }

    public boolean setListOption(String fieldName, String[] exportValues, String[] displayValues) {
        if (exportValues == null && displayValues == null) {
            return false;
        }
        if (exportValues == null || displayValues == null || exportValues.length == displayValues.length) {
            int ftype = getFieldType(fieldName);
            if (ftype != 6 && ftype != 5) {
                return false;
            }
            Item fd = (Item) this.fields.get(fieldName);
            String[] sing = null;
            if (exportValues == null && displayValues != null) {
                sing = displayValues;
            } else if (exportValues != null && displayValues == null) {
                sing = exportValues;
            }
            PdfArray opt = new PdfArray();
            int k;
            if (sing != null) {
                for (String pdfString : sing) {
                    opt.add(new PdfString(pdfString, PdfObject.TEXT_UNICODE));
                }
            } else {
                for (k = 0; k < exportValues.length; k++) {
                    PdfArray a = new PdfArray();
                    a.add(new PdfString(exportValues[k], PdfObject.TEXT_UNICODE));
                    a.add(new PdfString(displayValues[k], PdfObject.TEXT_UNICODE));
                    opt.add(a);
                }
            }
            fd.writeToAll(PdfName.OPT, opt, 5);
            return true;
        }
        throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.export.and.the.display.array.must.have.the.same.size", new Object[0]));
    }

    public int getFieldType(String fieldName) {
        Item fd = getFieldItem(fieldName);
        if (fd == null) {
            return 0;
        }
        PdfDictionary merged = fd.getMerged(0);
        PdfName type = merged.getAsName(PdfName.FT);
        if (type == null) {
            return 0;
        }
        int ff = 0;
        PdfNumber ffo = merged.getAsNumber(PdfName.FF);
        if (ffo != null) {
            ff = ffo.intValue();
        }
        if (PdfName.BTN.equals(type)) {
            if ((65536 & ff) != 0) {
                return 1;
            }
            if ((32768 & ff) != 0) {
                return 3;
            }
            return 2;
        } else if (PdfName.TX.equals(type)) {
            return 4;
        } else {
            if (PdfName.CH.equals(type)) {
                if ((131072 & ff) != 0) {
                    return 6;
                }
                return 5;
            } else if (PdfName.SIG.equals(type)) {
                return 7;
            } else {
                return 0;
            }
        }
    }

    public void exportAsFdf(FdfWriter writer) {
        for (Entry<String, Item> entry : this.fields.entrySet()) {
            String name = (String) entry.getKey();
            if (((Item) entry.getValue()).getMerged(0).get(PdfName.f136V) != null) {
                String value = getField(name);
                if (this.lastWasString) {
                    writer.setFieldAsString(name, value);
                } else {
                    writer.setFieldAsName(name, value);
                }
            }
        }
    }

    public boolean renameField(String oldName, String newName) {
        int idx1 = oldName.lastIndexOf(46) + 1;
        int idx2 = newName.lastIndexOf(46) + 1;
        if (idx1 != idx2 || !oldName.substring(0, idx1).equals(newName.substring(0, idx2)) || this.fields.containsKey(newName)) {
            return false;
        }
        Item item = (Item) this.fields.get(oldName);
        if (item == null) {
            return false;
        }
        newName = newName.substring(idx2);
        item.writeToAll(PdfName.f134T, new PdfString(newName, PdfObject.TEXT_UNICODE), 5);
        item.markUsed(this, 4);
        this.fields.remove(oldName);
        this.fields.put(newName, item);
        return true;
    }

    public static Object[] splitDAelements(String da) {
        try {
            PRTokeniser tk = new PRTokeniser(new RandomAccessFileOrArray(new RandomAccessSourceFactory().createSource(PdfEncodings.convertToBytes(da, null))));
            ArrayList<String> stack = new ArrayList();
            Object[] ret = new Object[3];
            while (tk.nextToken()) {
                if (tk.getTokenType() != TokenType.COMMENT) {
                    if (tk.getTokenType() == TokenType.OTHER) {
                        String operator = tk.getStringValue();
                        if (operator.equals("Tf")) {
                            if (stack.size() >= 2) {
                                ret[0] = stack.get(stack.size() - 2);
                                ret[1] = new Float((String) stack.get(stack.size() - 1));
                            }
                        } else if (operator.equals("g")) {
                            if (stack.size() >= 1) {
                                float gray = new Float((String) stack.get(stack.size() - 1)).floatValue();
                                if (gray != 0.0f) {
                                    ret[2] = new GrayColor(gray);
                                }
                            }
                        } else if (operator.equals("rg")) {
                            if (stack.size() >= 3) {
                                ret[2] = new BaseColor(new Float((String) stack.get(stack.size() - 3)).floatValue(), new Float((String) stack.get(stack.size() - 2)).floatValue(), new Float((String) stack.get(stack.size() - 1)).floatValue());
                            }
                        } else if (operator.equals("k") && stack.size() >= 4) {
                            ret[2] = new CMYKColor(new Float((String) stack.get(stack.size() - 4)).floatValue(), new Float((String) stack.get(stack.size() - 3)).floatValue(), new Float((String) stack.get(stack.size() - 2)).floatValue(), new Float((String) stack.get(stack.size() - 1)).floatValue());
                        }
                        stack.clear();
                    } else {
                        stack.add(tk.getStringValue());
                    }
                }
            }
            return ret;
        } catch (IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
    }

    public void decodeGenericDictionary(PdfDictionary merged, BaseField tx) throws IOException, DocumentException {
        int flags;
        PdfString da = merged.getAsString(PdfName.DA);
        if (da != null) {
            Object[] dab = splitDAelements(da.toUnicodeString());
            if (dab[1] != null) {
                tx.setFontSize(((Float) dab[1]).floatValue());
            }
            if (dab[2] != null) {
                tx.setTextColor((BaseColor) dab[2]);
            }
            if (dab[0] != null) {
                PdfDictionary font = merged.getAsDict(PdfName.DR);
                if (font != null) {
                    font = font.getAsDict(PdfName.FONT);
                    if (font != null) {
                        PdfObject po = font.get(new PdfName((String) dab[0]));
                        if (po == null || po.type() != 10) {
                            BaseFont bf = (BaseFont) this.localFonts.get(dab[0]);
                            if (bf == null) {
                                String[] fn = (String[]) stdFieldFontNames.get(dab[0]);
                                if (fn != null) {
                                    try {
                                        String enc = "winansi";
                                        if (fn.length > 1) {
                                            enc = fn[1];
                                        }
                                        tx.setFont(BaseFont.createFont(fn[0], enc, false));
                                    } catch (Exception e) {
                                    }
                                }
                            } else {
                                tx.setFont(bf);
                            }
                        } else {
                            PRIndirectReference por = (PRIndirectReference) po;
                            tx.setFont(new DocumentFont((PRIndirectReference) po));
                            Integer porkey = Integer.valueOf(por.getNumber());
                            BaseFont porf = (BaseFont) this.extensionFonts.get(porkey);
                            if (porf == null && !this.extensionFonts.containsKey(porkey)) {
                                PdfDictionary fd = ((PdfDictionary) PdfReader.getPdfObject(po)).getAsDict(PdfName.FONTDESCRIPTOR);
                                if (fd != null) {
                                    PRStream prs = (PRStream) PdfReader.getPdfObject(fd.get(PdfName.FONTFILE2));
                                    if (prs == null) {
                                        prs = (PRStream) PdfReader.getPdfObject(fd.get(PdfName.FONTFILE3));
                                    }
                                    if (prs == null) {
                                        this.extensionFonts.put(porkey, null);
                                    } else {
                                        try {
                                            porf = BaseFont.createFont("font.ttf", BaseFont.IDENTITY_H, true, false, PdfReader.getStreamBytes(prs), null);
                                        } catch (Exception e2) {
                                        }
                                        this.extensionFonts.put(porkey, porf);
                                    }
                                }
                            }
                            if (tx instanceof TextField) {
                                ((TextField) tx).setExtensionFont(porf);
                            }
                        }
                    }
                }
            }
        }
        PdfDictionary mk = merged.getAsDict(PdfName.MK);
        if (mk != null) {
            BaseColor border = getMKColor(mk.getAsArray(PdfName.BC));
            tx.setBorderColor(border);
            if (border != null) {
                tx.setBorderWidth(BaseField.BORDER_WIDTH_THIN);
            }
            tx.setBackgroundColor(getMKColor(mk.getAsArray(PdfName.BG)));
            PdfNumber rotation = mk.getAsNumber(PdfName.f132R);
            if (rotation != null) {
                tx.setRotation(rotation.intValue());
            }
        }
        PdfNumber nfl = merged.getAsNumber(PdfName.f122F);
        tx.setVisibility(2);
        if (nfl != null) {
            flags = nfl.intValue();
            if ((flags & 4) != 0 && (flags & 2) != 0) {
                tx.setVisibility(1);
            } else if ((flags & 4) != 0 && (flags & 32) != 0) {
                tx.setVisibility(3);
            } else if ((flags & 4) != 0) {
                tx.setVisibility(0);
            }
        }
        nfl = merged.getAsNumber(PdfName.FF);
        flags = 0;
        if (nfl != null) {
            flags = nfl.intValue();
        }
        tx.setOptions(flags);
        if ((16777216 & flags) != 0) {
            PdfNumber maxLen = merged.getAsNumber(PdfName.MAXLEN);
            int len = 0;
            if (maxLen != null) {
                len = maxLen.intValue();
            }
            tx.setMaxCharacterLength(len);
        }
        nfl = merged.getAsNumber(PdfName.f131Q);
        if (nfl != null) {
            if (nfl.intValue() == 1) {
                tx.setAlignment(1);
            } else if (nfl.intValue() == 2) {
                tx.setAlignment(2);
            }
        }
        PdfDictionary bs = merged.getAsDict(PdfName.BS);
        if (bs != null) {
            PdfNumber w = bs.getAsNumber(PdfName.f137W);
            if (w != null) {
                tx.setBorderWidth(w.floatValue());
            }
            PdfName s = bs.getAsName(PdfName.f133S);
            if (PdfName.f120D.equals(s)) {
                tx.setBorderStyle(1);
                return;
            } else if (PdfName.f118B.equals(s)) {
                tx.setBorderStyle(2);
                return;
            } else if (PdfName.f124I.equals(s)) {
                tx.setBorderStyle(3);
                return;
            } else if (PdfName.f135U.equals(s)) {
                tx.setBorderStyle(4);
                return;
            } else {
                return;
            }
        }
        PdfArray bd = merged.getAsArray(PdfName.BORDER);
        if (bd != null) {
            if (bd.size() >= 3) {
                tx.setBorderWidth(bd.getAsNumber(2).floatValue());
            }
            if (bd.size() >= 4) {
                tx.setBorderStyle(1);
            }
        }
    }

    PdfAppearance getAppearance(PdfDictionary merged, String[] values, String fieldName) throws IOException, DocumentException {
        this.topFirst = 0;
        String text = values.length > 0 ? values[0] : null;
        if (this.fieldCache == null || !this.fieldCache.containsKey(fieldName)) {
            TextField textField = new TextField(this.writer, null, null);
            textField.setExtraMargin(this.extraMarginLeft, this.extraMarginTop);
            textField.setBorderWidth(0.0f);
            textField.setSubstitutionFonts(this.substitutionFonts);
            decodeGenericDictionary(merged, textField);
            Rectangle box = PdfReader.getNormalizedRectangle(merged.getAsArray(PdfName.RECT));
            if (textField.getRotation() == 90 || textField.getRotation() == 270) {
                box = box.rotate();
            }
            textField.setBox(box);
            if (this.fieldCache != null) {
                this.fieldCache.put(fieldName, textField);
            }
        } else {
            TextField tx = (TextField) this.fieldCache.get(fieldName);
            tx.setWriter(this.writer);
        }
        PdfName fieldType = merged.getAsName(PdfName.FT);
        if (PdfName.TX.equals(fieldType)) {
            if (values.length > 0 && values[0] != null) {
                tx.setText(values[0]);
            }
            return tx.getAppearance();
        } else if (PdfName.CH.equals(fieldType)) {
            PdfArray opt = merged.getAsArray(PdfName.OPT);
            int flags = 0;
            PdfNumber nfl = merged.getAsNumber(PdfName.FF);
            if (nfl != null) {
                flags = nfl.intValue();
            }
            if ((131072 & flags) == 0 || opt != null) {
                if (opt != null) {
                    int k;
                    String[] choices = new String[opt.size()];
                    String[] choicesExp = new String[opt.size()];
                    for (k = 0; k < opt.size(); k++) {
                        PdfObject obj = opt.getPdfObject(k);
                        if (obj.isString()) {
                            String toUnicodeString = ((PdfString) obj).toUnicodeString();
                            choicesExp[k] = toUnicodeString;
                            choices[k] = toUnicodeString;
                        } else {
                            PdfArray a = (PdfArray) obj;
                            choicesExp[k] = a.getAsString(0).toUnicodeString();
                            choices[k] = a.getAsString(1).toUnicodeString();
                        }
                    }
                    if ((131072 & flags) != 0) {
                        for (k = 0; k < choices.length; k++) {
                            if (text.equals(choicesExp[k])) {
                                text = choices[k];
                                break;
                            }
                        }
                        tx.setText(text);
                        return tx.getAppearance();
                    }
                    ArrayList<Integer> indexes = new ArrayList();
                    k = 0;
                    while (k < choicesExp.length) {
                        for (String val : values) {
                            if (val != null && val.equals(choicesExp[k])) {
                                indexes.add(Integer.valueOf(k));
                                break;
                            }
                        }
                        k++;
                    }
                    tx.setChoices(choices);
                    tx.setChoiceExports(choicesExp);
                    tx.setChoiceSelections(indexes);
                }
                PdfAppearance app = tx.getListAppearance();
                this.topFirst = tx.getTopFirst();
                return app;
            }
            tx.setText(text);
            return tx.getAppearance();
        } else {
            throw new DocumentException(MessageLocalization.getComposedMessage("an.appearance.was.requested.without.a.variable.text.field", new Object[0]));
        }
    }

    PdfAppearance getAppearance(PdfDictionary merged, String text, String fieldName) throws IOException, DocumentException {
        return getAppearance(merged, new String[]{text}, fieldName);
    }

    BaseColor getMKColor(PdfArray ar) {
        if (ar == null) {
            return null;
        }
        switch (ar.size()) {
            case 1:
                return new GrayColor(ar.getAsNumber(0).floatValue());
            case 3:
                return new BaseColor(ExtendedColor.normalize(ar.getAsNumber(0).floatValue()), ExtendedColor.normalize(ar.getAsNumber(1).floatValue()), ExtendedColor.normalize(ar.getAsNumber(2).floatValue()));
            case 4:
                return new CMYKColor(ar.getAsNumber(0).floatValue(), ar.getAsNumber(1).floatValue(), ar.getAsNumber(2).floatValue(), ar.getAsNumber(3).floatValue());
            default:
                return null;
        }
    }

    public String getFieldRichValue(String name) {
        if (this.xfa.isXfaPresent()) {
            return null;
        }
        Item item = (Item) this.fields.get(name);
        if (item == null) {
            return null;
        }
        PdfString rich = item.getMerged(0).getAsString(PdfName.RV);
        if (rich != null) {
            return rich.toString();
        }
        return null;
    }

    public String getField(String name) {
        if (this.xfa.isXfaPresent()) {
            name = this.xfa.findFieldName(name, this);
            if (name == null) {
                return null;
            }
            return XfaForm.getNodeText(this.xfa.findDatasetsNode(Xml2Som.getShortName(name)));
        }
        Item item = (Item) this.fields.get(name);
        if (item == null) {
            return null;
        }
        this.lastWasString = false;
        PdfDictionary mergedDict = item.getMerged(0);
        PdfObject v = PdfReader.getPdfObject(mergedDict.get(PdfName.f136V));
        if (v == null) {
            return "";
        }
        if (v instanceof PRStream) {
            try {
                return new String(PdfReader.getStreamBytes((PRStream) v));
            } catch (IOException e) {
                throw new ExceptionConverter(e);
            }
        }
        if (PdfName.BTN.equals(mergedDict.getAsName(PdfName.FT))) {
            PdfNumber ff = mergedDict.getAsNumber(PdfName.FF);
            int flags = 0;
            if (ff != null) {
                flags = ff.intValue();
            }
            if ((65536 & flags) != 0) {
                return "";
            }
            String value = "";
            if (v instanceof PdfName) {
                value = PdfName.decodeName(v.toString());
            } else if (v instanceof PdfString) {
                value = ((PdfString) v).toUnicodeString();
            }
            PdfArray opts = item.getValue(0).getAsArray(PdfName.OPT);
            if (opts == null) {
                return value;
            }
            try {
                value = opts.getAsString(Integer.parseInt(value)).toUnicodeString();
                this.lastWasString = true;
                return value;
            } catch (Exception e2) {
                return value;
            }
        } else if (v instanceof PdfString) {
            this.lastWasString = true;
            return ((PdfString) v).toUnicodeString();
        } else if (v instanceof PdfName) {
            return PdfName.decodeName(v.toString());
        } else {
            return "";
        }
    }

    public String[] getListSelection(String name) {
        String[] ret = getField(name) == null ? new String[0] : new String[]{getField(name)};
        Item item = (Item) this.fields.get(name);
        if (item == null) {
            return ret;
        }
        PdfArray values = item.getMerged(0).getAsArray(PdfName.f124I);
        if (values == null) {
            return ret;
        }
        ret = new String[values.size()];
        String[] options = getListOptionExport(name);
        int idx = 0;
        Iterator<PdfObject> i = values.listIterator();
        while (i.hasNext()) {
            int idx2 = idx + 1;
            ret[idx] = options[((PdfNumber) i.next()).intValue()];
            idx = idx2;
        }
        return ret;
    }

    public boolean setFieldProperty(String field, String name, Object value, int[] inst) {
        if (this.writer == null) {
            throw new RuntimeException(MessageLocalization.getComposedMessage("this.acrofields.instance.is.read.only", new Object[0]));
        }
        try {
            Item item = (Item) this.fields.get(field);
            if (item == null) {
                return false;
            }
            InstHit instHit = new InstHit(inst);
            int k;
            PdfString da;
            Object[] dao;
            PdfAppearance cb;
            PdfObject pdfString;
            if (name.equalsIgnoreCase("textfont")) {
                for (k = 0; k < item.size(); k++) {
                    if (instHit.isHit(k)) {
                        PdfDictionary merged = item.getMerged(k);
                        da = merged.getAsString(PdfName.DA);
                        PdfDictionary dr = merged.getAsDict(PdfName.DR);
                        if (da == null) {
                            continue;
                        } else {
                            if (dr == null) {
                                dr = new PdfDictionary();
                                merged.put(PdfName.DR, dr);
                            }
                            dao = splitDAelements(da.toUnicodeString());
                            cb = new PdfAppearance();
                            if (dao[0] != null) {
                                BaseFont bf = (BaseFont) value;
                                PdfName psn = (PdfName) PdfAppearance.stdFieldFontNames.get(bf.getPostscriptFontName());
                                if (psn == null) {
                                    PdfName pdfName = new PdfName(bf.getPostscriptFontName());
                                }
                                PdfDictionary fonts = dr.getAsDict(PdfName.FONT);
                                if (fonts == null) {
                                    fonts = new PdfDictionary();
                                    dr.put(PdfName.FONT, fonts);
                                }
                                PdfIndirectReference fref = (PdfIndirectReference) fonts.get(psn);
                                PdfObject top = this.reader.getCatalog().getAsDict(PdfName.ACROFORM);
                                markUsed(top);
                                dr = top.getAsDict(PdfName.DR);
                                if (dr == null) {
                                    dr = new PdfDictionary();
                                    top.put(PdfName.DR, dr);
                                }
                                markUsed(dr);
                                PdfDictionary fontsTop = dr.getAsDict(PdfName.FONT);
                                if (fontsTop == null) {
                                    fontsTop = new PdfDictionary();
                                    dr.put(PdfName.FONT, fontsTop);
                                }
                                markUsed(fontsTop);
                                PdfIndirectReference frefTop = (PdfIndirectReference) fontsTop.get(psn);
                                if (frefTop != null) {
                                    if (fref == null) {
                                        fonts.put(psn, frefTop);
                                    }
                                } else if (fref == null) {
                                    FontDetails fd;
                                    if (bf.getFontType() == 4) {
                                        fd = new FontDetails(null, ((DocumentFont) bf).getIndirectReference(), bf);
                                    } else {
                                        bf.setSubset(false);
                                        fd = this.writer.addSimple(bf);
                                        this.localFonts.put(psn.toString().substring(1), bf);
                                    }
                                    fontsTop.put(psn, fd.getIndirectReference());
                                    fonts.put(psn, fd.getIndirectReference());
                                }
                                cb.getInternalBuffer().append(psn.getBytes()).append(' ').append(((Float) dao[1]).floatValue()).append(" Tf ");
                                if (dao[2] != null) {
                                    cb.setColorFill((BaseColor) dao[2]);
                                }
                                pdfString = new PdfString(cb.toString());
                                item.getMerged(k).put(PdfName.DA, pdfString);
                                item.getWidget(k).put(PdfName.DA, pdfString);
                                markUsed(item.getWidget(k));
                            } else {
                                continue;
                            }
                        }
                    }
                }
            } else if (name.equalsIgnoreCase("textcolor")) {
                for (k = 0; k < item.size(); k++) {
                    if (instHit.isHit(k)) {
                        da = item.getMerged(k).getAsString(PdfName.DA);
                        if (da != null) {
                            dao = splitDAelements(da.toUnicodeString());
                            cb = new PdfAppearance();
                            if (dao[0] != null) {
                                cb.getInternalBuffer().append(new PdfName((String) dao[0]).getBytes()).append(' ').append(((Float) dao[1]).floatValue()).append(" Tf ");
                                cb.setColorFill((BaseColor) value);
                                pdfString = new PdfString(cb.toString());
                                item.getMerged(k).put(PdfName.DA, pdfString);
                                item.getWidget(k).put(PdfName.DA, pdfString);
                                markUsed(item.getWidget(k));
                            }
                        }
                    }
                }
            } else if (name.equalsIgnoreCase("textsize")) {
                for (k = 0; k < item.size(); k++) {
                    if (instHit.isHit(k)) {
                        da = item.getMerged(k).getAsString(PdfName.DA);
                        if (da != null) {
                            dao = splitDAelements(da.toUnicodeString());
                            cb = new PdfAppearance();
                            if (dao[0] != null) {
                                cb.getInternalBuffer().append(new PdfName((String) dao[0]).getBytes()).append(' ').append(((Float) value).floatValue()).append(" Tf ");
                                if (dao[2] != null) {
                                    cb.setColorFill((BaseColor) dao[2]);
                                }
                                pdfString = new PdfString(cb.toString());
                                item.getMerged(k).put(PdfName.DA, pdfString);
                                item.getWidget(k).put(PdfName.DA, pdfString);
                                markUsed(item.getWidget(k));
                            }
                        }
                    }
                }
            } else if (!name.equalsIgnoreCase(HtmlTags.BGCOLOR) && !name.equalsIgnoreCase("bordercolor")) {
                return false;
            } else {
                PdfName dname = name.equalsIgnoreCase(HtmlTags.BGCOLOR) ? PdfName.BG : PdfName.BC;
                for (k = 0; k < item.size(); k++) {
                    if (instHit.isHit(k)) {
                        PdfDictionary mk;
                        PdfObject mk2 = item.getMerged(k).getAsDict(PdfName.MK);
                        if (mk2 != null) {
                            markUsed(mk2);
                        } else if (value == null) {
                            return true;
                        } else {
                            mk = new PdfDictionary();
                            item.getMerged(k).put(PdfName.MK, mk);
                            item.getWidget(k).put(PdfName.MK, mk);
                            markUsed(item.getWidget(k));
                        }
                        if (value == null) {
                            mk.remove(dname);
                        } else {
                            mk.put(dname, PdfAnnotation.getMKColor((BaseColor) value));
                        }
                    }
                }
            }
            return true;
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    public boolean setFieldProperty(String field, String name, int value, int[] inst) {
        if (this.writer == null) {
            throw new RuntimeException(MessageLocalization.getComposedMessage("this.acrofields.instance.is.read.only", new Object[0]));
        }
        Item item = (Item) this.fields.get(field);
        if (item == null) {
            return false;
        }
        InstHit hit = new InstHit(inst);
        PdfNumber num;
        int k;
        if (name.equalsIgnoreCase("flags")) {
            num = new PdfNumber(value);
            for (k = 0; k < item.size(); k++) {
                if (hit.isHit(k)) {
                    item.getMerged(k).put(PdfName.f122F, num);
                    item.getWidget(k).put(PdfName.f122F, num);
                    markUsed(item.getWidget(k));
                }
            }
        } else if (name.equalsIgnoreCase("setflags")) {
            for (k = 0; k < item.size(); k++) {
                if (hit.isHit(k)) {
                    num = item.getWidget(k).getAsNumber(PdfName.f122F);
                    val = 0;
                    if (num != null) {
                        val = num.intValue();
                    }
                    num = new PdfNumber(val | value);
                    item.getMerged(k).put(PdfName.f122F, num);
                    item.getWidget(k).put(PdfName.f122F, num);
                    markUsed(item.getWidget(k));
                }
            }
        } else if (name.equalsIgnoreCase("clrflags")) {
            for (k = 0; k < item.size(); k++) {
                if (hit.isHit(k)) {
                    PdfDictionary widget = item.getWidget(k);
                    num = widget.getAsNumber(PdfName.f122F);
                    val = 0;
                    if (num != null) {
                        val = num.intValue();
                    }
                    num = new PdfNumber((value ^ -1) & val);
                    item.getMerged(k).put(PdfName.f122F, num);
                    widget.put(PdfName.f122F, num);
                    markUsed(widget);
                }
            }
        } else if (name.equalsIgnoreCase("fflags")) {
            num = new PdfNumber(value);
            for (k = 0; k < item.size(); k++) {
                if (hit.isHit(k)) {
                    item.getMerged(k).put(PdfName.FF, num);
                    item.getValue(k).put(PdfName.FF, num);
                    markUsed(item.getValue(k));
                }
            }
        } else if (name.equalsIgnoreCase("setfflags")) {
            for (k = 0; k < item.size(); k++) {
                if (hit.isHit(k)) {
                    valDict = item.getValue(k);
                    num = valDict.getAsNumber(PdfName.FF);
                    val = 0;
                    if (num != null) {
                        val = num.intValue();
                    }
                    num = new PdfNumber(val | value);
                    item.getMerged(k).put(PdfName.FF, num);
                    valDict.put(PdfName.FF, num);
                    markUsed(valDict);
                }
            }
        } else if (!name.equalsIgnoreCase("clrfflags")) {
            return false;
        } else {
            for (k = 0; k < item.size(); k++) {
                if (hit.isHit(k)) {
                    valDict = item.getValue(k);
                    num = valDict.getAsNumber(PdfName.FF);
                    val = 0;
                    if (num != null) {
                        val = num.intValue();
                    }
                    num = new PdfNumber((value ^ -1) & val);
                    item.getMerged(k).put(PdfName.FF, num);
                    valDict.put(PdfName.FF, num);
                    markUsed(valDict);
                }
            }
        }
        return true;
    }

    public void mergeXfaData(Node n) throws IOException, DocumentException {
        Xml2SomDatasets data = new Xml2SomDatasets(n);
        Iterator i$ = data.getOrder().iterator();
        while (i$.hasNext()) {
            String name = (String) i$.next();
            setField(name, XfaForm.getNodeText((Node) data.getName2Node().get(name)));
        }
    }

    public void setFields(FdfReader fdf) throws IOException, DocumentException {
        for (String f : fdf.getFields().keySet()) {
            String v = fdf.getFieldValue(f);
            if (v != null) {
                setField(f, v);
            }
        }
    }

    public void setFields(XfdfReader xfdf) throws IOException, DocumentException {
        for (String f : xfdf.getFields().keySet()) {
            String v = xfdf.getFieldValue(f);
            if (v != null) {
                setField(f, v);
            }
            List<String> l = xfdf.getListValues(f);
            if (l != null) {
                setListSelection(v, (String[]) l.toArray(new String[l.size()]));
            }
        }
    }

    public boolean regenerateField(String name) throws IOException, DocumentException {
        String value = getField(name);
        return setField(name, value, value);
    }

    public boolean setField(String name, String value) throws IOException, DocumentException {
        return setField(name, value, null);
    }

    public boolean setFieldRichValue(String name, String richValue) throws DocumentException, IOException {
        if (this.writer == null) {
            throw new DocumentException(MessageLocalization.getComposedMessage("this.acrofields.instance.is.read.only", new Object[0]));
        }
        Item item = getFieldItem(name);
        if (item == null || getFieldType(name) != 4) {
            return false;
        }
        PdfNumber ffNum = item.getMerged(0).getAsNumber(PdfName.FF);
        int flagVal = 0;
        if (ffNum != null) {
            flagVal = ffNum.intValue();
        }
        if ((33554432 & flagVal) == 0) {
            return false;
        }
        item.writeToAll(PdfName.RV, new PdfString(richValue), 5);
        item.writeToAll(PdfName.f136V, new PdfString(XmlToTxt.parse(new ByteArrayInputStream(richValue.getBytes()))), 5);
        return true;
    }

    public boolean setField(String name, String value, String display) throws IOException, DocumentException {
        if (this.writer == null) {
            throw new DocumentException(MessageLocalization.getComposedMessage("this.acrofields.instance.is.read.only", new Object[0]));
        }
        if (this.xfa.isXfaPresent()) {
            name = this.xfa.findFieldName(name, this);
            if (name == null) {
                return false;
            }
            String shortName = Xml2Som.getShortName(name);
            Node xn = this.xfa.findDatasetsNode(shortName);
            if (xn == null) {
                xn = this.xfa.getDatasetsSom().insertNode(this.xfa.getDatasetsNode(), shortName);
            }
            this.xfa.setNodeText(xn, value);
        }
        Item item = (Item) this.fields.get(name);
        if (item == null) {
            return false;
        }
        PdfDictionary merged = item.getMerged(0);
        PdfName type = merged.getAsName(PdfName.FT);
        if (PdfName.TX.equals(type)) {
            PdfNumber maxLen = merged.getAsNumber(PdfName.MAXLEN);
            int len = 0;
            if (maxLen != null) {
                len = maxLen.intValue();
            }
            if (len > 0) {
                value = value.substring(0, Math.min(len, value.length()));
            }
        }
        if (display == null) {
            display = value;
        }
        PdfObject pdfString;
        int idx;
        PdfObject widget;
        PdfDictionary appDic;
        if (PdfName.TX.equals(type) || PdfName.CH.equals(type)) {
            pdfString = new PdfString(value, PdfObject.TEXT_UNICODE);
            for (idx = 0; idx < item.size(); idx++) {
                PdfObject valueDic = item.getValue(idx);
                valueDic.put(PdfName.f136V, pdfString);
                valueDic.remove(PdfName.f124I);
                markUsed(valueDic);
                merged = item.getMerged(idx);
                merged.remove(PdfName.f124I);
                merged.put(PdfName.f136V, pdfString);
                widget = item.getWidget(idx);
                if (this.generateAppearances) {
                    PdfAppearance app = getAppearance(merged, display, name);
                    if (PdfName.CH.equals(type)) {
                        pdfString = new PdfNumber(this.topFirst);
                        widget.put(PdfName.TI, pdfString);
                        merged.put(PdfName.TI, pdfString);
                    }
                    appDic = widget.getAsDict(PdfName.AP);
                    if (appDic == null) {
                        appDic = new PdfDictionary();
                        widget.put(PdfName.AP, appDic);
                        merged.put(PdfName.AP, appDic);
                    }
                    appDic.put(PdfName.f128N, app.getIndirectReference());
                    this.writer.releaseTemplate(app);
                } else {
                    widget.remove(PdfName.AP);
                    merged.remove(PdfName.AP);
                }
                markUsed(widget);
            }
            return true;
        } else if (!PdfName.BTN.equals(type)) {
            return false;
        } else {
            PdfNumber ff = item.getMerged(0).getAsNumber(PdfName.FF);
            int flags = 0;
            if (ff != null) {
                flags = ff.intValue();
            }
            if ((65536 & flags) != 0) {
                try {
                    Image img = Image.getInstance(Base64.decode(value));
                    PushbuttonField pb = getNewPushbuttonFromField(name);
                    pb.setImage(img);
                    replacePushbuttonField(name, pb.getField());
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
            PdfObject vt;
            pdfString = new PdfName(value);
            ArrayList<String> lopt = new ArrayList();
            PdfArray opts = item.getValue(0).getAsArray(PdfName.OPT);
            if (opts != null) {
                for (int k = 0; k < opts.size(); k++) {
                    PdfString valStr = opts.getAsString(k);
                    if (valStr != null) {
                        lopt.add(valStr.toUnicodeString());
                    } else {
                        lopt.add(null);
                    }
                }
            }
            int vidx = lopt.indexOf(value);
            if (vidx >= 0) {
                pdfString = new PdfName(String.valueOf(vidx));
            } else {
                vt = pdfString;
            }
            for (idx = 0; idx < item.size(); idx++) {
                merged = item.getMerged(idx);
                widget = item.getWidget(idx);
                PdfDictionary valDict = item.getValue(idx);
                markUsed(item.getValue(idx));
                valDict.put(PdfName.f136V, vt);
                merged.put(PdfName.f136V, vt);
                markUsed(widget);
                appDic = widget.getAsDict(PdfName.AP);
                if (appDic == null) {
                    return false;
                }
                PdfDictionary normal = appDic.getAsDict(PdfName.f128N);
                if (isInAP(normal, vt) || normal == null) {
                    merged.put(PdfName.AS, vt);
                    widget.put(PdfName.AS, vt);
                } else {
                    merged.put(PdfName.AS, PdfName.Off);
                    widget.put(PdfName.AS, PdfName.Off);
                }
            }
            return true;
        }
    }

    public boolean setListSelection(String name, String[] value) throws IOException, DocumentException {
        Item item = getFieldItem(name);
        if (item == null) {
            return false;
        }
        PdfDictionary merged = item.getMerged(0);
        if (!PdfName.CH.equals(merged.getAsName(PdfName.FT))) {
            return false;
        }
        String[] options = getListOptionExport(name);
        PdfArray array = new PdfArray();
        for (String element : value) {
            for (int j = 0; j < options.length; j++) {
                if (options[j].equals(element)) {
                    array.add(new PdfNumber(j));
                    break;
                }
            }
        }
        item.writeToAll(PdfName.f124I, array, 5);
        PdfObject vals = new PdfArray();
        for (String pdfString : value) {
            vals.add(new PdfString(pdfString));
        }
        item.writeToAll(PdfName.f136V, vals, 5);
        PdfAppearance app = getAppearance(merged, value, name);
        PdfDictionary apDic = new PdfDictionary();
        apDic.put(PdfName.f128N, app.getIndirectReference());
        item.writeToAll(PdfName.AP, apDic, 3);
        this.writer.releaseTemplate(app);
        item.markUsed(this, 6);
        return true;
    }

    boolean isInAP(PdfDictionary nDic, PdfName check) {
        return (nDic == null || nDic.get(check) == null) ? false : true;
    }

    public Map<String, Item> getFields() {
        return this.fields;
    }

    public Item getFieldItem(String name) {
        if (this.xfa.isXfaPresent()) {
            name = this.xfa.findFieldName(name, this);
            if (name == null) {
                return null;
            }
        }
        return (Item) this.fields.get(name);
    }

    public String getTranslatedFieldName(String name) {
        if (!this.xfa.isXfaPresent()) {
            return name;
        }
        String namex = this.xfa.findFieldName(name, this);
        if (namex != null) {
            return namex;
        }
        return name;
    }

    public List<FieldPosition> getFieldPositions(String name) {
        Item item = getFieldItem(name);
        if (item == null) {
            return null;
        }
        List<FieldPosition> ret = new ArrayList();
        for (int k = 0; k < item.size(); k++) {
            try {
                PdfArray rect = item.getWidget(k).getAsArray(PdfName.RECT);
                if (rect != null) {
                    Rectangle r = PdfReader.getNormalizedRectangle(rect);
                    int page = item.getPage(k).intValue();
                    int rotation = this.reader.getPageRotation(page);
                    FieldPosition fp = new FieldPosition();
                    fp.page = page;
                    if (rotation != 0) {
                        Rectangle pageSize = this.reader.getPageSize(page);
                        switch (rotation) {
                            case 90:
                                r = new Rectangle(r.getBottom(), pageSize.getRight() - r.getLeft(), r.getTop(), pageSize.getRight() - r.getRight());
                                break;
                            case 180:
                                r = new Rectangle(pageSize.getRight() - r.getLeft(), pageSize.getTop() - r.getBottom(), pageSize.getRight() - r.getRight(), pageSize.getTop() - r.getTop());
                                break;
                            case 270:
                                r = new Rectangle(pageSize.getTop() - r.getBottom(), r.getLeft(), pageSize.getTop() - r.getTop(), r.getRight());
                                break;
                        }
                        r.normalize();
                    }
                    fp.position = r;
                    ret.add(fp);
                }
            } catch (Exception e) {
            }
        }
        return ret;
    }

    private int removeRefFromArray(PdfArray array, PdfObject refo) {
        if (refo == null || !refo.isIndirect()) {
            return array.size();
        }
        PdfIndirectReference ref = (PdfIndirectReference) refo;
        int j = 0;
        while (j < array.size()) {
            PdfObject obj = array.getPdfObject(j);
            if (obj.isIndirect() && ((PdfIndirectReference) obj).getNumber() == ref.getNumber()) {
                int j2 = j - 1;
                array.remove(j);
                j = j2;
            }
            j++;
        }
        return array.size();
    }

    public boolean removeFieldsFromPage(int page) {
        if (page < 1) {
            return false;
        }
        String[] names = new String[this.fields.size()];
        this.fields.keySet().toArray(names);
        boolean found = false;
        for (String removeField : names) {
            boolean fr = removeField(removeField, page);
            if (found || fr) {
                found = true;
            } else {
                found = false;
            }
        }
        return found;
    }

    public boolean removeField(String name, int page) {
        Item item = getFieldItem(name);
        if (item == null) {
            return false;
        }
        PdfDictionary acroForm = (PdfDictionary) PdfReader.getPdfObject(this.reader.getCatalog().get(PdfName.ACROFORM), this.reader.getCatalog());
        if (acroForm == null) {
            return false;
        }
        PdfArray arrayf = acroForm.getAsArray(PdfName.FIELDS);
        if (arrayf == null) {
            return false;
        }
        int k = 0;
        while (k < item.size()) {
            int pageV = item.getPage(k).intValue();
            if (page == -1 || page == pageV) {
                PdfIndirectReference ref = item.getWidgetRef(k);
                PdfDictionary wd = item.getWidget(k);
                PdfDictionary pageDic = this.reader.getPageN(pageV);
                PdfArray annots = pageDic.getAsArray(PdfName.ANNOTS);
                if (annots != null) {
                    if (removeRefFromArray(annots, ref) == 0) {
                        pageDic.remove(PdfName.ANNOTS);
                        markUsed(pageDic);
                    } else {
                        markUsed(annots);
                    }
                }
                PdfReader.killIndirect(ref);
                PdfIndirectReference kid = ref;
                while (true) {
                    ref = wd.getAsIndirectObject(PdfName.PARENT);
                    if (ref == null) {
                        break;
                    }
                    wd = wd.getAsDict(PdfName.PARENT);
                    if (removeRefFromArray(wd.getAsArray(PdfName.KIDS), kid) != 0) {
                        break;
                    }
                    kid = ref;
                    PdfReader.killIndirect(ref);
                }
                if (ref == null) {
                    removeRefFromArray(arrayf, kid);
                    markUsed(arrayf);
                }
                if (page != -1) {
                    item.remove(k);
                    k--;
                }
            }
            k++;
        }
        if (page == -1 || item.size() == 0) {
            this.fields.remove(name);
        }
        return true;
    }

    public boolean removeField(String name) {
        return removeField(name, -1);
    }

    public boolean isGenerateAppearances() {
        return this.generateAppearances;
    }

    public void setGenerateAppearances(boolean generateAppearances) {
        this.generateAppearances = generateAppearances;
        PdfDictionary top = this.reader.getCatalog().getAsDict(PdfName.ACROFORM);
        if (generateAppearances) {
            top.remove(PdfName.NEEDAPPEARANCES);
        } else {
            top.put(PdfName.NEEDAPPEARANCES, PdfBoolean.PDFTRUE);
        }
    }

    public boolean clearSignatureField(String name) {
        this.sigNames = null;
        getSignatureNames();
        if (!this.sigNames.containsKey(name)) {
            return false;
        }
        Item sig = (Item) this.fields.get(name);
        sig.markUsed(this, 6);
        int n = sig.size();
        for (int k = 0; k < n; k++) {
            clearSigDic(sig.getMerged(k));
            clearSigDic(sig.getWidget(k));
            clearSigDic(sig.getValue(k));
        }
        return true;
    }

    private static void clearSigDic(PdfDictionary dic) {
        dic.remove(PdfName.AP);
        dic.remove(PdfName.AS);
        dic.remove(PdfName.f136V);
        dic.remove(PdfName.DV);
        dic.remove(PdfName.SV);
        dic.remove(PdfName.FF);
        dic.put(PdfName.f122F, new PdfNumber(4));
    }

    public ArrayList<String> getSignatureNames() {
        if (this.sigNames != null) {
            return new ArrayList(this.orderedSignatureNames);
        }
        this.sigNames = new HashMap();
        this.orderedSignatureNames = new ArrayList();
        ArrayList<Object[]> sorter = new ArrayList();
        for (Entry<String, Item> entry : this.fields.entrySet()) {
            PdfDictionary merged = ((Item) entry.getValue()).getMerged(0);
            if (PdfName.SIG.equals(merged.get(PdfName.FT))) {
                PdfDictionary v = merged.getAsDict(PdfName.f136V);
                if (!(v == null || v.getAsString(PdfName.CONTENTS) == null)) {
                    PdfArray ro = v.getAsArray(PdfName.BYTERANGE);
                    if (ro != null) {
                        int rangeSize = ro.size();
                        if (rangeSize >= 2) {
                            int length = ro.getAsNumber(rangeSize - 1).intValue() + ro.getAsNumber(rangeSize - 2).intValue();
                            Object obj = new Object[2];
                            obj[0] = entry.getKey();
                            obj[1] = new int[]{length, 0};
                            sorter.add(obj);
                        }
                    }
                }
            }
        }
        Collections.sort(sorter, new SorterComparator());
        if (!sorter.isEmpty()) {
            if (((long) ((int[]) ((Object[]) sorter.get(sorter.size() - 1))[1])[0]) == this.reader.getFileLength()) {
                this.totalRevisions = sorter.size();
            } else {
                this.totalRevisions = sorter.size() + 1;
            }
            for (int k = 0; k < sorter.size(); k++) {
                Object[] objs = (Object[]) sorter.get(k);
                String name = objs[0];
                int[] p = (int[]) objs[1];
                p[1] = k + 1;
                this.sigNames.put(name, p);
                this.orderedSignatureNames.add(name);
            }
        }
        return new ArrayList(this.orderedSignatureNames);
    }

    public ArrayList<String> getBlankSignatureNames() {
        getSignatureNames();
        ArrayList<String> sigs = new ArrayList();
        for (Entry<String, Item> entry : this.fields.entrySet()) {
            if (PdfName.SIG.equals(((Item) entry.getValue()).getMerged(0).getAsName(PdfName.FT)) && !this.sigNames.containsKey(entry.getKey())) {
                sigs.add(entry.getKey());
            }
        }
        return sigs;
    }

    public PdfDictionary getSignatureDictionary(String name) {
        getSignatureNames();
        name = getTranslatedFieldName(name);
        if (this.sigNames.containsKey(name)) {
            return ((Item) this.fields.get(name)).getMerged(0).getAsDict(PdfName.f136V);
        }
        return null;
    }

    public PdfIndirectReference getNormalAppearance(String name) {
        getSignatureNames();
        Item item = (Item) this.fields.get(getTranslatedFieldName(name));
        if (item == null) {
            return null;
        }
        PdfDictionary ap = item.getMerged(0).getAsDict(PdfName.AP);
        if (ap == null) {
            return null;
        }
        PdfIndirectReference ref = ap.getAsIndirectObject(PdfName.f128N);
        if (ref == null) {
            return null;
        }
        return ref;
    }

    public boolean signatureCoversWholeDocument(String name) {
        getSignatureNames();
        name = getTranslatedFieldName(name);
        if (!this.sigNames.containsKey(name)) {
            return false;
        }
        return ((long) ((int[]) this.sigNames.get(name))[0]) == this.reader.getFileLength();
    }

    public PdfPKCS7 verifySignature(String name) {
        return verifySignature(name, null);
    }

    public PdfPKCS7 verifySignature(String name, String provider) {
        PdfDictionary v = getSignatureDictionary(name);
        if (v == null) {
            return null;
        }
        try {
            PdfPKCS7 pk;
            PdfName sub = v.getAsName(PdfName.SUBFILTER);
            PdfString contents = v.getAsString(PdfName.CONTENTS);
            if (sub.equals(PdfName.ADBE_X509_RSA_SHA1)) {
                PdfString cert = v.getAsString(PdfName.CERT);
                if (cert == null) {
                    cert = v.getAsArray(PdfName.CERT).getAsString(0);
                }
                pk = new PdfPKCS7(contents.getOriginalBytes(), cert.getBytes(), provider);
            } else {
                pk = new PdfPKCS7(contents.getOriginalBytes(), sub, provider);
            }
            updateByteRange(pk, v);
            PdfString str = v.getAsString(PdfName.f127M);
            if (str != null) {
                pk.setSignDate(PdfDate.decode(str.toString()));
            }
            PdfObject obj = PdfReader.getPdfObject(v.get(PdfName.NAME));
            if (obj != null) {
                if (obj.isString()) {
                    pk.setSignName(((PdfString) obj).toUnicodeString());
                } else if (obj.isName()) {
                    pk.setSignName(PdfName.decodeName(obj.toString()));
                }
            }
            str = v.getAsString(PdfName.REASON);
            if (str != null) {
                pk.setReason(str.toUnicodeString());
            }
            str = v.getAsString(PdfName.LOCATION);
            if (str == null) {
                return pk;
            }
            pk.setLocation(str.toUnicodeString());
            return pk;
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    private void updateByteRange(PdfPKCS7 pkcs7, PdfDictionary v) {
        Exception e;
        Throwable th;
        PdfArray b = v.getAsArray(PdfName.BYTERANGE);
        InputStream inputStream = null;
        try {
            InputStream rg = new RASInputStream(new RandomAccessSourceFactory().createRanged(this.reader.getSafeFile().createSourceView(), b.asLongArray()));
            try {
                byte[] buf = new byte[8192];
                while (true) {
                    int rd = rg.read(buf, 0, buf.length);
                    if (rd <= 0) {
                        break;
                    }
                    pkcs7.update(buf, 0, rd);
                }
                if (rg != null) {
                    try {
                        rg.close();
                    } catch (IOException e2) {
                        throw new ExceptionConverter(e2);
                    }
                }
            } catch (Exception e3) {
                e = e3;
                inputStream = rg;
                try {
                    throw new ExceptionConverter(e);
                } catch (Throwable th2) {
                    th = th2;
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e22) {
                            throw new ExceptionConverter(e22);
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                inputStream = rg;
                if (inputStream != null) {
                    inputStream.close();
                }
                throw th;
            }
        } catch (Exception e4) {
            e = e4;
            throw new ExceptionConverter(e);
        }
    }

    private void markUsed(PdfObject obj) {
        if (this.append) {
            ((PdfStamperImp) this.writer).markUsed(obj);
        }
    }

    public int getTotalRevisions() {
        getSignatureNames();
        return this.totalRevisions;
    }

    public int getRevision(String field) {
        getSignatureNames();
        field = getTranslatedFieldName(field);
        if (this.sigNames.containsKey(field)) {
            return ((int[]) this.sigNames.get(field))[1];
        }
        return 0;
    }

    public InputStream extractRevision(String field) throws IOException {
        getSignatureNames();
        field = getTranslatedFieldName(field);
        if (!this.sigNames.containsKey(field)) {
            return null;
        }
        return new RASInputStream(new WindowRandomAccessSource(this.reader.getSafeFile().createSourceView(), 0, (long) ((int[]) this.sigNames.get(field))[0]));
    }

    public Map<String, TextField> getFieldCache() {
        return this.fieldCache;
    }

    public void setFieldCache(Map<String, TextField> fieldCache) {
        this.fieldCache = fieldCache;
    }

    public void setExtraMargin(float extraMarginLeft, float extraMarginTop) {
        this.extraMarginLeft = extraMarginLeft;
        this.extraMarginTop = extraMarginTop;
    }

    public void addSubstitutionFont(BaseFont font) {
        if (this.substitutionFonts == null) {
            this.substitutionFonts = new ArrayList();
        }
        this.substitutionFonts.add(font);
    }

    static {
        stdFieldFontNames.put("CoBO", new String[]{"Courier-BoldOblique"});
        stdFieldFontNames.put("CoBo", new String[]{"Courier-Bold"});
        stdFieldFontNames.put("CoOb", new String[]{"Courier-Oblique"});
        stdFieldFontNames.put("Cour", new String[]{"Courier"});
        stdFieldFontNames.put("HeBO", new String[]{"Helvetica-BoldOblique"});
        stdFieldFontNames.put("HeBo", new String[]{"Helvetica-Bold"});
        stdFieldFontNames.put("HeOb", new String[]{"Helvetica-Oblique"});
        stdFieldFontNames.put("Helv", new String[]{"Helvetica"});
        stdFieldFontNames.put("Symb", new String[]{"Symbol"});
        stdFieldFontNames.put("TiBI", new String[]{"Times-BoldItalic"});
        stdFieldFontNames.put("TiBo", new String[]{"Times-Bold"});
        stdFieldFontNames.put("TiIt", new String[]{"Times-Italic"});
        stdFieldFontNames.put("TiRo", new String[]{"Times-Roman"});
        stdFieldFontNames.put("ZaDb", new String[]{"ZapfDingbats"});
        stdFieldFontNames.put("HySm", new String[]{"HYSMyeongJo-Medium", "UniKS-UCS2-H"});
        stdFieldFontNames.put("HyGo", new String[]{"HYGoThic-Medium", "UniKS-UCS2-H"});
        stdFieldFontNames.put("KaGo", new String[]{"HeiseiKakuGo-W5", "UniKS-UCS2-H"});
        stdFieldFontNames.put("KaMi", new String[]{"HeiseiMin-W3", "UniJIS-UCS2-H"});
        stdFieldFontNames.put("MHei", new String[]{"MHei-Medium", "UniCNS-UCS2-H"});
        stdFieldFontNames.put("MSun", new String[]{"MSung-Light", "UniCNS-UCS2-H"});
        stdFieldFontNames.put("STSo", new String[]{"STSong-Light", "UniGB-UCS2-H"});
    }

    public ArrayList<BaseFont> getSubstitutionFonts() {
        return this.substitutionFonts;
    }

    public void setSubstitutionFonts(ArrayList<BaseFont> substitutionFonts) {
        this.substitutionFonts = substitutionFonts;
    }

    public XfaForm getXfa() {
        return this.xfa;
    }

    public void removeXfa() {
        this.reader.getCatalog().getAsDict(PdfName.ACROFORM).remove(PdfName.XFA);
        try {
            this.xfa = new XfaForm(this.reader);
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    public PushbuttonField getNewPushbuttonFromField(String field) {
        return getNewPushbuttonFromField(field, 0);
    }

    public PushbuttonField getNewPushbuttonFromField(String field, int order) {
        try {
            if (getFieldType(field) != 1) {
                return null;
            }
            Item item = getFieldItem(field);
            if (order >= item.size()) {
                return null;
            }
            PushbuttonField newButton = new PushbuttonField(this.writer, ((FieldPosition) getFieldPositions(field).get(order)).position, null);
            PdfDictionary dic = item.getMerged(order);
            decodeGenericDictionary(dic, newButton);
            PdfDictionary mk = dic.getAsDict(PdfName.MK);
            if (mk == null) {
                return newButton;
            }
            PdfString text = mk.getAsString(PdfName.CA);
            if (text != null) {
                newButton.setText(text.toUnicodeString());
            }
            PdfNumber tp = mk.getAsNumber(PdfName.TP);
            if (tp != null) {
                newButton.setLayout(tp.intValue() + 1);
            }
            PdfDictionary ifit = mk.getAsDict(PdfName.IF);
            if (ifit != null) {
                PdfName sw = ifit.getAsName(PdfName.SW);
                if (sw != null) {
                    int scale = 1;
                    if (sw.equals(PdfName.f118B)) {
                        scale = 3;
                    } else if (sw.equals(PdfName.f133S)) {
                        scale = 4;
                    } else if (sw.equals(PdfName.f128N)) {
                        scale = 2;
                    }
                    newButton.setScaleIcon(scale);
                }
                sw = ifit.getAsName(PdfName.f133S);
                if (sw != null && sw.equals(PdfName.f117A)) {
                    newButton.setProportionalIcon(false);
                }
                PdfArray aj = ifit.getAsArray(PdfName.f117A);
                if (aj != null && aj.size() == 2) {
                    float left = aj.getAsNumber(0).floatValue();
                    float bottom = aj.getAsNumber(1).floatValue();
                    newButton.setIconHorizontalAdjustment(left);
                    newButton.setIconVerticalAdjustment(bottom);
                }
                PdfBoolean fb = ifit.getAsBoolean(PdfName.FB);
                if (fb != null && fb.booleanValue()) {
                    newButton.setIconFitToBounds(true);
                }
            }
            PdfObject i = mk.get(PdfName.f124I);
            if (i == null || !i.isIndirect()) {
                return newButton;
            }
            newButton.setIconReference((PRIndirectReference) i);
            return newButton;
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    public boolean replacePushbuttonField(String field, PdfFormField button) {
        return replacePushbuttonField(field, button, 0);
    }

    public boolean replacePushbuttonField(String field, PdfFormField button, int order) {
        if (getFieldType(field) != 1) {
            return false;
        }
        Item item = getFieldItem(field);
        if (order >= item.size()) {
            return false;
        }
        PdfDictionary merged = item.getMerged(order);
        PdfDictionary values = item.getValue(order);
        PdfDictionary widgets = item.getWidget(order);
        for (int k = 0; k < buttonRemove.length; k++) {
            merged.remove(buttonRemove[k]);
            values.remove(buttonRemove[k]);
            widgets.remove(buttonRemove[k]);
        }
        for (PdfName key : button.getKeys()) {
            if (!key.equals(PdfName.f134T)) {
                if (key.equals(PdfName.FF)) {
                    values.put(key, button.get(key));
                } else {
                    widgets.put(key, button.get(key));
                }
                merged.put(key, button.get(key));
                markUsed(values);
                markUsed(widgets);
            }
        }
        return true;
    }

    public boolean doesSignatureFieldExist(String name) {
        return getBlankSignatureNames().contains(name) || getSignatureNames().contains(name);
    }
}

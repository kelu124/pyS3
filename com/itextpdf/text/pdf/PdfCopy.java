package com.itextpdf.text.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.exceptions.BadPasswordException;
import com.itextpdf.text.log.Counter;
import com.itextpdf.text.log.CounterFactory;
import com.itextpdf.text.pdf.AcroFields.Item;
import com.itextpdf.text.pdf.PdfWriter$PdfBody.PdfCrossReference;
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

public class PdfCopy extends PdfWriter {
    protected static Counter COUNTER = CounterFactory.getCounter(PdfCopy.class);
    private static final PdfName annotId = new PdfName("iTextAnnotId");
    private static int annotIdCnt = 0;
    protected static final HashSet<PdfName> fieldKeys = new HashSet();
    private static final PdfName iTextTag = new PdfName("_iTextTag_");
    protected static final HashSet<PdfName> widgetKeys = new HashSet();
    private static final Integer zero = Integer.valueOf(0);
    private PdfIndirectReference acroForm;
    private ArrayList<String> calculationOrder;
    private ArrayList<Object> calculationOrderRefs;
    private int currentStructArrayNumber = 0;
    protected HashSet<PdfObject> disableIndirects;
    protected PdfArray fieldArray;
    protected HashSet<PdfTemplate> fieldTemplates;
    private HashMap<String, Object> fieldTree;
    protected ArrayList<AcroFields> fields;
    private boolean hasSignature;
    protected ArrayList<ImportedPage> importedPages;
    protected HashMap<PdfReader, HashMap<RefKey, IndirectReferences>> indirectMap;
    protected HashMap<RefKey, PdfIndirectObject> indirectObjects;
    protected HashMap<RefKey, IndirectReferences> indirects;
    protected boolean mergeFields = false;
    private boolean mergeFieldsInternalCall = false;
    private HashMap<Integer, PdfIndirectObject> mergedMap;
    private HashSet<Object> mergedRadioButtons = new HashSet();
    private HashSet<PdfIndirectObject> mergedSet;
    private HashMap<Object, PdfString> mergedTextFields = new HashMap();
    protected int[] namePtr = new int[]{0};
    private boolean needAppearances = false;
    protected HashMap<PdfObject, PdfObject> parentObjects;
    protected PdfReader reader;
    private HashSet<PdfReader> readersWithImportedStructureTreeRootKids = new HashSet();
    private PdfDictionary resources;
    private boolean rotateContents = true;
    protected ArrayList<PdfIndirectObject> savedObjects;
    private PdfStructTreeController structTreeController = null;
    protected PRIndirectReference structTreeRootReference;
    private HashMap<PdfArray, ArrayList<Integer>> tabOrder;
    private HashMap<Integer, PdfIndirectObject> unmergedMap;
    private HashSet<PdfIndirectObject> unmergedSet;
    protected boolean updateRootKids = false;

    protected static class ImportedPage {
        PdfIndirectReference annotsIndirectReference;
        PdfArray mergedFields;
        int pageNumber;
        PdfReader reader;

        ImportedPage(PdfReader reader, int pageNumber, boolean keepFields) {
            this.pageNumber = pageNumber;
            this.reader = reader;
            if (keepFields) {
                this.mergedFields = new PdfArray();
            }
        }

        public boolean equals(Object o) {
            if (!(o instanceof ImportedPage)) {
                return false;
            }
            ImportedPage other = (ImportedPage) o;
            if (this.pageNumber == other.pageNumber && this.reader.equals(other.reader)) {
                return true;
            }
            return false;
        }

        public String toString() {
            return Integer.toString(this.pageNumber);
        }
    }

    static class IndirectReferences {
        boolean hasCopied = false;
        PdfIndirectReference theRef;

        IndirectReferences(PdfIndirectReference ref) {
            this.theRef = ref;
        }

        void setCopied() {
            this.hasCopied = true;
        }

        void setNotCopied() {
            this.hasCopied = false;
        }

        boolean getCopied() {
            return this.hasCopied;
        }

        PdfIndirectReference getRef() {
            return this.theRef;
        }

        public String toString() {
            String ext = "";
            if (this.hasCopied) {
                ext = ext + " Copied";
            }
            return getRef() + ext;
        }
    }

    public static class PageStamp {
        PdfCopy cstp;
        StampContent over;
        PdfDictionary pageN;
        PageResources pageResources;
        PdfReader reader;
        StampContent under;

        PageStamp(PdfReader reader, PdfDictionary pageN, PdfCopy cstp) {
            this.pageN = pageN;
            this.reader = reader;
            this.cstp = cstp;
        }

        public PdfContentByte getUnderContent() {
            if (this.under == null) {
                if (this.pageResources == null) {
                    this.pageResources = new PageResources();
                    this.pageResources.setOriginalResources(this.pageN.getAsDict(PdfName.RESOURCES), this.cstp.namePtr);
                }
                this.under = new StampContent(this.cstp, this.pageResources);
            }
            return this.under;
        }

        public PdfContentByte getOverContent() {
            if (this.over == null) {
                if (this.pageResources == null) {
                    this.pageResources = new PageResources();
                    this.pageResources.setOriginalResources(this.pageN.getAsDict(PdfName.RESOURCES), this.cstp.namePtr);
                }
                this.over = new StampContent(this.cstp, this.pageResources);
            }
            return this.over;
        }

        public void alterContents() throws IOException {
            if (this.over != null || this.under != null) {
                PdfArray ar;
                PdfObject content = PdfReader.getPdfObject(this.pageN.get(PdfName.CONTENTS), this.pageN);
                if (content == null) {
                    ar = new PdfArray();
                    this.pageN.put(PdfName.CONTENTS, ar);
                } else if (content.isArray()) {
                    ar = (PdfArray) content;
                } else if (content.isStream()) {
                    ar = new PdfArray();
                    ar.add(this.pageN.get(PdfName.CONTENTS));
                    this.pageN.put(PdfName.CONTENTS, ar);
                } else {
                    ar = new PdfArray();
                    this.pageN.put(PdfName.CONTENTS, ar);
                }
                ByteBuffer out = new ByteBuffer();
                if (this.under != null) {
                    out.append(PdfContents.SAVESTATE);
                    applyRotation(this.pageN, out);
                    out.append(this.under.getInternalBuffer());
                    out.append(PdfContents.RESTORESTATE);
                }
                if (this.over != null) {
                    out.append(PdfContents.SAVESTATE);
                }
                PdfStream stream = new PdfStream(out.toByteArray());
                stream.flateCompress(this.cstp.getCompressionLevel());
                ar.addFirst(this.cstp.addToBody(stream).getIndirectReference());
                out.reset();
                if (this.over != null) {
                    out.append(' ');
                    out.append(PdfContents.RESTORESTATE);
                    out.append(PdfContents.SAVESTATE);
                    applyRotation(this.pageN, out);
                    out.append(this.over.getInternalBuffer());
                    out.append(PdfContents.RESTORESTATE);
                    stream = new PdfStream(out.toByteArray());
                    stream.flateCompress(this.cstp.getCompressionLevel());
                    ar.add(this.cstp.addToBody(stream).getIndirectReference());
                }
                this.pageN.put(PdfName.RESOURCES, this.pageResources.getResources());
            }
        }

        void applyRotation(PdfDictionary pageN, ByteBuffer out) {
            if (this.cstp.rotateContents) {
                Rectangle page = this.reader.getPageSizeWithRotation(pageN);
                switch (page.getRotation()) {
                    case 90:
                        out.append(PdfContents.ROTATE90);
                        out.append(page.getTop());
                        out.append(' ').append('0').append(PdfContents.ROTATEFINAL);
                        return;
                    case 180:
                        out.append(PdfContents.ROTATE180);
                        out.append(page.getRight());
                        out.append(' ');
                        out.append(page.getTop());
                        out.append(PdfContents.ROTATEFINAL);
                        return;
                    case 270:
                        out.append(PdfContents.ROTATE270);
                        out.append('0').append(' ');
                        out.append(page.getRight());
                        out.append(PdfContents.ROTATEFINAL);
                        return;
                    default:
                        return;
                }
            }
        }

        private void addDocumentField(PdfIndirectReference ref) {
            if (this.cstp.fieldArray == null) {
                this.cstp.fieldArray = new PdfArray();
            }
            this.cstp.fieldArray.add(ref);
        }

        private void expandFields(PdfFormField field, ArrayList<PdfAnnotation> allAnnots) {
            allAnnots.add(field);
            ArrayList<PdfFormField> kids = field.getKids();
            if (kids != null) {
                Iterator i$ = kids.iterator();
                while (i$.hasNext()) {
                    expandFields((PdfFormField) i$.next(), allAnnots);
                }
            }
        }

        public void addAnnotation(PdfAnnotation annot) {
            try {
                PdfFormField field;
                ArrayList<PdfAnnotation> allAnnots = new ArrayList();
                if (annot.isForm()) {
                    field = (PdfFormField) annot;
                    if (field.getParent() == null) {
                        expandFields(field, allAnnots);
                        if (this.cstp.fieldTemplates == null) {
                            this.cstp.fieldTemplates = new HashSet();
                        }
                    } else {
                        return;
                    }
                }
                allAnnots.add(annot);
                for (int k = 0; k < allAnnots.size(); k++) {
                    PdfObject annot2 = (PdfAnnotation) allAnnots.get(k);
                    if (annot2.isForm()) {
                        if (!annot2.isUsed()) {
                            HashSet<PdfTemplate> templates = annot2.getTemplates();
                            if (templates != null) {
                                this.cstp.fieldTemplates.addAll(templates);
                            }
                        }
                        field = (PdfFormField) annot2;
                        if (field.getParent() == null) {
                            addDocumentField(field.getIndirectReference());
                        }
                    }
                    if (annot2.isAnnotation()) {
                        PdfArray annots;
                        PdfObject pdfobj = PdfReader.getPdfObject(this.pageN.get(PdfName.ANNOTS), this.pageN);
                        if (pdfobj == null || !pdfobj.isArray()) {
                            annots = new PdfArray();
                            this.pageN.put(PdfName.ANNOTS, annots);
                        } else {
                            annots = (PdfArray) pdfobj;
                        }
                        annots.add(annot2.getIndirectReference());
                        if (!annot2.isUsed()) {
                            PdfRectangle rect = (PdfRectangle) annot2.get(PdfName.RECT);
                            if (!(rect == null || (rect.left() == 0.0f && rect.right() == 0.0f && rect.top() == 0.0f && rect.bottom() == 0.0f))) {
                                int rotation = this.reader.getPageRotation(this.pageN);
                                Rectangle pageSize = this.reader.getPageSizeWithRotation(this.pageN);
                                switch (rotation) {
                                    case 90:
                                        annot2.put(PdfName.RECT, new PdfRectangle(pageSize.getTop() - rect.bottom(), rect.left(), pageSize.getTop() - rect.top(), rect.right()));
                                        break;
                                    case 180:
                                        annot2.put(PdfName.RECT, new PdfRectangle(pageSize.getRight() - rect.left(), pageSize.getTop() - rect.bottom(), pageSize.getRight() - rect.right(), pageSize.getTop() - rect.top()));
                                        break;
                                    case 270:
                                        annot2.put(PdfName.RECT, new PdfRectangle(rect.bottom(), pageSize.getRight() - rect.left(), rect.top(), pageSize.getRight() - rect.right()));
                                        break;
                                }
                            }
                        }
                    }
                    if (!annot2.isUsed()) {
                        annot2.setUsed();
                        this.cstp.addToBody(annot2, annot2.getIndirectReference());
                    }
                }
            } catch (IOException e) {
                throw new ExceptionConverter(e);
            }
        }
    }

    public static class StampContent extends PdfContentByte {
        PageResources pageResources;

        StampContent(PdfWriter writer, PageResources pageResources) {
            super(writer);
            this.pageResources = pageResources;
        }

        public PdfContentByte getDuplicate() {
            return new StampContent(this.writer, this.pageResources);
        }

        PageResources getPageResources() {
            return this.pageResources;
        }
    }

    protected void flushAcroFields() throws java.io.IOException, com.itextpdf.text.pdf.BadPdfFormatException {
        /* JADX: method processing error */
/*
Error: java.util.NoSuchElementException
	at java.util.HashMap$HashIterator.nextNode(HashMap.java:1431)
	at java.util.HashMap$KeyIterator.next(HashMap.java:1453)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.applyRemove(BlockFinallyExtract.java:535)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.extractFinally(BlockFinallyExtract.java:175)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.processExceptionHandler(BlockFinallyExtract.java:79)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:51)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r11 = this;
        r9 = r11.mergeFields;
        if (r9 == 0) goto L_0x006c;
    L_0x0004:
        r9 = r11.importedPages;	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
        r0 = r9.iterator();	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
    L_0x000a:
        r9 = r0.hasNext();	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
        if (r9 == 0) goto L_0x0092;	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
    L_0x0010:
        r4 = r0.next();	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
        r4 = (com.itextpdf.text.pdf.PdfCopy.ImportedPage) r4;	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
        r9 = r4.reader;	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
        r10 = r4.pageNumber;	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
        r5 = r9.getPageN(r10);	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
        if (r5 == 0) goto L_0x000a;	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
    L_0x0020:
        r9 = com.itextpdf.text.pdf.PdfName.ANNOTS;	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
        r6 = r5.getAsArray(r9);	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
        if (r6 == 0) goto L_0x000a;	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
    L_0x0028:
        r9 = r6.size();	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
        if (r9 == 0) goto L_0x000a;	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
    L_0x002e:
        r9 = r4.reader;	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
        r9 = r9.getAcroFields();	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
        r9 = r9.getFields();	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
        r9 = r9.values();	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
        r1 = r9.iterator();	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
    L_0x0040:
        r9 = r1.hasNext();	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
        if (r9 == 0) goto L_0x006d;	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
    L_0x0046:
        r3 = r1.next();	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
        r3 = (com.itextpdf.text.pdf.AcroFields.Item) r3;	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
        r9 = r3.widget_refs;	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
        r2 = r9.iterator();	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
    L_0x0052:
        r9 = r2.hasNext();	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
        if (r9 == 0) goto L_0x0040;	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
    L_0x0058:
        r8 = r2.next();	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
        r8 = (com.itextpdf.text.pdf.PdfIndirectReference) r8;	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
        r9 = r6.arrayList;	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
        r9.remove(r8);	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
        goto L_0x0052;
    L_0x0064:
        r9 = move-exception;
        r9 = r11.tagged;
        if (r9 != 0) goto L_0x006c;
    L_0x0069:
        r11.flushIndirectObjects();
    L_0x006c:
        return;
    L_0x006d:
        r9 = r6.arrayList;	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
        r1 = r9.iterator();	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
    L_0x0073:
        r9 = r1.hasNext();	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
        if (r9 == 0) goto L_0x000a;	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
    L_0x0079:
        r8 = r1.next();	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
        r8 = (com.itextpdf.text.pdf.PdfObject) r8;	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
        r9 = r4.mergedFields;	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
        r10 = r11.copyObject(r8);	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
        r9.add(r10);	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
        goto L_0x0073;
    L_0x0089:
        r9 = move-exception;
        r10 = r11.tagged;
        if (r10 != 0) goto L_0x0091;
    L_0x008e:
        r11.flushIndirectObjects();
    L_0x0091:
        throw r9;
    L_0x0092:
        r9 = r11.indirectMap;	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
        r9 = r9.keySet();	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
        r0 = r9.iterator();	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
    L_0x009c:
        r9 = r0.hasNext();	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
        if (r9 == 0) goto L_0x00ac;	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
    L_0x00a2:
        r7 = r0.next();	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
        r7 = (com.itextpdf.text.pdf.PdfReader) r7;	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
        r7.removeFields();	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
        goto L_0x009c;	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
    L_0x00ac:
        r11.mergeFields();	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
        r11.createAcroForms();	 Catch:{ ClassCastException -> 0x0064, all -> 0x0089 }
        r9 = r11.tagged;
        if (r9 != 0) goto L_0x006c;
    L_0x00b6:
        r11.flushIndirectObjects();
        goto L_0x006c;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.itextpdf.text.pdf.PdfCopy.flushAcroFields():void");
    }

    static {
        widgetKeys.add(PdfName.SUBTYPE);
        widgetKeys.add(PdfName.CONTENTS);
        widgetKeys.add(PdfName.RECT);
        widgetKeys.add(PdfName.NM);
        widgetKeys.add(PdfName.f127M);
        widgetKeys.add(PdfName.f122F);
        widgetKeys.add(PdfName.BS);
        widgetKeys.add(PdfName.BORDER);
        widgetKeys.add(PdfName.AP);
        widgetKeys.add(PdfName.AS);
        widgetKeys.add(PdfName.f119C);
        widgetKeys.add(PdfName.f117A);
        widgetKeys.add(PdfName.STRUCTPARENT);
        widgetKeys.add(PdfName.OC);
        widgetKeys.add(PdfName.f123H);
        widgetKeys.add(PdfName.MK);
        widgetKeys.add(PdfName.DA);
        widgetKeys.add(PdfName.f131Q);
        widgetKeys.add(PdfName.f130P);
        widgetKeys.add(PdfName.TYPE);
        widgetKeys.add(annotId);
        fieldKeys.add(PdfName.AA);
        fieldKeys.add(PdfName.FT);
        fieldKeys.add(PdfName.TU);
        fieldKeys.add(PdfName.TM);
        fieldKeys.add(PdfName.FF);
        fieldKeys.add(PdfName.f136V);
        fieldKeys.add(PdfName.DV);
        fieldKeys.add(PdfName.DS);
        fieldKeys.add(PdfName.RV);
        fieldKeys.add(PdfName.OPT);
        fieldKeys.add(PdfName.MAXLEN);
        fieldKeys.add(PdfName.TI);
        fieldKeys.add(PdfName.f124I);
        fieldKeys.add(PdfName.LOCK);
        fieldKeys.add(PdfName.SV);
    }

    protected Counter getCounter() {
        return COUNTER;
    }

    public PdfCopy(Document document, OutputStream os) throws DocumentException {
        super(new PdfDocument(), os);
        document.addDocListener(this.pdf);
        this.pdf.addWriter(this);
        this.indirectMap = new HashMap();
        this.parentObjects = new HashMap();
        this.disableIndirects = new HashSet();
        this.indirectObjects = new HashMap();
        this.savedObjects = new ArrayList();
        this.importedPages = new ArrayList();
    }

    public void setPageEvent(PdfPageEvent event) {
        throw new UnsupportedOperationException();
    }

    public boolean isRotateContents() {
        return this.rotateContents;
    }

    public void setRotateContents(boolean rotateContents) {
        this.rotateContents = rotateContents;
    }

    public void setMergeFields() {
        this.mergeFields = true;
        this.resources = new PdfDictionary();
        this.fields = new ArrayList();
        this.calculationOrder = new ArrayList();
        this.fieldTree = new HashMap();
        this.unmergedMap = new HashMap();
        this.unmergedSet = new HashSet();
        this.mergedMap = new HashMap();
        this.mergedSet = new HashSet();
    }

    public PdfImportedPage getImportedPage(PdfReader reader, int pageNumber) {
        if (!this.mergeFields || this.mergeFieldsInternalCall) {
            if (this.mergeFields) {
                this.importedPages.add(new ImportedPage(reader, pageNumber, this.mergeFields));
            }
            if (this.structTreeController != null) {
                this.structTreeController.reader = null;
            }
            this.disableIndirects.clear();
            this.parentObjects.clear();
            return getImportedPageImpl(reader, pageNumber);
        }
        throw new IllegalArgumentException(MessageLocalization.getComposedMessage("1.method.cannot.be.used.in.mergeFields.mode.please.use.addDocument", "getImportedPage"));
    }

    public PdfImportedPage getImportedPage(PdfReader reader, int pageNumber, boolean keepTaggedPdfStructure) throws BadPdfFormatException {
        if (!this.mergeFields || this.mergeFieldsInternalCall) {
            this.updateRootKids = false;
            if (keepTaggedPdfStructure) {
                if (this.structTreeController == null) {
                    this.structTreeController = new PdfStructTreeController(reader, this);
                } else if (reader != this.structTreeController.reader) {
                    this.structTreeController.setReader(reader);
                }
                ImportedPage newPage = new ImportedPage(reader, pageNumber, this.mergeFields);
                switch (checkStructureTreeRootKids(newPage)) {
                    case -1:
                        clearIndirects(reader);
                        this.updateRootKids = true;
                        break;
                    case 0:
                        this.updateRootKids = false;
                        break;
                    case 1:
                        this.updateRootKids = true;
                        break;
                }
                this.importedPages.add(newPage);
                this.disableIndirects.clear();
                this.parentObjects.clear();
                return getImportedPageImpl(reader, pageNumber);
            }
            if (this.mergeFields) {
                this.importedPages.add(new ImportedPage(reader, pageNumber, this.mergeFields));
            }
            return getImportedPageImpl(reader, pageNumber);
        }
        throw new IllegalArgumentException(MessageLocalization.getComposedMessage("1.method.cannot.be.used.in.mergeFields.mode.please.use.addDocument", "getImportedPage"));
    }

    private void clearIndirects(PdfReader reader) {
        HashMap<RefKey, IndirectReferences> currIndirects = (HashMap) this.indirectMap.get(reader);
        ArrayList<RefKey> forDelete = new ArrayList();
        for (Entry<RefKey, IndirectReferences> entry : currIndirects.entrySet()) {
            PdfIndirectObject iobj = (PdfIndirectObject) this.indirectObjects.get(new RefKey(((IndirectReferences) entry.getValue()).theRef));
            if (iobj == null) {
                forDelete.add(entry.getKey());
            } else if (iobj.object.isArray() || iobj.object.isDictionary() || iobj.object.isStream()) {
                forDelete.add(entry.getKey());
            }
        }
        Iterator i$ = forDelete.iterator();
        while (i$.hasNext()) {
            currIndirects.remove((RefKey) i$.next());
        }
    }

    private int checkStructureTreeRootKids(ImportedPage newPage) {
        if (this.importedPages.size() == 0) {
            return 1;
        }
        boolean readerExist = false;
        Iterator i$ = this.importedPages.iterator();
        while (i$.hasNext()) {
            if (((ImportedPage) i$.next()).reader.equals(newPage.reader)) {
                readerExist = true;
                break;
            }
        }
        if (!readerExist) {
            return 1;
        }
        ImportedPage lastPage = (ImportedPage) this.importedPages.get(this.importedPages.size() - 1);
        if (!lastPage.reader.equals(newPage.reader) || newPage.pageNumber <= lastPage.pageNumber) {
            return -1;
        }
        if (this.readersWithImportedStructureTreeRootKids.contains(newPage.reader)) {
            return 0;
        }
        return 1;
    }

    protected void structureTreeRootKidsForReaderImported(PdfReader reader) {
        this.readersWithImportedStructureTreeRootKids.add(reader);
    }

    protected void fixStructureTreeRoot(HashSet<RefKey> activeKeys, HashSet<PdfName> activeClassMaps) {
        HashMap<PdfName, PdfObject> newClassMap = new HashMap(activeClassMaps.size());
        Iterator i$ = activeClassMaps.iterator();
        while (i$.hasNext()) {
            PdfName key = (PdfName) i$.next();
            PdfObject cm = (PdfObject) this.structureTreeRoot.classes.get(key);
            if (cm != null) {
                newClassMap.put(key, cm);
            }
        }
        this.structureTreeRoot.classes = newClassMap;
        PdfArray kids = this.structureTreeRoot.getAsArray(PdfName.f125K);
        if (kids != null) {
            int i = 0;
            while (i < kids.size()) {
                if (!activeKeys.contains(new RefKey((PdfIndirectReference) kids.getPdfObject(i)))) {
                    int i2 = i - 1;
                    kids.remove(i);
                    i = i2;
                }
                i++;
            }
        }
    }

    protected PdfImportedPage getImportedPageImpl(PdfReader reader, int pageNumber) {
        if (this.currentPdfReaderInstance == null) {
            this.currentPdfReaderInstance = super.getPdfReaderInstance(reader);
        } else if (this.currentPdfReaderInstance.getReader() != reader) {
            this.currentPdfReaderInstance = super.getPdfReaderInstance(reader);
        }
        return this.currentPdfReaderInstance.getImportedPage(pageNumber);
    }

    protected PdfIndirectReference copyIndirect(PRIndirectReference in, boolean keepStructure, boolean directRootKids) throws IOException, BadPdfFormatException {
        RefKey key = new RefKey(in);
        IndirectReferences iRef = (IndirectReferences) this.indirects.get(key);
        PdfObject obj = PdfReader.getPdfObjectRelease((PdfObject) in);
        if (keepStructure && directRootKids && (obj instanceof PdfDictionary) && ((PdfDictionary) obj).contains(PdfName.PG)) {
            return null;
        }
        PdfIndirectReference theRef;
        if (iRef != null) {
            theRef = iRef.getRef();
            if (iRef.getCopied()) {
                return theRef;
            }
        }
        theRef = this.body.getPdfIndirectReference();
        iRef = new IndirectReferences(theRef);
        this.indirects.put(key, iRef);
        if (obj != null && obj.isDictionary()) {
            PdfObject type = PdfReader.getPdfObjectRelease(((PdfDictionary) obj).get(PdfName.TYPE));
            if (type != null && PdfName.PAGE.equals(type)) {
                return theRef;
            }
        }
        iRef.setCopied();
        if (obj != null) {
            this.parentObjects.put(obj, in);
        }
        PdfObject res = copyObject(obj, keepStructure, directRootKids);
        if (this.disableIndirects.contains(obj)) {
            iRef.setNotCopied();
        }
        if (res != null) {
            addToBody(res, theRef);
            return theRef;
        }
        this.indirects.remove(key);
        return null;
    }

    protected PdfIndirectReference copyIndirect(PRIndirectReference in) throws IOException, BadPdfFormatException {
        return copyIndirect(in, false, false);
    }

    protected PdfDictionary copyDictionary(PdfDictionary in, boolean keepStruct, boolean directRootKids) throws IOException, BadPdfFormatException {
        PdfDictionary out = new PdfDictionary();
        PdfObject type = PdfReader.getPdfObjectRelease(in.get(PdfName.TYPE));
        if (keepStruct) {
            if (directRootKids && in.contains(PdfName.PG)) {
                PdfObject curr = in;
                this.disableIndirects.add(curr);
                while (this.parentObjects.containsKey(curr) && !this.disableIndirects.contains(curr)) {
                    curr = (PdfObject) this.parentObjects.get(curr);
                    this.disableIndirects.add(curr);
                }
                return null;
            }
            this.structTreeController.addRole(in.getAsName(PdfName.f133S));
            this.structTreeController.addClass(in);
        }
        if (!(this.structTreeController == null || this.structTreeController.reader == null || (!in.contains(PdfName.STRUCTPARENTS) && !in.contains(PdfName.STRUCTPARENT)))) {
            PdfName key = PdfName.STRUCTPARENT;
            if (in.contains(PdfName.STRUCTPARENTS)) {
                key = PdfName.STRUCTPARENTS;
            }
            PdfObject value = in.get(key);
            out.put(key, new PdfNumber(this.currentStructArrayNumber));
            PdfStructTreeController pdfStructTreeController = this.structTreeController;
            PdfNumber pdfNumber = (PdfNumber) value;
            int i = this.currentStructArrayNumber;
            this.currentStructArrayNumber = i + 1;
            pdfStructTreeController.copyStructTreeForPage(pdfNumber, i);
        }
        for (PdfName key2 : in.getKeys()) {
            value = in.get(key2);
            if (this.structTreeController == null || this.structTreeController.reader == null || !(key2.equals(PdfName.STRUCTPARENTS) || key2.equals(PdfName.STRUCTPARENT))) {
                PdfObject res;
                if (!PdfName.PAGE.equals(type)) {
                    if (this.tagged && value.isIndirect() && isStructTreeRootReference((PRIndirectReference) value)) {
                        res = this.structureTreeRoot.getReference();
                    } else {
                        res = copyObject(value, keepStruct, directRootKids);
                    }
                    if (res != null) {
                        out.put(key2, res);
                    }
                } else if (!(key2.equals(PdfName.f118B) || key2.equals(PdfName.PARENT))) {
                    this.parentObjects.put(value, in);
                    res = copyObject(value, keepStruct, directRootKids);
                    if (res != null) {
                        out.put(key2, res);
                    }
                }
            }
        }
        return out;
    }

    protected PdfDictionary copyDictionary(PdfDictionary in) throws IOException, BadPdfFormatException {
        return copyDictionary(in, false, false);
    }

    protected PdfStream copyStream(PRStream in) throws IOException, BadPdfFormatException {
        PRStream out = new PRStream(in, null);
        for (PdfName key : in.getKeys()) {
            PdfObject value = in.get(key);
            this.parentObjects.put(value, in);
            PdfObject res = copyObject(value);
            if (res != null) {
                out.put(key, res);
            }
        }
        return out;
    }

    protected PdfArray copyArray(PdfArray in, boolean keepStruct, boolean directRootKids) throws IOException, BadPdfFormatException {
        PdfArray out = new PdfArray();
        Iterator<PdfObject> i = in.listIterator();
        while (i.hasNext()) {
            PdfObject value = (PdfObject) i.next();
            this.parentObjects.put(value, in);
            PdfObject res = copyObject(value, keepStruct, directRootKids);
            if (res != null) {
                out.add(res);
            }
        }
        return out;
    }

    protected PdfArray copyArray(PdfArray in) throws IOException, BadPdfFormatException {
        return copyArray(in, false, false);
    }

    protected PdfObject copyObject(PdfObject in, boolean keepStruct, boolean directRootKids) throws IOException, BadPdfFormatException {
        if (in == null) {
            return PdfNull.PDFNULL;
        }
        switch (in.type) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 8:
                return in;
            case 5:
                return copyArray((PdfArray) in, keepStruct, directRootKids);
            case 6:
                return copyDictionary((PdfDictionary) in, keepStruct, directRootKids);
            case 7:
                return copyStream((PRStream) in);
            case 10:
                if (keepStruct || directRootKids) {
                    return copyIndirect((PRIndirectReference) in, keepStruct, directRootKids);
                }
                return copyIndirect((PRIndirectReference) in);
            default:
                if (in.type < 0) {
                    String lit = ((PdfLiteral) in).toString();
                    if (lit.equals(PdfBoolean.TRUE) || lit.equals(PdfBoolean.FALSE)) {
                        return new PdfBoolean(lit);
                    }
                    return new PdfLiteral(lit);
                }
                System.out.println("CANNOT COPY type " + in.type);
                return null;
        }
    }

    protected PdfObject copyObject(PdfObject in) throws IOException, BadPdfFormatException {
        return copyObject(in, false, false);
    }

    protected int setFromIPage(PdfImportedPage iPage) {
        int pageNum = iPage.getPageNumber();
        PdfReaderInstance inst = iPage.getPdfReaderInstance();
        this.currentPdfReaderInstance = inst;
        this.reader = inst.getReader();
        setFromReader(this.reader);
        return pageNum;
    }

    protected void setFromReader(PdfReader reader) {
        this.reader = reader;
        this.indirects = (HashMap) this.indirectMap.get(reader);
        if (this.indirects == null) {
            this.indirects = new HashMap();
            this.indirectMap.put(reader, this.indirects);
        }
    }

    public void addPage(PdfImportedPage iPage) throws IOException, BadPdfFormatException {
        if (!this.mergeFields || this.mergeFieldsInternalCall) {
            int pageNum = setFromIPage(iPage);
            PdfDictionary thePage = this.reader.getPageN(pageNum);
            PRIndirectReference origRef = this.reader.getPageOrigRef(pageNum);
            this.reader.releasePage(pageNum);
            RefKey key = new RefKey(origRef);
            IndirectReferences iRef = (IndirectReferences) this.indirects.get(key);
            if (!(iRef == null || iRef.getCopied())) {
                this.pageReferences.add(iRef.getRef());
                iRef.setCopied();
            }
            PdfIndirectReference pageRef = getCurrentPage();
            if (iRef == null) {
                iRef = new IndirectReferences(pageRef);
                this.indirects.put(key, iRef);
            }
            iRef.setCopied();
            if (this.tagged) {
                this.structTreeRootReference = (PRIndirectReference) this.reader.getCatalog().get(PdfName.STRUCTTREEROOT);
            }
            PdfDictionary newPage = copyDictionary(thePage);
            if (this.mergeFields) {
                ImportedPage importedPage = (ImportedPage) this.importedPages.get(this.importedPages.size() - 1);
                importedPage.annotsIndirectReference = this.body.getPdfIndirectReference();
                newPage.put(PdfName.ANNOTS, importedPage.annotsIndirectReference);
            }
            this.root.addPage(newPage);
            iPage.setCopied();
            this.currentPageNumber++;
            this.pdf.setPageCount(this.currentPageNumber);
            this.structTreeRootReference = null;
            return;
        }
        throw new IllegalArgumentException(MessageLocalization.getComposedMessage("1.method.cannot.be.used.in.mergeFields.mode.please.use.addDocument", "addPage"));
    }

    public void addPage(Rectangle rect, int rotation) throws DocumentException {
        if (!this.mergeFields || this.mergeFieldsInternalCall) {
            PdfDictionary page = new PdfPage(new PdfRectangle(rect, rotation), new HashMap(), new PageResources().getResources(), 0);
            page.put(PdfName.TABS, getTabs());
            this.root.addPage(page);
            this.currentPageNumber++;
            this.pdf.setPageCount(this.currentPageNumber);
            return;
        }
        throw new IllegalArgumentException(MessageLocalization.getComposedMessage("1.method.cannot.be.used.in.mergeFields.mode.please.use.addDocument", "addPage"));
    }

    public void addDocument(PdfReader reader, List<Integer> pagesToKeep) throws DocumentException, IOException {
        if (this.indirectMap.containsKey(reader)) {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("document.1.has.already.been.added", reader.toString()));
        }
        reader.selectPages(pagesToKeep, false);
        addDocument(reader);
    }

    public void copyDocumentFields(PdfReader reader) throws DocumentException, IOException {
        if (!this.document.isOpen()) {
            throw new DocumentException(MessageLocalization.getComposedMessage("the.document.is.not.open.yet.you.can.only.add.meta.information", new Object[0]));
        } else if (this.indirectMap.containsKey(reader)) {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("document.1.has.already.been.added", reader.toString()));
        } else if (!reader.isOpenedWithFullPermissions()) {
            throw new BadPasswordException(MessageLocalization.getComposedMessage("pdfreader.not.opened.with.owner.password", new Object[0]));
        } else if (this.mergeFields) {
            this.indirects = new HashMap();
            this.indirectMap.put(reader, this.indirects);
            reader.consolidateNamedDestinations();
            reader.shuffleSubsetNames();
            if (this.tagged && PdfStructTreeController.checkTagged(reader)) {
                this.structTreeRootReference = (PRIndirectReference) reader.getCatalog().get(PdfName.STRUCTTREEROOT);
                if (this.structTreeController == null) {
                    this.structTreeController = new PdfStructTreeController(reader, this);
                } else if (reader != this.structTreeController.reader) {
                    this.structTreeController.setReader(reader);
                }
            }
            List<PdfObject> annotationsToBeCopied = new ArrayList();
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                PdfDictionary page = reader.getPageNRelease(i);
                if (page != null && page.contains(PdfName.ANNOTS)) {
                    PdfArray annots = page.getAsArray(PdfName.ANNOTS);
                    if (annots != null && annots.size() > 0) {
                        if (this.importedPages.size() < i) {
                            throw new DocumentException(MessageLocalization.getComposedMessage("there.are.not.enough.imported.pages.for.copied.fields", new Object[0]));
                        }
                        ((HashMap) this.indirectMap.get(reader)).put(new RefKey(reader.pageRefs.getPageOrigRef(i)), new IndirectReferences((PdfIndirectReference) this.pageReferences.get(i - 1)));
                        for (int j = 0; j < annots.size(); j++) {
                            PdfDictionary annot = annots.getAsDict(j);
                            if (annot != null) {
                                PdfName pdfName = annotId;
                                int i2 = annotIdCnt + 1;
                                annotIdCnt = i2;
                                annot.put(pdfName, new PdfNumber(i2));
                                annotationsToBeCopied.add(annots.getPdfObject(j));
                            }
                        }
                    }
                }
            }
            for (PdfObject annot2 : annotationsToBeCopied) {
                copyObject(annot2);
            }
            if (this.tagged && this.structTreeController != null) {
                this.structTreeController.attachStructTreeRootKids(null);
            }
            AcroFields acro = reader.getAcroFields();
            if (!acro.isGenerateAppearances()) {
                this.needAppearances = true;
            }
            this.fields.add(acro);
            updateCalculationOrder(reader);
            this.structTreeRootReference = null;
        } else {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("1.method.can.be.only.used.in.mergeFields.mode.please.use.addDocument", "copyDocumentFields"));
        }
    }

    public void addDocument(PdfReader reader) throws DocumentException, IOException {
        if (!this.document.isOpen()) {
            throw new DocumentException(MessageLocalization.getComposedMessage("the.document.is.not.open.yet.you.can.only.add.meta.information", new Object[0]));
        } else if (this.indirectMap.containsKey(reader)) {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("document.1.has.already.been.added", reader.toString()));
        } else if (reader.isOpenedWithFullPermissions()) {
            int i;
            boolean tagged;
            if (this.mergeFields) {
                boolean needapp;
                reader.consolidateNamedDestinations();
                reader.shuffleSubsetNames();
                for (i = 1; i <= reader.getNumberOfPages(); i++) {
                    PdfDictionary page = reader.getPageNRelease(i);
                    if (page != null && page.contains(PdfName.ANNOTS)) {
                        PdfArray annots = page.getAsArray(PdfName.ANNOTS);
                        if (annots != null) {
                            for (int j = 0; j < annots.size(); j++) {
                                PdfDictionary annot = annots.getAsDict(j);
                                if (annot != null) {
                                    PdfName pdfName = annotId;
                                    int i2 = annotIdCnt + 1;
                                    annotIdCnt = i2;
                                    annot.put(pdfName, new PdfNumber(i2));
                                }
                            }
                        }
                    }
                }
                AcroFields acro = reader.getAcroFields();
                if (acro.isGenerateAppearances()) {
                    needapp = false;
                } else {
                    needapp = true;
                }
                if (needapp) {
                    this.needAppearances = true;
                }
                this.fields.add(acro);
                updateCalculationOrder(reader);
            }
            if (this.tagged && PdfStructTreeController.checkTagged(reader)) {
                tagged = true;
            } else {
                tagged = false;
            }
            this.mergeFieldsInternalCall = true;
            for (i = 1; i <= reader.getNumberOfPages(); i++) {
                addPage(getImportedPage(reader, i, tagged));
            }
            this.mergeFieldsInternalCall = false;
        } else {
            throw new BadPasswordException(MessageLocalization.getComposedMessage("pdfreader.not.opened.with.owner.password", new Object[0]));
        }
    }

    public PdfIndirectObject addToBody(PdfObject object, PdfIndirectReference ref) throws IOException {
        return addToBody(object, ref, false);
    }

    public PdfIndirectObject addToBody(PdfObject object, PdfIndirectReference ref, boolean formBranching) throws IOException {
        PdfIndirectObject iobj;
        if (formBranching) {
            updateReferences(object);
        }
        if ((this.tagged || this.mergeFields) && this.indirectObjects != null && (object.isArray() || object.isDictionary() || object.isStream() || object.isNull())) {
            RefKey key = new RefKey(ref);
            PdfIndirectObject obj = (PdfIndirectObject) this.indirectObjects.get(key);
            if (obj == null) {
                obj = new PdfIndirectObject(ref, object, (PdfWriter) this);
                this.indirectObjects.put(key, obj);
            }
            iobj = obj;
        } else {
            iobj = super.addToBody(object, ref);
        }
        if (this.mergeFields && object.isDictionary()) {
            PdfNumber annotId = ((PdfDictionary) object).getAsNumber(annotId);
            if (annotId != null) {
                if (formBranching) {
                    this.mergedMap.put(Integer.valueOf(annotId.intValue()), iobj);
                    this.mergedSet.add(iobj);
                } else {
                    this.unmergedMap.put(Integer.valueOf(annotId.intValue()), iobj);
                    this.unmergedSet.add(iobj);
                }
            }
        }
        return iobj;
    }

    protected void cacheObject(PdfIndirectObject iobj) {
        if ((this.tagged || this.mergeFields) && this.indirectObjects != null) {
            this.savedObjects.add(iobj);
            RefKey key = new RefKey(iobj.number, iobj.generation);
            if (!this.indirectObjects.containsKey(key)) {
                this.indirectObjects.put(key, iobj);
            }
        }
    }

    protected void flushTaggedObjects() throws IOException {
        try {
            fixTaggedStructure();
        } catch (ClassCastException e) {
        } finally {
            flushIndirectObjects();
        }
    }

    protected void fixTaggedStructure() throws IOException {
        HashMap<Integer, PdfIndirectReference> numTree = this.structureTreeRoot.getNumTree();
        HashSet<RefKey> activeKeys = new HashSet();
        ArrayList<PdfIndirectReference> actives = new ArrayList();
        if (this.mergeFields && this.acroForm != null) {
            actives.add(this.acroForm);
            activeKeys.add(new RefKey(this.acroForm));
        }
        Iterator i$ = this.pageReferences.iterator();
        while (i$.hasNext()) {
            PdfIndirectReference page = (PdfIndirectReference) i$.next();
            actives.add(page);
            activeKeys.add(new RefKey(page));
        }
        int i = numTree.size() - 1;
        int pageRefIndex = 0;
        while (i >= 0) {
            int pageRefIndex2;
            PdfIndirectReference currNum = (PdfIndirectReference) numTree.get(Integer.valueOf(i));
            RefKey refKey = new RefKey(currNum);
            PdfObject obj = ((PdfIndirectObject) this.indirectObjects.get(refKey)).object;
            if (obj.isDictionary()) {
                boolean addActiveKeys = false;
                if (this.pageReferences.contains(((PdfDictionary) obj).get(PdfName.PG))) {
                    addActiveKeys = true;
                } else {
                    PdfDictionary k = PdfStructTreeController.getKDict((PdfDictionary) obj);
                    if (k != null && this.pageReferences.contains(k.get(PdfName.PG))) {
                        addActiveKeys = true;
                    }
                }
                if (addActiveKeys) {
                    activeKeys.add(refKey);
                    actives.add(currNum);
                } else {
                    numTree.remove(Integer.valueOf(i));
                }
                pageRefIndex2 = pageRefIndex;
            } else if (obj.isArray()) {
                activeKeys.add(refKey);
                actives.add(currNum);
                PdfArray currNums = (PdfArray) obj;
                pageRefIndex2 = pageRefIndex + 1;
                PdfIndirectReference currPage = (PdfIndirectReference) this.pageReferences.get(pageRefIndex);
                actives.add(currPage);
                activeKeys.add(new RefKey(currPage));
                PdfIndirectReference prevKid = null;
                for (int j = 0; j < currNums.size(); j++) {
                    PdfIndirectReference currKid = (PdfIndirectReference) currNums.getDirectObject(j);
                    if (!currKid.equals(prevKid)) {
                        refKey = new RefKey(currKid);
                        activeKeys.add(refKey);
                        actives.add(currKid);
                        PdfIndirectObject iobj = (PdfIndirectObject) this.indirectObjects.get(refKey);
                        if (iobj.object.isDictionary()) {
                            PdfDictionary dict = iobj.object;
                            PdfIndirectReference pg = (PdfIndirectReference) dict.get(PdfName.PG);
                            if (!(pg == null || this.pageReferences.contains(pg) || pg.equals(currPage))) {
                                dict.put(PdfName.PG, currPage);
                                PdfArray kids = dict.getAsArray(PdfName.f125K);
                                if (kids != null && kids.getDirectObject(0).isNumber()) {
                                    kids.remove(0);
                                }
                            }
                        }
                        prevKid = currKid;
                    }
                }
            } else {
                pageRefIndex2 = pageRefIndex;
            }
            i--;
            pageRefIndex = pageRefIndex2;
        }
        HashSet<PdfName> activeClassMaps = new HashSet();
        findActives(actives, activeKeys, activeClassMaps);
        fixPgKey(findActiveParents(activeKeys), activeKeys);
        fixStructureTreeRoot(activeKeys, activeClassMaps);
        for (Entry<RefKey, PdfIndirectObject> entry : this.indirectObjects.entrySet()) {
            if (!activeKeys.contains(entry.getKey())) {
                entry.setValue(null);
            } else if (((PdfIndirectObject) entry.getValue()).object.isArray()) {
                removeInactiveReferences((PdfArray) ((PdfIndirectObject) entry.getValue()).object, activeKeys);
            } else if (((PdfIndirectObject) entry.getValue()).object.isDictionary()) {
                PdfObject kids2 = ((PdfDictionary) ((PdfIndirectObject) entry.getValue()).object).get(PdfName.f125K);
                if (kids2 != null && kids2.isArray()) {
                    removeInactiveReferences((PdfArray) kids2, activeKeys);
                }
            }
        }
    }

    private void removeInactiveReferences(PdfArray array, HashSet<RefKey> activeKeys) {
        int i = 0;
        while (i < array.size()) {
            PdfObject obj = array.getPdfObject(i);
            if ((obj.type() == 0 && !activeKeys.contains(new RefKey((PdfIndirectReference) obj))) || (obj.isDictionary() && containsInactivePg((PdfDictionary) obj, activeKeys))) {
                int i2 = i - 1;
                array.remove(i);
                i = i2;
            }
            i++;
        }
    }

    private boolean containsInactivePg(PdfDictionary dict, HashSet<RefKey> activeKeys) {
        PdfObject pg = dict.get(PdfName.PG);
        if (pg == null || activeKeys.contains(new RefKey((PdfIndirectReference) pg))) {
            return false;
        }
        return true;
    }

    private ArrayList<PdfIndirectReference> findActiveParents(HashSet<RefKey> activeKeys) {
        ArrayList<PdfIndirectReference> newRefs = new ArrayList();
        ArrayList<RefKey> tmpActiveKeys = new ArrayList(activeKeys);
        for (int i = 0; i < tmpActiveKeys.size(); i++) {
            PdfIndirectObject iobj = (PdfIndirectObject) this.indirectObjects.get(tmpActiveKeys.get(i));
            if (iobj != null && iobj.object.isDictionary()) {
                PdfObject parent = ((PdfDictionary) iobj.object).get(PdfName.f130P);
                if (parent != null && parent.type() == 0) {
                    RefKey key = new RefKey((PdfIndirectReference) parent);
                    if (!activeKeys.contains(key)) {
                        activeKeys.add(key);
                        tmpActiveKeys.add(key);
                        newRefs.add((PdfIndirectReference) parent);
                    }
                }
            }
        }
        return newRefs;
    }

    private void fixPgKey(ArrayList<PdfIndirectReference> newRefs, HashSet<RefKey> activeKeys) {
        Iterator i$ = newRefs.iterator();
        while (i$.hasNext()) {
            PdfIndirectObject iobj = (PdfIndirectObject) this.indirectObjects.get(new RefKey((PdfIndirectReference) i$.next()));
            if (iobj != null && iobj.object.isDictionary()) {
                PdfDictionary dict = iobj.object;
                PdfObject pg = dict.get(PdfName.PG);
                if (pg != null && !activeKeys.contains(new RefKey((PdfIndirectReference) pg))) {
                    PdfArray kids = dict.getAsArray(PdfName.f125K);
                    if (kids != null) {
                        int i = 0;
                        while (i < kids.size()) {
                            PdfObject obj = kids.getPdfObject(i);
                            if (obj.type() == 0) {
                                PdfIndirectObject kid = (PdfIndirectObject) this.indirectObjects.get(new RefKey((PdfIndirectReference) obj));
                                if (kid != null && kid.object.isDictionary()) {
                                    PdfObject kidPg = ((PdfDictionary) kid.object).get(PdfName.PG);
                                    if (kidPg != null && activeKeys.contains(new RefKey((PdfIndirectReference) kidPg))) {
                                        dict.put(PdfName.PG, kidPg);
                                        break;
                                    }
                                }
                            }
                            int i2 = i - 1;
                            kids.remove(i);
                            i = i2;
                            i++;
                        }
                    }
                }
            }
        }
    }

    private void findActives(ArrayList<PdfIndirectReference> actives, HashSet<RefKey> activeKeys, HashSet<PdfName> activeClassMaps) {
        for (int i = 0; i < actives.size(); i++) {
            PdfIndirectObject iobj = (PdfIndirectObject) this.indirectObjects.get(new RefKey((PdfIndirectReference) actives.get(i)));
            if (!(iobj == null || iobj.object == null)) {
                switch (iobj.object.type()) {
                    case 0:
                        findActivesFromReference((PdfIndirectReference) iobj.object, actives, activeKeys);
                        break;
                    case 5:
                        findActivesFromArray((PdfArray) iobj.object, actives, activeKeys, activeClassMaps);
                        break;
                    case 6:
                    case 7:
                        findActivesFromDict((PdfDictionary) iobj.object, actives, activeKeys, activeClassMaps);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void findActivesFromReference(PdfIndirectReference iref, ArrayList<PdfIndirectReference> actives, HashSet<RefKey> activeKeys) {
        RefKey key = new RefKey(iref);
        PdfIndirectObject iobj = (PdfIndirectObject) this.indirectObjects.get(key);
        if ((iobj == null || !iobj.object.isDictionary() || !containsInactivePg((PdfDictionary) iobj.object, activeKeys)) && !activeKeys.contains(key)) {
            activeKeys.add(key);
            actives.add(iref);
        }
    }

    private void findActivesFromArray(PdfArray array, ArrayList<PdfIndirectReference> actives, HashSet<RefKey> activeKeys, HashSet<PdfName> activeClassMaps) {
        Iterator i$ = array.iterator();
        while (i$.hasNext()) {
            PdfObject obj = (PdfObject) i$.next();
            switch (obj.type()) {
                case 0:
                    findActivesFromReference((PdfIndirectReference) obj, actives, activeKeys);
                    break;
                case 5:
                    findActivesFromArray((PdfArray) obj, actives, activeKeys, activeClassMaps);
                    break;
                case 6:
                case 7:
                    findActivesFromDict((PdfDictionary) obj, actives, activeKeys, activeClassMaps);
                    break;
                default:
                    break;
            }
        }
    }

    private void findActivesFromDict(PdfDictionary dict, ArrayList<PdfIndirectReference> actives, HashSet<RefKey> activeKeys, HashSet<PdfName> activeClassMaps) {
        if (!containsInactivePg(dict, activeKeys)) {
            for (PdfName key : dict.getKeys()) {
                PdfObject obj = dict.get(key);
                if (!key.equals(PdfName.f130P)) {
                    if (!key.equals(PdfName.f119C)) {
                        switch (obj.type()) {
                            case 0:
                                findActivesFromReference((PdfIndirectReference) obj, actives, activeKeys);
                                break;
                            case 5:
                                findActivesFromArray((PdfArray) obj, actives, activeKeys, activeClassMaps);
                                break;
                            case 6:
                            case 7:
                                findActivesFromDict((PdfDictionary) obj, actives, activeKeys, activeClassMaps);
                                break;
                            default:
                                break;
                        }
                    } else if (obj.isArray()) {
                        Iterator i$ = ((PdfArray) obj).iterator();
                        while (i$.hasNext()) {
                            PdfObject cm = (PdfObject) i$.next();
                            if (cm.isName()) {
                                activeClassMaps.add((PdfName) cm);
                            }
                        }
                    } else if (obj.isName()) {
                        activeClassMaps.add((PdfName) obj);
                    }
                }
            }
        }
    }

    protected void flushIndirectObjects() throws IOException {
        Iterator i$ = this.savedObjects.iterator();
        while (i$.hasNext()) {
            PdfIndirectObject iobj = (PdfIndirectObject) i$.next();
            this.indirectObjects.remove(new RefKey(iobj.number, iobj.generation));
        }
        HashSet<RefKey> inactives = new HashSet();
        for (Entry<RefKey, PdfIndirectObject> entry : this.indirectObjects.entrySet()) {
            if (entry.getValue() != null) {
                writeObjectToBody((PdfIndirectObject) entry.getValue());
            } else {
                inactives.add(entry.getKey());
            }
        }
        i$ = new ArrayList(this.body.xrefs).iterator();
        while (i$.hasNext()) {
            PdfCrossReference cr = (PdfCrossReference) i$.next();
            if (inactives.contains(new RefKey(cr.getRefnum(), 0))) {
                this.body.xrefs.remove(cr);
            }
        }
        this.indirectObjects = null;
    }

    private void writeObjectToBody(PdfIndirectObject object) throws IOException {
        PdfDictionary dictionary;
        PdfNumber annotId;
        boolean skipWriting = false;
        if (this.mergeFields) {
            updateAnnotationReferences(object.object);
            if (object.object.isDictionary() || object.object.isStream()) {
                dictionary = object.object;
                if (this.unmergedSet.contains(object)) {
                    annotId = dictionary.getAsNumber(annotId);
                    if (annotId != null && this.mergedMap.containsKey(Integer.valueOf(annotId.intValue()))) {
                        skipWriting = true;
                    }
                }
                if (this.mergedSet.contains(object)) {
                    annotId = dictionary.getAsNumber(annotId);
                    if (annotId != null) {
                        PdfIndirectObject unmerged = (PdfIndirectObject) this.unmergedMap.get(Integer.valueOf(annotId.intValue()));
                        if (unmerged != null && unmerged.object.isDictionary()) {
                            PdfNumber structParent = ((PdfDictionary) unmerged.object).getAsNumber(PdfName.STRUCTPARENT);
                            if (structParent != null) {
                                dictionary.put(PdfName.STRUCTPARENT, structParent);
                            }
                        }
                    }
                }
            }
        }
        if (!skipWriting) {
            dictionary = null;
            annotId = null;
            if (this.mergeFields && object.object.isDictionary()) {
                dictionary = object.object;
                annotId = dictionary.getAsNumber(annotId);
                if (annotId != null) {
                    dictionary.remove(annotId);
                }
            }
            this.body.add(object.object, object.number, object.generation, true);
            if (annotId != null) {
                dictionary.put(annotId, annotId);
            }
        }
    }

    private void updateAnnotationReferences(PdfObject obj) {
        PdfObject o;
        Iterator i$;
        PdfIndirectObject entry;
        PdfNumber annotId;
        PdfIndirectObject merged;
        if (obj.isArray()) {
            PdfArray array = (PdfArray) obj;
            for (int i = 0; i < array.size(); i++) {
                o = array.getPdfObject(i);
                if (o == null || o.type() != 0) {
                    updateAnnotationReferences(o);
                } else {
                    i$ = this.unmergedSet.iterator();
                    while (i$.hasNext()) {
                        entry = (PdfIndirectObject) i$.next();
                        if (entry.getIndirectReference().getNumber() == ((PdfIndirectReference) o).getNumber() && entry.getIndirectReference().getGeneration() == ((PdfIndirectReference) o).getGeneration() && entry.object.isDictionary()) {
                            annotId = ((PdfDictionary) entry.object).getAsNumber(annotId);
                            if (annotId != null) {
                                merged = (PdfIndirectObject) this.mergedMap.get(Integer.valueOf(annotId.intValue()));
                                if (merged != null) {
                                    array.set(i, merged.getIndirectReference());
                                }
                            }
                        }
                    }
                }
            }
        } else if (obj.isDictionary() || obj.isStream()) {
            PdfDictionary dictionary = (PdfDictionary) obj;
            for (PdfName key : dictionary.getKeys()) {
                o = dictionary.get(key);
                if (o == null || o.type() != 0) {
                    updateAnnotationReferences(o);
                } else {
                    Iterator i$2 = this.unmergedSet.iterator();
                    while (i$2.hasNext()) {
                        entry = (PdfIndirectObject) i$2.next();
                        if (entry.getIndirectReference().getNumber() == ((PdfIndirectReference) o).getNumber() && entry.getIndirectReference().getGeneration() == ((PdfIndirectReference) o).getGeneration() && entry.object.isDictionary()) {
                            annotId = ((PdfDictionary) entry.object).getAsNumber(annotId);
                            if (annotId != null) {
                                merged = (PdfIndirectObject) this.mergedMap.get(Integer.valueOf(annotId.intValue()));
                                if (merged != null) {
                                    dictionary.put(key, merged.getIndirectReference());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void updateCalculationOrder(PdfReader reader) {
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

    private static String getCOName(PdfReader reader, PRIndirectReference ref) {
        String name = "";
        while (ref != null) {
            PdfObject obj = PdfReader.getPdfObject(ref);
            if (obj == null || obj.type() != 6) {
                break;
            }
            PdfDictionary dic = (PdfDictionary) obj;
            PdfString t = dic.getAsString(PdfName.f134T);
            if (t != null) {
                name = t.toUnicodeString() + "." + name;
            }
            ref = (PRIndirectReference) dic.get(PdfName.PARENT);
        }
        if (name.endsWith(".")) {
            return name.substring(0, name.length() - 2);
        }
        return name;
    }

    private void mergeFields() {
        int pageOffset = 0;
        for (int k = 0; k < this.fields.size(); k++) {
            AcroFields af = (AcroFields) this.fields.get(k);
            Map<String, Item> fd = af.getFields();
            if (pageOffset < this.importedPages.size() && ((ImportedPage) this.importedPages.get(pageOffset)).reader == af.reader) {
                addPageOffsetToField(fd, pageOffset);
                pageOffset += af.reader.getNumberOfPages();
            }
            mergeWithMaster(fd);
        }
    }

    private void addPageOffsetToField(Map<String, Item> fd, int pageOffset) {
        if (pageOffset != 0) {
            for (Item item : fd.values()) {
                for (int k = 0; k < item.size(); k++) {
                    item.forcePage(k, item.getPage(k).intValue() + pageOffset);
                }
            }
        }
    }

    private void mergeWithMaster(Map<String, Item> fd) {
        for (Entry<String, Item> entry : fd.entrySet()) {
            mergeField((String) entry.getKey(), (Item) entry.getValue());
        }
    }

    private void mergeField(String name, Item item) {
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
                        if (fieldKeys.contains(key)) {
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

    private void createWidgets(ArrayList<Object> list, Item item) {
        for (int k = 0; k < item.size(); k++) {
            list.add(item.getPage(k));
            PdfDictionary merged = item.getMerged(k);
            PdfObject dr = merged.get(PdfName.DR);
            if (dr != null) {
                PdfFormField.mergeResources(this.resources, (PdfDictionary) PdfReader.getPdfObject(dr));
            }
            PdfDictionary widget = new PdfDictionary();
            for (PdfName key : merged.getKeys()) {
                if (widgetKeys.contains(key)) {
                    widget.put(key, merged.get(key));
                }
            }
            widget.put(iTextTag, new PdfNumber(item.getTabOrder(k).intValue() + 1));
            list.add(widget);
        }
    }

    private PdfObject propagate(PdfObject obj) throws IOException {
        if (obj == null) {
            return new PdfNull();
        }
        if (obj.isArray()) {
            PdfArray a = (PdfArray) obj;
            for (int i = 0; i < a.size(); i++) {
                a.set(i, propagate(a.getPdfObject(i)));
            }
            return a;
        } else if (!obj.isDictionary() && !obj.isStream()) {
            return obj.isIndirect() ? addToBody(propagate(PdfReader.getPdfObject(obj))).getIndirectReference() : obj;
        } else {
            PdfObject d = (PdfDictionary) obj;
            for (PdfName key : d.getKeys()) {
                d.put(key, propagate(d.get(key)));
            }
            return d;
        }
    }

    private void createAcroForms() throws IOException, BadPdfFormatException {
        Iterator i$;
        ImportedPage importedPage;
        if (this.fieldTree.isEmpty()) {
            i$ = this.importedPages.iterator();
            while (i$.hasNext()) {
                importedPage = (ImportedPage) i$.next();
                if (importedPage.mergedFields.size() > 0) {
                    addToBody(importedPage.mergedFields, importedPage.annotsIndirectReference);
                }
            }
            return;
        }
        PdfDictionary form = new PdfDictionary();
        form.put(PdfName.DR, propagate(this.resources));
        if (this.needAppearances) {
            form.put(PdfName.NEEDAPPEARANCES, PdfBoolean.PDFTRUE);
        }
        form.put(PdfName.DA, new PdfString("/Helv 0 Tf 0 g "));
        this.tabOrder = new HashMap();
        this.calculationOrderRefs = new ArrayList(this.calculationOrder);
        form.put(PdfName.FIELDS, branchForm(this.fieldTree, null, ""));
        if (this.hasSignature) {
            form.put(PdfName.SIGFLAGS, new PdfNumber(3));
        }
        PdfArray co = new PdfArray();
        for (int k = 0; k < this.calculationOrderRefs.size(); k++) {
            Object obj = this.calculationOrderRefs.get(k);
            if (obj instanceof PdfIndirectReference) {
                co.add((PdfIndirectReference) obj);
            }
        }
        if (co.size() > 0) {
            form.put(PdfName.CO, co);
        }
        this.acroForm = addToBody(form).getIndirectReference();
        i$ = this.importedPages.iterator();
        while (i$.hasNext()) {
            importedPage = (ImportedPage) i$.next();
            addToBody(importedPage.mergedFields, importedPage.annotsIndirectReference);
        }
    }

    private void updateReferences(PdfObject obj) {
        PdfObject o;
        IndirectReferences indRef;
        if (obj.isDictionary() || obj.isStream()) {
            PdfDictionary dictionary = (PdfDictionary) obj;
            for (PdfName key : dictionary.getKeys()) {
                o = dictionary.get(key);
                if (o.isIndirect()) {
                    indRef = (IndirectReferences) ((HashMap) this.indirectMap.get(((PRIndirectReference) o).getReader())).get(new RefKey((PRIndirectReference) o));
                    if (indRef != null) {
                        dictionary.put(key, indRef.getRef());
                    }
                } else {
                    updateReferences(o);
                }
            }
        } else if (obj.isArray()) {
            PdfArray array = (PdfArray) obj;
            for (int i = 0; i < array.size(); i++) {
                o = array.getPdfObject(i);
                if (o.isIndirect()) {
                    indRef = (IndirectReferences) ((HashMap) this.indirectMap.get(((PRIndirectReference) o).getReader())).get(new RefKey((PRIndirectReference) o));
                    if (indRef != null) {
                        array.set(i, indRef.getRef());
                    }
                } else {
                    updateReferences(o);
                }
            }
        }
    }

    private PdfArray branchForm(HashMap<String, Object> level, PdfIndirectReference parent, String fname) throws IOException, BadPdfFormatException {
        PdfArray arr = new PdfArray();
        for (Entry<String, Object> entry : level.entrySet()) {
            String name = (String) entry.getKey();
            ArrayList<Object> obj = entry.getValue();
            PdfObject ind = getPdfIndirectReference();
            PdfDictionary dic = new PdfDictionary();
            if (parent != null) {
                dic.put(PdfName.PARENT, parent);
            }
            dic.put(PdfName.f134T, new PdfString(name, PdfObject.TEXT_UNICODE));
            String fname2 = fname + "." + name;
            int coidx = this.calculationOrder.indexOf(fname2);
            if (coidx >= 0) {
                this.calculationOrderRefs.set(coidx, ind);
            }
            if (obj instanceof HashMap) {
                dic.put(PdfName.KIDS, branchForm((HashMap) obj, ind, fname2));
                arr.add(ind);
                addToBody(dic, ind, true);
            } else {
                ArrayList<Object> list = obj;
                dic.mergeDifferent((PdfDictionary) list.get(0));
                PdfArray annots;
                PdfNumber nn;
                if (list.size() == 3) {
                    dic.mergeDifferent((PdfDictionary) list.get(2));
                    annots = ((ImportedPage) this.importedPages.get(((Integer) list.get(1)).intValue() - 1)).mergedFields;
                    nn = (PdfNumber) dic.get(iTextTag);
                    dic.remove(iTextTag);
                    dic.put(PdfName.TYPE, PdfName.ANNOT);
                    adjustTabOrder(annots, ind, nn);
                } else {
                    PdfDictionary field = (PdfDictionary) list.get(0);
                    PdfObject kids = new PdfArray();
                    for (int k = 1; k < list.size(); k += 2) {
                        annots = ((ImportedPage) this.importedPages.get(((Integer) list.get(k)).intValue() - 1)).mergedFields;
                        PdfObject widget = new PdfDictionary();
                        widget.merge((PdfDictionary) list.get(k + 1));
                        widget.put(PdfName.PARENT, ind);
                        nn = (PdfNumber) widget.get(iTextTag);
                        widget.remove(iTextTag);
                        if (isTextField(field)) {
                            PdfString v = field.getAsString(PdfName.f136V);
                            PdfObject ap = widget.getDirectObject(PdfName.AP);
                            if (!(v == null || ap == null)) {
                                if (this.mergedTextFields.containsKey(list)) {
                                    try {
                                        BaseField textField = new TextField(this, null, null);
                                        ((AcroFields) this.fields.get(0)).decodeGenericDictionary(widget, textField);
                                        Rectangle box = PdfReader.getNormalizedRectangle(widget.getAsArray(PdfName.RECT));
                                        if (textField.getRotation() == 90 || textField.getRotation() == 270) {
                                            box = box.rotate();
                                        }
                                        textField.setBox(box);
                                        textField.setText(((PdfString) this.mergedTextFields.get(list)).toUnicodeString());
                                        PdfAppearance app = textField.getAppearance();
                                        ((PdfDictionary) ap).put(PdfName.f128N, app.getIndirectReference());
                                    } catch (DocumentException e) {
                                    }
                                } else {
                                    this.mergedTextFields.put(list, v);
                                }
                            }
                        } else if (isCheckButton(field)) {
                            v = field.getAsName(PdfName.f136V);
                            as = widget.getAsName(PdfName.AS);
                            if (!(v == null || as == null)) {
                                widget.put(PdfName.AS, v);
                            }
                        } else if (isRadioButton(field)) {
                            v = field.getAsName(PdfName.f136V);
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
                        widget.put(PdfName.TYPE, PdfName.ANNOT);
                        PdfObject wref = addToBody(widget, getPdfIndirectReference(), true).getIndirectReference();
                        adjustTabOrder(annots, wref, nn);
                        kids.add(wref);
                    }
                    dic.put(PdfName.KIDS, kids);
                }
                arr.add(ind);
                addToBody(dic, ind, true);
            }
        }
        return arr;
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
            annots.add(ind);
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

    protected PdfDictionary getCatalog(PdfIndirectReference rootObj) {
        try {
            PdfDictionary theCat = this.pdf.getCatalog(rootObj);
            buildStructTreeRootForTagged(theCat);
            if (this.fieldArray != null) {
                addFieldResources(theCat);
            } else if (this.mergeFields && this.acroForm != null) {
                theCat.put(PdfName.ACROFORM, this.acroForm);
            }
            return theCat;
        } catch (IOException e) {
            throw new ExceptionConverter(e);
        }
    }

    protected boolean isStructTreeRootReference(PdfIndirectReference prRef) {
        if (prRef == null || this.structTreeRootReference == null || prRef.number != this.structTreeRootReference.number || prRef.generation != this.structTreeRootReference.generation) {
            return false;
        }
        return true;
    }

    private void addFieldResources(PdfDictionary catalog) throws IOException {
        if (this.fieldArray != null) {
            PdfDictionary acroForm = new PdfDictionary();
            catalog.put(PdfName.ACROFORM, acroForm);
            acroForm.put(PdfName.FIELDS, this.fieldArray);
            acroForm.put(PdfName.DA, new PdfString("/Helv 0 Tf 0 g "));
            if (!this.fieldTemplates.isEmpty()) {
                PdfDictionary dic;
                PdfDictionary dr = new PdfDictionary();
                acroForm.put(PdfName.DR, dr);
                Iterator i$ = this.fieldTemplates.iterator();
                while (i$.hasNext()) {
                    PdfFormField.mergeResources(dr, (PdfDictionary) ((PdfTemplate) i$.next()).getResources());
                }
                PdfDictionary fonts = dr.getAsDict(PdfName.FONT);
                if (fonts == null) {
                    fonts = new PdfDictionary();
                    dr.put(PdfName.FONT, fonts);
                }
                if (!fonts.contains(PdfName.HELV)) {
                    dic = new PdfDictionary(PdfName.FONT);
                    dic.put(PdfName.BASEFONT, PdfName.HELVETICA);
                    dic.put(PdfName.ENCODING, PdfName.WIN_ANSI_ENCODING);
                    dic.put(PdfName.NAME, PdfName.HELV);
                    dic.put(PdfName.SUBTYPE, PdfName.TYPE1);
                    fonts.put(PdfName.HELV, addToBody(dic).getIndirectReference());
                }
                if (!fonts.contains(PdfName.ZADB)) {
                    dic = new PdfDictionary(PdfName.FONT);
                    dic.put(PdfName.BASEFONT, PdfName.ZAPFDINGBATS);
                    dic.put(PdfName.NAME, PdfName.ZADB);
                    dic.put(PdfName.SUBTYPE, PdfName.TYPE1);
                    fonts.put(PdfName.ZADB, addToBody(dic).getIndirectReference());
                }
            }
        }
    }

    public void close() {
        if (this.open) {
            this.pdf.close();
            super.close();
        }
    }

    public PdfIndirectReference add(PdfOutline outline) {
        return null;
    }

    public void addAnnotation(PdfAnnotation annot) {
    }

    PdfIndirectReference add(PdfPage page, PdfContents contents) throws PdfException {
        return null;
    }

    public void freeReader(PdfReader reader) throws IOException {
        if (this.mergeFields) {
            throw new UnsupportedOperationException(MessageLocalization.getComposedMessage("it.is.not.possible.to.free.reader.in.merge.fields.mode", new Object[0]));
        }
        PdfArray array = reader.trailer.getAsArray(PdfName.ID);
        if (array != null) {
            this.originalFileID = array.getAsString(0).getBytes();
        }
        this.indirectMap.remove(reader);
        this.currentPdfReaderInstance = null;
        super.freeReader(reader);
    }

    protected PdfName getOffStateName(PdfDictionary widget) {
        return PdfName.Off;
    }

    static Integer getFlags(PdfDictionary field) {
        if (!PdfName.BTN.equals(field.getAsName(PdfName.FT))) {
            return null;
        }
        PdfNumber flags = field.getAsNumber(PdfName.FF);
        if (flags != null) {
            return Integer.valueOf(flags.intValue());
        }
        return null;
    }

    static boolean isCheckButton(PdfDictionary field) {
        Integer flags = getFlags(field);
        return flags == null || ((flags.intValue() & 65536) == 0 && (flags.intValue() & 32768) == 0);
    }

    static boolean isRadioButton(PdfDictionary field) {
        Integer flags = getFlags(field);
        return (flags == null || (flags.intValue() & 65536) != 0 || (flags.intValue() & 32768) == 0) ? false : true;
    }

    static boolean isTextField(PdfDictionary field) {
        return PdfName.TX.equals(field.getAsName(PdfName.FT));
    }

    public PageStamp createPageStamp(PdfImportedPage iPage) {
        int pageNum = iPage.getPageNumber();
        PdfReader reader = iPage.getPdfReaderInstance().getReader();
        if (!isTagged()) {
            return new PageStamp(reader, reader.getPageN(pageNum), this);
        }
        throw new RuntimeException(MessageLocalization.getComposedMessage("creating.page.stamp.not.allowed.for.tagged.reader", new Object[0]));
    }
}

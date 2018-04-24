package com.itextpdf.text.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocListener;
import com.itextpdf.text.DocWriter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Image;
import com.itextpdf.text.ImgJBIG2;
import com.itextpdf.text.ImgWMF;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Version;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.html.HtmlTags;
import com.itextpdf.text.log.Counter;
import com.itextpdf.text.log.CounterFactory;
import com.itextpdf.text.pdf.PdfDocument.Destination;
import com.itextpdf.text.pdf.collection.PdfCollection;
import com.itextpdf.text.pdf.events.PdfPageEventForwarder;
import com.itextpdf.text.pdf.interfaces.IAccessibleElement;
import com.itextpdf.text.pdf.interfaces.PdfAnnotations;
import com.itextpdf.text.pdf.interfaces.PdfDocumentActions;
import com.itextpdf.text.pdf.interfaces.PdfEncryptionSettings;
import com.itextpdf.text.pdf.interfaces.PdfIsoConformance;
import com.itextpdf.text.pdf.interfaces.PdfPageActions;
import com.itextpdf.text.pdf.interfaces.PdfRunDirection;
import com.itextpdf.text.pdf.interfaces.PdfVersion;
import com.itextpdf.text.pdf.interfaces.PdfViewerPreferences;
import com.itextpdf.text.pdf.interfaces.PdfXConformance;
import com.itextpdf.text.pdf.internal.PdfVersionImp;
import com.itextpdf.text.pdf.internal.PdfXConformanceImp;
import com.itextpdf.text.xml.xmp.PdfProperties;
import com.itextpdf.text.xml.xmp.XmpWriter;
import com.itextpdf.xmp.XMPConst;
import com.itextpdf.xmp.XMPException;
import com.itextpdf.xmp.options.PropertyOptions;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class PdfWriter extends DocWriter implements PdfViewerPreferences, PdfEncryptionSettings, PdfVersion, PdfDocumentActions, PdfPageActions, PdfRunDirection, PdfAnnotations {
    public static final int ALLOW_ASSEMBLY = 1024;
    public static final int ALLOW_COPY = 16;
    public static final int ALLOW_DEGRADED_PRINTING = 4;
    public static final int ALLOW_FILL_IN = 256;
    public static final int ALLOW_MODIFY_ANNOTATIONS = 32;
    public static final int ALLOW_MODIFY_CONTENTS = 8;
    public static final int ALLOW_PRINTING = 2052;
    public static final int ALLOW_SCREENREADERS = 512;
    @Deprecated
    public static final int AllowAssembly = 1024;
    @Deprecated
    public static final int AllowCopy = 16;
    @Deprecated
    public static final int AllowDegradedPrinting = 4;
    @Deprecated
    public static final int AllowFillIn = 256;
    @Deprecated
    public static final int AllowModifyAnnotations = 32;
    @Deprecated
    public static final int AllowModifyContents = 8;
    @Deprecated
    public static final int AllowPrinting = 2052;
    @Deprecated
    public static final int AllowScreenReaders = 512;
    protected static Counter COUNTER = CounterFactory.getCounter(PdfWriter.class);
    public static final int CenterWindow = 65536;
    public static final PdfName DID_PRINT = PdfName.DP;
    public static final PdfName DID_SAVE = PdfName.DS;
    public static final PdfName DOCUMENT_CLOSE = PdfName.WC;
    public static final int DO_NOT_ENCRYPT_METADATA = 8;
    public static final int DirectionL2R = 4194304;
    public static final int DirectionR2L = 8388608;
    public static final int DisplayDocTitle = 131072;
    public static final int EMBEDDED_FILES_ONLY = 24;
    public static final int ENCRYPTION_AES_128 = 2;
    public static final int ENCRYPTION_AES_256 = 3;
    static final int ENCRYPTION_MASK = 7;
    public static final int FitWindow = 32768;
    public static final int GENERATION_MAX = 65535;
    public static final int HideMenubar = 8192;
    public static final int HideToolbar = 4096;
    public static final int HideWindowUI = 16384;
    public static final float NO_SPACE_CHAR_RATIO = 1.0E7f;
    public static final int NonFullScreenPageModeUseNone = 262144;
    public static final int NonFullScreenPageModeUseOC = 2097152;
    public static final int NonFullScreenPageModeUseOutlines = 524288;
    public static final int NonFullScreenPageModeUseThumbs = 1048576;
    public static final PdfName PAGE_CLOSE = PdfName.C;
    public static final PdfName PAGE_OPEN = PdfName.O;
    public static final int PDFX1A2001 = 1;
    public static final int PDFX32002 = 2;
    public static final int PDFXNONE = 0;
    public static final PdfName PDF_VERSION_1_2 = new PdfName("1.2");
    public static final PdfName PDF_VERSION_1_3 = new PdfName("1.3");
    public static final PdfName PDF_VERSION_1_4 = new PdfName("1.4");
    public static final PdfName PDF_VERSION_1_5 = new PdfName("1.5");
    public static final PdfName PDF_VERSION_1_6 = new PdfName("1.6");
    public static final PdfName PDF_VERSION_1_7 = new PdfName("1.7");
    public static final int PageLayoutOneColumn = 2;
    public static final int PageLayoutSinglePage = 1;
    public static final int PageLayoutTwoColumnLeft = 4;
    public static final int PageLayoutTwoColumnRight = 8;
    public static final int PageLayoutTwoPageLeft = 16;
    public static final int PageLayoutTwoPageRight = 32;
    public static final int PageModeFullScreen = 512;
    public static final int PageModeUseAttachments = 2048;
    public static final int PageModeUseNone = 64;
    public static final int PageModeUseOC = 1024;
    public static final int PageModeUseOutlines = 128;
    public static final int PageModeUseThumbs = 256;
    public static final int PrintScalingNone = 16777216;
    public static final int RUN_DIRECTION_DEFAULT = 0;
    public static final int RUN_DIRECTION_LTR = 2;
    public static final int RUN_DIRECTION_NO_BIDI = 1;
    public static final int RUN_DIRECTION_RTL = 3;
    public static final int SIGNATURE_APPEND_ONLY = 2;
    public static final int SIGNATURE_EXISTS = 1;
    public static final float SPACE_CHAR_RATIO_DEFAULT = 2.5f;
    public static final int STANDARD_ENCRYPTION_128 = 1;
    public static final int STANDARD_ENCRYPTION_40 = 0;
    @Deprecated
    public static final boolean STRENGTH128BITS = true;
    @Deprecated
    public static final boolean STRENGTH40BITS = false;
    public static final char VERSION_1_2 = '2';
    public static final char VERSION_1_3 = '3';
    public static final char VERSION_1_4 = '4';
    public static final char VERSION_1_5 = '5';
    public static final char VERSION_1_6 = '6';
    public static final char VERSION_1_7 = '7';
    public static final PdfName WILL_PRINT = PdfName.WP;
    public static final PdfName WILL_SAVE = PdfName.WS;
    public static final int markAll = 0;
    public static final int markInlineElementsOnly = 1;
    private static final List<PdfName> standardStructElems_1_4 = Arrays.asList(new PdfName[]{PdfName.DOCUMENT, PdfName.PART, PdfName.ART, PdfName.SECT, PdfName.DIV, PdfName.BLOCKQUOTE, PdfName.CAPTION, PdfName.TOC, PdfName.TOCI, PdfName.INDEX, PdfName.NONSTRUCT, PdfName.PRIVATE, PdfName.P, PdfName.H, PdfName.H1, PdfName.H2, PdfName.H3, PdfName.H4, PdfName.H5, PdfName.H6, PdfName.L, PdfName.LBL, PdfName.LI, PdfName.LBODY, PdfName.TABLE, PdfName.TR, PdfName.TH, PdfName.TD, PdfName.SPAN, PdfName.QUOTE, PdfName.NOTE, PdfName.REFERENCE, PdfName.BIBENTRY, PdfName.CODE, PdfName.LINK, PdfName.FIGURE, PdfName.FORMULA, PdfName.FORM});
    private static final List<PdfName> standardStructElems_1_7 = Arrays.asList(new PdfName[]{PdfName.DOCUMENT, PdfName.PART, PdfName.ART, PdfName.SECT, PdfName.DIV, PdfName.BLOCKQUOTE, PdfName.CAPTION, PdfName.TOC, PdfName.TOCI, PdfName.INDEX, PdfName.NONSTRUCT, PdfName.PRIVATE, PdfName.P, PdfName.H, PdfName.H1, PdfName.H2, PdfName.H3, PdfName.H4, PdfName.H5, PdfName.H6, PdfName.L, PdfName.LBL, PdfName.LI, PdfName.LBODY, PdfName.TABLE, PdfName.TR, PdfName.TH, PdfName.TD, PdfName.THEAD, PdfName.TBODY, PdfName.TFOOT, PdfName.SPAN, PdfName.QUOTE, PdfName.NOTE, PdfName.REFERENCE, PdfName.BIBENTRY, PdfName.CODE, PdfName.LINK, PdfName.ANNOT, PdfName.RUBY, PdfName.RB, PdfName.RT, PdfName.RP, PdfName.WARICHU, PdfName.WT, PdfName.WP, PdfName.FIGURE, PdfName.FORMULA, PdfName.FORM});
    protected HashMap<PdfStream, PdfIndirectReference> JBIG2Globals = new HashMap();
    protected PdfArray OCGLocked = new PdfArray();
    protected PdfArray OCGRadioGroup = new PdfArray();
    protected PdfOCProperties OCProperties;
    protected PdfBody body;
    protected int colorNumber = 1;
    protected ICC_Profile colorProfile;
    protected int compressionLevel = -1;
    protected PdfEncryption crypto;
    protected int currentPageNumber = 1;
    protected PdfReaderInstance currentPdfReaderInstance;
    protected PdfDictionary defaultColorspace = new PdfDictionary();
    protected PdfContentByte directContent;
    protected PdfContentByte directContentUnder;
    protected HashMap<ICachedColorSpace, ColorDetails> documentColors = new HashMap();
    protected HashMap<PdfDictionary, PdfObject[]> documentExtGState = new HashMap();
    protected LinkedHashMap<BaseFont, FontDetails> documentFonts = new LinkedHashMap();
    protected HashSet<PdfOCG> documentOCG = new HashSet();
    protected ArrayList<PdfOCG> documentOCGorder = new ArrayList();
    protected HashMap<PdfPatternPainter, PdfName> documentPatterns = new HashMap();
    protected HashMap<Object, PdfObject[]> documentProperties = new HashMap();
    protected HashSet<PdfShadingPattern> documentShadingPatterns = new HashSet();
    protected HashSet<PdfShading> documentShadings = new HashSet();
    protected HashMap<ColorDetails, ColorDetails> documentSpotPatterns = new HashMap();
    protected PdfDictionary extraCatalog;
    protected int fontNumber = 1;
    protected HashMap<PdfIndirectReference, Object[]> formXObjects = new HashMap();
    protected int formXObjectsCounter = 1;
    protected boolean fullCompression = false;
    protected PdfDictionary group;
    protected PdfDictionary imageDictionary = new PdfDictionary();
    private final HashMap<Long, PdfName> images = new HashMap();
    protected List<HashMap<String, Object>> newBookmarks;
    protected byte[] originalFileID = null;
    protected PdfDictionary pageDictEntries = new PdfDictionary();
    private PdfPageEvent pageEvent;
    protected ArrayList<PdfIndirectReference> pageReferences = new ArrayList();
    protected ColorDetails patternColorspaceCMYK;
    protected ColorDetails patternColorspaceGRAY;
    protected ColorDetails patternColorspaceRGB;
    protected int patternNumber = 1;
    protected PdfDocument pdf;
    protected PdfIsoConformance pdfIsoConformance = initPdfIsoConformance();
    protected PdfVersionImp pdf_version = new PdfVersionImp();
    protected long prevxref = 0;
    protected HashMap<PdfReader, PdfReaderInstance> readerInstances = new HashMap();
    private boolean rgbTransparencyBlending;
    protected PdfPages root = new PdfPages(this);
    protected int runDirection = 1;
    private float spaceCharRatio = SPACE_CHAR_RATIO_DEFAULT;
    protected PdfStructureTreeRoot structureTreeRoot;
    protected PdfName tabs = null;
    protected boolean tagged = false;
    protected int taggingMode = 1;
    protected TtfUnicodeWriter ttfUnicodeWriter = null;
    private boolean userProperties;
    protected byte[] xmpMetadata = null;
    protected XmpWriter xmpWriter = null;

    protected Counter getCounter() {
        return COUNTER;
    }

    protected PdfWriter() {
    }

    protected PdfWriter(PdfDocument document, OutputStream os) {
        super(document, os);
        this.pdf = document;
        this.directContentUnder = new PdfContentByte(this);
        this.directContent = this.directContentUnder.getDuplicate();
    }

    public static PdfWriter getInstance(Document document, OutputStream os) throws DocumentException {
        PdfDocument pdf = new PdfDocument();
        document.addDocListener(pdf);
        PdfWriter writer = new PdfWriter(pdf, os);
        pdf.addWriter(writer);
        return writer;
    }

    public static PdfWriter getInstance(Document document, OutputStream os, DocListener listener) throws DocumentException {
        PdfDocument pdf = new PdfDocument();
        pdf.addDocListener(listener);
        document.addDocListener(pdf);
        PdfWriter writer = new PdfWriter(pdf, os);
        pdf.addWriter(writer);
        return writer;
    }

    PdfDocument getPdfDocument() {
        return this.pdf;
    }

    public PdfDictionary getInfo() {
        return this.pdf.getInfo();
    }

    public float getVerticalPosition(boolean ensureNewLine) {
        return this.pdf.getVerticalPosition(ensureNewLine);
    }

    public void setInitialLeading(float leading) throws DocumentException {
        if (this.open) {
            throw new DocumentException(MessageLocalization.getComposedMessage("you.can.t.set.the.initial.leading.if.the.document.is.already.open", new Object[0]));
        }
        this.pdf.setLeading(leading);
    }

    public PdfContentByte getDirectContent() {
        if (this.open) {
            return this.directContent;
        }
        throw new RuntimeException(MessageLocalization.getComposedMessage("the.document.is.not.open", new Object[0]));
    }

    public PdfContentByte getDirectContentUnder() {
        if (this.open) {
            return this.directContentUnder;
        }
        throw new RuntimeException(MessageLocalization.getComposedMessage("the.document.is.not.open", new Object[0]));
    }

    void resetContent() {
        this.directContent.reset();
        this.directContentUnder.reset();
    }

    public ICC_Profile getColorProfile() {
        return this.colorProfile;
    }

    void addLocalDestinations(TreeMap<String, Destination> desto) throws IOException {
        for (Entry<String, Destination> entry : desto.entrySet()) {
            String name = (String) entry.getKey();
            Destination dest = (Destination) entry.getValue();
            PdfObject destination = dest.destination;
            if (dest.reference == null) {
                dest.reference = getPdfIndirectReference();
            }
            if (destination == null) {
                addToBody(new PdfString("invalid_" + name), dest.reference);
            } else {
                addToBody(destination, dest.reference);
            }
        }
    }

    public PdfIndirectObject addToBody(PdfObject object) throws IOException {
        PdfIndirectObject iobj = this.body.add(object);
        cacheObject(iobj);
        return iobj;
    }

    public PdfIndirectObject addToBody(PdfObject object, boolean inObjStm) throws IOException {
        PdfIndirectObject iobj = this.body.add(object, inObjStm);
        cacheObject(iobj);
        return iobj;
    }

    public PdfIndirectObject addToBody(PdfObject object, PdfIndirectReference ref) throws IOException {
        PdfIndirectObject iobj = this.body.add(object, ref);
        cacheObject(iobj);
        return iobj;
    }

    public PdfIndirectObject addToBody(PdfObject object, PdfIndirectReference ref, boolean inObjStm) throws IOException {
        PdfIndirectObject iobj = this.body.add(object, ref, inObjStm);
        cacheObject(iobj);
        return iobj;
    }

    public PdfIndirectObject addToBody(PdfObject object, int refNumber) throws IOException {
        PdfIndirectObject iobj = this.body.add(object, refNumber);
        cacheObject(iobj);
        return iobj;
    }

    public PdfIndirectObject addToBody(PdfObject object, int refNumber, boolean inObjStm) throws IOException {
        PdfIndirectObject iobj = this.body.add(object, refNumber, 0, inObjStm);
        cacheObject(iobj);
        return iobj;
    }

    protected void cacheObject(PdfIndirectObject iobj) {
    }

    public PdfIndirectReference getPdfIndirectReference() {
        return this.body.getPdfIndirectReference();
    }

    protected int getIndirectReferenceNumber() {
        return this.body.getIndirectReferenceNumber();
    }

    public OutputStreamCounter getOs() {
        return this.os;
    }

    protected PdfDictionary getCatalog(PdfIndirectReference rootObj) {
        PdfDictionary catalog = this.pdf.getCatalog(rootObj);
        buildStructTreeRootForTagged(catalog);
        if (!this.documentOCG.isEmpty()) {
            fillOCProperties(false);
            catalog.put(PdfName.OCPROPERTIES, this.OCProperties);
        }
        return catalog;
    }

    protected void buildStructTreeRootForTagged(PdfDictionary catalog) {
        if (this.tagged) {
            try {
                getStructureTreeRoot().buildTree();
                catalog.put(PdfName.STRUCTTREEROOT, this.structureTreeRoot.getReference());
                PdfDictionary mi = new PdfDictionary();
                mi.put(PdfName.MARKED, PdfBoolean.PDFTRUE);
                if (this.userProperties) {
                    mi.put(PdfName.USERPROPERTIES, PdfBoolean.PDFTRUE);
                }
                catalog.put(PdfName.MARKINFO, mi);
            } catch (Exception e) {
                throw new ExceptionConverter(e);
            }
        }
    }

    public PdfDictionary getExtraCatalog() {
        if (this.extraCatalog == null) {
            this.extraCatalog = new PdfDictionary();
        }
        return this.extraCatalog;
    }

    public void addPageDictEntry(PdfName key, PdfObject object) {
        this.pageDictEntries.put(key, object);
    }

    public PdfDictionary getPageDictEntries() {
        return this.pageDictEntries;
    }

    public void resetPageDictEntries() {
        this.pageDictEntries = new PdfDictionary();
    }

    public void setLinearPageMode() {
        this.root.setLinearMode(null);
    }

    public int reorderPages(int[] order) throws DocumentException {
        return this.root.reorderPages(order);
    }

    public PdfIndirectReference getPageReference(int page) {
        page--;
        if (page < 0) {
            throw new IndexOutOfBoundsException(MessageLocalization.getComposedMessage("the.page.number.must.be.gt.eq.1", new Object[0]));
        } else if (page < this.pageReferences.size()) {
            ref = (PdfIndirectReference) this.pageReferences.get(page);
            if (ref != null) {
                return ref;
            }
            ref = this.body.getPdfIndirectReference();
            this.pageReferences.set(page, ref);
            return ref;
        } else {
            int empty = page - this.pageReferences.size();
            for (int k = 0; k < empty; k++) {
                this.pageReferences.add(null);
            }
            ref = this.body.getPdfIndirectReference();
            this.pageReferences.add(ref);
            return ref;
        }
    }

    public int getPageNumber() {
        return this.pdf.getPageNumber();
    }

    PdfIndirectReference getCurrentPage() {
        return getPageReference(this.currentPageNumber);
    }

    public int getCurrentPageNumber() {
        return this.currentPageNumber;
    }

    public void setPageViewport(PdfArray vp) {
        addPageDictEntry(PdfName.VP, vp);
    }

    public void setTabs(PdfName tabs) {
        this.tabs = tabs;
    }

    public PdfName getTabs() {
        return this.tabs;
    }

    PdfIndirectReference add(PdfPage page, PdfContents contents) throws PdfException {
        if (this.open) {
            try {
                page.add(addToBody(contents).getIndirectReference());
                if (this.group != null) {
                    page.put(PdfName.GROUP, this.group);
                    this.group = null;
                } else if (this.rgbTransparencyBlending) {
                    PdfDictionary pp = new PdfDictionary();
                    pp.put(PdfName.TYPE, PdfName.GROUP);
                    pp.put(PdfName.S, PdfName.TRANSPARENCY);
                    pp.put(PdfName.CS, PdfName.DEVICERGB);
                    page.put(PdfName.GROUP, pp);
                }
                this.root.addPage(page);
                this.currentPageNumber++;
                return null;
            } catch (IOException ioe) {
                throw new ExceptionConverter(ioe);
            }
        }
        throw new PdfException(MessageLocalization.getComposedMessage("the.document.is.not.open", new Object[0]));
    }

    public void setPageEvent(PdfPageEvent event) {
        if (event == null) {
            this.pageEvent = null;
        } else if (this.pageEvent == null) {
            this.pageEvent = event;
        } else if (this.pageEvent instanceof PdfPageEventForwarder) {
            ((PdfPageEventForwarder) this.pageEvent).addPageEvent(event);
        } else {
            PdfPageEventForwarder forward = new PdfPageEventForwarder();
            forward.addPageEvent(this.pageEvent);
            forward.addPageEvent(event);
            this.pageEvent = forward;
        }
    }

    public PdfPageEvent getPageEvent() {
        return this.pageEvent;
    }

    public void open() {
        super.open();
        try {
            this.pdf_version.writeHeader(this.os);
            this.body = new PdfBody(this);
            if (isPdfX() && ((PdfXConformanceImp) this.pdfIsoConformance).isPdfX32002()) {
                PdfObject sec = new PdfDictionary();
                sec.put(PdfName.GAMMA, new PdfArray(new float[]{2.2f, 2.2f, 2.2f}));
                sec.put(PdfName.MATRIX, new PdfArray(new float[]{0.4124f, 0.2126f, 0.0193f, 0.3576f, 0.7152f, 0.1192f, 0.1805f, 0.0722f, 0.9505f}));
                sec.put(PdfName.WHITEPOINT, new PdfArray(new float[]{0.9505f, BaseField.BORDER_WIDTH_THIN, 1.089f}));
                PdfArray arr = new PdfArray(PdfName.CALRGB);
                arr.add(sec);
                setDefaultColorspace(PdfName.DEFAULTRGB, addToBody(arr).getIndirectReference());
            }
        } catch (IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
    }

    public void close() {
        if (this.open) {
            if (this.currentPageNumber - 1 != this.pageReferences.size()) {
                throw new RuntimeException("The page " + this.pageReferences.size() + " was requested but the document has only " + (this.currentPageNumber - 1) + " pages.");
            }
            this.pdf.close();
            try {
                PdfObject fileID;
                addSharedObjectsToBody();
                Iterator i$ = this.documentOCG.iterator();
                while (i$.hasNext()) {
                    PdfOCG layer = (PdfOCG) i$.next();
                    addToBody(layer.getPdfObject(), layer.getRef());
                }
                PdfObject catalog = getCatalog(this.root.writePageTree());
                if (!this.documentOCG.isEmpty()) {
                    checkPdfIsoConformance(this, 7, this.OCProperties);
                }
                if (this.xmpMetadata == null && this.xmpWriter != null) {
                    try {
                        OutputStream baos = new ByteArrayOutputStream();
                        this.xmpWriter.serialize(baos);
                        this.xmpWriter.close();
                        this.xmpMetadata = baos.toByteArray();
                    } catch (IOException e) {
                        this.xmpWriter = null;
                    } catch (XMPException e2) {
                        this.xmpWriter = null;
                    }
                }
                if (this.xmpMetadata != null) {
                    PdfObject pdfStream = new PdfStream(this.xmpMetadata);
                    pdfStream.put(PdfName.TYPE, PdfName.METADATA);
                    pdfStream.put(PdfName.SUBTYPE, PdfName.XML);
                    if (!(this.crypto == null || this.crypto.isMetadataEncrypted())) {
                        PdfObject ar = new PdfArray();
                        ar.add(PdfName.CRYPT);
                        pdfStream.put(PdfName.FILTER, ar);
                    }
                    catalog.put(PdfName.METADATA, this.body.add(pdfStream).getIndirectReference());
                }
                if (isPdfX()) {
                    completeInfoDictionary(getInfo());
                    completeExtraCatalog(getExtraCatalog());
                }
                if (this.extraCatalog != null) {
                    catalog.mergeDifferent(this.extraCatalog);
                }
                writeOutlines(catalog, false);
                PdfIndirectObject indirectCatalog = addToBody(catalog, false);
                PdfIndirectObject infoObj = addToBody(getInfo(), false);
                PdfIndirectReference encryption = null;
                this.body.flushObjStm();
                boolean isModified = this.originalFileID != null;
                if (this.crypto != null) {
                    encryption = addToBody(this.crypto.getEncryptionDictionary(), false).getIndirectReference();
                    fileID = this.crypto.getFileID(isModified);
                } else {
                    fileID = PdfEncryption.createInfoId(isModified ? this.originalFileID : PdfEncryption.createDocumentId(), isModified);
                }
                this.body.writeCrossReferenceTable(this.os, indirectCatalog.getIndirectReference(), infoObj.getIndirectReference(), encryption, fileID, this.prevxref);
                if (this.fullCompression) {
                    writeKeyInfo(this.os);
                    this.os.write(getISOBytes("startxref\n"));
                    this.os.write(getISOBytes(String.valueOf(this.body.offset())));
                    this.os.write(getISOBytes("\n%%EOF\n"));
                } else {
                    new PdfTrailer(this.body.size(), this.body.offset(), indirectCatalog.getIndirectReference(), infoObj.getIndirectReference(), encryption, fileID, this.prevxref).toPdf(this, this.os);
                }
                super.close();
            } catch (Exception ioe) {
                throw new ExceptionConverter(ioe);
            } catch (Throwable th) {
                super.close();
            }
        }
        getCounter().written(this.os.getCounter());
    }

    protected void addXFormsToBody() throws IOException {
        for (Object[] objs : this.formXObjects.values()) {
            PdfTemplate template = objs[1];
            if ((template == null || !(template.getIndirectReference() instanceof PRIndirectReference)) && template != null && template.getType() == 1) {
                addToBody(template.getFormXObject(this.compressionLevel), template.getIndirectReference());
            }
        }
    }

    protected void addSharedObjectsToBody() throws IOException {
        for (FontDetails details : this.documentFonts.values()) {
            details.writeFont(this);
        }
        addXFormsToBody();
        for (PdfReaderInstance element : this.readerInstances.values()) {
            this.currentPdfReaderInstance = element;
            this.currentPdfReaderInstance.writeAllPages();
        }
        this.currentPdfReaderInstance = null;
        for (ColorDetails color : this.documentColors.values()) {
            addToBody(color.getPdfObject(this), color.getIndirectReference());
        }
        for (PdfPatternPainter pat : this.documentPatterns.keySet()) {
            addToBody(pat.getPattern(this.compressionLevel), pat.getIndirectReference());
        }
        Iterator i$ = this.documentShadingPatterns.iterator();
        while (i$.hasNext()) {
            ((PdfShadingPattern) i$.next()).addToBody();
        }
        i$ = this.documentShadings.iterator();
        while (i$.hasNext()) {
            ((PdfShading) i$.next()).addToBody();
        }
        for (Entry<PdfDictionary, PdfObject[]> entry : this.documentExtGState.entrySet()) {
            addToBody((PdfDictionary) entry.getKey(), (PdfIndirectReference) ((PdfObject[]) entry.getValue())[1]);
        }
        for (Entry<Object, PdfObject[]> entry2 : this.documentProperties.entrySet()) {
            PdfLayerMembership prop = entry2.getKey();
            PdfObject[] obj = (PdfObject[]) entry2.getValue();
            if (prop instanceof PdfLayerMembership) {
                PdfLayerMembership layer = prop;
                addToBody(layer.getPdfObject(), layer.getRef());
            } else if ((prop instanceof PdfDictionary) && !(prop instanceof PdfLayer)) {
                addToBody((PdfObject) prop, (PdfIndirectReference) obj[1]);
            }
        }
    }

    public PdfOutline getRootOutline() {
        return this.directContent.getRootOutline();
    }

    public void setOutlines(List<HashMap<String, Object>> outlines) {
        this.newBookmarks = outlines;
    }

    protected void writeOutlines(PdfDictionary catalog, boolean namedAsNames) throws IOException {
        if (this.newBookmarks != null && !this.newBookmarks.isEmpty()) {
            PdfObject top = new PdfDictionary();
            PdfIndirectReference topRef = getPdfIndirectReference();
            Object[] kids = SimpleBookmark.iterateOutlines(this, topRef, this.newBookmarks, namedAsNames);
            top.put(PdfName.FIRST, (PdfIndirectReference) kids[0]);
            top.put(PdfName.LAST, (PdfIndirectReference) kids[1]);
            top.put(PdfName.COUNT, new PdfNumber(((Integer) kids[2]).intValue()));
            addToBody(top, topRef);
            catalog.put(PdfName.OUTLINES, topRef);
        }
    }

    public void setPdfVersion(char version) {
        this.pdf_version.setPdfVersion(version);
    }

    public void setAtLeastPdfVersion(char version) {
        this.pdf_version.setAtLeastPdfVersion(version);
    }

    public void setPdfVersion(PdfName version) {
        this.pdf_version.setPdfVersion(version);
    }

    public void addDeveloperExtension(PdfDeveloperExtension de) {
        this.pdf_version.addDeveloperExtension(de);
    }

    PdfVersionImp getPdfVersion() {
        return this.pdf_version;
    }

    public void setViewerPreferences(int preferences) {
        this.pdf.setViewerPreferences(preferences);
    }

    public void addViewerPreference(PdfName key, PdfObject value) {
        this.pdf.addViewerPreference(key, value);
    }

    public void setPageLabels(PdfPageLabels pageLabels) {
        this.pdf.setPageLabels(pageLabels);
    }

    public void addNamedDestinations(Map<String, String> map, int page_offset) {
        for (Entry<String, String> entry : map.entrySet()) {
            String dest = (String) entry.getValue();
            int page = Integer.parseInt(dest.substring(0, dest.indexOf(" ")));
            addNamedDestination((String) entry.getKey(), page + page_offset, new PdfDestination(dest.substring(dest.indexOf(" ") + 1)));
        }
    }

    public void addNamedDestination(String name, int page, PdfDestination dest) {
        dest.addPage(getPageReference(page));
        this.pdf.localDestination(name, dest);
    }

    public void addJavaScript(PdfAction js) {
        this.pdf.addJavaScript(js);
    }

    public void addJavaScript(String code, boolean unicode) {
        addJavaScript(PdfAction.javaScript(code, this, unicode));
    }

    public void addJavaScript(String code) {
        addJavaScript(code, false);
    }

    public void addJavaScript(String name, PdfAction js) {
        this.pdf.addJavaScript(name, js);
    }

    public void addJavaScript(String name, String code, boolean unicode) {
        addJavaScript(name, PdfAction.javaScript(code, this, unicode));
    }

    public void addJavaScript(String name, String code) {
        addJavaScript(name, code, false);
    }

    public void addFileAttachment(String description, byte[] fileStore, String file, String fileDisplay) throws IOException {
        addFileAttachment(description, PdfFileSpecification.fileEmbedded(this, file, fileDisplay, fileStore));
    }

    public void addFileAttachment(String description, PdfFileSpecification fs) throws IOException {
        this.pdf.addFileAttachment(description, fs);
    }

    public void addFileAttachment(PdfFileSpecification fs) throws IOException {
        addFileAttachment(null, fs);
    }

    public void setOpenAction(String name) {
        this.pdf.setOpenAction(name);
    }

    public void setOpenAction(PdfAction action) {
        this.pdf.setOpenAction(action);
    }

    public void setAdditionalAction(PdfName actionType, PdfAction action) throws DocumentException {
        if (actionType.equals(DOCUMENT_CLOSE) || actionType.equals(WILL_SAVE) || actionType.equals(DID_SAVE) || actionType.equals(WILL_PRINT) || actionType.equals(DID_PRINT)) {
            this.pdf.addAdditionalAction(actionType, action);
        } else {
            throw new DocumentException(MessageLocalization.getComposedMessage("invalid.additional.action.type.1", new Object[]{actionType.toString()}));
        }
    }

    public void setCollection(PdfCollection collection) {
        setAtLeastPdfVersion(VERSION_1_7);
        this.pdf.setCollection(collection);
    }

    public PdfAcroForm getAcroForm() {
        return this.pdf.getAcroForm();
    }

    public void addAnnotation(PdfAnnotation annot) {
        this.pdf.addAnnotation(annot);
    }

    void addAnnotation(PdfAnnotation annot, int page) {
        addAnnotation(annot);
    }

    public void addCalculationOrder(PdfFormField annot) {
        this.pdf.addCalculationOrder(annot);
    }

    public void setSigFlags(int f) {
        this.pdf.setSigFlags(f);
    }

    public void setLanguage(String language) {
        this.pdf.setLanguage(language);
    }

    public void setXmpMetadata(byte[] xmpMetadata) {
        this.xmpMetadata = xmpMetadata;
    }

    public void setPageXmpMetadata(byte[] xmpMetadata) throws IOException {
        this.pdf.setXmpMetadata(xmpMetadata);
    }

    public XmpWriter getXmpWriter() {
        return this.xmpWriter;
    }

    public void createXmpMetadata() {
        try {
            this.xmpWriter = createXmpWriter(null, this.pdf.getInfo());
            if (isTagged()) {
                this.xmpWriter.getXmpMeta().setPropertyInteger(XMPConst.NS_PDFUA_ID, PdfProperties.PART, 1, new PropertyOptions(1073741824));
            }
            this.xmpMetadata = null;
        } catch (XMPException e) {
            throw new ExceptionConverter(e);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    protected PdfIsoConformance initPdfIsoConformance() {
        return new PdfXConformanceImp(this);
    }

    public void setPDFXConformance(int pdfx) {
        if (!(this.pdfIsoConformance instanceof PdfXConformanceImp) || ((PdfXConformance) this.pdfIsoConformance).getPDFXConformance() == pdfx) {
            return;
        }
        if (this.pdf.isOpen()) {
            throw new PdfXConformanceException(MessageLocalization.getComposedMessage("pdfx.conformance.can.only.be.set.before.opening.the.document", new Object[0]));
        } else if (this.crypto != null) {
            throw new PdfXConformanceException(MessageLocalization.getComposedMessage("a.pdfx.conforming.document.cannot.be.encrypted", new Object[0]));
        } else {
            if (pdfx != 0) {
                setPdfVersion((char) VERSION_1_3);
            }
            ((PdfXConformance) this.pdfIsoConformance).setPDFXConformance(pdfx);
        }
    }

    public int getPDFXConformance() {
        if (this.pdfIsoConformance instanceof PdfXConformanceImp) {
            return ((PdfXConformance) this.pdfIsoConformance).getPDFXConformance();
        }
        return 0;
    }

    public boolean isPdfX() {
        if (this.pdfIsoConformance instanceof PdfXConformanceImp) {
            return ((PdfXConformance) this.pdfIsoConformance).isPdfX();
        }
        return false;
    }

    public boolean isPdfIso() {
        return this.pdfIsoConformance.isPdfIso();
    }

    public void setOutputIntents(String outputConditionIdentifier, String outputCondition, String registryName, String info, ICC_Profile colorProfile) throws IOException {
        checkPdfIsoConformance(this, 19, colorProfile);
        getExtraCatalog();
        PdfObject out = new PdfDictionary(PdfName.OUTPUTINTENT);
        if (outputCondition != null) {
            out.put(PdfName.OUTPUTCONDITION, new PdfString(outputCondition, PdfObject.TEXT_UNICODE));
        }
        if (outputConditionIdentifier != null) {
            out.put(PdfName.OUTPUTCONDITIONIDENTIFIER, new PdfString(outputConditionIdentifier, PdfObject.TEXT_UNICODE));
        }
        if (registryName != null) {
            out.put(PdfName.REGISTRYNAME, new PdfString(registryName, PdfObject.TEXT_UNICODE));
        }
        if (info != null) {
            out.put(PdfName.INFO, new PdfString(info, PdfObject.TEXT_UNICODE));
        }
        if (colorProfile != null) {
            out.put(PdfName.DESTOUTPUTPROFILE, addToBody(new PdfICCBased(colorProfile, this.compressionLevel)).getIndirectReference());
        }
        out.put(PdfName.S, PdfName.GTS_PDFX);
        this.extraCatalog.put(PdfName.OUTPUTINTENTS, new PdfArray(out));
        this.colorProfile = colorProfile;
    }

    public void setOutputIntents(String outputConditionIdentifier, String outputCondition, String registryName, String info, byte[] destOutputProfile) throws IOException {
        setOutputIntents(outputConditionIdentifier, outputCondition, registryName, info, destOutputProfile == null ? null : ICC_Profile.getInstance(destOutputProfile));
    }

    public boolean setOutputIntents(PdfReader reader, boolean checkExistence) throws IOException {
        PdfArray outs = reader.getCatalog().getAsArray(PdfName.OUTPUTINTENTS);
        if (outs == null || outs.isEmpty()) {
            return false;
        }
        PdfDictionary out = outs.getAsDict(0);
        PdfObject obj = PdfReader.getPdfObject(out.get(PdfName.S));
        if (obj == null || !PdfName.GTS_PDFX.equals(obj)) {
            return false;
        }
        if (checkExistence) {
            return true;
        }
        PRStream stream = (PRStream) PdfReader.getPdfObject(out.get(PdfName.DESTOUTPUTPROFILE));
        byte[] destProfile = null;
        if (stream != null) {
            destProfile = PdfReader.getStreamBytes(stream);
        }
        setOutputIntents(getNameString(out, PdfName.OUTPUTCONDITIONIDENTIFIER), getNameString(out, PdfName.OUTPUTCONDITION), getNameString(out, PdfName.REGISTRYNAME), getNameString(out, PdfName.INFO), destProfile);
        return true;
    }

    private static String getNameString(PdfDictionary dic, PdfName key) {
        PdfObject obj = PdfReader.getPdfObject(dic.get(key));
        if (obj == null || !obj.isString()) {
            return null;
        }
        return ((PdfString) obj).toUnicodeString();
    }

    PdfEncryption getEncryption() {
        return this.crypto;
    }

    public void setEncryption(byte[] userPassword, byte[] ownerPassword, int permissions, int encryptionType) throws DocumentException {
        if (this.pdf.isOpen()) {
            throw new DocumentException(MessageLocalization.getComposedMessage("encryption.can.only.be.added.before.opening.the.document", new Object[0]));
        }
        this.crypto = new PdfEncryption();
        this.crypto.setCryptoMode(encryptionType, 0);
        this.crypto.setupAllKeys(userPassword, ownerPassword, permissions);
    }

    public void setEncryption(Certificate[] certs, int[] permissions, int encryptionType) throws DocumentException {
        if (this.pdf.isOpen()) {
            throw new DocumentException(MessageLocalization.getComposedMessage("encryption.can.only.be.added.before.opening.the.document", new Object[0]));
        }
        this.crypto = new PdfEncryption();
        if (certs != null) {
            for (int i = 0; i < certs.length; i++) {
                this.crypto.addRecipient(certs[i], permissions[i]);
            }
        }
        this.crypto.setCryptoMode(encryptionType, 0);
        this.crypto.getEncryptionDictionary();
    }

    @Deprecated
    public void setEncryption(byte[] userPassword, byte[] ownerPassword, int permissions, boolean strength128Bits) throws DocumentException {
        setEncryption(userPassword, ownerPassword, permissions, strength128Bits ? 1 : 0);
    }

    @Deprecated
    public void setEncryption(boolean strength, String userPassword, String ownerPassword, int permissions) throws DocumentException {
        setEncryption(getISOBytes(userPassword), getISOBytes(ownerPassword), permissions, strength ? 1 : 0);
    }

    @Deprecated
    public void setEncryption(int encryptionType, String userPassword, String ownerPassword, int permissions) throws DocumentException {
        setEncryption(getISOBytes(userPassword), getISOBytes(ownerPassword), permissions, encryptionType);
    }

    public boolean isFullCompression() {
        return this.fullCompression;
    }

    public void setFullCompression() throws DocumentException {
        if (this.open) {
            throw new DocumentException(MessageLocalization.getComposedMessage("you.can.t.set.the.full.compression.if.the.document.is.already.open", new Object[0]));
        }
        this.fullCompression = true;
        setAtLeastPdfVersion(VERSION_1_5);
    }

    public int getCompressionLevel() {
        return this.compressionLevel;
    }

    public void setCompressionLevel(int compressionLevel) {
        if (compressionLevel < 0 || compressionLevel > 9) {
            this.compressionLevel = -1;
        } else {
            this.compressionLevel = compressionLevel;
        }
    }

    FontDetails addSimple(BaseFont bf) {
        FontDetails ret = (FontDetails) this.documentFonts.get(bf);
        if (ret == null) {
            checkPdfIsoConformance(this, 4, bf);
            int i;
            if (bf.getFontType() == 4) {
                StringBuilder append = new StringBuilder().append("F");
                i = this.fontNumber;
                this.fontNumber = i + 1;
                ret = new FontDetails(new PdfName(append.append(i).toString()), ((DocumentFont) bf).getIndirectReference(), bf);
            } else {
                StringBuilder append2 = new StringBuilder().append("F");
                i = this.fontNumber;
                this.fontNumber = i + 1;
                ret = new FontDetails(new PdfName(append2.append(i).toString()), this.body.getPdfIndirectReference(), bf);
            }
            this.documentFonts.put(bf, ret);
        }
        return ret;
    }

    void eliminateFontSubset(PdfDictionary fonts) {
        for (FontDetails ft : this.documentFonts.values()) {
            if (fonts.get(ft.getFontName()) != null) {
                ft.setSubset(false);
            }
        }
    }

    PdfName addDirectTemplateSimple(PdfTemplate template, PdfName forcedName) {
        PdfName name;
        PdfName name2;
        PdfIndirectReference ref = template.getIndirectReference();
        Object[] obj = (Object[]) this.formXObjects.get(ref);
        if (obj == null) {
            if (forcedName == null) {
                try {
                    name = new PdfName("Xf" + this.formXObjectsCounter);
                } catch (Exception e) {
                    e = e;
                    throw new ExceptionConverter(e);
                }
                try {
                    this.formXObjectsCounter++;
                    name2 = name;
                } catch (Exception e2) {
                    Exception e3;
                    e3 = e2;
                    name2 = name;
                    throw new ExceptionConverter(e3);
                }
            }
            name2 = forcedName;
            if (template.getType() == 2) {
                PdfImportedPage ip = (PdfImportedPage) template;
                PdfReader r = ip.getPdfReaderInstance().getReader();
                if (!this.readerInstances.containsKey(r)) {
                    this.readerInstances.put(r, ip.getPdfReaderInstance());
                }
                template = null;
            }
            this.formXObjects.put(ref, new Object[]{name2, template});
        } else {
            name2 = (PdfName) obj[0];
        }
        return name2;
    }

    public void releaseTemplate(PdfTemplate tp) throws IOException {
        Object[] objs = (Object[]) this.formXObjects.get(tp.getIndirectReference());
        if (objs != null && objs[1] != null) {
            PdfTemplate template = objs[1];
            if (!(template.getIndirectReference() instanceof PRIndirectReference) && template.getType() == 1) {
                addToBody(template.getFormXObject(this.compressionLevel), template.getIndirectReference());
                objs[1] = null;
            }
        }
    }

    public PdfImportedPage getImportedPage(PdfReader reader, int pageNumber) {
        return getPdfReaderInstance(reader).getImportedPage(pageNumber);
    }

    protected PdfReaderInstance getPdfReaderInstance(PdfReader reader) {
        PdfReaderInstance inst = (PdfReaderInstance) this.readerInstances.get(reader);
        if (inst != null) {
            return inst;
        }
        inst = reader.getPdfReaderInstance(this);
        this.readerInstances.put(reader, inst);
        return inst;
    }

    public void freeReader(PdfReader reader) throws IOException {
        this.currentPdfReaderInstance = (PdfReaderInstance) this.readerInstances.get(reader);
        if (this.currentPdfReaderInstance != null) {
            this.currentPdfReaderInstance.writeAllPages();
            this.currentPdfReaderInstance = null;
            this.readerInstances.remove(reader);
        }
    }

    public long getCurrentDocumentSize() {
        return (this.body.offset() + ((long) (this.body.size() * 20))) + 72;
    }

    protected int getNewObjectNumber(PdfReader reader, int number, int generation) {
        if (this.currentPdfReaderInstance == null || this.currentPdfReaderInstance.getReader() != reader) {
            this.currentPdfReaderInstance = getPdfReaderInstance(reader);
        }
        return this.currentPdfReaderInstance.getNewObjectNumber(number, generation);
    }

    RandomAccessFileOrArray getReaderFile(PdfReader reader) {
        return this.currentPdfReaderInstance.getReaderFile();
    }

    PdfName getColorspaceName() {
        StringBuilder append = new StringBuilder().append("CS");
        int i = this.colorNumber;
        this.colorNumber = i + 1;
        return new PdfName(append.append(i).toString());
    }

    ColorDetails addSimple(ICachedColorSpace spc) {
        ColorDetails ret = (ColorDetails) this.documentColors.get(spc);
        if (ret == null) {
            ret = new ColorDetails(getColorspaceName(), this.body.getPdfIndirectReference(), spc);
            if (spc instanceof IPdfSpecialColorSpace) {
                ((IPdfSpecialColorSpace) spc).getColorantDetails(this);
            }
            this.documentColors.put(spc, ret);
        }
        return ret;
    }

    PdfName addSimplePattern(PdfPatternPainter painter) {
        Exception e;
        PdfName name = (PdfName) this.documentPatterns.get(painter);
        if (name != null) {
            return name;
        }
        try {
            PdfName name2 = new PdfName("P" + this.patternNumber);
            try {
                this.patternNumber++;
                this.documentPatterns.put(painter, name2);
                return name2;
            } catch (Exception e2) {
                e = e2;
                name = name2;
                throw new ExceptionConverter(e);
            }
        } catch (Exception e3) {
            e = e3;
            throw new ExceptionConverter(e);
        }
    }

    void addSimpleShadingPattern(PdfShadingPattern shading) {
        if (!this.documentShadingPatterns.contains(shading)) {
            shading.setName(this.patternNumber);
            this.patternNumber++;
            this.documentShadingPatterns.add(shading);
            addSimpleShading(shading.getShading());
        }
    }

    void addSimpleShading(PdfShading shading) {
        if (!this.documentShadings.contains(shading)) {
            this.documentShadings.add(shading);
            shading.setName(this.documentShadings.size());
        }
    }

    PdfObject[] addSimpleExtGState(PdfDictionary gstate) {
        if (!this.documentExtGState.containsKey(gstate)) {
            this.documentExtGState.put(gstate, new PdfObject[]{new PdfName("GS" + (this.documentExtGState.size() + 1)), getPdfIndirectReference()});
        }
        return (PdfObject[]) this.documentExtGState.get(gstate);
    }

    PdfObject[] addSimpleProperty(Object prop, PdfIndirectReference refi) {
        if (!this.documentProperties.containsKey(prop)) {
            if (prop instanceof PdfOCG) {
                checkPdfIsoConformance(this, 7, prop);
            }
            this.documentProperties.put(prop, new PdfObject[]{new PdfName("Pr" + (this.documentProperties.size() + 1)), refi});
        }
        return (PdfObject[]) this.documentProperties.get(prop);
    }

    boolean propertyExists(Object prop) {
        return this.documentProperties.containsKey(prop);
    }

    public void setTagged() {
        setTagged(1);
    }

    public void setTagged(int taggingMode) {
        if (this.open) {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("tagging.must.be.set.before.opening.the.document", new Object[0]));
        }
        this.tagged = true;
        this.taggingMode = taggingMode;
    }

    public boolean needToBeMarkedInContent(IAccessibleElement element) {
        if ((this.taggingMode & 1) == 0 || element.isInline() || PdfName.ARTIFACT.equals(element.getRole())) {
            return true;
        }
        return false;
    }

    public void checkElementRole(IAccessibleElement element, IAccessibleElement parent) {
        if (parent != null && (parent.getRole() == null || PdfName.ARTIFACT.equals(parent.getRole()))) {
            element.setRole(null);
        } else if ((this.taggingMode & 1) == 0 || !element.isInline() || element.getRole() != null) {
        } else {
            if (parent == null || !parent.isInline()) {
                throw new IllegalArgumentException(MessageLocalization.getComposedMessage("inline.elements.with.role.null.are.not.allowed", new Object[0]));
            }
        }
    }

    public boolean isTagged() {
        return this.tagged;
    }

    protected void flushTaggedObjects() throws IOException {
    }

    protected void flushAcroFields() throws IOException, BadPdfFormatException {
    }

    public PdfStructureTreeRoot getStructureTreeRoot() {
        if (this.tagged && this.structureTreeRoot == null) {
            this.structureTreeRoot = new PdfStructureTreeRoot(this);
        }
        return this.structureTreeRoot;
    }

    public PdfOCProperties getOCProperties() {
        fillOCProperties(true);
        return this.OCProperties;
    }

    public void addOCGRadioGroup(ArrayList<PdfLayer> group) {
        PdfObject ar = new PdfArray();
        for (int k = 0; k < group.size(); k++) {
            PdfLayer layer = (PdfLayer) group.get(k);
            if (layer.getTitle() == null) {
                ar.add(layer.getRef());
            }
        }
        if (ar.size() != 0) {
            this.OCGRadioGroup.add(ar);
        }
    }

    public void lockLayer(PdfLayer layer) {
        this.OCGLocked.add(layer.getRef());
    }

    private static void getOCGOrder(PdfArray order, PdfLayer layer) {
        if (layer.isOnPanel()) {
            if (layer.getTitle() == null) {
                order.add(layer.getRef());
            }
            ArrayList<PdfLayer> children = layer.getChildren();
            if (children != null) {
                PdfObject kids = new PdfArray();
                if (layer.getTitle() != null) {
                    kids.add(new PdfString(layer.getTitle(), PdfObject.TEXT_UNICODE));
                }
                for (int k = 0; k < children.size(); k++) {
                    getOCGOrder(kids, (PdfLayer) children.get(k));
                }
                if (kids.size() > 0) {
                    order.add(kids);
                }
            }
        }
    }

    private void addASEvent(PdfName event, PdfName category) {
        PdfArray arr = new PdfArray();
        Iterator i$ = this.documentOCG.iterator();
        while (i$.hasNext()) {
            PdfLayer layer = (PdfLayer) i$.next();
            PdfDictionary usage = layer.getAsDict(PdfName.USAGE);
            if (!(usage == null || usage.get(category) == null)) {
                arr.add(layer.getRef());
            }
        }
        if (arr.size() != 0) {
            PdfDictionary d = this.OCProperties.getAsDict(PdfName.D);
            PdfArray arras = d.getAsArray(PdfName.AS);
            if (arras == null) {
                arras = new PdfArray();
                d.put(PdfName.AS, arras);
            }
            PdfObject as = new PdfDictionary();
            as.put(PdfName.EVENT, event);
            as.put(PdfName.CATEGORY, new PdfArray((PdfObject) category));
            as.put(PdfName.OCGS, arr);
            arras.add(as);
        }
    }

    protected void fillOCProperties(boolean erase) {
        PdfArray gr;
        Iterator i$;
        if (this.OCProperties == null) {
            this.OCProperties = new PdfOCProperties();
        }
        if (erase) {
            this.OCProperties.remove(PdfName.OCGS);
            this.OCProperties.remove(PdfName.D);
        }
        if (this.OCProperties.get(PdfName.OCGS) == null) {
            gr = new PdfArray();
            i$ = this.documentOCG.iterator();
            while (i$.hasNext()) {
                gr.add(((PdfLayer) i$.next()).getRef());
            }
            this.OCProperties.put(PdfName.OCGS, gr);
        }
        if (this.OCProperties.get(PdfName.D) == null) {
            ArrayList<PdfOCG> docOrder = new ArrayList(this.documentOCGorder);
            Iterator<PdfOCG> it = docOrder.iterator();
            while (it.hasNext()) {
                if (((PdfLayer) it.next()).getParent() != null) {
                    it.remove();
                }
            }
            PdfArray order = new PdfArray();
            i$ = docOrder.iterator();
            while (i$.hasNext()) {
                getOCGOrder(order, (PdfLayer) i$.next());
            }
            PdfDictionary d = new PdfDictionary();
            this.OCProperties.put(PdfName.D, d);
            d.put(PdfName.ORDER, order);
            if (docOrder.size() > 0 && (docOrder.get(0) instanceof PdfLayer)) {
                PdfString name = ((PdfLayer) docOrder.get(0)).getAsString(PdfName.NAME);
                if (name != null) {
                    d.put(PdfName.NAME, name);
                }
            }
            gr = new PdfArray();
            i$ = this.documentOCG.iterator();
            while (i$.hasNext()) {
                PdfLayer layer = (PdfLayer) i$.next();
                if (!layer.isOn()) {
                    gr.add(layer.getRef());
                }
            }
            if (gr.size() > 0) {
                d.put(PdfName.OFF, gr);
            }
            if (this.OCGRadioGroup.size() > 0) {
                d.put(PdfName.RBGROUPS, this.OCGRadioGroup);
            }
            if (this.OCGLocked.size() > 0) {
                d.put(PdfName.LOCKED, this.OCGLocked);
            }
            addASEvent(PdfName.VIEW, PdfName.ZOOM);
            addASEvent(PdfName.VIEW, PdfName.VIEW);
            addASEvent(PdfName.PRINT, PdfName.PRINT);
            addASEvent(PdfName.EXPORT, PdfName.EXPORT);
            d.put(PdfName.LISTMODE, PdfName.VISIBLEPAGES);
        }
    }

    void registerLayer(PdfOCG layer) {
        checkPdfIsoConformance(this, 7, layer);
        if (!(layer instanceof PdfLayer)) {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("only.pdflayer.is.accepted", new Object[0]));
        } else if (((PdfLayer) layer).getTitle() != null) {
            this.documentOCGorder.add(layer);
        } else if (!this.documentOCG.contains(layer)) {
            this.documentOCG.add(layer);
            this.documentOCGorder.add(layer);
        }
    }

    public Rectangle getPageSize() {
        return this.pdf.getPageSize();
    }

    public void setCropBoxSize(Rectangle crop) {
        this.pdf.setCropBoxSize(crop);
    }

    public void setBoxSize(String boxName, Rectangle size) {
        this.pdf.setBoxSize(boxName, size);
    }

    public Rectangle getBoxSize(String boxName) {
        return this.pdf.getBoxSize(boxName);
    }

    public Rectangle getBoxSize(String boxName, Rectangle intersectingRectangle) {
        Rectangle pdfRectangle = this.pdf.getBoxSize(boxName);
        if (pdfRectangle == null || intersectingRectangle == null) {
            return null;
        }
        com.itextpdf.awt.geom.Rectangle outRect = new com.itextpdf.awt.geom.Rectangle(pdfRectangle).intersection(new com.itextpdf.awt.geom.Rectangle(intersectingRectangle));
        if (outRect.isEmpty()) {
            return null;
        }
        Rectangle output = new Rectangle((float) outRect.getX(), (float) outRect.getY(), (float) (outRect.getX() + outRect.getWidth()), (float) (outRect.getY() + outRect.getHeight()));
        output.normalize();
        return output;
    }

    public void setPageEmpty(boolean pageEmpty) {
        if (!pageEmpty) {
            this.pdf.setPageEmpty(pageEmpty);
        }
    }

    public boolean isPageEmpty() {
        return this.pdf.isPageEmpty();
    }

    public void setPageAction(PdfName actionType, PdfAction action) throws DocumentException {
        if (actionType.equals(PAGE_OPEN) || actionType.equals(PAGE_CLOSE)) {
            this.pdf.setPageAction(actionType, action);
        } else {
            throw new DocumentException(MessageLocalization.getComposedMessage("invalid.page.additional.action.type.1", new Object[]{actionType.toString()}));
        }
    }

    public void setDuration(int seconds) {
        this.pdf.setDuration(seconds);
    }

    public void setTransition(PdfTransition transition) {
        this.pdf.setTransition(transition);
    }

    public void setThumbnail(Image image) throws PdfException, DocumentException {
        this.pdf.setThumbnail(image);
    }

    public PdfDictionary getGroup() {
        return this.group;
    }

    public void setGroup(PdfDictionary group) {
        this.group = group;
    }

    public float getSpaceCharRatio() {
        return this.spaceCharRatio;
    }

    public void setSpaceCharRatio(float spaceCharRatio) {
        if (spaceCharRatio < 0.001f) {
            this.spaceCharRatio = 0.001f;
        } else {
            this.spaceCharRatio = spaceCharRatio;
        }
    }

    public void setRunDirection(int runDirection) {
        if (runDirection < 1 || runDirection > 3) {
            throw new RuntimeException(MessageLocalization.getComposedMessage("invalid.run.direction.1", runDirection));
        }
        this.runDirection = runDirection;
    }

    public int getRunDirection() {
        return this.runDirection;
    }

    public void setUserunit(float userunit) throws DocumentException {
        if (userunit < BaseField.BORDER_WIDTH_THIN || userunit > 75000.0f) {
            throw new DocumentException(MessageLocalization.getComposedMessage("userunit.should.be.a.value.between.1.and.75000", new Object[0]));
        }
        addPageDictEntry(PdfName.USERUNIT, new PdfNumber(userunit));
        setAtLeastPdfVersion(VERSION_1_6);
    }

    public PdfDictionary getDefaultColorspace() {
        return this.defaultColorspace;
    }

    public void setDefaultColorspace(PdfName key, PdfObject cs) {
        if (cs == null || cs.isNull()) {
            this.defaultColorspace.remove(key);
        }
        this.defaultColorspace.put(key, cs);
    }

    ColorDetails addSimplePatternColorspace(BaseColor color) {
        int type = ExtendedColor.getType(color);
        if (type == 4 || type == 5) {
            throw new RuntimeException(MessageLocalization.getComposedMessage("an.uncolored.tile.pattern.can.not.have.another.pattern.or.shading.as.color", new Object[0]));
        }
        PdfObject array;
        switch (type) {
            case 0:
                if (this.patternColorspaceRGB == null) {
                    this.patternColorspaceRGB = new ColorDetails(getColorspaceName(), this.body.getPdfIndirectReference(), null);
                    array = new PdfArray(PdfName.PATTERN);
                    array.add(PdfName.DEVICERGB);
                    addToBody(array, this.patternColorspaceRGB.getIndirectReference());
                }
                return this.patternColorspaceRGB;
            case 1:
                if (this.patternColorspaceGRAY == null) {
                    this.patternColorspaceGRAY = new ColorDetails(getColorspaceName(), this.body.getPdfIndirectReference(), null);
                    array = new PdfArray(PdfName.PATTERN);
                    array.add(PdfName.DEVICEGRAY);
                    addToBody(array, this.patternColorspaceGRAY.getIndirectReference());
                }
                return this.patternColorspaceGRAY;
            case 2:
                if (this.patternColorspaceCMYK == null) {
                    this.patternColorspaceCMYK = new ColorDetails(getColorspaceName(), this.body.getPdfIndirectReference(), null);
                    array = new PdfArray(PdfName.PATTERN);
                    array.add(PdfName.DEVICECMYK);
                    addToBody(array, this.patternColorspaceCMYK.getIndirectReference());
                }
                return this.patternColorspaceCMYK;
            case 3:
                ColorDetails details = addSimple(((SpotColor) color).getPdfSpotColor());
                ColorDetails patternDetails = (ColorDetails) this.documentSpotPatterns.get(details);
                if (patternDetails != null) {
                    return patternDetails;
                }
                patternDetails = new ColorDetails(getColorspaceName(), this.body.getPdfIndirectReference(), null);
                array = new PdfArray(PdfName.PATTERN);
                array.add(details.getIndirectReference());
                addToBody(array, patternDetails.getIndirectReference());
                this.documentSpotPatterns.put(details, patternDetails);
                return patternDetails;
            default:
                try {
                    throw new RuntimeException(MessageLocalization.getComposedMessage("invalid.color.type", new Object[0]));
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
        }
        throw new RuntimeException(e.getMessage());
    }

    public boolean isStrictImageSequence() {
        return this.pdf.isStrictImageSequence();
    }

    public void setStrictImageSequence(boolean strictImageSequence) {
        this.pdf.setStrictImageSequence(strictImageSequence);
    }

    public void clearTextWrap() throws DocumentException {
        this.pdf.clearTextWrap();
    }

    public PdfName addDirectImageSimple(Image image) throws PdfException, DocumentException {
        return addDirectImageSimple(image, null);
    }

    public PdfName addDirectImageSimple(Image image, PdfIndirectReference fixedRef) throws PdfException, DocumentException {
        PdfName name;
        if (this.images.containsKey(image.getMySerialId())) {
            name = (PdfName) this.images.get(image.getMySerialId());
        } else {
            if (image.isImgTemplate()) {
                name = new PdfName(HtmlTags.IMG + this.images.size());
                if (image instanceof ImgWMF) {
                    try {
                        ((ImgWMF) image).readWMF(PdfTemplate.createTemplate(this, 0.0f, 0.0f));
                    } catch (Exception e) {
                        throw new DocumentException(e);
                    }
                }
            }
            PdfIndirectReference dref = image.getDirectReference();
            if (dref != null) {
                PdfName pdfName = new PdfName(HtmlTags.IMG + this.images.size());
                this.images.put(image.getMySerialId(), pdfName);
                this.imageDictionary.put(pdfName, dref);
                return pdfName;
            }
            Image maskImage = image.getImageMask();
            PdfIndirectReference maskRef = null;
            if (maskImage != null) {
                maskRef = getImageReference((PdfName) this.images.get(maskImage.getMySerialId()));
            }
            PdfImage i = new PdfImage(image, HtmlTags.IMG + this.images.size(), maskRef);
            if (image instanceof ImgJBIG2) {
                byte[] globals = ((ImgJBIG2) image).getGlobalBytes();
                if (globals != null) {
                    PdfDictionary decodeparms = new PdfDictionary();
                    decodeparms.put(PdfName.JBIG2GLOBALS, getReferenceJBIG2Globals(globals));
                    i.put(PdfName.DECODEPARMS, decodeparms);
                }
            }
            if (image.hasICCProfile()) {
                PdfObject iccRef = add(new PdfICCBased(image.getICCProfile(), image.getCompressionLevel()));
                PdfArray iccArray = new PdfArray();
                iccArray.add(PdfName.ICCBASED);
                iccArray.add(iccRef);
                PdfArray colorspace = i.getAsArray(PdfName.COLORSPACE);
                if (colorspace == null) {
                    i.put(PdfName.COLORSPACE, iccArray);
                } else if (colorspace.size() <= 1 || !PdfName.INDEXED.equals(colorspace.getPdfObject(0))) {
                    i.put(PdfName.COLORSPACE, iccArray);
                } else {
                    colorspace.set(1, iccArray);
                }
            }
            add(i, fixedRef);
            name = i.name();
            this.images.put(image.getMySerialId(), name);
        }
        return name;
    }

    PdfIndirectReference add(PdfImage pdfImage, PdfIndirectReference fixedRef) throws PdfException {
        if (this.imageDictionary.contains(pdfImage.name())) {
            return (PdfIndirectReference) this.imageDictionary.get(pdfImage.name());
        }
        checkPdfIsoConformance(this, 5, pdfImage);
        if (fixedRef instanceof PRIndirectReference) {
            PRIndirectReference r2 = (PRIndirectReference) fixedRef;
            fixedRef = new PdfIndirectReference(0, getNewObjectNumber(r2.getReader(), r2.getNumber(), r2.getGeneration()));
        }
        if (fixedRef == null) {
            try {
                fixedRef = addToBody(pdfImage).getIndirectReference();
            } catch (IOException ioe) {
                throw new ExceptionConverter(ioe);
            }
        }
        addToBody((PdfObject) pdfImage, fixedRef);
        this.imageDictionary.put(pdfImage.name(), fixedRef);
        return fixedRef;
    }

    PdfIndirectReference getImageReference(PdfName name) {
        return (PdfIndirectReference) this.imageDictionary.get(name);
    }

    protected PdfIndirectReference add(PdfICCBased icc) {
        try {
            return addToBody(icc).getIndirectReference();
        } catch (IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
    }

    protected PdfIndirectReference getReferenceJBIG2Globals(byte[] content) {
        if (content == null) {
            return null;
        }
        for (PdfStream stream : this.JBIG2Globals.keySet()) {
            if (Arrays.equals(content, stream.getBytes())) {
                return (PdfIndirectReference) this.JBIG2Globals.get(stream);
            }
        }
        PdfStream stream2 = new PdfStream(content);
        try {
            PdfIndirectObject ref = addToBody(stream2);
            this.JBIG2Globals.put(stream2, ref.getIndirectReference());
            return ref.getIndirectReference();
        } catch (IOException e) {
            return null;
        }
    }

    public boolean isUserProperties() {
        return this.userProperties;
    }

    public void setUserProperties(boolean userProperties) {
        this.userProperties = userProperties;
    }

    public boolean isRgbTransparencyBlending() {
        return this.rgbTransparencyBlending;
    }

    public void setRgbTransparencyBlending(boolean rgbTransparencyBlending) {
        this.rgbTransparencyBlending = rgbTransparencyBlending;
    }

    protected static void writeKeyInfo(OutputStream os) throws IOException {
        String k = Version.getInstance().getKey();
        if (k == null) {
            k = "iText";
        }
        os.write(getISOBytes(String.format("%%%s-%s\n", new Object[]{k, version.getRelease()})));
    }

    protected TtfUnicodeWriter getTtfUnicodeWriter() {
        if (this.ttfUnicodeWriter == null) {
            this.ttfUnicodeWriter = new TtfUnicodeWriter(this);
        }
        return this.ttfUnicodeWriter;
    }

    protected XmpWriter createXmpWriter(ByteArrayOutputStream baos, PdfDictionary info) throws IOException {
        return new XmpWriter((OutputStream) baos, info);
    }

    protected XmpWriter createXmpWriter(ByteArrayOutputStream baos, HashMap<String, String> info) throws IOException {
        return new XmpWriter((OutputStream) baos, (Map) info);
    }

    public PdfAnnotation createAnnotation(Rectangle rect, PdfName subtype) {
        PdfAnnotation a = new PdfAnnotation(this, rect);
        if (subtype != null) {
            a.put(PdfName.SUBTYPE, subtype);
        }
        return a;
    }

    public PdfAnnotation createAnnotation(float llx, float lly, float urx, float ury, PdfString title, PdfString content, PdfName subtype) {
        PdfAnnotation a = new PdfAnnotation(this, llx, lly, urx, ury, title, content);
        if (subtype != null) {
            a.put(PdfName.SUBTYPE, subtype);
        }
        return a;
    }

    public PdfAnnotation createAnnotation(float llx, float lly, float urx, float ury, PdfAction action, PdfName subtype) {
        PdfAnnotation a = new PdfAnnotation(this, llx, lly, urx, ury, action);
        if (subtype != null) {
            a.put(PdfName.SUBTYPE, subtype);
        }
        return a;
    }

    public static void checkPdfIsoConformance(PdfWriter writer, int key, Object obj1) {
        if (writer != null) {
            writer.checkPdfIsoConformance(key, obj1);
        }
    }

    public void checkPdfIsoConformance(int key, Object obj1) {
        this.pdfIsoConformance.checkPdfIsoConformance(key, obj1);
    }

    private void completeInfoDictionary(PdfDictionary info) {
        if (isPdfX()) {
            if (info.get(PdfName.GTS_PDFXVERSION) == null) {
                if (((PdfXConformanceImp) this.pdfIsoConformance).isPdfX1A2001()) {
                    info.put(PdfName.GTS_PDFXVERSION, new PdfString("PDF/X-1:2001"));
                    info.put(new PdfName("GTS_PDFXConformance"), new PdfString("PDF/X-1a:2001"));
                } else if (((PdfXConformanceImp) this.pdfIsoConformance).isPdfX32002()) {
                    info.put(PdfName.GTS_PDFXVERSION, new PdfString("PDF/X-3:2002"));
                }
            }
            if (info.get(PdfName.TITLE) == null) {
                info.put(PdfName.TITLE, new PdfString("Pdf document"));
            }
            if (info.get(PdfName.CREATOR) == null) {
                info.put(PdfName.CREATOR, new PdfString("Unknown"));
            }
            if (info.get(PdfName.TRAPPED) == null) {
                info.put(PdfName.TRAPPED, new PdfName(XMPConst.FALSESTR));
            }
        }
    }

    private void completeExtraCatalog(PdfDictionary extraCatalog) {
        if (isPdfX() && extraCatalog.get(PdfName.OUTPUTINTENTS) == null) {
            PdfObject out = new PdfDictionary(PdfName.OUTPUTINTENT);
            out.put(PdfName.OUTPUTCONDITION, new PdfString("SWOP CGATS TR 001-1995"));
            out.put(PdfName.OUTPUTCONDITIONIDENTIFIER, new PdfString("CGATS TR 001"));
            out.put(PdfName.REGISTRYNAME, new PdfString("http://www.color.org"));
            out.put(PdfName.INFO, new PdfString(""));
            out.put(PdfName.S, PdfName.GTS_PDFX);
            extraCatalog.put(PdfName.OUTPUTINTENTS, new PdfArray(out));
        }
    }

    public List<PdfName> getStandardStructElems() {
        if (this.pdf_version.getVersion() < VERSION_1_7) {
            return standardStructElems_1_4;
        }
        return standardStructElems_1_7;
    }
}

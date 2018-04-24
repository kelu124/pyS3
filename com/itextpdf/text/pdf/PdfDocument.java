package com.itextpdf.text.pdf;

import com.itextpdf.text.AccessibleElementId;
import com.itextpdf.text.Anchor;
import com.itextpdf.text.Annotation;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.ListLabel;
import com.itextpdf.text.MarkedObject;
import com.itextpdf.text.MarkedSection;
import com.itextpdf.text.Meta;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Section;
import com.itextpdf.text.TabSettings;
import com.itextpdf.text.Version;
import com.itextpdf.text.api.WriterOperation;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.collection.PdfCollection;
import com.itextpdf.text.pdf.draw.DrawInterface;
import com.itextpdf.text.pdf.interfaces.IAccessibleElement;
import com.itextpdf.text.pdf.internal.PdfAnnotationsImp;
import com.itextpdf.text.pdf.internal.PdfViewerPreferencesImp;
import com.itextpdf.text.xml.xmp.PdfProperties;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.TreeMap;

public class PdfDocument extends Document {
    protected static final DecimalFormat SIXTEEN_DIGITS = new DecimalFormat("0000000000000000");
    static final String hangingPunctuation = ".,;:'";
    protected PdfDictionary additionalActions;
    protected int alignment = 0;
    protected PdfAction anchorAction = null;
    PdfAnnotationsImp annotationsImp;
    protected HashMap<String, PdfRectangle> boxSize = new HashMap();
    protected PdfCollection collection;
    protected float currentHeight = 0.0f;
    protected PdfOutline currentOutline;
    protected HashMap<String, PdfObject> documentFileAttachment = new HashMap();
    protected HashMap<String, PdfObject> documentLevelJS = new HashMap();
    protected boolean firstPageEvent = true;
    private ArrayList<Element> floatingElements = new ArrayList();
    protected PdfContentByte graphics;
    protected float imageEnd = -1.0f;
    protected Image imageWait = null;
    protected Indentation indentation = new Indentation();
    protected PdfInfo info = new PdfInfo();
    protected boolean isSectionTitle = false;
    int jsCounter;
    protected PdfString language;
    protected int lastElementType = -1;
    protected float leading = 0.0f;
    private Stack<Float> leadingStack = new Stack();
    protected PdfLine line = null;
    protected ArrayList<PdfLine> lines = new ArrayList();
    protected TreeMap<String, Destination> localDestinations = new TreeMap();
    protected HashMap<Object, Integer> markPoints = new HashMap();
    protected float nextMarginBottom;
    protected float nextMarginLeft;
    protected float nextMarginRight;
    protected float nextMarginTop;
    protected Rectangle nextPageSize = null;
    protected PdfAction openActionAction;
    protected String openActionName;
    protected boolean openMCDocument = false;
    protected PdfDictionary pageAA = null;
    private boolean pageEmpty = true;
    protected PdfPageLabels pageLabels;
    protected PageResources pageResources;
    protected PdfOutline rootOutline;
    protected boolean strictImageSequence = false;
    protected HashMap<AccessibleElementId, PdfStructureElement> structElements = new HashMap();
    protected HashMap<Object, int[]> structParentIndices = new HashMap();
    protected TabSettings tabSettings;
    protected PdfContentByte text;
    protected int textEmptySize;
    protected HashMap<String, PdfRectangle> thisBoxSize = new HashMap();
    protected PdfViewerPreferencesImp viewerPreferences = new PdfViewerPreferencesImp();
    protected PdfWriter writer;

    public class Destination {
        public PdfAction action;
        public PdfDestination destination;
        public PdfIndirectReference reference;
    }

    public static class Indentation {
        float imageIndentLeft = 0.0f;
        float imageIndentRight = 0.0f;
        float indentBottom = 0.0f;
        float indentLeft = 0.0f;
        float indentRight = 0.0f;
        float indentTop = 0.0f;
        float listIndentLeft = 0.0f;
        float sectionIndentLeft = 0.0f;
        float sectionIndentRight = 0.0f;
    }

    static class PdfCatalog extends PdfDictionary {
        PdfWriter writer;

        PdfCatalog(PdfIndirectReference pages, PdfWriter writer) {
            super(CATALOG);
            this.writer = writer;
            put(PdfName.PAGES, pages);
        }

        void addNames(TreeMap<String, Destination> localDestinations, HashMap<String, PdfObject> documentLevelJS, HashMap<String, PdfObject> documentFileAttachment, PdfWriter writer) {
            if (!localDestinations.isEmpty() || !documentLevelJS.isEmpty() || !documentFileAttachment.isEmpty()) {
                try {
                    PdfDictionary names = new PdfDictionary();
                    if (!localDestinations.isEmpty()) {
                        PdfArray ar = new PdfArray();
                        for (Entry<String, Destination> entry : localDestinations.entrySet()) {
                            String name = (String) entry.getKey();
                            Destination dest = (Destination) entry.getValue();
                            if (dest.destination != null) {
                                PdfIndirectReference ref = dest.reference;
                                ar.add(new PdfString(name, PdfObject.TEXT_UNICODE));
                                ar.add(ref);
                            }
                        }
                        if (ar.size() > 0) {
                            PdfDictionary dests = new PdfDictionary();
                            dests.put(PdfName.NAMES, ar);
                            names.put(PdfName.DESTS, writer.addToBody(dests).getIndirectReference());
                        }
                    }
                    if (!documentLevelJS.isEmpty()) {
                        names.put(PdfName.JAVASCRIPT, writer.addToBody(PdfNameTree.writeTree(documentLevelJS, writer)).getIndirectReference());
                    }
                    if (!documentFileAttachment.isEmpty()) {
                        names.put(PdfName.EMBEDDEDFILES, writer.addToBody(PdfNameTree.writeTree(documentFileAttachment, writer)).getIndirectReference());
                    }
                    if (names.size() > 0) {
                        put(PdfName.NAMES, writer.addToBody(names).getIndirectReference());
                    }
                } catch (IOException e) {
                    throw new ExceptionConverter(e);
                }
            }
        }

        void setOpenAction(PdfAction action) {
            put(PdfName.OPENACTION, action);
        }

        void setAdditionalActions(PdfDictionary actions) {
            try {
                put(PdfName.AA, this.writer.addToBody(actions).getIndirectReference());
            } catch (Exception e) {
                throw new ExceptionConverter(e);
            }
        }
    }

    public static class PdfInfo extends PdfDictionary {
        PdfInfo() {
            addProducer();
            addCreationDate();
        }

        PdfInfo(String author, String title, String subject) {
            this();
            addTitle(title);
            addSubject(subject);
            addAuthor(author);
        }

        void addTitle(String title) {
            put(PdfName.TITLE, new PdfString(title, PdfObject.TEXT_UNICODE));
        }

        void addSubject(String subject) {
            put(PdfName.SUBJECT, new PdfString(subject, PdfObject.TEXT_UNICODE));
        }

        void addKeywords(String keywords) {
            put(PdfName.KEYWORDS, new PdfString(keywords, PdfObject.TEXT_UNICODE));
        }

        void addAuthor(String author) {
            put(PdfName.AUTHOR, new PdfString(author, PdfObject.TEXT_UNICODE));
        }

        void addCreator(String creator) {
            put(PdfName.CREATOR, new PdfString(creator, PdfObject.TEXT_UNICODE));
        }

        void addProducer() {
            put(PdfName.PRODUCER, new PdfString(Version.getInstance().getVersion()));
        }

        void addCreationDate() {
            PdfString date = new PdfDate();
            put(PdfName.CREATIONDATE, date);
            put(PdfName.MODDATE, date);
        }

        void addkey(String key, String value) {
            if (!key.equals(PdfProperties.PRODUCER) && !key.equals("CreationDate")) {
                put(new PdfName(key), new PdfString(value, PdfObject.TEXT_UNICODE));
            }
        }
    }

    public PdfDocument() {
        addProducer();
        addCreationDate();
    }

    public void addWriter(PdfWriter writer) throws DocumentException {
        if (this.writer == null) {
            this.writer = writer;
            this.annotationsImp = new PdfAnnotationsImp(writer);
            return;
        }
        throw new DocumentException(MessageLocalization.getComposedMessage("you.can.only.add.a.writer.to.a.pdfdocument.once", new Object[0]));
    }

    public float getLeading() {
        return this.leading;
    }

    void setLeading(float leading) {
        this.leading = leading;
    }

    protected void pushLeading() {
        this.leadingStack.push(Float.valueOf(this.leading));
    }

    protected void popLeading() {
        this.leading = ((Float) this.leadingStack.pop()).floatValue();
        if (this.leadingStack.size() > 0) {
            this.leading = ((Float) this.leadingStack.peek()).floatValue();
        }
    }

    public TabSettings getTabSettings() {
        return this.tabSettings;
    }

    public void setTabSettings(TabSettings tabSettings) {
        this.tabSettings = tabSettings;
    }

    public boolean add(Element element) throws DocumentException {
        if (this.writer != null && this.writer.isPaused()) {
            return false;
        }
        try {
            if (element.type() != 37) {
                flushFloatingElements();
            }
            TabSettings backupTabSettings;
            Indentation indentation;
            PdfPageEvent pageEvent;
            switch (element.type()) {
                case 0:
                    this.info.addkey(((Meta) element).getName(), ((Meta) element).getContent());
                    break;
                case 1:
                    this.info.addTitle(((Meta) element).getContent());
                    break;
                case 2:
                    this.info.addSubject(((Meta) element).getContent());
                    break;
                case 3:
                    this.info.addKeywords(((Meta) element).getContent());
                    break;
                case 4:
                    this.info.addAuthor(((Meta) element).getContent());
                    break;
                case 5:
                    this.info.addProducer();
                    break;
                case 6:
                    this.info.addCreationDate();
                    break;
                case 7:
                    this.info.addCreator(((Meta) element).getContent());
                    break;
                case 8:
                    setLanguage(((Meta) element).getContent());
                    break;
                case 10:
                    if (this.line == null) {
                        carriageReturn();
                    }
                    PdfChunk chunk = new PdfChunk((Chunk) element, this.anchorAction, this.tabSettings);
                    while (true) {
                        PdfChunk overflow = this.line.add(chunk);
                        if (overflow == null) {
                            this.pageEmpty = false;
                            if (chunk.isAttribute(Chunk.NEWPAGE)) {
                                newPage();
                                break;
                            }
                        }
                        carriageReturn();
                        boolean newlineSplit = chunk.isNewlineSplit();
                        chunk = overflow;
                        if (!newlineSplit) {
                            chunk.trimFirstSpace();
                        }
                    }
                    break;
                case 11:
                    backupTabSettings = this.tabSettings;
                    if (((Phrase) element).getTabSettings() != null) {
                        this.tabSettings = ((Phrase) element).getTabSettings();
                    }
                    this.leading = ((Phrase) element).getTotalLeading();
                    pushLeading();
                    element.process(this);
                    this.tabSettings = backupTabSettings;
                    popLeading();
                    break;
                case 12:
                    backupTabSettings = this.tabSettings;
                    if (((Phrase) element).getTabSettings() != null) {
                        this.tabSettings = ((Phrase) element).getTabSettings();
                    }
                    Paragraph paragraph = (Paragraph) element;
                    if (isTagged(this.writer)) {
                        flushLines();
                        this.text.openMCBlock(paragraph);
                    }
                    addSpacing(paragraph.getSpacingBefore(), this.leading, paragraph.getFont());
                    this.alignment = paragraph.getAlignment();
                    this.leading = paragraph.getTotalLeading();
                    pushLeading();
                    carriageReturn();
                    if (this.currentHeight + calculateLineHeight() > indentTop() - indentBottom()) {
                        newPage();
                    }
                    indentation = this.indentation;
                    indentation.indentLeft += paragraph.getIndentationLeft();
                    indentation = this.indentation;
                    indentation.indentRight += paragraph.getIndentationRight();
                    carriageReturn();
                    pageEvent = this.writer.getPageEvent();
                    if (!(pageEvent == null || this.isSectionTitle)) {
                        pageEvent.onParagraph(this.writer, this, indentTop() - this.currentHeight);
                    }
                    if (paragraph.getKeepTogether()) {
                        carriageReturn();
                        PdfPTable pdfPTable = new PdfPTable(1);
                        pdfPTable.setKeepTogether(paragraph.getKeepTogether());
                        pdfPTable.setWidthPercentage(100.0f);
                        PdfPCell cell = new PdfPCell();
                        cell.addElement(paragraph);
                        cell.setBorder(0);
                        cell.setPadding(0.0f);
                        pdfPTable.addCell(cell);
                        indentation = this.indentation;
                        indentation.indentLeft -= paragraph.getIndentationLeft();
                        indentation = this.indentation;
                        indentation.indentRight -= paragraph.getIndentationRight();
                        add((Element) pdfPTable);
                        indentation = this.indentation;
                        indentation.indentLeft += paragraph.getIndentationLeft();
                        indentation = this.indentation;
                        indentation.indentRight += paragraph.getIndentationRight();
                    } else {
                        this.line.setExtraIndent(paragraph.getFirstLineIndent());
                        element.process(this);
                        carriageReturn();
                        addSpacing(paragraph.getSpacingAfter(), paragraph.getTotalLeading(), paragraph.getFont(), true);
                    }
                    if (!(pageEvent == null || this.isSectionTitle)) {
                        pageEvent.onParagraphEnd(this.writer, this, indentTop() - this.currentHeight);
                    }
                    this.alignment = 0;
                    if (!(this.floatingElements == null || this.floatingElements.size() == 0)) {
                        flushFloatingElements();
                    }
                    indentation = this.indentation;
                    indentation.indentLeft -= paragraph.getIndentationLeft();
                    indentation = this.indentation;
                    indentation.indentRight -= paragraph.getIndentationRight();
                    carriageReturn();
                    this.tabSettings = backupTabSettings;
                    popLeading();
                    if (isTagged(this.writer)) {
                        flushLines();
                        this.text.closeMCBlock(paragraph);
                        break;
                    }
                    break;
                case 13:
                case 16:
                    Section section = (Section) element;
                    pageEvent = this.writer.getPageEvent();
                    boolean hasTitle = section.isNotAddedYet() && section.getTitle() != null;
                    if (section.isTriggerNewPage()) {
                        newPage();
                    }
                    if (hasTitle) {
                        float fith = indentTop() - this.currentHeight;
                        int rotation = this.pageSize.getRotation();
                        if (rotation == 90 || rotation == 180) {
                            fith = this.pageSize.getHeight() - fith;
                        }
                        PdfDestination pdfDestination = new PdfDestination(2, fith);
                        while (this.currentOutline.level() >= section.getDepth()) {
                            this.currentOutline = this.currentOutline.parent();
                        }
                        this.currentOutline = new PdfOutline(this.currentOutline, pdfDestination, section.getBookmarkTitle(), section.isBookmarkOpen());
                    }
                    carriageReturn();
                    indentation = this.indentation;
                    indentation.sectionIndentLeft += section.getIndentationLeft();
                    indentation = this.indentation;
                    indentation.sectionIndentRight += section.getIndentationRight();
                    if (section.isNotAddedYet() && pageEvent != null) {
                        if (element.type() == 16) {
                            pageEvent.onChapter(this.writer, this, indentTop() - this.currentHeight, section.getTitle());
                        } else {
                            pageEvent.onSection(this.writer, this, indentTop() - this.currentHeight, section.getDepth(), section.getTitle());
                        }
                    }
                    if (hasTitle) {
                        this.isSectionTitle = true;
                        add(section.getTitle());
                        this.isSectionTitle = false;
                    }
                    indentation = this.indentation;
                    indentation.sectionIndentLeft += section.getIndentation();
                    element.process(this);
                    flushLines();
                    indentation = this.indentation;
                    indentation.sectionIndentLeft -= section.getIndentationLeft() + section.getIndentation();
                    indentation = this.indentation;
                    indentation.sectionIndentRight -= section.getIndentationRight();
                    if (section.isComplete() && pageEvent != null) {
                        if (element.type() != 16) {
                            pageEvent.onSectionEnd(this.writer, this, indentTop() - this.currentHeight);
                            break;
                        }
                        pageEvent.onChapterEnd(this.writer, this, indentTop() - this.currentHeight);
                        break;
                    }
                    break;
                case 14:
                    List list = (List) element;
                    if (isTagged(this.writer)) {
                        flushLines();
                        this.text.openMCBlock(list);
                    }
                    if (list.isAlignindent()) {
                        list.normalizeIndentation();
                    }
                    indentation = this.indentation;
                    indentation.listIndentLeft += list.getIndentationLeft();
                    indentation = this.indentation;
                    indentation.indentRight += list.getIndentationRight();
                    element.process(this);
                    indentation = this.indentation;
                    indentation.listIndentLeft -= list.getIndentationLeft();
                    indentation = this.indentation;
                    indentation.indentRight -= list.getIndentationRight();
                    carriageReturn();
                    if (isTagged(this.writer)) {
                        flushLines();
                        this.text.closeMCBlock(list);
                        break;
                    }
                    break;
                case 15:
                    ListItem listItem = (ListItem) element;
                    if (isTagged(this.writer)) {
                        flushLines();
                        this.text.openMCBlock(listItem);
                    }
                    addSpacing(listItem.getSpacingBefore(), this.leading, listItem.getFont());
                    this.alignment = listItem.getAlignment();
                    indentation = this.indentation;
                    indentation.listIndentLeft += listItem.getIndentationLeft();
                    indentation = this.indentation;
                    indentation.indentRight += listItem.getIndentationRight();
                    this.leading = listItem.getTotalLeading();
                    pushLeading();
                    carriageReturn();
                    this.line.setListItem(listItem);
                    element.process(this);
                    addSpacing(listItem.getSpacingAfter(), listItem.getTotalLeading(), listItem.getFont(), true);
                    if (this.line.hasToBeJustified()) {
                        this.line.resetAlignment();
                    }
                    carriageReturn();
                    indentation = this.indentation;
                    indentation.listIndentLeft -= listItem.getIndentationLeft();
                    indentation = this.indentation;
                    indentation.indentRight -= listItem.getIndentationRight();
                    popLeading();
                    if (isTagged(this.writer)) {
                        flushLines();
                        this.text.closeMCBlock(listItem.getListBody());
                        this.text.closeMCBlock(listItem);
                        break;
                    }
                    break;
                case 17:
                    Anchor anchor = (Anchor) element;
                    String url = anchor.getReference();
                    this.leading = anchor.getLeading();
                    pushLeading();
                    if (url != null) {
                        this.anchorAction = new PdfAction(url);
                    }
                    element.process(this);
                    this.anchorAction = null;
                    popLeading();
                    break;
                case 23:
                    PdfPTable ptable = (PdfPTable) element;
                    if (ptable.size() > ptable.getHeaderRows()) {
                        ensureNewLine();
                        flushLines();
                        addPTable(ptable);
                        this.pageEmpty = false;
                        newLine();
                        break;
                    }
                    break;
                case 29:
                    if (this.line == null) {
                        carriageReturn();
                    }
                    Annotation annot = (Annotation) element;
                    Rectangle rectangle = new Rectangle(0.0f, 0.0f);
                    if (this.line != null) {
                        rectangle = new Rectangle(annot.llx(indentRight() - this.line.widthLeft()), annot.ury((indentTop() - this.currentHeight) - 20.0f), annot.urx((indentRight() - this.line.widthLeft()) + 20.0f), annot.lly(indentTop() - this.currentHeight));
                    }
                    this.annotationsImp.addPlainAnnotation(PdfAnnotationsImp.convertAnnotation(this.writer, annot, rect));
                    this.pageEmpty = false;
                    break;
                case 30:
                    this.graphics.rectangle((Rectangle) element);
                    this.pageEmpty = false;
                    break;
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                    if (isTagged(this.writer) && !((Image) element).isImgTemplate()) {
                        flushLines();
                        this.text.openMCBlock((Image) element);
                    }
                    add((Image) element);
                    if (isTagged(this.writer) && !((Image) element).isImgTemplate()) {
                        flushLines();
                        this.text.closeMCBlock((Image) element);
                        break;
                    }
                case 37:
                    ensureNewLine();
                    flushLines();
                    addDiv((PdfDiv) element);
                    this.pageEmpty = false;
                    break;
                case 50:
                    if (element instanceof MarkedSection) {
                        MarkedObject mo = ((MarkedSection) element).getTitle();
                        if (mo != null) {
                            mo.process(this);
                        }
                    }
                    ((MarkedObject) element).process(this);
                    break;
                case 55:
                    ((DrawInterface) element).draw(this.graphics, indentLeft(), indentBottom(), indentRight(), indentTop(), (indentTop() - this.currentHeight) - (this.leadingStack.size() > 0 ? this.leading : 0.0f));
                    this.pageEmpty = false;
                    break;
                case Element.WRITABLE_DIRECT /*666*/:
                    if (this.writer != null) {
                        ((WriterOperation) element).write(this.writer, this);
                        break;
                    }
                    break;
                default:
                    return false;
            }
            this.lastElementType = element.type();
            return true;
        } catch (Exception e) {
            throw new DocumentException(e);
        }
    }

    public void open() {
        if (!this.open) {
            super.open();
            this.writer.open();
            this.rootOutline = new PdfOutline(this.writer);
            this.currentOutline = this.rootOutline;
        }
        try {
            initPage();
            if (isTagged(this.writer)) {
                this.openMCDocument = true;
            }
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    public void close() {
        boolean wasImage = false;
        if (!this.close) {
            try {
                if (isTagged(this.writer)) {
                    flushFloatingElements();
                    flushLines();
                    this.writer.getDirectContent().closeMCBlock(this);
                    this.writer.flushAcroFields();
                    this.writer.flushTaggedObjects();
                    if (isPageEmpty()) {
                        int pageReferenceCount = this.writer.pageReferences.size();
                        if (pageReferenceCount > 0 && this.writer.currentPageNumber == pageReferenceCount) {
                            this.writer.pageReferences.remove(pageReferenceCount - 1);
                        }
                    }
                } else {
                    this.writer.flushAcroFields();
                }
                if (this.imageWait != null) {
                    wasImage = true;
                }
                newPage();
                if (this.imageWait != null || wasImage) {
                    newPage();
                }
                if (this.annotationsImp.hasUnusedAnnotations()) {
                    throw new RuntimeException(MessageLocalization.getComposedMessage("not.all.annotations.could.be.added.to.the.document.the.document.doesn.t.have.enough.pages", new Object[0]));
                }
                PdfPageEvent pageEvent = this.writer.getPageEvent();
                if (pageEvent != null) {
                    pageEvent.onCloseDocument(this.writer, this);
                }
                super.close();
                this.writer.addLocalDestinations(this.localDestinations);
                calculateOutlineCount();
                writeOutlines();
                this.writer.close();
            } catch (Exception e) {
                throw ExceptionConverter.convertException(e);
            }
        }
    }

    public void setXmpMetadata(byte[] xmpMetadata) throws IOException {
        PdfStream xmp = new PdfStream(xmpMetadata);
        xmp.put(PdfName.TYPE, PdfName.METADATA);
        xmp.put(PdfName.SUBTYPE, PdfName.XML);
        PdfEncryption crypto = this.writer.getEncryption();
        if (!(crypto == null || crypto.isMetadataEncrypted())) {
            PdfArray ar = new PdfArray();
            ar.add(PdfName.CRYPT);
            xmp.put(PdfName.FILTER, ar);
        }
        this.writer.addPageDictEntry(PdfName.METADATA, this.writer.addToBody(xmp).getIndirectReference());
    }

    public boolean newPage() {
        try {
            flushFloatingElements();
            this.lastElementType = -1;
            if (isPageEmpty()) {
                setNewPageSizeAndMargins();
                return false;
            } else if (!this.open || this.close) {
                throw new RuntimeException(MessageLocalization.getComposedMessage("the.document.is.not.open", new Object[0]));
            } else {
                PdfPageEvent pageEvent = this.writer.getPageEvent();
                if (pageEvent != null) {
                    pageEvent.onEndPage(this.writer, this);
                }
                super.newPage();
                this.indentation.imageIndentLeft = 0.0f;
                this.indentation.imageIndentRight = 0.0f;
                try {
                    flushLines();
                    int rotation = this.pageSize.getRotation();
                    if (this.writer.isPdfIso()) {
                        if (this.thisBoxSize.containsKey("art") && this.thisBoxSize.containsKey("trim")) {
                            throw new PdfXConformanceException(MessageLocalization.getComposedMessage("only.one.of.artbox.or.trimbox.can.exist.in.the.page", new Object[0]));
                        } else if (!(this.thisBoxSize.containsKey("art") || this.thisBoxSize.containsKey("trim"))) {
                            if (this.thisBoxSize.containsKey("crop")) {
                                this.thisBoxSize.put("trim", this.thisBoxSize.get("crop"));
                            } else {
                                this.thisBoxSize.put("trim", new PdfRectangle(this.pageSize, this.pageSize.getRotation()));
                            }
                        }
                    }
                    this.pageResources.addDefaultColorDiff(this.writer.getDefaultColorspace());
                    if (this.writer.isRgbTransparencyBlending()) {
                        PdfDictionary dcs = new PdfDictionary();
                        dcs.put(PdfName.CS, PdfName.DEVICERGB);
                        this.pageResources.addDefaultColorDiff(dcs);
                    }
                    PdfPage page = new PdfPage(new PdfRectangle(this.pageSize, rotation), this.thisBoxSize, this.pageResources.getResources(), rotation);
                    if (isTagged(this.writer)) {
                        page.put(PdfName.TABS, PdfName.f133S);
                    } else {
                        page.put(PdfName.TABS, this.writer.getTabs());
                    }
                    page.putAll(this.writer.getPageDictEntries());
                    this.writer.resetPageDictEntries();
                    if (this.pageAA != null) {
                        page.put(PdfName.AA, this.writer.addToBody(this.pageAA).getIndirectReference());
                        this.pageAA = null;
                    }
                    if (this.annotationsImp.hasUnusedAnnotations()) {
                        PdfArray array = this.annotationsImp.rotateAnnotations(this.writer, this.pageSize);
                        if (array.size() != 0) {
                            page.put(PdfName.ANNOTS, array);
                        }
                    }
                    if (isTagged(this.writer)) {
                        page.put(PdfName.STRUCTPARENTS, new PdfNumber(getStructParentIndex(this.writer.getCurrentPage())));
                    }
                    if (this.text.size() > this.textEmptySize || isTagged(this.writer)) {
                        this.text.endText();
                    } else {
                        this.text = null;
                    }
                    ArrayList<IAccessibleElement> mcBlocks = null;
                    if (isTagged(this.writer)) {
                        mcBlocks = this.writer.getDirectContent().saveMCBlocks();
                    }
                    this.writer.add(page, new PdfContents(this.writer.getDirectContentUnder(), this.graphics, !isTagged(this.writer) ? this.text : null, this.writer.getDirectContent(), this.pageSize));
                    initPage();
                    if (isTagged(this.writer)) {
                        this.writer.getDirectContentUnder().restoreMCBlocks(mcBlocks);
                    }
                    return true;
                } catch (DocumentException de) {
                    throw new ExceptionConverter(de);
                } catch (IOException ioe) {
                    throw new ExceptionConverter(ioe);
                }
            }
        } catch (DocumentException de2) {
            throw new ExceptionConverter(de2);
        }
    }

    public boolean setPageSize(Rectangle pageSize) {
        if (this.writer != null && this.writer.isPaused()) {
            return false;
        }
        this.nextPageSize = new Rectangle(pageSize);
        return true;
    }

    public boolean setMargins(float marginLeft, float marginRight, float marginTop, float marginBottom) {
        if (this.writer != null && this.writer.isPaused()) {
            return false;
        }
        this.nextMarginLeft = marginLeft;
        this.nextMarginRight = marginRight;
        this.nextMarginTop = marginTop;
        this.nextMarginBottom = marginBottom;
        return true;
    }

    public boolean setMarginMirroring(boolean MarginMirroring) {
        if (this.writer == null || !this.writer.isPaused()) {
            return super.setMarginMirroring(MarginMirroring);
        }
        return false;
    }

    public boolean setMarginMirroringTopBottom(boolean MarginMirroringTopBottom) {
        if (this.writer == null || !this.writer.isPaused()) {
            return super.setMarginMirroringTopBottom(MarginMirroringTopBottom);
        }
        return false;
    }

    public void setPageCount(int pageN) {
        if (this.writer == null || !this.writer.isPaused()) {
            super.setPageCount(pageN);
        }
    }

    public void resetPageCount() {
        if (this.writer == null || !this.writer.isPaused()) {
            super.resetPageCount();
        }
    }

    protected void initPage() throws DocumentException {
        this.pageN++;
        this.annotationsImp.resetAnnotations();
        this.pageResources = new PageResources();
        this.writer.resetContent();
        if (isTagged(this.writer)) {
            this.graphics = this.writer.getDirectContentUnder().getDuplicate();
            this.writer.getDirectContent().duplicatedFrom = this.graphics;
        } else {
            this.graphics = new PdfContentByte(this.writer);
        }
        setNewPageSizeAndMargins();
        this.imageEnd = -1.0f;
        this.indentation.imageIndentRight = 0.0f;
        this.indentation.imageIndentLeft = 0.0f;
        this.indentation.indentBottom = 0.0f;
        this.indentation.indentTop = 0.0f;
        this.currentHeight = 0.0f;
        this.thisBoxSize = new HashMap(this.boxSize);
        if (!(this.pageSize.getBackgroundColor() == null && !this.pageSize.hasBorders() && this.pageSize.getBorderColor() == null)) {
            add(this.pageSize);
        }
        float oldleading = this.leading;
        int oldAlignment = this.alignment;
        this.pageEmpty = true;
        try {
            if (this.imageWait != null) {
                add(this.imageWait);
                this.imageWait = null;
            }
            this.leading = oldleading;
            this.alignment = oldAlignment;
            carriageReturn();
            PdfPageEvent pageEvent = this.writer.getPageEvent();
            if (pageEvent != null) {
                if (this.firstPageEvent) {
                    pageEvent.onOpenDocument(this.writer, this);
                }
                pageEvent.onStartPage(this.writer, this);
            }
            this.firstPageEvent = false;
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    protected void newLine() throws DocumentException {
        this.lastElementType = -1;
        carriageReturn();
        if (!(this.lines == null || this.lines.isEmpty())) {
            this.lines.add(this.line);
            this.currentHeight += this.line.height();
        }
        this.line = new PdfLine(indentLeft(), indentRight(), this.alignment, this.leading);
    }

    protected float calculateLineHeight() {
        float tempHeight = this.line.height();
        if (tempHeight != this.leading) {
            return tempHeight + this.leading;
        }
        return tempHeight;
    }

    protected void carriageReturn() {
        if (this.lines == null) {
            this.lines = new ArrayList();
        }
        if (this.line != null && this.line.size() > 0) {
            if (this.currentHeight + calculateLineHeight() > indentTop() - indentBottom() && this.currentHeight != 0.0f) {
                PdfLine overflowLine = this.line;
                this.line = null;
                newPage();
                this.line = overflowLine;
                overflowLine.left = indentLeft();
            }
            this.currentHeight += this.line.height();
            this.lines.add(this.line);
            this.pageEmpty = false;
        }
        if (this.imageEnd > -1.0f && this.currentHeight > this.imageEnd) {
            this.imageEnd = -1.0f;
            this.indentation.imageIndentRight = 0.0f;
            this.indentation.imageIndentLeft = 0.0f;
        }
        this.line = new PdfLine(indentLeft(), indentRight(), this.alignment, this.leading);
    }

    public float getVerticalPosition(boolean ensureNewLine) {
        if (ensureNewLine) {
            ensureNewLine();
        }
        return (top() - this.currentHeight) - this.indentation.indentTop;
    }

    protected void ensureNewLine() {
        try {
            if (this.lastElementType == 11 || this.lastElementType == 10) {
                newLine();
                flushLines();
            }
        } catch (DocumentException ex) {
            throw new ExceptionConverter(ex);
        }
    }

    protected float flushLines() throws DocumentException {
        if (this.lines == null) {
            return 0.0f;
        }
        if (this.line != null && this.line.size() > 0) {
            this.lines.add(this.line);
            this.line = new PdfLine(indentLeft(), indentRight(), this.alignment, this.leading);
        }
        if (this.lines.isEmpty()) {
            return 0.0f;
        }
        Object[] currentValues = new Object[2];
        PdfFont currentFont = null;
        float displacement = 0.0f;
        currentValues[1] = new Float(0.0f);
        Iterator i$ = this.lines.iterator();
        while (i$.hasNext()) {
            PdfLine l = (PdfLine) i$.next();
            float moveTextX = (((l.indentLeft() - indentLeft()) + this.indentation.indentLeft) + this.indentation.listIndentLeft) + this.indentation.sectionIndentLeft;
            this.text.moveText(moveTextX, -l.height());
            l.flush();
            if (l.listSymbol() != null) {
                ListLabel lbl = null;
                Chunk symbol = l.listSymbol();
                if (isTagged(this.writer)) {
                    lbl = l.listItem().getListLabel();
                    this.graphics.openMCBlock(lbl);
                    Chunk chunk = new Chunk(symbol);
                    chunk.setRole(null);
                    symbol = chunk;
                }
                ColumnText.showTextAligned(this.graphics, 0, new Phrase(symbol), this.text.getXTLM() - l.listIndent(), this.text.getYTLM(), 0.0f);
                if (lbl != null) {
                    this.graphics.closeMCBlock(lbl);
                }
            }
            currentValues[0] = currentFont;
            if (isTagged(this.writer) && l.listItem() != null) {
                this.text.openMCBlock(l.listItem().getListBody());
            }
            writeLineToContent(l, this.text, this.graphics, currentValues, this.writer.getSpaceCharRatio());
            currentFont = currentValues[0];
            displacement += l.height();
            this.text.moveText(-moveTextX, 0.0f);
        }
        this.lines = new ArrayList();
        return displacement;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    float writeLineToContent(com.itextpdf.text.pdf.PdfLine r96, com.itextpdf.text.pdf.PdfContentByte r97, com.itextpdf.text.pdf.PdfContentByte r98, java.lang.Object[] r99, float r100) throws com.itextpdf.text.DocumentException {
        /*
        r95 = this;
        r4 = 0;
        r42 = r99[r4];
        r42 = (com.itextpdf.text.pdf.PdfFont) r42;
        r4 = 1;
        r4 = r99[r4];
        r4 = (java.lang.Float) r4;
        r57 = r4.floatValue();
        r49 = 0;
        r18 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r59 = 2143289344; // 0x7fc00000 float:NaN double:1.058925634E-314;
        r35 = 0;
        r33 = 0;
        r47 = 0;
        r4 = r97.getXTLM();
        r6 = r96.getOriginalWidth();
        r61 = r4 + r6;
        r65 = r96.numberOfSpaces();
        r62 = r96.getLineLengthUtf32();
        r4 = r96.hasToBeJustified();
        if (r4 == 0) goto L_0x027b;
    L_0x0032:
        if (r65 != 0) goto L_0x0039;
    L_0x0034:
        r4 = 1;
        r0 = r62;
        if (r0 <= r4) goto L_0x027b;
    L_0x0039:
        r53 = 1;
    L_0x003b:
        r76 = r96.getSeparatorCount();
        if (r76 <= 0) goto L_0x027f;
    L_0x0041:
        r4 = r96.widthLeft();
        r0 = r76;
        r6 = (float) r0;
        r47 = r4 / r6;
    L_0x004a:
        r58 = r96.getLastStrokeChunk();
        r39 = 0;
        r12 = r97.getXTLM();
        r5 = r12;
        r9 = r97.getYTLM();
        r29 = 0;
        r14 = 0;
        r54 = r96.iterator();
        r85 = r14;
    L_0x0062:
        r4 = r54.hasNext();
        if (r4 == 0) goto L_0x0929;
    L_0x0068:
        r38 = r54.next();
        r38 = (com.itextpdf.text.pdf.PdfChunk) r38;
        r0 = r95;
        r4 = r0.writer;
        r4 = isTagged(r4);
        if (r4 == 0) goto L_0x0087;
    L_0x0078:
        r0 = r38;
        r4 = r0.accessibleElement;
        if (r4 == 0) goto L_0x0087;
    L_0x007e:
        r0 = r38;
        r4 = r0.accessibleElement;
        r0 = r97;
        r0.openMCBlock(r4);
    L_0x0087:
        r40 = r38.color();
        r4 = r38.font();
        r45 = r4.size();
        r4 = r38.isImage();
        if (r4 == 0) goto L_0x0335;
    L_0x0099:
        r32 = r38.height();
        r43 = 0;
    L_0x009f:
        r48 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r0 = r39;
        r1 = r58;
        if (r0 > r1) goto L_0x096d;
    L_0x00a7:
        if (r53 == 0) goto L_0x0355;
    L_0x00a9:
        r0 = r38;
        r1 = r33;
        r2 = r35;
        r93 = r0.getWidthCorrected(r1, r2);
    L_0x00b3:
        r4 = r38.isStroked();
        if (r4 == 0) goto L_0x0967;
    L_0x00b9:
        r4 = r39 + 1;
        r0 = r96;
        r64 = r0.getChunk(r4);
        r4 = r38.isSeparator();
        if (r4 == 0) goto L_0x00f6;
    L_0x00c7:
        r93 = r47;
        r4 = "SEPARATOR";
        r0 = r38;
        r4 = r0.getAttribute(r4);
        r4 = (java.lang.Object[]) r4;
        r75 = r4;
        r75 = (java.lang.Object[]) r75;
        r4 = 0;
        r3 = r75[r4];
        r3 = (com.itextpdf.text.pdf.draw.DrawInterface) r3;
        r4 = 1;
        r92 = r75[r4];
        r92 = (java.lang.Boolean) r92;
        r4 = r92.booleanValue();
        if (r4 == 0) goto L_0x035b;
    L_0x00e7:
        r6 = r9 + r43;
        r4 = r96.getOriginalWidth();
        r7 = r5 + r4;
        r8 = r32 - r43;
        r4 = r98;
        r3.draw(r4, r5, r6, r7, r8, r9);
    L_0x00f6:
        r4 = r38.isTab();
        if (r4 == 0) goto L_0x0963;
    L_0x00fc:
        r4 = "TABSETTINGS";
        r0 = r38;
        r4 = r0.isAttribute(r4);
        if (r4 == 0) goto L_0x036e;
    L_0x0106:
        r86 = r38.getTabStop();
        if (r86 == 0) goto L_0x036b;
    L_0x010c:
        r4 = r86.getPosition();
        r14 = r4 + r5;
        r4 = r86.getLeader();
        if (r4 == 0) goto L_0x0127;
    L_0x0118:
        r10 = r86.getLeader();
        r13 = r9 + r43;
        r15 = r32 - r43;
        r11 = r98;
        r16 = r9;
        r10.draw(r11, r12, r13, r14, r15, r16);
    L_0x0127:
        r89 = r12;
        r12 = r14;
        r14 = r89;
    L_0x012c:
        r4 = "BACKGROUND";
        r0 = r38;
        r4 = r0.isAttribute(r4);
        if (r4 == 0) goto L_0x01c2;
    L_0x0136:
        r52 = r98.getInText();
        if (r52 == 0) goto L_0x0149;
    L_0x013c:
        r0 = r95;
        r4 = r0.writer;
        r4 = isTagged(r4);
        if (r4 == 0) goto L_0x0149;
    L_0x0146:
        r98.endText();
    L_0x0149:
        r83 = r57;
        if (r64 == 0) goto L_0x0159;
    L_0x014d:
        r4 = "BACKGROUND";
        r0 = r64;
        r4 = r0.isAttribute(r4);
        if (r4 == 0) goto L_0x0159;
    L_0x0157:
        r83 = 0;
    L_0x0159:
        if (r64 != 0) goto L_0x015d;
    L_0x015b:
        r83 = r83 + r49;
    L_0x015d:
        r4 = "BACKGROUND";
        r0 = r38;
        r4 = r0.getAttribute(r4);
        r4 = (java.lang.Object[]) r4;
        r36 = r4;
        r36 = (java.lang.Object[]) r36;
        r4 = 0;
        r4 = r36[r4];
        r4 = (com.itextpdf.text.BaseColor) r4;
        r0 = r98;
        r0.setColorFill(r4);
        r4 = 1;
        r4 = r36[r4];
        r4 = (float[]) r4;
        r44 = r4;
        r44 = (float[]) r44;
        r4 = 0;
        r4 = r44[r4];
        r4 = r12 - r4;
        r6 = r9 + r43;
        r7 = 1;
        r7 = r44[r7];
        r6 = r6 - r7;
        r7 = r38.getTextRise();
        r6 = r6 + r7;
        r7 = r93 - r83;
        r8 = 0;
        r8 = r44[r8];
        r7 = r7 + r8;
        r8 = 2;
        r8 = r44[r8];
        r7 = r7 + r8;
        r8 = r32 - r43;
        r10 = 1;
        r10 = r44[r10];
        r8 = r8 + r10;
        r10 = 3;
        r10 = r44[r10];
        r8 = r8 + r10;
        r0 = r98;
        r0.rectangle(r4, r6, r7, r8);
        r98.fill();
        r4 = 0;
        r0 = r98;
        r0.setGrayFill(r4);
        if (r52 == 0) goto L_0x01c2;
    L_0x01b2:
        r0 = r95;
        r4 = r0.writer;
        r4 = isTagged(r4);
        if (r4 == 0) goto L_0x01c2;
    L_0x01bc:
        r4 = 1;
        r0 = r98;
        r0.beginText(r4);
    L_0x01c2:
        r4 = "UNDERLINE";
        r0 = r38;
        r4 = r0.isAttribute(r4);
        if (r4 == 0) goto L_0x03c0;
    L_0x01cc:
        r4 = r38.isNewlineSplit();
        if (r4 != 0) goto L_0x03c0;
    L_0x01d2:
        r52 = r98.getInText();
        if (r52 == 0) goto L_0x01e5;
    L_0x01d8:
        r0 = r95;
        r4 = r0.writer;
        r4 = isTagged(r4);
        if (r4 == 0) goto L_0x01e5;
    L_0x01e2:
        r98.endText();
    L_0x01e5:
        r83 = r57;
        if (r64 == 0) goto L_0x01f5;
    L_0x01e9:
        r4 = "UNDERLINE";
        r0 = r64;
        r4 = r0.isAttribute(r4);
        if (r4 == 0) goto L_0x01f5;
    L_0x01f3:
        r83 = 0;
    L_0x01f5:
        if (r64 != 0) goto L_0x01f9;
    L_0x01f7:
        r83 = r83 + r49;
    L_0x01f9:
        r4 = "UNDERLINE";
        r0 = r38;
        r4 = r0.getAttribute(r4);
        r4 = (java.lang.Object[][]) r4;
        r91 = r4;
        r91 = (java.lang.Object[][]) r91;
        r74 = 0;
        r55 = 0;
    L_0x020b:
        r0 = r91;
        r4 = r0.length;
        r0 = r55;
        if (r0 >= r4) goto L_0x03a7;
    L_0x0212:
        r66 = r91[r55];
        r4 = 0;
        r74 = r66[r4];
        r74 = (com.itextpdf.text.BaseColor) r74;
        r4 = 1;
        r4 = r66[r4];
        r4 = (float[]) r4;
        r70 = r4;
        r70 = (float[]) r70;
        if (r74 != 0) goto L_0x0226;
    L_0x0224:
        r74 = r40;
    L_0x0226:
        if (r74 == 0) goto L_0x022f;
    L_0x0228:
        r0 = r98;
        r1 = r74;
        r0.setColorStroke(r1);
    L_0x022f:
        r4 = 0;
        r4 = r70[r4];
        r6 = 1;
        r6 = r70[r6];
        r6 = r6 * r45;
        r4 = r4 + r6;
        r0 = r98;
        r0.setLineWidth(r4);
        r4 = 2;
        r4 = r70[r4];
        r6 = 3;
        r6 = r70[r6];
        r6 = r6 * r45;
        r77 = r4 + r6;
        r4 = 4;
        r4 = r70[r4];
        r0 = (int) r4;
        r37 = r0;
        if (r37 == 0) goto L_0x0256;
    L_0x024f:
        r0 = r98;
        r1 = r37;
        r0.setLineCap(r1);
    L_0x0256:
        r4 = r9 + r77;
        r0 = r98;
        r0.moveTo(r12, r4);
        r4 = r12 + r93;
        r4 = r4 - r83;
        r6 = r9 + r77;
        r0 = r98;
        r0.lineTo(r4, r6);
        r98.stroke();
        if (r74 == 0) goto L_0x0270;
    L_0x026d:
        r98.resetGrayStroke();
    L_0x0270:
        if (r37 == 0) goto L_0x0278;
    L_0x0272:
        r4 = 0;
        r0 = r98;
        r0.setLineCap(r4);
    L_0x0278:
        r55 = r55 + 1;
        goto L_0x020b;
    L_0x027b:
        r53 = 0;
        goto L_0x003b;
    L_0x027f:
        if (r53 == 0) goto L_0x0320;
    L_0x0281:
        if (r76 != 0) goto L_0x0320;
    L_0x0283:
        r4 = r96.isNewlineSplit();
        if (r4 == 0) goto L_0x02c4;
    L_0x0289:
        r4 = r96.widthLeft();
        r0 = r65;
        r6 = (float) r0;
        r6 = r6 * r100;
        r0 = r62;
        r7 = (float) r0;
        r6 = r6 + r7;
        r7 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r6 = r6 - r7;
        r6 = r6 * r57;
        r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r4 < 0) goto L_0x02c4;
    L_0x029f:
        r4 = r96.isRTL();
        if (r4 == 0) goto L_0x02be;
    L_0x02a5:
        r4 = r96.widthLeft();
        r0 = r65;
        r6 = (float) r0;
        r6 = r6 * r100;
        r0 = r62;
        r7 = (float) r0;
        r6 = r6 + r7;
        r7 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r6 = r6 - r7;
        r6 = r6 * r57;
        r4 = r4 - r6;
        r6 = 0;
        r0 = r97;
        r0.moveText(r4, r6);
    L_0x02be:
        r35 = r100 * r57;
        r33 = r57;
        goto L_0x004a;
    L_0x02c4:
        r93 = r96.widthLeft();
        r4 = r96.size();
        r4 = r4 + -1;
        r0 = r96;
        r56 = r0.getChunk(r4);
        if (r56 == 0) goto L_0x030a;
    L_0x02d6:
        r73 = r56.toString();
        r4 = r73.length();
        if (r4 <= 0) goto L_0x030a;
    L_0x02e0:
        r4 = ".,;:'";
        r6 = r73.length();
        r6 = r6 + -1;
        r0 = r73;
        r20 = r0.charAt(r6);
        r0 = r20;
        r4 = r4.indexOf(r0);
        if (r4 < 0) goto L_0x030a;
    L_0x02f6:
        r67 = r93;
        r4 = r56.font();
        r0 = r20;
        r4 = r4.width(r0);
        r6 = 1053609165; // 0x3ecccccd float:0.4 double:5.205520926E-315;
        r4 = r4 * r6;
        r93 = r93 + r4;
        r49 = r93 - r67;
    L_0x030a:
        r0 = r65;
        r4 = (float) r0;
        r4 = r4 * r100;
        r0 = r62;
        r6 = (float) r0;
        r4 = r4 + r6;
        r6 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r4 = r4 - r6;
        r34 = r93 / r4;
        r35 = r100 * r34;
        r33 = r34;
        r57 = r34;
        goto L_0x004a;
    L_0x0320:
        r0 = r96;
        r4 = r0.alignment;
        if (r4 == 0) goto L_0x032d;
    L_0x0326:
        r0 = r96;
        r4 = r0.alignment;
        r6 = -1;
        if (r4 != r6) goto L_0x004a;
    L_0x032d:
        r4 = r96.widthLeft();
        r61 = r61 - r4;
        goto L_0x004a;
    L_0x0335:
        r4 = r38.font();
        r4 = r4.getFont();
        r6 = 1;
        r0 = r45;
        r32 = r4.getFontDescriptor(r6, r0);
        r4 = r38.font();
        r4 = r4.getFont();
        r6 = 3;
        r0 = r45;
        r43 = r4.getFontDescriptor(r6, r0);
        goto L_0x009f;
    L_0x0355:
        r93 = r38.width();
        goto L_0x00b3;
    L_0x035b:
        r13 = r9 + r43;
        r14 = r12 + r93;
        r15 = r32 - r43;
        r10 = r3;
        r11 = r98;
        r16 = r9;
        r10.draw(r11, r12, r13, r14, r15, r16);
        goto L_0x00f6;
    L_0x036b:
        r14 = r12;
        goto L_0x0127;
    L_0x036e:
        r4 = "TAB";
        r0 = r38;
        r4 = r0.getAttribute(r4);
        r4 = (java.lang.Object[]) r4;
        r84 = r4;
        r84 = (java.lang.Object[]) r84;
        r4 = 0;
        r3 = r84[r4];
        r3 = (com.itextpdf.text.pdf.draw.DrawInterface) r3;
        r4 = 1;
        r4 = r84[r4];
        r4 = (java.lang.Float) r4;
        r6 = r4.floatValue();
        r4 = 3;
        r4 = r84[r4];
        r4 = (java.lang.Float) r4;
        r4 = r4.floatValue();
        r14 = r6 + r4;
        r4 = (r14 > r12 ? 1 : (r14 == r12 ? 0 : -1));
        if (r4 <= 0) goto L_0x0127;
    L_0x0399:
        r13 = r9 + r43;
        r15 = r32 - r43;
        r10 = r3;
        r11 = r98;
        r16 = r9;
        r10.draw(r11, r12, r13, r14, r15, r16);
        goto L_0x0127;
    L_0x03a7:
        r4 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r0 = r98;
        r0.setLineWidth(r4);
        if (r52 == 0) goto L_0x03c0;
    L_0x03b0:
        r0 = r95;
        r4 = r0.writer;
        r4 = isTagged(r4);
        if (r4 == 0) goto L_0x03c0;
    L_0x03ba:
        r4 = 1;
        r0 = r98;
        r0.beginText(r4);
    L_0x03c0:
        r4 = "ACTION";
        r0 = r38;
        r4 = r0.isAttribute(r4);
        if (r4 == 0) goto L_0x0473;
    L_0x03ca:
        r83 = r57;
        if (r64 == 0) goto L_0x03da;
    L_0x03ce:
        r4 = "ACTION";
        r0 = r64;
        r4 = r0.isAttribute(r4);
        if (r4 == 0) goto L_0x03da;
    L_0x03d8:
        r83 = 0;
    L_0x03da:
        if (r64 != 0) goto L_0x03de;
    L_0x03dc:
        r83 = r83 + r49;
    L_0x03de:
        r30 = 0;
        r4 = r38.isImage();
        if (r4 == 0) goto L_0x07bb;
    L_0x03e6:
        r0 = r95;
        r15 = r0.writer;
        r4 = r38.getImageOffsetY();
        r17 = r9 + r4;
        r4 = r12 + r93;
        r18 = r4 - r83;
        r4 = r38.getImageHeight();
        r4 = r4 + r9;
        r6 = r38.getImageOffsetY();
        r19 = r4 + r6;
        r4 = "ACTION";
        r0 = r38;
        r20 = r0.getAttribute(r4);
        r20 = (com.itextpdf.text.pdf.PdfAction) r20;
        r21 = 0;
        r16 = r12;
        r30 = r15.createAnnotation(r16, r17, r18, r19, r20, r21);
    L_0x0411:
        r4 = 1;
        r0 = r97;
        r1 = r30;
        r0.addAnnotation(r1, r4);
        r0 = r95;
        r4 = r0.writer;
        r4 = isTagged(r4);
        if (r4 == 0) goto L_0x0473;
    L_0x0423:
        r0 = r38;
        r4 = r0.accessibleElement;
        if (r4 == 0) goto L_0x0473;
    L_0x0429:
        r0 = r95;
        r4 = r0.structElements;
        r0 = r38;
        r6 = r0.accessibleElement;
        r6 = r6.getId();
        r81 = r4.get(r6);
        r81 = (com.itextpdf.text.pdf.PdfStructureElement) r81;
        if (r81 == 0) goto L_0x0473;
    L_0x043d:
        r0 = r95;
        r1 = r30;
        r82 = r0.getStructParentIndex(r1);
        r4 = com.itextpdf.text.pdf.PdfName.STRUCTPARENT;
        r6 = new com.itextpdf.text.pdf.PdfNumber;
        r0 = r82;
        r6.<init>(r0);
        r0 = r30;
        r0.put(r4, r6);
        r0 = r95;
        r4 = r0.writer;
        r4 = r4.getCurrentPage();
        r0 = r81;
        r1 = r30;
        r0.setAnnotation(r1, r4);
        r0 = r95;
        r4 = r0.writer;
        r4 = r4.getStructureTreeRoot();
        r6 = r81.getReference();
        r0 = r82;
        r4.setAnnotationMark(r0, r6);
    L_0x0473:
        r4 = "REMOTEGOTO";
        r0 = r38;
        r4 = r0.isAttribute(r4);
        if (r4 == 0) goto L_0x04cb;
    L_0x047d:
        r83 = r57;
        if (r64 == 0) goto L_0x048d;
    L_0x0481:
        r4 = "REMOTEGOTO";
        r0 = r64;
        r4 = r0.isAttribute(r4);
        if (r4 == 0) goto L_0x048d;
    L_0x048b:
        r83 = 0;
    L_0x048d:
        if (r64 != 0) goto L_0x0491;
    L_0x048f:
        r83 = r83 + r49;
    L_0x0491:
        r4 = "REMOTEGOTO";
        r0 = r38;
        r4 = r0.getAttribute(r4);
        r4 = (java.lang.Object[]) r4;
        r66 = r4;
        r66 = (java.lang.Object[]) r66;
        r4 = 0;
        r16 = r66[r4];
        r16 = (java.lang.String) r16;
        r4 = 1;
        r4 = r66[r4];
        r4 = r4 instanceof java.lang.String;
        if (r4 == 0) goto L_0x07e7;
    L_0x04ab:
        r4 = 1;
        r17 = r66[r4];
        r17 = (java.lang.String) r17;
        r4 = r9 + r43;
        r6 = r38.getTextRise();
        r19 = r4 + r6;
        r4 = r12 + r93;
        r20 = r4 - r83;
        r4 = r9 + r32;
        r6 = r38.getTextRise();
        r21 = r4 + r6;
        r15 = r95;
        r18 = r12;
        r15.remoteGoto(r16, r17, r18, r19, r20, r21);
    L_0x04cb:
        r4 = "LOCALGOTO";
        r0 = r38;
        r4 = r0.isAttribute(r4);
        if (r4 == 0) goto L_0x04ff;
    L_0x04d5:
        r83 = r57;
        if (r64 == 0) goto L_0x04e5;
    L_0x04d9:
        r4 = "LOCALGOTO";
        r0 = r64;
        r4 = r0.isAttribute(r4);
        if (r4 == 0) goto L_0x04e5;
    L_0x04e3:
        r83 = 0;
    L_0x04e5:
        if (r64 != 0) goto L_0x04e9;
    L_0x04e7:
        r83 = r83 + r49;
    L_0x04e9:
        r4 = "LOCALGOTO";
        r0 = r38;
        r7 = r0.getAttribute(r4);
        r7 = (java.lang.String) r7;
        r4 = r12 + r93;
        r10 = r4 - r83;
        r11 = r9 + r45;
        r6 = r95;
        r8 = r12;
        r6.localGoto(r7, r8, r9, r10, r11);
    L_0x04ff:
        r4 = "LOCALDESTINATION";
        r0 = r38;
        r4 = r0.isAttribute(r4);
        if (r4 == 0) goto L_0x0521;
    L_0x0509:
        r4 = "LOCALDESTINATION";
        r0 = r38;
        r4 = r0.getAttribute(r4);
        r4 = (java.lang.String) r4;
        r6 = new com.itextpdf.text.pdf.PdfDestination;
        r7 = 0;
        r8 = r9 + r45;
        r10 = 0;
        r6.<init>(r7, r12, r8, r10);
        r0 = r95;
        r0.localDestination(r4, r6);
    L_0x0521:
        r4 = "GENERICTAG";
        r0 = r38;
        r4 = r0.isAttribute(r4);
        if (r4 == 0) goto L_0x056d;
    L_0x052b:
        r83 = r57;
        if (r64 == 0) goto L_0x053b;
    L_0x052f:
        r4 = "GENERICTAG";
        r0 = r64;
        r4 = r0.isAttribute(r4);
        if (r4 == 0) goto L_0x053b;
    L_0x0539:
        r83 = 0;
    L_0x053b:
        if (r64 != 0) goto L_0x053f;
    L_0x053d:
        r83 = r83 + r49;
    L_0x053f:
        r71 = new com.itextpdf.text.Rectangle;
        r4 = r12 + r93;
        r4 = r4 - r83;
        r6 = r9 + r45;
        r0 = r71;
        r0.<init>(r12, r9, r4, r6);
        r0 = r95;
        r4 = r0.writer;
        r69 = r4.getPageEvent();
        if (r69 == 0) goto L_0x056d;
    L_0x0556:
        r0 = r95;
        r6 = r0.writer;
        r4 = "GENERICTAG";
        r0 = r38;
        r4 = r0.getAttribute(r4);
        r4 = (java.lang.String) r4;
        r0 = r69;
        r1 = r95;
        r2 = r71;
        r0.onGenericTag(r6, r1, r2, r4);
    L_0x056d:
        r4 = "PDFANNOTATION";
        r0 = r38;
        r4 = r0.isAttribute(r4);
        if (r4 == 0) goto L_0x05b5;
    L_0x0577:
        r83 = r57;
        if (r64 == 0) goto L_0x0587;
    L_0x057b:
        r4 = "PDFANNOTATION";
        r0 = r64;
        r4 = r0.isAttribute(r4);
        if (r4 == 0) goto L_0x0587;
    L_0x0585:
        r83 = 0;
    L_0x0587:
        if (r64 != 0) goto L_0x058b;
    L_0x0589:
        r83 = r83 + r49;
    L_0x058b:
        r4 = "PDFANNOTATION";
        r0 = r38;
        r4 = r0.getAttribute(r4);
        r4 = (com.itextpdf.text.pdf.PdfAnnotation) r4;
        r30 = com.itextpdf.text.pdf.PdfFormField.shallowDuplicate(r4);
        r4 = com.itextpdf.text.pdf.PdfName.RECT;
        r6 = new com.itextpdf.text.pdf.PdfRectangle;
        r7 = r9 + r43;
        r8 = r12 + r93;
        r8 = r8 - r83;
        r10 = r9 + r32;
        r6.<init>(r12, r7, r8, r10);
        r0 = r30;
        r0.put(r4, r6);
        r4 = 1;
        r0 = r97;
        r1 = r30;
        r0.addAnnotation(r1, r4);
    L_0x05b5:
        r4 = "SKEW";
        r0 = r38;
        r4 = r0.getAttribute(r4);
        r4 = (float[]) r4;
        r68 = r4;
        r68 = (float[]) r68;
        r4 = "HSCALE";
        r0 = r38;
        r50 = r0.getAttribute(r4);
        r50 = (java.lang.Float) r50;
        if (r68 != 0) goto L_0x05d1;
    L_0x05cf:
        if (r50 == 0) goto L_0x095f;
    L_0x05d1:
        r19 = 0;
        r20 = 0;
        if (r68 == 0) goto L_0x05dd;
    L_0x05d7:
        r4 = 0;
        r19 = r68[r4];
        r4 = 1;
        r20 = r68[r4];
    L_0x05dd:
        if (r50 == 0) goto L_0x095b;
    L_0x05df:
        r18 = r50.floatValue();
    L_0x05e3:
        r21 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r17 = r97;
        r22 = r12;
        r23 = r9;
        r17.setTextMatrix(r18, r19, r20, r21, r22, r23);
    L_0x05ee:
        if (r53 != 0) goto L_0x062a;
    L_0x05f0:
        r4 = "WORD_SPACING";
        r0 = r38;
        r4 = r0.isAttribute(r4);
        if (r4 == 0) goto L_0x060d;
    L_0x05fa:
        r4 = "WORD_SPACING";
        r0 = r38;
        r94 = r0.getAttribute(r4);
        r94 = (java.lang.Float) r94;
        r4 = r94.floatValue();
        r0 = r97;
        r0.setWordSpacing(r4);
    L_0x060d:
        r4 = "CHAR_SPACING";
        r0 = r38;
        r4 = r0.isAttribute(r4);
        if (r4 == 0) goto L_0x062a;
    L_0x0617:
        r4 = "CHAR_SPACING";
        r0 = r38;
        r41 = r0.getAttribute(r4);
        r41 = (java.lang.Float) r41;
        r4 = r41.floatValue();
        r0 = r97;
        r0.setCharacterSpacing(r4);
    L_0x062a:
        r4 = r38.isImage();
        if (r4 == 0) goto L_0x0683;
    L_0x0630:
        r22 = r38.getImage();
        r93 = r38.getImageWidth();
        r4 = r38.getImageScalePercentage();
        r0 = r22;
        r63 = r0.matrix(r4);
        r4 = 4;
        r6 = r38.getImageOffsetX();
        r6 = r6 + r12;
        r7 = 4;
        r7 = r63[r7];
        r6 = r6 - r7;
        r63[r4] = r6;
        r4 = 5;
        r6 = r38.getImageOffsetY();
        r6 = r6 + r9;
        r7 = 5;
        r7 = r63[r7];
        r6 = r6 - r7;
        r63[r4] = r6;
        r4 = 0;
        r23 = r63[r4];
        r4 = 1;
        r24 = r63[r4];
        r4 = 2;
        r25 = r63[r4];
        r4 = 3;
        r26 = r63[r4];
        r4 = 4;
        r27 = r63[r4];
        r4 = 5;
        r28 = r63[r4];
        r21 = r98;
        r21.addImage(r22, r23, r24, r25, r26, r27, r28);
        r4 = r12 + r57;
        r6 = r38.getImageWidth();
        r4 = r4 + r6;
        r6 = r97.getXTLM();
        r4 = r4 - r6;
        r6 = 0;
        r0 = r97;
        r0.moveText(r4, r6);
    L_0x0683:
        r12 = r12 + r93;
        r39 = r39 + 1;
    L_0x0687:
        r4 = r38.isImage();
        if (r4 != 0) goto L_0x06aa;
    L_0x068d:
        r4 = r38.font();
        r0 = r42;
        r4 = r4.compareTo(r0);
        if (r4 == 0) goto L_0x06aa;
    L_0x0699:
        r42 = r38.font();
        r4 = r42.getFont();
        r6 = r42.size();
        r0 = r97;
        r0.setFontAndSize(r4, r6);
    L_0x06aa:
        r72 = 0;
        r4 = "TEXTRENDERMODE";
        r0 = r38;
        r4 = r0.getAttribute(r4);
        r4 = (java.lang.Object[]) r4;
        r88 = r4;
        r88 = (java.lang.Object[]) r88;
        r90 = 0;
        r80 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r79 = 0;
        r4 = "SUBSUPSCRIPT";
        r0 = r38;
        r46 = r0.getAttribute(r4);
        r46 = (java.lang.Float) r46;
        if (r88 == 0) goto L_0x0712;
    L_0x06cc:
        r4 = 0;
        r4 = r88[r4];
        r4 = (java.lang.Integer) r4;
        r4 = r4.intValue();
        r90 = r4 & 3;
        if (r90 == 0) goto L_0x06e0;
    L_0x06d9:
        r0 = r97;
        r1 = r90;
        r0.setTextRenderingMode(r1);
    L_0x06e0:
        r4 = 1;
        r0 = r90;
        if (r0 == r4) goto L_0x06ea;
    L_0x06e5:
        r4 = 2;
        r0 = r90;
        if (r0 != r4) goto L_0x0712;
    L_0x06ea:
        r4 = 1;
        r4 = r88[r4];
        r4 = (java.lang.Float) r4;
        r80 = r4.floatValue();
        r4 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r4 = (r80 > r4 ? 1 : (r80 == r4 ? 0 : -1));
        if (r4 == 0) goto L_0x0700;
    L_0x06f9:
        r0 = r97;
        r1 = r80;
        r0.setLineWidth(r1);
    L_0x0700:
        r4 = 2;
        r79 = r88[r4];
        r79 = (com.itextpdf.text.BaseColor) r79;
        if (r79 != 0) goto L_0x0709;
    L_0x0707:
        r79 = r40;
    L_0x0709:
        if (r79 == 0) goto L_0x0712;
    L_0x070b:
        r0 = r97;
        r1 = r79;
        r0.setColorStroke(r1);
    L_0x0712:
        if (r46 == 0) goto L_0x0718;
    L_0x0714:
        r72 = r46.floatValue();
    L_0x0718:
        if (r40 == 0) goto L_0x0721;
    L_0x071a:
        r0 = r97;
        r1 = r40;
        r0.setColorFill(r1);
    L_0x0721:
        r4 = 0;
        r4 = (r72 > r4 ? 1 : (r72 == r4 ? 0 : -1));
        if (r4 == 0) goto L_0x072d;
    L_0x0726:
        r0 = r97;
        r1 = r72;
        r0.setTextRise(r1);
    L_0x072d:
        r4 = r38.isImage();
        if (r4 == 0) goto L_0x080d;
    L_0x0733:
        r29 = 1;
    L_0x0735:
        r4 = 0;
        r4 = (r72 > r4 ? 1 : (r72 == r4 ? 0 : -1));
        if (r4 == 0) goto L_0x0740;
    L_0x073a:
        r4 = 0;
        r0 = r97;
        r0.setTextRise(r4);
    L_0x0740:
        if (r40 == 0) goto L_0x0745;
    L_0x0742:
        r97.resetRGBColorFill();
    L_0x0745:
        if (r90 == 0) goto L_0x074d;
    L_0x0747:
        r4 = 0;
        r0 = r97;
        r0.setTextRenderingMode(r4);
    L_0x074d:
        if (r79 == 0) goto L_0x0752;
    L_0x074f:
        r97.resetRGBColorStroke();
    L_0x0752:
        r4 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r4 = (r80 > r4 ? 1 : (r80 == r4 ? 0 : -1));
        if (r4 == 0) goto L_0x075f;
    L_0x0758:
        r4 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r0 = r97;
        r0.setLineWidth(r4);
    L_0x075f:
        r4 = "SKEW";
        r0 = r38;
        r4 = r0.isAttribute(r4);
        if (r4 != 0) goto L_0x0773;
    L_0x0769:
        r4 = "HSCALE";
        r0 = r38;
        r4 = r0.isAttribute(r4);
        if (r4 == 0) goto L_0x077a;
    L_0x0773:
        r29 = 1;
        r0 = r97;
        r0.setTextMatrix(r12, r9);
    L_0x077a:
        if (r53 != 0) goto L_0x079e;
    L_0x077c:
        r4 = "CHAR_SPACING";
        r0 = r38;
        r4 = r0.isAttribute(r4);
        if (r4 == 0) goto L_0x078d;
    L_0x0786:
        r0 = r97;
        r1 = r33;
        r0.setCharacterSpacing(r1);
    L_0x078d:
        r4 = "WORD_SPACING";
        r0 = r38;
        r4 = r0.isAttribute(r4);
        if (r4 == 0) goto L_0x079e;
    L_0x0797:
        r0 = r97;
        r1 = r35;
        r0.setWordSpacing(r1);
    L_0x079e:
        r0 = r95;
        r4 = r0.writer;
        r4 = isTagged(r4);
        if (r4 == 0) goto L_0x07b7;
    L_0x07a8:
        r0 = r38;
        r4 = r0.accessibleElement;
        if (r4 == 0) goto L_0x07b7;
    L_0x07ae:
        r0 = r38;
        r4 = r0.accessibleElement;
        r0 = r97;
        r0.closeMCBlock(r4);
    L_0x07b7:
        r85 = r14;
        goto L_0x0062;
    L_0x07bb:
        r0 = r95;
        r15 = r0.writer;
        r4 = r9 + r43;
        r6 = r38.getTextRise();
        r17 = r4 + r6;
        r4 = r12 + r93;
        r18 = r4 - r83;
        r4 = r9 + r32;
        r6 = r38.getTextRise();
        r19 = r4 + r6;
        r4 = "ACTION";
        r0 = r38;
        r20 = r0.getAttribute(r4);
        r20 = (com.itextpdf.text.pdf.PdfAction) r20;
        r21 = 0;
        r16 = r12;
        r30 = r15.createAnnotation(r16, r17, r18, r19, r20, r21);
        goto L_0x0411;
    L_0x07e7:
        r4 = 1;
        r4 = r66[r4];
        r4 = (java.lang.Integer) r4;
        r17 = r4.intValue();
        r4 = r9 + r43;
        r6 = r38.getTextRise();
        r19 = r4 + r6;
        r4 = r12 + r93;
        r20 = r4 - r83;
        r4 = r9 + r32;
        r6 = r38.getTextRise();
        r21 = r4 + r6;
        r15 = r95;
        r18 = r12;
        r15.remoteGoto(r16, r17, r18, r19, r20, r21);
        goto L_0x04cb;
    L_0x080d:
        r4 = r38.isHorizontalSeparator();
        if (r4 == 0) goto L_0x0837;
    L_0x0813:
        r31 = new com.itextpdf.text.pdf.PdfTextArray;
        r31.<init>();
        r0 = r47;
        r4 = -r0;
        r6 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r4 = r4 * r6;
        r0 = r38;
        r6 = r0.font;
        r6 = r6.size();
        r4 = r4 / r6;
        r4 = r4 / r18;
        r0 = r31;
        r0.add(r4);
        r0 = r97;
        r1 = r31;
        r0.showText(r1);
        goto L_0x0735;
    L_0x0837:
        r4 = r38.isTab();
        if (r4 == 0) goto L_0x0864;
    L_0x083d:
        r4 = (r14 > r12 ? 1 : (r14 == r12 ? 0 : -1));
        if (r4 == 0) goto L_0x0864;
    L_0x0841:
        r31 = new com.itextpdf.text.pdf.PdfTextArray;
        r31.<init>();
        r4 = r14 - r12;
        r6 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r4 = r4 * r6;
        r0 = r38;
        r6 = r0.font;
        r6 = r6.size();
        r4 = r4 / r6;
        r4 = r4 / r18;
        r0 = r31;
        r0.add(r4);
        r0 = r97;
        r1 = r31;
        r0.showText(r1);
        goto L_0x0735;
    L_0x0864:
        if (r53 == 0) goto L_0x0903;
    L_0x0866:
        if (r65 <= 0) goto L_0x0903;
    L_0x0868:
        r4 = r38.isSpecialEncoding();
        if (r4 == 0) goto L_0x0903;
    L_0x086e:
        r4 = (r18 > r59 ? 1 : (r18 == r59 ? 0 : -1));
        if (r4 == 0) goto L_0x0887;
    L_0x0872:
        r59 = r18;
        r4 = r35 / r18;
        r0 = r97;
        r0.setWordSpacing(r4);
        r4 = r33 / r18;
        r6 = r97.getCharacterSpacing();
        r4 = r4 + r6;
        r0 = r97;
        r0.setCharacterSpacing(r4);
    L_0x0887:
        r73 = r38.toString();
        r4 = 32;
        r0 = r73;
        r51 = r0.indexOf(r4);
        if (r51 >= 0) goto L_0x089e;
    L_0x0895:
        r0 = r97;
        r1 = r73;
        r0.showText(r1);
        goto L_0x0735;
    L_0x089e:
        r0 = r35;
        r4 = -r0;
        r6 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r4 = r4 * r6;
        r0 = r38;
        r6 = r0.font;
        r6 = r6.size();
        r4 = r4 / r6;
        r78 = r4 / r18;
        r87 = new com.itextpdf.text.pdf.PdfTextArray;
        r4 = 0;
        r0 = r73;
        r1 = r51;
        r4 = r0.substring(r4, r1);
        r0 = r87;
        r0.<init>(r4);
        r60 = r51;
    L_0x08c1:
        r4 = 32;
        r6 = r60 + 1;
        r0 = r73;
        r51 = r0.indexOf(r4, r6);
        if (r51 < 0) goto L_0x08e6;
    L_0x08cd:
        r0 = r87;
        r1 = r78;
        r0.add(r1);
        r0 = r73;
        r1 = r60;
        r2 = r51;
        r4 = r0.substring(r1, r2);
        r0 = r87;
        r0.add(r4);
        r60 = r51;
        goto L_0x08c1;
    L_0x08e6:
        r0 = r87;
        r1 = r78;
        r0.add(r1);
        r0 = r73;
        r1 = r60;
        r4 = r0.substring(r1);
        r0 = r87;
        r0.add(r4);
        r0 = r97;
        r1 = r87;
        r0.showText(r1);
        goto L_0x0735;
    L_0x0903:
        if (r53 == 0) goto L_0x091e;
    L_0x0905:
        r4 = (r18 > r59 ? 1 : (r18 == r59 ? 0 : -1));
        if (r4 == 0) goto L_0x091e;
    L_0x0909:
        r59 = r18;
        r4 = r35 / r18;
        r0 = r97;
        r0.setWordSpacing(r4);
        r4 = r33 / r18;
        r6 = r97.getCharacterSpacing();
        r4 = r4 + r6;
        r0 = r97;
        r0.setCharacterSpacing(r4);
    L_0x091e:
        r4 = r38.toString();
        r0 = r97;
        r0.showText(r4);
        goto L_0x0735;
    L_0x0929:
        if (r53 == 0) goto L_0x093f;
    L_0x092b:
        r4 = 0;
        r0 = r97;
        r0.setWordSpacing(r4);
        r4 = 0;
        r0 = r97;
        r0.setCharacterSpacing(r4);
        r4 = r96.isNewlineSplit();
        if (r4 == 0) goto L_0x093f;
    L_0x093d:
        r57 = 0;
    L_0x093f:
        if (r29 == 0) goto L_0x094d;
    L_0x0941:
        r4 = r97.getXTLM();
        r4 = r5 - r4;
        r6 = 0;
        r0 = r97;
        r0.moveText(r4, r6);
    L_0x094d:
        r4 = 0;
        r99[r4] = r42;
        r4 = 1;
        r6 = new java.lang.Float;
        r0 = r57;
        r6.<init>(r0);
        r99[r4] = r6;
        return r61;
    L_0x095b:
        r18 = r48;
        goto L_0x05e3;
    L_0x095f:
        r18 = r48;
        goto L_0x05ee;
    L_0x0963:
        r14 = r85;
        goto L_0x012c;
    L_0x0967:
        r14 = r85;
        r18 = r48;
        goto L_0x0683;
    L_0x096d:
        r14 = r85;
        r18 = r48;
        goto L_0x0687;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.itextpdf.text.pdf.PdfDocument.writeLineToContent(com.itextpdf.text.pdf.PdfLine, com.itextpdf.text.pdf.PdfContentByte, com.itextpdf.text.pdf.PdfContentByte, java.lang.Object[], float):float");
    }

    protected float indentLeft() {
        return left(((this.indentation.indentLeft + this.indentation.listIndentLeft) + this.indentation.imageIndentLeft) + this.indentation.sectionIndentLeft);
    }

    protected float indentRight() {
        return right((this.indentation.indentRight + this.indentation.sectionIndentRight) + this.indentation.imageIndentRight);
    }

    protected float indentTop() {
        return top(this.indentation.indentTop);
    }

    float indentBottom() {
        return bottom(this.indentation.indentBottom);
    }

    protected void addSpacing(float extraspace, float oldleading, Font f) {
        addSpacing(extraspace, oldleading, f, false);
    }

    protected void addSpacing(float extraspace, float oldleading, Font f, boolean spacingAfter) {
        if (extraspace != 0.0f && !this.pageEmpty) {
            if (!spacingAfter || this.pageEmpty || this.lines.size() != 0 || this.line.size() != 0) {
                if (this.currentHeight + (spacingAfter ? extraspace : calculateLineHeight()) > indentTop() - indentBottom()) {
                    newPage();
                    return;
                }
                this.leading = extraspace;
                carriageReturn();
                if (f.isUnderlined() || f.isStrikethru()) {
                    Font f2 = new Font(f);
                    f2.setStyle((f2.getStyle() & -5) & -9);
                    f = f2;
                }
                Chunk space = new Chunk(" ", f);
                if (spacingAfter && this.pageEmpty) {
                    space = new Chunk("", f);
                }
                space.process(this);
                carriageReturn();
                this.leading = oldleading;
            }
        }
    }

    PdfInfo getInfo() {
        return this.info;
    }

    PdfCatalog getCatalog(PdfIndirectReference pages) {
        PdfCatalog catalog = new PdfCatalog(pages, this.writer);
        if (this.rootOutline.getKids().size() > 0) {
            catalog.put(PdfName.PAGEMODE, PdfName.USEOUTLINES);
            catalog.put(PdfName.OUTLINES, this.rootOutline.indirectReference());
        }
        this.writer.getPdfVersion().addToCatalog(catalog);
        this.viewerPreferences.addToCatalog(catalog);
        if (this.pageLabels != null) {
            catalog.put(PdfName.PAGELABELS, this.pageLabels.getDictionary(this.writer));
        }
        catalog.addNames(this.localDestinations, getDocumentLevelJS(), this.documentFileAttachment, this.writer);
        if (this.openActionName != null) {
            catalog.setOpenAction(getLocalGotoAction(this.openActionName));
        } else if (this.openActionAction != null) {
            catalog.setOpenAction(this.openActionAction);
        }
        if (this.additionalActions != null) {
            catalog.setAdditionalActions(this.additionalActions);
        }
        if (this.collection != null) {
            catalog.put(PdfName.COLLECTION, this.collection);
        }
        if (this.annotationsImp.hasValidAcroForm()) {
            try {
                catalog.put(PdfName.ACROFORM, this.writer.addToBody(this.annotationsImp.getAcroForm()).getIndirectReference());
            } catch (IOException e) {
                throw new ExceptionConverter(e);
            }
        }
        if (this.language != null) {
            catalog.put(PdfName.LANG, this.language);
        }
        return catalog;
    }

    void addOutline(PdfOutline outline, String name) {
        localDestination(name, outline.getPdfDestination());
    }

    public PdfOutline getRootOutline() {
        return this.rootOutline;
    }

    void calculateOutlineCount() {
        if (this.rootOutline.getKids().size() != 0) {
            traverseOutlineCount(this.rootOutline);
        }
    }

    void traverseOutlineCount(PdfOutline outline) {
        ArrayList<PdfOutline> kids = outline.getKids();
        PdfOutline parent = outline.parent();
        if (!kids.isEmpty()) {
            for (int k = 0; k < kids.size(); k++) {
                traverseOutlineCount((PdfOutline) kids.get(k));
            }
            if (parent == null) {
                return;
            }
            if (outline.isOpen()) {
                parent.setCount((outline.getCount() + parent.getCount()) + 1);
                return;
            }
            parent.setCount(parent.getCount() + 1);
            outline.setCount(-outline.getCount());
        } else if (parent != null) {
            parent.setCount(parent.getCount() + 1);
        }
    }

    void writeOutlines() throws IOException {
        if (this.rootOutline.getKids().size() != 0) {
            outlineTree(this.rootOutline);
            this.writer.addToBody(this.rootOutline, this.rootOutline.indirectReference());
        }
    }

    void outlineTree(PdfOutline outline) throws IOException {
        int k;
        outline.setIndirectReference(this.writer.getPdfIndirectReference());
        if (outline.parent() != null) {
            outline.put(PdfName.PARENT, outline.parent().indirectReference());
        }
        ArrayList<PdfOutline> kids = outline.getKids();
        int size = kids.size();
        for (k = 0; k < size; k++) {
            outlineTree((PdfOutline) kids.get(k));
        }
        for (k = 0; k < size; k++) {
            if (k > 0) {
                ((PdfOutline) kids.get(k)).put(PdfName.PREV, ((PdfOutline) kids.get(k - 1)).indirectReference());
            }
            if (k < size - 1) {
                ((PdfOutline) kids.get(k)).put(PdfName.NEXT, ((PdfOutline) kids.get(k + 1)).indirectReference());
            }
        }
        if (size > 0) {
            outline.put(PdfName.FIRST, ((PdfOutline) kids.get(0)).indirectReference());
            outline.put(PdfName.LAST, ((PdfOutline) kids.get(size - 1)).indirectReference());
        }
        for (k = 0; k < size; k++) {
            PdfOutline kid = (PdfOutline) kids.get(k);
            this.writer.addToBody(kid, kid.indirectReference());
        }
    }

    void setViewerPreferences(int preferences) {
        this.viewerPreferences.setViewerPreferences(preferences);
    }

    void addViewerPreference(PdfName key, PdfObject value) {
        this.viewerPreferences.addViewerPreference(key, value);
    }

    void setPageLabels(PdfPageLabels pageLabels) {
        this.pageLabels = pageLabels;
    }

    public PdfPageLabels getPageLabels() {
        return this.pageLabels;
    }

    void localGoto(String name, float llx, float lly, float urx, float ury) {
        this.annotationsImp.addPlainAnnotation(this.writer.createAnnotation(llx, lly, urx, ury, getLocalGotoAction(name), null));
    }

    void remoteGoto(String filename, String name, float llx, float lly, float urx, float ury) {
        this.annotationsImp.addPlainAnnotation(this.writer.createAnnotation(llx, lly, urx, ury, new PdfAction(filename, name), null));
    }

    void remoteGoto(String filename, int page, float llx, float lly, float urx, float ury) {
        addAnnotation(this.writer.createAnnotation(llx, lly, urx, ury, new PdfAction(filename, page), null));
    }

    void setAction(PdfAction action, float llx, float lly, float urx, float ury) {
        addAnnotation(this.writer.createAnnotation(llx, lly, urx, ury, action, null));
    }

    PdfAction getLocalGotoAction(String name) {
        Destination dest = (Destination) this.localDestinations.get(name);
        if (dest == null) {
            dest = new Destination();
        }
        if (dest.action != null) {
            return dest.action;
        }
        if (dest.reference == null) {
            dest.reference = this.writer.getPdfIndirectReference();
        }
        PdfAction action = new PdfAction(dest.reference);
        dest.action = action;
        this.localDestinations.put(name, dest);
        return action;
    }

    boolean localDestination(String name, PdfDestination destination) {
        Destination dest = (Destination) this.localDestinations.get(name);
        if (dest == null) {
            dest = new Destination();
        }
        if (dest.destination != null) {
            return false;
        }
        dest.destination = destination;
        this.localDestinations.put(name, dest);
        if (!destination.hasPage()) {
            destination.addPage(this.writer.getCurrentPage());
        }
        return true;
    }

    void addJavaScript(PdfAction js) {
        if (js.get(PdfName.JS) == null) {
            throw new RuntimeException(MessageLocalization.getComposedMessage("only.javascript.actions.are.allowed", new Object[0]));
        }
        try {
            HashMap hashMap = this.documentLevelJS;
            DecimalFormat decimalFormat = SIXTEEN_DIGITS;
            int i = this.jsCounter;
            this.jsCounter = i + 1;
            hashMap.put(decimalFormat.format((long) i), this.writer.addToBody(js).getIndirectReference());
        } catch (IOException e) {
            throw new ExceptionConverter(e);
        }
    }

    void addJavaScript(String name, PdfAction js) {
        if (js.get(PdfName.JS) == null) {
            throw new RuntimeException(MessageLocalization.getComposedMessage("only.javascript.actions.are.allowed", new Object[0]));
        }
        try {
            this.documentLevelJS.put(name, this.writer.addToBody(js).getIndirectReference());
        } catch (IOException e) {
            throw new ExceptionConverter(e);
        }
    }

    HashMap<String, PdfObject> getDocumentLevelJS() {
        return this.documentLevelJS;
    }

    void addFileAttachment(String description, PdfFileSpecification fs) throws IOException {
        if (description == null) {
            PdfString desc = (PdfString) fs.get(PdfName.DESC);
            if (desc == null) {
                description = "";
            } else {
                description = PdfEncodings.convertToString(desc.getBytes(), null);
            }
        }
        fs.addDescription(description, true);
        if (description.length() == 0) {
            description = "Unnamed";
        }
        String fn = PdfEncodings.convertToString(new PdfString(description, PdfObject.TEXT_UNICODE).getBytes(), null);
        int k = 0;
        while (this.documentFileAttachment.containsKey(fn)) {
            k++;
            fn = PdfEncodings.convertToString(new PdfString(description + " " + k, PdfObject.TEXT_UNICODE).getBytes(), null);
        }
        this.documentFileAttachment.put(fn, fs.getReference());
    }

    HashMap<String, PdfObject> getDocumentFileAttachment() {
        return this.documentFileAttachment;
    }

    void setOpenAction(String name) {
        this.openActionName = name;
        this.openActionAction = null;
    }

    void setOpenAction(PdfAction action) {
        this.openActionAction = action;
        this.openActionName = null;
    }

    void addAdditionalAction(PdfName actionType, PdfAction action) {
        if (this.additionalActions == null) {
            this.additionalActions = new PdfDictionary();
        }
        if (action == null) {
            this.additionalActions.remove(actionType);
        } else {
            this.additionalActions.put(actionType, action);
        }
        if (this.additionalActions.size() == 0) {
            this.additionalActions = null;
        }
    }

    public void setCollection(PdfCollection collection) {
        this.collection = collection;
    }

    PdfAcroForm getAcroForm() {
        return this.annotationsImp.getAcroForm();
    }

    void setSigFlags(int f) {
        this.annotationsImp.setSigFlags(f);
    }

    void addCalculationOrder(PdfFormField formField) {
        this.annotationsImp.addCalculationOrder(formField);
    }

    void addAnnotation(PdfAnnotation annot) {
        this.pageEmpty = false;
        this.annotationsImp.addAnnotation(annot);
    }

    void setLanguage(String language) {
        this.language = new PdfString(language);
    }

    void setCropBoxSize(Rectangle crop) {
        setBoxSize("crop", crop);
    }

    void setBoxSize(String boxName, Rectangle size) {
        if (size == null) {
            this.boxSize.remove(boxName);
        } else {
            this.boxSize.put(boxName, new PdfRectangle(size));
        }
    }

    protected void setNewPageSizeAndMargins() {
        this.pageSize = this.nextPageSize;
        if (this.marginMirroring && (getPageNumber() & 1) == 0) {
            this.marginRight = this.nextMarginLeft;
            this.marginLeft = this.nextMarginRight;
        } else {
            this.marginLeft = this.nextMarginLeft;
            this.marginRight = this.nextMarginRight;
        }
        if (this.marginMirroringTopBottom && (getPageNumber() & 1) == 0) {
            this.marginTop = this.nextMarginBottom;
            this.marginBottom = this.nextMarginTop;
        } else {
            this.marginTop = this.nextMarginTop;
            this.marginBottom = this.nextMarginBottom;
        }
        if (isTagged(this.writer)) {
            this.text = this.graphics;
        } else {
            this.text = new PdfContentByte(this.writer);
            this.text.reset();
        }
        this.text.beginText();
        this.text.moveText(left(), top());
        if (isTagged(this.writer)) {
            this.textEmptySize = this.text.size();
        }
    }

    Rectangle getBoxSize(String boxName) {
        PdfRectangle r = (PdfRectangle) this.thisBoxSize.get(boxName);
        if (r != null) {
            return r.getRectangle();
        }
        return null;
    }

    void setPageEmpty(boolean pageEmpty) {
        this.pageEmpty = pageEmpty;
    }

    boolean isPageEmpty() {
        if (isTagged(this.writer)) {
            if (this.writer != null) {
                if (this.writer.getDirectContent().size(false) != 0 || this.writer.getDirectContentUnder().size(false) != 0 || this.text.size(false) - this.textEmptySize != 0) {
                    return false;
                }
                if (!(this.pageEmpty || this.writer.isPaused())) {
                    return false;
                }
            }
            return true;
        }
        if (this.writer != null) {
            if (this.writer.getDirectContent().size() != 0 || this.writer.getDirectContentUnder().size() != 0) {
                return false;
            }
            if (!(this.pageEmpty || this.writer.isPaused())) {
                return false;
            }
        }
        return true;
    }

    void setDuration(int seconds) {
        if (seconds > 0) {
            this.writer.addPageDictEntry(PdfName.DUR, new PdfNumber(seconds));
        }
    }

    void setTransition(PdfTransition transition) {
        this.writer.addPageDictEntry(PdfName.TRANS, transition.getTransitionDictionary());
    }

    void setPageAction(PdfName actionType, PdfAction action) {
        if (this.pageAA == null) {
            this.pageAA = new PdfDictionary();
        }
        this.pageAA.put(actionType, action);
    }

    void setThumbnail(Image image) throws PdfException, DocumentException {
        this.writer.addPageDictEntry(PdfName.THUMB, this.writer.getImageReference(this.writer.addDirectImageSimple(image)));
    }

    PageResources getPageResources() {
        return this.pageResources;
    }

    boolean isStrictImageSequence() {
        return this.strictImageSequence;
    }

    void setStrictImageSequence(boolean strictImageSequence) {
        this.strictImageSequence = strictImageSequence;
    }

    public void clearTextWrap() {
        float tmpHeight = this.imageEnd - this.currentHeight;
        if (this.line != null) {
            tmpHeight += this.line.height();
        }
        if (this.imageEnd > -1.0f && tmpHeight > 0.0f) {
            carriageReturn();
            this.currentHeight += tmpHeight;
        }
    }

    public int getStructParentIndex(Object obj) {
        int[] i = (int[]) this.structParentIndices.get(obj);
        if (i == null) {
            i = new int[]{this.structParentIndices.size(), 0};
            this.structParentIndices.put(obj, i);
        }
        return i[0];
    }

    public int getNextMarkPoint(Object obj) {
        int[] i = (int[]) this.structParentIndices.get(obj);
        if (i == null) {
            i = new int[]{this.structParentIndices.size(), 0};
            this.structParentIndices.put(obj, i);
        }
        int markPoint = i[1];
        i[1] = i[1] + 1;
        return markPoint;
    }

    public int[] getStructParentIndexAndNextMarkPoint(Object obj) {
        int[] i = (int[]) this.structParentIndices.get(obj);
        if (i == null) {
            i = new int[]{this.structParentIndices.size(), 0};
            this.structParentIndices.put(obj, i);
        }
        int markPoint = i[1];
        i[1] = i[1] + 1;
        return new int[]{i[0], markPoint};
    }

    protected void add(Image image) throws PdfException, DocumentException {
        if (image.hasAbsoluteY()) {
            this.graphics.addImage(image);
            this.pageEmpty = false;
            return;
        }
        boolean textwrap;
        if (this.currentHeight != 0.0f && (indentTop() - this.currentHeight) - image.getScaledHeight() < indentBottom()) {
            if (this.strictImageSequence || this.imageWait != null) {
                newPage();
                if (this.currentHeight != 0.0f && (indentTop() - this.currentHeight) - image.getScaledHeight() < indentBottom()) {
                    this.imageWait = image;
                    return;
                }
            }
            this.imageWait = image;
            return;
        }
        this.pageEmpty = false;
        if (image == this.imageWait) {
            this.imageWait = null;
        }
        if ((image.getAlignment() & 4) != 4 || (image.getAlignment() & 1) == 1) {
            textwrap = false;
        } else {
            textwrap = true;
        }
        boolean underlying;
        if ((image.getAlignment() & 8) == 8) {
            underlying = true;
        } else {
            underlying = false;
        }
        float diff = this.leading / BaseField.BORDER_WIDTH_MEDIUM;
        if (textwrap) {
            diff += this.leading;
        }
        float lowerleft = ((indentTop() - this.currentHeight) - image.getScaledHeight()) - diff;
        float[] mt = image.matrix();
        float startPosition = indentLeft() - mt[4];
        if ((image.getAlignment() & 2) == 2) {
            startPosition = (indentRight() - image.getScaledWidth()) - mt[4];
        }
        if ((image.getAlignment() & 1) == 1) {
            startPosition = (indentLeft() + (((indentRight() - indentLeft()) - image.getScaledWidth()) / BaseField.BORDER_WIDTH_MEDIUM)) - mt[4];
        }
        if (image.hasAbsoluteX()) {
            startPosition = image.getAbsoluteX();
        }
        if (textwrap) {
            if (this.imageEnd < 0.0f || this.imageEnd < (this.currentHeight + image.getScaledHeight()) + diff) {
                this.imageEnd = (this.currentHeight + image.getScaledHeight()) + diff;
            }
            Indentation indentation;
            if ((image.getAlignment() & 2) == 2) {
                indentation = this.indentation;
                indentation.imageIndentRight += image.getScaledWidth() + image.getIndentationLeft();
            } else {
                indentation = this.indentation;
                indentation.imageIndentLeft += image.getScaledWidth() + image.getIndentationRight();
            }
        } else if ((image.getAlignment() & 2) == 2) {
            startPosition -= image.getIndentationRight();
        } else if ((image.getAlignment() & 1) == 1) {
            startPosition += image.getIndentationLeft() - image.getIndentationRight();
        } else {
            startPosition += image.getIndentationLeft();
        }
        this.graphics.addImage(image, mt[0], mt[1], mt[2], mt[3], startPosition, lowerleft - mt[5]);
        if (!textwrap && !underlying) {
            this.currentHeight += image.getScaledHeight() + diff;
            flushLines();
            this.text.moveText(0.0f, -(image.getScaledHeight() + diff));
            newLine();
        }
    }

    void addPTable(PdfPTable ptable) throws DocumentException {
        PdfContentByte pdfContentByte;
        if (isTagged(this.writer)) {
            pdfContentByte = this.text;
        } else {
            pdfContentByte = this.writer.getDirectContent();
        }
        ColumnText ct = new ColumnText(pdfContentByte);
        ct.setRunDirection(ptable.getRunDirection());
        if (ptable.getKeepTogether() && !fitsPage(ptable, 0.0f) && this.currentHeight > 0.0f) {
            newPage();
        }
        if (this.currentHeight == 0.0f) {
            ct.setAdjustFirstLine(false);
        }
        ct.addElement(ptable);
        boolean he = ptable.isHeadersInEvent();
        ptable.setHeadersInEvent(true);
        int loop = 0;
        while (true) {
            ct.setSimpleColumn(indentLeft(), indentBottom(), indentRight(), indentTop() - this.currentHeight);
            if ((ct.go() & 1) != 0) {
                break;
            }
            if (indentTop() - this.currentHeight == ct.getYLine()) {
                loop++;
            } else {
                loop = 0;
            }
            if (loop == 3) {
                throw new DocumentException(MessageLocalization.getComposedMessage("infinite.table.loop", new Object[0]));
            }
            newPage();
            if (isTagged(this.writer)) {
                ct.setCanvas(this.text);
            }
        }
        if (isTagged(this.writer)) {
            this.text.setTextMatrix(indentLeft(), ct.getYLine());
        } else {
            this.text.moveText(0.0f, (ct.getYLine() - indentTop()) + this.currentHeight);
        }
        this.currentHeight = indentTop() - ct.getYLine();
        ptable.setHeadersInEvent(he);
    }

    private void addDiv(PdfDiv div) throws DocumentException {
        if (this.floatingElements == null) {
            this.floatingElements = new ArrayList();
        }
        this.floatingElements.add(div);
    }

    private void flushFloatingElements() throws DocumentException {
        if (this.floatingElements != null && !this.floatingElements.isEmpty()) {
            ArrayList<Element> cachedFloatingElements = this.floatingElements;
            this.floatingElements = null;
            FloatLayout fl = new FloatLayout(cachedFloatingElements, false);
            int loop = 0;
            while (true) {
                float left = indentLeft();
                fl.setSimpleColumn(indentLeft(), indentBottom(), indentRight(), indentTop() - this.currentHeight);
                try {
                    if ((fl.layout(isTagged(this.writer) ? this.text : this.writer.getDirectContent(), false) & 1) != 0) {
                        break;
                    }
                    if (indentTop() - this.currentHeight == fl.getYLine() || isPageEmpty()) {
                        loop++;
                    } else {
                        loop = 0;
                    }
                    if (loop != 2) {
                        newPage();
                    } else {
                        return;
                    }
                } catch (Exception e) {
                    return;
                }
            }
            if (isTagged(this.writer)) {
                this.text.setTextMatrix(indentLeft(), fl.getYLine());
            } else {
                this.text.moveText(0.0f, (fl.getYLine() - indentTop()) + this.currentHeight);
            }
            this.currentHeight = indentTop() - fl.getYLine();
        }
    }

    boolean fitsPage(PdfPTable table, float margin) {
        float spacingBefore;
        if (!table.isLockedWidth()) {
            table.setTotalWidth(((indentRight() - indentLeft()) * table.getWidthPercentage()) / 100.0f);
        }
        ensureNewLine();
        float floatValue = Float.valueOf(table.isSkipFirstHeader() ? table.getTotalHeight() - table.getHeaderHeight() : table.getTotalHeight()).floatValue();
        if (this.currentHeight > 0.0f) {
            spacingBefore = table.spacingBefore();
        } else {
            spacingBefore = 0.0f;
        }
        return spacingBefore + floatValue <= ((indentTop() - this.currentHeight) - indentBottom()) - margin;
    }

    private static boolean isTagged(PdfWriter writer) {
        return writer != null && writer.isTagged();
    }

    private PdfLine getLastLine() {
        if (this.lines.size() > 0) {
            return (PdfLine) this.lines.get(this.lines.size() - 1);
        }
        return null;
    }
}

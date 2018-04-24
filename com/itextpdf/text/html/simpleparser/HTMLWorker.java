package com.itextpdf.text.html.simpleparser;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocListener;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.FontProvider;
import com.itextpdf.text.Image;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.TextElementArray;
import com.itextpdf.text.html.HtmlTags;
import com.itextpdf.text.html.HtmlUtilities;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.pdf.BaseField;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.xml.simpleparser.SimpleXMLDocHandler;
import com.itextpdf.text.xml.simpleparser.SimpleXMLParser;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

@Deprecated
public class HTMLWorker implements SimpleXMLDocHandler, DocListener {
    public static final String FONT_PROVIDER = "font_factory";
    public static final String IMG_BASEURL = "img_baseurl";
    public static final String IMG_PROCESSOR = "img_interface";
    public static final String IMG_PROVIDER = "img_provider";
    public static final String IMG_STORE = "img_static";
    public static final String LINK_PROVIDER = "alink_interface";
    private static Logger LOGGER = LoggerFactory.getLogger(HTMLWorker.class);
    private final ChainedProperties chain;
    protected Paragraph currentParagraph;
    protected DocListener document;
    private final ElementFactory factory;
    private boolean insidePRE;
    protected List<Element> objectList;
    private boolean pendingLI;
    private boolean pendingTD;
    private boolean pendingTR;
    private Map<String, Object> providers;
    protected boolean skipText;
    protected Stack<Element> stack;
    private StyleSheet style;
    private final Stack<boolean[]> tableState;
    protected Map<String, HTMLTagProcessor> tags;

    public HTMLWorker(DocListener document) {
        this(document, null, null);
    }

    public HTMLWorker(DocListener document, Map<String, HTMLTagProcessor> tags, StyleSheet style) {
        this.style = new StyleSheet();
        this.stack = new Stack();
        this.chain = new ChainedProperties();
        this.providers = new HashMap();
        this.factory = new ElementFactory();
        this.tableState = new Stack();
        this.pendingTR = false;
        this.pendingTD = false;
        this.pendingLI = false;
        this.insidePRE = false;
        this.skipText = false;
        this.document = document;
        setSupportedTags(tags);
        setStyleSheet(style);
    }

    public void setSupportedTags(Map<String, HTMLTagProcessor> tags) {
        if (tags == null) {
            tags = new HTMLTagProcessors();
        }
        this.tags = tags;
    }

    public void setStyleSheet(StyleSheet style) {
        if (style == null) {
            style = new StyleSheet();
        }
        this.style = style;
    }

    public void parse(Reader reader) throws IOException {
        LOGGER.info("Please note, there is a more extended version of the HTMLWorker available in the iText XMLWorker");
        SimpleXMLParser.parse(this, null, reader, true);
    }

    public void startDocument() {
        HashMap<String, String> attrs = new HashMap();
        this.style.applyStyle(HtmlTags.BODY, attrs);
        this.chain.addToChain(HtmlTags.BODY, attrs);
    }

    public void startElement(String tag, Map<String, String> attrs) {
        HTMLTagProcessor htmlTag = (HTMLTagProcessor) this.tags.get(tag);
        if (htmlTag != null) {
            this.style.applyStyle(tag, attrs);
            StyleSheet.resolveStyleAttribute(attrs, this.chain);
            try {
                htmlTag.startElement(this, tag, attrs);
            } catch (DocumentException e) {
                throw new ExceptionConverter(e);
            } catch (IOException e2) {
                throw new ExceptionConverter(e2);
            }
        }
    }

    public void text(String content) {
        if (!this.skipText) {
            if (this.currentParagraph == null) {
                this.currentParagraph = createParagraph();
            }
            if (!this.insidePRE) {
                if (content.trim().length() != 0 || content.indexOf(32) >= 0) {
                    content = HtmlUtilities.eliminateWhiteSpace(content);
                } else {
                    return;
                }
            }
            this.currentParagraph.add(createChunk(content));
        }
    }

    public void endElement(String tag) {
        HTMLTagProcessor htmlTag = (HTMLTagProcessor) this.tags.get(tag);
        if (htmlTag != null) {
            try {
                htmlTag.endElement(this, tag);
            } catch (DocumentException e) {
                throw new ExceptionConverter(e);
            }
        }
    }

    public void endDocument() {
        int k = 0;
        while (k < this.stack.size()) {
            try {
                this.document.add((Element) this.stack.elementAt(k));
                k++;
            } catch (Exception e) {
                throw new ExceptionConverter(e);
            }
        }
        if (this.currentParagraph != null) {
            this.document.add(this.currentParagraph);
        }
        this.currentParagraph = null;
    }

    public void newLine() {
        if (this.currentParagraph == null) {
            this.currentParagraph = new Paragraph();
        }
        this.currentParagraph.add(createChunk("\n"));
    }

    public void carriageReturn() throws DocumentException {
        if (this.currentParagraph != null) {
            if (this.stack.empty()) {
                this.document.add(this.currentParagraph);
            } else {
                Element obj = (Element) this.stack.pop();
                if (obj instanceof TextElementArray) {
                    ((TextElementArray) obj).add(this.currentParagraph);
                }
                this.stack.push(obj);
            }
            this.currentParagraph = null;
        }
    }

    public void flushContent() {
        pushToStack(this.currentParagraph);
        this.currentParagraph = new Paragraph();
    }

    public void pushToStack(Element element) {
        if (element != null) {
            this.stack.push(element);
        }
    }

    public void updateChain(String tag, Map<String, String> attrs) {
        this.chain.addToChain(tag, attrs);
    }

    public void updateChain(String tag) {
        this.chain.removeChain(tag);
    }

    public void setProviders(Map<String, Object> providers) {
        if (providers != null) {
            this.providers = providers;
            FontProvider ff = null;
            if (providers != null) {
                ff = (FontProvider) providers.get(FONT_PROVIDER);
            }
            if (ff != null) {
                this.factory.setFontProvider(ff);
            }
        }
    }

    public Chunk createChunk(String content) {
        return this.factory.createChunk(content, this.chain);
    }

    public Paragraph createParagraph() {
        return this.factory.createParagraph(this.chain);
    }

    public com.itextpdf.text.List createList(String tag) {
        return this.factory.createList(tag, this.chain);
    }

    public ListItem createListItem() {
        return this.factory.createListItem(this.chain);
    }

    public LineSeparator createLineSeparator(Map<String, String> attrs) {
        return this.factory.createLineSeparator(attrs, this.currentParagraph.getLeading() / BaseField.BORDER_WIDTH_MEDIUM);
    }

    public Image createImage(Map<String, String> attrs) throws DocumentException, IOException {
        String src = (String) attrs.get(HtmlTags.SRC);
        if (src == null) {
            return null;
        }
        return this.factory.createImage(src, attrs, this.chain, this.document, (ImageProvider) this.providers.get(IMG_PROVIDER), (ImageStore) this.providers.get(IMG_STORE), (String) this.providers.get(IMG_BASEURL));
    }

    public CellWrapper createCell(String tag) {
        return new CellWrapper(tag, this.chain);
    }

    public void processLink() {
        if (this.currentParagraph == null) {
            this.currentParagraph = new Paragraph();
        }
        LinkProcessor i = (LinkProcessor) this.providers.get(LINK_PROVIDER);
        if (i == null || !i.process(this.currentParagraph, this.chain)) {
            String href = this.chain.getProperty(HtmlTags.HREF);
            if (href != null) {
                for (Chunk ck : this.currentParagraph.getChunks()) {
                    ck.setAnchor(href);
                }
            }
        }
        if (this.stack.isEmpty()) {
            this.currentParagraph = new Paragraph(new Phrase(this.currentParagraph));
            return;
        }
        Paragraph tmp = (Paragraph) this.stack.pop();
        tmp.add(new Phrase(this.currentParagraph));
        this.currentParagraph = tmp;
    }

    public void processList() throws DocumentException {
        if (!this.stack.empty()) {
            Element obj = (Element) this.stack.pop();
            if (!(obj instanceof com.itextpdf.text.List)) {
                this.stack.push(obj);
            } else if (this.stack.empty()) {
                this.document.add(obj);
            } else {
                ((TextElementArray) this.stack.peek()).add(obj);
            }
        }
    }

    public void processListItem() throws DocumentException {
        if (!this.stack.empty()) {
            Element obj = (Element) this.stack.pop();
            if (!(obj instanceof ListItem)) {
                this.stack.push(obj);
            } else if (this.stack.empty()) {
                this.document.add(obj);
            } else {
                ListItem item = (ListItem) obj;
                Element list = (Element) this.stack.pop();
                if (list instanceof com.itextpdf.text.List) {
                    ((com.itextpdf.text.List) list).add(item);
                    item.adjustListSymbolFont();
                    this.stack.push(list);
                    return;
                }
                this.stack.push(list);
            }
        }
    }

    public void processImage(Image img, Map<String, String> attrs) throws DocumentException {
        ImageProcessor processor = (ImageProcessor) this.providers.get(IMG_PROCESSOR);
        if (processor == null || !processor.process(img, attrs, this.chain, this.document)) {
            String align = (String) attrs.get(HtmlTags.ALIGN);
            if (align != null) {
                carriageReturn();
            }
            if (this.currentParagraph == null) {
                this.currentParagraph = createParagraph();
            }
            this.currentParagraph.add(new Chunk(img, 0.0f, 0.0f, true));
            this.currentParagraph.setAlignment(HtmlUtilities.alignmentValue(align));
            if (align != null) {
                carriageReturn();
            }
        }
    }

    public void processTable() throws DocumentException {
        PdfPTable tb = ((TableWrapper) this.stack.pop()).createTable();
        tb.setSplitRows(true);
        if (this.stack.empty()) {
            this.document.add(tb);
        } else {
            ((TextElementArray) this.stack.peek()).add(tb);
        }
    }

    public void processRow() {
        Element obj;
        ArrayList<PdfPCell> row = new ArrayList();
        ArrayList<Float> cellWidths = new ArrayList();
        boolean percentage = false;
        float totalWidth = 0.0f;
        int zeroWidth = 0;
        do {
            obj = (Element) this.stack.pop();
            if (obj instanceof CellWrapper) {
                CellWrapper cell = (CellWrapper) obj;
                float width = cell.getWidth();
                cellWidths.add(new Float(width));
                percentage |= cell.isPercentage();
                if (width == 0.0f) {
                    zeroWidth++;
                } else {
                    totalWidth += width;
                }
                row.add(cell.getCell());
            }
        } while (!(obj instanceof TableWrapper));
        TableWrapper table = (TableWrapper) obj;
        table.addRow(row);
        if (cellWidths.size() > 0) {
            totalWidth = 100.0f - totalWidth;
            Collections.reverse(cellWidths);
            float[] widths = new float[cellWidths.size()];
            boolean hasZero = false;
            for (int i = 0; i < widths.length; i++) {
                widths[i] = ((Float) cellWidths.get(i)).floatValue();
                if (widths[i] == 0.0f && percentage && zeroWidth > 0) {
                    widths[i] = totalWidth / ((float) zeroWidth);
                }
                if (widths[i] == 0.0f) {
                    hasZero = true;
                    break;
                }
            }
            if (!hasZero) {
                table.setColWidths(widths);
            }
        }
        this.stack.push(table);
    }

    public void pushTableState() {
        this.tableState.push(new boolean[]{this.pendingTR, this.pendingTD});
    }

    public void popTableState() {
        boolean[] state = (boolean[]) this.tableState.pop();
        this.pendingTR = state[0];
        this.pendingTD = state[1];
    }

    public boolean isPendingTR() {
        return this.pendingTR;
    }

    public void setPendingTR(boolean pendingTR) {
        this.pendingTR = pendingTR;
    }

    public boolean isPendingTD() {
        return this.pendingTD;
    }

    public void setPendingTD(boolean pendingTD) {
        this.pendingTD = pendingTD;
    }

    public boolean isPendingLI() {
        return this.pendingLI;
    }

    public void setPendingLI(boolean pendingLI) {
        this.pendingLI = pendingLI;
    }

    public boolean isInsidePRE() {
        return this.insidePRE;
    }

    public void setInsidePRE(boolean insidePRE) {
        this.insidePRE = insidePRE;
    }

    public boolean isSkipText() {
        return this.skipText;
    }

    public void setSkipText(boolean skipText) {
        this.skipText = skipText;
    }

    public static List<Element> parseToList(Reader reader, StyleSheet style) throws IOException {
        return parseToList(reader, style, null);
    }

    public static List<Element> parseToList(Reader reader, StyleSheet style, HashMap<String, Object> providers) throws IOException {
        return parseToList(reader, style, null, providers);
    }

    public static List<Element> parseToList(Reader reader, StyleSheet style, Map<String, HTMLTagProcessor> tags, HashMap<String, Object> providers) throws IOException {
        HTMLWorker worker = new HTMLWorker(null, tags, style);
        worker.document = worker;
        worker.setProviders(providers);
        worker.objectList = new ArrayList();
        worker.parse(reader);
        return worker.objectList;
    }

    public boolean add(Element element) throws DocumentException {
        this.objectList.add(element);
        return true;
    }

    public void close() {
    }

    public boolean newPage() {
        return true;
    }

    public void open() {
    }

    public void resetPageCount() {
    }

    public boolean setMarginMirroring(boolean marginMirroring) {
        return false;
    }

    public boolean setMarginMirroringTopBottom(boolean marginMirroring) {
        return false;
    }

    public boolean setMargins(float marginLeft, float marginRight, float marginTop, float marginBottom) {
        return true;
    }

    public void setPageCount(int pageN) {
    }

    public boolean setPageSize(Rectangle pageSize) {
        return true;
    }

    @Deprecated
    public void setInterfaceProps(HashMap<String, Object> providers) {
        setProviders(providers);
    }

    @Deprecated
    public Map<String, Object> getInterfaceProps() {
        return this.providers;
    }
}

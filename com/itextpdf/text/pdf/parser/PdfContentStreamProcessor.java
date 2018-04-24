package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.io.RandomAccessSourceFactory;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PRIndirectReference;
import com.itextpdf.text.pdf.PRTokeniser;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfContentParser;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfLiteral;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfStream;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class PdfContentStreamProcessor {
    public static final String DEFAULTOPERATOR = "DefaultOperator";
    private final Map<Integer, CMapAwareDocumentFont> cachedFonts = new HashMap();
    private final Stack<GraphicsState> gsStack = new Stack();
    private final Stack<MarkedContentInfo> markedContentStack = new Stack();
    private final Map<String, ContentOperator> operators;
    private final RenderListener renderListener;
    private ResourceDictionary resources;
    private Matrix textLineMatrix;
    private Matrix textMatrix;
    private final Map<PdfName, XObjectDoHandler> xobjectDoHandlers;

    private static class BeginMarkedContent implements ContentOperator {
        private BeginMarkedContent() {
        }

        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) throws Exception {
            processor.beginMarkedContent((PdfName) operands.get(0), new PdfDictionary());
        }
    }

    private static class BeginMarkedContentDictionary implements ContentOperator {
        private BeginMarkedContentDictionary() {
        }

        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) throws Exception {
            processor.beginMarkedContent((PdfName) operands.get(0), getPropertiesDictionary((PdfObject) operands.get(1), processor.resources));
        }

        private PdfDictionary getPropertiesDictionary(PdfObject operand1, ResourceDictionary resources) {
            if (operand1.isDictionary()) {
                return (PdfDictionary) operand1;
            }
            return resources.getAsDict((PdfName) operand1);
        }
    }

    private static class BeginText implements ContentOperator {
        private BeginText() {
        }

        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> arrayList) {
            processor.textMatrix = new Matrix();
            processor.textLineMatrix = processor.textMatrix;
            processor.beginText();
        }
    }

    private static class Do implements ContentOperator {
        private Do() {
        }

        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) throws IOException {
            processor.displayXObject((PdfName) operands.get(0));
        }
    }

    private static class EndMarkedContent implements ContentOperator {
        private EndMarkedContent() {
        }

        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> arrayList) throws Exception {
            processor.endMarkedContent();
        }
    }

    private static class EndText implements ContentOperator {
        private EndText() {
        }

        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> arrayList) {
            processor.textMatrix = null;
            processor.textLineMatrix = null;
            processor.endText();
        }
    }

    private static class FormXObjectDoHandler implements XObjectDoHandler {
        private FormXObjectDoHandler() {
        }

        public void handleXObject(PdfContentStreamProcessor processor, PdfStream stream, PdfIndirectReference ref) {
            PdfDictionary resources = stream.getAsDict(PdfName.RESOURCES);
            try {
                byte[] contentBytes = ContentByteUtils.getContentBytesFromContentObject(stream);
                PdfArray matrix = stream.getAsArray(PdfName.MATRIX);
                new PushGraphicsState().invoke(processor, null, null);
                if (matrix != null) {
                    Matrix formMatrix = new Matrix(matrix.getAsNumber(0).floatValue(), matrix.getAsNumber(1).floatValue(), matrix.getAsNumber(2).floatValue(), matrix.getAsNumber(3).floatValue(), matrix.getAsNumber(4).floatValue(), matrix.getAsNumber(5).floatValue());
                    processor.gs().ctm = formMatrix.multiply(processor.gs().ctm);
                }
                processor.processContent(contentBytes, resources);
                new PopGraphicsState().invoke(processor, null, null);
            } catch (IOException e1) {
                throw new ExceptionConverter(e1);
            }
        }
    }

    private static class IgnoreOperatorContentOperator implements ContentOperator {
        private IgnoreOperatorContentOperator() {
        }

        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> arrayList) {
        }
    }

    private static class IgnoreXObjectDoHandler implements XObjectDoHandler {
        private IgnoreXObjectDoHandler() {
        }

        public void handleXObject(PdfContentStreamProcessor processor, PdfStream xobjectStream, PdfIndirectReference ref) {
        }
    }

    private static class ImageXObjectDoHandler implements XObjectDoHandler {
        private ImageXObjectDoHandler() {
        }

        public void handleXObject(PdfContentStreamProcessor processor, PdfStream xobjectStream, PdfIndirectReference ref) {
            processor.renderListener.renderImage(ImageRenderInfo.createForXObject(processor.gs().ctm, ref, processor.resources.getAsDict(PdfName.COLORSPACE)));
        }
    }

    private static class ModifyCurrentTransformationMatrix implements ContentOperator {
        private ModifyCurrentTransformationMatrix() {
        }

        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            GraphicsState gs = (GraphicsState) processor.gsStack.peek();
            gs.ctm = new Matrix(((PdfNumber) operands.get(0)).floatValue(), ((PdfNumber) operands.get(1)).floatValue(), ((PdfNumber) operands.get(2)).floatValue(), ((PdfNumber) operands.get(3)).floatValue(), ((PdfNumber) operands.get(4)).floatValue(), ((PdfNumber) operands.get(5)).floatValue()).multiply(gs.ctm);
        }
    }

    private static class MoveNextLineAndShowText implements ContentOperator {
        private final ShowText showText;
        private final TextMoveNextLine textMoveNextLine;

        public MoveNextLineAndShowText(TextMoveNextLine textMoveNextLine, ShowText showText) {
            this.textMoveNextLine = textMoveNextLine;
            this.showText = showText;
        }

        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            this.textMoveNextLine.invoke(processor, null, new ArrayList(0));
            this.showText.invoke(processor, null, operands);
        }
    }

    private static class MoveNextLineAndShowTextWithSpacing implements ContentOperator {
        private final MoveNextLineAndShowText moveNextLineAndShowText;
        private final SetTextCharacterSpacing setTextCharacterSpacing;
        private final SetTextWordSpacing setTextWordSpacing;

        public MoveNextLineAndShowTextWithSpacing(SetTextWordSpacing setTextWordSpacing, SetTextCharacterSpacing setTextCharacterSpacing, MoveNextLineAndShowText moveNextLineAndShowText) {
            this.setTextWordSpacing = setTextWordSpacing;
            this.setTextCharacterSpacing = setTextCharacterSpacing;
            this.moveNextLineAndShowText = moveNextLineAndShowText;
        }

        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            PdfNumber aw = (PdfNumber) operands.get(0);
            PdfNumber ac = (PdfNumber) operands.get(1);
            PdfString string = (PdfString) operands.get(2);
            ArrayList<PdfObject> twOperands = new ArrayList(1);
            twOperands.add(0, aw);
            this.setTextWordSpacing.invoke(processor, null, twOperands);
            ArrayList<PdfObject> tcOperands = new ArrayList(1);
            tcOperands.add(0, ac);
            this.setTextCharacterSpacing.invoke(processor, null, tcOperands);
            ArrayList<PdfObject> tickOperands = new ArrayList(1);
            tickOperands.add(0, string);
            this.moveNextLineAndShowText.invoke(processor, null, tickOperands);
        }
    }

    private static class PopGraphicsState implements ContentOperator {
        private PopGraphicsState() {
        }

        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> arrayList) {
            processor.gsStack.pop();
        }
    }

    private static class ProcessGraphicsStateResource implements ContentOperator {
        private ProcessGraphicsStateResource() {
        }

        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            PdfName dictionaryName = (PdfName) operands.get(0);
            PdfDictionary extGState = processor.resources.getAsDict(PdfName.EXTGSTATE);
            if (extGState == null) {
                throw new IllegalArgumentException(MessageLocalization.getComposedMessage("resources.do.not.contain.extgstate.entry.unable.to.process.operator.1", operator));
            }
            PdfDictionary gsDic = extGState.getAsDict(dictionaryName);
            if (gsDic == null) {
                throw new IllegalArgumentException(MessageLocalization.getComposedMessage("1.is.an.unknown.graphics.state.dictionary", dictionaryName));
            }
            PdfArray fontParameter = gsDic.getAsArray(PdfName.FONT);
            if (fontParameter != null) {
                CMapAwareDocumentFont font = processor.getFont((PRIndirectReference) fontParameter.getPdfObject(0));
                float size = fontParameter.getAsNumber(1).floatValue();
                processor.gs().font = font;
                processor.gs().fontSize = size;
            }
        }
    }

    private static class PushGraphicsState implements ContentOperator {
        private PushGraphicsState() {
        }

        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> arrayList) {
            processor.gsStack.push(new GraphicsState((GraphicsState) processor.gsStack.peek()));
        }
    }

    private static class ResourceDictionary extends PdfDictionary {
        private final List<PdfDictionary> resourcesStack = new ArrayList();

        public void push(PdfDictionary resources) {
            this.resourcesStack.add(resources);
        }

        public void pop() {
            this.resourcesStack.remove(this.resourcesStack.size() - 1);
        }

        public PdfObject getDirectObject(PdfName key) {
            for (int i = this.resourcesStack.size() - 1; i >= 0; i--) {
                PdfDictionary subResource = (PdfDictionary) this.resourcesStack.get(i);
                if (subResource != null) {
                    PdfObject obj = subResource.getDirectObject(key);
                    if (obj != null) {
                        return obj;
                    }
                }
            }
            return super.getDirectObject(key);
        }
    }

    private static class SetCMYKFill implements ContentOperator {
        private SetCMYKFill() {
        }

        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            processor.gs().fillColor = PdfContentStreamProcessor.getColor(4, (List) operands);
        }
    }

    private static class SetCMYKStroke implements ContentOperator {
        private SetCMYKStroke() {
        }

        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            processor.gs().strokeColor = PdfContentStreamProcessor.getColor(4, (List) operands);
        }
    }

    private static class SetColorFill implements ContentOperator {
        private SetColorFill() {
        }

        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            processor.gs().fillColor = PdfContentStreamProcessor.getColor(processor.gs().colorSpaceFill, (List) operands);
        }
    }

    private static class SetColorSpaceFill implements ContentOperator {
        private SetColorSpaceFill() {
        }

        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            processor.gs().colorSpaceFill = (PdfName) operands.get(0);
        }
    }

    private static class SetColorSpaceStroke implements ContentOperator {
        private SetColorSpaceStroke() {
        }

        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            processor.gs().colorSpaceStroke = (PdfName) operands.get(0);
        }
    }

    private static class SetColorStroke implements ContentOperator {
        private SetColorStroke() {
        }

        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            processor.gs().strokeColor = PdfContentStreamProcessor.getColor(processor.gs().colorSpaceStroke, (List) operands);
        }
    }

    private static class SetGrayFill implements ContentOperator {
        private SetGrayFill() {
        }

        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            processor.gs().fillColor = PdfContentStreamProcessor.getColor(1, (List) operands);
        }
    }

    private static class SetGrayStroke implements ContentOperator {
        private SetGrayStroke() {
        }

        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            processor.gs().strokeColor = PdfContentStreamProcessor.getColor(1, (List) operands);
        }
    }

    private static class SetRGBFill implements ContentOperator {
        private SetRGBFill() {
        }

        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            processor.gs().fillColor = PdfContentStreamProcessor.getColor(3, (List) operands);
        }
    }

    private static class SetRGBStroke implements ContentOperator {
        private SetRGBStroke() {
        }

        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            processor.gs().strokeColor = PdfContentStreamProcessor.getColor(3, (List) operands);
        }
    }

    private static class SetTextCharacterSpacing implements ContentOperator {
        private SetTextCharacterSpacing() {
        }

        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            PdfNumber charSpace = (PdfNumber) operands.get(0);
            processor.gs().characterSpacing = charSpace.floatValue();
        }
    }

    private static class SetTextFont implements ContentOperator {
        private SetTextFont() {
        }

        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            CMapAwareDocumentFont font;
            PdfName fontResourceName = (PdfName) operands.get(0);
            float size = ((PdfNumber) operands.get(1)).floatValue();
            PdfObject fontObject = processor.resources.getAsDict(PdfName.FONT).get(fontResourceName);
            if (fontObject instanceof PdfDictionary) {
                font = processor.getFont((PdfDictionary) fontObject);
            } else {
                font = processor.getFont((PRIndirectReference) fontObject);
            }
            processor.gs().font = font;
            processor.gs().fontSize = size;
        }
    }

    private static class SetTextHorizontalScaling implements ContentOperator {
        private SetTextHorizontalScaling() {
        }

        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            PdfNumber scale = (PdfNumber) operands.get(0);
            processor.gs().horizontalScaling = scale.floatValue() / 100.0f;
        }
    }

    private static class SetTextLeading implements ContentOperator {
        private SetTextLeading() {
        }

        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            PdfNumber leading = (PdfNumber) operands.get(0);
            processor.gs().leading = leading.floatValue();
        }
    }

    private static class SetTextRenderMode implements ContentOperator {
        private SetTextRenderMode() {
        }

        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            PdfNumber render = (PdfNumber) operands.get(0);
            processor.gs().renderMode = render.intValue();
        }
    }

    private static class SetTextRise implements ContentOperator {
        private SetTextRise() {
        }

        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            PdfNumber rise = (PdfNumber) operands.get(0);
            processor.gs().rise = rise.floatValue();
        }
    }

    private static class SetTextWordSpacing implements ContentOperator {
        private SetTextWordSpacing() {
        }

        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            PdfNumber wordSpace = (PdfNumber) operands.get(0);
            processor.gs().wordSpacing = wordSpace.floatValue();
        }
    }

    private static class ShowText implements ContentOperator {
        private ShowText() {
        }

        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            processor.displayPdfString((PdfString) operands.get(0));
        }
    }

    private static class ShowTextArray implements ContentOperator {
        private ShowTextArray() {
        }

        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            Iterator<PdfObject> i = ((PdfArray) operands.get(0)).listIterator();
            while (i.hasNext()) {
                PdfObject entryObj = (PdfObject) i.next();
                if (entryObj instanceof PdfString) {
                    processor.displayPdfString((PdfString) entryObj);
                } else {
                    processor.applyTextAdjust(((PdfNumber) entryObj).floatValue());
                }
            }
        }
    }

    private static class TextMoveNextLine implements ContentOperator {
        private final TextMoveStartNextLine moveStartNextLine;

        public TextMoveNextLine(TextMoveStartNextLine moveStartNextLine) {
            this.moveStartNextLine = moveStartNextLine;
        }

        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> arrayList) {
            ArrayList<PdfObject> tdoperands = new ArrayList(2);
            tdoperands.add(0, new PdfNumber(0));
            tdoperands.add(1, new PdfNumber(-processor.gs().leading));
            this.moveStartNextLine.invoke(processor, null, tdoperands);
        }
    }

    private static class TextMoveStartNextLine implements ContentOperator {
        private TextMoveStartNextLine() {
        }

        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            processor.textMatrix = new Matrix(((PdfNumber) operands.get(0)).floatValue(), ((PdfNumber) operands.get(1)).floatValue()).multiply(processor.textLineMatrix);
            processor.textLineMatrix = processor.textMatrix;
        }
    }

    private static class TextMoveStartNextLineWithLeading implements ContentOperator {
        private final TextMoveStartNextLine moveStartNextLine;
        private final SetTextLeading setTextLeading;

        public TextMoveStartNextLineWithLeading(TextMoveStartNextLine moveStartNextLine, SetTextLeading setTextLeading) {
            this.moveStartNextLine = moveStartNextLine;
            this.setTextLeading = setTextLeading;
        }

        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            float ty = ((PdfNumber) operands.get(1)).floatValue();
            ArrayList<PdfObject> tlOperands = new ArrayList(1);
            tlOperands.add(0, new PdfNumber(-ty));
            this.setTextLeading.invoke(processor, null, tlOperands);
            this.moveStartNextLine.invoke(processor, null, operands);
        }
    }

    private static class TextSetTextMatrix implements ContentOperator {
        private TextSetTextMatrix() {
        }

        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            processor.textLineMatrix = new Matrix(((PdfNumber) operands.get(0)).floatValue(), ((PdfNumber) operands.get(1)).floatValue(), ((PdfNumber) operands.get(2)).floatValue(), ((PdfNumber) operands.get(3)).floatValue(), ((PdfNumber) operands.get(4)).floatValue(), ((PdfNumber) operands.get(5)).floatValue());
            processor.textMatrix = processor.textLineMatrix;
        }
    }

    public PdfContentStreamProcessor(RenderListener renderListener) {
        this.renderListener = renderListener;
        this.operators = new HashMap();
        populateOperators();
        this.xobjectDoHandlers = new HashMap();
        populateXObjectDoHandlers();
        reset();
    }

    private void populateXObjectDoHandlers() {
        registerXObjectDoHandler(PdfName.DEFAULT, new IgnoreXObjectDoHandler());
        registerXObjectDoHandler(PdfName.FORM, new FormXObjectDoHandler());
        registerXObjectDoHandler(PdfName.IMAGE, new ImageXObjectDoHandler());
    }

    public XObjectDoHandler registerXObjectDoHandler(PdfName xobjectSubType, XObjectDoHandler handler) {
        return (XObjectDoHandler) this.xobjectDoHandlers.put(xobjectSubType, handler);
    }

    private CMapAwareDocumentFont getFont(PRIndirectReference ind) {
        Integer n = Integer.valueOf(ind.getNumber());
        CMapAwareDocumentFont font = (CMapAwareDocumentFont) this.cachedFonts.get(n);
        if (font != null) {
            return font;
        }
        font = new CMapAwareDocumentFont(ind);
        this.cachedFonts.put(n, font);
        return font;
    }

    private CMapAwareDocumentFont getFont(PdfDictionary fontResource) {
        return new CMapAwareDocumentFont(fontResource);
    }

    private void populateOperators() {
        registerContentOperator(DEFAULTOPERATOR, new IgnoreOperatorContentOperator());
        registerContentOperator("q", new PushGraphicsState());
        registerContentOperator("Q", new PopGraphicsState());
        registerContentOperator("g", new SetGrayFill());
        registerContentOperator("G", new SetGrayStroke());
        registerContentOperator("rg", new SetRGBFill());
        registerContentOperator("RG", new SetRGBStroke());
        registerContentOperator("k", new SetCMYKFill());
        registerContentOperator("K", new SetCMYKStroke());
        registerContentOperator("cs", new SetColorSpaceFill());
        registerContentOperator("CS", new SetColorSpaceStroke());
        registerContentOperator("sc", new SetColorFill());
        registerContentOperator("SC", new SetColorStroke());
        registerContentOperator("scn", new SetColorFill());
        registerContentOperator("SCN", new SetColorStroke());
        registerContentOperator("cm", new ModifyCurrentTransformationMatrix());
        registerContentOperator("gs", new ProcessGraphicsStateResource());
        SetTextCharacterSpacing tcOperator = new SetTextCharacterSpacing();
        registerContentOperator("Tc", tcOperator);
        SetTextWordSpacing twOperator = new SetTextWordSpacing();
        registerContentOperator("Tw", twOperator);
        registerContentOperator("Tz", new SetTextHorizontalScaling());
        SetTextLeading tlOperator = new SetTextLeading();
        registerContentOperator("TL", tlOperator);
        registerContentOperator("Tf", new SetTextFont());
        registerContentOperator("Tr", new SetTextRenderMode());
        registerContentOperator("Ts", new SetTextRise());
        registerContentOperator("BT", new BeginText());
        registerContentOperator("ET", new EndText());
        registerContentOperator("BMC", new BeginMarkedContent());
        registerContentOperator("BDC", new BeginMarkedContentDictionary());
        registerContentOperator("EMC", new EndMarkedContent());
        TextMoveStartNextLine tdOperator = new TextMoveStartNextLine();
        registerContentOperator("Td", tdOperator);
        registerContentOperator("TD", new TextMoveStartNextLineWithLeading(tdOperator, tlOperator));
        registerContentOperator("Tm", new TextSetTextMatrix());
        TextMoveNextLine tstarOperator = new TextMoveNextLine(tdOperator);
        registerContentOperator("T*", tstarOperator);
        ShowText tjOperator = new ShowText();
        registerContentOperator("Tj", tjOperator);
        MoveNextLineAndShowText tickOperator = new MoveNextLineAndShowText(tstarOperator, tjOperator);
        registerContentOperator("'", tickOperator);
        registerContentOperator("\"", new MoveNextLineAndShowTextWithSpacing(twOperator, tcOperator, tickOperator));
        registerContentOperator("TJ", new ShowTextArray());
        registerContentOperator("Do", new Do());
    }

    public ContentOperator registerContentOperator(String operatorString, ContentOperator operator) {
        return (ContentOperator) this.operators.put(operatorString, operator);
    }

    public void reset() {
        this.gsStack.removeAllElements();
        this.gsStack.add(new GraphicsState());
        this.textMatrix = null;
        this.textLineMatrix = null;
        this.resources = new ResourceDictionary();
    }

    private GraphicsState gs() {
        return (GraphicsState) this.gsStack.peek();
    }

    private void invokeOperator(PdfLiteral operator, ArrayList<PdfObject> operands) throws Exception {
        ContentOperator op = (ContentOperator) this.operators.get(operator.toString());
        if (op == null) {
            op = (ContentOperator) this.operators.get(DEFAULTOPERATOR);
        }
        op.invoke(this, operator, operands);
    }

    private void beginMarkedContent(PdfName tag, PdfDictionary dict) {
        this.markedContentStack.push(new MarkedContentInfo(tag, dict));
    }

    private void endMarkedContent() {
        this.markedContentStack.pop();
    }

    private void beginText() {
        this.renderListener.beginTextBlock();
    }

    private void endText() {
        this.renderListener.endTextBlock();
    }

    private void displayPdfString(PdfString string) {
        TextRenderInfo renderInfo = new TextRenderInfo(string, gs(), this.textMatrix, this.markedContentStack);
        this.renderListener.renderText(renderInfo);
        this.textMatrix = new Matrix(renderInfo.getUnscaledWidth(), 0.0f).multiply(this.textMatrix);
    }

    private void displayXObject(PdfName xobjectName) throws IOException {
        PdfDictionary xobjects = this.resources.getAsDict(PdfName.XOBJECT);
        PdfObject xobject = xobjects.getDirectObject(xobjectName);
        PdfStream xobjectStream = (PdfStream) xobject;
        PdfName subType = xobjectStream.getAsName(PdfName.SUBTYPE);
        if (xobject.isStream()) {
            XObjectDoHandler handler = (XObjectDoHandler) this.xobjectDoHandlers.get(subType);
            if (handler == null) {
                handler = (XObjectDoHandler) this.xobjectDoHandlers.get(PdfName.DEFAULT);
            }
            handler.handleXObject(this, xobjectStream, xobjects.getAsIndirectObject(xobjectName));
            return;
        }
        throw new IllegalStateException(MessageLocalization.getComposedMessage("XObject.1.is.not.a.stream", xobjectName));
    }

    private void applyTextAdjust(float tj) {
        this.textMatrix = new Matrix((((-tj) / 1000.0f) * gs().fontSize) * gs().horizontalScaling, 0.0f).multiply(this.textMatrix);
    }

    public void processContent(byte[] contentBytes, PdfDictionary resources) {
        this.resources.push(resources);
        try {
            PdfContentParser ps = new PdfContentParser(new PRTokeniser(new RandomAccessFileOrArray(new RandomAccessSourceFactory().createSource(contentBytes))));
            ArrayList<PdfObject> operands = new ArrayList();
            while (ps.parse(operands).size() > 0) {
                PdfLiteral operator = (PdfLiteral) operands.get(operands.size() - 1);
                if ("BI".equals(operator.toString())) {
                    PdfDictionary colorSpaceDic = resources != null ? resources.getAsDict(PdfName.COLORSPACE) : null;
                    handleInlineImage(InlineImageUtils.parseInlineImage(ps, colorSpaceDic), colorSpaceDic);
                } else {
                    invokeOperator(operator, operands);
                }
            }
            this.resources.pop();
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    protected void handleInlineImage(InlineImageInfo info, PdfDictionary colorSpaceDic) {
        this.renderListener.renderImage(ImageRenderInfo.createForEmbeddedImage(gs().ctm, info, colorSpaceDic));
    }

    private static BaseColor getColor(PdfName colorSpace, List<PdfObject> operands) {
        if (PdfName.DEVICEGRAY.equals(colorSpace)) {
            return getColor(1, (List) operands);
        }
        if (PdfName.DEVICERGB.equals(colorSpace)) {
            return getColor(3, (List) operands);
        }
        if (PdfName.DEVICECMYK.equals(colorSpace)) {
            return getColor(4, (List) operands);
        }
        return null;
    }

    private static BaseColor getColor(int nOperands, List<PdfObject> operands) {
        float[] c = new float[nOperands];
        for (int i = 0; i < nOperands; i++) {
            c[i] = ((PdfNumber) operands.get(i)).floatValue();
        }
        switch (nOperands) {
            case 1:
                return new GrayColor(c[0]);
            case 3:
                return new BaseColor(c[0], c[1], c[2]);
            case 4:
                return new CMYKColor(c[0], c[1], c[2], c[3]);
            default:
                return null;
        }
    }
}

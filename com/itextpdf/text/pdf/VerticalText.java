package com.itextpdf.text.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.error_messages.MessageLocalization;
import java.util.ArrayList;
import java.util.Iterator;

public class VerticalText {
    public static final int NO_MORE_COLUMN = 2;
    public static final int NO_MORE_TEXT = 1;
    protected int alignment = 0;
    protected ArrayList<PdfChunk> chunks = new ArrayList();
    private Float curCharSpace = Float.valueOf(0.0f);
    protected int currentChunkMarker = -1;
    protected PdfChunk currentStandbyChunk;
    protected float height;
    protected float leading;
    protected int maxLines;
    protected String splittedChunkText;
    protected float startX;
    protected float startY;
    protected PdfContentByte text;

    public VerticalText(PdfContentByte text) {
        this.text = text;
    }

    public void addText(Phrase phrase) {
        for (Chunk c : phrase.getChunks()) {
            this.chunks.add(new PdfChunk(c, null));
        }
    }

    public void addText(Chunk chunk) {
        this.chunks.add(new PdfChunk(chunk, null));
    }

    public void setVerticalLayout(float startX, float startY, float height, int maxLines, float leading) {
        this.startX = startX;
        this.startY = startY;
        this.height = height;
        this.maxLines = maxLines;
        setLeading(leading);
    }

    public void setLeading(float leading) {
        this.leading = leading;
    }

    public float getLeading() {
        return this.leading;
    }

    protected PdfLine createLine(float width) {
        PdfLine pdfLine = null;
        if (!this.chunks.isEmpty()) {
            this.splittedChunkText = null;
            this.currentStandbyChunk = null;
            pdfLine = new PdfLine(0.0f, width, this.alignment, 0.0f);
            this.currentChunkMarker = 0;
            while (this.currentChunkMarker < this.chunks.size()) {
                PdfChunk original = (PdfChunk) this.chunks.get(this.currentChunkMarker);
                String total = original.toString();
                this.currentStandbyChunk = pdfLine.add(original);
                if (this.currentStandbyChunk != null) {
                    this.splittedChunkText = original.toString();
                    original.setValue(total);
                    break;
                }
                this.currentChunkMarker++;
            }
        }
        return pdfLine;
    }

    protected void shortenChunkArray() {
        if (this.currentChunkMarker >= 0) {
            if (this.currentChunkMarker >= this.chunks.size()) {
                this.chunks.clear();
                return;
            }
            ((PdfChunk) this.chunks.get(this.currentChunkMarker)).setValue(this.splittedChunkText);
            this.chunks.set(this.currentChunkMarker, this.currentStandbyChunk);
            for (int j = this.currentChunkMarker - 1; j >= 0; j--) {
                this.chunks.remove(j);
            }
        }
    }

    public int go() {
        return go(false);
    }

    public int go(boolean simulate) {
        int status;
        boolean dirty = false;
        PdfContentByte graphics = null;
        if (this.text != null) {
            graphics = this.text.getDuplicate();
        } else if (!simulate) {
            throw new NullPointerException(MessageLocalization.getComposedMessage("verticaltext.go.with.simulate.eq.eq.false.and.text.eq.eq.null", new Object[0]));
        }
        while (this.maxLines > 0) {
            if (this.chunks.isEmpty()) {
                status = 1;
                break;
            }
            PdfLine line = createLine(this.height);
            if (!(simulate || dirty)) {
                this.text.beginText();
                dirty = true;
            }
            shortenChunkArray();
            if (!simulate) {
                this.text.setTextMatrix(this.startX, this.startY - line.indentLeft());
                writeLine(line, this.text, graphics);
            }
            this.maxLines--;
            this.startX -= this.leading;
        }
        status = 2;
        if (this.chunks.isEmpty()) {
            status = 2 | 1;
        }
        if (dirty) {
            this.text.endText();
            this.text.add(graphics);
        }
        return status;
    }

    void writeLine(PdfLine line, PdfContentByte text, PdfContentByte graphics) {
        PdfFont currentFont = null;
        Iterator<PdfChunk> j = line.iterator();
        while (j.hasNext()) {
            PdfChunk chunk = (PdfChunk) j.next();
            if (!(chunk.isImage() || chunk.font().compareTo(currentFont) == 0)) {
                currentFont = chunk.font();
                text.setFontAndSize(currentFont.getFont(), currentFont.size());
            }
            Object[] textRender = (Object[]) chunk.getAttribute(Chunk.TEXTRENDERMODE);
            int tr = 0;
            float strokeWidth = BaseField.BORDER_WIDTH_THIN;
            BaseColor color = chunk.color();
            BaseColor strokeColor = null;
            if (textRender != null) {
                tr = ((Integer) textRender[0]).intValue() & 3;
                if (tr != 0) {
                    text.setTextRenderingMode(tr);
                }
                if (tr == 1 || tr == 2) {
                    strokeWidth = ((Float) textRender[1]).floatValue();
                    if (strokeWidth != BaseField.BORDER_WIDTH_THIN) {
                        text.setLineWidth(strokeWidth);
                    }
                    strokeColor = textRender[2];
                    if (strokeColor == null) {
                        strokeColor = color;
                    }
                    if (strokeColor != null) {
                        text.setColorStroke(strokeColor);
                    }
                }
            }
            Float charSpace = (Float) chunk.getAttribute(Chunk.CHAR_SPACING);
            if (!(charSpace == null || this.curCharSpace.equals(charSpace))) {
                this.curCharSpace = Float.valueOf(charSpace.floatValue());
                text.setCharacterSpacing(this.curCharSpace.floatValue());
            }
            if (color != null) {
                text.setColorFill(color);
            }
            text.showText(chunk.toString());
            if (color != null) {
                text.resetRGBColorFill();
            }
            if (tr != 0) {
                text.setTextRenderingMode(0);
            }
            if (strokeColor != null) {
                text.resetRGBColorStroke();
            }
            if (strokeWidth != BaseField.BORDER_WIDTH_THIN) {
                text.setLineWidth(BaseField.BORDER_WIDTH_THIN);
            }
        }
    }

    public void setOrigin(float startX, float startY) {
        this.startX = startX;
        this.startY = startY;
    }

    public float getOriginX() {
        return this.startX;
    }

    public float getOriginY() {
        return this.startY;
    }

    public int getMaxLines() {
        return this.maxLines;
    }

    public void setMaxLines(int maxLines) {
        this.maxLines = maxLines;
    }

    public float getHeight() {
        return this.height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setAlignment(int alignment) {
        this.alignment = alignment;
    }

    public int getAlignment() {
        return this.alignment;
    }
}

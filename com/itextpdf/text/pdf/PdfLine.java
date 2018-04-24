package com.itextpdf.text.pdf;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Image;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.TabStop;
import com.itextpdf.text.TabStop.Alignment;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.poi.hssf.record.DeltaRecord;

public class PdfLine {
    protected int alignment;
    protected float height;
    protected boolean isRTL = false;
    protected float left;
    protected ArrayList<PdfChunk> line;
    protected ListItem listItem = null;
    protected boolean newlineSplit = false;
    protected float originalWidth;
    protected float tabPosition = Float.NaN;
    protected TabStop tabStop = null;
    protected float tabStopAnchorPosition = Float.NaN;
    protected float width;

    PdfLine(float left, float right, int alignment, float height) {
        this.left = left;
        this.width = right - left;
        this.originalWidth = this.width;
        this.alignment = alignment;
        this.height = height;
        this.line = new ArrayList();
    }

    PdfLine(float left, float originalWidth, float remainingWidth, int alignment, boolean newlineSplit, ArrayList<PdfChunk> line, boolean isRTL) {
        this.left = left;
        this.originalWidth = originalWidth;
        this.width = remainingWidth;
        this.alignment = alignment;
        this.line = line;
        this.newlineSplit = newlineSplit;
        this.isRTL = isRTL;
    }

    PdfChunk add(PdfChunk chunk) {
        if (chunk == null || chunk.toString().equals("")) {
            return null;
        }
        PdfChunk overflow = chunk.split(this.width);
        boolean z = chunk.isNewlineSplit() || overflow == null;
        this.newlineSplit = z;
        if (chunk.isTab()) {
            Object[] tab = (Object[]) chunk.getAttribute(Chunk.TAB);
            if (chunk.isAttribute(Chunk.TABSETTINGS)) {
                boolean isWhiteSpace = ((Boolean) tab[1]).booleanValue();
                if (isWhiteSpace && this.line.isEmpty()) {
                    return null;
                }
                flush();
                this.tabStopAnchorPosition = Float.NaN;
                this.tabStop = PdfChunk.getTabStop(chunk, this.originalWidth - this.width);
                if (this.tabStop.getPosition() > this.originalWidth) {
                    if (isWhiteSpace) {
                        overflow = null;
                    } else if (((double) Math.abs(this.originalWidth - this.width)) < DeltaRecord.DEFAULT_VALUE) {
                        addToLine(chunk);
                        overflow = null;
                    } else {
                        overflow = chunk;
                    }
                    this.width = 0.0f;
                    return overflow;
                }
                chunk.setTabStop(this.tabStop);
                if (this.tabStop.getAlignment() == Alignment.LEFT) {
                    this.width = this.originalWidth - this.tabStop.getPosition();
                    this.tabStop = null;
                    this.tabPosition = Float.NaN;
                } else {
                    this.tabPosition = this.originalWidth - this.width;
                }
                addToLine(chunk);
                return overflow;
            }
            Float tabStopPosition = Float.valueOf(((Float) tab[1]).floatValue());
            if (((Boolean) tab[2]).booleanValue() && tabStopPosition.floatValue() < this.originalWidth - this.width) {
                return chunk;
            }
            chunk.adjustLeft(this.left);
            this.width = this.originalWidth - tabStopPosition.floatValue();
            addToLine(chunk);
            return overflow;
        } else if (chunk.length() > 0 || chunk.isImage()) {
            if (overflow != null) {
                chunk.trimLastSpace();
            }
            this.width -= chunk.width();
            addToLine(chunk);
            return overflow;
        } else if (this.line.size() < 1) {
            chunk = overflow;
            overflow = chunk.truncate(this.width);
            this.width -= chunk.width();
            if (chunk.length() > 0) {
                addToLine(chunk);
                return overflow;
            }
            if (overflow != null) {
                addToLine(overflow);
            }
            return null;
        } else {
            this.width = ((PdfChunk) this.line.get(this.line.size() - 1)).trimLastSpace() + this.width;
            return overflow;
        }
    }

    private void addToLine(PdfChunk chunk) {
        if (chunk.changeLeading) {
            float f;
            if (chunk.isImage()) {
                Image img = chunk.getImage();
                f = ((chunk.getImageHeight() + chunk.getImageOffsetY()) + img.getBorderWidthTop()) + img.getSpacingBefore();
            } else {
                f = chunk.getLeading();
            }
            if (f > this.height) {
                this.height = f;
            }
        }
        if (this.tabStop != null && this.tabStop.getAlignment() == Alignment.ANCHOR && Float.isNaN(this.tabStopAnchorPosition)) {
            String value = chunk.toString();
            int anchorIndex = value.indexOf(this.tabStop.getAnchorChar());
            if (anchorIndex != -1) {
                this.tabStopAnchorPosition = (this.originalWidth - this.width) - chunk.width(value.substring(anchorIndex, value.length()));
            }
        }
        this.line.add(chunk);
    }

    public int size() {
        return this.line.size();
    }

    public Iterator<PdfChunk> iterator() {
        return this.line.iterator();
    }

    float height() {
        return this.height;
    }

    float indentLeft() {
        if (this.isRTL) {
            switch (this.alignment) {
                case 1:
                    return this.left + (this.width / BaseField.BORDER_WIDTH_MEDIUM);
                case 2:
                    return this.left;
                case 3:
                    return (hasToBeJustified() ? 0.0f : this.width) + this.left;
                default:
                    return this.left + this.width;
            }
        }
        if (getSeparatorCount() <= 0) {
            switch (this.alignment) {
                case 1:
                    return this.left + (this.width / BaseField.BORDER_WIDTH_MEDIUM);
                case 2:
                    return this.left + this.width;
            }
        }
        return this.left;
    }

    public boolean hasToBeJustified() {
        return ((this.alignment == 3 && !this.newlineSplit) || this.alignment == 8) && this.width != 0.0f;
    }

    public void resetAlignment() {
        if (this.alignment == 3) {
            this.alignment = 0;
        }
    }

    void setExtraIndent(float extra) {
        this.left += extra;
        this.width -= extra;
        this.originalWidth -= extra;
    }

    float widthLeft() {
        return this.width;
    }

    int numberOfSpaces() {
        int numberOfSpaces = 0;
        Iterator i$ = this.line.iterator();
        while (i$.hasNext()) {
            String tmp = ((PdfChunk) i$.next()).toString();
            int length = tmp.length();
            for (int i = 0; i < length; i++) {
                if (tmp.charAt(i) == ' ') {
                    numberOfSpaces++;
                }
            }
        }
        return numberOfSpaces;
    }

    public void setListItem(ListItem listItem) {
        this.listItem = listItem;
    }

    public Chunk listSymbol() {
        return this.listItem != null ? this.listItem.getListSymbol() : null;
    }

    public float listIndent() {
        return this.listItem != null ? this.listItem.getIndentationLeft() : 0.0f;
    }

    public ListItem listItem() {
        return this.listItem;
    }

    public String toString() {
        StringBuffer tmp = new StringBuffer();
        Iterator i$ = this.line.iterator();
        while (i$.hasNext()) {
            tmp.append(((PdfChunk) i$.next()).toString());
        }
        return tmp.toString();
    }

    public int getLineLengthUtf32() {
        int total = 0;
        Iterator i$ = this.line.iterator();
        while (i$.hasNext()) {
            total += ((PdfChunk) i$.next()).lengthUtf32();
        }
        return total;
    }

    public boolean isNewlineSplit() {
        return this.newlineSplit && this.alignment != 8;
    }

    public int getLastStrokeChunk() {
        int lastIdx = this.line.size() - 1;
        while (lastIdx >= 0 && !((PdfChunk) this.line.get(lastIdx)).isStroked()) {
            lastIdx--;
        }
        return lastIdx;
    }

    public PdfChunk getChunk(int idx) {
        if (idx < 0 || idx >= this.line.size()) {
            return null;
        }
        return (PdfChunk) this.line.get(idx);
    }

    public float getOriginalWidth() {
        return this.originalWidth;
    }

    float[] getMaxSize(float fixedLeading, float multipliedLeading) {
        float normal_leading = 0.0f;
        float image_leading = -10000.0f;
        for (int k = 0; k < this.line.size(); k++) {
            PdfChunk chunk = (PdfChunk) this.line.get(k);
            if (chunk.isImage()) {
                Image img = chunk.getImage();
                if (chunk.changeLeading()) {
                    image_leading = Math.max((chunk.getImageHeight() + chunk.getImageOffsetY()) + img.getSpacingBefore(), image_leading);
                }
            } else if (chunk.changeLeading()) {
                normal_leading = Math.max(chunk.getLeading(), normal_leading);
            } else {
                normal_leading = Math.max((chunk.font().size() * multipliedLeading) + fixedLeading, normal_leading);
            }
        }
        float[] fArr = new float[2];
        if (normal_leading <= 0.0f) {
            normal_leading = fixedLeading;
        }
        fArr[0] = normal_leading;
        fArr[1] = image_leading;
        return fArr;
    }

    boolean isRTL() {
        return this.isRTL;
    }

    int getSeparatorCount() {
        int s = 0;
        Iterator i$ = this.line.iterator();
        while (i$.hasNext()) {
            PdfChunk ck = (PdfChunk) i$.next();
            if (ck.isTab()) {
                if (!ck.isAttribute(Chunk.TABSETTINGS)) {
                    return -1;
                }
            } else if (ck.isHorizontalSeparator()) {
                s++;
            }
        }
        return s;
    }

    public float getWidthCorrected(float charSpacing, float wordSpacing) {
        float total = 0.0f;
        for (int k = 0; k < this.line.size(); k++) {
            total += ((PdfChunk) this.line.get(k)).getWidthCorrected(charSpacing, wordSpacing);
        }
        return total;
    }

    public float getAscender() {
        float ascender = 0.0f;
        for (int k = 0; k < this.line.size(); k++) {
            PdfChunk ck = (PdfChunk) this.line.get(k);
            if (ck.isImage()) {
                ascender = Math.max(ascender, ck.getImageHeight() + ck.getImageOffsetY());
            } else {
                PdfFont font = ck.font();
                float textRise = ck.getTextRise();
                if (textRise <= 0.0f) {
                    textRise = 0.0f;
                }
                ascender = Math.max(ascender, font.getFont().getFontDescriptor(1, font.size()) + textRise);
            }
        }
        return ascender;
    }

    public float getDescender() {
        float descender = 0.0f;
        for (int k = 0; k < this.line.size(); k++) {
            PdfChunk ck = (PdfChunk) this.line.get(k);
            if (ck.isImage()) {
                descender = Math.min(descender, ck.getImageOffsetY());
            } else {
                PdfFont font = ck.font();
                float textRise = ck.getTextRise();
                if (textRise >= 0.0f) {
                    textRise = 0.0f;
                }
                descender = Math.min(descender, font.getFont().getFontDescriptor(3, font.size()) + textRise);
            }
        }
        return descender;
    }

    public void flush() {
        if (this.tabStop != null) {
            float textWidth = (this.originalWidth - this.width) - this.tabPosition;
            float tabStopPosition = this.tabStop.getPosition(this.tabPosition, this.originalWidth - this.width, this.tabStopAnchorPosition);
            this.width = (this.originalWidth - tabStopPosition) - textWidth;
            if (this.width < 0.0f) {
                tabStopPosition += this.width;
            }
            this.tabStop.setPosition(tabStopPosition);
            this.tabStop = null;
            this.tabPosition = Float.NaN;
        }
    }
}

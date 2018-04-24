package com.itextpdf.text.html.simpleparser;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.ElementListener;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.TextElementArray;
import com.itextpdf.text.html.HtmlTags;
import com.itextpdf.text.html.HtmlUtilities;
import com.itextpdf.text.pdf.PdfPCell;
import java.util.List;

@Deprecated
public class CellWrapper implements TextElementArray {
    private final PdfPCell cell;
    private boolean percentage;
    private float width;

    public CellWrapper(String tag, ChainedProperties chain) {
        this.cell = createPdfPCell(tag, chain);
        String value = chain.getProperty(HtmlTags.WIDTH);
        if (value != null) {
            value = value.trim();
            if (value.endsWith("%")) {
                this.percentage = true;
                value = value.substring(0, value.length() - 1);
            }
            this.width = Float.parseFloat(value);
        }
    }

    public PdfPCell createPdfPCell(String tag, ChainedProperties chain) {
        PdfPCell cell = new PdfPCell((Phrase) null);
        String value = chain.getProperty(HtmlTags.COLSPAN);
        if (value != null) {
            cell.setColspan(Integer.parseInt(value));
        }
        value = chain.getProperty(HtmlTags.ROWSPAN);
        if (value != null) {
            cell.setRowspan(Integer.parseInt(value));
        }
        if (tag.equals(HtmlTags.TH)) {
            cell.setHorizontalAlignment(1);
        }
        value = chain.getProperty(HtmlTags.ALIGN);
        if (value != null) {
            cell.setHorizontalAlignment(HtmlUtilities.alignmentValue(value));
        }
        value = chain.getProperty(HtmlTags.VALIGN);
        cell.setVerticalAlignment(5);
        if (value != null) {
            cell.setVerticalAlignment(HtmlUtilities.alignmentValue(value));
        }
        value = chain.getProperty(HtmlTags.BORDER);
        float border = 0.0f;
        if (value != null) {
            border = Float.parseFloat(value);
        }
        cell.setBorderWidth(border);
        value = chain.getProperty(HtmlTags.CELLPADDING);
        if (value != null) {
            cell.setPadding(Float.parseFloat(value));
        }
        cell.setUseDescender(true);
        cell.setBackgroundColor(HtmlUtilities.decodeColor(chain.getProperty(HtmlTags.BGCOLOR)));
        return cell;
    }

    public PdfPCell getCell() {
        return this.cell;
    }

    public float getWidth() {
        return this.width;
    }

    public boolean isPercentage() {
        return this.percentage;
    }

    public boolean add(Element o) {
        this.cell.addElement(o);
        return true;
    }

    public List<Chunk> getChunks() {
        return null;
    }

    public boolean isContent() {
        return false;
    }

    public boolean isNestable() {
        return false;
    }

    public boolean process(ElementListener listener) {
        return false;
    }

    public int type() {
        return 0;
    }
}

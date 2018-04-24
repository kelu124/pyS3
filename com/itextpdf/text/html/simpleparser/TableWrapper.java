package com.itextpdf.text.html.simpleparser;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.ElementListener;
import com.itextpdf.text.html.HtmlTags;
import com.itextpdf.text.html.HtmlUtilities;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Deprecated
public class TableWrapper implements Element {
    private float[] colWidths;
    private final List<List<PdfPCell>> rows = new ArrayList();
    private final Map<String, String> styles = new HashMap();

    public TableWrapper(Map<String, String> attrs) {
        this.styles.putAll(attrs);
    }

    public void addRow(List<PdfPCell> row) {
        if (row != null) {
            Collections.reverse(row);
            this.rows.add(row);
        }
    }

    public void setColWidths(float[] colWidths) {
        this.colWidths = colWidths;
    }

    public PdfPTable createTable() {
        if (this.rows.isEmpty()) {
            return new PdfPTable(1);
        }
        int ncol = 0;
        for (PdfPCell pc : (List) this.rows.get(0)) {
            ncol += pc.getColspan();
        }
        PdfPTable table = new PdfPTable(ncol);
        String width = (String) this.styles.get(HtmlTags.WIDTH);
        if (width == null) {
            table.setWidthPercentage(100.0f);
        } else if (width.endsWith("%")) {
            table.setWidthPercentage(Float.parseFloat(width.substring(0, width.length() - 1)));
        } else {
            table.setTotalWidth(Float.parseFloat(width));
            table.setLockedWidth(true);
        }
        String alignment = (String) this.styles.get(HtmlTags.ALIGN);
        int align = 0;
        if (alignment != null) {
            align = HtmlUtilities.alignmentValue(alignment);
        }
        table.setHorizontalAlignment(align);
        try {
            if (this.colWidths != null) {
                table.setWidths(this.colWidths);
            }
        } catch (Exception e) {
        }
        for (List<PdfPCell> col : this.rows) {
            for (PdfPCell pc2 : col) {
                table.addCell(pc2);
            }
        }
        return table;
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

package com.itextpdf.text.pdf.events;

import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPRow;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPTableEvent;
import com.itextpdf.text.pdf.PdfPTableEventAfterSplit;
import com.itextpdf.text.pdf.PdfPTableEventSplit;
import java.util.ArrayList;
import java.util.Iterator;

public class PdfPTableEventForwarder implements PdfPTableEventAfterSplit {
    protected ArrayList<PdfPTableEvent> events = new ArrayList();

    public void addTableEvent(PdfPTableEvent event) {
        this.events.add(event);
    }

    public void tableLayout(PdfPTable table, float[][] widths, float[] heights, int headerRows, int rowStart, PdfContentByte[] canvases) {
        Iterator i$ = this.events.iterator();
        while (i$.hasNext()) {
            ((PdfPTableEvent) i$.next()).tableLayout(table, widths, heights, headerRows, rowStart, canvases);
        }
    }

    public void splitTable(PdfPTable table) {
        Iterator i$ = this.events.iterator();
        while (i$.hasNext()) {
            PdfPTableEvent event = (PdfPTableEvent) i$.next();
            if (event instanceof PdfPTableEventSplit) {
                ((PdfPTableEventSplit) event).splitTable(table);
            }
        }
    }

    public void afterSplitTable(PdfPTable table, PdfPRow startRow, int startIdx) {
        Iterator i$ = this.events.iterator();
        while (i$.hasNext()) {
            PdfPTableEvent event = (PdfPTableEvent) i$.next();
            if (event instanceof PdfPTableEventAfterSplit) {
                ((PdfPTableEventAfterSplit) event).afterSplitTable(table, startRow, startIdx);
            }
        }
    }
}

package com.itextpdf.text.pdf.events;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import java.util.ArrayList;
import java.util.Iterator;

public class PdfPCellEventForwarder implements PdfPCellEvent {
    protected ArrayList<PdfPCellEvent> events = new ArrayList();

    public void addCellEvent(PdfPCellEvent event) {
        this.events.add(event);
    }

    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        Iterator i$ = this.events.iterator();
        while (i$.hasNext()) {
            ((PdfPCellEvent) i$.next()).cellLayout(cell, position, canvases);
        }
    }
}

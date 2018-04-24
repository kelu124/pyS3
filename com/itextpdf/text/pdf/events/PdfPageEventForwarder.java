package com.itextpdf.text.pdf.events;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPageEvent;
import com.itextpdf.text.pdf.PdfWriter;
import java.util.ArrayList;
import java.util.Iterator;

public class PdfPageEventForwarder implements PdfPageEvent {
    protected ArrayList<PdfPageEvent> events = new ArrayList();

    public void addPageEvent(PdfPageEvent event) {
        this.events.add(event);
    }

    public void onOpenDocument(PdfWriter writer, Document document) {
        Iterator i$ = this.events.iterator();
        while (i$.hasNext()) {
            ((PdfPageEvent) i$.next()).onOpenDocument(writer, document);
        }
    }

    public void onStartPage(PdfWriter writer, Document document) {
        Iterator i$ = this.events.iterator();
        while (i$.hasNext()) {
            ((PdfPageEvent) i$.next()).onStartPage(writer, document);
        }
    }

    public void onEndPage(PdfWriter writer, Document document) {
        Iterator i$ = this.events.iterator();
        while (i$.hasNext()) {
            ((PdfPageEvent) i$.next()).onEndPage(writer, document);
        }
    }

    public void onCloseDocument(PdfWriter writer, Document document) {
        Iterator i$ = this.events.iterator();
        while (i$.hasNext()) {
            ((PdfPageEvent) i$.next()).onCloseDocument(writer, document);
        }
    }

    public void onParagraph(PdfWriter writer, Document document, float paragraphPosition) {
        Iterator i$ = this.events.iterator();
        while (i$.hasNext()) {
            ((PdfPageEvent) i$.next()).onParagraph(writer, document, paragraphPosition);
        }
    }

    public void onParagraphEnd(PdfWriter writer, Document document, float paragraphPosition) {
        Iterator i$ = this.events.iterator();
        while (i$.hasNext()) {
            ((PdfPageEvent) i$.next()).onParagraphEnd(writer, document, paragraphPosition);
        }
    }

    public void onChapter(PdfWriter writer, Document document, float paragraphPosition, Paragraph title) {
        Iterator i$ = this.events.iterator();
        while (i$.hasNext()) {
            ((PdfPageEvent) i$.next()).onChapter(writer, document, paragraphPosition, title);
        }
    }

    public void onChapterEnd(PdfWriter writer, Document document, float position) {
        Iterator i$ = this.events.iterator();
        while (i$.hasNext()) {
            ((PdfPageEvent) i$.next()).onChapterEnd(writer, document, position);
        }
    }

    public void onSection(PdfWriter writer, Document document, float paragraphPosition, int depth, Paragraph title) {
        Iterator i$ = this.events.iterator();
        while (i$.hasNext()) {
            ((PdfPageEvent) i$.next()).onSection(writer, document, paragraphPosition, depth, title);
        }
    }

    public void onSectionEnd(PdfWriter writer, Document document, float position) {
        Iterator i$ = this.events.iterator();
        while (i$.hasNext()) {
            ((PdfPageEvent) i$.next()).onSectionEnd(writer, document, position);
        }
    }

    public void onGenericTag(PdfWriter writer, Document document, Rectangle rect, String text) {
        Iterator i$ = this.events.iterator();
        while (i$.hasNext()) {
            ((PdfPageEvent) i$.next()).onGenericTag(writer, document, rect, text);
        }
    }
}

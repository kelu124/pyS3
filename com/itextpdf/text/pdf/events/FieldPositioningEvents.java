package com.itextpdf.text.pdf.events;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.BaseField;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfRectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.TextField;
import java.io.IOException;
import java.util.HashMap;

public class FieldPositioningEvents extends PdfPageEventHelper implements PdfPCellEvent {
    protected PdfFormField cellField = null;
    protected PdfWriter fieldWriter = null;
    protected HashMap<String, PdfFormField> genericChunkFields = new HashMap();
    public float padding;
    protected PdfFormField parent = null;

    public void addField(String text, PdfFormField field) {
        this.genericChunkFields.put(text, field);
    }

    public FieldPositioningEvents(PdfWriter writer, PdfFormField field) {
        this.cellField = field;
        this.fieldWriter = writer;
    }

    public FieldPositioningEvents(PdfFormField parent, PdfFormField field) {
        this.cellField = field;
        this.parent = parent;
    }

    public FieldPositioningEvents(PdfWriter writer, String text) throws IOException, DocumentException {
        this.fieldWriter = writer;
        TextField tf = new TextField(writer, new Rectangle(0.0f, 0.0f), text);
        tf.setFontSize(14.0f);
        this.cellField = tf.getTextField();
    }

    public FieldPositioningEvents(PdfWriter writer, PdfFormField parent, String text) throws IOException, DocumentException {
        this.parent = parent;
        TextField tf = new TextField(writer, new Rectangle(0.0f, 0.0f), text);
        tf.setFontSize(14.0f);
        this.cellField = tf.getTextField();
    }

    public void setPadding(float padding) {
        this.padding = padding;
    }

    public void setParent(PdfFormField parent) {
        this.parent = parent;
    }

    public void onGenericTag(PdfWriter writer, Document document, Rectangle rect, String text) {
        rect.setBottom(rect.getBottom() - BaseField.BORDER_WIDTH_THICK);
        PdfFormField field = (PdfFormField) this.genericChunkFields.get(text);
        if (field == null) {
            TextField tf = new TextField(writer, new Rectangle(rect.getLeft(this.padding), rect.getBottom(this.padding), rect.getRight(this.padding), rect.getTop(this.padding)), text);
            tf.setFontSize(14.0f);
            try {
                field = tf.getTextField();
            } catch (Exception e) {
                throw new ExceptionConverter(e);
            }
        }
        field.put(PdfName.RECT, new PdfRectangle(rect.getLeft(this.padding), rect.getBottom(this.padding), rect.getRight(this.padding), rect.getTop(this.padding)));
        if (this.parent == null) {
            writer.addAnnotation(field);
        } else {
            this.parent.addKid(field);
        }
    }

    public void cellLayout(PdfPCell cell, Rectangle rect, PdfContentByte[] canvases) {
        if (this.cellField == null || (this.fieldWriter == null && this.parent == null)) {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("you.have.used.the.wrong.constructor.for.this.fieldpositioningevents.class", new Object[0]));
        }
        this.cellField.put(PdfName.RECT, new PdfRectangle(rect.getLeft(this.padding), rect.getBottom(this.padding), rect.getRight(this.padding), rect.getTop(this.padding)));
        if (this.parent == null) {
            this.fieldWriter.addAnnotation(this.cellField);
        } else {
            this.parent.addKid(this.cellField);
        }
    }
}

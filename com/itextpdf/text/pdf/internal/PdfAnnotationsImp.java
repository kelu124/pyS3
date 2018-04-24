package com.itextpdf.text.pdf.internal;

import com.itextpdf.text.Annotation;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfAcroForm;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfFileSpecification;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfRectangle;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import org.bytedeco.javacpp.dc1394;

public class PdfAnnotationsImp {
    protected PdfAcroForm acroForm;
    protected ArrayList<PdfAnnotation> annotations;
    protected ArrayList<PdfAnnotation> delayedAnnotations = new ArrayList();

    public PdfAnnotationsImp(PdfWriter writer) {
        this.acroForm = new PdfAcroForm(writer);
    }

    public boolean hasValidAcroForm() {
        return this.acroForm.isValid();
    }

    public PdfAcroForm getAcroForm() {
        return this.acroForm;
    }

    public void setSigFlags(int f) {
        this.acroForm.setSigFlags(f);
    }

    public void addCalculationOrder(PdfFormField formField) {
        this.acroForm.addCalculationOrder(formField);
    }

    public void addAnnotation(PdfAnnotation annot) {
        if (annot.isForm()) {
            PdfFormField field = (PdfFormField) annot;
            if (field.getParent() == null) {
                addFormFieldRaw(field);
                return;
            }
            return;
        }
        this.annotations.add(annot);
    }

    public void addPlainAnnotation(PdfAnnotation annot) {
        this.annotations.add(annot);
    }

    void addFormFieldRaw(PdfFormField field) {
        this.annotations.add(field);
        ArrayList<PdfFormField> kids = field.getKids();
        if (kids != null) {
            for (int k = 0; k < kids.size(); k++) {
                addFormFieldRaw((PdfFormField) kids.get(k));
            }
        }
    }

    public boolean hasUnusedAnnotations() {
        return !this.annotations.isEmpty();
    }

    public void resetAnnotations() {
        this.annotations = this.delayedAnnotations;
        this.delayedAnnotations = new ArrayList();
    }

    public PdfArray rotateAnnotations(PdfWriter writer, Rectangle pageSize) {
        PdfArray array = new PdfArray();
        int rotation = pageSize.getRotation() % dc1394.DC1394_COLOR_CODING_RGB16S;
        int currentPage = writer.getCurrentPageNumber();
        for (int k = 0; k < this.annotations.size(); k++) {
            PdfAnnotation dic = (PdfAnnotation) this.annotations.get(k);
            if (dic.getPlaceInPage() > currentPage) {
                this.delayedAnnotations.add(dic);
            } else {
                if (dic.isForm()) {
                    if (!dic.isUsed()) {
                        HashSet<PdfTemplate> templates = dic.getTemplates();
                        if (templates != null) {
                            this.acroForm.addFieldTemplates(templates);
                        }
                    }
                    PdfFormField field = (PdfFormField) dic;
                    if (field.getParent() == null) {
                        this.acroForm.addDocumentField(field.getIndirectReference());
                    }
                }
                if (dic.isAnnotation()) {
                    array.add(dic.getIndirectReference());
                    if (!dic.isUsed()) {
                        PdfRectangle rect;
                        PdfArray tmp = dic.getAsArray(PdfName.RECT);
                        if (tmp.size() == 4) {
                            rect = new PdfRectangle(tmp.getAsNumber(0).floatValue(), tmp.getAsNumber(1).floatValue(), tmp.getAsNumber(2).floatValue(), tmp.getAsNumber(3).floatValue());
                        } else {
                            rect = new PdfRectangle(tmp.getAsNumber(0).floatValue(), tmp.getAsNumber(1).floatValue());
                        }
                        if (rect != null) {
                            switch (rotation) {
                                case 90:
                                    dic.put(PdfName.RECT, new PdfRectangle(pageSize.getTop() - rect.bottom(), rect.left(), pageSize.getTop() - rect.top(), rect.right()));
                                    break;
                                case 180:
                                    dic.put(PdfName.RECT, new PdfRectangle(pageSize.getRight() - rect.left(), pageSize.getTop() - rect.bottom(), pageSize.getRight() - rect.right(), pageSize.getTop() - rect.top()));
                                    break;
                                case 270:
                                    dic.put(PdfName.RECT, new PdfRectangle(rect.bottom(), pageSize.getRight() - rect.left(), rect.top(), pageSize.getRight() - rect.right()));
                                    break;
                            }
                        }
                    }
                }
                if (dic.isUsed()) {
                    continue;
                } else {
                    dic.setUsed();
                    try {
                        writer.addToBody(dic, dic.getIndirectReference());
                    } catch (IOException e) {
                        throw new ExceptionConverter(e);
                    }
                }
            }
        }
        return array;
    }

    public static PdfAnnotation convertAnnotation(PdfWriter writer, Annotation annot, Rectangle defaultRect) throws IOException {
        switch (annot.annotationType()) {
            case 1:
                return writer.createAnnotation(annot.llx(), annot.lly(), annot.urx(), annot.ury(), new PdfAction((URL) annot.attributes().get(Annotation.URL)), null);
            case 2:
                return writer.createAnnotation(annot.llx(), annot.lly(), annot.urx(), annot.ury(), new PdfAction((String) annot.attributes().get(Annotation.FILE)), null);
            case 3:
                return writer.createAnnotation(annot.llx(), annot.lly(), annot.urx(), annot.ury(), new PdfAction((String) annot.attributes().get(Annotation.FILE), (String) annot.attributes().get(Annotation.DESTINATION)), null);
            case 4:
                return writer.createAnnotation(annot.llx(), annot.lly(), annot.urx(), annot.ury(), new PdfAction((String) annot.attributes().get(Annotation.FILE), ((Integer) annot.attributes().get(Annotation.PAGE)).intValue()), null);
            case 5:
                return writer.createAnnotation(annot.llx(), annot.lly(), annot.urx(), annot.ury(), new PdfAction(((Integer) annot.attributes().get(Annotation.NAMED)).intValue()), null);
            case 6:
                return writer.createAnnotation(annot.llx(), annot.lly(), annot.urx(), annot.ury(), new PdfAction((String) annot.attributes().get(Annotation.APPLICATION), (String) annot.attributes().get(Annotation.PARAMETERS), (String) annot.attributes().get(Annotation.OPERATION), (String) annot.attributes().get(Annotation.DEFAULTDIR)), null);
            case 7:
                PdfFileSpecification fs;
                boolean[] sparams = (boolean[]) annot.attributes().get(Annotation.PARAMETERS);
                String fname = (String) annot.attributes().get(Annotation.FILE);
                String mimetype = (String) annot.attributes().get(Annotation.MIMETYPE);
                if (sparams[0]) {
                    fs = PdfFileSpecification.fileEmbedded(writer, fname, fname, null);
                } else {
                    fs = PdfFileSpecification.fileExtern(writer, fname);
                }
                return PdfAnnotation.createScreen(writer, new Rectangle(annot.llx(), annot.lly(), annot.urx(), annot.ury()), fname, fs, mimetype, sparams[1]);
            default:
                return writer.createAnnotation(defaultRect.getLeft(), defaultRect.getBottom(), defaultRect.getRight(), defaultRect.getTop(), new PdfString(annot.title(), PdfObject.TEXT_UNICODE), new PdfString(annot.content(), PdfObject.TEXT_UNICODE), null);
        }
    }
}

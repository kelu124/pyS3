package com.itextpdf.text.pdf.interfaces;

import com.itextpdf.text.AccessibleElementId;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import java.util.HashMap;

public interface IAccessibleElement {
    PdfObject getAccessibleAttribute(PdfName pdfName);

    HashMap<PdfName, PdfObject> getAccessibleAttributes();

    AccessibleElementId getId();

    PdfName getRole();

    boolean isInline();

    void setAccessibleAttribute(PdfName pdfName, PdfObject pdfObject);

    void setId(AccessibleElementId accessibleElementId);

    void setRole(PdfName pdfName);
}

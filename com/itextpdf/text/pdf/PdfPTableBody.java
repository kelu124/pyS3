package com.itextpdf.text.pdf;

import com.itextpdf.text.AccessibleElementId;
import com.itextpdf.text.pdf.interfaces.IAccessibleElement;
import java.util.ArrayList;
import java.util.HashMap;

public class PdfPTableBody implements IAccessibleElement {
    protected HashMap<PdfName, PdfObject> accessibleAttributes = null;
    protected AccessibleElementId id = new AccessibleElementId();
    protected PdfName role = PdfName.TBODY;
    protected ArrayList<PdfPRow> rows = null;

    public PdfObject getAccessibleAttribute(PdfName key) {
        if (this.accessibleAttributes != null) {
            return (PdfObject) this.accessibleAttributes.get(key);
        }
        return null;
    }

    public void setAccessibleAttribute(PdfName key, PdfObject value) {
        if (this.accessibleAttributes == null) {
            this.accessibleAttributes = new HashMap();
        }
        this.accessibleAttributes.put(key, value);
    }

    public HashMap<PdfName, PdfObject> getAccessibleAttributes() {
        return this.accessibleAttributes;
    }

    public PdfName getRole() {
        return this.role;
    }

    public void setRole(PdfName role) {
        this.role = role;
    }

    public AccessibleElementId getId() {
        return this.id;
    }

    public void setId(AccessibleElementId id) {
        this.id = id;
    }

    public boolean isInline() {
        return false;
    }
}

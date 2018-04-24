package com.itextpdf.text.pdf;

import com.itextpdf.text.AccessibleElementId;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.interfaces.IAccessibleElement;
import java.io.IOException;
import java.util.HashMap;

public class PdfTemplate extends PdfContentByte implements IAccessibleElement {
    public static final int TYPE_IMPORTED = 2;
    public static final int TYPE_PATTERN = 3;
    public static final int TYPE_TEMPLATE = 1;
    protected HashMap<PdfName, PdfObject> accessibleAttributes;
    private PdfDictionary additional;
    protected Rectangle bBox;
    protected boolean contentTagged;
    protected PdfTransparencyGroup group;
    private AccessibleElementId id;
    protected PdfOCG layer;
    protected PdfArray matrix;
    protected PdfIndirectReference pageReference;
    protected PageResources pageResources;
    protected PdfName role;
    protected PdfIndirectReference thisReference;
    protected int type;

    protected PdfTemplate() {
        super(null);
        this.bBox = new Rectangle(0.0f, 0.0f);
        this.contentTagged = false;
        this.additional = null;
        this.role = PdfName.FIGURE;
        this.accessibleAttributes = null;
        this.id = null;
        this.type = 1;
    }

    PdfTemplate(PdfWriter wr) {
        super(wr);
        this.bBox = new Rectangle(0.0f, 0.0f);
        this.contentTagged = false;
        this.additional = null;
        this.role = PdfName.FIGURE;
        this.accessibleAttributes = null;
        this.id = null;
        this.type = 1;
        this.pageResources = new PageResources();
        this.pageResources.addDefaultColor(wr.getDefaultColorspace());
        this.thisReference = this.writer.getPdfIndirectReference();
    }

    public static PdfTemplate createTemplate(PdfWriter writer, float width, float height) {
        return createTemplate(writer, width, height, null);
    }

    static PdfTemplate createTemplate(PdfWriter writer, float width, float height, PdfName forcedName) {
        PdfTemplate template = new PdfTemplate(writer);
        template.setWidth(width);
        template.setHeight(height);
        writer.addDirectTemplateSimple(template, forcedName);
        return template;
    }

    public boolean isTagged() {
        return super.isTagged() && this.contentTagged;
    }

    public void setWidth(float width) {
        this.bBox.setLeft(0.0f);
        this.bBox.setRight(width);
    }

    public void setHeight(float height) {
        this.bBox.setBottom(0.0f);
        this.bBox.setTop(height);
    }

    public float getWidth() {
        return this.bBox.getWidth();
    }

    public float getHeight() {
        return this.bBox.getHeight();
    }

    public Rectangle getBoundingBox() {
        return this.bBox;
    }

    public void setBoundingBox(Rectangle bBox) {
        this.bBox = bBox;
    }

    public void setLayer(PdfOCG layer) {
        this.layer = layer;
    }

    public PdfOCG getLayer() {
        return this.layer;
    }

    public void setMatrix(float a, float b, float c, float d, float e, float f) {
        this.matrix = new PdfArray();
        this.matrix.add(new PdfNumber(a));
        this.matrix.add(new PdfNumber(b));
        this.matrix.add(new PdfNumber(c));
        this.matrix.add(new PdfNumber(d));
        this.matrix.add(new PdfNumber(e));
        this.matrix.add(new PdfNumber(f));
    }

    PdfArray getMatrix() {
        return this.matrix;
    }

    public PdfIndirectReference getIndirectReference() {
        if (this.thisReference == null) {
            this.thisReference = this.writer.getPdfIndirectReference();
        }
        return this.thisReference;
    }

    public void beginVariableText() {
        this.content.append("/Tx BMC ");
    }

    public void endVariableText() {
        this.content.append("EMC ");
    }

    PdfObject getResources() {
        return getPageResources().getResources();
    }

    public PdfStream getFormXObject(int compressionLevel) throws IOException {
        return new PdfFormXObject(this, compressionLevel);
    }

    public PdfContentByte getDuplicate() {
        PdfTemplate tpl = new PdfTemplate();
        tpl.writer = this.writer;
        tpl.pdf = this.pdf;
        tpl.thisReference = this.thisReference;
        tpl.pageResources = this.pageResources;
        tpl.bBox = new Rectangle(this.bBox);
        tpl.group = this.group;
        tpl.layer = this.layer;
        if (this.matrix != null) {
            tpl.matrix = new PdfArray(this.matrix);
        }
        tpl.separator = this.separator;
        tpl.additional = this.additional;
        return tpl;
    }

    public int getType() {
        return this.type;
    }

    PageResources getPageResources() {
        return this.pageResources;
    }

    public PdfTransparencyGroup getGroup() {
        return this.group;
    }

    public void setGroup(PdfTransparencyGroup group) {
        this.group = group;
    }

    public PdfDictionary getAdditional() {
        return this.additional;
    }

    public void setAdditional(PdfDictionary additional) {
        this.additional = additional;
    }

    public PdfIndirectReference getCurrentPage() {
        return this.pageReference == null ? this.writer.getCurrentPage() : this.pageReference;
    }

    public PdfIndirectReference getPageReference() {
        return this.pageReference;
    }

    public void setPageReference(PdfIndirectReference pageReference) {
        this.pageReference = pageReference;
    }

    public boolean isContentTagged() {
        return this.contentTagged;
    }

    public void setContentTagged(boolean contentTagged) {
        this.contentTagged = contentTagged;
    }

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
        if (this.id == null) {
            this.id = new AccessibleElementId();
        }
        return this.id;
    }

    public void setId(AccessibleElementId id) {
        this.id = id;
    }

    public boolean isInline() {
        return true;
    }
}

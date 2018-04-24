package com.itextpdf.text;

import com.itextpdf.text.api.Indentable;
import com.itextpdf.text.api.Spaceable;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.interfaces.IAccessibleElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Paragraph extends Phrase implements Indentable, Spaceable, IAccessibleElement {
    private static final long serialVersionUID = 7852314969733375514L;
    protected HashMap<PdfName, PdfObject> accessibleAttributes = null;
    protected int alignment = -1;
    private float extraParagraphSpace = 0.0f;
    private float firstLineIndent = 0.0f;
    private AccessibleElementId id = null;
    protected float indentationLeft;
    protected float indentationRight;
    protected boolean keeptogether = false;
    protected PdfName role = PdfName.P;
    protected float spacingAfter;
    protected float spacingBefore;

    public Paragraph(float leading) {
        super(leading);
    }

    public Paragraph(Chunk chunk) {
        super(chunk);
    }

    public Paragraph(float leading, Chunk chunk) {
        super(leading, chunk);
    }

    public Paragraph(String string) {
        super(string);
    }

    public Paragraph(String string, Font font) {
        super(string, font);
    }

    public Paragraph(float leading, String string) {
        super(leading, string);
    }

    public Paragraph(float leading, String string, Font font) {
        super(leading, string, font);
    }

    public Paragraph(Phrase phrase) {
        super(phrase);
        if (phrase instanceof Paragraph) {
            Paragraph p = (Paragraph) phrase;
            setAlignment(p.alignment);
            setIndentationLeft(p.getIndentationLeft());
            setIndentationRight(p.getIndentationRight());
            setFirstLineIndent(p.getFirstLineIndent());
            setSpacingAfter(p.getSpacingAfter());
            setSpacingBefore(p.getSpacingBefore());
            setExtraParagraphSpace(p.getExtraParagraphSpace());
            setRole(p.role);
            this.id = p.getId();
            if (p.accessibleAttributes != null) {
                this.accessibleAttributes = new HashMap(p.accessibleAttributes);
            }
        }
    }

    public Paragraph cloneShallow(boolean spacingBefore) {
        Paragraph copy = new Paragraph();
        copy.setFont(getFont());
        copy.setAlignment(getAlignment());
        copy.setLeading(getLeading(), this.multipliedLeading);
        copy.setIndentationLeft(getIndentationLeft());
        copy.setIndentationRight(getIndentationRight());
        copy.setFirstLineIndent(getFirstLineIndent());
        copy.setSpacingAfter(getSpacingAfter());
        if (spacingBefore) {
            copy.setSpacingBefore(getSpacingBefore());
        }
        copy.setExtraParagraphSpace(getExtraParagraphSpace());
        copy.setRole(this.role);
        copy.id = getId();
        if (this.accessibleAttributes != null) {
            copy.accessibleAttributes = new HashMap(this.accessibleAttributes);
        }
        copy.setTabSettings(getTabSettings());
        copy.setKeepTogether(getKeepTogether());
        return copy;
    }

    public List<Element> breakUp() {
        List<Element> list = new ArrayList();
        Paragraph tmp = null;
        Iterator i$ = iterator();
        while (i$.hasNext()) {
            Element e = (Element) i$.next();
            if (e.type() == 14 || e.type() == 23 || e.type() == 12) {
                if (tmp != null && tmp.size() > 0) {
                    tmp.setSpacingAfter(0.0f);
                    list.add(tmp);
                    tmp = cloneShallow(false);
                }
                if (list.size() == 0) {
                    switch (e.type()) {
                        case 12:
                            ((Paragraph) e).setSpacingBefore(getSpacingBefore());
                            break;
                        case 14:
                            ListItem firstItem = ((List) e).getFirstItem();
                            if (firstItem != null) {
                                firstItem.setSpacingBefore(getSpacingBefore());
                                break;
                            }
                            break;
                        case 23:
                            ((PdfPTable) e).setSpacingBefore(getSpacingBefore());
                            break;
                    }
                }
                list.add(e);
            } else {
                if (tmp == null) {
                    boolean z;
                    if (list.size() == 0) {
                        z = true;
                    } else {
                        z = false;
                    }
                    tmp = cloneShallow(z);
                }
                tmp.add(e);
            }
        }
        if (tmp != null && tmp.size() > 0) {
            list.add(tmp);
        }
        if (list.size() != 0) {
            Element lastElement = (Element) list.get(list.size() - 1);
            switch (lastElement.type()) {
                case 12:
                    ((Paragraph) lastElement).setSpacingAfter(getSpacingAfter());
                    break;
                case 14:
                    ListItem lastItem = ((List) lastElement).getLastItem();
                    if (lastItem != null) {
                        lastItem.setSpacingAfter(getSpacingAfter());
                        break;
                    }
                    break;
                case 23:
                    ((PdfPTable) lastElement).setSpacingAfter(getSpacingAfter());
                    break;
            }
        }
        return list;
    }

    public int type() {
        return 12;
    }

    public boolean add(Element o) {
        if (o instanceof List) {
            List list = (List) o;
            list.setIndentationLeft(list.getIndentationLeft() + this.indentationLeft);
            list.setIndentationRight(this.indentationRight);
            return super.add(list);
        } else if (o instanceof Image) {
            super.addSpecial(o);
            return true;
        } else if (!(o instanceof Paragraph)) {
            return super.add(o);
        } else {
            super.addSpecial(o);
            return true;
        }
    }

    public void setAlignment(int alignment) {
        this.alignment = alignment;
    }

    public void setIndentationLeft(float indentation) {
        this.indentationLeft = indentation;
    }

    public void setIndentationRight(float indentation) {
        this.indentationRight = indentation;
    }

    public void setFirstLineIndent(float firstLineIndent) {
        this.firstLineIndent = firstLineIndent;
    }

    public void setSpacingBefore(float spacing) {
        this.spacingBefore = spacing;
    }

    public void setSpacingAfter(float spacing) {
        this.spacingAfter = spacing;
    }

    public void setKeepTogether(boolean keeptogether) {
        this.keeptogether = keeptogether;
    }

    public boolean getKeepTogether() {
        return this.keeptogether;
    }

    public int getAlignment() {
        return this.alignment;
    }

    public float getIndentationLeft() {
        return this.indentationLeft;
    }

    public float getIndentationRight() {
        return this.indentationRight;
    }

    public float getFirstLineIndent() {
        return this.firstLineIndent;
    }

    public float getSpacingBefore() {
        return this.spacingBefore;
    }

    public float getSpacingAfter() {
        return this.spacingAfter;
    }

    public float getExtraParagraphSpace() {
        return this.extraParagraphSpace;
    }

    public void setExtraParagraphSpace(float extraParagraphSpace) {
        this.extraParagraphSpace = extraParagraphSpace;
    }

    @Deprecated
    public float spacingBefore() {
        return getSpacingBefore();
    }

    @Deprecated
    public float spacingAfter() {
        return this.spacingAfter;
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
        return false;
    }
}

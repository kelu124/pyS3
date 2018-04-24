package com.itextpdf.text.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListBody;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.ListLabel;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.interfaces.IAccessibleElement;
import com.itextpdf.text.pdf.interfaces.IPdfStructureElement;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;

public class PdfStructureElement extends PdfDictionary implements IPdfStructureElement {
    private PdfStructureElement parent;
    private PdfIndirectReference reference;
    private PdfName structureType;
    private PdfStructureTreeRoot top;

    public PdfStructureElement(PdfStructureElement parent, PdfName structureType) {
        this.top = parent.top;
        init(parent, structureType);
        this.parent = parent;
        put(PdfName.f130P, parent.reference);
        put(PdfName.TYPE, PdfName.STRUCTELEM);
    }

    public PdfStructureElement(PdfStructureTreeRoot parent, PdfName structureType) {
        this.top = parent;
        init(parent, structureType);
        put(PdfName.f130P, parent.getReference());
        put(PdfName.TYPE, PdfName.STRUCTELEM);
    }

    protected PdfStructureElement(PdfDictionary parent, PdfName structureType) {
        if (parent instanceof PdfStructureElement) {
            this.top = ((PdfStructureElement) parent).top;
            init(parent, structureType);
            this.parent = (PdfStructureElement) parent;
            put(PdfName.f130P, ((PdfStructureElement) parent).reference);
            put(PdfName.TYPE, PdfName.STRUCTELEM);
        } else if (parent instanceof PdfStructureTreeRoot) {
            this.top = (PdfStructureTreeRoot) parent;
            init(parent, structureType);
            put(PdfName.f130P, ((PdfStructureTreeRoot) parent).getReference());
            put(PdfName.TYPE, PdfName.STRUCTELEM);
        }
    }

    public PdfName getStructureType() {
        return this.structureType;
    }

    private void init(PdfDictionary parent, PdfName structureType) {
        PdfArray kids;
        if (this.top.getWriter().getStandardStructElems().contains(structureType)) {
            this.structureType = structureType;
        } else {
            PdfDictionary roleMap = this.top.getAsDict(PdfName.ROLEMAP);
            if (roleMap == null || !roleMap.contains(structureType)) {
                throw new ExceptionConverter(new DocumentException(MessageLocalization.getComposedMessage("unknown.structure.element.role.1", structureType.toString())));
            }
            this.structureType = roleMap.getAsName(structureType);
        }
        PdfObject kido = parent.get(PdfName.f125K);
        if (kido == null) {
            kids = new PdfArray();
            parent.put(PdfName.f125K, kids);
        } else if (kido instanceof PdfArray) {
            kids = (PdfArray) kido;
        } else {
            kids = new PdfArray();
            kids.add(kido);
            parent.put(PdfName.f125K, kids);
        }
        if (kids.size() > 0) {
            if (kids.getAsNumber(0) != null) {
                kids.remove(0);
            }
            if (kids.size() > 0) {
                PdfDictionary mcr = kids.getAsDict(0);
                if (mcr != null && PdfName.MCR.equals(mcr.getAsName(PdfName.TYPE))) {
                    kids.remove(0);
                }
            }
        }
        kids.add(this);
        put(PdfName.f133S, structureType);
        this.reference = this.top.getWriter().getPdfIndirectReference();
    }

    public PdfDictionary getParent() {
        return getParent(false);
    }

    public PdfDictionary getParent(boolean includeStructTreeRoot) {
        if (this.parent == null && includeStructTreeRoot) {
            return this.top;
        }
        return this.parent;
    }

    void setPageMark(int page, int mark) {
        if (mark >= 0) {
            put(PdfName.f125K, new PdfNumber(mark));
        }
        this.top.setPageMark(page, this.reference);
    }

    void setAnnotation(PdfAnnotation annot, PdfIndirectReference currentPage) {
        PdfArray kArray = getAsArray(PdfName.f125K);
        if (kArray == null) {
            kArray = new PdfArray();
            PdfObject k = get(PdfName.f125K);
            if (k != null) {
                kArray.add(k);
            }
            put(PdfName.f125K, kArray);
        }
        PdfDictionary dict = new PdfDictionary();
        dict.put(PdfName.TYPE, PdfName.OBJR);
        dict.put(PdfName.OBJ, annot.getIndirectReference());
        if (annot.getRole() == PdfName.FORM) {
            dict.put(PdfName.PG, currentPage);
        }
        kArray.add(dict);
    }

    public PdfIndirectReference getReference() {
        return this.reference;
    }

    public PdfObject getAttribute(PdfName name) {
        PdfDictionary attr = getAsDict(PdfName.f117A);
        if (attr != null && attr.contains(name)) {
            return attr.get(name);
        }
        PdfDictionary parent = getParent();
        if (parent instanceof PdfStructureElement) {
            return ((PdfStructureElement) parent).getAttribute(name);
        }
        if (parent instanceof PdfStructureTreeRoot) {
            return ((PdfStructureTreeRoot) parent).getAttribute(name);
        }
        return new PdfNull();
    }

    public void setAttribute(PdfName name, PdfObject obj) {
        PdfDictionary attr = getAsDict(PdfName.f117A);
        if (attr == null) {
            attr = new PdfDictionary();
            put(PdfName.f117A, attr);
        }
        attr.put(name, obj);
    }

    public void writeAttributes(IAccessibleElement element) {
        if (element instanceof ListItem) {
            writeAttributes((ListItem) element);
        } else if (element instanceof Paragraph) {
            writeAttributes((Paragraph) element);
        } else if (element instanceof Chunk) {
            writeAttributes((Chunk) element);
        } else if (element instanceof Image) {
            writeAttributes((Image) element);
        } else if (element instanceof List) {
            writeAttributes((List) element);
        } else if (element instanceof ListLabel) {
            writeAttributes((ListLabel) element);
        } else if (element instanceof ListBody) {
            writeAttributes((ListBody) element);
        } else if (element instanceof PdfPTable) {
            writeAttributes((PdfPTable) element);
        } else if (element instanceof PdfPRow) {
            writeAttributes((PdfPRow) element);
        } else if (element instanceof PdfPHeaderCell) {
            writeAttributes((PdfPHeaderCell) element);
        } else if (element instanceof PdfPCell) {
            writeAttributes((PdfPCell) element);
        } else if (element instanceof PdfPTableHeader) {
            writeAttributes((PdfPTableHeader) element);
        } else if (element instanceof PdfPTableFooter) {
            writeAttributes((PdfPTableFooter) element);
        } else if (element instanceof PdfPTableBody) {
            writeAttributes((PdfPTableBody) element);
        } else if (element instanceof PdfDiv) {
            writeAttributes((PdfDiv) element);
        } else if (element instanceof PdfTemplate) {
            writeAttributes((PdfTemplate) element);
        } else if (element instanceof Document) {
            writeAttributes((Document) element);
        }
        if (element.getAccessibleAttributes() != null) {
            for (PdfName key : element.getAccessibleAttributes().keySet()) {
                if (key.equals(PdfName.LANG) || key.equals(PdfName.ALT) || key.equals(PdfName.ACTUALTEXT) || key.equals(PdfName.f121E)) {
                    put(key, element.getAccessibleAttribute(key));
                } else {
                    setAttribute(key, element.getAccessibleAttribute(key));
                }
            }
        }
    }

    private void writeAttributes(Chunk chunk) {
        if (chunk == null) {
            return;
        }
        if (chunk.getImage() != null) {
            writeAttributes(chunk.getImage());
            return;
        }
        HashMap<String, Object> attr = chunk.getAttributes();
        if (attr != null) {
            BaseColor color;
            setAttribute(PdfName.f129O, PdfName.LAYOUT);
            if (attr.containsKey(Chunk.UNDERLINE)) {
                setAttribute(PdfName.TEXTDECORATIONTYPE, PdfName.UNDERLINE);
            }
            if (attr.containsKey(Chunk.BACKGROUND)) {
                color = ((Object[]) attr.get(Chunk.BACKGROUND))[0];
                setAttribute(PdfName.BACKGROUNDCOLOR, new PdfArray(new float[]{((float) color.getRed()) / 255.0f, ((float) color.getGreen()) / 255.0f, ((float) color.getBlue()) / 255.0f}));
            }
            IPdfStructureElement parent = (IPdfStructureElement) getParent(true);
            PdfObject obj = parent.getAttribute(PdfName.COLOR);
            if (!(chunk.getFont() == null || chunk.getFont().getColor() == null)) {
                setColorAttribute(chunk.getFont().getColor(), obj, PdfName.COLOR);
            }
            PdfObject decorThickness = parent.getAttribute(PdfName.TEXTDECORATIONTHICKNESS);
            PdfObject decorColor = parent.getAttribute(PdfName.TEXTDECORATIONCOLOR);
            if (attr.containsKey(Chunk.UNDERLINE)) {
                Object[][] unders = (Object[][]) attr.get(Chunk.UNDERLINE);
                Object[] arr = unders[unders.length - 1];
                color = (BaseColor) arr[0];
                float thickness = ((float[]) arr[1])[0];
                if (!(decorThickness instanceof PdfNumber)) {
                    setAttribute(PdfName.TEXTDECORATIONTHICKNESS, new PdfNumber(thickness));
                } else if (Float.compare(thickness, ((PdfNumber) decorThickness).floatValue()) != 0) {
                    setAttribute(PdfName.TEXTDECORATIONTHICKNESS, new PdfNumber(thickness));
                }
                if (color != null) {
                    setColorAttribute(color, decorColor, PdfName.TEXTDECORATIONCOLOR);
                }
            }
            if (attr.containsKey(Chunk.LINEHEIGHT)) {
                float height = ((Float) attr.get(Chunk.LINEHEIGHT)).floatValue();
                PdfObject parentLH = parent.getAttribute(PdfName.LINEHEIGHT);
                if (!(parentLH instanceof PdfNumber)) {
                    setAttribute(PdfName.LINEHEIGHT, new PdfNumber(height));
                } else if (Float.compare(((PdfNumber) parentLH).floatValue(), height) != 0) {
                    setAttribute(PdfName.LINEHEIGHT, new PdfNumber(height));
                }
            }
        }
    }

    private void writeAttributes(Image image) {
        if (image != null) {
            setAttribute(PdfName.f129O, PdfName.LAYOUT);
            if (image.getWidth() > 0.0f) {
                setAttribute(PdfName.WIDTH, new PdfNumber(image.getWidth()));
            }
            if (image.getHeight() > 0.0f) {
                setAttribute(PdfName.HEIGHT, new PdfNumber(image.getHeight()));
            }
            setAttribute(PdfName.BBOX, new PdfRectangle((Rectangle) image, image.getRotation()));
            if (image.getBackgroundColor() != null) {
                BaseColor color = image.getBackgroundColor();
                setAttribute(PdfName.BACKGROUNDCOLOR, new PdfArray(new float[]{((float) color.getRed()) / 255.0f, ((float) color.getGreen()) / 255.0f, ((float) color.getBlue()) / 255.0f}));
            }
        }
    }

    private void writeAttributes(PdfTemplate template) {
        if (template != null) {
            setAttribute(PdfName.f129O, PdfName.LAYOUT);
            if (template.getWidth() > 0.0f) {
                setAttribute(PdfName.WIDTH, new PdfNumber(template.getWidth()));
            }
            if (template.getHeight() > 0.0f) {
                setAttribute(PdfName.HEIGHT, new PdfNumber(template.getHeight()));
            }
            setAttribute(PdfName.BBOX, new PdfRectangle(template.getBoundingBox()));
        }
    }

    private void writeAttributes(Paragraph paragraph) {
        if (paragraph != null) {
            setAttribute(PdfName.f129O, PdfName.LAYOUT);
            if (Float.compare(paragraph.getSpacingBefore(), 0.0f) != 0) {
                setAttribute(PdfName.SPACEBEFORE, new PdfNumber(paragraph.getSpacingBefore()));
            }
            if (Float.compare(paragraph.getSpacingAfter(), 0.0f) != 0) {
                setAttribute(PdfName.SPACEAFTER, new PdfNumber(paragraph.getSpacingAfter()));
            }
            IPdfStructureElement parent = (IPdfStructureElement) getParent(true);
            PdfObject obj = parent.getAttribute(PdfName.COLOR);
            if (!(paragraph.getFont() == null || paragraph.getFont().getColor() == null)) {
                setColorAttribute(paragraph.getFont().getColor(), obj, PdfName.COLOR);
            }
            obj = parent.getAttribute(PdfName.TEXTINDENT);
            if (Float.compare(paragraph.getFirstLineIndent(), 0.0f) != 0) {
                boolean writeIndent = true;
                if ((obj instanceof PdfNumber) && Float.compare(((PdfNumber) obj).floatValue(), new Float(paragraph.getFirstLineIndent()).floatValue()) == 0) {
                    writeIndent = false;
                }
                if (writeIndent) {
                    setAttribute(PdfName.TEXTINDENT, new PdfNumber(paragraph.getFirstLineIndent()));
                }
            }
            obj = parent.getAttribute(PdfName.STARTINDENT);
            if (obj instanceof PdfNumber) {
                if (Float.compare(((PdfNumber) obj).floatValue(), paragraph.getIndentationLeft()) != 0) {
                    setAttribute(PdfName.STARTINDENT, new PdfNumber(paragraph.getIndentationLeft()));
                }
            } else if (Math.abs(paragraph.getIndentationLeft()) > Float.MIN_VALUE) {
                setAttribute(PdfName.STARTINDENT, new PdfNumber(paragraph.getIndentationLeft()));
            }
            obj = parent.getAttribute(PdfName.ENDINDENT);
            if (obj instanceof PdfNumber) {
                if (Float.compare(((PdfNumber) obj).floatValue(), paragraph.getIndentationRight()) != 0) {
                    setAttribute(PdfName.ENDINDENT, new PdfNumber(paragraph.getIndentationRight()));
                }
            } else if (Float.compare(paragraph.getIndentationRight(), 0.0f) != 0) {
                setAttribute(PdfName.ENDINDENT, new PdfNumber(paragraph.getIndentationRight()));
            }
            setTextAlignAttribute(paragraph.getAlignment());
        }
    }

    private void writeAttributes(List list) {
        if (list != null) {
            setAttribute(PdfName.f129O, PdfName.LIST);
            if (list.isAutoindent()) {
                if (list.isNumbered()) {
                    if (!list.isLettered()) {
                        setAttribute(PdfName.LISTNUMBERING, PdfName.DECIMAL);
                    } else if (list.isLowercase()) {
                        setAttribute(PdfName.LISTNUMBERING, PdfName.LOWERROMAN);
                    } else {
                        setAttribute(PdfName.LISTNUMBERING, PdfName.UPPERROMAN);
                    }
                } else if (list.isLettered()) {
                    if (list.isLowercase()) {
                        setAttribute(PdfName.LISTNUMBERING, PdfName.LOWERALPHA);
                    } else {
                        setAttribute(PdfName.LISTNUMBERING, PdfName.UPPERALPHA);
                    }
                }
            }
            PdfObject obj = this.parent.getAttribute(PdfName.STARTINDENT);
            if (obj instanceof PdfNumber) {
                if (Float.compare(((PdfNumber) obj).floatValue(), list.getIndentationLeft()) != 0) {
                    setAttribute(PdfName.STARTINDENT, new PdfNumber(list.getIndentationLeft()));
                }
            } else if (Math.abs(list.getIndentationLeft()) > Float.MIN_VALUE) {
                setAttribute(PdfName.STARTINDENT, new PdfNumber(list.getIndentationLeft()));
            }
            obj = this.parent.getAttribute(PdfName.ENDINDENT);
            if (obj instanceof PdfNumber) {
                if (Float.compare(((PdfNumber) obj).floatValue(), list.getIndentationRight()) != 0) {
                    setAttribute(PdfName.ENDINDENT, new PdfNumber(list.getIndentationRight()));
                }
            } else if (Float.compare(list.getIndentationRight(), 0.0f) != 0) {
                setAttribute(PdfName.ENDINDENT, new PdfNumber(list.getIndentationRight()));
            }
        }
    }

    private void writeAttributes(ListItem listItem) {
        if (listItem != null) {
            PdfObject obj = this.parent.getAttribute(PdfName.STARTINDENT);
            if (obj instanceof PdfNumber) {
                if (Float.compare(((PdfNumber) obj).floatValue(), listItem.getIndentationLeft()) != 0) {
                    setAttribute(PdfName.STARTINDENT, new PdfNumber(listItem.getIndentationLeft()));
                }
            } else if (Math.abs(listItem.getIndentationLeft()) > Float.MIN_VALUE) {
                setAttribute(PdfName.STARTINDENT, new PdfNumber(listItem.getIndentationLeft()));
            }
            obj = this.parent.getAttribute(PdfName.ENDINDENT);
            if (obj instanceof PdfNumber) {
                if (Float.compare(((PdfNumber) obj).floatValue(), listItem.getIndentationRight()) != 0) {
                    setAttribute(PdfName.ENDINDENT, new PdfNumber(listItem.getIndentationRight()));
                }
            } else if (Float.compare(listItem.getIndentationRight(), 0.0f) != 0) {
                setAttribute(PdfName.ENDINDENT, new PdfNumber(listItem.getIndentationRight()));
            }
        }
    }

    private void writeAttributes(ListBody listBody) {
        if (listBody == null) {
        }
    }

    private void writeAttributes(ListLabel listLabel) {
        if (listLabel != null) {
            PdfObject obj = this.parent.getAttribute(PdfName.STARTINDENT);
            if (obj instanceof PdfNumber) {
                if (Float.compare(((PdfNumber) obj).floatValue(), listLabel.getIndentation()) != 0) {
                    setAttribute(PdfName.STARTINDENT, new PdfNumber(listLabel.getIndentation()));
                }
            } else if (Math.abs(listLabel.getIndentation()) > Float.MIN_VALUE) {
                setAttribute(PdfName.STARTINDENT, new PdfNumber(listLabel.getIndentation()));
            }
        }
    }

    private void writeAttributes(PdfPTable table) {
        if (table != null) {
            setAttribute(PdfName.f129O, PdfName.TABLE);
            if (Float.compare(table.getSpacingBefore(), 0.0f) != 0) {
                setAttribute(PdfName.SPACEBEFORE, new PdfNumber(table.getSpacingBefore()));
            }
            if (Float.compare(table.getSpacingAfter(), 0.0f) != 0) {
                setAttribute(PdfName.SPACEAFTER, new PdfNumber(table.getSpacingAfter()));
            }
            if (table.getTotalHeight() > 0.0f) {
                setAttribute(PdfName.HEIGHT, new PdfNumber(table.getTotalHeight()));
            }
            if (table.getTotalWidth() > 0.0f) {
                setAttribute(PdfName.WIDTH, new PdfNumber(table.getTotalWidth()));
            }
            if (table.getSummary() != null) {
                setAttribute(PdfName.SUMMARY, new PdfString(table.getSummary()));
            }
        }
    }

    private void writeAttributes(PdfPRow row) {
        if (row != null) {
            setAttribute(PdfName.f129O, PdfName.TABLE);
        }
    }

    private void writeAttributes(PdfPCell cell) {
        if (cell != null) {
            setAttribute(PdfName.f129O, PdfName.TABLE);
            if (cell.getColspan() != 1) {
                setAttribute(PdfName.COLSPAN, new PdfNumber(cell.getColspan()));
            }
            if (cell.getRowspan() != 1) {
                setAttribute(PdfName.ROWSPAN, new PdfNumber(cell.getRowspan()));
            }
            if (cell.getHeaders() != null) {
                PdfArray headers = new PdfArray();
                Iterator i$ = cell.getHeaders().iterator();
                while (i$.hasNext()) {
                    PdfPHeaderCell header = (PdfPHeaderCell) i$.next();
                    if (header.getName() != null) {
                        headers.add(new PdfString(header.getName()));
                    }
                }
                if (!headers.isEmpty()) {
                    setAttribute(PdfName.HEADERS, headers);
                }
            }
            if (cell.getFixedHeight() > 0.0f) {
                setAttribute(PdfName.HEIGHT, new PdfNumber(cell.getFixedHeight()));
            }
            if (cell.getWidth() > 0.0f) {
                setAttribute(PdfName.WIDTH, new PdfNumber(cell.getWidth()));
            }
            if (cell.getBackgroundColor() != null) {
                BaseColor color = cell.getBackgroundColor();
                setAttribute(PdfName.BACKGROUNDCOLOR, new PdfArray(new float[]{((float) color.getRed()) / 255.0f, ((float) color.getGreen()) / 255.0f, ((float) color.getBlue()) / 255.0f}));
            }
        }
    }

    private void writeAttributes(PdfPHeaderCell headerCell) {
        if (headerCell != null) {
            if (headerCell.getScope() != 0) {
                switch (headerCell.getScope()) {
                    case 1:
                        setAttribute(PdfName.SCOPE, PdfName.ROW);
                        break;
                    case 2:
                        setAttribute(PdfName.SCOPE, PdfName.COLUMN);
                        break;
                    case 3:
                        setAttribute(PdfName.SCOPE, PdfName.BOTH);
                        break;
                }
            }
            if (headerCell.getName() != null) {
                setAttribute(PdfName.NAME, new PdfName(headerCell.getName()));
            }
            writeAttributes((PdfPCell) headerCell);
        }
    }

    private void writeAttributes(PdfPTableHeader header) {
        if (header != null) {
            setAttribute(PdfName.f129O, PdfName.TABLE);
        }
    }

    private void writeAttributes(PdfPTableBody body) {
        if (body == null) {
        }
    }

    private void writeAttributes(PdfPTableFooter footer) {
        if (footer == null) {
        }
    }

    private void writeAttributes(PdfDiv div) {
        if (div != null) {
            if (div.getBackgroundColor() != null) {
                setColorAttribute(div.getBackgroundColor(), null, PdfName.BACKGROUNDCOLOR);
            }
            setTextAlignAttribute(div.getTextAlignment());
        }
    }

    private void writeAttributes(Document document) {
        if (document == null) {
        }
    }

    private boolean colorsEqual(PdfArray parentColor, float[] color) {
        if (Float.compare(color[0], parentColor.getAsNumber(0).floatValue()) == 0 && Float.compare(color[1], parentColor.getAsNumber(1).floatValue()) == 0 && Float.compare(color[2], parentColor.getAsNumber(2).floatValue()) == 0) {
            return true;
        }
        return false;
    }

    private void setColorAttribute(BaseColor newColor, PdfObject oldColor, PdfName attributeName) {
        float[] colorArr = new float[]{((float) newColor.getRed()) / 255.0f, ((float) newColor.getGreen()) / 255.0f, ((float) newColor.getBlue()) / 255.0f};
        if (oldColor == null || !(oldColor instanceof PdfArray)) {
            setAttribute(attributeName, new PdfArray(colorArr));
        } else if (colorsEqual((PdfArray) oldColor, colorArr)) {
            setAttribute(attributeName, new PdfArray(colorArr));
        } else {
            setAttribute(attributeName, new PdfArray(colorArr));
        }
    }

    private void setTextAlignAttribute(int elementAlign) {
        PdfName align = null;
        switch (elementAlign) {
            case 0:
                align = PdfName.START;
                break;
            case 1:
                align = PdfName.CENTER;
                break;
            case 2:
                align = PdfName.END;
                break;
            case 3:
                align = PdfName.JUSTIFY;
                break;
        }
        PdfObject obj = this.parent.getAttribute(PdfName.TEXTALIGN);
        if (obj instanceof PdfName) {
            PdfName textAlign = (PdfName) obj;
            if (align != null && !textAlign.equals(align)) {
                setAttribute(PdfName.TEXTALIGN, align);
            }
        } else if (align != null && !PdfName.START.equals(align)) {
            setAttribute(PdfName.TEXTALIGN, align);
        }
    }

    public void toPdf(PdfWriter writer, OutputStream os) throws IOException {
        PdfWriter.checkPdfIsoConformance(writer, 16, this);
        super.toPdf(writer, os);
    }
}

package com.itextpdf.text.pdf;

import com.itextpdf.text.AccessibleElementId;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.events.PdfPCellEventForwarder;
import com.itextpdf.text.pdf.interfaces.IAccessibleElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.bytedeco.javacpp.dc1394;

public class PdfPCell extends Rectangle implements IAccessibleElement {
    protected HashMap<PdfName, PdfObject> accessibleAttributes;
    private PdfPCellEvent cellEvent;
    private int colspan;
    private ColumnText column;
    private float fixedHeight;
    protected ArrayList<PdfPHeaderCell> headers;
    protected AccessibleElementId id;
    private Image image;
    private float minimumHeight;
    private boolean noWrap;
    private float paddingBottom;
    private float paddingLeft;
    private float paddingRight;
    private float paddingTop;
    protected Phrase phrase;
    protected PdfName role;
    private int rotation;
    private int rowspan;
    private PdfPTable table;
    private boolean useBorderPadding;
    private boolean useDescender;
    private int verticalAlignment;

    public PdfPCell() {
        super(0.0f, 0.0f, 0.0f, 0.0f);
        this.column = new ColumnText(null);
        this.verticalAlignment = 4;
        this.paddingLeft = BaseField.BORDER_WIDTH_MEDIUM;
        this.paddingRight = BaseField.BORDER_WIDTH_MEDIUM;
        this.paddingTop = BaseField.BORDER_WIDTH_MEDIUM;
        this.paddingBottom = BaseField.BORDER_WIDTH_MEDIUM;
        this.fixedHeight = 0.0f;
        this.noWrap = false;
        this.colspan = 1;
        this.rowspan = 1;
        this.useDescender = false;
        this.useBorderPadding = false;
        this.role = PdfName.TD;
        this.accessibleAttributes = null;
        this.id = new AccessibleElementId();
        this.headers = null;
        this.borderWidth = 0.5f;
        this.border = 15;
        this.column.setLeading(0.0f, BaseField.BORDER_WIDTH_THIN);
    }

    public PdfPCell(Phrase phrase) {
        super(0.0f, 0.0f, 0.0f, 0.0f);
        this.column = new ColumnText(null);
        this.verticalAlignment = 4;
        this.paddingLeft = BaseField.BORDER_WIDTH_MEDIUM;
        this.paddingRight = BaseField.BORDER_WIDTH_MEDIUM;
        this.paddingTop = BaseField.BORDER_WIDTH_MEDIUM;
        this.paddingBottom = BaseField.BORDER_WIDTH_MEDIUM;
        this.fixedHeight = 0.0f;
        this.noWrap = false;
        this.colspan = 1;
        this.rowspan = 1;
        this.useDescender = false;
        this.useBorderPadding = false;
        this.role = PdfName.TD;
        this.accessibleAttributes = null;
        this.id = new AccessibleElementId();
        this.headers = null;
        this.borderWidth = 0.5f;
        this.border = 15;
        ColumnText columnText = this.column;
        this.phrase = phrase;
        columnText.addText(phrase);
        this.column.setLeading(0.0f, BaseField.BORDER_WIDTH_THIN);
    }

    public PdfPCell(Image image) {
        this(image, false);
    }

    public PdfPCell(Image image, boolean fit) {
        super(0.0f, 0.0f, 0.0f, 0.0f);
        this.column = new ColumnText(null);
        this.verticalAlignment = 4;
        this.paddingLeft = BaseField.BORDER_WIDTH_MEDIUM;
        this.paddingRight = BaseField.BORDER_WIDTH_MEDIUM;
        this.paddingTop = BaseField.BORDER_WIDTH_MEDIUM;
        this.paddingBottom = BaseField.BORDER_WIDTH_MEDIUM;
        this.fixedHeight = 0.0f;
        this.noWrap = false;
        this.colspan = 1;
        this.rowspan = 1;
        this.useDescender = false;
        this.useBorderPadding = false;
        this.role = PdfName.TD;
        this.accessibleAttributes = null;
        this.id = new AccessibleElementId();
        this.headers = null;
        this.borderWidth = 0.5f;
        this.border = 15;
        this.column.setLeading(0.0f, BaseField.BORDER_WIDTH_THIN);
        if (fit) {
            this.image = image;
            setPadding(this.borderWidth / BaseField.BORDER_WIDTH_MEDIUM);
            return;
        }
        image.setScaleToFitLineWhenOverflow(false);
        ColumnText columnText = this.column;
        Phrase phrase = new Phrase(new Chunk(image, 0.0f, 0.0f, true));
        this.phrase = phrase;
        columnText.addText(phrase);
        setPadding(0.0f);
    }

    public PdfPCell(PdfPTable table) {
        this(table, null);
    }

    public PdfPCell(PdfPTable table, PdfPCell style) {
        super(0.0f, 0.0f, 0.0f, 0.0f);
        this.column = new ColumnText(null);
        this.verticalAlignment = 4;
        this.paddingLeft = BaseField.BORDER_WIDTH_MEDIUM;
        this.paddingRight = BaseField.BORDER_WIDTH_MEDIUM;
        this.paddingTop = BaseField.BORDER_WIDTH_MEDIUM;
        this.paddingBottom = BaseField.BORDER_WIDTH_MEDIUM;
        this.fixedHeight = 0.0f;
        this.noWrap = false;
        this.colspan = 1;
        this.rowspan = 1;
        this.useDescender = false;
        this.useBorderPadding = false;
        this.role = PdfName.TD;
        this.accessibleAttributes = null;
        this.id = new AccessibleElementId();
        this.headers = null;
        this.borderWidth = 0.5f;
        this.border = 15;
        this.column.setLeading(0.0f, BaseField.BORDER_WIDTH_THIN);
        this.table = table;
        table.setWidthPercentage(100.0f);
        table.setExtendLastRow(true);
        this.column.addElement(table);
        if (style != null) {
            cloneNonPositionParameters(style);
            this.verticalAlignment = style.verticalAlignment;
            this.paddingLeft = style.paddingLeft;
            this.paddingRight = style.paddingRight;
            this.paddingTop = style.paddingTop;
            this.paddingBottom = style.paddingBottom;
            this.colspan = style.colspan;
            this.rowspan = style.rowspan;
            this.cellEvent = style.cellEvent;
            this.useDescender = style.useDescender;
            this.useBorderPadding = style.useBorderPadding;
            this.rotation = style.rotation;
            return;
        }
        setPadding(0.0f);
    }

    public PdfPCell(PdfPCell cell) {
        super(cell.llx, cell.lly, cell.urx, cell.ury);
        this.column = new ColumnText(null);
        this.verticalAlignment = 4;
        this.paddingLeft = BaseField.BORDER_WIDTH_MEDIUM;
        this.paddingRight = BaseField.BORDER_WIDTH_MEDIUM;
        this.paddingTop = BaseField.BORDER_WIDTH_MEDIUM;
        this.paddingBottom = BaseField.BORDER_WIDTH_MEDIUM;
        this.fixedHeight = 0.0f;
        this.noWrap = false;
        this.colspan = 1;
        this.rowspan = 1;
        this.useDescender = false;
        this.useBorderPadding = false;
        this.role = PdfName.TD;
        this.accessibleAttributes = null;
        this.id = new AccessibleElementId();
        this.headers = null;
        cloneNonPositionParameters(cell);
        this.verticalAlignment = cell.verticalAlignment;
        this.paddingLeft = cell.paddingLeft;
        this.paddingRight = cell.paddingRight;
        this.paddingTop = cell.paddingTop;
        this.paddingBottom = cell.paddingBottom;
        this.phrase = cell.phrase;
        this.fixedHeight = cell.fixedHeight;
        this.minimumHeight = cell.minimumHeight;
        this.noWrap = cell.noWrap;
        this.colspan = cell.colspan;
        this.rowspan = cell.rowspan;
        if (cell.table != null) {
            this.table = new PdfPTable(cell.table);
        }
        this.image = Image.getInstance(cell.image);
        this.cellEvent = cell.cellEvent;
        this.useDescender = cell.useDescender;
        this.column = ColumnText.duplicate(cell.column);
        this.useBorderPadding = cell.useBorderPadding;
        this.rotation = cell.rotation;
        this.id = cell.id;
        this.role = cell.role;
        if (cell.accessibleAttributes != null) {
            this.accessibleAttributes = new HashMap(cell.accessibleAttributes);
        }
        this.headers = cell.headers;
    }

    public void addElement(Element element) {
        if (this.table != null) {
            this.table = null;
            this.column.setText(null);
        }
        if (element instanceof PdfPTable) {
            ((PdfPTable) element).setSplitLate(false);
        } else if (element instanceof PdfDiv) {
            Iterator i$ = ((PdfDiv) element).getContent().iterator();
            while (i$.hasNext()) {
                Element divChildElement = (Element) i$.next();
                if (divChildElement instanceof PdfPTable) {
                    ((PdfPTable) divChildElement).setSplitLate(false);
                }
            }
        }
        this.column.addElement(element);
    }

    public Phrase getPhrase() {
        return this.phrase;
    }

    public void setPhrase(Phrase phrase) {
        this.table = null;
        this.image = null;
        ColumnText columnText = this.column;
        this.phrase = phrase;
        columnText.setText(phrase);
    }

    public int getHorizontalAlignment() {
        return this.column.getAlignment();
    }

    public void setHorizontalAlignment(int horizontalAlignment) {
        this.column.setAlignment(horizontalAlignment);
    }

    public int getVerticalAlignment() {
        return this.verticalAlignment;
    }

    public void setVerticalAlignment(int verticalAlignment) {
        if (this.table != null) {
            this.table.setExtendLastRow(verticalAlignment == 4);
        }
        this.verticalAlignment = verticalAlignment;
    }

    public float getEffectivePaddingLeft() {
        if (!isUseBorderPadding()) {
            return this.paddingLeft;
        }
        return this.paddingLeft + (getBorderWidthLeft() / (isUseVariableBorders() ? BaseField.BORDER_WIDTH_THIN : BaseField.BORDER_WIDTH_MEDIUM));
    }

    public float getPaddingLeft() {
        return this.paddingLeft;
    }

    public void setPaddingLeft(float paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public float getEffectivePaddingRight() {
        if (!isUseBorderPadding()) {
            return this.paddingRight;
        }
        return this.paddingRight + (getBorderWidthRight() / (isUseVariableBorders() ? BaseField.BORDER_WIDTH_THIN : BaseField.BORDER_WIDTH_MEDIUM));
    }

    public float getPaddingRight() {
        return this.paddingRight;
    }

    public void setPaddingRight(float paddingRight) {
        this.paddingRight = paddingRight;
    }

    public float getEffectivePaddingTop() {
        if (!isUseBorderPadding()) {
            return this.paddingTop;
        }
        return this.paddingTop + (getBorderWidthTop() / (isUseVariableBorders() ? BaseField.BORDER_WIDTH_THIN : BaseField.BORDER_WIDTH_MEDIUM));
    }

    public float getPaddingTop() {
        return this.paddingTop;
    }

    public void setPaddingTop(float paddingTop) {
        this.paddingTop = paddingTop;
    }

    public float getEffectivePaddingBottom() {
        if (!isUseBorderPadding()) {
            return this.paddingBottom;
        }
        return this.paddingBottom + (getBorderWidthBottom() / (isUseVariableBorders() ? BaseField.BORDER_WIDTH_THIN : BaseField.BORDER_WIDTH_MEDIUM));
    }

    public float getPaddingBottom() {
        return this.paddingBottom;
    }

    public void setPaddingBottom(float paddingBottom) {
        this.paddingBottom = paddingBottom;
    }

    public void setPadding(float padding) {
        this.paddingBottom = padding;
        this.paddingTop = padding;
        this.paddingLeft = padding;
        this.paddingRight = padding;
    }

    public boolean isUseBorderPadding() {
        return this.useBorderPadding;
    }

    public void setUseBorderPadding(boolean use) {
        this.useBorderPadding = use;
    }

    public void setLeading(float fixedLeading, float multipliedLeading) {
        this.column.setLeading(fixedLeading, multipliedLeading);
    }

    public float getLeading() {
        return this.column.getLeading();
    }

    public float getMultipliedLeading() {
        return this.column.getMultipliedLeading();
    }

    public void setIndent(float indent) {
        this.column.setIndent(indent);
    }

    public float getIndent() {
        return this.column.getIndent();
    }

    public float getExtraParagraphSpace() {
        return this.column.getExtraParagraphSpace();
    }

    public void setExtraParagraphSpace(float extraParagraphSpace) {
        this.column.setExtraParagraphSpace(extraParagraphSpace);
    }

    public void setFixedHeight(float fixedHeight) {
        this.fixedHeight = fixedHeight;
        this.minimumHeight = 0.0f;
    }

    public float getFixedHeight() {
        return this.fixedHeight;
    }

    public boolean hasFixedHeight() {
        return getFixedHeight() > 0.0f;
    }

    public void setMinimumHeight(float minimumHeight) {
        this.minimumHeight = minimumHeight;
        this.fixedHeight = 0.0f;
    }

    public float getMinimumHeight() {
        return this.minimumHeight;
    }

    public boolean hasMinimumHeight() {
        return getMinimumHeight() > 0.0f;
    }

    public boolean isNoWrap() {
        return this.noWrap;
    }

    public void setNoWrap(boolean noWrap) {
        this.noWrap = noWrap;
    }

    public PdfPTable getTable() {
        return this.table;
    }

    void setTable(PdfPTable table) {
        this.table = table;
        this.column.setText(null);
        this.image = null;
        if (table != null) {
            table.setExtendLastRow(this.verticalAlignment == 4);
            this.column.addElement(table);
            table.setWidthPercentage(100.0f);
        }
    }

    public int getColspan() {
        return this.colspan;
    }

    public void setColspan(int colspan) {
        this.colspan = colspan;
    }

    public int getRowspan() {
        return this.rowspan;
    }

    public void setRowspan(int rowspan) {
        this.rowspan = rowspan;
    }

    public void setFollowingIndent(float indent) {
        this.column.setFollowingIndent(indent);
    }

    public float getFollowingIndent() {
        return this.column.getFollowingIndent();
    }

    public void setRightIndent(float indent) {
        this.column.setRightIndent(indent);
    }

    public float getRightIndent() {
        return this.column.getRightIndent();
    }

    public float getSpaceCharRatio() {
        return this.column.getSpaceCharRatio();
    }

    public void setSpaceCharRatio(float spaceCharRatio) {
        this.column.setSpaceCharRatio(spaceCharRatio);
    }

    public void setRunDirection(int runDirection) {
        this.column.setRunDirection(runDirection);
    }

    public int getRunDirection() {
        return this.column.getRunDirection();
    }

    public Image getImage() {
        return this.image;
    }

    public void setImage(Image image) {
        this.column.setText(null);
        this.table = null;
        this.image = image;
    }

    public PdfPCellEvent getCellEvent() {
        return this.cellEvent;
    }

    public void setCellEvent(PdfPCellEvent cellEvent) {
        if (cellEvent == null) {
            this.cellEvent = null;
        } else if (this.cellEvent == null) {
            this.cellEvent = cellEvent;
        } else if (this.cellEvent instanceof PdfPCellEventForwarder) {
            ((PdfPCellEventForwarder) this.cellEvent).addCellEvent(cellEvent);
        } else {
            PdfPCellEventForwarder forward = new PdfPCellEventForwarder();
            forward.addCellEvent(this.cellEvent);
            forward.addCellEvent(cellEvent);
            this.cellEvent = forward;
        }
    }

    public int getArabicOptions() {
        return this.column.getArabicOptions();
    }

    public void setArabicOptions(int arabicOptions) {
        this.column.setArabicOptions(arabicOptions);
    }

    public boolean isUseAscender() {
        return this.column.isUseAscender();
    }

    public void setUseAscender(boolean useAscender) {
        this.column.setUseAscender(useAscender);
    }

    public boolean isUseDescender() {
        return this.useDescender;
    }

    public void setUseDescender(boolean useDescender) {
        this.useDescender = useDescender;
    }

    public ColumnText getColumn() {
        return this.column;
    }

    public List<Element> getCompositeElements() {
        return getColumn().compositeElements;
    }

    public void setColumn(ColumnText column) {
        this.column = column;
    }

    public int getRotation() {
        return this.rotation;
    }

    public void setRotation(int rotation) {
        rotation %= dc1394.DC1394_COLOR_CODING_RGB16S;
        if (rotation < 0) {
            rotation += dc1394.DC1394_COLOR_CODING_RGB16S;
        }
        if (rotation % 90 != 0) {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("rotation.must.be.a.multiple.of.90", new Object[0]));
        }
        this.rotation = rotation;
    }

    public float getMaxHeight() {
        boolean pivoted = getRotation() == 90 || getRotation() == 270;
        Image img = getImage();
        if (img != null) {
            img.scalePercent(100.0f);
            img.scalePercent(100.0f * ((((getRight() - getEffectivePaddingRight()) - getEffectivePaddingLeft()) - getLeft()) / (pivoted ? img.getScaledHeight() : img.getScaledWidth())));
            setBottom(((getTop() - getEffectivePaddingTop()) - getEffectivePaddingBottom()) - (pivoted ? img.getScaledWidth() : img.getScaledHeight()));
        } else if ((pivoted && hasFixedHeight()) || getColumn() == null) {
            setBottom(getTop() - getFixedHeight());
        } else {
            float right;
            float top;
            float left;
            float bottom;
            ColumnText ct = ColumnText.duplicate(getColumn());
            if (pivoted) {
                right = PdfPRow.RIGHT_LIMIT;
                top = getRight() - getEffectivePaddingRight();
                left = 0.0f;
                bottom = getLeft() + getEffectivePaddingLeft();
            } else {
                right = isNoWrap() ? PdfPRow.RIGHT_LIMIT : getRight() - getEffectivePaddingRight();
                top = getTop() - getEffectivePaddingTop();
                left = getLeft() + getEffectivePaddingLeft();
                bottom = hasFixedHeight() ? (getTop() + getEffectivePaddingBottom()) - getFixedHeight() : PdfPRow.BOTTOM_LIMIT;
            }
            PdfPRow.setColumn(ct, left, bottom, right, top);
            try {
                ct.go(true);
                if (pivoted) {
                    setBottom(((getTop() - getEffectivePaddingTop()) - getEffectivePaddingBottom()) - ct.getFilledWidth());
                } else {
                    float yLine = ct.getYLine();
                    if (isUseDescender()) {
                        yLine += ct.getDescender();
                    }
                    setBottom(yLine - getEffectivePaddingBottom());
                }
            } catch (DocumentException e) {
                throw new ExceptionConverter(e);
            }
        }
        float height = getHeight();
        if (height == getEffectivePaddingTop() + getEffectivePaddingBottom()) {
            height = 0.0f;
        }
        if (hasFixedHeight()) {
            return getFixedHeight();
        }
        if (!hasMinimumHeight() || height >= getMinimumHeight()) {
            return height;
        }
        return getMinimumHeight();
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
        return this.id;
    }

    public void setId(AccessibleElementId id) {
        this.id = id;
    }

    public boolean isInline() {
        return false;
    }

    public void addHeader(PdfPHeaderCell header) {
        if (this.headers == null) {
            this.headers = new ArrayList();
        }
        this.headers.add(header);
    }

    public ArrayList<PdfPHeaderCell> getHeaders() {
        return this.headers;
    }
}

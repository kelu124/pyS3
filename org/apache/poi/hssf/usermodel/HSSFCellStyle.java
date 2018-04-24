package org.apache.poi.hssf.usermodel;

import com.lee.ultrascan.library.ProbeParams;
import java.util.List;
import org.apache.poi.hssf.model.InternalWorkbook;
import org.apache.poi.hssf.record.EscherAggregate;
import org.apache.poi.hssf.record.ExtendedFormatRecord;
import org.apache.poi.hssf.record.FontRecord;
import org.apache.poi.hssf.record.FormatRecord;
import org.apache.poi.hssf.record.StyleRecord;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.Removal;

public final class HSSFCellStyle implements CellStyle {
    private static final ThreadLocal<String> getDataFormatStringCache = new ThreadLocal();
    private static final ThreadLocal<Short> lastDateFormat = new 1();
    private static final ThreadLocal<List<FormatRecord>> lastFormats = new ThreadLocal();
    private final ExtendedFormatRecord _format;
    private final short _index;
    private final InternalWorkbook _workbook;

    protected HSSFCellStyle(short index, ExtendedFormatRecord rec, HSSFWorkbook workbook) {
        this(index, rec, workbook.getWorkbook());
    }

    protected HSSFCellStyle(short index, ExtendedFormatRecord rec, InternalWorkbook workbook) {
        this._workbook = workbook;
        this._index = index;
        this._format = rec;
    }

    public short getIndex() {
        return this._index;
    }

    public HSSFCellStyle getParentStyle() {
        short parentIndex = this._format.getParentIndex();
        if (parentIndex == (short) 0 || parentIndex == EscherAggregate.ST_NIL) {
            return null;
        }
        return new HSSFCellStyle(parentIndex, this._workbook.getExFormatAt(parentIndex), this._workbook);
    }

    public void setDataFormat(short fmt) {
        this._format.setFormatIndex(fmt);
    }

    public short getDataFormat() {
        return this._format.getFormatIndex();
    }

    public String getDataFormatString() {
        if (getDataFormatStringCache.get() != null && ((Short) lastDateFormat.get()).shortValue() == getDataFormat() && this._workbook.getFormats().equals(lastFormats.get())) {
            return (String) getDataFormatStringCache.get();
        }
        lastFormats.set(this._workbook.getFormats());
        lastDateFormat.set(Short.valueOf(getDataFormat()));
        getDataFormatStringCache.set(getDataFormatString(this._workbook));
        return (String) getDataFormatStringCache.get();
    }

    public String getDataFormatString(Workbook workbook) {
        return getDataFormat() == -1 ? "General" : new HSSFDataFormat(((HSSFWorkbook) workbook).getWorkbook()).getFormat(getDataFormat());
    }

    public String getDataFormatString(InternalWorkbook workbook) {
        return new HSSFDataFormat(workbook).getFormat(getDataFormat());
    }

    public void setFont(Font font) {
        setFont((HSSFFont) font);
    }

    public void setFont(HSSFFont font) {
        this._format.setIndentNotParentFont(true);
        this._format.setFontIndex(font.getIndex());
    }

    public short getFontIndex() {
        return this._format.getFontIndex();
    }

    public HSSFFont getFont(Workbook parentWorkbook) {
        return ((HSSFWorkbook) parentWorkbook).getFontAt(getFontIndex());
    }

    public void setHidden(boolean hidden) {
        this._format.setIndentNotParentCellOptions(true);
        this._format.setHidden(hidden);
    }

    public boolean getHidden() {
        return this._format.isHidden();
    }

    public void setLocked(boolean locked) {
        this._format.setIndentNotParentCellOptions(true);
        this._format.setLocked(locked);
    }

    public boolean getLocked() {
        return this._format.isLocked();
    }

    @Removal(version = "3.17")
    public void setAlignment(short align) {
        this._format.setIndentNotParentAlignment(true);
        this._format.setAlignment(align);
    }

    public void setAlignment(HorizontalAlignment align) {
        this._format.setIndentNotParentAlignment(true);
        this._format.setAlignment(align.getCode());
    }

    public short getAlignment() {
        return this._format.getAlignment();
    }

    public HorizontalAlignment getAlignmentEnum() {
        return HorizontalAlignment.forInt(this._format.getAlignment());
    }

    public void setWrapText(boolean wrapped) {
        this._format.setIndentNotParentAlignment(true);
        this._format.setWrapText(wrapped);
    }

    public boolean getWrapText() {
        return this._format.getWrapText();
    }

    @Removal(version = "3.17")
    public void setVerticalAlignment(short align) {
        this._format.setVerticalAlignment(align);
    }

    public void setVerticalAlignment(VerticalAlignment align) {
        this._format.setVerticalAlignment(align.getCode());
    }

    public short getVerticalAlignment() {
        return this._format.getVerticalAlignment();
    }

    public VerticalAlignment getVerticalAlignmentEnum() {
        return VerticalAlignment.forInt(this._format.getVerticalAlignment());
    }

    public void setRotation(short rotation) {
        if (rotation != (short) 255) {
            if (rotation < (short) 0 && rotation >= (short) -90) {
                rotation = (short) (90 - rotation);
            } else if ((rotation <= (short) 90 || rotation > EscherAggregate.ST_BORDERCALLOUT90) && (rotation < (short) -90 || rotation > (short) 90)) {
                throw new IllegalArgumentException("The rotation must be between -90 and 90 degrees, or 0xff");
            }
        }
        this._format.setRotation(rotation);
    }

    public short getRotation() {
        short rotation = this._format.getRotation();
        if (rotation == (short) 255) {
            return rotation;
        }
        if (rotation > (short) 90) {
            rotation = (short) (90 - rotation);
        }
        return rotation;
    }

    public void setIndention(short indent) {
        this._format.setIndent(indent);
    }

    public short getIndention() {
        return this._format.getIndent();
    }

    @Removal(version = "3.17")
    public void setBorderLeft(short border) {
        this._format.setIndentNotParentBorder(true);
        this._format.setBorderLeft(border);
    }

    public void setBorderLeft(BorderStyle border) {
        setBorderLeft(border.getCode());
    }

    public short getBorderLeft() {
        return this._format.getBorderLeft();
    }

    public BorderStyle getBorderLeftEnum() {
        return BorderStyle.valueOf(this._format.getBorderLeft());
    }

    @Removal(version = "3.17")
    public void setBorderRight(short border) {
        this._format.setIndentNotParentBorder(true);
        this._format.setBorderRight(border);
    }

    public void setBorderRight(BorderStyle border) {
        setBorderRight(border.getCode());
    }

    public short getBorderRight() {
        return this._format.getBorderRight();
    }

    public BorderStyle getBorderRightEnum() {
        return BorderStyle.valueOf(this._format.getBorderRight());
    }

    @Removal(version = "3.17")
    public void setBorderTop(short border) {
        this._format.setIndentNotParentBorder(true);
        this._format.setBorderTop(border);
    }

    public void setBorderTop(BorderStyle border) {
        setBorderTop(border.getCode());
    }

    public short getBorderTop() {
        return this._format.getBorderTop();
    }

    public BorderStyle getBorderTopEnum() {
        return BorderStyle.valueOf(this._format.getBorderTop());
    }

    @Removal(version = "3.17")
    public void setBorderBottom(short border) {
        this._format.setIndentNotParentBorder(true);
        this._format.setBorderBottom(border);
    }

    public void setBorderBottom(BorderStyle border) {
        setBorderBottom(border.getCode());
    }

    public short getBorderBottom() {
        return this._format.getBorderBottom();
    }

    public BorderStyle getBorderBottomEnum() {
        return BorderStyle.valueOf(this._format.getBorderBottom());
    }

    public void setLeftBorderColor(short color) {
        this._format.setLeftBorderPaletteIdx(color);
    }

    public short getLeftBorderColor() {
        return this._format.getLeftBorderPaletteIdx();
    }

    public void setRightBorderColor(short color) {
        this._format.setRightBorderPaletteIdx(color);
    }

    public short getRightBorderColor() {
        return this._format.getRightBorderPaletteIdx();
    }

    public void setTopBorderColor(short color) {
        this._format.setTopBorderPaletteIdx(color);
    }

    public short getTopBorderColor() {
        return this._format.getTopBorderPaletteIdx();
    }

    public void setBottomBorderColor(short color) {
        this._format.setBottomBorderPaletteIdx(color);
    }

    public short getBottomBorderColor() {
        return this._format.getBottomBorderPaletteIdx();
    }

    @Removal(version = "3.17")
    public void setFillPattern(short fp) {
        setFillPattern(FillPatternType.forInt(fp));
    }

    public void setFillPattern(FillPatternType fp) {
        this._format.setAdtlFillPattern(fp.getCode());
    }

    public short getFillPattern() {
        return getFillPatternEnum().getCode();
    }

    public FillPatternType getFillPatternEnum() {
        return FillPatternType.forInt(this._format.getAdtlFillPattern());
    }

    private void checkDefaultBackgroundFills() {
        if (this._format.getFillForeground() == (short) 64) {
            if (this._format.getFillBackground() != (short) 65) {
                setFillBackgroundColor((short) 65);
            }
        } else if (this._format.getFillBackground() == (short) 65 && this._format.getFillForeground() != (short) 64) {
            setFillBackgroundColor((short) 64);
        }
    }

    public void setFillBackgroundColor(short bg) {
        this._format.setFillBackground(bg);
        checkDefaultBackgroundFills();
    }

    public short getFillBackgroundColor() {
        short result = this._format.getFillBackground();
        if (result == (short) 65) {
            return (short) 64;
        }
        return result;
    }

    public HSSFColor getFillBackgroundColorColor() {
        return new HSSFPalette(this._workbook.getCustomPalette()).getColor(getFillBackgroundColor());
    }

    public void setFillForegroundColor(short bg) {
        this._format.setFillForeground(bg);
        checkDefaultBackgroundFills();
    }

    public short getFillForegroundColor() {
        return this._format.getFillForeground();
    }

    public HSSFColor getFillForegroundColorColor() {
        return new HSSFPalette(this._workbook.getCustomPalette()).getColor(getFillForegroundColor());
    }

    public String getUserStyleName() {
        StyleRecord sr = this._workbook.getStyleRecord(this._index);
        if (sr == null || sr.isBuiltin()) {
            return null;
        }
        return sr.getName();
    }

    public void setUserStyleName(String styleName) {
        StyleRecord sr = this._workbook.getStyleRecord(this._index);
        if (sr == null) {
            sr = this._workbook.createStyleRecord(this._index);
        }
        if (!sr.isBuiltin() || this._index > (short) 20) {
            sr.setName(styleName);
            return;
        }
        throw new IllegalArgumentException("Unable to set user specified style names for built in styles!");
    }

    public void setShrinkToFit(boolean shrinkToFit) {
        this._format.setShrinkToFit(shrinkToFit);
    }

    public boolean getShrinkToFit() {
        return this._format.getShrinkToFit();
    }

    public short getReadingOrder() {
        return this._format.getReadingOrder();
    }

    public void setReadingOrder(short order) {
        this._format.setReadingOrder(order);
    }

    public void verifyBelongsToWorkbook(HSSFWorkbook wb) {
        if (wb.getWorkbook() != this._workbook) {
            throw new IllegalArgumentException("This Style does not belong to the supplied Workbook. Are you trying to assign a style from one workbook to the cell of a differnt workbook?");
        }
    }

    public void cloneStyleFrom(CellStyle source) {
        if (source instanceof HSSFCellStyle) {
            cloneStyleFrom((HSSFCellStyle) source);
            return;
        }
        throw new IllegalArgumentException("Can only clone from one HSSFCellStyle to another, not between HSSFCellStyle and XSSFCellStyle");
    }

    public void cloneStyleFrom(HSSFCellStyle source) {
        this._format.cloneStyleFrom(source._format);
        if (this._workbook != source._workbook) {
            lastDateFormat.set(Short.valueOf(ProbeParams.BBP_CTL_LOG_INCREMENT));
            lastFormats.set(null);
            getDataFormatStringCache.set(null);
            setDataFormat((short) this._workbook.createFormat(source.getDataFormatString()));
            FontRecord fr = this._workbook.createNewFont();
            fr.cloneStyleFrom(source._workbook.getFontRecordAt(source.getFontIndex()));
            setFont(new HSSFFont((short) this._workbook.getFontIndex(fr), fr));
        }
    }

    public int hashCode() {
        return (((this._format == null ? 0 : this._format.hashCode()) + 31) * 31) + this._index;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof HSSFCellStyle)) {
            return false;
        }
        HSSFCellStyle other = (HSSFCellStyle) obj;
        if (this._format == null) {
            if (other._format != null) {
                return false;
            }
        } else if (!this._format.equals(other._format)) {
            return false;
        }
        if (this._index != other._index) {
            return false;
        }
        return true;
    }
}

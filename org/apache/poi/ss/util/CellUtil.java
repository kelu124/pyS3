package org.apache.poi.ss.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;
import org.apache.poi.util.Removal;

public final class CellUtil {
    public static final String ALIGNMENT = "alignment";
    public static final String BORDER_BOTTOM = "borderBottom";
    public static final String BORDER_LEFT = "borderLeft";
    public static final String BORDER_RIGHT = "borderRight";
    public static final String BORDER_TOP = "borderTop";
    public static final String BOTTOM_BORDER_COLOR = "bottomBorderColor";
    public static final String DATA_FORMAT = "dataFormat";
    public static final String FILL_BACKGROUND_COLOR = "fillBackgroundColor";
    public static final String FILL_FOREGROUND_COLOR = "fillForegroundColor";
    public static final String FILL_PATTERN = "fillPattern";
    public static final String FONT = "font";
    public static final String HIDDEN = "hidden";
    public static final String INDENTION = "indention";
    public static final String LEFT_BORDER_COLOR = "leftBorderColor";
    public static final String LOCKED = "locked";
    public static final String RIGHT_BORDER_COLOR = "rightBorderColor";
    public static final String ROTATION = "rotation";
    public static final String TOP_BORDER_COLOR = "topBorderColor";
    public static final String VERTICAL_ALIGNMENT = "verticalAlignment";
    public static final String WRAP_TEXT = "wrapText";
    private static final Set<String> booleanValues = Collections.unmodifiableSet(new HashSet(Arrays.asList(new String[]{LOCKED, HIDDEN, WRAP_TEXT})));
    private static final Set<String> borderTypeValues = Collections.unmodifiableSet(new HashSet(Arrays.asList(new String[]{BORDER_BOTTOM, BORDER_LEFT, BORDER_RIGHT, BORDER_TOP})));
    private static final POILogger log = POILogFactory.getLogger(CellUtil.class);
    private static final Set<String> shortValues = Collections.unmodifiableSet(new HashSet(Arrays.asList(new String[]{BOTTOM_BORDER_COLOR, LEFT_BORDER_COLOR, RIGHT_BORDER_COLOR, TOP_BORDER_COLOR, FILL_FOREGROUND_COLOR, FILL_BACKGROUND_COLOR, INDENTION, DATA_FORMAT, "font", ROTATION})));
    private static UnicodeMapping[] unicodeMappings = new UnicodeMapping[]{um("alpha", "α"), um("beta", "β"), um("gamma", "γ"), um("delta", "δ"), um("epsilon", "ε"), um("zeta", "ζ"), um("eta", "η"), um("theta", "θ"), um("iota", "ι"), um("kappa", "κ"), um("lambda", "λ"), um("mu", "μ"), um("nu", "ν"), um("xi", "ξ"), um("omicron", "ο")};

    private CellUtil() {
    }

    public static Row getRow(int rowIndex, Sheet sheet) {
        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            return sheet.createRow(rowIndex);
        }
        return row;
    }

    public static Cell getCell(Row row, int columnIndex) {
        Cell cell = row.getCell(columnIndex);
        if (cell == null) {
            return row.createCell(columnIndex);
        }
        return cell;
    }

    public static Cell createCell(Row row, int column, String value, CellStyle style) {
        Cell cell = getCell(row, column);
        cell.setCellValue(cell.getRow().getSheet().getWorkbook().getCreationHelper().createRichTextString(value));
        if (style != null) {
            cell.setCellStyle(style);
        }
        return cell;
    }

    public static Cell createCell(Row row, int column, String value) {
        return createCell(row, column, value, null);
    }

    @Deprecated
    @Removal(version = "3.17")
    public static void setAlignment(Cell cell, Workbook workbook, short align) {
        setAlignment(cell, HorizontalAlignment.forInt(align));
    }

    public static void setAlignment(Cell cell, HorizontalAlignment align) {
        setCellStyleProperty(cell, ALIGNMENT, align);
    }

    public static void setVerticalAlignment(Cell cell, VerticalAlignment align) {
        setCellStyleProperty(cell, VERTICAL_ALIGNMENT, align);
    }

    @Deprecated
    @Removal(version = "3.17")
    public static void setFont(Cell cell, Workbook workbook, Font font) {
        setFont(cell, font);
    }

    public static void setFont(Cell cell, Font font) {
        Workbook wb = cell.getSheet().getWorkbook();
        short fontIndex = font.getIndex();
        if (wb.getFontAt(fontIndex).equals(font)) {
            setCellStyleProperty(cell, "font", Short.valueOf(fontIndex));
            return;
        }
        throw new IllegalArgumentException("Font does not belong to this workbook");
    }

    public static void setCellStyleProperties(Cell cell, Map<String, Object> properties) {
        Workbook workbook = cell.getSheet().getWorkbook();
        CellStyle newStyle = null;
        Map<String, Object> values = getFormatProperties(cell.getCellStyle());
        putAll(properties, values);
        int numberCellStyles = workbook.getNumCellStyles();
        for (int i = 0; i < numberCellStyles; i++) {
            CellStyle wbStyle = workbook.getCellStyleAt(i);
            if (getFormatProperties(wbStyle).equals(values)) {
                newStyle = wbStyle;
                break;
            }
        }
        if (newStyle == null) {
            newStyle = workbook.createCellStyle();
            setFormatProperties(newStyle, workbook, values);
        }
        cell.setCellStyle(newStyle);
    }

    @Deprecated
    @Removal(version = "3.17")
    public static void setCellStyleProperty(Cell cell, Workbook workbook, String propertyName, Object propertyValue) {
        setCellStyleProperty(cell, propertyName, propertyValue);
    }

    public static void setCellStyleProperty(Cell cell, String propertyName, Object propertyValue) {
        setCellStyleProperties(cell, Collections.singletonMap(propertyName, propertyValue));
    }

    private static Map<String, Object> getFormatProperties(CellStyle style) {
        Map<String, Object> properties = new HashMap();
        put(properties, ALIGNMENT, style.getAlignmentEnum());
        put(properties, VERTICAL_ALIGNMENT, style.getVerticalAlignmentEnum());
        put(properties, BORDER_BOTTOM, style.getBorderBottomEnum());
        put(properties, BORDER_LEFT, style.getBorderLeftEnum());
        put(properties, BORDER_RIGHT, style.getBorderRightEnum());
        put(properties, BORDER_TOP, style.getBorderTopEnum());
        put(properties, BOTTOM_BORDER_COLOR, Short.valueOf(style.getBottomBorderColor()));
        put(properties, DATA_FORMAT, Short.valueOf(style.getDataFormat()));
        put(properties, FILL_PATTERN, style.getFillPatternEnum());
        put(properties, FILL_FOREGROUND_COLOR, Short.valueOf(style.getFillForegroundColor()));
        put(properties, FILL_BACKGROUND_COLOR, Short.valueOf(style.getFillBackgroundColor()));
        put(properties, "font", Short.valueOf(style.getFontIndex()));
        put(properties, HIDDEN, Boolean.valueOf(style.getHidden()));
        put(properties, INDENTION, Short.valueOf(style.getIndention()));
        put(properties, LEFT_BORDER_COLOR, Short.valueOf(style.getLeftBorderColor()));
        put(properties, LOCKED, Boolean.valueOf(style.getLocked()));
        put(properties, RIGHT_BORDER_COLOR, Short.valueOf(style.getRightBorderColor()));
        put(properties, ROTATION, Short.valueOf(style.getRotation()));
        put(properties, TOP_BORDER_COLOR, Short.valueOf(style.getTopBorderColor()));
        put(properties, WRAP_TEXT, Boolean.valueOf(style.getWrapText()));
        return properties;
    }

    private static void putAll(Map<String, Object> src, Map<String, Object> dest) {
        for (String key : src.keySet()) {
            if (shortValues.contains(key)) {
                dest.put(key, Short.valueOf(getShort(src, key)));
            } else if (booleanValues.contains(key)) {
                dest.put(key, Boolean.valueOf(getBoolean(src, key)));
            } else if (borderTypeValues.contains(key)) {
                dest.put(key, getBorderStyle(src, key));
            } else if (ALIGNMENT.equals(key)) {
                dest.put(key, getHorizontalAlignment(src, key));
            } else if (VERTICAL_ALIGNMENT.equals(key)) {
                dest.put(key, getVerticalAlignment(src, key));
            } else if (FILL_PATTERN.equals(key)) {
                dest.put(key, getFillPattern(src, key));
            } else if (log.check(3)) {
                log.log(3, "Ignoring unrecognized CellUtil format properties key: " + key);
            }
        }
    }

    private static void setFormatProperties(CellStyle style, Workbook workbook, Map<String, Object> properties) {
        style.setAlignment(getHorizontalAlignment(properties, ALIGNMENT));
        style.setVerticalAlignment(getVerticalAlignment(properties, VERTICAL_ALIGNMENT));
        style.setBorderBottom(getBorderStyle(properties, BORDER_BOTTOM));
        style.setBorderLeft(getBorderStyle(properties, BORDER_LEFT));
        style.setBorderRight(getBorderStyle(properties, BORDER_RIGHT));
        style.setBorderTop(getBorderStyle(properties, BORDER_TOP));
        style.setBottomBorderColor(getShort(properties, BOTTOM_BORDER_COLOR));
        style.setDataFormat(getShort(properties, DATA_FORMAT));
        style.setFillPattern(getFillPattern(properties, FILL_PATTERN));
        style.setFillForegroundColor(getShort(properties, FILL_FOREGROUND_COLOR));
        style.setFillBackgroundColor(getShort(properties, FILL_BACKGROUND_COLOR));
        style.setFont(workbook.getFontAt(getShort(properties, "font")));
        style.setHidden(getBoolean(properties, HIDDEN));
        style.setIndention(getShort(properties, INDENTION));
        style.setLeftBorderColor(getShort(properties, LEFT_BORDER_COLOR));
        style.setLocked(getBoolean(properties, LOCKED));
        style.setRightBorderColor(getShort(properties, RIGHT_BORDER_COLOR));
        style.setRotation(getShort(properties, ROTATION));
        style.setTopBorderColor(getShort(properties, TOP_BORDER_COLOR));
        style.setWrapText(getBoolean(properties, WRAP_TEXT));
    }

    private static short getShort(Map<String, Object> properties, String name) {
        Object value = properties.get(name);
        if (value instanceof Short) {
            return ((Short) value).shortValue();
        }
        return (short) 0;
    }

    private static BorderStyle getBorderStyle(Map<String, Object> properties, String name) {
        BorderStyle value = properties.get(name);
        if (value instanceof BorderStyle) {
            return value;
        }
        if (value instanceof Short) {
            if (log.check(5)) {
                log.log(5, "Deprecation warning: CellUtil properties map uses Short values for " + name + ". Should use BorderStyle enums instead.");
            }
            System.out.println("BorderStyle short usage");
            return BorderStyle.valueOf(((Short) value).shortValue());
        } else if (value == null) {
            return BorderStyle.NONE;
        } else {
            throw new RuntimeException("Unexpected border style class. Must be BorderStyle or Short (deprecated).");
        }
    }

    private static FillPatternType getFillPattern(Map<String, Object> properties, String name) {
        FillPatternType value = properties.get(name);
        if (value instanceof FillPatternType) {
            return value;
        }
        if (value instanceof Short) {
            if (log.check(5)) {
                log.log(5, "Deprecation warning: CellUtil properties map uses Short values for " + name + ". Should use FillPatternType enums instead.");
            }
            System.out.println("FillPatternType short usage");
            return FillPatternType.forInt(((Short) value).shortValue());
        } else if (value == null) {
            return FillPatternType.NO_FILL;
        } else {
            throw new RuntimeException("Unexpected fill pattern style class. Must be FillPatternType or Short (deprecated).");
        }
    }

    private static HorizontalAlignment getHorizontalAlignment(Map<String, Object> properties, String name) {
        HorizontalAlignment value = properties.get(name);
        if (value instanceof HorizontalAlignment) {
            return value;
        }
        if (value instanceof Short) {
            if (log.check(5)) {
                log.log(5, "Deprecation warning: CellUtil properties map used a Short value for " + name + ". Should use HorizontalAlignment enums instead.");
            }
            System.out.println("HorizontalAlignment short usage");
            return HorizontalAlignment.forInt(((Short) value).shortValue());
        } else if (value == null) {
            return HorizontalAlignment.GENERAL;
        } else {
            throw new RuntimeException("Unexpected horizontal alignment style class. Must be HorizontalAlignment or Short (deprecated).");
        }
    }

    private static VerticalAlignment getVerticalAlignment(Map<String, Object> properties, String name) {
        VerticalAlignment value = properties.get(name);
        if (value instanceof VerticalAlignment) {
            return value;
        }
        if (value instanceof Short) {
            if (log.check(5)) {
                log.log(5, "Deprecation warning: CellUtil properties map used a Short value for " + name + ". Should use VerticalAlignment enums instead.");
            }
            System.out.println("VerticalAlignment usage " + name + " " + value);
            return VerticalAlignment.forInt(((Short) value).shortValue());
        } else if (value == null) {
            return VerticalAlignment.BOTTOM;
        } else {
            throw new RuntimeException("Unexpected vertical alignment style class. Must be VerticalAlignment or Short (deprecated).");
        }
    }

    private static boolean getBoolean(Map<String, Object> properties, String name) {
        Object value = properties.get(name);
        if (value instanceof Boolean) {
            return ((Boolean) value).booleanValue();
        }
        return false;
    }

    private static void put(Map<String, Object> properties, String name, Object value) {
        properties.put(name, value);
    }

    private static void putShort(Map<String, Object> properties, String name, short value) {
        properties.put(name, Short.valueOf(value));
    }

    private static void putEnum(Map<String, Object> properties, String name, Enum<?> value) {
        properties.put(name, value);
    }

    private static void putBoolean(Map<String, Object> properties, String name, boolean value) {
        properties.put(name, Boolean.valueOf(value));
    }

    public static Cell translateUnicodeValues(Cell cell) {
        String s = cell.getRichStringCellValue().getString();
        boolean foundUnicode = false;
        String lowerCaseStr = s.toLowerCase(Locale.ROOT);
        for (UnicodeMapping entry : unicodeMappings) {
            String key = entry.entityName;
            if (lowerCaseStr.contains(key)) {
                s = s.replaceAll(key, entry.resolvedValue);
                foundUnicode = true;
            }
        }
        if (foundUnicode) {
            cell.setCellValue(cell.getRow().getSheet().getWorkbook().getCreationHelper().createRichTextString(s));
        }
        return cell;
    }

    private static UnicodeMapping um(String entityName, String resolvedValue) {
        return new UnicodeMapping(entityName, resolvedValue);
    }
}

package org.apache.poi.ss.format;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JLabel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;

public class CellFormat {
    private static final CellFormatPart DEFAULT_TEXT_FORMAT = new CellFormatPart("@");
    public static final CellFormat GENERAL_FORMAT = new CellFormat("General") {
        public CellFormatResult apply(Object value) {
            return new CellFormatResult(true, new CellGeneralFormatter().format(value), null);
        }
    };
    private static final String INVALID_VALUE_FOR_FORMAT = "###############################################################################################################################################################################################################################################################";
    private static final Pattern ONE_PART = Pattern.compile(CellFormatPart.FORMAT_PAT.pattern() + "(;|$)", 6);
    private static String QUOTE = "\"";
    private static final Map<String, CellFormat> formatCache = new WeakHashMap();
    private final String format;
    private final int formatPartCount;
    private final CellFormatPart negNumFmt;
    private final CellFormatPart posNumFmt;
    private final CellFormatPart textFmt;
    private final CellFormatPart zeroNumFmt;

    static /* synthetic */ class C11002 {
        static final /* synthetic */ int[] $SwitchMap$org$apache$poi$ss$usermodel$CellType = new int[CellType.values().length];

        static {
            try {
                $SwitchMap$org$apache$poi$ss$usermodel$CellType[CellType.BLANK.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$apache$poi$ss$usermodel$CellType[CellType.BOOLEAN.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$apache$poi$ss$usermodel$CellType[CellType.NUMERIC.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$apache$poi$ss$usermodel$CellType[CellType.STRING.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    public static CellFormat getInstance(String format) {
        CellFormat fmt = (CellFormat) formatCache.get(format);
        if (fmt == null) {
            if (format.equals("General") || format.equals("@")) {
                fmt = GENERAL_FORMAT;
            } else {
                fmt = new CellFormat(format);
            }
            formatCache.put(format, fmt);
        }
        return fmt;
    }

    private CellFormat(String format) {
        this.format = format;
        Matcher m = ONE_PART.matcher(format);
        List<CellFormatPart> parts = new ArrayList();
        while (m.find()) {
            try {
                String valueDesc = m.group();
                if (valueDesc.endsWith(";")) {
                    valueDesc = valueDesc.substring(0, valueDesc.length() - 1);
                }
                parts.add(new CellFormatPart(valueDesc));
            } catch (RuntimeException e) {
                CellFormatter.logger.log(Level.WARNING, "Invalid format: " + CellFormatter.quote(m.group()), e);
                parts.add(null);
            }
        }
        this.formatPartCount = parts.size();
        switch (this.formatPartCount) {
            case 1:
                this.posNumFmt = (CellFormatPart) parts.get(0);
                this.negNumFmt = null;
                this.zeroNumFmt = null;
                this.textFmt = DEFAULT_TEXT_FORMAT;
                return;
            case 2:
                this.posNumFmt = (CellFormatPart) parts.get(0);
                this.negNumFmt = (CellFormatPart) parts.get(1);
                this.zeroNumFmt = null;
                this.textFmt = DEFAULT_TEXT_FORMAT;
                return;
            case 3:
                this.posNumFmt = (CellFormatPart) parts.get(0);
                this.negNumFmt = (CellFormatPart) parts.get(1);
                this.zeroNumFmt = (CellFormatPart) parts.get(2);
                this.textFmt = DEFAULT_TEXT_FORMAT;
                return;
            default:
                this.posNumFmt = (CellFormatPart) parts.get(0);
                this.negNumFmt = (CellFormatPart) parts.get(1);
                this.zeroNumFmt = (CellFormatPart) parts.get(2);
                this.textFmt = (CellFormatPart) parts.get(3);
                return;
        }
    }

    public CellFormatResult apply(Object value) {
        if (value instanceof Number) {
            double val = ((Number) value).doubleValue();
            if (val >= 0.0d || ((this.formatPartCount != 2 || this.posNumFmt.hasCondition() || this.negNumFmt.hasCondition()) && ((this.formatPartCount != 3 || this.negNumFmt.hasCondition()) && (this.formatPartCount != 4 || this.negNumFmt.hasCondition())))) {
                return getApplicableFormatPart(Double.valueOf(val)).apply(Double.valueOf(val));
            }
            return this.negNumFmt.apply(Double.valueOf(-val));
        } else if (!(value instanceof Date)) {
            return this.textFmt.apply(value);
        } else {
            Double numericValue = Double.valueOf(DateUtil.getExcelDate((Date) value));
            if (DateUtil.isValidExcelDate(numericValue.doubleValue())) {
                return getApplicableFormatPart(numericValue).apply(value);
            }
            throw new IllegalArgumentException("value " + numericValue + " of date " + value + " is not a valid Excel date");
        }
    }

    private CellFormatResult apply(Date date, double numericValue) {
        return getApplicableFormatPart(Double.valueOf(numericValue)).apply(date);
    }

    public CellFormatResult apply(Cell c) {
        switch (C11002.$SwitchMap$org$apache$poi$ss$usermodel$CellType[ultimateTypeEnum(c).ordinal()]) {
            case 1:
                return apply((Object) "");
            case 2:
                return apply(Boolean.valueOf(c.getBooleanCellValue()));
            case 3:
                Object value = Double.valueOf(c.getNumericCellValue());
                if (getApplicableFormatPart(value).getCellFormatType() != CellFormatType.DATE) {
                    return apply(value);
                }
                if (DateUtil.isValidExcelDate(value.doubleValue())) {
                    return apply(c.getDateCellValue(), value.doubleValue());
                }
                return apply(INVALID_VALUE_FOR_FORMAT);
            case 4:
                return apply(c.getStringCellValue());
            default:
                return apply((Object) "?");
        }
    }

    public CellFormatResult apply(JLabel label, Object value) {
        CellFormatResult result = apply(value);
        label.setText(result.text);
        if (result.textColor != null) {
            label.setForeground(result.textColor);
        }
        return result;
    }

    private CellFormatResult apply(JLabel label, Date date, double numericValue) {
        CellFormatResult result = apply(date, numericValue);
        label.setText(result.text);
        if (result.textColor != null) {
            label.setForeground(result.textColor);
        }
        return result;
    }

    public CellFormatResult apply(JLabel label, Cell c) {
        switch (C11002.$SwitchMap$org$apache$poi$ss$usermodel$CellType[ultimateTypeEnum(c).ordinal()]) {
            case 1:
                return apply(label, (Object) "");
            case 2:
                return apply(label, Boolean.valueOf(c.getBooleanCellValue()));
            case 3:
                Object value = Double.valueOf(c.getNumericCellValue());
                if (getApplicableFormatPart(value).getCellFormatType() != CellFormatType.DATE) {
                    return apply(label, value);
                }
                if (DateUtil.isValidExcelDate(value.doubleValue())) {
                    return apply(label, c.getDateCellValue(), value.doubleValue());
                }
                return apply(label, INVALID_VALUE_FOR_FORMAT);
            case 4:
                return apply(label, c.getStringCellValue());
            default:
                return apply(label, (Object) "?");
        }
    }

    private CellFormatPart getApplicableFormatPart(Object value) {
        if (value instanceof Number) {
            double val = ((Number) value).doubleValue();
            if (this.formatPartCount == 1) {
                if (!this.posNumFmt.hasCondition() || (this.posNumFmt.hasCondition() && this.posNumFmt.applies(Double.valueOf(val)))) {
                    return this.posNumFmt;
                }
                return new CellFormatPart("General");
            } else if (this.formatPartCount == 2) {
                if ((!this.posNumFmt.hasCondition() && val >= 0.0d) || (this.posNumFmt.hasCondition() && this.posNumFmt.applies(Double.valueOf(val)))) {
                    return this.posNumFmt;
                }
                if (!this.negNumFmt.hasCondition() || (this.negNumFmt.hasCondition() && this.negNumFmt.applies(Double.valueOf(val)))) {
                    return this.negNumFmt;
                }
                return new CellFormatPart(QUOTE + INVALID_VALUE_FOR_FORMAT + QUOTE);
            } else if ((!this.posNumFmt.hasCondition() && val > 0.0d) || (this.posNumFmt.hasCondition() && this.posNumFmt.applies(Double.valueOf(val)))) {
                return this.posNumFmt;
            } else {
                if ((this.negNumFmt.hasCondition() || val >= 0.0d) && (!this.negNumFmt.hasCondition() || !this.negNumFmt.applies(Double.valueOf(val)))) {
                    return this.zeroNumFmt;
                }
                return this.negNumFmt;
            }
        }
        throw new IllegalArgumentException("value must be a Number");
    }

    public static int ultimateType(Cell cell) {
        return ultimateTypeEnum(cell).getCode();
    }

    public static CellType ultimateTypeEnum(Cell cell) {
        CellType type = cell.getCellTypeEnum();
        if (type == CellType.FORMULA) {
            return cell.getCachedFormulaResultTypeEnum();
        }
        return type;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CellFormat)) {
            return false;
        }
        return this.format.equals(((CellFormat) obj).format);
    }

    public int hashCode() {
        return this.format.hashCode();
    }
}

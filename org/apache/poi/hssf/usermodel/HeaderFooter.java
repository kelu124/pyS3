package org.apache.poi.hssf.usermodel;

public abstract class HeaderFooter implements org.apache.poi.ss.usermodel.HeaderFooter {

    private enum MarkupTag {
        SHEET_NAME_FIELD("&A", false),
        DATE_FIELD("&D", false),
        FILE_FIELD("&F", false),
        FULL_FILE_FIELD("&Z", false),
        PAGE_FIELD("&P", false),
        TIME_FIELD("&T", false),
        NUM_PAGES_FIELD("&N", false),
        PICTURE_FIELD("&G", false),
        BOLD_FIELD("&B", true),
        ITALIC_FIELD("&I", true),
        STRIKETHROUGH_FIELD("&S", true),
        SUBSCRIPT_FIELD("&Y", true),
        SUPERSCRIPT_FIELD("&X", true),
        UNDERLINE_FIELD("&U", true),
        DOUBLE_UNDERLINE_FIELD("&E", true);
        
        private final boolean _occursInPairs;
        private final String _representation;

        private MarkupTag(String sequence, boolean occursInPairs) {
            this._representation = sequence;
            this._occursInPairs = occursInPairs;
        }

        public String getRepresentation() {
            return this._representation;
        }

        public boolean occursPairs() {
            return this._occursInPairs;
        }
    }

    protected abstract String getRawText();

    protected abstract void setHeaderFooterText(String str);

    protected HeaderFooter() {
    }

    private String[] splitParts() {
        String text = getRawText();
        String _left = "";
        String _center = "";
        String _right = "";
        while (text.length() > 1) {
            if (text.charAt(0) == '&') {
                int pos = text.length();
                switch (text.charAt(1)) {
                    case 'C':
                        if (text.indexOf("&L") >= 0) {
                            pos = Math.min(pos, text.indexOf("&L"));
                        }
                        if (text.indexOf("&R") >= 0) {
                            pos = Math.min(pos, text.indexOf("&R"));
                        }
                        _center = text.substring(2, pos);
                        text = text.substring(pos);
                        continue;
                    case 'L':
                        if (text.indexOf("&C") >= 0) {
                            pos = Math.min(pos, text.indexOf("&C"));
                        }
                        if (text.indexOf("&R") >= 0) {
                            pos = Math.min(pos, text.indexOf("&R"));
                        }
                        _left = text.substring(2, pos);
                        text = text.substring(pos);
                        continue;
                    case 'R':
                        if (text.indexOf("&C") >= 0) {
                            pos = Math.min(pos, text.indexOf("&C"));
                        }
                        if (text.indexOf("&L") >= 0) {
                            pos = Math.min(pos, text.indexOf("&L"));
                        }
                        _right = text.substring(2, pos);
                        text = text.substring(pos);
                        continue;
                    default:
                        _center = text;
                        break;
                }
            }
            _center = text;
            return new String[]{_left, _center, _right};
        }
        return new String[]{_left, _center, _right};
    }

    public final String getLeft() {
        return splitParts()[0];
    }

    public final void setLeft(String newLeft) {
        updatePart(0, newLeft);
    }

    public final String getCenter() {
        return splitParts()[1];
    }

    public final void setCenter(String newCenter) {
        updatePart(1, newCenter);
    }

    public final String getRight() {
        return splitParts()[2];
    }

    public final void setRight(String newRight) {
        updatePart(2, newRight);
    }

    private void updatePart(int partIndex, String newValue) {
        String[] parts = splitParts();
        if (newValue == null) {
            newValue = "";
        }
        parts[partIndex] = newValue;
        updateHeaderFooterText(parts);
    }

    private void updateHeaderFooterText(String[] parts) {
        String _left = parts[0];
        String _center = parts[1];
        String _right = parts[2];
        if (_center.length() >= 1 || _left.length() >= 1 || _right.length() >= 1) {
            StringBuilder sb = new StringBuilder(64);
            sb.append("&C");
            sb.append(_center);
            sb.append("&L");
            sb.append(_left);
            sb.append("&R");
            sb.append(_right);
            setHeaderFooterText(sb.toString());
            return;
        }
        setHeaderFooterText("");
    }

    public static String fontSize(short size) {
        return "&" + size;
    }

    public static String font(String font, String style) {
        return "&\"" + font + "," + style + "\"";
    }

    public static String page() {
        return MarkupTag.PAGE_FIELD.getRepresentation();
    }

    public static String numPages() {
        return MarkupTag.NUM_PAGES_FIELD.getRepresentation();
    }

    public static String date() {
        return MarkupTag.DATE_FIELD.getRepresentation();
    }

    public static String time() {
        return MarkupTag.TIME_FIELD.getRepresentation();
    }

    public static String file() {
        return MarkupTag.FILE_FIELD.getRepresentation();
    }

    public static String tab() {
        return MarkupTag.SHEET_NAME_FIELD.getRepresentation();
    }

    public static String startBold() {
        return MarkupTag.BOLD_FIELD.getRepresentation();
    }

    public static String endBold() {
        return MarkupTag.BOLD_FIELD.getRepresentation();
    }

    public static String startUnderline() {
        return MarkupTag.UNDERLINE_FIELD.getRepresentation();
    }

    public static String endUnderline() {
        return MarkupTag.UNDERLINE_FIELD.getRepresentation();
    }

    public static String startDoubleUnderline() {
        return MarkupTag.DOUBLE_UNDERLINE_FIELD.getRepresentation();
    }

    public static String endDoubleUnderline() {
        return MarkupTag.DOUBLE_UNDERLINE_FIELD.getRepresentation();
    }

    public static String stripFields(String pText) {
        if (pText == null || pText.length() == 0) {
            return pText;
        }
        String text = pText;
        for (MarkupTag mt : MarkupTag.values()) {
            String seq = mt.getRepresentation();
            while (true) {
                int pos = text.indexOf(seq);
                if (pos <= -1) {
                    break;
                }
                text = text.substring(0, pos) + text.substring(seq.length() + pos);
            }
        }
        return text.replaceAll("\\&\\d+", "").replaceAll("\\&\".*?,.*?\"", "");
    }
}

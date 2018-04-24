package org.apache.poi.sl.draw;

import com.google.common.base.Ascii;
import com.itextpdf.text.html.HtmlTags;
import com.itextpdf.text.pdf.BaseField;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.io.InvalidObjectException;
import java.text.AttributedCharacterIterator;
import java.text.AttributedCharacterIterator.Attribute;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.poi.sl.usermodel.AutoNumberingScheme;
import org.apache.poi.sl.usermodel.Hyperlink;
import org.apache.poi.sl.usermodel.Insets2D;
import org.apache.poi.sl.usermodel.PaintStyle;
import org.apache.poi.sl.usermodel.PlaceableShape;
import org.apache.poi.sl.usermodel.ShapeContainer;
import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.sl.usermodel.TextParagraph;
import org.apache.poi.sl.usermodel.TextParagraph.BulletStyle;
import org.apache.poi.sl.usermodel.TextParagraph.TextAlign;
import org.apache.poi.sl.usermodel.TextRun;
import org.apache.poi.sl.usermodel.TextRun.TextCap;
import org.apache.poi.sl.usermodel.TextShape.TextDirection;
import org.apache.poi.util.Units;
import org.bytedeco.javacpp.avcodec;

public class DrawTextParagraph implements Drawable {
    static final /* synthetic */ boolean $assertionsDisabled = (!DrawTextParagraph.class.desiredAssertionStatus());
    public static final XlinkAttribute HYPERLINK_HREF = new XlinkAttribute(HtmlTags.HREF);
    public static final XlinkAttribute HYPERLINK_LABEL = new XlinkAttribute("label");
    protected int autoNbrIdx = 0;
    protected DrawTextFragment bullet;
    protected List<DrawTextFragment> lines = new ArrayList();
    protected double maxLineHeight;
    protected TextParagraph<?, ?, ?> paragraph;
    protected String rawText;
    double f176x;
    double f177y;

    class C10831 implements PlaceableShape {
        C10831() {
        }

        public ShapeContainer<?, ?> getParent() {
            return null;
        }

        public Rectangle2D getAnchor() {
            return DrawTextParagraph.this.paragraph.getParentShape().getAnchor();
        }

        public void setAnchor(Rectangle2D anchor) {
        }

        public double getRotation() {
            return 0.0d;
        }

        public void setRotation(double theta) {
        }

        public void setFlipHorizontal(boolean flip) {
        }

        public void setFlipVertical(boolean flip) {
        }

        public boolean getFlipHorizontal() {
            return false;
        }

        public boolean getFlipVertical() {
            return false;
        }

        public Sheet<?, ?> getSheet() {
            return DrawTextParagraph.this.paragraph.getParentShape().getSheet();
        }
    }

    private static class AttributedStringData {
        Attribute attribute;
        int beginIndex;
        int endIndex;
        Object value;

        AttributedStringData(Attribute attribute, Object value, int beginIndex, int endIndex) {
            this.attribute = attribute;
            this.value = value;
            this.beginIndex = beginIndex;
            this.endIndex = endIndex;
        }
    }

    private static class XlinkAttribute extends Attribute {
        XlinkAttribute(String name) {
            super(name);
        }

        protected Object readResolve() throws InvalidObjectException {
            if (DrawTextParagraph.HYPERLINK_HREF.getName().equals(getName())) {
                return DrawTextParagraph.HYPERLINK_HREF;
            }
            if (DrawTextParagraph.HYPERLINK_LABEL.getName().equals(getName())) {
                return DrawTextParagraph.HYPERLINK_LABEL;
            }
            throw new InvalidObjectException("unknown attribute name");
        }
    }

    public DrawTextParagraph(TextParagraph<?, ?, ?> paragraph) {
        this.paragraph = paragraph;
    }

    public void setPosition(double x, double y) {
        this.f176x = x;
        this.f177y = y;
    }

    public double getY() {
        return this.f177y;
    }

    public void setAutoNumberingIdx(int index) {
        this.autoNbrIdx = index;
    }

    public void draw(Graphics2D graphics) {
        if (!this.lines.isEmpty()) {
            double penY = this.f177y;
            boolean firstLine = true;
            int indentLevel = this.paragraph.getIndentLevel();
            Double leftMargin = this.paragraph.getLeftMargin();
            if (leftMargin == null) {
                leftMargin = Double.valueOf(Units.toPoints(347663 * ((long) indentLevel)));
            }
            Double indent = this.paragraph.getIndent();
            if (indent == null) {
                indent = Double.valueOf(Units.toPoints(347663 * ((long) indentLevel)));
            }
            if (isHSLF()) {
                indent = Double.valueOf(indent.doubleValue() - leftMargin.doubleValue());
            }
            Double spacing = this.paragraph.getLineSpacing();
            if (spacing == null) {
                spacing = Double.valueOf(100.0d);
            }
            for (DrawTextFragment line : this.lines) {
                double penX;
                if (firstLine) {
                    if (!isEmptyParagraph()) {
                        this.bullet = getBullet(graphics, line.getAttributedString().getIterator());
                    }
                    if (this.bullet != null) {
                        this.bullet.setPosition((this.f176x + leftMargin.doubleValue()) + indent.doubleValue(), penY);
                        this.bullet.draw(graphics);
                        penX = this.f176x + Math.max(leftMargin.doubleValue(), (leftMargin.doubleValue() + indent.doubleValue()) + ((double) (this.bullet.getLayout().getAdvance() + BaseField.BORDER_WIDTH_THIN)));
                    } else {
                        penX = this.f176x + leftMargin.doubleValue();
                    }
                } else {
                    penX = this.f176x + leftMargin.doubleValue();
                }
                Rectangle2D anchor = DrawShape.getAnchor(graphics, (PlaceableShape) this.paragraph.getParentShape());
                Insets2D insets = this.paragraph.getParentShape().getInsets();
                double leftInset = insets.left;
                double rightInset = insets.right;
                TextAlign ta = this.paragraph.getTextAlign();
                if (ta == null) {
                    ta = TextAlign.LEFT;
                }
                switch (ta) {
                    case CENTER:
                        penX += ((((anchor.getWidth() - ((double) line.getWidth())) - leftInset) - rightInset) - leftMargin.doubleValue()) / 2.0d;
                        break;
                    case RIGHT:
                        penX += ((anchor.getWidth() - ((double) line.getWidth())) - leftInset) - rightInset;
                        break;
                }
                line.setPosition(penX, penY);
                line.draw(graphics);
                if (spacing.doubleValue() > 0.0d) {
                    penY += (spacing.doubleValue() * 0.01d) * ((double) line.getHeight());
                } else {
                    penY += -spacing.doubleValue();
                }
                firstLine = false;
            }
            this.f177y = penY - this.f177y;
        }
    }

    public float getFirstLineHeight() {
        return this.lines.isEmpty() ? 0.0f : ((DrawTextFragment) this.lines.get(0)).getHeight();
    }

    public float getLastLineHeight() {
        return this.lines.isEmpty() ? 0.0f : ((DrawTextFragment) this.lines.get(this.lines.size() - 1)).getHeight();
    }

    public boolean isEmptyParagraph() {
        return this.lines.isEmpty() || this.rawText.trim().isEmpty();
    }

    public void applyTransform(Graphics2D graphics) {
    }

    public void drawContent(Graphics2D graphics) {
    }

    protected void breakText(Graphics2D graphics) {
        this.lines.clear();
        DrawFactory fact = DrawFactory.getInstance(graphics);
        StringBuilder text = new StringBuilder();
        AttributedString at = getAttributedString(graphics, text);
        boolean emptyParagraph = "".equals(text.toString().trim());
        AttributedCharacterIterator it = at.getIterator();
        LineBreakMeasurer measurer = new LineBreakMeasurer(it, graphics.getFontRenderContext());
        int endIndex;
        do {
            int startIndex = measurer.getPosition();
            double wrappingWidth = getWrappingWidth(this.lines.size() == 0, graphics) + 1.0d;
            if (wrappingWidth < 0.0d) {
                wrappingWidth = 1.0d;
            }
            int nextBreak = text.indexOf("\n", startIndex + 1);
            if (nextBreak == -1) {
                nextBreak = it.getEndIndex();
            }
            TextLayout layout = measurer.nextLayout((float) wrappingWidth, nextBreak, true);
            if (layout == null) {
                layout = measurer.nextLayout((float) wrappingWidth, nextBreak, false);
            }
            if (layout == null) {
                break;
            }
            endIndex = measurer.getPosition();
            if (endIndex < it.getEndIndex() && text.charAt(endIndex) == '\n') {
                measurer.setPosition(endIndex + 1);
            }
            TextAlign hAlign = this.paragraph.getTextAlign();
            if (hAlign == TextAlign.JUSTIFY || hAlign == TextAlign.JUSTIFY_LOW) {
                layout = layout.getJustifiedLayout((float) wrappingWidth);
            }
            DrawTextFragment line = fact.getTextFragment(layout, emptyParagraph ? null : new AttributedString(it, startIndex, endIndex));
            this.lines.add(line);
            this.maxLineHeight = Math.max(this.maxLineHeight, (double) line.getHeight());
        } while (endIndex != it.getEndIndex());
        this.rawText = text.toString();
    }

    protected DrawTextFragment getBullet(Graphics2D graphics, AttributedCharacterIterator firstLineAttr) {
        BulletStyle bulletStyle = this.paragraph.getBulletStyle();
        if (bulletStyle == null) {
            return null;
        }
        String buCharacter;
        AutoNumberingScheme ans = bulletStyle.getAutoNumberingScheme();
        if (ans != null) {
            buCharacter = ans.format(this.autoNbrIdx);
        } else {
            buCharacter = bulletStyle.getBulletCharacter();
        }
        if (buCharacter == null) {
            return null;
        }
        String buFont = bulletStyle.getBulletFont();
        if (buFont == null) {
            buFont = this.paragraph.getDefaultFontFamily();
        }
        if ($assertionsDisabled || buFont != null) {
            Paint fgPaint;
            PlaceableShape<?, ?> ps = getParagraphShape();
            PaintStyle fgPaintStyle = bulletStyle.getBulletFontColor();
            if (fgPaintStyle == null) {
                fgPaint = (Paint) firstLineAttr.getAttribute(TextAttribute.FOREGROUND);
            } else {
                fgPaint = new DrawPaint(ps).getPaint(graphics, fgPaintStyle);
            }
            float fontSize = ((Float) firstLineAttr.getAttribute(TextAttribute.SIZE)).floatValue();
            Double buSz = bulletStyle.getBulletFontSize();
            if (buSz == null) {
                buSz = Double.valueOf(100.0d);
            }
            if (buSz.doubleValue() > 0.0d) {
                fontSize = (float) (((double) fontSize) * (buSz.doubleValue() * 0.01d));
            } else {
                fontSize = (float) (-buSz.doubleValue());
            }
            AttributedString str = new AttributedString(mapFontCharset(buCharacter, buFont));
            str.addAttribute(TextAttribute.FOREGROUND, fgPaint);
            str.addAttribute(TextAttribute.FAMILY, buFont);
            str.addAttribute(TextAttribute.SIZE, Float.valueOf(fontSize));
            return DrawFactory.getInstance(graphics).getTextFragment(new TextLayout(str.getIterator(), graphics.getFontRenderContext()), str);
        }
        throw new AssertionError();
    }

    protected String getRenderableText(TextRun tr) {
        StringBuilder buf = new StringBuilder();
        TextCap cap = tr.getTextCap();
        String tabs = null;
        for (char c : tr.getRawText().toCharArray()) {
            char c2;
            switch (c2) {
                case '\t':
                    if (tabs == null) {
                        tabs = tab2space(tr);
                    }
                    buf.append(tabs);
                    break;
                case '\u000b':
                    buf.append('\n');
                    break;
                default:
                    switch (cap) {
                        case ALL:
                            c2 = Character.toUpperCase(c2);
                            break;
                        case SMALL:
                            c2 = Character.toLowerCase(c2);
                            break;
                    }
                    buf.append(c2);
                    break;
            }
        }
        return buf.toString();
    }

    private String tab2space(TextRun tr) {
        AttributedString string = new AttributedString(" ");
        String fontFamily = tr.getFontFamily();
        if (fontFamily == null) {
            fontFamily = "Lucida Sans";
        }
        string.addAttribute(TextAttribute.FAMILY, fontFamily);
        Double fs = tr.getFontSize();
        if (fs == null) {
            fs = Double.valueOf(12.0d);
        }
        string.addAttribute(TextAttribute.SIZE, Float.valueOf(fs.floatValue()));
        double wspace = (double) new TextLayout(string.getIterator(), new FontRenderContext(null, true, true)).getAdvance();
        Double tabSz = this.paragraph.getDefaultTabSize();
        if (tabSz == null) {
            tabSz = Double.valueOf(4.0d * wspace);
        }
        int numSpaces = (int) Math.ceil(tabSz.doubleValue() / wspace);
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < numSpaces; i++) {
            buf.append(' ');
        }
        return buf.toString();
    }

    protected double getWrappingWidth(boolean firstLine, Graphics2D graphics) {
        PlaceableShape ts = this.paragraph.getParentShape();
        Insets2D insets = ts.getInsets();
        double leftInset = insets.left;
        double rightInset = insets.right;
        int indentLevel = this.paragraph.getIndentLevel();
        if (indentLevel == -1) {
            indentLevel = 0;
        }
        Double leftMargin = this.paragraph.getLeftMargin();
        if (leftMargin == null) {
            leftMargin = Double.valueOf(Units.toPoints(347663 * ((long) (indentLevel + 1))));
        }
        Double indent = this.paragraph.getIndent();
        if (indent == null) {
            indent = Double.valueOf(Units.toPoints(347663 * ((long) indentLevel)));
        }
        Double rightMargin = this.paragraph.getRightMargin();
        if (rightMargin == null) {
            rightMargin = Double.valueOf(0.0d);
        }
        Rectangle2D anchor = DrawShape.getAnchor(graphics, ts);
        TextDirection textDir = ts.getTextDirection();
        if (ts.getWordWrap()) {
            double width;
            switch (textDir) {
                case VERTICAL:
                case VERTICAL_270:
                    width = (((anchor.getHeight() - leftInset) - rightInset) - leftMargin.doubleValue()) - rightMargin.doubleValue();
                    break;
                default:
                    width = (((anchor.getWidth() - leftInset) - rightInset) - leftMargin.doubleValue()) - rightMargin.doubleValue();
                    break;
            }
            if (!firstLine || isHSLF()) {
                return width;
            }
            if (this.bullet != null) {
                if (indent.doubleValue() > 0.0d) {
                    return width - indent.doubleValue();
                }
                return width;
            } else if (indent.doubleValue() > 0.0d) {
                return width - indent.doubleValue();
            } else {
                if (indent.doubleValue() < 0.0d) {
                    return width + leftMargin.doubleValue();
                }
                return width;
            }
        }
        Dimension pageDim = ts.getSheet().getSlideShow().getPageSize();
        switch (textDir) {
            case VERTICAL:
                return pageDim.getHeight() - anchor.getX();
            case VERTICAL_270:
                return anchor.getX();
            default:
                return pageDim.getWidth() - anchor.getX();
        }
    }

    private PlaceableShape<?, ?> getParagraphShape() {
        return new C10831();
    }

    protected AttributedString getAttributedString(Graphics2D graphics, StringBuilder text) {
        List<AttributedStringData> attList = new ArrayList();
        if (text == null) {
            text = new StringBuilder();
        }
        PlaceableShape<?, ?> ps = getParagraphShape();
        DrawFontManager fontHandler = (DrawFontManager) graphics.getRenderingHint(Drawable.FONT_HANDLER);
        for (TextRun run : this.paragraph) {
            Double fontSz;
            String runText = getRenderableText(run);
            if (!runText.isEmpty()) {
                String fontFamily = run.getFontFamily();
                Map<String, String> fontMap = (Map) graphics.getRenderingHint(Drawable.FONT_MAP);
                if (fontMap != null && fontMap.containsKey(fontFamily)) {
                    fontFamily = (String) fontMap.get(fontFamily);
                }
                if (fontHandler != null) {
                    fontFamily = fontHandler.getRendererableFont(fontFamily, run.getPitchAndFamily());
                }
                if (fontFamily == null) {
                    fontFamily = this.paragraph.getDefaultFontFamily();
                }
                int beginIndex = text.length();
                text.append(mapFontCharset(runText, fontFamily));
                int endIndex = text.length();
                attList.add(new AttributedStringData(TextAttribute.FAMILY, fontFamily, beginIndex, endIndex));
                attList.add(new AttributedStringData(TextAttribute.FOREGROUND, new DrawPaint(ps).getPaint(graphics, run.getFontColor()), beginIndex, endIndex));
                fontSz = run.getFontSize();
                if (fontSz == null) {
                    fontSz = this.paragraph.getDefaultFontSize();
                }
                attList.add(new AttributedStringData(TextAttribute.SIZE, Float.valueOf(fontSz.floatValue()), beginIndex, endIndex));
                if (run.isBold()) {
                    attList.add(new AttributedStringData(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD, beginIndex, endIndex));
                }
                if (run.isItalic()) {
                    attList.add(new AttributedStringData(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE, beginIndex, endIndex));
                }
                if (run.isUnderlined()) {
                    attList.add(new AttributedStringData(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON, beginIndex, endIndex));
                    attList.add(new AttributedStringData(TextAttribute.INPUT_METHOD_UNDERLINE, TextAttribute.UNDERLINE_LOW_TWO_PIXEL, beginIndex, endIndex));
                }
                if (run.isStrikethrough()) {
                    attList.add(new AttributedStringData(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON, beginIndex, endIndex));
                }
                if (run.isSubscript()) {
                    attList.add(new AttributedStringData(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUB, beginIndex, endIndex));
                }
                if (run.isSuperscript()) {
                    attList.add(new AttributedStringData(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUPER, beginIndex, endIndex));
                }
                Hyperlink<?, ?> hl = run.getHyperlink();
                if (hl != null) {
                    attList.add(new AttributedStringData(HYPERLINK_HREF, hl.getAddress(), beginIndex, endIndex));
                    attList.add(new AttributedStringData(HYPERLINK_LABEL, hl.getLabel(), beginIndex, endIndex));
                }
            }
        }
        if (text.length() == 0) {
            fontSz = this.paragraph.getDefaultFontSize();
            text.append(" ");
            attList.add(new AttributedStringData(TextAttribute.SIZE, Float.valueOf(fontSz.floatValue()), 0, 1));
        }
        AttributedString string = new AttributedString(text.toString());
        for (AttributedStringData asd : attList) {
            string.addAttribute(asd.attribute, asd.value, asd.beginIndex, asd.endIndex);
        }
        return string;
    }

    protected boolean isHSLF() {
        return this.paragraph.getClass().getName().contains("HSLF");
    }

    protected String mapFontCharset(String text, String fontFamily) {
        String attStr = text;
        if (!"Wingdings".equalsIgnoreCase(fontFamily)) {
            return attStr;
        }
        boolean changed = false;
        char[] chrs = attStr.toCharArray();
        int i = 0;
        while (i < chrs.length) {
            if ((' ' <= chrs[i] && chrs[i] <= Ascii.MAX) || (' ' <= chrs[i] && chrs[i] <= 'ÿ')) {
                chrs[i] = (char) (chrs[i] | avcodec.MB_TYPE_L0L1);
                changed = true;
            }
            i++;
        }
        if (changed) {
            return new String(chrs);
        }
        return attStr;
    }
}

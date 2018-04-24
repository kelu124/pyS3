package org.apache.poi.hssf.usermodel;

import com.itextpdf.text.pdf.BaseField;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.util.NotImplemented;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;
import org.apache.poi.util.SuppressForbidden;

public class EscherGraphics extends Graphics {
    private static final POILogger logger = POILogFactory.getLogger(EscherGraphics.class);
    private Color background = Color.white;
    private final HSSFShapeGroup escherGroup;
    private Font font;
    private Color foreground;
    private final float verticalPixelsPerPoint;
    private float verticalPointsPerPixel = BaseField.BORDER_WIDTH_THIN;
    private final HSSFWorkbook workbook;

    public EscherGraphics(HSSFShapeGroup escherGroup, HSSFWorkbook workbook, Color forecolor, float verticalPointsPerPixel) {
        this.escherGroup = escherGroup;
        this.workbook = workbook;
        this.verticalPointsPerPixel = verticalPointsPerPixel;
        this.verticalPixelsPerPoint = BaseField.BORDER_WIDTH_THIN / verticalPointsPerPixel;
        this.font = new Font(HSSFFont.FONT_ARIAL, 0, 10);
        this.foreground = forecolor;
    }

    EscherGraphics(HSSFShapeGroup escherGroup, HSSFWorkbook workbook, Color foreground, Font font, float verticalPointsPerPixel) {
        this.escherGroup = escherGroup;
        this.workbook = workbook;
        this.foreground = foreground;
        this.font = font;
        this.verticalPointsPerPixel = verticalPointsPerPixel;
        this.verticalPixelsPerPoint = BaseField.BORDER_WIDTH_THIN / verticalPointsPerPixel;
    }

    @NotImplemented
    public void clearRect(int x, int y, int width, int height) {
        Color color = this.foreground;
        setColor(this.background);
        fillRect(x, y, width, height);
        setColor(color);
    }

    @NotImplemented
    public void clipRect(int x, int y, int width, int height) {
        if (logger.check(5)) {
            logger.log(5, "clipRect not supported");
        }
    }

    @NotImplemented
    public void copyArea(int x, int y, int width, int height, int dx, int dy) {
        if (logger.check(5)) {
            logger.log(5, "copyArea not supported");
        }
    }

    public Graphics create() {
        return new EscherGraphics(this.escherGroup, this.workbook, this.foreground, this.font, this.verticalPointsPerPixel);
    }

    public void dispose() {
    }

    @NotImplemented
    public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        if (logger.check(5)) {
            logger.log(5, "drawArc not supported");
        }
    }

    @NotImplemented
    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color bgcolor, ImageObserver observer) {
        if (logger.check(5)) {
            logger.log(5, "drawImage not supported");
        }
        return true;
    }

    @NotImplemented
    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
        if (logger.check(5)) {
            logger.log(5, "drawImage not supported");
        }
        return true;
    }

    public boolean drawImage(Image image, int i, int j, int k, int l, Color color, ImageObserver imageobserver) {
        return drawImage(image, i, j, i + k, j + l, 0, 0, image.getWidth(imageobserver), image.getHeight(imageobserver), color, imageobserver);
    }

    public boolean drawImage(Image image, int i, int j, int k, int l, ImageObserver imageobserver) {
        return drawImage(image, i, j, i + k, j + l, 0, 0, image.getWidth(imageobserver), image.getHeight(imageobserver), imageobserver);
    }

    public boolean drawImage(Image image, int i, int j, Color color, ImageObserver imageobserver) {
        return drawImage(image, i, j, image.getWidth(imageobserver), image.getHeight(imageobserver), color, imageobserver);
    }

    public boolean drawImage(Image image, int i, int j, ImageObserver imageobserver) {
        return drawImage(image, i, j, image.getWidth(imageobserver), image.getHeight(imageobserver), imageobserver);
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        drawLine(x1, y1, x2, y2, 0);
    }

    public void drawLine(int x1, int y1, int x2, int y2, int width) {
        HSSFSimpleShape shape = this.escherGroup.createShape(new HSSFChildAnchor(x1, y1, x2, y2));
        shape.setShapeType(20);
        shape.setLineWidth(width);
        shape.setLineStyleColor(this.foreground.getRed(), this.foreground.getGreen(), this.foreground.getBlue());
    }

    public void drawOval(int x, int y, int width, int height) {
        HSSFSimpleShape shape = this.escherGroup.createShape(new HSSFChildAnchor(x, y, x + width, y + height));
        shape.setShapeType(3);
        shape.setLineWidth(0);
        shape.setLineStyleColor(this.foreground.getRed(), this.foreground.getGreen(), this.foreground.getBlue());
        shape.setNoFill(true);
    }

    public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        int right = findBiggest(xPoints);
        int bottom = findBiggest(yPoints);
        int left = findSmallest(xPoints);
        int top = findSmallest(yPoints);
        HSSFPolygon shape = this.escherGroup.createPolygon(new HSSFChildAnchor(left, top, right, bottom));
        shape.setPolygonDrawArea(right - left, bottom - top);
        shape.setPoints(addToAll(xPoints, -left), addToAll(yPoints, -top));
        shape.setLineStyleColor(this.foreground.getRed(), this.foreground.getGreen(), this.foreground.getBlue());
        shape.setLineWidth(0);
        shape.setNoFill(true);
    }

    private int[] addToAll(int[] values, int amount) {
        int[] result = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = values[i] + amount;
        }
        return result;
    }

    @NotImplemented
    public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
        if (logger.check(5)) {
            logger.log(5, "drawPolyline not supported");
        }
    }

    @NotImplemented
    public void drawRect(int x, int y, int width, int height) {
        if (logger.check(5)) {
            logger.log(5, "drawRect not supported");
        }
    }

    @NotImplemented
    public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        if (logger.check(5)) {
            logger.log(5, "drawRoundRect not supported");
        }
    }

    public void drawString(String str, int x, int y) {
        if (str != null && !str.equals("")) {
            Font excelFont = this.font;
            if (this.font.getName().equals("SansSerif")) {
                excelFont = new Font(HSSFFont.FONT_ARIAL, this.font.getStyle(), (int) (((float) this.font.getSize()) / this.verticalPixelsPerPoint));
            } else {
                excelFont = new Font(this.font.getName(), this.font.getStyle(), (int) (((float) this.font.getSize()) / this.verticalPixelsPerPoint));
            }
            y = (int) (((float) y) - ((((float) this.font.getSize()) / this.verticalPixelsPerPoint) + (BaseField.BORDER_WIDTH_MEDIUM * this.verticalPixelsPerPoint)));
            HSSFTextbox textbox = this.escherGroup.createTextbox(new HSSFChildAnchor(x, y, x + ((StaticFontMetrics.getFontDetails(excelFont).getStringWidth(str) * 8) + 12), y + (((int) ((((float) this.font.getSize()) / this.verticalPixelsPerPoint) + 6.0f)) * 2)));
            textbox.setNoFill(true);
            textbox.setLineStyle(-1);
            HSSFRichTextString s = new HSSFRichTextString(str);
            s.applyFont(matchFont(excelFont));
            textbox.setString(s);
        }
    }

    private HSSFFont matchFont(Font matchFont) {
        boolean bold;
        boolean italic;
        short s;
        short s2 = (short) 700;
        HSSFColor hssfColor = this.workbook.getCustomPalette().findColor((byte) this.foreground.getRed(), (byte) this.foreground.getGreen(), (byte) this.foreground.getBlue());
        if (hssfColor == null) {
            hssfColor = this.workbook.getCustomPalette().findSimilarColor((byte) this.foreground.getRed(), (byte) this.foreground.getGreen(), (byte) this.foreground.getBlue());
        }
        if ((matchFont.getStyle() & 1) != 0) {
            bold = true;
        } else {
            bold = false;
        }
        if ((matchFont.getStyle() & 2) != 0) {
            italic = true;
        } else {
            italic = false;
        }
        HSSFWorkbook hSSFWorkbook = this.workbook;
        if (bold) {
            s = (short) 700;
        } else {
            s = (short) 0;
        }
        HSSFFont hssfFont = hSSFWorkbook.findFont(s, hssfColor.getIndex(), (short) (matchFont.getSize() * 20), matchFont.getName(), italic, false, (short) 0, (byte) 0);
        if (hssfFont == null) {
            hssfFont = this.workbook.createFont();
            if (!bold) {
                s2 = (short) 0;
            }
            hssfFont.setBoldweight(s2);
            hssfFont.setColor(hssfColor.getIndex());
            hssfFont.setFontHeight((short) (matchFont.getSize() * 20));
            hssfFont.setFontName(matchFont.getName());
            hssfFont.setItalic(italic);
            hssfFont.setStrikeout(false);
            hssfFont.setTypeOffset((short) 0);
            hssfFont.setUnderline((byte) 0);
        }
        return hssfFont;
    }

    public void drawString(AttributedCharacterIterator iterator, int x, int y) {
        if (logger.check(5)) {
            logger.log(5, "drawString not supported");
        }
    }

    public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        if (logger.check(5)) {
            logger.log(5, "fillArc not supported");
        }
    }

    public void fillOval(int x, int y, int width, int height) {
        HSSFSimpleShape shape = this.escherGroup.createShape(new HSSFChildAnchor(x, y, x + width, y + height));
        shape.setShapeType(3);
        shape.setLineStyle(-1);
        shape.setFillColor(this.foreground.getRed(), this.foreground.getGreen(), this.foreground.getBlue());
        shape.setLineStyleColor(this.foreground.getRed(), this.foreground.getGreen(), this.foreground.getBlue());
        shape.setNoFill(false);
    }

    public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        int right = findBiggest(xPoints);
        int bottom = findBiggest(yPoints);
        int left = findSmallest(xPoints);
        int top = findSmallest(yPoints);
        HSSFPolygon shape = this.escherGroup.createPolygon(new HSSFChildAnchor(left, top, right, bottom));
        shape.setPolygonDrawArea(right - left, bottom - top);
        shape.setPoints(addToAll(xPoints, -left), addToAll(yPoints, -top));
        shape.setLineStyleColor(this.foreground.getRed(), this.foreground.getGreen(), this.foreground.getBlue());
        shape.setFillColor(this.foreground.getRed(), this.foreground.getGreen(), this.foreground.getBlue());
    }

    private int findBiggest(int[] values) {
        int result = Integer.MIN_VALUE;
        for (int i = 0; i < values.length; i++) {
            if (values[i] > result) {
                result = values[i];
            }
        }
        return result;
    }

    private int findSmallest(int[] values) {
        int result = Integer.MAX_VALUE;
        for (int i = 0; i < values.length; i++) {
            if (values[i] < result) {
                result = values[i];
            }
        }
        return result;
    }

    public void fillRect(int x, int y, int width, int height) {
        HSSFSimpleShape shape = this.escherGroup.createShape(new HSSFChildAnchor(x, y, x + width, y + height));
        shape.setShapeType(1);
        shape.setLineStyle(-1);
        shape.setFillColor(this.foreground.getRed(), this.foreground.getGreen(), this.foreground.getBlue());
        shape.setLineStyleColor(this.foreground.getRed(), this.foreground.getGreen(), this.foreground.getBlue());
    }

    public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        if (logger.check(5)) {
            logger.log(5, "fillRoundRect not supported");
        }
    }

    public Shape getClip() {
        return getClipBounds();
    }

    public Rectangle getClipBounds() {
        return null;
    }

    public Color getColor() {
        return this.foreground;
    }

    public Font getFont() {
        return this.font;
    }

    @SuppressForbidden
    public FontMetrics getFontMetrics(Font f) {
        return Toolkit.getDefaultToolkit().getFontMetrics(f);
    }

    public void setClip(int x, int y, int width, int height) {
        setClip(new Rectangle(x, y, width, height));
    }

    @NotImplemented
    public void setClip(Shape shape) {
    }

    public void setColor(Color color) {
        this.foreground = color;
    }

    public void setFont(Font f) {
        this.font = f;
    }

    @NotImplemented
    public void setPaintMode() {
        if (logger.check(5)) {
            logger.log(5, "setPaintMode not supported");
        }
    }

    @NotImplemented
    public void setXORMode(Color color) {
        if (logger.check(5)) {
            logger.log(5, "setXORMode not supported");
        }
    }

    @NotImplemented
    public void translate(int x, int y) {
        if (logger.check(5)) {
            logger.log(5, "translate not supported");
        }
    }

    public Color getBackground() {
        return this.background;
    }

    public void setBackground(Color background) {
        this.background = background;
    }

    HSSFShapeGroup getEscherGraphics() {
        return this.escherGroup;
    }
}

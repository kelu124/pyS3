package org.apache.poi.sl.draw;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import org.apache.poi.sl.usermodel.Insets2D;
import org.apache.poi.sl.usermodel.PlaceableShape;
import org.apache.poi.sl.usermodel.ShapeContainer;
import org.apache.poi.sl.usermodel.TextParagraph;
import org.apache.poi.sl.usermodel.TextParagraph.BulletStyle;
import org.apache.poi.sl.usermodel.TextRun;
import org.apache.poi.sl.usermodel.TextShape;
import org.apache.poi.sl.usermodel.TextShape.TextDirection;

public class DrawTextShape extends DrawSimpleShape {
    public DrawTextShape(TextShape<?, ?> shape) {
        super(shape);
    }

    public void drawContent(Graphics2D graphics) {
        DrawFactory.getInstance(graphics).fixFonts(graphics);
        TextShape<?, ?> s = getShape();
        Rectangle2D anchor = DrawShape.getAnchor(graphics, (PlaceableShape) s);
        Insets2D insets = s.getInsets();
        double x = anchor.getX() + insets.left;
        double y = anchor.getY();
        AffineTransform tx = graphics.getTransform();
        boolean vertFlip = s.getFlipVertical();
        boolean horzFlip = s.getFlipHorizontal();
        ShapeContainer<?, ?> sc = s.getParent();
        while (sc instanceof PlaceableShape) {
            PlaceableShape<?, ?> ps = (PlaceableShape) sc;
            vertFlip ^= ps.getFlipVertical();
            horzFlip ^= ps.getFlipHorizontal();
            sc = ps.getParent();
        }
        if ((horzFlip ^ vertFlip) != 0) {
            double ax = anchor.getX();
            double ay = anchor.getY();
            graphics.translate(anchor.getWidth() + ax, ay);
            graphics.scale(-1.0d, 1.0d);
            graphics.translate(-ax, -ay);
        }
        Double textRot = s.getTextRotation();
        if (!(textRot == null || textRot.doubleValue() == 0.0d)) {
            double cx = anchor.getCenterX();
            double cy = anchor.getCenterY();
            graphics.translate(cx, cy);
            graphics.rotate(Math.toRadians(textRot.doubleValue()));
            graphics.translate(-cx, -cy);
        }
        switch (s.getVerticalAlignment()) {
            case BOTTOM:
                y += (anchor.getHeight() - getTextHeight(graphics)) - insets.bottom;
                break;
            case MIDDLE:
                y += insets.top + ((((anchor.getHeight() - getTextHeight(graphics)) - insets.top) - insets.bottom) / 2.0d);
                break;
            default:
                y += insets.top;
                break;
        }
        TextDirection textDir = s.getTextDirection();
        if (textDir == TextDirection.VERTICAL || textDir == TextDirection.VERTICAL_270) {
            double deg = textDir == TextDirection.VERTICAL ? 90.0d : 270.0d;
            cx = anchor.getCenterX();
            cy = anchor.getCenterY();
            graphics.translate(cx, cy);
            graphics.rotate(Math.toRadians(deg));
            graphics.translate(-cx, -cy);
            double dx = (anchor.getWidth() - anchor.getHeight()) / 2.0d;
            graphics.translate(dx, -dx);
        }
        drawParagraphs(graphics, x, y);
        graphics.setTransform(tx);
    }

    public double drawParagraphs(Graphics2D graphics, double x, double y) {
        DrawFactory fact = DrawFactory.getInstance(graphics);
        double y0 = y;
        Iterator<? extends TextParagraph<?, ?, ? extends TextRun>> paragraphs = getShape().iterator();
        boolean isFirstLine = true;
        int autoNbrIdx = 0;
        while (paragraphs.hasNext()) {
            TextParagraph p = (TextParagraph) paragraphs.next();
            DrawTextParagraph dp = fact.getDrawable(p);
            BulletStyle bs = p.getBulletStyle();
            if (bs == null || bs.getAutoNumberingScheme() == null) {
                autoNbrIdx = -1;
            } else {
                Integer startAt = bs.getAutoNumberingStartAt();
                if (startAt == null) {
                    startAt = Integer.valueOf(1);
                }
                if (startAt.intValue() > autoNbrIdx) {
                    autoNbrIdx = startAt.intValue();
                }
            }
            dp.setAutoNumberingIdx(autoNbrIdx);
            dp.breakText(graphics);
            if (!isFirstLine) {
                Double spaceBefore = p.getSpaceBefore();
                if (spaceBefore == null) {
                    spaceBefore = Double.valueOf(0.0d);
                }
                if (spaceBefore.doubleValue() > 0.0d) {
                    y += (spaceBefore.doubleValue() * 0.01d) * ((double) dp.getFirstLineHeight());
                } else {
                    y += -spaceBefore.doubleValue();
                }
            }
            isFirstLine = false;
            dp.setPosition(x, y);
            dp.draw(graphics);
            y += dp.getY();
            if (paragraphs.hasNext()) {
                Double spaceAfter = p.getSpaceAfter();
                if (spaceAfter == null) {
                    spaceAfter = Double.valueOf(0.0d);
                }
                if (spaceAfter.doubleValue() > 0.0d) {
                    y += (spaceAfter.doubleValue() * 0.01d) * ((double) dp.getLastLineHeight());
                } else {
                    y += -spaceAfter.doubleValue();
                }
            }
            autoNbrIdx++;
        }
        return y - y0;
    }

    public double getTextHeight() {
        return getTextHeight(null);
    }

    protected double getTextHeight(Graphics2D oldGraphics) {
        Graphics2D graphics = new BufferedImage(1, 1, 1).createGraphics();
        if (oldGraphics != null) {
            graphics.addRenderingHints(oldGraphics.getRenderingHints());
            graphics.setTransform(oldGraphics.getTransform());
        }
        DrawFactory.getInstance(graphics).fixFonts(graphics);
        return drawParagraphs(graphics, 0.0d, 0.0d);
    }

    protected TextShape<?, ?> getShape() {
        return (TextShape) this.shape;
    }
}

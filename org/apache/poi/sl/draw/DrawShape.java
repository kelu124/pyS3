package org.apache.poi.sl.draw;

import com.itextpdf.text.pdf.BaseField;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Locale;
import org.apache.poi.sl.usermodel.PlaceableShape;
import org.apache.poi.sl.usermodel.Shape;
import org.apache.poi.sl.usermodel.StrokeStyle;
import org.apache.poi.sl.usermodel.StrokeStyle.LineCap;
import org.apache.poi.sl.usermodel.StrokeStyle.LineDash;
import org.bytedeco.javacpp.avutil;

public class DrawShape implements Drawable {
    protected final Shape<?, ?> shape;

    public DrawShape(Shape<?, ?> shape) {
        this.shape = shape;
    }

    public void applyTransform(Graphics2D graphics) {
        if (this.shape instanceof PlaceableShape) {
            PlaceableShape<?, ?> ps = this.shape;
            AffineTransform tx = (AffineTransform) graphics.getRenderingHint(Drawable.GROUP_TRANSFORM);
            if (tx == null) {
                tx = new AffineTransform();
            }
            Rectangle2D anchor = tx.createTransformedShape(ps.getAnchor()).getBounds2D();
            double rotation = ps.getRotation();
            if (rotation != 0.0d) {
                double centerX = anchor.getCenterX();
                double centerY = anchor.getCenterY();
                rotation %= 360.0d;
                if (rotation < 0.0d) {
                    rotation += 360.0d;
                }
                int quadrant = ((((int) rotation) + 45) / 90) % 4;
                double scaleX = 1.0d;
                double scaleY = 1.0d;
                if (quadrant == 1 || quadrant == 3) {
                    AffineTransform txs;
                    if (ps.getClass().getCanonicalName().toLowerCase(Locale.ROOT).contains("hslf")) {
                        AffineTransform affineTransform = new AffineTransform(tx);
                    } else {
                        txs = new AffineTransform();
                        txs.translate(centerX, centerY);
                        txs.rotate(avutil.M_PI_2);
                        txs.translate(-centerX, -centerY);
                        txs.concatenate(tx);
                    }
                    txs.translate(centerX, centerY);
                    txs.rotate(avutil.M_PI_2);
                    txs.translate(-centerX, -centerY);
                    Rectangle2D anchor2 = txs.createTransformedShape(ps.getAnchor()).getBounds2D();
                    scaleX = safeScale(anchor.getWidth(), anchor2.getWidth());
                    scaleY = safeScale(anchor.getHeight(), anchor2.getHeight());
                } else {
                    quadrant = 0;
                }
                graphics.translate(centerX, centerY);
                double rot = Math.toRadians(rotation - (((double) quadrant) * 90.0d));
                if (rot != 0.0d) {
                    graphics.rotate(rot);
                }
                graphics.scale(scaleX, scaleY);
                rot = Math.toRadians((double) (quadrant * 90));
                if (rot != 0.0d) {
                    graphics.rotate(rot);
                }
                graphics.translate(-centerX, -centerY);
            }
            if (ps.getFlipHorizontal()) {
                graphics.translate(anchor.getX() + anchor.getWidth(), anchor.getY());
                graphics.scale(-1.0d, 1.0d);
                graphics.translate(-anchor.getX(), -anchor.getY());
            }
            if (ps.getFlipVertical()) {
                graphics.translate(anchor.getX(), anchor.getY() + anchor.getHeight());
                graphics.scale(1.0d, -1.0d);
                graphics.translate(-anchor.getX(), -anchor.getY());
            }
        }
    }

    private static double safeScale(double dim1, double dim2) {
        if (dim1 == 0.0d || dim2 == 0.0d) {
            return 1.0d;
        }
        return dim1 / dim2;
    }

    public void draw(Graphics2D graphics) {
    }

    public void drawContent(Graphics2D graphics) {
    }

    public static Rectangle2D getAnchor(Graphics2D graphics, PlaceableShape<?, ?> shape) {
        return getAnchor(graphics, shape.getAnchor());
    }

    public static Rectangle2D getAnchor(Graphics2D graphics, Rectangle2D anchor) {
        if (graphics == null) {
            return anchor;
        }
        AffineTransform tx = (AffineTransform) graphics.getRenderingHint(Drawable.GROUP_TRANSFORM);
        if (tx != null) {
            anchor = tx.createTransformedShape(anchor).getBounds2D();
        }
        return anchor;
    }

    protected Shape<?, ?> getShape() {
        return this.shape;
    }

    protected static BasicStroke getStroke(StrokeStyle strokeStyle) {
        int lineCap;
        float lineWidth = (float) strokeStyle.getLineWidth();
        if (lineWidth == 0.0f) {
            lineWidth = 0.25f;
        }
        LineDash lineDash = strokeStyle.getLineDash();
        if (lineDash == null) {
            lineDash = LineDash.SOLID;
        }
        int[] dashPatI = lineDash.pattern;
        float[] dashPatF = null;
        if (dashPatI != null) {
            dashPatF = new float[dashPatI.length];
            for (int i = 0; i < dashPatI.length; i++) {
                dashPatF[i] = ((float) dashPatI[i]) * Math.max(BaseField.BORDER_WIDTH_THIN, lineWidth);
            }
        }
        LineCap lineCapE = strokeStyle.getLineCap();
        if (lineCapE == null) {
            lineCapE = LineCap.FLAT;
        }
        switch (lineCapE) {
            case ROUND:
                lineCap = 1;
                break;
            case SQUARE:
                lineCap = 2;
                break;
            default:
                lineCap = 0;
                break;
        }
        return new BasicStroke(lineWidth, lineCap, 1, lineWidth, dashPatF, 0.0f);
    }
}

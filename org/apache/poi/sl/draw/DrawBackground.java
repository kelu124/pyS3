package org.apache.poi.sl.draw;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import org.apache.poi.sl.usermodel.Background;
import org.apache.poi.sl.usermodel.PlaceableShape;
import org.apache.poi.sl.usermodel.ShapeContainer;
import org.apache.poi.sl.usermodel.Sheet;

public class DrawBackground extends DrawShape {
    public DrawBackground(Background<?, ?> shape) {
        super(shape);
    }

    public void draw(Graphics2D graphics) {
        Dimension pg = this.shape.getSheet().getSlideShow().getPageSize();
        final Rectangle2D anchor = new Double(0.0d, 0.0d, pg.getWidth(), pg.getHeight());
        PlaceableShape<?, ?> c10761 = new PlaceableShape() {
            public ShapeContainer<?, ?> getParent() {
                return null;
            }

            public Rectangle2D getAnchor() {
                return anchor;
            }

            public void setAnchor(Rectangle2D newAnchor) {
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
                return DrawBackground.this.shape.getSheet();
            }
        };
        Paint fill = DrawFactory.getInstance(graphics).getPaint(c10761).getPaint(graphics, getShape().getFillStyle().getPaint());
        Rectangle2D anchor2 = DrawShape.getAnchor(graphics, anchor);
        if (fill != null) {
            graphics.setRenderingHint(Drawable.GRADIENT_SHAPE, anchor);
            graphics.setPaint(fill);
            graphics.fill(anchor2);
        }
    }

    protected Background<?, ?> getShape() {
        return (Background) this.shape;
    }
}

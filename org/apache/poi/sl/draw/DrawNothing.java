package org.apache.poi.sl.draw;

import java.awt.Graphics2D;
import org.apache.poi.sl.usermodel.Shape;

public class DrawNothing implements Drawable {
    protected final Shape<?, ?> shape;

    public DrawNothing(Shape<?, ?> shape) {
        this.shape = shape;
    }

    public void applyTransform(Graphics2D graphics) {
    }

    public void draw(Graphics2D graphics) {
    }

    public void drawContent(Graphics2D context) {
    }
}

package org.apache.poi.sl.draw;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import org.apache.poi.sl.usermodel.GroupShape;
import org.apache.poi.sl.usermodel.Shape;

public class DrawGroupShape extends DrawShape {
    public DrawGroupShape(GroupShape<?, ?> shape) {
        super(shape);
    }

    public void draw(Graphics2D graphics) {
        double scaleY;
        Rectangle2D interior = getShape().getInteriorAnchor();
        Rectangle2D exterior = getShape().getAnchor();
        AffineTransform tx = (AffineTransform) graphics.getRenderingHint(Drawable.GROUP_TRANSFORM);
        AffineTransform affineTransform = new AffineTransform(tx);
        double scaleX = interior.getWidth() == 0.0d ? 1.0d : exterior.getWidth() / interior.getWidth();
        if (interior.getHeight() == 0.0d) {
            scaleY = 1.0d;
        } else {
            scaleY = exterior.getHeight() / interior.getHeight();
        }
        tx.translate(exterior.getX(), exterior.getY());
        tx.scale(scaleX, scaleY);
        tx.translate(-interior.getX(), -interior.getY());
        DrawFactory drawFact = DrawFactory.getInstance(graphics);
        AffineTransform at2 = graphics.getTransform();
        for (Shape child : getShape()) {
            AffineTransform at = graphics.getTransform();
            graphics.setRenderingHint(Drawable.GSAVE, Boolean.valueOf(true));
            Drawable draw = drawFact.getDrawable(child);
            draw.applyTransform(graphics);
            draw.draw(graphics);
            graphics.setTransform(at);
            graphics.setRenderingHint(Drawable.GRESTORE, Boolean.valueOf(true));
        }
        graphics.setTransform(at2);
        graphics.setRenderingHint(Drawable.GROUP_TRANSFORM, affineTransform);
    }

    protected GroupShape<?, ?> getShape() {
        return (GroupShape) this.shape;
    }
}

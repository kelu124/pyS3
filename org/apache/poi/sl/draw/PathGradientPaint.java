package org.apache.poi.sl.draw;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint.ColorSpaceType;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.IllegalPathStateException;
import java.awt.geom.Point2D.Double;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

class PathGradientPaint implements Paint {
    protected final int capStyle;
    protected final Color[] colors;
    protected final float[] fractions;
    protected final int joinStyle;
    protected final int transparency;

    class PathGradientContext implements PaintContext {
        protected final Rectangle deviceBounds;
        protected final int gradientSteps;
        protected final RenderingHints hints;
        protected final PaintContext pCtx;
        WritableRaster raster;
        protected final Shape shape;
        protected final Rectangle2D userBounds;
        protected final AffineTransform xform;

        public PathGradientContext(ColorModel cm, Rectangle deviceBounds, Rectangle2D userBounds, AffineTransform xform, RenderingHints hints) {
            this.shape = (Shape) hints.get(Drawable.GRADIENT_SHAPE);
            if (this.shape == null) {
                throw new IllegalPathStateException("PathGradientPaint needs a shape to be set via the rendering hint Drawable.GRADIANT_SHAPE.");
            }
            this.deviceBounds = deviceBounds;
            this.userBounds = userBounds;
            this.xform = xform;
            this.hints = hints;
            this.gradientSteps = getGradientSteps(this.shape);
            LinearGradientPaint gradientPaint = new LinearGradientPaint(new Double(0.0d, 0.0d), new Double((double) this.gradientSteps, 0.0d), PathGradientPaint.this.fractions, PathGradientPaint.this.colors, CycleMethod.NO_CYCLE, ColorSpaceType.SRGB, new AffineTransform());
            Rectangle bounds = new Rectangle(0, 0, this.gradientSteps, 1);
            this.pCtx = gradientPaint.createContext(cm, bounds, bounds, new AffineTransform(), hints);
        }

        public void dispose() {
        }

        public ColorModel getColorModel() {
            return this.pCtx.getColorModel();
        }

        public Raster getRaster(int xOffset, int yOffset, int w, int h) {
            ColorModel cm = getColorModel();
            if (this.raster == null) {
                createRaster();
            }
            WritableRaster childRaster = cm.createCompatibleWritableRaster(w, h);
            Rectangle2D childRect = new Rectangle2D.Double((double) xOffset, (double) yOffset, (double) w, (double) h);
            if (childRect.intersects(this.deviceBounds)) {
                Rectangle2D destRect = new Rectangle2D.Double();
                Rectangle2D.intersect(childRect, this.deviceBounds, destRect);
                int dw = (int) destRect.getWidth();
                int dh = (int) destRect.getHeight();
                WritableRaster writableRaster = childRaster;
                writableRaster.setDataElements((int) (destRect.getX() - childRect.getX()), (int) (destRect.getY() - childRect.getY()), dw, dh, this.raster.getDataElements((int) (destRect.getX() - this.deviceBounds.getX()), (int) (destRect.getY() - this.deviceBounds.getY()), dw, dh, null));
            }
            return childRaster;
        }

        protected int getGradientSteps(Shape gradientShape) {
            Rectangle rect = gradientShape.getBounds();
            int lower = 1;
            int upper = (int) (Math.max(rect.getWidth(), rect.getHeight()) / 2.0d);
            while (lower < upper - 1) {
                int mid = lower + ((upper - lower) / 2);
                if (new Area(new BasicStroke((float) mid, PathGradientPaint.this.capStyle, PathGradientPaint.this.joinStyle).createStrokedShape(gradientShape)).isSingular()) {
                    upper = mid;
                } else {
                    lower = mid;
                }
            }
            return upper;
        }

        protected void createRaster() {
            ColorModel cm = getColorModel();
            this.raster = cm.createCompatibleWritableRaster((int) this.deviceBounds.getWidth(), (int) this.deviceBounds.getHeight());
            Graphics2D graphics = new BufferedImage(cm, this.raster, false, null).createGraphics();
            graphics.setRenderingHints(this.hints);
            graphics.translate(-this.deviceBounds.getX(), -this.deviceBounds.getY());
            graphics.transform(this.xform);
            Raster img2 = this.pCtx.getRaster(0, 0, this.gradientSteps, 1);
            int[] rgb = new int[cm.getNumComponents()];
            for (int i = this.gradientSteps - 1; i >= 0; i--) {
                img2.getPixel(i, 0, rgb);
                Color c = new Color(rgb[0], rgb[1], rgb[2]);
                if (rgb.length == 4) {
                    graphics.setComposite(AlphaComposite.getInstance(2, ((float) rgb[3]) / 255.0f));
                }
                graphics.setStroke(new BasicStroke((float) (i + 1), PathGradientPaint.this.capStyle, PathGradientPaint.this.joinStyle));
                graphics.setColor(c);
                graphics.draw(this.shape);
            }
            graphics.dispose();
        }
    }

    public PathGradientPaint(Color[] colors, float[] fractions) {
        this(colors, fractions, 1, 1);
    }

    public PathGradientPaint(Color[] colors, float[] fractions, int capStyle, int joinStyle) {
        int i = 1;
        this.colors = (Color[]) colors.clone();
        this.fractions = (float[]) fractions.clone();
        this.capStyle = capStyle;
        this.joinStyle = joinStyle;
        boolean opaque = true;
        for (Color c : colors) {
            if (c != null) {
                opaque = opaque && c.getAlpha() == 255;
            }
        }
        if (!opaque) {
            i = 3;
        }
        this.transparency = i;
    }

    public PaintContext createContext(ColorModel cm, Rectangle deviceBounds, Rectangle2D userBounds, AffineTransform transform, RenderingHints hints) {
        return new PathGradientContext(cm, deviceBounds, userBounds, transform, hints);
    }

    public int getTransparency() {
        return this.transparency;
    }
}

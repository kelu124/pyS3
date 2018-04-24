package org.apache.poi.sl.draw;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.io.IOException;
import org.apache.poi.sl.usermodel.PictureData;
import org.apache.poi.sl.usermodel.PictureData.PictureType;
import org.apache.poi.sl.usermodel.PictureShape;
import org.apache.poi.sl.usermodel.RectAlign;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

public class DrawPictureShape extends DrawSimpleShape {
    private static final POILogger LOG = POILogFactory.getLogger(DrawPictureShape.class);
    private static final String WMF_IMAGE_RENDERER = "org.apache.poi.hwmf.draw.HwmfSLImageRenderer";

    public DrawPictureShape(PictureShape<?, ?> shape) {
        super(shape);
    }

    public void drawContent(Graphics2D graphics) {
        PictureData data = getShape().getPictureData();
        if (data != null) {
            Rectangle2D anchor = DrawShape.getAnchor(graphics, getShape());
            Insets insets = getShape().getClipping();
            try {
                ImageRenderer renderer = getImageRenderer(graphics, data.getContentType());
                renderer.loadImage(data.getData(), data.getContentType());
                renderer.drawImage(graphics, anchor, insets);
            } catch (IOException e) {
                LOG.log(7, new Object[]{"image can't be loaded/rendered.", e});
            }
        }
    }

    public static ImageRenderer getImageRenderer(Graphics2D graphics, String contentType) {
        ImageRenderer renderer = (ImageRenderer) graphics.getRenderingHint(Drawable.IMAGE_RENDERER);
        if (renderer != null) {
            return renderer;
        }
        if (PictureType.WMF.contentType.equals(contentType)) {
            try {
                return (ImageRenderer) Thread.currentThread().getContextClassLoader().loadClass(WMF_IMAGE_RENDERER).newInstance();
            } catch (Exception e) {
                LOG.log(7, new Object[]{"WMF image renderer is not on the classpath - include poi-scratchpad jar!", e});
            }
        }
        return new BitmapImageRenderer();
    }

    protected PictureShape<?, ?> getShape() {
        return (PictureShape) this.shape;
    }

    public void resize() {
        PictureShape<?, ?> ps = getShape();
        Dimension dim = ps.getPictureData().getImageDimension();
        Rectangle2D origRect = ps.getAnchor();
        ps.setAnchor(new Double(origRect.getX(), origRect.getY(), dim.getWidth(), dim.getHeight()));
    }

    public void resize(Rectangle2D target) {
        resize(target, RectAlign.CENTER);
    }

    public void resize(Rectangle2D target, RectAlign align) {
        PictureShape<?, ?> ps = getShape();
        Dimension dim = ps.getPictureData().getImageDimension();
        if (dim.width <= 0 || dim.height <= 0) {
            ps.setAnchor(target);
            return;
        }
        double w = target.getWidth();
        double h = target.getHeight();
        double sx = w / ((double) dim.width);
        double sy = h / ((double) dim.height);
        double dx = 0.0d;
        double dy = 0.0d;
        if (sx > sy) {
            w = sy * ((double) dim.width);
            dx = target.getWidth() - w;
        } else if (sy > sx) {
            h = sx * ((double) dim.height);
            dy = target.getHeight() - h;
        } else {
            ps.setAnchor(target);
            return;
        }
        double x = target.getX();
        double y = target.getY();
        switch (align) {
            case TOP:
                x += dx / 2.0d;
                break;
            case TOP_RIGHT:
                x += dx;
                break;
            case RIGHT:
                x += dx;
                y += dy / 2.0d;
                break;
            case BOTTOM_RIGHT:
                x += dx;
                y += dy;
                break;
            case BOTTOM:
                x += dx / 2.0d;
                y += dy;
                break;
            case BOTTOM_LEFT:
                y += dy;
                break;
            case LEFT:
                y += dy / 2.0d;
                break;
            case TOP_LEFT:
                break;
            default:
                x += dx / 2.0d;
                y += dy / 2.0d;
                break;
        }
        ps.setAnchor(new Double(x, y, w, h));
    }
}

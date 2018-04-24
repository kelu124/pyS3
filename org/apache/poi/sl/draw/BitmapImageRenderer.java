package org.apache.poi.sl.draw;

import com.itextpdf.text.html.HtmlTags;
import com.itextpdf.text.pdf.BaseField;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;
import org.bytedeco.javacpp.ARToolKitPlus;

public class BitmapImageRenderer implements ImageRenderer {
    private static final POILogger LOG = POILogFactory.getLogger(ImageRenderer.class);
    protected BufferedImage img;

    public void loadImage(InputStream data, String contentType) throws IOException {
        this.img = readImage(data, contentType);
    }

    public void loadImage(byte[] data, String contentType) throws IOException {
        this.img = readImage(new ByteArrayInputStream(data), contentType);
    }

    private static BufferedImage readImage(InputStream data, String contentType) throws IOException {
        Throwable th;
        IOException lastException = null;
        BufferedImage img = null;
        ImageInputStream iis = new MemoryCacheImageInputStream(data);
        try {
            ImageInputStream iis2 = new MemoryCacheImageInputStream(data);
            try {
                iis2.mark();
                Iterator<ImageReader> iter = ImageIO.getImageReaders(iis2);
                while (img == null && iter.hasNext()) {
                    ImageReader reader = (ImageReader) iter.next();
                    ImageReadParam param = reader.getDefaultReadParam();
                    int mode = 0;
                    IOException lastException2 = lastException;
                    while (img == null && mode < 2) {
                        iis2.reset();
                        iis2.mark();
                        if (mode == 1) {
                            Iterator<ImageTypeSpecifier> imageTypes = reader.getImageTypes(0);
                            while (imageTypes.hasNext()) {
                                ImageTypeSpecifier imageTypeSpecifier = (ImageTypeSpecifier) imageTypes.next();
                                if (imageTypeSpecifier.getBufferedImageType() == 10) {
                                    param.setDestinationType(imageTypeSpecifier);
                                    break;
                                }
                            }
                        }
                        try {
                            reader.setInput(iis2, false, true);
                            img = reader.read(0, param);
                            lastException = lastException2;
                        } catch (IOException e) {
                            lastException = e;
                        } catch (RuntimeException e2) {
                            lastException = new IOException("ImageIO runtime exception - " + (mode == 0 ? HtmlTags.NORMAL : "fallback"), e2);
                        } catch (Throwable th2) {
                            th = th2;
                            iis = iis2;
                            lastException = lastException2;
                        }
                        mode++;
                        lastException2 = lastException;
                    }
                    reader.dispose();
                    lastException = lastException2;
                }
                iis2.close();
                if (img == null) {
                    if (lastException != null) {
                        throw lastException;
                    }
                    LOG.log(5, new Object[]{"Content-type: " + contentType + " is not support. Image ignored."});
                    return null;
                } else if (img.getType() == 2) {
                    return img;
                } else {
                    BufferedImage argbImg = new BufferedImage(img.getWidth(), img.getHeight(), 2);
                    Graphics g = argbImg.getGraphics();
                    g.drawImage(img, 0, 0, null);
                    g.dispose();
                    return argbImg;
                }
            } catch (Throwable th3) {
                th = th3;
                iis = iis2;
                iis.close();
                throw th;
            }
        } catch (Throwable th4) {
            th = th4;
            iis.close();
            throw th;
        }
    }

    public BufferedImage getImage() {
        return this.img;
    }

    public BufferedImage getImage(Dimension dim) {
        double w_old = (double) this.img.getWidth();
        double h_old = (double) this.img.getHeight();
        BufferedImage scaled = new BufferedImage((int) w_old, (int) h_old, 2);
        double w_new = dim.getWidth();
        double h_new = dim.getHeight();
        AffineTransform at = new AffineTransform();
        at.scale(w_new / w_old, h_new / h_old);
        new AffineTransformOp(at, 2).filter(this.img, scaled);
        return scaled;
    }

    public Dimension getDimension() {
        return this.img == null ? new Dimension(0, 0) : new Dimension(this.img.getWidth(), this.img.getHeight());
    }

    public void setAlpha(double alpha) {
        if (this.img != null) {
            Dimension dim = getDimension();
            BufferedImage newImg = new BufferedImage((int) dim.getWidth(), (int) dim.getHeight(), 2);
            Graphics2D g = newImg.createGraphics();
            g.drawImage(this.img, new RescaleOp(new float[]{BaseField.BORDER_WIDTH_THIN, BaseField.BORDER_WIDTH_THIN, BaseField.BORDER_WIDTH_THIN, (float) alpha}, new float[]{0.0f, 0.0f, 0.0f, 0.0f}, null), 0, 0);
            g.dispose();
            this.img = newImg;
        }
    }

    public boolean drawImage(Graphics2D graphics, Rectangle2D anchor) {
        return drawImage(graphics, anchor, null);
    }

    public boolean drawImage(Graphics2D graphics, Rectangle2D anchor, Insets clip) {
        if (this.img == null) {
            return false;
        }
        boolean isClipped = true;
        if (clip == null) {
            isClipped = false;
            Insets insets = new Insets(0, 0, 0, 0);
        }
        int iw = this.img.getWidth();
        int ih = this.img.getHeight();
        double sx = anchor.getWidth() / (((double) iw) * (((double) ((ARToolKitPlus.AR_AREA_MAX - clip.left) - clip.right)) / 100000.0d));
        double sy = anchor.getHeight() / (((double) ih) * (((double) ((ARToolKitPlus.AR_AREA_MAX - clip.top) - clip.bottom)) / 100000.0d));
        AffineTransform at = new AffineTransform(sx, 0.0d, 0.0d, sy, anchor.getX() - (((((double) iw) * sx) * ((double) clip.left)) / 100000.0d), anchor.getY() - (((((double) ih) * sy) * ((double) clip.top)) / 100000.0d));
        Shape clipOld = graphics.getClip();
        if (isClipped) {
            graphics.clip(anchor.getBounds2D());
        }
        graphics.drawRenderedImage(this.img, at);
        graphics.setClip(clipOld);
        return true;
    }
}

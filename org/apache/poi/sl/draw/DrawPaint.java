package org.apache.poi.sl.draw;

import com.itextpdf.text.pdf.BaseField;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint.ColorSpaceType;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.sl.usermodel.ColorStyle;
import org.apache.poi.sl.usermodel.PaintStyle;
import org.apache.poi.sl.usermodel.PaintStyle.GradientPaint;
import org.apache.poi.sl.usermodel.PaintStyle.SolidPaint;
import org.apache.poi.sl.usermodel.PaintStyle.TexturePaint;
import org.apache.poi.sl.usermodel.PlaceableShape;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;
import org.bytedeco.javacpp.ARToolKitPlus;

public class DrawPaint {
    static final /* synthetic */ boolean $assertionsDisabled = (!DrawPaint.class.desiredAssertionStatus());
    private static final POILogger LOG = POILogFactory.getLogger(DrawPaint.class);
    private static final Color TRANSPARENT = new Color(BaseField.BORDER_WIDTH_THIN, BaseField.BORDER_WIDTH_THIN, BaseField.BORDER_WIDTH_THIN, 0.0f);
    protected PlaceableShape<?, ?> shape;

    private static class SimpleSolidPaint implements SolidPaint {
        private final ColorStyle solidColor;

        SimpleSolidPaint(final Color color) {
            if (color == null) {
                throw new NullPointerException("Color needs to be specified");
            }
            this.solidColor = new ColorStyle() {
                public Color getColor() {
                    return new Color(color.getRed(), color.getGreen(), color.getBlue());
                }

                public int getAlpha() {
                    return (int) Math.round((((double) color.getAlpha()) * 100000.0d) / 255.0d);
                }

                public int getHueOff() {
                    return -1;
                }

                public int getHueMod() {
                    return -1;
                }

                public int getSatOff() {
                    return -1;
                }

                public int getSatMod() {
                    return -1;
                }

                public int getLumOff() {
                    return -1;
                }

                public int getLumMod() {
                    return -1;
                }

                public int getShade() {
                    return -1;
                }

                public int getTint() {
                    return -1;
                }
            };
        }

        SimpleSolidPaint(ColorStyle color) {
            if (color == null) {
                throw new NullPointerException("Color needs to be specified");
            }
            this.solidColor = color;
        }

        public ColorStyle getSolidColor() {
            return this.solidColor;
        }
    }

    public DrawPaint(PlaceableShape<?, ?> shape) {
        this.shape = shape;
    }

    public static SolidPaint createSolidPaint(Color color) {
        return color == null ? null : new SimpleSolidPaint(color);
    }

    public static SolidPaint createSolidPaint(ColorStyle color) {
        return color == null ? null : new SimpleSolidPaint(color);
    }

    public Paint getPaint(Graphics2D graphics, PaintStyle paint) {
        if (paint instanceof SolidPaint) {
            return getSolidPaint((SolidPaint) paint, graphics);
        }
        if (paint instanceof GradientPaint) {
            return getGradientPaint((GradientPaint) paint, graphics);
        }
        if (paint instanceof TexturePaint) {
            return getTexturePaint((TexturePaint) paint, graphics);
        }
        return null;
    }

    protected Paint getSolidPaint(SolidPaint fill, Graphics2D graphics) {
        return applyColorTransform(fill.getSolidColor());
    }

    protected Paint getGradientPaint(GradientPaint fill, Graphics2D graphics) {
        switch (fill.getGradientType()) {
            case linear:
                return createLinearGradientPaint(fill, graphics);
            case circular:
                return createRadialGradientPaint(fill, graphics);
            case shape:
                return createPathGradientPaint(fill, graphics);
            default:
                throw new UnsupportedOperationException("gradient fill of type " + fill + " not supported.");
        }
    }

    protected Paint getTexturePaint(TexturePaint fill, Graphics2D graphics) {
        InputStream is = fill.getImageData();
        if (is == null) {
            return null;
        }
        if ($assertionsDisabled || graphics != null) {
            ImageRenderer renderer = DrawPictureShape.getImageRenderer(graphics, fill.getContentType());
            try {
                BufferedImage image;
                renderer.loadImage(is, fill.getContentType());
                is.close();
                int alpha = fill.getAlpha();
                if (alpha >= 0 && alpha < ARToolKitPlus.AR_AREA_MAX) {
                    renderer.setAlpha((double) (((float) alpha) / 100000.0f));
                }
                Rectangle2D textAnchor = this.shape.getAnchor();
                if ("image/x-wmf".equals(fill.getContentType())) {
                    image = renderer.getImage(new Dimension((int) textAnchor.getWidth(), (int) textAnchor.getHeight()));
                } else {
                    image = renderer.getImage();
                }
                if (image != null) {
                    return new java.awt.TexturePaint(image, textAnchor);
                }
                LOG.log(7, new Object[]{"Can't load image data"});
                return null;
            } catch (IOException e) {
                LOG.log(7, new Object[]{"Can't load image data - using transparent color", e});
                return null;
            } catch (Throwable th) {
                is.close();
            }
        }
        throw new AssertionError();
    }

    public static Color applyColorTransform(ColorStyle color) {
        if (color == null || color.getColor() == null) {
            return TRANSPARENT;
        }
        Color result = color.getColor();
        double alpha = getAlpha(result, color);
        double[] hsl = RGB2HSL(result);
        applyHslModOff(hsl, 0, color.getHueMod(), color.getHueOff());
        applyHslModOff(hsl, 1, color.getSatMod(), color.getSatOff());
        applyHslModOff(hsl, 2, color.getLumMod(), color.getLumOff());
        applyShade(hsl, color);
        applyTint(hsl, color);
        return HSL2RGB(hsl[0], hsl[1], hsl[2], alpha);
    }

    private static double getAlpha(Color c, ColorStyle fc) {
        double alpha = ((double) c.getAlpha()) / 255.0d;
        int fcAlpha = fc.getAlpha();
        if (fcAlpha != -1) {
            alpha *= ((double) fcAlpha) / 100000.0d;
        }
        return Math.min(1.0d, Math.max(0.0d, alpha));
    }

    private static void applyHslModOff(double[] hsl, int hslPart, int mod, int off) {
        if (mod == -1) {
            mod = ARToolKitPlus.AR_AREA_MAX;
        }
        if (off == -1) {
            off = 0;
        }
        if (mod != ARToolKitPlus.AR_AREA_MAX || off != 0) {
            hsl[hslPart] = (hsl[hslPart] * (((double) mod) / 100000.0d)) + (((double) off) / 1000.0d);
        }
    }

    private static void applyShade(double[] hsl, ColorStyle fc) {
        int shade = fc.getShade();
        if (shade != -1) {
            hsl[2] = hsl[2] * (((double) shade) / 100000.0d);
        }
    }

    private static void applyTint(double[] hsl, ColorStyle fc) {
        int tint = fc.getTint();
        if (tint != -1) {
            double ftint = (double) (((float) tint) / 100000.0f);
            hsl[2] = (hsl[2] * ftint) + (100.0d - (ftint * 100.0d));
        }
    }

    protected Paint createLinearGradientPaint(GradientPaint fill, Graphics2D graphics) {
        double angle = fill.getGradientAngle();
        Rectangle2D anchor = DrawShape.getAnchor(graphics, this.shape);
        AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(angle), anchor.getX() + (anchor.getWidth() / 2.0d), anchor.getY() + (anchor.getHeight() / 2.0d));
        Point2D p1 = at.transform(new Double((anchor.getX() + (anchor.getWidth() / 2.0d)) - (Math.sqrt((anchor.getHeight() * anchor.getHeight()) + (anchor.getWidth() * anchor.getWidth())) / 2.0d), anchor.getY() + (anchor.getHeight() / 2.0d)), null);
        Point2D p2 = at.transform(new Double(anchor.getX() + anchor.getWidth(), anchor.getY() + (anchor.getHeight() / 2.0d)), null);
        snapToAnchor(p1, anchor);
        snapToAnchor(p2, anchor);
        if (p1.equals(p2)) {
            return null;
        }
        float[] fractions = fill.getGradientFractions();
        Color[] colors = new Color[fractions.length];
        ColorStyle[] arr$ = fill.getGradientColors();
        int len$ = arr$.length;
        int i$ = 0;
        int i = 0;
        while (i$ < len$) {
            ColorStyle fc = arr$[i$];
            int i2 = i + 1;
            colors[i] = fc == null ? TRANSPARENT : applyColorTransform(fc);
            i$++;
            i = i2;
        }
        AffineTransform grAt = new AffineTransform();
        if (fill.isRotatedWithShape()) {
            double rotation = this.shape.getRotation();
            if (rotation != 0.0d) {
                double centerX = anchor.getX() + (anchor.getWidth() / 2.0d);
                double centerY = anchor.getY() + (anchor.getHeight() / 2.0d);
                grAt.translate(centerX, centerY);
                grAt.rotate(Math.toRadians(-rotation));
                grAt.translate(-centerX, -centerY);
            }
        }
        return new LinearGradientPaint(p1, p2, fractions, colors, CycleMethod.NO_CYCLE, ColorSpaceType.SRGB, grAt);
    }

    protected Paint createRadialGradientPaint(GradientPaint fill, Graphics2D graphics) {
        Rectangle2D anchor = DrawShape.getAnchor(graphics, this.shape);
        Point2D pCenter = new Double(anchor.getX() + (anchor.getWidth() / 2.0d), anchor.getY() + (anchor.getHeight() / 2.0d));
        float radius = (float) Math.max(anchor.getWidth(), anchor.getHeight());
        float[] fractions = fill.getGradientFractions();
        Color[] colors = new Color[fractions.length];
        ColorStyle[] arr$ = fill.getGradientColors();
        int len$ = arr$.length;
        int i$ = 0;
        int i = 0;
        while (i$ < len$) {
            int i2 = i + 1;
            colors[i] = applyColorTransform(arr$[i$]);
            i$++;
            i = i2;
        }
        return new RadialGradientPaint(pCenter, radius, fractions, colors);
    }

    protected Paint createPathGradientPaint(GradientPaint fill, Graphics2D graphics) {
        float[] fractions = fill.getGradientFractions();
        Color[] colors = new Color[fractions.length];
        ColorStyle[] arr$ = fill.getGradientColors();
        int len$ = arr$.length;
        int i$ = 0;
        int i = 0;
        while (i$ < len$) {
            int i2 = i + 1;
            colors[i] = applyColorTransform(arr$[i$]);
            i$++;
            i = i2;
        }
        return new PathGradientPaint(colors, fractions);
    }

    protected void snapToAnchor(Point2D p, Rectangle2D anchor) {
        if (p.getX() < anchor.getX()) {
            p.setLocation(anchor.getX(), p.getY());
        } else if (p.getX() > anchor.getX() + anchor.getWidth()) {
            p.setLocation(anchor.getX() + anchor.getWidth(), p.getY());
        }
        if (p.getY() < anchor.getY()) {
            p.setLocation(p.getX(), anchor.getY());
        } else if (p.getY() > anchor.getY() + anchor.getHeight()) {
            p.setLocation(p.getX(), anchor.getY() + anchor.getHeight());
        }
    }

    public static Color HSL2RGB(double h, double s, double l, double alpha) {
        s = Math.max(0.0d, Math.min(100.0d, s));
        l = Math.max(0.0d, Math.min(100.0d, l));
        if (alpha < 0.0d || alpha > 1.0d) {
            throw new IllegalArgumentException("Color parameter outside of expected range - Alpha: " + alpha);
        }
        h = (h % 360.0d) / 360.0d;
        s /= 100.0d;
        l /= 100.0d;
        double q = l < 0.5d ? l * (1.0d + s) : (l + s) - (s * l);
        double p = (2.0d * l) - q;
        return new Color((float) Math.min(Math.max(0.0d, HUE2RGB(p, q, 0.3333333333333333d + h)), 1.0d), (float) Math.min(Math.max(0.0d, HUE2RGB(p, q, h)), 1.0d), (float) Math.min(Math.max(0.0d, HUE2RGB(p, q, h - 0.3333333333333333d)), 1.0d), (float) alpha);
    }

    private static double HUE2RGB(double p, double q, double h) {
        if (h < 0.0d) {
            h += 1.0d;
        }
        if (h > 1.0d) {
            h -= 1.0d;
        }
        if (6.0d * h < 1.0d) {
            return p + (((q - p) * 6.0d) * h);
        }
        if (2.0d * h >= 1.0d) {
            return 3.0d * h < 2.0d ? p + (((q - p) * 6.0d) * (0.6666666666666666d - h)) : p;
        } else {
            return q;
        }
    }

    private static double[] RGB2HSL(Color color) {
        double s;
        float[] rgb = color.getRGBColorComponents(null);
        double r = (double) rgb[0];
        double g = (double) rgb[1];
        double b = (double) rgb[2];
        double min = Math.min(r, Math.min(g, b));
        double max = Math.max(r, Math.max(g, b));
        double h = 0.0d;
        if (max == min) {
            h = 0.0d;
        } else if (max == r) {
            h = (((60.0d * (g - b)) / (max - min)) + 360.0d) % 360.0d;
        } else if (max == g) {
            h = ((60.0d * (b - r)) / (max - min)) + 120.0d;
        } else if (max == b) {
            h = ((60.0d * (r - g)) / (max - min)) + 240.0d;
        }
        double l = (max + min) / 2.0d;
        if (max == min) {
            s = 0.0d;
        } else if (l <= 0.5d) {
            s = (max - min) / (max + min);
        } else {
            s = (max - min) / ((2.0d - max) - min);
        }
        return new double[]{h, 100.0d * s, 100.0d * l};
    }

    public static int srgb2lin(float sRGB) {
        if (((double) sRGB) <= 0.04045d) {
            return (int) Math.rint((((double) sRGB) * 100000.0d) / 12.92d);
        }
        return (int) Math.rint(Math.pow((((double) sRGB) + 0.055d) / 1.055d, 2.4d) * 100000.0d);
    }

    public static float lin2srgb(int linRGB) {
        if (((double) linRGB) <= 0.0031308d) {
            return (float) ((((double) linRGB) / 100000.0d) * 12.92d);
        }
        return (float) ((1.055d * Math.pow(((double) linRGB) / 100000.0d, 0.4166666666666667d)) - 0.055d);
    }
}

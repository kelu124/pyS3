package org.apache.poi.hssf.usermodel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.io.PrintStream;
import java.text.AttributedCharacterIterator;
import java.util.Arrays;
import java.util.Map;
import org.apache.poi.util.Internal;

public class DummyGraphics2d extends Graphics2D {
    private BufferedImage bufimg;
    private final Graphics2D g2D;
    private final PrintStream log;

    public DummyGraphics2d() {
        this(System.out);
    }

    public DummyGraphics2d(PrintStream log) {
        this.bufimg = new BufferedImage(1000, 1000, 2);
        this.g2D = (Graphics2D) this.bufimg.getGraphics();
        this.log = log;
    }

    public DummyGraphics2d(PrintStream log, Graphics2D g2D) {
        this.g2D = g2D;
        this.log = log;
    }

    public void addRenderingHints(Map<?, ?> hints) {
        this.log.println("addRenderingHinds(Map):\n  hints = " + hints);
        this.g2D.addRenderingHints(hints);
    }

    public void clip(Shape s) {
        this.log.println("clip(Shape):\n  s = " + s);
        this.g2D.clip(s);
    }

    public void draw(Shape s) {
        this.log.println("draw(Shape):\n  s = " + s);
        this.g2D.draw(s);
    }

    public void drawGlyphVector(GlyphVector g, float x, float y) {
        this.log.println("drawGlyphVector(GlyphVector, float, float):\n  g = " + g + "\n  x = " + x + "\n  y = " + y);
        this.g2D.drawGlyphVector(g, x, y);
    }

    public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {
        this.log.println("drawImage(BufferedImage, BufferedImageOp, x, y):\n  img = " + img + "\n  op = " + op + "\n  x = " + x + "\n  y = " + y);
        this.g2D.drawImage(img, op, x, y);
    }

    public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs) {
        this.log.println("drawImage(Image,AfflineTransform,ImageObserver):\n  img = " + img + "\n  xform = " + xform + "\n  obs = " + obs);
        return this.g2D.drawImage(img, xform, obs);
    }

    public void drawRenderableImage(RenderableImage img, AffineTransform xform) {
        this.log.println("drawRenderableImage(RenderableImage, AfflineTransform):\n  img = " + img + "\n  xform = " + xform);
        this.g2D.drawRenderableImage(img, xform);
    }

    public void drawRenderedImage(RenderedImage img, AffineTransform xform) {
        this.log.println("drawRenderedImage(RenderedImage, AffineTransform):\n  img = " + img + "\n  xform = " + xform);
        this.g2D.drawRenderedImage(img, xform);
    }

    public void drawString(AttributedCharacterIterator iterator, float x, float y) {
        this.log.println("drawString(AttributedCharacterIterator):\n  iterator = " + iterator + "\n  x = " + x + "\n  y = " + y);
        this.g2D.drawString(iterator, x, y);
    }

    public void drawString(String s, float x, float y) {
        this.log.println("drawString(s,x,y):\n  s = " + s + "\n  x = " + x + "\n  y = " + y);
        this.g2D.drawString(s, x, y);
    }

    public void fill(Shape s) {
        this.log.println("fill(Shape):\n  s = " + s);
        this.g2D.fill(s);
    }

    public Color getBackground() {
        this.log.println("getBackground():");
        return this.g2D.getBackground();
    }

    public Composite getComposite() {
        this.log.println("getComposite():");
        return this.g2D.getComposite();
    }

    public GraphicsConfiguration getDeviceConfiguration() {
        this.log.println("getDeviceConfiguration():");
        return this.g2D.getDeviceConfiguration();
    }

    public FontRenderContext getFontRenderContext() {
        this.log.println("getFontRenderContext():");
        return this.g2D.getFontRenderContext();
    }

    public Paint getPaint() {
        this.log.println("getPaint():");
        return this.g2D.getPaint();
    }

    public Object getRenderingHint(Key hintKey) {
        this.log.println("getRenderingHint(RenderingHints.Key):\n  hintKey = " + hintKey);
        return this.g2D.getRenderingHint(hintKey);
    }

    public RenderingHints getRenderingHints() {
        this.log.println("getRenderingHints():");
        return this.g2D.getRenderingHints();
    }

    public Stroke getStroke() {
        this.log.println("getStroke():");
        return this.g2D.getStroke();
    }

    public AffineTransform getTransform() {
        this.log.println("getTransform():");
        return this.g2D.getTransform();
    }

    public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
        this.log.println("hit(Rectangle, Shape, onStroke):\n  rect = " + rect + "\n  s = " + s + "\n  onStroke = " + onStroke);
        return this.g2D.hit(rect, s, onStroke);
    }

    public void rotate(double theta) {
        this.log.println("rotate(theta):\n  theta = " + theta);
        this.g2D.rotate(theta);
    }

    public void rotate(double theta, double x, double y) {
        this.log.println("rotate(double,double,double):\n  theta = " + theta + "\n  x = " + x + "\n  y = " + y);
        this.g2D.rotate(theta, x, y);
    }

    public void scale(double sx, double sy) {
        this.log.println("scale(double,double):\n  sx = " + sx + "\n  sy");
        this.g2D.scale(sx, sy);
    }

    public void setBackground(Color color) {
        this.log.println("setBackground(Color):\n  color = " + color);
        this.g2D.setBackground(color);
    }

    public void setComposite(Composite comp) {
        this.log.println("setComposite(Composite):\n  comp = " + comp);
        this.g2D.setComposite(comp);
    }

    public void setPaint(Paint paint) {
        this.log.println("setPaint(Paint):\n  paint = " + paint);
        this.g2D.setPaint(paint);
    }

    public void setRenderingHint(Key hintKey, Object hintValue) {
        this.log.println("setRenderingHint(RenderingHints.Key, Object):\n  hintKey = " + hintKey + "\n  hintValue = " + hintValue);
        this.g2D.setRenderingHint(hintKey, hintValue);
    }

    public void setRenderingHints(Map<?, ?> hints) {
        this.log.println("setRenderingHints(Map):\n  hints = " + hints);
        this.g2D.setRenderingHints(hints);
    }

    public void setStroke(Stroke s) {
        String l;
        if (s instanceof BasicStroke) {
            BasicStroke bs = (BasicStroke) s;
            l = "setStroke(Stoke):\n  s = BasicStroke(\n    dash[]: " + Arrays.toString(bs.getDashArray()) + "\n    dashPhase: " + bs.getDashPhase() + "\n    endCap: " + bs.getEndCap() + "\n    lineJoin: " + bs.getLineJoin() + "\n    width: " + bs.getLineWidth() + "\n    miterLimit: " + bs.getMiterLimit() + "\n  )";
        } else {
            l = "setStroke(Stoke):\n  s = " + s;
        }
        this.log.println(l);
        this.g2D.setStroke(s);
    }

    public void setTransform(AffineTransform Tx) {
        this.log.println("setTransform():\n  Tx = " + Tx);
        this.g2D.setTransform(Tx);
    }

    public void shear(double shx, double shy) {
        this.log.println("shear(shx, dhy):\n  shx = " + shx + "\n  shy = " + shy);
        this.g2D.shear(shx, shy);
    }

    public void transform(AffineTransform Tx) {
        this.log.println("transform(AffineTransform):\n  Tx = " + Tx);
        this.g2D.transform(Tx);
    }

    public void translate(double tx, double ty) {
        this.log.println("translate(double, double):\n  tx = " + tx + "\n  ty = " + ty);
        this.g2D.translate(tx, ty);
    }

    public void clearRect(int x, int y, int width, int height) {
        this.log.println("clearRect(int,int,int,int):\n  x = " + x + "\n  y = " + y + "\n  width = " + width + "\n  height = " + height);
        this.g2D.clearRect(x, y, width, height);
    }

    public void clipRect(int x, int y, int width, int height) {
        this.log.println("clipRect(int, int, int, int):\n  x = " + x + "\n  y = " + y + "\n  width = " + width + "height = " + height);
        this.g2D.clipRect(x, y, width, height);
    }

    public void copyArea(int x, int y, int width, int height, int dx, int dy) {
        this.log.println("copyArea(int,int,int,int):\n  x = " + x + "\n  y = " + y + "\n  width = " + width + "\n  height = " + height);
        this.g2D.copyArea(x, y, width, height, dx, dy);
    }

    public Graphics create() {
        this.log.println("create():");
        return this.g2D.create();
    }

    public Graphics create(int x, int y, int width, int height) {
        this.log.println("create(int,int,int,int):\n  x = " + x + "\n  y = " + y + "\n  width = " + width + "\n  height = " + height);
        return this.g2D.create(x, y, width, height);
    }

    public void dispose() {
        this.log.println("dispose():");
        this.g2D.dispose();
    }

    public void draw3DRect(int x, int y, int width, int height, boolean raised) {
        this.log.println("draw3DRect(int,int,int,int,boolean):\n  x = " + x + "\n  y = " + y + "\n  width = " + width + "\n  height = " + height + "\n  raised = " + raised);
        this.g2D.draw3DRect(x, y, width, height, raised);
    }

    public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        this.log.println("drawArc(int,int,int,int,int,int):\n  x = " + x + "\n  y = " + y + "\n  width = " + width + "\n  height = " + height + "\n  startAngle = " + startAngle + "\n  arcAngle = " + arcAngle);
        this.g2D.drawArc(x, y, width, height, startAngle, arcAngle);
    }

    public void drawBytes(byte[] data, int offset, int length, int x, int y) {
        this.log.println("drawBytes(byte[],int,int,int,int):\n  data = " + Arrays.toString(data) + "\n  offset = " + offset + "\n  length = " + length + "\n  x = " + x + "\n  y = " + y);
        this.g2D.drawBytes(data, offset, length, x, y);
    }

    public void drawChars(char[] data, int offset, int length, int x, int y) {
        this.log.println("drawChars(data,int,int,int,int):\n  data = " + Arrays.toString(data) + "\n  offset = " + offset + "\n  length = " + length + "\n  x = " + x + "\n  y = " + y);
        this.g2D.drawChars(data, offset, length, x, y);
    }

    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
        this.log.println("drawImage(Image,int,int,int,int,int,int,int,int,ImageObserver):\n  img = " + img + "\n  dx1 = " + dx1 + "\n  dy1 = " + dy1 + "\n  dx2 = " + dx2 + "\n  dy2 = " + dy2 + "\n  sx1 = " + sx1 + "\n  sy1 = " + sy1 + "\n  sx2 = " + sx2 + "\n  sy2 = " + sy2 + "\n  observer = " + observer);
        return this.g2D.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer);
    }

    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color bgcolor, ImageObserver observer) {
        this.log.println("drawImage(Image,int,int,int,int,int,int,int,int,Color,ImageObserver):\n  img = " + img + "\n  dx1 = " + dx1 + "\n  dy1 = " + dy1 + "\n  dx2 = " + dx2 + "\n  dy2 = " + dy2 + "\n  sx1 = " + sx1 + "\n  sy1 = " + sy1 + "\n  sx2 = " + sx2 + "\n  sy2 = " + sy2 + "\n  bgcolor = " + bgcolor + "\n  observer = " + observer);
        return this.g2D.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, bgcolor, observer);
    }

    public boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer) {
        this.log.println("drawImage(Image,int,int,Color,ImageObserver):\n  img = " + img + "\n  x = " + x + "\n  y = " + y + "\n  bgcolor = " + bgcolor + "\n  observer = " + observer);
        return this.g2D.drawImage(img, x, y, bgcolor, observer);
    }

    public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
        this.log.println("drawImage(Image,int,int,observer):\n  img = " + img + "\n  x = " + x + "\n  y = " + y + "\n  observer = " + observer);
        return this.g2D.drawImage(img, x, y, observer);
    }

    public boolean drawImage(Image img, int x, int y, int width, int height, Color bgcolor, ImageObserver observer) {
        this.log.println("drawImage(Image,int,int,int,int,Color,ImageObserver):\n  img = " + img + "\n  x = " + x + "\n  y = " + y + "\n  width = " + width + "\n  height = " + height + "\n  bgcolor = " + bgcolor + "\n  observer = " + observer);
        return this.g2D.drawImage(img, x, y, width, height, bgcolor, observer);
    }

    public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {
        this.log.println("drawImage(Image,int,int,width,height,observer):\n  img = " + img + "\n  x = " + x + "\n  y = " + y + "\n  width = " + width + "\n  height = " + height + "\n  observer = " + observer);
        return this.g2D.drawImage(img, x, y, width, height, observer);
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        this.log.println("drawLine(int,int,int,int):\n  x1 = " + x1 + "\n  y1 = " + y1 + "\n  x2 = " + x2 + "\n  y2 = " + y2);
        this.g2D.drawLine(x1, y1, x2, y2);
    }

    public void drawOval(int x, int y, int width, int height) {
        this.log.println("drawOval(int,int,int,int):\n  x = " + x + "\n  y = " + y + "\n  width = " + width + "\n  height = " + height);
        this.g2D.drawOval(x, y, width, height);
    }

    public void drawPolygon(Polygon p) {
        this.log.println("drawPolygon(Polygon):\n  p = " + p);
        this.g2D.drawPolygon(p);
    }

    public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        this.log.println("drawPolygon(int[],int[],int):\n  xPoints = " + Arrays.toString(xPoints) + "\n  yPoints = " + Arrays.toString(yPoints) + "\n  nPoints = " + nPoints);
        this.g2D.drawPolygon(xPoints, yPoints, nPoints);
    }

    public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
        this.log.println("drawPolyline(int[],int[],int):\n  xPoints = " + Arrays.toString(xPoints) + "\n  yPoints = " + Arrays.toString(yPoints) + "\n  nPoints = " + nPoints);
        this.g2D.drawPolyline(xPoints, yPoints, nPoints);
    }

    public void drawRect(int x, int y, int width, int height) {
        this.log.println("drawRect(int,int,int,int):\n  x = " + x + "\n  y = " + y + "\n  width = " + width + "\n  height = " + height);
        this.g2D.drawRect(x, y, width, height);
    }

    public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        this.log.println("drawRoundRect(int,int,int,int,int,int):\n  x = " + x + "\n  y = " + y + "\n  width = " + width + "\n  height = " + height + "\n  arcWidth = " + arcWidth + "\n  arcHeight = " + arcHeight);
        this.g2D.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    public void drawString(AttributedCharacterIterator iterator, int x, int y) {
        this.log.println("drawString(AttributedCharacterIterator,int,int):\n  iterator = " + iterator + "\n  x = " + x + "\n  y = " + y);
        this.g2D.drawString(iterator, x, y);
    }

    public void drawString(String str, int x, int y) {
        this.log.println("drawString(str,int,int):\n  str = " + str + "\n  x = " + x + "\n  y = " + y);
        this.g2D.drawString(str, x, y);
    }

    public void fill3DRect(int x, int y, int width, int height, boolean raised) {
        this.log.println("fill3DRect(int,int,int,int,boolean):\n  x = " + x + "\n  y = " + y + "\n  width = " + width + "\n  height = " + height + "\n  raised = " + raised);
        this.g2D.fill3DRect(x, y, width, height, raised);
    }

    public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        this.log.println("fillArc(int,int,int,int,int,int):\n  x = " + x + "\n  y = " + y + "\n  width = " + width + "\n  height = " + height + "\n  startAngle = " + startAngle + "\n  arcAngle = " + arcAngle);
        this.g2D.fillArc(x, y, width, height, startAngle, arcAngle);
    }

    public void fillOval(int x, int y, int width, int height) {
        this.log.println("fillOval(int,int,int,int):\n  x = " + x + "\n  y = " + y + "\n  width = " + width + "\n  height = " + height);
        this.g2D.fillOval(x, y, width, height);
    }

    public void fillPolygon(Polygon p) {
        this.log.println("fillPolygon(Polygon):\n  p = " + p);
        this.g2D.fillPolygon(p);
    }

    public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        this.log.println("fillPolygon(int[],int[],int):\n  xPoints = " + Arrays.toString(xPoints) + "\n  yPoints = " + Arrays.toString(yPoints) + "\n  nPoints = " + nPoints);
        this.g2D.fillPolygon(xPoints, yPoints, nPoints);
    }

    public void fillRect(int x, int y, int width, int height) {
        this.log.println("fillRect(int,int,int,int):\n  x = " + x + "\n  y = " + y + "\n  width = " + width + "\n  height = " + height);
        this.g2D.fillRect(x, y, width, height);
    }

    public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        this.log.println("fillRoundRect(int,int,int,int,int,int):\n  x = " + x + "\n  y = " + y + "\n  width = " + width + "\n  height = " + height);
        this.g2D.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    @Internal
    public final void finalize() {
        this.log.println("finalize():");
        this.g2D.finalize();
        super.finalize();
    }

    public Shape getClip() {
        this.log.println("getClip():");
        return this.g2D.getClip();
    }

    public Rectangle getClipBounds() {
        this.log.println("getClipBounds():");
        return this.g2D.getClipBounds();
    }

    public Rectangle getClipBounds(Rectangle r) {
        this.log.println("getClipBounds(Rectangle):\n  r = " + r);
        return this.g2D.getClipBounds(r);
    }

    public Color getColor() {
        this.log.println("getColor():");
        return this.g2D.getColor();
    }

    public Font getFont() {
        this.log.println("getFont():");
        return this.g2D.getFont();
    }

    public FontMetrics getFontMetrics() {
        this.log.println("getFontMetrics():");
        return this.g2D.getFontMetrics();
    }

    public FontMetrics getFontMetrics(Font f) {
        this.log.println("getFontMetrics():");
        return this.g2D.getFontMetrics(f);
    }

    public boolean hitClip(int x, int y, int width, int height) {
        this.log.println("hitClip(int,int,int,int):\n  x = " + x + "\n  y = " + y + "\n  width = " + width + "\n  height = " + height);
        return this.g2D.hitClip(x, y, width, height);
    }

    public void setClip(Shape clip) {
        this.log.println("setClip(Shape):\n  clip = " + clip);
        this.g2D.setClip(clip);
    }

    public void setClip(int x, int y, int width, int height) {
        this.log.println("setClip(int,int,int,int):\n  x = " + x + "\n  y = " + y + "\n  width = " + width + "\n  height = " + height);
        this.g2D.setClip(x, y, width, height);
    }

    public void setColor(Color c) {
        this.log.println("setColor():\n  c = " + c);
        this.g2D.setColor(c);
    }

    public void setFont(Font font) {
        this.log.println("setFont(Font):\n  font = " + font);
        this.g2D.setFont(font);
    }

    public void setPaintMode() {
        this.log.println("setPaintMode():");
        this.g2D.setPaintMode();
    }

    public void setXORMode(Color c1) {
        this.log.println("setXORMode(Color):\n  c1 = " + c1);
        this.g2D.setXORMode(c1);
    }

    public String toString() {
        this.log.println("toString():");
        return this.g2D.toString();
    }

    public void translate(int x, int y) {
        this.log.println("translate(int,int):\n  x = " + x + "\n  y = " + y);
        this.g2D.translate(x, y);
    }
}

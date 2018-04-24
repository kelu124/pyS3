package org.apache.poi.sl.draw;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D.Double;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.EventFilter;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import org.apache.poi.sl.draw.binding.CTCustomGeometry2D;
import org.apache.poi.sl.draw.geom.Context;
import org.apache.poi.sl.draw.geom.CustomGeometry;
import org.apache.poi.sl.draw.geom.Outline;
import org.apache.poi.sl.draw.geom.Path;
import org.apache.poi.sl.usermodel.LineDecoration;
import org.apache.poi.sl.usermodel.LineDecoration.DecorationShape;
import org.apache.poi.sl.usermodel.LineDecoration.DecorationSize;
import org.apache.poi.sl.usermodel.Shadow;
import org.apache.poi.sl.usermodel.SimpleShape;

public class DrawSimpleShape extends DrawShape {
    private static final double DECO_SIZE_POW = 1.5d;

    static class C10811 implements EventFilter {
        C10811() {
        }

        public boolean accept(XMLEvent event) {
            return event.isStartElement();
        }
    }

    public DrawSimpleShape(SimpleShape<?, ?> shape) {
        super(shape);
    }

    public void draw(Graphics2D graphics) {
        DrawPaint drawPaint = DrawFactory.getInstance(graphics).getPaint(getShape());
        Paint fill = drawPaint.getPaint(graphics, getShape().getFillStyle().getPaint());
        Paint line = drawPaint.getPaint(graphics, getShape().getStrokeStyle().getPaint());
        BasicStroke stroke = getStroke();
        graphics.setStroke(stroke);
        Collection<Outline> elems = computeOutlines(graphics);
        drawShadow(graphics, elems, fill, line);
        if (fill != null) {
            graphics.setPaint(fill);
            for (Outline o : elems) {
                if (o.getPath().isFilled()) {
                    Shape s = o.getOutline();
                    graphics.setRenderingHint(Drawable.GRADIENT_SHAPE, s);
                    graphics.fill(s);
                }
            }
        }
        drawContent(graphics);
        if (line != null) {
            graphics.setPaint(line);
            graphics.setStroke(stroke);
            for (Outline o2 : elems) {
                if (o2.getPath().isStroked()) {
                    s = o2.getOutline();
                    graphics.setRenderingHint(Drawable.GRADIENT_SHAPE, s);
                    graphics.draw(s);
                }
            }
        }
        drawDecoration(graphics, line, stroke);
    }

    protected void drawDecoration(Graphics2D graphics, Paint line, BasicStroke stroke) {
        if (line != null) {
            graphics.setPaint(line);
            List<Outline> lst = new ArrayList();
            LineDecoration deco = getShape().getLineDecoration();
            Outline head = getHeadDecoration(graphics, deco, stroke);
            if (head != null) {
                lst.add(head);
            }
            Outline tail = getTailDecoration(graphics, deco, stroke);
            if (tail != null) {
                lst.add(tail);
            }
            for (Outline o : lst) {
                Shape s = o.getOutline();
                Path p = o.getPath();
                graphics.setRenderingHint(Drawable.GRADIENT_SHAPE, s);
                if (p.isFilled()) {
                    graphics.fill(s);
                }
                if (p.isStroked()) {
                    graphics.draw(s);
                }
            }
        }
    }

    protected Outline getTailDecoration(Graphics2D graphics, LineDecoration deco, BasicStroke stroke) {
        if (deco == null || stroke == null) {
            return null;
        }
        DecorationSize tailLength = deco.getTailLength();
        if (tailLength == null) {
            tailLength = DecorationSize.MEDIUM;
        }
        DecorationSize tailWidth = deco.getTailWidth();
        if (tailWidth == null) {
            tailWidth = DecorationSize.MEDIUM;
        }
        double lineWidth = Math.max(2.5d, (double) stroke.getLineWidth());
        Rectangle2D anchor = DrawShape.getAnchor(graphics, getShape());
        double x2 = anchor.getX() + anchor.getWidth();
        double y2 = anchor.getY() + anchor.getHeight();
        double alpha = Math.atan(anchor.getHeight() / anchor.getWidth());
        AffineTransform at = new AffineTransform();
        Shape tailShape = null;
        Path p = null;
        double scaleY = Math.pow(DECO_SIZE_POW, (double) (tailWidth.ordinal() + 1));
        double scaleX = Math.pow(DECO_SIZE_POW, (double) (tailLength.ordinal() + 1));
        DecorationShape tailShapeEnum = deco.getTailShape();
        if (tailShapeEnum == null) {
            return null;
        }
        switch (tailShapeEnum) {
            case OVAL:
                p = new Path();
                tailShape = new Double(0.0d, 0.0d, lineWidth * scaleX, lineWidth * scaleY);
                Rectangle2D bounds = tailShape.getBounds2D();
                at.translate(x2 - (bounds.getWidth() / 2.0d), y2 - (bounds.getHeight() / 2.0d));
                at.rotate(alpha, bounds.getX() + (bounds.getWidth() / 2.0d), bounds.getY() + (bounds.getHeight() / 2.0d));
                break;
            case STEALTH:
            case ARROW:
                Path path = new Path(false, true);
                Path2D.Double arrow = new Path2D.Double();
                arrow.moveTo((-lineWidth) * scaleX, ((-lineWidth) * scaleY) / 2.0d);
                arrow.lineTo(0.0d, 0.0d);
                arrow.lineTo((-lineWidth) * scaleX, (lineWidth * scaleY) / 2.0d);
                tailShape = arrow;
                at.translate(x2, y2);
                at.rotate(alpha);
                break;
            case TRIANGLE:
                p = new Path();
                Path2D.Double triangle = new Path2D.Double();
                triangle.moveTo((-lineWidth) * scaleX, ((-lineWidth) * scaleY) / 2.0d);
                triangle.lineTo(0.0d, 0.0d);
                triangle.lineTo((-lineWidth) * scaleX, (lineWidth * scaleY) / 2.0d);
                triangle.closePath();
                tailShape = triangle;
                at.translate(x2, y2);
                at.rotate(alpha);
                break;
        }
        if (tailShape != null) {
            tailShape = at.createTransformedShape(tailShape);
        }
        if (tailShape == null) {
            return null;
        }
        return new Outline(tailShape, p);
    }

    protected Outline getHeadDecoration(Graphics2D graphics, LineDecoration deco, BasicStroke stroke) {
        if (deco == null || stroke == null) {
            return null;
        }
        DecorationSize headLength = deco.getHeadLength();
        if (headLength == null) {
            headLength = DecorationSize.MEDIUM;
        }
        DecorationSize headWidth = deco.getHeadWidth();
        if (headWidth == null) {
            headWidth = DecorationSize.MEDIUM;
        }
        double lineWidth = Math.max(2.5d, (double) stroke.getLineWidth());
        Rectangle2D anchor = DrawShape.getAnchor(graphics, getShape());
        double x1 = anchor.getX();
        double y1 = anchor.getY();
        double alpha = Math.atan(anchor.getHeight() / anchor.getWidth());
        AffineTransform at = new AffineTransform();
        Shape headShape = null;
        Path p = null;
        double scaleY = Math.pow(DECO_SIZE_POW, (double) (headWidth.ordinal() + 1));
        double scaleX = Math.pow(DECO_SIZE_POW, (double) (headLength.ordinal() + 1));
        DecorationShape headShapeEnum = deco.getHeadShape();
        if (headShapeEnum == null) {
            return null;
        }
        switch (headShapeEnum) {
            case OVAL:
                p = new Path();
                headShape = new Double(0.0d, 0.0d, lineWidth * scaleX, lineWidth * scaleY);
                Rectangle2D bounds = headShape.getBounds2D();
                at.translate(x1 - (bounds.getWidth() / 2.0d), y1 - (bounds.getHeight() / 2.0d));
                at.rotate(alpha, bounds.getX() + (bounds.getWidth() / 2.0d), bounds.getY() + (bounds.getHeight() / 2.0d));
                break;
            case STEALTH:
            case ARROW:
                Path path = new Path(false, true);
                Path2D.Double arrow = new Path2D.Double();
                arrow.moveTo(lineWidth * scaleX, ((-lineWidth) * scaleY) / 2.0d);
                arrow.lineTo(0.0d, 0.0d);
                arrow.lineTo(lineWidth * scaleX, (lineWidth * scaleY) / 2.0d);
                headShape = arrow;
                at.translate(x1, y1);
                at.rotate(alpha);
                break;
            case TRIANGLE:
                p = new Path();
                Path2D.Double triangle = new Path2D.Double();
                triangle.moveTo(lineWidth * scaleX, ((-lineWidth) * scaleY) / 2.0d);
                triangle.lineTo(0.0d, 0.0d);
                triangle.lineTo(lineWidth * scaleX, (lineWidth * scaleY) / 2.0d);
                triangle.closePath();
                headShape = triangle;
                at.translate(x1, y1);
                at.rotate(alpha);
                break;
        }
        if (headShape != null) {
            headShape = at.createTransformedShape(headShape);
        }
        if (headShape == null) {
            return null;
        }
        return new Outline(headShape, p);
    }

    public BasicStroke getStroke() {
        return DrawShape.getStroke(getShape().getStrokeStyle());
    }

    protected void drawShadow(Graphics2D graphics, Collection<Outline> outlines, Paint fill, Paint line) {
        Shadow<?, ?> shadow = getShape().getShadow();
        if (shadow == null) {
            return;
        }
        if (fill != null || line != null) {
            Color shadowColor = DrawPaint.applyColorTransform(shadow.getFillStyle().getSolidColor());
            double shapeRotation = getShape().getRotation();
            if (getShape().getFlipVertical()) {
                shapeRotation += 180.0d;
            }
            double angle = shadow.getAngle() - shapeRotation;
            double dist = shadow.getDistance();
            double dx = dist * Math.cos(Math.toRadians(angle));
            double dy = dist * Math.sin(Math.toRadians(angle));
            graphics.translate(dx, dy);
            for (Outline o : outlines) {
                Shape s = o.getOutline();
                Path p = o.getPath();
                graphics.setRenderingHint(Drawable.GRADIENT_SHAPE, s);
                graphics.setPaint(shadowColor);
                if (fill != null && p.isFilled()) {
                    graphics.fill(s);
                } else if (line != null && p.isStroked()) {
                    graphics.draw(s);
                }
            }
            graphics.translate(-dx, -dy);
        }
    }

    protected static CustomGeometry getCustomGeometry(String name) {
        return getCustomGeometry(name, null);
    }

    protected static CustomGeometry getCustomGeometry(String name, Graphics2D graphics) {
        Map<String, CustomGeometry> presets;
        if (graphics == null) {
            presets = null;
        } else {
            presets = (Map) graphics.getRenderingHint(Drawable.PRESET_GEOMETRY_CACHE);
        }
        if (presets == null) {
            presets = new HashMap();
            if (graphics != null) {
                graphics.setRenderingHint(Drawable.PRESET_GEOMETRY_CACHE, presets);
            }
            String packageName = "org.apache.poi.sl.draw.binding";
            InputStream presetIS = Drawable.class.getResourceAsStream("presetShapeDefinitions.xml");
            EventFilter startElementFilter = new C10811();
            try {
                XMLInputFactory staxFactory = XMLInputFactory.newInstance();
                XMLEventReader staxReader = staxFactory.createXMLEventReader(presetIS);
                XMLEventReader staxFiltRd = staxFactory.createFilteredReader(staxReader, startElementFilter);
                staxFiltRd.nextEvent();
                Unmarshaller unmarshaller = JAXBContext.newInstance(packageName).createUnmarshaller();
                while (staxFiltRd.peek() != null) {
                    presets.put(((StartElement) staxFiltRd.peek()).getName().getLocalPart(), new CustomGeometry((CTCustomGeometry2D) unmarshaller.unmarshal(staxReader, CTCustomGeometry2D.class).getValue()));
                }
                staxFiltRd.close();
                staxReader.close();
                try {
                    presetIS.close();
                } catch (IOException e) {
                    throw new RuntimeException("Unable to load preset geometries.", e);
                }
            } catch (Exception e2) {
                throw new RuntimeException("Unable to load preset geometries.", e2);
            } catch (Throwable th) {
                try {
                    presetIS.close();
                } catch (IOException e3) {
                    throw new RuntimeException("Unable to load preset geometries.", e3);
                }
            }
        }
        return (CustomGeometry) presets.get(name);
    }

    protected Collection<Outline> computeOutlines(Graphics2D graphics) {
        List<Outline> lst = new ArrayList();
        CustomGeometry geom = getShape().getGeometry();
        if (geom != null) {
            Rectangle2D anchor = DrawShape.getAnchor(graphics, getShape());
            Iterator i$ = geom.iterator();
            while (i$.hasNext()) {
                double h;
                double scaleX;
                double scaleY;
                Path p = (Path) i$.next();
                double w = p.getW() == -1 ? anchor.getWidth() * 12700.0d : (double) p.getW();
                if (p.getH() == -1) {
                    h = anchor.getHeight() * 12700.0d;
                } else {
                    h = (double) p.getH();
                }
                Shape gp = p.getPath(new Context(geom, new Rectangle2D.Double(0.0d, 0.0d, w, h), getShape()));
                AffineTransform at = new AffineTransform();
                at.translate(anchor.getX(), anchor.getY());
                if (p.getW() != -1) {
                    scaleX = anchor.getWidth() / ((double) p.getW());
                } else {
                    scaleX = 7.874015748031496E-5d;
                }
                if (p.getH() != -1) {
                    scaleY = anchor.getHeight() / ((double) p.getH());
                } else {
                    scaleY = 7.874015748031496E-5d;
                }
                at.scale(scaleX, scaleY);
                lst.add(new Outline(at.createTransformedShape(gp), p));
            }
        }
        return lst;
    }

    protected SimpleShape<?, ?> getShape() {
        return (SimpleShape) this.shape;
    }
}

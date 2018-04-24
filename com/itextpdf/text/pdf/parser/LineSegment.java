package com.itextpdf.text.pdf.parser;

import com.itextpdf.awt.geom.Rectangle2D.Float;

public class LineSegment {
    private final Vector endPoint;
    private final Vector startPoint;

    public LineSegment(Vector startPoint, Vector endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public Vector getStartPoint() {
        return this.startPoint;
    }

    public Vector getEndPoint() {
        return this.endPoint;
    }

    public float getLength() {
        return this.endPoint.subtract(this.startPoint).length();
    }

    public Float getBoundingRectange() {
        float x1 = getStartPoint().get(0);
        float y1 = getStartPoint().get(1);
        float x2 = getEndPoint().get(0);
        float y2 = getEndPoint().get(1);
        return new Float(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1));
    }

    public LineSegment transformBy(Matrix m) {
        return new LineSegment(this.startPoint.cross(m), this.endPoint.cross(m));
    }
}

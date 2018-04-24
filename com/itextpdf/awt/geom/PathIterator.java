package com.itextpdf.awt.geom;

public interface PathIterator {
    public static final int SEG_CLOSE = 4;
    public static final int SEG_CUBICTO = 3;
    public static final int SEG_LINETO = 1;
    public static final int SEG_MOVETO = 0;
    public static final int SEG_QUADTO = 2;
    public static final int WIND_EVEN_ODD = 0;
    public static final int WIND_NON_ZERO = 1;

    int currentSegment(double[] dArr);

    int currentSegment(float[] fArr);

    int getWindingRule();

    boolean isDone();

    void next();
}

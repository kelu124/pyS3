package com.itextpdf.text.pdf.parser;

import com.itextpdf.awt.geom.Rectangle2D;
import com.itextpdf.text.Rectangle;

public class RegionTextRenderFilter extends RenderFilter {
    private final Rectangle2D filterRect;

    public RegionTextRenderFilter(Rectangle2D filterRect) {
        this.filterRect = filterRect;
    }

    public RegionTextRenderFilter(Rectangle filterRect) {
        this.filterRect = new com.itextpdf.awt.geom.Rectangle(filterRect);
    }

    public boolean allowText(TextRenderInfo renderInfo) {
        LineSegment segment = renderInfo.getBaseline();
        Vector startPoint = segment.getStartPoint();
        Vector endPoint = segment.getEndPoint();
        return this.filterRect.intersectsLine((double) startPoint.get(0), (double) startPoint.get(1), (double) endPoint.get(0), (double) endPoint.get(1));
    }
}

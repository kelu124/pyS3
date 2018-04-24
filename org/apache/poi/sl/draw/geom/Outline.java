package org.apache.poi.sl.draw.geom;

import java.awt.Shape;

public class Outline {
    private Path path;
    private Shape shape;

    public Outline(Shape shape, Path path) {
        this.shape = shape;
        this.path = path;
    }

    public Path getPath() {
        return this.path;
    }

    public Shape getOutline() {
        return this.shape;
    }
}

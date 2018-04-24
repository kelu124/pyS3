package org.apache.poi.sl.draw;

import java.awt.Graphics2D;
import org.apache.poi.util.Internal;

public interface Drawable {
    public static final DrawableHint DRAW_FACTORY = new DrawableHint(1);
    public static final DrawableHint FONT_FALLBACK = new DrawableHint(8);
    public static final DrawableHint FONT_HANDLER = new DrawableHint(7);
    public static final DrawableHint FONT_MAP = new DrawableHint(9);
    public static final DrawableHint GRADIENT_SHAPE = new DrawableHint(5);
    public static final DrawableHint GRESTORE = new DrawableHint(11);
    @Internal
    public static final DrawableHint GROUP_TRANSFORM = new DrawableHint(2);
    public static final DrawableHint GSAVE = new DrawableHint(10);
    public static final DrawableHint IMAGE_RENDERER = new DrawableHint(3);
    public static final DrawableHint PRESET_GEOMETRY_CACHE = new DrawableHint(6);
    public static final int TEXT_AS_CHARACTERS = 1;
    public static final int TEXT_AS_SHAPES = 2;
    public static final DrawableHint TEXT_RENDERING_MODE = new DrawableHint(4);

    void applyTransform(Graphics2D graphics2D);

    void draw(Graphics2D graphics2D);

    void drawContent(Graphics2D graphics2D);
}

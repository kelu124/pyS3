package org.apache.poi.sl.draw;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public interface ImageRenderer {
    boolean drawImage(Graphics2D graphics2D, Rectangle2D rectangle2D);

    boolean drawImage(Graphics2D graphics2D, Rectangle2D rectangle2D, Insets insets);

    Dimension getDimension();

    BufferedImage getImage();

    BufferedImage getImage(Dimension dimension);

    void loadImage(InputStream inputStream, String str) throws IOException;

    void loadImage(byte[] bArr, String str) throws IOException;

    void setAlpha(double d);
}

package org.apache.poi.sl.usermodel;

import java.io.InputStream;

public interface PaintStyle {

    public interface SolidPaint extends PaintStyle {
        ColorStyle getSolidColor();
    }

    public interface GradientPaint extends PaintStyle {

        public enum GradientType {
            linear,
            circular,
            shape
        }

        double getGradientAngle();

        ColorStyle[] getGradientColors();

        float[] getGradientFractions();

        GradientType getGradientType();

        boolean isRotatedWithShape();
    }

    public interface TexturePaint extends PaintStyle {
        int getAlpha();

        String getContentType();

        InputStream getImageData();
    }
}

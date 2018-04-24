package org.apache.poi.ss.usermodel;

import java.awt.Dimension;

public interface Picture {
    ClientAnchor getClientAnchor();

    Dimension getImageDimension();

    PictureData getPictureData();

    ClientAnchor getPreferredSize();

    ClientAnchor getPreferredSize(double d, double d2);

    Sheet getSheet();

    void resize();

    void resize(double d);

    void resize(double d, double d2);
}

package org.apache.poi.ss.usermodel.charts;

public interface ManualLayout {
    LayoutMode getHeightMode();

    double getHeightRatio();

    LayoutTarget getTarget();

    LayoutMode getWidthMode();

    double getWidthRatio();

    double getX();

    LayoutMode getXMode();

    double getY();

    LayoutMode getYMode();

    void setHeightMode(LayoutMode layoutMode);

    void setHeightRatio(double d);

    void setTarget(LayoutTarget layoutTarget);

    void setWidthMode(LayoutMode layoutMode);

    void setWidthRatio(double d);

    void setX(double d);

    void setXMode(LayoutMode layoutMode);

    void setY(double d);

    void setYMode(LayoutMode layoutMode);
}

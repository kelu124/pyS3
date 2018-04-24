package org.apache.poi.sl.usermodel;

import java.awt.Color;

public interface ColorStyle {
    int getAlpha();

    Color getColor();

    int getHueMod();

    int getHueOff();

    int getLumMod();

    int getLumOff();

    int getSatMod();

    int getSatOff();

    int getShade();

    int getTint();
}

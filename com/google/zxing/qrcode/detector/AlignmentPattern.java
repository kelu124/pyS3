package com.google.zxing.qrcode.detector;

import com.google.zxing.ResultPoint;
import com.itextpdf.text.pdf.BaseField;

public final class AlignmentPattern extends ResultPoint {
    private final float estimatedModuleSize;

    AlignmentPattern(float posX, float posY, float estimatedModuleSize) {
        super(posX, posY);
        this.estimatedModuleSize = estimatedModuleSize;
    }

    boolean aboutEquals(float moduleSize, float i, float j) {
        if (Math.abs(i - getY()) > moduleSize || Math.abs(j - getX()) > moduleSize) {
            return false;
        }
        float moduleSizeDiff = Math.abs(moduleSize - this.estimatedModuleSize);
        if (moduleSizeDiff <= BaseField.BORDER_WIDTH_THIN || moduleSizeDiff <= this.estimatedModuleSize) {
            return true;
        }
        return false;
    }

    AlignmentPattern combineEstimate(float i, float j, float newModuleSize) {
        return new AlignmentPattern((getX() + j) / BaseField.BORDER_WIDTH_MEDIUM, (getY() + i) / BaseField.BORDER_WIDTH_MEDIUM, (this.estimatedModuleSize + newModuleSize) / BaseField.BORDER_WIDTH_MEDIUM);
    }
}

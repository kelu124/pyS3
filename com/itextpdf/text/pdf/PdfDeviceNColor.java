package com.itextpdf.text.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.error_messages.MessageLocalization;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Locale;

public class PdfDeviceNColor implements ICachedColorSpace, IPdfSpecialColorSpace {
    ColorDetails[] colorantsDetails;
    PdfSpotColor[] spotColors;

    public PdfDeviceNColor(PdfSpotColor[] spotColors) {
        this.spotColors = spotColors;
    }

    public int getNumberOfColorants() {
        return this.spotColors.length;
    }

    public PdfSpotColor[] getSpotColors() {
        return this.spotColors;
    }

    public ColorDetails[] getColorantDetails(PdfWriter writer) {
        if (this.colorantsDetails == null) {
            this.colorantsDetails = new ColorDetails[this.spotColors.length];
            int i = 0;
            for (PdfSpotColor spotColorant : this.spotColors) {
                this.colorantsDetails[i] = writer.addSimple(spotColorant);
                i++;
            }
        }
        return this.colorantsDetails;
    }

    public PdfObject getPdfObject(PdfWriter writer) {
        int i;
        PdfArray array = new PdfArray(PdfName.DEVICEN);
        PdfArray colorants = new PdfArray();
        float[] colorantsRanges = new float[(this.spotColors.length * 2)];
        PdfDictionary colorantsDict = new PdfDictionary();
        String psFunFooter = "";
        int numberOfColorants = this.spotColors.length;
        float[][] CMYK = (float[][]) Array.newInstance(Float.TYPE, new int[]{4, numberOfColorants});
        for (i = 0; i < numberOfColorants; i++) {
            PdfSpotColor spotColorant = this.spotColors[i];
            colorantsRanges[i * 2] = 0.0f;
            colorantsRanges[(i * 2) + 1] = BaseField.BORDER_WIDTH_THIN;
            colorants.add(spotColorant.getName());
            if (colorantsDict.get(spotColorant.getName()) != null) {
                throw new RuntimeException(MessageLocalization.getComposedMessage("devicen.component.names.shall.be.different", new Object[0]));
            }
            if (this.colorantsDetails != null) {
                colorantsDict.put(spotColorant.getName(), this.colorantsDetails[i].getIndirectReference());
            } else {
                colorantsDict.put(spotColorant.getName(), spotColorant.getPdfObject(writer));
            }
            BaseColor color = spotColorant.getAlternativeCS();
            if (color instanceof ExtendedColor) {
                switch (((ExtendedColor) color).type) {
                    case 1:
                        CMYK[0][i] = 0.0f;
                        CMYK[1][i] = 0.0f;
                        CMYK[2][i] = 0.0f;
                        CMYK[3][i] = BaseField.BORDER_WIDTH_THIN - ((GrayColor) color).getGray();
                        break;
                    case 2:
                        CMYK[0][i] = ((CMYKColor) color).getCyan();
                        CMYK[1][i] = ((CMYKColor) color).getMagenta();
                        CMYK[2][i] = ((CMYKColor) color).getYellow();
                        CMYK[3][i] = ((CMYKColor) color).getBlack();
                        break;
                    case 7:
                        CMYKColor cmyk = ((LabColor) color).toCmyk();
                        CMYK[0][i] = cmyk.getCyan();
                        CMYK[1][i] = cmyk.getMagenta();
                        CMYK[2][i] = cmyk.getYellow();
                        CMYK[3][i] = cmyk.getBlack();
                        break;
                    default:
                        throw new RuntimeException(MessageLocalization.getComposedMessage("only.rgb.gray.and.cmyk.are.supported.as.alternative.color.spaces", new Object[0]));
                }
            }
            float computedK;
            float r = (float) color.getRed();
            float g = (float) color.getGreen();
            float b = (float) color.getBlue();
            float computedC = 0.0f;
            float computedM = 0.0f;
            float computedY = 0.0f;
            if (r == 0.0f && g == 0.0f && b == 0.0f) {
                computedK = BaseField.BORDER_WIDTH_THIN;
            } else {
                computedC = BaseField.BORDER_WIDTH_THIN - (r / 255.0f);
                computedM = BaseField.BORDER_WIDTH_THIN - (g / 255.0f);
                computedY = BaseField.BORDER_WIDTH_THIN - (b / 255.0f);
                float minCMY = Math.min(computedC, Math.min(computedM, computedY));
                computedC = (computedC - minCMY) / (BaseField.BORDER_WIDTH_THIN - minCMY);
                computedM = (computedM - minCMY) / (BaseField.BORDER_WIDTH_THIN - minCMY);
                computedY = (computedY - minCMY) / (BaseField.BORDER_WIDTH_THIN - minCMY);
                computedK = minCMY;
            }
            CMYK[0][i] = computedC;
            CMYK[1][i] = computedM;
            CMYK[2][i] = computedY;
            CMYK[3][i] = computedK;
            psFunFooter = psFunFooter + "pop ";
        }
        array.add(colorants);
        String psFunHeader = String.format(Locale.US, "1.000000 %d 1 roll ", new Object[]{Integer.valueOf(numberOfColorants + 1)});
        array.add(PdfName.DEVICECMYK);
        psFunHeader = psFunHeader + psFunHeader + psFunHeader + psFunHeader;
        String psFun = "";
        for (i = numberOfColorants + 4; i > numberOfColorants; i--) {
            psFun = psFun + String.format(Locale.US, "%d -1 roll ", new Object[]{Integer.valueOf(i)});
            for (int j = numberOfColorants; j > 0; j--) {
                psFun = psFun + String.format(Locale.US, "%d index %f mul 1.000000 cvr exch sub mul ", new Object[]{Integer.valueOf(j), Float.valueOf(CMYK[(numberOfColorants + 4) - i][numberOfColorants - j])});
            }
            psFun = psFun + String.format(Locale.US, "1.000000 cvr exch sub %d 1 roll ", new Object[]{Integer.valueOf(i)});
        }
        float[] fArr = new float[8];
        array.add(PdfFunction.type4(writer, colorantsRanges, new float[]{0.0f, BaseField.BORDER_WIDTH_THIN, 0.0f, BaseField.BORDER_WIDTH_THIN, 0.0f, BaseField.BORDER_WIDTH_THIN, 0.0f, BaseField.BORDER_WIDTH_THIN}, "{ " + psFunHeader + psFun + psFunFooter + "}").getReference());
        PdfDictionary attr = new PdfDictionary();
        attr.put(PdfName.SUBTYPE, PdfName.NCHANNEL);
        attr.put(PdfName.COLORANTS, colorantsDict);
        array.add(attr);
        return array;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PdfDeviceNColor)) {
            return false;
        }
        if (Arrays.equals(this.spotColors, ((PdfDeviceNColor) o).spotColors)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Arrays.hashCode(this.spotColors);
    }
}

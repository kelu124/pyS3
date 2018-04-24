package com.itextpdf.text.pdf;

public class SpotColor extends ExtendedColor {
    private static final long serialVersionUID = -6257004582113248079L;
    PdfSpotColor spot;
    float tint;

    public SpotColor(PdfSpotColor spot, float tint) {
        super(3, (((((float) spot.getAlternativeCS().getRed()) / 255.0f) - BaseField.BORDER_WIDTH_THIN) * tint) + BaseField.BORDER_WIDTH_THIN, (((((float) spot.getAlternativeCS().getGreen()) / 255.0f) - BaseField.BORDER_WIDTH_THIN) * tint) + BaseField.BORDER_WIDTH_THIN, (((((float) spot.getAlternativeCS().getBlue()) / 255.0f) - BaseField.BORDER_WIDTH_THIN) * tint) + BaseField.BORDER_WIDTH_THIN);
        this.spot = spot;
        this.tint = tint;
    }

    public PdfSpotColor getPdfSpotColor() {
        return this.spot;
    }

    public float getTint() {
        return this.tint;
    }

    public boolean equals(Object obj) {
        return (obj instanceof SpotColor) && ((SpotColor) obj).spot.equals(this.spot) && ((SpotColor) obj).tint == this.tint;
    }

    public int hashCode() {
        return this.spot.hashCode() ^ Float.floatToIntBits(this.tint);
    }
}

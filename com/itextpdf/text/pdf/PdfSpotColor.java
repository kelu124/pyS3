package com.itextpdf.text.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.error_messages.MessageLocalization;

public class PdfSpotColor implements ICachedColorSpace, IPdfSpecialColorSpace {
    public ColorDetails altColorDetails;
    public BaseColor altcs;
    public PdfName name;

    public PdfSpotColor(String name, BaseColor altcs) {
        this.name = new PdfName(name);
        this.altcs = altcs;
    }

    public ColorDetails[] getColorantDetails(PdfWriter writer) {
        if (this.altColorDetails == null && (this.altcs instanceof ExtendedColor) && ((ExtendedColor) this.altcs).getType() == 7) {
            this.altColorDetails = writer.addSimple(((LabColor) this.altcs).getLabColorSpace());
        }
        return new ColorDetails[]{this.altColorDetails};
    }

    public BaseColor getAlternativeCS() {
        return this.altcs;
    }

    public PdfName getName() {
        return this.name;
    }

    @Deprecated
    protected PdfObject getSpotObject(PdfWriter writer) {
        return getPdfObject(writer);
    }

    public PdfObject getPdfObject(PdfWriter writer) {
        PdfFunction func;
        PdfArray array = new PdfArray(PdfName.SEPARATION);
        array.add(this.name);
        if (this.altcs instanceof ExtendedColor) {
            switch (((ExtendedColor) this.altcs).type) {
                case 1:
                    array.add(PdfName.DEVICEGRAY);
                    func = PdfFunction.type2(writer, new float[]{0.0f, BaseField.BORDER_WIDTH_THIN}, null, new float[]{BaseField.BORDER_WIDTH_THIN}, new float[]{((GrayColor) this.altcs).getGray()}, BaseField.BORDER_WIDTH_THIN);
                    break;
                case 2:
                    array.add(PdfName.DEVICECMYK);
                    CMYKColor cmyk = this.altcs;
                    func = PdfFunction.type2(writer, new float[]{0.0f, BaseField.BORDER_WIDTH_THIN}, null, new float[]{0.0f, 0.0f, 0.0f, 0.0f}, new float[]{cmyk.getCyan(), cmyk.getMagenta(), cmyk.getYellow(), cmyk.getBlack()}, BaseField.BORDER_WIDTH_THIN);
                    break;
                case 7:
                    LabColor lab = this.altcs;
                    if (this.altColorDetails != null) {
                        array.add(this.altColorDetails.getIndirectReference());
                    } else {
                        array.add(lab.getLabColorSpace().getPdfObject(writer));
                    }
                    func = PdfFunction.type2(writer, new float[]{0.0f, BaseField.BORDER_WIDTH_THIN}, null, new float[]{100.0f, 0.0f, 0.0f}, new float[]{lab.getL(), lab.getA(), lab.getB()}, BaseField.BORDER_WIDTH_THIN);
                    break;
                default:
                    throw new RuntimeException(MessageLocalization.getComposedMessage("only.rgb.gray.and.cmyk.are.supported.as.alternative.color.spaces", new Object[0]));
            }
        }
        array.add(PdfName.DEVICERGB);
        func = PdfFunction.type2(writer, new float[]{0.0f, BaseField.BORDER_WIDTH_THIN}, null, new float[]{BaseField.BORDER_WIDTH_THIN, BaseField.BORDER_WIDTH_THIN, BaseField.BORDER_WIDTH_THIN}, new float[]{((float) this.altcs.getRed()) / 255.0f, ((float) this.altcs.getGreen()) / 255.0f, ((float) this.altcs.getBlue()) / 255.0f}, BaseField.BORDER_WIDTH_THIN);
        array.add(func.getReference());
        return array;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PdfSpotColor)) {
            return false;
        }
        PdfSpotColor spotColor = (PdfSpotColor) o;
        if (!this.altcs.equals(spotColor.altcs)) {
            return false;
        }
        if (this.name.equals(spotColor.name)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (this.name.hashCode() * 31) + this.altcs.hashCode();
    }
}

package com.itextpdf.text.pdf;

public interface IPdfSpecialColorSpace {
    ColorDetails[] getColorantDetails(PdfWriter pdfWriter);
}

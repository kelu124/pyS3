package com.itextpdf.text.pdf.fonts.cmaps;

import com.itextpdf.text.pdf.PRTokeniser;
import java.io.IOException;

public interface CidLocation {
    PRTokeniser getLocation(String str) throws IOException;
}

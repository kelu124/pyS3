package com.itextpdf.text.pdf.languages;

import com.itextpdf.text.pdf.Glyph;
import java.util.List;

public interface GlyphRepositioner {
    void repositionGlyphs(List<Glyph> list);
}

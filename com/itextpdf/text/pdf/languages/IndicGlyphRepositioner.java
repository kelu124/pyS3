package com.itextpdf.text.pdf.languages;

import com.itextpdf.text.pdf.Glyph;
import java.util.List;

abstract class IndicGlyphRepositioner implements GlyphRepositioner {
    abstract List<String> getCharactersToBeShiftedLeftByOnePosition();

    IndicGlyphRepositioner() {
    }

    public void repositionGlyphs(List<Glyph> glyphList) {
        int i = 0;
        while (i < glyphList.size()) {
            Glyph glyph = (Glyph) glyphList.get(i);
            Glyph nextGlyph = getNextGlyph(glyphList, i);
            if (nextGlyph != null && getCharactersToBeShiftedLeftByOnePosition().contains(nextGlyph.chars)) {
                glyphList.set(i, nextGlyph);
                glyphList.set(i + 1, glyph);
                i++;
            }
            i++;
        }
    }

    private Glyph getNextGlyph(List<Glyph> glyphs, int currentIndex) {
        if (currentIndex + 1 < glyphs.size()) {
            return (Glyph) glyphs.get(currentIndex + 1);
        }
        return null;
    }
}

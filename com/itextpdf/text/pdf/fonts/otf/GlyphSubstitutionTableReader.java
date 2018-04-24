package com.itextpdf.text.pdf.fonts.otf;

import com.itextpdf.text.pdf.Glyph;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GlyphSubstitutionTableReader extends OpenTypeFontTableReader {
    private final Map<Integer, Character> glyphToCharacterMap;
    private final int[] glyphWidthsByIndex;
    private Map<Integer, List<Integer>> rawLigatureSubstitutionMap;

    public GlyphSubstitutionTableReader(String fontFilePath, int gsubTableLocation, Map<Integer, Character> glyphToCharacterMap, int[] glyphWidthsByIndex) throws IOException {
        super(fontFilePath, gsubTableLocation);
        this.glyphWidthsByIndex = glyphWidthsByIndex;
        this.glyphToCharacterMap = glyphToCharacterMap;
    }

    public void read() throws FontReadingException {
        this.rawLigatureSubstitutionMap = new LinkedHashMap();
        startReadingTable();
    }

    public Map<String, Glyph> getGlyphSubstitutionMap() throws FontReadingException {
        Map<String, Glyph> glyphSubstitutionMap = new LinkedHashMap();
        for (Integer glyphIdToReplace : this.rawLigatureSubstitutionMap.keySet()) {
            List<Integer> constituentGlyphs = (List) this.rawLigatureSubstitutionMap.get(glyphIdToReplace);
            StringBuilder chars = new StringBuilder(constituentGlyphs.size());
            for (Integer constituentGlyphId : constituentGlyphs) {
                chars.append(getTextFromGlyph(constituentGlyphId.intValue(), this.glyphToCharacterMap));
            }
            Glyph glyph = new Glyph(glyphIdToReplace.intValue(), this.glyphWidthsByIndex[glyphIdToReplace.intValue()], chars.toString());
            glyphSubstitutionMap.put(glyph.chars, glyph);
        }
        return Collections.unmodifiableMap(glyphSubstitutionMap);
    }

    private String getTextFromGlyph(int glyphId, Map<Integer, Character> glyphToCharacterMap) throws FontReadingException {
        StringBuilder chars = new StringBuilder(1);
        Character c = (Character) glyphToCharacterMap.get(Integer.valueOf(glyphId));
        if (c == null) {
            List<Integer> constituentGlyphs = (List) this.rawLigatureSubstitutionMap.get(Integer.valueOf(glyphId));
            if (constituentGlyphs == null || constituentGlyphs.isEmpty()) {
                throw new FontReadingException("No corresponding character or simple glyphs found for GlyphID=" + glyphId);
            }
            for (Integer intValue : constituentGlyphs) {
                chars.append(getTextFromGlyph(intValue.intValue(), glyphToCharacterMap));
            }
        } else {
            chars.append(c.charValue());
        }
        return chars.toString();
    }

    protected void readSubTable(int lookupType, int subTableLocation) throws IOException {
        if (lookupType == 1) {
            readSingleSubstitutionSubtable(subTableLocation);
        } else if (lookupType == 4) {
            readLigatureSubstitutionSubtable(subTableLocation);
        } else {
            System.err.println("LookupType " + lookupType + " is not yet handled for " + GlyphSubstitutionTableReader.class.getSimpleName());
        }
    }

    private void readSingleSubstitutionSubtable(int subTableLocation) throws IOException {
        this.rf.seek((long) subTableLocation);
        int substFormat = this.rf.readShort();
        LOG.debug("substFormat=" + substFormat);
        if (substFormat == 1) {
            int coverage = this.rf.readShort();
            LOG.debug("coverage=" + coverage);
            int deltaGlyphID = this.rf.readShort();
            LOG.debug("deltaGlyphID=" + deltaGlyphID);
            for (Integer intValue : readCoverageFormat(subTableLocation + coverage)) {
                int substituteGlyphId = intValue.intValue() + deltaGlyphID;
                this.rawLigatureSubstitutionMap.put(Integer.valueOf(substituteGlyphId), Arrays.asList(new Integer[]{Integer.valueOf(coverageGlyphId)}));
            }
        } else if (substFormat == 2) {
            System.err.println("LookupType 1 :: substFormat 2 is not yet handled by " + GlyphSubstitutionTableReader.class.getSimpleName());
        } else {
            throw new IllegalArgumentException("Bad substFormat: " + substFormat);
        }
    }

    private void readLigatureSubstitutionSubtable(int ligatureSubstitutionSubtableLocation) throws IOException {
        this.rf.seek((long) ligatureSubstitutionSubtableLocation);
        int substFormat = this.rf.readShort();
        LOG.debug("substFormat=" + substFormat);
        if (substFormat != 1) {
            throw new IllegalArgumentException("The expected SubstFormat is 1");
        }
        int i;
        int coverage = this.rf.readShort();
        LOG.debug("coverage=" + coverage);
        int ligSetCount = this.rf.readShort();
        List<Integer> ligatureOffsets = new ArrayList(ligSetCount);
        for (i = 0; i < ligSetCount; i++) {
            ligatureOffsets.add(Integer.valueOf(this.rf.readShort()));
        }
        List<Integer> coverageGlyphIds = readCoverageFormat(ligatureSubstitutionSubtableLocation + coverage);
        if (ligSetCount != coverageGlyphIds.size()) {
            throw new IllegalArgumentException("According to the OpenTypeFont specifications, the coverage count should be equal to the no. of LigatureSetTables");
        }
        for (i = 0; i < ligSetCount; i++) {
            int coverageGlyphId = ((Integer) coverageGlyphIds.get(i)).intValue();
            int ligatureOffset = ((Integer) ligatureOffsets.get(i)).intValue();
            LOG.debug("ligatureOffset=" + ligatureOffset);
            readLigatureSetTable(ligatureSubstitutionSubtableLocation + ligatureOffset, coverageGlyphId);
        }
    }

    private void readLigatureSetTable(int ligatureSetTableLocation, int coverageGlyphId) throws IOException {
        this.rf.seek((long) ligatureSetTableLocation);
        int ligatureCount = this.rf.readShort();
        LOG.debug("ligatureCount=" + ligatureCount);
        List<Integer> ligatureOffsets = new ArrayList(ligatureCount);
        for (int i = 0; i < ligatureCount; i++) {
            ligatureOffsets.add(Integer.valueOf(this.rf.readShort()));
        }
        for (Integer intValue : ligatureOffsets) {
            readLigatureTable(ligatureSetTableLocation + intValue.intValue(), coverageGlyphId);
        }
    }

    private void readLigatureTable(int ligatureTableLocation, int coverageGlyphId) throws IOException {
        this.rf.seek((long) ligatureTableLocation);
        int ligGlyph = this.rf.readShort();
        LOG.debug("ligGlyph=" + ligGlyph);
        int compCount = this.rf.readShort();
        List<Integer> glyphIdList = new ArrayList();
        glyphIdList.add(Integer.valueOf(coverageGlyphId));
        for (int i = 0; i < compCount - 1; i++) {
            glyphIdList.add(Integer.valueOf(this.rf.readShort()));
        }
        LOG.debug("glyphIdList=" + glyphIdList);
        List<Integer> previousValue = (List) this.rawLigatureSubstitutionMap.put(Integer.valueOf(ligGlyph), glyphIdList);
        if (previousValue != null) {
            LOG.warn("!!!!!!!!!!glyphId=" + ligGlyph + ",\npreviousValue=" + previousValue + ",\ncurrentVal=" + glyphIdList);
        }
    }
}

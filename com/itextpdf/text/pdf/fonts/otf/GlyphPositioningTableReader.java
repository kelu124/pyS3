package com.itextpdf.text.pdf.fonts.otf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GlyphPositioningTableReader extends OpenTypeFontTableReader {

    static class MarkRecord {
        final int markAnchorOffset;
        final int markClass;

        public MarkRecord(int markClass, int markAnchorOffset) {
            this.markClass = markClass;
            this.markAnchorOffset = markAnchorOffset;
        }
    }

    static class PosLookupRecord {
        final int lookupListIndex;
        final int sequenceIndex;

        public PosLookupRecord(int sequenceIndex, int lookupListIndex) {
            this.sequenceIndex = sequenceIndex;
            this.lookupListIndex = lookupListIndex;
        }
    }

    public GlyphPositioningTableReader(String fontFilePath, int gposTableLocation) throws IOException {
        super(fontFilePath, gposTableLocation);
    }

    public void read() throws FontReadingException {
        startReadingTable();
    }

    protected void readSubTable(int lookupType, int subTableLocation) throws IOException {
        if (lookupType == 1) {
            readLookUpType_1(subTableLocation);
        } else if (lookupType == 4) {
            readLookUpType_4(subTableLocation);
        } else if (lookupType == 8) {
            readLookUpType_8(subTableLocation);
        } else {
            System.err.println("The lookupType " + lookupType + " is not yet supported by " + GlyphPositioningTableReader.class.getSimpleName());
        }
    }

    private void readLookUpType_1(int lookupTableLocation) throws IOException {
        this.rf.seek((long) lookupTableLocation);
        int posFormat = this.rf.readShort();
        if (posFormat == 1) {
            LOG.debug("Reading `Look Up Type 1, Format 1` ....");
            int coverageOffset = this.rf.readShort();
            int valueFormat = this.rf.readShort();
            if ((valueFormat & 1) == 1) {
                LOG.debug("xPlacement=" + this.rf.readShort());
            }
            if ((valueFormat & 2) == 2) {
                LOG.debug("yPlacement=" + this.rf.readShort());
            }
            LOG.debug("glyphCodes=" + readCoverageFormat(lookupTableLocation + coverageOffset));
            return;
        }
        System.err.println("The PosFormat " + posFormat + " for `LookupType 1` is not yet supported by " + GlyphPositioningTableReader.class.getSimpleName());
    }

    private void readLookUpType_4(int lookupTableLocation) throws IOException {
        this.rf.seek((long) lookupTableLocation);
        int posFormat = this.rf.readShort();
        if (posFormat == 1) {
            LOG.debug("Reading `Look Up Type 4, Format 1` ....");
            int markCoverageOffset = this.rf.readShort();
            int baseCoverageOffset = this.rf.readShort();
            int classCount = this.rf.readShort();
            int markArrayOffset = this.rf.readShort();
            int baseArrayOffset = this.rf.readShort();
            LOG.debug("markCoverages=" + readCoverageFormat(lookupTableLocation + markCoverageOffset));
            LOG.debug("baseCoverages=" + readCoverageFormat(lookupTableLocation + baseCoverageOffset));
            readMarkArrayTable(lookupTableLocation + markArrayOffset);
            readBaseArrayTable(lookupTableLocation + baseArrayOffset, classCount);
            return;
        }
        System.err.println("The posFormat " + posFormat + " is not supported by " + GlyphPositioningTableReader.class.getSimpleName());
    }

    private void readLookUpType_8(int lookupTableLocation) throws IOException {
        this.rf.seek((long) lookupTableLocation);
        int posFormat = this.rf.readShort();
        if (posFormat == 3) {
            LOG.debug("Reading `Look Up Type 8, Format 3` ....");
            readChainingContextPositioningFormat_3(lookupTableLocation);
            return;
        }
        System.err.println("The posFormat " + posFormat + " for `Look Up Type 8` is not supported by " + GlyphPositioningTableReader.class.getSimpleName());
    }

    private void readChainingContextPositioningFormat_3(int lookupTableLocation) throws IOException {
        int i;
        int backtrackGlyphCount = this.rf.readShort();
        LOG.debug("backtrackGlyphCount=" + backtrackGlyphCount);
        List<Integer> backtrackGlyphOffsets = new ArrayList(backtrackGlyphCount);
        for (i = 0; i < backtrackGlyphCount; i++) {
            backtrackGlyphOffsets.add(Integer.valueOf(this.rf.readShort()));
        }
        int inputGlyphCount = this.rf.readShort();
        LOG.debug("inputGlyphCount=" + inputGlyphCount);
        List<Integer> inputGlyphOffsets = new ArrayList(inputGlyphCount);
        for (i = 0; i < inputGlyphCount; i++) {
            inputGlyphOffsets.add(Integer.valueOf(this.rf.readShort()));
        }
        int lookaheadGlyphCount = this.rf.readShort();
        LOG.debug("lookaheadGlyphCount=" + lookaheadGlyphCount);
        List<Integer> lookaheadGlyphOffsets = new ArrayList(lookaheadGlyphCount);
        for (i = 0; i < lookaheadGlyphCount; i++) {
            lookaheadGlyphOffsets.add(Integer.valueOf(this.rf.readShort()));
        }
        int posCount = this.rf.readShort();
        LOG.debug("posCount=" + posCount);
        List<PosLookupRecord> arrayList = new ArrayList(posCount);
        for (i = 0; i < posCount; i++) {
            int sequenceIndex = this.rf.readShort();
            int lookupListIndex = this.rf.readShort();
            LOG.debug("sequenceIndex=" + sequenceIndex + ", lookupListIndex=" + lookupListIndex);
            arrayList.add(new PosLookupRecord(sequenceIndex, lookupListIndex));
        }
        for (Integer intValue : backtrackGlyphOffsets) {
            LOG.debug("backtrackGlyphs=" + readCoverageFormat(lookupTableLocation + intValue.intValue()));
        }
        for (Integer intValue2 : inputGlyphOffsets) {
            LOG.debug("inputGlyphs=" + readCoverageFormat(lookupTableLocation + intValue2.intValue()));
        }
        for (Integer intValue22 : lookaheadGlyphOffsets) {
            LOG.debug("lookaheadGlyphs=" + readCoverageFormat(lookupTableLocation + intValue22.intValue()));
        }
    }

    private void readMarkArrayTable(int markArrayLocation) throws IOException {
        this.rf.seek((long) markArrayLocation);
        int markCount = this.rf.readShort();
        List<MarkRecord> markRecords = new ArrayList();
        for (int i = 0; i < markCount; i++) {
            markRecords.add(readMarkRecord());
        }
        for (MarkRecord markRecord : markRecords) {
            readAnchorTable(markRecord.markAnchorOffset + markArrayLocation);
        }
    }

    private MarkRecord readMarkRecord() throws IOException {
        return new MarkRecord(this.rf.readShort(), this.rf.readShort());
    }

    private void readAnchorTable(int anchorTableLocation) throws IOException {
        this.rf.seek((long) anchorTableLocation);
        int anchorFormat = this.rf.readShort();
        if (anchorFormat != 1) {
            System.err.println("The extra features of the AnchorFormat " + anchorFormat + " will not be used");
        }
        int x = this.rf.readShort();
        int y = this.rf.readShort();
    }

    private void readBaseArrayTable(int baseArrayTableLocation, int classCount) throws IOException {
        this.rf.seek((long) baseArrayTableLocation);
        int baseCount = this.rf.readShort();
        Set<Integer> baseAnchors = new HashSet();
        for (int i = 0; i < baseCount; i++) {
            for (int k = 0; k < classCount; k++) {
                baseAnchors.add(Integer.valueOf(this.rf.readShort()));
            }
        }
        for (Integer intValue : baseAnchors) {
            readAnchorTable(baseArrayTableLocation + intValue.intValue());
        }
    }
}

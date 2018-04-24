package com.itextpdf.text.pdf.fonts.otf;

import com.itextpdf.text.io.RandomAccessSourceFactory;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class OpenTypeFontTableReader {
    protected static final Logger LOG = LoggerFactory.getLogger(OpenTypeFontTableReader.class);
    protected final RandomAccessFileOrArray rf;
    private List<String> supportedLanguages;
    protected final int tableLocation;

    protected abstract void readSubTable(int i, int i2) throws IOException;

    public OpenTypeFontTableReader(String fontFilePath, int tableLocation) throws IOException {
        this.rf = new RandomAccessFileOrArray(new RandomAccessSourceFactory().createBestSource(fontFilePath));
        this.tableLocation = tableLocation;
    }

    public Language getSupportedLanguage() throws FontReadingException {
        Language[] allLangs = Language.values();
        for (String supportedLang : this.supportedLanguages) {
            for (Language lang : allLangs) {
                if (lang.isSupported(supportedLang)) {
                    return lang;
                }
            }
        }
        throw new FontReadingException("Unsupported languages " + this.supportedLanguages);
    }

    protected final void startReadingTable() throws FontReadingException {
        try {
            TableHeader header = readHeader();
            readScriptListTable(this.tableLocation + header.scriptListOffset);
            readFeatureListTable(this.tableLocation + header.featureListOffset);
            readLookupListTable(this.tableLocation + header.lookupListOffset);
        } catch (IOException e) {
            throw new FontReadingException("Error reading font file", e);
        }
    }

    private void readLookupListTable(int lookupListTableLocation) throws IOException {
        int i;
        this.rf.seek((long) lookupListTableLocation);
        int lookupCount = this.rf.readShort();
        List<Integer> lookupTableOffsets = new ArrayList();
        for (i = 0; i < lookupCount; i++) {
            lookupTableOffsets.add(Integer.valueOf(this.rf.readShort()));
        }
        for (i = 0; i < lookupCount; i++) {
            readLookupTable(lookupListTableLocation + ((Integer) lookupTableOffsets.get(i)).intValue());
        }
    }

    private void readLookupTable(int lookupTableLocation) throws IOException {
        this.rf.seek((long) lookupTableLocation);
        int lookupType = this.rf.readShort();
        this.rf.skipBytes(2);
        int subTableCount = this.rf.readShort();
        List<Integer> subTableOffsets = new ArrayList();
        for (int i = 0; i < subTableCount; i++) {
            subTableOffsets.add(Integer.valueOf(this.rf.readShort()));
        }
        for (Integer intValue : subTableOffsets) {
            readSubTable(lookupType, lookupTableLocation + intValue.intValue());
        }
    }

    protected final List<Integer> readCoverageFormat(int coverageLocation) throws IOException {
        List<Integer> glyphIds;
        this.rf.seek((long) coverageLocation);
        int coverageFormat = this.rf.readShort();
        int i;
        if (coverageFormat == 1) {
            int glyphCount = this.rf.readShort();
            glyphIds = new ArrayList(glyphCount);
            for (i = 0; i < glyphCount; i++) {
                glyphIds.add(Integer.valueOf(this.rf.readShort()));
            }
        } else if (coverageFormat == 2) {
            int rangeCount = this.rf.readShort();
            glyphIds = new ArrayList();
            for (i = 0; i < rangeCount; i++) {
                readRangeRecord(glyphIds);
            }
        } else {
            throw new UnsupportedOperationException("Invalid coverage format: " + coverageFormat);
        }
        return Collections.unmodifiableList(glyphIds);
    }

    private void readRangeRecord(List<Integer> glyphIds) throws IOException {
        int startGlyphId = this.rf.readShort();
        int endGlyphId = this.rf.readShort();
        int startCoverageIndex = this.rf.readShort();
        for (int glyphId = startGlyphId; glyphId <= endGlyphId; glyphId++) {
            glyphIds.add(Integer.valueOf(glyphId));
        }
    }

    private void readScriptListTable(int scriptListTableLocationOffset) throws IOException {
        this.rf.seek((long) scriptListTableLocationOffset);
        int scriptCount = this.rf.readShort();
        Map<String, Integer> scriptRecords = new HashMap(scriptCount);
        for (int i = 0; i < scriptCount; i++) {
            readScriptRecord(scriptListTableLocationOffset, scriptRecords);
        }
        List<String> supportedLanguages = new ArrayList(scriptCount);
        for (String scriptName : scriptRecords.keySet()) {
            readScriptTable(((Integer) scriptRecords.get(scriptName)).intValue());
            supportedLanguages.add(scriptName);
        }
        this.supportedLanguages = Collections.unmodifiableList(supportedLanguages);
    }

    private void readScriptRecord(int scriptListTableLocationOffset, Map<String, Integer> scriptRecords) throws IOException {
        scriptRecords.put(this.rf.readString(4, "utf-8"), Integer.valueOf(scriptListTableLocationOffset + this.rf.readShort()));
    }

    private void readScriptTable(int scriptTableLocationOffset) throws IOException {
        this.rf.seek((long) scriptTableLocationOffset);
        int defaultLangSys = this.rf.readShort();
        int langSysCount = this.rf.readShort();
        if (langSysCount > 0) {
            Map<String, Integer> langSysRecords = new LinkedHashMap(langSysCount);
            for (int i = 0; i < langSysCount; i++) {
                readLangSysRecord(langSysRecords);
            }
            for (String langSysTag : langSysRecords.keySet()) {
                readLangSysTable(((Integer) langSysRecords.get(langSysTag)).intValue() + scriptTableLocationOffset);
            }
        }
        readLangSysTable(scriptTableLocationOffset + defaultLangSys);
    }

    private void readLangSysRecord(Map<String, Integer> langSysRecords) throws IOException {
        langSysRecords.put(this.rf.readString(4, "utf-8"), Integer.valueOf(this.rf.readShort()));
    }

    private void readLangSysTable(int langSysTableLocationOffset) throws IOException {
        this.rf.seek((long) langSysTableLocationOffset);
        LOG.debug("lookupOrderOffset=" + this.rf.readShort());
        LOG.debug("reqFeatureIndex=" + this.rf.readShort());
        int featureCount = this.rf.readShort();
        List<Short> featureListIndices = new ArrayList(featureCount);
        for (int i = 0; i < featureCount; i++) {
            featureListIndices.add(Short.valueOf(this.rf.readShort()));
        }
        LOG.debug("featureListIndices=" + featureListIndices);
    }

    private void readFeatureListTable(int featureListTableLocationOffset) throws IOException {
        this.rf.seek((long) featureListTableLocationOffset);
        int featureCount = this.rf.readShort();
        LOG.debug("featureCount=" + featureCount);
        Map<String, Short> featureRecords = new LinkedHashMap(featureCount);
        for (int i = 0; i < featureCount; i++) {
            featureRecords.put(this.rf.readString(4, "utf-8"), Short.valueOf(this.rf.readShort()));
        }
        for (String featureName : featureRecords.keySet()) {
            LOG.debug("*************featureName=" + featureName);
            readFeatureTable(((Short) featureRecords.get(featureName)).shortValue() + featureListTableLocationOffset);
        }
    }

    private void readFeatureTable(int featureTableLocationOffset) throws IOException {
        this.rf.seek((long) featureTableLocationOffset);
        LOG.debug("featureParamsOffset=" + this.rf.readShort());
        int lookupCount = this.rf.readShort();
        LOG.debug("lookupCount=" + lookupCount);
        List<Short> lookupListIndices = new ArrayList(lookupCount);
        for (int i = 0; i < lookupCount; i++) {
            lookupListIndices.add(Short.valueOf(this.rf.readShort()));
        }
    }

    private TableHeader readHeader() throws IOException {
        this.rf.seek((long) this.tableLocation);
        return new TableHeader(this.rf.readInt(), this.rf.readUnsignedShort(), this.rf.readUnsignedShort(), this.rf.readUnsignedShort());
    }
}

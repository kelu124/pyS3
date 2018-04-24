package com.itextpdf.text.pdf.fonts.otf;

public class TableHeader {
    public int featureListOffset;
    public int lookupListOffset;
    public int scriptListOffset;
    public int version;

    public TableHeader(int version, int scriptListOffset, int featureListOffset, int lookupListOffset) {
        this.version = version;
        this.scriptListOffset = scriptListOffset;
        this.featureListOffset = featureListOffset;
        this.lookupListOffset = lookupListOffset;
    }
}

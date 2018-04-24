package org.apache.poi.hpsf.extractor;

import org.apache.poi.POIDocument;
import org.apache.poi.POIOLE2TextExtractor;
import org.apache.poi.POITextExtractor;
import org.apache.poi.hpsf.CustomProperties;
import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hpsf.HPSFPropertiesOnlyDocument;
import org.apache.poi.hpsf.Property;
import org.apache.poi.hpsf.SpecialPropertySet;
import org.apache.poi.hpsf.wellknown.PropertyIDMap;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class HPSFPropertiesExtractor extends POIOLE2TextExtractor {

    private static abstract class HelperPropertySet extends SpecialPropertySet {
        public HelperPropertySet() {
            super(null);
        }

        public static String getPropertyValueText(Object val) {
            if (val == null) {
                return "(not set)";
            }
            return SpecialPropertySet.getPropertyStringValue(val);
        }
    }

    public static void main(java.lang.String[] r7) throws java.io.IOException {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.ssa.SSATransform.placePhi(SSATransform.java:82)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:50)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r0 = r7;
        r4 = r0.length;
        r3 = 0;
    L_0x0003:
        if (r3 >= r4) goto L_0x002a;
    L_0x0005:
        r2 = r0[r3];
        r1 = new org.apache.poi.hpsf.extractor.HPSFPropertiesExtractor;
        r5 = new org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
        r6 = new java.io.File;
        r6.<init>(r2);
        r5.<init>(r6);
        r1.<init>(r5);
        r5 = java.lang.System.out;	 Catch:{ all -> 0x0025 }
        r6 = r1.getText();	 Catch:{ all -> 0x0025 }
        r5.println(r6);	 Catch:{ all -> 0x0025 }
        r1.close();
        r3 = r3 + 1;
        goto L_0x0003;
    L_0x0025:
        r5 = move-exception;
        r1.close();
        throw r5;
    L_0x002a:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.poi.hpsf.extractor.HPSFPropertiesExtractor.main(java.lang.String[]):void");
    }

    public HPSFPropertiesExtractor(POIOLE2TextExtractor mainExtractor) {
        super(mainExtractor);
    }

    public HPSFPropertiesExtractor(POIDocument doc) {
        super(doc);
    }

    public HPSFPropertiesExtractor(POIFSFileSystem fs) {
        super(new HPSFPropertiesOnlyDocument(fs));
    }

    public HPSFPropertiesExtractor(NPOIFSFileSystem fs) {
        super(new HPSFPropertiesOnlyDocument(fs));
    }

    public String getDocumentSummaryInformationText() {
        if (this.document == null) {
            return "";
        }
        DocumentSummaryInformation dsi = this.document.getDocumentSummaryInformation();
        StringBuilder text = new StringBuilder();
        text.append(getPropertiesText(dsi));
        CustomProperties cps = dsi == null ? null : dsi.getCustomProperties();
        if (cps != null) {
            for (String key : cps.nameSet()) {
                text.append(key).append(" = ").append(HelperPropertySet.getPropertyValueText(cps.get(key))).append("\n");
            }
        }
        return text.toString();
    }

    public String getSummaryInformationText() {
        if (this.document == null) {
            return "";
        }
        return getPropertiesText(this.document.getSummaryInformation());
    }

    private static String getPropertiesText(SpecialPropertySet ps) {
        if (ps == null) {
            return "";
        }
        StringBuilder text = new StringBuilder();
        PropertyIDMap idMap = ps.getPropertySetIDMap();
        for (Property prop : ps.getProperties()) {
            String type = Long.toString(prop.getID());
            Object typeObj = idMap.get(prop.getID());
            if (typeObj != null) {
                type = typeObj.toString();
            }
            text.append(type).append(" = ").append(HelperPropertySet.getPropertyValueText(prop.getValue())).append("\n");
        }
        return text.toString();
    }

    public String getText() {
        return getSummaryInformationText() + getDocumentSummaryInformationText();
    }

    public POITextExtractor getMetadataTextExtractor() {
        throw new IllegalStateException("You already have the Metadata Text Extractor, not recursing!");
    }

    public boolean equals(Object o) {
        return super.equals(o);
    }

    public int hashCode() {
        return super.hashCode();
    }
}

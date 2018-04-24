package org.apache.poi.ss.util;

final class CellUtil$UnicodeMapping {
    public final String entityName;
    public final String resolvedValue;

    public CellUtil$UnicodeMapping(String pEntityName, String pResolvedValue) {
        this.entityName = "&" + pEntityName + ";";
        this.resolvedValue = pResolvedValue;
    }
}

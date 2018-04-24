package org.apache.poi.hssf.record;

import org.apache.poi.hssf.record.common.UnicodeString;
import org.apache.poi.util.IntMapper;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

class SSTDeserializer {
    private static POILogger logger = POILogFactory.getLogger(SSTDeserializer.class);
    private IntMapper<UnicodeString> strings;

    public SSTDeserializer(IntMapper<UnicodeString> strings) {
        this.strings = strings;
    }

    public void manufactureStrings(int stringCount, RecordInputStream in) {
        for (int i = 0; i < stringCount; i++) {
            UnicodeString str;
            if (in.available() != 0 || in.hasNextRecord()) {
                str = new UnicodeString(in);
            } else {
                logger.log(7, new Object[]{"Ran out of data before creating all the strings! String at index " + i + ""});
                str = new UnicodeString("");
            }
            addToStringTable(this.strings, str);
        }
    }

    public static void addToStringTable(IntMapper<UnicodeString> strings, UnicodeString string) {
        strings.add(string);
    }
}

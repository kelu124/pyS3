package org.apache.poi.hpsf.wellknown;

import com.itextpdf.text.pdf.BaseField;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PropertyIDMap extends HashMap<Long, String> {
    public static final int PID_APPNAME = 18;
    public static final int PID_AUTHOR = 4;
    public static final int PID_BYTECOUNT = 4;
    public static final int PID_CATEGORY = 2;
    public static final int PID_CCHWITHSPACES = 17;
    public static final int PID_CHARCOUNT = 16;
    public static final int PID_CODEPAGE = 1;
    public static final int PID_COMMENTS = 6;
    public static final int PID_COMPANY = 15;
    public static final int PID_CONTENTSTATUS = 27;
    public static final int PID_CONTENTTYPE = 26;
    public static final int PID_CREATE_DTM = 12;
    public static final int PID_DICTIONARY = 0;
    public static final int PID_DIGSIG = 24;
    public static final int PID_DOCPARTS = 13;
    public static final int PID_DOCVERSION = 29;
    public static final int PID_EDITTIME = 10;
    public static final int PID_HEADINGPAIR = 12;
    public static final int PID_HIDDENCOUNT = 9;
    public static final int PID_HYPERLINKSCHANGED = 22;
    public static final int PID_KEYWORDS = 5;
    public static final int PID_LANGUAGE = 28;
    public static final int PID_LASTAUTHOR = 8;
    public static final int PID_LASTPRINTED = 11;
    public static final int PID_LASTSAVE_DTM = 13;
    public static final int PID_LINECOUNT = 5;
    public static final int PID_LINKSDIRTY = 16;
    public static final int PID_MANAGER = 14;
    public static final int PID_MAX = 31;
    public static final int PID_MMCLIPCOUNT = 10;
    public static final int PID_NOTECOUNT = 8;
    public static final int PID_PAGECOUNT = 14;
    public static final int PID_PARCOUNT = 6;
    public static final int PID_PRESFORMAT = 3;
    public static final int PID_REVNUMBER = 9;
    public static final int PID_SCALE = 11;
    public static final int PID_SECURITY = 19;
    public static final int PID_SLIDECOUNT = 7;
    public static final int PID_SUBJECT = 3;
    public static final int PID_TEMPLATE = 7;
    public static final int PID_THUMBNAIL = 17;
    public static final int PID_TITLE = 2;
    public static final int PID_VERSION = 23;
    public static final int PID_WORDCOUNT = 15;
    private static PropertyIDMap documentSummaryInformationProperties;
    private static PropertyIDMap summaryInformationProperties;

    public PropertyIDMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public PropertyIDMap(Map<Long, String> map) {
        super(map);
    }

    public Object put(long id, String idString) {
        return put(Long.valueOf(id), idString);
    }

    public Object get(long id) {
        return get(Long.valueOf(id));
    }

    public static PropertyIDMap getSummaryInformationProperties() {
        if (summaryInformationProperties == null) {
            PropertyIDMap m = new PropertyIDMap(18, BaseField.BORDER_WIDTH_THIN);
            m.put(2, "PID_TITLE");
            m.put(3, "PID_SUBJECT");
            m.put(4, "PID_AUTHOR");
            m.put(5, "PID_KEYWORDS");
            m.put(6, "PID_COMMENTS");
            m.put(7, "PID_TEMPLATE");
            m.put(8, "PID_LASTAUTHOR");
            m.put(9, "PID_REVNUMBER");
            m.put(10, "PID_EDITTIME");
            m.put(11, "PID_LASTPRINTED");
            m.put(12, "PID_CREATE_DTM");
            m.put(13, "PID_LASTSAVE_DTM");
            m.put(14, "PID_PAGECOUNT");
            m.put(15, "PID_WORDCOUNT");
            m.put(16, "PID_CHARCOUNT");
            m.put(17, "PID_THUMBNAIL");
            m.put(18, "PID_APPNAME");
            m.put(19, "PID_SECURITY");
            summaryInformationProperties = new PropertyIDMap(Collections.unmodifiableMap(m));
        }
        return summaryInformationProperties;
    }

    public static PropertyIDMap getDocumentSummaryInformationProperties() {
        if (documentSummaryInformationProperties == null) {
            PropertyIDMap m = new PropertyIDMap(17, BaseField.BORDER_WIDTH_THIN);
            m.put(0, "PID_DICTIONARY");
            m.put(1, "PID_CODEPAGE");
            m.put(2, "PID_CATEGORY");
            m.put(3, "PID_PRESFORMAT");
            m.put(4, "PID_BYTECOUNT");
            m.put(5, "PID_LINECOUNT");
            m.put(6, "PID_PARCOUNT");
            m.put(7, "PID_SLIDECOUNT");
            m.put(8, "PID_NOTECOUNT");
            m.put(9, "PID_HIDDENCOUNT");
            m.put(10, "PID_MMCLIPCOUNT");
            m.put(11, "PID_SCALE");
            m.put(12, "PID_HEADINGPAIR");
            m.put(13, "PID_DOCPARTS");
            m.put(14, "PID_MANAGER");
            m.put(15, "PID_COMPANY");
            m.put(16, "PID_LINKSDIRTY");
            documentSummaryInformationProperties = new PropertyIDMap(Collections.unmodifiableMap(m));
        }
        return documentSummaryInformationProperties;
    }

    public static void main(String[] args) {
        PropertyIDMap s1 = getSummaryInformationProperties();
        PropertyIDMap s2 = getDocumentSummaryInformationProperties();
        System.out.println("s1: " + s1);
        System.out.println("s2: " + s2);
    }
}

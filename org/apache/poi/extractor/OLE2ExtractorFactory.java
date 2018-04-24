package org.apache.poi.extractor;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.POIOLE2TextExtractor;
import org.apache.poi.POITextExtractor;
import org.apache.poi.hssf.OldExcelFormatException;
import org.apache.poi.hssf.extractor.EventBasedExcelExtractor;
import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hssf.model.InternalWorkbook;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.Entry;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.poifs.filesystem.OPOIFSFileSystem;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

public class OLE2ExtractorFactory {
    private static final POILogger LOGGER = POILogFactory.getLogger(OLE2ExtractorFactory.class);
    private static Boolean allPreferEventExtractors;
    private static final ThreadLocal<Boolean> threadPreferEventExtractors = new C10501();

    static class C10501 extends ThreadLocal<Boolean> {
        C10501() {
        }

        protected Boolean initialValue() {
            return Boolean.FALSE;
        }
    }

    public static boolean getThreadPrefersEventExtractors() {
        return ((Boolean) threadPreferEventExtractors.get()).booleanValue();
    }

    public static Boolean getAllThreadsPreferEventExtractors() {
        return allPreferEventExtractors;
    }

    public static void setThreadPrefersEventExtractors(boolean preferEventExtractors) {
        threadPreferEventExtractors.set(Boolean.valueOf(preferEventExtractors));
    }

    public static void setAllThreadsPreferEventExtractors(Boolean preferEventExtractors) {
        allPreferEventExtractors = preferEventExtractors;
    }

    protected static boolean getPreferEventExtractor() {
        if (allPreferEventExtractors != null) {
            return allPreferEventExtractors.booleanValue();
        }
        return ((Boolean) threadPreferEventExtractors.get()).booleanValue();
    }

    public static POIOLE2TextExtractor createExtractor(POIFSFileSystem fs) throws IOException {
        return (POIOLE2TextExtractor) createExtractor(fs.getRoot());
    }

    public static POIOLE2TextExtractor createExtractor(NPOIFSFileSystem fs) throws IOException {
        return (POIOLE2TextExtractor) createExtractor(fs.getRoot());
    }

    public static POIOLE2TextExtractor createExtractor(OPOIFSFileSystem fs) throws IOException {
        return (POIOLE2TextExtractor) createExtractor(fs.getRoot());
    }

    public static POITextExtractor createExtractor(InputStream input) throws IOException {
        Class<?> cls = getOOXMLClass();
        if (cls == null) {
            return createExtractor(new NPOIFSFileSystem(input));
        }
        try {
            return (POITextExtractor) cls.getDeclaredMethod("createExtractor", new Class[]{InputStream.class}).invoke(null, new Object[]{input});
        } catch (IllegalArgumentException iae) {
            throw iae;
        } catch (Exception e) {
            throw new IllegalArgumentException("Error creating Extractor for InputStream", e);
        }
    }

    private static Class<?> getOOXMLClass() {
        try {
            return OLE2ExtractorFactory.class.getClassLoader().loadClass("org.apache.poi.extractor.ExtractorFactory");
        } catch (ClassNotFoundException e) {
            LOGGER.log(5, new Object[]{"POI OOXML jar missing"});
            return null;
        }
    }

    private static Class<?> getScratchpadClass() {
        try {
            return OLE2ExtractorFactory.class.getClassLoader().loadClass("org.apache.poi.extractor.OLE2ScratchpadExtractorFactory");
        } catch (ClassNotFoundException e) {
            LOGGER.log(7, new Object[]{"POI Scratchpad jar missing"});
            throw new IllegalStateException("POI Scratchpad jar missing, required for ExtractorFactory");
        }
    }

    public static POITextExtractor createExtractor(DirectoryNode poifsDir) throws IOException {
        String[] arr$ = InternalWorkbook.WORKBOOK_DIR_ENTRY_NAMES;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            if (!poifsDir.hasEntry(arr$[i$])) {
                i$++;
            } else if (getPreferEventExtractor()) {
                return new EventBasedExcelExtractor(poifsDir);
            } else {
                return new ExcelExtractor(poifsDir);
            }
        }
        if (poifsDir.hasEntry(InternalWorkbook.OLD_WORKBOOK_DIR_ENTRY_NAME)) {
            throw new OldExcelFormatException("Old Excel Spreadsheet format (1-95) found. Please call OldExcelExtractor directly for basic text extraction");
        }
        try {
            POITextExtractor ext = (POITextExtractor) getScratchpadClass().getDeclaredMethod("createExtractor", new Class[]{DirectoryNode.class}).invoke(null, new Object[]{poifsDir});
            if (ext != null) {
                return ext;
            }
            throw new IllegalArgumentException("No supported documents found in the OLE2 stream");
        } catch (IllegalArgumentException iae) {
            throw iae;
        } catch (Exception e) {
            throw new IllegalArgumentException("Error creating Scratchpad Extractor", e);
        }
    }

    public static POITextExtractor[] getEmbededDocsTextExtractors(POIOLE2TextExtractor ext) throws IOException {
        List<Entry> dirs = new ArrayList();
        List<InputStream> nonPOIFS = new ArrayList();
        DirectoryEntry root = ext.getRoot();
        if (root == null) {
            throw new IllegalStateException("The extractor didn't know which POIFS it came from!");
        }
        if (ext instanceof ExcelExtractor) {
            Iterator<Entry> it = root.getEntries();
            while (it.hasNext()) {
                Entry entry = (Entry) it.next();
                if (entry.getName().startsWith("MBD")) {
                    dirs.add(entry);
                }
            }
        } else {
            try {
                getScratchpadClass().getDeclaredMethod("identifyEmbeddedResources", new Class[]{POIOLE2TextExtractor.class, List.class, List.class}).invoke(null, new Object[]{ext, dirs, nonPOIFS});
            } catch (Exception e) {
                throw new IllegalArgumentException("Error checking for Scratchpad embedded resources", e);
            }
        }
        if (dirs.size() == 0 && nonPOIFS.size() == 0) {
            return new POITextExtractor[0];
        }
        ArrayList<POITextExtractor> e2 = new ArrayList();
        for (Entry dir : dirs) {
            e2.add(createExtractor((DirectoryNode) dir));
        }
        for (InputStream nonPOIF : nonPOIFS) {
            try {
                e2.add(createExtractor(nonPOIF));
            } catch (IllegalArgumentException ie) {
                LOGGER.log(5, new Object[]{ie});
            } catch (Exception xe) {
                LOGGER.log(5, new Object[]{xe});
            }
        }
        return (POITextExtractor[]) e2.toArray(new POITextExtractor[e2.size()]);
    }
}

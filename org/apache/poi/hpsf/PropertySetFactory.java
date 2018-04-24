package org.apache.poi.hpsf;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import org.apache.poi.hpsf.wellknown.SectionIDMap;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.DocumentInputStream;

public class PropertySetFactory {
    public static PropertySet create(DirectoryEntry dir, String name) throws FileNotFoundException, NoPropertySetStreamException, IOException, UnsupportedEncodingException {
        Throwable th;
        InputStream inp = null;
        try {
            PropertySet create;
            InputStream inp2 = new DocumentInputStream((DocumentEntry) dir.getEntry(name));
            try {
                create = create(inp2);
                if (inp2 != null) {
                    inp2.close();
                }
            } catch (MarkUnsupportedException e) {
                create = null;
                if (inp2 != null) {
                    inp2.close();
                }
            } catch (Throwable th2) {
                th = th2;
                inp = inp2;
                if (inp != null) {
                    inp.close();
                }
                throw th;
            }
            return create;
        } catch (Throwable th3) {
            th = th3;
            if (inp != null) {
                inp.close();
            }
            throw th;
        }
    }

    public static PropertySet create(InputStream stream) throws NoPropertySetStreamException, MarkUnsupportedException, UnsupportedEncodingException, IOException {
        PropertySet ps = new PropertySet(stream);
        try {
            if (ps.isSummaryInformation()) {
                return new SummaryInformation(ps);
            }
            if (ps.isDocumentSummaryInformation()) {
                return new DocumentSummaryInformation(ps);
            }
            return ps;
        } catch (UnexpectedPropertySetTypeException ex) {
            throw new IllegalStateException(ex);
        }
    }

    public static SummaryInformation newSummaryInformation() {
        MutablePropertySet ps = new MutablePropertySet();
        ((MutableSection) ps.getFirstSection()).setFormatID(SectionIDMap.SUMMARY_INFORMATION_ID);
        try {
            return new SummaryInformation(ps);
        } catch (Throwable ex) {
            throw new HPSFRuntimeException(ex);
        }
    }

    public static DocumentSummaryInformation newDocumentSummaryInformation() {
        MutablePropertySet ps = new MutablePropertySet();
        ((MutableSection) ps.getFirstSection()).setFormatID(SectionIDMap.DOCUMENT_SUMMARY_INFORMATION_ID[0]);
        try {
            return new DocumentSummaryInformation(ps);
        } catch (Throwable ex) {
            throw new HPSFRuntimeException(ex);
        }
    }
}

package org.apache.poi.poifs.eventfilesystem;

import org.apache.poi.poifs.filesystem.DocumentInputStream;
import org.apache.poi.poifs.filesystem.POIFSDocumentPath;

public class POIFSReaderEvent {
    private final String documentName;
    private final POIFSDocumentPath path;
    private final DocumentInputStream stream;

    POIFSReaderEvent(DocumentInputStream stream, POIFSDocumentPath path, String documentName) {
        this.stream = stream;
        this.path = path;
        this.documentName = documentName;
    }

    public DocumentInputStream getStream() {
        return this.stream;
    }

    public POIFSDocumentPath getPath() {
        return this.path;
    }

    public String getName() {
        return this.documentName;
    }
}

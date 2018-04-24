package org.apache.poi.poifs.filesystem;

public class POIFSWriterEvent {
    private String documentName;
    private int limit;
    private POIFSDocumentPath path;
    private DocumentOutputStream stream;

    POIFSWriterEvent(DocumentOutputStream stream, POIFSDocumentPath path, String documentName, int limit) {
        this.stream = stream;
        this.path = path;
        this.documentName = documentName;
        this.limit = limit;
    }

    public DocumentOutputStream getStream() {
        return this.stream;
    }

    public POIFSDocumentPath getPath() {
        return this.path;
    }

    public String getName() {
        return this.documentName;
    }

    public int getLimit() {
        return this.limit;
    }
}

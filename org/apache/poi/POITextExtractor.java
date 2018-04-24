package org.apache.poi;

import java.io.Closeable;
import java.io.IOException;

public abstract class POITextExtractor implements Closeable {
    private Closeable fsToClose = null;

    public abstract POITextExtractor getMetadataTextExtractor();

    public abstract String getText();

    public void setFilesystem(Closeable fs) {
        this.fsToClose = fs;
    }

    public void close() throws IOException {
        if (this.fsToClose != null) {
            this.fsToClose.close();
        }
    }
}

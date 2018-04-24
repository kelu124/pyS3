package org.apache.poi.hpsf;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.POIDocument;
import org.apache.poi.poifs.filesystem.EntryUtils;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.poifs.filesystem.OPOIFSFileSystem;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class HPSFPropertiesOnlyDocument extends POIDocument {
    public HPSFPropertiesOnlyDocument(NPOIFSFileSystem fs) {
        super(fs.getRoot());
    }

    public HPSFPropertiesOnlyDocument(OPOIFSFileSystem fs) {
        super(fs);
    }

    public HPSFPropertiesOnlyDocument(POIFSFileSystem fs) {
        super(fs);
    }

    public void write() throws IOException {
        NPOIFSFileSystem fs = this.directory.getFileSystem();
        validateInPlaceWritePossible();
        writeProperties(fs, null);
        fs.writeFilesystem();
    }

    public void write(File newFile) throws IOException {
        NPOIFSFileSystem fs = POIFSFileSystem.create(newFile);
        try {
            write(fs);
            fs.writeFilesystem();
        } finally {
            fs.close();
        }
    }

    public void write(OutputStream out) throws IOException {
        NPOIFSFileSystem fs = new NPOIFSFileSystem();
        try {
            write(fs);
            fs.writeFilesystem(out);
        } finally {
            fs.close();
        }
    }

    private void write(NPOIFSFileSystem fs) throws IOException {
        List<String> excepts = new ArrayList(2);
        writeProperties(fs, excepts);
        EntryUtils.copyNodes(this.directory, fs.getRoot(), excepts);
    }
}

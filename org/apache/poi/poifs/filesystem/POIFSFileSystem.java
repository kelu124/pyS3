package org.apache.poi.poifs.filesystem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.poifs.dev.POIFSViewable;
import org.apache.poi.util.CloseIgnoringInputStream;

public class POIFSFileSystem extends NPOIFSFileSystem implements POIFSViewable {
    public static InputStream createNonClosingInputStream(InputStream is) {
        return new CloseIgnoringInputStream(is);
    }

    public POIFSFileSystem(InputStream stream) throws IOException {
        super(stream);
    }

    public POIFSFileSystem(File file, boolean readOnly) throws IOException {
        super(file, readOnly);
    }

    public POIFSFileSystem(File file) throws IOException {
        super(file);
    }

    public static boolean hasPOIFSHeader(InputStream inp) throws IOException {
        return NPOIFSFileSystem.hasPOIFSHeader(inp);
    }

    public static boolean hasPOIFSHeader(byte[] header8Bytes) {
        return NPOIFSFileSystem.hasPOIFSHeader(header8Bytes);
    }

    public static POIFSFileSystem create(File file) throws IOException {
        POIFSFileSystem tmp = new POIFSFileSystem();
        FileOutputStream fout = new FileOutputStream(file);
        tmp.writeFilesystem(fout);
        fout.close();
        tmp.close();
        return new POIFSFileSystem(file, false);
    }

    public static void main(String[] args) throws IOException {
        OPOIFSFileSystem.main(args);
    }
}

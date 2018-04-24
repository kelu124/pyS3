package org.apache.poi.poifs.dev;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Iterator;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.DocumentInputStream;
import org.apache.poi.poifs.filesystem.DocumentNode;
import org.apache.poi.poifs.filesystem.Entry;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.poifs.filesystem.NPOIFSStream;
import org.apache.poi.util.IOUtils;

public class POIFSDump {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("Must specify at least one file to dump");
            System.exit(1);
        }
        boolean dumpProps = false;
        boolean dumpMini = false;
        for (String filename : args) {
            if (filename.equalsIgnoreCase("-dumprops") || filename.equalsIgnoreCase("-dump-props") || filename.equalsIgnoreCase("-dump-properties")) {
                dumpProps = true;
            } else if (filename.equalsIgnoreCase("-dumpmini") || filename.equalsIgnoreCase("-dump-mini") || filename.equalsIgnoreCase("-dump-ministream") || filename.equalsIgnoreCase("-dump-mini-stream")) {
                dumpMini = true;
            } else {
                System.out.println("Dumping " + filename);
                FileInputStream is = new FileInputStream(filename);
                try {
                    NPOIFSFileSystem fs = new NPOIFSFileSystem(is);
                    DirectoryEntry root = fs.getRoot();
                    File file = new File(new File(filename).getName(), root.getName());
                    if (file.exists() || file.mkdirs()) {
                        dump(root, file);
                        if (dumpProps) {
                            dump(fs, fs.getHeaderBlock().getPropertyStart(), "properties", file);
                        }
                        if (dumpMini) {
                            int startBlock = fs.getPropertyTable().getRoot().getStartBlock();
                            if (startBlock == -2) {
                                System.err.println("No Mini Stream in file");
                            } else {
                                try {
                                    dump(fs, startBlock, "mini-stream", file);
                                } catch (Throwable th) {
                                    fs.close();
                                }
                            }
                        }
                        fs.close();
                    } else {
                        throw new IOException("Could not create directory " + file);
                    }
                } finally {
                    is.close();
                }
            }
        }
    }

    public static void dump(DirectoryEntry root, File parent) throws IOException {
        Iterator<Entry> it = root.getEntries();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            if (entry instanceof DocumentNode) {
                DocumentEntry node = (DocumentNode) entry;
                DocumentInputStream is = new DocumentInputStream(node);
                byte[] bytes = IOUtils.toByteArray(is);
                is.close();
                OutputStream out = new FileOutputStream(new File(parent, node.getName().trim()));
                try {
                    out.write(bytes);
                } finally {
                    out.close();
                }
            } else if (entry instanceof DirectoryEntry) {
                DirectoryEntry dir = (DirectoryEntry) entry;
                File file = new File(parent, entry.getName());
                if (file.exists() || file.mkdirs()) {
                    dump(dir, file);
                } else {
                    throw new IOException("Could not create directory " + file);
                }
            } else {
                System.err.println("Skipping unsupported POIFS entry: " + entry);
            }
        }
    }

    public static void dump(NPOIFSFileSystem fs, int startBlock, String name, File parent) throws IOException {
        FileOutputStream out = new FileOutputStream(new File(parent, name));
        try {
            byte[] b = new byte[fs.getBigBlockSize()];
            Iterator i$ = new NPOIFSStream(fs, startBlock).iterator();
            while (i$.hasNext()) {
                ByteBuffer bb = (ByteBuffer) i$.next();
                int len = bb.remaining();
                bb.get(b);
                out.write(b, 0, len);
            }
        } finally {
            out.close();
        }
    }
}

package org.apache.poi.poifs.dev;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.DocumentNode;
import org.apache.poi.poifs.filesystem.Entry;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class POIFSLister {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("Must specify at least one file to view");
            System.exit(1);
        }
        boolean withSizes = false;
        boolean newPOIFS = true;
        int j = 0;
        while (j < args.length) {
            if (args[j].equalsIgnoreCase("-size") || args[j].equalsIgnoreCase("-sizes")) {
                withSizes = true;
            } else if (args[j].equalsIgnoreCase("-old") || args[j].equalsIgnoreCase("-old-poifs")) {
                newPOIFS = false;
            } else if (newPOIFS) {
                viewFile(args[j], withSizes);
            } else {
                viewFileOld(args[j], withSizes);
            }
            j++;
        }
    }

    public static void viewFile(String filename, boolean withSizes) throws IOException {
        displayDirectory(new NPOIFSFileSystem(new File(filename)).getRoot(), "", withSizes);
    }

    public static void viewFileOld(String filename, boolean withSizes) throws IOException {
        displayDirectory(new POIFSFileSystem(new FileInputStream(filename)).getRoot(), "", withSizes);
    }

    public static void displayDirectory(DirectoryNode dir, String indent, boolean withSizes) {
        System.out.println(indent + dir.getName() + " -");
        String newIndent = indent + "  ";
        boolean hadChildren = false;
        Iterator<Entry> it = dir.getEntries();
        while (it.hasNext()) {
            hadChildren = true;
            Entry entry = (Entry) it.next();
            if (entry instanceof DirectoryNode) {
                displayDirectory((DirectoryNode) entry, newIndent, withSizes);
            } else {
                DocumentNode doc = (DocumentNode) entry;
                String name = doc.getName();
                String size = "";
                if (name.charAt(0) < '\n') {
                    name = name.substring(1) + " <" + ("(0x0" + name.charAt(0) + ")" + name.substring(1)) + ">";
                }
                if (withSizes) {
                    size = " [" + doc.getSize() + " / 0x" + Integer.toHexString(doc.getSize()) + "]";
                }
                System.out.println(newIndent + name + size);
            }
        }
        if (!hadChildren) {
            System.out.println(newIndent + "(no children)");
        }
    }
}

package org.apache.poi.poifs.dev;

import java.io.File;
import java.io.IOException;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;

public class POIFSViewer {
    public static void main(String[] args) {
        boolean printNames = true;
        if (args.length == 0) {
            System.err.println("Must specify at least one file to view");
            System.exit(1);
        }
        if (args.length <= 1) {
            printNames = false;
        }
        for (String viewFile : args) {
            viewFile(viewFile, printNames);
        }
    }

    private static void viewFile(String filename, boolean printName) {
        if (printName) {
            StringBuffer flowerbox = new StringBuffer();
            flowerbox.append(".");
            for (int j = 0; j < filename.length(); j++) {
                flowerbox.append("-");
            }
            flowerbox.append(".");
            System.out.println(flowerbox);
            System.out.println("|" + filename + "|");
            System.out.println(flowerbox);
        }
        try {
            NPOIFSFileSystem fs = new NPOIFSFileSystem(new File(filename));
            for (String s : POIFSViewEngine.inspectViewable(fs, true, 0, "  ")) {
                System.out.print(s);
            }
            fs.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

package org.apache.poi.poifs.macros;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.poi.util.StringUtil;

public class VBAMacroExtractor {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("Use:");
            System.err.println("   VBAMacroExtractor <office.doc> [output]");
            System.err.println("");
            System.err.println("If an output directory is given, macros are written there");
            System.err.println("Otherwise they are output to the screen");
            System.exit(1);
        }
        File input = new File(args[0]);
        File output = null;
        if (args.length > 1) {
            output = new File(args[1]);
        }
        new VBAMacroExtractor().extract(input, output);
    }

    public void extract(File input, File outputDir, String extension) throws IOException {
        if (input.exists()) {
            System.err.print("Extracting VBA Macros from " + input + " to ");
            if (outputDir == null) {
                System.err.println("STDOUT");
            } else if (outputDir.exists() || outputDir.mkdirs()) {
                System.err.println(outputDir);
            } else {
                throw new IOException("Output directory " + outputDir + " could not be created");
            }
            VBAMacroReader reader = new VBAMacroReader(input);
            Map<String, String> macros = reader.readMacros();
            reader.close();
            String divider = "---------------------------------------";
            for (Entry<String, String> entry : macros.entrySet()) {
                String moduleName = (String) entry.getKey();
                String moduleCode = (String) entry.getValue();
                if (outputDir == null) {
                    System.out.println("---------------------------------------");
                    System.out.println(moduleName);
                    System.out.println("");
                    System.out.println(moduleCode);
                } else {
                    File out = new File(outputDir, moduleName + extension);
                    FileOutputStream fout = new FileOutputStream(out);
                    OutputStreamWriter fwriter = new OutputStreamWriter(fout, StringUtil.UTF8);
                    fwriter.write(moduleCode);
                    fwriter.close();
                    fout.close();
                    System.out.println("Extracted " + out);
                }
            }
            if (outputDir == null) {
                System.out.println("---------------------------------------");
                return;
            }
            return;
        }
        throw new FileNotFoundException(input.toString());
    }

    public void extract(File input, File outputDir) throws IOException {
        extract(input, outputDir, ".vba");
    }
}

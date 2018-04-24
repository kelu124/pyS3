package org.apache.poi.dev;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.poi.util.XMLHelper;
import org.w3c.dom.Element;

public class RecordGenerator {
    public static void main(String[] args) throws Exception {
        Class.forName("org.apache.poi.generator.FieldIterator");
        if (args.length != 4) {
            System.out.println("Usage:");
            System.out.println("  java org.apache.poi.hssf.util.RecordGenerator RECORD_DEFINTIONS RECORD_STYLES DEST_SRC_PATH TEST_SRC_PATH");
            return;
        }
        generateRecords(args[0], args[1], args[2], args[3]);
    }

    private static void generateRecords(String defintionsDir, String recordStyleDir, String destSrcPathDir, String testSrcPathDir) throws Exception {
        File[] definitionsFiles = new File(defintionsDir).listFiles();
        if (definitionsFiles == null) {
            System.err.println(defintionsDir + " is not a directory.");
            return;
        }
        for (File file : definitionsFiles) {
            if (file.isFile() && (file.getName().endsWith("_record.xml") || file.getName().endsWith("_type.xml"))) {
                Element record = XMLHelper.getDocumentBuilderFactory().newDocumentBuilder().parse(file).getDocumentElement();
                String extendstg = record.getElementsByTagName("extends").item(0).getFirstChild().getNodeValue();
                String suffix = record.getElementsByTagName("suffix").item(0).getFirstChild().getNodeValue();
                String recordName = record.getAttributes().getNamedItem("name").getNodeValue();
                String packageName = record.getAttributes().getNamedItem("package").getNodeValue().replace('.', '/');
                String destinationPath = destSrcPathDir + "/" + packageName;
                File destinationPathFile = new File(destinationPath);
                if (destinationPathFile.mkdirs()) {
                    System.out.println("Created destination directory: " + destinationPath);
                    String destinationFilepath = destinationPath + "/" + recordName + suffix + ".java";
                    transform(file, new File(destinationFilepath), new File(recordStyleDir + "/" + extendstg.toLowerCase(Locale.ROOT) + ".xsl"));
                    System.out.println("Generated " + suffix + ": " + destinationFilepath);
                    destinationPath = testSrcPathDir + "/" + packageName;
                    destinationPathFile = new File(destinationPath);
                    if (destinationPathFile.mkdirs()) {
                        System.out.println("Created destination directory: " + destinationPath);
                        destinationFilepath = destinationPath + "/Test" + recordName + suffix + ".java";
                        if (new File(destinationFilepath).exists()) {
                            System.out.println("Skipped test generation: " + destinationFilepath);
                        } else {
                            transform(file, new File(destinationFilepath), new File(recordStyleDir + "/" + extendstg.toLowerCase(Locale.ROOT) + "_test.xsl"));
                            System.out.println("Generated test: " + destinationFilepath);
                        }
                    } else {
                        throw new IOException("Could not create directory " + destinationPathFile);
                    }
                }
                throw new IOException("Could not create directory " + destinationPathFile);
            }
        }
    }

    private static void transform(File in, File out, File xslt) throws FileNotFoundException, TransformerException {
        try {
            Transformer t = TransformerFactory.newInstance().newTransformer(new StreamSource(xslt));
            Properties p = new Properties();
            p.setProperty("method", "text");
            t.setOutputProperties(p);
            t.transform(new StreamSource(in), new StreamResult(out));
        } catch (TransformerException ex) {
            System.err.println("Error compiling XSL style sheet " + xslt);
            throw ex;
        }
    }
}

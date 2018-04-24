package org.apache.poi.hssf.dev;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RecordFactory;
import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;

public class RecordLister {
    String file;

    public void run() throws IOException {
        InputStream din;
        NPOIFSFileSystem fs = new NPOIFSFileSystem(new File(this.file), true);
        try {
            din = BiffViewer.getPOIFSInputStream(fs);
            RecordInputStream rinp = new RecordInputStream(din);
            while (rinp.hasNextRecord()) {
                int sid = rinp.getNextSid();
                rinp.nextRecord();
                int size = rinp.available();
                Class<? extends Record> clz = RecordFactory.getRecordClass(sid);
                System.out.print(formatSID(sid) + " - " + formatSize(size) + " bytes");
                if (clz != null) {
                    System.out.print("  \t");
                    System.out.print(clz.getName().replace("org.apache.poi.hssf.record.", ""));
                }
                System.out.println();
                byte[] data = rinp.readRemainder();
                if (data.length > 0) {
                    System.out.print("   ");
                    System.out.println(formatData(data));
                }
            }
            din.close();
            fs.close();
        } catch (Throwable th) {
            fs.close();
        }
    }

    private static String formatSID(int sid) {
        int i;
        String hex = Integer.toHexString(sid);
        String dec = Integer.toString(sid);
        StringBuffer s = new StringBuffer();
        s.append("0x");
        for (i = hex.length(); i < 4; i++) {
            s.append('0');
        }
        s.append(hex);
        s.append(" (");
        for (i = dec.length(); i < 4; i++) {
            s.append('0');
        }
        s.append(dec);
        s.append(")");
        return s.toString();
    }

    private static String formatSize(int size) {
        int i;
        String hex = Integer.toHexString(size);
        String dec = Integer.toString(size);
        StringBuffer s = new StringBuffer();
        for (i = hex.length(); i < 3; i++) {
            s.append('0');
        }
        s.append(hex);
        s.append(" (");
        for (i = dec.length(); i < 3; i++) {
            s.append('0');
        }
        s.append(dec);
        s.append(")");
        return s.toString();
    }

    private static String formatData(byte[] data) {
        if (data == null || data.length == 0) {
            return "";
        }
        StringBuffer s = new StringBuffer();
        if (data.length > 9) {
            s.append(byteToHex(data[0]));
            s.append(' ');
            s.append(byteToHex(data[1]));
            s.append(' ');
            s.append(byteToHex(data[2]));
            s.append(' ');
            s.append(byteToHex(data[3]));
            s.append(' ');
            s.append(" .... ");
            s.append(' ');
            s.append(byteToHex(data[data.length - 4]));
            s.append(' ');
            s.append(byteToHex(data[data.length - 3]));
            s.append(' ');
            s.append(byteToHex(data[data.length - 2]));
            s.append(' ');
            s.append(byteToHex(data[data.length - 1]));
        } else {
            for (byte byteToHex : data) {
                s.append(byteToHex(byteToHex));
                s.append(' ');
            }
        }
        return s.toString();
    }

    private static String byteToHex(byte b) {
        int i = b;
        if (i < (byte) 0) {
            i += 256;
        }
        String s = Integer.toHexString(i);
        if (i < 16) {
            return "0" + s;
        }
        return s;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1 || args[0].equals("--help")) {
            System.out.println("RecordLister");
            System.out.println("Outputs the summary of the records in file order");
            System.out.println("usage: java org.apache.poi.hssf.dev.RecordLister filename");
            return;
        }
        RecordLister viewer = new RecordLister();
        viewer.setFile(args[0]);
        viewer.run();
    }
}

package org.apache.poi.hssf.dev;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.hssf.eventusermodel.HSSFEventFactory;
import org.apache.poi.hssf.eventusermodel.HSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFRequest;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;

public class EFBiffViewer {
    String file;

    class C10521 implements HSSFListener {
        C10521() {
        }

        public void processRecord(Record rec) {
            System.out.println(rec.toString());
        }
    }

    public void run() throws IOException {
        InputStream din;
        NPOIFSFileSystem fs = new NPOIFSFileSystem(new File(this.file), true);
        try {
            din = BiffViewer.getPOIFSInputStream(fs);
            HSSFRequest req = new HSSFRequest();
            req.addListenerForAllRecords(new C10521());
            new HSSFEventFactory().processEvents(req, din);
            din.close();
            fs.close();
        } catch (Throwable th) {
            fs.close();
        }
    }

    public void setFile(String file) {
        this.file = file;
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1 || args[0].equals("--help")) {
            System.out.println("EFBiffViewer");
            System.out.println("Outputs biffview of records based on HSSFEventFactory");
            System.out.println("usage: java org.apache.poi.hssf.dev.EBBiffViewer filename");
            return;
        }
        EFBiffViewer viewer = new EFBiffViewer();
        viewer.setFile(args[0]);
        viewer.run();
    }
}

package org.apache.poi.poifs.crypt;

import java.io.FileInputStream;
import java.io.OutputStream;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.poifs.filesystem.POIFSWriterEvent;
import org.apache.poi.poifs.filesystem.POIFSWriterListener;
import org.apache.poi.util.LittleEndian;

class ChunkedCipherOutputStream$EncryptedPackageWriter implements POIFSWriterListener {
    final /* synthetic */ ChunkedCipherOutputStream this$0;

    private ChunkedCipherOutputStream$EncryptedPackageWriter(ChunkedCipherOutputStream chunkedCipherOutputStream) {
        this.this$0 = chunkedCipherOutputStream;
    }

    public void processPOIFSWriterEvent(POIFSWriterEvent event) {
        try {
            OutputStream os = event.getStream();
            byte[] buf = new byte[this.this$0.chunkSize];
            LittleEndian.putLong(buf, 0, ChunkedCipherOutputStream.access$100(this.this$0));
            os.write(buf, 0, 8);
            FileInputStream fis = new FileInputStream(ChunkedCipherOutputStream.access$200(this.this$0));
            while (true) {
                int readBytes = fis.read(buf);
                if (readBytes == -1) {
                    break;
                }
                os.write(buf, 0, readBytes);
            }
            fis.close();
            os.close();
            if (!ChunkedCipherOutputStream.access$200(this.this$0).delete()) {
                ChunkedCipherOutputStream.access$300().log(7, new Object[]{"Can't delete temporary encryption file: " + ChunkedCipherOutputStream.access$200(this.this$0)});
            }
        } catch (Throwable e) {
            throw new EncryptedDocumentException(e);
        }
    }
}

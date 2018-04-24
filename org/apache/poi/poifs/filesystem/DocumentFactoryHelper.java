package org.apache.poi.poifs.filesystem;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.security.GeneralSecurityException;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.poifs.common.POIFSConstants;
import org.apache.poi.poifs.crypt.Decryptor;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.util.IOUtils;

public class DocumentFactoryHelper {
    public static InputStream getDecryptedStream(final NPOIFSFileSystem fs, String password) throws IOException {
        Decryptor d = Decryptor.getInstance(new EncryptionInfo(fs));
        boolean passwordCorrect = false;
        if (password != null) {
            try {
                if (d.verifyPassword(password)) {
                    passwordCorrect = true;
                }
            } catch (GeneralSecurityException e) {
                throw new IOException(e);
            }
        }
        if (!passwordCorrect && d.verifyPassword(Decryptor.DEFAULT_PASSWORD)) {
            passwordCorrect = true;
        }
        if (passwordCorrect) {
            return new FilterInputStream(d.getDataStream(fs.getRoot())) {
                public void close() throws IOException {
                    fs.close();
                    super.close();
                }
            };
        }
        if (password != null) {
            throw new EncryptedDocumentException("Password incorrect");
        }
        throw new EncryptedDocumentException("The supplied spreadsheet is protected, but no password was supplied");
    }

    public static boolean hasOOXMLHeader(InputStream inp) throws IOException {
        inp.mark(4);
        byte[] header = new byte[4];
        int bytesRead = IOUtils.readFully(inp, header);
        if (inp instanceof PushbackInputStream) {
            ((PushbackInputStream) inp).unread(header, 0, bytesRead);
        } else {
            inp.reset();
        }
        if (bytesRead == 4 && header[0] == POIFSConstants.OOXML_FILE_HEADER[0] && header[1] == POIFSConstants.OOXML_FILE_HEADER[1] && header[2] == POIFSConstants.OOXML_FILE_HEADER[2] && header[3] == POIFSConstants.OOXML_FILE_HEADER[3]) {
            return true;
        }
        return false;
    }
}

package org.apache.poi.poifs.crypt;

import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import javax.crypto.SecretKey;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.poifs.filesystem.OPOIFSFileSystem;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public abstract class Encryptor {
    protected static final String DEFAULT_POIFS_ENTRY = "EncryptedPackage";
    private SecretKey secretKey;

    public abstract void confirmPassword(String str);

    public abstract void confirmPassword(String str, byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, byte[] bArr5);

    public abstract OutputStream getDataStream(DirectoryNode directoryNode) throws IOException, GeneralSecurityException;

    public static Encryptor getInstance(EncryptionInfo info) {
        return info.getEncryptor();
    }

    public OutputStream getDataStream(NPOIFSFileSystem fs) throws IOException, GeneralSecurityException {
        return getDataStream(fs.getRoot());
    }

    public OutputStream getDataStream(OPOIFSFileSystem fs) throws IOException, GeneralSecurityException {
        return getDataStream(fs.getRoot());
    }

    public OutputStream getDataStream(POIFSFileSystem fs) throws IOException, GeneralSecurityException {
        return getDataStream(fs.getRoot());
    }

    public SecretKey getSecretKey() {
        return this.secretKey;
    }

    protected void setSecretKey(SecretKey secretKey) {
        this.secretKey = secretKey;
    }
}

package org.apache.poi.poifs.crypt.standard;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.poifs.crypt.CryptoFunctions;
import org.apache.poi.poifs.crypt.DataSpaceMapUtils;
import org.apache.poi.poifs.crypt.Decryptor;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionVerifier;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.POIFSWriterEvent;
import org.apache.poi.poifs.filesystem.POIFSWriterListener;
import org.apache.poi.util.IOUtils;
import org.apache.poi.util.LittleEndianByteArrayOutputStream;
import org.apache.poi.util.LittleEndianOutputStream;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;
import org.apache.poi.util.TempFile;

public class StandardEncryptor extends Encryptor {
    private static final POILogger logger = POILogFactory.getLogger(StandardEncryptor.class);
    private final StandardEncryptionInfoBuilder builder;

    protected class StandardCipherOutputStream extends FilterOutputStream implements POIFSWriterListener {
        protected long countBytes;
        protected final DirectoryNode dir;
        protected final File fileOut;

        private StandardCipherOutputStream(DirectoryNode dir, File fileOut) throws IOException {
            super(new CipherOutputStream(new FileOutputStream(fileOut), StandardEncryptor.this.getCipher(StandardEncryptor.this.getSecretKey(), "PKCS5Padding")));
            this.fileOut = fileOut;
            this.dir = dir;
        }

        protected StandardCipherOutputStream(StandardEncryptor standardEncryptor, DirectoryNode dir) throws IOException {
            this(dir, TempFile.createTempFile("encrypted_package", "crypt"));
        }

        public void write(byte[] b, int off, int len) throws IOException {
            this.out.write(b, off, len);
            this.countBytes += (long) len;
        }

        public void write(int b) throws IOException {
            this.out.write(b);
            this.countBytes++;
        }

        public void close() throws IOException {
            super.close();
            writeToPOIFS();
        }

        void writeToPOIFS() throws IOException {
            this.dir.createDocument(Decryptor.DEFAULT_POIFS_ENTRY, (int) (this.fileOut.length() + 8), this);
        }

        public void processPOIFSWriterEvent(POIFSWriterEvent event) {
            try {
                LittleEndianOutputStream leos = new LittleEndianOutputStream(event.getStream());
                leos.writeLong(this.countBytes);
                FileInputStream fis = new FileInputStream(this.fileOut);
                IOUtils.copy(fis, leos);
                fis.close();
                if (!this.fileOut.delete()) {
                    StandardEncryptor.logger.log(7, new Object[]{"Can't delete temporary encryption file: " + this.fileOut});
                }
                leos.close();
            } catch (Throwable e) {
                throw new EncryptedDocumentException(e);
            }
        }
    }

    protected StandardEncryptor(StandardEncryptionInfoBuilder builder) {
        this.builder = builder;
    }

    public void confirmPassword(String password) {
        Random r = new SecureRandom();
        byte[] salt = new byte[16];
        byte[] verifier = new byte[16];
        r.nextBytes(salt);
        r.nextBytes(verifier);
        confirmPassword(password, null, null, salt, verifier, null);
    }

    public void confirmPassword(String password, byte[] keySpec, byte[] keySalt, byte[] verifier, byte[] verifierSalt, byte[] integritySalt) {
        StandardEncryptionVerifier ver = this.builder.getVerifier();
        ver.setSalt(verifierSalt);
        SecretKey secretKey = StandardDecryptor.generateSecretKey(password, ver, getKeySizeInBytes());
        setSecretKey(secretKey);
        Cipher cipher = getCipher(secretKey, null);
        try {
            byte[] encryptedVerifier = cipher.doFinal(verifier);
            byte[] encryptedVerifierHash = cipher.doFinal(Arrays.copyOf(CryptoFunctions.getMessageDigest(ver.getHashAlgorithm()).digest(verifier), ver.getCipherAlgorithm().encryptedVerifierHashLength));
            ver.setEncryptedVerifier(encryptedVerifier);
            ver.setEncryptedVerifierHash(encryptedVerifierHash);
        } catch (GeneralSecurityException e) {
            throw new EncryptedDocumentException("Password confirmation failed", e);
        }
    }

    private Cipher getCipher(SecretKey key, String padding) {
        EncryptionVerifier ver = this.builder.getVerifier();
        return CryptoFunctions.getCipher(key, ver.getCipherAlgorithm(), ver.getChainingMode(), null, 1, padding);
    }

    public OutputStream getDataStream(DirectoryNode dir) throws IOException, GeneralSecurityException {
        createEncryptionInfoEntry(dir);
        DataSpaceMapUtils.addDefaultDataSpace(dir);
        return new StandardCipherOutputStream(this, dir);
    }

    protected int getKeySizeInBytes() {
        return this.builder.getHeader().getKeySize() / 8;
    }

    protected void createEncryptionInfoEntry(DirectoryNode dir) throws IOException {
        final EncryptionInfo info = this.builder.getEncryptionInfo();
        final StandardEncryptionHeader header = this.builder.getHeader();
        final StandardEncryptionVerifier verifier = this.builder.getVerifier();
        DataSpaceMapUtils.createEncryptionEntry(dir, "EncryptionInfo", new EncryptionRecord() {
            public void write(LittleEndianByteArrayOutputStream bos) {
                bos.writeShort(info.getVersionMajor());
                bos.writeShort(info.getVersionMinor());
                bos.writeInt(info.getEncryptionFlags());
                header.write(bos);
                verifier.write(bos);
            }
        });
    }
}

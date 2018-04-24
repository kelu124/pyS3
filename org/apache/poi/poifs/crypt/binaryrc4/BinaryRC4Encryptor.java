package org.apache.poi.poifs.crypt.binaryrc4;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.poifs.crypt.ChunkedCipherOutputStream;
import org.apache.poi.poifs.crypt.CryptoFunctions;
import org.apache.poi.poifs.crypt.DataSpaceMapUtils;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.crypt.standard.EncryptionRecord;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.util.LittleEndianByteArrayOutputStream;

public class BinaryRC4Encryptor extends Encryptor {
    private final BinaryRC4EncryptionInfoBuilder builder;

    protected class BinaryRC4CipherOutputStream extends ChunkedCipherOutputStream {
        protected Cipher initCipherForBlock(Cipher cipher, int block, boolean lastChunk) throws GeneralSecurityException {
            return BinaryRC4Decryptor.initCipherForBlock(cipher, block, BinaryRC4Encryptor.this.builder, BinaryRC4Encryptor.this.getSecretKey(), 1);
        }

        protected void calculateChecksum(File file, int i) {
        }

        protected void createEncryptionInfoEntry(DirectoryNode dir, File tmpFile) throws IOException, GeneralSecurityException {
            BinaryRC4Encryptor.this.createEncryptionInfoEntry(dir);
        }

        public BinaryRC4CipherOutputStream(DirectoryNode dir) throws IOException, GeneralSecurityException {
            super(dir, 512);
        }
    }

    protected BinaryRC4Encryptor(BinaryRC4EncryptionInfoBuilder builder) {
        this.builder = builder;
    }

    public void confirmPassword(String password) {
        Random r = new SecureRandom();
        byte[] salt = new byte[16];
        byte[] verifier = new byte[16];
        r.nextBytes(salt);
        r.nextBytes(verifier);
        confirmPassword(password, null, null, verifier, salt, null);
    }

    public void confirmPassword(String password, byte[] keySpec, byte[] keySalt, byte[] verifier, byte[] verifierSalt, byte[] integritySalt) {
        BinaryRC4EncryptionVerifier ver = this.builder.getVerifier();
        ver.setSalt(verifierSalt);
        SecretKey skey = BinaryRC4Decryptor.generateSecretKey(password, ver);
        setSecretKey(skey);
        try {
            Cipher cipher = BinaryRC4Decryptor.initCipherForBlock(null, 0, this.builder, skey, 1);
            byte[] encryptedVerifier = new byte[16];
            cipher.update(verifier, 0, 16, encryptedVerifier);
            ver.setEncryptedVerifier(encryptedVerifier);
            ver.setEncryptedVerifierHash(cipher.doFinal(CryptoFunctions.getMessageDigest(ver.getHashAlgorithm()).digest(verifier)));
        } catch (GeneralSecurityException e) {
            throw new EncryptedDocumentException("Password confirmation failed", e);
        }
    }

    public OutputStream getDataStream(DirectoryNode dir) throws IOException, GeneralSecurityException {
        return new BinaryRC4CipherOutputStream(dir);
    }

    protected int getKeySizeInBytes() {
        return this.builder.getHeader().getKeySize() / 8;
    }

    protected void createEncryptionInfoEntry(DirectoryNode dir) throws IOException {
        DataSpaceMapUtils.addDefaultDataSpace(dir);
        final EncryptionInfo info = this.builder.getEncryptionInfo();
        final BinaryRC4EncryptionHeader header = this.builder.getHeader();
        final BinaryRC4EncryptionVerifier verifier = this.builder.getVerifier();
        DataSpaceMapUtils.createEncryptionEntry(dir, "EncryptionInfo", new EncryptionRecord() {
            public void write(LittleEndianByteArrayOutputStream bos) {
                bos.writeShort(info.getVersionMajor());
                bos.writeShort(info.getVersionMinor());
                header.write(bos);
                verifier.write(bos);
            }
        });
    }
}

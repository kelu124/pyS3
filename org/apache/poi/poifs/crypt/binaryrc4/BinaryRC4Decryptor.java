package org.apache.poi.poifs.crypt.binaryrc4;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.poifs.crypt.ChunkedCipherInputStream;
import org.apache.poi.poifs.crypt.CryptoFunctions;
import org.apache.poi.poifs.crypt.Decryptor;
import org.apache.poi.poifs.crypt.EncryptionInfoBuilder;
import org.apache.poi.poifs.crypt.EncryptionVerifier;
import org.apache.poi.poifs.crypt.HashAlgorithm;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.DocumentInputStream;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.StringUtil;

public class BinaryRC4Decryptor extends Decryptor {
    private long _length = -1;

    private class BinaryRC4CipherInputStream extends ChunkedCipherInputStream {
        protected Cipher initCipherForBlock(Cipher existing, int block) throws GeneralSecurityException {
            return BinaryRC4Decryptor.initCipherForBlock(existing, block, BinaryRC4Decryptor.this.builder, BinaryRC4Decryptor.this.getSecretKey(), 2);
        }

        public BinaryRC4CipherInputStream(DocumentInputStream stream, long size) throws GeneralSecurityException {
            super(stream, size, 512);
        }
    }

    protected BinaryRC4Decryptor(BinaryRC4EncryptionInfoBuilder builder) {
        super(builder);
    }

    public boolean verifyPassword(String password) {
        EncryptionVerifier ver = this.builder.getVerifier();
        SecretKey skey = generateSecretKey(password, ver);
        try {
            Cipher cipher = initCipherForBlock(null, 0, this.builder, skey, 2);
            byte[] encryptedVerifier = ver.getEncryptedVerifier();
            byte[] verifier = new byte[encryptedVerifier.length];
            cipher.update(encryptedVerifier, 0, encryptedVerifier.length, verifier);
            setVerifier(verifier);
            if (!Arrays.equals(CryptoFunctions.getMessageDigest(ver.getHashAlgorithm()).digest(verifier), cipher.doFinal(ver.getEncryptedVerifierHash()))) {
                return false;
            }
            setSecretKey(skey);
            return true;
        } catch (Throwable e) {
            throw new EncryptedDocumentException(e);
        }
    }

    protected static Cipher initCipherForBlock(Cipher cipher, int block, EncryptionInfoBuilder builder, SecretKey skey, int encryptMode) throws GeneralSecurityException {
        HashAlgorithm hashAlgo = builder.getVerifier().getHashAlgorithm();
        byte[] blockKey = new byte[4];
        LittleEndian.putUInt(blockKey, 0, (long) block);
        SecretKey key = new SecretKeySpec(CryptoFunctions.generateKey(skey.getEncoded(), hashAlgo, blockKey, 16), skey.getAlgorithm());
        if (cipher == null) {
            return CryptoFunctions.getCipher(key, builder.getHeader().getCipherAlgorithm(), null, null, encryptMode);
        }
        cipher.init(encryptMode, key);
        return cipher;
    }

    protected static SecretKey generateSecretKey(String password, EncryptionVerifier ver) {
        if (password.length() > 255) {
            password = password.substring(0, 255);
        }
        MessageDigest hashAlg = CryptoFunctions.getMessageDigest(ver.getHashAlgorithm());
        byte[] hash = hashAlg.digest(StringUtil.getToUnicodeLE(password));
        byte[] salt = ver.getSalt();
        hashAlg.reset();
        for (int i = 0; i < 16; i++) {
            hashAlg.update(hash, 0, 5);
            hashAlg.update(salt);
        }
        hash = new byte[5];
        System.arraycopy(hashAlg.digest(), 0, hash, 0, 5);
        return new SecretKeySpec(hash, ver.getCipherAlgorithm().jceId);
    }

    public InputStream getDataStream(DirectoryNode dir) throws IOException, GeneralSecurityException {
        DocumentInputStream dis = dir.createDocumentInputStream(Decryptor.DEFAULT_POIFS_ENTRY);
        this._length = dis.readLong();
        return new BinaryRC4CipherInputStream(dis, this._length);
    }

    public long getLength() {
        if (this._length != -1) {
            return this._length;
        }
        throw new IllegalStateException("Decryptor.getDataStream() was not called");
    }
}

package org.apache.poi.poifs.crypt.standard;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.poifs.crypt.ChainingMode;
import org.apache.poi.poifs.crypt.CryptoFunctions;
import org.apache.poi.poifs.crypt.Decryptor;
import org.apache.poi.poifs.crypt.EncryptionHeader;
import org.apache.poi.poifs.crypt.EncryptionInfoBuilder;
import org.apache.poi.poifs.crypt.EncryptionVerifier;
import org.apache.poi.poifs.crypt.HashAlgorithm;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.DocumentInputStream;
import org.apache.poi.util.BoundedInputStream;
import org.apache.poi.util.LittleEndian;

public class StandardDecryptor extends Decryptor {
    static final /* synthetic */ boolean $assertionsDisabled = (!StandardDecryptor.class.desiredAssertionStatus());
    private long _length = -1;

    protected StandardDecryptor(EncryptionInfoBuilder builder) {
        super(builder);
    }

    public boolean verifyPassword(String password) {
        EncryptionVerifier ver = this.builder.getVerifier();
        SecretKey skey = generateSecretKey(password, ver, getKeySizeInBytes());
        Cipher cipher = getCipher(skey);
        try {
            byte[] verifier = cipher.doFinal(ver.getEncryptedVerifier());
            setVerifier(verifier);
            byte[] calcVerifierHash = CryptoFunctions.getMessageDigest(ver.getHashAlgorithm()).digest(verifier);
            if (!Arrays.equals(calcVerifierHash, Arrays.copyOf(cipher.doFinal(ver.getEncryptedVerifierHash()), calcVerifierHash.length))) {
                return false;
            }
            setSecretKey(skey);
            return true;
        } catch (Throwable e) {
            throw new EncryptedDocumentException(e);
        }
    }

    protected static SecretKey generateSecretKey(String password, EncryptionVerifier ver, int keySize) {
        HashAlgorithm hashAlgo = ver.getHashAlgorithm();
        byte[] pwHash = CryptoFunctions.hashPassword(password, hashAlgo, ver.getSalt(), ver.getSpinCount());
        byte[] blockKey = new byte[4];
        LittleEndian.putInt(blockKey, 0, 0);
        byte[] finalHash = CryptoFunctions.generateKey(pwHash, hashAlgo, blockKey, hashAlgo.hashSize);
        byte[] x1 = fillAndXor(finalHash, (byte) 54);
        byte[] x2 = fillAndXor(finalHash, (byte) 92);
        byte[] x3 = new byte[(x1.length + x2.length)];
        System.arraycopy(x1, 0, x3, 0, x1.length);
        System.arraycopy(x2, 0, x3, x1.length, x2.length);
        return new SecretKeySpec(Arrays.copyOf(x3, keySize), ver.getCipherAlgorithm().jceId);
    }

    protected static byte[] fillAndXor(byte[] hash, byte fillByte) {
        byte[] buff = new byte[64];
        Arrays.fill(buff, fillByte);
        for (int i = 0; i < hash.length; i++) {
            buff[i] = (byte) (buff[i] ^ hash[i]);
        }
        return CryptoFunctions.getMessageDigest(HashAlgorithm.sha1).digest(buff);
    }

    private Cipher getCipher(SecretKey key) {
        EncryptionHeader em = this.builder.getHeader();
        ChainingMode cm = em.getChainingMode();
        if ($assertionsDisabled || cm == ChainingMode.ecb) {
            return CryptoFunctions.getCipher(key, em.getCipherAlgorithm(), cm, null, 2);
        }
        throw new AssertionError();
    }

    public InputStream getDataStream(DirectoryNode dir) throws IOException {
        DocumentInputStream dis = dir.createDocumentInputStream(Decryptor.DEFAULT_POIFS_ENTRY);
        this._length = dis.readLong();
        if (getSecretKey() == null) {
            verifyPassword(null);
        }
        int blockSize = this.builder.getHeader().getCipherAlgorithm().blockSize;
        long cipherLen = ((this._length / ((long) blockSize)) + 1) * ((long) blockSize);
        return new BoundedInputStream(new CipherInputStream(new BoundedInputStream(dis, cipherLen), getCipher(getSecretKey())), this._length);
    }

    public long getLength() {
        if (this._length != -1) {
            return this._length;
        }
        throw new IllegalStateException("Decryptor.getDataStream() was not called");
    }
}

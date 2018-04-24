package org.apache.poi.hssf.record.crypto;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.poifs.crypt.CipherAlgorithm;
import org.apache.poi.poifs.crypt.CryptoFunctions;
import org.apache.poi.poifs.crypt.HashAlgorithm;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

public class Biff8RC4Key extends Biff8EncryptionKey {
    public static final int KEY_DIGEST_LENGTH = 5;
    private static final int PASSWORD_HASH_NUMBER_OF_BYTES_USED = 5;
    private static POILogger log = POILogFactory.getLogger(Biff8RC4Key.class);

    Biff8RC4Key(byte[] keyDigest) {
        if (keyDigest.length != 5) {
            throw new IllegalArgumentException("Expected 5 byte key digest, but got " + HexDump.toHex(keyDigest));
        }
        this._secretKey = new SecretKeySpec((byte[]) keyDigest.clone(), CipherAlgorithm.rc4.jceId);
    }

    public static Biff8RC4Key create(String password, byte[] salt) {
        return new Biff8RC4Key(createKeyDigest(password, salt));
    }

    public boolean validate(byte[] verifier, byte[] verifierHash) {
        check16Bytes(verifier, "verifier");
        check16Bytes(verifierHash, "verifierHash");
        Cipher rc4 = getCipher();
        initCipherForBlock(rc4, 0);
        byte[] verifierPrime = (byte[]) verifier.clone();
        byte[] verifierHashPrime = (byte[]) verifierHash.clone();
        try {
            rc4.update(verifierPrime, 0, verifierPrime.length, verifierPrime);
            rc4.update(verifierHashPrime, 0, verifierHashPrime.length, verifierHashPrime);
            MessageDigest md5 = CryptoFunctions.getMessageDigest(HashAlgorithm.md5);
            md5.update(verifierPrime);
            byte[] finalVerifierResult = md5.digest();
            if (log.check(1)) {
                byte[] verifierHashThatWouldWork = xor(verifierHash, xor(verifierHashPrime, finalVerifierResult));
                log.log(1, new Object[]{"valid verifierHash value", HexDump.toHex(verifierHashThatWouldWork)});
            }
            return Arrays.equals(verifierHashPrime, finalVerifierResult);
        } catch (ShortBufferException e) {
            throw new EncryptedDocumentException("buffer too short", e);
        }
    }

    Cipher getCipher() {
        return CryptoFunctions.getCipher(this._secretKey, CipherAlgorithm.rc4, null, null, 1);
    }

    static byte[] createKeyDigest(String password, byte[] docIdData) {
        int i;
        check16Bytes(docIdData, "docId");
        int nChars = Math.min(password.length(), 16);
        byte[] passwordData = new byte[(nChars * 2)];
        for (i = 0; i < nChars; i++) {
            char ch = password.charAt(i);
            passwordData[(i * 2) + 0] = (byte) ((ch << 0) & 255);
            passwordData[(i * 2) + 1] = (byte) ((ch << 8) & 255);
        }
        MessageDigest md5 = CryptoFunctions.getMessageDigest(HashAlgorithm.md5);
        md5.update(passwordData);
        byte[] passwordHash = md5.digest();
        md5.reset();
        for (i = 0; i < 16; i++) {
            md5.update(passwordHash, 0, 5);
            md5.update(docIdData, 0, docIdData.length);
        }
        return CryptoFunctions.getBlock0(md5.digest(), 5);
    }

    void initCipherForBlock(Cipher rc4, int keyBlockNo) {
        byte[] buf = new byte[4];
        LittleEndian.putInt(buf, 0, keyBlockNo);
        MessageDigest md5 = CryptoFunctions.getMessageDigest(HashAlgorithm.md5);
        md5.update(this._secretKey.getEncoded());
        md5.update(buf);
        try {
            rc4.init(1, new SecretKeySpec(md5.digest(), this._secretKey.getAlgorithm()));
        } catch (GeneralSecurityException e) {
            throw new EncryptedDocumentException("Can't rekey for next block", e);
        }
    }

    private static byte[] xor(byte[] a, byte[] b) {
        byte[] c = new byte[a.length];
        for (int i = 0; i < c.length; i++) {
            c[i] = (byte) (a[i] ^ b[i]);
        }
        return c;
    }

    private static void check16Bytes(byte[] data, String argName) {
        if (data.length != 16) {
            throw new IllegalArgumentException("Expected 16 byte " + argName + ", but got " + HexDump.toHex(data));
        }
    }
}

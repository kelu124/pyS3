package org.apache.poi.poifs.crypt.cryptoapi;

import org.apache.poi.poifs.crypt.ChainingMode;
import org.apache.poi.poifs.crypt.CipherAlgorithm;
import org.apache.poi.poifs.crypt.HashAlgorithm;
import org.apache.poi.poifs.crypt.standard.StandardEncryptionVerifier;
import org.apache.poi.util.LittleEndianInput;

public class CryptoAPIEncryptionVerifier extends StandardEncryptionVerifier {
    protected CryptoAPIEncryptionVerifier(LittleEndianInput is, CryptoAPIEncryptionHeader header) {
        super(is, header);
    }

    protected CryptoAPIEncryptionVerifier(CipherAlgorithm cipherAlgorithm, HashAlgorithm hashAlgorithm, int keyBits, int blockSize, ChainingMode chainingMode) {
        super(cipherAlgorithm, hashAlgorithm, keyBits, blockSize, chainingMode);
    }

    protected void setSalt(byte[] salt) {
        super.setSalt(salt);
    }

    protected void setEncryptedVerifier(byte[] encryptedVerifier) {
        super.setEncryptedVerifier(encryptedVerifier);
    }

    protected void setEncryptedVerifierHash(byte[] encryptedVerifierHash) {
        super.setEncryptedVerifierHash(encryptedVerifierHash);
    }
}

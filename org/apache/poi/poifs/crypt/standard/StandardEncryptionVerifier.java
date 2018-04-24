package org.apache.poi.poifs.crypt.standard;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.poifs.crypt.ChainingMode;
import org.apache.poi.poifs.crypt.CipherAlgorithm;
import org.apache.poi.poifs.crypt.EncryptionVerifier;
import org.apache.poi.poifs.crypt.HashAlgorithm;
import org.apache.poi.util.LittleEndianByteArrayOutputStream;
import org.apache.poi.util.LittleEndianInput;

public class StandardEncryptionVerifier extends EncryptionVerifier implements EncryptionRecord {
    static final /* synthetic */ boolean $assertionsDisabled = (!StandardEncryptionVerifier.class.desiredAssertionStatus());
    private static final int SPIN_COUNT = 50000;
    private final int verifierHashSize;

    protected StandardEncryptionVerifier(LittleEndianInput is, StandardEncryptionHeader header) {
        if (is.readInt() != 16) {
            throw new RuntimeException("Salt size != 16 !?");
        }
        byte[] salt = new byte[16];
        is.readFully(salt);
        setSalt(salt);
        byte[] encryptedVerifier = new byte[16];
        is.readFully(encryptedVerifier);
        setEncryptedVerifier(encryptedVerifier);
        this.verifierHashSize = is.readInt();
        byte[] encryptedVerifierHash = new byte[header.getCipherAlgorithm().encryptedVerifierHashLength];
        is.readFully(encryptedVerifierHash);
        setEncryptedVerifierHash(encryptedVerifierHash);
        setSpinCount(SPIN_COUNT);
        setCipherAlgorithm(header.getCipherAlgorithm());
        setChainingMode(header.getChainingMode());
        setEncryptedKey(null);
        setHashAlgorithm(header.getHashAlgorithmEx());
    }

    protected StandardEncryptionVerifier(CipherAlgorithm cipherAlgorithm, HashAlgorithm hashAlgorithm, int keyBits, int blockSize, ChainingMode chainingMode) {
        setCipherAlgorithm(cipherAlgorithm);
        setHashAlgorithm(hashAlgorithm);
        setChainingMode(chainingMode);
        setSpinCount(SPIN_COUNT);
        this.verifierHashSize = hashAlgorithm.hashSize;
    }

    protected void setSalt(byte[] salt) {
        if (salt == null || salt.length != 16) {
            throw new EncryptedDocumentException("invalid verifier salt");
        }
        super.setSalt(salt);
    }

    protected void setEncryptedVerifier(byte[] encryptedVerifier) {
        super.setEncryptedVerifier(encryptedVerifier);
    }

    protected void setEncryptedVerifierHash(byte[] encryptedVerifierHash) {
        super.setEncryptedVerifierHash(encryptedVerifierHash);
    }

    public void write(LittleEndianByteArrayOutputStream bos) {
        byte[] salt = getSalt();
        if ($assertionsDisabled || salt.length == 16) {
            bos.writeInt(salt.length);
            bos.write(salt);
            byte[] encryptedVerifier = getEncryptedVerifier();
            if ($assertionsDisabled || encryptedVerifier.length == 16) {
                bos.write(encryptedVerifier);
                bos.writeInt(20);
                byte[] encryptedVerifierHash = getEncryptedVerifierHash();
                if ($assertionsDisabled || encryptedVerifierHash.length == getCipherAlgorithm().encryptedVerifierHashLength) {
                    bos.write(encryptedVerifierHash);
                    return;
                }
                throw new AssertionError();
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    protected int getVerifierHashSize() {
        return this.verifierHashSize;
    }
}

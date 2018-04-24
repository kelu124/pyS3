package org.apache.poi.poifs.crypt.binaryrc4;

import java.io.IOException;
import org.apache.poi.poifs.crypt.ChainingMode;
import org.apache.poi.poifs.crypt.CipherAlgorithm;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionInfoBuilder;
import org.apache.poi.poifs.crypt.HashAlgorithm;
import org.apache.poi.util.LittleEndianInput;

public class BinaryRC4EncryptionInfoBuilder implements EncryptionInfoBuilder {
    static final /* synthetic */ boolean $assertionsDisabled = (!BinaryRC4EncryptionInfoBuilder.class.desiredAssertionStatus());
    BinaryRC4Decryptor decryptor;
    BinaryRC4Encryptor encryptor;
    BinaryRC4EncryptionHeader header;
    EncryptionInfo info;
    BinaryRC4EncryptionVerifier verifier;

    public void initialize(EncryptionInfo info, LittleEndianInput dis) throws IOException {
        this.info = info;
        int vMajor = info.getVersionMajor();
        int vMinor = info.getVersionMinor();
        if ($assertionsDisabled || (vMajor == 1 && vMinor == 1)) {
            this.header = new BinaryRC4EncryptionHeader();
            this.verifier = new BinaryRC4EncryptionVerifier(dis);
            this.decryptor = new BinaryRC4Decryptor(this);
            this.encryptor = new BinaryRC4Encryptor(this);
            return;
        }
        throw new AssertionError();
    }

    public void initialize(EncryptionInfo info, CipherAlgorithm cipherAlgorithm, HashAlgorithm hashAlgorithm, int keyBits, int blockSize, ChainingMode chainingMode) {
        this.info = info;
        this.header = new BinaryRC4EncryptionHeader();
        this.verifier = new BinaryRC4EncryptionVerifier();
        this.decryptor = new BinaryRC4Decryptor(this);
        this.encryptor = new BinaryRC4Encryptor(this);
    }

    public BinaryRC4EncryptionHeader getHeader() {
        return this.header;
    }

    public BinaryRC4EncryptionVerifier getVerifier() {
        return this.verifier;
    }

    public BinaryRC4Decryptor getDecryptor() {
        return this.decryptor;
    }

    public BinaryRC4Encryptor getEncryptor() {
        return this.encryptor;
    }

    public EncryptionInfo getEncryptionInfo() {
        return this.info;
    }
}

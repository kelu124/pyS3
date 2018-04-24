package org.apache.poi.poifs.crypt;

import java.io.IOException;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.poifs.filesystem.OPOIFSFileSystem;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.util.BitField;
import org.apache.poi.util.BitFieldFactory;
import org.apache.poi.util.LittleEndianInput;

public class EncryptionInfo {
    public static final BitField flagAES = BitFieldFactory.getInstance(32);
    public static final BitField flagCryptoAPI = BitFieldFactory.getInstance(4);
    public static final BitField flagDocProps = BitFieldFactory.getInstance(8);
    public static final BitField flagExternal = BitFieldFactory.getInstance(16);
    private final Decryptor decryptor;
    private final int encryptionFlags;
    private final Encryptor encryptor;
    private final EncryptionHeader header;
    private final EncryptionVerifier verifier;
    private final int versionMajor;
    private final int versionMinor;

    public EncryptionInfo(POIFSFileSystem fs) throws IOException {
        this(fs.getRoot());
    }

    public EncryptionInfo(OPOIFSFileSystem fs) throws IOException {
        this(fs.getRoot());
    }

    public EncryptionInfo(NPOIFSFileSystem fs) throws IOException {
        this(fs.getRoot());
    }

    public EncryptionInfo(DirectoryNode dir) throws IOException {
        this(dir.createDocumentInputStream("EncryptionInfo"), false);
    }

    public EncryptionInfo(LittleEndianInput dis, boolean isCryptoAPI) throws IOException {
        EncryptionMode encryptionMode;
        this.versionMajor = dis.readShort();
        this.versionMinor = dis.readShort();
        if (!isCryptoAPI && this.versionMajor == EncryptionMode.binaryRC4.versionMajor && this.versionMinor == EncryptionMode.binaryRC4.versionMinor) {
            encryptionMode = EncryptionMode.binaryRC4;
            this.encryptionFlags = -1;
        } else if (!isCryptoAPI && this.versionMajor == EncryptionMode.agile.versionMajor && this.versionMinor == EncryptionMode.agile.versionMinor) {
            encryptionMode = EncryptionMode.agile;
            this.encryptionFlags = dis.readInt();
        } else if (!isCryptoAPI && 2 <= this.versionMajor && this.versionMajor <= 4 && this.versionMinor == EncryptionMode.standard.versionMinor) {
            encryptionMode = EncryptionMode.standard;
            this.encryptionFlags = dis.readInt();
        } else if (!isCryptoAPI || 2 > this.versionMajor || this.versionMajor > 4 || this.versionMinor != EncryptionMode.cryptoAPI.versionMinor) {
            this.encryptionFlags = dis.readInt();
            throw new EncryptedDocumentException("Unknown encryption: version major: " + this.versionMajor + " / version minor: " + this.versionMinor + " / fCrypto: " + flagCryptoAPI.isSet(this.encryptionFlags) + " / fExternal: " + flagExternal.isSet(this.encryptionFlags) + " / fDocProps: " + flagDocProps.isSet(this.encryptionFlags) + " / fAES: " + flagAES.isSet(this.encryptionFlags));
        } else {
            encryptionMode = EncryptionMode.cryptoAPI;
            this.encryptionFlags = dis.readInt();
        }
        try {
            EncryptionInfoBuilder eib = getBuilder(encryptionMode);
            eib.initialize(this, dis);
            this.header = eib.getHeader();
            this.verifier = eib.getVerifier();
            this.decryptor = eib.getDecryptor();
            this.encryptor = eib.getEncryptor();
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public EncryptionInfo(EncryptionMode encryptionMode) {
        this(encryptionMode, null, null, -1, -1, null);
    }

    public EncryptionInfo(EncryptionMode encryptionMode, CipherAlgorithm cipherAlgorithm, HashAlgorithm hashAlgorithm, int keyBits, int blockSize, ChainingMode chainingMode) {
        this.versionMajor = encryptionMode.versionMajor;
        this.versionMinor = encryptionMode.versionMinor;
        this.encryptionFlags = encryptionMode.encryptionFlags;
        try {
            EncryptionInfoBuilder eib = getBuilder(encryptionMode);
            eib.initialize(this, cipherAlgorithm, hashAlgorithm, keyBits, blockSize, chainingMode);
            this.header = eib.getHeader();
            this.verifier = eib.getVerifier();
            this.decryptor = eib.getDecryptor();
            this.encryptor = eib.getEncryptor();
        } catch (Throwable e) {
            throw new EncryptedDocumentException(e);
        }
    }

    protected static EncryptionInfoBuilder getBuilder(EncryptionMode encryptionMode) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        return (EncryptionInfoBuilder) Thread.currentThread().getContextClassLoader().loadClass(encryptionMode.builder).newInstance();
    }

    public int getVersionMajor() {
        return this.versionMajor;
    }

    public int getVersionMinor() {
        return this.versionMinor;
    }

    public int getEncryptionFlags() {
        return this.encryptionFlags;
    }

    public EncryptionHeader getHeader() {
        return this.header;
    }

    public EncryptionVerifier getVerifier() {
        return this.verifier;
    }

    public Decryptor getDecryptor() {
        return this.decryptor;
    }

    public Encryptor getEncryptor() {
        return this.encryptor;
    }
}

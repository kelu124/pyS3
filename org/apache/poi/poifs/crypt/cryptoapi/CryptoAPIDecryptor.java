package org.apache.poi.poifs.crypt.cryptoapi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.poifs.crypt.CryptoFunctions;
import org.apache.poi.poifs.crypt.Decryptor;
import org.apache.poi.poifs.crypt.EncryptionHeader;
import org.apache.poi.poifs.crypt.EncryptionInfoBuilder;
import org.apache.poi.poifs.crypt.EncryptionVerifier;
import org.apache.poi.poifs.crypt.HashAlgorithm;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.DocumentInputStream;
import org.apache.poi.poifs.filesystem.DocumentNode;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.util.BitField;
import org.apache.poi.util.BitFieldFactory;
import org.apache.poi.util.BoundedInputStream;
import org.apache.poi.util.IOUtils;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.LittleEndianInputStream;
import org.apache.poi.util.StringUtil;

public class CryptoAPIDecryptor extends Decryptor {
    static final /* synthetic */ boolean $assertionsDisabled = (!CryptoAPIDecryptor.class.desiredAssertionStatus());
    private long _length = -1;

    private class SeekableByteArrayInputStream extends ByteArrayInputStream {
        Cipher cipher;
        byte[] oneByte = new byte[]{(byte) 0};

        public void seek(int newpos) {
            if (newpos > this.count) {
                throw new ArrayIndexOutOfBoundsException(newpos);
            }
            this.pos = newpos;
            this.mark = newpos;
        }

        public void setBlock(int block) throws GeneralSecurityException {
            this.cipher = CryptoAPIDecryptor.this.initCipherForBlock(this.cipher, block);
        }

        public synchronized int read() {
            int i = -1;
            synchronized (this) {
                int ch = super.read();
                if (ch != -1) {
                    this.oneByte[0] = (byte) ch;
                    try {
                        this.cipher.update(this.oneByte, 0, 1, this.oneByte);
                        i = this.oneByte[0];
                    } catch (Throwable e) {
                        throw new EncryptedDocumentException(e);
                    }
                }
            }
            return i;
        }

        public synchronized int read(byte[] b, int off, int len) {
            int readLen;
            readLen = super.read(b, off, len);
            if (readLen == -1) {
                readLen = -1;
            } else {
                try {
                    this.cipher.update(b, off, readLen, b, off);
                } catch (Throwable e) {
                    throw new EncryptedDocumentException(e);
                }
            }
            return readLen;
        }

        public SeekableByteArrayInputStream(byte[] buf) throws GeneralSecurityException {
            super(buf);
            this.cipher = CryptoAPIDecryptor.this.initCipherForBlock(null, 0);
        }
    }

    static class StreamDescriptorEntry {
        static BitField flagStream = BitFieldFactory.getInstance(1);
        int block;
        int flags;
        int reserved2;
        String streamName;
        int streamOffset;
        int streamSize;

        StreamDescriptorEntry() {
        }
    }

    protected CryptoAPIDecryptor(CryptoAPIEncryptionInfoBuilder builder) {
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

    public Cipher initCipherForBlock(Cipher cipher, int block) throws GeneralSecurityException {
        return initCipherForBlock(cipher, block, this.builder, getSecretKey(), 2);
    }

    protected static Cipher initCipherForBlock(Cipher cipher, int block, EncryptionInfoBuilder builder, SecretKey skey, int encryptMode) throws GeneralSecurityException {
        HashAlgorithm hashAlgo = builder.getVerifier().getHashAlgorithm();
        byte[] blockKey = new byte[4];
        LittleEndian.putUInt(blockKey, 0, (long) block);
        MessageDigest hashAlg = CryptoFunctions.getMessageDigest(hashAlgo);
        hashAlg.update(skey.getEncoded());
        byte[] encKey = hashAlg.digest(blockKey);
        EncryptionHeader header = builder.getHeader();
        int keyBits = header.getKeySize();
        encKey = CryptoFunctions.getBlock0(encKey, keyBits / 8);
        if (keyBits == 40) {
            encKey = CryptoFunctions.getBlock0(encKey, 16);
        }
        SecretKey key = new SecretKeySpec(encKey, skey.getAlgorithm());
        if (cipher == null) {
            return CryptoFunctions.getCipher(key, header.getCipherAlgorithm(), null, null, encryptMode);
        }
        cipher.init(encryptMode, key);
        return cipher;
    }

    protected static SecretKey generateSecretKey(String password, EncryptionVerifier ver) {
        if (password.length() > 255) {
            password = password.substring(0, 255);
        }
        MessageDigest hashAlg = CryptoFunctions.getMessageDigest(ver.getHashAlgorithm());
        hashAlg.update(ver.getSalt());
        return new SecretKeySpec(hashAlg.digest(StringUtil.getToUnicodeLE(password)), ver.getCipherAlgorithm().jceId);
    }

    public InputStream getDataStream(DirectoryNode dir) throws IOException, GeneralSecurityException {
        NPOIFSFileSystem fsOut = new NPOIFSFileSystem();
        DocumentInputStream dis = dir.createDocumentInputStream((DocumentNode) dir.getEntry("EncryptedSummary"));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        IOUtils.copy(dis, bos);
        dis.close();
        InputStream seekableByteArrayInputStream = new SeekableByteArrayInputStream(bos.toByteArray());
        LittleEndianInputStream littleEndianInputStream = new LittleEndianInputStream(seekableByteArrayInputStream);
        int streamDescriptorArrayOffset = (int) littleEndianInputStream.readUInt();
        littleEndianInputStream.readUInt();
        seekableByteArrayInputStream.skip((long) (streamDescriptorArrayOffset - 8));
        seekableByteArrayInputStream.setBlock(0);
        int encryptedStreamDescriptorCount = (int) littleEndianInputStream.readUInt();
        StreamDescriptorEntry[] entries = new StreamDescriptorEntry[encryptedStreamDescriptorCount];
        int i = 0;
        while (i < encryptedStreamDescriptorCount) {
            StreamDescriptorEntry entry = new StreamDescriptorEntry();
            entries[i] = entry;
            entry.streamOffset = (int) littleEndianInputStream.readUInt();
            entry.streamSize = (int) littleEndianInputStream.readUInt();
            entry.block = littleEndianInputStream.readUShort();
            int nameSize = littleEndianInputStream.readUByte();
            entry.flags = littleEndianInputStream.readUByte();
            entry.reserved2 = littleEndianInputStream.readInt();
            entry.streamName = StringUtil.readUnicodeLE(littleEndianInputStream, nameSize);
            littleEndianInputStream.readShort();
            if ($assertionsDisabled || entry.streamName.length() == nameSize) {
                i++;
            } else {
                throw new AssertionError();
            }
        }
        for (StreamDescriptorEntry entry2 : entries) {
            seekableByteArrayInputStream.seek(entry2.streamOffset);
            seekableByteArrayInputStream.setBlock(entry2.block);
            InputStream is = new BoundedInputStream(seekableByteArrayInputStream, (long) entry2.streamSize);
            fsOut.createDocument(is, entry2.streamName);
            is.close();
        }
        littleEndianInputStream.close();
        seekableByteArrayInputStream.close();
        bos.reset();
        fsOut.writeFilesystem(bos);
        fsOut.close();
        this._length = (long) bos.size();
        return new ByteArrayInputStream(bos.toByteArray());
    }

    public long getLength() {
        if (this._length != -1) {
            return this._length;
        }
        throw new IllegalStateException("Decryptor.getDataStream() was not called");
    }
}

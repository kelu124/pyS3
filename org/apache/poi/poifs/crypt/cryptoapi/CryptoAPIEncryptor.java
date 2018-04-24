package org.apache.poi.poifs.crypt.cryptoapi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.crypto.Cipher;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hpsf.PropertySetFactory;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hpsf.WritingNotSupportedException;
import org.apache.poi.poifs.crypt.CryptoFunctions;
import org.apache.poi.poifs.crypt.DataSpaceMapUtils;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.crypt.standard.EncryptionRecord;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.DocumentInputStream;
import org.apache.poi.util.IOUtils;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.LittleEndianByteArrayOutputStream;
import org.apache.poi.util.StringUtil;

public class CryptoAPIEncryptor extends Encryptor {
    static final /* synthetic */ boolean $assertionsDisabled = (!CryptoAPIEncryptor.class.desiredAssertionStatus());
    private final CryptoAPIEncryptionInfoBuilder builder;

    private class CipherByteArrayOutputStream extends ByteArrayOutputStream {
        Cipher cipher;
        byte[] oneByte = new byte[]{(byte) 0};

        public CipherByteArrayOutputStream() throws GeneralSecurityException {
            setBlock(0);
        }

        public byte[] getBuf() {
            return this.buf;
        }

        public void setSize(int count) {
            this.count = count;
        }

        public void setBlock(int block) throws GeneralSecurityException {
            this.cipher = CryptoAPIEncryptor.this.initCipherForBlock(this.cipher, block);
        }

        public void write(int b) {
            try {
                this.oneByte[0] = (byte) b;
                this.cipher.update(this.oneByte, 0, 1, this.oneByte, 0);
                super.write(this.oneByte);
            } catch (Throwable e) {
                throw new EncryptedDocumentException(e);
            }
        }

        public void write(byte[] b, int off, int len) {
            try {
                this.cipher.update(b, off, len, b, off);
                super.write(b, off, len);
            } catch (Throwable e) {
                throw new EncryptedDocumentException(e);
            }
        }
    }

    protected CryptoAPIEncryptor(CryptoAPIEncryptionInfoBuilder builder) {
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
        if ($assertionsDisabled || !(verifier == null || verifierSalt == null)) {
            CryptoAPIEncryptionVerifier ver = this.builder.getVerifier();
            ver.setSalt(verifierSalt);
            setSecretKey(CryptoAPIDecryptor.generateSecretKey(password, ver));
            try {
                Cipher cipher = initCipherForBlock(null, 0);
                byte[] encryptedVerifier = new byte[verifier.length];
                cipher.update(verifier, 0, verifier.length, encryptedVerifier);
                ver.setEncryptedVerifier(encryptedVerifier);
                ver.setEncryptedVerifierHash(cipher.doFinal(CryptoFunctions.getMessageDigest(ver.getHashAlgorithm()).digest(verifier)));
                return;
            } catch (GeneralSecurityException e) {
                throw new EncryptedDocumentException("Password confirmation failed", e);
            }
        }
        throw new AssertionError();
    }

    public Cipher initCipherForBlock(Cipher cipher, int block) throws GeneralSecurityException {
        return CryptoAPIDecryptor.initCipherForBlock(cipher, block, this.builder, getSecretKey(), 1);
    }

    public OutputStream getDataStream(DirectoryNode dir) throws IOException, GeneralSecurityException {
        CipherByteArrayOutputStream bos = new CipherByteArrayOutputStream();
        byte[] buf = new byte[8];
        bos.write(buf, 0, 8);
        String[] entryNames = new String[]{SummaryInformation.DEFAULT_STREAM_NAME, DocumentSummaryInformation.DEFAULT_STREAM_NAME};
        List<StreamDescriptorEntry> descList = new ArrayList();
        int block = 0;
        for (String entryName : entryNames) {
            if (dir.hasEntry(entryName)) {
                StreamDescriptorEntry descEntry = new StreamDescriptorEntry();
                descEntry.block = block;
                descEntry.streamOffset = bos.size();
                descEntry.streamName = entryName;
                descEntry.flags = StreamDescriptorEntry.flagStream.setValue(0, 1);
                descEntry.reserved2 = 0;
                bos.setBlock(block);
                DocumentInputStream dis = dir.createDocumentInputStream(entryName);
                IOUtils.copy(dis, bos);
                dis.close();
                descEntry.streamSize = bos.size() - descEntry.streamOffset;
                descList.add(descEntry);
                dir.getEntry(entryName).delete();
                block++;
            }
        }
        int streamDescriptorArrayOffset = bos.size();
        bos.setBlock(0);
        LittleEndian.putUInt(buf, 0, (long) descList.size());
        bos.write(buf, 0, 4);
        for (StreamDescriptorEntry sde : descList) {
            LittleEndian.putUInt(buf, 0, (long) sde.streamOffset);
            bos.write(buf, 0, 4);
            LittleEndian.putUInt(buf, 0, (long) sde.streamSize);
            bos.write(buf, 0, 4);
            LittleEndian.putUShort(buf, 0, sde.block);
            bos.write(buf, 0, 2);
            LittleEndian.putUByte(buf, 0, (short) sde.streamName.length());
            bos.write(buf, 0, 1);
            LittleEndian.putUByte(buf, 0, (short) sde.flags);
            bos.write(buf, 0, 1);
            LittleEndian.putUInt(buf, 0, (long) sde.reserved2);
            bos.write(buf, 0, 4);
            byte[] nameBytes = StringUtil.getToUnicodeLE(sde.streamName);
            bos.write(nameBytes, 0, nameBytes.length);
            LittleEndian.putShort(buf, 0, (short) 0);
            bos.write(buf, 0, 2);
        }
        int savedSize = bos.size();
        int streamDescriptorArraySize = savedSize - streamDescriptorArrayOffset;
        LittleEndian.putUInt(buf, 0, (long) streamDescriptorArrayOffset);
        LittleEndian.putUInt(buf, 4, (long) streamDescriptorArraySize);
        bos.reset();
        bos.setBlock(0);
        bos.write(buf, 0, 8);
        bos.setSize(savedSize);
        dir.createDocument("EncryptedSummary", new ByteArrayInputStream(bos.getBuf(), 0, savedSize));
        try {
            PropertySetFactory.newDocumentSummaryInformation().write(dir, DocumentSummaryInformation.DEFAULT_STREAM_NAME);
            return bos;
        } catch (WritingNotSupportedException e) {
            throw new IOException(e);
        }
    }

    protected int getKeySizeInBytes() {
        return this.builder.getHeader().getKeySize() / 8;
    }

    protected void createEncryptionInfoEntry(DirectoryNode dir) throws IOException {
        DataSpaceMapUtils.addDefaultDataSpace(dir);
        final EncryptionInfo info = this.builder.getEncryptionInfo();
        final CryptoAPIEncryptionHeader header = this.builder.getHeader();
        final CryptoAPIEncryptionVerifier verifier = this.builder.getVerifier();
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

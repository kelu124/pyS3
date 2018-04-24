package org.apache.poi.poifs.crypt.standard;

import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.poifs.crypt.ChainingMode;
import org.apache.poi.poifs.crypt.CipherAlgorithm;
import org.apache.poi.poifs.crypt.CipherProvider;
import org.apache.poi.poifs.crypt.EncryptionHeader;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.HashAlgorithm;
import org.apache.poi.util.BitField;
import org.apache.poi.util.LittleEndianByteArrayOutputStream;
import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;
import org.apache.poi.util.StringUtil;

public class StandardEncryptionHeader extends EncryptionHeader implements EncryptionRecord {
    protected StandardEncryptionHeader(LittleEndianInput is) throws IOException {
        setFlags(is.readInt());
        setSizeExtra(is.readInt());
        setCipherAlgorithm(CipherAlgorithm.fromEcmaId(is.readInt()));
        setHashAlgorithm(HashAlgorithm.fromEcmaId(is.readInt()));
        int keySize = is.readInt();
        if (keySize == 0) {
            keySize = 40;
        }
        setKeySize(keySize);
        setBlockSize(getKeySize());
        setCipherProvider(CipherProvider.fromEcmaId(is.readInt()));
        is.readLong();
        ((InputStream) is).mark(5);
        int checkForSalt = is.readInt();
        ((InputStream) is).reset();
        if (checkForSalt == 16) {
            setCspName("");
        } else {
            StringBuilder builder = new StringBuilder();
            while (true) {
                char c = (char) is.readShort();
                if (c == '\u0000') {
                    break;
                }
                builder.append(c);
            }
            setCspName(builder.toString());
        }
        setChainingMode(ChainingMode.ecb);
        setKeySalt(null);
    }

    protected StandardEncryptionHeader(CipherAlgorithm cipherAlgorithm, HashAlgorithm hashAlgorithm, int keyBits, int blockSize, ChainingMode chainingMode) {
        boolean z = true;
        setCipherAlgorithm(cipherAlgorithm);
        setHashAlgorithm(hashAlgorithm);
        setKeySize(keyBits);
        setBlockSize(blockSize);
        setCipherProvider(cipherAlgorithm.provider);
        int i = EncryptionInfo.flagCryptoAPI.setBoolean(0, true);
        BitField bitField = EncryptionInfo.flagAES;
        if (cipherAlgorithm.provider != CipherProvider.aes) {
            z = false;
        }
        setFlags(bitField.setBoolean(0, z) | i);
    }

    public void write(LittleEndianByteArrayOutputStream bos) {
        int startIdx = bos.getWriteIndex();
        LittleEndianOutput sizeOutput = bos.createDelayedOutput(4);
        bos.writeInt(getFlags());
        bos.writeInt(0);
        bos.writeInt(getCipherAlgorithm().ecmaId);
        bos.writeInt(getHashAlgorithmEx().ecmaId);
        bos.writeInt(getKeySize());
        bos.writeInt(getCipherProvider().ecmaId);
        bos.writeInt(0);
        bos.writeInt(0);
        String cspName = getCspName();
        if (cspName == null) {
            cspName = getCipherProvider().cipherProviderName;
        }
        bos.write(StringUtil.getToUnicodeLE(cspName));
        bos.writeShort(0);
        sizeOutput.writeInt((bos.getWriteIndex() - startIdx) - 4);
    }
}

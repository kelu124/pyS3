package org.apache.poi.poifs.crypt;

import com.itextpdf.text.pdf.security.DigestAlgorithms;
import com.itextpdf.text.pdf.security.SecurityConstants;
import org.apache.poi.EncryptedDocumentException;
import org.bytedeco.javacpp.avcodec;

public enum HashAlgorithm {
    none("", 0, "", 0, "", false),
    sha1(DigestAlgorithms.SHA1, avcodec.AV_CODEC_ID_AYUV, SecurityConstants.SHA1, 20, "HmacSHA1", false),
    sha256("SHA-256", avcodec.AV_CODEC_ID_SNOW, "SHA256", 32, "HmacSHA256", false),
    sha384(DigestAlgorithms.SHA384, avcodec.AV_CODEC_ID_SMVJPEG, "SHA384", 48, "HmacSHA384", false),
    sha512(DigestAlgorithms.SHA512, avcodec.AV_CODEC_ID_APNG, "SHA512", 64, "HmacSHA512", false),
    md5("MD5", -1, "MD5", 16, "HmacMD5", false),
    md2("MD2", -1, "MD2", 16, "Hmac-MD2", true),
    md4("MD4", -1, "MD4", 16, "Hmac-MD4", true),
    ripemd128("RipeMD128", -1, "RIPEMD-128", 16, "HMac-RipeMD128", true),
    ripemd160("RipeMD160", -1, "RIPEMD-160", 20, "HMac-RipeMD160", true),
    whirlpool("Whirlpool", -1, "WHIRLPOOL", 64, "HMac-Whirlpool", true),
    sha224("SHA-224", -1, "SHA224", 28, "HmacSHA224", true);
    
    public final int ecmaId;
    public final String ecmaString;
    public final int hashSize;
    public final String jceHmacId;
    public final String jceId;
    public final boolean needsBouncyCastle;

    private HashAlgorithm(String jceId, int ecmaId, String ecmaString, int hashSize, String jceHmacId, boolean needsBouncyCastle) {
        this.jceId = jceId;
        this.ecmaId = ecmaId;
        this.ecmaString = ecmaString;
        this.hashSize = hashSize;
        this.jceHmacId = jceHmacId;
        this.needsBouncyCastle = needsBouncyCastle;
    }

    public static HashAlgorithm fromEcmaId(int ecmaId) {
        for (HashAlgorithm ha : values()) {
            if (ha.ecmaId == ecmaId) {
                return ha;
            }
        }
        throw new EncryptedDocumentException("hash algorithm not found");
    }

    public static HashAlgorithm fromEcmaId(String ecmaString) {
        for (HashAlgorithm ha : values()) {
            if (ha.ecmaString.equals(ecmaString)) {
                return ha;
            }
        }
        throw new EncryptedDocumentException("hash algorithm not found");
    }

    public static HashAlgorithm fromString(String string) {
        for (HashAlgorithm ha : values()) {
            if (ha.ecmaString.equalsIgnoreCase(string) || ha.jceId.equalsIgnoreCase(string)) {
                return ha;
            }
        }
        throw new EncryptedDocumentException("hash algorithm not found");
    }
}

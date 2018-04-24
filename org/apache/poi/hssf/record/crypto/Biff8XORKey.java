package org.apache.poi.hssf.record.crypto;

import javax.crypto.spec.SecretKeySpec;
import org.apache.poi.poifs.crypt.CryptoFunctions;

public class Biff8XORKey extends Biff8EncryptionKey {
    final int _xorKey;

    public Biff8XORKey(String password, int xorKey) {
        this._xorKey = xorKey;
        this._secretKey = new SecretKeySpec(CryptoFunctions.createXorArray1(password), "XOR");
    }

    public static Biff8XORKey create(String password, int xorKey) {
        return new Biff8XORKey(password, xorKey);
    }

    public boolean validate(String password, int verifier) {
        return this._xorKey == CryptoFunctions.createXorKey1(password) && CryptoFunctions.createXorVerifier1(password) == verifier;
    }
}

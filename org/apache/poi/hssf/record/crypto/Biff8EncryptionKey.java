package org.apache.poi.hssf.record.crypto;

import javax.crypto.SecretKey;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.poifs.crypt.Decryptor;

public abstract class Biff8EncryptionKey {
    private static final ThreadLocal<String> _userPasswordTLS = new ThreadLocal();
    protected SecretKey _secretKey;

    public static Biff8EncryptionKey create(byte[] salt) {
        return Biff8RC4Key.create(Decryptor.DEFAULT_PASSWORD, salt);
    }

    public static Biff8EncryptionKey create(String password, byte[] salt) {
        return Biff8RC4Key.create(password, salt);
    }

    public boolean validate(byte[] saltData, byte[] saltHash) {
        throw new EncryptedDocumentException("validate is not supported (in super-class).");
    }

    public static void setCurrentUserPassword(String password) {
        _userPasswordTLS.set(password);
    }

    public static String getCurrentUserPassword() {
        return (String) _userPasswordTLS.get();
    }
}

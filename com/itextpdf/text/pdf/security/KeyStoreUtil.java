package com.itextpdf.text.pdf.security;

import com.itextpdf.text.ExceptionConverter;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

public class KeyStoreUtil {
    public static KeyStore loadCacertsKeyStore(String provider) {
        Exception e;
        Throwable th;
        FileInputStream fin = null;
        try {
            KeyStore k;
            FileInputStream fin2 = new FileInputStream(new File(new File(new File(System.getProperty("java.home"), "lib"), "security"), "cacerts"));
            if (provider == null) {
                try {
                    k = KeyStore.getInstance("JKS");
                } catch (Exception e2) {
                    e = e2;
                    fin = fin2;
                    try {
                        throw new ExceptionConverter(e);
                    } catch (Throwable th2) {
                        th = th2;
                        if (fin != null) {
                            try {
                                fin.close();
                            } catch (Exception e3) {
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    fin = fin2;
                    if (fin != null) {
                        fin.close();
                    }
                    throw th;
                }
            }
            k = KeyStore.getInstance("JKS", provider);
            k.load(fin2, null);
            if (fin2 != null) {
                try {
                    fin2.close();
                } catch (Exception e4) {
                }
            }
            return k;
        } catch (Exception e5) {
            e = e5;
            throw new ExceptionConverter(e);
        }
    }

    public static KeyStore loadCacertsKeyStore() {
        return loadCacertsKeyStore(null);
    }
}

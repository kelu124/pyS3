package com.itextpdf.text.pdf.crypto;

public final class IVGenerator {
    private static ARCFOUREncryption arcfour = new ARCFOUREncryption();

    static {
        long time = System.currentTimeMillis();
        arcfour.prepareARCFOURKey((time + "+" + Runtime.getRuntime().freeMemory()).getBytes());
    }

    private IVGenerator() {
    }

    public static byte[] getIV() {
        return getIV(16);
    }

    public static byte[] getIV(int len) {
        byte[] b = new byte[len];
        synchronized (arcfour) {
            arcfour.encryptARCFOUR(b);
        }
        return b;
    }
}

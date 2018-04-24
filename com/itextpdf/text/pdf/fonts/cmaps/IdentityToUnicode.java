package com.itextpdf.text.pdf.fonts.cmaps;

import java.io.IOException;

public class IdentityToUnicode {
    private static CMapToUnicode identityCNS;
    private static CMapToUnicode identityGB;
    private static CMapToUnicode identityJapan;
    private static CMapToUnicode identityKorea;

    public static CMapToUnicode GetMapFromOrdering(String ordering) throws IOException {
        CMapUniCid uni;
        if (ordering.equals("CNS1")) {
            if (identityCNS == null) {
                uni = CMapCache.getCachedCMapUniCid("UniCNS-UTF16-H");
                if (uni == null) {
                    return null;
                }
                identityCNS = uni.exportToUnicode();
            }
            return identityCNS;
        } else if (ordering.equals("Japan1")) {
            if (identityJapan == null) {
                uni = CMapCache.getCachedCMapUniCid("UniJIS-UTF16-H");
                if (uni == null) {
                    return null;
                }
                identityJapan = uni.exportToUnicode();
            }
            return identityJapan;
        } else if (ordering.equals("Korea1")) {
            if (identityKorea == null) {
                uni = CMapCache.getCachedCMapUniCid("UniKS-UTF16-H");
                if (uni == null) {
                    return null;
                }
                identityKorea = uni.exportToUnicode();
            }
            return identityKorea;
        } else if (!ordering.equals("GB1")) {
            return null;
        } else {
            if (identityGB == null) {
                uni = CMapCache.getCachedCMapUniCid("UniGB-UTF16-H");
                if (uni == null) {
                    return null;
                }
                identityGB = uni.exportToUnicode();
            }
            return identityGB;
        }
    }
}

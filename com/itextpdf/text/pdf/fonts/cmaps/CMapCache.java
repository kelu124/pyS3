package com.itextpdf.text.pdf.fonts.cmaps;

import java.io.IOException;
import java.util.HashMap;

public class CMapCache {
    private static final HashMap<String, CMapByteCid> cacheByteCid = new HashMap();
    private static final HashMap<String, CMapCidByte> cacheCidByte = new HashMap();
    private static final HashMap<String, CMapCidUni> cacheCidUni = new HashMap();
    private static final HashMap<String, CMapUniCid> cacheUniCid = new HashMap();

    public static CMapUniCid getCachedCMapUniCid(String name) throws IOException {
        CMapUniCid cmap;
        synchronized (cacheUniCid) {
            cmap = (CMapUniCid) cacheUniCid.get(name);
        }
        if (cmap == null) {
            cmap = new CMapUniCid();
            CMapParserEx.parseCid(name, cmap, new CidResource());
            synchronized (cacheUniCid) {
                cacheUniCid.put(name, cmap);
            }
        }
        return cmap;
    }

    public static CMapCidUni getCachedCMapCidUni(String name) throws IOException {
        CMapCidUni cmap;
        synchronized (cacheCidUni) {
            cmap = (CMapCidUni) cacheCidUni.get(name);
        }
        if (cmap == null) {
            cmap = new CMapCidUni();
            CMapParserEx.parseCid(name, cmap, new CidResource());
            synchronized (cacheCidUni) {
                cacheCidUni.put(name, cmap);
            }
        }
        return cmap;
    }

    public static CMapCidByte getCachedCMapCidByte(String name) throws IOException {
        CMapCidByte cmap;
        synchronized (cacheCidByte) {
            cmap = (CMapCidByte) cacheCidByte.get(name);
        }
        if (cmap == null) {
            cmap = new CMapCidByte();
            CMapParserEx.parseCid(name, cmap, new CidResource());
            synchronized (cacheCidByte) {
                cacheCidByte.put(name, cmap);
            }
        }
        return cmap;
    }

    public static CMapByteCid getCachedCMapByteCid(String name) throws IOException {
        CMapByteCid cmap;
        synchronized (cacheByteCid) {
            cmap = (CMapByteCid) cacheByteCid.get(name);
        }
        if (cmap == null) {
            cmap = new CMapByteCid();
            CMapParserEx.parseCid(name, cmap, new CidResource());
            synchronized (cacheByteCid) {
                cacheByteCid.put(name, cmap);
            }
        }
        return cmap;
    }
}

package org.apache.poi.poifs.filesystem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Set;
import org.apache.poi.hpsf.ClassID;

public interface DirectoryEntry extends Entry, Iterable<Entry> {
    DirectoryEntry createDirectory(String str) throws IOException;

    DocumentEntry createDocument(String str, int i, POIFSWriterListener pOIFSWriterListener) throws IOException;

    DocumentEntry createDocument(String str, InputStream inputStream) throws IOException;

    Iterator<Entry> getEntries();

    Entry getEntry(String str) throws FileNotFoundException;

    int getEntryCount();

    Set<String> getEntryNames();

    ClassID getStorageClsid();

    boolean hasEntry(String str);

    boolean isEmpty();

    void setStorageClsid(ClassID classID);
}

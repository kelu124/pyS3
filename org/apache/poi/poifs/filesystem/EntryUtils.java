package org.apache.poi.poifs.filesystem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.poi.util.Internal;

@Internal
public class EntryUtils {
    @Internal
    public static void copyNodeRecursively(Entry entry, DirectoryEntry target) throws IOException {
        if (entry.isDirectoryEntry()) {
            DirectoryEntry dirEntry = (DirectoryEntry) entry;
            DirectoryEntry newTarget = target.createDirectory(entry.getName());
            newTarget.setStorageClsid(dirEntry.getStorageClsid());
            Iterator<Entry> entries = dirEntry.getEntries();
            while (entries.hasNext()) {
                copyNodeRecursively((Entry) entries.next(), newTarget);
            }
            return;
        }
        DocumentEntry dentry = (DocumentEntry) entry;
        DocumentInputStream dstream = new DocumentInputStream(dentry);
        target.createDocument(dentry.getName(), dstream);
        dstream.close();
    }

    public static void copyNodes(DirectoryEntry sourceRoot, DirectoryEntry targetRoot) throws IOException {
        for (Entry entry : sourceRoot) {
            copyNodeRecursively(entry, targetRoot);
        }
    }

    public static void copyNodes(FilteringDirectoryNode filteredSource, FilteringDirectoryNode filteredTarget) throws IOException {
        copyNodes((DirectoryEntry) filteredSource, (DirectoryEntry) filteredTarget);
    }

    public static void copyNodes(DirectoryEntry sourceRoot, DirectoryEntry targetRoot, List<String> excepts) throws IOException {
        Iterator<Entry> entries = sourceRoot.getEntries();
        while (entries.hasNext()) {
            Entry entry = (Entry) entries.next();
            if (!excepts.contains(entry.getName())) {
                copyNodeRecursively(entry, targetRoot);
            }
        }
    }

    public static void copyNodes(OPOIFSFileSystem source, OPOIFSFileSystem target) throws IOException {
        copyNodes(source.getRoot(), target.getRoot());
    }

    public static void copyNodes(NPOIFSFileSystem source, NPOIFSFileSystem target) throws IOException {
        copyNodes(source.getRoot(), target.getRoot());
    }

    public static void copyNodes(OPOIFSFileSystem source, OPOIFSFileSystem target, List<String> excepts) throws IOException {
        copyNodes(new FilteringDirectoryNode(source.getRoot(), excepts), new FilteringDirectoryNode(target.getRoot(), excepts));
    }

    public static void copyNodes(NPOIFSFileSystem source, NPOIFSFileSystem target, List<String> excepts) throws IOException {
        copyNodes(new FilteringDirectoryNode(source.getRoot(), excepts), new FilteringDirectoryNode(target.getRoot(), excepts));
    }

    public static boolean areDirectoriesIdentical(DirectoryEntry dirA, DirectoryEntry dirB) {
        if (!dirA.getName().equals(dirB.getName())) {
            return false;
        }
        if (dirA.getEntryCount() != dirB.getEntryCount()) {
            return false;
        }
        Map<String, Integer> aSizes = new HashMap();
        for (Entry a : dirA) {
            String aName = a.getName();
            if (a.isDirectoryEntry()) {
                aSizes.put(aName, Integer.valueOf(-12345));
            } else {
                aSizes.put(aName, Integer.valueOf(((DocumentNode) a).getSize()));
            }
        }
        for (Entry b : dirB) {
            Entry b2;
            String bName = b2.getName();
            if (!aSizes.containsKey(bName)) {
                return false;
            }
            int size;
            if (b2.isDirectoryEntry()) {
                size = -12345;
            } else {
                size = ((DocumentNode) b2).getSize();
            }
            if (size != ((Integer) aSizes.get(bName)).intValue()) {
                return false;
            }
            aSizes.remove(bName);
        }
        if (!aSizes.isEmpty()) {
            return false;
        }
        for (Entry a2 : dirA) {
            try {
                boolean match;
                b2 = dirB.getEntry(a2.getName());
                if (a2.isDirectoryEntry()) {
                    match = areDirectoriesIdentical((DirectoryEntry) a2, (DirectoryEntry) b2);
                    continue;
                } else {
                    match = areDocumentsIdentical((DocumentEntry) a2, (DocumentEntry) b2);
                    continue;
                }
                if (!match) {
                    return false;
                }
            } catch (FileNotFoundException e) {
                return false;
            } catch (IOException e2) {
                return false;
            }
        }
        return true;
    }

    public static boolean areDocumentsIdentical(DocumentEntry docA, DocumentEntry docB) throws IOException {
        Throwable th;
        boolean z = false;
        if (docA.getName().equals(docB.getName()) && docA.getSize() == docB.getSize()) {
            z = true;
            DocumentInputStream inpA = null;
            DocumentInputStream inpB = null;
            try {
                DocumentInputStream inpA2 = new DocumentInputStream(docA);
                try {
                    DocumentInputStream inpB2 = new DocumentInputStream(docB);
                    int readB;
                    do {
                        try {
                            int readA = inpA2.read();
                            readB = inpB2.read();
                            if (readA == readB) {
                                if (readA == -1) {
                                    break;
                                }
                            } else {
                                z = false;
                                break;
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            inpB = inpB2;
                            inpA = inpA2;
                        }
                    } while (readB != -1);
                    if (inpA2 != null) {
                        inpA2.close();
                    }
                    if (inpB2 != null) {
                        inpB2.close();
                    }
                } catch (Throwable th3) {
                    th = th3;
                    inpA = inpA2;
                    if (inpA != null) {
                        inpA.close();
                    }
                    if (inpB != null) {
                        inpB.close();
                    }
                    throw th;
                }
            } catch (Throwable th4) {
                th = th4;
                if (inpA != null) {
                    inpA.close();
                }
                if (inpB != null) {
                    inpB.close();
                }
                throw th;
            }
        }
        return z;
    }
}

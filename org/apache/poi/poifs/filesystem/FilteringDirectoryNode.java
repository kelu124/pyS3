package org.apache.poi.poifs.filesystem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.poi.hpsf.ClassID;

public class FilteringDirectoryNode implements DirectoryEntry {
    private Map<String, List<String>> childExcludes = new HashMap();
    private DirectoryEntry directory;
    private Set<String> excludes = new HashSet();

    private class FilteringIterator implements Iterator<Entry> {
        private Entry next;
        private Iterator<Entry> parent;

        private FilteringIterator() {
            this.parent = FilteringDirectoryNode.this.directory.getEntries();
            locateNext();
        }

        private void locateNext() {
            this.next = null;
            while (this.parent.hasNext() && this.next == null) {
                Entry e = (Entry) this.parent.next();
                if (!FilteringDirectoryNode.this.excludes.contains(e.getName())) {
                    this.next = FilteringDirectoryNode.this.wrapEntry(e);
                }
            }
        }

        public boolean hasNext() {
            return this.next != null;
        }

        public Entry next() {
            Entry e = this.next;
            locateNext();
            return e;
        }

        public void remove() {
            throw new UnsupportedOperationException("Remove not supported");
        }
    }

    public FilteringDirectoryNode(DirectoryEntry directory, Collection<String> excludes) {
        this.directory = directory;
        for (String excl : excludes) {
            int splitAt = excl.indexOf(47);
            if (splitAt == -1) {
                this.excludes.add(excl);
            } else {
                String child = excl.substring(0, splitAt);
                String childExcl = excl.substring(splitAt + 1);
                if (!this.childExcludes.containsKey(child)) {
                    this.childExcludes.put(child, new ArrayList());
                }
                ((List) this.childExcludes.get(child)).add(childExcl);
            }
        }
    }

    public DirectoryEntry createDirectory(String name) throws IOException {
        return this.directory.createDirectory(name);
    }

    public DocumentEntry createDocument(String name, InputStream stream) throws IOException {
        return this.directory.createDocument(name, stream);
    }

    public DocumentEntry createDocument(String name, int size, POIFSWriterListener writer) throws IOException {
        return this.directory.createDocument(name, size, writer);
    }

    public Iterator<Entry> getEntries() {
        return new FilteringIterator();
    }

    public Iterator<Entry> iterator() {
        return getEntries();
    }

    public int getEntryCount() {
        int size = this.directory.getEntryCount();
        for (String excl : this.excludes) {
            if (this.directory.hasEntry(excl)) {
                size--;
            }
        }
        return size;
    }

    public Set<String> getEntryNames() {
        Set<String> names = new HashSet();
        for (String name : this.directory.getEntryNames()) {
            if (!this.excludes.contains(name)) {
                names.add(name);
            }
        }
        return names;
    }

    public boolean isEmpty() {
        return getEntryCount() == 0;
    }

    public boolean hasEntry(String name) {
        if (this.excludes.contains(name)) {
            return false;
        }
        return this.directory.hasEntry(name);
    }

    public Entry getEntry(String name) throws FileNotFoundException {
        if (!this.excludes.contains(name)) {
            return wrapEntry(this.directory.getEntry(name));
        }
        throw new FileNotFoundException(name);
    }

    private Entry wrapEntry(Entry entry) {
        String name = entry.getName();
        if (this.childExcludes.containsKey(name) && (entry instanceof DirectoryEntry)) {
            return new FilteringDirectoryNode((DirectoryEntry) entry, (Collection) this.childExcludes.get(name));
        }
        return entry;
    }

    public ClassID getStorageClsid() {
        return this.directory.getStorageClsid();
    }

    public void setStorageClsid(ClassID clsidStorage) {
        this.directory.setStorageClsid(clsidStorage);
    }

    public boolean delete() {
        return this.directory.delete();
    }

    public boolean renameTo(String newName) {
        return this.directory.renameTo(newName);
    }

    public String getName() {
        return this.directory.getName();
    }

    public DirectoryEntry getParent() {
        return this.directory.getParent();
    }

    public boolean isDirectoryEntry() {
        return true;
    }

    public boolean isDocumentEntry() {
        return false;
    }
}

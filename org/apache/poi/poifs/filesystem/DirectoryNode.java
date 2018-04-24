package org.apache.poi.poifs.filesystem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.poi.hpsf.ClassID;
import org.apache.poi.poifs.dev.POIFSViewable;
import org.apache.poi.poifs.property.DirectoryProperty;
import org.apache.poi.poifs.property.DocumentProperty;
import org.apache.poi.poifs.property.Property;

public class DirectoryNode extends EntryNode implements DirectoryEntry, POIFSViewable, Iterable<Entry> {
    private Map<String, Entry> _byname;
    private ArrayList<Entry> _entries;
    private NPOIFSFileSystem _nfilesystem;
    private OPOIFSFileSystem _ofilesystem;
    private POIFSDocumentPath _path;

    DirectoryNode(DirectoryProperty property, OPOIFSFileSystem filesystem, DirectoryNode parent) {
        this(property, parent, filesystem, (NPOIFSFileSystem) null);
    }

    DirectoryNode(DirectoryProperty property, NPOIFSFileSystem nfilesystem, DirectoryNode parent) {
        this(property, parent, (OPOIFSFileSystem) null, nfilesystem);
    }

    private DirectoryNode(DirectoryProperty property, DirectoryNode parent, OPOIFSFileSystem ofilesystem, NPOIFSFileSystem nfilesystem) {
        super(property, parent);
        this._ofilesystem = ofilesystem;
        this._nfilesystem = nfilesystem;
        if (parent == null) {
            this._path = new POIFSDocumentPath();
        } else {
            this._path = new POIFSDocumentPath(parent._path, new String[]{property.getName()});
        }
        this._byname = new HashMap();
        this._entries = new ArrayList();
        Iterator<Property> iter = property.getChildren();
        while (iter.hasNext()) {
            Entry childNode;
            Property child = (Property) iter.next();
            if (child.isDirectory()) {
                DirectoryProperty childDir = (DirectoryProperty) child;
                if (this._ofilesystem != null) {
                    childNode = new DirectoryNode(childDir, this._ofilesystem, this);
                } else {
                    childNode = new DirectoryNode(childDir, this._nfilesystem, this);
                }
            } else {
                childNode = new DocumentNode((DocumentProperty) child, this);
            }
            this._entries.add(childNode);
            this._byname.put(childNode.getName(), childNode);
        }
    }

    public POIFSDocumentPath getPath() {
        return this._path;
    }

    public NPOIFSFileSystem getFileSystem() {
        return this._nfilesystem;
    }

    public OPOIFSFileSystem getOFileSystem() {
        return this._ofilesystem;
    }

    public NPOIFSFileSystem getNFileSystem() {
        return this._nfilesystem;
    }

    public DocumentInputStream createDocumentInputStream(String documentName) throws IOException {
        return createDocumentInputStream(getEntry(documentName));
    }

    public DocumentInputStream createDocumentInputStream(Entry document) throws IOException {
        if (document.isDocumentEntry()) {
            return new DocumentInputStream((DocumentEntry) document);
        }
        throw new IOException("Entry '" + document.getName() + "' is not a DocumentEntry");
    }

    DocumentEntry createDocument(OPOIFSDocument document) throws IOException {
        DocumentProperty property = document.getDocumentProperty();
        DocumentNode rval = new DocumentNode(property, this);
        ((DirectoryProperty) getProperty()).addChild(property);
        this._ofilesystem.addDocument(document);
        this._entries.add(rval);
        this._byname.put(property.getName(), rval);
        return rval;
    }

    DocumentEntry createDocument(NPOIFSDocument document) throws IOException {
        DocumentProperty property = document.getDocumentProperty();
        DocumentNode rval = new DocumentNode(property, this);
        ((DirectoryProperty) getProperty()).addChild(property);
        this._nfilesystem.addDocument(document);
        this._entries.add(rval);
        this._byname.put(property.getName(), rval);
        return rval;
    }

    boolean changeName(String oldName, String newName) {
        boolean rval = false;
        EntryNode child = (EntryNode) this._byname.get(oldName);
        if (child != null) {
            rval = ((DirectoryProperty) getProperty()).changeName(child.getProperty(), newName);
            if (rval) {
                this._byname.remove(oldName);
                this._byname.put(child.getProperty().getName(), child);
            }
        }
        return rval;
    }

    boolean deleteEntry(EntryNode entry) {
        boolean rval = ((DirectoryProperty) getProperty()).deleteChild(entry.getProperty());
        if (rval) {
            this._entries.remove(entry);
            this._byname.remove(entry.getName());
            if (this._ofilesystem != null) {
                this._ofilesystem.remove(entry);
            } else {
                try {
                    this._nfilesystem.remove(entry);
                } catch (IOException e) {
                }
            }
        }
        return rval;
    }

    public Iterator<Entry> getEntries() {
        return this._entries.iterator();
    }

    public Set<String> getEntryNames() {
        return this._byname.keySet();
    }

    public boolean isEmpty() {
        return this._entries.isEmpty();
    }

    public int getEntryCount() {
        return this._entries.size();
    }

    public boolean hasEntry(String name) {
        return name != null && this._byname.containsKey(name);
    }

    public Entry getEntry(String name) throws FileNotFoundException {
        Entry rval = null;
        if (name != null) {
            rval = (Entry) this._byname.get(name);
        }
        if (rval != null) {
            return rval;
        }
        throw new FileNotFoundException("no such entry: \"" + name + "\", had: " + this._byname.keySet());
    }

    public DocumentEntry createDocument(String name, InputStream stream) throws IOException {
        if (this._nfilesystem != null) {
            return createDocument(new NPOIFSDocument(name, this._nfilesystem, stream));
        }
        return createDocument(new OPOIFSDocument(name, stream));
    }

    public DocumentEntry createDocument(String name, int size, POIFSWriterListener writer) throws IOException {
        if (this._nfilesystem != null) {
            return createDocument(new NPOIFSDocument(name, size, this._nfilesystem, writer));
        }
        return createDocument(new OPOIFSDocument(name, size, this._path, writer));
    }

    public DirectoryEntry createDirectory(String name) throws IOException {
        DirectoryNode rval;
        DirectoryProperty property = new DirectoryProperty(name);
        if (this._ofilesystem != null) {
            rval = new DirectoryNode(property, this._ofilesystem, this);
            this._ofilesystem.addDirectory(property);
        } else {
            rval = new DirectoryNode(property, this._nfilesystem, this);
            this._nfilesystem.addDirectory(property);
        }
        ((DirectoryProperty) getProperty()).addChild(property);
        this._entries.add(rval);
        this._byname.put(name, rval);
        return rval;
    }

    public DocumentEntry createOrUpdateDocument(String name, InputStream stream) throws IOException {
        if (!hasEntry(name)) {
            return createDocument(name, stream);
        }
        DocumentNode existing = (DocumentNode) getEntry(name);
        if (this._nfilesystem != null) {
            new NPOIFSDocument(existing).replaceContents(stream);
            return existing;
        }
        deleteEntry(existing);
        return createDocument(name, stream);
    }

    public ClassID getStorageClsid() {
        return getProperty().getStorageClsid();
    }

    public void setStorageClsid(ClassID clsidStorage) {
        getProperty().setStorageClsid(clsidStorage);
    }

    public boolean isDirectoryEntry() {
        return true;
    }

    protected boolean isDeleteOK() {
        return isEmpty();
    }

    public Object[] getViewableArray() {
        return new Object[0];
    }

    public Iterator<Object> getViewableIterator() {
        List<Object> components = new ArrayList();
        components.add(getProperty());
        Iterator<Entry> iter = this._entries.iterator();
        while (iter.hasNext()) {
            components.add(iter.next());
        }
        return components.iterator();
    }

    public boolean preferArray() {
        return false;
    }

    public String getShortDescription() {
        return getName();
    }

    public Iterator<Entry> iterator() {
        return getEntries();
    }
}

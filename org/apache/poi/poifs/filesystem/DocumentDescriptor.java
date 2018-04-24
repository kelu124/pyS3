package org.apache.poi.poifs.filesystem;

public class DocumentDescriptor {
    private int hashcode = 0;
    private String name;
    private POIFSDocumentPath path;

    public DocumentDescriptor(POIFSDocumentPath path, String name) {
        if (path == null) {
            throw new NullPointerException("path must not be null");
        } else if (name == null) {
            throw new NullPointerException("name must not be null");
        } else if (name.length() == 0) {
            throw new IllegalArgumentException("name cannot be empty");
        } else {
            this.path = path;
            this.name = name;
        }
    }

    public boolean equals(Object o) {
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        if (this == o) {
            return true;
        }
        DocumentDescriptor descriptor = (DocumentDescriptor) o;
        boolean rval = this.path.equals(descriptor.path) && this.name.equals(descriptor.name);
        return rval;
    }

    public int hashCode() {
        if (this.hashcode == 0) {
            this.hashcode = this.path.hashCode() ^ this.name.hashCode();
        }
        return this.hashcode;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer((this.path.length() + 1) * 40);
        for (int j = 0; j < this.path.length(); j++) {
            buffer.append(this.path.getComponent(j)).append("/");
        }
        buffer.append(this.name);
        return buffer.toString();
    }
}

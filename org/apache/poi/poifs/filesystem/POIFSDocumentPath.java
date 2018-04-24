package org.apache.poi.poifs.filesystem;

import java.io.File;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

public class POIFSDocumentPath {
    private static final POILogger log = POILogFactory.getLogger(POIFSDocumentPath.class);
    private final String[] components;
    private int hashcode;

    public POIFSDocumentPath(String[] components) throws IllegalArgumentException {
        this.hashcode = 0;
        if (components == null) {
            this.components = new String[0];
            return;
        }
        this.components = new String[components.length];
        int j = 0;
        while (j < components.length) {
            if (components[j] == null || components[j].length() == 0) {
                throw new IllegalArgumentException("components cannot contain null or empty strings");
            }
            this.components[j] = components[j];
            j++;
        }
    }

    public POIFSDocumentPath() {
        this.hashcode = 0;
        this.components = new String[0];
    }

    public POIFSDocumentPath(POIFSDocumentPath path, String[] components) throws IllegalArgumentException {
        int j;
        this.hashcode = 0;
        if (components == null) {
            this.components = new String[path.components.length];
        } else {
            this.components = new String[(path.components.length + components.length)];
        }
        for (j = 0; j < path.components.length; j++) {
            this.components[j] = path.components[j];
        }
        if (components != null) {
            for (j = 0; j < components.length; j++) {
                if (components[j] == null) {
                    throw new IllegalArgumentException("components cannot contain null");
                }
                if (components[j].length() == 0) {
                    log.log(5, new Object[]{"Directory under " + path + " has an empty name, " + "not all OLE2 readers will handle this file correctly!"});
                }
                this.components[path.components.length + j] = components[j];
            }
        }
    }

    public boolean equals(Object o) {
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        if (this == o) {
            return true;
        }
        POIFSDocumentPath path = (POIFSDocumentPath) o;
        if (path.components.length != this.components.length) {
            return false;
        }
        for (int j = 0; j < this.components.length; j++) {
            if (!path.components[j].equals(this.components[j])) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        if (this.hashcode == 0) {
            this.hashcode = computeHashCode();
        }
        return this.hashcode;
    }

    private int computeHashCode() {
        int code = 0;
        for (String hashCode : this.components) {
            code += hashCode.hashCode();
        }
        return code;
    }

    public int length() {
        return this.components.length;
    }

    public String getComponent(int n) throws ArrayIndexOutOfBoundsException {
        return this.components[n];
    }

    public POIFSDocumentPath getParent() {
        int length = this.components.length - 1;
        if (length < 0) {
            return null;
        }
        String[] parentComponents = new String[length];
        System.arraycopy(this.components, 0, parentComponents, 0, length);
        return new POIFSDocumentPath(parentComponents);
    }

    public String getName() {
        if (this.components.length == 0) {
            return "";
        }
        return this.components[this.components.length - 1];
    }

    public String toString() {
        StringBuffer b = new StringBuffer();
        int l = length();
        b.append(File.separatorChar);
        for (int i = 0; i < l; i++) {
            b.append(getComponent(i));
            if (i < l - 1) {
                b.append(File.separatorChar);
            }
        }
        return b.toString();
    }
}

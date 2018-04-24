package org.apache.poi.poifs.property;

import org.apache.poi.poifs.filesystem.OPOIFSDocument;

public class DocumentProperty extends Property {
    private OPOIFSDocument _document = null;

    public DocumentProperty(String name, int size) {
        setName(name);
        setSize(size);
        setNodeColor((byte) 1);
        setPropertyType((byte) 2);
    }

    protected DocumentProperty(int index, byte[] array, int offset) {
        super(index, array, offset);
    }

    public void setDocument(OPOIFSDocument doc) {
        this._document = doc;
    }

    public OPOIFSDocument getDocument() {
        return this._document;
    }

    public boolean shouldUseSmallBlocks() {
        return super.shouldUseSmallBlocks();
    }

    public boolean isDirectory() {
        return false;
    }

    protected void preWrite() {
    }

    public void updateSize(int size) {
        setSize(size);
    }
}

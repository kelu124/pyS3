package org.apache.poi.poifs.property;

import org.apache.poi.poifs.storage.SmallDocumentBlock;

public final class RootProperty extends DirectoryProperty {
    private static final String NAME = "Root Entry";

    RootProperty() {
        super(NAME);
        setNodeColor((byte) 1);
        setPropertyType((byte) 5);
        setStartBlock(-2);
    }

    protected RootProperty(int index, byte[] array, int offset) {
        super(index, array, offset);
    }

    public void setSize(int size) {
        super.setSize(SmallDocumentBlock.calcSize(size));
    }

    public String getName() {
        return NAME;
    }
}

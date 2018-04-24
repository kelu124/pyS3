package org.apache.poi.poifs.property;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import org.apache.poi.poifs.filesystem.BATManaged;
import org.apache.poi.poifs.storage.HeaderBlock;

public abstract class PropertyTableBase implements BATManaged {
    private final HeaderBlock _header_block;
    protected final List<Property> _properties;

    public PropertyTableBase(HeaderBlock header_block) {
        this._header_block = header_block;
        this._properties = new ArrayList();
        addProperty(new RootProperty());
    }

    public PropertyTableBase(HeaderBlock header_block, List<Property> properties) throws IOException {
        this._header_block = header_block;
        this._properties = properties;
        populatePropertyTree((DirectoryProperty) this._properties.get(0));
    }

    public void addProperty(Property property) {
        this._properties.add(property);
    }

    public void removeProperty(Property property) {
        this._properties.remove(property);
    }

    public RootProperty getRoot() {
        return (RootProperty) this._properties.get(0);
    }

    private void populatePropertyTree(DirectoryProperty root) throws IOException {
        int index = root.getChildIndex();
        if (Property.isValidIndex(index)) {
            Stack<Property> children = new Stack();
            children.push(this._properties.get(index));
            while (!children.empty()) {
                Property property = (Property) children.pop();
                if (property != null) {
                    root.addChild(property);
                    if (property.isDirectory()) {
                        populatePropertyTree((DirectoryProperty) property);
                    }
                    index = property.getPreviousChildIndex();
                    if (Property.isValidIndex(index)) {
                        children.push(this._properties.get(index));
                    }
                    index = property.getNextChildIndex();
                    if (Property.isValidIndex(index)) {
                        children.push(this._properties.get(index));
                    }
                }
            }
        }
    }

    public int getStartBlock() {
        return this._header_block.getPropertyStart();
    }

    public void setStartBlock(int index) {
        this._header_block.setPropertyStart(index);
    }
}

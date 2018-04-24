package org.apache.poi.poifs.property;

import java.io.IOException;
import java.util.Iterator;

public interface Parent extends Child {
    void addChild(Property property) throws IOException;

    Iterator getChildren();

    void setNextChild(Child child);

    void setPreviousChild(Child child);
}

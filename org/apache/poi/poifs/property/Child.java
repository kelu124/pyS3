package org.apache.poi.poifs.property;

public interface Child {
    Child getNextChild();

    Child getPreviousChild();

    void setNextChild(Child child);

    void setPreviousChild(Child child);
}

package org.apache.poi.hpsf;

public class CustomProperty extends MutableProperty {
    private String name;

    public CustomProperty() {
        this.name = null;
    }

    public CustomProperty(Property property) {
        this(property, null);
    }

    public CustomProperty(Property property, String name) {
        super(property);
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean equalsContents(Object o) {
        boolean equalNames;
        CustomProperty c = (CustomProperty) o;
        String name1 = c.getName();
        String name2 = getName();
        if (name1 != null) {
            equalNames = name1.equals(name2);
        } else if (name2 == null) {
            equalNames = true;
        } else {
            equalNames = false;
        }
        if (equalNames && c.getID() == getID() && c.getType() == getType() && c.getValue().equals(getValue())) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (int) getID();
    }

    public boolean equals(Object o) {
        return o instanceof CustomProperty ? equalsContents(o) : false;
    }
}

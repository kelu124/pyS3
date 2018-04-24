package org.bytedeco.javacpp.tools;

import java.util.LinkedHashMap;

class TemplateMap extends LinkedHashMap<String, Type> {
    Declarator declarator = null;
    TemplateMap parent = null;
    Type type = null;
    boolean variadic = false;

    TemplateMap(TemplateMap parent) {
        this.parent = parent;
    }

    String getName() {
        if (this.type != null) {
            return this.type.cppName;
        }
        return this.declarator != null ? this.declarator.cppName : null;
    }

    boolean full() {
        for (Type t : values()) {
            if (t == null) {
                return false;
            }
        }
        return true;
    }

    Type get(String key) {
        Type value = (Type) super.get(key);
        if (value != null || this.parent == null) {
            return value;
        }
        return this.parent.get(key);
    }
}

package org.apache.poi.hpsf;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class CustomProperties extends HashMap<Object, CustomProperty> {
    private final Map<Long, String> dictionaryIDToName = new HashMap();
    private final Map<String, Long> dictionaryNameToID = new HashMap();
    private boolean isPure = true;

    public CustomProperty put(String name, CustomProperty cp) {
        if (name == null) {
            this.isPure = false;
            return null;
        } else if (name.equals(cp.getName())) {
            Long idKey = Long.valueOf(cp.getID());
            Long oldID = (Long) this.dictionaryNameToID.get(name);
            this.dictionaryIDToName.remove(oldID);
            this.dictionaryNameToID.put(name, idKey);
            this.dictionaryIDToName.put(idKey, name);
            CustomProperty oldCp = (CustomProperty) super.remove(oldID);
            super.put(idKey, cp);
            return oldCp;
        } else {
            throw new IllegalArgumentException("Parameter \"name\" (" + name + ") and custom property's name (" + cp.getName() + ") do not match.");
        }
    }

    private Object put(CustomProperty customProperty) throws ClassCastException {
        String name = customProperty.getName();
        Long oldId = (Long) this.dictionaryNameToID.get(name);
        if (oldId != null) {
            customProperty.setID(oldId.longValue());
        } else {
            long max = 1;
            for (Long long1 : this.dictionaryIDToName.keySet()) {
                long id = long1.longValue();
                if (id > max) {
                    max = id;
                }
            }
            customProperty.setID(1 + max);
        }
        return put(name, customProperty);
    }

    public Object remove(String name) {
        Long id = (Long) this.dictionaryNameToID.get(name);
        if (id == null) {
            return null;
        }
        this.dictionaryIDToName.remove(id);
        this.dictionaryNameToID.remove(name);
        return super.remove(id);
    }

    public Object put(String name, String value) {
        MutableProperty p = new MutableProperty();
        p.setID(-1);
        p.setType(31);
        p.setValue(value);
        return put(new CustomProperty(p, name));
    }

    public Object put(String name, Long value) {
        MutableProperty p = new MutableProperty();
        p.setID(-1);
        p.setType(20);
        p.setValue(value);
        return put(new CustomProperty(p, name));
    }

    public Object put(String name, Double value) {
        MutableProperty p = new MutableProperty();
        p.setID(-1);
        p.setType(5);
        p.setValue(value);
        return put(new CustomProperty(p, name));
    }

    public Object put(String name, Integer value) {
        MutableProperty p = new MutableProperty();
        p.setID(-1);
        p.setType(3);
        p.setValue(value);
        return put(new CustomProperty(p, name));
    }

    public Object put(String name, Boolean value) {
        MutableProperty p = new MutableProperty();
        p.setID(-1);
        p.setType(11);
        p.setValue(value);
        return put(new CustomProperty(p, name));
    }

    public Object get(String name) {
        CustomProperty cp = (CustomProperty) super.get((Long) this.dictionaryNameToID.get(name));
        return cp != null ? cp.getValue() : null;
    }

    public Object put(String name, Date value) {
        MutableProperty p = new MutableProperty();
        p.setID(-1);
        p.setType(64);
        p.setValue(value);
        return put(new CustomProperty(p, name));
    }

    public Set keySet() {
        return this.dictionaryNameToID.keySet();
    }

    public Set<String> nameSet() {
        return this.dictionaryNameToID.keySet();
    }

    public Set<String> idSet() {
        return this.dictionaryNameToID.keySet();
    }

    public void setCodepage(int codepage) {
        MutableProperty p = new MutableProperty();
        p.setID(1);
        p.setType(2);
        p.setValue(Integer.valueOf(codepage));
        put(new CustomProperty(p));
    }

    Map<Long, String> getDictionary() {
        return this.dictionaryIDToName;
    }

    public boolean containsKey(Object key) {
        if (key instanceof Long) {
            return super.containsKey(key);
        }
        if (key instanceof String) {
            return super.containsKey(this.dictionaryNameToID.get(key));
        }
        return false;
    }

    public boolean containsValue(Object value) {
        if (value instanceof CustomProperty) {
            return super.containsValue(value);
        }
        for (CustomProperty cp : super.values()) {
            if (cp.getValue() == value) {
                return true;
            }
        }
        return false;
    }

    public int getCodepage() {
        int codepage = -1;
        Iterator<CustomProperty> i = values().iterator();
        while (codepage == -1 && i.hasNext()) {
            CustomProperty cp = (CustomProperty) i.next();
            if (cp.getID() == 1) {
                codepage = ((Integer) cp.getValue()).intValue();
            }
        }
        return codepage;
    }

    public boolean isPure() {
        return this.isPure;
    }

    public void setPure(boolean isPure) {
        this.isPure = isPure;
    }
}

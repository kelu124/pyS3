package org.apache.poi.hpsf;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.poi.hpsf.wellknown.SectionIDMap;
import org.apache.poi.util.LittleEndian;

public class Section {
    protected Map<Long, String> dictionary;
    protected ClassID formatID;
    protected long offset;
    protected Property[] properties;
    protected int size;
    private boolean wasNull;

    static class PropertyListEntry implements Comparable<PropertyListEntry> {
        int id;
        int length;
        int offset;

        PropertyListEntry() {
        }

        public int compareTo(PropertyListEntry o) {
            int otherOffset = o.offset;
            if (this.offset < otherOffset) {
                return -1;
            }
            if (this.offset == otherOffset) {
                return 0;
            }
            return 1;
        }

        public int hashCode() {
            return ((((this.id + 31) * 31) + this.length) * 31) + this.offset;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            PropertyListEntry other = (PropertyListEntry) obj;
            if (this.id != other.id) {
                return false;
            }
            if (this.length != other.length) {
                return false;
            }
            if (this.offset != other.offset) {
                return false;
            }
            return true;
        }

        public String toString() {
            StringBuffer b = new StringBuffer();
            b.append(getClass().getName());
            b.append("[id=");
            b.append(this.id);
            b.append(", offset=");
            b.append(this.offset);
            b.append(", length=");
            b.append(this.length);
            b.append(']');
            return b.toString();
        }
    }

    public ClassID getFormatID() {
        return this.formatID;
    }

    public long getOffset() {
        return this.offset;
    }

    public int getSize() {
        return this.size;
    }

    public int getPropertyCount() {
        return this.properties.length;
    }

    public Property[] getProperties() {
        return this.properties;
    }

    protected Section() {
    }

    public Section(byte[] src, int offset) throws UnsupportedEncodingException {
        int i;
        int o1 = offset;
        this.formatID = new ClassID(src, o1);
        this.offset = LittleEndian.getUInt(src, o1 + 16);
        o1 = (int) this.offset;
        this.size = (int) LittleEndian.getUInt(src, o1);
        o1 += 4;
        int propertyCount = (int) LittleEndian.getUInt(src, o1);
        o1 += 4;
        this.properties = new Property[propertyCount];
        int pass1Offset = o1;
        List<PropertyListEntry> arrayList = new ArrayList(propertyCount);
        for (i = 0; i < this.properties.length; i++) {
            PropertyListEntry ple = new PropertyListEntry();
            ple.id = (int) LittleEndian.getUInt(src, pass1Offset);
            pass1Offset += 4;
            ple.offset = (int) LittleEndian.getUInt(src, pass1Offset);
            pass1Offset += 4;
            arrayList.add(ple);
        }
        Collections.sort(arrayList);
        for (i = 0; i < propertyCount - 1; i++) {
            PropertyListEntry ple1 = (PropertyListEntry) arrayList.get(i);
            ple1.length = ((PropertyListEntry) arrayList.get(i + 1)).offset - ple1.offset;
        }
        if (propertyCount > 0) {
            ple = (PropertyListEntry) arrayList.get(propertyCount - 1);
            ple.length = this.size - ple.offset;
        }
        int codepage = -1;
        Iterator<PropertyListEntry> i2 = arrayList.iterator();
        while (codepage == -1 && i2.hasNext()) {
            ple = (PropertyListEntry) i2.next();
            if (ple.id == 1) {
                int o = (int) (this.offset + ((long) ple.offset));
                long type = LittleEndian.getUInt(src, o);
                o += 4;
                if (type != 2) {
                    throw new HPSFRuntimeException("Value type of property ID 1 is not VT_I2 but " + type + ".");
                }
                codepage = LittleEndian.getUShort(src, o);
            }
        }
        int i1 = 0;
        for (PropertyListEntry ple2 : arrayList) {
            Property p = new Property((long) ple2.id, src, this.offset + ((long) ple2.offset), ple2.length, codepage);
            if (p.getID() == 1) {
                p = new Property(p.getID(), p.getType(), Integer.valueOf(codepage));
            }
            int i12 = i1 + 1;
            this.properties[i1] = p;
            i1 = i12;
        }
        this.dictionary = (Map) getProperty(0);
    }

    public Object getProperty(long id) {
        this.wasNull = false;
        for (int i = 0; i < this.properties.length; i++) {
            if (id == this.properties[i].getID()) {
                return this.properties[i].getValue();
            }
        }
        this.wasNull = true;
        return null;
    }

    protected int getPropertyIntValue(long id) {
        Number o = getProperty(id);
        if (o == null) {
            return 0;
        }
        if ((o instanceof Long) || (o instanceof Integer)) {
            return o.intValue();
        }
        throw new HPSFRuntimeException("This property is not an integer type, but " + o.getClass().getName() + ".");
    }

    protected boolean getPropertyBooleanValue(int id) {
        Boolean b = (Boolean) getProperty((long) id);
        if (b == null) {
            return false;
        }
        return b.booleanValue();
    }

    public boolean wasNull() {
        return this.wasNull;
    }

    public String getPIDString(long pid) {
        String s = null;
        if (this.dictionary != null) {
            s = (String) this.dictionary.get(Long.valueOf(pid));
        }
        if (s == null) {
            return SectionIDMap.getPIDString(getFormatID().getBytes(), pid);
        }
        return s;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof Section)) {
            return false;
        }
        Section s = (Section) o;
        if (!s.getFormatID().equals(getFormatID())) {
            return false;
        }
        Object[] pa1 = new Property[getProperties().length];
        Object[] pa2 = new Property[s.getProperties().length];
        System.arraycopy(getProperties(), 0, pa1, 0, pa1.length);
        System.arraycopy(s.getProperties(), 0, pa2, 0, pa2.length);
        Property p10 = null;
        Property p20 = null;
        int i = 0;
        while (i < pa1.length) {
            long id = pa1[i].getID();
            if (id == 0) {
                p10 = pa1[i];
                pa1 = remove(pa1, i);
                i--;
            }
            if (id == 1) {
                pa1 = remove(pa1, i);
                i--;
            }
            i++;
        }
        i = 0;
        while (i < pa2.length) {
            id = pa2[i].getID();
            if (id == 0) {
                p20 = pa2[i];
                pa2 = remove(pa2, i);
                i--;
            }
            if (id == 1) {
                pa2 = remove(pa2, i);
                i--;
            }
            i++;
        }
        if (pa1.length != pa2.length) {
            return false;
        }
        boolean dictionaryEqual = true;
        if (p10 != null && p20 != null) {
            dictionaryEqual = p10.getValue().equals(p20.getValue());
        } else if (!(p10 == null && p20 == null)) {
            dictionaryEqual = false;
        }
        if (dictionaryEqual) {
            return Util.equals(pa1, pa2);
        }
        return false;
    }

    private Property[] remove(Property[] pa, int i) {
        Property[] h = new Property[(pa.length - 1)];
        if (i > 0) {
            System.arraycopy(pa, 0, h, 0, i);
        }
        System.arraycopy(pa, i + 1, h, i, h.length - i);
        return h;
    }

    public int hashCode() {
        long hashCode = 0 + ((long) getFormatID().hashCode());
        for (Property hashCode2 : getProperties()) {
            hashCode += (long) hashCode2.hashCode();
        }
        return (int) (4294967295L & hashCode);
    }

    public String toString() {
        StringBuffer b = new StringBuffer();
        Property[] pa = getProperties();
        b.append(getClass().getName());
        b.append('[');
        b.append("formatID: ");
        b.append(getFormatID());
        b.append(", offset: ");
        b.append(getOffset());
        b.append(", propertyCount: ");
        b.append(getPropertyCount());
        b.append(", size: ");
        b.append(getSize());
        b.append(", properties: [\n");
        for (Property property : pa) {
            b.append(property.toString());
            b.append(",\n");
        }
        b.append(']');
        b.append(']');
        return b.toString();
    }

    public Map<Long, String> getDictionary() {
        return this.dictionary;
    }

    public int getCodepage() {
        Integer codepage = (Integer) getProperty(1);
        if (codepage == null) {
            return -1;
        }
        return codepage.intValue();
    }
}

package org.apache.poi.ddf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndian;

public abstract class AbstractEscherOptRecord extends EscherRecord {
    protected List<EscherProperty> properties = new ArrayList();

    class C10471 implements Comparator<EscherProperty> {
        C10471() {
        }

        public int compare(EscherProperty p1, EscherProperty p2) {
            short s1 = p1.getPropertyNumber();
            short s2 = p2.getPropertyNumber();
            if (s1 < s2) {
                return -1;
            }
            return s1 == s2 ? 0 : 1;
        }
    }

    public void addEscherProperty(EscherProperty prop) {
        this.properties.add(prop);
    }

    public int fillFields(byte[] data, int offset, EscherRecordFactory recordFactory) {
        int bytesRemaining = readHeader(data, offset);
        int pos = offset + 8;
        this.properties = new EscherPropertyFactory().createProperties(data, pos, readInstance(data, offset));
        return bytesRemaining + 8;
    }

    public List<EscherProperty> getEscherProperties() {
        return this.properties;
    }

    public EscherProperty getEscherProperty(int index) {
        return (EscherProperty) this.properties.get(index);
    }

    private int getPropertiesSize() {
        int totalSize = 0;
        for (EscherProperty property : this.properties) {
            totalSize += property.getPropertySize();
        }
        return totalSize;
    }

    public int getRecordSize() {
        return getPropertiesSize() + 8;
    }

    public <T extends EscherProperty> T lookup(int propId) {
        for (T prop : this.properties) {
            if (prop.getPropertyNumber() == propId) {
                return prop;
            }
        }
        return null;
    }

    public int serialize(int offset, byte[] data, EscherSerializationListener listener) {
        listener.beforeRecordSerialize(offset, getRecordId(), this);
        LittleEndian.putShort(data, offset, getOptions());
        LittleEndian.putShort(data, offset + 2, getRecordId());
        LittleEndian.putInt(data, offset + 4, getPropertiesSize());
        int pos = offset + 8;
        for (EscherProperty property : this.properties) {
            pos += property.serializeSimplePart(data, pos);
        }
        for (EscherProperty property2 : this.properties) {
            pos += property2.serializeComplexPart(data, pos);
        }
        listener.afterRecordSerialize(pos, getRecordId(), pos - offset, this);
        return pos - offset;
    }

    public void sortProperties() {
        Collections.sort(this.properties, new C10471());
    }

    public void setEscherProperty(EscherProperty value) {
        Iterator<EscherProperty> iterator = this.properties.iterator();
        while (iterator.hasNext()) {
            if (((EscherProperty) iterator.next()).getId() == value.getId()) {
                iterator.remove();
            }
        }
        this.properties.add(value);
        sortProperties();
    }

    public void removeEscherProperty(int num) {
        Iterator<EscherProperty> iterator = getEscherProperties().iterator();
        while (iterator.hasNext()) {
            if (((EscherProperty) iterator.next()).getPropertyNumber() == num) {
                iterator.remove();
            }
        }
    }

    public String toString() {
        String nl = System.getProperty("line.separator");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getClass().getName());
        stringBuilder.append(":");
        stringBuilder.append(nl);
        stringBuilder.append("  isContainer: ");
        stringBuilder.append(isContainerRecord());
        stringBuilder.append(nl);
        stringBuilder.append("  version: 0x");
        stringBuilder.append(HexDump.toHex(getVersion()));
        stringBuilder.append(nl);
        stringBuilder.append("  instance: 0x");
        stringBuilder.append(HexDump.toHex(getInstance()));
        stringBuilder.append(nl);
        stringBuilder.append("  recordId: 0x");
        stringBuilder.append(HexDump.toHex(getRecordId()));
        stringBuilder.append(nl);
        stringBuilder.append("  numchildren: ");
        stringBuilder.append(getChildRecords().size());
        stringBuilder.append(nl);
        stringBuilder.append("  properties:");
        stringBuilder.append(nl);
        for (EscherProperty property : this.properties) {
            stringBuilder.append("    ").append(property.toString()).append(nl);
        }
        return stringBuilder.toString();
    }

    public String toXml(String tab) {
        StringBuilder builder = new StringBuilder();
        builder.append(tab).append(formatXmlRecordHeader(getClass().getSimpleName(), HexDump.toHex(getRecordId()), HexDump.toHex(getVersion()), HexDump.toHex(getInstance())));
        for (EscherProperty property : getEscherProperties()) {
            builder.append(property.toXml(tab + "\t"));
        }
        builder.append(tab).append("</").append(getClass().getSimpleName()).append(">\n");
        return builder.toString();
    }
}

package org.apache.poi.hpsf;

import java.io.IOException;
import java.io.OutputStream;

public class MutableProperty extends Property {
    public MutableProperty(Property p) {
        setID(p.getID());
        setType(p.getType());
        setValue(p.getValue());
    }

    public void setID(long id) {
        this.id = id;
    }

    public void setType(long type) {
        this.type = type;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public int write(OutputStream out, int codepage) throws IOException, WritingNotSupportedException {
        long variantType = getType();
        if (codepage == 1200 && variantType == 30) {
            variantType = 31;
        }
        return (0 + TypeWriter.writeUIntToStream(out, variantType)) + VariantSupport.write(out, variantType, getValue(), codepage);
    }
}

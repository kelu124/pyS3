package org.apache.poi.hpsf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.util.LittleEndian;

public class MutablePropertySet extends PropertySet {
    private static final int OFFSET_HEADER = ((((BYTE_ORDER_ASSERTION.length + FORMAT_ASSERTION.length) + 4) + 16) + 4);

    public MutablePropertySet() {
        this.byteOrder = LittleEndian.getUShort(BYTE_ORDER_ASSERTION);
        this.format = LittleEndian.getUShort(FORMAT_ASSERTION);
        this.osVersion = 133636;
        this.classID = new ClassID();
        this.sections = new LinkedList();
        this.sections.add(new MutableSection());
    }

    public MutablePropertySet(PropertySet ps) {
        this.byteOrder = ps.getByteOrder();
        this.format = ps.getFormat();
        this.osVersion = ps.getOSVersion();
        setClassID(ps.getClassID());
        clearSections();
        if (this.sections == null) {
            this.sections = new LinkedList();
        }
        for (Section section : ps.getSections()) {
            addSection(new MutableSection(section));
        }
    }

    public void setByteOrder(int byteOrder) {
        this.byteOrder = byteOrder;
    }

    public void setFormat(int format) {
        this.format = format;
    }

    public void setOSVersion(int osVersion) {
        this.osVersion = osVersion;
    }

    public void setClassID(ClassID classID) {
        this.classID = classID;
    }

    public void clearSections() {
        this.sections = null;
    }

    public void addSection(Section section) {
        if (this.sections == null) {
            this.sections = new LinkedList();
        }
        this.sections.add(section);
    }

    public void write(OutputStream out) throws WritingNotSupportedException, IOException {
        int nrSections = this.sections.size();
        TypeWriter.writeToStream(out, (short) getByteOrder());
        TypeWriter.writeToStream(out, (short) getFormat());
        TypeWriter.writeToStream(out, getOSVersion());
        TypeWriter.writeToStream(out, getClassID());
        TypeWriter.writeToStream(out, nrSections);
        int offset = OFFSET_HEADER + (nrSections * 20);
        int sectionsBegin = offset;
        for (Section section : this.sections) {
            MutableSection s = (MutableSection) section;
            if (s.getFormatID() == null) {
                throw new NoFormatIDException();
            }
            TypeWriter.writeToStream(out, s.getFormatID());
            TypeWriter.writeUIntToStream(out, (long) offset);
            try {
                offset += s.getSize();
            } catch (HPSFRuntimeException ex) {
                Throwable cause = ex.getReason();
                if (cause instanceof UnsupportedEncodingException) {
                    throw new IllegalPropertySetDataException(cause);
                }
                throw ex;
            }
        }
        offset = sectionsBegin;
        for (Section section2 : this.sections) {
            offset += ((MutableSection) section2).write(out);
        }
        out.close();
    }

    public InputStream toInputStream() throws IOException, WritingNotSupportedException {
        ByteArrayOutputStream psStream = new ByteArrayOutputStream();
        try {
            write(psStream);
            return new ByteArrayInputStream(psStream.toByteArray());
        } finally {
            psStream.close();
        }
    }

    public void write(DirectoryEntry dir, String name) throws WritingNotSupportedException, IOException {
        try {
            dir.getEntry(name).delete();
        } catch (FileNotFoundException e) {
        }
        dir.createDocument(name, toInputStream());
    }
}

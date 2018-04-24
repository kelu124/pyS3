package org.apache.poi.hssf.usermodel;

import org.apache.poi.ddf.DefaultEscherRecordFactory;
import org.apache.poi.ddf.EscherBSERecord;
import org.apache.poi.ddf.EscherContainerRecord;
import org.apache.poi.ddf.EscherOptRecord;
import org.apache.poi.ddf.EscherProperties;
import org.apache.poi.ddf.EscherSimpleProperty;
import org.apache.poi.hssf.record.CommonObjectDataSubRecord;
import org.apache.poi.hssf.record.EndSubRecord;
import org.apache.poi.hssf.record.NoteRecord;
import org.apache.poi.hssf.record.NoteStructureSubRecord;
import org.apache.poi.hssf.record.ObjRecord;
import org.apache.poi.hssf.record.TextObjectRecord;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.util.CellAddress;
import org.bytedeco.javacpp.dc1394;

public class HSSFComment extends HSSFTextbox implements Comment {
    private static final int FILL_TYPE_PICTURE = 3;
    private static final int FILL_TYPE_SOLID = 0;
    private static final int GROUP_SHAPE_HIDDEN_MASK = 16777218;
    private static final int GROUP_SHAPE_NOT_HIDDEN_MASK = -16777219;
    private static final int GROUP_SHAPE_PROPERTY_DEFAULT_VALUE = 655362;
    private final NoteRecord _note;

    public /* bridge */ /* synthetic */ RichTextString getString() {
        return super.getString();
    }

    public HSSFComment(EscherContainerRecord spContainer, ObjRecord objRecord, TextObjectRecord textObjectRecord, NoteRecord note) {
        super(spContainer, objRecord, textObjectRecord);
        this._note = note;
    }

    public HSSFComment(HSSFShape parent, HSSFAnchor anchor) {
        this(parent, anchor, createNoteRecord());
    }

    private HSSFComment(HSSFShape parent, HSSFAnchor anchor, NoteRecord note) {
        super(parent, anchor);
        this._note = note;
        setFillColor(134217808);
        setVisible(false);
        setAuthor("");
        ((CommonObjectDataSubRecord) getObjRecord().getSubRecords().get(0)).setObjectType((short) 25);
    }

    protected HSSFComment(NoteRecord note, TextObjectRecord txo) {
        this(null, new HSSFClientAnchor(), note);
    }

    void afterInsert(HSSFPatriarch patriarch) {
        super.afterInsert(patriarch);
        patriarch.getBoundAggregate().addTailRecord(getNoteRecord());
    }

    protected EscherContainerRecord createSpContainer() {
        EscherContainerRecord spContainer = super.createSpContainer();
        EscherOptRecord opt = (EscherOptRecord) spContainer.getChildById(EscherOptRecord.RECORD_ID);
        opt.removeEscherProperty(129);
        opt.removeEscherProperty(131);
        opt.removeEscherProperty(130);
        opt.removeEscherProperty(132);
        opt.setEscherProperty(new EscherSimpleProperty((short) 959, false, false, GROUP_SHAPE_PROPERTY_DEFAULT_VALUE));
        return spContainer;
    }

    protected ObjRecord createObjRecord() {
        ObjRecord obj = new ObjRecord();
        CommonObjectDataSubRecord c = new CommonObjectDataSubRecord();
        c.setObjectType((short) 202);
        c.setLocked(true);
        c.setPrintable(true);
        c.setAutofill(false);
        c.setAutoline(true);
        NoteStructureSubRecord u = new NoteStructureSubRecord();
        EndSubRecord e = new EndSubRecord();
        obj.addSubRecord(c);
        obj.addSubRecord(u);
        obj.addSubRecord(e);
        return obj;
    }

    private static NoteRecord createNoteRecord() {
        NoteRecord note = new NoteRecord();
        note.setFlags((short) 0);
        note.setAuthor("");
        return note;
    }

    void setShapeId(int shapeId) {
        if (shapeId > 65535) {
            throw new IllegalArgumentException("Cannot add more than 65535 shapes");
        }
        super.setShapeId(shapeId);
        ((CommonObjectDataSubRecord) getObjRecord().getSubRecords().get(0)).setObjectId(shapeId);
        this._note.setShapeId(shapeId);
    }

    public void setVisible(boolean visible) {
        short s;
        boolean z = false;
        NoteRecord noteRecord = this._note;
        if (visible) {
            s = (short) 2;
        } else {
            s = (short) 0;
        }
        noteRecord.setFlags(s);
        if (!visible) {
            z = true;
        }
        setHidden(z);
    }

    public boolean isVisible() {
        return this._note.getFlags() == (short) 2;
    }

    public CellAddress getAddress() {
        return new CellAddress(getRow(), getColumn());
    }

    public void setAddress(CellAddress address) {
        setRow(address.getRow());
        setColumn(address.getColumn());
    }

    public void setAddress(int row, int col) {
        setRow(row);
        setColumn(col);
    }

    public int getRow() {
        return this._note.getRow();
    }

    public void setRow(int row) {
        this._note.setRow(row);
    }

    public int getColumn() {
        return this._note.getColumn();
    }

    public void setColumn(int col) {
        this._note.setColumn(col);
    }

    public String getAuthor() {
        return this._note.getAuthor();
    }

    public void setAuthor(String author) {
        if (this._note != null) {
            this._note.setAuthor(author);
        }
    }

    protected NoteRecord getNoteRecord() {
        return this._note;
    }

    public boolean hasPosition() {
        if (this._note != null && getColumn() >= 0 && getRow() >= 0) {
            return true;
        }
        return false;
    }

    public ClientAnchor getClientAnchor() {
        HSSFAnchor ha = super.getAnchor();
        if (ha instanceof ClientAnchor) {
            return (ClientAnchor) ha;
        }
        throw new IllegalStateException("Anchor can not be changed in " + ClientAnchor.class.getSimpleName());
    }

    public void setShapeType(int shapeType) {
        throw new IllegalStateException("Shape type can not be changed in " + getClass().getSimpleName());
    }

    public void afterRemove(HSSFPatriarch patriarch) {
        super.afterRemove(patriarch);
        patriarch.getBoundAggregate().removeTailRecord(getNoteRecord());
    }

    protected HSSFShape cloneShape() {
        TextObjectRecord txo = (TextObjectRecord) getTextObjectRecord().cloneViaReserialise();
        EscherContainerRecord spContainer = new EscherContainerRecord();
        spContainer.fillFields(getEscherContainer().serialize(), 0, new DefaultEscherRecordFactory());
        return new HSSFComment(spContainer, (ObjRecord) getObjRecord().cloneViaReserialise(), txo, (NoteRecord) getNoteRecord().cloneViaReserialise());
    }

    public void setBackgroundImage(int pictureIndex) {
        setPropertyValue(new EscherSimpleProperty((short) 390, false, true, pictureIndex));
        setPropertyValue(new EscherSimpleProperty(EscherProperties.FILL__FILLTYPE, false, false, 3));
        EscherBSERecord bse = getPatriarch().getSheet().getWorkbook().getWorkbook().getBSERecord(pictureIndex);
        bse.setRef(bse.getRef() + 1);
    }

    public void resetBackgroundImage() {
        EscherSimpleProperty property = (EscherSimpleProperty) getOptRecord().lookup(dc1394.DC1394_TRIGGER_MODE_14);
        if (property != null) {
            EscherBSERecord bse = getPatriarch().getSheet().getWorkbook().getWorkbook().getBSERecord(property.getPropertyValue());
            bse.setRef(bse.getRef() - 1);
            getOptRecord().removeEscherProperty(dc1394.DC1394_TRIGGER_MODE_14);
        }
        setPropertyValue(new EscherSimpleProperty(EscherProperties.FILL__FILLTYPE, false, false, 0));
    }

    public int getBackgroundImageId() {
        EscherSimpleProperty property = (EscherSimpleProperty) getOptRecord().lookup(dc1394.DC1394_TRIGGER_MODE_14);
        return property == null ? 0 : property.getPropertyValue();
    }

    private void setHidden(boolean value) {
        EscherSimpleProperty property = (EscherSimpleProperty) getOptRecord().lookup(959);
        if (value) {
            setPropertyValue(new EscherSimpleProperty((short) 959, false, false, property.getPropertyValue() | GROUP_SHAPE_HIDDEN_MASK));
        } else {
            setPropertyValue(new EscherSimpleProperty((short) 959, false, false, property.getPropertyValue() & GROUP_SHAPE_NOT_HIDDEN_MASK));
        }
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof HSSFComment)) {
            return false;
        }
        return getNoteRecord().equals(((HSSFComment) obj).getNoteRecord());
    }

    public int hashCode() {
        return ((getRow() * 17) + getColumn()) * 31;
    }
}

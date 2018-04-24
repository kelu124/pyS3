package org.apache.poi.hssf.usermodel;

import org.apache.poi.ddf.DefaultEscherRecordFactory;
import org.apache.poi.ddf.EscherBoolProperty;
import org.apache.poi.ddf.EscherClientDataRecord;
import org.apache.poi.ddf.EscherContainerRecord;
import org.apache.poi.ddf.EscherOptRecord;
import org.apache.poi.ddf.EscherProperties;
import org.apache.poi.ddf.EscherRGBProperty;
import org.apache.poi.ddf.EscherRecord;
import org.apache.poi.ddf.EscherSimpleProperty;
import org.apache.poi.ddf.EscherSpRecord;
import org.apache.poi.ddf.EscherTextboxRecord;
import org.apache.poi.hssf.record.CommonObjectDataSubRecord;
import org.apache.poi.hssf.record.EndSubRecord;
import org.apache.poi.hssf.record.EscherAggregate;
import org.apache.poi.hssf.record.ObjRecord;
import org.apache.poi.hssf.record.TextObjectRecord;

public class HSSFTextbox extends HSSFSimpleShape {
    public static final short HORIZONTAL_ALIGNMENT_CENTERED = (short) 2;
    public static final short HORIZONTAL_ALIGNMENT_DISTRIBUTED = (short) 7;
    public static final short HORIZONTAL_ALIGNMENT_JUSTIFIED = (short) 4;
    public static final short HORIZONTAL_ALIGNMENT_LEFT = (short) 1;
    public static final short HORIZONTAL_ALIGNMENT_RIGHT = (short) 3;
    public static final short OBJECT_TYPE_TEXT = (short) 6;
    public static final short VERTICAL_ALIGNMENT_BOTTOM = (short) 3;
    public static final short VERTICAL_ALIGNMENT_CENTER = (short) 2;
    public static final short VERTICAL_ALIGNMENT_DISTRIBUTED = (short) 7;
    public static final short VERTICAL_ALIGNMENT_JUSTIFY = (short) 4;
    public static final short VERTICAL_ALIGNMENT_TOP = (short) 1;

    public HSSFTextbox(EscherContainerRecord spContainer, ObjRecord objRecord, TextObjectRecord textObjectRecord) {
        super(spContainer, objRecord, textObjectRecord);
    }

    public HSSFTextbox(HSSFShape parent, HSSFAnchor anchor) {
        super(parent, anchor);
        setHorizontalAlignment((short) 1);
        setVerticalAlignment((short) 1);
        setString(new HSSFRichTextString(""));
    }

    protected ObjRecord createObjRecord() {
        ObjRecord obj = new ObjRecord();
        CommonObjectDataSubRecord c = new CommonObjectDataSubRecord();
        c.setObjectType((short) 6);
        c.setLocked(true);
        c.setPrintable(true);
        c.setAutofill(true);
        c.setAutoline(true);
        EndSubRecord e = new EndSubRecord();
        obj.addSubRecord(c);
        obj.addSubRecord(e);
        return obj;
    }

    protected EscherContainerRecord createSpContainer() {
        EscherContainerRecord spContainer = new EscherContainerRecord();
        EscherSpRecord sp = new EscherSpRecord();
        EscherOptRecord opt = new EscherOptRecord();
        EscherClientDataRecord clientData = new EscherClientDataRecord();
        EscherTextboxRecord escherTextbox = new EscherTextboxRecord();
        spContainer.setRecordId(EscherContainerRecord.SP_CONTAINER);
        spContainer.setOptions((short) 15);
        sp.setRecordId(EscherSpRecord.RECORD_ID);
        sp.setOptions((short) 3234);
        sp.setFlags(2560);
        opt.setRecordId(EscherOptRecord.RECORD_ID);
        opt.addEscherProperty(new EscherSimpleProperty((short) 128, 0));
        opt.addEscherProperty(new EscherSimpleProperty((short) 133, 0));
        opt.addEscherProperty(new EscherSimpleProperty((short) 135, 0));
        opt.addEscherProperty(new EscherSimpleProperty((short) 959, 524288));
        opt.addEscherProperty(new EscherSimpleProperty((short) 129, 0));
        opt.addEscherProperty(new EscherSimpleProperty((short) 131, 0));
        opt.addEscherProperty(new EscherSimpleProperty((short) 130, 0));
        opt.addEscherProperty(new EscherSimpleProperty((short) 132, 0));
        opt.setEscherProperty(new EscherSimpleProperty(EscherProperties.LINESTYLE__LINEDASHING, 0));
        opt.setEscherProperty(new EscherBoolProperty(EscherProperties.LINESTYLE__NOLINEDRAWDASH, 524296));
        opt.setEscherProperty(new EscherSimpleProperty(EscherProperties.LINESTYLE__LINEWIDTH, 9525));
        opt.setEscherProperty(new EscherRGBProperty(EscherProperties.FILL__FILLCOLOR, HSSFShape.FILL__FILLCOLOR_DEFAULT));
        opt.setEscherProperty(new EscherRGBProperty(EscherProperties.LINESTYLE__COLOR, HSSFShape.LINESTYLE__COLOR_DEFAULT));
        opt.setEscherProperty(new EscherBoolProperty(EscherProperties.FILL__NOFILLHITTEST, 65536));
        opt.setEscherProperty(new EscherBoolProperty((short) 959, 524288));
        EscherRecord anchor = getAnchor().getEscherAnchor();
        clientData.setRecordId(EscherClientDataRecord.RECORD_ID);
        clientData.setOptions((short) 0);
        escherTextbox.setRecordId(EscherTextboxRecord.RECORD_ID);
        escherTextbox.setOptions((short) 0);
        spContainer.addChildRecord(sp);
        spContainer.addChildRecord(opt);
        spContainer.addChildRecord(anchor);
        spContainer.addChildRecord(clientData);
        spContainer.addChildRecord(escherTextbox);
        return spContainer;
    }

    void afterInsert(HSSFPatriarch patriarch) {
        EscherAggregate agg = patriarch.getBoundAggregate();
        agg.associateShapeToObjRecord(getEscherContainer().getChildById(EscherClientDataRecord.RECORD_ID), getObjRecord());
        if (getTextObjectRecord() != null) {
            agg.associateShapeToObjRecord(getEscherContainer().getChildById(EscherTextboxRecord.RECORD_ID), getTextObjectRecord());
        }
    }

    public int getMarginLeft() {
        EscherSimpleProperty property = (EscherSimpleProperty) getOptRecord().lookup(129);
        return property == null ? 0 : property.getPropertyValue();
    }

    public void setMarginLeft(int marginLeft) {
        setPropertyValue(new EscherSimpleProperty((short) 129, marginLeft));
    }

    public int getMarginRight() {
        EscherSimpleProperty property = (EscherSimpleProperty) getOptRecord().lookup(131);
        return property == null ? 0 : property.getPropertyValue();
    }

    public void setMarginRight(int marginRight) {
        setPropertyValue(new EscherSimpleProperty((short) 131, marginRight));
    }

    public int getMarginTop() {
        EscherSimpleProperty property = (EscherSimpleProperty) getOptRecord().lookup(130);
        return property == null ? 0 : property.getPropertyValue();
    }

    public void setMarginTop(int marginTop) {
        setPropertyValue(new EscherSimpleProperty((short) 130, marginTop));
    }

    public int getMarginBottom() {
        EscherSimpleProperty property = (EscherSimpleProperty) getOptRecord().lookup(132);
        return property == null ? 0 : property.getPropertyValue();
    }

    public void setMarginBottom(int marginBottom) {
        setPropertyValue(new EscherSimpleProperty((short) 132, marginBottom));
    }

    public short getHorizontalAlignment() {
        return (short) getTextObjectRecord().getHorizontalTextAlignment();
    }

    public void setHorizontalAlignment(short align) {
        getTextObjectRecord().setHorizontalTextAlignment(align);
    }

    public short getVerticalAlignment() {
        return (short) getTextObjectRecord().getVerticalTextAlignment();
    }

    public void setVerticalAlignment(short align) {
        getTextObjectRecord().setVerticalTextAlignment(align);
    }

    public void setShapeType(int shapeType) {
        throw new IllegalStateException("Shape type can not be changed in " + getClass().getSimpleName());
    }

    protected HSSFShape cloneShape() {
        TextObjectRecord txo = getTextObjectRecord() == null ? null : (TextObjectRecord) getTextObjectRecord().cloneViaReserialise();
        EscherContainerRecord spContainer = new EscherContainerRecord();
        spContainer.fillFields(getEscherContainer().serialize(), 0, new DefaultEscherRecordFactory());
        return new HSSFTextbox(spContainer, (ObjRecord) getObjRecord().cloneViaReserialise(), txo);
    }

    protected void afterRemove(HSSFPatriarch patriarch) {
        patriarch.getBoundAggregate().removeShapeToObjRecord(getEscherContainer().getChildById(EscherClientDataRecord.RECORD_ID));
        patriarch.getBoundAggregate().removeShapeToObjRecord(getEscherContainer().getChildById(EscherTextboxRecord.RECORD_ID));
    }
}

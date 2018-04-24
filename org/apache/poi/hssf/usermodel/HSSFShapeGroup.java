package org.apache.poi.hssf.usermodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ddf.DefaultEscherRecordFactory;
import org.apache.poi.ddf.EscherBoolProperty;
import org.apache.poi.ddf.EscherChildAnchorRecord;
import org.apache.poi.ddf.EscherClientAnchorRecord;
import org.apache.poi.ddf.EscherClientDataRecord;
import org.apache.poi.ddf.EscherContainerRecord;
import org.apache.poi.ddf.EscherOptRecord;
import org.apache.poi.ddf.EscherRecord;
import org.apache.poi.ddf.EscherSpRecord;
import org.apache.poi.ddf.EscherSpgrRecord;
import org.apache.poi.hssf.record.CommonObjectDataSubRecord;
import org.apache.poi.hssf.record.EndSubRecord;
import org.apache.poi.hssf.record.GroupMarkerSubRecord;
import org.apache.poi.hssf.record.ObjRecord;

public class HSSFShapeGroup extends HSSFShape implements HSSFShapeContainer {
    private EscherSpgrRecord _spgrRecord;
    private final List<HSSFShape> shapes;

    public HSSFShapeGroup(EscherContainerRecord spgrContainer, ObjRecord objRecord) {
        super(spgrContainer, objRecord);
        this.shapes = new ArrayList();
        EscherContainerRecord spContainer = (EscherContainerRecord) spgrContainer.getChildContainers().get(0);
        this._spgrRecord = (EscherSpgrRecord) spContainer.getChild(0);
        for (EscherRecord ch : spContainer.getChildRecords()) {
            switch (ch.getRecordId()) {
                case (short) -4087:
                    break;
                case (short) -4081:
                    this.anchor = new HSSFChildAnchor((EscherChildAnchorRecord) ch);
                    break;
                case (short) -4080:
                    this.anchor = new HSSFClientAnchor((EscherClientAnchorRecord) ch);
                    break;
                default:
                    break;
            }
        }
    }

    public HSSFShapeGroup(HSSFShape parent, HSSFAnchor anchor) {
        super(parent, anchor);
        this.shapes = new ArrayList();
        this._spgrRecord = (EscherSpgrRecord) ((EscherContainerRecord) getEscherContainer().getChild(0)).getChildById(EscherSpgrRecord.RECORD_ID);
    }

    protected EscherContainerRecord createSpContainer() {
        EscherContainerRecord spgrContainer = new EscherContainerRecord();
        EscherContainerRecord spContainer = new EscherContainerRecord();
        EscherSpgrRecord spgr = new EscherSpgrRecord();
        EscherSpRecord sp = new EscherSpRecord();
        EscherOptRecord opt = new EscherOptRecord();
        EscherClientDataRecord clientData = new EscherClientDataRecord();
        spgrContainer.setRecordId(EscherContainerRecord.SPGR_CONTAINER);
        spgrContainer.setOptions((short) 15);
        spContainer.setRecordId(EscherContainerRecord.SP_CONTAINER);
        spContainer.setOptions((short) 15);
        spgr.setRecordId(EscherSpgrRecord.RECORD_ID);
        spgr.setOptions((short) 1);
        spgr.setRectX1(0);
        spgr.setRectY1(0);
        spgr.setRectX2(IEEEDouble.EXPONENT_BIAS);
        spgr.setRectY2(255);
        sp.setRecordId(EscherSpRecord.RECORD_ID);
        sp.setOptions((short) 2);
        if (getAnchor() instanceof HSSFClientAnchor) {
            sp.setFlags(513);
        } else {
            sp.setFlags(515);
        }
        opt.setRecordId(EscherOptRecord.RECORD_ID);
        opt.setOptions((short) 35);
        opt.addEscherProperty(new EscherBoolProperty((short) 127, 262148));
        opt.addEscherProperty(new EscherBoolProperty((short) 959, 524288));
        EscherRecord anchor = getAnchor().getEscherAnchor();
        clientData.setRecordId(EscherClientDataRecord.RECORD_ID);
        clientData.setOptions((short) 0);
        spgrContainer.addChildRecord(spContainer);
        spContainer.addChildRecord(spgr);
        spContainer.addChildRecord(sp);
        spContainer.addChildRecord(opt);
        spContainer.addChildRecord(anchor);
        spContainer.addChildRecord(clientData);
        return spgrContainer;
    }

    protected ObjRecord createObjRecord() {
        ObjRecord obj = new ObjRecord();
        CommonObjectDataSubRecord cmo = new CommonObjectDataSubRecord();
        cmo.setObjectType((short) 0);
        cmo.setLocked(true);
        cmo.setPrintable(true);
        cmo.setAutofill(true);
        cmo.setAutoline(true);
        GroupMarkerSubRecord gmo = new GroupMarkerSubRecord();
        EndSubRecord end = new EndSubRecord();
        obj.addSubRecord(cmo);
        obj.addSubRecord(gmo);
        obj.addSubRecord(end);
        return obj;
    }

    protected void afterRemove(HSSFPatriarch patriarch) {
        patriarch.getBoundAggregate().removeShapeToObjRecord(((EscherContainerRecord) getEscherContainer().getChildContainers().get(0)).getChildById(EscherClientDataRecord.RECORD_ID));
        for (int i = 0; i < this.shapes.size(); i++) {
            HSSFShape shape = (HSSFShape) this.shapes.get(i);
            removeShape(shape);
            shape.afterRemove(getPatriarch());
        }
        this.shapes.clear();
    }

    private void onCreate(HSSFShape shape) {
        if (getPatriarch() != null) {
            EscherSpRecord sp;
            EscherContainerRecord spContainer = shape.getEscherContainer();
            shape.setShapeId(getPatriarch().newShapeId());
            getEscherContainer().addChildRecord(spContainer);
            shape.afterInsert(getPatriarch());
            if (shape instanceof HSSFShapeGroup) {
                sp = (EscherSpRecord) ((EscherContainerRecord) shape.getEscherContainer().getChildContainers().get(0)).getChildById(EscherSpRecord.RECORD_ID);
            } else {
                sp = (EscherSpRecord) shape.getEscherContainer().getChildById(EscherSpRecord.RECORD_ID);
            }
            sp.setFlags(sp.getFlags() | 2);
        }
    }

    public HSSFShapeGroup createGroup(HSSFChildAnchor anchor) {
        HSSFShapeGroup group = new HSSFShapeGroup((HSSFShape) this, (HSSFAnchor) anchor);
        group.setParent(this);
        group.setAnchor(anchor);
        this.shapes.add(group);
        onCreate(group);
        return group;
    }

    public void addShape(HSSFShape shape) {
        shape.setPatriarch(getPatriarch());
        shape.setParent(this);
        this.shapes.add(shape);
    }

    public HSSFSimpleShape createShape(HSSFChildAnchor anchor) {
        HSSFSimpleShape shape = new HSSFSimpleShape((HSSFShape) this, (HSSFAnchor) anchor);
        shape.setParent(this);
        shape.setAnchor(anchor);
        this.shapes.add(shape);
        onCreate(shape);
        EscherSpRecord sp = (EscherSpRecord) shape.getEscherContainer().getChildById(EscherSpRecord.RECORD_ID);
        if (shape.getAnchor().isHorizontallyFlipped()) {
            sp.setFlags(sp.getFlags() | 64);
        }
        if (shape.getAnchor().isVerticallyFlipped()) {
            sp.setFlags(sp.getFlags() | 128);
        }
        return shape;
    }

    public HSSFTextbox createTextbox(HSSFChildAnchor anchor) {
        HSSFTextbox shape = new HSSFTextbox(this, anchor);
        shape.setParent(this);
        shape.setAnchor(anchor);
        this.shapes.add(shape);
        onCreate(shape);
        return shape;
    }

    public HSSFPolygon createPolygon(HSSFChildAnchor anchor) {
        HSSFPolygon shape = new HSSFPolygon((HSSFShape) this, (HSSFAnchor) anchor);
        shape.setParent(this);
        shape.setAnchor(anchor);
        this.shapes.add(shape);
        onCreate(shape);
        return shape;
    }

    public HSSFPicture createPicture(HSSFChildAnchor anchor, int pictureIndex) {
        HSSFPicture shape = new HSSFPicture((HSSFShape) this, (HSSFAnchor) anchor);
        shape.setParent(this);
        shape.setAnchor(anchor);
        shape.setPictureIndex(pictureIndex);
        this.shapes.add(shape);
        onCreate(shape);
        EscherSpRecord sp = (EscherSpRecord) shape.getEscherContainer().getChildById(EscherSpRecord.RECORD_ID);
        if (shape.getAnchor().isHorizontallyFlipped()) {
            sp.setFlags(sp.getFlags() | 64);
        }
        if (shape.getAnchor().isVerticallyFlipped()) {
            sp.setFlags(sp.getFlags() | 128);
        }
        return shape;
    }

    public List<HSSFShape> getChildren() {
        return Collections.unmodifiableList(this.shapes);
    }

    public void setCoordinates(int x1, int y1, int x2, int y2) {
        this._spgrRecord.setRectX1(x1);
        this._spgrRecord.setRectX2(x2);
        this._spgrRecord.setRectY1(y1);
        this._spgrRecord.setRectY2(y2);
    }

    public void clear() {
        Iterator i$ = new ArrayList(this.shapes).iterator();
        while (i$.hasNext()) {
            removeShape((HSSFShape) i$.next());
        }
    }

    public int getX1() {
        return this._spgrRecord.getRectX1();
    }

    public int getY1() {
        return this._spgrRecord.getRectY1();
    }

    public int getX2() {
        return this._spgrRecord.getRectX2();
    }

    public int getY2() {
        return this._spgrRecord.getRectY2();
    }

    public int countOfAllChildren() {
        int count = this.shapes.size();
        for (HSSFShape shape : this.shapes) {
            count += shape.countOfAllChildren();
        }
        return count;
    }

    void afterInsert(HSSFPatriarch patriarch) {
        patriarch.getBoundAggregate().associateShapeToObjRecord(((EscherContainerRecord) getEscherContainer().getChildById(EscherContainerRecord.SP_CONTAINER)).getChildById(EscherClientDataRecord.RECORD_ID), getObjRecord());
    }

    void setShapeId(int shapeId) {
        ((EscherSpRecord) ((EscherContainerRecord) getEscherContainer().getChildById(EscherContainerRecord.SP_CONTAINER)).getChildById(EscherSpRecord.RECORD_ID)).setShapeId(shapeId);
        ((CommonObjectDataSubRecord) getObjRecord().getSubRecords().get(0)).setObjectId((short) (shapeId % 1024));
    }

    int getShapeId() {
        return ((EscherSpRecord) ((EscherContainerRecord) getEscherContainer().getChildById(EscherContainerRecord.SP_CONTAINER)).getChildById(EscherSpRecord.RECORD_ID)).getShapeId();
    }

    protected HSSFShape cloneShape() {
        throw new IllegalStateException("Use method cloneShape(HSSFPatriarch patriarch)");
    }

    protected HSSFShape cloneShape(HSSFPatriarch patriarch) {
        EscherContainerRecord spgrContainer = new EscherContainerRecord();
        spgrContainer.setRecordId(EscherContainerRecord.SPGR_CONTAINER);
        spgrContainer.setOptions((short) 15);
        EscherContainerRecord spContainer = new EscherContainerRecord();
        spContainer.fillFields(((EscherContainerRecord) getEscherContainer().getChildById(EscherContainerRecord.SP_CONTAINER)).serialize(), 0, new DefaultEscherRecordFactory());
        spgrContainer.addChildRecord(spContainer);
        ObjRecord obj = null;
        if (getObjRecord() != null) {
            obj = (ObjRecord) getObjRecord().cloneViaReserialise();
        }
        HSSFShapeGroup group = new HSSFShapeGroup(spgrContainer, obj);
        group.setPatriarch(patriarch);
        for (HSSFShape shape : getChildren()) {
            HSSFShape newShape;
            if (shape instanceof HSSFShapeGroup) {
                newShape = ((HSSFShapeGroup) shape).cloneShape(patriarch);
            } else {
                newShape = shape.cloneShape();
            }
            group.addShape(newShape);
            group.onCreate(newShape);
        }
        return group;
    }

    public boolean removeShape(HSSFShape shape) {
        boolean isRemoved = getEscherContainer().removeChildRecord(shape.getEscherContainer());
        if (isRemoved) {
            shape.afterRemove(getPatriarch());
            this.shapes.remove(shape);
        }
        return isRemoved;
    }

    public Iterator<HSSFShape> iterator() {
        return this.shapes.iterator();
    }
}

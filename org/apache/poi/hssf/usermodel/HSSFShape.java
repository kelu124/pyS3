package org.apache.poi.hssf.usermodel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.poi.ddf.EscherBoolProperty;
import org.apache.poi.ddf.EscherChildAnchorRecord;
import org.apache.poi.ddf.EscherClientAnchorRecord;
import org.apache.poi.ddf.EscherContainerRecord;
import org.apache.poi.ddf.EscherOptRecord;
import org.apache.poi.ddf.EscherProperties;
import org.apache.poi.ddf.EscherProperty;
import org.apache.poi.ddf.EscherRGBProperty;
import org.apache.poi.ddf.EscherSimpleProperty;
import org.apache.poi.ddf.EscherSpRecord;
import org.apache.poi.hssf.record.CommonObjectDataSubRecord;
import org.apache.poi.hssf.record.ObjRecord;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;
import org.bytedeco.javacpp.dc1394;
import org.bytedeco.javacpp.opencv_videoio;

public abstract class HSSFShape {
    public static final int FILL__FILLCOLOR_DEFAULT = 134217737;
    public static final int LINESTYLE_DASHDOTDOTSYS = 4;
    public static final int LINESTYLE_DASHDOTGEL = 8;
    public static final int LINESTYLE_DASHDOTSYS = 3;
    public static final int LINESTYLE_DASHGEL = 6;
    public static final int LINESTYLE_DASHSYS = 1;
    public static final int LINESTYLE_DEFAULT = -1;
    public static final int LINESTYLE_DOTGEL = 5;
    public static final int LINESTYLE_DOTSYS = 2;
    public static final int LINESTYLE_LONGDASHDOTDOTGEL = 10;
    public static final int LINESTYLE_LONGDASHDOTGEL = 9;
    public static final int LINESTYLE_LONGDASHGEL = 7;
    public static final int LINESTYLE_NONE = -1;
    public static final int LINESTYLE_SOLID = 0;
    public static final int LINESTYLE__COLOR_DEFAULT = 134217792;
    public static final int LINEWIDTH_DEFAULT = 9525;
    public static final int LINEWIDTH_ONE_PT = 12700;
    private static final POILogger LOG = POILogFactory.getLogger(HSSFShape.class);
    public static final int NO_FILLHITTEST_FALSE = 65536;
    public static final int NO_FILLHITTEST_TRUE = 1114112;
    public static final boolean NO_FILL_DEFAULT = true;
    private final EscherContainerRecord _escherContainer;
    private final ObjRecord _objRecord;
    private final EscherOptRecord _optRecord;
    private HSSFPatriarch _patriarch;
    HSSFAnchor anchor;
    private HSSFShape parent;

    abstract void afterInsert(HSSFPatriarch hSSFPatriarch);

    protected abstract void afterRemove(HSSFPatriarch hSSFPatriarch);

    protected abstract HSSFShape cloneShape();

    protected abstract ObjRecord createObjRecord();

    protected abstract EscherContainerRecord createSpContainer();

    public HSSFShape(EscherContainerRecord spContainer, ObjRecord objRecord) {
        this._escherContainer = spContainer;
        this._objRecord = objRecord;
        this._optRecord = (EscherOptRecord) spContainer.getChildById(EscherOptRecord.RECORD_ID);
        this.anchor = HSSFAnchor.createAnchorFromEscher(spContainer);
    }

    public HSSFShape(HSSFShape parent, HSSFAnchor anchor) {
        this.parent = parent;
        this.anchor = anchor;
        this._escherContainer = createSpContainer();
        this._optRecord = (EscherOptRecord) this._escherContainer.getChildById(EscherOptRecord.RECORD_ID);
        this._objRecord = createObjRecord();
    }

    void setShapeId(int shapeId) {
        ((EscherSpRecord) this._escherContainer.getChildById(EscherSpRecord.RECORD_ID)).setShapeId(shapeId);
        ((CommonObjectDataSubRecord) this._objRecord.getSubRecords().get(0)).setObjectId((short) (shapeId % 1024));
    }

    int getShapeId() {
        return ((EscherSpRecord) this._escherContainer.getChildById(EscherSpRecord.RECORD_ID)).getShapeId();
    }

    protected EscherContainerRecord getEscherContainer() {
        return this._escherContainer;
    }

    protected ObjRecord getObjRecord() {
        return this._objRecord;
    }

    public EscherOptRecord getOptRecord() {
        return this._optRecord;
    }

    public HSSFShape getParent() {
        return this.parent;
    }

    public HSSFAnchor getAnchor() {
        return this.anchor;
    }

    public void setAnchor(HSSFAnchor anchor) {
        int recordId = -1;
        int i;
        if (this.parent == null) {
            if (anchor instanceof HSSFChildAnchor) {
                throw new IllegalArgumentException("Must use client anchors for shapes directly attached to sheet.");
            }
            EscherClientAnchorRecord anch = (EscherClientAnchorRecord) this._escherContainer.getChildById(EscherClientAnchorRecord.RECORD_ID);
            if (anch != null) {
                i = 0;
                while (i < this._escherContainer.getChildRecords().size()) {
                    if (this._escherContainer.getChild(i).getRecordId() == EscherClientAnchorRecord.RECORD_ID && i != this._escherContainer.getChildRecords().size() - 1) {
                        recordId = this._escherContainer.getChild(i + 1).getRecordId();
                    }
                    i++;
                }
                this._escherContainer.removeChildRecord(anch);
            }
        } else if (anchor instanceof HSSFClientAnchor) {
            throw new IllegalArgumentException("Must use child anchors for shapes attached to groups.");
        } else {
            EscherChildAnchorRecord anch2 = (EscherChildAnchorRecord) this._escherContainer.getChildById(EscherChildAnchorRecord.RECORD_ID);
            if (anch2 != null) {
                i = 0;
                while (i < this._escherContainer.getChildRecords().size()) {
                    if (this._escherContainer.getChild(i).getRecordId() == EscherChildAnchorRecord.RECORD_ID && i != this._escherContainer.getChildRecords().size() - 1) {
                        recordId = this._escherContainer.getChild(i + 1).getRecordId();
                    }
                    i++;
                }
                this._escherContainer.removeChildRecord(anch2);
            }
        }
        if (-1 == recordId) {
            this._escherContainer.addChildRecord(anchor.getEscherAnchor());
        } else {
            this._escherContainer.addChildBefore(anchor.getEscherAnchor(), recordId);
        }
        this.anchor = anchor;
    }

    public int getLineStyleColor() {
        EscherRGBProperty rgbProperty = (EscherRGBProperty) this._optRecord.lookup(opencv_videoio.CV_CAP_PROP_XI_WB_KR);
        return rgbProperty == null ? LINESTYLE__COLOR_DEFAULT : rgbProperty.getRgbColor();
    }

    public void setLineStyleColor(int lineStyleColor) {
        setPropertyValue(new EscherRGBProperty(EscherProperties.LINESTYLE__COLOR, lineStyleColor));
    }

    public void setLineStyleColor(int red, int green, int blue) {
        setPropertyValue(new EscherRGBProperty(EscherProperties.LINESTYLE__COLOR, ((blue << 16) | (green << 8)) | red));
    }

    public int getFillColor() {
        EscherRGBProperty rgbProperty = (EscherRGBProperty) this._optRecord.lookup(dc1394.DC1394_TRIGGER_MODE_1);
        return rgbProperty == null ? FILL__FILLCOLOR_DEFAULT : rgbProperty.getRgbColor();
    }

    public void setFillColor(int fillColor) {
        setPropertyValue(new EscherRGBProperty(EscherProperties.FILL__FILLCOLOR, fillColor));
    }

    public void setFillColor(int red, int green, int blue) {
        setPropertyValue(new EscherRGBProperty(EscherProperties.FILL__FILLCOLOR, ((blue << 16) | (green << 8)) | red));
    }

    public int getLineWidth() {
        EscherSimpleProperty property = (EscherSimpleProperty) this._optRecord.lookup(opencv_videoio.CV_CAP_PROP_XI_LIMIT_BANDWIDTH);
        return property == null ? 9525 : property.getPropertyValue();
    }

    public void setLineWidth(int lineWidth) {
        setPropertyValue(new EscherSimpleProperty(EscherProperties.LINESTYLE__LINEWIDTH, lineWidth));
    }

    public int getLineStyle() {
        EscherSimpleProperty property = (EscherSimpleProperty) this._optRecord.lookup(opencv_videoio.CV_CAP_PROP_XI_IMAGE_DATA_BIT_DEPTH);
        if (property == null) {
            return -1;
        }
        return property.getPropertyValue();
    }

    public void setLineStyle(int lineStyle) {
        setPropertyValue(new EscherSimpleProperty(EscherProperties.LINESTYLE__LINEDASHING, lineStyle));
        if (getLineStyle() != 0) {
            setPropertyValue(new EscherSimpleProperty(EscherProperties.LINESTYLE__LINEENDCAPSTYLE, 0));
            if (getLineStyle() == -1) {
                setPropertyValue(new EscherBoolProperty(EscherProperties.LINESTYLE__NOLINEDRAWDASH, 524288));
            } else {
                setPropertyValue(new EscherBoolProperty(EscherProperties.LINESTYLE__NOLINEDRAWDASH, 524296));
            }
        }
    }

    public boolean isNoFill() {
        EscherBoolProperty property = (EscherBoolProperty) this._optRecord.lookup(447);
        if (property == null || property.getPropertyValue() == NO_FILLHITTEST_TRUE) {
            return true;
        }
        return false;
    }

    public void setNoFill(boolean noFill) {
        setPropertyValue(new EscherBoolProperty(EscherProperties.FILL__NOFILLHITTEST, noFill ? NO_FILLHITTEST_TRUE : 65536));
    }

    protected void setPropertyValue(EscherProperty property) {
        this._optRecord.setEscherProperty(property);
    }

    public void setFlipVertical(boolean value) {
        EscherSpRecord sp = (EscherSpRecord) getEscherContainer().getChildById(EscherSpRecord.RECORD_ID);
        if (value) {
            sp.setFlags(sp.getFlags() | 128);
        } else {
            sp.setFlags(sp.getFlags() & 2147483519);
        }
    }

    public void setFlipHorizontal(boolean value) {
        EscherSpRecord sp = (EscherSpRecord) getEscherContainer().getChildById(EscherSpRecord.RECORD_ID);
        if (value) {
            sp.setFlags(sp.getFlags() | 64);
        } else {
            sp.setFlags(sp.getFlags() & 2147483583);
        }
    }

    public boolean isFlipVertical() {
        return (((EscherSpRecord) getEscherContainer().getChildById(EscherSpRecord.RECORD_ID)).getFlags() & 128) != 0;
    }

    public boolean isFlipHorizontal() {
        return (((EscherSpRecord) getEscherContainer().getChildById(EscherSpRecord.RECORD_ID)).getFlags() & 64) != 0;
    }

    public int getRotationDegree() {
        int i = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        EscherSimpleProperty property = (EscherSimpleProperty) getOptRecord().lookup(4);
        if (property != null) {
            try {
                LittleEndian.putInt(property.getPropertyValue(), bos);
                i = LittleEndian.getShort(bos.toByteArray(), 2);
            } catch (IOException e) {
                LOG.log(7, new Object[]{"can't determine rotation degree", e});
            }
        }
        return i;
    }

    public void setRotationDegree(short value) {
        setPropertyValue(new EscherSimpleProperty((short) 4, value << 16));
    }

    public int countOfAllChildren() {
        return 1;
    }

    protected void setPatriarch(HSSFPatriarch _patriarch) {
        this._patriarch = _patriarch;
    }

    public HSSFPatriarch getPatriarch() {
        return this._patriarch;
    }

    protected void setParent(HSSFShape parent) {
        this.parent = parent;
    }
}

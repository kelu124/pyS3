package org.apache.poi.hssf.usermodel;

import com.itextpdf.text.pdf.codec.wmf.MetaDo;
import java.awt.Dimension;
import java.io.ByteArrayInputStream;
import org.apache.poi.ddf.DefaultEscherRecordFactory;
import org.apache.poi.ddf.EscherBSERecord;
import org.apache.poi.ddf.EscherClientDataRecord;
import org.apache.poi.ddf.EscherComplexProperty;
import org.apache.poi.ddf.EscherContainerRecord;
import org.apache.poi.ddf.EscherOptRecord;
import org.apache.poi.ddf.EscherProperties;
import org.apache.poi.ddf.EscherSimpleProperty;
import org.apache.poi.ddf.EscherTextboxRecord;
import org.apache.poi.hssf.record.CommonObjectDataSubRecord;
import org.apache.poi.hssf.record.ObjRecord;
import org.apache.poi.ss.usermodel.ClientAnchor.AnchorType;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.util.ImageUtils;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;
import org.apache.poi.util.StringUtil;
import org.bytedeco.javacpp.opencv_videoio;

public class HSSFPicture extends HSSFSimpleShape implements Picture {
    public static final int PICTURE_TYPE_DIB = 7;
    public static final int PICTURE_TYPE_EMF = 2;
    public static final int PICTURE_TYPE_JPEG = 5;
    public static final int PICTURE_TYPE_PICT = 4;
    public static final int PICTURE_TYPE_PNG = 6;
    public static final int PICTURE_TYPE_WMF = 3;
    private static POILogger logger = POILogFactory.getLogger(HSSFPicture.class);

    public HSSFPicture(EscherContainerRecord spContainer, ObjRecord objRecord) {
        super(spContainer, objRecord);
    }

    public HSSFPicture(HSSFShape parent, HSSFAnchor anchor) {
        super(parent, anchor);
        super.setShapeType(75);
        ((CommonObjectDataSubRecord) getObjRecord().getSubRecords().get(0)).setObjectType((short) 8);
    }

    public int getPictureIndex() {
        EscherSimpleProperty property = (EscherSimpleProperty) getOptRecord().lookup(MetaDo.META_SETROP2);
        if (property == null) {
            return -1;
        }
        return property.getPropertyValue();
    }

    public void setPictureIndex(int pictureIndex) {
        setPropertyValue(new EscherSimpleProperty((short) 260, false, true, pictureIndex));
    }

    protected EscherContainerRecord createSpContainer() {
        EscherContainerRecord spContainer = super.createSpContainer();
        EscherOptRecord opt = (EscherOptRecord) spContainer.getChildById(EscherOptRecord.RECORD_ID);
        opt.removeEscherProperty(opencv_videoio.CV_CAP_PROP_XI_IMAGE_DATA_BIT_DEPTH);
        opt.removeEscherProperty(511);
        spContainer.removeChildRecord(spContainer.getChildById(EscherTextboxRecord.RECORD_ID));
        return spContainer;
    }

    public void resize() {
        resize(Double.MAX_VALUE);
    }

    public void resize(double scale) {
        resize(scale, scale);
    }

    public void resize(double scaleX, double scaleY) {
        HSSFClientAnchor anchor = getClientAnchor();
        anchor.setAnchorType(AnchorType.MOVE_DONT_RESIZE);
        HSSFClientAnchor pref = getPreferredSize(scaleX, scaleY);
        int row2 = anchor.getRow1() + (pref.getRow2() - pref.getRow1());
        anchor.setCol2((short) (anchor.getCol1() + (pref.getCol2() - pref.getCol1())));
        anchor.setDx2(pref.getDx2());
        anchor.setRow2(row2);
        anchor.setDy2(pref.getDy2());
    }

    public HSSFClientAnchor getPreferredSize() {
        return getPreferredSize(1.0d);
    }

    public HSSFClientAnchor getPreferredSize(double scale) {
        return getPreferredSize(scale, scale);
    }

    public HSSFClientAnchor getPreferredSize(double scaleX, double scaleY) {
        ImageUtils.setPreferredSize(this, scaleX, scaleY);
        return getClientAnchor();
    }

    public Dimension getImageDimension() {
        EscherBSERecord bse = getPatriarch().getSheet().getWorkbook().getWorkbook().getBSERecord(getPictureIndex());
        byte[] data = bse.getBlipRecord().getPicturedata();
        return ImageUtils.getImageDimension(new ByteArrayInputStream(data), bse.getBlipTypeWin32());
    }

    public HSSFPictureData getPictureData() {
        return new HSSFPictureData(getPatriarch().getSheet().getWorkbook().getWorkbook().getBSERecord(getPictureIndex()).getBlipRecord());
    }

    void afterInsert(HSSFPatriarch patriarch) {
        patriarch.getBoundAggregate().associateShapeToObjRecord(getEscherContainer().getChildById(EscherClientDataRecord.RECORD_ID), getObjRecord());
        if (getPictureIndex() != -1) {
            EscherBSERecord bse = patriarch.getSheet().getWorkbook().getWorkbook().getBSERecord(getPictureIndex());
            bse.setRef(bse.getRef() + 1);
        }
    }

    public String getFileName() {
        EscherComplexProperty propFile = (EscherComplexProperty) getOptRecord().lookup(MetaDo.META_SETRELABS);
        return propFile == null ? "" : StringUtil.getFromUnicodeLE(propFile.getComplexData()).trim();
    }

    public void setFileName(String data) {
        setPropertyValue(new EscherComplexProperty(EscherProperties.BLIP__BLIPFILENAME, true, StringUtil.getToUnicodeLE(data)));
    }

    public void setShapeType(int shapeType) {
        throw new IllegalStateException("Shape type can not be changed in " + getClass().getSimpleName());
    }

    protected HSSFShape cloneShape() {
        EscherContainerRecord spContainer = new EscherContainerRecord();
        spContainer.fillFields(getEscherContainer().serialize(), 0, new DefaultEscherRecordFactory());
        return new HSSFPicture(spContainer, (ObjRecord) getObjRecord().cloneViaReserialise());
    }

    public HSSFClientAnchor getClientAnchor() {
        HSSFAnchor a = getAnchor();
        return a instanceof HSSFClientAnchor ? (HSSFClientAnchor) a : null;
    }

    public HSSFSheet getSheet() {
        return getPatriarch().getSheet();
    }
}

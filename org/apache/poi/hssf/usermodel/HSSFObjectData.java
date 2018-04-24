package org.apache.poi.hssf.usermodel;

import java.io.IOException;
import org.apache.poi.ddf.DefaultEscherRecordFactory;
import org.apache.poi.ddf.EscherBSERecord;
import org.apache.poi.ddf.EscherClientDataRecord;
import org.apache.poi.ddf.EscherContainerRecord;
import org.apache.poi.hssf.record.EmbeddedObjectRefSubRecord;
import org.apache.poi.hssf.record.ObjRecord;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.Entry;
import org.apache.poi.util.HexDump;

public final class HSSFObjectData extends HSSFPicture {
    private final DirectoryEntry _root;

    public HSSFObjectData(EscherContainerRecord spContainer, ObjRecord objRecord, DirectoryEntry _root) {
        super(spContainer, objRecord);
        this._root = _root;
    }

    public String getOLE2ClassName() {
        return findObjectRecord().getOLEClassName();
    }

    public DirectoryEntry getDirectory() throws IOException {
        String streamName = "MBD" + HexDump.toHex(findObjectRecord().getStreamId().intValue());
        Entry entry = this._root.getEntry(streamName);
        if (entry instanceof DirectoryEntry) {
            return (DirectoryEntry) entry;
        }
        throw new IOException("Stream " + streamName + " was not an OLE2 directory");
    }

    public byte[] getObjectData() {
        return findObjectRecord().getObjectData();
    }

    public boolean hasDirectoryEntry() {
        Integer streamId = findObjectRecord().getStreamId();
        return (streamId == null || streamId.intValue() == 0) ? false : true;
    }

    protected EmbeddedObjectRefSubRecord findObjectRecord() {
        for (Object subRecord : getObjRecord().getSubRecords()) {
            if (subRecord instanceof EmbeddedObjectRefSubRecord) {
                return (EmbeddedObjectRefSubRecord) subRecord;
            }
        }
        throw new IllegalStateException("Object data does not contain a reference to an embedded object OLE2 directory");
    }

    protected EscherContainerRecord createSpContainer() {
        throw new IllegalStateException("HSSFObjectData cannot be created from scratch");
    }

    protected ObjRecord createObjRecord() {
        throw new IllegalStateException("HSSFObjectData cannot be created from scratch");
    }

    protected void afterRemove(HSSFPatriarch patriarch) {
        throw new IllegalStateException("HSSFObjectData cannot be created from scratch");
    }

    void afterInsert(HSSFPatriarch patriarch) {
        patriarch.getBoundAggregate().associateShapeToObjRecord(getEscherContainer().getChildById(EscherClientDataRecord.RECORD_ID), getObjRecord());
        EscherBSERecord bse = patriarch.getSheet().getWorkbook().getWorkbook().getBSERecord(getPictureIndex());
        bse.setRef(bse.getRef() + 1);
    }

    protected HSSFShape cloneShape() {
        EscherContainerRecord spContainer = new EscherContainerRecord();
        spContainer.fillFields(getEscherContainer().serialize(), 0, new DefaultEscherRecordFactory());
        return new HSSFObjectData(spContainer, (ObjRecord) getObjRecord().cloneViaReserialise(), this._root);
    }
}

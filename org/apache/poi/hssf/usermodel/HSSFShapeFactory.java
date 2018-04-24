package org.apache.poi.hssf.usermodel;

import java.util.List;
import java.util.Map;
import org.apache.poi.ddf.EscherClientDataRecord;
import org.apache.poi.ddf.EscherContainerRecord;
import org.apache.poi.ddf.EscherOptRecord;
import org.apache.poi.ddf.EscherRecord;
import org.apache.poi.hssf.record.CommonObjectDataSubRecord;
import org.apache.poi.hssf.record.EmbeddedObjectRefSubRecord;
import org.apache.poi.hssf.record.EscherAggregate;
import org.apache.poi.hssf.record.ObjRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.SubRecord;
import org.apache.poi.hssf.record.TextObjectRecord;
import org.apache.poi.poifs.filesystem.DirectoryNode;

public class HSSFShapeFactory {
    public static void createShapeTree(EscherContainerRecord container, EscherAggregate agg, HSSFShapeContainer out, DirectoryNode root) {
        if (container.getRecordId() == (short) -4093) {
            ObjRecord obj = null;
            EscherClientDataRecord clientData = (EscherClientDataRecord) ((EscherContainerRecord) container.getChild(0)).getChildById(EscherClientDataRecord.RECORD_ID);
            if (clientData != null) {
                obj = (ObjRecord) agg.getShapeToObjMapping().get(clientData);
            }
            HSSFShapeGroup group = new HSSFShapeGroup(container, obj);
            List<EscherContainerRecord> children = container.getChildContainers();
            for (int i = 0; i < children.size(); i++) {
                EscherContainerRecord spContainer = (EscherContainerRecord) children.get(i);
                if (i != 0) {
                    createShapeTree(spContainer, agg, group, root);
                }
            }
            out.addShape(group);
        } else if (container.getRecordId() == (short) -4092) {
            Map<EscherRecord, Record> shapeToObj = agg.getShapeToObjMapping();
            ObjRecord objRecord = null;
            TextObjectRecord txtRecord = null;
            for (EscherRecord record : container.getChildRecords()) {
                switch (record.getRecordId()) {
                    case (short) -4083:
                        txtRecord = (TextObjectRecord) shapeToObj.get(record);
                        break;
                    case (short) -4079:
                        objRecord = (ObjRecord) shapeToObj.get(record);
                        break;
                    default:
                        break;
                }
            }
            if (isEmbeddedObject(objRecord)) {
                out.addShape(new HSSFObjectData(container, objRecord, root));
                return;
            }
            HSSFShape shape;
            switch (((CommonObjectDataSubRecord) objRecord.getSubRecords().get(0)).getObjectType()) {
                case (short) 1:
                    shape = new HSSFSimpleShape(container, objRecord);
                    break;
                case (short) 2:
                    shape = new HSSFSimpleShape(container, objRecord, txtRecord);
                    break;
                case (short) 6:
                    shape = new HSSFTextbox(container, objRecord, txtRecord);
                    break;
                case (short) 8:
                    shape = new HSSFPicture(container, objRecord);
                    break;
                case (short) 20:
                    shape = new HSSFCombobox(container, objRecord);
                    break;
                case (short) 25:
                    shape = new HSSFComment(container, objRecord, txtRecord, agg.getNoteRecordByObj(objRecord));
                    break;
                case (short) 30:
                    EscherOptRecord optRecord = (EscherOptRecord) container.getChildById(EscherOptRecord.RECORD_ID);
                    if (optRecord != null) {
                        if (optRecord.lookup(325) == null) {
                            shape = new HSSFSimpleShape(container, objRecord, txtRecord);
                            break;
                        } else {
                            shape = new HSSFPolygon(container, objRecord, txtRecord);
                            break;
                        }
                    }
                    shape = new HSSFSimpleShape(container, objRecord, txtRecord);
                    break;
                default:
                    shape = new HSSFSimpleShape(container, objRecord, txtRecord);
                    break;
            }
            out.addShape(shape);
        }
    }

    private static boolean isEmbeddedObject(ObjRecord obj) {
        for (SubRecord sub : obj.getSubRecords()) {
            if (sub instanceof EmbeddedObjectRefSubRecord) {
                return true;
            }
        }
        return false;
    }
}

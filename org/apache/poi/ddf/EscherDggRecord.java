package org.apache.poi.ddf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.RecordFormatException;

public final class EscherDggRecord extends EscherRecord {
    private static final Comparator<FileIdCluster> MY_COMP = new C10491();
    public static final String RECORD_DESCRIPTION = "MsofbtDgg";
    public static final short RECORD_ID = (short) -4090;
    private int field_1_shapeIdMax;
    private int field_3_numShapesSaved;
    private int field_4_drawingsSaved;
    private FileIdCluster[] field_5_fileIdClusters;
    private int maxDgId;

    static class C10491 implements Comparator<FileIdCluster> {
        C10491() {
        }

        public int compare(FileIdCluster f1, FileIdCluster f2) {
            if (f1.getDrawingGroupId() == f2.getDrawingGroupId()) {
                return 0;
            }
            if (f1.getDrawingGroupId() < f2.getDrawingGroupId()) {
                return -1;
            }
            return 1;
        }
    }

    public static class FileIdCluster {
        private int field_1_drawingGroupId;
        private int field_2_numShapeIdsUsed;

        public FileIdCluster(int drawingGroupId, int numShapeIdsUsed) {
            this.field_1_drawingGroupId = drawingGroupId;
            this.field_2_numShapeIdsUsed = numShapeIdsUsed;
        }

        public int getDrawingGroupId() {
            return this.field_1_drawingGroupId;
        }

        public int getNumShapeIdsUsed() {
            return this.field_2_numShapeIdsUsed;
        }

        public void incrementShapeId() {
            this.field_2_numShapeIdsUsed++;
        }
    }

    public int fillFields(byte[] data, int offset, EscherRecordFactory recordFactory) {
        int bytesRemaining = readHeader(data, offset);
        int pos = offset + 8;
        this.field_1_shapeIdMax = LittleEndian.getInt(data, pos + 0);
        int size = (0 + 4) + 4;
        this.field_3_numShapesSaved = LittleEndian.getInt(data, pos + 8);
        size += 4;
        this.field_4_drawingsSaved = LittleEndian.getInt(data, pos + 12);
        size += 4;
        this.field_5_fileIdClusters = new FileIdCluster[((bytesRemaining - 16) / 8)];
        for (int i = 0; i < this.field_5_fileIdClusters.length; i++) {
            this.field_5_fileIdClusters[i] = new FileIdCluster(LittleEndian.getInt(data, pos + size), LittleEndian.getInt(data, (pos + size) + 4));
            this.maxDgId = Math.max(this.maxDgId, this.field_5_fileIdClusters[i].getDrawingGroupId());
            size += 8;
        }
        bytesRemaining -= size;
        if (bytesRemaining == 0) {
            return (size + 8) + bytesRemaining;
        }
        throw new RecordFormatException("Expecting no remaining data but got " + bytesRemaining + " byte(s).");
    }

    public int serialize(int offset, byte[] data, EscherSerializationListener listener) {
        listener.beforeRecordSerialize(offset, getRecordId(), this);
        int pos = offset;
        LittleEndian.putShort(data, pos, getOptions());
        pos += 2;
        LittleEndian.putShort(data, pos, getRecordId());
        pos += 2;
        LittleEndian.putInt(data, pos, getRecordSize() - 8);
        pos += 4;
        LittleEndian.putInt(data, pos, this.field_1_shapeIdMax);
        pos += 4;
        LittleEndian.putInt(data, pos, getNumIdClusters());
        pos += 4;
        LittleEndian.putInt(data, pos, this.field_3_numShapesSaved);
        pos += 4;
        LittleEndian.putInt(data, pos, this.field_4_drawingsSaved);
        pos += 4;
        for (int i = 0; i < this.field_5_fileIdClusters.length; i++) {
            LittleEndian.putInt(data, pos, this.field_5_fileIdClusters[i].field_1_drawingGroupId);
            pos += 4;
            LittleEndian.putInt(data, pos, this.field_5_fileIdClusters[i].field_2_numShapeIdsUsed);
            pos += 4;
        }
        listener.afterRecordSerialize(pos, getRecordId(), getRecordSize(), this);
        return getRecordSize();
    }

    public int getRecordSize() {
        return (this.field_5_fileIdClusters.length * 8) + 24;
    }

    public short getRecordId() {
        return RECORD_ID;
    }

    public String getRecordName() {
        return "Dgg";
    }

    public String toString() {
        StringBuilder field_5_string = new StringBuilder();
        if (this.field_5_fileIdClusters != null) {
            for (int i = 0; i < this.field_5_fileIdClusters.length; i++) {
                field_5_string.append("  DrawingGroupId").append(i + 1).append(": ");
                field_5_string.append(this.field_5_fileIdClusters[i].field_1_drawingGroupId);
                field_5_string.append('\n');
                field_5_string.append("  NumShapeIdsUsed").append(i + 1).append(": ");
                field_5_string.append(this.field_5_fileIdClusters[i].field_2_numShapeIdsUsed);
                field_5_string.append('\n');
            }
        }
        return getClass().getName() + ":" + '\n' + "  RecordId: 0x" + HexDump.toHex(RECORD_ID) + '\n' + "  Version: 0x" + HexDump.toHex(getVersion()) + '\n' + "  Instance: 0x" + HexDump.toHex(getInstance()) + '\n' + "  ShapeIdMax: " + this.field_1_shapeIdMax + '\n' + "  NumIdClusters: " + getNumIdClusters() + '\n' + "  NumShapesSaved: " + this.field_3_numShapesSaved + '\n' + "  DrawingsSaved: " + this.field_4_drawingsSaved + '\n' + "" + field_5_string.toString();
    }

    public String toXml(String tab) {
        StringBuilder builder = new StringBuilder();
        builder.append(tab).append(formatXmlRecordHeader(getClass().getSimpleName(), HexDump.toHex(getRecordId()), HexDump.toHex(getVersion()), HexDump.toHex(getInstance()))).append(tab).append("\t").append("<ShapeIdMax>").append(this.field_1_shapeIdMax).append("</ShapeIdMax>\n").append(tab).append("\t").append("<NumIdClusters>").append(getNumIdClusters()).append("</NumIdClusters>\n").append(tab).append("\t").append("<NumShapesSaved>").append(this.field_3_numShapesSaved).append("</NumShapesSaved>\n").append(tab).append("\t").append("<DrawingsSaved>").append(this.field_4_drawingsSaved).append("</DrawingsSaved>\n");
        builder.append(tab).append("</").append(getClass().getSimpleName()).append(">\n");
        return builder.toString();
    }

    public int getShapeIdMax() {
        return this.field_1_shapeIdMax;
    }

    public void setShapeIdMax(int shapeIdMax) {
        this.field_1_shapeIdMax = shapeIdMax;
    }

    public int getNumIdClusters() {
        return this.field_5_fileIdClusters == null ? 0 : this.field_5_fileIdClusters.length + 1;
    }

    public int getNumShapesSaved() {
        return this.field_3_numShapesSaved;
    }

    public void setNumShapesSaved(int numShapesSaved) {
        this.field_3_numShapesSaved = numShapesSaved;
    }

    public int getDrawingsSaved() {
        return this.field_4_drawingsSaved;
    }

    public void setDrawingsSaved(int drawingsSaved) {
        this.field_4_drawingsSaved = drawingsSaved;
    }

    public int getMaxDrawingGroupId() {
        return this.maxDgId;
    }

    public void setMaxDrawingGroupId(int id) {
        this.maxDgId = id;
    }

    public FileIdCluster[] getFileIdClusters() {
        return this.field_5_fileIdClusters;
    }

    public void setFileIdClusters(FileIdCluster[] fileIdClusters) {
        this.field_5_fileIdClusters = (FileIdCluster[]) fileIdClusters.clone();
    }

    public void addCluster(int dgId, int numShapedUsed) {
        addCluster(dgId, numShapedUsed, true);
    }

    public void addCluster(int dgId, int numShapedUsed, boolean sort) {
        List<FileIdCluster> clusters = new ArrayList(Arrays.asList(this.field_5_fileIdClusters));
        clusters.add(new FileIdCluster(dgId, numShapedUsed));
        if (sort) {
            Collections.sort(clusters, MY_COMP);
        }
        this.maxDgId = Math.min(this.maxDgId, dgId);
        this.field_5_fileIdClusters = (FileIdCluster[]) clusters.toArray(new FileIdCluster[clusters.size()]);
    }
}

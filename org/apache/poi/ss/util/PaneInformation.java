package org.apache.poi.ss.util;

public class PaneInformation {
    public static final byte PANE_LOWER_LEFT = (byte) 2;
    public static final byte PANE_LOWER_RIGHT = (byte) 0;
    public static final byte PANE_UPPER_LEFT = (byte) 3;
    public static final byte PANE_UPPER_RIGHT = (byte) 1;
    private final byte activePane;
    private final boolean frozen;
    private final short leftColumn;
    private final short topRow;
    private final short f169x;
    private final short f170y;

    public PaneInformation(short x, short y, short top, short left, byte active, boolean frozen) {
        this.f169x = x;
        this.f170y = y;
        this.topRow = top;
        this.leftColumn = left;
        this.activePane = active;
        this.frozen = frozen;
    }

    public short getVerticalSplitPosition() {
        return this.f169x;
    }

    public short getHorizontalSplitPosition() {
        return this.f170y;
    }

    public short getHorizontalSplitTopRow() {
        return this.topRow;
    }

    public short getVerticalSplitLeftColumn() {
        return this.leftColumn;
    }

    public byte getActivePane() {
        return this.activePane;
    }

    public boolean isFreezePane() {
        return this.frozen;
    }
}

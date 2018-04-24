package org.apache.poi.ss.usermodel;

import org.apache.poi.util.Internal;
import org.apache.poi.util.Removal;

public interface ClientAnchor {
    @Removal(version = "3.17")
    public static final AnchorType DONT_MOVE_AND_RESIZE = AnchorType.DONT_MOVE_AND_RESIZE;
    @Removal(version = "3.17")
    public static final AnchorType MOVE_AND_RESIZE = AnchorType.MOVE_AND_RESIZE;
    @Removal(version = "3.17")
    public static final AnchorType MOVE_DONT_RESIZE = AnchorType.MOVE_DONT_RESIZE;

    public enum AnchorType {
        MOVE_AND_RESIZE(0),
        DONT_MOVE_DO_RESIZE(1),
        MOVE_DONT_RESIZE(2),
        DONT_MOVE_AND_RESIZE(3);
        
        public final short value;

        private AnchorType(int value) {
            this.value = (short) value;
        }

        @Internal
        public static AnchorType byId(int value) {
            return values()[value];
        }
    }

    AnchorType getAnchorType();

    short getCol1();

    short getCol2();

    int getDx1();

    int getDx2();

    int getDy1();

    int getDy2();

    int getRow1();

    int getRow2();

    @Removal(version = "3.17")
    void setAnchorType(int i);

    void setAnchorType(AnchorType anchorType);

    void setCol1(int i);

    void setCol2(int i);

    void setDx1(int i);

    void setDx2(int i);

    void setDy1(int i);

    void setDy2(int i);

    void setRow1(int i);

    void setRow2(int i);
}

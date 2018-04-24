package org.apache.poi.hssf.usermodel;

import org.apache.poi.ddf.EscherChildAnchorRecord;
import org.apache.poi.ddf.EscherClientAnchorRecord;
import org.apache.poi.ddf.EscherContainerRecord;
import org.apache.poi.ddf.EscherRecord;

public abstract class HSSFAnchor {
    protected boolean _isHorizontallyFlipped = false;
    protected boolean _isVerticallyFlipped = false;

    protected abstract void createEscherAnchor();

    public abstract int getDx1();

    public abstract int getDx2();

    public abstract int getDy1();

    public abstract int getDy2();

    protected abstract EscherRecord getEscherAnchor();

    public abstract boolean isHorizontallyFlipped();

    public abstract boolean isVerticallyFlipped();

    public abstract void setDx1(int i);

    public abstract void setDx2(int i);

    public abstract void setDy1(int i);

    public abstract void setDy2(int i);

    public HSSFAnchor() {
        createEscherAnchor();
    }

    public HSSFAnchor(int dx1, int dy1, int dx2, int dy2) {
        createEscherAnchor();
        setDx1(dx1);
        setDy1(dy1);
        setDx2(dx2);
        setDy2(dy2);
    }

    public static HSSFAnchor createAnchorFromEscher(EscherContainerRecord container) {
        if (container.getChildById(EscherChildAnchorRecord.RECORD_ID) != null) {
            return new HSSFChildAnchor((EscherChildAnchorRecord) container.getChildById(EscherChildAnchorRecord.RECORD_ID));
        }
        if (container.getChildById(EscherClientAnchorRecord.RECORD_ID) != null) {
            return new HSSFClientAnchor((EscherClientAnchorRecord) container.getChildById(EscherClientAnchorRecord.RECORD_ID));
        }
        return null;
    }
}

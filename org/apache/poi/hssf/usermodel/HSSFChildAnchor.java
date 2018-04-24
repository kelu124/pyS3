package org.apache.poi.hssf.usermodel;

import org.apache.poi.ddf.EscherChildAnchorRecord;
import org.apache.poi.ddf.EscherRecord;

public final class HSSFChildAnchor extends HSSFAnchor {
    static final /* synthetic */ boolean $assertionsDisabled = (!HSSFChildAnchor.class.desiredAssertionStatus());
    private EscherChildAnchorRecord _escherChildAnchor;

    public HSSFChildAnchor(EscherChildAnchorRecord escherChildAnchorRecord) {
        this._escherChildAnchor = escherChildAnchorRecord;
    }

    public HSSFChildAnchor() {
        this._escherChildAnchor = new EscherChildAnchorRecord();
    }

    public HSSFChildAnchor(int dx1, int dy1, int dx2, int dy2) {
        super(Math.min(dx1, dx2), Math.min(dy1, dy2), Math.max(dx1, dx2), Math.max(dy1, dy2));
        if (dx1 > dx2) {
            this._isHorizontallyFlipped = true;
        }
        if (dy1 > dy2) {
            this._isVerticallyFlipped = true;
        }
    }

    public int getDx1() {
        return this._escherChildAnchor.getDx1();
    }

    public void setDx1(int dx1) {
        this._escherChildAnchor.setDx1(dx1);
    }

    public int getDy1() {
        return this._escherChildAnchor.getDy1();
    }

    public void setDy1(int dy1) {
        this._escherChildAnchor.setDy1(dy1);
    }

    public int getDy2() {
        return this._escherChildAnchor.getDy2();
    }

    public void setDy2(int dy2) {
        this._escherChildAnchor.setDy2(dy2);
    }

    public int getDx2() {
        return this._escherChildAnchor.getDx2();
    }

    public void setDx2(int dx2) {
        this._escherChildAnchor.setDx2(dx2);
    }

    public void setAnchor(int dx1, int dy1, int dx2, int dy2) {
        setDx1(Math.min(dx1, dx2));
        setDy1(Math.min(dy1, dy2));
        setDx2(Math.max(dx1, dx2));
        setDy2(Math.max(dy1, dy2));
    }

    public boolean isHorizontallyFlipped() {
        return this._isHorizontallyFlipped;
    }

    public boolean isVerticallyFlipped() {
        return this._isVerticallyFlipped;
    }

    protected EscherRecord getEscherAnchor() {
        return this._escherChildAnchor;
    }

    protected void createEscherAnchor() {
        this._escherChildAnchor = new EscherChildAnchorRecord();
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        HSSFChildAnchor anchor = (HSSFChildAnchor) obj;
        if (!(anchor.getDx1() == getDx1() && anchor.getDx2() == getDx2() && anchor.getDy1() == getDy1() && anchor.getDy2() == getDy2())) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        if ($assertionsDisabled) {
            return 42;
        }
        throw new AssertionError("hashCode not designed");
    }
}

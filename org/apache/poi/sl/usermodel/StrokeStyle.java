package org.apache.poi.sl.usermodel;

public interface StrokeStyle {

    public enum LineCap {
        ROUND(0, 1),
        SQUARE(1, 2),
        FLAT(2, 3);
        
        public final int nativeId;
        public final int ooxmlId;

        private LineCap(int nativeId, int ooxmlId) {
            this.nativeId = nativeId;
            this.ooxmlId = ooxmlId;
        }

        public static LineCap fromNativeId(int nativeId) {
            for (LineCap ld : values()) {
                if (ld.nativeId == nativeId) {
                    return ld;
                }
            }
            return null;
        }

        public static LineCap fromOoxmlId(int ooxmlId) {
            for (LineCap lc : values()) {
                if (lc.ooxmlId == ooxmlId) {
                    return lc;
                }
            }
            return null;
        }
    }

    public enum LineCompound {
        SINGLE(0, 1),
        DOUBLE(1, 2),
        THICK_THIN(2, 3),
        THIN_THICK(3, 4),
        TRIPLE(4, 5);
        
        public final int nativeId;
        public final int ooxmlId;

        private LineCompound(int nativeId, int ooxmlId) {
            this.nativeId = nativeId;
            this.ooxmlId = ooxmlId;
        }

        public static LineCompound fromNativeId(int nativeId) {
            for (LineCompound lc : values()) {
                if (lc.nativeId == nativeId) {
                    return lc;
                }
            }
            return null;
        }

        public static LineCompound fromOoxmlId(int ooxmlId) {
            for (LineCompound lc : values()) {
                if (lc.ooxmlId == ooxmlId) {
                    return lc;
                }
            }
            return null;
        }
    }

    public enum LineDash {
        SOLID(1, 1, null),
        DOT(6, 2, 1, 1),
        DASH(7, 3, 3, 4),
        DASH_DOT(9, 5, 4, 3, 1, 3),
        LG_DASH(8, 4, 8, 3),
        LG_DASH_DOT(10, 6, 8, 3, 1, 3),
        LG_DASH_DOT_DOT(11, 7, 8, 3, 1, 3, 1, 3),
        SYS_DASH(2, 8, 2, 2),
        SYS_DOT(3, 9, 1, 1),
        SYS_DASH_DOT(4, 10, 2, 2, 1, 1),
        SYS_DASH_DOT_DOT(5, 11, 2, 2, 1, 1, 1, 1);
        
        public final int nativeId;
        public final int ooxmlId;
        public final int[] pattern;

        private LineDash(int nativeId, int ooxmlId, int... pattern) {
            this.nativeId = nativeId;
            this.ooxmlId = ooxmlId;
            if (pattern == null || pattern.length == 0) {
                pattern = null;
            }
            this.pattern = pattern;
        }

        public static LineDash fromNativeId(int nativeId) {
            for (LineDash ld : values()) {
                if (ld.nativeId == nativeId) {
                    return ld;
                }
            }
            return null;
        }

        public static LineDash fromOoxmlId(int ooxmlId) {
            for (LineDash ld : values()) {
                if (ld.ooxmlId == ooxmlId) {
                    return ld;
                }
            }
            return null;
        }
    }

    LineCap getLineCap();

    LineCompound getLineCompound();

    LineDash getLineDash();

    double getLineWidth();

    PaintStyle getPaint();
}

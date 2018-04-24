package org.apache.poi.sl.usermodel;

public interface LineDecoration {

    public enum DecorationShape {
        NONE(0, 1),
        TRIANGLE(1, 2),
        STEALTH(2, 3),
        DIAMOND(3, 4),
        OVAL(4, 5),
        ARROW(5, 6);
        
        public final int nativeId;
        public final int ooxmlId;

        private DecorationShape(int nativeId, int ooxmlId) {
            this.nativeId = nativeId;
            this.ooxmlId = ooxmlId;
        }

        public static DecorationShape fromNativeId(int nativeId) {
            for (DecorationShape ld : values()) {
                if (ld.nativeId == nativeId) {
                    return ld;
                }
            }
            return null;
        }

        public static DecorationShape fromOoxmlId(int ooxmlId) {
            for (DecorationShape ds : values()) {
                if (ds.ooxmlId == ooxmlId) {
                    return ds;
                }
            }
            return null;
        }
    }

    public enum DecorationSize {
        SMALL(0, 1),
        MEDIUM(1, 2),
        LARGE(2, 3);
        
        public final int nativeId;
        public final int ooxmlId;

        private DecorationSize(int nativeId, int ooxmlId) {
            this.nativeId = nativeId;
            this.ooxmlId = ooxmlId;
        }

        public static DecorationSize fromNativeId(int nativeId) {
            for (DecorationSize ld : values()) {
                if (ld.nativeId == nativeId) {
                    return ld;
                }
            }
            return null;
        }

        public static DecorationSize fromOoxmlId(int ooxmlId) {
            for (DecorationSize ds : values()) {
                if (ds.ooxmlId == ooxmlId) {
                    return ds;
                }
            }
            return null;
        }
    }

    DecorationSize getHeadLength();

    DecorationShape getHeadShape();

    DecorationSize getHeadWidth();

    DecorationSize getTailLength();

    DecorationShape getTailShape();

    DecorationSize getTailWidth();
}

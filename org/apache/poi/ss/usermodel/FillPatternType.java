package org.apache.poi.ss.usermodel;

public enum FillPatternType {
    NO_FILL(0),
    SOLID_FOREGROUND(1),
    FINE_DOTS(2),
    ALT_BARS(3),
    SPARSE_DOTS(4),
    THICK_HORZ_BANDS(5),
    THICK_VERT_BANDS(6),
    THICK_BACKWARD_DIAG(7),
    THICK_FORWARD_DIAG(8),
    BIG_SPOTS(9),
    BRICKS(10),
    THIN_HORZ_BANDS(11),
    THIN_VERT_BANDS(12),
    THIN_BACKWARD_DIAG(13),
    THIN_FORWARD_DIAG(14),
    SQUARES(15),
    DIAMONDS(16),
    LESS_DOTS(17),
    LEAST_DOTS(18);
    
    private static final int length = 0;
    private final short code;

    static {
        length = values().length;
    }

    private FillPatternType(int code) {
        this.code = (short) code;
    }

    public short getCode() {
        return this.code;
    }

    public static FillPatternType forInt(int code) {
        if (code >= 0 && code <= length) {
            return values()[code];
        }
        throw new IllegalArgumentException("Invalid FillPatternType code: " + code);
    }
}

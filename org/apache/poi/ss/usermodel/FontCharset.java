package org.apache.poi.ss.usermodel;

import com.itextpdf.text.Jpeg;
import com.itextpdf.xmp.XMPError;

public enum FontCharset {
    ANSI(0),
    DEFAULT(1),
    SYMBOL(2),
    MAC(77),
    SHIFTJIS(128),
    HANGEUL(129),
    JOHAB(130),
    GB2312(134),
    CHINESEBIG5(136),
    GREEK(161),
    TURKISH(162),
    VIETNAMESE(163),
    HEBREW(177),
    ARABIC(178),
    BALTIC(186),
    RUSSIAN(XMPError.BADSTREAM),
    THAI(222),
    EASTEUROPE(Jpeg.M_APPE),
    OEM(255);
    
    private static FontCharset[] _table;
    private int charset;

    static {
        _table = new FontCharset[256];
        for (FontCharset c : values()) {
            _table[c.getValue()] = c;
        }
    }

    private FontCharset(int value) {
        this.charset = value;
    }

    public int getValue() {
        return this.charset;
    }

    public static FontCharset valueOf(int value) {
        if (value >= _table.length) {
            return null;
        }
        return _table[value];
    }
}

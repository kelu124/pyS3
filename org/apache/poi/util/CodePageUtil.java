package org.apache.poi.util;

import com.google.zxing.common.StringUtils;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.xml.xmp.XmpWriter;
import java.io.UnsupportedEncodingException;

public class CodePageUtil {
    public static final int CP_037 = 37;
    public static final int CP_EUC_JP = 51932;
    public static final int CP_EUC_KR = 51949;
    public static final int CP_GB18030 = 54936;
    public static final int CP_GB2312 = 52936;
    public static final int CP_GBK = 936;
    public static final int CP_ISO_2022_JP1 = 50220;
    public static final int CP_ISO_2022_JP2 = 50221;
    public static final int CP_ISO_2022_JP3 = 50222;
    public static final int CP_ISO_2022_KR = 50225;
    public static final int CP_ISO_8859_1 = 28591;
    public static final int CP_ISO_8859_2 = 28592;
    public static final int CP_ISO_8859_3 = 28593;
    public static final int CP_ISO_8859_4 = 28594;
    public static final int CP_ISO_8859_5 = 28595;
    public static final int CP_ISO_8859_6 = 28596;
    public static final int CP_ISO_8859_7 = 28597;
    public static final int CP_ISO_8859_8 = 28598;
    public static final int CP_ISO_8859_9 = 28599;
    public static final int CP_JOHAB = 1361;
    public static final int CP_KOI8_R = 20866;
    public static final int CP_MAC_ARABIC = 10004;
    public static final int CP_MAC_CENTRAL_EUROPE = 10029;
    public static final int CP_MAC_CHINESE_SIMPLE = 10008;
    public static final int CP_MAC_CHINESE_TRADITIONAL = 10002;
    public static final int CP_MAC_CROATIAN = 10082;
    public static final int CP_MAC_CYRILLIC = 10007;
    public static final int CP_MAC_GREEK = 10006;
    public static final int CP_MAC_HEBREW = 10005;
    public static final int CP_MAC_ICELAND = 10079;
    public static final int CP_MAC_JAPAN = 10001;
    public static final int CP_MAC_KOREAN = 10003;
    public static final int CP_MAC_ROMAN = 10000;
    public static final int CP_MAC_ROMANIA = 10010;
    public static final int CP_MAC_ROMAN_BIFF23 = 32768;
    public static final int CP_MAC_THAI = 10021;
    public static final int CP_MAC_TURKISH = 10081;
    public static final int CP_MAC_UKRAINE = 10017;
    public static final int CP_MS949 = 949;
    public static final int CP_SJIS = 932;
    public static final int CP_UNICODE = 1200;
    public static final int CP_US_ACSII = 20127;
    public static final int CP_US_ASCII2 = 65000;
    public static final int CP_UTF16 = 1200;
    public static final int CP_UTF16_BE = 1201;
    public static final int CP_UTF8 = 65001;
    public static final int CP_WINDOWS_1250 = 1250;
    public static final int CP_WINDOWS_1251 = 1251;
    public static final int CP_WINDOWS_1252 = 1252;
    public static final int CP_WINDOWS_1252_BIFF23 = 32769;
    public static final int CP_WINDOWS_1253 = 1253;
    public static final int CP_WINDOWS_1254 = 1254;
    public static final int CP_WINDOWS_1255 = 1255;
    public static final int CP_WINDOWS_1256 = 1256;
    public static final int CP_WINDOWS_1257 = 1257;
    public static final int CP_WINDOWS_1258 = 1258;

    public static byte[] getBytesInCodePage(String string, int codepage) throws UnsupportedEncodingException {
        return string.getBytes(codepageToEncoding(codepage));
    }

    public static String getStringFromCodePage(byte[] string, int codepage) throws UnsupportedEncodingException {
        return getStringFromCodePage(string, 0, string.length, codepage);
    }

    public static String getStringFromCodePage(byte[] string, int offset, int length, int codepage) throws UnsupportedEncodingException {
        return new String(string, offset, length, codepageToEncoding(codepage));
    }

    public static String codepageToEncoding(int codepage) throws UnsupportedEncodingException {
        return codepageToEncoding(codepage, false);
    }

    public static String codepageToEncoding(int codepage, boolean javaLangFormat) throws UnsupportedEncodingException {
        if (codepage <= 0) {
            throw new UnsupportedEncodingException("Codepage number may not be " + codepage);
        }
        switch (codepage) {
            case 37:
                return "cp037";
            case CP_SJIS /*932*/:
                return StringUtils.SHIFT_JIS;
            case CP_GBK /*936*/:
                return "GBK";
            case CP_MS949 /*949*/:
                return "ms949";
            case 1200:
                return XmpWriter.UTF16;
            case CP_UTF16_BE /*1201*/:
                return XmpWriter.UTF16BE;
            case CP_WINDOWS_1250 /*1250*/:
                if (javaLangFormat) {
                    return BaseFont.CP1250;
                }
                return "windows-1250";
            case CP_WINDOWS_1251 /*1251*/:
                if (javaLangFormat) {
                    return "Cp1251";
                }
                return "windows-1251";
            case CP_WINDOWS_1252 /*1252*/:
            case 32769:
                if (javaLangFormat) {
                    return "Cp1252";
                }
                return "windows-1252";
            case CP_WINDOWS_1253 /*1253*/:
                if (javaLangFormat) {
                    return "Cp1253";
                }
                return "windows-1253";
            case CP_WINDOWS_1254 /*1254*/:
                if (javaLangFormat) {
                    return "Cp1254";
                }
                return "windows-1254";
            case CP_WINDOWS_1255 /*1255*/:
                if (javaLangFormat) {
                    return "Cp1255";
                }
                return "windows-1255";
            case CP_WINDOWS_1256 /*1256*/:
                if (javaLangFormat) {
                    return "Cp1255";
                }
                return "windows-1256";
            case CP_WINDOWS_1257 /*1257*/:
                if (javaLangFormat) {
                    return BaseFont.CP1257;
                }
                return "windows-1257";
            case CP_WINDOWS_1258 /*1258*/:
                if (javaLangFormat) {
                    return "Cp1258";
                }
                return "windows-1258";
            case CP_JOHAB /*1361*/:
                return "johab";
            case 10000:
            case 32768:
                return BaseFont.MACROMAN;
            case 10001:
                return StringUtils.SHIFT_JIS;
            case 10002:
                return "Big5";
            case 10003:
                return "EUC-KR";
            case 10004:
                return "MacArabic";
            case 10005:
                return "MacHebrew";
            case 10006:
                return "MacGreek";
            case CP_MAC_CYRILLIC /*10007*/:
                return "MacCyrillic";
            case CP_MAC_CHINESE_SIMPLE /*10008*/:
                return "EUC_CN";
            case CP_MAC_ROMANIA /*10010*/:
                return "MacRomania";
            case CP_MAC_UKRAINE /*10017*/:
                return "MacUkraine";
            case CP_MAC_THAI /*10021*/:
                return "MacThai";
            case CP_MAC_CENTRAL_EUROPE /*10029*/:
                return "MacCentralEurope";
            case CP_MAC_ICELAND /*10079*/:
                return "MacIceland";
            case CP_MAC_TURKISH /*10081*/:
                return "MacTurkish";
            case CP_MAC_CROATIAN /*10082*/:
                return "MacCroatian";
            case CP_US_ACSII /*20127*/:
            case CP_US_ASCII2 /*65000*/:
                return "US-ASCII";
            case CP_KOI8_R /*20866*/:
                return "KOI8-R";
            case CP_ISO_8859_1 /*28591*/:
                if (javaLangFormat) {
                    return "ISO8859_1";
                }
                return "ISO-8859-1";
            case CP_ISO_8859_2 /*28592*/:
                if (javaLangFormat) {
                    return "ISO8859_2";
                }
                return "ISO-8859-2";
            case CP_ISO_8859_3 /*28593*/:
                if (javaLangFormat) {
                    return "ISO8859_3";
                }
                return "ISO-8859-3";
            case CP_ISO_8859_4 /*28594*/:
                if (javaLangFormat) {
                    return "ISO8859_4";
                }
                return "ISO-8859-4";
            case CP_ISO_8859_5 /*28595*/:
                if (javaLangFormat) {
                    return "ISO8859_5";
                }
                return "ISO-8859-5";
            case CP_ISO_8859_6 /*28596*/:
                if (javaLangFormat) {
                    return "ISO8859_6";
                }
                return "ISO-8859-6";
            case CP_ISO_8859_7 /*28597*/:
                if (javaLangFormat) {
                    return "ISO8859_7";
                }
                return "ISO-8859-7";
            case CP_ISO_8859_8 /*28598*/:
                if (javaLangFormat) {
                    return "ISO8859_8";
                }
                return "ISO-8859-8";
            case CP_ISO_8859_9 /*28599*/:
                if (javaLangFormat) {
                    return "ISO8859_9";
                }
                return "ISO-8859-9";
            case CP_ISO_2022_JP1 /*50220*/:
            case CP_ISO_2022_JP2 /*50221*/:
            case CP_ISO_2022_JP3 /*50222*/:
                return "ISO-2022-JP";
            case CP_ISO_2022_KR /*50225*/:
                return "ISO-2022-KR";
            case CP_EUC_JP /*51932*/:
                return "EUC-JP";
            case CP_EUC_KR /*51949*/:
                return "EUC-KR";
            case CP_GB2312 /*52936*/:
                return StringUtils.GB2312;
            case CP_GB18030 /*54936*/:
                return "GB18030";
            case CP_UTF8 /*65001*/:
                return XmpWriter.UTF8;
            default:
                return "cp" + codepage;
        }
    }
}

package com.itextpdf.text.xml.xmp;

@Deprecated
public class XmpBasicSchema extends XmpSchema {
    public static final String ADVISORY = "xmp:Advisory";
    public static final String BASEURL = "xmp:BaseURL";
    public static final String CREATEDATE = "xmp:CreateDate";
    public static final String CREATORTOOL = "xmp:CreatorTool";
    public static final String DEFAULT_XPATH_ID = "xmp";
    public static final String DEFAULT_XPATH_URI = "http://ns.adobe.com/xap/1.0/";
    public static final String IDENTIFIER = "xmp:Identifier";
    public static final String METADATADATE = "xmp:MetadataDate";
    public static final String MODIFYDATE = "xmp:ModifyDate";
    public static final String NICKNAME = "xmp:Nickname";
    public static final String THUMBNAILS = "xmp:Thumbnails";
    private static final long serialVersionUID = -2416613941622479298L;

    public XmpBasicSchema() {
        super("xmlns:xmp=\"http://ns.adobe.com/xap/1.0/\"");
    }

    public void addCreatorTool(String creator) {
        setProperty(CREATORTOOL, creator);
    }

    public void addCreateDate(String date) {
        setProperty(CREATEDATE, date);
    }

    public void addModDate(String date) {
        setProperty(MODIFYDATE, date);
    }

    public void addMetaDataDate(String date) {
        setProperty(METADATADATE, date);
    }

    public void addIdentifiers(String[] id) {
        XmpArray array = new XmpArray(XmpArray.UNORDERED);
        for (Object add : id) {
            array.add(add);
        }
        setProperty(IDENTIFIER, array);
    }

    public void addNickname(String name) {
        setProperty(NICKNAME, name);
    }
}

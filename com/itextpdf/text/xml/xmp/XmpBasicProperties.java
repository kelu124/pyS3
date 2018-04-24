package com.itextpdf.text.xml.xmp;

import com.itextpdf.xmp.XMPException;
import com.itextpdf.xmp.XMPMeta;
import com.itextpdf.xmp.XMPUtils;
import com.itextpdf.xmp.options.PropertyOptions;

public class XmpBasicProperties {
    public static final String ADVISORY = "Advisory";
    public static final String BASEURL = "BaseURL";
    public static final String CREATEDATE = "CreateDate";
    public static final String CREATORTOOL = "CreatorTool";
    public static final String IDENTIFIER = "Identifier";
    public static final String METADATADATE = "MetadataDate";
    public static final String MODIFYDATE = "ModifyDate";
    public static final String NICKNAME = "Nickname";
    public static final String THUMBNAILS = "Thumbnails";

    public static void setCreatorTool(XMPMeta xmpMeta, String creator) throws XMPException {
        xmpMeta.setProperty("http://ns.adobe.com/xap/1.0/", CREATORTOOL, creator);
    }

    public static void setCreateDate(XMPMeta xmpMeta, String date) throws XMPException {
        xmpMeta.setProperty("http://ns.adobe.com/xap/1.0/", CREATEDATE, date);
    }

    public static void setModDate(XMPMeta xmpMeta, String date) throws XMPException {
        xmpMeta.setProperty("http://ns.adobe.com/xap/1.0/", MODIFYDATE, date);
    }

    public static void setMetaDataDate(XMPMeta xmpMeta, String date) throws XMPException {
        xmpMeta.setProperty("http://ns.adobe.com/xap/1.0/", METADATADATE, date);
    }

    public static void setIdentifiers(XMPMeta xmpMeta, String[] id) throws XMPException {
        XMPUtils.removeProperties(xmpMeta, "http://purl.org/dc/elements/1.1/", IDENTIFIER, true, true);
        for (String appendArrayItem : id) {
            xmpMeta.appendArrayItem("http://purl.org/dc/elements/1.1/", IDENTIFIER, new PropertyOptions(512), appendArrayItem, null);
        }
    }

    public static void setNickname(XMPMeta xmpMeta, String name) throws XMPException {
        xmpMeta.setProperty("http://ns.adobe.com/xap/1.0/", NICKNAME, name);
    }
}

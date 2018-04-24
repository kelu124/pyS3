package com.itextpdf.text.pdf;

import com.itextpdf.text.pdf.security.XpathConstructor;
import com.itextpdf.text.xml.xmp.PdfSchema;
import com.itextpdf.xmp.XMPConst;

public class XfaXpathConstructor implements XpathConstructor {
    private final String CONFIG;
    private final String CONNECTIONSET;
    private final String DATASETS;
    private final String LOCALESET;
    private final String PDF;
    private final String SOURCESET;
    private final String STYLESHEET;
    private final String TEMPLATE;
    private final String XDC;
    private final String XFDF;
    private final String XMPMETA;
    private String xpathExpression;

    public enum XdpPackage {
        Config,
        ConnectionSet,
        Datasets,
        LocaleSet,
        Pdf,
        SourceSet,
        Stylesheet,
        Template,
        Xdc,
        Xfdf,
        Xmpmeta
    }

    public XfaXpathConstructor() {
        this.CONFIG = "config";
        this.CONNECTIONSET = "connectionSet";
        this.DATASETS = "datasets";
        this.LOCALESET = "localeSet";
        this.PDF = PdfSchema.DEFAULT_XPATH_ID;
        this.SOURCESET = "sourceSet";
        this.STYLESHEET = "stylesheet";
        this.TEMPLATE = "template";
        this.XDC = "xdc";
        this.XFDF = "xfdf";
        this.XMPMETA = XMPConst.TAG_XMPMETA;
        this.xpathExpression = "";
    }

    public XfaXpathConstructor(XdpPackage xdpPackage) {
        String strPackage;
        this.CONFIG = "config";
        this.CONNECTIONSET = "connectionSet";
        this.DATASETS = "datasets";
        this.LOCALESET = "localeSet";
        this.PDF = PdfSchema.DEFAULT_XPATH_ID;
        this.SOURCESET = "sourceSet";
        this.STYLESHEET = "stylesheet";
        this.TEMPLATE = "template";
        this.XDC = "xdc";
        this.XFDF = "xfdf";
        this.XMPMETA = XMPConst.TAG_XMPMETA;
        switch (xdpPackage) {
            case Config:
                strPackage = "config";
                break;
            case ConnectionSet:
                strPackage = "connectionSet";
                break;
            case Datasets:
                strPackage = "datasets";
                break;
            case LocaleSet:
                strPackage = "localeSet";
                break;
            case Pdf:
                strPackage = PdfSchema.DEFAULT_XPATH_ID;
                break;
            case SourceSet:
                strPackage = "sourceSet";
                break;
            case Stylesheet:
                strPackage = "stylesheet";
                break;
            case Template:
                strPackage = "template";
                break;
            case Xdc:
                strPackage = "xdc";
                break;
            case Xfdf:
                strPackage = "xfdf";
                break;
            case Xmpmeta:
                strPackage = XMPConst.TAG_XMPMETA;
                break;
            default:
                this.xpathExpression = "";
                return;
        }
        StringBuilder builder = new StringBuilder("/xdp:xdp/*[local-name()='");
        builder.append(strPackage);
        builder.append("']");
        this.xpathExpression = builder.toString();
    }

    public String getXpathExpression() {
        return this.xpathExpression;
    }
}

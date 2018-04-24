package com.itextpdf.xmp.impl;

import com.itextpdf.text.xml.xmp.DublinCoreProperties;
import com.itextpdf.text.xml.xmp.DublinCoreSchema;
import com.itextpdf.text.xml.xmp.PdfProperties;
import com.itextpdf.text.xml.xmp.PdfSchema;
import com.itextpdf.text.xml.xmp.XmpBasicProperties;
import com.itextpdf.text.xml.xmp.XmpBasicSchema;
import com.itextpdf.text.xml.xmp.XmpMMSchema;
import com.itextpdf.xmp.XMPConst;
import com.itextpdf.xmp.XMPException;
import com.itextpdf.xmp.XMPSchemaRegistry;
import com.itextpdf.xmp.options.AliasOptions;
import com.itextpdf.xmp.properties.XMPAliasInfo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

public final class XMPSchemaRegistryImpl implements XMPSchemaRegistry, XMPConst {
    private Map aliasMap = new HashMap();
    private Map namespaceToPrefixMap = new HashMap();
    private Pattern f168p = Pattern.compile("[/*?\\[\\]]");
    private Map prefixToNamespaceMap = new HashMap();

    public XMPSchemaRegistryImpl() {
        try {
            registerStandardNamespaces();
            registerStandardAliases();
        } catch (XMPException e) {
            throw new RuntimeException("The XMPSchemaRegistry cannot be initialized!");
        }
    }

    public synchronized String registerNamespace(String namespaceURI, String suggestedPrefix) throws XMPException {
        String registeredPrefix;
        ParameterAsserts.assertSchemaNS(namespaceURI);
        ParameterAsserts.assertPrefix(suggestedPrefix);
        if (suggestedPrefix.charAt(suggestedPrefix.length() - 1) != ':') {
            suggestedPrefix = suggestedPrefix + ':';
        }
        if (Utils.isXMLNameNS(suggestedPrefix.substring(0, suggestedPrefix.length() - 1))) {
            registeredPrefix = (String) this.namespaceToPrefixMap.get(namespaceURI);
            String registeredNS = (String) this.prefixToNamespaceMap.get(suggestedPrefix);
            if (registeredPrefix == null) {
                if (registeredNS != null) {
                    String generatedPrefix = suggestedPrefix;
                    int i = 1;
                    while (this.prefixToNamespaceMap.containsKey(generatedPrefix)) {
                        generatedPrefix = suggestedPrefix.substring(0, suggestedPrefix.length() - 1) + "_" + i + "_:";
                        i++;
                    }
                    suggestedPrefix = generatedPrefix;
                }
                this.prefixToNamespaceMap.put(suggestedPrefix, namespaceURI);
                this.namespaceToPrefixMap.put(namespaceURI, suggestedPrefix);
                registeredPrefix = suggestedPrefix;
            }
        } else {
            throw new XMPException("The prefix is a bad XML name", 201);
        }
        return registeredPrefix;
    }

    public synchronized void deleteNamespace(String namespaceURI) {
        String prefixToDelete = getNamespacePrefix(namespaceURI);
        if (prefixToDelete != null) {
            this.namespaceToPrefixMap.remove(namespaceURI);
            this.prefixToNamespaceMap.remove(prefixToDelete);
        }
    }

    public synchronized String getNamespacePrefix(String namespaceURI) {
        return (String) this.namespaceToPrefixMap.get(namespaceURI);
    }

    public synchronized String getNamespaceURI(String namespacePrefix) {
        if (namespacePrefix != null) {
            if (!namespacePrefix.endsWith(":")) {
                namespacePrefix = namespacePrefix + ":";
            }
        }
        return (String) this.prefixToNamespaceMap.get(namespacePrefix);
    }

    public synchronized Map getNamespaces() {
        return Collections.unmodifiableMap(new TreeMap(this.namespaceToPrefixMap));
    }

    public synchronized Map getPrefixes() {
        return Collections.unmodifiableMap(new TreeMap(this.prefixToNamespaceMap));
    }

    private void registerStandardNamespaces() throws XMPException {
        registerNamespace(XMPConst.NS_XML, "xml");
        registerNamespace(XMPConst.NS_RDF, "rdf");
        registerNamespace("http://purl.org/dc/elements/1.1/", DublinCoreSchema.DEFAULT_XPATH_ID);
        registerNamespace(XMPConst.NS_IPTCCORE, "Iptc4xmpCore");
        registerNamespace(XMPConst.NS_IPTCEXT, "Iptc4xmpExt");
        registerNamespace(XMPConst.NS_DICOM, "DICOM");
        registerNamespace(XMPConst.NS_PLUS, "plus");
        registerNamespace(XMPConst.NS_X, "x");
        registerNamespace(XMPConst.NS_IX, "iX");
        registerNamespace("http://ns.adobe.com/xap/1.0/", XmpBasicSchema.DEFAULT_XPATH_ID);
        registerNamespace(XMPConst.NS_XMP_RIGHTS, "xmpRights");
        registerNamespace("http://ns.adobe.com/xap/1.0/mm/", XmpMMSchema.DEFAULT_XPATH_ID);
        registerNamespace(XMPConst.NS_XMP_BJ, "xmpBJ");
        registerNamespace(XMPConst.NS_XMP_NOTE, "xmpNote");
        registerNamespace("http://ns.adobe.com/pdf/1.3/", PdfSchema.DEFAULT_XPATH_ID);
        registerNamespace(XMPConst.NS_PDFX, "pdfx");
        registerNamespace(XMPConst.NS_PDFX_ID, "pdfxid");
        registerNamespace(XMPConst.NS_PDFA_SCHEMA, "pdfaSchema");
        registerNamespace(XMPConst.NS_PDFA_PROPERTY, "pdfaProperty");
        registerNamespace(XMPConst.NS_PDFA_TYPE, "pdfaType");
        registerNamespace(XMPConst.NS_PDFA_FIELD, "pdfaField");
        registerNamespace(XMPConst.NS_PDFA_ID, "pdfaid");
        registerNamespace(XMPConst.NS_PDFUA_ID, "pdfuaid");
        registerNamespace(XMPConst.NS_PDFA_EXTENSION, "pdfaExtension");
        registerNamespace(XMPConst.NS_PHOTOSHOP, "photoshop");
        registerNamespace(XMPConst.NS_PSALBUM, "album");
        registerNamespace(XMPConst.NS_EXIF, "exif");
        registerNamespace(XMPConst.NS_EXIFX, "exifEX");
        registerNamespace(XMPConst.NS_EXIF_AUX, "aux");
        registerNamespace(XMPConst.NS_TIFF, "tiff");
        registerNamespace(XMPConst.NS_PNG, "png");
        registerNamespace(XMPConst.NS_JPEG, "jpeg");
        registerNamespace(XMPConst.NS_JP2K, "jp2k");
        registerNamespace(XMPConst.NS_CAMERARAW, "crs");
        registerNamespace(XMPConst.NS_ADOBESTOCKPHOTO, "bmsp");
        registerNamespace(XMPConst.NS_CREATOR_ATOM, "creatorAtom");
        registerNamespace(XMPConst.NS_ASF, "asf");
        registerNamespace(XMPConst.NS_WAV, "wav");
        registerNamespace(XMPConst.NS_BWF, "bext");
        registerNamespace(XMPConst.NS_RIFFINFO, "riffinfo");
        registerNamespace(XMPConst.NS_SCRIPT, "xmpScript");
        registerNamespace(XMPConst.NS_TXMP, "txmp");
        registerNamespace(XMPConst.NS_SWF, "swf");
        registerNamespace(XMPConst.NS_DM, "xmpDM");
        registerNamespace(XMPConst.NS_TRANSIENT, "xmpx");
        registerNamespace(XMPConst.TYPE_TEXT, "xmpT");
        registerNamespace(XMPConst.TYPE_PAGEDFILE, "xmpTPg");
        registerNamespace(XMPConst.TYPE_GRAPHICS, "xmpG");
        registerNamespace(XMPConst.TYPE_IMAGE, "xmpGImg");
        registerNamespace(XMPConst.TYPE_FONT, "stFnt");
        registerNamespace(XMPConst.TYPE_DIMENSIONS, "stDim");
        registerNamespace(XMPConst.TYPE_RESOURCEEVENT, "stEvt");
        registerNamespace(XMPConst.TYPE_RESOURCEREF, "stRef");
        registerNamespace(XMPConst.TYPE_ST_VERSION, "stVer");
        registerNamespace(XMPConst.TYPE_ST_JOB, "stJob");
        registerNamespace(XMPConst.TYPE_MANIFESTITEM, "stMfs");
        registerNamespace(XMPConst.TYPE_IDENTIFIERQUAL, "xmpidq");
    }

    public synchronized XMPAliasInfo resolveAlias(String aliasNS, String aliasProp) {
        XMPAliasInfo xMPAliasInfo;
        String aliasPrefix = getNamespacePrefix(aliasNS);
        if (aliasPrefix == null) {
            xMPAliasInfo = null;
        } else {
            xMPAliasInfo = (XMPAliasInfo) this.aliasMap.get(aliasPrefix + aliasProp);
        }
        return xMPAliasInfo;
    }

    public synchronized XMPAliasInfo findAlias(String qname) {
        return (XMPAliasInfo) this.aliasMap.get(qname);
    }

    public synchronized XMPAliasInfo[] findAliases(String aliasNS) {
        List result;
        String prefix = getNamespacePrefix(aliasNS);
        result = new ArrayList();
        if (prefix != null) {
            for (String qname : this.aliasMap.keySet()) {
                if (qname.startsWith(prefix)) {
                    result.add(findAlias(qname));
                }
            }
        }
        return (XMPAliasInfo[]) result.toArray(new XMPAliasInfo[result.size()]);
    }

    synchronized void registerAlias(String aliasNS, String aliasProp, String actualNS, String actualProp, AliasOptions aliasForm) throws XMPException {
        AliasOptions aliasOpts;
        ParameterAsserts.assertSchemaNS(aliasNS);
        ParameterAsserts.assertPropName(aliasProp);
        ParameterAsserts.assertSchemaNS(actualNS);
        ParameterAsserts.assertPropName(actualProp);
        if (aliasForm != null) {
            aliasOpts = new AliasOptions(XMPNodeUtils.verifySetOptions(aliasForm.toPropertyOptions(), null).getOptions());
        } else {
            aliasOpts = new AliasOptions();
        }
        if (this.f168p.matcher(aliasProp).find() || this.f168p.matcher(actualProp).find()) {
            throw new XMPException("Alias and actual property names must be simple", 102);
        }
        String aliasPrefix = getNamespacePrefix(aliasNS);
        final String actualPrefix = getNamespacePrefix(actualNS);
        if (aliasPrefix == null) {
            throw new XMPException("Alias namespace is not registered", 101);
        } else if (actualPrefix == null) {
            throw new XMPException("Actual namespace is not registered", 101);
        } else {
            String key = aliasPrefix + aliasProp;
            if (this.aliasMap.containsKey(key)) {
                throw new XMPException("Alias is already existing", 4);
            } else if (this.aliasMap.containsKey(actualPrefix + actualProp)) {
                throw new XMPException("Actual property is already an alias, use the base property", 4);
            } else {
                final String str = actualNS;
                final String str2 = actualProp;
                this.aliasMap.put(key, new XMPAliasInfo() {
                    public String getNamespace() {
                        return str;
                    }

                    public String getPrefix() {
                        return actualPrefix;
                    }

                    public String getPropName() {
                        return str2;
                    }

                    public AliasOptions getAliasForm() {
                        return aliasOpts;
                    }

                    public String toString() {
                        return actualPrefix + str2 + " NS(" + str + "), FORM (" + getAliasForm() + ")";
                    }
                });
            }
        }
    }

    public synchronized Map getAliases() {
        return Collections.unmodifiableMap(new TreeMap(this.aliasMap));
    }

    private void registerStandardAliases() throws XMPException {
        AliasOptions aliasToArrayOrdered = new AliasOptions().setArrayOrdered(true);
        AliasOptions aliasToArrayAltText = new AliasOptions().setArrayAltText(true);
        registerAlias("http://ns.adobe.com/xap/1.0/", "Author", "http://purl.org/dc/elements/1.1/", DublinCoreProperties.CREATOR, aliasToArrayOrdered);
        registerAlias("http://ns.adobe.com/xap/1.0/", "Authors", "http://purl.org/dc/elements/1.1/", DublinCoreProperties.CREATOR, null);
        registerAlias("http://ns.adobe.com/xap/1.0/", "Description", "http://purl.org/dc/elements/1.1/", DublinCoreProperties.DESCRIPTION, null);
        registerAlias("http://ns.adobe.com/xap/1.0/", "Format", "http://purl.org/dc/elements/1.1/", DublinCoreProperties.FORMAT, null);
        registerAlias("http://ns.adobe.com/xap/1.0/", PdfProperties.KEYWORDS, "http://purl.org/dc/elements/1.1/", "subject", null);
        registerAlias("http://ns.adobe.com/xap/1.0/", "Locale", "http://purl.org/dc/elements/1.1/", DublinCoreProperties.LANGUAGE, null);
        registerAlias("http://ns.adobe.com/xap/1.0/", "Title", "http://purl.org/dc/elements/1.1/", "title", null);
        registerAlias(XMPConst.NS_XMP_RIGHTS, "Copyright", "http://purl.org/dc/elements/1.1/", DublinCoreProperties.RIGHTS, null);
        registerAlias("http://ns.adobe.com/pdf/1.3/", "Author", "http://purl.org/dc/elements/1.1/", DublinCoreProperties.CREATOR, aliasToArrayOrdered);
        registerAlias("http://ns.adobe.com/pdf/1.3/", XmpBasicProperties.BASEURL, "http://ns.adobe.com/xap/1.0/", XmpBasicProperties.BASEURL, null);
        registerAlias("http://ns.adobe.com/pdf/1.3/", "CreationDate", "http://ns.adobe.com/xap/1.0/", XmpBasicProperties.CREATEDATE, null);
        registerAlias("http://ns.adobe.com/pdf/1.3/", "Creator", "http://ns.adobe.com/xap/1.0/", XmpBasicProperties.CREATORTOOL, null);
        registerAlias("http://ns.adobe.com/pdf/1.3/", "ModDate", "http://ns.adobe.com/xap/1.0/", XmpBasicProperties.MODIFYDATE, null);
        registerAlias("http://ns.adobe.com/pdf/1.3/", "Subject", "http://purl.org/dc/elements/1.1/", DublinCoreProperties.DESCRIPTION, aliasToArrayAltText);
        registerAlias("http://ns.adobe.com/pdf/1.3/", "Title", "http://purl.org/dc/elements/1.1/", "title", aliasToArrayAltText);
        registerAlias(XMPConst.NS_PHOTOSHOP, "Author", "http://purl.org/dc/elements/1.1/", DublinCoreProperties.CREATOR, aliasToArrayOrdered);
        registerAlias(XMPConst.NS_PHOTOSHOP, "Caption", "http://purl.org/dc/elements/1.1/", DublinCoreProperties.DESCRIPTION, aliasToArrayAltText);
        registerAlias(XMPConst.NS_PHOTOSHOP, "Copyright", "http://purl.org/dc/elements/1.1/", DublinCoreProperties.RIGHTS, aliasToArrayAltText);
        registerAlias(XMPConst.NS_PHOTOSHOP, PdfProperties.KEYWORDS, "http://purl.org/dc/elements/1.1/", "subject", null);
        registerAlias(XMPConst.NS_PHOTOSHOP, "Marked", XMPConst.NS_XMP_RIGHTS, "Marked", null);
        registerAlias(XMPConst.NS_PHOTOSHOP, "Title", "http://purl.org/dc/elements/1.1/", "title", aliasToArrayAltText);
        registerAlias(XMPConst.NS_PHOTOSHOP, "WebStatement", XMPConst.NS_XMP_RIGHTS, "WebStatement", null);
        registerAlias(XMPConst.NS_TIFF, "Artist", "http://purl.org/dc/elements/1.1/", DublinCoreProperties.CREATOR, aliasToArrayOrdered);
        registerAlias(XMPConst.NS_TIFF, "Copyright", "http://purl.org/dc/elements/1.1/", DublinCoreProperties.RIGHTS, null);
        registerAlias(XMPConst.NS_TIFF, "DateTime", "http://ns.adobe.com/xap/1.0/", XmpBasicProperties.MODIFYDATE, null);
        registerAlias(XMPConst.NS_TIFF, "ImageDescription", "http://purl.org/dc/elements/1.1/", DublinCoreProperties.DESCRIPTION, null);
        registerAlias(XMPConst.NS_TIFF, "Software", "http://ns.adobe.com/xap/1.0/", XmpBasicProperties.CREATORTOOL, null);
        registerAlias(XMPConst.NS_PNG, "Author", "http://purl.org/dc/elements/1.1/", DublinCoreProperties.CREATOR, aliasToArrayOrdered);
        registerAlias(XMPConst.NS_PNG, "Copyright", "http://purl.org/dc/elements/1.1/", DublinCoreProperties.RIGHTS, aliasToArrayAltText);
        registerAlias(XMPConst.NS_PNG, "CreationTime", "http://ns.adobe.com/xap/1.0/", XmpBasicProperties.CREATEDATE, null);
        registerAlias(XMPConst.NS_PNG, "Description", "http://purl.org/dc/elements/1.1/", DublinCoreProperties.DESCRIPTION, aliasToArrayAltText);
        registerAlias(XMPConst.NS_PNG, "ModificationTime", "http://ns.adobe.com/xap/1.0/", XmpBasicProperties.MODIFYDATE, null);
        registerAlias(XMPConst.NS_PNG, "Software", "http://ns.adobe.com/xap/1.0/", XmpBasicProperties.CREATORTOOL, null);
        registerAlias(XMPConst.NS_PNG, "Title", "http://purl.org/dc/elements/1.1/", "title", aliasToArrayAltText);
    }
}

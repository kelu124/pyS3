package com.itextpdf.xmp.impl;

import com.itextpdf.xmp.XMPConst;
import com.itextpdf.xmp.XMPDateTime;
import com.itextpdf.xmp.XMPException;
import com.itextpdf.xmp.XMPIterator;
import com.itextpdf.xmp.XMPMeta;
import com.itextpdf.xmp.XMPPathFactory;
import com.itextpdf.xmp.XMPUtils;
import com.itextpdf.xmp.impl.xpath.XMPPath;
import com.itextpdf.xmp.impl.xpath.XMPPathParser;
import com.itextpdf.xmp.options.IteratorOptions;
import com.itextpdf.xmp.options.ParseOptions;
import com.itextpdf.xmp.options.PropertyOptions;
import com.itextpdf.xmp.properties.XMPProperty;
import java.util.Calendar;
import java.util.Iterator;

public class XMPMetaImpl implements XMPMeta, XMPConst {
    static final /* synthetic */ boolean $assertionsDisabled = (!XMPMetaImpl.class.desiredAssertionStatus());
    private static final int VALUE_BASE64 = 7;
    private static final int VALUE_BOOLEAN = 1;
    private static final int VALUE_CALENDAR = 6;
    private static final int VALUE_DATE = 5;
    private static final int VALUE_DOUBLE = 4;
    private static final int VALUE_INTEGER = 2;
    private static final int VALUE_LONG = 3;
    private static final int VALUE_STRING = 0;
    private String packetHeader;
    private XMPNode tree;

    public XMPMetaImpl() {
        this.packetHeader = null;
        this.tree = new XMPNode(null, null, null);
    }

    public XMPMetaImpl(XMPNode tree) {
        this.packetHeader = null;
        this.tree = tree;
    }

    public void appendArrayItem(String schemaNS, String arrayName, PropertyOptions arrayOptions, String itemValue, PropertyOptions itemOptions) throws XMPException {
        ParameterAsserts.assertSchemaNS(schemaNS);
        ParameterAsserts.assertArrayName(arrayName);
        if (arrayOptions == null) {
            arrayOptions = new PropertyOptions();
        }
        if (arrayOptions.isOnlyArrayOptions()) {
            arrayOptions = XMPNodeUtils.verifySetOptions(arrayOptions, null);
            XMPPath arrayPath = XMPPathParser.expandXPath(schemaNS, arrayName);
            XMPNode arrayNode = XMPNodeUtils.findNode(this.tree, arrayPath, false, null);
            if (arrayNode != null) {
                if (!arrayNode.getOptions().isArray()) {
                    throw new XMPException("The named property is not an array", 102);
                }
            } else if (arrayOptions.isArray()) {
                arrayNode = XMPNodeUtils.findNode(this.tree, arrayPath, true, arrayOptions);
                if (arrayNode == null) {
                    throw new XMPException("Failure creating array node", 102);
                }
            } else {
                throw new XMPException("Explicit arrayOptions required to create new array", 103);
            }
            doSetArrayItem(arrayNode, -1, itemValue, itemOptions, true);
            return;
        }
        throw new XMPException("Only array form flags allowed for arrayOptions", 103);
    }

    public void appendArrayItem(String schemaNS, String arrayName, String itemValue) throws XMPException {
        appendArrayItem(schemaNS, arrayName, null, itemValue, null);
    }

    public int countArrayItems(String schemaNS, String arrayName) throws XMPException {
        ParameterAsserts.assertSchemaNS(schemaNS);
        ParameterAsserts.assertArrayName(arrayName);
        XMPNode arrayNode = XMPNodeUtils.findNode(this.tree, XMPPathParser.expandXPath(schemaNS, arrayName), false, null);
        if (arrayNode == null) {
            return 0;
        }
        if (arrayNode.getOptions().isArray()) {
            return arrayNode.getChildrenLength();
        }
        throw new XMPException("The named property is not an array", 102);
    }

    public void deleteArrayItem(String schemaNS, String arrayName, int itemIndex) {
        try {
            ParameterAsserts.assertSchemaNS(schemaNS);
            ParameterAsserts.assertArrayName(arrayName);
            deleteProperty(schemaNS, XMPPathFactory.composeArrayItemPath(arrayName, itemIndex));
        } catch (XMPException e) {
        }
    }

    public void deleteProperty(String schemaNS, String propName) {
        try {
            ParameterAsserts.assertSchemaNS(schemaNS);
            ParameterAsserts.assertPropName(propName);
            XMPNode propNode = XMPNodeUtils.findNode(this.tree, XMPPathParser.expandXPath(schemaNS, propName), false, null);
            if (propNode != null) {
                XMPNodeUtils.deleteNode(propNode);
            }
        } catch (XMPException e) {
        }
    }

    public void deleteQualifier(String schemaNS, String propName, String qualNS, String qualName) {
        try {
            ParameterAsserts.assertSchemaNS(schemaNS);
            ParameterAsserts.assertPropName(propName);
            deleteProperty(schemaNS, propName + XMPPathFactory.composeQualifierPath(qualNS, qualName));
        } catch (XMPException e) {
        }
    }

    public void deleteStructField(String schemaNS, String structName, String fieldNS, String fieldName) {
        try {
            ParameterAsserts.assertSchemaNS(schemaNS);
            ParameterAsserts.assertStructName(structName);
            deleteProperty(schemaNS, structName + XMPPathFactory.composeStructFieldPath(fieldNS, fieldName));
        } catch (XMPException e) {
        }
    }

    public boolean doesPropertyExist(String schemaNS, String propName) {
        try {
            ParameterAsserts.assertSchemaNS(schemaNS);
            ParameterAsserts.assertPropName(propName);
            if (XMPNodeUtils.findNode(this.tree, XMPPathParser.expandXPath(schemaNS, propName), false, null) != null) {
                return true;
            }
            return false;
        } catch (XMPException e) {
            return false;
        }
    }

    public boolean doesArrayItemExist(String schemaNS, String arrayName, int itemIndex) {
        try {
            ParameterAsserts.assertSchemaNS(schemaNS);
            ParameterAsserts.assertArrayName(arrayName);
            return doesPropertyExist(schemaNS, XMPPathFactory.composeArrayItemPath(arrayName, itemIndex));
        } catch (XMPException e) {
            return false;
        }
    }

    public boolean doesStructFieldExist(String schemaNS, String structName, String fieldNS, String fieldName) {
        try {
            ParameterAsserts.assertSchemaNS(schemaNS);
            ParameterAsserts.assertStructName(structName);
            return doesPropertyExist(schemaNS, structName + XMPPathFactory.composeStructFieldPath(fieldNS, fieldName));
        } catch (XMPException e) {
            return false;
        }
    }

    public boolean doesQualifierExist(String schemaNS, String propName, String qualNS, String qualName) {
        try {
            ParameterAsserts.assertSchemaNS(schemaNS);
            ParameterAsserts.assertPropName(propName);
            return doesPropertyExist(schemaNS, propName + XMPPathFactory.composeQualifierPath(qualNS, qualName));
        } catch (XMPException e) {
            return false;
        }
    }

    public XMPProperty getArrayItem(String schemaNS, String arrayName, int itemIndex) throws XMPException {
        ParameterAsserts.assertSchemaNS(schemaNS);
        ParameterAsserts.assertArrayName(arrayName);
        return getProperty(schemaNS, XMPPathFactory.composeArrayItemPath(arrayName, itemIndex));
    }

    public XMPProperty getLocalizedText(String schemaNS, String altTextName, String genericLang, String specificLang) throws XMPException {
        ParameterAsserts.assertSchemaNS(schemaNS);
        ParameterAsserts.assertArrayName(altTextName);
        ParameterAsserts.assertSpecificLang(specificLang);
        if (genericLang != null) {
            genericLang = Utils.normalizeLangValue(genericLang);
        } else {
            genericLang = null;
        }
        specificLang = Utils.normalizeLangValue(specificLang);
        XMPNode arrayNode = XMPNodeUtils.findNode(this.tree, XMPPathParser.expandXPath(schemaNS, altTextName), false, null);
        if (arrayNode == null) {
            return null;
        }
        Object[] result = XMPNodeUtils.chooseLocalizedText(arrayNode, genericLang, specificLang);
        final XMPNode itemNode = result[1];
        if (((Integer) result[0]).intValue() != 0) {
            return new XMPProperty() {
                public String getValue() {
                    return itemNode.getValue();
                }

                public PropertyOptions getOptions() {
                    return itemNode.getOptions();
                }

                public String getLanguage() {
                    return itemNode.getQualifier(1).getValue();
                }

                public String toString() {
                    return itemNode.getValue().toString();
                }
            };
        }
        return null;
    }

    public void setLocalizedText(String schemaNS, String altTextName, String genericLang, String specificLang, String itemValue, PropertyOptions options) throws XMPException {
        ParameterAsserts.assertSchemaNS(schemaNS);
        ParameterAsserts.assertArrayName(altTextName);
        ParameterAsserts.assertSpecificLang(specificLang);
        genericLang = genericLang != null ? Utils.normalizeLangValue(genericLang) : null;
        specificLang = Utils.normalizeLangValue(specificLang);
        XMPNode arrayNode = XMPNodeUtils.findNode(this.tree, XMPPathParser.expandXPath(schemaNS, altTextName), true, new PropertyOptions(7680));
        if (arrayNode == null) {
            throw new XMPException("Failed to find or create array node", 102);
        }
        if (!arrayNode.getOptions().isArrayAltText()) {
            if (arrayNode.hasChildren() || !arrayNode.getOptions().isArrayAlternate()) {
                throw new XMPException("Specified property is no alt-text array", 102);
            }
            arrayNode.getOptions().setArrayAltText(true);
        }
        boolean haveXDefault = false;
        XMPNode xdItem = null;
        Iterator it = arrayNode.iterateChildren();
        while (it.hasNext()) {
            XMPNode currItem = (XMPNode) it.next();
            if (currItem.hasQualifier() && XMPConst.XML_LANG.equals(currItem.getQualifier(1).getName())) {
                if ("x-default".equals(currItem.getQualifier(1).getValue())) {
                    xdItem = currItem;
                    haveXDefault = true;
                    break;
                }
            }
            throw new XMPException("Language qualifier must be first", 102);
        }
        if (xdItem != null && arrayNode.getChildrenLength() > 1) {
            arrayNode.removeChild(xdItem);
            arrayNode.addChild(1, xdItem);
        }
        Object[] result = XMPNodeUtils.chooseLocalizedText(arrayNode, genericLang, specificLang);
        int match = ((Integer) result[0]).intValue();
        XMPNode itemNode = result[1];
        boolean specificXDefault = "x-default".equals(specificLang);
        switch (match) {
            case 0:
                XMPNodeUtils.appendLangItem(arrayNode, "x-default", itemValue);
                haveXDefault = true;
                if (!specificXDefault) {
                    XMPNodeUtils.appendLangItem(arrayNode, specificLang, itemValue);
                    break;
                }
                break;
            case 1:
                if (!specificXDefault) {
                    if (haveXDefault && xdItem != itemNode && xdItem != null && xdItem.getValue().equals(itemNode.getValue())) {
                        xdItem.setValue(itemValue);
                    }
                    itemNode.setValue(itemValue);
                    break;
                } else if ($assertionsDisabled || (haveXDefault && xdItem == itemNode)) {
                    it = arrayNode.iterateChildren();
                    while (it.hasNext()) {
                        currItem = (XMPNode) it.next();
                        if (currItem != xdItem) {
                            if (currItem.getValue().equals(xdItem != null ? xdItem.getValue() : null)) {
                                currItem.setValue(itemValue);
                            }
                        }
                    }
                    if (xdItem != null) {
                        xdItem.setValue(itemValue);
                        break;
                    }
                } else {
                    throw new AssertionError();
                }
                break;
            case 2:
                if (haveXDefault && xdItem != itemNode && xdItem != null && xdItem.getValue().equals(itemNode.getValue())) {
                    xdItem.setValue(itemValue);
                }
                itemNode.setValue(itemValue);
                break;
            case 3:
                XMPNodeUtils.appendLangItem(arrayNode, specificLang, itemValue);
                if (specificXDefault) {
                    haveXDefault = true;
                    break;
                }
                break;
            case 4:
                if (xdItem != null && arrayNode.getChildrenLength() == 1) {
                    xdItem.setValue(itemValue);
                }
                XMPNodeUtils.appendLangItem(arrayNode, specificLang, itemValue);
                break;
            case 5:
                XMPNodeUtils.appendLangItem(arrayNode, specificLang, itemValue);
                if (specificXDefault) {
                    haveXDefault = true;
                    break;
                }
                break;
            default:
                throw new XMPException("Unexpected result from ChooseLocalizedText", 9);
        }
        if (!haveXDefault && arrayNode.getChildrenLength() == 1) {
            XMPNodeUtils.appendLangItem(arrayNode, "x-default", itemValue);
        }
    }

    public void setLocalizedText(String schemaNS, String altTextName, String genericLang, String specificLang, String itemValue) throws XMPException {
        setLocalizedText(schemaNS, altTextName, genericLang, specificLang, itemValue, null);
    }

    public XMPProperty getProperty(String schemaNS, String propName) throws XMPException {
        return getProperty(schemaNS, propName, 0);
    }

    protected XMPProperty getProperty(String schemaNS, String propName, int valueType) throws XMPException {
        ParameterAsserts.assertSchemaNS(schemaNS);
        ParameterAsserts.assertPropName(propName);
        final XMPNode propNode = XMPNodeUtils.findNode(this.tree, XMPPathParser.expandXPath(schemaNS, propName), false, null);
        if (propNode == null) {
            return null;
        }
        if (valueType == 0 || !propNode.getOptions().isCompositeProperty()) {
            final Object value = evaluateNodeValue(valueType, propNode);
            return new XMPProperty() {
                public String getValue() {
                    return value != null ? value.toString() : null;
                }

                public PropertyOptions getOptions() {
                    return propNode.getOptions();
                }

                public String getLanguage() {
                    return null;
                }

                public String toString() {
                    return value.toString();
                }
            };
        }
        throw new XMPException("Property must be simple when a value type is requested", 102);
    }

    protected Object getPropertyObject(String schemaNS, String propName, int valueType) throws XMPException {
        ParameterAsserts.assertSchemaNS(schemaNS);
        ParameterAsserts.assertPropName(propName);
        XMPNode propNode = XMPNodeUtils.findNode(this.tree, XMPPathParser.expandXPath(schemaNS, propName), false, null);
        if (propNode == null) {
            return null;
        }
        if (valueType == 0 || !propNode.getOptions().isCompositeProperty()) {
            return evaluateNodeValue(valueType, propNode);
        }
        throw new XMPException("Property must be simple when a value type is requested", 102);
    }

    public Boolean getPropertyBoolean(String schemaNS, String propName) throws XMPException {
        return (Boolean) getPropertyObject(schemaNS, propName, 1);
    }

    public void setPropertyBoolean(String schemaNS, String propName, boolean propValue, PropertyOptions options) throws XMPException {
        setProperty(schemaNS, propName, propValue ? XMPConst.TRUESTR : XMPConst.FALSESTR, options);
    }

    public void setPropertyBoolean(String schemaNS, String propName, boolean propValue) throws XMPException {
        setProperty(schemaNS, propName, propValue ? XMPConst.TRUESTR : XMPConst.FALSESTR, null);
    }

    public Integer getPropertyInteger(String schemaNS, String propName) throws XMPException {
        return (Integer) getPropertyObject(schemaNS, propName, 2);
    }

    public void setPropertyInteger(String schemaNS, String propName, int propValue, PropertyOptions options) throws XMPException {
        setProperty(schemaNS, propName, new Integer(propValue), options);
    }

    public void setPropertyInteger(String schemaNS, String propName, int propValue) throws XMPException {
        setProperty(schemaNS, propName, new Integer(propValue), null);
    }

    public Long getPropertyLong(String schemaNS, String propName) throws XMPException {
        return (Long) getPropertyObject(schemaNS, propName, 3);
    }

    public void setPropertyLong(String schemaNS, String propName, long propValue, PropertyOptions options) throws XMPException {
        setProperty(schemaNS, propName, new Long(propValue), options);
    }

    public void setPropertyLong(String schemaNS, String propName, long propValue) throws XMPException {
        setProperty(schemaNS, propName, new Long(propValue), null);
    }

    public Double getPropertyDouble(String schemaNS, String propName) throws XMPException {
        return (Double) getPropertyObject(schemaNS, propName, 4);
    }

    public void setPropertyDouble(String schemaNS, String propName, double propValue, PropertyOptions options) throws XMPException {
        setProperty(schemaNS, propName, new Double(propValue), options);
    }

    public void setPropertyDouble(String schemaNS, String propName, double propValue) throws XMPException {
        setProperty(schemaNS, propName, new Double(propValue), null);
    }

    public XMPDateTime getPropertyDate(String schemaNS, String propName) throws XMPException {
        return (XMPDateTime) getPropertyObject(schemaNS, propName, 5);
    }

    public void setPropertyDate(String schemaNS, String propName, XMPDateTime propValue, PropertyOptions options) throws XMPException {
        setProperty(schemaNS, propName, propValue, options);
    }

    public void setPropertyDate(String schemaNS, String propName, XMPDateTime propValue) throws XMPException {
        setProperty(schemaNS, propName, propValue, null);
    }

    public Calendar getPropertyCalendar(String schemaNS, String propName) throws XMPException {
        return (Calendar) getPropertyObject(schemaNS, propName, 6);
    }

    public void setPropertyCalendar(String schemaNS, String propName, Calendar propValue, PropertyOptions options) throws XMPException {
        setProperty(schemaNS, propName, propValue, options);
    }

    public void setPropertyCalendar(String schemaNS, String propName, Calendar propValue) throws XMPException {
        setProperty(schemaNS, propName, propValue, null);
    }

    public byte[] getPropertyBase64(String schemaNS, String propName) throws XMPException {
        return (byte[]) getPropertyObject(schemaNS, propName, 7);
    }

    public String getPropertyString(String schemaNS, String propName) throws XMPException {
        return (String) getPropertyObject(schemaNS, propName, 0);
    }

    public void setPropertyBase64(String schemaNS, String propName, byte[] propValue, PropertyOptions options) throws XMPException {
        setProperty(schemaNS, propName, propValue, options);
    }

    public void setPropertyBase64(String schemaNS, String propName, byte[] propValue) throws XMPException {
        setProperty(schemaNS, propName, propValue, null);
    }

    public XMPProperty getQualifier(String schemaNS, String propName, String qualNS, String qualName) throws XMPException {
        ParameterAsserts.assertSchemaNS(schemaNS);
        ParameterAsserts.assertPropName(propName);
        return getProperty(schemaNS, propName + XMPPathFactory.composeQualifierPath(qualNS, qualName));
    }

    public XMPProperty getStructField(String schemaNS, String structName, String fieldNS, String fieldName) throws XMPException {
        ParameterAsserts.assertSchemaNS(schemaNS);
        ParameterAsserts.assertStructName(structName);
        return getProperty(schemaNS, structName + XMPPathFactory.composeStructFieldPath(fieldNS, fieldName));
    }

    public XMPIterator iterator() throws XMPException {
        return iterator(null, null, null);
    }

    public XMPIterator iterator(IteratorOptions options) throws XMPException {
        return iterator(null, null, options);
    }

    public XMPIterator iterator(String schemaNS, String propName, IteratorOptions options) throws XMPException {
        return new XMPIteratorImpl(this, schemaNS, propName, options);
    }

    public void setArrayItem(String schemaNS, String arrayName, int itemIndex, String itemValue, PropertyOptions options) throws XMPException {
        ParameterAsserts.assertSchemaNS(schemaNS);
        ParameterAsserts.assertArrayName(arrayName);
        XMPNode arrayNode = XMPNodeUtils.findNode(this.tree, XMPPathParser.expandXPath(schemaNS, arrayName), false, null);
        if (arrayNode != null) {
            doSetArrayItem(arrayNode, itemIndex, itemValue, options, false);
            return;
        }
        throw new XMPException("Specified array does not exist", 102);
    }

    public void setArrayItem(String schemaNS, String arrayName, int itemIndex, String itemValue) throws XMPException {
        setArrayItem(schemaNS, arrayName, itemIndex, itemValue, null);
    }

    public void insertArrayItem(String schemaNS, String arrayName, int itemIndex, String itemValue, PropertyOptions options) throws XMPException {
        ParameterAsserts.assertSchemaNS(schemaNS);
        ParameterAsserts.assertArrayName(arrayName);
        XMPNode arrayNode = XMPNodeUtils.findNode(this.tree, XMPPathParser.expandXPath(schemaNS, arrayName), false, null);
        if (arrayNode != null) {
            doSetArrayItem(arrayNode, itemIndex, itemValue, options, true);
            return;
        }
        throw new XMPException("Specified array does not exist", 102);
    }

    public void insertArrayItem(String schemaNS, String arrayName, int itemIndex, String itemValue) throws XMPException {
        insertArrayItem(schemaNS, arrayName, itemIndex, itemValue, null);
    }

    public void setProperty(String schemaNS, String propName, Object propValue, PropertyOptions options) throws XMPException {
        ParameterAsserts.assertSchemaNS(schemaNS);
        ParameterAsserts.assertPropName(propName);
        options = XMPNodeUtils.verifySetOptions(options, propValue);
        XMPNode propNode = XMPNodeUtils.findNode(this.tree, XMPPathParser.expandXPath(schemaNS, propName), true, options);
        if (propNode != null) {
            setNode(propNode, propValue, options, false);
            return;
        }
        throw new XMPException("Specified property does not exist", 102);
    }

    public void setProperty(String schemaNS, String propName, Object propValue) throws XMPException {
        setProperty(schemaNS, propName, propValue, null);
    }

    public void setQualifier(String schemaNS, String propName, String qualNS, String qualName, String qualValue, PropertyOptions options) throws XMPException {
        ParameterAsserts.assertSchemaNS(schemaNS);
        ParameterAsserts.assertPropName(propName);
        if (doesPropertyExist(schemaNS, propName)) {
            setProperty(schemaNS, propName + XMPPathFactory.composeQualifierPath(qualNS, qualName), qualValue, options);
            return;
        }
        throw new XMPException("Specified property does not exist!", 102);
    }

    public void setQualifier(String schemaNS, String propName, String qualNS, String qualName, String qualValue) throws XMPException {
        setQualifier(schemaNS, propName, qualNS, qualName, qualValue, null);
    }

    public void setStructField(String schemaNS, String structName, String fieldNS, String fieldName, String fieldValue, PropertyOptions options) throws XMPException {
        ParameterAsserts.assertSchemaNS(schemaNS);
        ParameterAsserts.assertStructName(structName);
        setProperty(schemaNS, structName + XMPPathFactory.composeStructFieldPath(fieldNS, fieldName), fieldValue, options);
    }

    public void setStructField(String schemaNS, String structName, String fieldNS, String fieldName, String fieldValue) throws XMPException {
        setStructField(schemaNS, structName, fieldNS, fieldName, fieldValue, null);
    }

    public String getObjectName() {
        return this.tree.getName() != null ? this.tree.getName() : "";
    }

    public void setObjectName(String name) {
        this.tree.setName(name);
    }

    public String getPacketHeader() {
        return this.packetHeader;
    }

    public void setPacketHeader(String packetHeader) {
        this.packetHeader = packetHeader;
    }

    public Object clone() {
        return new XMPMetaImpl((XMPNode) this.tree.clone());
    }

    public String dumpObject() {
        return getRoot().dumpNode(true);
    }

    public void sort() {
        this.tree.sort();
    }

    public void normalize(ParseOptions options) throws XMPException {
        if (options == null) {
            options = new ParseOptions();
        }
        XMPNormalizer.process(this, options);
    }

    public XMPNode getRoot() {
        return this.tree;
    }

    private void doSetArrayItem(XMPNode arrayNode, int itemIndex, String itemValue, PropertyOptions itemOptions, boolean insert) throws XMPException {
        XMPNode itemNode = new XMPNode(XMPConst.ARRAY_ITEM_NAME, null);
        itemOptions = XMPNodeUtils.verifySetOptions(itemOptions, itemValue);
        int maxIndex = insert ? arrayNode.getChildrenLength() + 1 : arrayNode.getChildrenLength();
        if (itemIndex == -1) {
            itemIndex = maxIndex;
        }
        if (1 > itemIndex || itemIndex > maxIndex) {
            throw new XMPException("Array index out of bounds", 104);
        }
        if (!insert) {
            arrayNode.removeChild(itemIndex);
        }
        arrayNode.addChild(itemIndex, itemNode);
        setNode(itemNode, itemValue, itemOptions, false);
    }

    void setNode(XMPNode node, Object value, PropertyOptions newOptions, boolean deleteExisting) throws XMPException {
        if (deleteExisting) {
            node.clear();
        }
        node.getOptions().mergeWith(newOptions);
        if (!node.getOptions().isCompositeProperty()) {
            XMPNodeUtils.setNodeValue(node, value);
        } else if (value == null || value.toString().length() <= 0) {
            node.removeChildren();
        } else {
            throw new XMPException("Composite nodes can't have values", 102);
        }
    }

    private Object evaluateNodeValue(int valueType, XMPNode propNode) throws XMPException {
        String rawValue = propNode.getValue();
        switch (valueType) {
            case 1:
                return new Boolean(XMPUtils.convertToBoolean(rawValue));
            case 2:
                return new Integer(XMPUtils.convertToInteger(rawValue));
            case 3:
                return new Long(XMPUtils.convertToLong(rawValue));
            case 4:
                return new Double(XMPUtils.convertToDouble(rawValue));
            case 5:
                return XMPUtils.convertToDate(rawValue);
            case 6:
                return XMPUtils.convertToDate(rawValue).getCalendar();
            case 7:
                return XMPUtils.decodeBase64(rawValue);
            default:
                return (rawValue != null || propNode.getOptions().isCompositeProperty()) ? rawValue : "";
        }
    }
}

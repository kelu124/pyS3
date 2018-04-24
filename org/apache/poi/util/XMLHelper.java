package org.apache.poi.util;

import javax.xml.parsers.DocumentBuilderFactory;

public final class XMLHelper {
    private static POILogger logger = POILogFactory.getLogger(XMLHelper.class);

    public static DocumentBuilderFactory getDocumentBuilderFactory() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setExpandEntityReferences(false);
        trySetSAXFeature(factory, "http://javax.xml.XMLConstants/feature/secure-processing", true);
        trySetSAXFeature(factory, "http://xml.org/sax/features/external-general-entities", false);
        trySetSAXFeature(factory, "http://xml.org/sax/features/external-parameter-entities", false);
        trySetSAXFeature(factory, "http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        trySetSAXFeature(factory, "http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
        return factory;
    }

    private static void trySetSAXFeature(DocumentBuilderFactory documentBuilderFactory, String feature, boolean enabled) {
        try {
            documentBuilderFactory.setFeature(feature, enabled);
        } catch (Exception e) {
            logger.log(5, new Object[]{"SAX Feature unsupported", feature, e});
        } catch (AbstractMethodError ame) {
            logger.log(5, new Object[]{"Cannot set SAX feature because outdated XML parser in classpath", feature, ame});
        }
    }
}

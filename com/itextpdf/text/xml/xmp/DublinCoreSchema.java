package com.itextpdf.text.xml.xmp;

@Deprecated
public class DublinCoreSchema extends XmpSchema {
    public static final String CONTRIBUTOR = "dc:contributor";
    public static final String COVERAGE = "dc:coverage";
    public static final String CREATOR = "dc:creator";
    public static final String DATE = "dc:date";
    public static final String DEFAULT_XPATH_ID = "dc";
    public static final String DEFAULT_XPATH_URI = "http://purl.org/dc/elements/1.1/";
    public static final String DESCRIPTION = "dc:description";
    public static final String FORMAT = "dc:format";
    public static final String IDENTIFIER = "dc:identifier";
    public static final String LANGUAGE = "dc:language";
    public static final String PUBLISHER = "dc:publisher";
    public static final String RELATION = "dc:relation";
    public static final String RIGHTS = "dc:rights";
    public static final String SOURCE = "dc:source";
    public static final String SUBJECT = "dc:subject";
    public static final String TITLE = "dc:title";
    public static final String TYPE = "dc:type";
    private static final long serialVersionUID = -4551741356374797330L;

    public DublinCoreSchema() {
        super("xmlns:dc=\"http://purl.org/dc/elements/1.1/\"");
        setProperty(FORMAT, "application/pdf");
    }

    public void addTitle(String title) {
        XmpArray array = new XmpArray(XmpArray.ALTERNATIVE);
        array.add(title);
        setProperty(TITLE, array);
    }

    public void addTitle(LangAlt title) {
        setProperty(TITLE, title);
    }

    public void addDescription(String desc) {
        XmpArray array = new XmpArray(XmpArray.ALTERNATIVE);
        array.add(desc);
        setProperty(DESCRIPTION, array);
    }

    public void addDescription(LangAlt desc) {
        setProperty(DESCRIPTION, desc);
    }

    public void addSubject(String subject) {
        XmpArray array = new XmpArray(XmpArray.UNORDERED);
        array.add(subject);
        setProperty(SUBJECT, array);
    }

    public void addSubject(String[] subject) {
        XmpArray array = new XmpArray(XmpArray.UNORDERED);
        for (Object add : subject) {
            array.add(add);
        }
        setProperty(SUBJECT, array);
    }

    public void addAuthor(String author) {
        XmpArray array = new XmpArray(XmpArray.ORDERED);
        array.add(author);
        setProperty(CREATOR, array);
    }

    public void addAuthor(String[] author) {
        XmpArray array = new XmpArray(XmpArray.ORDERED);
        for (Object add : author) {
            array.add(add);
        }
        setProperty(CREATOR, array);
    }

    public void addPublisher(String publisher) {
        XmpArray array = new XmpArray(XmpArray.ORDERED);
        array.add(publisher);
        setProperty(PUBLISHER, array);
    }

    public void addPublisher(String[] publisher) {
        XmpArray array = new XmpArray(XmpArray.ORDERED);
        for (Object add : publisher) {
            array.add(add);
        }
        setProperty(PUBLISHER, array);
    }
}

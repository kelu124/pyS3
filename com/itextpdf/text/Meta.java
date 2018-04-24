package com.itextpdf.text;

import java.util.ArrayList;
import java.util.List;

public class Meta implements Element {
    public static final String AUTHOR = "author";
    public static final String CREATIONDATE = "creationdate";
    public static final String KEYWORDS = "keywords";
    public static final String PRODUCER = "producer";
    public static final String SUBJECT = "subject";
    public static final String TITLE = "title";
    public static final String UNKNOWN = "unknown";
    private final StringBuffer content;
    private final int type;

    Meta(int type, String content) {
        this.type = type;
        this.content = new StringBuffer(content);
    }

    public Meta(String tag, String content) {
        this.type = getType(tag);
        this.content = new StringBuffer(content);
    }

    public boolean process(ElementListener listener) {
        try {
            return listener.add(this);
        } catch (DocumentException e) {
            return false;
        }
    }

    public int type() {
        return this.type;
    }

    public List<Chunk> getChunks() {
        return new ArrayList();
    }

    public boolean isContent() {
        return false;
    }

    public boolean isNestable() {
        return false;
    }

    public StringBuffer append(String string) {
        return this.content.append(string);
    }

    public String getContent() {
        return this.content.toString();
    }

    public String getName() {
        switch (this.type) {
            case 1:
                return "title";
            case 2:
                return "subject";
            case 3:
                return KEYWORDS;
            case 4:
                return AUTHOR;
            case 5:
                return PRODUCER;
            case 6:
                return CREATIONDATE;
            default:
                return "unknown";
        }
    }

    public static int getType(String tag) {
        if ("subject".equals(tag)) {
            return 2;
        }
        if (KEYWORDS.equals(tag)) {
            return 3;
        }
        if (AUTHOR.equals(tag)) {
            return 4;
        }
        if ("title".equals(tag)) {
            return 1;
        }
        if (PRODUCER.equals(tag)) {
            return 5;
        }
        if (CREATIONDATE.equals(tag)) {
            return 6;
        }
        return 0;
    }
}

package com.itextpdf.text.html.simpleparser;

import java.util.Map;

final class ChainedProperties$TagAttributes {
    final Map<String, String> attrs;
    final String tag;

    ChainedProperties$TagAttributes(String tag, Map<String, String> attrs) {
        this.tag = tag;
        this.attrs = attrs;
    }
}

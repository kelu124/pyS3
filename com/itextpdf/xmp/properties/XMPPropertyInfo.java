package com.itextpdf.xmp.properties;

import com.itextpdf.xmp.options.PropertyOptions;

public interface XMPPropertyInfo extends XMPProperty {
    String getNamespace();

    PropertyOptions getOptions();

    String getPath();

    String getValue();
}

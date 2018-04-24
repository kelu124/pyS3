package com.itextpdf.xmp.properties;

import com.itextpdf.xmp.options.PropertyOptions;

public interface XMPProperty {
    String getLanguage();

    PropertyOptions getOptions();

    String getValue();
}

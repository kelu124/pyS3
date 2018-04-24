package com.itextpdf.xmp.properties;

import com.itextpdf.xmp.options.AliasOptions;

public interface XMPAliasInfo {
    AliasOptions getAliasForm();

    String getNamespace();

    String getPrefix();

    String getPropName();
}

package org.apache.poi.common.usermodel;

import org.apache.poi.util.Removal;

public interface Hyperlink {
    @Removal(version = "3.17")
    public static final int LINK_DOCUMENT = 2;
    @Removal(version = "3.17")
    public static final int LINK_EMAIL = 3;
    @Removal(version = "3.17")
    public static final int LINK_FILE = 4;
    @Removal(version = "3.17")
    public static final int LINK_URL = 1;

    String getAddress();

    String getLabel();

    int getType();

    HyperlinkType getTypeEnum();

    void setAddress(String str);

    void setLabel(String str);
}

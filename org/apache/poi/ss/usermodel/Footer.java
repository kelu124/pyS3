package org.apache.poi.ss.usermodel;

public interface Footer extends HeaderFooter {
    String getCenter();

    String getLeft();

    String getRight();

    void setCenter(String str);

    void setLeft(String str);

    void setRight(String str);
}

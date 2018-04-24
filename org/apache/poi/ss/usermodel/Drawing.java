package org.apache.poi.ss.usermodel;

public interface Drawing {
    ClientAnchor createAnchor(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8);

    Comment createCellComment(ClientAnchor clientAnchor);

    Chart createChart(ClientAnchor clientAnchor);

    Picture createPicture(ClientAnchor clientAnchor, int i);
}

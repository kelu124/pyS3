package org.apache.poi.sl.usermodel;

public interface GraphicalFrame<S extends Shape<S, P>, P extends TextParagraph<S, P, ?>> extends Shape<S, P>, PlaceableShape<S, P> {
    PictureShape<S, P> getFallbackPicture();
}

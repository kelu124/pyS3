package org.apache.poi.sl.usermodel;

import java.awt.Graphics2D;

public interface Sheet<S extends Shape<S, P>, P extends TextParagraph<S, P, ?>> extends ShapeContainer<S, P> {
    void draw(Graphics2D graphics2D);

    Background<S, P> getBackground();

    boolean getFollowMasterGraphics();

    MasterSheet<S, P> getMasterSheet();

    SlideShow<S, P> getSlideShow();
}

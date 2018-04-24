package org.apache.poi.sl.usermodel;

public interface Slide<S extends Shape<S, P>, P extends TextParagraph<S, P, ?>> extends Sheet<S, P> {
    boolean getFollowMasterBackground();

    boolean getFollowMasterColourScheme();

    boolean getFollowMasterObjects();

    Notes<S, P> getNotes();

    int getSlideNumber();

    String getTitle();

    void setFollowMasterBackground(boolean z);

    void setFollowMasterColourScheme(boolean z);

    void setFollowMasterObjects(boolean z);

    void setNotes(Notes<S, P> notes);
}

package org.apache.poi.sl.usermodel;

import java.awt.Dimension;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import org.apache.poi.sl.usermodel.PictureData.PictureType;

public interface SlideShow<S extends Shape<S, P>, P extends TextParagraph<S, P, ?>> extends Closeable {
    PictureData addPicture(File file, PictureType pictureType) throws IOException;

    PictureData addPicture(InputStream inputStream, PictureType pictureType) throws IOException;

    PictureData addPicture(byte[] bArr, PictureType pictureType) throws IOException;

    MasterSheet<S, P> createMasterSheet() throws IOException;

    Slide<S, P> createSlide() throws IOException;

    PictureData findPictureData(byte[] bArr);

    Dimension getPageSize();

    List<? extends PictureData> getPictureData();

    Resources getResources();

    List<? extends MasterSheet<S, P>> getSlideMasters();

    List<? extends Slide<S, P>> getSlides();

    void setPageSize(Dimension dimension);

    void write(OutputStream outputStream) throws IOException;
}

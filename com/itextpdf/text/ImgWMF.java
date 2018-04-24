package com.itextpdf.text;

import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.codec.wmf.InputMeta;
import com.itextpdf.text.pdf.codec.wmf.MetaDo;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class ImgWMF extends Image {
    ImgWMF(Image image) {
        super(image);
    }

    public ImgWMF(URL url) throws BadElementException, IOException {
        super(url);
        processParameters();
    }

    public ImgWMF(String filename) throws BadElementException, MalformedURLException, IOException {
        this(Utilities.toURL(filename));
    }

    public ImgWMF(byte[] img) throws BadElementException, IOException {
        super((URL) null);
        this.rawData = img;
        this.originalData = img;
        processParameters();
    }

    private void processParameters() throws BadElementException, IOException {
        Throwable th;
        this.type = 35;
        this.originalType = 6;
        InputStream is = null;
        try {
            String errorID;
            if (this.rawData == null) {
                is = this.url.openStream();
                errorID = this.url.toString();
            } else {
                InputStream is2 = new ByteArrayInputStream(this.rawData);
                try {
                    errorID = "Byte array";
                    is = is2;
                } catch (Throwable th2) {
                    th = th2;
                    is = is2;
                    if (is != null) {
                        is.close();
                    }
                    this.plainWidth = getWidth();
                    this.plainHeight = getHeight();
                    throw th;
                }
            }
            InputMeta in = new InputMeta(is);
            if (in.readInt() != -1698247209) {
                throw new BadElementException(MessageLocalization.getComposedMessage("1.is.not.a.valid.placeable.windows.metafile", errorID));
            }
            in.readWord();
            int left = in.readShort();
            int top = in.readShort();
            int right = in.readShort();
            int bottom = in.readShort();
            int inch = in.readWord();
            this.dpiX = 72;
            this.dpiY = 72;
            this.scaledHeight = (((float) (bottom - top)) / ((float) inch)) * 72.0f;
            setTop(this.scaledHeight);
            this.scaledWidth = (((float) (right - left)) / ((float) inch)) * 72.0f;
            setRight(this.scaledWidth);
            if (is != null) {
                is.close();
            }
            this.plainWidth = getWidth();
            this.plainHeight = getHeight();
        } catch (Throwable th3) {
            th = th3;
            if (is != null) {
                is.close();
            }
            this.plainWidth = getWidth();
            this.plainHeight = getHeight();
            throw th;
        }
    }

    public void readWMF(PdfTemplate template) throws IOException, DocumentException {
        setTemplateData(template);
        template.setWidth(getWidth());
        template.setHeight(getHeight());
        InputStream is = null;
        try {
            if (this.rawData == null) {
                is = this.url.openStream();
            } else {
                is = new ByteArrayInputStream(this.rawData);
            }
            new MetaDo(is, template).readAll();
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
}

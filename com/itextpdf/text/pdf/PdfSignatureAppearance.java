package com.itextpdf.text.pdf;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Version;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.html.HtmlUtilities;
import com.itextpdf.text.io.RASInputStream;
import com.itextpdf.text.io.RandomAccessSource;
import com.itextpdf.text.io.RandomAccessSourceFactory;
import com.itextpdf.text.pdf.AcroFields.Item;
import com.itextpdf.text.pdf.security.CertificateInfo;
import com.itextpdf.text.pdf.security.CertificateInfo.X500Name;
import com.itextpdf.text.pdf.security.SecurityConstants;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map.Entry;

public class PdfSignatureAppearance {
    public static final int CERTIFIED_FORM_FILLING = 2;
    public static final int CERTIFIED_FORM_FILLING_AND_ANNOTATIONS = 3;
    public static final int CERTIFIED_NO_CHANGES_ALLOWED = 1;
    private static final float MARGIN = 2.0f;
    public static final int NOT_CERTIFIED = 0;
    private static final float TOP_SECTION = 0.3f;
    public static final String questionMark = "% DSUnknown\nq\n1 G\n1 g\n0.1 0 0 0.1 9 0 cm\n0 J 0 j 4 M []0 d\n1 i \n0 g\n313 292 m\n313 404 325 453 432 529 c\n478 561 504 597 504 645 c\n504 736 440 760 391 760 c\n286 760 271 681 265 626 c\n265 625 l\n100 625 l\n100 828 253 898 381 898 c\n451 898 679 878 679 650 c\n679 555 628 499 538 435 c\n488 399 467 376 467 292 c\n313 292 l\nh\n308 214 170 -164 re\nf\n0.44 G\n1.2 w\n1 1 0.4 rg\n287 318 m\n287 430 299 479 406 555 c\n451 587 478 623 478 671 c\n478 762 414 786 365 786 c\n260 786 245 707 239 652 c\n239 651 l\n74 651 l\n74 854 227 924 355 924 c\n425 924 653 904 653 676 c\n653 581 602 525 512 461 c\n462 425 441 402 441 318 c\n287 318 l\nh\n282 240 170 -164 re\nB\nQ\n";
    private boolean acro6Layers = true;
    private PdfTemplate[] app = new PdfTemplate[5];
    private byte[] bout;
    private int boutLen;
    private int certificationLevel = 0;
    private String contact;
    private PdfDictionary cryptoDictionary;
    private HashMap<PdfName, PdfLiteral> exclusionLocations;
    private String fieldName;
    private PdfTemplate frm;
    private Image image;
    private float imageScale;
    private Font layer2Font;
    private String layer2Text;
    private String layer4Text;
    private String location;
    private String locationCaption = "Location: ";
    private OutputStream originalout;
    private int page = 1;
    private Rectangle pageRect;
    private boolean preClosed = false;
    private RandomAccessFile raf;
    private long[] range;
    private String reason;
    private String reasonCaption = "Reason: ";
    private Rectangle rect;
    private RenderingMode renderingMode = RenderingMode.DESCRIPTION;
    private boolean reuseAppearance = false;
    private int runDirection = 1;
    private Certificate signCertificate;
    private Calendar signDate;
    private String signatureCreator;
    private SignatureEvent signatureEvent;
    private Image signatureGraphic = null;
    private ByteBuffer sigout;
    private PdfStamper stamper;
    private File tempFile;
    private PdfStamperImp writer;

    public enum RenderingMode {
        DESCRIPTION,
        NAME_AND_DESCRIPTION,
        GRAPHIC_AND_DESCRIPTION,
        GRAPHIC
    }

    public interface SignatureEvent {
        void getSignatureDictionary(PdfDictionary pdfDictionary);
    }

    PdfSignatureAppearance(PdfStamperImp writer) {
        this.writer = writer;
        this.signDate = new GregorianCalendar();
        this.fieldName = getNewSigName();
        this.signatureCreator = Version.getInstance().getVersion();
    }

    public void setCertificationLevel(int certificationLevel) {
        this.certificationLevel = certificationLevel;
    }

    public int getCertificationLevel() {
        return this.certificationLevel;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setReasonCaption(String reasonCaption) {
        this.reasonCaption = reasonCaption;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setLocationCaption(String locationCaption) {
        this.locationCaption = locationCaption;
    }

    public String getSignatureCreator() {
        return this.signatureCreator;
    }

    public void setSignatureCreator(String signatureCreator) {
        this.signatureCreator = signatureCreator;
    }

    public String getContact() {
        return this.contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Calendar getSignDate() {
        return this.signDate;
    }

    public void setSignDate(Calendar signDate) {
        this.signDate = signDate;
    }

    public InputStream getRangeStream() throws IOException {
        return new RASInputStream(new RandomAccessSourceFactory().createRanged(getUnderlyingSource(), this.range));
    }

    private RandomAccessSource getUnderlyingSource() throws IOException {
        RandomAccessSourceFactory fac = new RandomAccessSourceFactory();
        return this.raf == null ? fac.createSource(this.bout) : fac.createSource(this.raf);
    }

    public void addDeveloperExtension(PdfDeveloperExtension de) {
        this.writer.addDeveloperExtension(de);
    }

    public PdfDictionary getCryptoDictionary() {
        return this.cryptoDictionary;
    }

    public void setCryptoDictionary(PdfDictionary cryptoDictionary) {
        this.cryptoDictionary = cryptoDictionary;
    }

    public void setCertificate(Certificate signCertificate) {
        this.signCertificate = signCertificate;
    }

    public Certificate getCertificate() {
        return this.signCertificate;
    }

    public SignatureEvent getSignatureEvent() {
        return this.signatureEvent;
    }

    public void setSignatureEvent(SignatureEvent signatureEvent) {
        this.signatureEvent = signatureEvent;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public String getNewSigName() {
        AcroFields af = this.writer.getAcroFields();
        String name = SecurityConstants.Signature;
        int step = 0;
        boolean found = false;
        while (!found) {
            step++;
            String n1 = name + step;
            if (af.getFieldItem(n1) == null) {
                n1 = n1 + ".";
                found = true;
                for (String fn : af.getFields().keySet()) {
                    if (fn.startsWith(n1)) {
                        found = false;
                        break;
                    }
                }
            }
        }
        return name + step;
    }

    public int getPage() {
        return this.page;
    }

    public Rectangle getRect() {
        return this.rect;
    }

    public Rectangle getPageRect() {
        return this.pageRect;
    }

    public boolean isInvisible() {
        return this.rect == null || this.rect.getWidth() == 0.0f || this.rect.getHeight() == 0.0f;
    }

    public void setVisibleSignature(Rectangle pageRect, int page, String fieldName) {
        if (fieldName != null) {
            if (fieldName.indexOf(46) >= 0) {
                throw new IllegalArgumentException(MessageLocalization.getComposedMessage("field.names.cannot.contain.a.dot", new Object[0]));
            } else if (this.writer.getAcroFields().getFieldItem(fieldName) != null) {
                throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.field.1.already.exists", fieldName));
            } else {
                this.fieldName = fieldName;
            }
        }
        if (page < 1 || page > this.writer.reader.getNumberOfPages()) {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("invalid.page.number.1", page));
        }
        this.pageRect = new Rectangle(pageRect);
        this.pageRect.normalize();
        this.rect = new Rectangle(this.pageRect.getWidth(), this.pageRect.getHeight());
        this.page = page;
    }

    public void setVisibleSignature(String fieldName) {
        Item item = this.writer.getAcroFields().getFieldItem(fieldName);
        if (item == null) {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.field.1.does.not.exist", fieldName));
        }
        PdfDictionary merged = item.getMerged(0);
        if (PdfName.SIG.equals(PdfReader.getPdfObject(merged.get(PdfName.FT)))) {
            this.fieldName = fieldName;
            PdfArray r = merged.getAsArray(PdfName.RECT);
            this.pageRect = new Rectangle(r.getAsNumber(0).floatValue(), r.getAsNumber(1).floatValue(), r.getAsNumber(2).floatValue(), r.getAsNumber(3).floatValue());
            this.pageRect.normalize();
            this.page = item.getPage(0).intValue();
            int rotation = this.writer.reader.getPageRotation(this.page);
            Rectangle pageSize = this.writer.reader.getPageSizeWithRotation(this.page);
            switch (rotation) {
                case 90:
                    this.pageRect = new Rectangle(this.pageRect.getBottom(), pageSize.getTop() - this.pageRect.getLeft(), this.pageRect.getTop(), pageSize.getTop() - this.pageRect.getRight());
                    break;
                case 180:
                    this.pageRect = new Rectangle(pageSize.getRight() - this.pageRect.getLeft(), pageSize.getTop() - this.pageRect.getBottom(), pageSize.getRight() - this.pageRect.getRight(), pageSize.getTop() - this.pageRect.getTop());
                    break;
                case 270:
                    this.pageRect = new Rectangle(pageSize.getRight() - this.pageRect.getBottom(), this.pageRect.getLeft(), pageSize.getRight() - this.pageRect.getTop(), this.pageRect.getRight());
                    break;
            }
            if (rotation != 0) {
                this.pageRect.normalize();
            }
            this.rect = new Rectangle(this.pageRect.getWidth(), this.pageRect.getHeight());
            return;
        }
        throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.field.1.is.not.a.signature.field", fieldName));
    }

    public RenderingMode getRenderingMode() {
        return this.renderingMode;
    }

    public void setRenderingMode(RenderingMode renderingMode) {
        this.renderingMode = renderingMode;
    }

    public Image getSignatureGraphic() {
        return this.signatureGraphic;
    }

    public void setSignatureGraphic(Image signatureGraphic) {
        this.signatureGraphic = signatureGraphic;
    }

    public boolean isAcro6Layers() {
        return this.acro6Layers;
    }

    public void setAcro6Layers(boolean acro6Layers) {
        this.acro6Layers = acro6Layers;
    }

    public PdfTemplate getLayer(int layer) {
        if (layer < 0 || layer >= this.app.length) {
            return null;
        }
        PdfTemplate t = this.app[layer];
        if (t != null) {
            return t;
        }
        PdfTemplate[] pdfTemplateArr = this.app;
        t = new PdfTemplate(this.writer);
        pdfTemplateArr[layer] = t;
        t.setBoundingBox(this.rect);
        this.writer.addDirectTemplateSimple(t, new PdfName("n" + layer));
        return t;
    }

    public void setReuseAppearance(boolean reuseAppearance) {
        this.reuseAppearance = reuseAppearance;
    }

    public Image getImage() {
        return this.image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public float getImageScale() {
        return this.imageScale;
    }

    public void setImageScale(float imageScale) {
        this.imageScale = imageScale;
    }

    public void setLayer2Text(String text) {
        this.layer2Text = text;
    }

    public String getLayer2Text() {
        return this.layer2Text;
    }

    public Font getLayer2Font() {
        return this.layer2Font;
    }

    public void setLayer2Font(Font layer2Font) {
        this.layer2Font = layer2Font;
    }

    public void setRunDirection(int runDirection) {
        if (runDirection < 0 || runDirection > 3) {
            throw new RuntimeException(MessageLocalization.getComposedMessage("invalid.run.direction.1", runDirection));
        }
        this.runDirection = runDirection;
    }

    public int getRunDirection() {
        return this.runDirection;
    }

    public void setLayer4Text(String text) {
        this.layer4Text = text;
    }

    public String getLayer4Text() {
        return this.layer4Text;
    }

    public PdfTemplate getTopLayer() {
        if (this.frm == null) {
            this.frm = new PdfTemplate(this.writer);
            this.frm.setBoundingBox(this.rect);
            this.writer.addDirectTemplateSimple(this.frm, new PdfName("FRM"));
        }
        return this.frm;
    }

    public PdfTemplate getAppearance() throws DocumentException {
        if (isInvisible()) {
            PdfTemplate t = new PdfTemplate(this.writer);
            t.setBoundingBox(new Rectangle(0.0f, 0.0f));
            this.writer.addDirectTemplateSimple(t, null);
            return t;
        }
        String text;
        Font font;
        Rectangle rectangle;
        float size;
        Rectangle rotated;
        if (this.app[0] == null && !this.reuseAppearance) {
            createBlankN0();
        }
        if (this.app[1] == null && !this.acro6Layers) {
            PdfTemplate[] pdfTemplateArr = this.app;
            t = new PdfTemplate(this.writer);
            pdfTemplateArr[1] = t;
            t.setBoundingBox(new Rectangle(100.0f, 100.0f));
            this.writer.addDirectTemplateSimple(t, new PdfName("n1"));
            t.setLiteral(questionMark);
        }
        if (this.app[2] == null) {
            if (this.layer2Text == null) {
                StringBuilder buf = new StringBuilder();
                buf.append("Digitally signed by ");
                String name = null;
                X500Name x500name = CertificateInfo.getSubjectFields((X509Certificate) this.signCertificate);
                if (x500name != null) {
                    name = x500name.getField("CN");
                    if (name == null) {
                        name = x500name.getField("E");
                    }
                }
                if (name == null) {
                    name = "";
                }
                buf.append(name).append('\n');
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss z");
                buf.append("Date: ").append(simpleDateFormat.format(this.signDate.getTime()));
                if (this.reason != null) {
                    buf.append('\n').append(this.reasonCaption).append(this.reason);
                }
                if (this.location != null) {
                    buf.append('\n').append(this.locationCaption).append(this.location);
                }
                text = buf.toString();
            } else {
                text = this.layer2Text;
            }
            pdfTemplateArr = this.app;
            t = new PdfTemplate(this.writer);
            pdfTemplateArr[2] = t;
            t.setBoundingBox(this.rect);
            this.writer.addDirectTemplateSimple(t, new PdfName("n2"));
            if (this.image != null) {
                if (this.imageScale == 0.0f) {
                    t.addImage(this.image, this.rect.getWidth(), 0.0f, 0.0f, this.rect.getHeight(), 0.0f, 0.0f);
                } else {
                    float usableScale = this.imageScale;
                    if (this.imageScale < 0.0f) {
                        usableScale = Math.min(this.rect.getWidth() / this.image.getWidth(), this.rect.getHeight() / this.image.getHeight());
                    }
                    float w = this.image.getWidth() * usableScale;
                    float h = this.image.getHeight() * usableScale;
                    t.addImage(this.image, w, 0.0f, 0.0f, h, (this.rect.getWidth() - w) / 2.0f, (this.rect.getHeight() - h) / 2.0f);
                }
            }
            if (this.layer2Font == null) {
                font = new Font();
            } else {
                Font font2 = new Font(this.layer2Font);
            }
            float size2 = font.getSize();
            Rectangle dataRect = null;
            Rectangle signatureRect = null;
            if (this.renderingMode == RenderingMode.NAME_AND_DESCRIPTION || (this.renderingMode == RenderingMode.GRAPHIC_AND_DESCRIPTION && this.signatureGraphic != null)) {
                rectangle = new Rectangle(2.0f, 2.0f, (this.rect.getWidth() / 2.0f) - 2.0f, this.rect.getHeight() - 2.0f);
                rectangle = new Rectangle((this.rect.getWidth() / 2.0f) + BaseField.BORDER_WIDTH_THIN, 2.0f, this.rect.getWidth() - BaseField.BORDER_WIDTH_THIN, this.rect.getHeight() - 2.0f);
                if (this.rect.getHeight() > this.rect.getWidth()) {
                    rectangle = new Rectangle(2.0f, this.rect.getHeight() / 2.0f, this.rect.getWidth() - 2.0f, this.rect.getHeight());
                    rectangle = new Rectangle(2.0f, 2.0f, this.rect.getWidth() - 2.0f, (this.rect.getHeight() / 2.0f) - 2.0f);
                }
            } else if (this.renderingMode != RenderingMode.GRAPHIC) {
                rectangle = new Rectangle(2.0f, 2.0f, this.rect.getWidth() - 2.0f, (this.rect.getHeight() * 0.7f) - 2.0f);
            } else if (this.signatureGraphic == null) {
                throw new IllegalStateException(MessageLocalization.getComposedMessage("a.signature.image.should.be.present.when.rendering.mode.is.graphic.only", new Object[0]));
            } else {
                rectangle = new Rectangle(2.0f, 2.0f, this.rect.getWidth() - 2.0f, this.rect.getHeight() - 2.0f);
            }
            ColumnText ct2;
            Image im;
            switch (this.renderingMode) {
                case NAME_AND_DESCRIPTION:
                    String signedBy = CertificateInfo.getSubjectFields((X509Certificate) this.signCertificate).getField("CN");
                    if (signedBy == null) {
                        signedBy = CertificateInfo.getSubjectFields((X509Certificate) this.signCertificate).getField("E");
                    }
                    if (signedBy == null) {
                        signedBy = "";
                    }
                    float signedSize = ColumnText.fitText(font, signedBy, new Rectangle(signatureRect.getWidth() - 2.0f, signatureRect.getHeight() - 2.0f), -1.0f, this.runDirection);
                    ct2 = new ColumnText(t);
                    ct2.setRunDirection(this.runDirection);
                    ct2.setSimpleColumn(new Phrase(signedBy, font), signatureRect.getLeft(), signatureRect.getBottom(), signatureRect.getRight(), signatureRect.getTop(), signedSize, 0);
                    ct2.go();
                    break;
                case GRAPHIC_AND_DESCRIPTION:
                    if (this.signatureGraphic != null) {
                        ct2 = new ColumnText(t);
                        ct2.setRunDirection(this.runDirection);
                        ct2.setSimpleColumn(signatureRect.getLeft(), signatureRect.getBottom(), signatureRect.getRight(), signatureRect.getTop(), 0.0f, 2);
                        im = Image.getInstance(this.signatureGraphic);
                        im.scaleToFit(signatureRect.getWidth(), signatureRect.getHeight());
                        Paragraph p = new Paragraph();
                        float y = ((-im.getScaledHeight()) + 15.0f) - ((signatureRect.getHeight() - im.getScaledHeight()) / 2.0f);
                        Image image = im;
                        p.add(new Chunk(image, ((signatureRect.getWidth() - im.getScaledWidth()) / 2.0f) + (0.0f + ((signatureRect.getWidth() - im.getScaledWidth()) / 2.0f)), y, false));
                        ct2.addElement(p);
                        ct2.go();
                        break;
                    }
                    throw new IllegalStateException(MessageLocalization.getComposedMessage("a.signature.image.should.be.present.when.rendering.mode.is.graphic.and.description", new Object[0]));
                case GRAPHIC:
                    ct2 = new ColumnText(t);
                    ct2.setRunDirection(this.runDirection);
                    ct2.setSimpleColumn(signatureRect.getLeft(), signatureRect.getBottom(), signatureRect.getRight(), signatureRect.getTop(), 0.0f, 2);
                    im = Image.getInstance(this.signatureGraphic);
                    im.scaleToFit(signatureRect.getWidth(), signatureRect.getHeight());
                    Paragraph paragraph = new Paragraph(signatureRect.getHeight());
                    paragraph.add(new Chunk(im, (signatureRect.getWidth() - im.getScaledWidth()) / 2.0f, (signatureRect.getHeight() - im.getScaledHeight()) / 2.0f, false));
                    ct2.addElement(paragraph);
                    ct2.go();
                    break;
            }
            if (this.renderingMode != RenderingMode.GRAPHIC) {
                if (size2 <= 0.0f) {
                    size = ColumnText.fitText(font, text, new Rectangle(dataRect.getWidth(), dataRect.getHeight()), HtmlUtilities.DEFAULT_FONT_SIZE, this.runDirection);
                } else {
                    size = size2;
                }
                ColumnText columnText = new ColumnText(t);
                columnText.setRunDirection(this.runDirection);
                columnText.setSimpleColumn(new Phrase(text, font), dataRect.getLeft(), dataRect.getBottom(), dataRect.getRight(), dataRect.getTop(), size, 0);
                columnText.go();
            }
        }
        if (this.app[3] == null && !this.acro6Layers) {
            pdfTemplateArr = this.app;
            t = new PdfTemplate(this.writer);
            pdfTemplateArr[3] = t;
            t.setBoundingBox(new Rectangle(100.0f, 100.0f));
            this.writer.addDirectTemplateSimple(t, new PdfName("n3"));
            t.setLiteral("% DSBlank\n");
        }
        if (this.app[4] == null && !this.acro6Layers) {
            pdfTemplateArr = this.app;
            t = new PdfTemplate(this.writer);
            pdfTemplateArr[4] = t;
            t.setBoundingBox(new Rectangle(0.0f, this.rect.getHeight() * 0.7f, this.rect.getRight(), this.rect.getTop()));
            this.writer.addDirectTemplateSimple(t, new PdfName("n4"));
            if (this.layer2Font == null) {
                font = new Font();
            } else {
                font2 = new Font(this.layer2Font);
            }
            text = "Signature Not Verified";
            if (this.layer4Text != null) {
                text = this.layer4Text;
            }
            size = ColumnText.fitText(font, text, new Rectangle(this.rect.getWidth() - 4.0f, (this.rect.getHeight() * TOP_SECTION) - 4.0f), 15.0f, this.runDirection);
            columnText = new ColumnText(t);
            columnText.setRunDirection(this.runDirection);
            columnText.setSimpleColumn(new Phrase(text, font), 2.0f, 0.0f, this.rect.getWidth() - 2.0f, this.rect.getHeight() - 2.0f, size, 0);
            columnText.go();
        }
        int rotation = this.writer.reader.getPageRotation(this.page);
        rectangle = new Rectangle(this.rect);
        for (int n = rotation; n > 0; n -= 90) {
            rotated = rotated.rotate();
        }
        if (this.frm == null) {
            this.frm = new PdfTemplate(this.writer);
            this.frm.setBoundingBox(rotated);
            this.writer.addDirectTemplateSimple(this.frm, new PdfName("FRM"));
            float scale = Math.min(this.rect.getWidth(), this.rect.getHeight()) * 0.9f;
            float x = (this.rect.getWidth() - scale) / 2.0f;
            y = (this.rect.getHeight() - scale) / 2.0f;
            scale /= 100.0f;
            if (rotation == 90) {
                this.frm.concatCTM(0.0f, BaseField.BORDER_WIDTH_THIN, -1.0f, 0.0f, this.rect.getHeight(), 0.0f);
            } else if (rotation == 180) {
                this.frm.concatCTM(-1.0f, 0.0f, 0.0f, -1.0f, this.rect.getWidth(), this.rect.getHeight());
            } else if (rotation == 270) {
                this.frm.concatCTM(0.0f, -1.0f, BaseField.BORDER_WIDTH_THIN, 0.0f, 0.0f, this.rect.getWidth());
            }
            if (this.reuseAppearance) {
                PdfIndirectReference ref = this.writer.getAcroFields().getNormalAppearance(getFieldName());
                if (ref != null) {
                    this.frm.addTemplateReference(ref, new PdfName("n0"), BaseField.BORDER_WIDTH_THIN, 0.0f, 0.0f, BaseField.BORDER_WIDTH_THIN, 0.0f, 0.0f);
                } else {
                    this.reuseAppearance = false;
                    if (this.app[0] == null) {
                        createBlankN0();
                    }
                }
            }
            if (!this.reuseAppearance) {
                this.frm.addTemplate(this.app[0], 0.0f, 0.0f);
            }
            if (!this.acro6Layers) {
                this.frm.addTemplate(this.app[1], scale, 0.0f, 0.0f, scale, x, y);
            }
            this.frm.addTemplate(this.app[2], 0.0f, 0.0f);
            if (!this.acro6Layers) {
                this.frm.addTemplate(this.app[3], scale, 0.0f, 0.0f, scale, x, y);
                this.frm.addTemplate(this.app[4], 0.0f, 0.0f);
            }
        }
        PdfTemplate pdfTemplate = new PdfTemplate(this.writer);
        pdfTemplate.setBoundingBox(rotated);
        this.writer.addDirectTemplateSimple(pdfTemplate, null);
        pdfTemplate.addTemplate(this.frm, 0.0f, 0.0f);
        return pdfTemplate;
    }

    private void createBlankN0() {
        PdfTemplate[] pdfTemplateArr = this.app;
        PdfTemplate t = new PdfTemplate(this.writer);
        pdfTemplateArr[0] = t;
        t.setBoundingBox(new Rectangle(100.0f, 100.0f));
        this.writer.addDirectTemplateSimple(t, new PdfName("n0"));
        t.setLiteral("% DSBlank\n");
    }

    public PdfStamper getStamper() {
        return this.stamper;
    }

    void setStamper(PdfStamper stamper) {
        this.stamper = stamper;
    }

    ByteBuffer getSigout() {
        return this.sigout;
    }

    void setSigout(ByteBuffer sigout) {
        this.sigout = sigout;
    }

    OutputStream getOriginalout() {
        return this.originalout;
    }

    void setOriginalout(OutputStream originalout) {
        this.originalout = originalout;
    }

    public File getTempFile() {
        return this.tempFile;
    }

    void setTempFile(File tempFile) {
        this.tempFile = tempFile;
    }

    public boolean isPreClosed() {
        return this.preClosed;
    }

    public void preClose(HashMap<PdfName, Integer> exclusionSizes) throws IOException, DocumentException {
        if (this.preClosed) {
            throw new DocumentException(MessageLocalization.getComposedMessage("document.already.pre.closed", new Object[0]));
        }
        this.stamper.mergeVerification();
        this.preClosed = true;
        AcroFields af = this.writer.getAcroFields();
        String name = getFieldName();
        boolean fieldExists = af.doesSignatureFieldExist(name);
        PdfObject refSig = this.writer.getPdfIndirectReference();
        this.writer.setSigFlags(3);
        PdfDictionary fieldLock = null;
        if (fieldExists) {
            PdfObject widget = af.getFieldItem(name).getWidget(0);
            this.writer.markUsed(widget);
            fieldLock = widget.getAsDict(PdfName.LOCK);
            widget.put(PdfName.f130P, this.writer.getPageReference(getPage()));
            widget.put(PdfName.f136V, refSig);
            PdfObject obj = PdfReader.getPdfObjectRelease(widget.get(PdfName.f122F));
            int flags = 0;
            if (obj != null && obj.isNumber()) {
                flags = ((PdfNumber) obj).intValue();
            }
            widget.put(PdfName.f122F, new PdfNumber(flags | 128));
            PdfDictionary ap = new PdfDictionary();
            ap.put(PdfName.f128N, getAppearance().getIndirectReference());
            widget.put(PdfName.AP, ap);
        } else {
            PdfAnnotation sigField = PdfFormField.createSignature(this.writer);
            sigField.setFieldName(name);
            sigField.put(PdfName.f136V, refSig);
            sigField.setFlags(132);
            int pagen = getPage();
            if (isInvisible()) {
                sigField.setWidget(new Rectangle(0.0f, 0.0f), null);
            } else {
                sigField.setWidget(getPageRect(), null);
            }
            sigField.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, getAppearance());
            sigField.setPage(pagen);
            this.writer.addAnnotation(sigField, pagen);
        }
        this.exclusionLocations = new HashMap();
        if (this.cryptoDictionary == null) {
            throw new DocumentException("No crypto dictionary defined.");
        }
        PdfObject pdfLiteral = new PdfLiteral(80);
        this.exclusionLocations.put(PdfName.BYTERANGE, pdfLiteral);
        this.cryptoDictionary.put(PdfName.BYTERANGE, pdfLiteral);
        for (Entry<PdfName, Integer> entry : exclusionSizes.entrySet()) {
            PdfName key = (PdfName) entry.getKey();
            pdfLiteral = new PdfLiteral(((Integer) entry.getValue()).intValue());
            this.exclusionLocations.put(key, pdfLiteral);
            this.cryptoDictionary.put(key, pdfLiteral);
        }
        if (this.certificationLevel > 0) {
            addDocMDP(this.cryptoDictionary);
        }
        if (fieldLock != null) {
            addFieldMDP(this.cryptoDictionary, fieldLock);
        }
        if (this.signatureEvent != null) {
            this.signatureEvent.getSignatureDictionary(this.cryptoDictionary);
        }
        this.writer.addToBody(this.cryptoDictionary, refSig, false);
        if (this.certificationLevel > 0) {
            PdfDictionary docmdp = new PdfDictionary();
            docmdp.put(new PdfName("DocMDP"), refSig);
            this.writer.reader.getCatalog().put(new PdfName("Perms"), docmdp);
        }
        this.writer.close(this.stamper.getMoreInfo());
        this.range = new long[(this.exclusionLocations.size() * 2)];
        long byteRangePosition = ((PdfLiteral) this.exclusionLocations.get(PdfName.BYTERANGE)).getPosition();
        this.exclusionLocations.remove(PdfName.BYTERANGE);
        int idx = 1;
        for (PdfLiteral lit : this.exclusionLocations.values()) {
            long n = lit.getPosition();
            int i = idx + 1;
            this.range[idx] = n;
            idx = i + 1;
            this.range[i] = ((long) lit.getPosLength()) + n;
        }
        Arrays.sort(this.range, 1, this.range.length - 1);
        for (int k = 3; k < this.range.length - 2; k += 2) {
            long[] jArr = this.range;
            jArr[k] = jArr[k] - this.range[k - 1];
        }
        ByteBuffer bf;
        if (this.tempFile == null) {
            this.bout = this.sigout.getBuffer();
            this.boutLen = this.sigout.size();
            this.range[this.range.length - 1] = ((long) this.boutLen) - this.range[this.range.length - 2];
            bf = new ByteBuffer();
            bf.append('[');
            for (long append : this.range) {
                bf.append(append).append(' ');
            }
            bf.append(']');
            System.arraycopy(bf.getBuffer(), 0, this.bout, (int) byteRangePosition, bf.size());
            return;
        }
        try {
            this.raf = new RandomAccessFile(this.tempFile, "rw");
            this.range[this.range.length - 1] = this.raf.length() - this.range[this.range.length - 2];
            bf = new ByteBuffer();
            bf.append('[');
            for (long append2 : this.range) {
                bf.append(append2).append(' ');
            }
            bf.append(']');
            this.raf.seek(byteRangePosition);
            this.raf.write(bf.getBuffer(), 0, bf.size());
        } catch (IOException e) {
            try {
                this.raf.close();
            } catch (Exception e2) {
            }
            try {
                this.tempFile.delete();
            } catch (Exception e3) {
            }
            throw e;
        }
    }

    private void addDocMDP(PdfDictionary crypto) {
        PdfDictionary reference = new PdfDictionary();
        PdfDictionary transformParams = new PdfDictionary();
        transformParams.put(PdfName.f130P, new PdfNumber(this.certificationLevel));
        transformParams.put(PdfName.f136V, new PdfName("1.2"));
        transformParams.put(PdfName.TYPE, PdfName.TRANSFORMPARAMS);
        reference.put(PdfName.TRANSFORMMETHOD, PdfName.DOCMDP);
        reference.put(PdfName.TYPE, PdfName.SIGREF);
        reference.put(PdfName.TRANSFORMPARAMS, transformParams);
        if (this.writer.getPdfVersion().getVersion() < PdfWriter.VERSION_1_6) {
            reference.put(new PdfName(SecurityConstants.DigestValue), new PdfString("aa"));
            PdfArray loc = new PdfArray();
            loc.add(new PdfNumber(0));
            loc.add(new PdfNumber(0));
            reference.put(new PdfName("DigestLocation"), loc);
            reference.put(new PdfName(SecurityConstants.DigestMethod), new PdfName("MD5"));
        }
        reference.put(PdfName.DATA, this.writer.reader.getTrailer().get(PdfName.ROOT));
        PdfArray types = new PdfArray();
        types.add(reference);
        crypto.put(PdfName.REFERENCE, types);
    }

    private void addFieldMDP(PdfDictionary crypto, PdfDictionary fieldLock) {
        PdfDictionary reference = new PdfDictionary();
        PdfDictionary transformParams = new PdfDictionary();
        transformParams.putAll(fieldLock);
        transformParams.put(PdfName.TYPE, PdfName.TRANSFORMPARAMS);
        transformParams.put(PdfName.f136V, new PdfName("1.2"));
        reference.put(PdfName.TRANSFORMMETHOD, PdfName.FIELDMDP);
        reference.put(PdfName.TYPE, PdfName.SIGREF);
        reference.put(PdfName.TRANSFORMPARAMS, transformParams);
        reference.put(new PdfName(SecurityConstants.DigestValue), new PdfString("aa"));
        PdfArray loc = new PdfArray();
        loc.add(new PdfNumber(0));
        loc.add(new PdfNumber(0));
        reference.put(new PdfName("DigestLocation"), loc);
        reference.put(new PdfName(SecurityConstants.DigestMethod), new PdfName("MD5"));
        reference.put(PdfName.DATA, this.writer.reader.getTrailer().get(PdfName.ROOT));
        PdfArray types = crypto.getAsArray(PdfName.REFERENCE);
        if (types == null) {
            types = new PdfArray();
        }
        types.add(reference);
        crypto.put(PdfName.REFERENCE, types);
    }

    public void close(PdfDictionary update) throws IOException, DocumentException {
        try {
            if (this.preClosed) {
                ByteBuffer bf = new ByteBuffer();
                for (PdfName key : update.getKeys()) {
                    PdfObject obj = update.get(key);
                    PdfLiteral lit = (PdfLiteral) this.exclusionLocations.get(key);
                    if (lit == null) {
                        throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.key.1.didn.t.reserve.space.in.preclose", key.toString()));
                    }
                    bf.reset();
                    obj.toPdf(null, bf);
                    if (bf.size() > lit.getPosLength()) {
                        throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.key.1.is.too.big.is.2.reserved.3", key.toString(), String.valueOf(bf.size()), String.valueOf(lit.getPosLength())));
                    } else if (this.tempFile == null) {
                        System.arraycopy(bf.getBuffer(), 0, this.bout, (int) lit.getPosition(), bf.size());
                    } else {
                        this.raf.seek(lit.getPosition());
                        this.raf.write(bf.getBuffer(), 0, bf.size());
                    }
                }
                if (update.size() != this.exclusionLocations.size()) {
                    throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.update.dictionary.has.less.keys.than.required", new Object[0]));
                }
                if (this.tempFile == null) {
                    this.originalout.write(this.bout, 0, this.boutLen);
                } else if (this.originalout != null) {
                    this.raf.seek(0);
                    long length = this.raf.length();
                    byte[] buf = new byte[8192];
                    while (length > 0) {
                        int r = this.raf.read(buf, 0, (int) Math.min((long) buf.length, length));
                        if (r < 0) {
                            throw new EOFException(MessageLocalization.getComposedMessage("unexpected.eof", new Object[0]));
                        }
                        this.originalout.write(buf, 0, r);
                        length -= (long) r;
                    }
                }
                this.writer.reader.close();
                if (this.tempFile != null) {
                    try {
                        this.raf.close();
                    } catch (Exception e) {
                    }
                    if (this.originalout != null) {
                        try {
                            this.tempFile.delete();
                        } catch (Exception e2) {
                        }
                    }
                }
                if (this.originalout != null) {
                    try {
                        this.originalout.close();
                        return;
                    } catch (Exception e3) {
                        return;
                    }
                }
                return;
            }
            throw new DocumentException(MessageLocalization.getComposedMessage("preclose.must.be.called.first", new Object[0]));
        } catch (Throwable th) {
            this.writer.reader.close();
            if (this.tempFile != null) {
                try {
                    this.raf.close();
                } catch (Exception e4) {
                }
                if (this.originalout != null) {
                    try {
                        this.tempFile.delete();
                    } catch (Exception e5) {
                    }
                }
            }
            if (this.originalout != null) {
                try {
                    this.originalout.close();
                } catch (Exception e6) {
                }
            }
        }
    }
}

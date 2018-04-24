package com.itextpdf.text.pdf.internal;

import com.itextpdf.text.DocWriter;
import com.itextpdf.text.pdf.OutputStreamCounter;
import com.itextpdf.text.pdf.PdfDeveloperExtension;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.interfaces.PdfVersion;
import java.io.IOException;

public class PdfVersionImp implements PdfVersion {
    public static final byte[][] HEADER = new byte[][]{DocWriter.getISOBytes("\n"), DocWriter.getISOBytes("%PDF-"), DocWriter.getISOBytes("\n%âãÏÓ\n")};
    protected boolean appendmode = false;
    protected PdfName catalog_version = null;
    protected PdfDictionary extensions = null;
    protected boolean headerWasWritten = false;
    protected char header_version = PdfWriter.VERSION_1_4;
    protected char version = PdfWriter.VERSION_1_4;

    public void setPdfVersion(char version) {
        this.version = version;
        if (this.headerWasWritten || this.appendmode) {
            setPdfVersion(getVersionAsName(version));
        } else {
            this.header_version = version;
        }
    }

    public void setAtLeastPdfVersion(char version) {
        if (version > this.header_version) {
            setPdfVersion(version);
        }
    }

    public void setPdfVersion(PdfName version) {
        if (this.catalog_version == null || this.catalog_version.compareTo(version) < 0) {
            this.catalog_version = version;
        }
    }

    public void setAppendmode(boolean appendmode) {
        this.appendmode = appendmode;
    }

    public void writeHeader(OutputStreamCounter os) throws IOException {
        if (this.appendmode) {
            os.write(HEADER[0]);
            return;
        }
        os.write(HEADER[1]);
        os.write(getVersionAsByteArray(this.header_version));
        os.write(HEADER[2]);
        this.headerWasWritten = true;
    }

    public PdfName getVersionAsName(char version) {
        switch (version) {
            case '2':
                return PdfWriter.PDF_VERSION_1_2;
            case '3':
                return PdfWriter.PDF_VERSION_1_3;
            case '4':
                return PdfWriter.PDF_VERSION_1_4;
            case '5':
                return PdfWriter.PDF_VERSION_1_5;
            case '6':
                return PdfWriter.PDF_VERSION_1_6;
            case '7':
                return PdfWriter.PDF_VERSION_1_7;
            default:
                return PdfWriter.PDF_VERSION_1_4;
        }
    }

    public byte[] getVersionAsByteArray(char version) {
        return DocWriter.getISOBytes(getVersionAsName(version).toString().substring(1));
    }

    public void addToCatalog(PdfDictionary catalog) {
        if (this.catalog_version != null) {
            catalog.put(PdfName.VERSION, this.catalog_version);
        }
        if (this.extensions != null) {
            catalog.put(PdfName.EXTENSIONS, this.extensions);
        }
    }

    public void addDeveloperExtension(PdfDeveloperExtension de) {
        if (this.extensions == null) {
            this.extensions = new PdfDictionary();
        } else {
            PdfDictionary extension = this.extensions.getAsDict(de.getPrefix());
            if (extension != null) {
                if (de.getBaseversion().compareTo(extension.getAsName(PdfName.BASEVERSION)) < 0) {
                    return;
                }
                if (de.getExtensionLevel() - extension.getAsNumber(PdfName.EXTENSIONLEVEL).intValue() <= 0) {
                    return;
                }
            }
        }
        this.extensions.put(de.getPrefix(), de.getDeveloperExtensions());
    }

    public char getVersion() {
        return this.version;
    }
}
